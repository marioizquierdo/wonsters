package es.engade.thearsmonsters.http.view.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class BornMonsterForm extends DefaultActionForm {

    private String monsterName;
    private String eggId;
    private Long eggIdAsLong;
        
    public BornMonsterForm() {
        reset();
    }
    
    public String getMonsterName() {
		return monsterName;
	}

	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
	}

	public String getEggId() {
		return eggId;
	}

	public void setEggId(String eggId) {
		this.eggId = eggId;
	}

	public long getEggIdAsLong() {
		return eggIdAsLong;
	}


	@Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    @Override
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        eggIdAsLong = new Long(PropertyValidator.validateLong(
        		errors,"eggId", eggId, true, 1, Long.MAX_VALUE));

        PropertyValidator.validateMandatory(errors, "monsterName", monsterName);

        return errors;
        
    }
    
    private void reset() {
    	monsterName = "";
    	eggId = null;
    	eggIdAsLong = null;
    }
    
}
