package es.engade.thearsmonsters.model.entities.monster.attr;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;

public class AttrComposeMult extends AttrCompose {
	
	private static final long serialVersionUID = -7552636904160588575L;

	public AttrComposeMult(AttrType type, Attr leftAttr, 
			Attr rightAttr) {
		super(type, leftAttr, rightAttr);
	}
	
	protected int composeLevel(int leftAttrValue, int rightAttrValue) {
		return leftAttrValue * rightAttrValue;
	}
	
	protected String getComposeSimbol() {
		return "*";
	}

	protected List<String> brackets(List<String> description) {
		return description; // Los componentes de la multiplicación no necesitan paréntesis
	}

}
