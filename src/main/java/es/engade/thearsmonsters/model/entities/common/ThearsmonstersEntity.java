package es.engade.thearsmonsters.model.entities.common;

import com.google.appengine.api.datastore.Key;

/**
 * Abstracción de métodos comunes a todas las entities
 */
public abstract class ThearsmonstersEntity {
	
	/**
	 * IdKey son las claves primarias de las entities.
	 * Son del estilo 'User(1)/Lair(2)/Monster(13)'
	 */
	public abstract Key getIdKey();
	public abstract void setIdKey(Key idKey);
	
	/**
	 * Id es la representación como String.
	 * Se utiliza para transmitir el idKey en las URLs como parámetro.
	 * Son del estilo 'aglub19hcHBfaWRyIQsSBFVzZXIYAQwLEgRMYWlyGAIMCxIHTW9uc3RlchgNDA'
	 */
	public String getId() {
		return KeyUtils.toString(getIdKey());
	}
	
	public void touch() {
		
	}
	
}
