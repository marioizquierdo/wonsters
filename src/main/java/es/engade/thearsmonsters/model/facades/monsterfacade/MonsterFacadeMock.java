package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionToDo;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionsToDo;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class MonsterFacadeMock implements MonsterFacade {

	private List<MonsterEgg> eggsList;
	private MonsterEgg egg;
		
	public MonsterFacadeMock() {
		eggsList = new ArrayList<MonsterEgg>();
		egg = new MonsterEgg();
		eggsList.add(egg);
	}
	
	public List<MonsterEgg> findEggs(Lair lair) throws InternalErrorException {
		return lair.getMonsterEggs();
	}

	public void incubateEgg(Lair lair, String eggId)
			throws InternalErrorException, InstanceNotFoundException {
	}

	public Integer sellEgg(Lair lair, String eggId)
			throws InternalErrorException, InstanceNotFoundException {
		return 3;
	}
	public Monster bornMonster(Lair lair, String eggId, String monsterName) 
			throws InternalErrorException,
			InstanceNotFoundException, MonsterGrowException,
			InsuficientVitalSpaceException {
		return FactoryData.MonsterWhoIs.Child.build();
	}
	
	public void buryMonster(Lair lair, String monsterId){
		System.out.println("Mounstro "+ monsterId +"enterrado");
		return;
	}

	public MonsterEgg buyEgg(Lair lair, MonsterRace race)
			throws InternalErrorException, InsuficientMoneyException,
			MaxEggsException {
		return egg;
	}

	public List<Monster> findLairMonsters(Lair lair)
			throws InternalErrorException {
		return lair.getMonsters();
	}

	public Monster findMonster(Lair lair, String monsterId) throws InternalErrorException,
			InstanceNotFoundException {
		
		return FactoryData.MonsterWhoIs.Adult.build();
	}

	public Monster metamorphosisToAdult(Lair lair, String monsterId)
			throws InternalErrorException, InstanceNotFoundException,
			MonsterGrowException {
		return null;
	}
	
	public List<MonsterActionSuggestion> suggestMonsterActions(Lair lair, Monster monster) throws InstanceNotFoundException{
		List<MonsterActionSuggestion> actions = new ArrayList<MonsterActionSuggestion>();
		actions.add(MonsterActionType.GarbageHarvest.build(monster, RoomType.Warehouse).getSuggestion());
		actions.add(MonsterActionType.WorkInTheWorks.build(monster, RoomType.TradeOffice).getSuggestion());
		actions.add(MonsterActionType.WorkInTheWorks.build(monster, RoomType.Warehouse).getSuggestion());
		return actions;
	}
	
	public List<MonsterActionSuggestion> suggestMonsterActions(Lair lair, String monsterId) throws InstanceNotFoundException{
		return suggestMonsterActions(null, FactoryData.MonsterWhoIs.Adult.build());
	}
	
	public boolean executeMonsterActions(Lair lair, MonsterActionsToDo actionsToDo) {
		return true;
	}

}
