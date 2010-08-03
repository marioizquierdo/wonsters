package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.List;

public class LairBlock {

	private List<LairInfo> lairs;
	private boolean hasMoreElements;
	
	public List<LairInfo> getLairs() {
		return lairs;
	}
	public boolean hasMoreElements() {
		return hasMoreElements;
	}
	
	public LairBlock(List<LairInfo> lairs, boolean hasMoreElements) {
		this.lairs = lairs;
		this.hasMoreElements = hasMoreElements;
	}

}
