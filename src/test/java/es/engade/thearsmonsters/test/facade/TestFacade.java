package es.engade.thearsmonsters.test.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.common.dao.exception.ConstraintException;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDaoJdo;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.entities.user.dao.UserDaoJdo;
import es.engade.thearsmonsters.model.facades.userfacade.LoginResult;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
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
	// como si te registras en la página
	public void addFixtureData(String login)
		throws FullPlacesException, InternalErrorException, DuplicateInstanceException{
		try {
			UserDetails userDetails = new UserDetails(login, login, login + "@mail.com", "es");
			User newUser = new User(login,
					PasswordEncrypter.crypt(login), userDetails);
			Lair newLair = FactoryData.LairWhatIs.Default.build(newUser);
			Address nextAddress = lairDao.findNextFreeAddress();
			newLair.setAddress(nextAddress);

			userDao.save(newUser);
		} catch (ConstraintException ex) {
			throw new DuplicateInstanceException(login, User.class.getName());
		}
	}
    
	public void deleteFixtureData(String login)
		throws InstanceNotFoundException, InternalErrorException {
		// borrar a la peña
	}

}