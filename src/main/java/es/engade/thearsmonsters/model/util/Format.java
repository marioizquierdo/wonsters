package es.engade.thearsmonsters.model.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Format {
	
	private final static String DATE_FORMAT = "yyyy:MM:dd:HH:mm:ss";
	private final static String P_SEP = ", ";
	
	/**
	 * Convierte un calendar a un String legible.
	 * @return un Stirng en el formato "yyyy:MM:dd:HH:mm:ss", por ejemplo "2009:10:23:16:32:00"
	 */
	public static String calendar(Calendar date) {
		if(date == null) { return null; };
		SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
		String dateString = f.format(date.getTime());
		return dateString;
	}
	
	/**
	 * Convierte un String en formato "yyyy:MM:dd:HH:mm:ss" a un Calendar equivalente.
	 * @throws IllegalArgumentException si el formato de dateStr es incorrecto.
	 */
	public static Calendar calendar(String dateStr) {
		if(dateStr == null) { return null; }
		try {
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat(DATE_FORMAT);
			date = (Date) formatter.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("dateStr is "+dateStr+
					", and must fit in the format yyyy:MM:dd:HH:mm:ss.", e);
		}

		return null;
	}
	
	/**
	 * Dado un array de parejas (key,value) muestra como resultado cada key junto
	 * con su valor. sirve para hacer toStirng de objetos de manera cómoda, ya que
	 * se encarga de formatear correctamente cada tipo de atributo que recibe.<br>
	 * Ejemplo: Format.p(new Object[]{"name", name, "hitsFrom", hitsFrom, "channelTags", channelTags})
	 */
	public static String p(Object[] a) {
		String result = "";
		for(int i=0; i<a.length-1; i+=2) {
			String key = a[i].toString();
			Object obj = a[i+1];
			String pair;
			try {
				Calendar calendar = Calendar.class.cast(obj);
				pair = p(key, calendar);
			} catch (ClassCastException e) {
				try {
					Object[] array = (new Object[]{}).getClass().cast(obj);
					pair = p(key, array);
				} catch (ClassCastException e2) {
					pair = p(key, obj);
				}
			}
			result += pair;
		}
		return result.substring(0, result.length() - P_SEP.length());
	}
	
	public static String p(Class<?> ownerClass, Object[] attrs) {
		return ownerClass.getSimpleName()+"{"+p(attrs)+"}";
	}
	
	/**
	 * Convierte un array en un conjunto. Los elementos del conjunto serán
	 * del mismo tipo que el array (clase cl especificada)
	 */
	public static <klass> Set<klass> arrayToSet(klass[] array) {
		Set<klass> set = new HashSet<klass>();
		for(klass o: array) {
			set.add(o);
		}
		return set;
	}
	
	private static String p(String key, Calendar value) {
		return p(key, Format.calendar(value));
	}
	
	private static String p(String key, Object value) {
		return p(key, value.toString());
	}
	
	private static String p(String key, String value) {
		if(value == null) {
			return key + ": null " + P_SEP;
		} else {
			return key + ": \"" + value +"\""+ P_SEP;
		}
	}
	
	private static String p(String key, Object[] value) {
		return p(key, Arrays.asList(value));
	}
	
	private static String p(String key, Collection<Object> value) {
		String p = "[";
		Iterator<Object> iterator = value.iterator();
		while(iterator.hasNext()) {
			p += iterator.next().toString();
			if(iterator.hasNext()) {
				p += P_SEP;
			}
		}
		return p + "]";
	}

}
