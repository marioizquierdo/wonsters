package es.engade.thearsmonsters.test.testcases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.types.Dormitories;
import es.engade.thearsmonsters.model.entities.room.types.TruffleFarm;
import es.engade.thearsmonsters.model.entities.room.types.Warehouse;
import es.engade.thearsmonsters.model.monsteraction.CultivateTruffles;
import es.engade.thearsmonsters.model.monsteraction.GarbageHarvest;
import es.engade.thearsmonsters.model.monsteraction.Sleep;
import es.engade.thearsmonsters.test.DataSpawner;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.test.DataSpawner.MonsterInstance;

public class MonsterActionTest extends GaeTest{

	Lair lair;
	Monster monsterChild;
	Monster monsterAdult;
	Monster monsterOld;
	
	@Before
    public void setUp() throws Exception {
		lair = new Lair();
		monsterChild = DataSpawner.generateMonster(MonsterInstance.Child);
		monsterAdult = DataSpawner.generateMonster(MonsterInstance.Adult);
		monsterOld = DataSpawner.generateMonster(MonsterInstance.Old);
    }


    @Test 
    public void testCultivateTruffles() {
    	
    	Dormitories dormitories = new Dormitories(lair);
    	TruffleFarm truffleFarm = new TruffleFarm(lair);
    	
    	CultivateTruffles actionChildDormitories = 	new CultivateTruffles(monsterChild,dormitories);
    	CultivateTruffles actionAdultDormitories = 	new CultivateTruffles(monsterAdult,dormitories);
    	CultivateTruffles actionOldDormitories = 	new CultivateTruffles(monsterOld,dormitories);
    	
    	CultivateTruffles actionChildFarm 	= 	new CultivateTruffles(monsterChild,truffleFarm);
    	CultivateTruffles actionAdultFarm	= 	new CultivateTruffles(monsterAdult,truffleFarm);
    	CultivateTruffles actionOldFarm 	= 	new CultivateTruffles(monsterOld,truffleFarm);
    	
    	
    	assertFalse(actionChildDormitories.isValid());
    	assertFalse(actionAdultDormitories.isValid());
    	assertFalse(actionOldDormitories.isValid());
    	assertFalse(actionChildFarm.isValid());
    	assertTrue(actionAdultFarm.isValid());
    	assertFalse(actionOldFarm.isValid());
    	
    }

    @Test 
    public void testGarbageHarvest() {
    	Warehouse wareHouse = new Warehouse(lair);
    	TruffleFarm truffleFarm = new TruffleFarm(lair);
    	
    	GarbageHarvest actionChildWarehouse = 	new GarbageHarvest(monsterChild,wareHouse);
    	GarbageHarvest actionAdultWarehouse = 	new GarbageHarvest(monsterAdult,wareHouse);
    	GarbageHarvest actionOldWarehouse = 	new GarbageHarvest(monsterOld,wareHouse);
    	
    	GarbageHarvest actionChildFarm 	= 	new GarbageHarvest(monsterChild,truffleFarm);
    	GarbageHarvest actionAdultFarm	= 	new GarbageHarvest(monsterAdult,truffleFarm);
    	GarbageHarvest actionOldFarm 	= 	new GarbageHarvest(monsterOld,truffleFarm);
    	
    	
    	assertFalse(actionChildWarehouse.isValid());
    	assertTrue(actionAdultWarehouse.isValid());
    	assertFalse(actionOldWarehouse.isValid());
    	assertFalse(actionChildFarm.isValid());
    	assertFalse(actionAdultFarm.isValid());
    	assertFalse(actionOldFarm.isValid());
    }
    
    @Test 
    public void testSleep() {
    	Dormitories dormitories = new Dormitories(lair);
    	TruffleFarm truffleFarm = new TruffleFarm(lair);
    	
    	Sleep actionChildDormitories = 	new Sleep(monsterChild,dormitories);
    	Sleep actionAdultDormitories = 	new Sleep(monsterAdult,dormitories);
    	Sleep actionOldDormitories = 	new Sleep(monsterOld,dormitories);
    	
    	Sleep actionChildFarm 	= 	new Sleep(monsterChild,truffleFarm);
    	Sleep actionAdultFarm	= 	new Sleep(monsterAdult,truffleFarm);
    	Sleep actionOldFarm 	= 	new Sleep(monsterOld,truffleFarm);
    	
    	
    	assertTrue(actionChildDormitories.isValid());
    	assertTrue(actionAdultDormitories.isValid());
    	assertTrue(actionOldDormitories.isValid());
    	assertFalse(actionChildFarm.isValid());
    	assertFalse(actionAdultFarm.isValid());
    	assertFalse(actionOldFarm.isValid());
    }


}
