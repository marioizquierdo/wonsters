package es.engade.thearsmonsters.test;

import java.util.Calendar;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.RoomData;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.RoomPublicAccessOpen;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;
import es.engade.thearsmonsters.model.entities.room.types.Dormitories;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.util.CalendarTools;

/**
 * Esta clase tiene métodos estáticos para generar datos (salas, monstruos, etc).
 * Estos datos se utilizan como datos de prueba.
 */
public class DataSpawner {
	
	public static final Calendar now = CalendarTools.now();

	// TODO: Hay que implementar los demás generateXXX igual que generateMonster
	public enum MonsterInstance {
		Child		(new Monster(Id.fromString("child"), null, MonsterRace.Bu, "Josito", now, now, MonsterAge.Child)), 
		Adult		(new Monster(Id.fromString("adult"), null, MonsterRace.Bu, "Iago", now, now, MonsterAge.Adult)), 
		Old			(new Monster(Id.fromString("old"), null, MonsterRace.Bu, "Matias", now, now, MonsterAge.Old));
		private final Monster monster;
		MonsterInstance(Monster monster) {this.monster = monster;}
		public Monster getInstance() {return this.monster;}
		
	}

    //DEFAULT LAIR VALUES
	public static final int DEFAULT_LAIR_MONEY = 1000;
	public static final int DEFAULT_LAIR_GARBAGE = 1000;
    public static final int DEFAULT_LAIR_OCCUPIED_VITAL_SPACE = 10;
    public static final byte DEFAULT_LAIR_INITIAL_SLEEPING_TURN = 0;
    public static final byte DEFAULT_LAIR_INITIAL_MEAL_TURN = 12;
    public static final long DEFAULT_LAIR_LAST_CHANGE_RESOURCES_TURN = 10000;
    public static final int DEFAULT_LAIR_STREET = 1;
    public static final int DEFAULT_LAIR_BUILDING = 1;
    public static final int DEFAULT_LAIR_FLOOR = 1;
	
    //DEFAULT USER VALUES
    public static final String DEFAULT_USER_LOGIN = "testLogin";
    public static final String DEFAULT_USER_PASSWORD = "clearPassword";
    public static final String DEFAULT_USER_NAME = "Testfulano";
    public static final String DEFAULT_USER_SURNAME = "Delapeña";
    public static final String DEFAULT_USER_EMAIL = "fulano@delapeña.es";
    public static final String DEFAULT_USER_LANGUAGE = "es";
    
    //DEFAULT MONSTER VALUES
    public static final MonsterAge DEFAULT_MONSTER_AGESTATE = MonsterAge.Adult;
    public static final MonsterRace DEFAULT_MONSTER_RACE = MonsterRace.Mongo;
    
    //DEFAULT ROOM VALUES
    public static final int DEFAULT_ROOM_LEVEL = 1;
    public static final int DEFAULT_ROOM_SIZE = 5;
    public static final int DEFAULT_ROOM_PRICE = 10;
    public static final int DEFAULT_ROOM_GUILD_PRICE = 3;
    public static final String DEFAULT_ROOM_MARKETING_TEXT = "vente p'acá";
    
    //LAIR GENERATOR
	public static Lair generateLair(){
		return new Lair(null, generateUser()
		        , DEFAULT_LAIR_MONEY, DEFAULT_LAIR_GARBAGE
		        , DEFAULT_LAIR_OCCUPIED_VITAL_SPACE, generateRoomData(), generateAddress());
	}

	public static RoomData generateRoomData(){
	    return new RoomData(DEFAULT_LAIR_LAST_CHANGE_RESOURCES_TURN);
	}
	
	public static Address generateAddress(){
	    return new Address(DEFAULT_LAIR_STREET,DEFAULT_LAIR_BUILDING,DEFAULT_LAIR_FLOOR);
	}
    
	//USER GENERATOR
    public static User generateUser(){
        return new User(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD, generateUserDetails());
    }
    
    public static UserDetails generateUserDetails(){
        return new UserDetails(DEFAULT_USER_NAME, DEFAULT_USER_SURNAME
                , DEFAULT_USER_EMAIL, DEFAULT_USER_LANGUAGE);
    }
    
    //MONSTER EGG GENERATOR
    public MonsterEgg generateMonsterEgg(){
        return new MonsterEgg (null, generateLair(), 
                DEFAULT_MONSTER_RACE, generateBorningDate(), generateMonster());
    }

    public Calendar generateBorningDate() {
        //TODO que fecha devuelvo?
        return null;
    }

    //MONSTER GENERATOR
    
    /**
     * Devuelve un monstruo aleatorio de entre las MonsterInstances disponibles.
     */
    public static Monster generateMonster() {
    	int randomIndex = (int) (Math.random() * MonsterInstance.values().length);
    	Monster randomMonster = MonsterInstance.values()[randomIndex].getInstance();
    	return randomMonster;
    }
    
    /**
     * Devuelve la instancia del MonsterInstance seleccionado.
     */
    public static Monster generateMonster(MonsterInstance which) {
    	return which.getInstance();
    }
    
    public Calendar generateCocoonCloseUpDate() {
        // TODO otra fecha!!!
        return null;
    }
    
    //ROOM GENERATOR
    public Room generateRoom(){
        return new Dormitories(null, generateLair(), DEFAULT_ROOM_LEVEL, DEFAULT_ROOM_SIZE,
                generateRoomPublicAccess(), generateRoomState());
    }

    private RoomPublicAccess generateRoomPublicAccess() {
        // TODO Auto-generated method stub
        return new RoomPublicAccessOpen(DEFAULT_ROOM_PRICE, 
                DEFAULT_ROOM_GUILD_PRICE, DEFAULT_ROOM_MARKETING_TEXT);
    }

    private RoomState generateRoomState() {
        return new RoomNormalState();
    }
}
