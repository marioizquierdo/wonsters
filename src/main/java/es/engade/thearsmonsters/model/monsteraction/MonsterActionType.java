package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
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

		// Se comprueba si hay suficiente espacio en el almacén para añadir más basura
		boolean validate(MonsterAction action) {
			int currentGarbage = action.getLair().getGarbage();
			int maxGarbage = action.getLair().getGarbageStorageCapacity();

			boolean valid = currentGarbage < maxGarbage;
			if (!valid) action.addError("validateGarbageHarvest");
			return valid;
		}

		// Añade la basura en la guarida y añade experiencia al monstruo en la
		// habilidad de recolección de basura.
		void doExecute(MonsterAction action) {
			int current = this.targetValue(action);
			int increment = this.targetValueIncreasePerTurn(action);
			int max = this.targetValueMax(action);

			if (current + increment > max) {
				action.getLair().setGarbage(max);
			} else {
				action.getLair().setGarbage(current + increment);
			}
			action.getMonster().addExp(AttrType.HarvesterSkill, 20);
		}

		// targetValue = Cantidad de basura que hay en la guarida.
        public Integer targetValue(MonsterAction action) {
	        return action.getLair().getGarbage();
        }

        // targetValueIncreasePerTurn = Atributo compuesto "recolección" del monstruo
        public Integer targetValueIncreasePerTurn(MonsterAction action) {
	        return action.getMonster().getComposeAttr(AttrType.Harvest).getLevel();
        }

        // targetValueMax = Capacidad del almacén
        public Integer targetValueMax(MonsterAction action) {
        	return action.getLair().getGarbageStorageCapacity();
        }
        
        // Parámetros para Monster.actions.type.GarbageHarvest.info
        // {0} Nivel recolección del monstruo
        // {1} Cantidad de basura que se recolecta por turno (es decir, targetValueIncreasePerTurn)
        protected Object[] infoMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueIncreasePerTurn(action), // {0}
    				targetValueIncreasePerTurn(action)  // {1}
    		};
    	}
        
        // Parámetros para Monster.actions.type.GarbageHarvest.targetValue
        // {0} targetValue con marca para ser identificado por JavaScript
        // {1} capacidad máxima del almacén.
        protected Object[] targetValueMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueMessageParam(action),
    				action.getLair().getGarbageStorageCapacity()
    		};
    	}
        
	},

	/**
	 * Trabajar en las obras de una sala.
	 * Un monstruo adulto avanza en el effortDone de las obras tanto como su atributo Build en cada turno.
	 */
	WorkInTheWorks("Adult", "all") {

		// Se comprueba si la sala está en obras.
		boolean validate(MonsterAction action) {
			boolean valid = action.getRoom().isInWorks();
			if(!valid) action.addError("validateWorkInTheWorks");
			return valid;
		}

		// Avanza en el esfuerzo realizado (effortDone) de las obras de la sala.
		// Si se terminan las obras se aumenta de nivel.
		void doExecute(MonsterAction action) {
			Room room = action.getRoom();
			Monster monster = action.getMonster();
			RoomInWorksState roomWorks = (RoomInWorksState) room.getState();
			int monsterConstructionAttr = monster.getAttr(AttrType.Construction).getLevel();
			roomWorks.setEffortDone(room.getEffortDone() + monsterConstructionAttr);

			int totalEffort = room.isInInitialState() ? room.getEffortBuild() : room.getEffortUpgrade();
			if (room.getEffortDone() >= totalEffort) {
				room.setLevel(room.getLevel() + 1);
				room.setState(new RoomNormalState());
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
        
        // Parámetros para Monster.actions.type.WorkInTheWorks.info
        // {0} Nivel de la sala al que se va a mejorar.
        // {1} Attr Construcción del monstruo
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
        
        // Parámetros para Monster.actions.type.WorkInTheWorks.targetValue
        // {0} targetValue con marca para ser identificado por JavaScript
        // {1} esfuerzo total requerido
        // {2} siguiente nivel de la sala
        protected Object[] targetValueMessageParams(MonsterAction action) {
    		return new Object[]{
    				targetValueMessageParam(action), // {0}
    				action.getRoom().getEffortUpgrade(), // {1}
    				action.getRoom().getLevel() + 1 // {2}
    		};
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
	public MonsterAction build(Monster monster, Room room) {
		return new MonsterAction(this, monster, room);
	}

	/**
	 * Crea una nueva MonsterAction de este tipo que solo se puede ejecutar en
	 * la misma guarida del monstruo.
	 */
	public MonsterAction build(Monster monster, RoomType roomType) {
		return new MonsterAction(this, monster, roomType);
	}

	public List<MonsterAge> getAllowedMonsterAges() {
		return allowedMonsterAges;
	}

	public List<RoomType> getAllowedRoomTypes() {
		return allowedRoomTypes;
	}
	
	
	// **** getSuggestion (info para la vista) **** //
	
	/**
	 * Obtiene una "action suggestion", que contiene toda la información necesaria
	 * para ofrecer una sugerencia de realización de tarea al jugador.
	 * Aunque este método se puede sobreescibir en cada MonsterActionType, se ha dividido
	 * cada valor del objeto MonsterActionSuggestion en un método plantilla único, así
	 * se pueden sobreescribir solo los datos necesarios, simplificando el resultado.
	 * Todos los métodos plantilla reciben el monstruo, sala y guarida como parámetro por si lo necesitan.
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
				targetValueMessageParams(action)
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
	 * Máxima cantidad de turnos que se pueden asignar a esta tarea (null = indefinido, sin límite).
	 * Previene a la vista para que no permita asignar valores incorrectos.
	 */
	protected Integer maxTurnsToAssign(MonsterAction action) {
    	Integer current = targetValue(action);
    	Integer increment = targetValueIncreasePerTurn(action);
    	Integer max = targetValueMax(action);
    	if(current != null && increment != null && max != null) {
    		return (max - current) / increment;
    	} else {
    		return null;
    	}
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
	 * Validación básica de que la sala es una de las salas permitidas.
	 */
	boolean validateBasicRoomConditions(MonsterAction action) {
		boolean valid = this.allowedRoomTypes.contains(action.getRoomType());
		if(!valid) action.addError("validateBasicRoomConditions");
		return valid;
	}

	/**
	 * Validación básica de que el monstruo tiene la edad permitida y que tiene
	 * al menos un turno libre para ejecutar la acción.
	 */
	boolean validateBasicMonsterConditions(MonsterAction action) {
		Monster monster = action.getMonster();
		boolean valid = this.allowedMonsterAges.contains(monster.getAge())
				&& monster.isFreeTurnsAvailable();
		if(!valid) action.addError("validateBasicMonsterConditions"); 
		return valid;
	}

	/**
	 * Condiciones extra que tiene cada MonsterAction en particular.
	 */
	boolean validate(MonsterAction action) {
		return true; // comportamiento por defecto, que se reescribe en cada instancia de MonsterActionType.
	}

	
	// **** EXECUTION ****//

	/**
	 * Ejecuta la acción del monstruo y modifica el estado del monstruo, de la
	 * sala y de la guarida de esa sala (posiblemente también el de los demás
	 * monstruos que tengan actividad en esa sala en ese mismo momento. Tener en
	 * cuenta que execute hace un isValid previamente.
	 * 
	 * @return true si se ha podido guardar. False si la tarea no es válida.
	 */
	public boolean execute(MonsterAction action) {
		Monster monster = action.getMonster();
		if (isValid(action)) { // tambien se comprueba qu el  monstruo tiene al menos un turno libre.
			monster.useFreeTurn();
			doExecute(action);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Ejecución de la acción de cada MonsterAction en particular. La acción ya
	 * es válida, así que en principio no hace falta hacer comprobaciones. Solo
	 * hace falta implementar las modificaciones que se producen en el monstruo,
	 * en la sala y en la guarida, excepto lo de restar un turno al monstruo,
	 * que ya se hace previamente.
	 */
	abstract void doExecute(MonsterAction action);

}