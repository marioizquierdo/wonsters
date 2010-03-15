package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.lairfacade.BuildingChunk;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.LairFacadeMock;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class LairAction extends AThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
    	LairFacade lairFacade = new LairFacadeMock();
    	Lair lair;
    	

    	try {
    		/*
    		 * Esta acción puede recibir los parámetro en la url: street, building, floor, login y showRoomType.
    		 * Si se reciben solamente street y building entonces se inserta un BuildingChunkVO en la
    		 * 		request que contenga las diferentes guaridas y se redirige a ShowBuilding.do
    		 * Si se reciben street, building y floor (que identifiquen una guarida por su dirección) o
    		 * 		bien un login entonces se inserta una LairVO en la request para mostrar sus datos.
    		 * 		(si la guarida es la misma que la de la sesión, como se supone que esta última está
    		 * 		siempre actualizada, entonces no hace falta recargarla de la base de datos).
    		 * El parámetro showRoomType no influye en esta acción. Solo sirve para saber si en la vista
    		 * 		es necesario mostrar los datos de alguna sala o no.
    		 */
    		Lair myLair = SessionManager.getMyLair(request);
	    	if(request.getParameter("street")!=null && request.getParameter("building")!=null) {
	    		
	    		/* Buscar un bloque de guaridas por dirección o bien una sola guarida */
	    		int street = Integer.parseInt(request.getParameter("street"));
	    		int building = Integer.parseInt(request.getParameter("building"));
	    		
	    		if(request.getParameter("floor")!=null && 
	    				request.getParameter("floor")!="") {
	        		int floor = Integer.parseInt(request.getParameter("floor"));

	        		/* Buscar guarida por direccion */
	        		// Si la dirección es la de myLair, entonces se devuelve esa
		    		if(street == myLair.getAddress().getStreet() && 
		    				building == myLair.getAddress().getBuilding() &&
		    				floor == myLair.getAddress().getFloor()) {
		    			lair = myLair;
		    			
		    		} else { // Sino pues la recupera de la base de datos
		    	    	lair = lairFacade.findLairByAddress(street, building, floor);
		    		}
	    			
	    		}else{
	    			
	        		/* Buscar bloque de guaridas por dirección */
	    	    	BuildingChunk buildingChunk = lairFacade.findBuilding(street, building);
	    	    	request.setAttribute("building", buildingChunk);
	    	        return mapping.findForward("ShowBuilding");
	    	        
	    		}
	    	} else {
	    	
		    	/* Buscar guarida por login */
	            String login = request.getParameter("login");
	    		
	    		// Si el login name es el del usuario regustrado entonces la guarida es la que esta en la sesión
	    		// Si no hay login, se entiende que es el del jugador logueado.
	    		if(login=="" || login==null || 
	    				login.equals(myLair.getUser().getLogin())) {
	    			lair = myLair;
	    			
	    		} else { // Sino pues carga la guarida de ese otro jugador
	    			lair = lairFacade.findLairByLogin(login);
	    		}
	    	}

	    	request.setAttribute("lair", lair);
	    	
        }catch (IncorrectAddressException e){
        	FlashMessage.showError(request, e);
        }catch (InstanceNotFoundException e){
        	FlashMessage.show(request, "Lair.notFound", new String[]{}, FlashMessage.Status.ERROR, null);
	    }catch (NumberFormatException e){
        	FlashMessage.show(request, "Lair.badParameter", new String[]{}, FlashMessage.Status.ERROR, null);
	    }

        return mapping.findForward("ShowLair");
    }
    
}
