package es.engade.thearsmonsters.http.controller.actions.monsters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.frontcontroller.ForwardParameters;
import es.engade.thearsmonsters.http.view.actionforms.MonsterActionsToDoForm;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
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
    	MonsterActionsToDoForm monsterActionsToDo = (MonsterActionsToDoForm) form;
        String monsterIdString = monsterActionsToDo.getMonsterId();
        Map<MonsterActionSuggestion, Integer> actionsToDo = monsterActionsToDo.getActionsToDo();
        
		try {
	        Key monsterId = KeyFactory.stringToKey(monsterIdString);
	        for(Map.Entry<MonsterActionSuggestion, Integer> entry : actionsToDo.entrySet()) {
	    		MonsterActionSuggestion action = entry.getKey();
	    		Integer times = entry.getValue();
	        	for(int i = 0; i < times; i++) {
		                monsterFacade.executeMonsterAction(null, action.getMonsterActionType(), monsterId, action.getRoomType());
	        	}
	        }
        } catch (Exception e) {
            return mapping.findForward("InternalError");
        }
        
        /* Return ActionForward. */
        return (new ForwardParameters()).add("monsterId", monsterIdString).forward(mapping.findForward("Monster"));
    }
}