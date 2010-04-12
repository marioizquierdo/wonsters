package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
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
		return (room.getLair().getGarbage() < room.getLair().getGarbageStorageCapacity());
		 // TODO: ¿hay algo más que comprobar aquí
	}
	 
	@Override
    protected void doExecute() {
		int currentGarbage = room.getLair().getGarbage();
		int maxGarbage = room.getLair().getGarbageStorageCapacity();
		int monsterHarvestAttr = monster.getComposeAttr(AttrType.Harvest).getLevel();
		
		if ((maxGarbage - currentGarbage) < monsterHarvestAttr){
			room.getLair().setGarbage(maxGarbage);
		}else {
			room.getLair().setGarbage(currentGarbage + monsterHarvestAttr); 
		}
			
		monster.addExp(AttrType.HarvesterSkill, 20);
	}
	
	public String getType(){
		return "garbageHarvest";
	}
	
	//Cuando se implmente la enum MonsterActionType los toStrings se ponen en la clase monsterAction
	public String toString() {
		return "Recolectar basura(st)";
	}

}
