package es.engade.thearsmonsters.test.model.monster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.monsteraction.CultivateTruffles;
import es.engade.thearsmonsters.model.monsteraction.GarbageHarvest;
import es.engade.thearsmonsters.model.monsteraction.WorkInTheWorks;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.test.util.FactoryData;
import es.engade.thearsmonsters.test.util.FactoryData.LairWhatIs;
import es.engade.thearsmonsters.test.util.FactoryData.MonsterWhatIs;

public class MonsterActionTest extends GaeTest{

	Lair lair;
	Monster monsterChild;
	Monster monsterAdult;
	Monster monsterOld;
	
	@Before
    public void setUp() throws Exception {
		lair = FactoryData.generate(LairWhatIs.Default);
		monsterChild = FactoryData.generate(MonsterWhatIs.Child);
		monsterAdult = FactoryData.generate(MonsterWhatIs.Adult);
		monsterOld = FactoryData.generate(MonsterWhatIs.Old);
		monsterChild.setLair(lair);
		monsterAdult.setLair(lair);
		monsterOld.setLair(lair);
	}


    @Test 
    public void testCultivateTruffles() {
    	
    	Room dormitories = RoomType.Dormitories.build(lair);
    	Room truffleFarm = RoomType.TruffleFarm.build(lair);
    	
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
    	Room wareHouse = RoomType.Warehouse.build(lair);
    	Room truffleFarm = RoomType.TruffleFarm.build(lair);
    	
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
    	Room dormitories = RoomType.Dormitories.build(lair);
    	Room truffleFarm = RoomType.TruffleFarm.build(lair);
    	
    	
    	WorkInTheWorks actionChildDormitories = 	new WorkInTheWorks(monsterChild,dormitories);
    	WorkInTheWorks actionAdultDormitories = 	new WorkInTheWorks(monsterAdult,dormitories);
    	WorkInTheWorks actionOldDormitories = 		new WorkInTheWorks(monsterOld,dormitories);
    	
    	WorkInTheWorks actionChildFarm 	= 			new WorkInTheWorks(monsterChild,truffleFarm);
    	WorkInTheWorks actionAdultFarm	= 			new WorkInTheWorks(monsterAdult,truffleFarm);
    	WorkInTheWorks actionOldFarm 	= 			new WorkInTheWorks(monsterOld,truffleFarm);
    	
    	
    	/* Los dormitorios se crean por defecto en estado normal */
    	assertFalse(actionChildDormitories.isValid());
    	assertFalse(actionAdultDormitories.isValid());
    	assertFalse(actionOldDormitories.isValid());
    	
    	
    	assertFalse(actionChildFarm.isValid());
    	assertTrue(actionAdultFarm.isValid());
    	assertFalse(actionOldFarm.isValid());
    }

    
    @Test 
    public void testBuildRoom() {
    	int contador = 0;
    	int levelInitial = 0;
    	int effortToUpgrade = 0;
    	int initialFreeTurns = 0;
    	int turnsToUpgrade = 0;
    	
    	// Instancio una guarida nueva con los valores iniciales 
    	Lair lair = FactoryData.generate(LairWhatIs.InInitialState);
    	
    	//Utilizo el monstruo adulto para establecerle un numero de turnos alto
    	monsterAdult.setFreeTurns(1000);
    	
    	//A�ado el monstruo a la guarida y le a�ado experiencia en la construccion
    	lair.addMonster(monsterAdult);
    	monsterAdult.addExp(AttrType.ConstructorSkill,100);
    	monsterAdult.addExp(AttrType.Strenght,100);
    	
    	/* Creo una room de tipo TruffleFarm */
    	Room truffleFarm = RoomType.TruffleFarm.build(lair);
    	
    	/* Añado la room creada a la guarida */
    	lair.addRoom(truffleFarm);
    	
    	/* Creo la accion */
    	WorkInTheWorks actionAdultFarm	= new WorkInTheWorks(monsterAdult,truffleFarm);

    	/*Obtengo el esfuerzo que hay que hacer para subir de nivel la sala */
    	effortToUpgrade = truffleFarm.getEffortUpgrade();;
    	
    	/*Obtengo el numero de turnos necesarios para subir de nivel la sala, 
    	 * dividiendo el esfuerzo entre el nivel del monstruo en la construccion */
    	turnsToUpgrade =  effortToUpgrade/monsterAdult.getAttr(AttrType.Construction).getLevel();
    	
    	/*Obtengo el numero de turnos libres iniciales que tiene el monstruo */
    	initialFreeTurns = monsterAdult.getFreeTurns();
    	
    	/* Obtengo el nivel inicial de la sala */
    	levelInitial = truffleFarm.getLevel();
    	
    	
    	/* Ejecuto el numero de turnos necesarios para subir de nivel la sala */
    	while (contador < turnsToUpgrade){
    		actionAdultFarm.execute();
    		contador ++;
    	}
    	
    	/* Compruebo que la sala subio de nivel */
    	assertEquals(truffleFarm.getLevel(),levelInitial+1);
    	
    	/* Compruebo que el numero de turnos consumidos por el monstruo son los adecuados */
    	assertEquals(initialFreeTurns,turnsToUpgrade  + monsterAdult.getFreeTurns());
    	
    }
    
}
