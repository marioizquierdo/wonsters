package es.engade.thearsmonsters.model.facades.lairfacade;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class LairRankingInfoChunk {

	private Hashtable<String, LairInfo> lairsHash;
	private List<LairInfo> lairs;
	private boolean hasMoreElements;

	
	public LairRankingInfoChunk(List<LairInfo> lairs, boolean hasMoreElements) {
		lairsHash = new Hashtable<String, LairInfo>();
		for (LairInfo lair : lairs) {
			lairsHash.put(lair.getLogin(), lair);
		}
		this.lairs = lairs;
		this.hasMoreElements = hasMoreElements;
	}
	
	public List<LairInfo> getElements() {
		return lairs;
	}
	public boolean hasMoreElements() {
		return hasMoreElements;
	}
	
	public int getSize() {
		return lairs.size();
	}
	
	public LairInfo getFirst() {
		return lairs.get(0);
	}
	
	public LairInfo getLast() {
		return lairs.get(lairs.size() - 1);
	}
	
	/** 
	 * Devuelve true si el usuario con ese login está incluido en el ranking
	 * @param login nombre del usuario
	 */
	public boolean isUserIncluded(String login) {
		if (lairsHash.contains(login)) {
			return true;
		} else {
			for (String l : lairsHash.keySet()) {
				if (l.equalsIgnoreCase(login)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** isUserIncluded(String login) JSTL wrapper.
	 * para poder invocarlo desde JSTL (ya que no permite getters con parámetros)
	 * @return un Hash con el método get modificado, de esta forma usamos el key del get para pasarlo como parámetro al método.
	 */
	@SuppressWarnings("serial")
    public Map<String, Boolean> getUserIncluded() {
		return new HashMap<String, Boolean>() {
			public Boolean get(Object key) {
            	return isUserIncluded((String) key);
            }
		};
	}
	
	/**
	 *  Devueve la posicion del usuario ranking.positionOfUser empezando desde 1 (el primero es 1, no 0),
	 *  o -1 si no esta incluido en la lista.
	 */
	public int positionOfUser(String login) {
		LairInfo lair = lairsHash.get(login);
		if (lair == null) {
			for (String l : lairsHash.keySet()) {
				if (l.equalsIgnoreCase(login)) {
					lair = lairsHash.get(l);
					break;
				}
			}
		}
		if (lair == null) {
			return -1;
		} else {
			return lairs.indexOf(lair) + 1; // hay que sumarle 1 porque el ranking no es zero-based-index.
		}
	}
	
	/**
	 * positionOfUser(String login) JSTL wrapper.
	 * para poder invocarlo desde JSTL (ya que no permite getters con parámetros)
	 * @return un Hash con el método get modificado, de esta forma usamos el key del get para pasarlo como parámetro al método.
	 */
	@SuppressWarnings("serial")
    public Map<String, Integer> getPositionOfUser() {
		return new HashMap<String, Integer>() {
			public Integer get(Object key) {
            	return positionOfUser((String) key);
            }
		};
	}
	

}
