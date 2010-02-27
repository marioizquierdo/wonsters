package es.engade.thearsmonsters.model.entities.common;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;


public class Id implements Serializable {

    private static final long serialVersionUID = 4486611656587675338L;
	private Key id;

	public Id(Key id) {
		this.id = id;
	}

	public static Id autoGenerate() {
		// the numeric identifier of the key in kind, unique across all root entities of this kind, must not be zero
		long randomid = (long) (Math.random() * 10000);
		return new Id(KeyFactory.createKey("thearsmonsters", randomid));
	}


	public static Id fromString(String strId) {
		return new Id(KeyFactory.stringToKey(strId));
	}
	
	@Override
	public String toString() {
		return KeyFactory.keyToString(id);
	}

}
