package es.engade.thearsmonsters.model.entities.room.types;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;
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
	
	public Dormitories(Id roomId, Lair lair, int level, int size, 
    		RoomPublicAccess publicAccess, RoomState state) {
		super(roomId, lair, level, size, publicAccess, state);
	}
	
    public Dormitories(Lair lair) {
    	super(lair);
    	this.size = 1;
        this.state = new RoomNormalState();
    }
	
	@Override
    public RoomType getRoomType() {
		return RoomType.Dormitories;
	}

	@Override
    protected double _gEnl(int size) {
		return 20 * Math.pow(1.15, size-1);
	}

	@Override
    protected double _eEnl(int size) {
		return 50 * Math.pow(1.1, size-1);
	}
	
}
