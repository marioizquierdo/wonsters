package es.engade.thearsmonsters.model.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pa
 *
 */
public class PaginatorChunk implements Serializable {

    private List list; // List of elements
    private int startIndex; // Position of the first element (starts in 1)
    private int count; // max number of elements of each page
    private int totalCount; // Total number of elements in the database table
    private Map<String,String> commonParams; // Parameters for previous and next links
    
    public PaginatorChunk(List list, int startIndex, int count, int totalCount) {
        
        this.list = list;
        this.startIndex = startIndex;
        this.count = count;
        this.totalCount = totalCount;
        this.commonParams = new HashMap<String,String>();

    }
    
    //---------- Getters ------------//
    
    /**
     * Get elements as a list
     */
    public List getList() {
    	return list;
    }

	public int getStartIndex() {
		return startIndex;
	}
	
	public int getCount() {
		return count;
	}

	public int getTotalCount() {
		return totalCount;
	}
		
    /**
     * @return params for the previous link or null if the next link must not be displayed
     */
	public Map<String,String> getPreviousLinkParameters() {
		if(startIndex > 1) {
			Map<String,String> params = copy(commonParams);
			params.put("startIndex", getPreviousStartIndex()+"");
			params.put("count", count+"");
			return params;
		} else {
			return null;
		}
	}
		
    /**
     * @return params for the next link or null if the next link must not be displayed
     */
    public Map<String, String> getNextLinkParameters() {
    	if((startIndex + count - 1) < totalCount) {
    		Map<String,String> params = copy(commonParams);
    		params.put("startIndex", getNextStartIndex()+"");
    		params.put("count", count+"");
	        return params;
	    } else {
	    	return null;
	    }
    }
    
    public int getCurrentPage() {
    	return (startIndex / count) + 1;
    }
    
    /**
     * Number of pages having 'count' number of elements on each page.
     */
    public int getTotalPages() {
    	return (totalCount / count) + 1;
    }
	
	//------------ Setters -----------------//
    
	/**
	 * Parameters used in next or previous links (common).
	 */
    public void setCommonNextPreviousLinkParameters(Map<String, String> commonParams) {
    	this.commonParams = commonParams;
    }
    
    /**
     * Instead of define a Map and set params, you can add one by one using this method.
     * @see setCommonNextPreviousLinkParameters
     */
    public PaginatorChunk addParameter(String key, String value) {
    	this.commonParams.put(key, value);
    	return this;
    }

	public String toString() {
        return new String("list = [" + list + "] | " +
                "startIndex = " + startIndex + " | " +
                "count = " + count + " | " +
                "totalCount = " + totalCount);
    }  
	
	
	//---- Helpers -----//
	
    private int getPreviousStartIndex() {
	    if((startIndex-count) > 0) {
	    	return startIndex-count;
	    } else {
	    	return 1;
	    }
    }
    
    private int getNextStartIndex() {
    	return startIndex + count;
    }
    
    /**
     * Return a clone of the params Map but without clone the elements inside.
     */
    private Map<String,String> copy(Map<String,String> params) {
		Map<String,String> copy = new HashMap<String,String>();
		for(String key: params.keySet()) {
			copy.put(key, params.get(key));
		}
		return copy;
    }

}
