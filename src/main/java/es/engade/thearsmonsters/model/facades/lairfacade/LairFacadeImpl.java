package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
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
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

@Service("lairFacade")
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
        // TODO implementar
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

        // TODO: DE 0 a N-1 o de 1 a N ¿?¿?¿?¿?¿?
        // TODO: Si no hay más.... devuelve -1 o algo?
        // Temporalmente se accede en varias consultas...
        List<Lair> lairs = lairDao.findLairsByBuilding(street, building);
        for (Lair lair : lairs) {
            try {
                lair.setUser(userDao.get(lair.getUser().getId()));
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            }
        }
        int nextStreet, nextBuilding, nextBuildingStreet, previousStreet, previousBuilding, previousBuildingStreet;
        if (building == GameConf.getMaxNumberOfBuildings()) {
            nextBuilding = 1;
            nextStreet = street + 1;
            nextBuildingStreet = street + 1;
            previousBuilding = building - 1;
            previousStreet = street;
            previousBuildingStreet = street;
        } else if (building == 1) {
            nextBuilding = building + 1;
            nextStreet = street;
            nextBuildingStreet = street;
            previousBuilding = GameConf.getMaxNumberOfBuildings();
            previousStreet = street - 1;
            previousBuildingStreet = street - 1;
        } else {
            nextBuilding = building + 1;
            nextStreet = street;
            nextBuildingStreet = street;
            previousBuilding = building - 1;
            previousStreet = street;
            previousBuildingStreet = street;
        }

        BuildingChunk bc = new BuildingChunk(lairs, nextStreet, nextBuilding,
                nextBuildingStreet, previousStreet, previousBuilding,
                previousBuildingStreet);
        return bc;
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
            throws InstanceNotFoundException, InternalErrorException,
            IncorrectAddressException {

        Lair lair = null;

        try {
            lair = lairDao.findLairByAddress(street, building, floor);
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

        return lair;

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
