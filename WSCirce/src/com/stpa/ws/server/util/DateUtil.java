package com.stpa.ws.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.validation.CirceValidation;

public class DateUtil {

	// Esta constante es usada como valor por defecto en algunos portlets
	public static final String FORMATO_RECUPERADO_FORMULARIOS_WEB = "dd/MM/yyyy";
	public static final String FORMATO_RECUPERADO_DIA = "dd";
    public static final String FORMATO_RECUPERADO_MES = "MM";
    public static final String FORMATO_RECUPERADO_ANYO = "yyyy";
    // Constantes setter objetos fecha FPF Modelo 600 (ver FormBeanModelo600)
    public static final String constante_diaFecha_FormBeanModelo600 = "diaFecha";
    public static final String constante_mesFecha_FormBeanModelo600 = "mesFecha";
    public static final String constante_anyoFecha_FormBeanModelo600 = "anyoFecha";

	public static String getFechaUHoraAsDateAplazamiento(String fechaUHoraAsString) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("Ini getFechaUHoraAsDateAplazamiento(String fechaUHoraAsString): fechaUHoraAsString: " + fechaUHoraAsString,Logger.LOGTYPE.APPLOG);
		//String fecha final
		String fechaAplazadaformatedAsString = null;
		//Datos base fecha
		int fechaDiaPosterior = 20;
		int fechaDiaBase = 5;
		int fechaMesIncrementoOpcion13 = 13;
		int fechaMesIncrementoOpcion12 = 12;
		//Trabajando fecha		
		SimpleDateFormat formateador = new SimpleDateFormat(FORMATO_RECUPERADO_FORMULARIOS_WEB);
		formateador.setLenient(false);
		Date fechaUHoraAsDate = null;
		
		//Objeto Calendar a trabajar.
		Calendar calendario = Calendar.getInstance();
		Calendar calendarioDiaPosterior = Calendar.getInstance();
		Calendar calendarioDiaBase = Calendar.getInstance();
				
