package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacadeMock;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class ShellEggAction extends AThearsmonstersDefaultAction {

    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
    	// Confirmación
    	ActionForward confirm = Confirmation.confirm("EggsManagement.shellEggs.confirm", 
    			"EggsManagement.do", request, mapping);
    	if(confirm != null) return confirm;
    	
        MonsterFacade monsterFacade =new MonsterFacadeMock();
        try {
	        /* Get data. */
	        long eggId = Long.parseLong(request.getParameter("eggId"));
	        Lair lair = SessionManager.getMyLair(request);
	            
	        /* Model action */
			Integer moneyBack = monsterFacade.shellEgg(eggId, lair);
			
			/* Show results */
			FlashMessage.show(request, "EggsManagement.shellEggs.doneMessage", moneyBack.toString());
			
			
		} catch (InstanceNotFoundException e) {
        	throw new InternalErrorException(e);
		}

        return mapping.findForward("EggsManagement");
        
    }
    
}
