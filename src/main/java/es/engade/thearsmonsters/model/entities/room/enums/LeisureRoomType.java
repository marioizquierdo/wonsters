package es.engade.thearsmonsters.model.entities.room.enums;

import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

/**
 * Subconjunto de RoomType
 */
public enum LeisureRoomType {
	EyeOfTheLife		(RoomType.EyeOfTheLife) /*, MOCK IMPLEMENTATION mientras no haya leisures de verdad
	MetalLeisureRoom		(RoomType.MetalLeisureRoom),
	ChillOutLeisureRoom		(RoomType.ChillOutLeisureRoom),
	TechnoLeisureRoom		(RoomType.TechnoLeisureRoom),
	ReggaetonLeisureRoom	(RoomType.ReggaetonLeisureRoom),
	IndieRockLeisureRoom	(RoomType.IndieRockLeisureRoom)*/;
	
	
	/**
	 * Devuelve una sala aleatoria
	 * @return a LeisureRoomType random instance
	 */
	public static LeisureRoomType getRandomType() {
		LeisureRoomType[] types = LeisureRoomType.values();
		int randIndex = (int)Math.floor(Math.random() * types.length);
		return types[randIndex];
	}
	
	private final RoomType type; // Tipo correspondiente en RoomType
	LeisureRoomType(RoomType type) {this.type = type;}
	
}