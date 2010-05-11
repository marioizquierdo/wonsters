package es.engade.thearsmonsters.test.facade;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.util.GameConf;
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
    private static MonsterEggDao monsterEggDao;
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
        monsterEggDao = appContext.getBean(MonsterEggDao.class);
        monsterFacade = (MonsterFacade) appContext.getBean("monsterFacade");
    }
    
    @Before
    public void populateDB() {
        
        int numberOfUsers = 0;
        User user = FactoryData.UserWhoIs.Random.build();
        user.setLogin(LOGIN);
        
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
        for (User u : allPersistentUsers) {
            try {
                userDao.remove(u.getId());
            } catch (InstanceNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    @Test
    public void testBuyEgg()
    throws InternalErrorException, 
        InsuficientMoneyException, MaxEggsException {
        
        Lair lair = persistentUser.getLair();
        MonsterRace race = MonsterRace.Lipendula;
        
        int initialMoney = lair.getMoney();
        int initialNumberOfEggs = lair.getMonsterEggs().size();
        
        MonsterEgg egg = monsterFacade.buyEgg(lair, race);
        
        assertEquals(initialMoney - race.getBuyEggPrice(), lair.getMoney());
        assertEquals(initialNumberOfEggs + 1, lair.getMonsterEggs().size());
        assertEquals(race, lair.getMonsterEggs().get(
                lair.getMonsterEggs().size() -1).getRace()
                );
        assert(lair.getMonsterEggs().contains(egg));
    }
    
    @Test(expected=InsuficientMoneyException.class)
    public void testBuyEggWithoutMoney()
    throws InternalErrorException, 
        InsuficientMoneyException, MaxEggsException {
        
        Lair lair = persistentUser.getLair();
        MonsterRace race = MonsterRace.Electroserpe;
        
        monsterFacade.buyEgg(lair, race);
        
    }
    
    @Test(expected=MaxEggsException.class)
    public void testBuyEggOverflow()
    throws InternalErrorException, 
        InsuficientMoneyException, MaxEggsException {
        
        Lair lair = persistentUser.getLair();
        MonsterRace race = MonsterRace.Bu;
        
        for (int i = 0; i < GameConf.getMaxEggs(); i++) {
            monsterFacade.buyEgg(lair, race);
        }
    }

    @Test
    public void testFindEggs()
    throws InternalErrorException {
        
        Lair lair = persistentUser.getLair();
        int numberOfEggs = lair.getMonsterEggs().size();
        
        List<MonsterEgg> eggs = monsterFacade.findEggs(lair);
        
        assertEquals(numberOfEggs, eggs.size());
        for (MonsterEgg egg : eggs) {
            assert(lair.getMonsterEggs().contains(egg));
        }
        
    }

    @Test
    public void testShellEgg() 
    throws InternalErrorException, InstanceNotFoundException {
    
        Lair lair = persistentUser.getLair();
        
        int initialNumberOfEggs = lair.getMonsterEggs().size();
        int initialMoney = lair.getMoney();
        
        String eggId = KeyUtils.toString(lair.getMonsterEggs().get(0).getId());
        
        int sellPrice = monsterFacade.shellEgg(lair, eggId);
        
        assertEquals(initialNumberOfEggs - 1, lair.getMonsterEggs().size());
        assertEquals(initialMoney + sellPrice, lair.getMoney());
        
    }
    
    @Test
    public void testShellEggWithMoreChicha() 
    throws InternalErrorException, InstanceNotFoundException, 
        InsuficientMoneyException, MaxEggsException {
    
        Lair lair = persistentUser.getLair();
        MonsterRace race = MonsterRace.Lipendula;

        MonsterEgg egg = monsterFacade.buyEgg(lair, race);
        
        int initialNumberOfEggs = lair.getMonsterEggs().size();
        int initialMoney = lair.getMoney();
        
        String eggId = KeyUtils.toString(egg.getId());
        
        int sellPrice = monsterFacade.shellEgg(lair, eggId);
        
        assertEquals(initialNumberOfEggs - 1, lair.getMonsterEggs().size());
        assertEquals(initialMoney + sellPrice, lair.getMoney());
        
    }
    
    @Test(expected=InstanceNotFoundException.class)
    public void testShellEggFromOtherUser() 
    throws InternalErrorException, InstanceNotFoundException {
    
        Lair lair = persistentUser.getLair();
        Lair otherLair = allPersistentUsers.get(NUMBER_OF_USERS-1).getLair();
        assert(!lair.equals(otherLair));
        
        String eggId = KeyUtils.toString(otherLair.getMonsterEggs().get(0).getId());
        
        monsterFacade.shellEgg(lair, eggId);
        
    }

    @Test
    public void testIncubateEgg()
    throws InternalErrorException, 
        InstanceNotFoundException, InsuficientVitalSpaceException {
        
        Lair lair = persistentUser.getLair();
        MonsterEgg egg = lair.getMonsterEggs().get(0);
        
        assert (!egg.isIncubated());
        assert (egg.getBorningDate() == null);
        
        monsterFacade.incubateEgg(lair, KeyUtils.toString(egg.getId()));
        
        assert (!egg.isIncubated());
        assert(egg.getBorningDate() != null);
        assert(monsterFacade.findEggs(lair).contains(egg));
    }

    @Test(expected=InstanceNotFoundException.class)
    public void testIncubateEggFromOtherLair()
    throws InternalErrorException, 
        InstanceNotFoundException, InsuficientVitalSpaceException {
        
        Lair lair = persistentUser.getLair();
        Lair otherLair = allPersistentUsers.get(NUMBER_OF_USERS-1).getLair();
        assert(!lair.equals(otherLair));
        MonsterEgg egg = otherLair.getMonsterEggs().get(0);
        
        monsterFacade.incubateEgg(lair, KeyUtils.toString(egg.getId()));
        
    }
    
    @Test(expected=InsuficientVitalSpaceException.class)
    public void testIncubateEggOverflow()
    throws InternalErrorException, 
        InstanceNotFoundException, InsuficientVitalSpaceException {
        
        Lair lair = persistentUser.getLair();
        MonsterEgg egg = lair.getMonsterEggs().get(0);
        
        while(lair.getVitalSpaceFree() > egg.getRace().getVitalSpace()) {
            lair.addMonster(FactoryData.MonsterWhoIs.Child.build());
        }
        userDao.update(persistentUser);
        lair.setVitalSpaceOccupied();
        
        monsterFacade.incubateEgg(lair, KeyUtils.toString(egg.getId()));
        
    }
    
    @Test
    public void testBornMonster()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException, InsuficientVitalSpaceException {
        
        Lair lair = persistentUser.getLair();
        MonsterEgg egg = lair.getMonsterEggs().get(0);
        
        Date date = new Date();
        date.setTime(date.getTime() - (1000*60));
        egg.setBorningDate(date);
        userDao.update(persistentUser);

        int initialNumberOfEggs = lair.getMonsterEggs().size();
        int initialNumberOfMonsters = lair.getMonsters().size();
        int initialVitalSpaceOccupied = lair.getVitalSpaceOccupied();
        
        Monster monster = monsterFacade.bornMonster(lair, KeyUtils.toString(egg.getId()), "TestName");
        
        assert(lair.getMonsters().contains(monster));
        assertEquals(initialNumberOfEggs - 1, lair.getMonsterEggs().size());
        assertEquals(initialNumberOfMonsters + 1, lair.getMonsters().size());
        assertEquals(
                initialVitalSpaceOccupied + monster.getRace().getVitalSpace(), 
                lair.getVitalSpaceOccupied()
                );
        
    }

    @Test(expected=MonsterGrowException.class)
    public void testBornMonsterPrematurely()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException, InsuficientVitalSpaceException {
        
        Lair lair = persistentUser.getLair();
        MonsterEgg egg = lair.getMonsterEggs().get(0);
    
        monsterFacade.bornMonster(lair, KeyUtils.toString(egg.getId()), "TestName");

    }
    
    @Test(expected=InsuficientVitalSpaceException.class)
    public void testBornMonsterOverflow()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException, InsuficientVitalSpaceException {
        
        Lair lair = persistentUser.getLair();
        MonsterEgg egg = lair.getMonsterEggs().get(0);
        
        Date date = new Date();
        date.setTime(date.getTime() - (1000*60));
        egg.setBorningDate(date);
        userDao.update(persistentUser);
        
        while(lair.getVitalSpaceFree() > egg.getRace().getVitalSpace()) {
            lair.addMonster(FactoryData.MonsterWhoIs.Child.build());
        }
        userDao.update(persistentUser);
        lair.setVitalSpaceOccupied();
    
        monsterFacade.bornMonster(lair, KeyUtils.toString(egg.getId()), "TestName");

    }
    
    @Test
    public void testMetamorphosisToAdult()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException {
        
        assertEquals(persistentChildMonster.getAge(), MonsterAge.Child);
        
        String monsterId = KeyUtils.toString(persistentChildMonster.getId());
        Monster recoveredMonster = monsterFacade.metamorphosisToAdult(persistentUser.getLair(), monsterId);
        
        //TODO de momento falla pq no está implementado metamorphosisToAdult
        assertEquals(MonsterAge.Adult, recoveredMonster.getAge());
        
    }

    @Test(expected = MonsterGrowException.class)
    public void testMetamorphosisToAdultAgeFail()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException {
        
        assert(!persistentAdultMonster.getAge().equals(MonsterAge.Child));
        
        String monsterId = KeyUtils.toString(persistentAdultMonster.getId());
        monsterFacade.metamorphosisToAdult(persistentUser.getLair(), monsterId);
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testMetamorphosisToAdultLairFail()
    throws InternalErrorException, InstanceNotFoundException, 
    MonsterGrowException {
        
        assert(persistentChildMonster.getAge().equals(MonsterAge.Child));
        
        String monsterId = KeyUtils.toString(persistentChildMonster.getId());
        
        monsterFacade.metamorphosisToAdult(anotherPersistentUser.getLair(), monsterId);
        
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
        
        Monster monster = monsterFacade.findMonster(null, KeyUtils.toString(persistentChildMonster.getId()));
        
        assertEquals(persistentChildMonster, monster);
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testFindMonsterFail()
    throws InternalErrorException, InstanceNotFoundException {
        
        monsterFacade.findMonster(null, UNEXISTENT_MONSTER_ID);
        
    }
}
