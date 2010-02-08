package es.engade.thearsmonsters.model.entities.monster;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrTypeClass;

public class Monster implements Serializable {

	private static final long serialVersionUID = 5232245628626701489L;
	private Id id;
	private User user;
	private Map<AttrType, Attr> workSkills; 
	private Map<AttrType, Attr> simpleAttrs;
	//private List<Task> tasks;
	private MonsterAge ageState;
	private Calendar borningDate;
	private Calendar cocoonCloseUpDate;
	private String name;
	private MonsterRace race;
	
	public Monster(){}
	
	public Monster(Id Id, MonsterRace race, User user, String name, Calendar borningDate, Calendar cocoonCloseUpDate,
			MonsterAge ageState){
		// Se supone que los attrs son 'completos', es decir, que hay un atributo por cada AttrType.
		this.setId(Id);
		this.user= user;
		this.race = race;
		this.name = name;
		this.borningDate = borningDate;
		this.cocoonCloseUpDate = cocoonCloseUpDate;
		this.ageState = ageState;
		this.simpleAttrs = AttrType.initializeSimpleAttrs();
		this.workSkills = AttrType.initializeWorkSkills();
	}

	//-- GETTERS --//
	public Id getId() { return id; }
	public User getUser() {return user;}	
	public MonsterAge getAgeState(){return ageState;}	
	public Calendar getBorningDate(){return borningDate;}
	public Calendar getCocoonCloseUpDate(){return cocoonCloseUpDate;}
	public String getName(){return name;}
	public MonsterRace getRace(){return race;}
	
	//-- SETTERS --//
	public void setId(Id id) { this.id = id; }
	public void setName(String name) {this.name = name;}
	public void setAgeState(MonsterAge ageState) {this.ageState = ageState;}
	

	
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
		
	
//-- Private methods --//


}
