package es.engade.thearsmonsters.test.util;

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
import es.engade.thearsmonsters.model.util.CalendarTools;

/**
 * Esta clase tiene métodos estáticos para generar datos (salas, monstruos, etc).
 * Estos datos se utilizan como datos de prueba, o donde sea necesario generar nuevos datos.
 */
public class FactoryData {
	
	public static final Date now = CalendarTools.now();
	public static final Date tomorrow = CalendarTools.tomorrow();
    
	
	
	/*** Generate User ***/
	public static User generateRandUser() {
		return generateRandUserScaffold();
	}
	
	public static User generateUser(String login) {
		return generateUserScaffold(login);
	}
	
	/*** Generate Lair ***/
	
	/**
     * Devuelve una nueva guarida.
     * Existen dos posibilidades, se devuelve una guarida con todas las relaciones establecidas
     * (TestDefault) o por el contrario un objeto guarida con sus valores iniciales (TestDefault) . 
     */
    public static Lair generate(LairWhatIs criteria){
    	switch(criteria){
    	case InInitialState:
    		return FactoryData.generateInitialLair();
    	case Default:
    		return FactoryData.generateDefaultLair();
    	default: return null;
    	}
    }
    

    
	/**
	 * Instancias de Guarida.
	 * Son las diferentes guaridas que se pueden obtener con el DataSpawner,
	 * se trata de una enumeración con elementos que describen alguna característica importante del
	 * tipo de guarida que se va a devolver (para identificarlo).
	 */
	public enum LairWhatIs { 
		InInitialState, // Guarida en estado inicial (sin monstruos y solo con el ojo de la vida) 
		Default , //   Devuelve la guarida test por defecto, generada en generateUserScaffold
		// Añadir más tipos de guaridas que es necesario autogenerar.
	}
	
	
	/*** Generate Room ***/
	
	/**
	 * Genera una sala con todas sus relaciones establecidas (guarida, usuario, etc),
	 * siguieno el criterio establecido.
	 */
	public static Room generate(RoomWhatIs criteria) {
		switch(criteria) {
		case eyeOfTheLife:
			return generateRandUserScaffold().getLair().getRoom(RoomType.EyeOfTheLife);
		case warehouse:
			return generateRandUserScaffold().getLair().getRoom(RoomType.Warehouse);
		default:
			return null;
		}
	}
	
	public enum RoomWhatIs {
		eyeOfTheLife,
		warehouse
	}
	
	
	/*** Generate MonsterEggs ***/
	// TODO: generar huevos de monstruo
	
	
	/*** Generate Monsters ***/
    
    /**
     * Devuelve nuevo monstruo generado con todas sus relaciones establecidas.
     * Es decir, que cada vez que se genera un monstruo, también se genera su guarida, usuario, salas, etc,
     * y todo es nuevo (dos llamadas a generateMonster(MonsterWhatIs.Child) devuelven dos monstruos diferentes,
     * con guaridas diferentes, pero asegurándose de que al menos tienen la edad Child.
     */
    public static Monster generate(MonsterWhatIs criteria) {
    	switch(criteria) {
    	case Rand: 
    		return generateRandMonster();
    	case Child:
    		return generateMonsterByAge(MonsterAge.Child);
    	case Adult:
			return generateMonsterByAge(MonsterAge.Adult);
    	case Old:
			return generateMonsterByAge(MonsterAge.Old);
    	default: return null;
    	}
    }
    
	/**
	 * Instancias de Monstruo.
	 * Son los diferentes monstruos que se pueden generar,
	 * se trata de una enumeración con elementos que describen alguna característica importante del
	 * tipo de monstruo que se va a devolver (para identificarlo).
	 */
	public enum MonsterWhatIs { 
		Rand,  // Cualquier monstruo de los siguientes
		Child, // Monstruo con edad = Child
		Adult, // Monstruo con edad = Adult
		Old	   // Monstruo con edad = Old
		// Añadir más tipos de monstruo que es necesario autogenerar.
	}
	
    
	


    
    
    
//// Private
    
    private static Monster generateMonsterByAge(MonsterAge age) {
    	List<Monster> monsters = generateRandUserScaffold().getLair().getMonsters();
		for(Monster m : monsters) {
			if(m.getAge().equals(age)) {
				return m;
			}
		};
		return null;
    }
    
	/**
     * Devuelve una nueva guarida con los valores iniciales.
     * Es decir la guarida estar� el estado inicial en el que comienzas el juego, sin monstruos y
     * solo con el ojo de la vida
     */
	private static Lair generateInitialLair(){
		Lair lair = new Lair(FactoryData.generateRandUserScaffold(),
		        1000, // money 
		        1000, // garbage
		        10,   // occupied vital space
		        new RoomData(100), 
		        new Address(1, 1, 1));
		Room eyeOfTheLife = RoomType.EyeOfTheLife.build(lair);
		lair.addRoom(eyeOfTheLife);
		return lair;
	}
	
	/**
	 * Guarida de pruebas por defecto, con varias salas y varios monstruos instanciados.
	 * @return
	 */
	private static Lair generateDefaultLair() {
		return generateRandUserScaffold().getLair();
	}
    
    /**
     * Devuelve un monstruo aleatorio de entre las MonsterInstances disponibles.
     */
    private static Monster generateRandMonster() {
    	int randomIndex = (int) (Math.random() * MonsterWhatIs.values().length);
    	MonsterWhatIs randomInstance = MonsterWhatIs.values()[randomIndex];
    	return generate(randomInstance);
    }
    
	
	/**
	 * Igual que generateUserScaffold(String userLogin), pero con login aleatorio.
	 */
	private static User generateRandUserScaffold() {
		String randomLogin = "login_" + ((int) (Math.random() * 10000));
		return generateUserScaffold(randomLogin);
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
	        10,   // occupied vital space
	        new RoomData(100), 
	        new Address(1, 1, 1));
	
		user.setLair(lair); // add to user
    
    
		//*** ROOMS ***//
	
		Room eyeOfTheLife = RoomType.EyeOfTheLife.build(lair);
		Room dormitories = RoomType.Dormitories.build(lair);
    	Room warehouse = RoomType.Warehouse.build(lair);
    	Room truffleFarm = RoomType.TruffleFarm.build(lair);
    	Room tradeOffice = RoomType.TradeOffice.build(lair);
    
    	// modify and add to lair
    	dormitories.setLevel(10); 
    	dormitories.setStateCancelWorks();
    	
    	lair.addRoom(eyeOfTheLife).addRoom(dormitories).
    		addRoom(warehouse).addRoom(truffleFarm).addRoom(tradeOffice);


    
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
