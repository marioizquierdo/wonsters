package es.engade.thearsmonsters.model.entities.monster;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.ThearsmonstersEntity;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrTypeClass;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterFavoriteMusic;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.util.DateTools;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.model.util.GameConf;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Monster extends ThearsmonstersEntity implements Serializable {

    private static final long serialVersionUID = 20100305L;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
    
    @Persistent
	private Lair lair;
    
    @Persistent(serialized="true", defaultFetchGroup="true") 
	private Map<AttrType, Attr> workSkills;
    
    @Persistent(serialized="true", defaultFetchGroup="true") 
	private Map<AttrType, Attr> simpleAttrs;
    
    @Persistent
	private MonsterAge age;
    
    @Persistent
	private Date borningDate;
    
    @Persistent
	private Date cocoonCloseUpDate;
    
    @Persistent
    private Date growOldDate;
    
    @Persistent
    private Date deceaseDate;
    
    @Persistent
    private MonsterFavoriteMusic favoriteMusic;
    
    @Persistent
	private Date freeTurnsTimestamp;
    
    @Persistent
	private int freeTurns;
    
//    @Persistent(serialized="true", defaultFetchGroup="true")
//	private List<MonsterActivity> activities;
    
    @Persistent
	private String name;
    
    @Persistent
	private MonsterRace race;
    	
	public Monster(){
//	    activities = new ArrayList<MonsterActivity>();
	}
	
	/**
	 * Constructor del monstruo con todos los datos necesarios.
	 */
	public Monster(Lair lair, MonsterRace race, String name, Date borningDate, Date cocoonCloseUpDate, MonsterAge ageState) {
		this.setLair(lair);
		this.setRace(race);
		this.setName(name);
		this.setFavoriteMusic(this.getFavoriteMusic());
		this.setBorningDate(borningDate);
		this.setCocoonCloseUpDate(cocoonCloseUpDate);

		// Se supone que los attrs son 'completos', es decir, que hay un atributo por cada AttrType.
		// Por defecto los crea e inicializa todos a cero.
		this.simpleAttrs = AttrType.initializeSimpleAttrs();
		this.workSkills = AttrType.initializeWorkSkills();
		
		this.freeTurnsTimestamp = DateTools.now();
		this.freeTurns = 0;
		
		// Como el minimo de esperanza de vida es 1, debemos fraccionar ese tiempo
		// para poder envejecer.
		long minutesToGrowOld = Math.round(
				Double.valueOf(race.getLifeExpectancyDays() * 24 * 60
				* MonsterAge.PERCENT_OF_LIFE_EXPECTANCY_TO_GROW_OLD) / 100
			);
		minutesToGrowOld += (Math.random() - 0.5) * 2 
			* Double.valueOf(minutesToGrowOld * MonsterAge.PERCENT_OF_VARIATION_TO_GROW_OLD)/100;
		this.growOldDate = DateTools.new_byMinutesFrom(borningDate, minutesToGrowOld);
		long minutesToDecease = Math.round(race.getLifeExpectancyDays() * 24 * 60 +
				(Math.random() - 0.5) * 2
				* Double.valueOf((race.getLifeExpectancyDays() * 24 * 60) 
				* MonsterAge.PERCENT_OF_LIFE_EXPECTANCY_VARIATION) / 100
			);
		this.deceaseDate = DateTools.new_byMinutesFrom(borningDate, minutesToDecease);
		
//		this.activities = new ArrayList<MonsterActivity>(); // No implementado por ahora
		this.setAge(ageState);
	}
	
	/**
	 * Crear un nuevo monstruo recién nacido con todos sus datos iniciales.
	 * Nota: no lo añade a la guarida, después de crearlo hay que añadirlo 
	 * usando lair.addMonster(monster);
	 */
	public Monster(Lair lair, MonsterRace race, String name) {
		this(lair, race, name, DateTools.now(), null, MonsterAge.Child);
		
		// Set initial attribute levels (100 exp = 1 level)
		addExp(AttrType.Strenght, race.getStrenghtLevel() * 100);
		addExp(AttrType.Agility, race.getAgilityLevel() * 100);
		addExp(AttrType.Vitality, race.getVitalityLevel() * 100);
		addExp(AttrType.Intelligence, race.getIntelligenceLevel() * 100);
		addExp(AttrType.Charisma, race.getCharismaLevel() * 100);
		
		//Give some happiness to new born monsters
		addExp(AttrType.Happiness, 50 * 100);
		
		// Set initial free turns
		setFreeTurns(GameConf.getTurnsPerDay());
	}

	//-- simple GETTERS --//
	public Key getIdKey() { return id; }
	public Lair getLair() { return lair; }
	public Date getBorningDate() { return borningDate; }
	public Date getCocoonCloseUpDate() { return cocoonCloseUpDate; }
	public Date getGrowOldDate() { return growOldDate; }
	public Date getDeceaseDate() { return deceaseDate; }
	public String getName() { return name; }
	public MonsterRace getRace() { return race; }
	// getFreeTurns y isFreeTurnsAvailable están en la sección de las monster actions.
	
	public MonsterFavoriteMusic getFavoriteMusic() {
		if (favoriteMusic == null) {
			int index = Math.abs(this.getName().toString().hashCode()) % MonsterFavoriteMusic.values().length;
			favoriteMusic = MonsterFavoriteMusic.values()[index]; 
		}
		return favoriteMusic;
	}
	
	public void setFavoriteMusic(MonsterFavoriteMusic favoriteMusic) {
		this.favoriteMusic = favoriteMusic;
	}
	
	public int getAgeDays() {
	    return (int) DateTools.daysBetween(getBorningDate(), DateTools.now());
	}
	
	//Calcula la hora en la que tendremos el proximo turno libre
	public long getNextTurnTime() {
		long millisPerDay = 24*60*60*1000;
		long millisPerTurn = millisPerDay / GameConf.getTurnsPerDay();
		int numberOfTurns = (int)Math.ceil((Double.valueOf(DateTools.now().getTime() - this.getBorningDate().getTime()) / millisPerTurn));
		long nextTurnTime = this.getBorningDate().getTime() + numberOfTurns * millisPerTurn;
		return nextTurnTime;
		
//		int minutesPerDay = 24*60;
//		int minutesPerTurn = minutesPerDay / GameConf.getTurnsPerDay();
//		Date dateWhenZeroTurns = DateTools.new_byMinutesFrom(freeTurnsTimestamp, - (freeTurns * minutesPerTurn));
//		return DateTools.new_byMinutesFrom(dateWhenZeroTurns, (getFreeTurns() + 1) * minutesPerTurn).getTime();
	}
	
	/**
	 * Number from 0 to 100 that represents the percentage of the monster life lived yet (100% when dies)
	 */
	public int getAgePercentageLived(){
	    return getAgeDays() * 100 / race.getLifeExpectancyDays();
	}
	
	//-- simple SETTERS --//
	public void setIdKey(Key id) { this.id = id; }
	public void setLair(Lair lair) { this.lair = lair; }
	public void setRace(MonsterRace race) { this.race = race; }
	public void setName(String name) { this.name = name; }
	public void setBorningDate(Date borningDate) { this.borningDate = borningDate; }
	public void setCocoonCloseUpDate(Date cocoonCloseUpDate) { this.cocoonCloseUpDate = cocoonCloseUpDate; }
	public void setGrowOldDate(Date growOldDate) { this.growOldDate = growOldDate; }
	public void setDeceaseDate(Date deceaseDate) { this.deceaseDate = deceaseDate; }
	// setFreeTurns está en la sección de las monster actions.

	
	
	//-- Monster Age --//

	public MonsterAge getAge() { 
		correctCocoonOrAdultAges();
		return this.age;
	}

	/**
	 * @throws MonsterGrowException si se intenta hacer un cambio de edad incorrecto
	 */
	public void setAge(MonsterAge age) throws MonsterGrowException {
		if(!MonsterAge.validateAgeFlow(this.age, age)) throw new MonsterGrowException(this.getIdKey(), age);
		this.age = age;
		correctCocoonOrAdultAges();
	}
	
	public void setAgeForced(MonsterAge age) {
		this.age = age;
	}
	
	/**
	 * Fija la fecha para que salga del capuyo (cocoonCloseUpDate) y cambia la edad
	 * a AgeState.Adult o a AgeState.Cocoon (depende si el tiempo de metamorfosis es diferente de cero o no).
	 * @throws MonsterGrowException si la edad del monstruo no es AgeState.Child
	 */
	public void metamorphosisToAdult() throws MonsterGrowException {	
		Date metamorphosisTime = DateTools.new_byMinutesFromNow(race.getMetamorphosisMinutes());
		setCocoonCloseUpDate(metamorphosisTime);
		setAge(MonsterAge.Cocoon);
	}
	
	
	//-- Monster Attributes and Skills --//
	
	/**
	 * Añade experiencia al atributo del tipo indicado.
	 * Si la experiencia es mayor que 100, sube un nivel más al monstruo.
	 * @param exp cantidad de experiencia añadida. Tener en cuenta que cada nivel tiene 100 de experiencia.
	 */
	public boolean addExp(AttrType type, int exp) {
		return this.getAttr(type).addExp(exp);
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
	 * Igual que getAttrs pero las claves del hash son Strings.
	 * Necesario para acceder a los atributos desde JSTL, por ejemplo:
	 *   ${monster.attr["WorkSkill"].level}
	 */
	public Map<String, Attr> getAttr() {
		return stringfyKeys(getAttrs());
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
	
	/**
	 * Igual que getComposeAttrs pero las claves del hash son Strings.
	 * Necesario para acceder a los atributos desde JSTL, por ejemplo:
	 *   ${monster.composeAttr.WorkSkill.level}
	 */
	public Map<String, Attr> getComposeAttr() {
		return stringfyKeys(getComposeAttrs());
	}
	
	public Map<AttrType, Attr> getComposeAttrs() {
		Map<AttrType,Attr> attrs = new HashMap<AttrType,Attr>();
		for(AttrType e : AttrType.values()) {
			if (e.getAttrClass() == AttrTypeClass.ComposeAttr){
				attrs.put(e, this.getComposeAttr(e));
			}
		}
		return attrs;
	}
	
	public Attr getSimpleAttr(AttrType type) { 
	    return getSimpleAttrs().get(type); 
	}
	
	/**
	 * Igual que getSimpleAttrs pero las claves del hash son Strings.
	 * Necesario para acceder a los atributos desde JSTL, por ejemplo:
	 *   ${monster.simpleAttr["Agility"].level}
	 */
	public Map<String, Attr> getSimpleAttr() {
		return stringfyKeys(getSimpleAttrs());
	}
	
	public Map<AttrType, Attr> getSimpleAttrs() {
	    if (this.simpleAttrs == null) {
	        this.simpleAttrs = AttrType.initializeSimpleAttrs();
	    }
	    return this.simpleAttrs; 
	}
	
	public void setSimpleAttrs(Map<AttrType, Attr> simpleAttrs) { 
	    this.simpleAttrs = simpleAttrs; 
	}
	
	public Attr getWorkSkill(AttrType type) {
	    return getWorkSkills().get(type); 
	}
	
	/**
	 * Igual que getWorkSkills pero las claves del hash son Strings.
	 * Necesario para acceder a los atributos desde JSTL, por ejemplo:
	 *   ${monster.workSkills["HarvesterSkill"].level}
	 */
	public Map<String, Attr> getWorkSkill() {
		return stringfyKeys(getWorkSkills());
	}
	
	/**
	 * Devuelve el atributo de trabajo que tenga mayor nivel
	 */
	public Attr getBestWorkSkill(){
		Attr best = getWorkSkills().get(AttrType.HarvesterSkill); // por poner uno, se comienza con HarvesterSkill
	    for(Attr skill: getWorkSkills().values()) {
	    	if(skill.getLevel() > best.getLevel()) {
	    		best = skill;
	    	} else if(skill.getLevel() == best.getLevel()) { // en caso de empate, gana el que tenga más experiencia
	    		if(skill.getExp() > best.getExp()) {
	    			best = skill;
	    		}
	    	}
	    }
	    return best;
	}
	
	public Map<AttrType, Attr> getWorkSkills() {
	    if (this.workSkills == null) {
            this.workSkills = AttrType.initializeWorkSkills();
        }
	    return this.workSkills; 
	}
	
	public void setWorkSkills(Map<AttrType, Attr> workSkills) { 
	    this.workSkills = workSkills; 
	}
	
	
	
	
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

		long millisPerDay = 24*60*60*1000;
		long millisPerTurn = millisPerDay / GameConf.getTurnsPerDay();
		int numberOfTurns = (int)Math.floor((Double.valueOf(DateTools.now().getTime() - this.getBorningDate().getTime()) / millisPerTurn));
		freeTurns = getFreeTurns(timestamp); // turnos libres que hay en ese momento.
		freeTurnsTimestamp = new Date(this.getBorningDate().getTime() + numberOfTurns * millisPerTurn);
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
		
		// Los monstruos muertos no tienen turnos libres.
		if(getAge() == MonsterAge.Dead) return 0;
		
		int turnsPerDay = GameConf.getTurnsPerDay() - this.taskHours(); // turnos acumulados cada día
		double daysFromTimestamp = DateTools.daysBetween(this.freeTurnsTimestamp, timestamp);
		
		// En el momento timestamp, los freeTurns son los que había en el instante freeTurnsTimestamp
		// mas el número de turnos acumulados (según los dias que han pasado desde el último timpestamp)
		int newFreeTurns = this.freeTurns;
		newFreeTurns += ((int) (turnsPerDay * daysFromTimestamp));
		if (newFreeTurns > getMaxNumberOfTurns()) {
			newFreeTurns = getMaxNumberOfTurns();
		}
		return  newFreeTurns;
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
	
	/**
	 * Numero maximo de turnos que puede acumular este monstruo
	 */
	public int getMaxNumberOfTurns() {
		return GameConf.getMaxNumberOfTurns();
	}
	
	
	//-- Object overrides --//
	
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
 
    
    /**
	 * getAge no devuelve simplemente this.age por el siguiente motivo:
	 * No hay ningún evento que transforme al monstruo de Cocoon a Adult, o a Dead,
	 * solamente el simple paso del tiempo (cuando getCocoonCloseUpDate() quede en el pasado).
	 * Entoces para que esto se gestione de forma automática (sin tener que forzar un
	 * reload en la base de datos), lo que se hace es un workaround:
	 * sea cual sea el valor de this.age, los métodos getAge() y setAge()
	 * devolverán siempre el valor adecuado utilizando este método.
	 */
	private void correctCocoonOrAdultAges() {
    	if(getCocoonCloseUpDate() != null) {
	    	// Adulto que aun no ha salido del capullo debe ser Cocoon
	    	if(this.age.equals(MonsterAge.Adult) &&
				getCocoonCloseUpDate().after(DateTools.now())) {
				this.age = MonsterAge.Cocoon;
			}
	    	
	    	// Capullo que ya ha salido del capullo debe ser Adult
	    	if(this.age.equals(MonsterAge.Cocoon) &&
				getCocoonCloseUpDate().before(DateTools.now())) {
	    		this.age = MonsterAge.Adult;
			}
    	}
    	//TODO: Dado que no hay un demonio que compruebe periodicamente
    	// los cambios de edad, es posible que estos no se hagan secuencialmente
    	// y por tanto un bicho pase directamente de adulto a muerto, o de
    	// joven a viejo o muerto.
    	if(this.age.equals(MonsterAge.Adult) && 
    			getGrowOldDate().before(DateTools.now())) {
    		this.age = MonsterAge.Old;
    	}
    	if(this.age.equals(MonsterAge.Old) &&
    			getDeceaseDate().before(DateTools.now())) {
    		this.age = MonsterAge.Dead;
    	}
    }
	
	/**
	 * Dado un Hash de Atributos con claves AttrType, devuelve
	 * otro igual pero las claves son Strings equivalentes.
	 */
	private Map<String, Attr> stringfyKeys(Map<AttrType, Attr> attrsMap) {
		Map<String, Attr> result = new HashMap<String, Attr>();
		for(Attr attr: attrsMap.values()) {
			result.put(attr.getType().name(), attr);
		}
		return result;
	}

	public void touch() {
		lair.touchInstance();
	}
	
}
