package es.engade.thearsmonsters.model.entities.egg;

import java.io.Serializable;
import java.util.Calendar;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.util.CalendarTools;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterEgg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 20091210L;
	private Id id;
	private Monster parent;
	private MonsterRace race;
	private Lair lair;
	private Calendar borningDate;
	
	public MonsterEgg () {}
	
	public MonsterEgg (Id id, Monster parent, 
			MonsterRace race, Lair lair, Calendar borningDate) {
		this.id = id;
		this.parent = parent;
		this.race = race;
		this.lair = lair;
		this.borningDate = borningDate;
	}
	
	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Monster getParent() {
		return parent;
	}

	public void setParent(Monster parent) {
		this.parent = parent;
	}

	public MonsterRace getRace() {
		return race;
	}

	public void setRace(MonsterRace race) {
		this.race = race;
	}

	public Lair getLair() {
		return lair;
	}

	public void setLair(Lair lair) {
		this.lair = lair;
	}

	public Calendar getBorningDate() {
		return borningDate;
	}

	public void setBorningDate(Calendar borningDate) {
		this.borningDate = borningDate;
	}

	/**
	 * Pone un huevo a incubar, fijando automáticamente su fecha de 
	 * nacimiento (que se sabe a partir de los minutos de incubación
	 * de la raza de ese huevo)
	 */
	public void setBorningDate() {
	    Calendar now = Calendar.getInstance(); // Calendar now
	    long nowInMillis = now.getTimeInMillis();
	    long incubationMillis = getRace().getIncubationMinutes() * 60 * 1000;

	    // Set de VO borning date
	    borningDate = Calendar.getInstance();
	    borningDate.setTimeInMillis(nowInMillis + incubationMillis); // Calendar at borningDate
	}
	
	public boolean isIncubated() {
		return borningDate!=null;
	}
	
	public boolean isReadyToBorn() {
		boolean readyToBorn;
		try {
			readyToBorn = borningDate.compareTo(Calendar.getInstance()) <= 0;
		}catch (Exception e){
			readyToBorn = false;
		}
		return readyToBorn;
	}
	
	public long getBorningDateEpoch() throws InternalErrorException {
		if(!isIncubated()) throw new InternalErrorException(new Exception("Epoch to burn undefined by now"));
		return borningDate.getTimeInMillis();
	}
	
	
	
	public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"race", race,
			"parent-name", parent.getName(),
			"lair-user", lair.getUser().getLoginName(),
			"borningDate", borningDate
		});
	}
	
	/**
	 * Compara este huevo con otro.
	 * No se tienen en cuenta ni el parent ni la guarida.
	 */
	public boolean equals(MonsterEgg e) {
		return (this.getId().equals(e.getId()) &&
				this.getRace().equals(e.getRace()) &&
				CalendarTools.equals(this.getBorningDate(), e.getBorningDate()));
	}


}
