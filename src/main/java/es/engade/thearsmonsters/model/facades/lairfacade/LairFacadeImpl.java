package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.dao.RoomDao;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.common.ThearsmonstersFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

@Service("lairFacade")
public class LairFacadeImpl extends ThearsmonstersFacade implements LairFacade {

    private LairDao lairDao;
    private RoomDao roomDao;
    private UserDao userDao;

    public void setLairDao(LairDao lairDao) {
        this.lairDao = lairDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setRoomDao(RoomDao roomDao) {
        this.roomDao = roomDao;
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

            lair.setGarbage(lair.getGarbage() + room.getGarbageUpgrade()); // spend
                                                                           // garbage
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
            InsuficientMoneyException, OnlyOneChangePerGameDayException,
            InternalErrorException {
        
        int currentGarbage = lair.getGarbage();
        int currentMoney = lair.getMoney();
        int maxGarbage = lair.getGarbageStorageCapacity();
        int maxMoney = lair.getMoneyStorageCapacity();
        int amountObtained = amount * (100 - lair.getPercentageCommision()) / 100; // aplica el porcentaje de comision por el cambio
        Date lastChangeResourcesDate = lair.getLastChangeResourcesDate();
        String login = lair.getUser().getLogin();
        
        
        // Check that the change is made only once per day
        if(!lair.isReadyToChangeResources()) {
            throw new OnlyOneChangePerGameDayException(lastChangeResourcesDate, login);
        }
        
        if (moneyOrGarbage.equals("garbage")) {
            if(amount > currentGarbage) {
                throw new InsuficientGarbageException(amount, currentGarbage);
            }
            if(amount > lair.getChangeResourcesMaxGarbageAmountEnabled()) {
                throw new TradeOfficeFullStorageException(currentMoney, amountObtained, maxMoney, login);
            }
            lair.setGarbage(currentGarbage - amount);
            lair.setMoney(currentMoney + amountObtained);
            
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
                    new Exception("Expected \"money\" or \"garbage\" resource," +
                            " but got \"" + moneyOrGarbage + "\"")
            );
        }
        
        // Mark diary change done
        lair.setLastChangeResourcesDateToNow();
        
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

        if (user.getLair() != null)
            lair = user.getLair();
        else {
            lair = lairDao.findLairByUser(user);
        }

        return lair;
    }

    @Transactional
    public void setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InstanceNotFoundException, InsuficientGarbageException {

        Room room = lair.getRoom(roomType);

        if (lair.getGarbage() >= room.getGarbageUpgrade()) { // Be sure there
                                                             // are needed
                                                             // garbage for
                                                             // start Works
            if (room.setStateStartUpgrading()) { // Check and start works

                lair.setGarbage(lair.getGarbage() - room.getGarbageUpgrade()); // spend
                                                                               // garbage
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

}
