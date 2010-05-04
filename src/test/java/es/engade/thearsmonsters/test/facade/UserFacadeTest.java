package es.engade.thearsmonsters.test.facade;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.userfacade.LoginResult;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.test.AppContext;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class UserFacadeTest extends GaeTest {

    private static final int NUMBER_OF_USERS = 15;
    private static final String NON_EXISTENT_LOGIN = "NonExistentUserLogin";
    private static final String LOGIN = "UserLogin";
    private static final String PASSWORD = "UserPass";
    private static final String INCORRECT_PASSWORD = "IncorrectPass";

    private static UserDao userDao;
    private static UserFacade userFacade;
    
    private User persistentUser;
    private List<User> allPersistentUsers = new ArrayList<User>(15);

    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userDao = appContext.getBean(UserDao.class);
        
    }
    
    @Before
    public void populateDB() {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userFacade = (UserFacade) appContext.getBean("userFacade");
        
        int numberOfUsers = 0;
        User user = FactoryData.UserWhoIs.Random.build();
        user.setLogin(LOGIN);
        user.setEncryptedPassword(PasswordEncrypter.crypt(PASSWORD));
        //TODO: Hasta que se elimine la herencia en Room
        user.getLair().setRooms(new ArrayList<Room>());
        
        // PERSIST

        persistentUser = userDao.save(user);
        allPersistentUsers.add(persistentUser);
        numberOfUsers++;
        
        while (numberOfUsers < NUMBER_OF_USERS) {
            allPersistentUsers.add(FactoryData.UserWhoIs.Random.build());
            allPersistentUsers.get(numberOfUsers).getLair().setRooms(new ArrayList<Room>());
            userDao.save(allPersistentUsers.get(numberOfUsers));
            numberOfUsers++;
        }
    }
    
    @After
    public void clearDB() {
        for (User u : allPersistentUsers)
            userDao.remove(u.getId());
    }
    
    @Test
    public void testRegisterUser() throws DuplicateInstanceException, 
        FullPlacesException, InternalErrorException, InstanceNotFoundException {
        
        User testUser = FactoryData.UserWhoIs.Random.build();
        String clearPasswd = "testPass";
        testUser.setEncryptedPassword(PasswordEncrypter.crypt(clearPasswd));
        userFacade.registerUser(testUser.getLogin(), clearPasswd, testUser.getUserDetails());
        
        User recoveredUser = userDao.findUserByLogin(testUser.getLogin());
        assertEquals(testUser, recoveredUser);
    }
    
    @Test(expected=DuplicateInstanceException.class)
    public void testRegisterDuplicatedUser() throws DuplicateInstanceException, 
        FullPlacesException, InternalErrorException {
        
        userFacade.registerUser(persistentUser.getLogin(), "psw", persistentUser.getUserDetails());
    }
    
    @Test
    public void testFindByLogin() throws InstanceNotFoundException {
        
        User recoveredUser = userDao.findUserByLogin(persistentUser.getLogin());
        
        assertEquals(persistentUser, recoveredUser);
        
    }
    
    @Test(expected=InstanceNotFoundException.class)
    public void testFindNonExistentUserByLogin() throws InstanceNotFoundException {
        
        userDao.findUserByLogin(NON_EXISTENT_LOGIN);

    }
    
    @Test
    public void testLoginClear() throws InstanceNotFoundException, 
    IncorrectPasswordException, InternalErrorException {
        
        LoginResult loginResult = userFacade.login(LOGIN, PASSWORD, false, false);
        assertEquals(persistentUser.getUserDetails().getFirstName(), loginResult.getFirstName());
        assertEquals(persistentUser.getLair(), loginResult.getLair());
        
    }
    
    @Test
    public void testLoginEncrypted() throws InstanceNotFoundException, 
    IncorrectPasswordException, InternalErrorException {
        
        LoginResult loginResult = userFacade.login(persistentUser.getLogin(), persistentUser.getEncryptedPassword(), true, false);
        assertEquals(persistentUser.getUserDetails().getFirstName(), loginResult.getFirstName());
        assertEquals(persistentUser.getLair(), loginResult.getLair());
  
    }
    
    @Test(expected=IncorrectPasswordException.class)
    public void testLoginFailPassword() throws InstanceNotFoundException, 
    IncorrectPasswordException, InternalErrorException {
        
        userFacade.login(LOGIN, INCORRECT_PASSWORD, false, false);
        
    }
    
    @Test(expected=InstanceNotFoundException.class)
    public void testLoginFailUser() throws InstanceNotFoundException, 
    IncorrectPasswordException, InternalErrorException {
        
        userFacade.login(NON_EXISTENT_LOGIN, INCORRECT_PASSWORD, false, false);
        
    }
    
    @Test
    public void testRemoveUserProfile() 
        throws InstanceNotFoundException, InternalErrorException {
        
        String LOGIN_TO_REMOVE = "loginToRemove";
        
        User user = FactoryData.UserWhoIs.Random.build();
        user.setLogin(LOGIN_TO_REMOVE);
        user.getLair().setRooms(new ArrayList<Room>());
        
        // PERSIST

        user = userDao.save(user);
        
        userFacade.removeUserProfile(LOGIN_TO_REMOVE);
        
        try {
            userDao.findUserByLogin(LOGIN_TO_REMOVE);
            assert(false);
        } catch (InstanceNotFoundException e) {
            assert(true);
        }
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testRemoveUnexistentUserProfile() 
        throws InstanceNotFoundException, InternalErrorException {
        
        userFacade.removeUserProfile(NON_EXISTENT_LOGIN);
        
    }

    @Test
    public void testUpdateUserProfileAfterLogin() throws Throwable {
        
        userFacade.login(LOGIN, PASSWORD, false, false);
        User userBeforeUpdate = userFacade.findUserProfile(LOGIN);
        
        UserDetails details = new UserDetails("updatedName", "updateSurname",
                "updatedEmail", "sp");
        
        assert(!userBeforeUpdate.getUserDetails().equals(details));
        
        userFacade.updateUserProfileDetails(LOGIN, details);
        
        User userAfterUpdate = userFacade.findUserProfile(LOGIN);
        assertEquals(details, userAfterUpdate.getUserDetails());

    }
}
