package es.engade.thearsmonsters.model.entities.room.types;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;

/**
 * Es donde se almacena la basura de la guarida. 
 * Su tamaño limita la cantidad de recursos que se pueden tener ahorrados. 
 * Cada plaza del almacén se corresponde con cierta cantidad de basura (una constante predeterminada, 
 * por ejemplo 1000 de basura por plaza). Solamente puede tener monstruos empleados 
 * (no clientes) adultos, que se encargan de recolectar basura durante las franjas horarias 
 * que tengan asignadas. Aunque supuestamente esta tarea se realice fuera del almacén, 
 * solamente podrán trabajar tantos monstruos simultáneos como plazas haya (para mantener la 
 * coherencia con el resto de las salas). Si el almacén llega al máximo de capacidad los monstruos 
 * no podrán recolectar más basura.
 * Los atributos de los monstruos se verán más adelante con detalle. 
 * Los que se usan para las tareas de la guarida son los <b>atributos compuestos específicos</b>, 
 * que son el resultado de combinar atributos simples y compuestos generales con las <b>habilidades de 
 * trabajo</b>, las cuales mejoran con la práctica (es decir, mejoran un poco cada vez que se realice 
 * la tarea asociada). La cantidad de basura que recolecta un monstruo por franja horaria viene 
 * determinada por su atributo \emph{recolección de basura}.
 * Esta sala no se puede subir de nivel (actualizar), solamente ampliar para tener un mayor espacio de almacenamiento.
 */
public class Warehouse extends Room {
	
	public Warehouse(Id roomId, Lair lair, int level, int size, 
    		RoomPublicAccess publicAccess, RoomState state) {
		super(roomId, lair, level, size, publicAccess, state);
	}
	
    public Warehouse(Lair lair) {
    	super(lair);
    }
	
	public RoomType getRoomType() {
		return RoomType.Warehouse;
	}

	protected double _gEnl(int size) {
		return 100 * Math.pow(1.4, size-1);
	}

	protected double _eEnl(int size) {
		return 30 * Math.pow(1.6, size-1);
	}
	
	
	/***** Exclusive Warehouse methods *******/
	
	public int getGarbageStorageCapacity() {
		return getGarbageStorageCapacityBySize(getSize());
	}
	
	public int getGarbageStorageCapacityBySize(int size) {
		if(size<=0) {
			return 0;
		} else {
			return gEnl(size) * 5; // need always to be more than _gEnl
		}
	}
	
}
