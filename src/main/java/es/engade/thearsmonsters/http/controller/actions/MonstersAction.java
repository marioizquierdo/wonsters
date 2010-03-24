package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonstersAction extends AThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        MonsterFacade monsterFacade = (MonsterFacade) appContext.getBean("monsterFacade");
    	List<Monster> monsters;
		Lair myLair = SessionManager.getMyLair(request);
    	
        /* Find User Monsters. */
		monsters = monsterFacade.findLairMonsters(myLair);
		
		/* Set request attributes */
		request.setAttribute("monsters", monsters);
        
        /* Return ActionForward. */    
        return mapping.findForward("Monsters");
    
    }
}
