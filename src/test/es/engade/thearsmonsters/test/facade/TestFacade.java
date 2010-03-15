package es.engade.thearsmonsters.test.facade;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * Fachada con acciones que permitan insertar o manipular datos de prueba.s
 */
public class TestFacade {
    
    public TestFacade() {}
    
    public void setLairState(Lair lair)
		throws InstanceNotFoundException, InternalErrorException {
            
        	// cambiar estado de la guarida
            
    }
    
	public void addFixtureDatas(List<String> logins)
		throws FullPlacesException, InternalErrorException {
            
            // Añadir guarida, monstruos y movidas a cada usuario utilizando las otras fachadas.
	}
	
	public void addFixtureData(String login)
		throws FullPlacesException, InternalErrorException {
		List<String> logins = new ArrayList<String>(1);
		logins.add(login);
		addFixtureDatas(logins);
	}
    
	public void deleteFixtureData(String login)
		throws InstanceNotFoundException, InternalErrorException {
		// borrar a la peña
	}

}