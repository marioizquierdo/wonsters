package es.engade.thearsmonsters.model.util;

public class MonsterName {
	
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
		int randomIndex = (int) (Math.random() * NAMES_DATABASE.length);
		return NAMES_DATABASE[randomIndex];
	}
}
