package com.stpa.ws.server.util;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import com.stp.webservices.ClienteWebServices;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.validation.CirceValidation;


public class WebServicesUtil {	
		
	public static HashMap<String, String> wsResponseObtenerPermisoCIRCE(String respuestaWebService) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("Ini wsResponseObtenerPermisoCIRCE(String respuestaWebService) respuestaWebService:" + respuestaWebService,Logger.LOGTYPE.CLIENTLOG);
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] {"STRING1_CANU", "STRING2_CANU", "STRING3_CANU", "STRING4_CANU", "NUME1_CANU", "NUME2_CANU", "NUME3_CANU", "NUME4_CANU", "FECHA1_CANU", "FECHA2_CANU", "FECHA3_CANU", "FECHA4_CANU", "STRING5_CANU", "STRING_CADE"};
		String[] estructurasARecuperar = new String[] {"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA"};
		com.stpa.ws.server.util.Logger.debug("wsResponseObtenerPermisoCIRCE(String respuestaWebService) Paso inicial",Logger.LOGTYPE.CLIENTLOG);
		Map<String, Object> respuestaAsMap=null;
		
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(re.getMessage(),re,Logger.LOGTYPE.CLIENTLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);			
		}
		
		if (respuestaAsMap != null && respuestaAsMap.containsKey("ERROR")) {
			Object[] lstErrores = (Object[]) respuestaAsMap.get("ERROR");
			String listaErrores = "";
			for (int i = 0; i < lstErrores.length; i++) {
				listaErrores += lstErrores[i].toString() + " \n";
			}
			com.stpa.ws.server.util.Logger.error("Se ha producido un error en el servicio com.stpa.ws.server.util.wsResponseObtenerPermisoCIRCE(String respuestaWebService)" + listaErrores,Logger.LOGTYPE.APPLOG);
			throw new StpawsException("Error oracle en el sistema Verificacion Permiso Servicio. Contacte con el adminstrador del sistema.", null);
		} else if(respuestaAsMap != null) {	
			// Control Error
			Object[] objcol = null;
			objcol = (Object[]) respuestaAsMap.get("CADE_CADENA");
			com.stpa.ws.server.util.Logger.debug("wsResponseObtenerPermisoCIRCE(String respuestaWebService) objcol:" + objcol,Logger.LOGTYPE.CLIENTLOG);
			if(objcol!=null){
				String[] objStr = (String[])objcol[0];
				resultado.put("STRING_CADE", objStr[0]);					
			}
		}		
		
		//Resultado final.
		com.stpa.ws.server.util.Logger.debug("Fin wsResponseObtenerPermisoCIRCE(String respuestaWebService) resultado:" + resultado,Logger.LOGTYPE.CLIENTLOG);
		return resultado;
	}
	
	/**
	 * Servicio validacion Primera Fecha Habil de la fecha de aplazamiento desde fecha facilitada por el sistema como factible desde la fecha solicitud.
	 * Servicio WEB: INTERNET.FechaValida
	 * @param Fecha Primera fecha de peticion. Procedente del calculo del dÃ­a posible (dia 5 o dia 20 dependiendo de la fecha de solicitud).
	 * @return Fecha Habil
	 * @throws StpawsException Excepcion controla por el programa CIRCE.
	 */
	public static String generarFechaHabil(String Fecha) throws StpawsException{
		com.stpa.ws.server.util.Logger.info("Ini generarFechaHabil(String Fecha); Fecha" + Fecha,Logger.LOGTYPE.APPLOG);
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"INTERNET.FechaValida");
		
		// Modelo
		fillPeti(xmlutils,Fecha,CirceImpuestoTrnsADJConstantes.WS_FECHA,CirceImpuestoTrnsADJConstantes.WS_FORMATO_FECHA,"1");
		fillPeti(xmlutils,"33090",CirceImpuestoTrnsADJConstantes.WS_STRING,null,"2");		
		
		com.stpa.ws.server.util.Logger.info("Fin generarFechaHabil(String Fecha); xmlutils" + xmlutils,Logger.LOGTYPE.APPLOG);
		return finGenerarPeticion(xmlutils,3, true);
	}
	/**
	 * Llamada al servicio Web que obtiene el numero de autoliquidacion
	 * @param idTramite CIF/NIF solicitante autorizado.
	 * @return Estructura peticion al PL.
	 * @throws StpawsException Excepcion controla de CIRCE durante el procesamiento o llamada.
	 */
	public static String obtenerNumeroAutoliquidacion(String idTramite) throws StpawsException{
		com.stpa.ws.server.util.Logger.debug("Ini obtenerNumeroAutoliquidacion(String idTramite); idTramite" + idTramite,Logger.LOGTYPE.APPLOG);
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"CIRCE.obtenernumauto");
		
		// Modelo
		fillPeti(xmlutils,idTramite,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"1");
		fillPeti(xmlutils,"600",CirceImpuestoTrnsADJConstantes.WS_STRING,null,"2");
		
		com.stpa.ws.server.util.Logger.debug("Fin obtenerNumeroAutoliquidacion(String idTramite); xmlutils" + xmlutils,Logger.LOGTYPE.APPLOG);
		return finGenerarPeticion(xmlutils,3, true);
	}
	/**
	 * Alta de los datos en el modelo 600 para el aplzamieto de la autoliquidacion.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE de la SOLICITUD junto con los datos de la RESPUESTA.
	 * @return Estructura peticion al PL.
	 * @throws StpawsException Excepcion controla de CIRCE durante el procesamiento o llamada.
	 */
	public static String obtenerIntegrarModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini obtenerIntegrarModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"CIRCE.RecibirDatosCirce");
		
		// Modelo
		com.stpa.ws.server.util.Logger.debug("Relleno en obtenerIntegrarModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ); Con los datos del modelo.",Logger.LOGTYPE.APPLOG);
		fillPeti(xmlutils,"CIRCE",CirceImpuestoTrnsADJConstantes.WS_STRING,null,"1");
		fillPeti(xmlutils,"33",CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"2");
		fillPeti(xmlutils,"61",CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"3");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"4");
		//Comprobar si esta relleno el nombre completo.
		String sNombreCompletoSujetoPasivo = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_completo();
			if(CirceValidation.isEmpty(sNombreCompletoSujetoPasivo)) {
				sNombreCompletoSujetoPasivo = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPrimer_apellido() + " " + lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getSegundo_apellido() + " " + lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre();
			}
		sNombreCompletoSujetoPasivo = sNombreCompletoSujetoPasivo.toUpperCase();
		fillPeti(xmlutils,sNombreCompletoSujetoPasivo,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"5");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTelefono(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"6");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getFax(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"7");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getEmail(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"8");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTipo_via(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"9");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_via(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"10");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNumero(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"11");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getDuplicado(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"12");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getEscalera(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"13");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPlanta(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"14");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPuerta(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"15");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getCodigo_postal(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"16");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getNombre(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"17");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getNombre(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"18");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"19");
		//Comprobar si esta relleno el nombre completo.
		String sNombreCompletoPresentador = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_completo();
			if(CirceValidation.isEmpty(sNombreCompletoPresentador)) {
				sNombreCompletoPresentador = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPrimer_apellido() + " " + lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getSegundo_apellido() + " " + lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre();
			}
		sNombreCompletoPresentador = sNombreCompletoPresentador.toUpperCase();
		fillPeti(xmlutils,sNombreCompletoPresentador,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"20");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTelefono(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"21");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getFax(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"22");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getEmail(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"23");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTipo_via(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"24");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_via(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"25");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNumero(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"26");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getDuplicado(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"27");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getEscalera(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"28");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPlanta(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"29");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPuerta(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"30");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getCodigo_postal(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"31");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().getNombre(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"32");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().getNombre(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"33");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getTipo_documento(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"34");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_devengo(),CirceImpuestoTrnsADJConstantes.WS_FECHA,CirceImpuestoTrnsADJConstantes.WS_FORMATO_FECHA,"35");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNumero_protocolo(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"36");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getNif_notario(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"37");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getLocalidad(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"38");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"39");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getExpresion_abreviada(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"40");
		// String Exento; To Upper case para el servicio.
		String sExento = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getExento();
			if (!CirceValidation.isEmpty(sExento)) {
				sExento = sExento.toUpperCase();
			}
		fillPeti(xmlutils,sExento,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"41");
		// String No Sujeto; To Upper case para el servicio.
		String sNoSujeto = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getNo_sujeto();
			if (!CirceValidation.isEmpty(sNoSujeto)) {
				sNoSujeto = sNoSujeto.toUpperCase();
			}
		fillPeti(xmlutils,sNoSujeto,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"42");		
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getFecha_ingreso(),CirceImpuestoTrnsADJConstantes.WS_FECHA,CirceImpuestoTrnsADJConstantes.WS_FORMATO_FECHA,"43");
		// Damos formato en Centimos de Euro a las cifras monetareas.
		String total_ingresar = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTotal_ingresar());
		fillPeti(xmlutils,total_ingresar,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"44");
		String ingreso_importe = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getIngreso_importe());
		fillPeti(xmlutils,ingreso_importe,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"45");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_presentacion(),CirceImpuestoTrnsADJConstantes.WS_FECHA,CirceImpuestoTrnsADJConstantes.WS_FORMATO_FECHA,"46");
		String base_imponible = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_imponible());
		fillPeti(xmlutils,base_imponible,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"47");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"48");
		String valor_declarado = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getValores().getValor_declarado());
		fillPeti(xmlutils,valor_declarado,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"49");
		String reduccion_porcentaje = NumberUtil.formatStringComaToPunto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getReduccion().getPorcentaje());
		fillPeti(xmlutils,reduccion_porcentaje,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"50");
		String reduccion_importe = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getReduccion().getImporte());
		fillPeti(xmlutils,reduccion_importe,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"51");
		String tipo_impositivo_porcentaje = NumberUtil.formatStringComaToPunto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().getPorcentaje());
		fillPeti(xmlutils,tipo_impositivo_porcentaje,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"52");
		String cuota = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getCuota());
		fillPeti(xmlutils,cuota,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"53");
		String bonificacion_porcentaje = NumberUtil.formatStringComaToPunto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBonificacion().getPorcentaje());
		fillPeti(xmlutils,bonificacion_porcentaje,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"54");
		String importe = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBonificacion().getImporte());
		fillPeti(xmlutils,importe,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"55");	
		String intereses = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getIntereses());
		fillPeti(xmlutils,intereses,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"56");
		String recargo = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getRecargo());
		fillPeti(xmlutils,recargo,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"57");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getFundamento(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"58");
		String base_liquidable = NumberUtil.getEuroInCentsFormat(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_liquidable());
		fillPeti(xmlutils,base_liquidable,CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"59");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getEntidad(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"60");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getOficina(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"61");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getControl(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"62");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getCuenta(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"63");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getCodigo_notario(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"64");
		fillPeti(xmlutils,lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getId_tramite(),CirceImpuestoTrnsADJConstantes.WS_STRING,null,"65");
		com.stpa.ws.server.util.Logger.debug("Fin del relleno en obtenerIntegrarModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ); Con los datos del modelo.",Logger.LOGTYPE.APPLOG);
		
		//Fin del relleno del modelo
		com.stpa.ws.server.util.Logger.info("Fin obtenerIntegrarModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		return finGenerarPeticion(xmlutils,66, false);
	}
	/**
	 * Llamada al procedimiento para recuperar los datos fijos para el justificante de presentacion (textos, path, etc.)
	 * @param numeroAutoliquidacion Numero de autoliquidacion.
	 * @return Estructura peticion al PL.
	 * @throws StpawsException Excepcion controla de CIRCE durante el procesamiento o llamada.
	 */
	public static String callRecuperarDatosJustificantePr(String numeroAutoliquidacion) throws StpawsException{
		com.stpa.ws.server.util.Logger.debug("Ini callRecuperarDatosJustificantePr(String numeroAutoliquidacion); numeroAutoliquidacion" + numeroAutoliquidacion,Logger.LOGTYPE.CLIENTLOG);
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"PROGRAMAS_AYUDA4.obtenerDatosInformes");
			
		// Datos genericos Certificado.
		fillPeti(xmlutils,"1",CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"1");
		fillPeti(xmlutils,"1",CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"2");
		fillPeti(xmlutils,"USU_WEB_SAC",CirceImpuestoTrnsADJConstantes.WS_STRING,null,"3");
		fillPeti(xmlutils,"33",CirceImpuestoTrnsADJConstantes.WS_INTEGER,null,"4");
		fillPeti(xmlutils,"NA",CirceImpuestoTrnsADJConstantes.WS_STRING,null,"5");
		fillPeti(xmlutils,numeroAutoliquidacion,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"6");		
		
		com.stpa.ws.server.util.Logger.debug("Fin callRecuperarDatosJustificantePr(String numeroAutoliquidacion); xmlutils" + xmlutils,Logger.LOGTYPE.CLIENTLOG);
		return finGenerarPeticion(xmlutils,7, true);
	}
	/**
	 * Obtener acceso para CIRCE según NIF/CIF solicitante.
	 * @param nifcifCertificado Nif/Cif solicitante
	 * @return Objeto resultado de la llamada al servicio Web PL lanzador.
	 * @throws StpawsException Error controlado durante la llamada al PL.
	 */
	public static String obtenerAccesoCIRCE(String nifcifCertificado) throws StpawsException{
		com.stpa.ws.server.util.Logger.debug("Ini obtenerAccesoCIRCE(String nifcifCertificado); nifcifCertificado" + nifcifCertificado,Logger.LOGTYPE.APPLOG);
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"INTERNET.Permisoservicio");
		
		// Modelo
		fillPeti(xmlutils,"CIRCE",CirceImpuestoTrnsADJConstantes.WS_STRING,null,"1");
		fillPeti(xmlutils,nifcifCertificado,CirceImpuestoTrnsADJConstantes.WS_STRING,null,"2");
		
		com.stpa.ws.server.util.Logger.debug("Fin obtenerAccesoCIRCE(String nifcifCertificado); xmlutils" + xmlutils,Logger.LOGTYPE.APPLOG);
		return finGenerarPeticion(xmlutils,3, true);
	}
	/**
	 * Metodo iniciador de la peticion para los Servicios Web a los que debe conectarse el WB de CIRCE.
	 * @param xmlutils Estructura XML de la peticion.
	 * @param accion Servicio al que debe llamar.
	 * @return Retorna el XML Object con los datos iniciales y estandar para la peticion.
	 * @throws StpawsException Error controla STPA para Web Service CIRCE.
	 */
	private static XMLUtils iniGenerarPeticion(XMLUtils xmlutils, String accion) throws  StpawsException{
		com.stpa.ws.server.util.Logger.info("Ini iniGenerarPeticion(XMLUtils xmlutils, String accion).",Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("accion:" + accion,Logger.LOGTYPE.APPLOG);
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"webservices.error.ini.generar.peticion") + re.getMessage(),re,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc","",new String[]{"nombre"},new String[]{accion});
		xmlutils.reParentar(1);		
		
		//Fin del Generacion de la peticion
		com.stpa.ws.server.util.Logger.debug("xmlutils:" + xmlutils,Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.info("Fin iniGenerarPeticion(XMLUtils xmlutils, String accion).",Logger.LOGTYPE.APPLOG);
		return xmlutils;
	}
	/**
	 * Metodo finalizador de la peticion para los Servicios Web a los que debe conectarse el WB de CIRCE.
	 * @param xmlutils Estructura XML de la peticion.
	 * @param posActual Posicion en la que debe continuar el servicio rellenando los valores.
	 * @return Retorna el String con los datos para la peticion al servicio. ParÃ¡metro de salida con el que poder invocar al servicio.
	 * @throws StpawsException Error controla STPA para Web Service CIRCE.
	 */
	private static String finGenerarPeticion(XMLUtils xmlutils, int posActual, boolean isNecesario) throws StpawsException{
		com.stpa.ws.server.util.Logger.info("Ini finGenerarPeticion((XMLUtils xmlutils, int posActual).",Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("posActual: " + posActual,Logger.LOGTYPE.APPLOG);
		// Identificacion       
		if (isNecesario) {fillPeti(xmlutils,"P","1",null,String.valueOf(posActual));}
		
		xmlutils.reParentar(-1);
		xmlutils.reParentar(-1);
		String xmlIn = "";
		try {
			xmlIn = xmlutils.informarXMLDoc();
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"webservices.error.fin.generar.peticion") + re.getMessage(),re,Logger.LOGTYPE.APPLOG);			
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), re);
		}
		
		//Fin del Generacion de la peticion
		com.stpa.ws.server.util.Logger.debug("xmlIn: " + xmlIn,Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.info("Ini finGenerarPeticion(XMLUtils xmlutils, int posActual).",Logger.LOGTYPE.APPLOG);
		return xmlIn; 
	}
	
	/**
	 * Metodo que rellena la peticion con la que formatear los datos de entrada al WS servicio destino.
	 * @param xmlutils Estructura XML de la peticion.
	 * @param valor Valor del campo VALOR
	 * @param tipo Valor del campo TIPO
	 * @param formato Formato de nvio del dato (numerico, string, fecha).
	 * @param orden Orden de envio del dato.
	 */
	
	private static void fillPeti(XMLUtils xmlutils, String valor, String tipo, String formato, String orden){
		com.stpa.ws.server.util.Logger.debug("Ini fillPeti(XMLUtils xmlutils, String valor, String tipo, String formato, String orden); String valor, String tipo, String formato, String orden" + valor ,Logger.LOGTYPE.CLIENTLOG);
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { orden });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valor);
		xmlutils.crearNode("tipo", tipo);
		if(formato!=null) {
			xmlutils.crearNode("formato", formato);
		} else {
			xmlutils.crearNode("formato", "");
		}
		xmlutils.reParentar(-1);
	}
	/**
	 * Obtener fecha hábil desde servicio INTERNET.FechaValida
	 * @param respuestaWebService Fecha solicitud fecha aplazamiento
	 * @return Respuesta con fecha habil
	 * @throws StpawsException Error controlado del sistema
	 */
	public static HashMap<String, String> wsResponseFechaHabil(String respuestaWebService) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini wsResponseFechaHabil(String respuestaWebService).",Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("respuestaWebService:" + respuestaWebService,Logger.LOGTYPE.APPLOG);
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU", "STRING2_CANU", "STRING3_CANU", "STRING4_CANU", "NUME1_CANU", "NUME2_CANU", "NUME3_CANU", "NUME4_CANU", "FECHA1_CANU", "FECHA2_CANU", "FECHA3_CANU", "FECHA4_CANU", "STRING5_CANU", "STRING_CADE"};
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA" };
		
		Map<String, Object> respuestaAsMap = null;
		
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(re.getMessage(),re,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), re);
		}
		
		if (respuestaAsMap != null && respuestaAsMap.containsKey("ERROR")) {
			Object[] lstErrores = (Object[]) respuestaAsMap.get("ERROR");
			String listaErrores = "";
			for (int i = 0; i < lstErrores.length; i++) {
				listaErrores += lstErrores[i].toString() + " \n";
			}
			com.stpa.ws.server.util.Logger.error("Se ha producido un error en el servicio com.stpa.ws.server.util.wsResponseFechaHabil(String respuestaWebService)" + listaErrores,Logger.LOGTYPE.APPLOG);
			throw new StpawsException("Error oracle en el sistema Obtener fecha habil. Contacte con el adminstrador del sistema.", null);
		} else if(respuestaAsMap != null) {	
		
			Object[] objcol1 = null;
			objcol1 = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
			com.stpa.ws.server.util.Logger.debug("wsResponseFechaHabil(String respuestaWebService) objcol1:" + objcol1,Logger.LOGTYPE.APPLOG);
			if(objcol1!=null){
				String[] objStr1 = (String[])objcol1[0];
				resultado.put("FECHA1_CANU", objStr1[8]);			
			}
			Object[] objcol2 = null;
			objcol2 = (Object[]) respuestaAsMap.get("CADE_CADENA");
			com.stpa.ws.server.util.Logger.debug("wsResponseFechaHabil(String respuestaWebService) objcol2:" + objcol2,Logger.LOGTYPE.APPLOG);
			if(objcol2!=null){
				String[] objStr2 = (String[])objcol2[0];
				resultado.put("STRING_CADE", objStr2[0]);			
			}
			
		}
		
		com.stpa.ws.server.util.Logger.debug("(respuestaWebService) resultado:" + resultado,Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.info("Fin wsResponseFechaHabil(String respuestaWebService).",Logger.LOGTYPE.APPLOG);
		return resultado;
	}
	/**
	 * Tramitacion de la respuesta del Servicio Web para obtener numero de autoliquidacion.
	 * @param respuestaWebService XML de respuseat del servicio Web CIRCE.obtenernumauto
	 * @return Mapa con los datos de la tramitacion.
	 * @throws StpawsException Error controlado del servicio CIRCE.
	 */
	public static HashMap<String, String> wsResponseObtenerNumAutoliquidacion(String respuestaWebService) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini wsResponseObtenerNumAutoliquidacion(String respuestaWebService).",Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("respuestaWebService:" + respuestaWebService,Logger.LOGTYPE.APPLOG);
		
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING_CADE", "STRING1_CANU", "STRING2_CANU", "STRING3_CANU", "STRING4_CANU", "NUME1_CANU", "NUME2_CANU", "NUME3_CANU", "NUME4_CANU", "FECHA1_CANU", "FECHA2_CANU", "FECHA3_CANU", "FECHA4_CANU", "STRING5_CANU"};
		String[] estructurasARecuperar = new String[] { "CADE_CADENA", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS"};
		
		Map<String, Object> respuestaAsMap=null;
		
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(re.getMessage(),re,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), re);			
		}
		
		//Objeto con Numero Autoliquidacion
		Object[] objcol0 = null;
		objcol0 = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
		com.stpa.ws.server.util.Logger.debug("wsResponseObtenerNumAutoliquidacion(String respuestaWebService) objcol0:" + objcol0,Logger.LOGTYPE.CLIENTLOG);
		if(objcol0!=null){
			String[] objStr0 = (String[])objcol0[0];
			resultado.put("STRING1_CANU", objStr0[0]);
			resultado.put("STRING2_CANU", objStr0[1]);			
		}
		
		//Error
		Object[] objcol1 = null;
		objcol1 = (Object[]) respuestaAsMap.get("CADE_CADENA");
		com.stpa.ws.server.util.Logger.debug("wsResponseObtenerNumAutoliquidacion(String respuestaWebService) objcol1:" + objcol1,Logger.LOGTYPE.CLIENTLOG);
		if(objcol1!=null){
			String[] objStr1 = (String[])objcol1[0];
			resultado.put("STRING_CADE", objStr1[0]);					
		}
		
		//Resultado final.
		com.stpa.ws.server.util.Logger.debug("resultado:" + resultado,Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.info("Fin wsResponseObtenerNumAutoliquidacion(String respuestaWebService).",Logger.LOGTYPE.APPLOG);
		return resultado;
	}
	/**
	 * Obtenemos y procesamos el resultado de la cadena del Certificado
	 * @param respuestaWebService respuesta de la llamada al PL que vamos a procesar.
	 * @return Mapa con el resultado de datos.
	 * @throws StpawsException Error controlado del Servicio.
	 */
	public static HashMap<String, String> wsResponseNIFCIFValido(String respuestaWebService) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini wsResponseNIFCIFValido(String respuestaWebService).",Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("respuestaWebService:" + respuestaWebService,Logger.LOGTYPE.APPLOG);
		
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU", "STRING2_CANU", "STRING3_CANU", "STRING4_CANU", "NUME1_CANU", "NUME2_CANU", "NUME3_CANU", "NUME4_CANU", "FECHA1_CANU", "FECHA2_CANU", "FECHA3_CANU", "FECHA4_CANU", "STRING5_CANU", "STRING_CADE"};
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA"};
		
		Map<String, Object> respuestaAsMap=null;
		
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(re.getMessage(),re,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), re);			
		}
		
		//Autorizacion
		Object[] objcol1 = null;
		objcol1 = (Object[]) respuestaAsMap.get("CADE_CADENA");
		com.stpa.ws.server.util.Logger.debug("wsResponseNIFCIFValido(String respuestaWebService) objcol1:" + objcol1,Logger.LOGTYPE.CLIENTLOG);
		if(objcol1!=null){
			String[] objStr1 = (String[])objcol1[0];
			resultado.put("STRING_CADE", objStr1[0]);					
		}
		
		//Resultado final.
		com.stpa.ws.server.util.Logger.debug("resultado:" + resultado,Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.info("Fin wsResponseNIFCIFValido(String respuestaWebService).",Logger.LOGTYPE.APPLOG);
		return resultado;
	}
	/**
	 * Lanzador llamda al motor de Web Services indicado
	 * @param peticion XML de la peticion rellena.
	 * @return tipoPeticion Tipo de lanzador a utilizar.
	 * @throws StpawsException Error controlado de CIRCE.
	 */
	public static String wsCall(String peticion, int tipoPeticion) throws StpawsException{
		com.stpa.ws.server.util.Logger.info("Ini wsCall(String peticion, int tipoPeticion).",Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("(String peticion, int tipoPeticion): peticion:" + peticion + "- tipoPeticion:" + tipoPeticion,Logger.LOGTYPE.APPLOG);
		Preferencias pref = new Preferencias();
		String xmlOut = "";
		ClienteWebServices clienteWS = new ClienteWebServices();
		try {
			
			String WSLANZADOR_WSDL_URL = new String("");
			String WSLANZADOR_SERVICE_NAME = new String("");
			String WSLANZADOR_NAMESPACE = pref.getM_wslanzadorservicenamespace();
			com.stpa.ws.server.util.Logger.debug("WSLANZADOR_NAMESPACE: "+WSLANZADOR_NAMESPACE,Logger.LOGTYPE.APPLOG);			
			if (tipoPeticion == CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL) {	
				WSLANZADOR_WSDL_URL = pref.getM_wslanzadorwsdlurl();
				WSLANZADOR_SERVICE_NAME = pref.getM_wslanzadorservicename();
				clienteWS.inicializarWSLanzador(WSLANZADOR_WSDL_URL, WSLANZADOR_SERVICE_NAME, WSLANZADOR_NAMESPACE);
			} else {				
				WSLANZADOR_WSDL_URL = pref.getM_wslanzadormasivowsdlurl();
				WSLANZADOR_SERVICE_NAME = pref.getM_wslanzadormasivoservicename();
				clienteWS.inicializarWSLanzadorMasivo(WSLANZADOR_WSDL_URL, WSLANZADOR_SERVICE_NAME, WSLANZADOR_NAMESPACE);
			}
			com.stpa.ws.server.util.Logger.debug("WSLANZADOR_WSDL_URL: "+WSLANZADOR_WSDL_URL,Logger.LOGTYPE.APPLOG);
			com.stpa.ws.server.util.Logger.debug("WSLANZADOR_SERVICE_NAME: "+WSLANZADOR_SERVICE_NAME,Logger.LOGTYPE.APPLOG);	
			
			// Conexion al servicio PL
			InetAddress addr = InetAddress.getLocalHost();
			com.stpa.ws.server.util.Logger.debug("addr: "+addr,Logger.LOGTYPE.APPLOG);
			String hostaddress = addr.getHostAddress();
			com.stpa.ws.server.util.Logger.debug("hostaddress: "+hostaddress,Logger.LOGTYPE.APPLOG);
			String accesoWebservice = pref.getM_wslanzadorentornoBDD();
			com.stpa.ws.server.util.Logger.debug("accesoWebservice: "+accesoWebservice,Logger.LOGTYPE.APPLOG);
			com.stpa.ws.server.util.Logger.debug("peticion: "+peticion,Logger.LOGTYPE.APPLOG);
			com.stpa.ws.server.util.Logger.debug("tipo peticion: "+tipoPeticion,Logger.LOGTYPE.APPLOG);
			if (tipoPeticion == CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL) {
				xmlOut = clienteWS.ejecutarWSLanzadorExecutePL(accesoWebservice, peticion, hostaddress, "", "", "");
			} else {
				xmlOut = clienteWS.ejecutarWSLanzadorMasivoExecutePL(accesoWebservice, peticion, hostaddress, "", "", "");
			}
			com.stpa.ws.server.util.Logger.debug("wsCall(String peticion): xmlOut:" + xmlOut,Logger.LOGTYPE.APPLOG);
		} catch (MalformedURLException mfe) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"webservices.error.call.invalid.url") + mfe.getMessage(),mfe,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), mfe);
		} catch (Exception wse) {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"webservices.error.call.no.connect") + wse.getMessage(),wse,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), wse);
		}
		com.stpa.ws.server.util.Logger.info("Fin wsCall(String peticion, int tipoPeticion).",Logger.LOGTYPE.APPLOG);
		return xmlOut;
	}
	/**
	 * Comprobamos si el WS llamado retorna ona Excepcion no controla da BBDD.
	 * @param XmlOut XML string retornado por el WS.
	 * @return Boolean indicando si es un ORA exception (true).
	 */
	public static boolean hasErrorRetornoWS(String xmlOut, String servicioWS) throws StpawsException{
		boolean bError = false;
		String oraError = new String("");
		if (xmlOut.indexOf("ORA-") > -1) {
			bError = true;			
			int posInicial = xmlOut.indexOf("<error>");
			int posFinal = xmlOut.indexOf("</error>");
			oraError = xmlOut.substring(posInicial+7, posFinal);
			com.stpa.ws.server.util.Logger.error("WebServicesUtil.hasErrorRetornoWS(String xmlOut, String servicioWS): ORA Exception is True." + ":" + xmlOut + "-" + servicioWS,Logger.LOGTYPE.APPLOG);
			com.stpa.ws.server.util.Logger.error("WebServicesUtil.hasErrorRetornoWS: ORA Exception:" + oraError,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), null);
		} else if (xmlOut.indexOf("<") == -1) {
			com.stpa.ws.server.util.Logger.error("WebServicesUtil.hasErrorRetornoWS: Error indeterminado, Cadena sin formato:" + xmlOut,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(CirceImpuestoTrnsADJConstantes.MSG_PROP,"msg.err.gen"), null);
		}
		// retornamos si hay error.
		return bError; 
	}
	/**
	 * Generamos la peticion de insercion de datos para el documento en la Base de Datos
	 * @param valoresDeParametros Valores a insertar en la Base de datos
	 * @return XML de inserción en la llamada al Web Service de Insercion.
	 * @throws StpawsException Error controlado de la aplicacion.
	 */
	public static String generarPeticionInsertarDocumentos(Map<String, String> valoresDeParametros) throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error("xmlutils.crearXMLDoc()." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.obj.no.valid")+ "-Error:" + re.getMessage(),re,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc", "", new String[] { "nombre" }, new String[] { "INTERNET_DOCUMENTOSV2.altadocumento" });
		xmlutils.reParentar(1);
		// parametro sql p_tipo(tipo_doc)
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "1" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_tipo"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_nombre(nombre_documento)
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "2" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_nombre"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_codverificacion
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "3" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_codverificacion"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_sujetopasivo(nif_cif)
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "4" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_sujetopasivo"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_presentador(tipo_id)
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "5" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_presentador"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_idsession
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "6" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_idsession"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_origen(origen_pt)
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "7" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_origen"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_libre
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "8" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_libre"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_publicable
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "9" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_publicable"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_documento(documento en base 64)
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "10" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_documento"));
		xmlutils.crearNode("tipo", "4");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_extension
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "11" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_extension"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
		// parametro sql p_conoracle
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { "12" });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valoresDeParametros.get("p_conoracle"));
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);

		xmlutils.reParentar(-1);
		xmlutils.reParentar(-1);
		String xmlIn = "";
		try {
			xmlIn = xmlutils.informarXMLDoc();
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error("xmlutils.informarXMLDoc()." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.obj.no.valid") + "-Error:" + re.getMessage(),re,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);
		}
		return xmlIn;
	}
	/**
	 * Respuesta llamada al Webservice para la insercion en la base de datos.
	 * @param respuestaWebService Respuesta devuelta por el Web service.
	 * @return Insercion correcta (true) o incorrecta (fales) de la insercion.
	 * @throws StpawsException Excepcion controlada de la aplicacion.
	 */
	public static boolean respuestaWSInsercionCorrecta(String respuestaWebService) throws StpawsException {
		boolean isInsercionCorrecta = false;		
		com.stpa.ws.server.util.Logger.debug("respuestaWSInsercionCorrecta(String respuestaWebService):respuestaWebService:" + respuestaWebService,Logger.LOGTYPE.APPLOG);
		
		if(respuestaWebService != null){
			int idxError = respuestaWebService.indexOf("<error>");
			
			if(idxError >= 0){
				com.stpa.ws.server.util.Logger.debug("respuestaWSInsercionCorrecta(String respuestaWebService):respuestaWebService:" + respuestaWebService,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
			}
			String[] columnasARecuperar = new String[] {"STRING_CADE"};
			String[] estructurasARecuperar = new String[] { "CADE_CADENA"};
			Map<String, Object> respuestaAsMap;
			try {
				respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
			} catch (RemoteException re) {
				com.stpa.ws.server.util.Logger.error("respuestaWSInsercionCorrecta(String respuestaWebService):XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false):" + respuestaWebService,re,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);
			}
			String codiError = null;
			Object[] objcol = (Object[]) respuestaAsMap.get("CADE_CADENA");
			if(objcol != null){
				for (int i = 0; i < objcol.length; i++) {
					String[] objrow = (String[]) objcol[i];
					if (!objrow[0].equals("")) {
						codiError = objrow[0];
					}
				}			
			}
			
			if(codiError != null && "00".equals(codiError)){
				isInsercionCorrecta = true;
			}			
		}		
		return isInsercionCorrecta;
	}	
	/**
	 * Obtener fecha hábil a partir de fecha petición autoliquidacion.
	 * @param fecha_identificacion Fecha identificada para la autoliquidacion
	 * @return Resultado fecha habil del servicio INTERNET.FechaValida
	 * @throws StpawsException Error controlado de respuesta de la aplicacion
	 */
	public static HashMap<String, String> getFechaHabil (String fecha_identificacion) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini getFechaHabil (String fecha_identificacion): Servicio INTERNET.FechaValida.",Logger.LOGTYPE.APPLOG);
		String peticion_generarFechaHabil = WebServicesUtil.generarFechaHabil(fecha_identificacion);
		com.stpa.ws.server.util.Logger.debug("peticion_generarFechaHabil: " + peticion_generarFechaHabil,Logger.LOGTYPE.APPLOG);
		String xmlOut_generarFechaHabil = WebServicesUtil.wsCall(peticion_generarFechaHabil, CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL);
		com.stpa.ws.server.util.Logger.debug("xmlOut_generarFechaHabil: " + xmlOut_generarFechaHabil,Logger.LOGTYPE.APPLOG);
		
		//Mapa resultado
		HashMap<String, String> resultado = WebServicesUtil.wsResponseFechaHabil(xmlOut_generarFechaHabil);
		
		com.stpa.ws.server.util.Logger.info("Fin getFechaHabil (String fecha_identificacion).",Logger.LOGTYPE.APPLOG);
		return resultado;
	}
}
