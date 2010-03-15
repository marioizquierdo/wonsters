package es.engade.thearsmonsters.model.facades.userfacade.exceptions;

import es.engade.thearsmonsters.util.exceptions.ModelException;

public class IncorrectPasswordException extends ModelException {

	private static final long serialVersionUID = 200912142050L;
	
    private String login;
    
    public IncorrectPasswordException(String login) {
        super("Incorrect password exception => login = " + login);
        this.login = login;
    }
    
    public String getLogin() {
        return login;
    }
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new IncorrectPasswordException("mizquierdo");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
