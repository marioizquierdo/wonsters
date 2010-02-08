package es.engade.thearsmonsters.model.entities.common;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;


public class Id implements Serializable {

  private Key id;


  public Id(Key id) {
    this.id = id;
  }


  public String toString() {
    return KeyFactory.keyToString(id);
  }
  
  public static Id fromString(String strId) {
    //return KeyFactory.stringToKey(strId);
	  return null; // TODO
  }

}
