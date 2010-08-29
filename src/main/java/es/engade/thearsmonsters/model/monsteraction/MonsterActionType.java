package es.engade.thearsmonsters.model.monsteraction;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
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
		boolean validate(Monster monster, Room room, Lair lair, List<String> errors) {
			int currentGarbage = lair.getGarbage();
			int maxGarbage = lair.getGarbageStorageCapacity();

			boolean valid = currentGarbage < maxGarbage;
			if (!valid) errors.add("validateGarbageHarvest");
			return valid;
		}

		// Añade la basura en la guarida y añade experiencia al monstruo en la
		// habilidad de recolección de basura.
		void doExecute(Monster monster, Room room, Lair lair) {
			int current = lair.getGarbage();
			int increment = this.targetValueIncreasePerTurn(monster, room, lair);
			int max = lair.getGarbageStorageCapacity();

			if (current + increment > max) {
				lair.setGarbage(max);
			} else {
				lair.setGarbage(current + increment);
			}
			monster.addExp(AttrType.HarvesterSkill, 20);
		}

		// targetValue = Cantidad de basura que hay en la guarida.
        public Integer targetValue(Monster monster, Room room, Lair lair) {
	        return lair.getGarbage();
        }

        // targetValueIncreasePerTurn = Atributo compuesto "recolección" del monstruo
        public Integer targetValueIncreasePerTurn(Monster monster, Room room, Lair lair) {
	        return monster.getComposeAttr(AttrType.Harvest).getLevel();
        }

        // targetValueMax = Capacidad del almacén
        public Integer targetValueMax(Monster monster, Room room, Lair lair) {
        	return lair.getGarbageStorageCapacity();
        }
        
        // Parámetros para Monster.actions.type.GarbageHarvest.info
        // {0} Nivel recolección del monstruo
        // {1} Cantidad de basura que se recolecta por turno (es decir, targetValueIncreasePerTurn)
        protected Object[] infoMessageParams(Monster monster, Room room, Lair lair) {
    		return new Object[]{
    				targetValueIncreasePerTurn(monster, room, lair), // {0}
    				targetValueIncreasePerTurn(monster, room, lair)  // {1}
    		};
    	}
        
        // Parámetros para Monster.actions.type.GarbageHarvest.targetValue
        // {0} targetValue con marca para ser identificado por JavaScript
        // {1} capacidad máxima del almacén.
        protected Object[] targetValueMessageParams(Monster monster, Room room, Lair lair) {
    		return new Object[]{
    				targetValueWithMark(monster, room, lair),
    				lair.getGarbageStorageCapacity()
    		};
    	}
        
	},

	/**
	 * Trabajar en las obras de una sala.
	 * Un monstruo adulto avanza en el effortDone de las obras tanto como su atributo Build en cada turno.
	 */
	WorkInTheWorks("Adult", "all") {

		// Se comprueba si la sala está en obras.
		boolean validate(Monster monster, Room room, Lair lair, List<String> errors) {
			boolean valid = room.isInWorks();
			if(!valid) errors.add("validateWorkInTheWorks");
			return valid;
		}

		// Avanza en el esfuerzo realizado (effortDone) de las obras de la sala.
		// Si se terminan las obras se aumenta de nivel.
		void doExecute(Monster monster, Room room, Lair lair) {
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
        public Integer targetValue(Monster monster, Room room, Lair lair) {
	        return room.getEffortDone();
        }

        // targetValueIncreasePerTurn = Atributo compuesto "construcción" del monstruo
        public Integer targetValueIncreasePerTurn(Monster monster, Room room, Lair lair) {
	        return monster.getComposeAttr(AttrType.Construction).getLevel();
        }

        // targetValueMax = Esfuerzo necesario para realizar toda la obra.
        public Integer targetValueMax(Monster monster, Room room, Lair lair) {
        	return room.getEffortUpgrade();
        }
        
        // Parámetros para Monster.actions.type.WorkInTheWorks.info
        // {0} Nivel de la sala al que se va a mejorar.
        // {1} Attr Construcción del monstruo
        // {2} Cantidad de esfuerzo realizado por turno (es decir, targetValueIncreasePerTurn)
        // {3} Cantidad de esfuerzo que hace falta para subir de nivel la sala
        // {4} Cantidad de esfuerzo que lleva completado
        // {5} % de obra completada
        protected Object[] infoMessageParams(Monster monster, Room room, Lair lair) {
    		return new Object[]{
    				room.getLevel() + 1, // {0}
    				targetValueIncreasePerTurn(monster, room, lair),  // {1}
    				targetValueIncreasePerTurn(monster, room, lair),  // {2}
    				room.getEffortUpgrade(), // {3}
    				room.getEffortDone(), // {4}
    				room.getEffortDonePercentage() // {5}
    		};
    	}
        
        // Parámetros para Monster.actions.type.WorkInTheWorks.targetValue
        // {0} targetValue con marca para ser identificado por JavaScript
        // {1} esfuerzo total requerido
        // {2} siguiente nivel de la sala
        protected Object[] targetValueMessageParams(Monster monster, Room room, Lair lair) {
    		return new Object[]{
    				targetValueWithMark(monster, room, lair), // {0}
    				room.getEffortUpgrade(), // {1}
    				room.getLevel() + 1 // {2}
    		};
    	}
	};

	// **** Attributes, Constructor and common Getters ****//

	private final List<MonsterAge> allowedMonsterAges;
	private final List<RoomType> allowedRoomTypes;
	private List<String> errors = new ArrayList<String>();

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

	public List<String> getErrors() {
		return errors;
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
	public MonsterActionSuggestion getSuggestion(Monster monster, Room room, Lair lair) {
		return new MonsterActionSuggestion(
				this, monster.getId(), room.getRoomType(), 
				maxTurnsToAssign(monster, room, lair),
				infoMessageKey(monster, room, lair), 
				infoMessageParams(monster, room, lair), 
				targetValue(monster, room, lair),
				targetValueIncreasePerTurn(monster, room, lair), 
				targetValueMessageKey(monster, room, lair),
				targetValueMessageParams(monster, room, lair)
		);
	}
	
	/**
	 * Clave del mensaje de info de la tarea. Por defecto es "Monster.actions.type.{actionType}.info"
	 */
	protected String infoMessageKey(Monster monster, Room room, Lair lair) {
		return "Monster.actions.type."+ this +".info";
	}
	
	/**
	 * Parámetros numéricos en el mensaje de info de la tarea. 
	 * Tienen que ir en el mismo orden de aparición {0}, {1}, {2}...
	 */
	protected Object[] infoMessageParams(Monster monster, Room room, Lair lair) {
		return new Object[]{};
	}
	
	/**
	 * Valor actual del objetivo de la tarea. 
	 * Cada una tiene el suyo propio, por ejemplo para el Gimnasio es el valor de la fuerza actual del monstruo.
	 */
	protected abstract Integer targetValue(Monster monster, Room room, Lair lair);
	
	/**
	 * Cantidad que se incrementa en el valor actual del objetivo de la tarea. 
	 * Por ejemplo para el Gimnasio es la cantidad de fuerza que se mejora por cada turno invertido.
	 */
	protected abstract Integer targetValueIncreasePerTurn(Monster monster, Room room, Lair lair);
	
	/**
	 * Valor máximo que puede alcanzar el "targetValue".
	 * Por ejemplo, para la recolección de basura es la capacidad del almacén.
	 */
	protected abstract Integer targetValueMax(Monster monster, Room room, Lair lair);
	
	/**
	 * Clave del mensaje de información sobre el objetivo de la tarea que se muestra debajo del título de la tarea en la vista.
	 * Por defecto es "Monster.actions.type.{actionType}.targetValue"
	 */
	protected String targetValueMessageKey(Monster monster, Room room, Lair lair) {
		return "Monster.actions.type."+ this +".targetValue";
	}
	
	/**
	 * Parámetros en el mensaje de info sobre el objetivo de la tarea. Tienen que ir en el mismo orden de aparición {0}, {1}, {2}...
	 * El parámetro que representa al "target value" debe ir envuelto con el método targetValueMark(targetValue) para que la vista
	 * sepa cual es y pueda modificar su valor dinámicamente (javascript).
	 * Por defecto se devuelve un único parámetro que es el "target value".
	 */ 
	protected Object[] targetValueMessageParams(Monster monster, Room room, Lair lair) {
		return new Object[]{targetValueWithMark(monster, room, lair)};
	}
	
	
	protected String targetValueWithMark(Monster monster, Room room, Lair lair) {
		return "<span class=\"targetValue\">"+ targetValue(monster, room, lair) +"</span>";
	}
	
	/**
	 * Máxima cantidad de turnos que se pueden asignar a esta tarea (null = indefinido, sin límite).
	 * Previene a la vista para que no permita asignar valores incorrectos.
	 */
	protected Integer maxTurnsToAssign(Monster monster, Room room, Lair lair) {
    	Integer current = targetValue(monster, room, lair);
    	Integer increment = targetValueIncreasePerTurn(monster, room, lair);
    	Integer max = targetValueMax(monster, room, lair);
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
	public boolean isValid(Monster monster, Room room) {
		errors.removeAll(errors);
		return validateBasicMonsterConditions(monster) && 
				validateBasicRoomConditions(room) && 
				validate(monster, room, room.getLair(), errors); // hook para implementar en cada MonsterAction particular
	}

	/**
	 * Igual que isValid(monster, room), pero la room se obtiene de la misma
	 * guarida que el monstruo (no vale para validar tareas en otra guarida).
	 */
	public boolean isValid(Monster monster, RoomType roomType) {
		errors.removeAll(errors);
		return isValid(monster, monster.getLair().getRoom(roomType));
	}

	/**
	 * Validación básica de que la sala es una de las salas permitidas.
	 */
	boolean validateBasicRoomConditions(Room room) {
		boolean valid = this.allowedRoomTypes.contains(room.getRoomType());
		if(!valid) errors.add("validateBasicRoomConditions");
		return valid;
	}

	/**
	 * Validación básica de que el monstruo tiene la edad permitida y que tiene
	 * al menos un turno libre para ejecutar la acción.
	 */
	boolean validateBasicMonsterConditions(Monster monster) {
		boolean valid = this.allowedMonsterAges.contains(monster.getAge())
				&& monster.isFreeTurnsAvailable();
		if(!valid) errors.add("validateBasicMonsterConditions"); 
		return valid;
	}

	/**
	 * Condiciones extra que tiene cada MonsterAction en particular.
	 */
	boolean validate(Monster monster, Room room, Lair lair, List<String> errors) {
		return true; // comportamiento por defecto.
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
	public boolean execute(Monster monster, Room room) {
		if (isValid(monster, room)) { // tambien se comprueba qu el  monstruo tiene al menos un turno libre.
			monster.useFreeTurn();
			doExecute(monster, room, room.getLair());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Igual que execute(monster, room), pero la room se obtiene de la misma
	 * guarida que el monstruo (no vale para ejecutar tareas en otra guarida).
	 */
	public boolean execute(Monster monster, RoomType roomType) {
		return execute(monster, monster.getLair().getRoom(roomType));
	}

	/**
	 * Ejecución de la acción de cada MonsterAction en particular. La acción ya
	 * es válida, así que en principio no hace falta hacer comprobaciones. Solo
	 * hace falta implementar las modificaciones que se producen en el monstruo,
	 * en la sala y en la guarida, excepto lo de restar un turno al monstruo,
	 * que ya se hace previamente.
	 */
	abstract void doExecute(Monster monster, Room room, Lair lair);

}