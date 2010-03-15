package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class OnlyOneChangePerGameDayException  extends ModelException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private long thisChangeTurn;
	private long lastChangeTurn;
	private String login;
	private String messageKey;
	private String[] messageParams;
	
    public OnlyOneChangePerGameDayException(long thisChangeTurn, long lastChangeTurn, String login) {
        super("Cant do the change in turn "+thisChangeTurn+" because the las change was made in turn "+
        		lastChangeTurn+". At lair of "+login);
        this.thisChangeTurn = thisChangeTurn;
        this.lastChangeTurn = lastChangeTurn;
        this.login = login;
        
        this.messageKey = "FlashMessage.OnlyOneChangePerGameDayException";
        byte dayTurn = GameConf.getDayTurn(lastChangeTurn); // The day turn of today you must have to wait for
        this.messageParams = new String[]{dayTurn+"", turnsToWaitFor()+""};
    }
    
    public OnlyOneChangePerGameDayException(long lastChangeTurn, String login) {
    	this(GameConf.getCurrentTurn(), lastChangeTurn, login);
    }

	public long getThisChangeTurn() {
		return thisChangeTurn;
	}

	public long getLastChangeTurn() {
		return lastChangeTurn;
	}

	public String getLogin() {
		return login;
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
