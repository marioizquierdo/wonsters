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
	
	@Override
    public List<MonsterAge> allowedMonsterAges() {
		return ages("Adult");
	}
	
	@Override
    public List<RoomType> allowedRoomTypes() {
		return rooms("Warehouse");
	}

	@Override
    protected boolean checkExtraConditions() {
		return true; // TODO: ¿hay algo más que comprobar aquí?
	}
	
	@Override
    protected void doExecute() {
		// TODO Dale caña aquí a la movida

	}

}
