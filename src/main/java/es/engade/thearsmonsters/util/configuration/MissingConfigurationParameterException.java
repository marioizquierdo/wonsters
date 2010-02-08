package es.engade.thearsmonsters.util.configuration;

public class MissingConfigurationParameterException extends Exception {

    private String parameterName;

    public MissingConfigurationParameterException(String parameterName) {
        super("Missing configuration parameter: '" + parameterName + "'");
        this.parameterName = parameterName;
    }
    
    public String getParameterName() {
        return parameterName;
    }

}
