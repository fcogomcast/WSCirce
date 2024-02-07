package com.stpa.ws.server.op;

import com.stpa.ws.acceso.bean.BeanFicheroSolicitudRespuesta;
import com.stpa.ws.firma.util.ExtraccionYTratamientoFicheros;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.base.IStpawsBase;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Respuesta;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.bean.TratamientoErrores;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.CirceCall;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PDFUtil;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.XMLUtils;
import com.stpa.ws.server.validation.CirceValidation;

public class CirceImpuestoTrnsADJop implements IStpawsBase {
	// carga preferencias del sistema.
	Preferencias pref = new Preferencias();	
	/* Inicio del lanzador de CIRCE. Logica del Negocio.
	 * @see com.stpa.ws.server.base.IStpawsBase#doOwnAction(java.lang.Object)
	 */
	public Object doOwnAction(Object objIn) {
		com.stpa.ws.server.util.Logger.info("PASO 0 -- Entro en el servicio CIRCE - CirceImpuestoTrnsADJop.doOwnAction().",Logger.LOGTYPE.APPLOG);
		//Creamos el Objeto CIRCE de entrada
		CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud = new CirceImpuestoTrnsADJ_Solicitud();
		//Creamos el Objeto de Respuesta
		CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta = new CirceImpuestoTrnsADJ_Respuesta();
		//creamos el objeto CONTROL DEL xml de la SOLICITUD y RESPUESTA
		BeanFicheroSolicitudRespuesta beanSolresp = new BeanFicheroSolicitudRespuesta();
		/*
		 * Control de errores respuesta CIRCE
		 * Inicialmente se cargara como OK
		 */
		TratamientoErrores tError = new TratamientoErrores();
		try {
			tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoOK,CirceImpuestoTrnsADJConstantes.constanterrorRetornoOK,PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.ok"));
			lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
		} catch(StpawsException stpa) {
			com.stpa.ws.server.util.Logger.error("CirceImpuestoTrnsADJop.doOwnAction()- StpawsException - Inicializar datos errores:" + stpa.getMessage(), stpa,Logger.LOGTYPE.APPLOG);
		} catch (Throwable tw) {
			com.stpa.ws.server.util.Logger.error("CirceImpuestoTrnsADJop.doOwnAction() - Throwable - Inicializar datos errores:" + tw.getMessage(), tw,Logger.LOGTYPE.APPLOG);
		}
		
		//Fecha Habil para el calculo autoliquidacion
		String fechaHabil = new String("");
		
		if (objIn instanceof CirceImpuestoTrnsADJ_Solicitud) {			
			/*
			 * XML Objeto Solicitud CIRCE
			 */
			lCirceImpuestoTrnsADJ_Solicitud = (CirceImpuestoTrnsADJ_Solicitud)objIn;
			/*
			 * Rellenamos el objeto CIRCE SOLICITUD en caso de venir informado el campo sCirceSolicitud de la solicitud.
			 * 
			 */
			String xml_entradad_Desde_CIRCE = lCirceImpuestoTrnsADJ_Solicitud.getSCirceSolicitud();
			com.stpa.ws.server.util.Logger.info("CirceImpuestoTrnsADJop.doOwnAction() - xml_entradad_Desde_CIRCE:" + xml_entradad_Desde_CIRCE,Logger.LOGTYPE.CLIENTLOG);
			if (!CirceValidation.isEmpty(xml_entradad_Desde_CIRCE)) {
				beanSolresp = StpawsUtil.setFicheroSolicitudRespuesta(beanSolresp,pref);
				
				//Fichero Solicitud.
				boolean bFicheroSolicitud = ExtraccionYTratamientoFicheros.crearFicheroSalida(xml_entradad_Desde_CIRCE, beanSolresp.getFicheroSolicitud());
				beanSolresp.setCreadoFicheroSolicitud(bFicheroSolicitud);
								
				lCirceImpuestoTrnsADJ_Solicitud = CirceCall.createObjetCIRCE(lCirceImpuestoTrnsADJ_Solicitud, xml_entradad_Desde_CIRCE);
			}
			/*
			 * Booleano incicando si se continua o no con el servicio.
			 */
			boolean bContinuarValidaciones = true;			
			
			try {	
				/*
				 * Volcado datos a enviar de respuesta a CIRCE.
				 */
				lCirceImpuestoTrnsADJ_Respuesta = StpawsUtil.setXMLCirce_Respuesta(lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
				/*
				 * Llamada al servicio de VALIDACION firma, VERIFICACION certificado, VERIFICACION acceso al servicio CIRCE.
				 * "True" Si el solicitante esta habilitado para acceder al servicio de autoliquidacion.
				 * Clase de Validacion FIRMA entrante; Servicio PXAutenticacionPA.
				 */				
				CirceValidation circeV = new CirceValidation();
				if(!circeV.isAllowed(lCirceImpuestoTrnsADJ_Solicitud)){
					com.stpa.ws.server.util.Logger.info("Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction().isAllowed(): KO para Validacion FIRMA, Verificacion CERTIFICADO, Verificación ACCESO para peticionario.",Logger.LOGTYPE.APPLOG);
					bContinuarValidaciones = false;					
				}
				
				/*
				 * Validamos los datos del XML y llamamos a los diversos servicios.
				 */
				if (bContinuarValidaciones){					
					/*
					 * Validamos los campos obligatorios y por formato.
					 */					
					if (StpawsUtil.isAccessFieldValidated(lCirceImpuestoTrnsADJ_Solicitud)) {
						com.stpa.ws.server.util.Logger.info("PASO 1 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction().isAccessFieldValidated(): OK para todos los campos de entrada a VALIDAR.",Logger.LOGTYPE.APPLOG);
						com.stpa.ws.server.util.Logger.debug("doOwnAction Tipo_Sociedad: " + lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getTipo_Sociedad(),Logger.LOGTYPE.APPLOG);
						com.stpa.ws.server.util.Logger.debug("doOwnAction Tipo_Confirmacion: " + lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion(),Logger.LOGTYPE.APPLOG);
						
						//Ini comprobamo el TIPO SOCIEDAD y el TIPO CONFIRMACION
						if(CirceValidation.isSNLE_AP(lCirceImpuestoTrnsADJ_Solicitud)) {
							
							//Paso 2: Calculo de la fecha de aplazamiento <identificacion><fecha>
							com.stpa.ws.server.util.Logger.info("PASO 2 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction().isSNLE_AP().",Logger.LOGTYPE.APPLOG);
							/*
							 * Controlamos en el interio del metodo si el Servicio devuelve un error PL controlado y si la variable fechaHabil es nula/vacio/blanco.
							 */
							fechaHabil = CirceValidation.doFechaHabil(lCirceImpuestoTrnsADJ_Solicitud);						
							
						} else if(CirceValidation.isSRL_PT(lCirceImpuestoTrnsADJ_Solicitud)) {
							//Paso 3: Verificacion datos bancarios para el pago.
							com.stpa.ws.server.util.Logger.info("PASO 3 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction().isSRL_PT().",Logger.LOGTYPE.APPLOG);
							
							if(!CirceValidation.isPagoBancarioValido(lCirceImpuestoTrnsADJ_Solicitud)) {
								bContinuarValidaciones = false;
								String [] codigosErrores_datBanc = StpawsUtil.doSplitErrores(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.datBanc"));
								com.stpa.ws.server.util.Logger.error("Cod error:" + codigosErrores_datBanc[0] + ":" + codigosErrores_datBanc[1],Logger.LOGTYPE.APPLOG);
								tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,codigosErrores_datBanc[0],codigosErrores_datBanc[1]);
								lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
							}					
							
						} else {
							if (!CirceValidation.isSNLE_EX(lCirceImpuestoTrnsADJ_Solicitud) && 
									!CirceValidation.isSRL_EX(lCirceImpuestoTrnsADJ_Solicitud))
							{
								bContinuarValidaciones = false;
								String [] codigosErrores_confPago = StpawsUtil.doSplitErrores(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.confPago"));
								com.stpa.ws.server.util.Logger.error("Cod error:" + codigosErrores_confPago[0] + ":" + codigosErrores_confPago[1],Logger.LOGTYPE.APPLOG);
								tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,codigosErrores_confPago[0],codigosErrores_confPago[1]);
								lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
							}
						}
						//Fin comprobamo el TIPO SOCIEDAD y el TIPO CONFORMACION
					
						//Iniciamos le resto de llamadas a Servicios CIRCE: CIRCE.obtenernumauto; CIRCE.recibirDatosCirce y Alta de los datos&Generacion PDF (Modelo 600 & Certificados).
					if (bContinuarValidaciones) {	
						boolean bIntegrarModelo600 	= false;
						boolean generaPDFs 			= false;
						boolean generaFirmaDigital 	= false;
						
						com.stpa.ws.server.util.Logger.info("PASO 4 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Ini Calculo numero autoliquidación para fecha habil facilitada a CIRCE.obtenernumauto.",Logger.LOGTYPE.APPLOG);
						//=================================
						// Obtencion Numero Autoliquidacion
						//=================================
						bIntegrarModelo600 = CirceValidation.doNumeroAutoliquidacion(lCirceImpuestoTrnsADJ_Solicitud);
						//=====================================
						// Fin Obtencion Numero Autoliquidacion
						//=====================================
						com.stpa.ws.server.util.Logger.debug("bIntegrarModelo600:" + bIntegrarModelo600,Logger.LOGTYPE.APPLOG);
						com.stpa.ws.server.util.Logger.info("PASO 4 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Fin Calculo numero autoliquidación para fecha habil facilitada a CIRCE.obtenernumauto.",Logger.LOGTYPE.APPLOG);
						//C.R.V. 02/08/2011. Se integra también en función de la constante de preferencias.
						if (bIntegrarModelo600 && pref.getM_integrarTributas().equals(CirceImpuestoTrnsADJConstantes.constantIntegrarTributas)) {
							com.stpa.ws.server.util.Logger.info("PASO 5 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Ini Integracion Modelo 600 y creacion PDF Modelo 600.",Logger.LOGTYPE.APPLOG);
							//=============================
							// Generacion Alta Modelo 600
							//=============================
							bIntegrarModelo600 = CirceValidation.doIntegrarModelo600(lCirceImpuestoTrnsADJ_Solicitud);							
							//Creamos el PDF del Modelo 600.
							if (bIntegrarModelo600) {
								bIntegrarModelo600 = generarPdfModelo600(lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
							}
							//===============================
							// Fin Generacion Alta Modelo 600
							//===============================							
							com.stpa.ws.server.util.Logger.info("PASO 5 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Fin Integracion Modelo 600 y creacion PDF Modelo 600.",Logger.LOGTYPE.APPLOG);
						}
						//===================================
						//  Carga Objeto de Respuesta con los datos actualizados.
						//===================================
						lCirceImpuestoTrnsADJ_Respuesta = StpawsUtil.setXMLCirce_Respuesta(lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
						//===================================
						//  Fin Carga Objeto de Respuesta
						//===================================						
						com.stpa.ws.server.util.Logger.info("PASO 6 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Ini Alta y generacion de PDF Certificados.",Logger.LOGTYPE.APPLOG);
						//=============================
						// Generacion PDFs
						//=============================
						//C.R.V. 02/08/2011. Se integra en función de la constante de preferencias, en caso de no integrar no se genera tampoco el PDF.
						if (pref.getM_integrarTributas().equals(CirceImpuestoTrnsADJConstantes.constantIntegrarTributas))
						{
							lCirceImpuestoTrnsADJ_Respuesta = generarPdfCertificado(lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta, fechaHabil);
						}
						// validamos si el contenido CERTIFICADO ha sido rellenado.
						if (!CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().getContenido())) {
							generaPDFs = true;
						} else {
							com.stpa.ws.server.util.Logger.error("PASO 6 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Alta y generacion de PDF Certificados.",Logger.LOGTYPE.APPLOG);
							// Generamos las trazas de errores
							tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"));
							lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
						}
						//=============================
						// Fin Generacion PDFs
						//=============================
						com.stpa.ws.server.util.Logger.info("PASO 6 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Fin Alta y generacion de PDF Certificados.",Logger.LOGTYPE.APPLOG);
						if (generaPDFs) {
							com.stpa.ws.server.util.Logger.info("PASO 7 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Ini Firmado Digital del mensaje de vuelta a CIRCE.",Logger.LOGTYPE.APPLOG);
							//=============================
							// Firmamos salida del Servicio
							//=============================
							lCirceImpuestoTrnsADJ_Respuesta = StpawsUtil.generarFirmaCIRCE(lCirceImpuestoTrnsADJ_Respuesta);
							generaFirmaDigital = true;
							//=============================
							// FIN Firmamos salida del Servicio
							//=============================
							com.stpa.ws.server.util.Logger.info("PASO 7 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Fin Firmado Digital del mensaje de vuelta a CIRCE.",Logger.LOGTYPE.APPLOG);
						}
						if(generaFirmaDigital) {
							com.stpa.ws.server.util.Logger.info("PASO 8 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Ini Alta documento del mensaje de vuelta a CIRCE.",Logger.LOGTYPE.APPLOG);
							//============================================
							// Inicio Generacion XML de salida hacia CIRCE
							//============================================
							StpawsUtil.altaDocumentoXMLRespuesta(lCirceImpuestoTrnsADJ_Respuesta, beanSolresp);
							//============================================
							// Fin Generacion XML de salida hacia CIRCE
							//============================================
							com.stpa.ws.server.util.Logger.info("PASO 8 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). Fin Alta documento del mensaje de vuelta a CIRCE.",Logger.LOGTYPE.APPLOG);
						} else {
							com.stpa.ws.server.util.Logger.info("PASO 9 - Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction(). No se ha podido integrar la SOLICITUD con la RESPUESTA a enviar a CIRCE.",Logger.LOGTYPE.APPLOG);
						}
						
					}
					// Fin if Booleano bContinuarValidaciones
					} else {
						bContinuarValidaciones = false;
						com.stpa.ws.server.util.Logger.error("PASO 1 - Fin Servicio CIRCE: CirceImpuestoTrnsADJop.doOwnAction().isAccessFieldValidated(): KO. Ver detalle del error retornado en los logs Application del servicio CIRCE.",Logger.LOGTYPE.APPLOG);
					}
				} else {
				// faltan campos validos
					com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.valiverif") + "Verifique el XML de Solicitud. Imposible Validar y/o Verificar los datos.",Logger.LOGTYPE.APPLOG);
					// Generamos las trazas de errores
					tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"));
					lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);						
				}
				}catch(StpawsException stpae){
					com.stpa.ws.server.util.Logger.error(stpae.getError(),stpae,Logger.LOGTYPE.APPLOG);
					tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,stpae.getError());
					lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);					
				}catch(Throwable te){
					String s = "";
					String s1 = "";
					try{
						s = PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen");
						s1 = PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.conexion");
					}catch(Throwable t){
						com.stpa.ws.server.util.Logger.error(t.getMessage(),t,Logger.LOGTYPE.APPLOG);
					}
					com.stpa.ws.server.util.Logger.error(s1 + te.getMessage(),te,Logger.LOGTYPE.APPLOG);					
					tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,s +te.getMessage());
					lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);					
				}
			}else{
				try {
					com.stpa.ws.server.util.Logger.error("Error al intanciar el objeto CirceImpuestoTrnsADJ_Solicitud." + PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.obj.no.valid"),Logger.LOGTYPE.APPLOG);
					tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"));
					lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);									
				} catch (StpawsException stpae) {
					com.stpa.ws.server.util.Logger.error("Error al tratar de rellenar el Objeto Retorno: Mensaje;" + stpae.getMessage(),stpae,Logger.LOGTYPE.APPLOG);
					tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,stpae.getError());
					lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);					
				} catch (Throwable te) {
					com.stpa.ws.server.util.Logger.error("Error al intanciar el objeto CirceImpuestoTrnsADJ_Solicitud: Mensaje;" + te.getMessage(),te,Logger.LOGTYPE.APPLOG);
					tError = rellenarRetonoErrores(CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO,te.getMessage());
					lCirceImpuestoTrnsADJ_Respuesta = tratarError(tError, lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
				} 
			}
		//Valor retorno.
		com.stpa.ws.server.util.Logger.info("PASO 0 -- Fin del proceso servicio CIRCE - CirceImpuestoTrnsADJop.doOwnAction().",Logger.LOGTYPE.APPLOG);
		return lCirceImpuestoTrnsADJ_Respuesta;
	}
	/**
	 * Tratamiento de los errores para la salida de los datos.<br>En caso de error, se genera la traza de error a retornar.
	 * @param tError Objeto con la carga del dato de error.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto SOLICITUD desde CIRCE.
	 * @param lCirceImpuestoTrnsADJ_Respuesta Objeto RESPUESTA hacia CIRCE.
	 * @return Retornamos el objeto rellenado para la respuesta hacia CIRCE.
	 */
	private CirceImpuestoTrnsADJ_Respuesta tratarError(TratamientoErrores tError,CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta){		
		if(lCirceImpuestoTrnsADJ_Respuesta==null) lCirceImpuestoTrnsADJ_Respuesta = new CirceImpuestoTrnsADJ_Respuesta();
		if(lCirceImpuestoTrnsADJ_Solicitud==null) lCirceImpuestoTrnsADJ_Solicitud = new CirceImpuestoTrnsADJ_Solicitud();
		
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getIdentificacion().setRetorno(tError.getRetorno());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getResultado().setReturncode(tError.getRetorno());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getResultado().getErrores().setCoderror(tError.getCodigoError());
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getResultado().getErrores().setTexterror(tError.getDescripcionError());
		
		// Retorno del objeto rellenado el error.
		com.stpa.ws.server.util.Logger.info("CirceImpuestoTrnsADJop:tratarError(). getRetorno:" +tError.getRetorno() + "-getCodigoError:" + tError.getCodigoError() + "-getDescripcionError:" + tError.getDescripcionError(),Logger.LOGTYPE.APPLOG);
		//Si se genera un error durante el proceso, borramos el numero de autiloquidación como código de retorno.
		if (CirceImpuestoTrnsADJConstantes.constanterrorRetornoKO.equals(tError.getRetorno())){
			lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setNum_itpajd("");
		}
		/*
		 * Volcado datos a enviar de respuesta a CIRCE.
		 */		
		try {
			lCirceImpuestoTrnsADJ_Respuesta = StpawsUtil.setXMLCirce_Respuesta(lCirceImpuestoTrnsADJ_Solicitud, lCirceImpuestoTrnsADJ_Respuesta);
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("Error durante la generacion des objeto respuesta hacia CIRCE. Exception:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
		
		//Generamos el XML de salida hacia CIRCE.
		lCirceImpuestoTrnsADJ_Respuesta.setSCirceRespuesta(XMLUtils.stringCirceRespuesta(lCirceImpuestoTrnsADJ_Respuesta));
		return lCirceImpuestoTrnsADJ_Respuesta;
	}
	
	/**
	 * Relleno de los codigos de error.
	 * @param retorno Codigo 0 para OK y 1 para KO
	 * @param codigoError Codigo interno del error.
	 * @param descripcionError Descripcion del error.
	 * @return Objeto con los datos del error relleno.
	 */	
	private TratamientoErrores rellenarRetonoErrores(String retorno, String codigoError, String descripcionError) {
	
		TratamientoErrores tratamientoErr = new TratamientoErrores();
		
		if (CirceValidation.isEmpty(retorno) && CirceValidation.isEmpty(codigoError)) {
			try {
				com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.decr.no.valid"),Logger.LOGTYPE.APPLOG);
			} catch (StpawsException stpae) {
				com.stpa.ws.server.util.Logger.error(stpae.getMessage(),Logger.LOGTYPE.APPLOG);				
			} 
		} else {
			tratamientoErr.setRetorno(retorno);
			tratamientoErr.setCodigoError(codigoError);
			tratamientoErr.setDescripcionError(descripcionError);
		}
		// Valor de retorno.
		return tratamientoErr;		
	}
	/**
	 * Generacion de los distintos modeos de PDF (Certificados).
	 * @param lCirceImpuestoTrnsADJ datos Salida XML - Respuesta.
	 * @param fechaHabil Fecha habil para autoliquidacion aplazada.
	 * @return Objeto Retorno hacia Servicio CIRCE.
	 * @throws StpawsException Error controlado CIRCE.
	 */	
	private CirceImpuestoTrnsADJ_Respuesta generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta, String fechaHabil) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Inicio generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		/*
		 * Generamos el Certificados para el tipo de Dato entrante. PDF & Alta documento en la Base de Datos.
		 */
		String modeloPdf = PDFUtil.generarPdfCertificado(lCirceImpuestoTrnsADJ_Solicitud, fechaHabil);
		//fran
		lCirceImpuestoTrnsADJ_Respuesta.getXml().getFichero().getElementos().getDatos_interlocutores().getCertificacion().setContenido(modeloPdf);

		com.stpa.ws.server.util.Logger.info("Fin generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		return lCirceImpuestoTrnsADJ_Respuesta;
	}
	/**
	 * Generacion de los distintos modeos de PDF (Modelo 600).
	 * @param lCirceImpuestoTrnsADJ datos Salida XML - Respuesta.
	 * @return boolean indicando que el proceso ha ido correcatmente.
	 * @throws StpawsException Error controlado CIRCE.
	 */	
	private boolean generarPdfModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Inicio generarPdfModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		/*
		 * Generamos el Modelo 600 CIRCE
		 */	
		boolean resultado = false;
		resultado = PDFUtil.generarPdfModelo600(lCirceImpuestoTrnsADJ_Solicitud,lCirceImpuestoTrnsADJ_Respuesta);			
		
		com.stpa.ws.server.util.Logger.info("Fin generarPdfModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		return resultado;
	}
	
}
