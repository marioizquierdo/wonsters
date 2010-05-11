package es.engade.thearsmonsters.model.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Clase de ayuda para simplificar el manejo de Fechas
 */
public class DateTools {

	/**
     * Devuelve un objeto Date que contiene como fecha el día actual.
     */
	public static Date now() {
		return new Date();
	}
	
	/**
     * Devuelve un objeto Date que contiene como fecha un día despues al momento de la llamada.
     */
	public static Date tomorrow() {
		long millisecondsAsDay = 24 * 60 * 60 * 1000;
	    Date t = new Date(); // now
	    //Obtengo los milisegundos que han pasado hasta el día de hoy
	    long milisecondsActually = t.getTime();
	    //establezco la fecha a mañana.
	    t.setTime(milisecondsActually + millisecondsAsDay); 
	    return t;
	}
	
	/**
     * Devuelve un objeto Date que contiene como fecha un día anterior al momento de la llamada.
     */
	public static Date yesterday() {
		//Obtengo los milisegundos que consituyen un día
		long millisecondsAsDay = 24 * 60 * 60 * 1000;
		//Creo una nueva date que se inicializa al momento actual
	    Date t = new Date();
	    //Obtengo los milisegundos que han pasado hasta el día de hoy
	    long milisecondsActually = t.getTime();
	    //establezco la fecha al dia anterior
	    t.setTime(milisecondsActually  - millisecondsAsDay); 
	    return t;
	}
	
	/**
     * Devuelve el número de milisegundos que existen entre dos fechas representadas con Calendar.
     */
	public static long distanceInMilliseconds(Calendar c1,Calendar c2){
		return (c2.getTimeInMillis() - c1.getTimeInMillis());
	}

	/**
     * Devuelve el número de milisegundos que existen entre dos fechas representadas con Date.
     */
	public static long distanceInMilliseconds(Date d1,Date d2){
		return (d2.getTime() - d1.getTime());
	}
	
	/**
     * Devuelve el número de días que existen entre dos fechas representadas con Calendar.
     */
	public static Float  distanceInDays(Calendar c1,Calendar c2){
		long millisecondsAsDay = 24 * 60 * 60 * 1000;
		long milliseconds = DateTools.distanceInMilliseconds(c1, c2);
		float days = milliseconds / millisecondsAsDay;
		return days;
	}
	
	/**
     * Devuelve el número de días que existen entre dos fechas representadas con Date.
     */
	public static Float distanceInDays(Date c1, Date c2){
        long millisecondsPerDay = 24 * 60 * 60 * 1000;
        long milliseconds = c2.getTime() - c1.getTime();
        float days = milliseconds / millisecondsPerDay;
        return days;
    }
	
	
	
	/**
	 * Compara los Calendar teniendo en cuenta que cualquiera de ellos
	 * puede ser nulo. (Si ambos son nulos, se consideran iguales).
	 * No tiene en cuenta los milisegundos, ya que en la base de datos, se almacenan con una precisión de segundos.
	 */
	public static boolean equals(Calendar c1, Calendar c2) {
		if(c1==null) {
			if(c2==null) return true;
			return false;
		} else {
			if(c2==null) return false;
			return c1.getTimeInMillis()/1000 == c2.getTimeInMillis()/1000;
		}
	}
	
	/**
     * Compara dos Date teniendo en cuenta que cualquiera de ellos
     * puede ser nulo. (Si ambos son nulos, se consideran iguales).
     * No tiene en cuenta los milisegundos, ya que en la base de datos, se almacenan con una precisión de segundos.
     */
    public static boolean equals(Date c1, Date c2) {
        if(c1==null) {
            if(c2==null) return true;
            return false;
        } else {
            if(c2==null) return false;
            return c1.compareTo(c2)==0;
        }
    }
	
	/**
	 * Representa el Date en un formato legible, teniendo en cuenta que puede ser Null.
	 */
	public static String toString(Date d) {
		if(d==null) return "null";
		return d.toString();
	}
}
