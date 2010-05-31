package es.engade.thearsmonsters.model.entities.lair;

import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.util.GameConf;

public class Address {

    public static final int FIRST_FLOOR = 0;
    public static final int FIRST_BUILDING = 0;
    public static final int FIRST_STREET = 0;
    public static final int LAST_FLOOR = GameConf.getMaxNumberOfFloors() - 1;
    public static final int LAST_BUILDING = GameConf.getMaxNumberOfBuildings() - 1;
    public static final int LAST_STREET = GameConf.getMaxNumberOfStreets() - 1;
    
    private int street;
    private int building;
    private int floor;
    
    public int getStreet() {
        return street;
    }

    public int getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public Address(int street, int building, int floor) {
        this.street = street;
        this.building = building;
        this.floor = floor;
    }
    
    public static Address nextAddress(int street, int building, int floor) throws FullPlacesException {

        int nFloor = floor;
        int nBuilding = 0;
        int nStreet = 0;
        
        if (!checkFloor(nFloor+1)) {
            nFloor = 0;
            nBuilding = 1;
        } else {
            nFloor ++;
        }
        nBuilding += building;
        if (!checkBuilding(nBuilding)) {
            nBuilding = nStreet = 1;
        }
        //TODO: En esta implementación temporal, esto puede desbordar!
        nStreet += street;
        if (nStreet > LAST_STREET) {
            throw new FullPlacesException();
        }
            
        return new Address(nStreet, nBuilding, nFloor);
        
    }
    
    public static Address initialAddress() {
        return new Address(0,0,0);
    }
    
    public static boolean checkAddress(int street, int building, int floor) {
        return (checkStreet(street) &&
                checkBuilding(building) &&
                checkFloor(floor));
    }
    
    public static boolean checkFloor(int floor) {
        return (floor >= FIRST_FLOOR && floor <= LAST_FLOOR);
    }
    
    public static boolean checkBuilding(int building) {
        return (building >= FIRST_BUILDING && building <= LAST_BUILDING);
    }
    
    public static boolean checkStreet(int street) {
        return (street >= FIRST_STREET && street <= LAST_STREET);
    }
    
    public static int nextStreet(int street) {
        int nextStreet = street+1;
        if(nextStreet > LAST_STREET) {
            nextStreet = FIRST_STREET;
        }
        return nextStreet;
    }
    
    public static int previousStreet(int street) {
        int previousStreet = street-1;
        if(previousStreet < FIRST_STREET) {
            previousStreet = LAST_STREET;
        }
        return previousStreet;
    }
    
    public static int nextBuilding(int building) {
        int nextBuilding = building+1;
        if(nextBuilding >= LAST_BUILDING) {
            nextBuilding = FIRST_BUILDING;
        }
        return nextBuilding;
    }
    
    public static int previousBuilding(int building) {
        int previousBuilding = building-1;
        if(previousBuilding < FIRST_BUILDING) {
            previousBuilding = LAST_BUILDING;
        }
        return previousBuilding;
    }
    
    public static int nextBuildingStreet(int building, int street) {
        if(building >= LAST_BUILDING) {
            return nextStreet(street);
        } else {
            return street;
        }
    }
    
    public static int previousBuildingStreet(int building, int street) {
        if(building <= FIRST_BUILDING) {
            return previousStreet(street);
        } else {
            return street;
        }
    }
}
