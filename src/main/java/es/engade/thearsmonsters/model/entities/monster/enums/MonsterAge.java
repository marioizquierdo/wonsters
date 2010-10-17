package es.engade.thearsmonsters.model.entities.monster.enums;

import java.util.ArrayList;
import java.util.List;

public enum MonsterAge {
	Child, 
	Cocoon, 
	Adult,
	Old, 
	Dead;
	
    public static final long PERCENT_OF_LIFE_EXPECTANCY_TO_GROW_OLD = 80; // Porcentaje de la esperanza de vida cuando pasa a ancianto
    public static final long PERCENT_OF_VARIATION_TO_GROW_OLD = 10; // Porcentaje de la esperanza de vida de variacion aleatoria para la fecha de convertirse en anciano
    public static final long PERCENT_OF_LIFE_EXPECTANCY_VARIATION = 5; // Porcentaje de la esperanza de vida de variacion aleatoria para la fecha de defuncion
	
	
    /**
     * Valida que una sucesión de edades es correcta.
     * Solo se puede asignar otra edad a un monstruo siguiendo el siguiente diagrama:
     *  Child -> Cocoon -> Adult -> Old
     *                        \-------\----> Death
     */
	public static boolean validateAgeFlow(MonsterAge from, MonsterAge to) {
		if(to == null) return false;
		return 
			from == null ||
			from.equals(Child) && to.equals(Cocoon) ||
			from.equals(Cocoon) && to.equals(Adult) ||
			from.equals(Adult) && to.equals(Old) ||
			from.equals(Adult) && to.equals(Dead) ||
			from.equals(Old) && to.equals(Dead);
	}
	

	
	/**
	 * Convierte la representación de una lista en String a la lista real.
	 * Se usa para simplificar la definición de listas de edades.
	 * @param stringList Literales de AgeState separados por comas, xej: "Child, Adult, Old",
 	 * 			o bien el string "all", que incluye a todas las edades,
 	 * 			o bien un string vacío "", que devuelve una lista vacía.
	 * @return Lista de AgeState correspondiente con los elementos declarados en el String de entrada.
	 */
	public static List<MonsterAge> list(String stringList) {
		if(stringList.equals("") || stringList==null) { // devuelve lista vacía
			return new ArrayList<MonsterAge>(0);
		}else if(stringList.toLowerCase().equals("all")) { // all significa añadir todos los tipos que haya en AgeState
			List<MonsterAge> list = new ArrayList<MonsterAge>(MonsterAge.values().length);
			for(MonsterAge ageState : MonsterAge.values()) {
				list.add(ageState);
			}
			return list;
		} else {
			String[] stringArray = stringList.split(","); // Se separan por comas
			List<MonsterAge> list = new ArrayList<MonsterAge>(stringArray.length); // Se define la lista
			for(String e : stringArray) { // se convierte cada String en su elemento de AgeState correspondiente
				list.add(Enum.valueOf(MonsterAge.class, e.trim()));
			}
			return list;
		}
	}
}
