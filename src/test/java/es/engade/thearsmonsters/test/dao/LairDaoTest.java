package es.engade.thearsmonsters.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.test.AppContext;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.factory.FactoryData;

public class LairDaoTest extends GaeTest {

    private static final int NUMBER_OF_USERS = 15;

    private static UserDao userDao;
    private static LairDao lairDao;
    
    private User persistentUser;
    private List<User> allPersistentUsers = new ArrayList<User>(15);

    static {
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        userDao = appContext.getBean(UserDao.class);
        lairDao = appContext.getBean(LairDao.class);
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
        int building = 1;
        int floor = 2;
        int garbage = 100;
        int money = 100;
        while (numberOfUsers < NUMBER_OF_USERS) {
            allPersistentUsers.add(FactoryData.UserWhoIs.Random.build());
            allPersistentUsers.get(numberOfUsers).getLair().setRooms(null);
            allPersistentUsers.get(numberOfUsers).getLair().setAddressBuilding(building);
            allPersistentUsers.get(numberOfUsers).getLair().setAddressFloor(floor);
                        
            // add score
            allPersistentUsers.get(numberOfUsers).getLair().setGarbage(garbage);
            allPersistentUsers.get(numberOfUsers).getLair().setMoney(money);
            
            userDao.save(allPersistentUsers.get(numberOfUsers));
            numberOfUsers++;
            floor++;
            garbage += 100;
            money += 100;
            if (floor >= GameConf.getMaxNumberOfFloors()) {
                floor = 1;
                building++;
            }
            System.out.println("ADDRESS PERSISTENT _ 1, "+ building + ", "+ floor);
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
    public void testFindByLogin() throws InstanceNotFoundException, FullPlacesException {
//
//        for (User u:allPersistentUsers) {
//            System.out.println("LAIR from " + u.getLogin() + " : " + u.getLair().getBuilding());
//        }
        
        Lair l = lairDao.findLairByUser(persistentUser);//.findLairByAddress(new Address(1,2,1));
        System.out.println("lairRR " + l);
        
        Address a = lairDao.findNextFreeAddress();
        System.out.println("ADDRESS _ " + a.getStreet() + ", "+ a.getBuilding() + ", "+ a.getFloor());
    }
    
    @Test
    public void testFindByBuilding() throws InstanceNotFoundException {

        int STREET = 1;
        int BUILDING = 2;
        
        List<Lair> lairs = lairDao.findLairsByBuilding(STREET, BUILDING);
        
//        System.out.println("numLairs = " + lairs.size());
        for (Lair lair : lairs) {
//            System.out.println("LAIR : " + lair.getAddress());
            assert(lair.getAddressStreet() == STREET);
            assert(lair.getAddressBuilding() == BUILDING);
        }
        
        for (User user : allPersistentUsers) {
            if (user.getLair().getAddressStreet() == STREET
                    && user.getLair().getAddressBuilding() == BUILDING) {
            
                assert(lairs.contains(user.getLair()));
            }
        }
        
    }
    
    @Test
    public void testLairsRanking() {
    	List<Lair> sortedLairs = lairDao.getLairsRanking(0, GameConf.getLairsRankingDepth());
    	
    	System.out.println(sortedLairs.size() + " lairs in rank");
    	int lastScore = Integer.MAX_VALUE;
    	for (Lair lair : sortedLairs) {
    		System.out.println("Lair " + lair.getAddress() + " - " + lair.getScore() + " pts.");
    		assert(lair.getScore() <= lastScore);
    		lastScore = lair.getScore();
    	}
    }
    
    @Test
    public void testLairsRankingPaginated() {
    	int pageSize = 5;
    	int numberOfPages = Integer.parseInt(
    			String.valueOf(Math.round(
    			Math.ceil(Double.valueOf(NUMBER_OF_USERS) / pageSize)
    			)));
    	int lastPage = numberOfPages - 1;
    	int startIndex = 0;
    	int lastScore = Integer.MAX_VALUE;

    	for (int i = 0; i < numberOfPages; i++) {
    		List<Lair> sortedLairs = lairDao.getLairsRanking(startIndex, pageSize);
    		if (i < lastPage) {
    			assert(sortedLairs.size() == pageSize);
    		} else {
    			assert(sortedLairs.size() == NUMBER_OF_USERS % numberOfPages);
    		}
    		assert(sortedLairs.get(sortedLairs.size() - 1).getScore() <= lastScore);
    		lastScore = sortedLairs.get(sortedLairs.size() - 1).getScore();
    		startIndex += pageSize;
    	}
    	List<Lair> sortedLairs = lairDao.getLairsRanking(startIndex, pageSize);
    	// check there is no more lairs
    	assert(sortedLairs.size() == 0);
    }
    
    @Test
    public void testFindByAddress() throws InstanceNotFoundException {

        Address address = persistentUser.getLair().getAddress();
        
        Lair lair = lairDao.findLairByAddress(
        		address.getStreet(),
        		address.getBuilding(),
        		address.getFloor());
      
        assertEquals(persistentUser.getLair(), lair);
        
    }

}
