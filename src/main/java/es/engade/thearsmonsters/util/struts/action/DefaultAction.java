package es.engade.thearsmonsters.util.struts.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * A base <code>Action</code> to facilitate the implementation of
 * web applications using Struts. It provides a default implementation
 * of <code>execute</code> that calls on the abstract method 
 * <code>doExecute</code>. 
 *
 */
public abstract class DefaultAction extends Action {

    /**
     *
     * A default implementation of <code>Execute</code> that calls on the 
     * abstract method <code>doExecute</code>. Currently its responsabilities 
     * are as follows:
     *
     * <ul>
     *
     * <li>It catches <code>InternalErrorException</code> (thrown by 
     * <code>doExecute</code>) and calls on the protected method
     * <code>doOnInternalErrorException</code>. 
     *
     * </ul>
     */
    public ActionForward execute(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {
        
        try {        
            return doExecute(mapping, form, request, response);            
        } catch (Exception e) { // Any exception thrown by "doExecute", including
                                // instances of "RuntimeException".        
            return doOnException(mapping, form, request, response, e);
        }
        
    }

    protected abstract ActionForward doExecute(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException;
        
    /**
     * Treats an <code>Exception</code> thrown by
     * <code>doExecute</code>. In particular, it works as follows:
     *
     * <ul>
     * <li>Logs the <code>Exception</code>.</li>
     * <li>Returns the <code>ActionForward</code> returned by
     * <code>mapping.findForward("InternalError")</code>.</li>
     * </ul>
     *
     */
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

        /* Redirect to input page. */
        return mapping.findForward("InternalError");
        
    }
    
    /**
     * If a custom Action simply needs to add some stuff to the doOnException
     * method, it may extend this one instead of the whole other. 
     */
    protected void extraDoOnException(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response, Exception exception)
        throws IOException, ServletException {
    	// do nothing extra by default
    }
    
    

}
