package es.engade.thearsmonsters.test.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.test.AppContext;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class UserDaoTest extends GaeTest {

    private static final int NUMBER_OF_USERS = 15;
    private static final String NON_EXISTENT_LOGIN = "NonExistentUserLogin";

    private static UserDao userDao;
    
    private User persistentUser;
    private List<User> allPersistentUsers = new ArrayList<User>(15);

    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userDao = appContext.getBean(UserDao.class);
    }
    
    @Before
    public void populateDB() {
        
        int numberOfUsers = 0;
        User user = FactoryData.UserWhoIs.Random.build();
        //TODO: Hasta que se elimine la herencia en Room
        user.getLair().setRooms(null);
        
        // PERSIST

        persistentUser = userDao.save(user);
        allPersistentUsers.add(persistentUser);
        numberOfUsers++;
        
        while (numberOfUsers < NUMBER_OF_USERS) {
            allPersistentUsers.add(FactoryData.UserWhoIs.Random.build());
            allPersistentUsers.get(numberOfUsers).getLair().setRooms(null);
            userDao.save(allPersistentUsers.get(numberOfUsers));
            numberOfUsers++;
        }
    }
    
    @After
    public void clearDB() {
        for (User u : allPersistentUsers) {
            try {
                userDao.remove(u.getIdKey());
            } catch (InstanceNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    @Test
    public void testFindByLogin() throws InstanceNotFoundException {
        
        User recoveredUser = userDao.findUserByLogin(persistentUser.getLogin());
        
        assertEquals(persistentUser, recoveredUser);
        
    }
    
    @Test(expected=InstanceNotFoundException.class)
    public void testFindNonExistentUserByLogin() throws InstanceNotFoundException {
        
        User recoveredUser = userDao.findUserByLogin(NON_EXISTENT_LOGIN);

        System.out.println("USER -> "+ recoveredUser);
    }
    
    @Test
    public void testGetNumberOfUsers() {
        
        int numberOfUsers = userDao.getNumberOfUsers();
        assertEquals(NUMBER_OF_USERS, numberOfUsers);
        
    }
}
