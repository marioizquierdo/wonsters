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
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
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
    	MonsterActionType garbageHarvest = MonsterActionType.GarbageHarvest;
    	Room wareHouse = FactoryData.RoomWhatIs.Warehouse.build();
    	Room tradeOffice = FactoryData.RoomWhatIs.TradeOffice.build();    	

    	assertTrue( garbageHarvest.isValid(monsterAdult, wareHouse)); // Solo los adultos pueden trabajar en el Almacén
    	assertFalse(garbageHarvest.isValid(monsterChild, wareHouse));
    	assertFalse(garbageHarvest.isValid(monsterOld,   wareHouse));
    	assertFalse(garbageHarvest.isValid(monsterChild, tradeOffice));
    	assertFalse(garbageHarvest.isValid(monsterAdult, tradeOffice));
    	assertFalse(garbageHarvest.isValid(monsterOld,   tradeOffice));
    }
    
    @Test 
    public void testWorkInTheWorksValid() {
    	MonsterActionType workInTheWorks = MonsterActionType.WorkInTheWorks;
    	Room roomInWorks = FactoryData.RoomWhatIs.InWorks.build();
    	Room room = FactoryData.RoomWhatIs.InNormalState.build();
    	
    	assertTrue(roomInWorks.isInWorks());
    	assertFalse(room.isInWorks());
    	

    	assertTrue( workInTheWorks.isValid(monsterAdult, roomInWorks)); // Solo los adultos pueden trabajar en las obras
    	assertFalse(workInTheWorks.isValid(monsterChild, roomInWorks));
    	assertFalse(workInTheWorks.isValid(monsterOld,   roomInWorks));
    	assertFalse(workInTheWorks.isValid(monsterAdult, room));
    	assertFalse(workInTheWorks.isValid(monsterChild, room));
    	assertFalse(workInTheWorks.isValid(monsterOld,   room));
    }

    
    @Test 
    public void testBuildRoom() {
    	int initialLevel = 0;
    	int effortToUpgrade = 0;
    	int initialFreeTurns = 0;
    	int turnsToUpgrade = 0;
    	int extraTurnsAvailable = 10;
    	
    	// Añadir un nivel en construccion y en fuerza
    	monsterAdult.addExp(AttrType.ConstructorSkill, 100);
    	monsterAdult.addExp(AttrType.Strenght, 100);
    	assertEquals(monsterAdult.getAttr(AttrType.Construction).getLevel(), 2); // Se supone que Construction == ConstructionSkill + Strenght
    	
    	Room tradeOffice = lair.getRoom(RoomType.TradeOffice);
    	MonsterAction action = MonsterActionType.WorkInTheWorks.build(monsterAdult, tradeOffice);

    	/* Obtengo el esfuerzo que hay que hacer para subir de nivel la sala */
    	effortToUpgrade = tradeOffice.getEffortUpgrade();
    	
    	/* Obtengo el numero de turnos necesarios para subir de nivel la sala, 
    	 * dividiendo el esfuerzo entre el nivel del monstruo en la construccion */
    	turnsToUpgrade = effortToUpgrade / monsterAdult.getComposeAttr(AttrType.Construction).getLevel();
    	
    	/* Le pongo más turnos libres de los necesarios, para asegurarnos de que puede terminar la obra */
    	monsterAdult.setFreeTurns(turnsToUpgrade + extraTurnsAvailable);
    	
    	initialFreeTurns = monsterAdult.getFreeTurns();
    	
    	/* Obtengo el nivel inicial de la sala */
    	initialLevel = tradeOffice.getLevel();
    	
    	
    	/* Ejecuto el numero de turnos necesarios para subir de nivel la sala */
    	assertTrue(action.isValid());
    	for(int i=0; i < turnsToUpgrade; i++) {
    		action.execute();
    	}
    	
    	/* Compruebo que la sala subio de nivel */
    	assertEquals(initialLevel + 1, tradeOffice.getLevel());
    	
    	/* Compruebo que el numero de turnos consumidos por el monstruo son los adecuados */
    	assertEquals(initialFreeTurns, turnsToUpgrade + monsterAdult.getFreeTurns());
    	
    	// Comprobar que una vez consumidos los turnos libres no se puede seguir realizando la acción
    	// porque ahora la sala ya no está en obras
    	assertFalse(action.isValid());
    	
    	// TODO: cuando se implemente el sistema de errores en las actions hay que comprobar que el error es el adecuado
    	//       por ahora, se muestra un println por pantalla, que debe ser: Not valid because room is not in works state yet.
    
    }
    
    @Test
    public void testHarvestGarbage(){
    	
    	int maxGarbage = lair.getGarbageStorageCapacity();
    	int contador = 0;
    	int maxTurns = 1000;
    	
    	//Añadir experiencia en la recolección de basura
    	monsterAdult.addExp(AttrType.HarvesterSkill, 100);
    	monsterAdult.addExp(AttrType.Strenght, 100);
    	
    	MonsterAction action = MonsterActionType.GarbageHarvest.build(monsterAdult, RoomType.Warehouse);
    	
    	monsterAdult.setFreeTurns(maxTurns);
    	
    	while (lair.getGarbage() < maxGarbage && contador < maxTurns){
    		action.execute();
    		contador++;
    	}
    	// Si llega hasta aquí es que el bucle se quedó pillado (por ejemplo porque no añade basura)
    	assertTrue(contador < maxTurns);
    	
    	// comprobamos que el monstruo gasta los turnos que debe gastar
    	assertEquals(1000 - contador, monsterAdult.getFreeTurns());
    	 
    	// comprobamos que la guarida se ha llenado de basura
    	assertEquals(lair.getGarbage(), maxGarbage);
    	
    	// comprobamos que ya no se puede recolectar más basura
    	assertFalse(action.isValid());
    	
    	
    }

    
    
}
