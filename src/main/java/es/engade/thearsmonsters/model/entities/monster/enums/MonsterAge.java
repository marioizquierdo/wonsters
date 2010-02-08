package es.engade.thearsmonsters.model.entities.monster.enums;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public enum MonsterAge {
	Child		((byte) 1),
	Cocoon		((byte) 2),
	Adult		((byte) 2),
	Old			((byte) 3),
	Dead		((byte) 4);
	
	/**
	 * If you know the type code then you can get the corresponding AgeState instance
	 * @param ageState DB code of the AgeState instance
	 * @return AttrType
	 */
	static public MonsterAge getFromCode(byte code) 
		throws InstanceNotFoundException {
		
		switch(code) {
			case 1: return Child;
			case 2: return Adult; // Desambiguation for Cocoon and Adult
			case 3: return Old;
			case 4: return Dead;
			
			default: throw new InstanceNotFoundException(
                    "AgeState not exists! (code="+ code +")", 
                    MonsterAge.class.getName());
		}
	}

	private final byte code; // Codigo en la BBDD
	MonsterAge(byte code) {this.code = code;}
	public byte code()   { return code; }
	public boolean equals(MonsterAge a) {return this.code() == a.code();}
	
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
