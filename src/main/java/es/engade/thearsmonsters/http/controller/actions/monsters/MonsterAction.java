package es.engade.thearsmonsters.http.controller.actions.monsters;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.appengine.api.datastore.KeyFactory;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterAction extends ThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        MonsterFacade monsterFacade = (MonsterFacade) AppContext.getInstance().getAppContext().getBean("monsterFacade");
    	String monsterId = request.getParameter("id");
    	Lair lair = SessionManager.getMyLair(request);
    	Monster monster;
    	List<MonsterActionSuggestion> suggestedMonsterActions;
    	
        /* Find Monster. */
		try {
			monster = monsterFacade.findMonster(lair, monsterId);
			suggestedMonsterActions = monsterFacade.suggestMonsterActions(lair, monsterId);
		} catch (InstanceNotFoundException e) {
	        return mapping.findForward("Monsters"); // si está mal el id, vamos a la lista de monstruos
		}
		
		request.setAttribute("suggestedMonsterActions", suggestedMonsterActions);
		
		/* Set request attributes */
		request.setAttribute("monster", monster);
        
        /* Return ActionForward. */    
        return mapping.findForward("Monster");
    
    }
}
