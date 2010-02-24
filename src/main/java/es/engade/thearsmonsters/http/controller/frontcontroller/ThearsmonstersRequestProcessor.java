package es.engade.thearsmonsters.http.controller.frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;

/**
 * A <code>RequestProcessor</code> that allows to execute a chain of 
 * <code>PreProcessingFilters</code>. The chain is executed before the 
 * corresponding action be invoked. If the chain returns a non 
 * <code>null</code> <code>ActionForward</code>, the action is not executed, 
 * but instead the control is passed to the returned <code>ActionForward</code>.
 */
public class ThearsmonstersRequestProcessor extends TilesRequestProcessor {

    PreProcessingFilter firstPreProcessingFilter;

    public ThearsmonstersRequestProcessor() {
    
        firstPreProcessingFilter = 
            new SessionPreProcessingFilter(
            new AuthenticationPreProcessingFilter(
            new AdminPreProcessingFilter(null)));
            
    }

    @Override
    protected ActionForward processActionPerform(
        HttpServletRequest request, HttpServletResponse response,
        Action action, ActionForm form, ActionMapping mapping)
        throws IOException, ServletException {
        
        ActionForward actionForward = firstPreProcessingFilter.process(
            request, response, action, form, mapping);
            
        if (actionForward == null) {
            return super.processActionPerform(request, response, action,
                form, mapping);
        } else {
            return actionForward;
        }
                
    }

}
