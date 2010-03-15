package es.engade.thearsmonsters.model.entities.room.types;

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
	
    private static final long serialVersionUID = 20100305L;
    
	public TruffleFarm(Lair lair, int level, 
    		RoomPublicAccess publicAccess, RoomState state) {
		super(lair, RoomType.TruffleFarm, level, publicAccess, state);
	}


}
