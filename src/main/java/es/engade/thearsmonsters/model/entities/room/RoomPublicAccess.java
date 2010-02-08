package es.engade.thearsmonsters.model.entities.room;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.room.exceptions.PublicAccessException;

public abstract class RoomPublicAccess implements Serializable {
	
	private static final long serialVersionUID = 200911261639L;
	
	public abstract boolean isPublished();
	
	public int getPublicPrice() throws PublicAccessException {
		throw new PublicAccessException("getPublicPrice");
	}
	
	public int getPublicGuildPrice() throws PublicAccessException {
		throw new PublicAccessException("getPublicGuildPrice");
	}
	
	public String getPublicMarketingText() throws PublicAccessException {
		throw new PublicAccessException("getPublicMarketingText");
	}

}
