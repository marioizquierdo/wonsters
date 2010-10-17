package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.common.ThearsmonstersFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

@Service("lairFacade")
public class LairFacadeImpl extends ThearsmonstersFacade implements LairFacade {

    private LairDao lairDao;
    private UserDao userDao;

    public void setLairDao(LairDao lairDao) {
        this.lairDao = lairDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void cancelWorks(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InstanceNotFoundException {

        Room room = lair.getRoom(roomType);
        if (!room.isInWorks())
            throw new InWorksActionException(
                    "The works for upgrade can not be cancelled", lair
                            .getUser().getLogin(), roomType);

        if (room.setStateCancelWorks()) { // Check and start works

            lair.setGarbage(lair.getGarbage() + room.getGarbageUpgrade()); // spend garbage
            // lairDao.update(lair); // save lair
            // roomDao.update(room); // save room
            userDao.update(lair.getUser());
        } else {
            throw new InWorksActionException(
                    "The works for upgrade can not be cancelled", lair
                            .getUser().getLogin(), roomType);
        }
    }

    @Transactional
    public int changeResources(Lair lair, String moneyOrGarbage, int amount)
            throws WarehouseFullStorageException,
            TradeOfficeFullStorageException, InsuficientGarbageException,
            InsuficientMoneyException, InternalErrorException {
        
        int currentGarbage = lair.getGarbage();
        int currentMoney = lair.getMoney();
        int maxGarbage = lair.getGarbageStorageCapacity();
        int maxMoney = lair.getMoneyStorageCapacity();
        int amountObtained = amount * (100 - lair.getPercentageCommision()) / 100; // aplica el porcentaje de comision por el cambio
        String login = lair.getUser().getLogin();
        
        // Check level > 0, it should not occur because the view hides links to this action when level == 0
        if((lair.getRoom(RoomType.TradeOffice).getLevel()) == 0) {
        	throw new InternalErrorException(new Exception("Can not trade at level 0."));
        }
        
        // Cambio basura por dinero
        if (moneyOrGarbage.equals("garbage")) {
            if(amount > currentGarbage) {
                throw new InsuficientGarbageException(amount, currentGarbage);
            }
            if(amount > lair.getChangeResourcesMaxGarbageAmountEnabled()) {
                throw new TradeOfficeFullStorageException(currentMoney, amountObtained, maxMoney, login);
            }
            lair.setGarbage(currentGarbage - amount);
            lair.setMoney(currentMoney + amountObtained);
            
        // Cambio dinero por basura
        } else if (moneyOrGarbage.equals("money")) {
            if(amount > currentMoney) {
                throw new InsuficientMoneyException(amount, currentMoney);
            }
            if(amount > lair.getChangeResourcesMaxMoneyAmountEnabled()) {
                throw new WarehouseFullStorageException(currentGarbage, amountObtained, maxGarbage, login);
            }
            lair.setMoney(currentMoney - amount);
            lair.setGarbage(currentGarbage + amountObtained);
        
        } else {
            throw new InternalErrorException(
                    new Exception("Expected \"money\" or \"garbage\" resource, but got \"" + moneyOrGarbage + "\"")
            );
        }
        
        // Save changes and return
        userDao.update(lair.getUser());
        
        return amountObtained;
    }

    public void createNewRoom(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InsuficientGarbageException {

        lair.buildRoom(roomType);
        userDao.update(lair.getUser());

    }

    public BuildingChunk findBuilding(int street, int building)
            throws InternalErrorException, IncorrectAddressException {

        // Las coordenadas (street, building) van de 0 a N-1.
    	// Se chequean al crear el BuildingChunk.
    	
    	
        // Temporalmente se accede en varias consultas...
        List<Lair> lairs = lairDao.findLairsByBuilding(street, building);
        for (Lair lair : lairs) {
            try {
                lair.setUser(userDao.get(lair.getUser().getIdKey())); // TODO: esto hay que optimizarlo
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return new BuildingChunk(lairs, street, building);
    }

    @Transactional(readOnly = true)
    public Lair findLair(String lairId) throws InstanceNotFoundException,
            InternalErrorException {

        Lair lair = null;

        try {
            lair = lairDao.get(getKeyFromString(lairId, Lair.class));
            if (lair == null)
                throw new InstanceNotFoundException(lairId, Lair.class
                        .getName());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
        System.out.println("LOADED 2 " + lair.getRooms().size() + " rooms");
        return lair;
    }

    public Lair findLairByAddress(int street, int building, int floor)
            throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
    	
    	// Check coordinates [0..N-1]
    	if(!Address.checkAddress(street, building, floor)) { 
    		throw new IncorrectAddressException(street, building, floor);
    	}

    	try {
        	return lairDao.findLairByAddress(street, building, floor);
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }


    }

    @Transactional(readOnly = true)
    public Lair findLairByLogin(String login) throws InstanceNotFoundException,
            InternalErrorException {

        User user = userDao.findUserByLogin(login);

        Lair lair = null;
//        if (user.getLair() != null)
//            lair = user.getLair();
//        else {
            lair = lairDao.findLairByUser(user);
//        }

        return lair;
    }

    @Transactional
    public void setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InstanceNotFoundException, InsuficientGarbageException {
    	
        Room room = lair.getRoom(roomType);
        int garbageToUpgrade = room.getGarbageUpgrade();
        
   

        if (lair.getGarbage() >= garbageToUpgrade) { // Be sure there are needed garbage for start Works
        	if (room.setStateStartUpgrading()) { // Check and start works
                lair.setGarbage(lair.getGarbage() - garbageToUpgrade); // spend
                // roomDao.update(room); // save room
                // lairDao.update(lair); // save lair
                userDao.update(lair.getUser());
            } else {
                throw new InWorksActionException(
                        "The works for upgrade can not be initialized", lair
                                .getUser().getLogin(), roomType);
            }
        } else {
            throw new InsuficientGarbageException(room.getGarbageUpgrade(),
                    lair.getGarbage());
        }

    }
    
    public LairRankingInfoChunk getLairsRanking(int startIndex, int count) {
    	List<Lair> lairs = lairDao.getLairsRanking(startIndex, count + 1);
    	List<LairInfo> lairInfos = new ArrayList<LairInfo>();
    	for (Lair lair : lairs) {
            try {
            	LairInfo lairInfo = new LairInfo(null, lair.getAddress(), lair.getGarbage(), lair.getMoney(), lair.getScore());
                lairInfo.setLogin(userDao.get(
                		lair.getUser().getIdKey()).getLogin()
                		); // TODO: esto hay que optimizarlo
                lairInfos.add(lairInfo);
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            }
        }
    	boolean hasMoreElements = lairs.size() > count;
    	
    	return new LairRankingInfoChunk(lairInfos, hasMoreElements);
    	
    	
//    	// Se pillan las guaridas del bloque, igual que en findBuilding(int street, int building) para ir tirando...
//    	// También peta cuando se intenta cargar el user. findBuilding también peta, da un  NullPointerException
//    	List<Lair> lairs = lairDao.findLairsByBuilding(0, 0); // de la coordenada (0,0) que ahi fijo que hay guaridas
//    	List<LairInfo> lairInfos = new ArrayList<LairInfo>();
//        for (Lair lair : lairs) {
//        	LairInfo lairInfo = new LairInfo("yo que se", lair.getAddress(), lair.getGarbage(), lair.getMoney(), lair.getScore());
//            lairInfos.add(lairInfo);
//        }
//    	return new LairRankingInfoChunk(lairInfos, false);
    }

}
