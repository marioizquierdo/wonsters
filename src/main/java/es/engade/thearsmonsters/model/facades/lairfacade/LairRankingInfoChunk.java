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
		lairsHash = new Hashtable<String, LairRankingInfo>();
		for (LairRankingInfo lair : lairs) {
			lairsHash.put(lair.getLogin(), lair);
		}
		this.lairs = lairs;
		this.hasMoreElements = hasMoreElements;
	}
	
	// Devuelve true si el usuario con ese login está incluido en el ranking
	public boolean isLoginIncluded(String login) {
		return lairsHash.contains(login);
	}
	
	// Devueve la posicion del usuario ranking.positionOfUser
	public int getPositionOfUser(String login) {
		LairRankingInfo lair = lairsHash.get(login);
		if (lair == null) {
			return -1;
		} else {
			return lairs.indexOf(lair);
		}
	}

}
