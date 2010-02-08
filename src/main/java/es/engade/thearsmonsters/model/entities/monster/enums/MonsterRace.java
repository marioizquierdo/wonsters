package es.engade.thearsmonsters.model.entities.monster.enums;

import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

/**
 * This enumeration makes easier to return a concrete class of a Monster
 * or MonsterEgg depending on the database field: 'Monster.race()'
 * When a new race of monster is implemented, the enumeration must be enlarged
 */
public enum MonsterRace {
	// NombreRaza		((byte)code, 
	//						fuerza, agilidad, vitalidad, inteligencia, carisma,
	//						espacioVital, esperanzaVida, tipoRaza, esVolador, 
	//						fertilidad%, tIncubacion', tMetamorfosis', precioCompraHuevo)
	
	Bu 				((byte)1, // Abrelatax Pignos Boo
						5, 1, 1, 1, 0,
						4, 5, MonsterRaceClass.Verme, false,
						0, 1, 1, 0),
	Polbo			((byte)2, // Balleno Heptocto
						5, 5, 5, 5, 5, 
						10, 10, MonsterRaceClass.Mollusca, false,
						5, 4, 5, 100),
	Lipendula		((byte)3, // Unibabosa voladora
						2, 30, 2, 2, 2, 
						16, 10, MonsterRaceClass.Verme, true,
						10, 10, 5, 200),
	Ocodomo			((byte)4, // Carnificis Abisal
						20, 15, 30, 20, 5, 
						40, 30, MonsterRaceClass.Crustacea, false,
						20, 60, 120, 1000),
	Mongo			((byte)5, // Celeris Caudicis
						5, 60, 15, 30, 25, 
						33, 30, MonsterRaceClass.Humanoide, true,
						25, 120, 100, 2500),
	Electroserpe	((byte)6, // Triserpe Electroplasmoide
						30, 15, 50, 30, 30, 
						62, 45, MonsterRaceClass.Crustacea, false,
						25, 120, 360, 2500),
	Quad			((byte)7, // Grutrul Quadlingue
						65, 40, 30, 20, 10, 
						75, 50, MonsterRaceClass.Mollusca, false,
						20, 601, 780, 3000),
	Ubunto			((byte)8, // Spureo Sapiens
						20, 30, 40, 50, 30, 
						85, 65, MonsterRaceClass.Humanoide, false,
						10, 2, 960, 5000);
	
	// ...
	
	/**
	 * If you know the code then you can get the corresponding enum instance
	 */
	static public MonsterRace getFromCode(byte code) 
		throws InstanceNotFoundException {
		
		for(MonsterRace e : MonsterRace.values()) {
			if(code == e.code()) return e;
		}
			
		throw new InstanceNotFoundException(
        	"MonsterRace not found (code="+ code +")", 
        	MonsterRace.class.getName());
	}
	
	
	/* El código que sigue aunque sea repetitivo y tocapelotas es necesario ... */

	private final int strenght; 	// Fuerza inicial
	private final int agility; 		// Agilidad inicial
	private final int vitality;		// Vitalidad inicial
	private final int intelligence; // Inteligencia inicial
	private final int charisma;		// Carisma inicial
	
	private final byte code; 		// Codigo de la raza de mosntruo en la BBDD
	
	private final int vitalSpace;	// Espacio vital
	private final int liveLenght;	// Esperanza de vida (de cria a anciano) en monstruodías
	private final MonsterRaceClass raceType; // Tipo de raza (Mollusca, Verme ...)
	private final boolean isFliying;	// Monstruo volador
	
	private final int  fertility;		// Fertilidad [0..100], probabilidad de enjendrar
	private final int incubationMinutes;	// Minutos para incubar (de huevo a cria)
	private final int metamorphosisMinutes; // Minutos para metamorfosis (de cria a adulto)
	private final int buyEggPrice;	// Precio de compra de un huevo (venderlo es la mitad) 
	
	MonsterRace(byte code, int strenght, int agility, int vitality, int intelligence, int charisma,
					int vitalSpace, int liveLenght, MonsterRaceClass raceType, boolean isFliying,
					int fertility, int incubationMinutes, int metamorphosisMinutes, int buyEggPrice) {
		this.code = code;
		this.strenght = strenght;
		this.agility = agility;
		this.vitality = vitality;
		this.intelligence = intelligence;
		this.charisma = charisma;
		this.vitalSpace = vitalSpace;
		this.liveLenght = liveLenght;
		this.raceType = raceType;
		this.isFliying = isFliying;
		this.fertility = fertility;
		this.incubationMinutes = incubationMinutes;
		this.metamorphosisMinutes = metamorphosisMinutes;
		this.buyEggPrice = buyEggPrice;
	}
	public byte code() { return code; }
	public byte getCode() { return code(); }
	public int getStrenght() { return strenght; }
	public int getAgility() { return agility; }
	public int getVitality() { return vitality; }
	public int getIntelligence() { return intelligence; }
	public int getCharisma() { return charisma; }
	public int getVitalSpace() { return vitalSpace; }
	
	/**
	 * Esperanza de vida. Número de turnos que puede vivir un monstruo de esta raza.
	 */
	public int getLiveLenght() { return liveLenght; }
	public MonsterRaceClass getRaceType() { return raceType; }
	public boolean isFliying() { return isFliying; }
	public int  getFertility() { return fertility; }
	public int getIncubationMinutes() { return incubationMinutes; }
	public int getMetamorphosisMinutes() { return metamorphosisMinutes; }	
	
	/**
	 * Precio de compra de un huevo de esta raza
	 */
	public int getBuyEggPrice() { return buyEggPrice; }
	
	/**
	 * Precio de venta de un huevo de esta raza
	 */
	public int getShellEggPrice() { return buyEggPrice / 2;}
	
}
