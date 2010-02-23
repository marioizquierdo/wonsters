package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacadeMock;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterAction extends AThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
    	MonsterFacade monsterFacade = new MonsterFacadeMock();
    	Monster monster;
    	Long monsterId;
    	
    	/* Get Request Data */
		if(request.getParameter("monsterId")==null) {
	        return mapping.findForward("Monsters");
		}else{
			monsterId = Long.parseLong(request.getParameter("monsterId"));
		}
    	
        /* Find Monster. */
		try {
			monster = monsterFacade.findMonster(monsterId);
		} catch (InstanceNotFoundException e) {
	        return mapping.findForward("Monsters");
		}
		
		/* Set request attributes */
		request.setAttribute("monster", monster);
        
        /* Return ActionForward. */    
        return mapping.findForward("Monster");
    
    }
}
