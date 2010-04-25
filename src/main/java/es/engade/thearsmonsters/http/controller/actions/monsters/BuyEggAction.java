package es.engade.thearsmonsters.http.controller.actions.monsters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.actions.Confirmation;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class BuyEggAction extends ThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
    	// Hay que confirmar la acción antes de que se ejecute
    	ActionForward confirm = Confirmation.confirm("EggsManagement.buyEggs.confirm", 
    			"monster/eggs.do", request, mapping);
    	if(confirm != null) return confirm;
    	
    	// Ejecutar la acción
        MonsterFacade monsterFacade = (MonsterFacade) AppContext.getInstance().getAppContext().getBean("monsterFacade");
        try {
        	
            /* Get data. */
            byte eggRaceCode = Byte.parseByte(request.getParameter("eggRaceCode"));
	        MonsterRace eggRace = MonsterRace.getFromCode(eggRaceCode);
	        Lair lair = SessionManager.getMyLair(request);
	            
	        /* Model action */
			monsterFacade.buyEgg(lair, eggRace);
			FlashMessage.show(request, "EggsManagement.buyEggs.doneMessage", eggRace.getBuyEggPrice()+"");
			
		} catch (InsuficientMoneyException e) {
			FlashMessage.showError(request, e, null);
		} catch (MaxEggsException e) {
			FlashMessage.showError(request, e, null);
		} catch (InstanceNotFoundException e) {
			throw new InternalErrorException(e);
		}

        return mapping.findForward("MonsterEggs");
        
    }
    
}
