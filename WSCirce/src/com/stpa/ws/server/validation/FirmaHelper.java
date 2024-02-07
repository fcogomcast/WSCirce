package com.stpa.ws.server.validation;

import java.io.ByteArrayInputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;

import org.bouncycastle.util.encoders.Base64;

import com.stpa.ws.firma.util.ExtraccionYTratamientoFicheros;
import com.stpa.ws.firma.util.Pkcs7Verify;
import com.stpa.ws.firma.util.UtilsWBFirma;
import com.stpa.ws.handler.HandlerUtil;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PropertiesUtils;

import es.tributasenasturias.firmadigital.FirmaDigital;
import es.tributasenasturias.firmadigital.WsFirmaDigital;

/**
 * Firma Helper ayuda a firmar el mensaje de retorno a CIRCE
 *
 */

public class FirmaHelper {
	private Preferencias pref = new Preferencias();
	/* Constructor */
	public FirmaHelper () {
		pref.CompruebaFicheroPreferencias();		
	}
	
	/**
	 * Crea y devuelve un mensaje SOAP con el contenido indicado en el string.
	 * 
	 * @param xmlContent
	 *            String con el mensaje SOAP en texto.
	 * @return
	 */
	public SOAPMessage creaMensajeSOAP(String xmlContent) throws SOAPException {
		SOAPMessage msg = null;
		SOAPPart part = null;
				
		MessageFactory msgFactory = MessageFactory.newInstance();		
		msg = msgFactory.createMessage();		
		part = msg.getSOAPPart();
		
		ByteArrayInputStream bas;
		try {		
			bas = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));	
			StreamSource strs = new StreamSource(bas);		
			part.setContent(strs);		
		}catch (Exception e) {			
			com.stpa.ws.server.util.Logger.error("FirmaHelper:creaMensajeSOAP():Error:"+e.getMessage(),e, Logger.LOGTYPE.APPLOG);			
		}			
		return msg;
	}
	
	/**
	 * Genera un SOAPMessage firmado a partir de un SOAPMessage de entrada.
	 * 
	 * @param contenidoAFirmar Contenido a firmar en formato String. 
	 * @param firmaBinaria. Indica si se firmará la representación binaria de <b>contenidoAFirmar<b>
	 * 						o la representación textual. false=representación textual, true=  binaria
	 * @return firmado SOAP String mensaje firmado.
	 */
	public String firmaMensaje(String contenidoAFirmar, boolean firmaBinaria) throws StpawsException {
		//SOAPMessage firmado;
		String firmado = null;
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		try {			
			// cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();
			//En caso de firma binaria, ya estamos utilizando Base64
			if (!firmaBinaria)
			{
				//Codificamos el mensaje a firmar para evitar problemas de codificacion en el envio del mensaje por medio del Servicio WEB.
				contenidoAFirmar = new String(Base64.encode(contenidoAFirmar.getBytes(CirceImpuestoTrnsADJConstantes.xmlEncodig)),CirceImpuestoTrnsADJConstantes.xmlEncodig);
			}
			if (pref.getM_debug().equals("1")) {				
				com.stpa.ws.server.util.Logger.info("FirmaHelper:firmaMensaje():contenidoAFirmar:"+contenidoAFirmar,Logger.LOGTYPE.APPLOG);
				com.stpa.ws.server.util.Logger.info("FirmaHelper:firmaMensaje():pref.getEndpointFirma():"+pref.getM_endPointFirma(),Logger.LOGTYPE.APPLOG);			
				com.stpa.ws.server.util.Logger.info("FirmaHelper:firmaMensaje():pref.getEndpointFirma():"+pref.getM_claveFirma(),Logger.LOGTYPE.APPLOG);
				com.stpa.ws.server.util.Logger.info("FirmaHelper:firmaMensaje():pref.getCertificadoFirma():"+pref.getM_certificadoFirma(),Logger.LOGTYPE.APPLOG);
			}
			
			if (CirceValidation.isEmpty(contenidoAFirmar)) {
				com.stpa.ws.server.util.Logger.error("FirmaHelper:firmaMensaje():Se ha producido un error durante la generacion de la Firma. Mensaje a firmar vacio.",Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
			}
			
			//Se modifica el endpoint
			String endpointFirma = pref.getM_endPointFirma();			
			if (!endpointFirma.equals("")) {
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						endpointFirma);
			}
			
			HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) srPort);
						
			try {																			
				// Recuperamos la clave y certificado de firma.
				String certificado = pref.getM_certificadoFirma();
				String clave = pref.getM_claveFirma();
																			  
			    String msgFirmado = srPort.firmarCIRCE(contenidoAFirmar, certificado, clave,firmaBinaria);
			    //Decodificamos el mensaje entrante para comparar ONTENIDO con FIRMA.
			    contenidoAFirmar = new String(Base64.decode(contenidoAFirmar),CirceImpuestoTrnsADJConstantes.xmlEncodig);
			    			    						 
			    if (pref.getM_debug().equals("1")) {			   
			    	com.stpa.ws.server.util.Logger.info("FirmaHelper:firmaMensaje():MENSAJE FIRMADO:"+msgFirmado,Logger.LOGTYPE.APPLOG);				    				   
			    }    
				    //Validamos la firma saliente
			    	boolean resultadoVerificacionFirma = false;
			    	String rutaFicheroContenido = pref.getM_pathSolresp() + CirceImpuestoTrnsADJConstantes.nombreFicheroContenido + UtilsWBFirma.getDate() + ".txt";
			    	String rutaFicheroFirma		= pref.getM_pathSolresp() + CirceImpuestoTrnsADJConstantes.nombreFicheroFirma + UtilsWBFirma.getDate() + ".p7s";
			    	boolean bFicheroContenido 	= ExtraccionYTratamientoFicheros.crearFicheroSalida(contenidoAFirmar, rutaFicheroContenido);
			    	boolean bFicheroFirma 		= ExtraccionYTratamientoFicheros.crearFicheroSalida(msgFirmado, rutaFicheroFirma);
			    	if (bFicheroContenido && bFicheroFirma) {
			    		Pkcs7Verify pv = new Pkcs7Verify(rutaFicheroContenido, rutaFicheroFirma);
			    		resultadoVerificacionFirma = pv.verifyPkcs7();
			    	} 
			    // String firmado saliente.	
			    if (resultadoVerificacionFirma) {
			    	firmado = msgFirmado;
			    	ExtraccionYTratamientoFicheros.borradoFichero(rutaFicheroContenido);
			    	ExtraccionYTratamientoFicheros.borradoFichero(rutaFicheroFirma);			    	
			    } else {
			    	com.stpa.ws.server.util.Logger.error("FirmaHelper:firmaMensaje():Integridad firma saliente erronea.",Logger.LOGTYPE.APPLOG);
					throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
			    }
			} catch (Exception ex) {
				com.stpa.ws.server.util.Logger.error("FirmaHelper:firmaMensaje():Error al firmar el mensaje de salida: " + ex.getMessage(), ex,Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),ex);
			}
		} catch (Exception ex) {
			com.stpa.ws.server.util.Logger.error("FirmaHelper:firmaMensaje():Error al firmar el mensaje de salida: " + ex.getMessage(), ex,Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),ex);
		}	  
		return firmado;
	}	

}
