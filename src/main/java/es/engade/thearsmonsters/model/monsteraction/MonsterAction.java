package es.engade.thearsmonsters.model.monsteraction;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
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
	private List<LocalizableMessage> errors; // Errores que pueda tener la acci�n despu�s de ejecutarse
	private List<LocalizableMessage> notifications; // Mensajes que pueda tener la acci�n despu�s de ejecutarse
	
	/**
	 * Constructor genérico, la room puede ser de la guarida del monstruo o de fuera.
	 */
	public MonsterAction(MonsterActionType type, Monster monster, Room room) {
	    this.type = type;
	    this.monster = monster;
	    this.room = room;
	    this.errors = new ArrayList<LocalizableMessage>();
	    this.notifications = new ArrayList<LocalizableMessage>();
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
	public List<LocalizableMessage> getErrors() { return this.errors; }
	/**
	 * Añade un error a la lista de errores.
	 * Este método debe llamarse solamente desde type.isValid() (MonsterActionType al validar)
	 */
	public void addError(LocalizableMessage message) { this.errors.add(message); }
	public void addError(String messageKey, Object[] messageParams) { LocalizableMessage message = new ActionMessage(messageKey, messageParams); addError(message); }
	public void addError(String messageKey) { addError(messageKey, new Object[]{}); }
	public void addScopedError(String scopedMessageKey, Object[] messageParams) { addError(type.getMessagesKeyScope() + "error." + scopedMessageKey, messageParams); }
	public void addScopedError(String scopedMessageKey) { addScopedError(scopedMessageKey, new Object[]{}); }
	
	

	/**
	 * Lista de notificaciones que pueden aparecer después de ejecutar la acción,
	 * que son eventos como por ejemplo "El gimnasio ha subido a nivel 5".
	 */
	public List<LocalizableMessage> getNotifications() { return this.notifications; }
	/**
	 * Añade un mensaje a la lista de notifications.
	 * Este método debe llamarse durante la ejecuci�n de la acci�n para notificar sucesos.
	 */
	public void addNotification(LocalizableMessage message) { this.notifications.add(message); }
	public void addNotification(String messageKey, Object[] messageParams) { LocalizableMessage message = new ActionMessage(messageKey, messageParams); addNotification(message); }
	public void addNotification(String messageKey) { addNotification(messageKey, new Object[]{}); }
	public void addScopedNotification(String scopedMessageKey, Object[] messageParams) { addNotification(type.getMessagesKeyScope() + "notification." + scopedMessageKey, messageParams); }
	public void addScopedNotification(String scopedMessageKey) { addScopedNotification(scopedMessageKey, new Object[]{}); }
	
	
	

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
	
	
	//*** Action Messages ***//
	
	/**
	 * Clase auxiliar para albergar los mensajes de error y las notificaciones.
	 */
	public class ActionMessage implements LocalizableMessage {
		private String messageKey;
		private Object[] messageParams;
		public ActionMessage(String messageKey) {
			this(messageKey, new String[]{});
		}
		public ActionMessage(String messageKey, Object[] messageParams) {
			this.messageKey = messageKey;
			this.messageParams = messageParams;
		}
		public String getMessageKey() { return this.messageKey; }
		public Object[] getMessageParams() { return this.messageParams; }
		public String toString() { return messageKey + "(params{" + messageParams + "})"; }
	}
	
}
