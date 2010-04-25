package es.engade.thearsmonsters.http.controller.actions.ajax;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;

/**
 * Works as a DefaultAction, but the error handling findForward is diferent.
 */
public abstract class DefaultHTMLAjaxAction extends ThearsmonstersDefaultAction {
    
    /**
     * Treats an <code>Exception</code> thrown by
     * <code>doExecute</code>. In particular, it works as follows:
     *
     * <ul>
     * <li>Logs the <code>Exception</code>.</li>
     * <li>Returns the <code>ActionForward</code> returned by
     * <code>mapping.findForward("AjaxHTMLInternalError")</code>.</li>
     * </ul>
     *
     */
    @Override
    protected ActionForward doOnException(
        ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response, Exception exception)
        throws IOException, ServletException {
    	
    	extraDoOnException(mapping, form, request, response, exception);
    	
        /* 
         * Log error, even with debug level <= 0, because it is a severe
         * error. 
         */
        ServletContext servletContext = 
            servlet.getServletConfig().getServletContext();
        servletContext.log(exception.getMessage(), exception);
		
        // Return the standard ajax load internal error
        return mapping.findForward("AjaxHTMLInternalError");
        
    }
}
