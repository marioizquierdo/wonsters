package es.engade.thearsmonsters.model.entities.room.types;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;

/**
 * Los monstruos cubren sus necesidades alimenticias solamente con una sustancia 
 * que produce el Ojo de la Vida, las trufas en realidad son como golosinas para los niños, 
 * y repercuten directamente en su felicidad. 
 * La temporada de trufas se completa en un ciclo de una semana de juego (siete días de juego). 
 * Durante cada ciclo se van acumulando trufas por cada franja horaria gracias a los monstruos 
 * que ejercen la tarea de cultivo dentro de ella. El tamaño de la sala condiciona el número de 
 * monstruos que pueden cultivar a la vez. La cantidad de trufas producidas depende de la suma 
 * del nivel del atributo compuesto \emph{cultivo} de cada monstruo junto con el nivel de la sala. 
 * Al terminar la semana se completa el ciclo y se reparten las trufas acumuladas entre todos 
 * los monstruos de la guarida (cantidad de espacio vital cubierto), determinando el nivel de 
 * felicidad de la semana siguiente. Es decir, cuantas más trufas se cosechen durante una semana, 
 * más felices serán los monstruos durante la semana siguiente.
 * 
 */
public class TruffleFarm extends Room {
	
	public TruffleFarm(Id roomId, Lair lair, int level, int size, 
    		RoomPublicAccess publicAccess, RoomState state) {
		super(roomId, lair, level, size, publicAccess, state);
	}
	
    public TruffleFarm(Lair lair) {
    	super(lair);
    }
	
	@Override
    public RoomType getRoomType() {
		return RoomType.TruffleFarm;
	}

	@Override
    protected double _gUpg(int level) {
		return getGarbageBuild() * Math.pow(1.4, level-1);
	}

	@Override
    protected double _eUpg(int level) {
		return getEffortBuild() * Math.pow(1.3, level-1);
	}

	@Override
    protected double _gEnl(int size) {
		return (getGarbageBuild() / 2) * Math.pow(1.8, size-1);
	}

	@Override
    protected double _eEnl(int size) {
		return (getEffortBuild() / 2) * Math.pow(1.8, size-1);
	}
	
}
