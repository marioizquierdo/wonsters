package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 200911261653L;
	
	private int street;
    private int building;
    private int floor;
    
    public Address(int street, int building, int floor) {
        
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
    

}
