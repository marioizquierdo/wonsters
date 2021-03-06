package es.engade.thearsmonsters.model.facades.lairfacade.exception;


import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class TradeOfficeFullStorageException  extends ModelException implements LocalizableMessage {

	private static final long serialVersionUID = 200912142039L;
	
	private int moneyInTheTradeOffice;
	private int moneyToStore;
	private int moneyStorageCapacity;
	private String login;
	private String messageKey;
	private String[] messageParams;
	
    public TradeOfficeFullStorageException(int moneyInTheTradeOffice, int moneyToStore, 
    		int moneyStorageCapacity, String login) {
        super("Having "+ moneyInTheTradeOffice +", try to store "+ moneyToStore +" of money in the " +
        		"TradeOffice, but the maximun storage capacity is "+ moneyStorageCapacity +
        		". In the lair of "+ login);
        this.moneyInTheTradeOffice = moneyInTheTradeOffice;
        this.moneyToStore = moneyToStore;
        this.moneyStorageCapacity = moneyStorageCapacity;
        this.login = login;
        
        this.messageKey = "FlashMessage.TradeOfficeFullStorageException";
        this.messageParams = new String[]{moneyInTheTradeOffice+"", moneyToStore+"", moneyStorageCapacity+""};
    }

	public int getMoneyInTheTradeOffice() {
		return moneyInTheTradeOffice;
	}

	public int getMoneyToStore() {
		return moneyToStore;
	}

	public int getMoneyStorageCapacity() {
		return moneyStorageCapacity;
	}

	public String getLogin() {
		return login;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	}

}
