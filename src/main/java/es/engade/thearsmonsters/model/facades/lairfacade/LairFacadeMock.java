package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.test.util.FactoryData;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class LairFacadeMock implements LairFacade {

	private List<Lair> lairs;
	private Lair lair;
	
	public LairFacadeMock() {
		lair = FactoryData.LairWhatIs.Default.build();
		lairs = new ArrayList<Lair>();
		lairs.add(FactoryData.LairWhatIs.Default.build("YO"));
		lairs.add(FactoryData.LairWhatIs.InInitialState.build("Mi vecino"));
		lairs.add(FactoryData.LairWhatIs.InInitialState.build("Mi otro vecino"));
	}
	
	@Override
	public void cancelWorks(Lair lair, RoomType roomType)
			throws InWorksActionException, InternalErrorException,
			InstanceNotFoundException{
	}

	@Override
	public int changeResources(String moneyOrGarbage, int amount, Lair lair)
			throws WarehouseFullStorageException,
			TradeOfficeFullStorageException, InsuficientGarbageException,
			InsuficientMoneyException, OnlyOneChangePerGameDayException,
			InternalErrorException {
		return 2000;
	}

	@Override
	public void createNewRoom(Lair lair, RoomType roomType)
			throws InWorksActionException, InternalErrorException, InsuficientGarbageException {
		lair.addRoom(roomType.build(lair));
	}

	@Override
	public BuildingChunk findBuilding(int street, int building)
			throws InternalErrorException, IncorrectAddressException {
		return new BuildingChunk(lairs, 2, 2, 2, 0, 0, 0);
	}
	
	@Override
	public Lair findLair(String lairId) throws InstanceNotFoundException,
			InternalErrorException {
		return lair;
	}

	@Override
	public Lair findLairByLogin(String login) throws InstanceNotFoundException,
			InternalErrorException {
		return lair;
	}

	@Override
	public Lair findLairByAddress(int street, int building, int floor)
			throws InstanceNotFoundException, InternalErrorException,
			IncorrectAddressException {
		return lair;
	}

	@Override
	public void setRoomEnlargingInWorksState(Lair lair, RoomType roomType)
			throws InWorksActionException, InternalErrorException,
			InstanceNotFoundException, InsuficientGarbageException {
	}

	@Override
	public void setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
			throws InWorksActionException, InternalErrorException,
			InstanceNotFoundException, InsuficientGarbageException {
	}

}
