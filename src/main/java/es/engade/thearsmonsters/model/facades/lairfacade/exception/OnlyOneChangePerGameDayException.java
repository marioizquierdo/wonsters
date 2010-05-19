package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import java.util.Date;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.model.util.DateTools;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class OnlyOneChangePerGameDayException  extends ModelException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private Date thisChangeDate;
	private Date lastChangeDate;
	private String login;
	private String messageKey;
	private String[] messageParams;
	
    public OnlyOneChangePerGameDayException(Date thisChangeDate, Date lastChangeDate, String login) {
        super("Cant do the change at "+ Format.date(thisChangeDate) +" because the las change was made at "+
        		Format.date(lastChangeDate) +". In the lair of "+login);
        this.thisChangeDate = thisChangeDate;
        this.lastChangeDate = lastChangeDate;
        this.login = login;
        
        this.messageKey = "FlashMessage.OnlyOneChangePerGameDayException";
        this.messageParams = new String[]{hoursToWaitFor()+"", minutesToNextChangeHour()+""};
    }
    
    public OnlyOneChangePerGameDayException(Date lastChangeDate, String login) {
    	this(DateTools.now(), lastChangeDate, login);
    }

	public Date getThisChangeDate() {
		return thisChangeDate;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public String getLogin() {
		return login;
	}

	public String getMessageKey() {
		return messageKey;
	}
	
	public Date nextChangeDate() {
		return DateTools.new_byDaysFrom(lastChangeDate, 1);
	}
	
	public long hoursToWaitFor() {
		return (long) DateTools.hoursFromNowTo(nextChangeDate());
	}
	
	public long minutesToNextChangeHour() {
		return (long) DateTools.minutesFromNowTo(nextChangeDate()) % 60;
	}

	public String[] getMessageParams() {
		return messageParams;
	} 

}
