package es.engade.thearsmonsters.model.facades.lairfacade.exception;


import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class WarehouseFullStorageException  extends ModelException implements LocalizableMessage {

	private static final long serialVersionUID = 200912142039L;
	
	private int garbageInTheWarehouse;
	private int garbageToStore;
	private int garbageStorageCapacity;
	private String login;
	private String messageKey;
	private String[] messageParams;
	
    public WarehouseFullStorageException(int garbageInTheWarehouse, int garbageToStore, 
    		int garbageStorageCapacity, String login) {
        super("Having "+ garbageInTheWarehouse +", try to store "+ garbageToStore +" of garbage in the " +
        		"Warehouse, but the maximun storage capacity is "+ garbageStorageCapacity +
        		". In the lair of "+ login);
        this.garbageInTheWarehouse = garbageInTheWarehouse;
        this.garbageToStore = garbageToStore;
        this.garbageStorageCapacity = garbageStorageCapacity;
        this.login = login;
        
        this.messageKey = "FlashMessage.WarehouseFullStorageException";
        this.messageParams = new String[]{garbageInTheWarehouse+"", garbageToStore+"", garbageStorageCapacity+""};
    }

	public int getGarbageInTheWarehouse() {
		return garbageInTheWarehouse;
	}

	public int getGarbageToStore() {
		return garbageToStore;
	}

	public int getGarbageStorageCapacity() {
		return garbageStorageCapacity;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getLogin() {
		return login;
	}

	public String[] getMessageParams() {
		return messageParams;
	}

}
