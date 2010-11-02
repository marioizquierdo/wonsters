package es.engade.thearsmonsters.http.view.applicationobjects;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PromotionalValidation {

	/*
		Diego
		"a4Q23","a4Q86","aQmsX","aQudA","aXd9X",
		"5gALg","W5LVb","7Ka7d","5dKW7","6Vbsb",
		"4aXWa","5dW97","4AgV7","V5ddg","59827",
		"W6a5m","V3dVb","Ld5K9","Aggu9","9Q7u3",
		Mario
		"a3dbW","aV93d","aQm8u","a5223","a624W",
		"4QAWL","5dQ3L","V2dQ8","4abb7","W5LV5",
		"5h7X4","bQgaQ","V5gga","4abbd","6mhbA",
		"4WVsQ","6KaQ6","W67hd","63uaQ","6h76L",
		Gefa
		"a524X","a4Q59","aV9Vh","a5Q4W","a3dd4",
		"AAKW3","77XgQ","4bXbX","VWamQ","9mu2d",
		"Ab79b","b47d8","Ada96","6KgQ9","V73X3",
		"5gVQb","AaV89","4A42a","3m6K3","3g63Q",
		Trono
		"a3da7","aV7uK","aXasK","a3s29","a7QbQ",
		"V2Xmm","V3mau","5gbW5","WX4aa","4A74V",
		"5h93W","bXV4L","662gm","58VsQ","Kb95W",
		"AAVaW","4A4QQ","3hs7W","3bV3W","6KbQ8",
		Castro
		"a229b","aV93m","a5Q58","aV8VK","a5529",
		"La67X","5h96K","4b8sd","6K96d","b77uK",
		"b77u9","7Ks4g","4A6V8","VA3X2","73WuQ",
		"3as8Q","4XKg4","5LsVd","Ka8L9","5dLmh",
		Angel
		"aV96a","a535V","aXd73","ashW9","a543W",
		"4duLs","VVLAu","AA27K","4A53h","5h8Qd",
		"W67L2","7L54V","89VAa","6Kud6","5gd8d",
		"9uK8A","6h42h","W5L9a","5gW5V","b4AmL",
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
 
    public static String generateValidCode(char ini, boolean include){
    	String code = new String();
    	while (!validate(code)) {
   			code = randomString(ini, include);
    	}
    	return code;
    }
    
    private static String randomString() {
    	char[] buf = new char[CODE_LENGTH];

    	for (int idx = 0; idx < buf.length; ++idx) 
    		buf[idx] = ALPHABET[random.nextInt(ALPHABET.length)];
    	return new String(buf);
    }
    
    private static String randomString(char ini, boolean include) {
    	char[] buf = new char[CODE_LENGTH];
    	int start = 0;
    	if (include) {
    		buf[0] = ini;
    		start = 1;
    	}
    	for (int idx = start; idx < buf.length; ++idx) {
    		if (idx == 0 && !include) {
    			char nextCh = ini;
    			while (nextCh == ini) {
    				nextCh = ALPHABET[random.nextInt(ALPHABET.length)];
    			}
    			buf[idx] = nextCh;
    		} else {
    			buf[idx] = ALPHABET[random.nextInt(ALPHABET.length)];
    		}
    	}
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
    	Set<String> codes = new HashSet<String>();
    	
    	int NUMERO_DE_CODIGOS = 20;
    	String[] users = {"Diego","Mario","Gefa","Trono","Castro","Angel"};
    	for (String user : users) {
    		System.out.println(user);
	    	for (int i = 0; i < NUMERO_DE_CODIGOS; i++) {
	    		String code;
	    		if (i < 5) {
	    			//codigos propios
	    			code = generateValidCode(ALPHABET[0], true);
	    			while (!codes.add(code)) { code = generateValidCode(ALPHABET[0], true); }
	    		} else {
	    			code = generateValidCode(ALPHABET[0], false);
	    			while (!codes.add(code)) { code = generateValidCode(ALPHABET[0], false); }
	    		}
    			System.out.print("\"" + code + "\",");
    			
	    		if (i % 5 == 4)
	    			System.out.println("");
	    	}
    	}
    	
    }
}
