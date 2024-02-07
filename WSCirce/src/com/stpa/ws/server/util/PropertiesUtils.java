package com.stpa.ws.server.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import com.stpa.ws.server.exception.StpawsException;

public class PropertiesUtils {
	
	private static String FICHERO_CONFIG = "com.stpa.ws.server.configuracion.messages";
	private static final HashMap<String, Properties> storedProp = new HashMap<String, Properties>();

	static {
		try {
			cargarProperties(FICHERO_CONFIG);
		} catch (StpawsException e) {
			com.stpa.ws.server.util.Logger.error(e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
	}

	public static void cargarProperties(String rutaFicheroProperties) throws StpawsException {
		cargarProperties(rutaFicheroProperties, null);
	}

	@SuppressWarnings("unchecked")
	public static synchronized void cargarProperties(String rutaFicheroProperties, Locale locale) throws StpawsException {
		String key = rutaFicheroProperties;
		if (!(storedProp.containsKey(key))) {
			com.stpa.ws.server.util.Logger.debug("Cargando propiedades del fichero: " + rutaFicheroProperties, com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			PropertyResourceBundle propertyRB = null;
			Properties tablaProperties = new Properties();
			try {
				if (locale == null) {
					propertyRB = (PropertyResourceBundle) PropertyResourceBundle.getBundle(rutaFicheroProperties);
				} else {
					propertyRB = (PropertyResourceBundle) PropertyResourceBundle.getBundle(rutaFicheroProperties, locale);
				}
			} catch (MissingResourceException me) {
				com.stpa.ws.server.util.Logger.error(me.getMessage(),me,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}

			Enumeration keys = propertyRB.getKeys();
			String aKey = null;

			while (keys.hasMoreElements()) {
				aKey = (String) keys.nextElement();
				tablaProperties.put(aKey, propertyRB.getString(aKey));
			}
			storedProp.put(key, tablaProperties);
		}
	}

	@SuppressWarnings("unchecked")
	public static Hashtable getCacheProperties(String rutaFicheroProperties) {
		return getCacheProperties(rutaFicheroProperties, null);
	}

	@SuppressWarnings("unchecked")
	public static Hashtable getCacheProperties(String rutaFicheroProperties, Locale locale) {
		String key = rutaFicheroProperties;
		if (locale != null)
			key = key + locale.getLanguage();
		if (storedProp.containsKey(key))
			return ((Hashtable) storedProp.get(key).clone());

		return null;
	}

	public static String getValorConfiguracion(String rutaFicheroProperties, String keyValorConfiguracion) throws StpawsException {
		return getValorConfiguracion(rutaFicheroProperties, keyValorConfiguracion, null);
	}
	
	public static String getValorConfiguracion(String keyValorConfiguracion) throws StpawsException {
		return getValorConfiguracion(FICHERO_CONFIG, keyValorConfiguracion, null);
	}

	public static String getValorConfiguracion(String rutaFicheroProperties, String keyValorConfiguracion, Locale locale) throws StpawsException {
		String key = rutaFicheroProperties;
		if (!(storedProp.containsKey(key)))
			cargarProperties(key, locale);
		return storedProp.get(key).getProperty(keyValorConfiguracion);
	}
	
	public static String getValorConfiguracionLang(String rutaFicheroProperties, String keyValorConfiguracion, Locale locale) throws StpawsException {
		return getValorConfiguracion(rutaFicheroProperties, keyValorConfiguracion, locale);
	}
}
