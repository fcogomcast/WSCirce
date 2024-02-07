package com.stpa.ws.server.validation;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.stpa.ws.firma.bean.BeanRespValidar;
import com.stpa.ws.firma.util.ValidacionEstandar;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.base.IStpawsValidation;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.DateUtil;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.WebServicesUtil;

public class CirceValidation implements IStpawsValidation{
	/*
	 * Invocamos al contructor de preferencias.
	 */
	Preferencias pref = new Preferencias();		
	/**
	 * Doble validacion de Acceso al sistema CIRCE, por una parte Verifica la Firma del XML de entrada y Valida su Certificado.
	 * Si el resultado de dicha validación es POSITIVA , Valida el permiso de acceso para el demandante.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto Solicitud desde la peticion CIRCE.
	 * @return Retorna un booleano segun la respuesta del servicio
	 * @throws StpawsException Excepcion contrilada del sistema.
	 */
	public boolean isAllowed(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		boolean bResultado = false;
		// Logica llamada al servicio de verificacion de acceso al servicio CIRCE: INTERNET.Permisoservicio
		com.stpa.ws.server.util.Logger.debug("Inicio CirceValidation.isAllowed(Object obj): Verificacion de acceso al servicio CIRCE: INTERNET.Permisoservicio",Logger.LOGTYPE.APPLOG);
		
		if (pref.getM_validaFirma().equals(CirceImpuestoTrnsADJConstantes.constante_VALIDA_FIRMA)) {
			//Validar la Firma
			boolean validarFirma = false;
			BeanRespValidar resultadoValidacion;
			try {				
				resultadoValidacion = isFirmaEstandarCirceValid(lCirceImpuestoTrnsADJ_Solicitud.getSCirceSolicitud());
				validarFirma = resultadoValidacion.isValidacionFirma();
			} catch (Throwable th) {
				com.stpa.ws.server.util.Logger.error("Error en la extraccion Firma." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.firma.no.valid") + "-Error:" + th.getMessage(), th,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),th);
			}
			
			//Verifica el Certificado y retorna el NIF/CIF en caso POSITIVO.
			boolean validarCertificado = false;
			String sX509Certificate = new String("");
			if (validarFirma) {
				//Accedemos al Certificado y sus datos.
				sX509Certificate = resultadoValidacion.getX509Certificate();
				if (!isEmpty(sX509Certificate)) {
					// Lo enviamos al servicio del Principado, para que nos indique si es valido.
					AutenticacionPAHelper aut = new AutenticacionPAHelper();
					try {
						// Llamamos al servicio de autenticación del Principado
						String strCIF = aut.login(sX509Certificate);
						if (!isEmpty(strCIF)) {
							// Creamos la peticion del servicio para verificar este NIF/CIF contra el WEB Servide Del internet.fechavalida
							validarCertificado = doNIFCIFValido(strCIF);
						} else {
							com.stpa.ws.server.util.Logger.error("Certificado No valida. NIF/CIF vacio.",Logger.LOGTYPE.APPLOG);
						}
					} catch(StpawsException st) {
						com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.nif.incorrecto") + "-" + st.getMessage(), st,Logger.LOGTYPE.APPLOG);
						throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),st);
					} catch (Throwable th) {
						com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.nif.incorrecto") + "-" + th.getMessage(), th,Logger.LOGTYPE.APPLOG);
						throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),th);
					}
					
				} else {
					com.stpa.ws.server.util.Logger.error("Certificado No valida. Objeto Certificado vacio." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.nif.incorrecto"),Logger.LOGTYPE.APPLOG);
					throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
				}
			} else {
				com.stpa.ws.server.util.Logger.error("Firma No valida." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.firma.no.valid"),Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
			}
			//Se ha podido validar el contenido y verificar la firma con el servicio Web adecuado.
			if (validarFirma && validarCertificado) {
				bResultado = true;
			}
		
		} else {
			// No se valida de la firma.
			return bResultado = true;
		}
		com.stpa.ws.server.util.Logger.debug("Fin CirceValidation.isAllowed(Object obj). bResultado:" + bResultado,Logger.LOGTYPE.APPLOG);
		return bResultado;
	}	
	/**
	 * Valida nulos y blancos.
	 * @param text Texto a validar.
	 * @return Boolean indicando si es un String con caracteres numericos y alfanumericos (return false).
	 */
	public static boolean isEmpty(String text) {
		return StringUtils.isBlank(text);
	}
	
	/**
	 * Verificamos si el demandante CIRCE es de tipo SLNE + AP
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE solicitud.
	 * @return Si es SLNE + AP devolverá un true.
	 */
	public static boolean isSNLE_AP(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) {
		if(CirceImpuestoTrnsADJConstantes.constantetipoTipoSociedad_SLNE.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad()) && CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_SLNE_AP.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())) {
			return true;
		}
		return false;
	}
	/**
	 * Verificamos si el demandante CIRCE es de tipo SLNE + EX
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE solicitud.
	 * @return Si es SLNE + EX devolverá un true.
	 */
	public static boolean isSNLE_EX(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) {
		if(CirceImpuestoTrnsADJConstantes.constantetipoTipoSociedad_SLNE.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad()) && CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_EX.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())) {
			return true;
		}
		return false;
	}
	/**
	 * Verificamos si el demandante CIRCE es de tipo SRL + PT
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE solicitud.
	 * @return Si es SRL + PT devolverá un true.
	 */
	public static boolean isSRL_PT(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) {
		if (CirceImpuestoTrnsADJConstantes.constantetipoTipoSociedad_SRL.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad()) && CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_SRL_PT.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())) {
			return true;
		}
		return false;
	}
	/**
	 * Verificamos si el demandante CIRCE es de tipo SRL + EX
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE solicitud.
	 * @return Si es SRL + EX devolverá un true.
	 */
	public static boolean isSRL_EX(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) {
		if (CirceImpuestoTrnsADJConstantes.constantetipoTipoSociedad_SRL.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad()) && CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_EX.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())) {
			return true;
		}
		return false;
	}
	/**
	 * Calculo fecha aplazamiento para generar una fecha valida de autoliquidacion.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE Solicitud para el cálulo de la fecha de aplazamiento
	 * @return Fecha válida para solicitar aplazamiento
	 * @throws StpawsException Error CIRCE controlado
	 */
	private static String doFechaAplazamiento(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini doFechaAplazamiento(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);		
		String fecha_identificacion = DateUtil.getFechaUHoraAsDateAplazamiento(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getFecha());
		com.stpa.ws.server.util.Logger.info("Fin doFechaAplazamiento(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		
		//Fecha de Aplazamiento calculado.
		return fecha_identificacion;
	}
	/**
	 * Cálculo de la fecha habil a partir de la fecha solicitud autoliquidacion
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto solicitud CIRCE
	 * @return fecha habil para autoliquidacion
	 * @throws StpawsException Error controlado del sistema CIRCE
	 */
	public static String doFechaHabil(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		
		//Paso 2: Calculo de la fecha de aplazamiento <identificacion><fecha>
		com.stpa.ws.server.util.Logger.info("Ini Calculo fecha doFechaHabil(): INTERNET.FechaValida",Logger.LOGTYPE.APPLOG);
		String fecha_identificacion = doFechaAplazamiento(lCirceImpuestoTrnsADJ_Solicitud);
		
		//Obtenemos la fecha de Aplazamiento llamado al Servicio Web INTERNET.FechaValida
		HashMap<String, String> resultado = WebServicesUtil.getFechaHabil(fecha_identificacion);
		String fechaHabil = resultado.get("FECHA1_CANU");
		String resultadoValidacionFechaHabil= resultado.get("CADE_CADENA");
		
		com.stpa.ws.server.util.Logger.debug("resultadoValidacionFechaHabil:" + resultadoValidacionFechaHabil,Logger.LOGTYPE.APPLOG);										
		if(CirceImpuestoTrnsADJConstantes.constanterrorRetornoGenericoWSService_KO.equals(resultadoValidacionFechaHabil)) {
			//Se ha producido error. Devolvemos la primera fecha habil.
			com.stpa.ws.server.util.Logger.error("Se ha producido error en INTERNET.FechaValida. El sistema no ha sido capaz de mandar una fecha habil.",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		com.stpa.ws.server.util.Logger.debug("Fecha Valida Devuelta: fechaHabil:" + fechaHabil,Logger.LOGTYPE.APPLOG);
		if (CirceValidation.isEmpty(fechaHabil)) {
			//Se ha producido error. Devolvemos la primera fecha habil.
			com.stpa.ws.server.util.Logger.error("Se ha producido error en INTERNET.FechaValida. El sistema ha enviado una fecha nula o en blanco.",Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		
		com.stpa.ws.server.util.Logger.info("Fin Calculo fecha doFechaHabil(): INTERNET.FechaValida",Logger.LOGTYPE.APPLOG);
		return fechaHabil; 	
	}
	/**
	 * Validamos los datos del pago bancario.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto solicitud con datos CIRCE de entrada.
	 * @return Retorna fales si alguno de los campos delpago bancario esta vacio
	 */
	public static boolean isPagoBancarioValido(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) {
		//Verificacion datos bancarios para el pago.
		com.stpa.ws.server.util.Logger.info("Ini isPagoBancarioValido(). Comprobacion Datos bancarios obligatorios para el pago telematico.",Logger.LOGTYPE.APPLOG);
		if((CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getEntidad())) || 
			(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getOficina())) || 
			(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getControl())) || 
			(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getCuenta())) || 
			(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getNif_titular()))){
			
			
			try {
				String[] codigosErrores_datBanc = StpawsUtil.doSplitErrores(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.datBanc"));
				com.stpa.ws.server.util.Logger.error("Cod error:" + codigosErrores_datBanc[0] + ":" + codigosErrores_datBanc[1],Logger.LOGTYPE.APPLOG);
			} catch (StpawsException e) {
				com.stpa.ws.server.util.Logger.error("CirceValidation.isPagoBancarioValido:" + e.getMessage(),Logger.LOGTYPE.APPLOG);
			}
				
			return false;
		} 
		
		com.stpa.ws.server.util.Logger.info("Fin isPagoBancarioValido(). Comprobacion Datos bancarios obligatorios para el pago telematico.",Logger.LOGTYPE.APPLOG);
		return true;
	}
	/**
	 * Obtener Numero de autoliquidacion y comprobacion para continuar o no hacia el Modelo 600 o generacion de los PDF de Certificados y Modelo 600.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE de la Solicitud.
	 * @return boolean indicando TRUE para realizar alta en Modelo 600; False para generar PDF sin dar de alta en el Modelo 600.
	 * @throws StpawsException Error controlado de CIRCE.
	 */
	public static boolean doNumeroAutoliquidacion(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini doNumeroAutoliquidacion(). Obtener Numero de Autoliquidacion.",Logger.LOGTYPE.APPLOG);
		
		boolean bContinuarTramitacion = false;
		
		String peticion_obtenerNumeroAutoliquidacion = WebServicesUtil.obtenerNumeroAutoliquidacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getId_tramite());
		String xmlOut_obtenerNumeroAutoliquidacion = WebServicesUtil.wsCall(peticion_obtenerNumeroAutoliquidacion, CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL);
		com.stpa.ws.server.util.Logger.debug("xmlOut: "+xmlOut_obtenerNumeroAutoliquidacion,Logger.LOGTYPE.APPLOG);
		
		// Comprobamos el mensaje retornado por el servicio Web.
		if (!WebServicesUtil.hasErrorRetornoWS(xmlOut_obtenerNumeroAutoliquidacion, "CIRCE.obtenernumauto"))  {
			// WS Retorna valores
			HashMap<String, String> resultado = WebServicesUtil.wsResponseObtenerNumAutoliquidacion(xmlOut_obtenerNumeroAutoliquidacion);			
			String numeroAutoliquidacion = resultado.get("STRING1_CANU");
			String estadoTramiteTributario = resultado.get("STRING2_CANU");
			String codErrorNumTramitacion = resultado.get("STRING_CADE");
						
			//Validaciones Paso 4
			//Comprobar codigo error devulto por el procedimiento CIRCE.obtenernumauto
			com.stpa.ws.server.util.Logger.debug("lCirceImpuestoTrnsADJ_Solicitud-->wsResponseObtenerNumAutoliquidacion:numeroAutoliquidacion:" + numeroAutoliquidacion + ",estadoTramiteTributario:" + estadoTramiteTributario + ",codErrorNumTramitacion:" + codErrorNumTramitacion,Logger.LOGTYPE.APPLOG);
			if (CirceImpuestoTrnsADJConstantes.constanterrorRetornoGenericoWSService_KO.equals(codErrorNumTramitacion)) {
				com.stpa.ws.server.util.Logger.error("Se ha producido error en paso 4. El sistema CIRCE no ha recuperado el codigo de Autoliquidacion. Codigo de error:" + codErrorNumTramitacion,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
			} else {
				com.stpa.ws.server.util.Logger.debug("CirceImpuestoTrnsADJop-->wsResponseObtenerNumAutoliquidacion:Retorno OK:numeroAutoliquidacion:" + numeroAutoliquidacion,Logger.LOGTYPE.APPLOG);
				lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setNum_itpajd(numeroAutoliquidacion.trim());
			}
			//Comprobar si pasamos al Paso 5: tramitar en el Modelo 600.
			if (CirceImpuestoTrnsADJConstantes.constantretornoTramiteTributario.equals(estadoTramiteTributario)) {
				com.stpa.ws.server.util.Logger.info("PASO 5 - Servicio CIRCE: No está dado de alta en el Modelo 600. iniciamos el paso 5.1." + codErrorNumTramitacion,Logger.LOGTYPE.APPLOG);				
				bContinuarTramitacion = true;
			} else {
				com.stpa.ws.server.util.Logger.info("PASO 5 - Servicio CIRCE: Está dado de alta en el Modelo 600. iniciamos el paso 6." + codErrorNumTramitacion,Logger.LOGTYPE.APPLOG);				
				bContinuarTramitacion = false;
			}
		}
		com.stpa.ws.server.util.Logger.info("Fin doNumeroAutoliquidacion(). Obtener Numero de Autoliquidacion.",Logger.LOGTYPE.APPLOG);
		return bContinuarTramitacion;
	}
	/**
	 * Tramitar los datos en el Modelo 600 para Autoliquidaciones CIRCE
	 * @param lCirceImpuestoTrnsADJ_Solicitud datos CIRCE para la tramitacion Modelo 600
	 * @return Booleano indicando True si le esta permitido continuar con el proceso o False para el caso contrario.
	 * @throws StpawsException error controlado por CIRCE.
	 */
	public static boolean doIntegrarModelo600 (CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini doIntegrarModelo600(). Ingerar datos en el Moelo 600 para Autoliquidaciones CIRCE.",Logger.LOGTYPE.APPLOG);
		
		//Paso 5 Llamar al procedimiento CIRCE.RecibirDatosCirce (Paso 5.1)
		com.stpa.ws.server.util.Logger.debug("Ini Calculo fecha CIRCE.RecibirDatosCirce",Logger.LOGTYPE.APPLOG);		
		boolean bAltaDatosModelo600 = false;
		
		//Asignamos Constante Fija 0,00 para el valor de Ingreso_Importe: POR PETICION DEL SERVICIO. NO QUITAR!!!
		lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setIngreso_importe(CirceImpuestoTrnsADJConstantes.constante_NININGRESO);
		String peticion_obtenerIntegrarModelo600 = WebServicesUtil.obtenerIntegrarModelo600(lCirceImpuestoTrnsADJ_Solicitud);
		String xmlOut_obtenerIntegrarModelo600 = WebServicesUtil.wsCall(peticion_obtenerIntegrarModelo600, CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL);
		com.stpa.ws.server.util.Logger.debug("xmlOut: "+xmlOut_obtenerIntegrarModelo600,Logger.LOGTYPE.APPLOG);
		
		// Comprobamos si devuelve una estructura de ERROR. Si lo hace, lanzamos una exception StpawsException
		if (!WebServicesUtil.hasErrorRetornoWS(xmlOut_obtenerIntegrarModelo600,"CIRCE.RecibirDatosCirce")) {										
			bAltaDatosModelo600 = true;			
		}
		
		//Retorno datos.
		com.stpa.ws.server.util.Logger.info("Fin doIntegrarModelo600(). Ingerar datos en el Moelo 600 para Autoliquidaciones CIRCE.",Logger.LOGTYPE.APPLOG);
		return bAltaDatosModelo600;
	}
	/**
	 * Obtenemos la Verificación del Acceso para CIRCE del SOLICITANTE según nif/cif facilitado
	 * @param nifCIFSolicitante Nif/Cif solicitante.
	 * @return Tiene acceso (true) o carece de el (false).
	 * @throws StpawsException Error controlado por el Servicio durante la ejecución de la peticion.
	 */
	public static boolean doNIFCIFValido(String nifCIFSolicitante) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini doNIFCIFValido(). Obtener comprobar Acceso valido para SOLICITANTE.",Logger.LOGTYPE.APPLOG);		
		boolean bContinuarTramitacion = false;
		
		String peticion_obtenerNIFCIFValido = WebServicesUtil.obtenerAccesoCIRCE(nifCIFSolicitante);
		String xmlOut_obtenerNIFCIFValido = WebServicesUtil.wsCall(peticion_obtenerNIFCIFValido, CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL);
		
		// Comprobamos el mensaje retornado por el servicio Web.
		if (!WebServicesUtil.hasErrorRetornoWS(xmlOut_obtenerNIFCIFValido, "internet.fechavalida"))  {
			// WS Retorna valores
			HashMap<String, String> resultado = WebServicesUtil.wsResponseNIFCIFValido(xmlOut_obtenerNIFCIFValido);			
			String codErrorAutorizaionServicioSolicitante = resultado.get("STRING_CADE");
			if (!isEmpty(codErrorAutorizaionServicioSolicitante)) {
				if (codErrorAutorizaionServicioSolicitante.equals(CirceImpuestoTrnsADJConstantes.constanterrorRetornoGenericoWSService_OK)) {
					bContinuarTramitacion = true;
				}
			} 
		}
		
		com.stpa.ws.server.util.Logger.info("Fin doNIFCIFValido(). Obtener comprobar Acceso valido para SOLICITANTE",Logger.LOGTYPE.APPLOG);
		return bContinuarTramitacion;
	}
	/**
	 * Obtenemos si el contenido firmado concuerda con su firma.
	 * @param ficheroCirce Fichero Contenido & Firma Contenido.
	 * @return objeto respuesta de la firma.
	 * @throws StpawsException Error controlado por el proceso de Validación Contenido.
	 */
	public BeanRespValidar isFirmaEstandarCirceValid(String ficheroCirce) throws StpawsException {
    	
    	BeanRespValidar bRV = null;    	
    	try {
    		ValidacionEstandar vE = new ValidacionEstandar();
    		bRV = vE.doValidacionFirmaCirce(ficheroCirce);
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("CirceValidation, isFirmaEstandarCirceValid: "+e.getMessage(), e,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
		}
        return bRV;
    }

}
