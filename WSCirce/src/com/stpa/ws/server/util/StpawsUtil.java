package com.stpa.ws.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.stpa.ws.acceso.bean.BeanFicheroSolicitudRespuesta;
import com.stpa.ws.firma.util.ExtraccionYTratamientoFicheros;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Respuesta;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.bean.Datos_STTCirce;
import com.stpa.ws.server.bean.Exenciones;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.validation.CirceValidation;
import com.stpa.ws.server.validation.FirmaHelper;

public class StpawsUtil {

	static int hexcase = 1;
	static int chrsz = 8;
	/**
	 * TIPO_INTERVINIENTE_SUJETOPASIVO = 0
	 */
	public static final int TIPO_INTERVINIENTE_SUJETOPASIVO = 0;
	/**
	 * TIPO_INTERVINIENTE_SUJETOPRESENTADOR = 1;
	 */
	public static final int TIPO_INTERVINIENTE_SUJETOPRESENTADOR = 1;
	/**
	 * TIPO_INTERVINIENTE_DATOSNOTARIALES = 2;
	 */
	public static final int TIPO_INTERVINIENTE_DATOSNOTARIALES = 2;
	/**
	 * TIPO_EXENCION_EXENTO = S;
	 */
	public static final String CONSTANTE_EXENCION = "S";	
	
	/**
	 * Genera la mac con los datos de la peticion y 
	 * comprueba que sea igual a la pasada por parametro
	 * @param p_cliente Parametro de la peticion
	 * @param p_timestamp Parametro de la peticion
	 * @param p_modelo_autoliquidacion Parametro de la peticion
	 * @param p_libre Parametro de la peticion
	 * @param mac Parametro a validar
	 * @return true si es correcta y false si no coincide
	 */
	/*
	public static boolean isMacValid(String param, String mac){
		boolean result = true;
		 asier a revisar
		boolean result = false;
		String s = SHAUtils.hex_sha1(param);
		if(mac!=null && !"".equals(mac) && s!=null && !"".equals(s)){
			if(s.equals(mac)) result = true;
		}
		
		return result;
	}
	*/
	/**D3CD6B19422124933ACB56B4A381E856108FD618
	 * Genera la mac como resultado de la respuesta del ws.
	 * @param p_cliente Parametro de la peticion
	 * @param p_timestamp Parametro de la peticion
	 * @param p_modelo_autoliquidacion Parametro de la peticion
	 * @param p_libre Parametro de la peticion
	 * @param r_timestamp Parametro de la respuesta
	 * @param r_resultado Parametro de la respuesta
	 * @param r_numero_autoliquidacion Parametro de la respuesta
	 * @return La nueva mac generada para esta peticion y respuesta
	 */
	/*
	public static String genMac(String param){		
		return SHAUtils.hex_sha1(param);
	}
	*/
	/**
	 * Time stamper con formato dd/MM/yyyy hh:mm:ss
	 * @return String con fecha en formato dd/MM/yyyy hh:mm:ss
	 */
	public static String getTimeStamp(){
		Calendar ca = new GregorianCalendar();
		return ca.get(Calendar.DAY_OF_MONTH) + "/" + ca.get(Calendar.MONTH) + "/" + ca.get(Calendar.YEAR) + " " + ca.get(Calendar.HOUR) + ":" + ca.get(Calendar.MINUTE) + ":" + ca.get(Calendar.SECOND);
	}
	/**
	 * Método generador de fecha en formato dd/MM/yyyy
	 * @param ca Objeto Calendar a generar en formato dd/MM/yyyy
	 * @return String con formato dd/MM/yyyy
	 */
	public static String calendarToString(Calendar ca){
		return ca.get(Calendar.DAY_OF_MONTH) + "/" + ca.get(Calendar.MONTH) + "/" + ca.get(Calendar.YEAR);
	}
	/**
	 * Campos a verificar para el Servicio validado CIRCE
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto de entrada CIRCE
	 * @return Acceso validado (true) o no (false)
	 * @throws StpawsException Error en el acceso por falta de campos obligatorios
	 */
	public static boolean isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
	
