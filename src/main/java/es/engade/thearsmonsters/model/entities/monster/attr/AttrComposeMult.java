package es.engade.thearsmonsters.model.entities.monster.attr;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;

public class AttrComposeMult extends AttrCompose {
	
	private static final long serialVersionUID = -7552636904160588575L;

	public AttrComposeMult(AttrType type, Attr leftAttr, 
			Attr rightAttr) {
		super(type, leftAttr, rightAttr);
	}
	
	@Override
    protected int composeLevel(int leftAttrValue, int rightAttrValue) {
		return leftAttrValue * rightAttrValue;
	}
	
	@Override
    protected String getComposeSimbol() {
		return "*";
	}

	@Override
    protected List<String> brackets(List<String> description) {
		return description; // Los componentes de la multiplicación no necesitan paréntesis
	}

}
