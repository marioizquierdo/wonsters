package es.engade.thearsmonsters.model.entities.monster.attr;

import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;

public class AttrComposeSum extends AttrCompose {
	
	private static final long serialVersionUID = 2581064923138892336L;

	public AttrComposeSum(AttrType type, Attr leftAttr, 
			Attr rightAttr) {
		super(type, leftAttr, rightAttr);
	}
	
	@Override
    protected int composeLevel(int leftAttrValue, int rightAttrValue) {
		return leftAttrValue + rightAttrValue;
	}
	
	@Override
    protected String getComposeSimbol() {
		return "+";
	}
	
}
