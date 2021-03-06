package es.engade.thearsmonsters.http.controller.actions.lair;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.http.view.actionforms.ChangeResourcesForm;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class ChangeResourcesAction extends ThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	    	    	
            LairFacade lairFacade = (LairFacade) AppContext.getInstance().getAppContext().getBean("lairFacade");
        	
	        try {
		        /* Get data. */
	        	ChangeResourcesForm changeResourcesForm = (ChangeResourcesForm) form;
		        int garbage = changeResourcesForm.getGarbageAsInt();
		        int money = changeResourcesForm.getMoneyAsInt();
		        Lair lair = SessionManager.getMyLair(request);
		            
		        /* Model action */
		        if(garbage > 0) {
		        	lairFacade.changeResources(lair, "garbage", garbage);
		        } else { // money > 0
		        	lairFacade.changeResources(lair, "money", money);
		        }
				
				/* Done message */
		        if(garbage == 0 && money == 0) {
		        	FlashMessage.show(request, "ChangeResources.SpecifyAmountError", "", FlashMessage.Status.ERROR, null);
		        } else {
		        	FlashMessage.show(request, "ChangeResources.doneMessage");
		        }
		        
			} catch (WarehouseFullStorageException e) {
				FlashMessage.showError(request, e);
			} catch (TradeOfficeFullStorageException e) {
				FlashMessage.showError(request, e);
			} catch (InsuficientGarbageException e) {
				FlashMessage.showError(request, e);
			} catch (InsuficientMoneyException e) {
				FlashMessage.showError(request, e);
			}

			return mapping.findForward("ShowResults");
    }
    
}
