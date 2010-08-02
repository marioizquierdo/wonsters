package es.engade.thearsmonsters.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de ayuda para simplificar el manejo de Fechas
 */
public class DateTools {
	
	public static long MILLISECONDS_PER_SECOND = 1000;
	public static long MILLISECONDS_PER_MINUTE = 60 * 1000;
	public static long MILLISECONDS_PER_HOUR = 60 * 60 * 1000;
	public static long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

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
	 * Añadir milisegundos a la fecha (la modifica)
	 */
	public static Date addMillisecondsTo(Date date, long milliseconds) {
		date.setTime(date.getTime() + milliseconds);
		return date;
	}

	/**
	 * Añadir segundos a la fecha (la modifica)
	 */
	public static Date addSecondsTo(Date date, long seconds) {
		date.setTime(date.getTime() + seconds);
		return addMillisecondsTo(date, seconds * MILLISECONDS_PER_SECOND);
	}

	/**
	 * Añadir minutos a la fecha (la modifica)
	 */
	public static Date addMinutesTo(Date date, long minutes) {
		date.setTime(date.getTime() + minutes * MILLISECONDS_PER_MINUTE);
		return date;
	}

	/**
	 * Añadir horas a la fecha (la modifica)
	 */
	public static Date addHoursTo(Date date, long hours) {
		date.setTime(date.getTime() + hours * MILLISECONDS_PER_HOUR);
		return date;
	}

	/**
	 * Añadir días a la fecha (la modifica)
	 */
	public static Date addDaysTo(Date date, long days) {
		date.setTime(date.getTime() + days * MILLISECONDS_PER_DAY);
		return date;
	}
	
	/**
	 * Crea un Date sumando milisegundos a partir de from
	 */
	public static Date new_byMillisecondsFrom(Date from, long milliseconds) {
		Date d = new Date();
		d.setTime(from.getTime() + milliseconds);
		return d;
	}
	public static Date new_byMillisecondsFromNow(long milliseconds) {
		return new_byMillisecondsFrom(now(), milliseconds);
	}
	
	/**
	 * Crea un Date sumando segundos a partir de from
	 */
	public static Date new_bySecondsFrom(Date from, long seconds) {
		
		return new_byMillisecondsFrom(from, seconds * MILLISECONDS_PER_SECOND);
	}
	public static Date new_bySecondsFromNow(long seconds) {
		return new_bySecondsFrom(now(), seconds);
	}
	
	/**
	 * Crea un Date sumando minutos a partir de from
	 */
	public static Date new_byMinutesFrom(Date from, long minutes) {
		return new_byMillisecondsFrom(from, minutes * MILLISECONDS_PER_MINUTE);
	}
	public static Date new_byMinutesFromNow(long minutes) {
		return new_byMinutesFrom(now(), minutes);
	}
	
	/**
	 * Crea un Date sumando horas a partir de from
	 */
	public static Date new_byHoursFrom(Date from, long hours) {
		return new_byMillisecondsFrom(from, hours * MILLISECONDS_PER_HOUR);
	}
	public static Date new_byHoursFromNow(long hours) {
		return new_byHoursFrom(now(), hours);
	}
	
	/**
	 * Fecha obtenida a sumando días a partir de from
	 */
	public static Date new_byDaysFrom(Date from, long days) {
		return new_byMillisecondsFrom(from, days * MILLISECONDS_PER_DAY);
	}
	public static Date new_byDaysFromNow(long days) {
		return new_byDaysFrom(now(), days);
	}
	
	
	
	/**
     * Milisegundos entre la primera y la segunda fecha.
     */
	public static long millisecondsBetween(Date from, Date to){
		return (to.getTime() - from.getTime());
	}
	public static long millisecondsFromNowTo(Date to) {
		return millisecondsBetween(now(), to);
	}
	
	/**
	 * Segundos entre la primera y la segunda fecha
	 */
	public static double secondsBetween(Date from, Date to) {
		return ((double) millisecondsBetween(from, to)) / MILLISECONDS_PER_SECOND;
	}
	public static double secondsFromNowTo(Date to) {
		return secondsBetween(now(), to);
	}
	
	/**
	 * Minutos entre la primera y la segunda fecha
	 */
	public static double minutesBetween(Date from, Date to) {
		return ((double) millisecondsBetween(from, to)) / MILLISECONDS_PER_MINUTE;
	}
	public static double minutesFromNowTo(Date to) {
		return minutesBetween(now(), to);
	}
	
	/**
	 * Minutos entre la primera y la segunda fecha
	 */
	public static double hoursBetween(Date from, Date to) {
		return ((double) millisecondsBetween(from, to)) / MILLISECONDS_PER_HOUR;
	}
	public static double hoursFromNowTo(Date to) {
		return hoursBetween(now(), to);
	}
	
	
	/**
     * Días entre la primera y la segunda fecha
     */
	public static double daysBetween(Date from, Date to) {
		return ((double) millisecondsBetween(from, to)) / MILLISECONDS_PER_DAY;
    }
	public static double daysFromNowTo(Date to) {
		return daysBetween(now(), to);
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
