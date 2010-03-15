package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MessagesAction extends AThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
//    	/* Get data */
//    	int startIndex;
//    	try {
//    		startIndex = Integer.parseInt(request.getParameter("startIndex"));
//    	} catch (NumberFormatException e) {
//    		startIndex = 1;
//    	}
//    	
//    	int count;
//    	try {
//    		count = Integer.parseInt(request.getParameter("count"));
//    	} catch (NumberFormatException e) {
//    		count = 20;
//    	}
//        
//        /* Set message paginator in the request. */
//    	
//        PaginatorChunk paginator = SessionManager.getUserFacade(request).
//        	viewMessages(startIndex, count);       
//        
//        request.setAttribute("paginator", paginator);
//
//        
//        /* Prepare form for new messages */
//        MessageForm messageForm = (MessageForm) form;
//        messageForm.setAuthor(SessionManager.getLogin(request));
        
        /* Return ActionForward. */
        return mapping.findForward("Messages");
    }
}
