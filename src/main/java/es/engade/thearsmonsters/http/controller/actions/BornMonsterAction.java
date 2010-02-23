package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.frontcontroller.ForwardParameters;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.http.view.actionforms.BornMonsterForm;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacadeMock;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class BornMonsterAction extends AThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	    	
	        MonsterFacade monsterFacade = new MonsterFacadeMock();
	        Monster monster = null;
	        try {
		        /* Get data. */
	            BornMonsterForm bornMonsterForm = (BornMonsterForm) form;
		        long eggId = bornMonsterForm.getEggIdAsLong();
		        String monsterName = bornMonsterForm.getMonsterName();
		        Lair lair = SessionManager.getMyLair(request);
		            
		        /* Model action */
				monster = monsterFacade.bornMonster(eggId, monsterName, lair);
				
				/* Notify message */
				FlashMessage.show(request, "BornMonster.doneMessage", monster.getName());
				
			} catch (InstanceNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (MonsterGrowException e) {
				throw new InternalErrorException(e);
			} catch (InsuficientVitalSpaceException e) {
				FlashMessage.showError(request, e, "eggsManagement1");
				return mapping.findForward("EggsManagement");
			}

			return (new ForwardParameters()).add("monsterId", monster.getId().toString()).forward(mapping.findForward("Monster"));
    }
    
}
