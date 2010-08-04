package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.List;

public class LairRankingInfoChunk {

	private List<LairRankingInfo> lairs;
	private boolean hasMoreElements;
	
	public List<LairRankingInfo> getElements() {
		return lairs;
	}
	public boolean hasMoreElements() {
		return hasMoreElements;
	}
	
	public LairRankingInfoChunk(List<LairRankingInfo> lairs, boolean hasMoreElements) {
		this.lairs = lairs;
		this.hasMoreElements = hasMoreElements;
	}

}
