package es.engade.thearsmonsters.test.facade;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.test.AppContext;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class MonsterFacadeTest extends GaeTest {

    private static final int NUMBER_OF_USERS = 3;
    private static final String LOGIN = "UserLogin";
    private static final String UNEXISTENT_MONSTER_ID = "NO!";

    private static UserDao userDao;
    private static MonsterDao monsterDao;
    private static MonsterFacade monsterFacade;
    
    private User persistentUser;
    private User anotherPersistentUser;
    private List<User> allPersistentUsers = new ArrayList<User>(NUMBER_OF_USERS);
    private List<Monster> persistentUserMonsters = new ArrayList<Monster>();
    private Monster persistentChildMonster, persistentAdultMonster, persistentOldMonster;
    
    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userDao = appContext.getBean(UserDao.class);
        monsterDao = appContext.getBean(MonsterDao.class);
        monsterFacade = (MonsterFacade) appContext.getBean("monsterFacade");
    }
    
    @Before
    public void populateDB() {
        
        int numberOfUsers = 0;
        User user = FactoryData.UserWhoIs.Random.build();
        user.setLogin(LOGIN);
        //TODO: Hasta que se elimine la herencia en Room
        user.getLair().setRooms(null);
        
        // PERSIST

        persistentUser = userDao.save(user);
        allPersistentUsers.add(persistentUser);
        numberOfUsers++;
        
        while (numberOfUsers < NUMBER_OF_USERS) {
            anotherPersistentUser = FactoryData.UserWhoIs.Random.build();
            anotherPersistentUser.getLair().setRooms(null);
            allPersistentUsers.add(anotherPersistentUser);
            userDao.save(allPersistentUsers.get(numberOfUsers));
            numberOfUsers++;
        }
        
        persistentUserMonsters = persistentUser.getLair().getMonsters();
        
        if (persistentUserMonsters.size() < 3)
            throw new RuntimeException("Test requires at least 3 persistent monsters");
        
        persistentChildMonster = persistentUserMonsters.get(0);
        if (persistentChildMonster.getAge() != MonsterAge.Child) {
            persistentChildMonster.setAge(MonsterAge.Child);
            monsterDao.update(persistentChildMonster);
        }
        persistentAdultMonster = persistentUserMonsters.get(1);
        if (persistentAdultMonster.getAge() != MonsterAge.Adult) {
            persistentAdultMonster.setAge(MonsterAge.Adult);
            monsterDao.update(persistentAdultMonster);
        }
        persistentOldMonster = persistentUserMonsters.get(2);
        if (persistentOldMonster.getAge() != MonsterAge.Old) {
            persistentOldMonster.setAge(MonsterAge.Old);
            monsterDao.update(persistentOldMonster);
        }
    }
    
    @After
    public void clearDB() {
        for (User u : allPersistentUsers)
            userDao.remove(u.getId());
    }
    
    @Test
    public void testBuyEgg()
    throws InternalErrorException, InsuficientMoneyException, MaxEggsException {
        
    }

    @Test
    public void testFindEggs()
    throws InternalErrorException {
        
    }

    @Test
    public void testShellEgg() 
    throws InternalErrorException, InstanceNotFoundException {
    
    }

    @Test
    public void testIncubateEgg()
    throws InternalErrorException, InstanceNotFoundException, InsuficientVitalSpaceException {
        
    }

    @Test
    public void testBornMonster()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException, InsuficientVitalSpaceException {
        
    }

    @Test
    public void testMetamorphosisToAdult()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException {
        
        assertEquals(persistentChildMonster.getAge(), MonsterAge.Child);
        
        String monsterId = KeyUtils.toString(persistentChildMonster.getId());
        Monster recoveredMonster = monsterFacade.metamorphosisToAdult(monsterId, persistentUser.getLair());
        
        //TODO de momento falla pq no está implementado metamorphosisToAdult
        assertEquals(MonsterAge.Adult, recoveredMonster.getAge());
        
    }

    @Test(expected = MonsterGrowException.class)
    public void testMetamorphosisToAdultAgeFail()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException {
        
        assert(!persistentAdultMonster.getAge().equals(MonsterAge.Child));
        
        String monsterId = KeyUtils.toString(persistentAdultMonster.getId());
        monsterFacade.metamorphosisToAdult(monsterId, persistentUser.getLair());
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testMetamorphosisToAdultLairFail()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException {
        
        assert(persistentChildMonster.getAge().equals(MonsterAge.Child));
        
        String monsterId = KeyUtils.toString(persistentChildMonster.getId());
        
        monsterFacade.metamorphosisToAdult(monsterId, anotherPersistentUser.getLair());
        
    }
    
    @Test
    public void testFindLairMonsters()
    throws InternalErrorException {
        
        List<Monster> lairMonsters = monsterFacade.findLairMonsters(persistentUser.getLair());
        
        assertEquals(persistentUserMonsters, lairMonsters);
        
    }

    @Test
    public void testFindMonster()
    throws InternalErrorException, InstanceNotFoundException {
        
        Monster monster = monsterFacade.findMonster(KeyUtils.toString(persistentChildMonster.getId()));
        
        assertEquals(persistentChildMonster, monster);
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testFindMonsterFail()
    throws InternalErrorException, InstanceNotFoundException {
        
        monsterFacade.findMonster(UNEXISTENT_MONSTER_ID);
        
    }
}
