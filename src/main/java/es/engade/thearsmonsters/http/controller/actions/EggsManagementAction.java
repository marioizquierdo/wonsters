package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacadeMock;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class EggsManagementAction extends AThearsmonstersDefaultAction {
	
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
    	MonsterFacade monsterFacade = new MonsterFacadeMock();
    	Lair lair = SessionManager.getMyLair(request);
    	List<MonsterEgg> eggs;
    	
        /* Find User Eggs. */
		eggs = monsterFacade.findEggs(lair);
		request.setAttribute("eggs", eggs);
		
		/* Set races on the request */
		MonsterRace[] races = MonsterRace.values();
		request.setAttribute("races", races);
        
        /* Return ActionForward. */    
        return mapping.findForward("EggsManagement");
    
    }
}
