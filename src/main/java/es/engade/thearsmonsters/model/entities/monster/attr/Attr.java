package es.engade.thearsmonsters.model.entities.monster.attr;

import java.io.Serializable;
import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;

public abstract class Attr implements Serializable {

	private static final long serialVersionUID = 8455891862894594575L;
	private AttrType type;
	
	public Attr(AttrType type) {
		this.type = type;
	}
	
	
	/**
	 * Tipo de Atributo. Del type también se puede sacar información útil.
	 */
	public AttrType getType() { return type; }
	
	/**
	 * Nivel del atributo o habilidad.
	 */
	public abstract int getLevel();
	
	
	/**
	 * Número entre 0 y 100 que indica la experiencia dentro del nivel. 
	 */
	public abstract int getExp();
	
	/**
	 * Añade experiencia al atributo. Si se pasa de 100, sube el nivel 
	 * automáticamente, añade la experiencia restante, y devuelve true.
	 * Este método solo debe ser utilizado desde Monster.
	 * Para añadir experiencia desde afuera hágase a través del método
	 * monster.addAttrExp(attrType, exp), sino puede causar error.
	 * @returns true si el atributo sube de nivel (es decir, si la experiencia total pasa de 100).
	 * @throws UnsupportedOperationException si se intenta añadir experiencia a un atributo compuesto.
	 */
	public abstract boolean addExp(int exp) throws UnsupportedOperationException;
	
	/**
	 * Nombre del atributo o descripción de la fórmula que lo compone.
	 * Se devuelve una lista de Strings en vez de un solo String porque algunos
	 * elementos deben ser traducidos (los que lleven el prefijo 'attr.')
	 * Ejemplo: {"attr.strenght", "+", "attr.happyness"} se debe traducir y concatenar
	 * en la vista como "fuerza + felicidad" (para el castellano) 
	 * @return una lista de Strings para ser traducida y concatenada en la vista
	 */
	public abstract List<String> getDescription();
	
	/**
	 * Devuelve lo mismo que getDescription() pero en lugar de tener parámetros
	 * con el prefijo 'attr.' para traducir lleva el valor
	 * Esta cadena podría ser un solo String (porque no necesita ser traducido), 
	 * pero se devuelve una lista para mantener coherencia con getDescription
	 */
	public abstract List<String> getValueDescription();
	
	
	@Override
    public boolean equals(Object o) {
		Attr a = (Attr) o;
		return 
			this.getType().equals(a.getType()) &&
			this.getLevel()==a.getLevel() &&
			this.getExp() == a.getExp();
	}
	
	// Muestra algo como Strength(5), o Agility(10+55%)
	@Override
    public String toString() {
		String s = getType().toString();
		s += "(" + getLevel();
		if(getExp() > 0) {
			s += "+" + getExp() + "%"; 
		}
		s += ")";
		return s;
	}
		
	
	
}
