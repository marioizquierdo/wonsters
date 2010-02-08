package es.engade.thearsmonsters.http.view.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class ChangeResourcesForm extends DefaultActionForm {

    private String garbage;
    private String money;
    private Integer garbageAsInt;
    private Integer moneyAsInt;
        
    public ChangeResourcesForm() {
        reset();
    }

	public String getGarbage() {
		return garbage;
	}

	public void setGarbage(String garbage) {
		this.garbage = garbage;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getGarbageAsInt() {
		return garbageAsInt;
	}

	public int getMoneyAsInt() {
		return moneyAsInt;
	}


	public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        garbageAsInt = (int) PropertyValidator.validateLong(
        		errors, "garbage", garbage, false, 0, Integer.MAX_VALUE);
        moneyAsInt = (int) PropertyValidator.validateLong(
        		errors, "money", money, false, 0, Integer.MAX_VALUE);
        if(garbageAsInt != 0 && moneyAsInt != 0) {
        	errors.add("garbage", new ActionMessage("ErrorMessages.changeResources.onlyMoneyOrGarbage"));
        }

        return errors;
        
    }
    
    private void reset() {
    	garbage = "0";
    	money = "0";
    	garbageAsInt = null;
    	moneyAsInt = null;
    }
    
}
