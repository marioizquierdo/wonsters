package es.engade.thearsmonsters.model.entities.room.types;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;

/**
 * Es donde los monstruos descansan cada día.
 * Al igual que el Ojo de la Vida, no es necesario construirla porque ya aparece al 
 * comienzo del juego, en cambio sí que se puede (y se debe) ampliar y mejorar. 
 * El concepto de plaza en esta sala es diferente al que tienen las demás; 
 * las plazas del dormitorio determinan el <b>espacio vital</b> que hay en la guarida 
 * (las razas mejores necesitan más espacio vital), por lo tanto el tamaño de los 
 * apartamentos indica el número de monstruos que puede haber en la guarida. 
 * El nivel de los dormitorios influye en el carisma de todos los monstruos de la guarida, 
 * unos buenos dormitorios implican mayor aceptación social.
 */
public class Dormitories extends Room {
	
    private static final long serialVersionUID = 20100305L;
    
	public Dormitories(Lair lair, int level,
    		RoomPublicAccess publicAccess, RoomState state) {
		super(lair, RoomType.Dormitories, level, publicAccess, state);
	}
	
}
