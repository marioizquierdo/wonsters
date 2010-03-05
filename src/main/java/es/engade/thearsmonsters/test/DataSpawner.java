package es.engade.thearsmonsters.test;

import java.util.Calendar;
import java.util.Set;

import es.engade.thearsmonsters.model.entities.common.Id;
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
 * Estos datos se utilizan como datos de prueba.
 */
public class DataSpawner {
	
	public static final Calendar now = CalendarTools.now();
	public static final Calendar tomorrow = CalendarTools.tomorrow();
	
	/**
	 * Igual que generateUserScaffold(String userLogin), pero con login aleatorio.
	 */
	public static User generateUserScaffold() {
		String randomLogin = "login_" + ((int) (Math.random() * 10000));
		return generateUserScaffold(randomLogin);
	}
	
	/**
	 * Construye un usuario con su guarida, salas, monstruos y huevos de monstruo,
	 * con todas las relaciones entre ellos establecidas.
	 * 
	 * @param userLogin es el nombre del usuario. La contraseña será userLogin + "_pass".
	 */
	public static User generateUserScaffold(String userLogin) {
		
	    //*** USER ***//
		
		User user = new User(userLogin, userLogin + "_pass", 
    			new UserDetails("Fulano", "Delapeña", "fulano@delapeña.es", "es"));

		//*** LAIR ***//
    
		Lair lair = new Lair(null, user,
	        1000, // money 
	        1000, // garbage
	        10,   // occupied vital space
	        new RoomData(100), 
	        new Address(1, 1, 1));
	
		user.setLair(lair); // add to user
    
    
		//*** ROOMS ***//
	
		Room eyeOfTheLife = RoomType.newRoom(RoomType.EyeOfTheLife.code(), lair);
		Room dormitories = RoomType.newRoom(RoomType.Dormitories.code(), lair);
    	Room warehouse = RoomType.newRoom(RoomType.Warehouse.code(), lair);
    	Room truffleFarm = RoomType.newRoom(RoomType.TruffleFarm.code(), lair);
    	Room tradeOffice = RoomType.newRoom(RoomType.TradeOffice.code(), lair);
    
    	// modify and add to lair
    	dormitories.setSize(15); 
    	dormitories.setLevel(10); 
    	dormitories.setStateCancelWorks();
    	
    	lair.addRoom(eyeOfTheLife).addRoom(dormitories).
    		addRoom(warehouse).addRoom(truffleFarm).addRoom(tradeOffice);


    
    	//*** MONSTER EGGS ***//
    
    	MonsterEgg monsterEggBu = new MonsterEgg (lair,
    		MonsterRace.Bu, null, null);
    
    	MonsterEgg monsterEggOcodomo = new MonsterEgg (lair,
    		MonsterRace.Ocodomo, null, null);
   
    	lair.addMonsterEgg(monsterEggBu).addMonsterEgg(monsterEggOcodomo); // Add to lair
    
    	
    	//*** MONSTERS ***//
		Monster child = new Monster(Id.autoGenerate(), lair, MonsterRace.Bu,      "Josito de " + lair.getUser().getLoginName(), now, now, MonsterAge.Child); 
		Monster adult =	new Monster(Id.autoGenerate(), lair, MonsterRace.Polbo,   "Héctor de " + lair.getUser().getLoginName(), now, now, MonsterAge.Adult);
		Monster old   = new Monster(Id.autoGenerate(), lair, MonsterRace.Ocodomo, "Matías de " + lair.getUser().getLoginName(), now, now, MonsterAge.Old);
    	
		lair.addMonster(child).addMonster(adult).addMonster(old);
    	
		
    	//*** return user ***//
    	return user;
	}
    
	
	
	/*** Generate User ***/
	public static User generateUser() {
		return generateUserScaffold();
	}
	
	public static User generateUser(String loginName) {
		return generateUserScaffold(loginName);
	}
	
	/*** Generate Lair ***/
	public static Lair generateLair() {
		return generateUserScaffold().getLair();
	}
	
	/*** Generate Room ***/
	public static Room generateRoom() {
		return generateUserScaffold().getLair().getRoom(RoomType.EyeOfTheLife);
	}
	// TODO: generar salas por tipo de sala.
	
	
	/*** Generate MonsterEggs ***/
	// TODO: generar huevos de monstruo
	
	
	/*** Generate Monsters ***/
    
    
    /**
     * Devuelve un monstruo aleatorio de entre las MonsterInstances disponibles.
     */
    public static Monster generateMonster() {
    	int randomIndex = (int) (Math.random() * MonsterWhatIs.values().length);
    	MonsterWhatIs randomInstance = MonsterWhatIs.values()[randomIndex];
    	return generateMonster(randomInstance);
    }
    
    /**
     * Devuelve nuevo monstruo generado con todas sus relaciones establecidas.
     * Es decir, que cada vez que se genera un monstruo, también se genera su guarida, usuario, salas, etc,
     * y todo es nuevo (dos llamadas a generateMonster(MonsterWhatIs.Child) devuelven dos monstruos diferentes,
     * con guaridas diferentes, pero asegurándose de que al menos tienen la edad Child.
     */
    public static Monster generateMonster(MonsterWhatIs criteria) {
    	switch(criteria) {
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
	 * Son los diferentes monstruos que se pueden obtener con el DataSpawner,
	 * se trata de una enumeración con elementos que describen alguna característica importante del
	 * tipo de monstruo que se va a devolver (para identificarlo).
	 */
	public enum MonsterWhatIs { 
		Child, // Monstruo con edad = Child
		Adult, // Monstruo con edad = Adult
		Old	   // Monstruo con edad = Old
		// Añadir más tipos de monstruo que es necesario autogenerar.
	}
    
    
    
//// Private
    
    private static Monster generateMonsterByAge(MonsterAge age) {
    	Set<Monster> monsters = generateUserScaffold().getLair().getMonsters();
		for(Monster m : monsters) {
			if(m.getAge().equals(age)) {
				return m;
			}
		};
		return null;
    }
    
    

}
