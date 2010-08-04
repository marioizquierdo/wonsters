package es.engade.thearsmonsters.model.facades.lairfacade;

import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;

public class LairRankingInfo {

	private String userName;
	private Address address;
	private int garbage;
	private int money;
	private int score;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Address getAddress() {
		return address;
	}
	public int getGarbage() {
		return garbage;
	}
	public int getMoney() {
		return money;
	}
	public int getScore() {
		return score;
	}
	
	public LairRankingInfo(String userName, Address address, int garbage, int money,
			int score) {
		this.userName = userName;
		this.address = address;
		this.garbage = garbage;
		this.money = money;
		this.score = score;
	}

	public LairRankingInfo(Lair lair) {
		this.userName = lair.getUser().getLogin();
		this.address = lair.getAddress();
		this.garbage = lair.getGarbage();
		this.money = lair.getMoney();
		this.score = lair.getScore();
	}
	
}
