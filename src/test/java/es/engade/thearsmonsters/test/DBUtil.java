package es.engade.thearsmonsters.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.room.dao.RoomDao;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;


/**
 * DB Utils para tests
 */
public class DBUtil {

    private UserDao userDao;
    private LairDao lairDao;
    private MonsterDao monsterDao;
    private MonsterEggDao monsterEggDao;
    private RoomDao roomDao;
    
    /**
     * Llena de movidas la BD
     */
    public void populateDB() {
        
    }
    
    /**
     * Limpia la BD
     */
    public void clearDB() {
        
    }
    
    private void initializeDaos() {

        ClassPathXmlApplicationContext appContest = AppContext.getInstance().getAppContext();

    }
}
