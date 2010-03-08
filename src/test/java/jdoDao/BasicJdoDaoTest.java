package jdoDao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.labs.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.ApiProxyLocal;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.apphosting.api.ApiProxy;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.RoomData;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;

public class BasicJdoDaoTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalTaskQueueTestConfig());

    private static MonsterEggDao monsterEggDao;
    private static LairDao lairDao;
    private static UserDao userDao;
    private static MonsterDao monsterDao;
//    private static RoomDao roomDao;
    
    static {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[] {"applicationContext.xml"});
        monsterEggDao = appContext.getBean(MonsterEggDao.class);
        lairDao = appContext.getBean(LairDao.class);
        userDao = appContext.getBean(UserDao.class);
        monsterDao = appContext.getBean(MonsterDao.class);
//        roomDao = appContext.getBean(RoomDao.class);
    }
    
    @Before
    public void setUp() {
        helper.setUp();
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        ApiProxy.setDelegate(LocalServiceTestHelper.getApiProxyLocal());
        ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
    }

    @After
    public void tearDown() {
        ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
        datastoreService.clearProfiles();
        helper.tearDown();
    }


    // Run this test twice to demonstrate we're not leaking state across tests.
    // If we _are_ leaking state across tests we'll get an exception on the
    // second test because there will already be a task with the given name.
    private void doTest() throws InterruptedException {
        QueueFactory.getDefaultQueue().add(TaskOptions.Builder.taskName("task29"));
        // give the task time to execute if tasks are actually enabled (which they
        // aren't, but that's part of the test)
        Thread.sleep(1000);
        LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
        QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
        assertEquals(1, qsi.getTaskInfo().size());
        assertEquals("task29", qsi.getTaskInfo().get(0).getTaskName());
    }

    @Test
    public void testTaskGetsScheduled1() throws InterruptedException {
        doTest();
    }

    @Test
    public void testTaskGetsScheduled2() throws InterruptedException {
        doTest();
    }

    @Test
    public void testSaveAndRecover() {
        
//      ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
//      new String[] {"applicationContext.xml"});
//      monsterEggDao = appContext.getBean(MonsterEggDao.class);

        User user = new User("user", "qeoqwdw", 
                new UserDetails("nome", "apldo", "a@b.c", "spanish"));
        
        Lair lair = new Lair(
                user, 4, 4, 4, new RoomData(), new Address(5,6,7));
        
        MonsterEgg egg = new MonsterEgg(lair, MonsterRace.Bu, new Date());//, new Monster());
        Monster monster = new Monster(lair, MonsterRace.Electroserpe, "testMonster", new Date(), new Date(), MonsterAge.Adult);
        lair.addMonsterEgg(egg);
        lair.addMonster(monster);
        
//        Room eyeOfLife = new EyeOfTheLife(lair);
        
        // PERSIST

        Key userKey = userDao.save(user).getId();
        Key lairId = lairDao.save(lair).getId();
        user.setLair(lair);

//        Key roomKey = roomDao.save(eyeOfLife).getId();
        
        Key eggId = monsterEggDao.save(egg).getId();
        Key monsterId = monsterDao.save(monster).getId();
        
        // RECOVER
        
        User userRecovered = userDao.get(userKey);
        System.out.println("Sav User " + user);
        userRecovered.getUserDetails();
        System.out.println("Rec User " + userRecovered + " ---> " + userKey );
        assertEquals(user, userRecovered);
        
        Lair lairRecovered = lairDao.get(lairId);
        System.out.println("Sav Lair " + lair);
        System.out.println("Rec Lair " + lairRecovered + " ---> " + lairId);
        assertEquals(lair, lairRecovered);

        MonsterEgg eggRecovered = monsterEggDao.get(eggId);
        System.out.println("Sav Egg " + egg);
        System.out.println("Rec Egg " + eggRecovered + " ---> " + eggId);
        assertEquals(egg, eggRecovered);
        
        Monster monsterRecovered = monsterDao.get(monsterId);
        System.out.println("Sav Monster " + monster);
        System.out.println("Rec Monster " + monsterRecovered + " ---> " + monsterId);
        assertEquals(monster, monsterRecovered);
        
//        System.out.println("ROOM " + roomKey + " ---> " + eyeOfLife);
    }

}

