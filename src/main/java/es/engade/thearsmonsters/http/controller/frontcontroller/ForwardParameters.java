package es.engade.thearsmonsters.http.controller.frontcontroller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts.action.ActionForward;

/**
 * If you have a Struts Action servlet and you want to redirect to another page, 
 * the standard Struts technique is to return an ActionForward and setup an appropriate 
 * forward entry in struts-config.xml:
 *      return mapping.findForward("success");
 * Unfortunately, this doesn’t provide a mechanism for passing request parameters to the target. 
 * So what can you do if you want to redirect to another page and pass some parameters along? 
 * You use this additional class.
 * 
 * Usage:
 * ActionForward forward = mapping.findForward("success");
 * return new ForwardParameters().add("myparam", "myvalue").add("something", "fornothing").forward(forward);
 */
public class ForwardParameters {
	private Map params = null;

	public ForwardParameters() {
		params = new HashMap();
	}

	/**
	 * Add a single parameter and value.
	 * 
	 * @param paramName
	 *            Parameter name
	 * @param paramValue
	 *            Parameter value
	 * 
	 * @return A reference to this object.
	 */
	public ForwardParameters add(String paramName, String paramValue) {
		params.put(paramName, paramValue);
		return this;
	}

	/**
	 * Add a set of parameters and values.
	 * 
	 * @param paramMap
	 *            Map containing parameter names and values.
	 * 
	 * @return A reference to this object.
	 */
	public ForwardParameters add(Map paramMap) {
		Iterator itr = paramMap.keySet().iterator();
		while (itr.hasNext()) {
			String paramName = (String) itr.next();
			params.put(paramName, paramMap.get(paramName));
		}

		return this;
	}

	/**
	 * Add parameters to a provided ActionForward.
	 * 
	 * @param forward
	 *            The ActionForward object to add parameters to.
	 * 
	 * @return ActionForward with parameters added to the URL.
	 */
	public ActionForward forward(ActionForward forward) {
		StringBuffer path = new StringBuffer(forward.getPath());
		Iterator itr = params.entrySet().iterator();
		if (itr.hasNext()) {
			// add first parameter, if avaliable
			Map.Entry entry = (Map.Entry) itr.next();
			path.append("?" + entry.getKey() + "=" + entry.getValue());

			// add other parameters
			while (itr.hasNext()) {
				entry = (Map.Entry) itr.next();
				path.append("&" + entry.getKey() + "=" + entry.getValue());
			}
		}
		// Clone previous action forward but with new path
		ActionForward forwardWithParams = new ActionForward(path.toString());
		forwardWithParams.setModule(forward.getModule());
		forwardWithParams.setName(forward.getName());
		forwardWithParams.setRedirect(forward.getRedirect());
		return forwardWithParams;
	}
}
