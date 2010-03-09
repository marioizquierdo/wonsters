package es.engade.thearsmonsters.test.system;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.util.configuration.ConfigurationParametersManager;
import es.engade.thearsmonsters.util.configuration.MissingConfigurationParameterException;

public class SystemTest extends GaeTest {

    @Test 
    public void configurationParametersManager_getParameter() throws MissingConfigurationParameterException {
    	String property = ConfigurationParametersManager.getParameter("junit.test.property");
    	assertEquals(property, "hello world!");
    }
    
    @Test(expected=MissingConfigurationParameterException.class)
    public void configurationParametersManager_getMissingParameter() throws MissingConfigurationParameterException {
    	ConfigurationParametersManager.getParameter("junit.test.missingProperty");
    }
}
