package es.engade.thearsmonsters.http.controller.actions.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class ShowRoomTasksAction extends DefaultHTMLAjaxAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
    		ActionForm form, HttpServletRequest request,
        	HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
    	/* Get data. */
    	RoomType roomType = RoomType.valueOf(request.getParameter("roomType"));
    	Lair lair = SessionManager.getMyLair(request);
    	Room room = lair.getRoom(roomType);
    	
        /* Reload room tasks. */
//    	TaskFacadeDelegate taskFacade = TaskFacadeDelegateFactory.getDelegate();
//        taskFacade.reloadRoomTasks(room);
       
        /* Set request parameters */
    	request.setAttribute("room",room);
    	return mapping.findForward("ShowRoomTasks");
    }
}
