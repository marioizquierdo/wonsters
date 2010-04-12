package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.io.Serializable;
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
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public interface MonsterFacade {

    public MonsterEgg buyEgg(MonsterRace race, Lair lair)
        throws InternalErrorException, InsuficientMoneyException, MaxEggsException;

    public List<MonsterEgg> findEggs(Lair lair)
        throws InternalErrorException;
        
    /**
     * Shell an existing egg from the given lair.
     * @return the money gained in the shell.
     */
    public Integer shellEgg(String eggId, Lair lair) 
    	throws InternalErrorException, InstanceNotFoundException;
    
    public void incubateEgg(String eggId, Lair lair)
		throws InternalErrorException, InstanceNotFoundException, InsuficientVitalSpaceException;
    
    public Monster bornMonster(String eggId, String monsterName, Lair lair)
    	throws InternalErrorException, InstanceNotFoundException, MonsterGrowException, InsuficientVitalSpaceException;
    
    public Monster metamorphosisToAdult(String monsterId, Lair lair)
		throws InternalErrorException, InstanceNotFoundException, MonsterGrowException;
    
    public List<Monster> findLairMonsters(Lair lair)
    	throws InternalErrorException;
    
    public Monster findMonster(String monsterId)
    	throws InternalErrorException, InstanceNotFoundException;
    
    /**
     * Sugerir acciones que puede hacer el monstruo en su propia guarida
     */
    public List<MonsterAction> suggestMonsterActions(Key monsterId) 
    	throws InstanceNotFoundException;
    
    /**
     * Ejecutar una acción de un monstruo en una sala de su propia guarida
     * TODO: Hay que implementar un caso de uso más eficiente, que permita ejecutar varias
     * acciones y que cada una de ellas pueda consumir varios turnos, y que al final guarde los cambios en la base
     * de datos tan solo una vez.
     */
    public boolean executeMonsterAction(MonsterActionType monsterActionType, Key monsterId, RoomType roomType) 
    	throws InstanceNotFoundException;

}

