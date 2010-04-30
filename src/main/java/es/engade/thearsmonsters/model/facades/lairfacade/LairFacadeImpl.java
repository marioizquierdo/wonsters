package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.dao.RoomDao;
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
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

@Service("lairFacade")
public class LairFacadeImpl implements LairFacade {

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

    public int changeResources(Lair lair, String moneyOrGarbage, int amount)
            throws WarehouseFullStorageException,
            TradeOfficeFullStorageException, InsuficientGarbageException,
            InsuficientMoneyException, OnlyOneChangePerGameDayException,
            InternalErrorException {
        return 2000;
    }

    public void createNewRoom(Lair lair, RoomType roomType)
            throws InWorksActionException, InternalErrorException,
            InsuficientGarbageException {

        lair.buildRoom(roomType);
        lairDao.update(lair);

    }

    public BuildingChunk findBuilding(int street, int building)
            throws InternalErrorException, IncorrectAddressException {

        // Las coordenadas (street, building) van de 0 a N-1.
    	// Se chequean al crear el BuildingChunk.
    	
    	
        // Temporalmente se accede en varias consultas...
        List<Lair> lairs = lairDao.findLairsByBuilding(street, building);
        for (Lair lair : lairs) {
            try {
                lair.setUser(userDao.get(lair.getUser().getId())); // TODO: esto hay que optimizarlo
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
            lair = lairDao.get(KeyUtils.fromString(lairId));
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

    @Transactional(readOnly = true)
    public Lair findLairByAddress(int street, int building, int floor)
            throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException {
    	
    	// Check coordinates [0..N-1]
    	if(		building < 0 || building >= GameConf.getMaxNumberOfBuildings() ||
    			street < 0 || street >= GameConf.getMaxNumberOfStreets() ||
    			floor < 0 || floor >= GameConf.getMaxNumberOfFloors()) 
    		throw new IncorrectAddressException(street, building, floor);

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
                userDao.save(lair.getUser());
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
