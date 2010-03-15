package es.engade.thearsmonsters.model.entities.room.types;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;

/**
 * Esta sala es un poco especial. Es la primera que aparece en el juego, no es necesario construirla, 
 * no se puede ampliar y no se puede mejorar. El ojo de la vida es el encargado de incubar 
 * los huevos de monstruo que se compran o producen, también es donde las crías realizan 
 * la metamorfosis para convertirse en adultos. Esta sala no tiene límite de plazas, es decir, 
 * que pueden entrar todos los monstruos que se desee simultáneamente. 
 * También es capaz de producir un mínimo de alimento, lo necesario para que los monstruos no 
 * mueran de hambre, y determina el espacio vital inicial de la guarida 
 * (para aumentarlo habrá que construir unos dormitorios). 
 * En principio es donde se alimentan las crías y los adultos, aunque luego se puede cambiar para 
 * la cocina o el comedor.
 *
 */

public class EyeOfTheLife extends Room {
	
    private static final long serialVersionUID = 20100305L;
    
	public EyeOfTheLife(Lair lair, int level, int size, 
    		RoomPublicAccess publicAccess, RoomState state) {
		super(lair, RoomType.EyeOfTheLife, level, publicAccess, state);
	}

}
