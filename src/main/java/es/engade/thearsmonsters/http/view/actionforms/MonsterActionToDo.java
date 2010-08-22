package es.engade.thearsmonsters.http.view.actionforms;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;

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