		//Identificacion
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta el getXml().getFichero().getIdentificacion().getTipo_Sociedad().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getId_comunicacion())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta el getXml().getFichero().getIdentificacion().getId_Comunidad().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getId_tramite())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta el getXml().getFichero().getIdentificacion().getId_Tramite().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getDescripcion())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getDescripcion().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getFecha())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getFecha().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getHora())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getHora().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getAcuse())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getAcuse().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getSincronia())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getSincronia().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getSistema_emisor())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getSistema_Emisor().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getSistema_receptor())) {
			com.stpa.ws.server.util.Logger.error("Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getIdentificacion().getSistema_Recepctor().",Logger.LOGTYPE.APPLOG);
			return false;
		}
		//Elementos
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getOrigen())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getOrigen().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getFormato())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getFormato().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getContenido().getFirma())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getContenido().getFirma().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().getId_pait())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().getId_pait().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getNif_notario())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getNif_notario().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getCodigo_notario())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la Codigo_notario().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_presentacion())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_presentacion().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_devengo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_devengo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getExpresion_abreviada())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getExpresion_abreviada().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNumero_protocolo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNumero_protocolo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getAnyo_protocolo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getAnyo_protocolo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_completo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_completo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		//Sujeto pasivo
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTelefono())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTelefono().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTipo_via())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTipo_via().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_via())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_via().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getCodigo_postal())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getCodigo_postal().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getCodigo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getCodigo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getNombre())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getNombre().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getCodigo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getCodigo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getNombre())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getNombre().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPais().getCodigo())) {
			com.stpa.ws.server.util.Logger.error("Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPais().getCodigo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPais().getNombre())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPais().getNombre().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		//Sujeto Interviniente
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_completo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_completo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTelefono())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTelefono().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTipo_via())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTipo_via().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_via())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_via().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getCodigo_postal())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getCodigo_postal().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().getCodigo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().getCodigo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().getNombre())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().getNombre().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().getCodigo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().getCodigo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().getNombre())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().getNombre().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPais().getCodigo())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPais().getCodigo().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPais().getNombre())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPais().getNombre().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		//Datos liquidaion
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getValores().getValor_declarado())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getValores().getValor_declarado().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getExento())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getExento().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getNo_sujeto())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getNo_sujeto().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		// Validacion exento y no-sujeto ambos a S, imposible.
		if ("S".equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getExento()) && "S".equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getNo_sujeto())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ); Incompatibilidad de datos: No puede ser Exento y No-sujeto a la vez." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.obj.no.valid"),Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		//Calculo
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getValores().getValor_declarado())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getValores().getValor_declarado().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_imponible())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_imponible().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_liquidable())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_liquidable().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().getPorcentaje())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().getPorcentaje().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().getImporte())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().getImporte().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getCuota())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getCuota().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getImporte_ingresar())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getImporte_ingresar().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTotal_ingresar())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTotal_ingresar().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		//Pago
		String tipo_confirmacion = new String("");
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia.campos") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), falta la getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion().",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		} else {
			//Si PT y fecha ingreso o ingreso_importe están informados, error.
			if (CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_SRL_PT.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion()))
			{
				if (!CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getFecha_ingreso()) ||
						!CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getIngreso_importe()))
				{
					com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.valiverif") + "Error validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), el tipo de sociedad es "+CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_SRL_PT + " y se ha indicado fecha de ingreso o ingreso importe.",Logger.LOGTYPE.APPLOG);
					throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
				}
			}
			tipo_confirmacion = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion();
			com.stpa.ws.server.util.Logger.debug("Validacion; isAccessFieldValidated (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ), tipo_confirmacion:" + tipo_confirmacion,Logger.LOGTYPE.APPLOG);
		}
		
		//Retorno TRUE para XML valido
		return true;
	}
	/**
	 * Devolveremos el nombre completo de Sujeto o en su defecto la conjuncion del Primer Apellido mas el Segundo Apellido mas el Nombre
	 * @param datos_STTCirce objeto datos_STTCirce procedente de CirceImpuestoTrnsADJ_Solicitud Objeto Solicitud CIRCE
	 * @param tipoInterviniente integer que denomina el tipo de Interviniente; 0 para Sujeto Pasivo, 1 para Presentador, 2 Notario
	 * @return Devolvera el nombre completo
	 * @throws StpawsException Error controlado de la aplicacion CIRCE
	 */
	public static String generarNombreCompleto(Datos_STTCirce datos_STTCirce, int tipoInterviniente) throws StpawsException {
		String nombreCompleto 	= new String("");
		String nCompleto_CIRCE 	= new String("");
		String nCompleto_PA_SA_N_CIRCE 	= new String("");
		
		if (tipoInterviniente == TIPO_INTERVINIENTE_SUJETOPASIVO) {
			nCompleto_CIRCE 			= 	datos_STTCirce.getIntervinientes().getSujeto_pasivo().getNombre_completo();
			nCompleto_PA_SA_N_CIRCE 	= 	datos_STTCirce.getIntervinientes().getSujeto_pasivo().getPrimer_apellido() + " " +
											datos_STTCirce.getIntervinientes().getSujeto_pasivo().getSegundo_apellido() + " " +
											datos_STTCirce.getIntervinientes().getSujeto_pasivo().getNombre();
			
		} else if (tipoInterviniente == TIPO_INTERVINIENTE_SUJETOPRESENTADOR) {
			nCompleto_CIRCE 			= 	datos_STTCirce.getIntervinientes().getSujeto_presentador().getNombre_completo();
			nCompleto_PA_SA_N_CIRCE 	= 	datos_STTCirce.getIntervinientes().getSujeto_presentador().getPrimer_apellido() + " " +
											datos_STTCirce.getIntervinientes().getSujeto_presentador().getSegundo_apellido() + " " +
											datos_STTCirce.getIntervinientes().getSujeto_presentador().getNombre();
		} else if(tipoInterviniente == TIPO_INTERVINIENTE_DATOSNOTARIALES) {
			nCompleto_CIRCE 			= 	datos_STTCirce.getId_notario().getNombre_completo();
			nCompleto_PA_SA_N_CIRCE 	= 	datos_STTCirce.getId_notario().getApellidos() + " " +
											datos_STTCirce.getId_notario().getNombre();
		} else {
			com.stpa.ws.server.util.Logger.error("generarNombreCompleto(Intervinientes intervinientes, int tipoInterviniente): Tipo Interviniente Incorrecto. tipoInterviniente:" + tipoInterviniente,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
		}
		// Validacion campos
		if (CirceValidation.isEmpty(nCompleto_CIRCE)) {
			nombreCompleto = nCompleto_PA_SA_N_CIRCE;
		} else {
			nombreCompleto = nCompleto_CIRCE;
		}
		
		return nombreCompleto.toUpperCase();
	}
	/**
	 * Utilizando reflection hacemos un set sobre el objeto deseado
	 * @param obj Objeto a Castear
	 * @param nombreField nombre del campo sobre el que haremos el SET
	 * @param valorField Valor del SET
	 * @throws StpawsException Error controlado del sistema CIRCE
	 */
	@SuppressWarnings("unchecked")
	public static void colocaValoresBeanFecha(Object obj, String nombreField, String valorField) throws StpawsException {
        try {            
        	Class c = Class.forName(obj.getClass().getName());

            Field field = c.getDeclaredField(nombreField);            
            field.setAccessible(true);
            field.set(obj, valorField);            
            field.setAccessible(false);    
        } catch (Throwable e) {
        	com.stpa.ws.server.util.Logger.error("colocaValoresBeanFecha(Object obj, String nombreField, String valorField), error.", e ,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
        }        
    }
	/**
	 * Devuelve el tipo de exención para la casilla de las liquidaciones
	 * @param objExcenciones Objeto con el tipo de exencion (Exento o No Sujeto)
	 * @param tipoExencion Indica el tipo de Exencion, EXENTO o NOSUJETO 
	 * @return Devuelve true SI es exento o no sujeto segun si está informada la validacion correspondiente.
	 * @throws StpawsException
	 */
	public static String getTipoLiquidacion(Exenciones objExcenciones) throws StpawsException {
		String sExenciones = new String("");
		
		if(objExcenciones.getExento().equals(CONSTANTE_EXENCION)){
			sExenciones = CirceImpuestoTrnsADJConstantes.TIPO_STRING_EXENTOBONIFICADO_pdf;
		} else if (objExcenciones.getNo_sujeto().equals(CONSTANTE_EXENCION)){
			sExenciones = CirceImpuestoTrnsADJConstantes.TIPO_STRING_NOSUJETO_pdf;
		} else {
			com.stpa.ws.server.util.Logger.debug("StpawsUtil:getTipoLiquidacion(Exenciones objExcenciones): No es ni EXENTO ni NO SUJETO",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		}
		
		return sExenciones; 
	}
	/**
	 * Generamos la firma para el objeto retorno CIRCE.
	 * @param lCirceImpuestoTrnsADJ_Respuesta Objeto CIRCE a con la respuesta.
	 * @return Objeto respuseta con al firma.
	 * @throws StpawsException Error controlado del sistema con el retorno.
	 */
	public static CirceImpuestoTrnsADJ_Respuesta generarFirmaCIRCE(CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta) throws StpawsException {
		Preferencias pref = new Preferencias();
		String resultadoCertificadoPDFFirmado = new String();
		String resultadoContenidoFirmado = new String();
		
		if (pref.getM_firmaDigital().equals("S")) {
			try {
				FirmaHelper firmaDigitalPDF = new FirmaHelper();				
				resultadoCertificadoPDFFirmado 	= firmaDigitalPDF.firmaMensaje(lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().getContenido(),true);
				lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setFirma(resultadoCertificadoPDFFirmado);
				
				FirmaHelper firmaDigitalXML = new FirmaHelper();
				String mensajeRespuestaAFirmar = StpawsUtil.documentoXMLContenido(lCirceImpuestoTrnsADJ_Respuesta);
				resultadoContenidoFirmado 		= firmaDigitalXML.firmaMensaje(mensajeRespuestaAFirmar, false);
				lCirceImpuestoTrnsADJ_Respuesta.getXml().getFirma_fichero().setFirma(resultadoContenidoFirmado);
				
			} catch (RuntimeException e) {
				com.stpa.ws.server.util.Logger.error("Error en firma:" + e.getMessage(), Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
			} catch (Exception ex) {
				com.stpa.ws.server.util.Logger.error("Error en firma:" + ex.getMessage(), Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), ex);
			}
		} 
		com.stpa.ws.server.util.Logger.debug("StpawsUtil:generarFirmaCIRCE():resultadoAltaFirma::" + resultadoContenidoFirmado,Logger.LOGTYPE.APPLOG);		
		return lCirceImpuestoTrnsADJ_Respuesta;
	}
	/**
	 * Asigna valores para el Objeto CIRCE de salida. los valores son Fijos.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE de Entrada.
	 * @param lCirceImpuestoTrnsADJ_Respuesta objeto CIRCE de Salida.
	 * @return Retorna un objeto CIRCE de lasila de tipo CirceImpuestoTrnsADJ_Respuesta.
	 */
	public static CirceImpuestoTrnsADJ_Respuesta setXMLCirce_Respuesta(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta) throws Exception{
		//Identificacion.
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setTipo_Sociedad(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setId_comunicacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getId_comunicacion());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setId_tramite(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getId_tramite());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setDescripcion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getDescripcion());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setFecha(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getFecha());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setHora(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getHora());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setAcuse(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getAcuse());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setSincronia(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getSincronia());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setSistema_emisor(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getSistema_receptor());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setSistema_receptor(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getSistema_emisor());
		//Elementos - Datos Interlocutor - Certificacion.
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setOrigen(CirceImpuestoTrnsADJConstantes.resultado_Origen);
		List<String> argumentoDestino = new ArrayList<String>();
		argumentoDestino.add(CirceImpuestoTrnsADJConstantes.resultado_RMP);
		argumentoDestino.add(CirceImpuestoTrnsADJConstantes.resultado_CGM);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setDestinos(argumentoDestino);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setFormato(CirceImpuestoTrnsADJConstantes.EXTENSION_PDF);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setNombre("");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setTamanyo("");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setFecha(DateUtil.getFechaUHoraActual()[0]);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setHora(DateUtil.getFechaUHoraActual()[1]);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setContenido("A rellenar: PDF Contenido en base 64");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setFirma("A rellenar: PDF Firmado");
		//Elementos - Datos STTCirce - Cabecera.
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().setAplicacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().getAplicacion());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().setId_pait(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().getId_pait());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().setComponente(CirceImpuestoTrnsADJConstantes.resultado_Componente);
		//Elementos - Datos STTCirce - Hecho Imponible
		String sModalidad;
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getModalidad())) {
			sModalidad = "";
		} else {
			sModalidad = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getModalidad();
		}
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setModalidad(sModalidad);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setFecha_presentacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_presentacion());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setFecha_devengo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_devengo());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setExpresion_abreviada(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getExpresion_abreviada());
		String sConcepto;
		if(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getConcepto())) {
			sConcepto = "";
		} else {
			sConcepto = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getConcepto();
		}
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setConcepto(sConcepto);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setNum_certificado("");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setNum_itpajd(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd());
		//Elementos - Datos STTCirce - Datos Notariales
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setTipo_documento(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getTipo_documento());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setNumero_protocolo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNumero_protocolo());
		String sNombreCompleto="";
		//FRAN if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNombre_completo())) {
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getNombre())) {
			sNombreCompleto = "";
		} else {
			//FRAN sNombreCompleto = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNombre_completo();
			sNombreCompleto = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getNombre();
			if (!CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getApellidos())) {
				sNombreCompleto+=" "+lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getApellidos();
			}
		}
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setNombre_completo(sNombreCompleto);

		//Elementos - Datos STTCirce - Sujeto
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setNombre_completo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_completo());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setNif(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setTipo_via(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTipo_via());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setNombre_via(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_via());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setNumero(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNumero());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setEscalera(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getEscalera());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setPlanta(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPlanta());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setPuerta(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPuerta());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setDuplicado(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getDuplicado());
		String sDuplicado;
		if (CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getDuplicado())) {
			sDuplicado = "";
		} else {
			sDuplicado = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getDuplicado();
		}
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setDuplicado(sDuplicado);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().setCodigo_postal(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getCodigo_postal());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().getMunicipio().setCodigo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getCodigo());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().getMunicipio().setNombre(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getNombre());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().getProvincia().setCodigo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getCodigo());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getSujeto().getProvincia().setNombre(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getNombre());
		//Elementos - Datos STTCirce - Pago 
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setTipo_confirmacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setDatos_pago(null);
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setFecha_ingreso("");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setIngreso_importe("");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setCodigo_pago("");
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setTexto_pago("");

		//Desaparecer la respuesta <DATOS_LIQUIDACION> FRAN (añadiendo null)
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().setDatos_liquidacion(null);

		//Objeto respuesta desde el WS de Circe.
		return lCirceImpuestoTrnsADJ_Respuesta;
	}
	/**
	 * Extraccion contenido a firmar.
	 * @param lCirceImpuestoTrnsADJ_Respuesta Objeto respuesta a firmar
	 * @return String contenido
	 * @throws StpawsException error durante la extraccion y tratamiento del contenido a firmar.
	 */
	public static String documentoXMLContenido(CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta) throws StpawsException {
		String resultado;
		try {
			
			String xml_contenido = XMLUtils.stringCirceRespuesta(lCirceImpuestoTrnsADJ_Respuesta);
			//Contenido a firmar
	        String campoInicialContenido = "<Identificacion>";
	        String campoFinalContenido = "</Elementos>";
	        //Procesado Contenido
	        int posicionInicialContenido = xml_contenido.indexOf(campoInicialContenido);
	        int posicionFinalContenido = xml_contenido.indexOf(campoFinalContenido);
	        resultado = xml_contenido.substring(posicionInicialContenido, posicionFinalContenido+campoFinalContenido.length());
	        
		} catch(Throwable th) {
			com.stpa.ws.server.util.Logger.error("StpawsUtil:documentoXMLContenido():Error:" + th.getMessage(), th,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), th);
		}
		com.stpa.ws.server.util.Logger.info("StpawsUtil:documentoXMLContenido():resultado:" + resultado,Logger.LOGTYPE.APPLOG);
		return resultado;
	}
	/**
	 * Alta de documento XML de salida hacia CIRCE en la base de datos.
	 * @param lCirceImpuestoTrnsADJ_Respuesta Objeto de respuesta para generar el XML de salida.
	 * @param beanSolresp Bean de control para obtener el nombre del fichero Solicitud y Respuesta.
	 * @throws StpawsException Error durante la ejecución de la trasforma.cion o almacenamiento.
	 */
	public static void altaDocumentoXMLRespuesta(CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta, BeanFicheroSolicitudRespuesta beanSolresp) throws StpawsException {
		//Generamos el XML de salida y cargamos el dato en el objeto de salida.
		String xml_salida;
		xml_salida = XMLUtils.stringCirceRespuesta(lCirceImpuestoTrnsADJ_Respuesta);
		lCirceImpuestoTrnsADJ_Respuesta.setSCirceRespuesta(xml_salida);
		if (CirceValidation.isEmpty(xml_salida)) {
			com.stpa.ws.server.util.Logger.error("Error en el almacenado del XMl respuesta CIRCE. XML de salida es nulo o vacio.",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
		} else {
			
			try {
				byte[] myBytes = xml_salida.getBytes(CirceImpuestoTrnsADJConstantes.xmlEncodig);
				ByteArrayInputStream bais = new ByteArrayInputStream(myBytes);
		        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		        
		        while(bais.available() != 0){			
		            baos.write(bais.read());           
		        }        
				//Almacenar el XML resultante en la base de datos.		
			
				String documentzippeado = new String("");
				documentzippeado = PDFUtil.comprimirPDF(baos);
				//Datos base de almacenado.
				String nombreDoc = lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd();
				String nifSujetoPasivo = lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif();
				String nifPresentador = lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif();
				PDFUtil.altaDocumento(nombreDoc,nifSujetoPasivo,"",CirceImpuestoTrnsADJConstantes.TIPODOC_MODELO_XML_RESULTANTE, nifPresentador, documentzippeado, CirceImpuestoTrnsADJConstantes.EXTENSION_XML);
				bais.close();
				baos.close();
				
				//Almacenado Salida			
				boolean bficheroFicheroRespuesta = ExtraccionYTratamientoFicheros.crearFicheroSalida(xml_salida,beanSolresp.getFicheroRespuesta());
				beanSolresp.setCreadoFicheroRespuesta(bficheroFicheroRespuesta);
				
			} catch (Throwable ex) {
				com.stpa.ws.server.util.Logger.error("Error en el almacenado del XMl Respuesta:" + ex.getMessage(), ex,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), ex);
			}
			
		}
	}
	/**
	 * Creamos el Bean con los nombre de los ficheros de entrada y salida.
	 * @param objBeanFicheroSolicitudRespuesta Objeto con el nombre y etado de la entrada y salida
	 * @param pref objeto de preferencias.
	 * @return retorna el objeto BeanFicheroSolicitudRespuesta.
	 */
	public static BeanFicheroSolicitudRespuesta setFicheroSolicitudRespuesta(BeanFicheroSolicitudRespuesta objBeanFicheroSolicitudRespuesta,Preferencias pref) {
		
		Date fecha = new Date();
		//Fichero Solicitud.
		String numUnico = Double.toString(Math.random());
		SimpleDateFormat formateadorCompleto = new SimpleDateFormat("dd_MM_yyyy_hh_mm_SSS");
		
		String sFicheroSolicitud = pref.getM_pathSolresp() + CirceImpuestoTrnsADJConstantes.nombreFicheroSolicitud + formateadorCompleto.format(fecha) + "_" + numUnico +".xml";
		objBeanFicheroSolicitudRespuesta.setFicheroSolicitud(sFicheroSolicitud);
		
		//Fichero Respuesta.		
		String sFicheroRespuesta = pref.getM_pathSolresp() + CirceImpuestoTrnsADJConstantes.nombreFicheroRespuesta + formateadorCompleto.format(fecha) + "_" + numUnico + ".xml";
		objBeanFicheroSolicitudRespuesta.setFicheroRespuesta(sFicheroRespuesta);
		
		
		return objBeanFicheroSolicitudRespuesta;
	}
	/**
     * Particion de un String segun el parametro : para el troceo.
     * @param codigoSplit Codigo a trocear.
     * @return String array de resultado. Dimension 2.
     */
    public static String[] doSplitErrores(String codigoSplit) {
        String [] spliteado = null;
        spliteado = codigoSplit.split(": ");
        return spliteado;
    }
}
