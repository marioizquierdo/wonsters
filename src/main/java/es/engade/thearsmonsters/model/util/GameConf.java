package es.engade.thearsmonsters.model.util;


import java.util.Date;

import es.engade.thearsmonsters.util.configuration.ConfigurationParametersManager;
import es.engade.thearsmonsters.util.configuration.MissingConfigurationParameterException;

public final class GameConf {
    
    public final static String
    FLOORS_PER_BUILDING_PARAMETER = "GameConf.floorsPerBuilding";
    
    public final static String
    BUILDINGS_PER_STREET_PARAMETER = "GameConf.buildingsPerStreet";
    
    public final static String
    TOTAL_STREETS_PARAMETER = "GameConf.totalStreets";
    
    public final static String
    TURNS_PER_DAY_PARAMETER = "GameConf.turnsPerDay";
    
    public final static String
    MAX_TURNS_PARAMETER = "GameConf.maxTurns";
    
    public final static String
    FIRST_TURN_EPOCH_PARAMETER = "GameConf.firstTurnEpoch";
    
    public final static String
    MAX_EGGS_PARAMETER = "GameConf.maxEggs";
    
    public final static String
    LAIRS_RANKING_DEPTH_PARAMETER = "GameConf.lairsRankingDepth";
    
    private static int MaxNumberOfFloors;
    private static int MaxNumberOfBuildings;
    private static int MaxNumberOfStreets;
    private static byte TurnsPerDay;
    private static byte MaxNumberOfTurns;
    private static long FirstTurnEpoch;
    private static int MaxEggs;
    private static int LairsRankingDepth;
    private static RuntimeException error = null;
    
    static { // Initialize configuration
        try {
			MaxNumberOfFloors = Integer.parseInt(ConfigurationParametersManager.getParameter(
					FLOORS_PER_BUILDING_PARAMETER));
			MaxNumberOfBuildings = Integer.parseInt(ConfigurationParametersManager.getParameter(
					BUILDINGS_PER_STREET_PARAMETER));
			MaxNumberOfStreets = Integer.parseInt(ConfigurationParametersManager.getParameter(
					TOTAL_STREETS_PARAMETER));
			MaxNumberOfTurns = Byte.parseByte(ConfigurationParametersManager.getParameter(
					MAX_TURNS_PARAMETER));
			TurnsPerDay = Byte.parseByte(ConfigurationParametersManager.getParameter(
					TURNS_PER_DAY_PARAMETER));
			FirstTurnEpoch = Long.parseLong(ConfigurationParametersManager.getParameter(
					FIRST_TURN_EPOCH_PARAMETER));
			MaxEggs =  Integer.parseInt(ConfigurationParametersManager.getParameter(
					MAX_EGGS_PARAMETER));
			LairsRankingDepth = Integer.parseInt(ConfigurationParametersManager.getParameter(
					LAIRS_RANKING_DEPTH_PARAMETER));
		} catch (NumberFormatException e) {
			error = e;
		} catch (MissingConfigurationParameterException e) {
			error = new RuntimeException(e);
		}
    }
    private GameConf () {}
    


    /**
     * Número de pisos en un edificio
     */
	public static int getMaxNumberOfFloors() {
    	checkError();
		return MaxNumberOfFloors;
	}

	/**
	 * Número de edificios en una calle
	 */
	public static int getMaxNumberOfBuildings() {
    	checkError();
		return MaxNumberOfBuildings;
	}

	/**
	 * Número de calles en el juego
	 */
	public static int getMaxNumberOfStreets() {
    	checkError();
		return MaxNumberOfStreets;
	}
	
	/**
	 * Turnos acumulables
	 */
	public static byte getMaxNumberOfTurns() {
    	checkError();
		return MaxNumberOfTurns;
	}
	
	/**
	 * Turnos que hay en un día de juego
	 */
	public static byte getTurnsPerDay() {
    	checkError();
		return TurnsPerDay;
	}
	
	/**
	 * Unix epoch (milisegundos desde 1970) del primer turno de juego
	 */
	public static long getFirstTurnEpoch() {
    	checkError();
		return FirstTurnEpoch;
	}
	
	/**
	 * Fecha del momento en el que comenzó el primer turno de juego
	 */
	public static Date getFirstTurnDate() {
    	Date date = new Date();
    	date.setTime(getFirstTurnEpoch());
		return date;
	}
	
    /**
     * Número máximo de huevos permitidos que puede almacenar cada jugador
     */
    public static int getMaxEggs() {
    	checkError();
		return MaxEggs;
	}
    
    /**
     * Profundidad del ranking de Lairs
     */
    public static int getLairsRankingDepth() {
    	checkError();
    	return LairsRankingDepth;
    }
    
	

	/**
	 * Comprueba que se hayan leido correctamente los parámetros
	 * @throws RuntimeException if there are any error reading the parameters
	 * 		when the aplication run at first time. 
	 */
	private static void checkError() {
		if(error!=null) throw error;
	}

}
