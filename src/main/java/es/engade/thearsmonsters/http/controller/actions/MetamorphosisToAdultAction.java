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
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.monsterfacade.MonsterFacade;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MetamorphosisToAdultAction extends AThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	    	
            MonsterFacade monsterFacade = (MonsterFacade) AppContext.getInstance().getAppContext().getBean("monsterFacade");
	        

	        /* Get data. */
	        String monsterId = request.getParameter("monsterId");
	        Lair lair = SessionManager.getMyLair(request);
	        
	        try {   
		        /* Model action */
				monsterFacade.metamorphosisToAdult(monsterId, lair);
				
			} catch (InstanceNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (MonsterGrowException e) {
				throw new InternalErrorException(e);
			}
			
			return new ForwardParameters().add("monsterId", monsterId+"").forward(mapping.findForward("Monster"));
    }
    
}
