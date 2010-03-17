package es.engade.thearsmonsters.model.entities.room.enums;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.publicaccess.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.publicaccess.RoomPublicAccessClose;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;
import es.engade.thearsmonsters.model.entities.room.state.RoomUpgradingState;

/**
 * Tipos de sala
 * Esta enumeración es a su vez:
 *   - Una enumeración de los tipos de sala que hay (RoomType.values();)
 *   - Un índice con los datos estáticos para cada tipo de sala (RoomType.Warehouse.getGarbageBuild();)
 *   - Una factoría para el estado inicial de las salas (RoomType.Warehouse.build(lair);)
 *   - Una estrategia para la clase Room, ya que delega algunas operaciones en su type (room.getEffortUpgrade)
 *   
 *  Cuando se añada un nuevo tipo de sala, prácticamente toda su información se añade en esta clase.
 */
public enum RoomType {
	// RoomType name		// publicable, garbageBuild, effortBuild,
							// initialLevel, maxLevel	// {maxLvel = -1 => no limits}
	
	// TODO: Información de salas desactualizada. Hay que repasar estos comentarios y poenrlos bien.
	// Trono implementará las MonsterActions en base a estas descripciones (cuande estén bien).
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
	 */
	EyeOfTheLife 			(false, 0, 0, 
							1, 1),
	
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
	Warehouse				(false, 0, 1, 
							1, 1),
							
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
	TradeOffice				(false, 50, 30, 
							1, 10),
	
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
	 */
	TruffleFarm				(false, 300, 150, 
							1, 10),
	
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
	Dormitories				(false, 0, 0,
							1, 1);
	
	/*Al descomentar esto, hay que descomentar tambien en los dos metodos siguientes RoomType.newRoom
	MetalLeisureRoom		(...),
							
	ChillOutLeisureRoom		(...),
							
	TechnoLeisureRoom		(...),
							
	ReggaetonLeisureRoom	(...),
							
	IndieRockLeisureRoom	(...),
							
	Gym						(...),
							
	Classroom				(...),
							
	Nursery					(...);
	*/
	// ..
	

	private final boolean publicable;
	private final int garbageBuild;
	private final int effortBuild;
	private final int initialLevel;
	private final int maxLevel;
	
	RoomType(boolean publicable, int garbageBuild, int effortBuild,
			int initialLevel, int maxLevel) {
		this.publicable = publicable;
		this.garbageBuild = garbageBuild;
		this.effortBuild = effortBuild;
		this.initialLevel = initialLevel;
		this.maxLevel = maxLevel;            // {-1: no limits, x(x>=0): exactly x}
	}
	
	// Getters
	public boolean isPublicable() {return publicable;}
	public int getGarbageBuild() {return garbageBuild;}
	public int getEffortBuild() {return effortBuild;}
	public int getInitialLevel() {return initialLevel;}
	public int getMaxLevel() {return maxLevel;}
	
	
	
	/**
	 * Factroy method.
	 * @return a new instance of a Room at its initial state, depending on this RoomType.
	 */
	public Room build(Lair lair) {
		RoomPublicAccess publicAccess;
		RoomState state;
		
		// EyeOfTheLife y Dormitories starts in normal state,
		// the other rooms starts in works state: monsters must to build them.
		if(this.equals(EyeOfTheLife) || this.equals(Dormitories)) {
			state = new RoomNormalState();
		} else {
			state = new RoomUpgradingState(0);
		}
		
		// All rooms starts with closed public access.
		publicAccess = new RoomPublicAccessClose();
		
		// Instantiate new room
		return new Room(lair, this, this.initialLevel, publicAccess, state);
	}
	
	
	/**
	 * Acts as Strategy method for Room delegation in getGarbageUpgrade
	 */
	public double getGarbageUpgrade(int level) {
		switch (this) {
		case TradeOffice: return 100 * Math.pow(1.8, level-1);
		case TruffleFarm: return getGarbageBuild() * Math.pow(1.4, level-1);
		case Dormitories: return 20 * Math.pow(1.15, level-1);
		default: return -1;
		}
	}
   
	/**
	 * Acts as Strategy method for Room delegation in getEffortUpgrade
	 */
	public double getEffortUpgrade(int level) {
		level = level -1; // se calcula para el nivel anterior (el minimo nivel es 0, no 1).
		switch (this) {
		case TradeOffice: return getEffortBuild() * Math.pow(1.6, level);
		case TruffleFarm: return getEffortBuild() * Math.pow(1.3, level-1);
		case Dormitories: return 50 * Math.pow(1.1, level);
		default: return -1;
		}
	}
   
   /**
    * ENLARGE NOT SUPPORTED IN THIS VERSION
    * Garbage needed to enlarge to that size.
    */
	public double getGarbageEnlarge(int size) {
 	   switch (this) {
 	   /* Por ahora, enlarge no soportado */
 		default: return -1;	/* Por defecto indica que no se puede agrandar */
 	   }
    }
    /**
     * ENLARGE NOT SUPPORTED IN THIS VERSION
     * Effort needed to enlarge to that size.
     */
     public double getEffortEnlarge(int size) {
  	   switch (this) {
  	   /* Por ahora, enlarge no soportado */
  		default: return -1;	/* Por defecto indica que no se puede agrandar */
  	   }
     }
     
     
 	/**
 	 * Convierte la representación de una lista en String a la lista real.
 	 * Se usa para simplificar la definición de listas de tipos de sala.
 	 * @param stringList Literales de AgeState separados por comas, xej: "Warehouse, TruffleFarm, EyeOfTheLife",
  	 * 			o bien el string "all", que incluye a todas las edades,
  	 * 			o bien un string vacío "", que devuelve una lista vacía.
 	 * @return Lista de RoomTypes correspondientes con los elementos declarados en el String de entrada.
 	 */
 	public static List<RoomType> list(String stringList) {
 		if(stringList.equals("") || stringList==null) { // devuelve lista vacía
 			return new ArrayList<RoomType>(0);
 		}else if(stringList.toLowerCase().equals("all")) { // all significa añadir todos los tipos que haya en AgeState
 			List<RoomType> list = new ArrayList<RoomType>(RoomType.values().length);
 			for(RoomType ageState : RoomType.values()) {
 				list.add(ageState);
 			}
 			return list;
 		} else {
 			String[] stringArray = stringList.split(","); // Se separan por comas
 			List<RoomType> list = new ArrayList<RoomType>(stringArray.length); // Se define la lista
 			for(String e : stringArray) { // se convierte cada String en su elemento de AgeState correspondiente
 				list.add(Enum.valueOf(RoomType.class, e.trim()));
 			}
 			return list;
 		}
 	}
	   
		
}