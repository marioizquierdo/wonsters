package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class CancelTaskAction extends AThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
//    
//    	try { //Get data
//    		byte turn		= Byte.parseByte(request.getParameter("taskTurn"));
//    		long monsterId 	= Long.parseLong(request.getParameter("monsterId"));
//        	TaskFacadeDelegate task = TaskFacadeDelegateFactory.getDelegate();
//    		task.cancelTask(monsterId,turn);
//    		
//    	}
//    	
//    	catch (InternalErrorException e){
//    		throw e;
//    	}
    	
    	
    	
    	return mapping.findForward("TaskCanceled");
    }
	

}
