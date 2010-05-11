package es.engade.thearsmonsters.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de ayuda para simplificar el manejo de Fechas
 */
public class DateTools {

	/**
     * Devuelve un objeto Date que representa el momento de la llamada.
     */
	public static Date now() {
		return new Date();
	}
	
	public static Date tomorrow() {
		return DateTools.new_byDaysFromNow(1);
	}
	
	public static Date yesterday() {
		return DateTools.new_byDaysFromNow(-1);
	}
	
	public static Date nextWeek() {
		return DateTools.new_byDaysFromNow(7);
	}
	
	public static Date nextMonth() {
		return DateTools.new_byDaysFromNow(30); // Puede no ser exacto para los meses de 31 dias
	}
	
	/**
	 * Fecha obtenida a sumando días a partir de ahora
	 */
	public static Date new_byMillisecondsFromNow(long milliseconds) {
		Date d = DateTools.now();
		d.setTime(d.getTime() + milliseconds);
		return d;
	}
	
	/**
	 * Fecha obtenida a sumando días a partir de ahora
	 */
	public static Date new_bySecondsFromNow(long seconds) {
		long millisecondsPerSecond = 60 * 1000;
		return new_byMillisecondsFromNow(seconds * millisecondsPerSecond);
	}
	
	/**
	 * Fecha obtenida a sumando días a partir de ahora
	 */
	public static Date new_byMinutesFromNow(long minutes) {
		long millisecondsPerMinute = 60 * 1000;
		return new_byMillisecondsFromNow(minutes * millisecondsPerMinute);
	}
	
	/**
	 * Fecha obtenida a sumando días a partir de ahora
	 */
	public static Date new_byHoursFromNow(long hours) {
		long millisecondsPerHour = 60 * 60 * 1000;
		return new_byMillisecondsFromNow(hours * millisecondsPerHour);
	}
	
	/**
	 * Fecha obtenida a sumando días a partir de ahora
	 */
	public static Date new_byDaysFromNow(long days) {
		long millisecondsPerDay = 24 * 60 * 60 * 1000;
		return new_byMillisecondsFromNow(days * millisecondsPerDay);
	}
	
	/**
     * Milisegundos entre la primera y la segunda fecha.
     */
	public static long distanceInMilliseconds(Date from, Date to){
		return (to.getTime() - from.getTime());
	}
	public static long distanceInMillisecondsFromNow(Date to) {
		return distanceInMilliseconds(DateTools.now(), to);
	}
	
	/**
	 * Segundos entre la primera y la segunda fecha
	 */
	public static Float distanceInSeconds(Date from, Date to) {
		long millisecondsPerSecond = 1000;
		return (float) (distanceInMilliseconds(from, to) / millisecondsPerSecond);
	}
	public static Float distanceInSecondsFromNow(Date to) {
		return distanceInSeconds(DateTools.now(), to);
	}
	
	/**
	 * Minutos entre la primera y la segunda fecha
	 */
	public static Float distanceInMinutes(Date from, Date to) {
		long millisecondsPerMinute = 60 * 1000;
		return (float) (distanceInMilliseconds(from, to) / millisecondsPerMinute);
	}
	public static Float distanceInMinutesFromNow(Date to) {
		return distanceInMinutes(DateTools.now(), to);
	}
	
	/**
	 * Minutos entre la primera y la segunda fecha
	 */
	public static Float distanceInHours(Date from, Date to) {
		long millisecondsPerHour = 60 * 60 * 1000;
		return (float) (distanceInMilliseconds(from, to) / millisecondsPerHour);
	}
	public static Float distanceInHoursFromNow(Date to) {
		return distanceInHours(DateTools.now(), to);
	}
	
	
	/**
     * Días entre la primera y la segunda fecha
     */
	public static Float distanceInDays(Date from, Date to) {
        long millisecondsPerDay = 24 * 60 * 60 * 1000;
		return (float) (distanceInMilliseconds(from, to) / millisecondsPerDay);
    }
	public static Float distanceInDaysFromNow(Date to) {
		return distanceInDays(DateTools.now(), to);
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
		DateFormat formatter = new SimpleDateFormat("dd:MM:yyyy");
		return formatter.format(d);
	}
}
