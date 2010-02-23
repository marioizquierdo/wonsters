package es.engade.thearsmonsters.model.entities.room.types;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;

/**
 * Es donde se guarda el dinero en forma de monedas de oro. 
 * Además permite intercambiar dinero por basura y viceversa, es decir, que para conseguir dinero se 
 * puede recolectar basura y luego cambiarla en esta sala. 
 * Los monstruos no necesitan entrar nunca, a menos que sea como obreros. 
 * El cambio de basura por dinero lo hace el jugador directa e instantáneamente. 
 * Solamente se puede hacer un intercambio basura-dinero por día de juego. 
 * Además el cambio no es en proporción 1 a 1 porque se cobra comisión. 
 * Cuando se construye la oficina, en nivel uno, el cambio es de 1 a 0.5, y lo mismo a la inversa. 
 * Es decir, que si se cambia dinero por basura y luego basura por dinero en realidad estamos 
 * perdiendo muchos recursos. Cuanto mayor sea el nivel de la oficina de comercio, 
 * menor será la comisión cobrada, pudiendo llegar a la relación ideal 1 a 1 (nivel 10). 
 * En ningún caso se puede ganar dinero haciendo cambios.
 * Cuanto más grande sea el tamaño, más cantidad de dinero se puede almacenar.
 */
public class TradeOffice extends Room {
	
	public TradeOffice(Id roomId, Lair lair, int level, int size, 
    		RoomPublicAccess publicAccess, RoomState state) {
		super(roomId, lair, level, size, publicAccess, state);
	}
	
    public TradeOffice(Lair lair) {
    	super(lair);
    }
	
	@Override
    public RoomType getRoomType() {
		return RoomType.TradeOffice;
	}

	@Override
    protected double _gUpg(int level) {
		return 100 * Math.pow(1.8, level-1);
	}

	@Override
    protected double _eUpg(int level) {
		return getEffortBuild() * Math.pow(1.6, level-1);
	}

	@Override
    protected double _gEnl(int size) {
		return getGarbageBuild() * 4 * Math.pow(1.4, size-1);
	}

	@Override
    protected double _eEnl(int size) {
		return getEffortBuild() * 2 * Math.pow(1.4, size-1);
	}
	
	
	/* ---- Exclusive TradeOffice methods ---- */
	
	/**
	 * Max amount of money that this lair can store.
	 */
	public int getMoneyStorageCapacity() {
		return getMoneyStorageCapacityBySize(getSize());
	}
	
	public int getMoneyStorageCapacityBySize(int size) {
		return gEnl(size) * 4; // need always to be more than _gEnl
	}
	
	/**
	 * Percentage commision (a number between 0 and 100)
	 * depends on the level of the trade Office.
	 */
	public int getPercentageCommision() {
		return getPercentageCommisionByLevel(getLevel()); 
	}
	
	public int getPercentageCommisionByLevel(int level) {
		switch (level) {
		case 1: return 50;
		case 2: return 45;
		case 3: return 35;
		case 4: return 30;
		case 5: return 25;
		case 6: return 20;
		case 7: return 15;
		case 8: return 10;
		case 9: return 5;
		default: return 0;
		}
	}
	
}
