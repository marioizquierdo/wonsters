package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;

public class GarbageHarvest extends MonsterAction {

	public GarbageHarvest(Monster monster, Room room) {
		super(monster, room);
	}
	
	public List<MonsterAge> allowedMonsterAges() {
		return ages("Adult");
	}
	
	public List<RoomType> allowedRoomTypes() {
		return rooms("Warehouse");
	}

	protected boolean checkExtraConditions() {
		return true; // TODO: ¿hay algo más que comprobar aquí?
	}
	
	protected void doExecute() {
		// TODO Dale caña aquí a la movida

	}

}
