package es.engade.thearsmonsters.model.entities.room;

import es.engade.thearsmonsters.model.entities.room.exceptions.PublicAccessException;

public class RoomPublicAccessOpen  extends RoomPublicAccess {

	private static final long serialVersionUID = 200911261639L;
	
	private int price;
	private int guildPrice;
	private String marketingText;
	
	public RoomPublicAccessOpen(int price, int guildPrice, String marketingText) {
		this.price = price;
		this.guildPrice = guildPrice;
		this.marketingText = marketingText;
	}
	
	@Override
    public boolean isPublished() {
		return true;
	}
	
	@Override
    public int getPublicPrice() throws PublicAccessException {
		return price;
	}

	public void setPublicPrice(int price) {
		this.price = price;
	}
	
	@Override
    public int getPublicGuildPrice() throws PublicAccessException {
		return guildPrice;
	}

	public void setPublicGuildPrice(int guildPrice) {
		this.guildPrice = guildPrice;
	}
	
	@Override
    public String getPublicMarketingText() throws PublicAccessException {
		return marketingText;
	}

	public void setPublicMarketingText(String marketingText) {
		this.marketingText = marketingText;
	}
	
	@Override
    public String toString() {
        return new String(
                "price = " + price + " | " +
        		"guildPrice = " + guildPrice + " | " +
                "marketingText = " + marketingText);
    }
	
	@Override
    public boolean equals(Object theObject) {
		try {
			RoomPublicAccessOpen access = (RoomPublicAccessOpen) theObject;
			return 
				this.price == access.getPublicPrice() &&
			    this.guildPrice == access.getPublicGuildPrice() &&
			    this.marketingText.equals(access.getPublicMarketingText());
		} catch (Exception e) {
			return false;
		}
	}
	
}
