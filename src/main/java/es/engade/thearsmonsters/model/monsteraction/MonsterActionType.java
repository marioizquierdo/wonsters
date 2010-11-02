package es.engade.thearsmonsters.model.monsteraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomInWorksState;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;

/**
 * Acciones que puede realizar un monstruo Esta enumeración es a su vez: - Una
 * enumeración de los tipos de acciones que hay (MonsterActionType.values();) -
 * Un índice con los datos de cada acción
 * (MonsterActionType.GarbageHarvest.getAllowedRoomTypes();) - Una factoría para
 * crear MonsterActions (MonsterActionType.GargageHarvest.build(monster,
 * roomType);) - Una estrategia para los métodos isValid y execute, ya que
 * MonsterAction delega su implementación aquí.
 * 
 * Cuando se añada una nueva MonsterAction, prácticamente toda su información se
 * añade en esta enum.
 */
public enum MonsterActionType {

	// MonsterAction (allowedRoomTypes, allowedMonsterAges) { 
	// 		validate and execute hooks 
	// }

	/**
	 * Recolectar basura. 
	 * Un monstruo adulto consigue en cada turno tanta basura como su atributo Harvest, y la guarda en el almacén.
	 */
	GarbageHarvest("Adult", "Warehouse") {

		// Se comprueba si hay suficiente espacio en el almacen para añadir mas basura
		boolean validate(MonsterAction action) {
			int currentGarbage = action.getLair().getGarbage();
			int maxGarbage = action.getLair().getGarbageStorageCapacity();

			boolean valid = currentGarbage < maxGarbage;
			if (!valid) action.addScopedError("validateGarbageHarvest");
			return valid;
		}

		// Suma la basura en la guarida e incrementa la experiencia al monstruo en la habilidad de recoleccion de basura.
		void doExecute(MonsterAction action) {
			int current = this.targetValue(action);

			int increment = this.targetValueIncreasePerTurn(action);
			int max = action.getLair().getGarbageStorageCapacity();
			int incrementExp = action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel();

			// Sumar basura en la guarida
			if (current + increment >= max) {
				action.getLair().setGarbage(max);
				action.addScopedNotification("fullStorageCapacity");
			} else {
				action.getLair().setGarbage(current + increment);			
			}
			
			// Incrementar experiencia en habilidad recoleccion
			Attr harvestSkill = action.getMonster().getWorkSkill(AttrType.HarvesterSkill);
			if(harvestSkill.addExp(incrementExp)) { // mejorar habilidad "recoleccion"
				action.addScopedNotification("monsterAttrLevelUp", new Object[] {
						harvestSkill.getLevel() // {0}
				});
			}
		}
        
		// targetValue = Cantidad de basura que hay en la guarida.
        public Integer targetValue(MonsterAction action) {
	        return action.getLair().getGarbage();
        }

        // targetValueIncreasePerTurn, se suma tanta basura como nivel del Atributo compuesto "recolección" del monstruo
        public Integer targetValueIncreasePerTurn(MonsterAction action) {
	        return action.getMonster().getComposeAttr(AttrType.Harvest).getLevel();
        }

        // targetValueMax = Capacidad del almacen
        public Integer targetValueMax(MonsterAction action) {
        	return action.getLair().getGarbageStorageCapacity();
        }
        
        // Parametros para Monster.actions.type.GarbageHarvest.info
        // {0} Nivel recoleccion del monstruo
        // {1} Cantidad de basura que se recolecta por turno (es decir, targetValueIncreasePerTurn)
        protected Object[] infoMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueIncreasePerTurn(action), // {0}
    				targetValueIncreasePerTurn(action)  // {1}
    		};
    	}
        
        // Parametros para Monster.actions.type.GarbageHarvest.targetValue
        // {0} targetValue con marca para ser identificado por JavaScript
        // {1} capacidad maxima del almacen.
        protected Object[] targetValueMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueMessageParam(action),
    				this.targetValueMax(action)
    		};
    	}
        
        // Parametros para cada Monster.actions.type.GarbageHarvest.targetValue.perTurn.{i}
        // ..perTurn.0 => Basura que se suma por turno, que es el targetValueIncreasePerTurn
        // ..perTurn.1 => Experiencia obtenida la habilidad "recoleccion"
        protected Object[] effectsPerTurnParams(MonsterAction action) {
    		return new Object[]{
    				targetValueIncreasePerTurn(action), // ..perTurn.0
    				action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel() // ..perTurn.1
    		};
    	}
        
        // Argumentos adicionales para que desde JavaScript se pueda hacer bien el calculo de incremento de
        // basura con respecto al numero de turnos gastados, ya que hay que tener en cuenta que la habilidad de
        // recoleccion va aumentando segun la inteligencia del bicho.
        //   * HarvesterSkill_exp_increment => Incremento en la experiencia de la recoleccion cada turno (es la inteligencia) 
        //   * HarvesterSkill_current_exp => Experiencia actual en la habilidad de recoleccion
        protected Map<String, Object> additionalArgs(MonsterAction action) {
        	Map<String, Object> additionalArgs = new HashMap<String, Object>(2);
        	additionalArgs.put("HarvesterSkill_exp_increment", action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel());
        	additionalArgs.put("HarvesterSkill_current_exp", action.getMonster().getWorkSkill(AttrType.HarvesterSkill).getExp());
        	return additionalArgs;
        }
        
	},
	

	/**
	 * Trabajar en las obras de una sala.
	 * Un monstruo adulto avanza en cada turno el effortDone de las obras tanto como su habilidad compuesta de construcci�n.
	 */
	WorkInTheWorks("Adult", "all") {

		// Se comprueba si la sala está en obras.
		boolean validate(MonsterAction action) {
			boolean valid = action.getRoom().isInWorks();
			if(!valid) action.addScopedError("roomNotInWorks");
			return valid;
		}
		
		// Para trabajar en las obras, tiene que permitir realizar tarea en salas de nivel 0.
		boolean validateBasicRoomConditions(MonsterAction action) {
			return true; // Vale cualquier tipo de sala ("all") y ademas vale que sea de nivel 0.
		}

		// Avanza en el esfuerzo realizado (effortDone) de las obras de la sala.
		// Si se terminan las obras se aumenta de nivel de la sala.
		void doExecute(MonsterAction action) {
			Room room = action.getRoom();
			Monster monster = action.getMonster();
			RoomInWorksState roomWorks = (RoomInWorksState) room.getState();
			int monsterConstructionLevel = monster.getComposeAttr(AttrType.Construction).getLevel();
			int incrementExp = action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel();

			// Avanzar en las obras
			roomWorks.setEffortDone(room.getEffortDone() + monsterConstructionLevel);
			
			// Adquirir experiencia en la habilidad "construccion"
			Attr constructionSkill = monster.getWorkSkill(AttrType.ConstructorSkill);
			if(constructionSkill.addExp(incrementExp)) {
				action.addScopedNotification("constructionLevelUp", new Object[]{
						monster.getName(), // {0} nombre del monstruo
						constructionSkill.getLevel() // {1} nivel que tiene ahora el atributo
				});
			}
			
			// Subir la sala de nivel si se completan las obras
			if (room.getEffortDone() >= room.getEffortUpgrade()) {
				room.setLevel(room.getLevel() + 1);
				room.setState(new RoomNormalState());
				if(room.getLevel() == 1) {
					action.addScopedNotification("roomBuilded"); // construccion terminada (nivel 1)
				} else {
					action.addScopedNotification("roomLevelUp", new Object[]{ // actualizacion terminada (nivel > 1)
						room.getLevel() // {0}
					});
				}
			}
		}
        
		// targetValue = Trabajo realizado en las obras de la sala.
        public Integer targetValue(MonsterAction action) {
	        return action.getRoom().getEffortDone();
        }

        // targetValueIncreasePerTurn = Atributo compuesto "construcción" del monstruo
        public Integer targetValueIncreasePerTurn(MonsterAction action) {
	        return action.getMonster().getComposeAttr(AttrType.Construction).getLevel();
        }

        // targetValueMax = Esfuerzo necesario para realizar toda la obra.
        public Integer targetValueMax(MonsterAction action) {
        	return action.getRoom().getEffortUpgrade();
        }
        
        // Parametros para Monster.actions.type.WorkInTheWorks.info
        // {0} Nivel de la sala al que se va a mejorar.
        // {1} Attr Construccion del monstruo
        // {2} Cantidad de esfuerzo realizado por turno (es decir, targetValueIncreasePerTurn)
        // {3} Cantidad de esfuerzo que hace falta para subir de nivel la sala
        // {4} Cantidad de esfuerzo que lleva completado
        // {5} % de obra completada
        protected Object[] infoMessageParams(MonsterAction action) {
        	Room room = action.getRoom();
    		return new Object[]{
    				room.getLevel() + 1, // {0}
    				targetValueIncreasePerTurn(action),  // {1}
    				targetValueIncreasePerTurn(action),  // {2}
    				room.getEffortUpgrade(), // {3}
    				room.getEffortDone(), // {4}
    				room.getEffortDonePercentage() // {5}
    		};
    	}
        
        // Parametros para Monster.actions.type.WorkInTheWorks.targetValue
        // {0} targetValue con marca para ser identificado por JavaScript
        // {1} esfuerzo total requerido
        // {2} siguiente nivel de la sala
        protected Object[] targetValueMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueMessageParam(action), // {0}
    				this.targetValueMax(action), // {1}
    				action.getRoom().getLevel() + 1 // {2}
    		};
    	}
        
        // Parametros para cada Monster.actions.type.WorkInTheWorks.targetValue.perTurn.{i}
        // ..perTurn.0 => Esfuerzo realizado por turno, que es el targetValueIncreasePerTurn
        // ..perTurn.1 => Experiencia obtenida la habilidad "construcción"
        protected Object[] effectsPerTurnParams(MonsterAction action) {
    		return new Object[]{
    				targetValueIncreasePerTurn(action), // ..perTurn.0
    				action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel() // ..perTurn.1
    		};
    	}
        
        // Argumentos adicionales para que desde JavaScript se pueda hacer bien el calculo del esfuerzo realizado
        // con respecto al numero de turnos gastados, ya que hay que tener en cuenta que la habilidad de construccion
        // va aumentando segun la inteligencia del bicho.
        //   * ConstructorSkill_exp_increment => Incremento en la experiencia de la construccion cada turno (es la inteligencia) 
        //   * ConstructorSkill_current_exp => Experiencia actual en la habilidad de construccion
        protected Map<String, Object> additionalArgs(MonsterAction action) {
        	Map<String, Object> additionalArgs = new HashMap<String, Object>(2);
        	additionalArgs.put("ConstructorSkill_exp_increment", action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel());
        	additionalArgs.put("ConstructorSkill_current_exp", action.getMonster().getWorkSkill(AttrType.ConstructorSkill).getExp());
        	return additionalArgs;
        }
	},
	
	GymTraining("Adult", "Gym") {

		// Un monstruo adulto siempre puede entrenar
		boolean validate(MonsterAction action) {
			return true;
		}

		// Aumenta la experiencia del monstruo en el atributo fuerza, por cada 100 de experiencia aumenta un nivel
		void doExecute(MonsterAction action) {
			int increment = this.targetValueIncreasePerTurn(action);

			Attr strenght = action.getMonster().getSimpleAttr(AttrType.Strenght);
			if(strenght.addExp(increment)) {
				action.addScopedNotification("strenghtLevelUp", new Object[] {
					strenght.getLevel() // {0}
				});
			}
		}

		// targetValue = Experiencia actual del monstruo en el aumento de fuerza
        public Integer targetValue(MonsterAction action) {
	        return action.getMonster().getSimpleAttr(AttrType.Strenght).getExp();
        }
        
        // targetValueIncreasePerTurn = Cantidad de exp que se aumenta en cada turno, 
        // que son 10 experiencia por cada nivel del gimnasio
        public Integer targetValueIncreasePerTurn(MonsterAction action) {
	        return action.getRoom().getLevel() * 10;
        }
		
        // Parametros para Monster.actions.type.GymTraining.info
        // {0} Nivel del gimnasio de la guarida
        // {1} Experiencia que obtiene el monstruo al ejecutar un turno
        protected Object[] infoMessageParams(MonsterAction action) {
        	int exp_added_per_turn = targetValueIncreasePerTurn(action);
        	return new Object[]{
    				action.getRoom().getLevel(), // {0}
    				exp_added_per_turn // {1}
    		};
    	}
        
        // Parámetros para Monster.actions.type.GymTraining.targetValue
        // {0} Experiencia acumulada (targetValue). Es el targetValue con marca para ser identificado por JavaScript
        // {1} Siguiente nivel al que subir� la fuerza al completar el 100%
        protected Object[] targetValueMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueMessageParam(action), // {0}
    				action.getMonster().getSimpleAttr(AttrType.Strenght).getLevel() + 1 // {1}
    		};
    	}
        
        // Parametros para cada Monster.actions.type.GymTraining.targetValue.perTurn.{i}
        // ..perTurn.0 => Experiencia que se suma por turno, que es el targetValueIncreasePerTurn
        protected Object[] effectsPerTurnParams(MonsterAction action) {
    		return new Object[]{
    				targetValueIncreasePerTurn(action), // ..perTurn.0
    		};
    	}

		// No hay l�mite para entrenar la fuerza. Si quiere gastar todos los turnos poni�ndose hasta arriba de esteroirdes, es su problema.
		protected Integer targetValueMax(MonsterAction action) {
			return null; // null => sin l�mite
		}
        
	},
	
	
	PlayAndLearn("Child", "Nursery") {

		// Un monstruo cria siempre puede ir al colegio
		boolean validate(MonsterAction action) {
			return true;
		}

		// Aumenta la experiencia del monstruo en el atributo inteligencia, por cada 100 de experiencia sube un nivel
		void doExecute(MonsterAction action) {
			int increment = this.targetValueIncreasePerTurn(action);
			Attr intelligence = action.getMonster().getSimpleAttr(AttrType.Intelligence);
			if(intelligence.addExp(increment)) {
				action.addScopedNotification("intelligenceLevelUp", new Object[]{
						intelligence.getLevel() // {0}
				});
			}
		}

		// targetValue = Experiencia actual del monstruo en el aumento de inteligencia
        public Integer targetValue(MonsterAction action) {
	        return action.getMonster().getSimpleAttr(AttrType.Intelligence).getExp();
        }

        // targetValueIncreasePerTurn = Cantidad de exp que se aumenta en cada turno
        // que es 10 experiencia por cada nivel del colegio
        public Integer targetValueIncreasePerTurn(MonsterAction action) {
	        int level = action.getRoom().getLevel();
	        return level * 10;
        }
		
        // Parámetros para Monster.actions.type.NurseryTeaching.info
        // {0} Nivel del colegio
        // {1} Experiencia que obtiene el monstruo al ejecutar un turno
        protected Object[] infoMessageParams(MonsterAction action) {
        	int exp_added_per_turn = targetValueIncreasePerTurn(action);
        	return new Object[]{
    				action.getRoom().getLevel(), // {0}
    				exp_added_per_turn // {1}
    		};
    	}
        
        // Parámetros para Monster.actions.type.NurseryTeaching.targetValue
        // {0} Experiencia que queda para aumentar de nivel la inteligencia
        // {1} Nivel del atributo inteligencia del monstruo
        protected Object[] targetValueMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueMessageParam(action), // {0}
    				action.getMonster().getSimpleAttr(AttrType.Intelligence).getLevel() + 1 // {1}
    		};
    	}
        
        // Parámetros para cada Monster.actions.type.NurseryTeaching.targetValue.perTurn.{i}
        // ..perTurn.0 => Experiencia que se suma por turno, que es el targetValueIncreasePerTurn
        protected Object[] effectsPerTurnParams(MonsterAction action) {
    		return new Object[]{
    				targetValueIncreasePerTurn(action), // ..perTurn.0
    		};
    	}
		
        // No hay limite para mejorar la inteligencia. Un monstruo cria puede gastar todos los turnos en el colegio si asi lo desea.
		protected Integer targetValueMax(MonsterAction action) {
			return null;
		}
        
	};
	
	

	// **** Attributes, Constructor and common Getters ****//

	private final List<MonsterAge> allowedMonsterAges;
	private final List<RoomType> allowedRoomTypes;

	MonsterActionType(String allowedMonsterAgesStringList, String allowedRoomTypesStringList) {
		allowedMonsterAges = MonsterAge.list(allowedMonsterAgesStringList);
		allowedRoomTypes = RoomType.list(allowedRoomTypesStringList);
	}

	/**
	 * Crea una nueva MonsterAction de este tipo.
	 */
	public MonsterAction build(Monster monster, Room room) { return new MonsterAction(this, monster, room); }

	/**
	 * Crea una nueva MonsterAction de este tipo que solo se puede ejecutar en
	 * la misma guarida del monstruo.
	 */
	public MonsterAction build(Monster monster, RoomType roomType) { return new MonsterAction(this, monster, roomType); }

	public List<MonsterAge> getAllowedMonsterAges() { return allowedMonsterAges; }
	public List<RoomType> getAllowedRoomTypes() { return allowedRoomTypes; }
	/**
	 * Devuelve la parte de una clave que por defecto apunta a los mensajes para este actionType,
	 * que es algo como "Monster.actions.type.{ActionType}.", al que se le puede concatenar el resto del scope.
	 */
	public String getMessagesKeyScope() { return "Monster.actions.type." + this + "."; }
	
	
	// **** getSuggestion (info para la vista) **** //
	
	/**
	 * Obtiene una "action suggestion", que contiene toda la información necesaria
	 * para ofrecer una sugerencia de realización de tarea al jugador.
	 * Aunque este método se puede sobreescibir en cada MonsterActionType, se ha dividido
	 * cada valor del objeto MonsterActionSuggestion en un método plantilla único, así
	 * se pueden sobreescribir solo los datos necesarios, simplificando el código.
	 */
	public MonsterActionSuggestion getSuggestion(MonsterAction action) {
		return new MonsterActionSuggestion(
				this, action.getMonster().getId(), action.getRoomType(), 
				maxTurnsToAssign(action),
				infoMessageKey(action), 
				infoMessageParams(action), 
				targetValue(action),
				targetValueIncreasePerTurn(action), 
				targetValueMessageKey(action),
				targetValueMessageParams(action),
			    effectsPerTurnMessageKey(action),
			    effectsPerTurnParams(action),
			    additionalArgs(action)
		);
	}
	
	/**
	 * Clave del mensaje de info de la tarea. Por defecto es "Monster.actions.type.{actionType}.info"
	 */
	protected String infoMessageKey(MonsterAction action) {
		return "Monster.actions.type."+ this +".info";
	}
	
	/**
	 * Parámetros numéricos en el mensaje de info de la tarea. 
	 * Tienen que ir en el mismo orden de aparición {0}, {1}, {2}...
	 */
	protected Object[] infoMessageParams(MonsterAction action) {
		return new Object[]{};
	}
	
	/**
	 * Valor actual del objetivo de la tarea. 
	 * Cada una tiene el suyo propio, por ejemplo para el Gimnasio es el valor de la fuerza actual del monstruo.
	 */
	protected abstract Integer targetValue(MonsterAction action);
	
	/**
	 * Cantidad que se incrementa en el valor actual del objetivo de la tarea. 
	 * Por ejemplo para el Gimnasio es la cantidad de fuerza que se mejora por cada turno invertido.
	 */
	protected abstract Integer targetValueIncreasePerTurn(MonsterAction action);
	
	/**
	 * Valor máximo que puede alcanzar el "targetValue". (null = sin máximo)
	 * Por ejemplo, para la recolección de basura es la capacidad del almacén.
	 */
	protected abstract Integer targetValueMax(MonsterAction action);
	
	/**
	 * Clave del mensaje de información sobre el objetivo de la tarea que se muestra debajo del título de la tarea en la vista.
	 * Por defecto es "Monster.actions.type.{actionType}.targetValue"
	 */
	protected String targetValueMessageKey(MonsterAction action) {
		return "Monster.actions.type."+ this +".targetValue";
	}
	
	/**
	 * Parámetros en el mensaje de info sobre el objetivo de la tarea. Tienen que ir en el mismo orden de aparición {0}, {1}, {2}...
	 * El parámetro que representa al "target value" debe ir envuelto con el método targetValueMessageParam(action) para que la vista
	 * sepa cual es y pueda modificar su valor dinámicamente (javascript).
	 * Por defecto se devuelve un único parámetro que es el "target value".
	 */ 
	protected Object[] targetValueMessageParams(MonsterAction action) {
		return new Object[]{targetValueMessageParam(action)};
	}
	
	/**
	 * Muestra el targetValue, pero con una marca HTML (span) para poder ser identificado desde JavaScript,
	 * ya que este valor en la vista se va incrementando según se vayan asignando turnos a esta tarea.
	 */
	protected String targetValueMessageParam(MonsterAction action) {
		return "<span class=\"targetValue\">"+ targetValue(action) +"</span>";
	}
	
	/**
	 * Maxima cantidad de turnos que se pueden asignar a esta tarea (null = indefinido, sin limite).
	 * Previene a la vista para que no permita asignar valores incorrectos.
	 */
	protected Integer maxTurnsToAssign(MonsterAction action) {
    	Integer current = targetValue(action);
    	Integer increment = targetValueIncreasePerTurn(action);
    	Integer max = targetValueMax(action);
    	
    	if(current != null && increment != null && max != null) {
    		return ((max + increment - 1) - current) / increment;
    	} else {
    		return null;
    	}
	}
	
	/**
	 * Parte de la clave de los mensajes que forman la lista de atributos que se mejoran en cada turno. 
     * Por defecto es "Monster.actions.type.{actionType}.perTurn
	 */
	protected String effectsPerTurnMessageKey(MonsterAction action) {
		return "Monster.actions.type."+ this +".perTurn";
	}
	
	/**
	 * Parámetros para construir la lista de effectos causados en cada turno en la vista.
	 * En base a effectsPerTurnMessageKey, se pasa cada parámetro a la clave
	 * {effectsPerTurnMessageKey}.{effectsPerTurnParams[i]}, siendo i el índice de 0 a effectsPerTurnParams.size - 1
	 */
	protected Object[] effectsPerTurnParams(MonsterAction action) {
		return new Object[]{}; // Por defecto no se pone nada, porque cada tarea lo hace distinto.
	}
	
    /**
     *  Argumentos adicionales que necesita informar a la vista este tipo de tarea. 
     *  Cuando se hace el c�lculo para el valor que toma el targetValue segun se utilicen mas turnos
     *  desde JavaScript, a veces es necesaria mas informacion. Por ejemplo al recolectar basura, dependiendo de la
     *  inteligencia del monstruo, la habilidad de recoleccion puede mejorar de nivel y sumar mas basura a partir de cierta
     *  cantidad de turnos utilizados. Lo mismo con las obras (y la habilidad de construccion que va aumentando). 
     */
    protected Map<String, Object> additionalArgs(MonsterAction action) {
    	return new HashMap<String, Object>(0); // Por defecto devuelve un hash vacio, sin argumentos adicionales.
    }
	
	
	

	// **** VALIDATION **** //

	/**
	 * Teniendo en cuenta los datos incluidos (monster y room), valida si esta
	 * tarea se puede ejecutar o no. Atención: La ejecución de este método no
	 * debe modificar el estado de la guarida ni del monstruo ni de la room ni
	 * de nada, (por lo tanto validate() tampoco puede), el único que modifica
	 * el estado es execute.
	 */
	public boolean isValid(MonsterAction action) {
		return validateBasicMonsterConditions(action) && 
				validateBasicRoomConditions(action) && 
				validate(action); // hook para implementar en cada MonsterAction particular
	}

	/**
	 * Validación básica de que el monstruo tiene la edad permitida.
	 */
	boolean validateBasicMonsterConditions(MonsterAction action) {
		Monster monster = action.getMonster();
		boolean valid = this.allowedMonsterAges.contains(monster.getAge());
		if(!valid) action.addError("Monster.actions.common.error.monsterAgeNotAllowed"); 
		return valid;
	}

	/**
	 * Validación básica de que la sala es una de las salas permitidas.
	 */
	boolean validateBasicRoomConditions(MonsterAction action) {
		boolean roomTypeAllowed = this.allowedRoomTypes.contains(action.getRoomType());
		if(!roomTypeAllowed) action.addError("Monster.actions.common.error.roomTypeNotAllowed");
		boolean roomLevel_gt_zero = action.getRoom().getLevel() > 0;
		if(!roomLevel_gt_zero) action.addError("Monster.actions.common.error.roomLevel_gt_zero");
		return roomTypeAllowed && roomLevel_gt_zero;
	}

	/**
	 * Condiciones extra que tiene cada MonsterAction en particular.
	 */
	boolean validate(MonsterAction action) {
		return true; // comportamiento por defecto, que se reescribe en cada instancia de MonsterActionType.
	}

	
	// **** EXECUTION ****//

	/**
	 * Ejecuta la accion del monstruo y modifica el estado del monstruo, de la
	 * sala y de la guarida de esa sala (posiblemente también el de los demás
	 * monstruos que tengan actividad en esa sala en ese mismo momento. Tener en
	 * cuenta que execute hace un isValid previamente.
	 * 
	 * @return true si se ha podido guardar. False si la tarea no es válida.
	 */
	public boolean execute(MonsterAction action) {
		Monster monster = action.getMonster();
		if (isValid(action) && monster.isFreeTurnsAvailable()) { // tambien se comprueba que el  monstruo tiene al menos un turno libre.
			monster.useFreeTurn();
			doExecute(action);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Ejecucion de la accion de cada MonsterAction en particular. La accion ya
	 * es valida, asi� que en principio no hace falta hacer comprobaciones. Solo
	 * hace falta implementar las modificaciones que se producen en el monstruo,
	 * en la sala y en la guarida, excepto lo de restar un turno al monstruo,
	 * que ya se hace previamente.
	 */
	abstract void doExecute(MonsterAction action);

}