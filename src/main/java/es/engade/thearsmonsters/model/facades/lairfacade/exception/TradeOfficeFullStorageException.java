package es.engade.thearsmonsters.model.facades.lairfacade.exception;


import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class TradeOfficeFullStorageException  extends ModelException implements LocalizableMessage {

	private static final long serialVersionUID = 200912142039L;
	
	private int moneyInTheTradeOffice;
	private int moneyToStore;
	private int moneyStorageCapacity;
	private String loginName;
	private String messageKey;
	private String[] messageParams;
	
    public TradeOfficeFullStorageException(int moneyInTheTradeOffice, int moneyToStore, 
    		int moneyStorageCapacity, String loginName) {
        super("Having "+ moneyInTheTradeOffice +", try to store "+ moneyToStore +" of money in the " +
        		"TradeOffice, but the maximun storage capacity is "+ moneyStorageCapacity +
        		". In the lair of "+ loginName);
        this.moneyInTheTradeOffice = moneyInTheTradeOffice;
        this.moneyToStore = moneyToStore;
        this.moneyStorageCapacity = moneyStorageCapacity;
        this.loginName = loginName;
        
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

	public String getLoginName() {
		return loginName;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	}

}
