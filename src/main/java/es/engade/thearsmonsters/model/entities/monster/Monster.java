package es.engade.thearsmonsters.model.entities.monster;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import antlr.collections.List;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrTypeClass;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.types.Dormitories;
import es.engade.thearsmonsters.model.util.CalendarTools;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.model.monsteraction.*;

public class Monster implements Serializable {

	private static final long serialVersionUID = 5232245628626701489L;
	private Id id;
	private Lair lair;
	private Map<AttrType, Attr> workSkills; 
	private Map<AttrType, Attr> simpleAttrs;
	//private List<Task> tasks;
	private MonsterAge ageState;
	private Calendar borningDate;
	private Calendar cocoonCloseUpDate;
	private Calendar freeTurnsTimestamp;
	private int freeTurns;
	private List activities;
	private String name;
	private MonsterRace race;
	
	public Monster(){}
	
	public Monster(Id Id, MonsterRace race, Lair lair, String name, Calendar borningDate, Calendar cocoonCloseUpDate,
			MonsterAge ageState){
		// Se supone que los attrs son 'completos', es decir, que hay un atributo por cada AttrType.
		this.setId(Id);
		this.setLair(lair);
		this.setRace(race);
		this.setName(name);
		this.setBorningDate(borningDate);
		this.setCocoonCloseUpDate(cocoonCloseUpDate);
		this.setAgeState(ageState);
		this.simpleAttrs = AttrType.initializeSimpleAttrs();
		this.workSkills = AttrType.initializeWorkSkills();
		this.freeTurns = 11;

		/* Esto de aqui abajo no se si esta bien */
		this.freeTurnsTimestamp= CalendarTools.now();
		this.activities = null;
		
	}

	//-- GETTERS --//
	public Id getId() { return id; }
	public Lair getLair() {return lair;}	
	public MonsterAge getAgeState(){return ageState;}	
	public Calendar getBorningDate(){return borningDate;}
	public Calendar getCocoonCloseUpDate(){return cocoonCloseUpDate;}
	public String getName(){return name;}
	public MonsterRace getRace(){return race;}
	public int getFreeTurns() {return freeTurns;}
	
	//-- SETTERS --//
	public void setId(Id id) { this.id = id; }
	public void setLair(Lair lair) { this.lair = lair; }
	public void setRace(MonsterRace race) { this.race = race; }
	public void setName(String name) { this.name = name; }
	public void setBorningDate(Calendar borningDate) { this.borningDate = borningDate; }
	public void setCocoonCloseUpDate(Calendar cocoonCloseUpDate) { this.cocoonCloseUpDate = cocoonCloseUpDate; }
	public void setAgeState(MonsterAge ageState) { this.ageState = ageState; }
	

	
	public void metamorphosisToAdul(){
		this.ageState = MonsterAge.Adult;
	}
	
	
	//-- Methods for monster attributes --//
	
	/**
	 * Añade experiencia al atributo del tipo indicado.
	 * Si la experiencia es mayor que 100, sube un nivel más al monstruo.
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
	
	
	
	
	//-- METHODS TO MANAGE MONSTER JOB TURNS --//	
	
	/** 
	 * Se obtiene los turnos que ya estan asignados a este monstruo en otras actividades
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
	 * Refresca el numero de turnos libres disponibles para el monstruo
	 */
	public void refreshFreeTurns(){
		
		int sleepHours,turnsPerDay;
		float daysFromTimestamp;
		Calendar calendarToday = CalendarTools.now();
		Dormitories dormitorie = (Dormitories) this.lair.getRoom(RoomType.Dormitories);
		
		sleepHours = 14 - dormitorie.getLevel();
		turnsPerDay = 24 - sleepHours - this.taskHours();
		daysFromTimestamp = CalendarTools.distanceInDays(this.freeTurnsTimestamp, calendarToday);
		
		this.freeTurns += turnsPerDay * daysFromTimestamp;
		this.freeTurnsTimestamp = Calendar.getInstance();
			
	}
	
	/**
	 * Consume un turno y refresca el atributo turnsPerDay
	 */
	public void useFreeTurns(){
		this.freeTurns-=1;
		this.refreshFreeTurns();
	}
	
	
	
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"race", race,
			"ageState", ageState,
			"name", name, 
			"id", id,
			"lair-user", lair.getUser().getLoginName(),
			"borningDate", borningDate,
			"cocoonCloseUpDate", cocoonCloseUpDate,
		});
	}
	
	/**
	 * Se iguala este monstruo con otro.
	 * No se tienen en cuenta ni la guarida ni los atributos del monstruo.
	 */
	public boolean equals(Monster m) {
		return 
			this.getRace().equals(m.getRace()) &&
			this.getAgeState().equals(m.getAgeState()) &&
			this.getName().equals(m.getName()) &&
			this.getId().equals(m.getId()) &&
			CalendarTools.equals(this.getBorningDate(), m.getBorningDate()) &&
			CalendarTools.equals(this.getCocoonCloseUpDate(), m.getCocoonCloseUpDate());
	}
	
	
//-- Private methods --//


}
