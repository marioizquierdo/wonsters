package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;

import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.util.GameConf;

public class Address implements Serializable, Comparable<Address> {

    public static final int BUILDINGS_PER_STREET = GameConf.getMaxNumberOfBuildings();
    public static final int FLOORS_PER_BUILDING = GameConf.getMaxNumberOfFloors();
    public static final int TOTAL_STREETS = GameConf.getMaxNumberOfStreets();
    public static final int MIN_FLOOR = 0;
    public static final int MAX_FLOOR = FLOORS_PER_BUILDING;

	private static final long serialVersionUID = 200911261653L;

	private int street;
    private int building;
    private int floor;
    
    public void setStreet(int street) {
        if (street > TOTAL_STREETS 
                || street < 0)
            throw new IncorrectAddressException(street, building, floor);
        this.street = street;
    }

    public void setBuilding(int building) {
        if (building > BUILDINGS_PER_STREET
                || building < 0)
            throw new IncorrectAddressException(street, building, floor);
        this.building = building;
    }

    public void setFloor(int floor) {
        if (floor > FLOORS_PER_BUILDING
                || floor < 0)
            throw new IncorrectAddressException(street, building, floor);
        this.floor = floor;
    }

    public Address(int street, int building, int floor) 
        throws IncorrectAddressException {
        
        if (street > TOTAL_STREETS 
                || building > BUILDINGS_PER_STREET 
                || floor > FLOORS_PER_BUILDING
                || street < 0
                || building < 0
                || floor < 0)
            throw new IncorrectAddressException(street, building, floor); 

        this.street = street;
        this.building = building;
        this.floor = floor;
        
    }
    
    @Override
    public String toString() {
        return "Address(" + street + ", " + building + ", " + floor + ")";
    }

	public int getStreet() {
		return street;
	}

	public int getBuilding() {
		return building;
	}

	public int getFloor() {
		return floor;
	}
	
	@Override
    public boolean equals(Object o) {
		Address address = (Address) o;
		return
			this.getStreet() == address.getStreet() &&
			this.getBuilding() == address.getBuilding() &&
			this.getFloor() == address.getFloor();
	}

    @Override
    public int compareTo(Address other) {
        return uniqueValue().compareTo(other.uniqueValue());
    }
    
    private Integer uniqueValue() {
        // Añadiendo uno al máximo de elementos permite que no influya
        // el comienzo de las direcciones en 0,0,0 o en 1,1,1 en la
        // unicidad del valor.
        return street * (BUILDINGS_PER_STREET+1) * (FLOORS_PER_BUILDING+1)  
        + building * (FLOORS_PER_BUILDING+1) 
        + floor;
    }

}
