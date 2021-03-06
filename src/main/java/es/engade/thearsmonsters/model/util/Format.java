package es.engade.thearsmonsters.model.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Format {
	
	private final static String DATE_FORMAT = "yyyy:MM:dd:HH:mm:ss";
	private final static String P_SEP = ", ";
	
	/**
	 * Convierte un date a un String legible.
	 */
	public static String date(Date date) {
		return DateTools.toString(date);
	}
	
	/**
	 * Convierte un String en formato "yyyy:MM:dd:HH:mm:ss" a un Calendar equivalente.
	 * @throws IllegalArgumentException si el formato de dateStr es incorrecto.
	 */
	public static Date date(String dateStr) {
		if(dateStr == null) { return null; }
		try {
			Date date;
			DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			date = formatter.parse(dateStr);
			return date;
		} catch (ParseException e) {
			throw new IllegalArgumentException("dateStr is "+dateStr+
					", and must fit in the format yyyy:MM:dd:HH:mm:ss.", e);
		}

		
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
				Date date = Date.class.cast(obj);
				pair = p(key, date);
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
	 * Convierte un array en un conjunto. Los elementos del conjunto ser�n
	 * del mismo tipo que el array (clase especificada)
	 */
	public static <klass> Set<klass> set(klass[] array) {
		Set<klass> set = new HashSet<klass>();
		for(klass o: array) {
			set.add(o);
		}
		return set;
	}
	
	/**
	 * Convierte un array en una ArrayList. Los elementos de la lista ser�n
	 * del mismo tipo que el array (clase especificada)
	 */
	public static <klass> List<klass> list(klass[] array) {
		List<klass> list = new ArrayList<klass>();
		for(klass o: array) {
			list.add(o);
		}
		return list;
	}
	
	private static String p(String key, Date value) {
		return p(key, Format.date(value));
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
	
	
	/**
	 * Cast a double value into a integer and
	 * round to zero the less significant digits.
	 */
	public static int roundValue(double value) {
		int v = (int) value;
		if(v > 100000000) {
			v = (v/1000000) * 1000000;
		} else if(v > 100000) {
			v = (v/1000) * 1000;
		} else if(v > 10000) {
			v = (v/100) * 100;
		} else if(v > 1000) {
			v = (v/10) * 10;
		}
		return v;
	}
}
