package es.engade.thearsmonsters.http.view.applicationobjects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class PromotionalValidation {

	/*
	 	Ejemplo de codigos validos
	 	"3dAA2","VX6us","9ms86","4X6s4","KXuAa",
		"V6Q3b","53A87","W5bWK","4b9uL","6Kh29",
		"3g4da","3bW35","3bsL6","3bV48","3dA9m",
		"AAVdK","5LdhX","W5b24","536La","8K45h",
		"8K386","3sV97","5dWdQ","3bW35","55Lm4",
		"58X98","WWA23","5gVhW","W9bL4","3dgLd",
		"3ag6A","Lbb5K","4add3","5gXQK","5WAAd",
		"76duh","6h2u9","W9LQ3","7V6Kg","6Kb3V",
		"5dddb","536d7","53AQQ","3mLh3","8K2W6",
		"W5VbX","4WVXu","3agV6","5gWV5","3sWa2",
		"LbsK9","7V72s","KAg5h","4abbm","52a5W",
		"62329","b2ua2","3d4mm","6h587","4A3Qs"
	 */
	// periodo de acierto, con alfabeto y longitud conocidos (1 acierto/x tests)
	private final static int TASA = 100;
    private final static Collection<String> INVITATION_CODE = Arrays.asList(
    		new String[] {"testInvitationCode"});
    private final static char[] ALPHABET = new char[]{
    	'a','b','d','g','u','A','V','K','L','h','s',
    	'm','2','3','4','5','6','7','8','9',
    	'X','W','Q'
    	};
    private final static int CODE_LENGTH = 5;
    private final static Random random = new Random();

    public static boolean validate(String code) {
    	//Longitud 5:
    	if (code.length() != CODE_LENGTH)
    		return false;
    	return sumNumbers(code.hashCode()) % TASA == 0;
    		
    }
    
    public static String generateValidCode(){
    	String code = new String();
    	while (!validate(code)) {
    		code = randomString();
    	}
    	return code;
    }
 
    private static String randomString() {
    	char[] buf = new char[CODE_LENGTH];

    	for (int idx = 0; idx < buf.length; ++idx) 
    		buf[idx] = ALPHABET[random.nextInt(ALPHABET.length)];
    	return new String(buf);
    }
    
    private static int sumNumbers(int value) {
    	String str = String.valueOf(value);
    	int result = 0;
    	for (byte b : str.getBytes()) {
    		result += b;
    	}
    	return result;
    	
    }
    
    public static void main(String arg[]) {
    	
    	int aciertos = 0, tests = 1000000;
    	for (int i = 0; i < tests; i++) {
    		if (validate(randomString()))
    			aciertos ++;
    	}
    	System.out.println(aciertos+"/"+tests+" = " + (double)aciertos/tests);
    	
    	int NUMERO_DE_CODIGOS = 20;
    	for (int i = 0; i < NUMERO_DE_CODIGOS; i++) {
    		System.out.print("\"" + generateValidCode() + "\",");
    		if (i % 5 == 4)
    			System.out.println("");
    	}
    	
    }
}
