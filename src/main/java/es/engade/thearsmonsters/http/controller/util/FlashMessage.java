package es.engade.thearsmonsters.http.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;

/**
 * Gestiona una lista de mensajes que guardan en la request para ser leidas
 * con el customTag /WEBINF/tags/flashMessages.tagx <br/>
 * Las acciones del controlador pueden guardar mensajes cómodamente utilizando
 * los métodos estáticos de la clase, y después aparezerán en cuanto se
 * renderize la página.
 */
public final class FlashMessage implements LocalizableMessage, Serializable {
	
    private static final long serialVersionUID = 4810699405867335326L;
    
	public static final String FLASH_MESSAGES_REQUEST_ATTRIBUTE = "flashMessages";
	public static enum Status {INFO, GOOD_NEW, ERROR};
	
	/**
	 * Añade un nuevo mensaje en la lista "flashMessages" de la sesión.
	 * @param request de la acción correspondiente.
	 * @param messageKey Clave del mensaje con valor localizado en Messages.properties.
	 * 		Null significa que el mensajeKey es 'FlashMessage.default.ERROR' si el status es ERROR o
	 * 		'FlashMessage.default' en otro caso.
	 * @param params parámetros que pueda tener el mensaje con la clave messageKey, tienen que ir en
	 * 		el mismo orden en el que aparecen en el mensaje localizado ya que se aplican con 'fmt:message' de JSTL.
	 * @param status Tipo de mensaje. Se aplica como clase CSS en la vista. Null significa lo mismo que Status.INFO.
	 * @params position id del elemento del DOM en la vista donde se muestra el mensaje, dentro de este
	 * 		elemento, el mensaje se insertará de primero (en jQuery sería algo como $('#'+position).prepend(message);).
	 * 		Null es lo mismo que 'flashMessages', que es el id del propio customtag:flashMessages.
	 */
	public final static void show(HttpServletRequest request, String messageKey, 
			Object[] params, Status status, String position) {
		if(status == null) status = Status.INFO;
		if(messageKey == null || messageKey == "") messageKey = status.equals(Status.ERROR) ? 
				"FlashMessage.default.ERROR" : "FlashMessage.default";
		if(position == null) position = "flashMessages";
		getFlashMessagesList(request).add(new FlashMessage(messageKey, params, status, position));
	}
	
	/**
	 * Mete un nuevo mensaje en la lista "flashMessages" de la session, pero utilizando
	 * un objeto que implemente la interfaz LocalizableMessage en lugar de pasar messageKey y messageParams.
	 */
	public final static void show(HttpServletRequest request, LocalizableMessage message, Status status, String position) {
		show(request, message.getMessageKey(), message.getMessageParams(), status, position);
	}
	
	/**
	 * Mete un nuevo mensaje en la lista "flashMessages" de la session, pero utilizando
	 * un objeto que implemente la interfaz LocalizableMessage en lugar de pasar messageKey y messageParams.<br/>
	 * Es equivalente a show(request, message.getMessageKey(), message.getMessageParams(), null, null)
	 */
	public final static void show(HttpServletRequest request, LocalizableMessage message) {
		show(request, message.getMessageKey(), message.getMessageParams(), null, null);
	}
	
	/**
	 * params en lugar de ser un String[] es un String de elementos separados por comas,
	 * que se convierte a un String[] (eliminando los espacios). <br/>
	 * Invocar FlashMessage.show(request, null, "3, 10,  mi mama me mima  ", null, null) es lo mismo
	 * que FlashMessage.show(request, null, new String[]{"3", "10", "mi mama me mima"}, null, null).
	 */
	public final static void show(HttpServletRequest request, String messageKey, 
			String params, Status status, String position) {
		String[] paramsArray;
		if(params == null) {
			paramsArray = new String[]{};
		} else {
			paramsArray = params.split(","); // Los params se separan por comas
			for(int i=0; i<paramsArray.length; i++) { // se recortan los espacios (trim) de cada elemento
				paramsArray[i] = paramsArray[i].trim();
			}
		}
		
		show(request, messageKey, paramsArray, status, position);
	}
	
	/**
	 * Muestra el mensaje por defecto para ese status.
	 * Lo mismo que show(request, null, null, status, null)
	 */
	public final static void show(HttpServletRequest request, Status status) {
		show(request, null, new String[]{}, status, null);
	}
	
	/**
	 * Muestra el mensaje que haya en messageKey, sin utilizar parametros.
	 */
	public final static void show(HttpServletRequest request, String messageKey) {
		show(request, messageKey, new String[]{}, null, null);
	}
	
	/**
	 * Lo mismo que show(request, messageKey, params, null, null)
	 */
	public final static void show(HttpServletRequest request, String messageKey, String params) {
		show(request, messageKey, params, null, null);
	}
	
	/**
	 * Lo mismo que show(request, messageKey, params, null, null)
	 */
	public final static void show(HttpServletRequest request, String messageKey, Object[] params) {
		show(request, messageKey, params, null, null);
	}
	
	/**
	 * Para mostrar errores que implementen la interfaz LocalizableMessage.
	 * Es lo mismo que show(request, error, Status.ERROR, position);
	 */
	public final static void showError(HttpServletRequest request, LocalizableMessage error, String position) {
		show(request, error, Status.ERROR, position);
	}
	
	/**
	 * Para mostrar errores que implementen la interfaz LocalizableMessage.
	 * Es lo mismo que showError(request, error, null);
	 */
	public final static void showError(HttpServletRequest request, LocalizableMessage error) {
		show(request, error, Status.ERROR, null);
	}
	
	
	@SuppressWarnings("unchecked")
	private final static List<LocalizableMessage> getFlashMessagesList(HttpServletRequest request) {
    	HttpSession session = request.getSession(true);
		List<LocalizableMessage> flashMessages = (List<LocalizableMessage>) session.getAttribute(FLASH_MESSAGES_REQUEST_ATTRIBUTE);
		if(flashMessages == null) {
			flashMessages = new ArrayList<LocalizableMessage>();
			session.setAttribute(FLASH_MESSAGES_REQUEST_ATTRIBUTE, flashMessages);
		}
		return flashMessages;
	}

	private String messageKey;
	private Object[] params;
	private Status status;
	private String position;
	
	private FlashMessage(String messageKey, Object[] params, Status status, String position) {
		this.messageKey = messageKey;
		this.params = params;
		this.status = status;
		this.position = position;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public Object[] getMessageParams() {
		return params;
	}

	public Status getStatus() {
		return status;
	}

	public String getPosition() {
		return position;
	}
	
}
