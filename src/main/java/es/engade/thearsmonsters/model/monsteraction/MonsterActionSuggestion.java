package es.engade.thearsmonsters.model.monsteraction;

import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.util.Format;

/**
 * Sugerencia para realizar una acción de un Monstruo.
 * Al contrario que una MonsterAction, no necesita conocer los datos del
 * monstruo ni de la sala, ya que lo único que interesa aquí es qué
 * monstruo hace qué y en qué sala. Así no es necesaria ninguna consulta
 * compleja al modelo.
 * 
 * Esta clase sirve de proxy entre la interfaz (vista) y el modelo.
 */
public class MonsterActionSuggestion {
	
	private MonsterActionType monsterActionType;
	private String monsterId;
	private RoomType roomType;
	
	public MonsterActionSuggestion(MonsterActionType monsterActionType,
            String monsterId, RoomType roomType) {
	    this.monsterActionType = monsterActionType;
	    this.monsterId = monsterId;
	    this.roomType = roomType;
    }
	
	public MonsterActionSuggestion(String monsterActionType,
            String monsterId, String roomType) {
	    this.monsterActionType = MonsterActionType.valueOf(monsterActionType);
	    this.monsterId = monsterId;
	    this.roomType = RoomType.valueOf(roomType);
    }
	
	
	public MonsterActionType getMonsterActionType() {
    	return monsterActionType;
    }
	
	public void setMonsterActionType(MonsterActionType monsterActionType) {
    	this.monsterActionType = monsterActionType;
    }
	
	public void setMonsterActionType(String monsterActionType) {
		this.monsterActionType = MonsterActionType.valueOf(monsterActionType);
	}
	
	public String getMonsterId() {
    	return monsterId;
    }
	
	public void setMonsterId(String monsterId) {
    	this.monsterId = monsterId;
    }
	
	public RoomType getRoomType() {
    	return roomType;
    }
	
	public void setRoomType(RoomType roomType) {
    	this.roomType = roomType;
    }
	
	public void setRoomType(String roomType) {
		this.roomType = RoomType.valueOf(roomType);
	}
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"monsterId", monsterId,
			"roomType", roomType,
			"monsterActionType", monsterActionType
		});
	}

}
