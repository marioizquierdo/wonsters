package es.engade.thearsmonsters.http.controller.actions.ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.exceptions.InternalErrorException;


public class SuggestTasksAjaxAction extends DefaultHTMLAjaxAction {
	
	@Override
    protected ActionForward doExecuteGameAction(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) 
			throws IOException, ServletException, InternalErrorException {
//
//		TaskFacadeDelegate delegateTask = TaskFacadeDelegateFactory.getDelegate();
//		
//        /* Get data. */
//
//		String ageStateStr = request.getParameter("ageState");
//		AgeState ageState = ageStateStr != null ? 
//				AgeState.valueOf(ageStateStr) : null;
//
//        String turnStr = request.getParameter("turn");
//        byte turn = turnStr != null ? 
//        		Byte.parseByte(turnStr) : -1;
//        		
//        long monsterId = Long.parseLong(
//        		request.getParameter("monsterId"));
//
//        boolean isSleepingTurn = Boolean.parseBoolean(
//        		request.getParameter("isSleepingTurn"));
//        
//        boolean isMealingTurn = Boolean.parseBoolean(
//        		request.getParameter("isMealingTurn"));
//
//        TaskType exceptTaskType = TaskType.valueOf(request.getParameter("taskType"));
//        
//        String roomTypeStr = request.getParameter("roomType");
//        
//        RoomType roomType = roomTypeStr != null ? 
//				RoomType.valueOf(roomTypeStr) : null;
//        
//        String loginName = SessionManager.getLoginName(request);
//            
//        /* Model action */
//        List<TaskVO> sTasks = delegateTask.getSuggestedTasks(
//        		ageState, loginName, turn, monsterId, 
//        		isSleepingTurn, isMealingTurn, roomType, exceptTaskType);
//        
//        /* Set request parameters */
//        request.setAttribute("suggestedTasks", sTasks);

		return mapping.findForward("ajaxTile");
	}
    
}
