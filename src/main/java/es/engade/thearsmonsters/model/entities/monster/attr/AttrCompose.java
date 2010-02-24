package es.engade.thearsmonsters.model.entities.monster.attr;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;

public abstract class AttrCompose extends Attr {
	
	private static final long serialVersionUID = 1L;
	protected Attr leftAttr;
	protected Attr rightAttr;
	
	public AttrCompose(AttrType type, Attr leftAttr, Attr rightAttr) {
		super(type);
		this.leftAttr = leftAttr;
		this.rightAttr = rightAttr;
	}
	
	@Override
    public void addExp(int exp) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Can not add experience to a compose attribute");
	}
	
	/**
	 * Se devuelve la media aritmética de la experiencia de los atributos
	 * de los que se compone. En realidad nunca se utiliza (se supone que un atributo
	 * compuesto no tiene exp porque no se puede subir de nivel directamente, sino
	 * a través de sus agregados. Sin embargo, en caso de querer saber la exp de este
	 * atributo, la media sería el valor más correcto.
	 */
	@Override
    public int getExp() {
		return (leftAttr.getExp() + rightAttr.getExp()) / 2;
	}
	
	@Override
    public int getLevel() {
		return composeLevel(leftAttr.getLevel(), rightAttr.getLevel());
	}
	
	@Override
    public List<String> getDescription() {
		List<String> desc = new ArrayList<String>();
		desc.addAll(leftAttr.getDescription());
		desc.add(getComposeSimbol());
		desc.addAll(rightAttr.getDescription());
		return brackets(desc);
	}
	
	@Override
    public List<String> getValueDescription() {
		List<String> desc = new ArrayList<String>();
		desc.addAll(leftAttr.getValueDescription());
		desc.add(getComposeSimbol());
		desc.addAll(rightAttr.getValueDescription());
		return brackets(desc);
	}
	
	/**
	 * Pone paréntesis a la descripción del atributo compuesto
	 */
	protected List<String> brackets(List<String> description) {
		description.add(0, "(");
		description.add(")");
		return description;
	}

	
	//---------- Métodos abstractos ------------//
	
	/**
	 * Operación de composición para el nivel del atributo compuesto.
	 */
	protected abstract int composeLevel(int leftAttrValue, int rightAttrValue);
	
	/**
	 * Símbolo para mostrar en las descripciones
	 */
	protected abstract String getComposeSimbol();
	
	
}
