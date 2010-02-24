package es.engade.thearsmonsters.model.facades.lairfacade;

import java.io.Serializable;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;

public class BuildingChunk implements Serializable {

	private static final long serialVersionUID = 200912142037L;
	
	private List<Lair> lairs;
    private int nextStreet; // next Street 
    private int nextBuilding; // next Building
    private int nextBuildingStreet; // next Street when calculate next Building
    private int previousStreet;
    private int previousBuilding;
    private int previousBuildingStreet;

    public BuildingChunk(List<Lair> lairs, 
    		int nextStreet, int nextBuilding, int nextBuildingStreet, 
    		int previousStreet, int previousBuilding, int previousBuildingStreet) {
        
        this.lairs = lairs;
        this.nextStreet = nextStreet;
        this.nextBuilding = nextBuilding;
        this.nextBuildingStreet = nextBuildingStreet;
        this.previousStreet = previousStreet;
        this.previousBuilding = previousBuilding;
        this.previousBuildingStreet = previousBuildingStreet;

    }

	public int getNextStreet() {
		return nextStreet;
	}

	public int getNextBuilding() {
		return nextBuilding;
	}

	public int getNextBuildingStreet() {
		return nextBuildingStreet;
	}

	public int getPreviousStreet() {
		return previousStreet;
	}

	public int getPreviousBuilding() {
		return previousBuilding;
	}

	public int getPreviousBuildingStreet() {
		return previousBuildingStreet;
	}

	public List<Lair> getLairVOs() {
		return lairs;
	}
    
    @Override
    public String toString() {
        return new String(
        		"lairs = " + lairs + " | " +
        		"nextStreet = " + nextStreet + " | " +
        		"nextBuilding = " + nextBuilding + " | " +
        		"previousStreet = " + previousStreet + " | " +
        		"previousBuilding = " + previousBuilding);
    }

}
