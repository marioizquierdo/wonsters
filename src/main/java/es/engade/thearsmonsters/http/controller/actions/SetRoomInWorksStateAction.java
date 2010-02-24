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
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.exceptions.NoRoomsLoadedException;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacadeMock;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class SetRoomInWorksStateAction extends AThearsmonstersDefaultAction {
	
	private static final String ENLARGE = "ENLARGE";
	private static final String UPGRADE = "UPGRADE";
	private static final String CANCEL_WORKS = "CANCEL_WORKS";
	private static final String BUILD_NEW_ROOM = "BUILD_NEW_ROOM";

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
        LairFacade lairFacade = new LairFacadeMock();

        /* Get data. */
        RoomType roomType = RoomType.valueOf(request.getParameter("roomType"));
        Lair lair = SessionManager.getMyLair(request);
        String action = request.getParameter("action");
        
        try {
	        
	        /* Model action */
	        if(action.equals(ENLARGE)) {
	        	lairFacade.setRoomEnlargingInWorksState(lair, roomType);
	        	
	        } else if(action.equals(UPGRADE)) {
	        	lairFacade.setRoomUpgradingInWorksState(lair, roomType);
	        	
	        } else if(action.equals(CANCEL_WORKS)) {
	        	lairFacade.cancelWorks(lair, roomType);
	        	
	        } else if(action.equals(BUILD_NEW_ROOM)) {
	        	lairFacade.createNewRoom(lair, roomType);
	        	
	        } else {
	        	throw incorrectParameter(action);
	        }
	        
	        /* Notify message */
			FlashMessage.show(request, "Lair.room.actions."+action+".doneMessage", "", null, "roomFlashMessage_"+roomType);

		} catch (InsuficientGarbageException e) {
			FlashMessage.showError(request, e, "roomFlashMessage_"+roomType);
			
		} catch (InWorksActionException e) {
        	throw new InternalErrorException(e);	
        	
		} catch (InstanceNotFoundException e) {
        	throw new InternalErrorException(e);	
        	
		} catch (NoRoomsLoadedException e) {
			// ¿Why session lair have no rooms?
        	throw new InternalErrorException(e);
		}
		
        return (new ForwardParameters()).add("showRoomType", roomType.toString()).forward(mapping.findForward("ShowLair"));

    }
    
    
    private InternalErrorException incorrectParameter(String action) {
    	String actionString = action==null ? action : "null";
    	return new InternalErrorException(new Exception(
    			"Incorrect request.getParameter('action'), expected " +
    			ENLARGE + ", " + UPGRADE + " or " + CANCEL_WORKS +
    			", but found "+ actionString));
    }
    
}
