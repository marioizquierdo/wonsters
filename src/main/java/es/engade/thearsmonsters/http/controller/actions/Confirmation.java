package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.view.applicationobjects.ConfirmationData;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * Este método de confirmación trata de simplificar la confirmación de acciones que la necesiten
 * Con esto se puede evitar que alguien se confunda pulsando sin querer un enlace que le puede hacer perder recursos
 * Por ejemplo: BuyEgg, compra un huevo, pero alomejor no quería gastar dinero
 * También se evitan ataques de URLs malintencionadas, aunque no es infranqueable:
 * La confirmación se realiza mediante un formulario que tiene un campo hidden: confirmed = TRUE
 * Este parámetro puede pasarse también por GET y también puede crearse un formulario desde otra página con esto
 * Pero almenos las medidas de seguridad son las mínimas
 */
public class Confirmation extends AThearsmonstersDefaultAction {

    private final static String CONFIRM_DATA_SESSION_ATTRIBUTE = "confirmData";

    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
    	// La información se recoge de la sesión y se cambia por un flag
        HttpSession session = request.getSession(false);
        ConfirmationData data = (ConfirmationData) session.getAttribute(CONFIRM_DATA_SESSION_ATTRIBUTE);
        session.setAttribute(CONFIRM_DATA_SESSION_ATTRIBUTE, "confirmed!"); // esto es para mayor seguridad
        
        // y se inserta en la request para usar en la página jspx
        request.setAttribute("confirmationData", data);
        
        // se limpia la sesión y se redirige al mensaje de confirmación
        return mapping.findForward("ShowConfirmation");
    }
    
    /**
     * Se usa desde otras acciones para realizar la confirmacion.
     * @return ActionForward a la acción Confirm, o NULL si la confirmación ya está realizada
     */
    public static ActionForward confirm(String message, String cancelAction,
    		HttpServletRequest request, ActionMapping mapping) {

    	String confirmed = request.getParameter("confirmed");
        HttpSession session = request.getSession(false);
        String confirmState = (String) session.getAttribute(CONFIRM_DATA_SESSION_ATTRIBUTE);
        
    	if(confirmed != null && confirmed.equals("TRUE") && confirmState.equals("confirmed!")) {
            session.removeAttribute(CONFIRM_DATA_SESSION_ATTRIBUTE);
    		return null;
    	}else{
	    	String acceptAction = request.getServletPath(); 	
	    	Map<String, String> parameters = getParameters(request);
	    	
	    	ConfirmationData confirmationData = new 
	    		ConfirmationData(acceptAction, parameters, cancelAction, message);
	    	
	    	// La información se evía a través de la sesión
	        session.setAttribute(CONFIRM_DATA_SESSION_ATTRIBUTE, confirmationData);
	    	return mapping.findForward("Confirmation");
    	}
    }
    
    
    /**
     * Helper privado. Recupera todos los parámetros (monovaluados) de la request
     */
    @SuppressWarnings("unchecked")
	private static Map<String, String> getParameters(HttpServletRequest request) {
    	Map<String, String> parameters = new HashMap<String, String>();
    	for(Enumeration e=request.getParameterNames(); e.hasMoreElements(); ) { 
    		String paramName = (String)e.nextElement();
    		String paramValue = (String)request.getParameter(paramName);
    		parameters.put(paramName, paramValue);
    	}
    	return parameters;
    }
    
}
