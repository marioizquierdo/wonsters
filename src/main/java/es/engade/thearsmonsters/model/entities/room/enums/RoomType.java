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
	// RoomType(publicable, garbageBuild, effortBuild, maxLevel(-1 => no limit))
	
	/**
	 * Esta sala es un poco especial. Es la primera que aparece en el juego, no es necesario construirla, 
	 * no se puede ampliar y no se puede mejorar. Papá Monstruo es el encargado de incubar 
	 * los huevos de monstruo que se compran o producen, también es donde las crías realizan 
	 * la metamorfosis para convertirse en adultos.
	 * Aquí en principio no se realiza ninguna tarea.
	 */
	MainMonster(false, 0, 0, 1),
							
	/**
	 * Es donde los monstruos descansan cada día.
	 * Al igual que PapaMonstruo, no es necesario construirla porque ya aparece al 
	 * comienzo del juego, en cambio si� que se puede mejorar su nivel. 
	 * Por cada nivel de los dormitorios se suma 10 de espacio vital.
	 * Aqui tampoco se realiza ninguna accion.
	 */
	Dormitories(false, 0, 0, -1) {
		public double getGarbageUpgrade(int level) {
			//return 50 * Math.pow(1.4, level-1);
			switch(level) {
				case 1: return 50;
				case 2: return 80;
				case 3: return 120;
				case 4: return 250;
				case 5: return 500;
				case 6: return 1200;
				case 7: return 3000;
				case 8: return 5000;
				case 9: return 9000;
				case 10: return 17000;
				default: return 17000 * Math.pow(1.6, level-10);
			}
		}
		public double getEffortUpgrade(int level) {
			switch(level) {
				case 1: return 20;
				case 2: return 30;
				case 3: return 50;
				case 4: return 100;
				case 5: return 300;
				case 6: return 800;
				case 7: return 1000;
				case 8: return 2000;
				case 9: return 4000;
				case 10: return 8000;
				default: return 200000 * Math.pow(1.7, level-10);
			}
		}
		
	},
	
	/**
	 * Es donde se almacena la basura de la guarida. 
	 * Su nivel limita la cantidad de basura que se pueden tener almacenada 
	 * (ver roomData.getGarbageStorageCapacity()).
	 * La accion que se realiza aquii es "recoleccion de basura". Por cada turno
	 * dedicado se recolecta tanta basura como el atributo compuesto "GarbageHarvest".
	 */
	Warehouse(false, 0, 5, -1) {
		public double getGarbageUpgrade(int level) {
			switch(level) {
				case 1: return 20;
				case 2: return 30;
				case 3: return 40;
				case 4: return 60;
				case 5: return 100;
				case 6: return 500;
				case 7: return 1000;
				case 8: return 2000;
				case 9: return 4000;
				case 10: return 8000;
				default: return 8000 * Math.pow(1.6, level-10);
			}
		}
		public double getEffortUpgrade(int level) {
			return 40 * Math.pow(1.4, level-1);
		}
	},
							
	/**
	 * Donde se realiza el cambio de basura por dinero.
	 * Cuanto más nivel tenga la oficina de comercio, menos comisión se cobra
	 * por cambio y más dinero se puede almacenar.
	 * Aquí no se realiza ninguna tarea por parte de los monstruos.
	 */			
	TradeOffice(false, 50, 20, 10) {
		public double getGarbageUpgrade(int level) {
			switch(level) {
				case 1: return 25;
				case 2: return 50;
				case 3: return 100;
				case 4: return 400;
				case 5: return 800;
				case 6: return 1700;
				case 7: return 2000;
				case 8: return 2500;
				case 9: return 5000;
				case 10: return 10000;
				default: return 0; // should never happen
			}
		}
		
		public double getEffortUpgrade(int level) {
			switch(level) {
				case 1: return 50;
				case 2: return 100;
				case 3: return 200;
				case 4: return 400;
				case 5: return 800;
				case 6: return 1700;
				case 7: return 2000;
				case 8: return 2400;
				case 9: return 3000;
				case 10: return 3000;
				default: return 0; // should never happen
			}
		}
	},
	
	
	/**
	 * Donde se entrenan los monstruos.
	 * La acción que se realiza aquí es "entrenar".
	 * Cuanto más nivel tenga el gimnasio más fuerza se incrementa en el monstruo
	 * en cada turno.
	 */			
	
	Gym(true, 100, 200, 10) {
		public double getGarbageUpgrade(int level) {
			switch(level) {
				case 1: return 50;
				case 2: return 300;
				case 3: return 500;
				case 4: return 800;
				case 5: return 1600;
				case 6: return 2000;
				case 7: return 3000;
				case 8: return 4000;
				case 9: return 5000;
				case 10: return 2000;
				default: return 0; // should never happen
			}
		}
		public double getEffortUpgrade(int level) {
			switch(level) {
				case 1: return 100;
				case 2: return 200;
				case 3: return 400;
				case 4: return 800;
				case 5: return 1600;
				case 6: return 3200;
				case 7: return 6500;
				case 8: return 12000;
				case 9: return 24000;
				case 10: return 50000;
				default: return 0; // should never happen
			}
		}
	},
		
		
		/**
		 * Donde se entrenan los monstruos.
		 * La acción que se realiza aquí es "entrenar".
		 * Cuanto más nivel tenga el gimnasio más fuerza se incrementa en el monstruo
		 * en cada turno.
		 */			
		
	Nursery(true, 500, 300, 10) {
		public double getGarbageUpgrade(int level) {
			switch(level) {
				case 1: return 1000;
				case 2: return 1500;
				case 3: return 2000;
				case 4: return 2500;
				case 5: return 3000;
				case 6: return 3500;
				case 7: return 4000;
				case 8: return 4500;
				case 9: return 5000;
				case 10: return 10000;
				default: return 0; // should never happen
			}
		}
		public double getEffortUpgrade(int level) {
			return 300 * Math.pow(1.5, level);
		}
	},
	/*
	 * Las siguientes salas aun no estan disponibles en esta version. 
	 * Se marcan con un coste negativo para listarlas en la vista.
	 * */
	LoveWardrobe(false, -1, 0,0),
	Classroom(false, -1, 0,0),
	MetalLeisureRoom(false, -1, 0,0),
	ChillOutLeisureRoom(false, -1, 0,0), 
	TechnoLeisureRoom(false, -1, 0,0),
	ReggaetonLeisureRoom(false, -1, 0,0),
	IndieRockLeisureRoom(false, -1, 0,0),
	TruffleFarm(false, -1, 0,0);
	
	/*Al descomentar esto, hay que descomentar tambien en los dos metodos siguientes RoomType.newRoom
	

	
	MetalLeisureRoom		(...),
							
	ChillOutLeisureRoom		(...),
							
	TechnoLeisureRoom		(...),
							
	ReggaetonLeisureRoom	(...),
							
	IndieRockLeisureRoom	(...),
							
	TruffleFarm				(...),
							
	Classroom				(...),
							
						(...);
	*/
	// ..
	

	private final boolean publicable;
	private final int garbageBuild;
	private final int effortBuild;
	private final int maxLevel;
	
	RoomType(boolean publicable, int garbageBuild, int effortBuild, int maxLevel) {
		this.publicable = publicable;
		this.garbageBuild = garbageBuild;
		this.effortBuild = effortBuild;
		this.maxLevel = maxLevel; // {-1: no limit, x | x >= 0: exactly x}
	}
	
	// Getters
	public boolean isPublicable() {return publicable;}
	public int getGarbageBuild() {return garbageBuild;}
	public int getEffortBuild() {return effortBuild;}
	public int getMaxLevel() {return maxLevel;}
	
	
	
	/**
	 * Factroy method. Creates new Room in its initial state, but do not add it to the lair.
	 * If you want to create a new room in a lair, use lair.buildRoom(RoomType).
	 * @return a new instance of a Room at its initial state, depending on this RoomType.
	 */
	public Room build(Lair lair) {
		RoomPublicAccess publicAccess;
		RoomState state;
		int initialLevel;
		
		// MainMonster y Dormitories starts in normal state,
		// the other rooms starts in works state: monsters must build them.
		if(this.equals(MainMonster) || this.equals(Dormitories)) {
			state = new RoomNormalState();
			initialLevel = 1;
		} else {
			state = new RoomUpgradingState(0);
			initialLevel = 0;
		}
		
		// All rooms starts with closed public access.
		publicAccess = new RoomPublicAccessClose();
		
		// Instantiate new room
		return new Room(lair, this, initialLevel, publicAccess, state);
	}
	
	
	/**
	 * Acts as Strategy method for Room delegation in getGarbageUpgrade
	 */
	public double getGarbageUpgrade(int level) {
		return -1; // Valor por defecto, que significa que no se puede subuir de nivel.
	}
   
	/**
	 * Acts as Strategy method for Room delegation in getEffortUpgrade
	 */
	public double getEffortUpgrade(int level) {
		return -1;
	}
   
 	/**
 	 * Convierte la representación de una lista en String a la lista real.
 	 * Se usa para simplificar la definición de listas de tipos de sala.
 	 * @param stringList Literales de AgeState separados por comas, xej: "Warehouse, TruffleFarm, MainMonster",
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