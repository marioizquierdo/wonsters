package es.engade.thearsmonsters.test.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.labs.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

import es.engade.thearsmonsters.model.entities.common.dao.exception.EntityNotFoundException;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.test.util.FactoryData;

public class BasicJdoDaoTest extends GaeTest{

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
    
    @Test
    public void testTaskGetsScheduled1() throws InterruptedException {
        doTest();
    }

    @Test
    public void testTaskGetsScheduled2() throws InterruptedException {
        doTest();
    }

    @Test
    public void testSaveOnCascade() {
        
        User user = FactoryData.generateRandUser();
        //TODO: Hasta que se elimine la herencia en Room
        user.getLair().setRooms(null);
        
        // PERSIST

        User persistentUser = userDao.save(user);
        Key userKey = persistentUser.getId();
        
        // RECOVER

        
        User userRecovered = userDao.get(userKey);
        userRecovered.getUserDetails();
        
        assertEquals(user, userRecovered);
        
        // Report
//        System.out.println("USER KEY ----> " +userKey);
//        System.out.println("Sav User " + user);
//        System.out.println("Rec User " + userRecovered + " ---> " + userKey );
//        System.out.println("Lair " + user.getLair().getId() + " ---> " + user.getLair());
//        System.out.println("Bichos " + user.getLair().getMonsters());
//        System.out.println("Bicho 0 " + user.getLair().getMonsters().get(0).getId());
//        System.out.println("Huevos " + user.getLair().getMonsterEggs());
//        System.out.println("Huevo 0 " + user.getLair().getMonsterEggs().get(0).getId());
        
    }
    
    @Test
    public void testUpdate() {
    
        UserDetails oldUD = new UserDetails("nome", "apldo", "a@b.c", "spanish");
        UserDetails newUD = new UserDetails("Antonio", "Testuser", "c@b.a", "turkish");
        User user = new User("user", "qeoqwdw", 
                oldUD);//TODO: Hasta que se elimine la herencia en Room
        Key userKey = userDao.save(user).getId();

        User userRecovered = userDao.get(userKey);
        assertEquals(user, userRecovered);
        
        userRecovered.setUserDetails(newUD);
        User updatedUser = userDao.update(userRecovered);
        
        assertEquals(userKey, updatedUser.getId());
        
        User updatedUserRecovered = userDao.get(updatedUser.getId());
        
        assertEquals(newUD, updatedUserRecovered.getUserDetails());

    }
    
    @Test
    public void testRemoveSingleInstance() {
        
        // persist
        
        User user = new User("user", "qeoqwdw", 
                new UserDetails("nome", "apldo", "a@b.c", "spanish"));
        Key userKey = userDao.save(user).getId();

        User userRecovered = userDao.get(userKey);
        assertEquals(user, userRecovered);
        
        // delete
        
        userDao.remove(userKey);
        
        try {
            userDao.get(userKey);
            assert(false);
        } catch (EntityNotFoundException e) {
            assert(true);
        }

        System.out.println(userRecovered.getId());
        
    }
    
    @Test
    public void testRemoveOnCascade() {
        
        User user = FactoryData.generateRandUser();
      //TODO: Hasta que se elimine la herencia en Room
        user.getLair().setRooms(null);
        
        userDao.save(user);
        Key userKey = user.getId();
        
        // Testing structure persistency
        Monster monster = user.getLair().getMonsters().get(0);
        Key monsterKey = monster.getId();
        Monster retMonster = monsterDao.get(monsterKey);
        assertEquals(monster, retMonster);
        
        // Removing user
        userDao.remove(userKey);
        try {
            userDao.get(userKey);
            assert(false);
        } catch (EntityNotFoundException e) {
            assert(true);
        }
        
        // Testing full structure deletion on cascade
        try {
            monsterDao.get(monsterKey);
            assert(false);
        } catch (EntityNotFoundException e) {
            assert(true);
        }
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
}

