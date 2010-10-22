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
		"Antonie",
		"Ángel",
		"Ángeles",
		"Austin",
		"Alejandro",
		"Alex",
		"Alexandre",
		"Asdruga",
		"Asclepíades",
		"Agliperto",
		"Acursio",
		"Answer",
		
		"Balleno",
		"Bart",
		"Barnei",
		"Benji",
		"Berberecho",
		"Beta",
		"Bedasta",
		"Bumble",
		"Benancio",
		"Beniccio",
		"Benedicto",
		"Bombat",
		"Bob",
		
		"Castro",
		"Cansado",
		"Charley",
		"Charlie",
		"Charles",
		"Cheji",
		"Chorizingar",
		"Cojoncio",
		"Cagancio",
		"Chus",
		"Calixto",
		"Cotilla",
		
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
		"Ezequiel",
		
		"Faemino",
		"Fernando",
		"Fagin",
		"Francisquito",
		"Follo",
		"Franco",
		"Frederick",
		
		"Gamfield",
		"Garfield",
		"Gael",
		"Gayosito",
		"Gaudencio",
		"Gamma",
		"Gimly",
		"Glenn",
		"Goikoetxea",
		"Grimwig",
		"Graciano",
		"Guille",
		"Guti",
		
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
		"John",
		"Jonathan",
		"Juan Carlos",
		"Justin",
		
		"Karmona",
		"Karma",
		"Karin",
		"Kevin",
		"Ken",
		"Koki",
		"Konum",
		"Koruño",
		"Kunsito",
		
		"Leonidas",
		"Leticia",
		"Lombardo",
		"Louisse",
		"Lazzy",
		
		"Manolo",
		"Malcom",
		"Mariano",
		"Maribel",
		"Mark",
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
		"Napoleon",
		"Nessy",
		"Neo",
		"Never",
		"Nerd",
		"Neng",
		"Nikita",
		"Nicholas",
		"No",
		
		"Oliver",
		"Oswaldo",
		"Óscar",
		"Octavio",
		
		"Paquito",
		"Pantojita",
		"Paul",
		"Paleto",
		"Pedrito",
		"Penélope",
		"Pelmazo",
		"Pepe",
		"Petra",
		"Petronilo",
		"Pablo",
		"PiedraRita",
		"Pistacho",
		"Puyol",
		"Prue",
		"Piedad",
		
		"Radchenko",
		"Rafael",
		"Rafita",
		"Richi",
		"Riky",
		"Richard",
		"Ricardo",
		"Rails",
		"Rémora",
		"Ronaldito",
		"Rosetón",
		"Rocco",
		"Rómulo",
		"Robert",
		"Rockstar",
		"Roquefort",
		"Rufina",
		"Ruby",
		"Russell",

		"Segismundo",
		"Sofía",
		"Songoku",
		"Socorro",
		"Sowerberries",
		"Stefan",
		"Stephen",

		"Tano",
		"Tesauricocrisonicocrisides",
		"Tiago",
		"Tjin",
		"Toby",
		"Tomy",
		"Tony",
		"Tomas",
		"Toquero",
		"Torero",
		"Torpedo",
		"Torrente",
		"Trono",
		
		"Vermello",
		"Venustiano",
		"Venancio",
		"Venao",
		"Vano",
		
		"Woodie",
		"William",
		"Willy",
		
		"Xena",
		"Xoia",
		"Xacobeo",
		
		"Z",
		"Zopenco",
		"Zapatero"
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
