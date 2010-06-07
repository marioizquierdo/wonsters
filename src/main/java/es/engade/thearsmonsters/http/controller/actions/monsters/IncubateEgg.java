package es.engade.thearsmonsters.http.controller.actions.monsters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.http.controller.util.FlashMessage.Status;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class IncubateEgg extends ThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
        MonsterFacade monsterFacade = (MonsterFacade) AppContext.getInstance().getAppContext().getBean("monsterFacade");
        
        try {
	        /* Get data. */
	        String eggId = request.getParameter("eggId");
	    	Lair lair = SessionManager.getMyLair(request);
	            
	        /* Model action */
			monsterFacade.incubateEgg(lair, eggId);
			
		} catch (InstanceNotFoundException e) {
		    FlashMessage.show(request, "ErrorMessages.raceNotExists", new String[]{}, Status.ERROR, null);
			
		}

        return mapping.findForward("MonsterEggs");
        
    }
    
}
