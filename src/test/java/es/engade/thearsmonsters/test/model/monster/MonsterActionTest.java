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
import es.engade.thearsmonsters.model.monsteraction.GarbageHarvest;
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.WorkInTheWorks;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class MonsterActionTest extends GaeTest{

	Lair lair;
	Monster monsterChild;
	Monster monsterAdult;
	Monster monsterOld;
	
	@Before
    public void setUp() throws Exception {
		lair = FactoryData.LairWhatIs.Default.build();
		monsterChild = FactoryData.MonsterWhoIs.Child.build();
		monsterAdult = FactoryData.MonsterWhoIs.Adult.build();
		monsterOld = FactoryData.MonsterWhoIs.Old.build();
		lair.addMonster(monsterChild);
		lair.addMonster(monsterAdult);
		lair.addMonster(monsterOld);
	}

    @Test 
    public void testGarbageHarvestValid() {
    	Room wareHouse = FactoryData.RoomWhatIs.Warehouse.build();
    	Room tradeOffice = FactoryData.RoomWhatIs.TradeOffice.build();    	

    	assertTrue(( new GarbageHarvest(monsterAdult, wareHouse)).isValid()); // Solo los adultos pueden trabajar en el Almacén
    	assertFalse((new GarbageHarvest(monsterChild, wareHouse)).isValid());
    	assertFalse((new GarbageHarvest(monsterOld,   wareHouse)).isValid());
    	assertFalse((new GarbageHarvest(monsterChild, tradeOffice)).isValid());
    	assertFalse((new GarbageHarvest(monsterAdult, tradeOffice)).isValid());
    	assertFalse((new GarbageHarvest(monsterOld,   tradeOffice)).isValid());
    }
    
    @Test 
    public void testWorkInTheWorksValid() {
    	Room roomInWorks = FactoryData.RoomWhatIs.InWorks.build();
    	Room room = FactoryData.RoomWhatIs.InNormalState.build();
    	
    	assertTrue(roomInWorks.isInWorks());
    	assertFalse(room.isInWorks());
    	

    	assertTrue((new WorkInTheWorks(monsterAdult, roomInWorks)).isValid()); // Solo los adultos pueden trabajar en las obras
    	assertFalse((new WorkInTheWorks(monsterChild, roomInWorks)).isValid());
    	assertFalse((new WorkInTheWorks(monsterOld,   roomInWorks)).isValid());

    	assertFalse((new WorkInTheWorks(monsterAdult, room)).isValid());
    	assertFalse((new WorkInTheWorks(monsterChild, room)).isValid());
    	assertFalse((new WorkInTheWorks(monsterOld,   room)).isValid());
    }

    
    @Test 
    public void testBuildRoom() {
    	int contador = 0;
    	int levelInitial = 0;
    	int effortToUpgrade = 0;
    	int initialFreeTurns = 0;
    	int turnsToUpgrade = 0;
    	
    	//Añado experiencia en la construccion
    	monsterAdult.addExp(AttrType.ConstructorSkill, 100);
    	monsterAdult.addExp(AttrType.Strenght, 100);
    	
    	/* Creo una room de tipo TradeOffice */
    	Room tradeOffice = lair.buildRoom(RoomType.TradeOffice);
    	
    	/* Creo la accion */
    	MonsterAction action = new WorkInTheWorks(monsterAdult, tradeOffice);

    	/* Obtengo el esfuerzo que hay que hacer para subir de nivel la sala */
    	effortToUpgrade = tradeOffice.getEffortUpgrade();
    	
    	/* Obtengo el numero de turnos necesarios para subir de nivel la sala, 
    	 * dividiendo el esfuerzo entre el nivel del monstruo en la construccion */
    	turnsToUpgrade = effortToUpgrade / monsterAdult.getComposeAttr(AttrType.Construction).getLevel();
    	
    	/* Le pongo más turnos libres de los necesarios, para asegurarnos de que puede terminar la obra */
    	monsterAdult.setFreeTurns(turnsToUpgrade + 10);
    	initialFreeTurns = monsterAdult.getFreeTurns();
    	
    	/* Obtengo el nivel inicial de la sala */
    	levelInitial = tradeOffice.getLevel();
    	
    	/* Ejecuto el numero de turnos necesarios para subir de nivel la sala */
    	while (contador < turnsToUpgrade){
    		action.execute();
    		contador ++;
    	}
    	
    	/* Compruebo que la sala subio de nivel */
    	assertEquals(tradeOffice.getLevel(), levelInitial + 1);
    	
    	/* Compruebo que el numero de turnos consumidos por el monstruo son los adecuados */
    	assertEquals(initialFreeTurns, turnsToUpgrade + monsterAdult.getFreeTurns());
    }
    
    @Test
    public void testHarvestGarbage(){
    	
    	int maxGarbage = lair.getGarbageStorageCapacity();
    	int contador = 0;
    	
    	//Añado experiencia en la recolección
    	monsterAdult.addExp(AttrType.HarvesterSkill, 100);
    	monsterAdult.addExp(AttrType.Strenght, 100);
    	
    	MonsterAction action = new GarbageHarvest(monsterAdult, lair.getRoom(RoomType.Warehouse));
    	
    	monsterAdult.setFreeTurns(1000);
    	
    	while (lair.getGarbage() < maxGarbage ){
    		action.execute();
    		contador++;
    	}
    	
    	//comprobamos que gasta los turnos del mounstro
    	assertEquals(1000-contador,monsterAdult.getFreeTurns());
    	
    	//comprobamos que se ha llenado de basura
    	assertEquals(lair.getGarbage(), maxGarbage);
    }
    
}
