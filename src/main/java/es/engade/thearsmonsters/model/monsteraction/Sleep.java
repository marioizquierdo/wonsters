package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;



public class Sleep extends MonsterAction{
	
	public Sleep(Monster monster, Room room) {
		super(monster, room);
	}
	
	public List<MonsterAge> allowedMonsterAges() {
		return ages("Child,Cocoon,Adult,Old");
	}
	
	public List<RoomType> allowedRoomTypes() {
		return rooms("Dormitories");
	}

	protected boolean checkExtraConditions() {
		return true; // TODO: Se comprueban condiciones extras para la action sleep
	}
	
	protected void doExecute() {
		// TODO Dale caña aquí a la movida

	}


}
