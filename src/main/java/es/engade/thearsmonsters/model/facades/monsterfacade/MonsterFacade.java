package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.io.Serializable;
import java.util.List;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public interface MonsterFacade extends Serializable {

    public MonsterEgg buyEgg(MonsterRace race, Lair lair)
        throws InternalErrorException, InsuficientMoneyException, MaxEggsException;

    public List<MonsterEgg> findEggs(Lair lair)
        throws InternalErrorException;
        
    /**
     * Shell an existing egg from the given lair.
     * @return the money gained in the shell.
     */
    public Integer shellEgg(long eggId, Lair lair) 
    	throws InternalErrorException, InstanceNotFoundException;
    
    public void incubateEgg(long eggId, Lair lair)
		throws InternalErrorException, InstanceNotFoundException, InsuficientVitalSpaceException;
    
    public Monster bornMonster(long eggId, String monsterName, Lair lair)
    	throws InternalErrorException, InstanceNotFoundException, MonsterGrowException, InsuficientVitalSpaceException;
    
    public Monster metamorphosisToAdult(long monsterId, Lair lair)
		throws InternalErrorException, InstanceNotFoundException, MonsterGrowException;
    
    public List<Monster> findLairMonsters(Lair lair)
    	throws InternalErrorException;
    
    public Monster findMonster(long monsterId)
    	throws InternalErrorException, InstanceNotFoundException;
}
