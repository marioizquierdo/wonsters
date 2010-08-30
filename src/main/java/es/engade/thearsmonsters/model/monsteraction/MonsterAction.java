package es.engade.thearsmonsters.model.monsteraction;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.util.Format;

public class MonsterAction {
	
	private MonsterActionType type;
	private Monster monster;
	private Room room;
	private List<String> errors;
	
	/**
	 * Constructor genérico, la room puede ser de la guarida del monstruo o de fuera.
	 */
	public MonsterAction(MonsterActionType type, Monster monster, Room room) {
	    this.type = type;
	    this.monster = monster;
	    this.room = room;
	    this.errors = new ArrayList<String>();
    }
	
	/**
	 * Acción que se realiza en la misma guarida que el monstruo.
	 */
	public MonsterAction(MonsterActionType type, Monster monster, RoomType roomType) {
		this(type, monster, monster.getLair().getRoom(roomType));
	}
	
	
	//*** Getters ***//

	public MonsterActionType getType() { return type; }
	public Monster getMonster() { return monster;  }
	public Room getRoom() { return room; }
	public RoomType getRoomType() { return room.getRoomType(); }
	public Lair getLair() { return room.getLair(); }
	public List<MonsterAge> getAllowedMonsterAges() { return type.getAllowedMonsterAges(); }
	public List<RoomType> getAllowedRoomTypes() { return type.getAllowedRoomTypes(); }
	
	
	//*** Validate and Execute ***//
	
	/**
	 * Devuelve true si esta action es válida, es decir, si se puede ejecutar.
	 * Se puede usar desde aquí o bien directamente desde el MonsterActionType.isValid
	 */
	public boolean isValid() {
		return type.isValid(this);
	}
	
	/**
	 * Valida la tarea y la ejecuta, con ello modifica el estado del monstruo, de la sala y de la guarida. 
	 * @return true si la tarea es válida y se ha ejecutado, false en caso contrario (si devuelve false, ningún cambio ha sido realizado).
	 */
	public boolean execute() {
		return type.execute(this);
	}
	

	/**
	 * Lista de errores que pueden aparecer después de validar la acción.
	 * Si al ejecutar isValid() se devuelve true, getErrors devolverá una lista vacía,
	 * si en cambio isValid() devuelve false, getErrors debe contener los errores derivados de la validación.
	 */
	public List<String> getErrors() {
		return this.errors;
	}
	
	
	/**
	 * Añade un error a la lista de errores.
	 * Este método debe llamarse solamente desde type.isValid() (MonsterActionType al validar)
	 */
	public void addError(String errorKey) {
		this.errors.add(errorKey);
	}
	

	//*** Other ***//
	
	/**
	 * Devuelve una sugerencia para realizar esta misma action.
	 * Esto es una instancia de MonsterActionSuggestion con los datos de esta action.
	 */
	public MonsterActionSuggestion getSuggestion() {
		return type.getSuggestion(this);
	}
	
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"type", type,
			"monster", monster,
			"room", room
		});
	}
	
}
