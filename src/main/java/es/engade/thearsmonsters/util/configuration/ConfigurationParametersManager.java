package es.engade.thearsmonsters.util.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigurationParametersManager {

    private static final String CONFIGURATION_FILE =
        "war" + File.separatorChar + "WEB-INF" +
        		File.separatorChar + "ConfigurationParameters.properties";    

    private static Map parameters;

    static {

        try {
            /* Read property file (if exists).*/    
            InputStream inputStream = new FileInputStream(new File(CONFIGURATION_FILE));
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(CONFIGURATION_FILE)));
            inputStream.close();

            /* 
             * We use a "HashMap" instead of a "HashTable" because HashMap's
             * methods are *not* synchronized (so they are faster), and the 
             * parameters are only read.
             */
            parameters = new HashMap(properties);
            
        } catch (Exception e) { 
        	System.out.println(e.getMessage());
        	e.printStackTrace();    
        }
        
    }
       
    private ConfigurationParametersManager() {}
    
	public static String getParameter(String name)
			throws MissingConfigurationParameterException {
		String value = (String) parameters.get(name);
		if (value == null) throw new MissingConfigurationParameterException(name);
		return value;
	}
     
}
