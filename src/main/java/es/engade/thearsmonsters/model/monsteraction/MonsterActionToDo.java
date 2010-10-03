package es.engade.thearsmonsters.model.monsteraction;

import java.io.Serializable;

import com.google.appengine.api.datastore.KeyFactory;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

/**
 * Value Object que Encapsula lainformación necesaria para ejecutar una acción.
 */
public class MonsterActionToDo implements Serializable {

    private static final long serialVersionUID = -1355362423677764260L;
	private MonsterActionType actionType; // tipo de tarea
	private String monsterId; // monstruo que realiza la acción
	private RoomType roomType; // tipo de sala dentro de la guarida donde se ejecuta la acción.
	private int turnsToUse; // cuantos turnos se van a utilizar sucesivamente en la tarea.
	
	public MonsterActionToDo(MonsterActionType type,
            String monsterId, RoomType roomType, int turnsToUse) {
	    this.actionType = type;
	    this.monsterId = monsterId;
	    this.roomType = roomType;
	    this.turnsToUse = turnsToUse;
    }
	
	/**
	 * Construye una MonsterAction para ejecutar.
	 * @param lair Guarida donde se ejecuta la acción. Debe ser la misma donde se encuentra el monsterId.
	 */
	public MonsterAction buildMonsterAction(Lair lair) {
    	Monster monster;
        try {
	        monster = lair.getMonster(KeyFactory.stringToKey(this.getMonsterId()));
        } catch (InstanceNotFoundException e) {
	        throw new IllegalArgumentException("The monster (monsterId: "+ monsterId +") should be in the lair (of "+ lair.getLogin() +"), and it isn't.", e);
        }
    	Room room = lair.getRoom(this.getRoomType());
    	MonsterActionType actionType = this.getActionType();
		return new MonsterAction(actionType, monster, room);
	}
	
	public void setTurnsToUse(Integer turnsToUse) {
    	this.turnsToUse = turnsToUse;
    }
	public MonsterActionType getActionType() {
    	return actionType;
    }
	public void setActionType(MonsterActionType actionType) {
    	this.actionType = actionType;
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
	public Integer getTurnsToUse() {
    	return turnsToUse;
    }
	
	@Override
    public String toString() {
	    return "MonsterActionToDo [monsterId=" + monsterId
	            + ", roomType=" + roomType + ", turnsToUse=" + turnsToUse
	            + ", actionType=" + actionType + "]";
    }

	
}
