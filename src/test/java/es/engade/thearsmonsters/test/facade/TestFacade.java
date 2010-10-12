package es.engade.thearsmonsters.test.facade;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import es.engade.thearsmonsters.model.entities.common.dao.exception.ConstraintException;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

/**
 * Fachada con acciones que permitan insertar o manipular datos de prueba.s
 */
public class TestFacade {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private LairDao lairDao;
    
    public TestFacade() {
    }
    
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setLairDao(LairDao lairDao) {
		this.lairDao = lairDao;
	}
    
    public void setLairState(Lair lair)
		throws InstanceNotFoundException, InternalErrorException {
            
        	// cambiar estado de la guarida
            
    }
    
	public void addFixtureDatas(List<String> logins)
		throws FullPlacesException, InternalErrorException, DuplicateInstanceException{
            for(String login : logins) {
            	addFixtureData(login);
            }
	}
	
	//Esta por arreglar. Solo inserta el primer usuario, haciendo un registro nuevo
	// como si te registras en la p√°gina
	public void addFixtureData(String login)
		throws FullPlacesException, InternalErrorException, DuplicateInstanceException{
		try {
			Lair newLair = FactoryData.LairWhatIs.FullOfMonsters.build(login);
			Address nextAddress = lairDao.findNextFreeAddress(); // FIXME: esto da NullPointerException
			newLair.setAddress(nextAddress);
			userDao.save(newLair.getUser()); // FIXME: si comentamos la linea anterior, el NullPointerException dar· aquÌ
			
		} catch (ConstraintException ex) {
			throw new DuplicateInstanceException(login, User.class.getName());
		}
	}
    
	public void deleteFixtureData(String login)
		throws InstanceNotFoundException, InternalErrorException {
		// borrar a la pe√±a
	}

}