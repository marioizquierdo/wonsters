package es.engade.thearsmonsters.model.entities.egg;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.ThearsmonstersEntity;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MonsterEgg extends ThearsmonstersEntity implements Serializable {
	
	private static final long serialVersionUID = 20100305L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
//	@NotPersistent
//	private Monster parent;
	
	@Persistent
	private MonsterRace race;
	
	@Persistent
	private Lair lair;
	
	@Persistent(defaultFetchGroup="true")
	private Date borningDate;
	
	public MonsterEgg () {}
	
	public MonsterEgg (Lair lair, MonsterRace race, 
			Date borningDate) {
		this.lair = lair;
		this.race = race;
		this.borningDate = borningDate;
	}
	
	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

//	public Monster getParent() {
//		return parent;
//	}
//
//	public void setParent(Monster parent) {
//		this.parent = parent;
//	}

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
	    System.out.println("THIS " + this);
	    System.out.println("BORNING DATE " + borningDate);
        System.out.println("NOW " + new Date());
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((borningDate == null) ? 0 : borningDate.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((race == null) ? 0 : race.hashCode());
        return result;
    }

	/**
     * Compara este huevo con otro.
     * No se tienen en cuenta ni el parent ni la guarida.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MonsterEgg other = (MonsterEgg) obj;
        if (borningDate == null) {
            if (other.borningDate != null)
                return false;
        } else if (!borningDate.equals(other.borningDate))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lair == null) {
            if (other.lair != null)
                return false;
        } else if (!race.equals(other.race))
            return false;
        return true;
    }

    @Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
		    "key", id,
			"race", race,
//			"parent-name", parent.getName(),
//			"lair-user", lair.getUser().getLogin(),
			"borningDate", borningDate
		});
	}

}
