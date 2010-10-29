package es.engade.thearsmonsters.http.controller.util;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Por alguna razón el saveErrors de Struts no nos funciona bien.
 * Por lo tanto hemos tenido que hacer nuestro propio para capturar los errores luego desde la vista
 * con nuestro propio customtag:form_error
 * 
 * El método saveErrors se pone en esta clase para refactorizar el saveErrorsFixed que hay en
 * ThearsmonstersDefaultAction y en DefaultActionForm.
 */
public class SaveErrorsFixed {
	
	private static String DEFAULT_SUFIX = "FormError";
	
    /**
     * 
     * @param request
     * @param errors Mensajes de error asociados a alguna property del formulario que se van a mostrar
     * @param suffix Por defecto se pone "FormError", que se añade a la property para darle nombre a la variable que se
     * 			va a guardar en la request. Sirve para poner sufijos distintos en caso de que haya varios formularios en la
     * 			misma página que tengan la misma property (por ejemplo en la portada el de registrarse y el de login comparten login y password).
     * @return true si hay errores a guardar.
     */
	@SuppressWarnings("unchecked")
    public static boolean saveErrors(HttpServletRequest request, ActionMessages errors, String othersuffix) {
    	String suffix = othersuffix == null ? DEFAULT_SUFIX : othersuffix;
		Iterator<String> itProp = errors.properties();
    	boolean hasErrors = itProp.hasNext();
        while (itProp.hasNext()) {
        	String prop = itProp.next();
        	ActionMessage am = (ActionMessage)errors.get(prop).next();
        	request.setAttribute(prop + suffix, am.getKey());
        }
        return hasErrors;
    }
	
	public static boolean saveErrors(HttpServletRequest request, ActionMessages errors) {
    	return saveErrors(request, errors, DEFAULT_SUFIX);
    }
}
