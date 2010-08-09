package es.engade.thearsmonsters.model.facades.userfacade;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * A facade to model the interaction of the user with the portal. There exist
 * some logical restrictions with regard to the order of method calling. 
 * In particular, <code>findUserProfile</code>,  
 * <code>updateUserProfileDetails</code> and <code>changePassword</code> can 
 * not be called if <code>login</code> or <code>registerUser</code> have not 
 * been previously called. After the user calls one of these two methods, the 
 * user is said to be authenticated.
 */
public interface UserFacade {

    public LoginResult registerUser(String login, String clearPassword,
        UserDetails userDetails)
        throws FullPlacesException, DuplicateInstanceException, InternalErrorException;

    public LoginResult login(String login, String password,
        boolean passwordIsEncrypted, boolean loginAsAdmin)
        throws InstanceNotFoundException, IncorrectPasswordException,
            InternalErrorException;

    public User findUserProfile(String login) 
    	throws InstanceNotFoundException, InternalErrorException;

    public void logout(Lair lair);
    /**
     * Modifica los datos del usuario. No hace falta pasarle el password
     * porque en el caso de uso no se solicita clave. 
     */
    public void updateUserProfileDetails(
        String login, 
        UserDetails userProfileDetailsVO)
        throws InternalErrorException;
    
    /**
     * Futuramente debería de solicitar el password en claro para que no
     * venga tu hermano cabrón y te borre la cuenta.
     * 
     */
	public void removeUserProfile(String login)
		throws InternalErrorException, InstanceNotFoundException;

    public void changePassword(String login, String oldClearPassword, 
        String newClearPassword) throws IncorrectPasswordException,
        InternalErrorException;
    
    public int countUsers() throws InternalErrorException;
    

    //------- Messages ------// (no son usuarios pero tampoco se va a hacer una fachada nueva por estos 2 metodos, que en futuras versiones serán eliminados) 
    
//    public MessageVO createMessage(String author, String content) throws InternalErrorException;
//    
//    public PaginatorChunkVO viewMessages(int startIndex, int count) throws InternalErrorException;

}