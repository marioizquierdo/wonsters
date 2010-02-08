package es.engade.thearsmonsters.http.view.applicationobjects;

import java.io.Serializable;
import java.util.Map;

public class ConfirmationData implements Serializable {
	private String acceptAction;
	private Map<String, String> acceptParameters;
	private String cancelLink;
	private String message;
	
	public ConfirmationData(String acceptAction, Map<String, String> acceptParameters, String cancelLink, String message) {
		this.acceptAction = acceptAction;
		this.acceptParameters = acceptParameters;
		this.cancelLink = cancelLink;
		this.message = message;
	}

	public String getAcceptAction() {
		return acceptAction;
	}

	public void setAcceptAction(String acceptAction) {
		this.acceptAction = acceptAction;
	}

	public Map<String, String> getAcceptParameters() {
		return acceptParameters;
	}

	public void setAcceptParameters(Map<String, String> acceptParameters) {
		this.acceptParameters = acceptParameters;
	}

	public String getCancelLink() {
		return cancelLink;
	}

	public void setCancelLink(String cancelAction) {
		this.cancelLink = cancelAction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
