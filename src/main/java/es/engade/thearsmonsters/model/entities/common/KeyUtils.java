package es.engade.thearsmonsters.model.entities.common;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class KeyUtils {

    public static boolean isParseable(String strId) {
        try {
            KeyUtils.fromString(strId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static String toString(Key key) {
        return KeyFactory.keyToString(key);
    }
      
    public static Key fromString(String strId) {
        return KeyFactory.stringToKey(strId);
    }
}
