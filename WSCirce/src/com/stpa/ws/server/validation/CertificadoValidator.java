package com.stpa.ws.server.validation;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Base64;
import com.stpa.ws.server.util.Logger;

public class CertificadoValidator implements IValidator<String> {
	Preferencias pref = new Preferencias();
	ResultadoValidacion res;

	public String getCertificadoCabecera(String datosXML) {
		String certificado = "";
		// Recuperamos la lista de nodos de la cabecera con certificado. Sólo
		// debería haber uno.
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();

			db = fact.newDocumentBuilder();
			fact.setNamespaceAware(true);
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(datosXML));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NodeList certificados = m_resultadoXML.getElementsByTagName("dsig:X509Certificate");

			if (certificados != null && certificados.getLength() != 0) {
				certificado = certificados.item(0).getTextContent();

			} else {
				certificado = null;
			}

		} catch (Exception ex) {
			certificado = null;
			com.stpa.ws.server.util.Logger.error("Error al parsear el certificado:" + ex.getMessage(), ex, Logger.LOGTYPE.APPLOG);
		}

		return certificado;
	}

	public CertificadoValidator() {		
		res = new ResultadoValidacion();
	}

	@Override
	public IResultadoValidacion getResultado() {
		return res;
	}

	@Override
	public boolean isValid(String datosXML) {			
		boolean valido = false;
		// Recuperamos el certificado.
		String certificado = getCertificadoCabecera(datosXML);		
		
		if (certificado != null && !certificado.equals("")) {
			// Lo enviamos al servicio del Principado, para que nos indique si
			// es válido.
			AutenticacionPAHelper aut = new AutenticacionPAHelper();

			try {
				// Llamamos al servicio de autenticación del Principado
				String strCIF = aut.login(certificado);					
				
				if (strCIF!=null) 
					com.stpa.ws.server.util.Logger.debug("HA SUPERADO LA AUTENTICACION DE CERTIFICADO DEL PRINCIPADO.", Logger.LOGTYPE.APPLOG);				
				
				
				if (strCIF==null) {					
					com.stpa.ws.server.util.Logger.debug("NO SUPERA AUTENTICACION DEL PRINCIPADO", Logger.LOGTYPE.APPLOG);
					
					String certificadoBase64 = certificado;
					X509Certificate cert;
					byte[] arr = Base64.decode(certificadoBase64.toCharArray());
					CertificateFactory cf;
					cf = CertificateFactory.getInstance("X.509");
					ByteArrayInputStream bais = new ByteArrayInputStream(arr);
					cert = (X509Certificate) cf.generateCertificate(bais);

					String principalName = cert.getSubjectDN().getName();
					principalName = principalName.substring(principalName
							.indexOf("CIF"), principalName.length());
					principalName = principalName.replaceAll("CIF", "").trim();

					strCIF = principalName.substring(0,
							principalName.indexOf(",")).toUpperCase();
				}
				
				
			} catch (StpawsException ex) {
				com.stpa.ws.server.util.Logger.error("Error al validar la autorización:" + ex.getMessage(), ex, Logger.LOGTYPE.CLIENTLOG);
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			} catch (CertificateException e) {
				com.stpa.ws.server.util.Logger.error("Error al validar el certificado.",Logger.LOGTYPE.APPLOG);
				res.addMessage("No se ha podido validar el certificado.");
				valido = false;
			} 

		} else {
			valido = false;
		}
		return valido;
	}
}