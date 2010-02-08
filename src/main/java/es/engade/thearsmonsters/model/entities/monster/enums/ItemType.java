package es.engade.thearsmonsters.model.entities.monster.enums;

import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public enum ItemType {
	Illness	((byte) 1),
	Weapon	((byte) 2),
	Armor	((byte) 3);
	//...
	
	
	/**
	 * If you know the code then you can get the corresponding enum instance
	 */
	static public ItemType getFromCode(byte code) 
		throws InstanceNotFoundException {
		
		for(ItemType e : ItemType.values()) {
			if(code == e.code()) return e;
		}
			
		throw new InstanceNotFoundException(
        	"Item not found (code="+ code +")", 
        	ItemType.class.getName());
	}
	
	private final byte code; // Codigo en la BBDD
	ItemType(byte code) {this.code = code;}
	public byte code()   { return code; }
	public boolean equals(ItemType a) { return this.code() == a.code();}
}
