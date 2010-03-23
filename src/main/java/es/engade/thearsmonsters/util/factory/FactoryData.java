package es.engade.thearsmonsters.util.factory;

import java.util.Date;
import java.util.List;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.RoomData;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.util.DateTools;

/**
 * Esta clase tiene métodos estáticos para generar datos (salas, monstruos, etc).
 * Estos datos se utilizan como datos de prueba, o donde sea necesario generar nuevos datos.
 */
public class FactoryData {
	
	public static final Date now = DateTools.now();
	public static final Date tomorrow = DateTools.tomorrow();
	
	
	
	//------ Build Users ------//
	
	public enum UserWhoIs {
		Random,
		Default {
			public User build(String login) {
				return generateUserScaffold(login);
			}
		};
		// ...
		
		public User build() { return generateUserScaffold(); } // default
		public User build(String login) { return generateUserScaffold(login); } // default
	}
	
	
	//------ Build Lairs ------//
	
	/**
	 * Instancias de Guarida.
	 * Son las diferentes guaridas que se pueden obtener con esta Factoría,
	 * se trata de una enumeración con elementos que describen alguna característica importante del
	 * tipo de guarida que se va a devolver (para identificarlo), y tienen métodos build para generarla.
	 * Ejemplo: FactoryData.LairWhatIs.InInitialState.build('user_login');
	 */
	public enum LairWhatIs { 
		InInitialState  { 		// Estado inicial cuando el jugador comienza el juego
			public Lair build() {
				return generateInitialLair();
			}
			public Lair build(String login) {
				return generateInitialLair(login);
			}
		},
		Default { 				// Guarida test por defecto, generada en generateUserScaffold
			public Lair build() {
				return generateDefaultLair();
			}
			public Lair build(String login) {
				return generateDefaultLair(login);
			}
		};
		// ...
		
		public Lair build() { return null; } // default
		public Lair build(String login) { return null; } // default
	}
	
	
	//------ Build Rooms ------//
	
	/**
	 * Genera una sala con todas sus relaciones establecidas (guarida, usuario, etc),
	 * siguieno el criterio establecido, siguiendo alguno de los criterios de la enumeración.
	 * Ejemplo: FactoryData.RoomWhatIs.EyeOfTheLife.build();
	 */
	public enum RoomWhatIs {
		/**
		 * Sala en su estado inicial, cuando se compra y se construye.
		 */
		InInitialState,
		
		/**
		 * Sala en obras.
		 */
		InWorks {
			public Room build() {
				return FactoryData.RoomWhatIs.InWorks.build(RoomType.TradeOffice); // pone TradeOffice si no se indica un tipo
			}
			public Room build(RoomType roomType) { 
				Room room = generateRoomByType(roomType);
				room.setStateStartUpgrading();
				return room; 
			}
		},
		
		/**
		 * Sala que no está en obras.
		 */
		InNormalState {
			public Room build() {
				return FactoryData.RoomWhatIs.InNormalState.build(RoomType.TradeOffice); // pone TradeOffice si no se indica un tipo
			}
			public Room build(RoomType roomType) { 
				Room room = generateRoomByType(roomType);
				room.setStateCancelWorks();
				return room; 
			}
		},
		
		EyeOfTheLife {
			public Room build() { return generateRoomByType(RoomType.EyeOfTheLife); }
		},
		Dormitories {
			public Room build() { return generateRoomByType(RoomType.Dormitories); }
		},
		Warehouse {
			public Room build() { return generateRoomByType(RoomType.Warehouse); }
		},
		TradeOffice {
			public Room build() { return generateRoomByType(RoomType.TradeOffice); }
		};
		
		public Room build() { return generateRoomByType(RoomType.EyeOfTheLife); } // default
		public Room build(RoomType roomType) { return generateRoomByType(roomType); } // default
	}
	
	
	//------ Build MonsterEggs ------//
	
	public enum EggWhatIs {
		Random {
			public MonsterEgg build() {
				return new MonsterEgg(generateDefaultLair(), MonsterRace.Bu, new Date());
			}
		};
		// ...
		
		public MonsterEgg build() { return null; } // default
	}
	
	
	//------ Build Monsters ------//
    
	/**
	 * Instancias de Monstruo.
	 * Son los diferentes monstruos que se pueden generar,
	 * se trata de una enumeración con elementos que describen alguna característica importante del
	 * tipo de monstruo que se va a devolver (para identificarlo).
	 * Cada vez que se genera un monstruo, también se genera su guarida, usuario, salas, etc,
     * y todo es nuevo (dos llamadas a generateMonster(MonsterWhatIs.Child) devuelven dos monstruos diferentes,
     * con guaridas diferentes, pero asegurándose de que al menos tienen la edad Child.
	 * Ejemplo: FactoryData.MonsterWhatIs.Child.build();
	 */
	public enum MonsterWhoIs { 
		Random {
			public Monster build() {
				return generateRandMonster();
			}
		},  
		Child { // Monstruo con edad = Child
			public Monster build() {
				return generateMonsterByAge(MonsterAge.Child);
			}
		},
		Adult { // Monstruo con edad = Adult
			public Monster build() {
				return generateMonsterByAge(MonsterAge.Adult);
			}
		},
		Old { // Monstruo con edad = Old
			public Monster build() {
				return generateMonsterByAge(MonsterAge.Old);
			}
		};
		// ...
		
