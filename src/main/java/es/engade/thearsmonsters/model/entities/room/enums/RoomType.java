package es.engade.thearsmonsters.model.entities.room.enums;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccessClose;
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
	
	EyeOfTheLife 			(false, 0, 0, 
							1, 1),
							
	Warehouse				(false, 0, 1, 
							1, 1),
							
	TradeOffice				(false, 50, 30, 
							1, 10),
					
	TruffleFarm				(false, 300, 150, 
							1, 10),
							
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