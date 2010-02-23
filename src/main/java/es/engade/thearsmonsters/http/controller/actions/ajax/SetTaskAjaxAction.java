package es.engade.thearsmonsters.http.controller.actions.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.exceptions.InternalErrorException;


public class SetTaskAjaxAction extends DefaultHTMLAjaxAction {
	
	@Override
    protected ActionForward doExecuteGameAction(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) 
			throws IOException, ServletException, InternalErrorException {
//		
//		TaskFacade delegateTask = TaskFacadeDelegateFactory.getDelegate();
//		
//        /* Get data. */
//		
//        byte turn = Byte.parseByte(request.getParameter("turn"));		
//        long monsterId = Long.parseLong(request.getParameter("monsterId"));
//        String roomLoginName = request.getParameter("roomLoginName");
//		RoomType roomRoomType = RoomType.valueOf(request.getParameter("roomRoomType"));
//		TaskRole role = TaskRole.valueOf(request.getParameter("role"));
//            
//        /* Model action */
//        Monster monster;
//		try {
//			monster = delegateTask.setTask(turn, monsterId, roomLoginName, roomRoomType, role);
//	        
//		// If setTasks throws InvalidTaskExceptions, the task will be the previous task (for replace in the view)
//		// and a list of errors will be put in the request (to show in the view).
//		} catch (InvalidTaskExceptions e) {
//			MonsterFacade delegateMonster = new MonsterFacadeMock();
//			try {
//				monster = delegateMonster.findMonster(monsterId);
//		        request.setAttribute("invalidTaskExceptions", e.getExceptions());
//				
//			} catch (InstanceNotFoundException e1) {
//				throw new InternalErrorException(e1);
//			}
//		}
//		
//        /* Set request parameters */
//        request.setAttribute("monster", monster);
//        request.setAttribute("task", monster.getTask(turn));
		
		return mapping.findForward("ajaxTile");
		
	}
	
}
