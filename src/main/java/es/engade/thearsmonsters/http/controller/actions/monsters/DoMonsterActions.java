package es.engade.thearsmonsters.http.controller.actions.monsters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.frontcontroller.ForwardParameters;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.view.actionforms.MonsterActionToDo;
import es.engade.thearsmonsters.http.view.actionforms.MonsterActionsToDoForm;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class DoMonsterActions extends ThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
    	MonsterFacade monsterFacade = (MonsterFacade) AppContext.getInstance().getAppContext().getBean("monsterFacade");
    	
        /* Get data. */
    	MonsterActionsToDoForm monsterActionsToDoForm = (MonsterActionsToDoForm) form;
        String monsterIdString = monsterActionsToDoForm.getMonsterId();
        
		try {
	        Lair lair = SessionManager.getMyLair(request);
	        monsterFacade.executeMonsterActions(lair, monsterActionsToDoForm.getActionsToDo());
        } catch (Exception e) {
            return mapping.findForward("InternalError");
        }
        
        /* Return ActionForward. */
        return (new ForwardParameters()).add("monsterId", monsterIdString).forward(mapping.findForward("Monster"));
    }
}