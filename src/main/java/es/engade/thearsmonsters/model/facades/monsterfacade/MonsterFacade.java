package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public interface MonsterFacade {

    public MonsterEgg buyEgg(Lair lair, MonsterRace race)
        throws InternalErrorException, InsuficientMoneyException, MaxEggsException;

    public List<MonsterEgg> findEggs(Lair lair)
        throws InternalErrorException;
        
    /**
     * Sell an existing egg from the given lair.
     * @return the money gained in the sell.
     */
    public Integer sellEgg(Lair lair, String eggId) 
    	throws InternalErrorException, InstanceNotFoundException;
    
    public void incubateEgg(Lair lair, String eggId)
		throws InternalErrorException, InstanceNotFoundException, InsuficientVitalSpaceException;
    
    public Monster bornMonster(Lair lair, String eggId, String monsterName)
    	throws InternalErrorException, InstanceNotFoundException, MonsterGrowException, InsuficientVitalSpaceException;
    
    public Monster metamorphosisToAdult(Lair lair, String monsterId)
		throws InternalErrorException, InstanceNotFoundException, MonsterGrowException;
    
    public List<Monster> findLairMonsters(Lair lair)
    	throws InternalErrorException;
    
    public Monster findMonster(Lair lair, String monsterId)
    	throws InternalErrorException, InstanceNotFoundException;
    
    /**
     * Sugerir acciones que puede hacer el monstruo en su propia guarida
     * @param lair TODO
     */
    public List<MonsterActionSuggestion> suggestMonsterActions(Lair lair, String monsterId) 
    	throws InstanceNotFoundException;
    
    /**
     * Ejecutar una acción de un monstruo en una sala de su propia guarida
     * TODO: Hay que implementar un caso de uso más eficiente, que permita ejecutar varias
     * acciones y que cada una de ellas pueda consumir varios turnos, y que al final guarde los cambios en la base
     * de datos tan solo una vez.
     * @param lair TODO
     */
    public boolean executeMonsterAction(Lair lair, MonsterActionType monsterActionType, Key monsterId, RoomType roomType) 
    	throws InstanceNotFoundException;

}

