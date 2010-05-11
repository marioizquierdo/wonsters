package es.engade.thearsmonsters.model.facades.common;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.common.ThearsmonstersEntity;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

/**
 * Abstracción de métodos comunes a todas las fachadas.
 */
public abstract class ThearsmonstersFacade {
	
	
	/**
	 * Parsea la keyAsString y si no es correcta lanza un InstanceNotFoundException.
	 * @param entityClass clase sobre la que se carga la key
	 * @return una instancia de Key equivalente a keyAsString
	 * @throws InstanceNotFoundException si la clave está mal formada o es errónea
	 */
	protected Key getKeyFromString(String keyAsString, Class<? extends ThearsmonstersEntity> entityClass) throws InstanceNotFoundException {
        // Comprueba que se puede parsear la clave y la obtiene
        if (KeyUtils.isParseable(keyAsString)) {
        	Key key = KeyUtils.fromString(keyAsString);
        	return key;
        } else {
            throw new InstanceNotFoundException(keyAsString, entityClass.getName());
        }
	}
}
