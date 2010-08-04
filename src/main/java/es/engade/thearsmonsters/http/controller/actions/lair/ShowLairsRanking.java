package es.engade.thearsmonsters.http.controller.actions.lair;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.LairRankingInfoChunk;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class ShowLairsRanking extends ThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        LairFacade lairFacade = (LairFacade) AppContext.getInstance().getAppContext().getBean("lairFacade");
    	//Lair lair = SessionManager.getMyLair(request);
    	
        
        /* Get raking. */
        LairRankingInfoChunk ranking = lairFacade.getLairsRanking(0, 100); // top 100
		
		/* Set request attributes */
		request.setAttribute("ranking", ranking);
        
        /* Return ActionForward. */    
        return mapping.findForward("Ranking");
    
    }
}

