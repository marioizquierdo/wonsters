package es.engade.thearsmonsters.model.entities.monster.enums;

import java.util.Date;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.util.DateTools;

/**
 * This enumeration makes easier to return a concrete class of a Monster
 * or MonsterEgg depending on the database field: 'Monster.race()'
 * When a new race of monster is implemented, the enumeration must be enlarged
 */
public enum MonsterRace {
	// NombreRaza	(
	//					fuerza, agilidad, vitalidad, inteligencia, carisma, 			// valor atributos al nacer
	//					espacioVital, precioCompraHuevo,
	// 					fertilidad (%), tipoRaza, esVolador
	//					esperanzaVida (días), incubationMinutes, metamorphosisMinutes)
	
	Bu 				(// Abrelatax Pignos Boo
						5, 1, 1, 1, 0,
						4, 0,
						0, MonsterRaceClass.Verme, false,
						4, 1, 1),
						
	Polbo			(// Balleno Heptocto
						5, 5, 5, 5, 5, 
						10, 100, 
						5, MonsterRaceClass.Mollusca, false,
						10, 4, 5),
						
	Lipendula		(// Unibabosa voladora
						2, 30, 2, 2, 2, 
						16, 200, 
						10, MonsterRaceClass.Verme, true,
						10, 10, 5),
						
	Ocodomo			(// Carnificis Abisal
						20, 15, 30, 20, 5, 
						40, 1000, 
						20, MonsterRaceClass.Crustacea, false,
						30, 60, 120),
						
	Mongo			(// Celeris Caudicis
						5, 60, 15, 30, 25, 
						33, 2500,
						25, MonsterRaceClass.Humanoide, true,
						30, 120, 100),
						
	Electroserpe	(// Triserpe Electroplasmoide
						30, 15, 50, 30, 30, 
						62, 2500,
						25, MonsterRaceClass.Crustacea, false,
						45, 120, 360),
						
	Quad			(// Grutrul Quadlingue
						65, 40, 30, 20, 10, 
						75, 3000,
						20, MonsterRaceClass.Mollusca, false,
						50, 601, 780),
						
	Ubunto			(// Spureo Sapiens
						20, 30, 40, 50, 30, 
						85, 5000,
						10, MonsterRaceClass.Humanoide, false,
						65, 2, 960);
	
	// ...
	


	private final int strenghtLevel; 		// Fuerza inicial
	private final int agilityLevel; 		// Agilidad inicial
	private final int vitalityLevel;		// Vitalidad inicial
	private final int intelligenceLevel; 	// Inteligencia inicial
	private final int charismaLevel;		// Carisma inicial
	
	private final int vitalSpace;			// Espacio vital
	private final int buyEggPrice;			// Precio de compra de un huevo (venderlo es la mitad)

	private final int fertility;			// Fertilidad [0..100], probabilidad de hit al reproducirse
	private final MonsterRaceClass raceType;// Tipo de raza (Mollusca, Verme ...)
	private final boolean isFliying;		// Monstruo volador

	private final int lifeExpectancyDays;	// Esperanza de vida (de cria a anciano) en monstruodías
	private final int incubationMinutes;	// Minutos para incubar (de huevo a cria)
	private final int metamorphosisMinutes; // Minutos para metamorfosis (de cria a adulto)
	
	MonsterRace(int strenghtLevel, int agilityLevel, int vitalityLevel, int intelligenceLevel, int charismaLevel,
					int vitalSpace, int buyEggPrice,
					int fertility, MonsterRaceClass raceType, boolean isFliying,
					int lifeExpectancyDays, int incubationMinutes, int metamorphosisMinutes) {
		this.strenghtLevel = strenghtLevel;
		this.agilityLevel = agilityLevel;
		this.vitalityLevel = vitalityLevel;
		this.intelligenceLevel = intelligenceLevel;
		this.charismaLevel = charismaLevel;

		this.vitalSpace = vitalSpace;
		this.buyEggPrice = buyEggPrice;
		
		this.fertility = fertility;
		this.raceType = raceType;
		this.isFliying = isFliying;

		this.lifeExpectancyDays = lifeExpectancyDays;
		this.incubationMinutes = incubationMinutes;
		this.metamorphosisMinutes = metamorphosisMinutes;
	}
	public int getStrenghtLevel() { return strenghtLevel; }
	public int getAgilityLevel() { return agilityLevel; }
	public int getVitalityLevel() { return vitalityLevel; }
	public int getIntelligenceLevel() { return intelligenceLevel; }
	public int getCharismaLevel() { return charismaLevel; }
	
	public int getVitalSpace() { return vitalSpace; }
	/**
	 * Precio de compra de un huevo de esta raza
	 */
	public int getBuyEggPrice() { return buyEggPrice; }
	
	/**
	 * Precio de venta de un huevo de esta raza
	 */
	public int getShellEggPrice() { return buyEggPrice / 2;}
	
	public int  getFertility() { return fertility; }
	public MonsterRaceClass getRaceType() { return raceType; }
	public boolean isFliying() { return isFliying; }
	
	/**
	 * Esperanza de vida. Dias que puede vivir un monstruo de esta raza.
	 */
	public int getLifeExpectancyDays() { return lifeExpectancyDays; }
	public int getIncubationMinutes() { return incubationMinutes; }
	public int getMetamorphosisMinutes() { return metamorphosisMinutes; }	
	
	/**
	 * Crea un nuevo monstruo de esta raza recién nacido.
	 * Nota: no lo añade a la guarida, después de crearlo hay que añadirlo 
	 * usando lair.addMonster(monster);
	 */
	public Monster newMonster(Lair lair, String name) {
		return new Monster(lair, this, name);
	}
	
}
