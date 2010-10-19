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
	//					espacioVital, precioCompraHuevo,
	// 					fertilidad (%), tipoRaza, esVolador
	//					esperanzaVida (dias), incubationMinutes, metamorphosisMinutes)
	
	Bu 				(// Abrelatax Pignos Boo
						4, 1, 1, 1, 1,
						4, 0,
						0, MonsterRaceClass.Verme, false,
						4, 0, 0), // 4 (esp.vida) * 0.80 (%esp.vida viejuno) * 15 (turnos/dia) + 15 (turnos iniciales) = 63 turnos acumulados en su vida util (cria+adulto)
						
	Mongo			(// Celeris Caudicis
						3, 1, 1, 10, 1, 
						6, 0,
						0, MonsterRaceClass.Humanoide, true,
						4, 2, 1), // 4 * 0.80 * 15 + 15 = 63 turnos acumulados en su vida util (cria+adulto)
						
	Ocodomo			(// Carnificis Abisal
						11, 1, 1, 1, 1, 
						10, 0, 
						0, MonsterRaceClass.Crustacea, false,
						5, 1, 1), // 5 * 0.80 * 15 + 15 = 75 turnos acumulados en su vida util (cria+adulto)
						
	Polbo			(// Balleno Heptocto
						5, 1, 1, 10, 1, 
						5, 50, 
						0, MonsterRaceClass.Mollusca, false,
						10, 2, 2), // 10 * 0.80 * 15 + 15 = 135 turnos acumulados en su vida util (cria+adulto)
						
	Lipendula		(// Unibabosa voladora
						5, 1, 1, 25, 1, 
						6, 200, 
						0, MonsterRaceClass.Verme, true,
						12, 5, 5), // 12 * 0.80 * 15 + 15 = 159 turnos acumulados en su vida util (cria+adulto)
						
	Electroserpe	(// Triserpe Electroplasmoide
						20, 1, 1, 15, 1, 
						18, 500,
						0, MonsterRaceClass.Crustacea, false,
						15, 20, 5), // 15 * 0.80 * 15 + 15 = 195 turnos acumulados en su vida util (cria+adulto)
						
	Quad			(// Grutrul Quadlingue
						50, 1, 1, 10, 1, 
						30, 1600,
						0, MonsterRaceClass.Mollusca, false,
						10, 180, 5), // 10 * 0.80 * 15 + 15 = 135 turnos acumulados en su vida util (cria+adulto)
						
	Ubunto			(// Spureo Sapiens
						1, 1, 1, 99, 1, 
						45, 3000,
						0, MonsterRaceClass.Humanoide, false,
						45, 960, 150); // 30 * 0.80 * 15 + 15 = 555 turnos acumulados en su vida util (cria+adulto)
	
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
