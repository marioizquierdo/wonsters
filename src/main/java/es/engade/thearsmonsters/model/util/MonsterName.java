package es.engade.thearsmonsters.model.util;

public class MonsterName {
	
	private static final int COMPOSE_PROBABILITY = 80;
	
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
		"Cherry", "Jenny", "Mickey", "Bear",
		"Viking", "Toy", "Lizard", "Moose",
		"Cat", "Cray", "Jelly"
	};
	
	public static final String[] SUFFIX_HARD_NAMES = {
		"Warrior", "Lord", "Ogre", "Orc", "Berserker",
		"Hunter", "Blade"
	};
	
	public static final String[] NAMES_DATABASE = {
		"Alfonsito", 
		"Ángel",
		"Bumble",
		"Castro",
		"Charley",
		"Charlie",
		"Diego",
		"Fagin",
		"Gamfield",
		"Garfield",
		"Guille",
		"Grimwig",
		"Iago",
		"Javi",
		"Jewel",
		"Jenny",
		"Kevin",
		"Leónidas",
		"Mario",
		"Monk",
		"Morfeo",
		"Nancy",
		"Neo",
		"Oliver",
		"Pedrito",
		"Rosetón",
		"Sowerberries",
		"Toby",
		"Tomy",
		"Trono",
	};
	
	/**
	 * @return a random Monster Name picked up from the NAMES_DATABASE constant list.
	 */
	public static final String random() {
		int randomType = (int) (Math.random() * 100);
		if (randomType < COMPOSE_PROBABILITY) {
			int randomPrefix = (int) (Math.random() * PREFIX_NAMES.length);
			int randomSuffix = (int) (Math.random() * SUFFIX_LIGHT_NAMES.length);
			return PREFIX_NAMES[randomPrefix] + SUFFIX_LIGHT_NAMES[randomSuffix];
		} else {
			int randomIndex = (int) (Math.random() * NAMES_DATABASE.length);
			return NAMES_DATABASE[randomIndex];
		}
	}
}
