package es.engade.thearsmonsters.util.factory;

import java.util.Date;
import java.util.List;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.RoomData;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.state.RoomInWorksState;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.model.util.DateTools;
import es.engade.thearsmonsters.model.util.GameConf;

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
			public Lair build(User user) {
                return generateInitialLair(user);
            }
		},
		Default { 				// Guarida test por defecto, generada en generateUserScaffold
			public Lair build() {
				return generateDefaultLair();
			}
			public Lair build(String login) {
				return generateDefaultLair(login);
			}
			public Lair build(User user) {
                return generateDefaultLair(user);
            }
		},
		FullOfMonsters {
			public Lair build() {
				return generateFullOfMonstersLair();
			}
			public Lair build(String login) {
				return generateFullOfMonstersLair(login);
			}
			public Lair build(User user) {
                return generateFullOfMonstersLair(user);
            }
		};
		// ...
		
		public Lair build() { return null; } // default
		public Lair build(String login) { return null; } // default
		public Lair build(User user) { return null; } // default
	}
	
	
	//------ Build Rooms ------//
	
	/**
	 * Genera una sala con todas sus relaciones establecidas (guarida, usuario, etc),
	 * siguieno el criterio establecido, siguiendo alguno de los criterios de la enumeración.
	 * Ejemplo: FactoryData.RoomWhatIs.MainMonster.build();
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
		
		MainMonster {
			public Room build() { return generateRoomByType(RoomType.MainMonster); }
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
		
		public Room build() { return generateRoomByType(RoomType.MainMonster); } // default
		public Room build(RoomType roomType) { return generateRoomByType(roomType); } // default
	}
	
	
	//------ Build MonsterEggs ------//
	
	public enum EggWhatIs {
		Random {
			public MonsterEgg build() {
				return new MonsterEgg(generateDefaultLair(), MonsterRace.Bu, DateTools.new_byMinutesFromNow((long) (Math.random() * 10) - 5));
			}
		},
		Incubated {
			public MonsterEgg build() {
				return new MonsterEgg(generateDefaultLair(), MonsterRace.Bu, DateTools.yesterday());
			}
		},
		NotIncubated {
			public MonsterEgg build() {
				return new MonsterEgg(generateDefaultLair(), MonsterRace.Bu, null);
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
     * solo con Papá Monstruo.
     */
    private static Lair generateInitialLair(User user) {
        
        // LAIR
        Lair lair = new Lair(user,
            0, // money 
            0, // garbage
            new RoomData(DateTools.yesterday(), 0), // lastChangeResourcesDate and occupied vital space
            0,0,0); // geographical position
    
        user.setLair(lair); // add to user
    
        // ROOMS
        lair.buildRoom(RoomType.MainMonster);
        lair.buildRoom(RoomType.Dormitories);
        
        // Unlock free monster races
        lair.unlockMonsterRace(MonsterRace.Bu);
        lair.unlockMonsterRace(MonsterRace.Ocodomo);
        lair.unlockMonsterRace(MonsterRace.Mongo);
        
        return lair;
    }
    
	/**
     * Devuelve una nueva guarida con los valores iniciales.
     * Es decir la guarida estará el estado inicial en el que comienzas el juego, sin monstruos y
     * solo con Papá Monstruo.
     */
	private static Lair generateInitialLair(String userLogin) {
		return generateInitialLair(new User(userLogin, userLogin + "_pass", 
    			new UserDetails("Fulano", "Delapeña", "fulano@delapeña.es", "es")));
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
	private static Lair generateDefaultLair(User user) {
        return generateUserScaffold(user).getLair();
    }
	private static Lair generateDefaultLair() {
		return generateDefaultLair(randomLogin());
	}
	private static Room generateRoomByType(RoomType roomType) {
		return generateUserScaffold().getLair().getRoom(roomType);
	}
	
	private static Lair generateFullOfMonstersLair(User user) {

        //*** LAIR ***//
    
        Lair lair = new Lair(user,
            10000, // money 
            50000, // garbage
            new RoomData(DateTools.yesterday(), 100), // lastChangeResourcesTurn, occupied vital space
            getRandStreet(), getRandBuilding(), getRandFloor()); // geographical position. Se puede cambiar despues con newLair.setAddress(lairDao.findNextFreeAddress()).
    
        user.setLair(lair); // add to user
    
    
        //*** ROOMS ***//
        
        lair.buildRoom(RoomType.MainMonster);
        Room dormitories = lair.buildRoom(RoomType.Dormitories);
        Room warehouse = lair.buildRoom(RoomType.Warehouse);
        Room tradeOffice = lair.buildRoom(RoomType.TradeOffice);
        Room gym = lair.buildRoom(RoomType.Gym);
        Room nursery = lair.buildRoom(RoomType.Nursery);
    
        // modify rooms
        dormitories.setLevel(20); 
        warehouse.setLevel(20); warehouse.setStateCancelWorks();
        tradeOffice.setLevel(9); tradeOffice.setStateCancelWorks();
        gym.setLevel(8); gym.setStateCancelWorks();
        nursery.setLevel(8);
        RoomInWorksState nurseryWorks = (RoomInWorksState) nursery.getState();
        nurseryWorks.setEffortDone(nursery.getEffortUpgrade() / 2);

    
        //*** MONSTERS ***//
        for(MonsterRace race: MonsterRace.values()) {
        	MonsterEgg egg = new MonsterEgg(lair, race, null);
            Monster child = new Monster(lair, race, race+"_cria");
            Monster adult = new Monster(lair, race, race+"_adulto", now, now, MonsterAge.Adult);
            Monster old = new Monster(lair, race, race+"_anciano", now, DateTools.yesterday(), MonsterAge.Old);
        	lair.addMonsterEgg(egg).addMonster(child).addMonster(adult).addMonster(old);
        }
        lair.addMonsterEgg(new MonsterEgg(lair, MonsterRace.Polbo, DateTools.new_byMinutesFromNow(1))); // Huevo adicional, a un minuto de eclosionar  
        
        //*** return lair ***//
        return lair;
	}
	private static Lair generateFullOfMonstersLair(String userLogin) {
		return generateFullOfMonstersLair(generateUserByLoginName(userLogin));
	}
	private static Lair generateFullOfMonstersLair() {
		return generateFullOfMonstersLair(randomLogin());
	}
    
    /**
     * Devuelve un monstruo aleatorio de entre las MonsterInstances disponibles.
     */
    private static Monster generateRandMonster() {
    	int randomIndex = (int) (Math.random() * MonsterWhoIs.values().length);
    	MonsterWhoIs randomInstance = MonsterWhoIs.values()[randomIndex];
    	return randomInstance.build();
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
	private static User generateUserScaffold(User user) {

        //*** LAIR ***//
    
        Lair lair = new Lair(user,
            1000, // money 
            500, // garbage
            new RoomData(DateTools.yesterday(), 10), // lastChangeResourcesTurn and occupied vital space
            getRandStreet(), getRandBuilding(), getRandFloor()); // geographical position. Se puede cambiar despues con newLair.setAddress(lairDao.findNextFreeAddress()).
    
        user.setLair(lair); // add to user
    
    
        //*** ROOMS ***//
        
        lair.buildRoom(RoomType.MainMonster);
        Room dormitories = lair.buildRoom(RoomType.Dormitories);
        Room warehouse = lair.buildRoom(RoomType.Warehouse);
        Room tradeOffice = lair.buildRoom(RoomType.TradeOffice);
    
        // modify rooms
        dormitories.setLevel(10); 
        warehouse.setLevel(15); 
        warehouse.setStateCancelWorks();
        tradeOffice.setLevel(5);

    
        //*** MONSTER EGGS ***//
    
        MonsterEgg monsterEggBu = new MonsterEgg (lair,
            MonsterRace.Bu, null);
    
        MonsterEgg monsterEggOcodomo = new MonsterEgg (lair,
            MonsterRace.Ocodomo, null);
   
        lair.addMonsterEgg(monsterEggBu).addMonsterEgg(monsterEggOcodomo); // Add to lair
    
        
        //*** MONSTERS ***//
        Monster child = new Monster(lair, MonsterRace.Bu,      "Josito de ");
        Monster adult = new Monster(lair, MonsterRace.Polbo, "Héctor de ", now, now, MonsterAge.Adult);
        Monster old = new Monster(lair, MonsterRace.Ocodomo, "Matías de ", now, DateTools.yesterday(), MonsterAge.Old);
        lair.addMonster(child).addMonster(adult).addMonster(old);
        
        
        //*** return user ***//
        return user;
    }

	private static User generateUserScaffold(String userLogin) {
		return generateUserScaffold(generateUserByLoginName(userLogin));
	}
    
    private static String randomLogin() {
    	return "login_" + ((int) (Math.random() * 10000));
    }
	
	// Crea un usuario con el mismo password que login.
	private static User generateUserByLoginName(String userLogin) {
		return new User(userLogin, PasswordEncrypter.crypt(userLogin), 
    			new UserDetails("Fulano", "Bengano", "fulano@mymail.es", "es"));
	}
	
	private static int getRandStreet() {
		return ((int) (GameConf.getMaxNumberOfStreets() * Math.random()));
	}
	private static int getRandBuilding() {
		return ((int) (GameConf.getMaxNumberOfStreets() * Math.random()));
	}
	private static int getRandFloor() {
		return ((int) (GameConf.getMaxNumberOfStreets() * Math.random()));
	}
    

}
