package es.engade.thearsmonsters.http.view.applicationobjects;

import java.util.Arrays;
import java.util.Collection;

public class PromotionalValidation {

	// De momento solo 1 codigo. Lo ideal es tener varios en algun sitio 
    // donde se puedan eliminar los ya utilizados.
    private final static Collection<String> INVITATION_CODE = Arrays.asList(
    		new String[] {"testInvitationCode"});
    
    public static boolean validate(String code) {
    	return (INVITATION_CODE.contains(code));
    }
}
