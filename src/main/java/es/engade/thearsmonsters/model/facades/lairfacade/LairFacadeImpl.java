package es.engade.thearsmonsters.model.facades.lairfacade;

import javax.jdo.annotations.Transactional;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class LairFacadeImpl implements LairFacade {

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
        // TODO Auto-generated method stub

    }

    public int changeResources(String moneyOrGarbage, int amount, Lair lair)
            throws WarehouseFullStorageException,
            TradeOfficeFullStorageException, InsuficientGarbageException,
            InsuficientMoneyException, OnlyOneChangePerGameDayException,
            InternalErrorException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void createNewRoom(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InsuficientGarbageException {
        // TODO Auto-generated method stub

    }

    public BuildingChunk findBuilding(int street, int building)
            throws InternalErrorException, IncorrectAddressException {
        // TODO Auto-generated method stub
        return null;
    }

    public Lair findLair(String lairId) throws InstanceNotFoundException,
            InternalErrorException {

        Lair lair = null;
        
        try {
            lair = lairDao.get(KeyUtils.fromString(lairId));
            if (lair == null)
                throw new InstanceNotFoundException(lairId, Lair.class.getName());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
        
        return lair;
    }

    public Lair findLairByAddress(int street, int building, int floor)
            throws InstanceNotFoundException, InternalErrorException,
            IncorrectAddressException {

        Address address = new Address(street, building, floor);
        Lair lair = null;
        
        try {
            lair = lairDao.findLairByAddress(address);
            if (lair == null)
                throw new InstanceNotFoundException(address, Lair.class.getName());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
        
        return lair;
        
    }

    @Transactional
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

    public void setRoomEnlargingInWorksState(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InstanceNotFoundException, InsuficientGarbageException {
        // TODO Auto-generated method stub

    }

    public void setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InstanceNotFoundException, InsuficientGarbageException {
        // TODO Auto-generated method stub

    }

}
