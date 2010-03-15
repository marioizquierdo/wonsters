package es.engade.thearsmonsters.model.entities.room.enums;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;
import es.engade.thearsmonsters.model.entities.room.types.Dormitories;
import es.engade.thearsmonsters.model.entities.room.types.EyeOfTheLife;
import es.engade.thearsmonsters.model.entities.room.types.TradeOffice;
import es.engade.thearsmonsters.model.entities.room.types.TruffleFarm;
import es.engade.thearsmonsters.model.entities.room.types.Warehouse;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

/**
 * This enumeration makes easier to return a concrete class of a Room
 * depending on the database field: 'Room.roomType'
 * When a new type of room is implemented, the enumeration must be enlarged
 */
public enum RoomType {
	// RoomType name		// code, publicable, garbageBuild, effortBuild,
							// maxSize, maxLevel,						// {-1: no limits, x(x>=0): exactly x}
							// maxWorkers, maxCustomers,				// {-2: no limits, -1: until no more free space, x(x>=0): exactly x monsters}
							// allowedWorkerAges, allowedCustomerAges	// List of ages allowed for that TaskRole
	
	EyeOfTheLife 			((byte)1, false, 0, 0, 
							1, 1, 
							0, -2, 
							nobody(), everybody()),
							
	Warehouse				((byte)2, false, 0, 1, 
							-1, 1,
							-1, 0,
							ages("Adult"), nobody()),
							
	TradeOffice				((byte)3, false, 50, 30, 
							-1, 10, 
							0, 0,
							nobody(), nobody()),
					
	TruffleFarm				((byte)4, false, 300, 150, 
							-1, 10, 
							-1, 0, 
							ages("Adult"), nobody()),
							
	Dormitories				((byte)5, false, 0, 0,
							-1, 1, 
							0, -2, 
							nobody(), everybody());
	/*Al descomentar esto, hay que descomentar tambien en los dos metodos siguientes RoomType.newRoom
	MetalLeisureRoom		((byte)6, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	ChillOutLeisureRoom		((byte)7, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	TechnoLeisureRoom		((byte)8, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	ReggaetonLeisureRoom	((byte)9, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	IndieRockLeisureRoom	((byte)10, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	Gym						((byte)11, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	Classroom				((byte)12, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList")),
							
	Nursery					((byte)13, true, garbageBuild, effortBuild, 
							maxSize, maxLevel, 
							maxWorkers, maxCustomers, 
							list("ageWorkerList"), list("ageCustomerList"));
	*/
	// ...
	
	/**
	 * Create a new Room depending on the roomType type code
	 * @return a concrete class of Room extension or null if the roomType is not a RoomType.code
	 */
	static public Room newRoom(byte roomTypeCode, Id roomId, Lair lair, 
			int level, int size, RoomPublicAccess publicAccess, RoomState state) {
		switch(roomTypeCode) {
		case 01: return new EyeOfTheLife(lair, level, size, publicAccess, state);
		case 02: return new Warehouse(lair, level, size, publicAccess, state);
		case 03: return new TradeOffice(lair, level, size, publicAccess, state);
		case 04: return new TruffleFarm(lair, level, size, publicAccess, state);
		case 05: return new Dormitories(lair, level, size, publicAccess, state);
		/*case 06: return new MetalLeisureRoom(roomId, login, level, size, publicAccess, state);
		case 07: return new ChillOutLeisureRoom(roomId, login, level, size, publicAccess, state);
		case 08: return new TechnoLeisureRoom(roomId, login, level, size, publicAccess, state);
		case 09: return new IndieRockLeisureRoom(roomId, login, level, size, publicAccess, state);
		case 10: return new ReggaetonLeisureRoom(roomId, login, level, size, publicAccess, state);
		case 11: return new Gym(roomId, login, level, size, publicAccess, state);
		case 12: return new Classroom(roomId, login, level, size, publicAccess, state);publicAccess, state); */
    	// ...
		
		default: return null;
		}
		
	}    	
	
