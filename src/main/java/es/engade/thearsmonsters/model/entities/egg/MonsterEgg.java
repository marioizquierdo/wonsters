package es.engade.thearsmonsters.model.entities.egg;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.util.CalendarTools;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MonsterEgg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 20091210L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	private Monster parent;
	
	@Persistent
	private MonsterRace race;
	
	private Lair lair;
	
	@Persistent
	private Date borningDate;
	
	public MonsterEgg () {}
	
	public MonsterEgg (Lair lair, MonsterRace race, 
			Date borningDate, Monster parent) {
//		this.id = id;
		this.lair = lair;
		this.race = race;
		this.borningDate = borningDate;
		this.parent = parent;
	}
	
	public Key getId() {
		return id;
	}

	public void setId(Key id) {
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

	public Date getBorningDate() {
		return borningDate;
	}

	public void setBorningDate(Date borningDate) {
		this.borningDate = borningDate;
	}

	/**
	 * Pone un huevo a incubar, fijando automáticamente su fecha de 
	 * nacimiento (que se sabe a partir de los minutos de incubación
	 * de la raza de ese huevo)
	 */
	public void setBorningDate() {
	    Date now = new Date();
	    long nowInMillis = now.getTime();
	    long incubationMillis = getRace().getIncubationMinutes() * 60 * 1000;

	    // Set de VO borning date
	    borningDate = new Date();
	    borningDate.setTime(nowInMillis + incubationMillis); // date at borningDate
	}
	
	public boolean isIncubated() {
		return borningDate!=null;
	}
	
	public boolean isReadyToBorn() {
		boolean readyToBorn;
		try {
			readyToBorn = borningDate.compareTo(new Date()) <= 0;
		}catch (Exception e){
			readyToBorn = false;
		}
		return readyToBorn;
	}
	
	public long getBorningDateEpoch() throws InternalErrorException {
		if(!isIncubated()) throw new InternalErrorException(new Exception("Epoch to burn undefined by now"));
		return borningDate.getTime();
	}
	
	
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"race", race,
//			"parent-name", parent.getName(),
//			"lair-user", lair.getUser().getLoginName(),
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
