package es.engade.thearsmonsters.util.struts.view;

/**
 * A class to represent label-value pairs for use in collections
 * that are utilized by the <code>&lt;html:options&gt;</code> tag.
 */
public class LabelValue {

    private String label;
    private String value;
    
    public LabelValue(String label, String value) {
        this.label = label;
        this.value = value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

}
