package es.engade.thearsmonsters.model.entities.room.enums;

/**
 * DB field for determine the works type of a room
 */
public enum WorksType {
	Upgrading 	((byte)1),
	Enlarging	((byte)2);
	
	private final byte code; // Codigo del tipo de obras
	WorksType(byte code) {this.code = code;}
	public byte code()   { return code; }
}