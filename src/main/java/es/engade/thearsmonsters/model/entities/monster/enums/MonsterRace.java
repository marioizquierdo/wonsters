package es.engade.thearsmonsters.model.entities.monster.enums;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;

/**
 * This enumeration makes easier to return a concrete class of a Monster
 * or MonsterEgg depending on the database field: 'Monster.race()'
 * When a new race of monster is implemented, the enumeration must be enlarged
 */
public enum MonsterRace {
	// NombreRaza	(
	//					fuerza, agilidad, vitalidad, inteligencia, carisma, 			// valor atributos al nacer
	//					espacioApartamentos, precioCompraHuevo,
	// 					fertilidad (%), tipoRaza, esVolador
	//					esperanzaVida (dias), incubationMinutes, metamorphosisMinutes)
	
	Bu 				(// Abrelatax Pignos Boo
						4, 1, 1, 4, 1,
						4, 0,
						0, MonsterRaceClass.Verme, false,
						4, 0, 1),
						
	Mongo			(// Celeris Caudicis
						4, 1, 1, 15, 1, 
						6, 0,
						0, MonsterRaceClass.Humanoide, true,
						2, 1, 1),
						
	Ocodomo			(// Carnificis Abisal
						11, 1, 1, 1, 1, 
						9, 0, 
						0, MonsterRaceClass.Crustacea, false,
						3, 2, 1),
						
	Polbo			(// Balleno Heptocto
						7, 1, 1, 6, 1, 
						5, 50, 
						0, MonsterRaceClass.Mollusca, false,
						3, 2, 2),
						
	Lipendula		(// Unibabosa voladora
						3, 1, 1, 22, 1, 
						8, 150, 
						0, MonsterRaceClass.Verme, true,
						7, 5, 5),
						
	Electroserpe	(// Triserpe Electroplasmoide
						24, 1, 1, 10, 1, 
						15, 800,
						0, MonsterRaceClass.Crustacea, false,
						12, 20, 5),
						
	Quad			(// Grutrul Quadlingue
						46, 1, 1, 5, 1, 
						23, 1500,
						0, MonsterRaceClass.Mollusca, false,
						14, 180, 5),
						
	Ubunto			(// Spureo Sapiens
						1, 1, 1, 30, 1, 
						33, 3333,
						0, MonsterRaceClass.Humanoide, false,
						35, 960, 150);
	
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
	public int getSellEggPrice() { return buyEggPrice / 2;}
	
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
