package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.monsteraction.GarbageHarvest;
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.WorkInTheWorks;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;
import es.engade.thearsmonsters.util.factory.FactoryData.MonsterWhoIs;

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

	public void incubateEgg(String eggId, Lair lair)
			throws InternalErrorException, InstanceNotFoundException,
			InsuficientVitalSpaceException {
	}

	public Integer shellEgg(String eggId, Lair lair)
			throws InternalErrorException, InstanceNotFoundException {
		return 3;
	}
	public Monster bornMonster(String eggId, String monsterName, Lair lair) 
			throws InternalErrorException,
			InstanceNotFoundException, MonsterGrowException,
			InsuficientVitalSpaceException {
		return FactoryData.MonsterWhoIs.Child.build();
	}

	public MonsterEgg buyEgg(MonsterRace race, Lair lair)
			throws InternalErrorException, InsuficientMoneyException,
			MaxEggsException {
		return egg;
	}

	public List<Monster> findLairMonsters(Lair lair)
			throws InternalErrorException {
		return lair.getMonsters();
	}

	public Monster findMonster(String monsterId) throws InternalErrorException,
			InstanceNotFoundException {
		
		return FactoryData.MonsterWhoIs.Adult.build();
	}

	public Monster metamorphosisToAdult(String monsterId, Lair lair)
			throws InternalErrorException, InstanceNotFoundException,
			MonsterGrowException {
		return null;
	}
	
	public List<MonsterAction> suggestMonsterActions(Key monsterId) throws InstanceNotFoundException{
		List listActionsValid = new ArrayList();
		Monster monster = FactoryData.MonsterWhoIs.Adult.build();
		GarbageHarvest garbage = new GarbageHarvest(monster,monster.getLair().getRoom(RoomType.Warehouse));
		WorkInTheWorks work = new WorkInTheWorks(monster,monster.getLair().getRoom(RoomType.TradeOffice));
		WorkInTheWorks work2 = new WorkInTheWorks(monster,monster.getLair().getRoom(RoomType.Dormitories));
		listActionsValid.add(garbage);
		listActionsValid.add(work);
		listActionsValid.add(work2);
		return listActionsValid;
	}

}