		try {
			fechaUHoraAsDate = formateador.parse(fechaUHoraAsString);
			calendario.setTime(fechaUHoraAsDate);
			//Fechas formateadas
			calendarioDiaPosterior.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), fechaDiaPosterior);
			calendarioDiaBase.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), fechaDiaBase);
			
			if(calendario.after(calendarioDiaPosterior)) {			
				calendario.add(Calendar.MONTH, +fechaMesIncrementoOpcion13);
				calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), fechaDiaBase);				
			} else if(calendario.after(calendarioDiaBase)) {			
				calendario.add(Calendar.MONTH, +fechaMesIncrementoOpcion12);
				calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), fechaDiaPosterior);				
			} else {
				calendario.add(Calendar.MONTH, +fechaMesIncrementoOpcion12);
				calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), fechaDiaBase);				
			}
			//fecha final.
			fechaAplazadaformatedAsString = formateador.format(calendario.getTime());		
			
		} catch (Throwable te) {
			// No se pudo parsear a un Date.Ese string no representa una fecha.
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.fecha.no.valida") + "-Error:" + te.getMessage(),Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen") , te);
		}
		
		// Retorno de fecha Aplazamiento
		com.stpa.ws.server.util.Logger.debug("Fin getFechaUHoraAsDateAplazamiento(String fechaUHoraAsString): fechaAplazadaformatedAsString: " + fechaAplazadaformatedAsString,Logger.LOGTYPE.APPLOG);
		return fechaAplazadaformatedAsString;
		
	}
	
	public static String getFechaAsFormatString(String fechaUHoraAFormatear, String formatoEntrada, String formatoDeSalida) {
		SimpleDateFormat formateador = new SimpleDateFormat(formatoEntrada);
		formateador.setLenient(false);
		String fechaUHoraFormateada = null;
		try {
			Date fechaUHora = formateador.parse(fechaUHoraAFormatear);
			formateador.applyPattern(formatoDeSalida);
			fechaUHoraFormateada = formateador.format(fechaUHora);
		} catch (Exception e) {
			fechaUHoraFormateada = null;
		}
		return fechaUHoraFormateada;
	}

	public static Date getFechaUHoraAsDate(String fechaUHoraAsString, String formatoEntradaFechaUHora) {
		SimpleDateFormat formateador = new SimpleDateFormat(formatoEntradaFechaUHora);
		formateador.setLenient(false);
		Date fechaUHoraAsDate = null;
		try {
			fechaUHoraAsDate = formateador.parse(fechaUHoraAsString);
		} catch (Exception e) {
			// No se pudo parsear a un Date.Ese string no representa una fecha.
			fechaUHoraAsDate = null;
		}
		return fechaUHoraAsDate;
	}

	public static Date getFechaUHoraAsDate(String fechaUHoraAsString) {
		SimpleDateFormat formateador = new SimpleDateFormat(FORMATO_RECUPERADO_FORMULARIOS_WEB);
		formateador.setLenient(false);
		Date fechaUHoraAsDate = null;
		try {
			fechaUHoraAsDate = formateador.parse(fechaUHoraAsString);
		} catch (Exception e) {
			// No se pudo parsear a un Date.Ese string no representa una fecha.
			fechaUHoraAsDate = null;
		}
		return fechaUHoraAsDate;
	}

	public static Calendar getFechaUHoraAsCalendar(String fechaUHoraAsString, String formatoEntradaFechaUHora) {
		SimpleDateFormat formateador = new SimpleDateFormat(formatoEntradaFechaUHora);
		formateador.setLenient(false);
		Calendar fechaUHoraAsCalendar;
		try {
			Date fechaUHoraAsDate = formateador.parse(fechaUHoraAsString);
			fechaUHoraAsCalendar = Calendar.getInstance();
			fechaUHoraAsCalendar.setTime(fechaUHoraAsDate);
		} catch (Exception e) {
			fechaUHoraAsCalendar = null;
		}
		return fechaUHoraAsCalendar;
	}

	public static String getTodayAsString(String formatoSalidaFechaUHora) {
		Date today = new Date();
		return getDateAsFormatString(today, formatoSalidaFechaUHora);
	}

	public static String getDateAsFormatString(Date fechaAsDate, String formatoSalidaFechaUHora) {
		SimpleDateFormat formateador = new SimpleDateFormat(formatoSalidaFechaUHora);
		formateador.setLenient(false);
		return formateador.format(fechaAsDate);
	}

	public static boolean isFecha(String fecha) {
		// Como no le indica el formato de la fecha usamos el valor por defecto
		if (DateUtil.getFechaUHoraAsDate(fecha, DateUtil.FORMATO_RECUPERADO_FORMULARIOS_WEB) == null) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNotFecha(String fecha) {
		return !isFecha(fecha);
	}

	public static boolean isFechaPasada(String fecha) {
		// Como no le indica el formato de la fecha usamos el valor por defecto
		Date fechaAsDate = DateUtil.getFechaUHoraAsDate(fecha, DateUtil.FORMATO_RECUPERADO_FORMULARIOS_WEB);
		Date today = new Date();
		if (fechaAsDate == null || fechaAsDate.compareTo(today) >= 0)
			return false;
		return true;
	}

	public static boolean isFechaPasadaPresente(String fecha) {
		// Como no le indica el formato de la fecha usamos el valor por defecto
		Date fechaAsDate = DateUtil.getFechaUHoraAsDate(fecha, DateUtil.FORMATO_RECUPERADO_FORMULARIOS_WEB);
		Date today = new Date();
		if (fechaAsDate == null || fechaAsDate.compareTo(today) > 0) {
			return false;
		}
		return true;
	}
	/**
	 * Devuelve la fecha actual en formato "dd de MMMM de yyyyy"
	 * @return Fecha en formato "dd de MMMM de yyyyy"
	 */
	public static String fechaSalida(){
		
		Locale lCastellano = new Locale("ES","es");
		SimpleDateFormat formateadorMes = new SimpleDateFormat("MMMMM", lCastellano);
		formateadorMes.setLenient(false);		
		Calendar calendario = Calendar.getInstance();
		String fechaSalida = calendario.get(java.util.Calendar.DATE) + " de " + StringUtils.capitalize(formateadorMes.format(calendario.getTime())) + " de " + calendario.get(java.util.Calendar.YEAR);
		
		//Retorno.
		return fechaSalida;
	}
	/**
	 * Retorna objeto Calendar de un String con el formato dd/MM/yyyy
	 * @param fecha fecha con formato de entrada válida dd/MM/yyyy
	 * @return Retorna fecha en formato Calendar
	 * @throws StpawsException Error controlado del sistema para el parseo de Fecha con formato distinto al de dd/MM/yyyy
	 */
	public static Calendar getFecha(String fecha) throws StpawsException {
		//Trabajando fecha		
		SimpleDateFormat formateador = new SimpleDateFormat(FORMATO_RECUPERADO_FORMULARIOS_WEB);
		formateador.setLenient(false);
		Date fechaUHoraAsDate = null;
		
		//Objeto Calendar a trabajar.
		Calendar calendario = Calendar.getInstance();
				
	
			try {
				fechaUHoraAsDate = formateador.parse(fecha);
				calendario.setTime(fechaUHoraAsDate);
			} catch (ParseException e) {
				com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.fecha.no.valida") + "-Error parseo de fecha de entrada; getFecha(String fecha): " + fecha, e,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
			}
			
			return calendario;
	}
	
	/**
	 * Devolvera en formato Calendar la fecha String dd/MM/yyyy introducida desde la Solicitud CIRCE
	 * @param obj objeto con la fecha
	 * @param fecha Fecha en formato String dd/MM/yyyy
	 * @throws StpawsException Error procedente del parseo del String controlado desde el Sistema CIRCE
	 */
	public static void setFechasDia_Mes_Anyo(String fecha, Object obj) throws StpawsException {
		if (!CirceValidation.isEmpty(fecha)) {
			Calendar calendario = DateUtil.getFecha(fecha);		
			
			SimpleDateFormat formatoDia = new SimpleDateFormat(DateUtil.FORMATO_RECUPERADO_DIA);
	        SimpleDateFormat formatoMes = new SimpleDateFormat(DateUtil.FORMATO_RECUPERADO_MES);
	        SimpleDateFormat formatoAnyo = new SimpleDateFormat(DateUtil.FORMATO_RECUPERADO_ANYO);
	        StpawsUtil.colocaValoresBeanFecha(obj,constante_diaFecha_FormBeanModelo600,formatoDia.format(calendario.getTime()));
	        StpawsUtil.colocaValoresBeanFecha(obj,constante_mesFecha_FormBeanModelo600,formatoMes.format(calendario.getTime()));
	        StpawsUtil.colocaValoresBeanFecha(obj,constante_anyoFecha_FormBeanModelo600,formatoAnyo.format(calendario.getTime())); 
		}        
	}
	/**
	 * Fracciona el string en tokens unitarios desde la posicion inicial marcada hasta una posición más.
	 * @param fechaAFraccionar String a fraccionar en tokens
	 * @param posicion posicion inicial de fraccionamiento
	 * @return Token fraccionado
	 * @throws StpawsException Error controlado del sistema CIRCE
	 */
	public static String getFechaFraccionada(String fechaAFraccionar, int posicion) throws StpawsException {
		String fechaFraccionada = new String("");
		if (CirceValidation.isEmpty(fechaAFraccionar))
			return "";
		try {
			if (fechaAFraccionar.length() > posicion) {
				fechaFraccionada = fechaAFraccionar.substring(posicion, posicion+1);	
			} 
		} catch(Throwable t) {
			com.stpa.ws.server.util.Logger.error("getFechaFraccionada(String fechaAFraccionar, int posicion): Error parseo fecha." + fechaAFraccionar, t,Logger.LOGTYPE.APPLOG);
		}
		
		return fechaFraccionada;
	}
	/**
     * Metodo que retorna la fecha y hora actuales.
     * @return Array de String con fecha y hora actual.
     */
    public static String[] getFechaUHoraActual() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String[] fechaHora = {"",""};

        try {
            fechaHora[0] = dateFormat.format(calendar.getTime());
            fechaHora[1] = timeFormat.format(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Retorno Fecha y hora actual
        return fechaHora;
    }
}
