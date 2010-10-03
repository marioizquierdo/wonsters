package es.engade.thearsmonsters.model.monsteraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;

/**
 * Lista de acciones para ejecutar.
 * Tiene dos estados: "sin ejecutar" y "ejecutado". Después de ser ejecutadas las accioines
 * (en monsterFacade.executeMonsterActions()), se pueden leer los mensajes conjuntos: notifications y errors
 * que devolvieron las ejecuciones de cada acción individual.
 */
public class MonsterActionsToDo implements Serializable {

    private static final long serialVersionUID = -1355362423677764260L;
	private List<MonsterActionToDo> list; // lista de acciones que hay que ejecutar
	private List<LocalizableMessage> notificationMessages; // notificaciones de las acciones después de la ejecución
	private List<LocalizableMessage> errorMessages; // mensajes de error después de la ejecución
	private boolean isExecuted; // Indica si las acciones ya han sido ejecutadas
	
	public MonsterActionsToDo(List<MonsterActionToDo> actionsToDo) {
	    this.list = actionsToDo;
	    this.notificationMessages = new ArrayList<LocalizableMessage>();
	    this.errorMessages = new ArrayList<LocalizableMessage>();
	    this.isExecuted = false;
    }
	
	//*** Common getters ***//
	public boolean isExecuted() { return isExecuted; }
	public void setExecuted(boolean isExecuted) { this.isExecuted = isExecuted; }
	public List<MonsterActionToDo> getList() { return list; }
	public List<LocalizableMessage> getNotificationMessages() { return notificationMessages; }
	public List<LocalizableMessage> getErrorMessages() { return errorMessages; }
	
	
	//*** Add messages ***//
	/**
	 * Añade todos los mensajes a la lista de mensajes.
	 */
	public void addAllNotificationMessages(List<LocalizableMessage> notificationMessages) {
		this.notificationMessages.addAll(notificationMessages);
	}
	public void addNotificationMessage(LocalizableMessage notificationMessage) {
		this.notificationMessages.add(notificationMessage);
	}
	public void addAllErrorMessages(List<LocalizableMessage> errorMessages) {
		this.errorMessages.addAll(notificationMessages);
	}
	public void addErrorMessage(LocalizableMessage errorMessage) {
		this.errorMessages.add(errorMessage);
	}
}