		public Monster build() { return null; } // default
	}
	
    
	
    
    
//// Private
    
    private static Monster generateMonsterByAge(MonsterAge age) {
    	List<Monster> monsters = generateUserScaffold().getLair().getMonsters();
		for(Monster m : monsters) {
			if(m.getAge().equals(age)) {
				return m;
			}
		};
		return null;
    }
    
	/**
     * Devuelve una nueva guarida con los valores iniciales.
     * Es decir la guarida estará el estado inicial en el que comienzas el juego, sin monstruos y
     * solo con el ojo de la vida.
     */
	private static Lair generateInitialLair(String login) {
		User scaffold = generateUserScaffold(login);
		Lair lair = scaffold.getLair();
		lair.buildRoom(RoomType.EyeOfTheLife);
		lair.buildRoom(RoomType.Dormitories);
		return lair;
	}
	
	private static Lair generateInitialLair() {
		return generateInitialLair(randomLogin());
	}
	
	/**
	 * Guarida de pruebas por defecto, con varias salas y varios monstruos instanciados.
	 * @return
	 */
	private static Lair generateDefaultLair(String login) {
		return generateUserScaffold(login).getLair();
	}
	
	private static Lair generateDefaultLair() {
		return generateDefaultLair(randomLogin());
	}
	
	private static Room generateRoomByType(RoomType roomType) {
		return generateUserScaffold().getLair().getRoom(roomType);
	}
    
    /**
     * Devuelve un monstruo aleatorio de entre las MonsterInstances disponibles.
     */
    private static Monster generateRandMonster() {
    	int randomIndex = (int) (Math.random() * MonsterWhoIs.values().length);
    	MonsterWhoIs randomInstance = MonsterWhoIs.values()[randomIndex];
    	return randomInstance.build();
    }
    
    private static String randomLogin() {
    	return "login_" + ((int) (Math.random() * 10000));
    }
	
	/**
	 * Igual que generateUserScaffold(String userLogin), pero con login aleatorio.
	 */
	private static User generateUserScaffold() {
		return generateUserScaffold(randomLogin());
	}
	
	/**
	 * Construye un usuario con su guarida, salas, monstruos y huevos de monstruo,
	 * con todas las relaciones entre ellos establecidas.
	 * 
	 * @param userLogin es el nombre del usuario. La contraseña será userLogin + "_pass".
	 */
	private static User generateUserScaffold(String userLogin) {
		
	    //*** USER ***//
		
		User user = new User(userLogin, userLogin + "_pass", 
    			new UserDetails("Fulano", "Delapeña", "fulano@delapeña.es", "es"));

		//*** LAIR ***//
    
		Lair lair = new Lair(user,
	        1000, // money 
	        1000, // garbage
	        new RoomData(100, 10), // lastChangeResourcesTurn and occupied vital space
	        new Address(1, 1, 1)); // geographical position
	
		user.setLair(lair); // add to user
    
    
		//*** ROOMS ***//
		
		lair.buildRoom(RoomType.EyeOfTheLife);
		Room dormitories = lair.buildRoom(RoomType.Dormitories);
    	Room warehouse = lair.buildRoom(RoomType.Warehouse);
    	Room tradeOffice = lair.buildRoom(RoomType.TradeOffice);
    
    	// modify rooms
    	dormitories.setLevel(10); 
    	warehouse.setLevel(3); 
    	warehouse.setStateCancelWorks();
    	tradeOffice.setLevel(2);


    
    	//*** MONSTER EGGS ***//
    
    	MonsterEgg monsterEggBu = new MonsterEgg (lair,
    		MonsterRace.Bu, null);
    
    	MonsterEgg monsterEggOcodomo = new MonsterEgg (lair,
    		MonsterRace.Ocodomo, null);
   
    	lair.addMonsterEgg(monsterEggBu).addMonsterEgg(monsterEggOcodomo); // Add to lair
    
    	
    	//*** MONSTERS ***//
		Monster child = new Monster(lair, MonsterRace.Bu,      "Josito de " + lair.getUser().getLogin(), now, now, MonsterAge.Child); 
		Monster adult =	new Monster(lair, MonsterRace.Polbo,   "Héctor de " + lair.getUser().getLogin(), now, now, MonsterAge.Adult);
		Monster old   = new Monster(lair, MonsterRace.Ocodomo, "Matías de " + lair.getUser().getLogin(), now, now, MonsterAge.Old);
    	
		lair.addMonster(child).addMonster(adult).addMonster(old);
    	
		
    	//*** return user ***//
    	return user;
	}
    

}
