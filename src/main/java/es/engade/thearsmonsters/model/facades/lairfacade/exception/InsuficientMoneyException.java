package es.engade.thearsmonsters.model.facades.lairfacade.exception;


import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class InsuficientMoneyException  extends ModelException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private int moneyNeeded;
	private int moneyAvaliable;
	private String messageKey;
	private String[] messageParams;
	
    public InsuficientMoneyException(int moneyNeeded, int moneyAvaliable) {
        super("Insuficient money: you need "+moneyNeeded+" but you only have "+moneyAvaliable);
        this.moneyNeeded = moneyNeeded;
        this.moneyAvaliable = moneyAvaliable;
        
        this.messageKey = "FlashMessage.InsuficientMoneyException";
        this.messageParams = new String[]{moneyNeeded+"", moneyAvaliable+""};
    }

	public int getMoneyNeeded() {
		return moneyNeeded;
	}

	public int getMoneyAvaliable() {
		return moneyAvaliable;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	}
    
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new InsuficientMoneyException(10, 20);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
