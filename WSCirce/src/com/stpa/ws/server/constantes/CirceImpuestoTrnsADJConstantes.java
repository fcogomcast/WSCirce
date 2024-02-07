package com.stpa.ws.server.constantes;

/**
 * Clase de Constantes para autoliquidacion CIRCE
 * @author Acenture Spain
 * @version 1.0
 */

public class CirceImpuestoTrnsADJConstantes {
	
	/*
	 * Validacion entadas para:
	 * Tipo: TIPO_SOCIEDAD Subtipo: TIPO_CONFORMACION
	 */	
	public static final String constantetipoTipoSociedad_SLNE = "SLNE";
	public static final String constantesubtipoTipoSociedad_SLNE_AP = "AP";
	public static final String constantesubtipoTipoSociedad_EX = "EX";
	
	public static final String constantetipoTipoSociedad_SRL = "SRL";
	public static final String constantesubtipoTipoSociedad_SRL_PT = "PT";
	
	// Constantes llamadas a Procedimientos
	public static final String WS_STRING = "1";
	public static final String WS_INTEGER = "2";	
	public static final String WS_FECHA = "3";
	public static final String WS_FORMATO_FECHA = "dd/mm/rrrr";
	
	// Codigo de errores
	public static final String constanterrorRetornoOK = "0";
	public static final String constanterrorRetornoKO = "1";
	/**
	 * String 00
	 */
	public static final String constanterrorRetornoGenericoWSService_OK = "00";
	/**
	 * String 01
	 */
	public static final String constanterrorRetornoGenericoWSService_KO = "01";
	/**
	 * String N
	 */
	public static final String constantretornoTramiteTributario = "N";
	/**
	 * String N
	 */
	public static final String constantretorno_NSujeto = "S";
	/**
	 * String N
	 */
	public static final String constantretorno_Exento = "S";
	/**
	 * String certNSujeto
	 */
	public static final String constantTipoCertificado_NoSujeo = "certNSujeto";
	public static final String constantTipoCertificado_NoSujeo_PDF = "certificacionnosujeto";
	/**
	 * String certExento
	 */
	public static final String constantTipoCertificado_Exento = "certExento";
	public static final String constantTipoCertificado_Exento_PDF = "certificacionexento";
	/**
	 * String certAplazado
	 */
	public static final String constantTipoCertificado_Aplazado = "certAplazado";
	public static final String constantTipoCertificado_Aplazado_PDF = "certificacionaplaz";
	/**
	 * String certPTelematic
	 */
	public static final String constantTipoCertificado_PTelematic = "certPTelematic";
	public static final String constantTipoCertificado_PTelematic_PDF = "certificacion";
	public static final String constantTipoCertificado_Ap_Tp = "0,00";
	/**
	 * String integrarTributas
	 */
	public static final String constantNoIntegrarTributas = "N";
	public static final String constantIntegrarTributas = "S";
	/**
	 * Extension = PDF
	 */
	public static String EXTENSION_PDF = "PDF";
	/**
	 * Extension = XML
	 */
	public static String EXTENSION_XML = "XML";
	/**
	 * constantModelo_600 = "Modelo600_CIRCE"
	 */
	public static final String constantModelo_600 = "Modelo600_CIRCE";
	/**
	 * constante_Emisor = "338000";
	 */
	public static final String constante_Emisor = "338000";
	/**
	 * TIPO_STRING_EXENTOBONIFICADO_pdf = "EB"
	 */
	public static final String TIPO_STRING_EXENTOBONIFICADO_pdf = "EB";
	/**
	 * TIPO_STRING_NOSUJETO_pdf = "NS"
	 */
	public static final String TIPO_STRING_NOSUJETO_pdf = "NS";
	/**
	 * TIPO_STRING_NOTARIAL = "N"
	 */
	public static final String TIPO_STRING_NOTARIAL = "N";
	/**
	 * TIPO_STRING_AUTOLIQUIDACION = "X"
	 */
	public static final String TIPO_STRING_AUTOLIQUIDACION = "X";
	/**
	 * TIPODOC_CERTIFICADO = "C"
	 */
	public static final String TIPODOC_CERTIFICADO = "C";
	/**
	 * TIPODOC_MODELO600 = "M"
	 */
	public static final String TIPODOC_MODELO600 = "M";
	/**
	 * TIPODOC_MODELO_XML_RESULTANTE = "M"
	 */
	public static final String TIPODOC_MODELO_XML_RESULTANTE = "M";
	/**
	 * constante_NININGRESO = "0,00"
	 */
	public static final String constante_NININGRESO = "0,00";
	/**
	 * constante_TIPOPETICION_LANZADOR_WSDL = 0
	 */
	public static final int constante_TIPOPETICION_LANZADOR_WSDL = 0;
	/**
	 * constante_TIPOPETICION_LANZADORMASIVO_WSDL = 1
	 */
	public static final int constante_TIPOPETICION_LANZADORMASIVO_WSDL = 1;
	/**
	 * constante_VALIDA_FIRMA = "S"
	 */
	public static final String constante_VALIDA_FIRMA = "S";
	/**
	 * MSG_PROP = "com.stpa.ws.server.configuracion.messages"
	 */
	public final static String MSG_PROP = "com.stpa.ws.server.configuracion.messages";
	/**
	 * Contantes para el parseo del XML para FIRMA-FIRMADO.
	 */
	public final static String constante_campoInicialContenido = "<Identificacion>";
	public final static String constante_campoFinalContenido = "</Elementos>";
	public final static String constante_campoInicialFirma = "<Firma_fichero><Firma>";
	public final static String constante_campoFinalFirma = "</Firma></Firma_fichero>";
	/**
	 * Valores fijo respuesta a CIRCE.
	 */
	public final static String resultado_Origen = "PRINCIPADO DE ASTURIAS";
	public final static String resultado_RMP = "RMP";
	public final static String resultado_CGM = "CGM";
	public final static String resultado_Componente = "F";
	public final static String xmlEncodig = "ISO-8859-1";
	public static final String nombreFicheroSolicitud = "Solicitud_CIRCE";
	public static final String nombreFicheroRespuesta = "Respuesta_CIRCE";
	public static final String nombreFicheroContenido = "Contenido_CIRCE";
	public static final String nombreFicheroFirma = "Firma_CIRCE";
		
}
