package es.engade.thearsmonsters.model.entities.monster.enums;

import java.util.HashMap;
import java.util.Map;



import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.attr.AttrBase;
import es.engade.thearsmonsters.model.entities.monster.attr.AttrCompose;
import es.engade.thearsmonsters.model.entities.monster.attr.AttrComposeSum;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public enum AttrType {
	// Attr Name	// code, AttrType,	icon
	Strenght		((byte) 1, AttrTypeClass.SimpleAttr),
	Agility			((byte) 2, AttrTypeClass.SimpleAttr),
	Intelligence	((byte) 3, AttrTypeClass.SimpleAttr),
	Vitality		((byte) 4, AttrTypeClass.SimpleAttr),
	Charisma		((byte) 5, AttrTypeClass.SimpleAttr),
	Happiness		((byte) 6, AttrTypeClass.SimpleAttr),
	
	HarvesterSkill			((byte) 51, AttrTypeClass.WorkSkill),
	ConstructorSkill		((byte) 52, AttrTypeClass.WorkSkill),
	
	Harvest			((byte) 101, AttrTypeClass.ComposeAttr),
	Construction	((byte) 102, AttrTypeClass.ComposeAttr);
	
	/**
	 * If you know the code then you can get the corresponding enum instance
	 */
	static public AttrType getFromCode(byte code) 
		throws InstanceNotFoundException {
		
		for(AttrType e : AttrType.values()) {
			if(code == e.code()) return e;
		}
			
		throw new InstanceNotFoundException(
        	"AttrType not found (code="+ code +")", 
        	AttrType.class.getName());
	}
	
	/**
	 * Método factoría abstracta que devuelve un atributo base (habilidad o atributo simple) a partir
	 * del código (BBDD) y del valor inicial del atributo (level + exp).
	 * Por defecto se asigna un ID inválido (para que desde el DAO se sepa que este atributo no se ha recuperado
	 * de la BD. Al utilizar el método initiateAttr de Monster ya se asiga un ID válido.
	 * @return Un atributo base
	 * @throws InstanceNotFoundException
	 */
	static public AttrBase newAttributeBase(AttrType typeCode, int value) 
		throws InstanceNotFoundException {
		
		return new AttrBase(typeCode, value, 0);
	}
	
	static public AttrCompose newAttrCompose(AttrType attrType, Map<AttrType, Attr> simpleAttrs, Map<AttrType, Attr> workSkills) {
		switch(attrType) {
		case Harvest: return new AttrComposeSum(attrType, 
				simpleAttrs.get(AttrType.Strenght), 
				workSkills.get(AttrType.HarvesterSkill));
		
		case Construction: return new AttrComposeSum(attrType, 
				simpleAttrs.get(AttrType.Strenght), 
				workSkills.get(AttrType.ConstructorSkill));
		// ...

		default: return null;
		}
	}
	
	/*
	 * Método que devuelve un Map<AttrType,Attr> con los atributos simples
	 * inicializados a cero.
	 * @return Map<AttrType,Attr> SimpleAttr at level 0
	 * */
	static public Map<AttrType,Attr> initializeSimpleAttrs(){	
		return initializeAttrsMapToZero(AttrTypeClass.SimpleAttr);
	}
	
	/*
	 * Método que devuelve un Map<AttrType,Attr> con los atributos simples
	 * inicializados a cero.
	 * @return Map<AttrType,Attr> SimpleAttr at level 0
	 * */
	static public Map<AttrType,Attr> initializeWorkSkills(){	
		return initializeAttrsMapToZero(AttrTypeClass.WorkSkill);
	}
	

	
	/*
	 * Método que devuelve un Map<AttrType,Attr> con los atributos del tipo
	 * AttrTypeClass(Simple, workskill, compose...) type inizializados a cero
	 * @param AttrTypeClass type The class of what attributes you want to initialize
	 * @return Map<AttrType,Attr> SimpleAttr at level 0
	 * */
	static public Map<AttrType,Attr> initializeAttrsMapToZero(AttrTypeClass type){	
		Map<AttrType,Attr> attrs = new HashMap<AttrType,Attr>();
		for(AttrType e : AttrType.values()) {
			if (e.attrClass == type){
				attrs.put(e, new AttrBase(e,0,0));
			}
		}
		return attrs;
	}
	private final byte code; // Codigo en la BBDD
	private final AttrTypeClass attrClass; // Clase de atributo (simple, compuesto, de trabajo, de batalla ...)
	AttrType(byte code, AttrTypeClass attrClass) {this.code = code; this.attrClass = attrClass;}
	public byte code()   { return code; }
	public AttrTypeClass getAttrClass() {return attrClass;}
	public boolean equals(AttrType attrType) { return this.code() == attrType.code();}
	
}
