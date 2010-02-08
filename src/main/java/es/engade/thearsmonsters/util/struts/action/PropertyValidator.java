package es.engade.thearsmonsters.util.struts.action;

import java.util.Collection;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.ParseException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

/**
 * It contains common validation/conversion operations for action form classes.
 * Each operation takes the following parameters:
 *
 * <ul> 
 * 
 * <li>An <code>ActionErrors</code>.</li>
 * <li>A property name: the name of a form field.</li>
 * <li>The property value: the value of the form field.</li>
 * <li>A <code>boolean</code> value specifying whether or not the property is 
 * mandatory.</li>
 * <li>A specification of the range of valid values (maybe specified with 
 * several parameters).</li>
 * <li>A <code>Locale</code> object if validation/conversion is
 * locale-dependent.</li>
 *
 * </ul>
 *
 * If the property value is not correct, an <code>ActionMessage</code> is added
 * to <code>ActionErrors</code> using the property name. The key of the
 * <code>ActionMessage</code> maybe one of the following values:
 *
 * <ul>
 *
 * <li><code>ErrorMessages.mandatoryField</code>: mandatory field.</li>
 * <li><code>ErrorMessages.incorrectValue</code>: incorrect value.</li>
 *
 * </ul>
 *
 * <p>
 * Some operations return the value resulting from the conversion. If the
 * conversion is not successful, the returned value is meaningless.
 * <p>
 */
public final class PropertyValidator {

    private final static String INCORRECT_VALUE = 
        "ErrorMessages.incorrectValue";
    private final static String MANDATORY_FIELD = 
        "ErrorMessages.mandatoryField";

    private PropertyValidator() {}

    public final static long validateLong(ActionErrors errors,
        String propertyName, String propertyValue, boolean mandatory,
        long lowerValidLimit, long upperValidLimit) {
        
        long propertyValueAsLong = 0;
        
        if (validateMandatory(errors, propertyName, propertyValue,
            mandatory)) {
        
            boolean propertyValueIsCorrect = true;
            try {
                propertyValueAsLong = 
                    new Long(propertyValue).longValue();
                if ( (propertyValueAsLong < lowerValidLimit) || 
                     (propertyValueAsLong > upperValidLimit) ) {
                    propertyValueIsCorrect = false;
                }
            } catch (NumberFormatException e) {
                propertyValueIsCorrect = false;
            }

            if (!propertyValueIsCorrect) {
                errors.add(propertyName, new ActionMessage(INCORRECT_VALUE));
            }
        
        }
        
        return propertyValueAsLong;
        
    }
    
    public final static double validateDouble(ActionErrors errors,
        String propertyName, String propertyValue, boolean mandatory,
        double lowerValidLimit, double upperValidLimit, Locale locale) {
        
        double propertyValueAsDouble = 0;
        
        if (validateMandatory(errors, propertyName, propertyValue,
            mandatory)) {
        
            boolean propertyValueIsCorrect = true;
            try {
                NumberFormat numberFormatter = 
                    NumberFormat.getNumberInstance(locale);
                propertyValueAsDouble = 
                    numberFormatter.parse(propertyValue).doubleValue();
                if ( (propertyValueAsDouble < lowerValidLimit) || 
                     (propertyValueAsDouble > upperValidLimit) ) {
                    propertyValueIsCorrect = false;
                }
            } catch (ParseException e) {
                propertyValueIsCorrect = false;
            }

            if (!propertyValueIsCorrect) {
                errors.add(propertyName, new ActionMessage(INCORRECT_VALUE));
            }
        
        }
        
        return propertyValueAsDouble;
        
    }
    
    public final static void validateString(ActionErrors errors,
        String propertyName, String propertyValue,
        boolean mandatory, Collection validValues) {
    
        if (validateMandatory(errors, propertyName, propertyValue,
            mandatory)) {
            
            if (!validValues.contains(propertyValue)) {
                errors.add(propertyName, new ActionMessage(INCORRECT_VALUE));
            }
            
        }
    
    }
    
    /**
     * Checks if a mandatory field is present.
     *
     * @return <code>false</code> if <code>propertyValue</code> is 
     *         <code>null</code> or the empty string; <code>true</code>
     *         otherwise
     */
    public final static boolean validateMandatory(ActionErrors errors,
        String propertyName, String propertyValue) {
        
        if ((propertyValue == null) || (propertyValue.length() == 0)) {
            errors.add(propertyName, new ActionMessage(MANDATORY_FIELD));
            return false;
        } else {
            return true;
        }
        
    }
    
    private final static boolean validateMandatory(ActionErrors errors,
        String propertyName, String propertyValue, boolean mandatory) {
        
        if (mandatory) {
            return validateMandatory(errors, propertyName, propertyValue);
        } else {
            return true;
        }
        
    }
    
}
