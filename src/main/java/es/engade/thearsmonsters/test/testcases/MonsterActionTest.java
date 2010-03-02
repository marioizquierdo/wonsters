package es.engade.thearsmonsters.test.testcases;

import static org.junit.Assert.*;

import java.util.List;


import org.junit.Before;
import org.junit.Test;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.types.Dormitories;
import es.engade.thearsmonsters.model.entities.room.types.TruffleFarm;
import es.engade.thearsmonsters.model.entities.room.types.Warehouse;
import es.engade.thearsmonsters.model.monsteraction.CultivateTruffles;
import es.engade.thearsmonsters.model.monsteraction.GarbageHarvest;
import es.engade.thearsmonsters.model.monsteraction.WorkInTheWorks;
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
		monsterChild.setLair(lair);
		monsterAdult.setLair(lair);
		monsterOld.setLair(lair);
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
    public void testWorkInTheWorks() {
    	Dormitories dormitories = new Dormitories(lair);
    	TruffleFarm truffleFarm = new TruffleFarm(lair);
    	
    	WorkInTheWorks actionChildDormitories = 	new WorkInTheWorks(monsterChild,dormitories);
    	WorkInTheWorks actionAdultDormitories = 	new WorkInTheWorks(monsterAdult,dormitories);
    	WorkInTheWorks actionOldDormitories = 		new WorkInTheWorks(monsterOld,dormitories);
    	
    	WorkInTheWorks actionChildFarm 	= 			new WorkInTheWorks(monsterChild,truffleFarm);
    	WorkInTheWorks actionAdultFarm	= 			new WorkInTheWorks(monsterAdult,truffleFarm);
    	WorkInTheWorks actionOldFarm 	= 			new WorkInTheWorks(monsterOld,truffleFarm);
    	
    	
    	assertFalse(actionChildDormitories.isValid());
    	assertTrue(actionAdultDormitories.isValid());
    	assertFalse(actionOldDormitories.isValid());
    	assertFalse(actionChildFarm.isValid());
    	assertTrue(actionAdultFarm.isValid());
    	assertFalse(actionOldFarm.isValid());
    }

    @Test 
    public void testUseFreeTurns() {
    	
    	int freeTurns;
    	Dormitories dormitories = new Dormitories(monsterChild.getLair());
    	List<Room> rooms = new java.util.Vector<Room>();
    	rooms.add(dormitories);
    	lair.setRooms(rooms);
    	
    	freeTurns = monsterChild.getFreeTurns();
    	/* Se acaba de crear el monstruo, sus turnos deben ser 0 */
    	assertEquals(freeTurns,11);

    	monsterChild.refreshFreeTurns();
    	
    	/* Estamos en el mismo d�a que se creo por lo que debe seguir siendo = a 0 */ 
    	assertEquals(monsterChild.getFreeTurns(),11);
    	
     	/* Utilizo un turno */
    	monsterChild.useFreeTurns();
    	
    	/*Deberiamos tener un turno menos */
    	assertEquals(monsterChild.getFreeTurns(),freeTurns-1);

    }

}
