package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.Hashtable;
import java.util.List;

public class LairRankingInfoChunk {

	private Hashtable<String, LairRankingInfo> lairsHash;
	private List<LairRankingInfo> lairs;
	private boolean hasMoreElements;
	
	public List<LairRankingInfo> getElements() {
		return lairs;
	}
	public boolean hasMoreElements() {
		return hasMoreElements;
	}
	
	public LairRankingInfoChunk(List<LairRankingInfo> lairs, boolean hasMoreElements) {
		for (LairRankingInfo lair : lairs) {
			lairsHash.put(lair.getLogin(), lair);
		}
		this.lairs = lairs;
		this.hasMoreElements = hasMoreElements;
	}
	
	public boolean isLogin(String login) {
		return lairsHash.contains(login);
	}
	
	public int getPosition(String login) {
		LairRankingInfo lair = lairsHash.get(login);
		if (lair == null) {
			return -1;
		} else {
			return lairs.indexOf(lair);
		}
	}

}
