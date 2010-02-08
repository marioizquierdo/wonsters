package es.engade.thearsmonsters.http.view.applicationobjects;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.engade.thearsmonsters.util.struts.view.LabelValue;

public class Languages {
/* 
 * In a more realistic application, these values could be read from a 
 * database in the "static" block.
 */

    private final static List LANGUAGES_en = Arrays.asList(new LabelValue[] {
        new LabelValue("English", "en"),
        new LabelValue("Galician", "gl"),
        new LabelValue("Spanish", "es") });
        
    private final static List LANGUAGES_es = Arrays.asList(new LabelValue[] {
        new LabelValue("Español", "es"),
        new LabelValue("Gallego", "gl"),
        new LabelValue("Inglés", "en") });
        
    private final static List LANGUAGES_gl = Arrays.asList(new LabelValue[] {
        new LabelValue("Español", "es"),
        new LabelValue("Galego", "gl"),
        new LabelValue("Inglés", "en") });
        
    private final static Map LANGUAGES;
        
    static {
    
        LANGUAGES = new HashMap();
        LANGUAGES.put("en", LANGUAGES_en);
        LANGUAGES.put("es", LANGUAGES_es);
        LANGUAGES.put("gl", LANGUAGES_gl);
    
    }    
    
    private Languages() {}

    public final static Collection getLanguageCodes() {
        return LANGUAGES.keySet();
    }

    public final static List getLanguages(String languageCode) {
    
        List languages = (List) LANGUAGES.get(languageCode);
        
        if (languages != null) {
            return languages;
        } else {
            return (List) LANGUAGES.get("es");
        }
        
    }

}
