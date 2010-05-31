package es.engade.thearsmonsters.model.facades.lairfacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.lair.Address;
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

    public BuildingChunk(List<Lair> lairsOfTheBuilding, int street, int building) throws IncorrectAddressException {
        
    	// Check coordinates [0..N-1]
    	if(!(Address.checkBuilding(building) && Address.checkStreet(street)))
    		throw new IncorrectAddressException(street, building);
    	
    	// Set attributes
        this.nextStreet = nextStreet(street);
        this.nextBuilding = nextBuilding(building);
        this.nextBuildingStreet = nextBuildingStreet(building, street);
        this.previousStreet = previousStreet(street);
        this.previousBuilding = previousBuilding(building);
        this.previousBuildingStreet = previousBuildingStreet(building, street);

        // Fill lairs list with nulls when empty place
        // (The index of the list must correspond to the floor coordinate)
        this.lairs = toBuildingList(lairsOfTheBuilding);
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
    
    /**
     * Dada una lista de guaridas que pertenezcan a un mismo bloque
     * (es decir, que tengan el mismo street y building), devuelve
     * otra lista similar donde la posición de cada guarida dentro de la
     * lista se corresponde con su coordenada floor (donde no haya guarida, pone null).
     * Por ejemplo, dadas la lista con las siguientes coordenadas (street, building, floor):
     * [(2,2,0), (2,2,1), (2,2,5), (2,2,4)] y suponiendo que GameConf.getMaxNumberOfFloors()==10, devolvería:
     * [(2,2,0), (2,2,1), null, null, (2,2,4), (2,2,5), null, null, null, null].
     * 
     * @throws IncorrectAddressException si alguna guarida tiene su coordenada floor incorrecta.
     */
    private List<Lair> toBuildingList(List<Lair> list) {
    	int floors = GameConf.getMaxNumberOfFloors();
    	List<Lair> buildingList = new ArrayList<Lair>(floors);
    	
    	for(int i=0; i<floors; i++) { // first, fill with nulls
    		buildingList.add(null);
    	}
    	
    	for(Lair lair : list) { // then put lairs in its position depending on the floor coordinate
    		try {
    			buildingList.set(lair.getAddressFloor(), lair);
    			
    		} catch (IndexOutOfBoundsException e) { // prevent it from incorrect address
    			throw new IncorrectAddressException(lair.getAddressStreet(), lair.getAddressBuilding(), lair.getAddressFloor());
    		}
    	}
    	return buildingList;
    }
    
    
    private int nextStreet(int street) {
    	return Address.nextStreet(street);
    }
    
    private int nextBuilding(int building) {
    	return Address.nextBuilding(building);
    }
    
    /**
     * Next street when advance one building: if the building is the last,
     * when advance to next, the street must be advanced too (avoid loops).
     */
    private int nextBuildingStreet(int building, int street) {
    	return Address.nextBuildingStreet(building, street);
    }
    
    private int previousStreet(int street) {
    	return Address.previousStreet(street);
    }
    
    private int previousBuilding(int building) {
    	return Address.previousBuilding(building);
    }
    
    /**
     * Previous street when advance one building: if the building is the first,
     * when back to previous, the street must get back too (avoid loops).
     */
    private int previousBuildingStreet(int building, int street) {
    	return Address.previousBuildingStreet(building, street);
    }

}
