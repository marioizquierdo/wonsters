package es.engade.thearsmonsters.test.facade;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.lairfacade.LairRankingInfoChunk;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.LairInfo;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.model.util.GameConf;
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
    private static LairDao lairDao;
    private static LairFacade lairFacade;
    
    private User persistentUser;
    private Lair persistentLair;
    private List<User> allPersistentUsers = new ArrayList<User>(15);

    // address
    int street, building, floor;
    
    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userDao = appContext.getBean(UserDao.class);
        lairDao = appContext.getBean(LairDao.class);
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
        
        int newStreet = 1, newBuilding = 1, newFloor = 2;
        int garbage, money;
        while (numberOfUsers < NUMBER_OF_USERS) {
        	garbage = Integer.parseInt(
        			Math.round(
        					Math.random() * 4000) + "");
        	money = Integer.parseInt(
        			Math.round(
        					Math.random() * 4000) + "");
            allPersistentUsers.add(FactoryData.UserWhoIs.Random.build());
            allPersistentUsers.get(numberOfUsers).getLair().setRooms(new ArrayList<Room>());
            User newUser = allPersistentUsers.get(numberOfUsers);
            newUser.getLair().setAddressStreet(newStreet);
            newUser.getLair().setAddressBuilding(newBuilding);
            newUser.getLair().setAddressFloor(newFloor);
            newUser.getLair().setGarbage(garbage);
            newUser.getLair().setMoney(money);
            newFloor++;
            if (newFloor >= GameConf.getMaxNumberOfFloors()) {
            	newFloor = 1;
            	newBuilding++;
            	if (newBuilding >= GameConf.getMaxNumberOfBuildings()) {
            		newBuilding = 1;
            		newStreet++;
            	}
            }
            userDao.save(newUser);
            numberOfUsers++;
        }
        
        persistentLair = persistentUser.getLair();
        
        // address
        street = persistentLair.getAddressStreet();
        building = persistentLair.getAddressBuilding();
        floor = persistentLair.getAddressFloor();
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
    public void testFindLair() 
        throws InstanceNotFoundException, InternalErrorException {
        
        Lair lair = lairFacade.findLair(KeyUtils.toString(persistentLair.getIdKey()));
        assertEquals(persistentLair, lair);
    }
    
    @Test(expected=InstanceNotFoundException.class)
    public void testFindNonExistentLair() 
    throws InstanceNotFoundException, InternalErrorException {
    
        User userToDelete = allPersistentUsers.get(NUMBER_OF_USERS - 1);
        lairDao.remove(userToDelete.getLair().getIdKey());
        Lair lair = lairFacade.findLair(KeyUtils.toString(userToDelete.getLair().getIdKey()));
        System.out.println("GOT " + lair);
        
    }

    @Test
    public void testFindLairByLogin() 
        throws InstanceNotFoundException, InternalErrorException {
        
        String login = persistentUser.getLogin();
        
        Lair lair = lairFacade.findLairByLogin(login);
        assertEquals(persistentLair, lair);
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentLairByLogin() 
    throws InstanceNotFoundException, InternalErrorException {
    
        String login = "NON_EXISTENT_LOGIN";
        
        lairFacade.findLairByLogin(login);
        
    }
    
    @Test
    public void testFindLairByAddress() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        Lair lair = lairFacade.findLairByAddress(street, building, floor);
        assertEquals(persistentLair, lair);
    }
    
    @Test(expected = IncorrectAddressException.class)
    public void testFindLairByInvalidAddressFloor() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        lairFacade.findLairByAddress(street, building, GameConf.getMaxNumberOfFloors() + 1);
        
    }
    
    @Test(expected = IncorrectAddressException.class)
    public void testFindLairByInvalidAddressBuilding() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        lairFacade.findLairByAddress(street, GameConf.getMaxNumberOfBuildings() + 1, floor);
        
    }

    @Test(expected = IncorrectAddressException.class)
    public void testFindLairByInvalidAddressStreet() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        lairFacade.findLairByAddress(GameConf.getMaxNumberOfStreets() + 1, building, floor);
        
    }
    
    @Test(expected = IncorrectAddressException.class)
    public void testFindLairByNegativeAddress() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        lairFacade.findLairByAddress(-1, building, floor);
        
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentLairByAddress() 
        throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
        
        lairFacade.findLairByAddress(
                GameConf.getMaxNumberOfStreets()-1,
                GameConf.getMaxNumberOfBuildings()-1,
                GameConf.getMaxNumberOfFloors()-1);
        
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
        InsuficientGarbageException, InsuficientMoneyException, InternalErrorException {
        
    }
    
    @Test
    public void testLairsRanking() {
    	LairRankingInfoChunk ranking = lairFacade.getLairsRanking(0, NUMBER_OF_USERS);
    	assertEquals(NUMBER_OF_USERS, ranking.getElements().size());
    	assertEquals(NUMBER_OF_USERS, ranking.getSize());
    	
    	// verificar que están en orden ascendente
    	int lastScore = Integer.MAX_VALUE;
    	for (LairInfo lairInfo : ranking.getElements()) {
    		assert(lairInfo.getScore() <= lastScore);
    		lastScore = lairInfo.getScore();
    	}
    	
    	// verificar métodos auxiliares
    	assertEquals(ranking.getFirst().getLogin(), ranking.getElements().get(0).getLogin());
    	assertEquals(ranking.getLast().getLogin(), ranking.getElements().get(ranking.getSize() - 1).getLogin());
    	assert(ranking.isUserIncluded(ranking.getFirst().getLogin()));
    	assert(ranking.isUserIncluded(ranking.getLast().getLogin()));
    	assert(!ranking.isUserIncluded("RandomNameThatDoesNotExists-d54qsa"));
    	assertEquals(ranking.positionOfUser(ranking.getFirst().getLogin()), 1);
    	assertEquals(ranking.positionOfUser(ranking.getLast().getLogin()), ranking.getSize());
    }

    @Test
    public void testLairsPaginated() {
    	int pageSize = 5;
    	int numberOfPages = Integer.parseInt(
    			String.valueOf(Math.round(
    			Math.ceil(Double.valueOf(NUMBER_OF_USERS) / pageSize)
    			)));
    	int lastPage = numberOfPages - 1;
    	int startIndex = 0;
    	int lastScore = Integer.MAX_VALUE;
    	int position = 1;
    	
    	for (int i = 0; i < numberOfPages; i++) {
    		System.out.println("Page #"+i);
    		LairRankingInfoChunk lairBlock = lairFacade.getLairsRanking(startIndex, pageSize);
    		for (LairInfo lair : lairBlock.getElements()) {
    			System.out.println("#" + position + "  " +
    					lair.getLogin() + " - " + lair.getAddress() 
    					+ " - " + lair.getScore() + " pts.");
    			position++;
    		}
    		if (i < lastPage) {
    			assert(lairBlock.getElements().size() == pageSize);
    		} else {
    			assert(lairBlock.getElements().size() == NUMBER_OF_USERS % numberOfPages);
    		}
    		assert(lairBlock.getElements().get(
    				lairBlock.getElements().size() - 1).getScore() <= lastScore);
    		lastScore = lairBlock.getElements().get(
    				lairBlock.getElements().size() - 1).getScore();
    		startIndex += pageSize;
    	}
    	List<Lair> sortedLairs = lairDao.getLairsRanking(startIndex, pageSize);
    	// check there is no more lairs
    	assert(sortedLairs.size() == 0);
    }

}
