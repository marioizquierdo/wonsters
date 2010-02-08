package es.engade.thearsmonsters.model.util;

import java.util.Calendar;

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
    FIRST_TURN_EPOCH_PARAMETER = "GameConf.firstTurnEpoch";
    
    public final static String
    MAX_EGGS_PARAMETER = "GameConf.maxEggs";
    
    private static int MaxNumberOfFloors;
    private static int MaxNumberOfBuildings;
    private static int MaxNumberOfStreets;
    private static byte TurnsPerDay;
    private static long FirstTurnEpoch;
    private static int MaxEggs;
    private static RuntimeException error = null;
    
    static { // Initialize configuration
        try {
			MaxNumberOfFloors = Integer.parseInt(ConfigurationParametersManager.getParameter(
					FLOORS_PER_BUILDING_PARAMETER));
			MaxNumberOfBuildings = Integer.parseInt(ConfigurationParametersManager.getParameter(
					BUILDINGS_PER_STREET_PARAMETER));
			MaxNumberOfStreets = Integer.parseInt(ConfigurationParametersManager.getParameter(
					TOTAL_STREETS_PARAMETER));
			TurnsPerDay = Byte.parseByte(ConfigurationParametersManager.getParameter(
					TURNS_PER_DAY_PARAMETER));
			FirstTurnEpoch = Long.parseLong(ConfigurationParametersManager.getParameter(
					FIRST_TURN_EPOCH_PARAMETER));
			MaxEggs =  Integer.parseInt(ConfigurationParametersManager.getParameter(
					MAX_EGGS_PARAMETER));
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
	 * Turnos que hay en un día de juego
	 */
	public static byte getTurnsPerDay() {
    	checkError();
		return TurnsPerDay;
	}
	
	/**
	 * En que dia de juego estamos, teniendo en cuenta el turno
	 * y el numero de turnos por dia.
	 */
	public static long getGameDay(long turn) {
		return turn / getTurnsPerDay();
	}
	
	/**
	 * Dado un turno cualquiera, saber a que turno del dia
	 * (de 0 a getTurnsPerDay()) se corresponde.
	 * @return
	 */
	public static byte getDayTurn(long turn) {
		return (byte) (turn % getTurnsPerDay());
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
	public static Calendar getFirstTurnDate() {
    	Calendar date = Calendar.getInstance();
    	date.setTimeInMillis(getFirstTurnEpoch());
		return date;
	}
	
	//DIEGO: Este método afecta a RoomData.isReadyToChangeResource(),
	// RoomData.setLastChangeResourcesTurnToNos()
	// y a OnlyOneChangePerDayException
	//
	// Lo dejo así de momento...

	/**
	 * Devuelve el turno actual de juego
	 */
	public static long getCurrentTurn() {
		return 0; //toTurn(Calendar.getInstance());
	}
	
    /**
     * Número máximo de huevos permitidos que puede almacenar cada jugador
     */
    public static int getMaxEggs() {
    	checkError();
		return MaxEggs;
	}
	
	/**
	 * Compruena que un turno del día de juego es correcto.
	 * Los turnos se cuentan desde cero hasta el total (getTurnsPerDay()) menos uno.
	 * @throws Exception si el turno es incorrecto o si hay errores en la configuración. 
	 */
	public static void checkDayTurn(byte dayTurn) {
		if(dayTurn < 0 || dayTurn >= getTurnsPerDay()) 
			throw new IndexOutOfBoundsException("Expected turn in [0 .. "+(getTurnsPerDay()-1)+"], but turn is "+ dayTurn);
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
