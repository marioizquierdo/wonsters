package es.engade.thearsmonsters.model.entities.monsterActivity.monsteractivity;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;

public class MonsterActivity implements Serializable {

	private static final long serialVersionUID = 8582747739862689965L;

	public enum Activity {Customer, Worker}
	
	public MonsterActivity() {}
	
	private byte hourFrom;
	private byte hourTo;
	private Monster monster;
	private Room romm;
	private Activity activity;
	public byte getHourFrom() {
		return hourFrom;
	}
	public void setHourFrom(byte hourFrom) {
		this.hourFrom = hourFrom;
	}
	public byte getHourTo() {
		return hourTo;
	}
	public void setHourTo(byte hourTo) {
		this.hourTo = hourTo;
	}
	public Monster getMonster() {
		return monster;
	}
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	public Room getRomm() {
		return romm;
	}
	public void setRomm(Room romm) {
		this.romm = romm;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public boolean isValid() {
		return false;
	}
	
	public byte hoursAmount() {
		return (byte) (hourTo - hourFrom);
	}
	
}
