package es.engade.thearsmonsters.http.view.actionforms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class MonsterActionsToDoForm extends DefaultActionForm {
    private static final long serialVersionUID = 4419231359798052338L;
    
	private String monsterId;
    private Map<String, String> suggestedActions;
    private Map<MonsterActionSuggestion, Integer> actionsToDo;
    
    public MonsterActionsToDoForm() {
        reset();
    }
    
    public String getMonsterId() {
        return monsterId;
    }
    
    public void setMmonsterId(String monsterId) {
        this.monsterId = monsterId;
    }
    
    public String getSuggestedAction(String key) {
    	return suggestedActions.get(key);
    }
    
    public void setSuggestedAction(String key, String value) {
    	suggestedActions.put(key, value);
    }
    
    public Map<MonsterActionSuggestion, Integer> getActionsToDo() {
    	return actionsToDo;
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        //PropertyValidator.validateMandatory(errors, "monsterId", monsterId);
        
        // Valida los suggestedActions y crea con ellos el hash actionsToDo
        for(Map.Entry<String, String> entry : suggestedActions.entrySet()) {
        	if(entry.getValue() != null && entry.getValue() != "" && entry.getValue() != "0") {
        		
	        	// La entryKey es un String del estilo "WorkInTheWorks_in_the_room_TradeOffice"
        		// es decir, que sigue el patrón "${monsterActionType}_in_the_room_${roomType}"
        		// porque asi lo ponemos en el form de monster.jspx
        		String entryKey[] = entry.getKey().split("_in_the_room_");
	        	String monsterActionType = entryKey[0];
	        	String roomType = entryKey[1];
	        	
	        	try {
		        	Integer turnsToUse = Integer.valueOf(entry.getValue());
		        	if(turnsToUse < 0) throw new NumberFormatException();
		        	
		        	MonsterActionSuggestion action = new MonsterActionSuggestion(monsterActionType, monsterId, roomType);
		        	actionsToDo.put(action, turnsToUse);
		        	
	            } catch (NumberFormatException e) {
	            	errors.add("suggestedActions("+ entry.getKey() +")", new ActionMessage("ErrorMessages.incorrectValue"));
	            }
	        }
        }

        return errors;
    }
    
    private void reset() {
        monsterId = null;
        suggestedActions = new HashMap<String, String>();
        actionsToDo = new HashMap<MonsterActionSuggestion, Integer>();
    }

}
