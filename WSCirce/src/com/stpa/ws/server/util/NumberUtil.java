package com.stpa.ws.server.util;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.lang.StringUtils;

import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.validation.CirceValidation;

public class NumberUtil {

	/**
	 * Reemplaza String de doubles procedentes de servicios con "," to "."
	 * @param porcentaje en formato con "," como separador.
	 * @return porcentaje en formato con "." como separador.
	 */
	public static String formatStringComaToPunto(String porcentaje) {
		if (!CirceValidation.isEmpty(porcentaje)) {
			if (porcentaje.indexOf(",") > 0) {
				porcentaje = porcentaje.replace(",", ".");
			}	
		}
		return porcentaje;	
	}
	/**
	 * Transforma Euros con céntimos a centimos, Ej.: 3006,00 euros a 300600 céntimos de euro.
	 * @param sEuro String Euro con céntimos separados por ",".
	 * @return String de euro en enteros de centimos
	 */
	public static String getEuroInCentsFormat(String sEuro)throws StpawsException{
		
		String formateado;
		String resultado = sEuro;
		int centEuro = 0;
		if (!CirceValidation.isEmpty(sEuro)) {
			try {
				formateado = getCifraToStringInDoubleFormat(sEuro);			
				BigDecimal euro = new BigDecimal(formateado);			
				
				if (euro.scale()>2) {
					resultado = sEuro;
					com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.moneda.no.valida")+ "-Escala incorrecta; getEuroInCentsFormat(String sEuro):" + sEuro,Logger.LOGTYPE.APPLOG);				
					throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
				} else {
					centEuro = euro.multiply(new BigDecimal(100)).intValue();
					resultado = String.valueOf(centEuro);
				}
			} catch (StpawsException stpae) {
				com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.moneda.no.valida") + stpae.getMessage(),stpae,Logger.LOGTYPE.APPLOG);				
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), stpae);
			} catch(Throwable te) {
				com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.moneda.no.valida") + te.getMessage(),te,Logger.LOGTYPE.APPLOG);				
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), te);
			}
		}
		return resultado;
	}
	/**
	 * Reemplaza el String numérico las "," por los "." necesarios para el servicio al que se le envia.
	 * @param sNumero Cifra con "," como separador de céntimo.
	 * @return Cifra con "." como separador de céntimo.
	 */
	
	private static String getCifraToStringInDoubleFormat(String sNumero)throws StpawsException{
		Double numero = null;
		String resultado = sNumero;
		if (!CirceValidation.isEmpty(sNumero)) {
			if (sNumero.indexOf(",") > 0) {
				sNumero = sNumero.replace(",", ".");
			}
			try {
				numero = Double.parseDouble(sNumero);
			} catch (NumberFormatException nfe) {
				com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.moneda.no.valida") + "sNumero:" + sNumero + "-" +  nfe.getMessage(),nfe,Logger.LOGTYPE.APPLOG);				
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), nfe);
			}
			resultado = numero.toString();
		}
		return resultado;
	}
	/**
	 * Este metodo es necesario ya que NumberFormat de java tiene un bug, ya que
	 * NO ES ESTRICTO a la hora de parsear.Por eso se tiene que verificar si los
	 * miles estan agrupados de tres en tres o simplemente no estan agrupados 
	 * @param numeroAEvaluar Numero a validar
	 * @return Es un grupo de miles valido (true).
	 */	
	private static boolean validarGruposMiles(String numeroAEvaluar) {
		DecimalFormatSymbols simbolos = new java.text.DecimalFormatSymbols();
		char charSimboloMiles = simbolos.getGroupingSeparator();
		String simboloMiles = String.valueOf(charSimboloMiles);
		char charSimboloDecimales = simbolos.getDecimalSeparator();
		String simboloDecimales = String.valueOf(charSimboloDecimales);
		if (StringUtils.contains(numeroAEvaluar, simboloMiles)) {
			// tengo que validar que a la derecha de cada simboloMiles
			// exista tres digitos.
			while (numeroAEvaluar.length() > 0) {
				int posicionSimboloMiles = StringUtils.indexOf(numeroAEvaluar, simboloMiles);
				if (posicionSimboloMiles > 0) {
					String posibleGrupoMiles = StringUtils.mid(numeroAEvaluar, posicionSimboloMiles + 1, 3);
					if (StringUtils.contains(posibleGrupoMiles, simboloMiles)) {
						return false;
					} else {
						numeroAEvaluar = StringUtils.substringAfter(numeroAEvaluar, simboloMiles);
					}
				} else {
					numeroAEvaluar = StringUtils.substringBefore(numeroAEvaluar, simboloDecimales);
					if (numeroAEvaluar.length() != 3) {
						return false;
					}
					return true;
				}
			}
		}
		return true;
	}
	/**
	 * Valida si es un numero valido.
	 * @param numeroAEvaluar Numero en formato String a validar.
	 * @return Es un numero valido.
	 */
	public static boolean isNumberValid(String numeroAEvaluar) {
		boolean isValid = true;
		NumberFormat parseador = NumberFormat.getInstance();
		// esto se hace si se quiere setear manualmente los simbolos
		// decimal y miles, pero este metodo isNumberValid ya usa el local por
		// defecto
		// DecimalFormatSymbols simbolos = new java.text.DecimalFormatSymbols();
		// simbolos.setDecimalSeparator(',');
		// simbolos.setGroupingSeparator('.');
		// formateador.setDecimalFormatSymbols(simbolos);

		if (StringUtils.isBlank(numeroAEvaluar)) {
			isValid = false;
		} else {
			if (!validarGruposMiles(numeroAEvaluar)) {
				isValid = false;
			} else {
				ParsePosition posicionador = new ParsePosition(0);
				parseador.parse(numeroAEvaluar, posicionador);
				if ((posicionador.getErrorIndex() > -1) || (posicionador.getIndex() < numeroAEvaluar.length())) {
					isValid = false;
				}
			}
		}
		return isValid;
	}
}
