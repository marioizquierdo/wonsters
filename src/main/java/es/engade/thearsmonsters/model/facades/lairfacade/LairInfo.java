package es.engade.thearsmonsters.model.facades.lairfacade;

import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;

/**
 * Informacion de una guarida de forma sencilla y encapsulada.
 * Incluye datos como login de usuario, dirección, basura, dinero, puntos de ranking, etc.
 */
public class LairInfo {

	private String login;
	private Address address;
	private int garbage;
	private int money;
	private int score;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
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
	
	public LairInfo(String userName, Address address, int garbage, int money,
			int score) {
		this.login = userName;
		this.address = address;
		this.garbage = garbage;
		this.money = money;
		this.score = score;
	}

	public LairInfo(Lair lair) {
		this.login = lair.getUser().getLogin();
		this.address = lair.getAddress();
		this.garbage = lair.getGarbage();
		this.money = lair.getMoney();
		this.score = lair.getScore();
	}
	
}
