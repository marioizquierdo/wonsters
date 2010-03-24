package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.http.view.actionforms.MessageForm;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class CreateMessageAction extends AThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        /* Get data. */
        MessageForm messageForm = (MessageForm) form;
        String author = messageForm.getAuthor();
        String content = messageForm.getContent();                  
        
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        UserFacade userFacade = (UserFacade) appContext.getBean("userFacade");

        /* Create Message. */
        //userFacade.createMessage(author, content);           
        
        /* Return ActionForward. */
        return mapping.findForward("Messages");
    }
}
