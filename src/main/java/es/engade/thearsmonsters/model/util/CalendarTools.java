package es.engade.thearsmonsters.model.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Clase de ayuda para simplificar el manejo de Calendars.
 */
public class CalendarTools {

	public static Calendar now() {
		return Calendar.getInstance();
	}
	
	public static Calendar tomorrow() {
		Calendar t = Calendar.getInstance();
		t.add(Calendar.DAY_OF_MONTH, 1);
		return t;
	}
	
	public static Calendar yesterday() {
		Calendar t = Calendar.getInstance();
		t.add(Calendar.DAY_OF_MONTH, -1);
		return t;
	}
	
	public static long distanceInMilliseconds(Calendar c1,Calendar c2){
		return (c2.getTimeInMillis() - c1.getTimeInMillis());
	}
	
	public static Float  distanceInDays(Calendar c1,Calendar c2){
		long millisecondsAsDay = 24 * 60 * 60 * 1000;
		long milliseconds = CalendarTools.distanceInMilliseconds(c1, c2);
		float days = milliseconds / millisecondsAsDay;
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
            return c1.getTime()/1000 == c2.getTime()/1000;
        }
    }
	
	/**
	 * Representa el Calendar en un formato legible, teniendo en cuenta que puede ser Null.
	 */
	public static String toString(Calendar c) {
		if(c==null) return "null";
		int h = c.get(Calendar.HOUR_OF_DAY);
		int m = c.get(Calendar.MINUTE);
		int s = c.get(Calendar.SECOND);
		int d = c.get(Calendar.DAY_OF_YEAR);
		return "day "+d+","+h+":"+m+":"+s;
	}
}
