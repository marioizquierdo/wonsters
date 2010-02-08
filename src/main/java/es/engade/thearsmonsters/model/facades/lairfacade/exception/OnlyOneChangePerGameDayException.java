package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class OnlyOneChangePerGameDayException  extends ModelException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private long thisChangeTurn;
	private long lastChangeTurn;
	private String loginName;
	private String messageKey;
	private String[] messageParams;
	
    public OnlyOneChangePerGameDayException(long thisChangeTurn, long lastChangeTurn, String loginName) {
        super("Cant do the change in turn "+thisChangeTurn+" because the las change was made in turn "+
        		lastChangeTurn+". At lair of "+loginName);
        this.thisChangeTurn = thisChangeTurn;
        this.lastChangeTurn = lastChangeTurn;
        this.loginName = loginName;
        
        this.messageKey = "FlashMessage.OnlyOneChangePerGameDayException";
        byte dayTurn = GameConf.getDayTurn(lastChangeTurn); // The day turn of today you must have to wait for
        this.messageParams = new String[]{dayTurn+"", turnsToWaitFor()+""};
    }
    
    public OnlyOneChangePerGameDayException(long lastChangeTurn, String loginName) {
    	this(GameConf.getCurrentTurn(), lastChangeTurn, loginName);
    }

	public long getThisChangeTurn() {
		return thisChangeTurn;
	}

	public long getLastChangeTurn() {
		return lastChangeTurn;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getMessageKey() {
		return messageKey;
	}
	
	public byte turnsToWaitFor() {
		return (byte) (lastChangeTurn + GameConf.getTurnsPerDay() - thisChangeTurn);
	}

	public String[] getMessageParams() {
		return messageParams;
	} 

}
