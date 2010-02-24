package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;



public class CultivateTruffles extends MonsterAction{
	
	public CultivateTruffles(Monster monster, Room room) {
		super(monster, room);
	}
	
	public List<MonsterAge> allowedMonsterAges() {
		return ages("Adult");
	}
	
	public List<RoomType> allowedRoomTypes() {
		return rooms("TruffleFarm");
	}

	protected boolean checkExtraConditions() {
		/* Aqui se a�adiran las condiciones extras para comprobar si se puede recoger trufas */
		return true; 
	}
	
	protected void doExecute() {
		// TODO Dale caña aquí a la movida
	}

}
