package es.engade.thearsmonsters.http.view.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class MessageForm extends DefaultActionForm {

    private String author;
    private String content;
        
    public MessageForm() {
        reset();
    }
    
    public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {

    	ActionErrors errors = new ActionErrors();
        PropertyValidator.validateMandatory(errors, "author", author);
        PropertyValidator.validateMandatory(errors, "content", content);

        return errors;
        
    }
    
    private void reset() {
    	author = "";
    	content = "";
    }
    
}
