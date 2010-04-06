package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.appengine.api.datastore.KeyFactory;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterAction extends AThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
        MonsterFacade monsterFacade = (MonsterFacade) appContext.getBean("monsterFacade");
    	String monsterId = request.getParameter("id");
    	Monster monster;
    	
        /* Find Monster. */
		try {
			monster = monsterFacade.findMonster(monsterId);
		} catch (InstanceNotFoundException e) {
	        return mapping.findForward("Monsters"); // si está mal el id, vamos a la lista de monstruos
		}
		
		/* Set request attributes */
		request.setAttribute("monster", monster);
        
        /* Return ActionForward. */    
        return mapping.findForward("Monster");
    
    }
}
