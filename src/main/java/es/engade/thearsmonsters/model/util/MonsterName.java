package es.engade.thearsmonsters.model.util;

public class MonsterName {
	
	private static final int COMPOSE_PROBABILITY = 10;
	
	public static final String[] PREFIX_NAMES = {
		"Light", "Heavy", "Fast", "Slow", "Spinning",
		"Fuzzy", "Hard", "Coarse", "Brute",
		"Frozen", "Liquid", "Solid", "Plasma", "Plush", 
		"Golden", "Silver", "Bronze", "Steel",
		"Iron", "Wooden", "Copper", "Metal", "Plastic",
		"Hidden", "Stealth",
		"Sad", "Cry", "Lazy", "Happy"
	};
	
	public static final String[] SUFFIX_LIGHT_NAMES = {
		"Cherry", "Mickey", "Bear",
		"Viking", "Toy", "Lizard", "Moose",
		"Cat", "Cray", "Jelly"
	};
	
	public static final String[] SUFFIX_HARD_NAMES = {
		"Warrior", "Lord", "Ogre", "Orc", "Berserker",
		"Hunter", "Blade", "Powers", "Dexter"
	};
	
	public static final String[] NAMES_DATABASE = {
		"Alfonsito",
		"Alfredo",
		"Alfilia",
		"Alfalfa",
		"Agapito",
		"Amunike",
		"Amaya",
		"Amelio",
		"Annibal",
		"Anaí",
		"Antonelli",
		"Ángel",
		"Ángeles",
		"Austin",
		"Alejandro",
		"Asdruga",
		"Asclepíades",
		"Agliperto",
		"Acursio",
		
		"Balleno",
		"Bart",
		"Benji",
		"Berberecho",
		"Beta",
		"Bedasta",
		"Bumble",
		"Benancio",
		"Beniccio",
		"Benedicto",
		
		"Castro",
		"Cansado",
		"Charley",
		"Charlie",
		"Cheji",
		"Chorizingar",
		"Cojoncio",
		"Cagancio",
		"Chus",
		"Calixto",
		"Cotilla",
		"Graciano",
		
		"Elíseo",
		"Epafrodita",
		"Eleupidio",
		"Eleuteria",
		"Euginio",
		
		"Darío",
		"Deuterio",
		"Demetrio",
		"Despistes",
		"Diego",
		"Desposorios",
		"Despojo",
		"Dimitry",
		"Don Flamenquito",
		"Dolce",
		"Draculin",
		"Donner",
		
		"Elena",
		"Eleuterio",
		"Elpidia",
		"Eusebio",
		"Erika",
		"Espantoso",
		"Espe",
		"Esmeralda",
		"Essencia",
		"Ezequiel",
		
		"Faemino",
		"Fernando",
		"Fagin",
		"Francisquito",
		"Follo",
		
		"Gamfield",
		"Garfield",
		"Gael",
		"Gaudencio",
		"Gamma",
		"Gimly",
		"Gayosito",
		"Goikoetxea",
		"Guille",
		"Guti",
		"Grimwig",
		
		"Hércules",
		"Hommer",
		
		"Iago",
		"Indigno",
		"Isovalio",
		
		"Javi",
		"Java",
		"Jacobo",
		"Jenny",
		"Jesulin",
		"Jewel",
		"Jose María",
		"Juan Carlos",
		"Justin",
		
		"Karmona",
		"Karma",
		"Karin",
		"Kevin",
		"Koki",
		"Kunsito",
		
		"Leonidas",
		"Leticia",
		"Lombardo",
		
		"Manolo",
		"Malcom",
		"Mariano",
		"Maribel",
		"Marimonster de Triana",
		"Mario",
		"Marietete",
		"Mariapelena",
		"Marianinco",
		"Mazinger",
		"Melibea",
		"Miky",
		"Michael",
		"Mochilo",
		"Morgan",
		"Monk",
		"Monstruin de los dolores",
		"Monstruo de la vega",
		"Monstruo de todos los santos",
		"Momo",
		"Morfeo",
		"Mosky",
		"Muiñeira",
		
		"Nancy",
		"Neo",
		"No",
		"Nikita",
		"Never",
		"Nerd",
		
		"Oliver",
		"Oswaldo",
		"Óscar",
		"Octavio",
		
		"Paquito",
		"Pantojita",
		"Paleto",
		"Pedrito",
		"Penélope",
		"Pepe",
		"Petra",
		"Petronilo",
		"Pablo",
		"PiedraRita",
		"Pistacho",
		"Puyol",
		"Prue",
		"Pelmazo",
		"Piedad",
		
		"Radchenko",
		"Rafael",
		"Rafita",
		"Richi",
		"Riky",
		"Ricardo",
		"Ruby",
		"Rails",
		"Rómulo",
		"Rémora",
		"Ronaldito",
		"Rosetón",
		"Rufina",

		"Segismundo",
		"Sofía",
		"Songoku",
		"Socorro",
		"Sowerberries",
		"Stefan",
		
		"Toby",
		"Tomy",
		"Tomas",
		"Toquero",
		"Trono",
		"Tano",
		"Tiago",
		"Tjin",
		"Tesauricocrisonicocrisides",
		
		"Vermello",
		"Venustiano",
		"Venancio",
		"Venao",
		"Vano",
		
		"Xena",
		"Xoia",
		"Xacobeo",
	};
	
	/**
	 * @return a random Monster Name picked up from the NAMES_DATABASE constant list.
	 */
	public static final String random() {
		int randomType = (int) (Math.random() * 100);
		if (randomType < COMPOSE_PROBABILITY) {
			int randomPrefix = (int) (Math.random() * PREFIX_NAMES.length);
			int randomSuffix = (int) (Math.random() * SUFFIX_LIGHT_NAMES.length);
			return PREFIX_NAMES[randomPrefix] + " " + SUFFIX_LIGHT_NAMES[randomSuffix];
		} else {
			int randomIndex = (int) (Math.random() * NAMES_DATABASE.length);
			return NAMES_DATABASE[randomIndex];
		}
	}
}
