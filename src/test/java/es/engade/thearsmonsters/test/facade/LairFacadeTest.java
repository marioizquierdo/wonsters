package es.engade.thearsmonsters.test.facade;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.test.AppContext;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class LairFacadeTest extends GaeTest {

    private static final int NUMBER_OF_USERS = 15;
    private static final String LOGIN = "UserLogin";
    private static final String PASSWORD = "UserPass";
    
    private static UserDao userDao;
    private static LairFacade lairFacade;
    
    private User persistentUser;
    private Lair persistentLair;
    private List<User> allPersistentUsers = new ArrayList<User>(15);

    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userDao = appContext.getBean(UserDao.class);
        lairFacade = (LairFacade) appContext.getBean("lairFacade");   
    }
    
    @Before
    public void populateDB() {

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
        
        persistentLair = persistentUser.getLair();
    }
    
    @After
    public void clearDB() {
        for (User u : allPersistentUsers)
            userDao.remove(u.getId());
    }
    
    @Test
    public void testFindLair() 
        throws InstanceNotFoundException, InternalErrorException {
        
    }
    
    @Test
    public void testFindNonExistentLair() 
    throws InstanceNotFoundException, InternalErrorException {
    
    }

    @Test
    public void testFindLairByLogin() 
        throws InstanceNotFoundException, InternalErrorException {
        
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentLairByLogin() 
    throws InstanceNotFoundException, InternalErrorException {
    
        throw new InstanceNotFoundException(null, null);
        
    }
    
    @Test
    public void testFindLairByAddress() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
    }
    
    @Test(expected = IncorrectAddressException.class)
    public void testFindLairByInvalidAddress() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        throw new IncorrectAddressException();
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentLairByAddress() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        throw new InstanceNotFoundException(null, null);
        
    }

    @Test
    public void testFindBuilding() 
        throws InternalErrorException, IncorrectAddressException {
        
    }
    
    @Test(expected = IncorrectAddressException.class)
    public void testFindBuildingInIncorrectAddress() 
        throws InternalErrorException, IncorrectAddressException {
        
        throw new IncorrectAddressException();
        
    }

    @Test
    public void testCreateNewRoom()
        throws InWorksActionException, InternalErrorException, InsuficientGarbageException {
        
    }
    
    @Test(expected = InsuficientGarbageException.class)
    public void testCreateNewRoomWithNoGarbage()
        throws InWorksActionException, InternalErrorException, InsuficientGarbageException {
        
        throw new InsuficientGarbageException(1000, 500);
        
    }
         
    @Test
    public void testSetRoomUpgradingInWorksState()
        throws InWorksActionException, InstanceNotFoundException, InsuficientGarbageException, InternalErrorException {
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testSetUnexistentRoomUpgradingInWorksState()
        throws InWorksActionException, InstanceNotFoundException, InsuficientGarbageException, InternalErrorException {
        
        throw new InstanceNotFoundException(null, null);
    }
    
    @Test(expected = InsuficientGarbageException.class)
    public void testSetRoomUpgradingInWorksStateWithNoGarbage()
        throws InWorksActionException, InstanceNotFoundException, InsuficientGarbageException, InternalErrorException {
        
        throw new InsuficientGarbageException(1000, 500);
    }
    
    @Test(expected = InWorksActionException.class)
    public void testSetRoomUpgradingInWorksStateOneMoreTime()
        throws InWorksActionException, InstanceNotFoundException, InsuficientGarbageException, InternalErrorException {
        
        throw new InWorksActionException("test");
    }
    
    @Test
    public void testCancelWorks()
        throws InWorksActionException, InternalErrorException, InstanceNotFoundException {
        
    }
    
    @Test
    public void testChangeResources()
        throws WarehouseFullStorageException, TradeOfficeFullStorageException, 
        InsuficientGarbageException, InsuficientMoneyException, 
        OnlyOneChangePerGameDayException, InternalErrorException {
        
    }

}
