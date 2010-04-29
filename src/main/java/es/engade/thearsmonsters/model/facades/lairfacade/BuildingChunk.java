package es.engade.thearsmonsters.model.facades.lairfacade;

import java.io.Serializable;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.util.GameConf;

/**
 * Devuelve un bloque de guaridas (siguadas en el mismo building), y a mayores tiene métodos
 * para calcular la siguiente coordenada.
 * Las coordenadas de una guarida son street, building y floor.
 * Desde la interfaz, cuando se obtiene un bloque situado en la coordenada (street, building),
 * se tienen que añadir los params necesarios para los botones next-building, next-street,
 * previous-building y previous-street, y deben avanzar de la siguiente forma:
 *  <ul>
 *  <li> Previous/Next Street: al llegar al principio/fin, se hace un bucle.
 *       El building permanece intacto, es decir, del punto (last-street, building)
 *       se pasa a (0, building), y lo mismo al retroceder.
 *  </li>
 *  <li> Previous/Next Building: Cuando se llega al último building, se pasa al
 *       primero, avanzando también el street, es decir, del punto (street, last-building)
 *       se pasa a (street + 1, 0), y lo mismo al retroceder.
 *  </li>
 *  </ul>
 */
public class BuildingChunk implements Serializable {

	private static final long serialVersionUID = 200912142037L;
	
	private List<Lair> lairs;
    private int nextStreet; // next Street 
    private int nextBuilding; // next Building
    private int nextBuildingStreet; // next Street when calculate next Building
    private int previousStreet;
    private int previousBuilding;
    private int previousBuildingStreet;

    public BuildingChunk(List<Lair> lairs, int street, int building) throws IncorrectAddressException {
        
    	// Check coordinates
    	if(building <= 0 || building >= GameConf.getMaxNumberOfBuildings() ||
    			street <= 0 || street >= GameConf.getMaxNumberOfStreets()) 
    		throw new IncorrectAddressException(street, building);
    	
    	// Fill attributes
        this.lairs = lairs;
        this.nextStreet = nextStreet(street);
        this.nextBuilding = nextBuilding(building);
        this.nextBuildingStreet = nextBuildingStreet(building, street);
        this.previousStreet = previousStreet(street);
        this.previousBuilding = previousBuilding(building);
        this.previousBuildingStreet = previousBuildingStreet(building, street);

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

	public List<Lair> getLairs() {
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
    
    
    
    //--------- Private --------//
    
    
    private int nextStreet(int street) {
    	int nextStreet = street+1;
    	if(nextStreet >= GameConf.getMaxNumberOfStreets()) {
    		nextStreet = 0;
    	}
    	return nextStreet;
    }
    
    private int nextBuilding(int building) {
    	int nextBuilding = building+1;
    	if(nextBuilding >= GameConf.getMaxNumberOfBuildings()) {
    		nextBuilding = 0;
    	}
    	return nextBuilding;
    }
    
    /**
     * Next street when advance one building: if the building is the last,
     * when advance to next, the street must be advanced too (avoid loops).
     */
    private int nextBuildingStreet(int building, int street) {
    	if(building >= (GameConf.getMaxNumberOfBuildings()-1)) {
    		return nextStreet(street);
    	} else {
    		return street;
    	}
    }
    
    private int previousStreet(int street) {
    	int previousStreet = street-1;
    	if(previousStreet < 0) {
    		previousStreet = GameConf.getMaxNumberOfStreets()-1;
    	}
    	return previousStreet;
    }
    
    private int previousBuilding(int building) {
    	int previousBuilding = building-1;
    	if(previousBuilding < 0) {
    		previousBuilding = GameConf.getMaxNumberOfBuildings()-1;
    	}
    	return previousBuilding;
    }
    
    /**
     * Previous street when advance one building: if the building is the first,
     * when back to previous, the street must get back too (avoid loops).
     */
    private int previousBuildingStreet(int building, int street) {
    	if(building == 0) {
    		return previousStreet(street);
    	} else {
    		return street;
    	}
    }

}
