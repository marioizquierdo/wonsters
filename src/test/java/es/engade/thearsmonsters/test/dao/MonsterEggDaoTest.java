package es.engade.thearsmonsters.test.dao;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.test.AppContext;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class MonsterEggDaoTest extends GaeTest {

    private User persistentUser;
    private List<MonsterEgg> persistentEggs;

    private static UserDao userDao;
    private static MonsterEggDao monsterEggDao;

    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance()
                .getAppContext();
        userDao = appContext.getBean(UserDao.class);
        monsterEggDao = appContext.getBean(MonsterEggDao.class);
    }

    @Before
    public void populateDB() {

        User user = FactoryData.UserWhoIs.Random.build();
        user.getLair().setRooms(null);

        // PERSIST

        persistentUser = userDao.save(user);
        persistentEggs = persistentUser.getLair().getMonsterEggs();
    }

    @After
    public void clearDB() {
        try {
            userDao.remove(persistentUser.getId());
        } catch (InstanceNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testFindEggsByLair() {
        List<MonsterEgg> recoveredEggs = monsterEggDao
                .findEggsByLair(persistentUser.getLair());

        assertEquals(persistentEggs, recoveredEggs);
    }
}
