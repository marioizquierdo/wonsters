package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;

public abstract class MonsterAction {
	
	/**
	 * Es el monstruo que ejecuta la acción
	 */
	protected Monster monster;
	
	/**
	 * Sala donde se ejecuta la acción.
	 * La guarida se obtiene a partir de esta sala.
	 */
	protected Room room;
	
	protected RoomType roomType;
	protected List<MonsterAge> ageStates;
	
	public MonsterAction(Monster monster, Room room) {
		this.monster = monster;
		this.room = room;
	}
	
	public Monster getMonster() { return this.monster; }
	public Room getRoom() { return this.room; }
	public Lair getLair() { return this.getRoom().getLair(); }
	public abstract List<RoomType> allowedRoomTypes();
	public abstract List<MonsterAge> allowedMonsterAges();
	
	
	/**
	 * Teniendo en cuenta los datos incluidos (monster y room), valida si esta tarea
	 * se puede ejecutar o no.
	 */
	public boolean isValid() {
		return checkBasicRoomConditions() &&
			checkBasicMonsterConditions() &&
			checkExtraConditions();
	}

	protected boolean checkBasicRoomConditions() {
		return this.allowedRoomTypes().contains(this.room.getRoomType());
	}
	protected boolean checkBasicMonsterConditions() {
		return this.allowedMonsterAges().contains(this.monster.getAge());
			//&& monster.hasFreeTurns(); TODO: añadir este método en monster.
	}
	protected boolean checkExtraConditions() {
		return true;
	}
	
	/**
	 * Ejecuta la acción del monstruo y modifica el estado del monstruo, de la sala
	 * y de la guarida de esa sala (posiblemente también el de los demás monstruos
	 * que tengan actividad en esa sala en ese mismo momento.
	 * Tener en cuenta que execute hace un isValid previamente.
	 * @return true si se ha podido guardar. False si la tarea no es válida.
	 */
	public boolean execute() {
		if(isValid()) {
			doExecute();
			return true;
		} else {
			return false;
		}
		
	}
	
	protected abstract void doExecute();
	
	protected List<MonsterAge> ages(String stringList) {
		return MonsterAge.list(stringList);
	}
	
	protected List<RoomType> rooms(String stringList) {
		return RoomType.list(stringList);
	}

}
