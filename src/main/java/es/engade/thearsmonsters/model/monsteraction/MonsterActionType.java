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

	// MonsterAction (allowedRoomTypes, allowedMonsterAges) { validate and
	// execute hooks }

	/**
	 * Recolectar basura. Un monstruo adulto consigue en cada turno tanta basura
	 * como su atributo Harvest, y la guarda en el almacén.
	 */
	GarbageHarvest("Adult", "Warehouse") {

		// Se comprueba si hay suficiente espacio en el almacén para añadir más
		// basura
		boolean validate(Monster monster, Room room, Lair lair,
				List<String> errors) {
			int currentGarbage = lair.getGarbage();
			int maxGarbage = lair.getGarbageStorageCapacity();

			boolean valid = currentGarbage < maxGarbage;
			if (!valid)
				errors.add("validateGarbageHarvest");
			return valid;
		}

		// Añade la basura en la guarida y añade experiencia al monstruo en la
		// habilidad de recolección de basura
		void doExecute(Monster monster, Room room, Lair lair) {
			int currentGarbage = lair.getGarbage();
			int monsterHarvestAttr = monster.getComposeAttr(AttrType.Harvest)
					.getLevel();

			if (currentGarbage + monsterHarvestAttr > lair
					.getGarbageStorageCapacity())
				lair.setGarbage(lair.getGarbageStorageCapacity());
			else
				lair.setGarbage(currentGarbage + monsterHarvestAttr);
			monster.addExp(AttrType.HarvesterSkill, 20);
		}
	},

	/**
	 * Trabajar en las obras de una sala. Un monstruo adulto avanza en el
	 * effortDone de las obras tanto como su atributo Build en cada turno.
	 */
	WorkInTheWorks("Adult", "all") {

		// Se comprueba si la sala está en obras.
		boolean validate(Monster monster, Room room, Lair lair,
				List<String> errors) {
			boolean valid = room.isInWorks();

			if (!valid)
				errors.add("validateWorkInTheWorks");

			return valid;
		}

		// Avanza en el esfuerzo realizado (effortDone) de las obras de la sala.
		// Si se terminan las obras se aumenta de nivel.
		void doExecute(Monster monster, Room room, Lair lair) {
			RoomInWorksState roomWorks = (RoomInWorksState) room.getState();
			int monsterConstructionAttr = monster
					.getAttr(AttrType.Construction).getLevel();
			roomWorks.setEffortDone(room.getEffortDone()
					+ monsterConstructionAttr);

			int totalEffort = room.isInInitialState() ? room.getEffortBuild()
					: room.getEffortUpgrade();
			if (room.getEffortDone() >= totalEffort) {
				room.setLevel(room.getLevel() + 1);
				room.setState(new RoomNormalState());
			}
		}
	};

	// **** Constructor, Attributes and common Getters ****//

	private final List<MonsterAge> allowedMonsterAges;
	private final List<RoomType> allowedRoomTypes;
	private List<String> errors = new ArrayList<String>();

	MonsterActionType(String allowedMonsterAgesStringList,
			String allowedRoomTypesStringList) {
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

	// **** VALIDATION ****//

	/**
	 * Teniendo en cuenta los datos incluidos (monster y room), valida si esta
	 * tarea se puede ejecutar o no. Atención: La ejecución de este método no
	 * debe modificar el estado de la guarida ni del monstruo ni de la room ni
	 * de nada, (por lo tanto validate() tampoco puede), el único que modifica
	 * el estado es execute.
	 */
	public boolean isValid(Monster monster, Room room) {
		errors.removeAll(errors);
		return validateBasicMonsterConditions(monster)
				&& validateBasicRoomConditions(room)
				&& validate(monster, room, room.getLair(), errors); // hook para
																	// implementar
																	// en cada
																	// MonsterAction
																	// particular
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
		if (!valid)
			errors.add("validateBasicRoomConditions");
		return valid;
	}

	/**
	 * Validación básica de que el monstruo tiene la edad permitida y que tiene
	 * al menos un turno libre para ejecutar la acción.
	 */
	boolean validateBasicMonsterConditions(Monster monster) {
		boolean valid = this.allowedMonsterAges.contains(monster.getAge())
				&& monster.isFreeTurnsAvailable();
		if (!valid)
			errors.add("validateBasicMonsterConditions"); // TODO: esto se
															// debería guardar
															// en una lista de
															// errores en el
															// objeto
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
		if (isValid(monster, room)) { // aqui tambien se comprueba que el
										// monstruo tiene al menos un turno
										// libre.
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