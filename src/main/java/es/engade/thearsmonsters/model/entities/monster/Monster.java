package es.engade.thearsmonsters.model.entities.monster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrTypeClass;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.monsteractivity.MonsterActivity;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.util.DateTools;
import es.engade.thearsmonsters.model.util.Format;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Monster implements Serializable {

    private static final long serialVersionUID = 20100305L;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
    
    @Persistent
	private Lair lair;
    
    @Persistent(serialized="true") 
	private Map<AttrType, Attr> workSkills;
    
    @Persistent(serialized="true") 
	private Map<AttrType, Attr> simpleAttrs;
    
    @Persistent
	private MonsterAge age;
    
    @Persistent
	private Date borningDate;
    
    @Persistent
	private Date cocoonCloseUpDate;
    
    @Persistent
	private Date freeTurnsTimestamp;
    
    @Persistent
	private int freeTurns;
    
    @Persistent(serialized="true", defaultFetchGroup="true")
	private List<MonsterActivity> activities;
    
    @Persistent
	private String name;
    
    @Persistent
	private MonsterRace race;
	
	public Monster(){
	    activities = new ArrayList<MonsterActivity>();
	}
	
	public Monster(Lair lair, MonsterRace race, String name, Date borningDate, Date cocoonCloseUpDate,
			MonsterAge ageState) {
		this.setLair(lair);
		this.setRace(race);
		this.setName(name);
		this.setBorningDate(borningDate);
		this.setCocoonCloseUpDate(cocoonCloseUpDate);
		this.setAge(ageState);

		// Se supone que los attrs son 'completos', es decir, que hay un atributo por cada AttrType.
		this.simpleAttrs = AttrType.initializeSimpleAttrs();
		this.workSkills = AttrType.initializeWorkSkills();
		this.freeTurns = 11;

		/* Esto de aqui abajo no se si esta bien */
		this.freeTurnsTimestamp = new Date();
		this.activities = new ArrayList<MonsterActivity>();
		
	}

	//-- GETTERS --//
	public Key getId() { return id; }
	public Lair getLair() { return lair; }	
	public MonsterAge getAge() { return age; }	
	public Date getBorningDate() { return borningDate; }
	public Date getCocoonCloseUpDate() { return cocoonCloseUpDate; }
	public String getName() { return name; }
	public MonsterRace getRace() { return race; }
	// getFreeTurns y isFreeTurnsAvailable están en la sección de las monster actions.
	
	public int getAgeDays(){ 
	    
	    int days = Math.round(DateTools.distanceInDays(getBorningDate(), DateTools.now()));

	    return days;
	    
	}
	
	public int getAgePercentageLived(){
	    
	    return Math.round((getAgeDays() / race.getLiveLenght()) * 100);
	    
	}
	
	public Attr getBestWorkSkill(){return this.workSkills.get(1);}
	
	//-- SETTERS --//
	public void setId(Key id) { this.id = id; }
	public void setLair(Lair lair) { this.lair = lair; }
	public void setRace(MonsterRace race) { this.race = race; }
	public void setName(String name) { this.name = name; }
	public void setBorningDate(Date borningDate) { this.borningDate = borningDate; }
	public void setCocoonCloseUpDate(Date cocoonCloseUpDate) { this.cocoonCloseUpDate = cocoonCloseUpDate; }
	public void setAge(MonsterAge ageState) { this.age = ageState; }
	// setFreeTurns está en la sección de las monster actions.

	
	
	/**
	 * Fija la fecha para que salga del capuyo (cocoonCloseUpDate) y cambia la edad
	 * a AgeState.Adult o a AgeState.Cocoon (depende si el tiempo de metamorfosis es diferente de cero o no).
	 * @throws MonsterGrowException si la edad del monstruo no es AgeState.Child
	 */
	public void metamorphosisToAdult() throws MonsterGrowException {
		if(getAge().equals(MonsterAge.Child)) {
			Date metamorphosisTime = DateTools.new_byMinutesFromNow(race.getMetamorphosisMinutes());
			setCocoonCloseUpDate(metamorphosisTime);
			this.setAge(MonsterAge.Adult);
		} else {
			throw new MonsterGrowException(this.getId());
		}
	}
	
	
	//-- Methods for monster attributes --//
	
	/**
	 * Añade experiencia al atributo del tipo indicado.
	 * Si la experiencia es mayor que 100, sube un nivel más al monstruo.
	 * @param exp cantidad de experiencia añadida. Tener en cuenta que cada nivel tiene 100 de experiencia.
	 */
	public void addExp(AttrType type, int exp) {
		this.getAttr(type).addExp(exp);
	}
	
	/**
	 * Devuelve el atributo identificado por type, sin
	 * importar si es simple, compuesto o habilidad.
	 */
	public Attr getAttr(AttrType type) {
		switch(type.getAttrClass()) {
		case SimpleAttr: return this.getSimpleAttr(type);
		case ComposeAttr: return this.getComposeAttr(type);
		case WorkSkill: return this.getWorkSkill(type);
		default: return null;
		}
	}
	
	/**
	 * @return Map<AttrType, Attr> All attributes of the monster: simple, compose and workskills.
	 */
	public Map<AttrType, Attr> getAttrs() {
		Map<AttrType,Attr> attrs = new HashMap<AttrType,Attr>();
		attrs.putAll(workSkills);
		attrs.putAll(simpleAttrs);
		return attrs;
	}
	
	public Attr getComposeAttr(AttrType type) { 
		return AttrType.newAttrCompose(type, this.getSimpleAttrs(), this.getWorkSkills());
	}
	
	public Map<AttrType, Attr> getComposeAttrs() {
		Map<AttrType,Attr> attrs = new HashMap<AttrType,Attr>();
		for(AttrType e : AttrType.values()) {
			if (e.getAttrClass()== AttrTypeClass.ComposeAttr){
				attrs.put(e, this.getComposeAttr(e));
			}
		}
		return attrs;
	}
	
	public Attr getSimpleAttr(AttrType type) { return simpleAttrs.get(type); }
	public Map<AttrType, Attr> getSimpleAttrs() { return this.simpleAttrs; }
	public Attr getWorkSkill(AttrType type) { return workSkills.get(type); }
	public Map<AttrType, Attr> getWorkSkills() { return this.workSkills; }
	
	
	
	
	//-- MONSTER ACTIONS (turns) --//	
	
	/** 
	 * Se obtiene los turnos que ya estan asignados a este monstruo en otras actividades
	 * TODO: cuando se implementen las MonsterActivities hay que descomentar este método.
	 * @return int, Devuelve el numero de turnos ocupados en realizar sus actividades asignadas
	 */
	public int taskHours(){
		int s = 0;
//		MonsterActivity activity;
//		for (int i=0;i<this.activities.length();i++) {
//			activity = (MonsterActivity) activities.elementAt(i);
//			s+=activity.hoursAmount();
//		}
		return s;
	}
	
	/**
	 * Refresca el valor de los freeTurns (y de freeTurnsTimestamp) para el momento timestamp,
	 * que debe ser posterior a freeTurnsTimestamp (no se puede refrescar hacia el pasado).
	 * @throws IllegalArgumentException si timestamp es antes de freeTurnsTimestamp.
	 */
	private void refreshFreeTurns(Date timestamp) {
		if(timestamp.before(freeTurnsTimestamp)) {
			throw new IllegalArgumentException("the value of the param timestamp can not be before freeTurnsTimestamp");
		}
		
		freeTurns = getFreeTurns(timestamp); // turnos libres que hay en ese momento.
		freeTurnsTimestamp = timestamp;
	}
	
	/**
	 * Refresca el valor de los freeTurns y de freeTurnsTimestamp al momento actual.
	 */
	private void refreshFreeTurns() {
		refreshFreeTurns(DateTools.now());
	}
	
	/**
	 * Número de turnos libres que tiene el monstruo en el instante timestamp
	 */
	public int getFreeTurns(Date timestamp) {
		// El atributo freeTurns es válido para el momento freeTurnsTimestamp,
		// en el momento timestamp su valor puede ser distinto, 
		// ya que los turnos se acumulan constantemente.
		
		if(timestamp.before(freeTurnsTimestamp)) { 
			throw new IllegalArgumentException("the value of the param timestamp can not be before freeTurnsTimestamp."); 
		}
		
		int turnsPerDay = 15 - this.taskHours(); // turnos acumulados cada día
		float daysFromTimestamp = DateTools.distanceInDays(this.freeTurnsTimestamp, timestamp);
		
		// En el momento timestamp, los freeTurns son los que había en el instante freeTurnsTimestamp
		// mas el número de turnos acumulados (según los dias que han pasado desde el último timpestamp)
		return  this.freeTurns + turnsPerDay * ((int) Math.floor(daysFromTimestamp));
	}
	
	/**
	 * Número de turnis libres que tiene el monstruo en este momento.
	 * (se van incrementando gradualmente al cabo del tiempo)
	 */
	public int getFreeTurns() {
		return getFreeTurns(DateTools.now());
	}
	
	/**
	 * Pone los turnos indicados, y actualiza freeTurnsTimestamp al momento actual.
	 * NOTA: Este método solo se debe usar desde los tests. Normalmente se usará useFreeTurn para restar un turno.
	 */
	public void setFreeTurns(int freeTurns) {
		if(freeTurns <= 0) { throw new IllegalArgumentException("freeTurns must be greather than 0");}
		refreshFreeTurns();
		this.freeTurns = freeTurns;
	}
	
	/**
	 * @return true if there are free turns, false if not.
	 */
	public boolean isFreeTurnsAvailable() { 
		return getFreeTurns() > 0;
	}
	
	/**
	 * Consume un turno.
	 * @return true si se ha podido usar el turno, o false si ya no le quedaban turnos libres.
	 */
	public boolean useFreeTurn() {
		if(isFreeTurnsAvailable()) {
			this.freeTurns -= 1;
			refreshFreeTurns();
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	@Override
    public String toString() {
		refreshFreeTurns();
		return Format.p(this.getClass(), new Object[]{
			"race", race,
			"ageState", age,
			"name", name, 
			"id", id,
//			"lair-user", lair.getUser().getLogin(),
			"borningDate", borningDate,
			"cocoonCloseUpDate", cocoonCloseUpDate,
			"freeTurns", freeTurns
		});
	}
	
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        result = prime * result
                + ((borningDate == null) ? 0 : borningDate.hashCode());
        result = prime
                * result
                + ((cocoonCloseUpDate == null) ? 0 : cocoonCloseUpDate
                        .hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((race == null) ? 0 : race.hashCode());
        return result;
    }

	/**
     * Se iguala este monstruo con otro.
     * No se tienen en cuenta ni la guarida ni los atributos del monstruo.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Monster other = (Monster) obj;
        if (age == null) {
            if (other.age != null)
                return false;
        } else if (!age.equals(other.age))
            return false;
        if (borningDate == null) {
            if (other.borningDate != null)
                return false;
        } else if (!borningDate.equals(other.borningDate))
            return false;
        if (cocoonCloseUpDate == null) {
            if (other.cocoonCloseUpDate != null)
                return false;
        } else if (!cocoonCloseUpDate.equals(other.cocoonCloseUpDate))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (race == null) {
            if (other.race != null)
                return false;
        } else if (!race.equals(other.race))
            return false;
        return true;
    }

//-- Private methods --//


}
