package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.SaveErrorsFixed;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.struts.action.DefaultAction;


/**
 * A base in game Action. Be sure that myLair is in the session when the user
 * is logged and clear session cached data in case of exception.<br/>
 * Subclasses just need to redefine doExecuteGameAction.<br/>
 * If a subclass needs to redefine doOnException, must call the extraDoOnException method inside.
 * A Struts default org.apache.struts.actions.ForwardAction may be changed for this one if
 * 	the view uses the myLair session property.
 */
public class ThearsmonstersDefaultAction extends DefaultAction {
	
    @Override
    protected ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException {
    	
    	// Common stuff here (actions that appear on all thearsmonsters requests)
    	
    	return doExecuteGameAction(mapping, form, request, response);
    }
    
    /**
     * Extra specific on error stuff for Thearsmonsters Actions 
     */
    @Override
    protected void extraDoOnException(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response, Exception exception)
        throws IOException, ServletException {
    	
    	// Some session data may be unstable. Delete it.
    	SessionManager.removeMyLair(request);
    	
    }
    
    /**
     * Normal Thearsmonsters actions must only extend that method.
     * If an action reimplements doExecute, this one is not necesary. 
     */
    @SuppressWarnings("deprecation")
    protected ActionForward doExecuteGameAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException {
    	// By default, acts like the standard org.apache.struts.actions.ForwardAction
    	ActionForward retVal = new ActionForward(mapping.getParameter());
        retVal.setContextRelative(true);
        return retVal;
    }

    
    /**
     * Por alguna razón el saveErrors de Struts no nos funciona bien.
     * Por lo tanto hemos tenido que hacer nuestro propio para capturar los errores luego desde la vista
     * con nuestro propio customtag:form_error
     * @param request
     * @param errors Mensajes de error asociados a alguna property del formulario que se van a mostrar
     * @param suffix Por defecto se pone "FormError", que se añade a la property para darle nombre a la variable que se
     * 			va a guardar en la request. Sirve para poner sufijos distintos en caso de que haya varios formularios en la
     * 			misma página que tengan la misma property (por ejemplo en la portada el de registrarse y el de login comparten login y password).
     * @return true si hay errores a guardar.
     */
    protected boolean saveErrorsFixed(HttpServletRequest request, ActionMessages errors, String suffix) {
    	return SaveErrorsFixed.saveErrors(request, errors, suffix); 
    }
    protected boolean saveErrorsFixed(HttpServletRequest request, ActionMessages errors) {
    	return SaveErrorsFixed.saveErrors(request, errors); 
    }

}
