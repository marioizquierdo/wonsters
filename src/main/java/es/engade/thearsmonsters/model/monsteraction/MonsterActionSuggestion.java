package es.engade.thearsmonsters.model.monsteraction;

import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.util.Format;

/**
 * Sugerencia para realizar una acción de un Monstruo.
 * En este objeto se encapsula información que es necesaria en la vista
 * para que el jugador decida en que tarea gastar los turnos libres de
 * su monstruo.
 */
public class MonsterActionSuggestion {
	
	private MonsterActionType monsterActionType; // tipo de tarea a realizar.
	private String monsterId; 				// identificador del monstruo que puede realizar la accion
	private RoomType roomType;  			// sala donde puede realizar esa acción (de la misma guarida que el monstruo)
	private Integer maxTurnsToAssign; 		// cantidad de turnos máxima que se puede asignar a esta tarea. Null significa "indefinido" (sin máximo)
	private String infoMessageKey; 			// clave del mensaje. Suele ser "Monster.actions.type.{monsterActionType}.info"
	private Object[] infoMessageParams; 		// array de parámetros numéricos para utilizar en el mensaje de ayuda. (la clave del mensaje se obtiene del tipo de tarea).
	private int targetValue;				// valor que modifica la tarea. Por ejemplo, en Construcción es el esfuerzo realizado en la obra.
	private int targetValueIncreasePerTurn; // cantidad del "target value" que se incrementa en cada turno gastado por el monstuo (ej: en Construcción es el atributo construcción del monstruo).
	private String targetValueMessageKey; 	// clave del mensaje informativo para el "target value". Suele ser "Monster.actions.type.{monsterActionType}.targetValue"
	private Object[] targetValueMessageParams; // array de parámetros numéricos para utilizar en el mensaje informativo del "target value".
		
	public MonsterActionSuggestion(MonsterActionType monsterActionType,
            String monsterId, RoomType roomType, Integer maxTurnsToAssign,
            String infoMessageKey, Object[] infoMessageParams, int targetValue,
            int targetValueIncreasePerTurn, String targetValueMessageKey,
            Object[] targetValueMessageParams) {
	    this.monsterActionType = monsterActionType;
	    this.monsterId = monsterId;
	    this.roomType = roomType;
	    this.maxTurnsToAssign = maxTurnsToAssign;
	    this.infoMessageKey = infoMessageKey;
	    this.infoMessageParams = infoMessageParams;
	    this.targetValue = targetValue;
	    this.targetValueIncreasePerTurn = targetValueIncreasePerTurn;
	    this.targetValueMessageKey = targetValueMessageKey;
	    this.targetValueMessageParams = targetValueMessageParams;
    }
	
	public MonsterActionType getMonsterActionType() { return monsterActionType; }
	public String getMonsterId() { return monsterId; }
	public RoomType getRoomType() { return roomType; }
	public Integer getMaxTurnsToAssign() { return maxTurnsToAssign; }
	public String getInfoMessageKey() { return infoMessageKey; }
	public Object[] getInfoMessageParams() { return infoMessageParams; }
	public int getTargetValue() { return targetValue; }
	public int getTargetValueIncreasePerTurn() { return targetValueIncreasePerTurn; }
	public String getTargetValueMessageKey() { return targetValueMessageKey; }
	public Object[] getTargetValueMessageParams() { return targetValueMessageParams; }

	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"monsterId", monsterId,
			"roomType", roomType,
			"monsterActionType", monsterActionType,
		    "maxTurnsToAssign", maxTurnsToAssign,
		    "infoMessageParams", infoMessageParams,
		    "targetValue", targetValue,
		    "targetValueIncreasePerTurn", targetValueIncreasePerTurn,
		    "targetValueMessageParams", targetValueMessageParams
		});
	}
}