	/**
	 * Create a new Room depending on the roomType type code with its Default State
	 * @return a concrete class of Room extension or null if the roomType is not a RoomType.code
	 */
	static public Room newRoom(byte roomTypeCode, Lair lair) {
		switch(roomTypeCode) {
		case 1: return new EyeOfTheLife(lair);
		case 2: return new Warehouse(lair);
		case 3: return new TradeOffice(lair);
		case 4: return new TruffleFarm(lair);
		case 5: return new Dormitories(lair);
		/*case 6: return new MetalLeisureRoom(login);
		case 7: return new ChillOutLeisureRoom(login);
		case 8: return new TechnoLeisureRoom(login);
		case 9: return new IndieRockLeisureRoom(login);
		case 10: return new ReggaetonLeisureRoom(login);
		case 11: return new Gym(login);
		case 12: return new Classroom(login);
		case 13: return new Nursery(login); */
    	// ...
		
		default: return null;
		}
	}
	
	/**
	 * If you know the code then you can get the corresponding enum instance
	 */
	static public RoomType getFromCode(byte code) 
		throws InstanceNotFoundException {
		
		for(RoomType e : RoomType.values()) {
			if(code == e.code()) return e;
		}
			
		throw new InstanceNotFoundException(
        	"RoomType not found (code="+ code +")", 
        		RoomType.class.getName());
	}
	
	private final byte code; // Codigo del tipo de sala en la BBDD
	private final boolean publicable;
	private final int garbageBuild;
	private final int effortBuild;
	private final int maxSize;
	private final int maxLevel;
	private final int maxWorkers;
	private final int maxCustomers;
	private final List<MonsterAge> ageWorker; // esto puede ser diferente para cada edad
	private final List<MonsterAge> ageCustomer;
	
	RoomType(byte code, boolean publicable, int garbageBuild, int effortBuild,
			int maxSize, int maxLevel, 
			int maxWorkers, int maxCustomers,
			List<MonsterAge> ageWorker, List<MonsterAge> ageCustomer) {
		this.code = code;
		this.publicable = publicable;
		this.garbageBuild = garbageBuild;
		this.effortBuild = effortBuild;
		this.maxSize = maxSize;    // {-1: no limits, x(x>=0): exactly x}
		this.maxLevel = maxLevel;  // {-1: no limits, x(x>=0): exactly x}
		this.maxWorkers = maxWorkers;		// {-2: no limits, -1: until no more free space, x(x>=0): exactly x monsters}
		this.maxCustomers = maxCustomers;	// {-2: no limits, -1: until no more free space, x(x>=0): exactly x monsters}
		this.ageWorker = ageWorker;
		this.ageCustomer = ageCustomer;
	}
	
	// Getters
	public byte code()   {return code;}
	public byte getCode() {return code;}
	public boolean isPublicable() {return publicable;}
	public int getGarbageBuild() {return garbageBuild;}
	public int getEffortBuild() {return effortBuild;}
	public int getMaxSize() {return maxSize;}
	public int getMaxLevel() {return maxLevel;}
	public int getMaxWorkers() {return maxWorkers;}
	public int getMaxCustomers() {return maxCustomers;}
	public List<MonsterAge> getAgeWorker() {return ageWorker;}
	public List<MonsterAge> getAgeCustomer() {return ageCustomer;}
	
	public boolean equals(RoomType roomType) {return this.code() == roomType.code();}
	
	private static List<MonsterAge> ages(String stringList) {
		return MonsterAge.list(stringList);
	}
	private static List<MonsterAge> nobody() {
		return MonsterAge.list("");
	}
	private static List<MonsterAge> everybody() {
		return MonsterAge.list("all");
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
			List<RoomType> list = new ArrayList<RoomType>(MonsterAge.values().length);
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