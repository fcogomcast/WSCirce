package com.stpa.ws.server.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Deflater;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.SimpleBookmark;
import com.stp.webservices.utils.Base64;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Respuesta;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.formularios.FormBeanAplPTel;
import com.stpa.ws.server.formularios.FormBeanCertificados;
import com.stpa.ws.server.formularios.FormBeanModelo600;
import com.stpa.ws.server.formularios.FormFillBase;
import com.stpa.ws.server.formularios.FormPDFBase;
import com.stpa.ws.server.formularios.JustificantePresentacion;
import com.stpa.ws.server.util.Logger.LOGTYPE;
import com.stpa.ws.server.validation.CirceValidation;

public class PDFUtil extends FormFillBase {

	public static boolean bAutoliquidacionComplementaria = false;
	/**
	 * Generar el PDF para Certificados No Sujetos
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto salida de CIRCE.
	 * @return String de salida del PDF.
	 * @throws StpawsException Error controlado STPA.
	 */
	public static String generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, String fechaHabil) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Inicio en generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ, String fechaHabil).",LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("fechaHabil:" + fechaHabil,LOGTYPE.APPLOG);
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ByteArrayOutputStream[] bufferArray = new ByteArrayOutputStream[]{new ByteArrayOutputStream()};
		//Buffer Salida para grabar datos
		ByteArrayOutputStream buffer_xml = new ByteArrayOutputStream();
		
		boolean isvalid = false;
		//Numero autoliquidacion.
		String sNum_itpajd = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd();
		
		//Paso 1: Rellenar bean:	
		Object objOut = tipoCertificado(lCirceImpuestoTrnsADJ_Solicitud);
		Object fBean=rellenarBeanCertificado(lCirceImpuestoTrnsADJ_Solicitud, objOut, fechaHabil);
		//Paso 1.1: Iniciamos el Objeto Justificante.
		JustificantePresentacion oJustificantePresentacion = new JustificantePresentacion(sNum_itpajd);
		HashMap<String, String> session=new HashMap<String, String>();
		session.put("idioma", "es");
		oJustificantePresentacion.Session = session;
		
		//Paso 2: Rellena y escribe PDF		
		if (objOut instanceof FormBeanAplPTel) {
			//Paso 3: escribir Pdf para Aplazados.			
			FormBeanAplPTel beanAp_T = (FormBeanAplPTel)objOut;
			//Para Aplazamiento/pago telematico			
			isvalid = escribePDFCertificado(fBean, buffer);
			
			oJustificantePresentacion.setCodVerificacion(oJustificantePresentacion.codigoVerificacion("C" + sNum_itpajd + beanAp_T.getNif()));
		} else if (objOut instanceof FormBeanCertificados) {
			//Paso 3: escribir Pdf para Exentos, No-Sujetos y Pago Telematico.			
			try {				
				oJustificantePresentacion.compila(getXsl(oJustificantePresentacion), bufferArray[0]);			
				buffer = (ByteArrayOutputStream) agregarDocumentos(bufferArray, buffer, null);
								
				if (bufferArray[0].toByteArray().length != 0 && buffer.toByteArray().length != 0){
					isvalid = true;
				}				
			} catch (Throwable te) {
				bufferArray = null;
				buffer = null;
				buffer_xml = null;			
				com.stpa.ws.server.util.Logger.error("Error en la creacion del pdf Certificados: generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ, String fechaHabil):" + te.toString(), te,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), te);
			}			
		}
		// Paso 4: Verifica si el válido el objeto
		if (!isvalid) {			
			com.stpa.ws.server.util.Logger.error("Error en la creacion del pdf Certificados",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			bufferArray = null;
			buffer = null;
			buffer_xml = null;
		}
		// Copiamos el buffer antes de encriptarlo para mandarlo codificado en el XMl de respuesta
		buffer_xml = buffer;
		// Paso 5: Comprimir PDF
		String documentzippeado = new String();
		documentzippeado = comprimirPDF(buffer);
		// Paso 6: Almacenado PDF
		String nombreDoc = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd();
		String nifSujetoPasivo = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif();
		String nifPresentador = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif();
		String codVerificacion = oJustificantePresentacion.getCodVerificacion();
		altaDocumento(nombreDoc,nifSujetoPasivo,codVerificacion,CirceImpuestoTrnsADJConstantes.TIPODOC_CERTIFICADO, nifPresentador, documentzippeado, CirceImpuestoTrnsADJConstantes.EXTENSION_PDF);
		
		//paso 6: Devolvemos el PDF resultado para el XML respuesta
		char[] c = Base64.encode(buffer_xml.toByteArray());
		//Cerramos el Buffer.
		try {
			bufferArray[0].close();
			buffer.close();
			buffer_xml.close();
		} catch (Throwable bF) {
			com.stpa.ws.server.util.Logger.info("Error en el proceso de cierre del buffer de los PDF Certificado." + bF.toString(),Logger.LOGTYPE.APPLOG);
		}
		com.stpa.ws.server.util.Logger.info("Fin en generarPdfCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ, String fechaHabil).",LOGTYPE.APPLOG);		
		return new String(c);
	}
	/**
	 * Rellena el objeto para el PDF de salida en el caso de los certificados "No Sujeto".
	 * @param lCirceImpuestoTrnsADJ Objeto XML de salida con los campos de la peticion CIRCE.
	 * @return Objeto para el PDF de No Sujeto.
	 * @throws StpawsException Error controla STPA.
	 */
	private static Object rellenarBeanCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, Object objIn, String fechaHabil) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini Rellenamos el bean Certificados", Logger.LOGTYPE.APPLOG);
		
		if (objIn instanceof FormBeanCertificados) {
			FormBeanCertificados beanNS_E = (FormBeanCertificados)objIn;		
			//rellenamos los campos
			beanNS_E.setSujeto_pasivo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_completo());
			beanNS_E.setNif(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif());
			beanNS_E.setNumAutoliquidacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd());
			beanNS_E.setFecha();
			
			com.stpa.ws.server.util.Logger.info("Fin Rellenamos el bean Certificados FormBeanCertificados.", Logger.LOGTYPE.APPLOG);
			return beanNS_E;
		} else if (objIn instanceof FormBeanAplPTel) {
			FormBeanAplPTel beanAp_T = (FormBeanAplPTel)objIn;
			//rellenamos los campos
			beanAp_T.setCatidadIngreso(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_Ap_Tp);
			beanAp_T.setFechaIngreso("");
			beanAp_T.setFechaAplazamiento(fechaHabil);
			beanAp_T.setSujeto_pasivo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_completo());
			beanAp_T.setNif(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif());
			beanAp_T.setNumAutoliquidacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd());
			beanAp_T.setFecha();
			
			com.stpa.ws.server.util.Logger.info("Fin Rellenamos el bean Certificados FormBeanAplPTel.", Logger.LOGTYPE.APPLOG);
			return beanAp_T;
		} else {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.pdf") + "rellenarBean(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ, Object objIn): Tipo Certificado nulo.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
	}
	/**
	 * Genera el PDF de No Sujeto
	 * @param beanNS Bean datos PDF salida.
	 * @return True indicando que se ha generado correctamente. False, fallo en la geeracion.
	 */
	private static boolean escribePDFCertificado(Object beanIn, OutputStream pdfSalida) throws StpawsException {
				
		PdfStamper stamp = null;
		Preferencias pref = new Preferencias();		
		PdfReader reader = null;
		
		try {			
		com.stpa.ws.server.util.Logger.debug("Inicio escribePDF Certificados",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		String modeloPDF = new String("");
		if (beanIn instanceof FormBeanCertificados) {
			modeloPDF = ((FormBeanCertificados)beanIn).getNombrePDF();
		} else if (beanIn instanceof FormBeanAplPTel) {
			modeloPDF = ((FormBeanAplPTel)beanIn).getNombrePDF();
		} else {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.tipo.cert") + "escribePDFCertificado(Object beanIn, OutputStream pdfSalida) instanceof nulo",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}
		String uristrOrigen = pref.getM_pdfmodelo() + modeloPDF + ".pdf";			
		reader = new PdfReader(uristrOrigen);
		stamp = new PdfStamper(reader, pdfSalida);
		
		// relleno los campos
		AcroFields form1 = stamp.getAcroFields();
		if (beanIn instanceof FormBeanCertificados) {
			FormBeanCertificados beanNS = (FormBeanCertificados)beanIn;
			form1.setField("sujetopasivo", beanNS.getSujeto_pasivo());
			form1.setField("NIF", beanNS.getNif());
			form1.setField("autoliq", beanNS.getNumAutoliquidacion());
			form1.setField("fecha_2", beanNS.getFecha());
		} else if(beanIn instanceof FormBeanAplPTel) {
			FormBeanAplPTel beanNS = (FormBeanAplPTel)beanIn;
			form1.setField("sujetopasivo", beanNS.getSujeto_pasivo());
			form1.setField("NIF", beanNS.getNif());
			form1.setField("autoliq", beanNS.getNumAutoliquidacion());
			form1.setField("fecha_2", beanNS.getFecha());
			form1.setField("cantidad", beanNS.getCatidadIngreso());
			form1.setField("fecha_1", beanNS.getFechaIngreso());
			form1.setField("fecha_3", beanNS.getFechaAplazamiento());
		} 
		
		stamp.setFormFlattening(true);
		reader.close();
		stamp.close();
		return true;
		
		
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("escribePDF(Object beanIn, OutputStream pdfSalida)", e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			try {
				stamp.close();
			} catch (Throwable t) {
				com.stpa.ws.server.util.Logger.error("escribePDF(Object beanIn, OutputStream pdfSalida). Error al cerrar el stamper", t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
		}
		
		return false;
	}
	/**
	 * Retorna el objeto del tipo de Certificado para la emision del PDF
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto XML a retornar a CIRCE.
	 * @return Objeto Tipo de Certificado
	 * @throws StpawsException Error STPA de la inexitencia del tipo de Certificado a Emitir.
	 */
	private static Object tipoCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
					
		if(CirceImpuestoTrnsADJConstantes.constantretorno_NSujeto.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getNo_sujeto())) {
			FormBeanCertificados bFormBeanCertificados = new FormBeanCertificados();
			bFormBeanCertificados.setTipoCentificado(CirceImpuestoTrnsADJConstantes.constantretorno_NSujeto);
			bFormBeanCertificados.setNombrePDF(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_NoSujeo_PDF);
			
			return bFormBeanCertificados;
		} else if(CirceImpuestoTrnsADJConstantes.constantretorno_Exento.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getExento())){
			FormBeanCertificados bFormBeanCertificados = new FormBeanCertificados();
			bFormBeanCertificados.setTipoCentificado(CirceImpuestoTrnsADJConstantes.constantretorno_Exento);
			bFormBeanCertificados.setNombrePDF(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_Exento_PDF);
			
			return bFormBeanCertificados;
		} else if (CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_SRL_PT.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())){
			FormBeanCertificados bFormBeanCertificados = new FormBeanCertificados();
			bFormBeanCertificados.setTipoCentificado(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_PTelematic);
			bFormBeanCertificados.setNombrePDF(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_PTelematic_PDF);
			
			return bFormBeanCertificados;				
			
		} else if (CirceImpuestoTrnsADJConstantes.constantesubtipoTipoSociedad_SLNE_AP.equals(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getTipo_confirmacion())) { 
			
				if((CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getEntidad())) || 
				(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getOficina())) || 
				(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getControl())) || 
				(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getCuenta())) || 
				(CirceValidation.isEmpty(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().getNif_titular()))){
					FormBeanCertificados bFormBeanCertificados = new FormBeanCertificados();
					bFormBeanCertificados.setTipoCentificado(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_PTelematic);
					bFormBeanCertificados.setNombrePDF(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_PTelematic_PDF);
					return bFormBeanCertificados;
				} else {
					FormBeanAplPTel bFormBeanAplPTel = new FormBeanAplPTel();
					bFormBeanAplPTel.setTipoCentificado(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_Aplazado);
					bFormBeanAplPTel.setNombrePDF(CirceImpuestoTrnsADJConstantes.constantTipoCertificado_Aplazado_PDF);
					return bFormBeanAplPTel;
				}					
				
				
		} else {
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.tipo.cert") + "tipoCertificado(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ): Tipo de Certificado indeterminado; Faltan datos.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
		}		
	}
	/**
	 * Compresion fichero PDF para enviarlo a la base de datos.
	 * @param pdf Bytearray del PDF generado
	 * @return String con el PDF comprimido listo para ser almacenado.
	 * @throws StpawsException Excepción controlada de la aplicación.
	 */
	public static String comprimirPDF(ByteArrayOutputStream pdf) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini comprimirPDF(ByteArrayOutputStream pdf).",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		
		String pdfComprimido = null;
		if(pdf != null){
			try{
				
				byte [] arrayBytes = compress(pdf.toByteArray());
				char[] caracteres64 = Base64.encode(arrayBytes);
				pdfComprimido = new String(caracteres64);		
				
			} 
			catch (Exception e){
				com.stpa.ws.server.util.Logger.error("comprimirPDF(ByteArrayOutputStream pdf): Exceptio:" + e.toString() ,e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
			}
		}
		
		com.stpa.ws.server.util.Logger.info("Fin comprimirPDF(ByteArrayOutputStream pdf).",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return pdfComprimido;		
	} 
	/**
	 * Comprime el fichero enviado
	 * @param input byte del fichero a ser comprimido.
	 * @return bytes resultantes de la compresion.
	 * @throws StpawsException Excepción controlada de la aplicacion.
	 */
	private static byte[] compress(byte[] input) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("compress(byte[] input): Inicio.",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
	    
		// Compressor with highest level of compression
	    Deflater compressor = new Deflater();
	    compressor.setLevel(Deflater.BEST_COMPRESSION);
	    
	    // Give the compressor the data to compress
	    compressor.setInput(input);
	    compressor.finish();
	    
	    // Create an expandable byte array to hold the compressed data.
	    // It is not necessary that the compressed data will be smaller than
	    // the uncompressed data.
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
	    
	    // Compress the data
	    byte[] buf = new byte[1024];
	    while (!compressor.finished()) {
	        int count = compressor.deflate(buf);
	        bos.write(buf, 0, count);
	    }
	    try {
	        bos.close();
	    } catch (IOException e) {
	    	com.stpa.ws.server.util.Logger.error("compress(byte[] input): IOExceptio:" + e.toString() ,e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
	    }
	    
	    // Get the compressed data
	    byte[] compressedData = bos.toByteArray();
	    com.stpa.ws.server.util.Logger.debug("compress(byte[] input): Fin.",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
	    return compressedData;
	}
	/**
	 * Alta de PDF en la base de datos de documentos.
	 * @param nombreDocumento Numero de autoliquidacion
	 * @param codigoVerificacion Código para el buscador de la Web de documentos: Autogenerado.
	 * @param nifPasivo Nif sujeto pasivo
	 * @param tipoDoc Tipo documento: Certificado (C), Modelo (M)
	 * @param nifPresentador Nif presentador
	 * @param idsesion Id session Web: Nulo.
	 * @param contenido String zippeado
	 * @param extension Extensión del fichero dentro del zip: PDF, XML
	 * @throws StpawsException Error controla de la aplicacion
	 */
	public static void altaDocumento(String nombreDocumento, String nifPasivo, String codigoVerificacion, 
			String tipoDoc, String nifPresentador, String contenido, String extension) throws StpawsException {
		boolean isAltaCorrecta = false;
		
		if(CirceValidation.isEmpty(contenido)){
			com.stpa.ws.server.util.Logger.error("altaDocumento(String nombreDocumento, String codigoVerificacion, String nifPasivo,String tipoDoc, String nifPresentador, String idsesion, String contenido, String extension): alta Documento|contenido vacio.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
		}
		
		try {
			HashMap<String, String> valoresParametros = new HashMap<String, String>();
			valoresParametros.put("p_tipo", tipoDoc);
			valoresParametros.put("p_nombre", nombreDocumento);
			valoresParametros.put("p_codverificacion", codigoVerificacion);
			valoresParametros.put("p_sujetopasivo", nifPasivo);
			valoresParametros.put("p_presentador", nifPresentador);
			valoresParametros.put("p_idsession", "");
			valoresParametros.put("p_origen", "P");
			valoresParametros.put("p_libre", "");
			valoresParametros.put("p_publicable", "S");
			valoresParametros.put("p_documento", contenido);
			valoresParametros.put("p_extension", extension);
			valoresParametros.put("p_conoracle", "P");
		
			// Generamos el XML de la peticion de llamada la servicio
			String peticion = WebServicesUtil.generarPeticionInsertarDocumentos(valoresParametros);
		
			String respuesta = "";
			// LLamada al Web Service
			respuesta = WebServicesUtil.wsCall(peticion, CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADORMASIVO_WSDL);
			// Resultado de la llamada al servicio.
			isAltaCorrecta= WebServicesUtil.respuestaWSInsercionCorrecta(respuesta);
			
		} catch (StpawsException e) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), e);
		}
		
		if(!isAltaCorrecta){
			com.stpa.ws.server.util.Logger.error("altaDocumento(String nombreDocumento, String codigoVerificacion, String nifPasivo,String tipoDoc, String nifPresentador, String idsesion, String contenido, String extension): MantenedorDocumentos.altaDocumento|codigo de error en el alta.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), null);
		}		
	}
	/**
	 * Metodo mediante el cual vamos a generar el PDF del modelo 600 de autoliquidaciones.
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto CIRCE de salida.
	 * @param lCirceImpuestoTrnsADJ_Respuesta Objeto WS CIRCE de respuesta.
	 * @return boolean indicando si el proceso se ha ejecutado correctamente.
	 * @throws StpawsException Error controlado del sistema CIRCE
	 */
	public static boolean generarPdfModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud, CirceImpuestoTrnsADJ_Respuesta lCirceImpuestoTrnsADJ_Respuesta) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini generarPdfModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ).",Logger.LOGTYPE.APPLOG);
		boolean resultado = true;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		// Buffer Salida para grabar datos
		ByteArrayOutputStream buffer_xml = new ByteArrayOutputStream();		
		
		//Paso 1: Rellenar bean:		
		Object fBean=rellenarBeanModelo600(lCirceImpuestoTrnsADJ_Solicitud);
		//Paso 1.1: Iniciamos el Objeto Justificante.
		String sNum_itpajd = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd();
		String nifSujetoPasivo = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif();
		//Mediante el método común de generar el código de verificacion.
		JustificantePresentacion oJustificantePresentacion = new JustificantePresentacion(sNum_itpajd);
		HashMap<String, String> session=new HashMap<String, String>();
		session.put("idioma", "es");
		oJustificantePresentacion.Session = session;
		oJustificantePresentacion.setCodVerificacion(oJustificantePresentacion.codigoVerificacion("M" + sNum_itpajd + nifSujetoPasivo));
		
		//paso 2: Escribir PDF Modelo 600
		boolean isvalid = escribePDFModelo600(fBean, buffer);
		if (!isvalid) {			
			com.stpa.ws.server.util.Logger.error("Error en la creacion del pdf Modelo 600",Logger.LOGTYPE.APPLOG);
			resultado = false;
			buffer = null;
			buffer_xml = null;			
		}	
		// Copiamos el buffer antes de encriptarlo para mandarlo codificado en el XML de respuesta
		buffer_xml = buffer;
		//Paso 3: Comprimir PDF	
		String documentzippeado = new String();		
		documentzippeado = comprimirPDF(buffer);
		
		//Paso 4: Almacenado PDF
		String nombreDoc = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd();
		String nifPresentador = lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif();
		String codVerificacion = oJustificantePresentacion.getCodVerificacion();
		altaDocumento(nombreDoc,nifSujetoPasivo,codVerificacion,CirceImpuestoTrnsADJConstantes.TIPODOC_MODELO600, nifPresentador, documentzippeado, CirceImpuestoTrnsADJConstantes.EXTENSION_PDF);
		
		//Paso 5: Devolvemos el PDF resultado para el XML respuesta
		char[] c = Base64.encode(buffer_xml.toByteArray());
		String sCharresultado = new String(c);
		if (CirceValidation.isEmpty(sCharresultado)) {
			com.stpa.ws.server.util.Logger.error("Error en la creacion del pdf Modelo 600, no hay char array resultado de PDF Modelo 600.",Logger.LOGTYPE.APPLOG);
			resultado = false;
		}
		//Cerramos el Buffer.
		try {
			buffer.close();
			buffer_xml.close();			
		} catch (Throwable bF) {
			com.stpa.ws.server.util.Logger.info("Error en el proceso de cierre del buffer de los PDF Modelo 600." + bF.toString(),Logger.LOGTYPE.APPLOG);
		}
		
		com.stpa.ws.server.util.Logger.info("Fin generarPdfModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ). Modelo 600 resultante:" + new String(c),Logger.LOGTYPE.APPLOG);
		return resultado;
		// Fin del proceso de creacion y alta Modelo 600 para Circe.
	}
	/**
	 * Metodo que rellena el objeto del Formulario Bena del modelo 600
	 * @param lCirceImpuestoTrnsADJ_Solicitud Objeto de respuesta a CIRCE
	 * @return Retornara el objeto Bean del modelo 600 para el PDF
	 * @throws StpawsException Errorres controlados del servicio CIRCE
	 */
	private static Object rellenarBeanModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ_Solicitud) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("Ini rellenarBeanModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ)",Logger.LOGTYPE.APPLOG);
		
		FormBeanModelo600 fbModelo600	= new FormBeanModelo600();
		
		// Datos generales
		fbModelo600.setNumAutoliquidacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getNum_itpajd());
		DateUtil.setFechasDia_Mes_Anyo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getFecha_devengo(), fbModelo600);
		// Datos Sujeto Pasivo
		fbModelo600.getFbModelo600SujetoPasivo().setNifCIF(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNif());
		fbModelo600.getFbModelo600SujetoPasivo().setApellidoNombreRazonSocial(StpawsUtil.generarNombreCompleto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce(), StpawsUtil.TIPO_INTERVINIENTE_SUJETOPASIVO));
		fbModelo600.getFbModelo600SujetoPasivo().setTelefono(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTelefono());
		fbModelo600.getFbModelo600SujetoPasivo().setSigla(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getTipo_via());
		fbModelo600.getFbModelo600SujetoPasivo().setCallePlazaAvd(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNombre_via());
		fbModelo600.getFbModelo600SujetoPasivo().setNumero(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getNumero());
		fbModelo600.getFbModelo600SujetoPasivo().setEsc(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getEscalera());
		fbModelo600.getFbModelo600SujetoPasivo().setPiso(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPlanta());
		fbModelo600.getFbModelo600SujetoPasivo().setPuerta(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPuerta());
		fbModelo600.getFbModelo600SujetoPasivo().setMunicipio(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().getNombre());
		fbModelo600.getFbModelo600SujetoPasivo().setProvincia(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().getNombre());
		fbModelo600.getFbModelo600SujetoPasivo().setCp(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getCodigo_postal());
		// Dato Presentador
		fbModelo600.getFbModelo600Presentador().setNifCIF(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNif());
		fbModelo600.getFbModelo600Presentador().setApellidoNombreRazonSocial(StpawsUtil.generarNombreCompleto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce(), StpawsUtil.TIPO_INTERVINIENTE_SUJETOPRESENTADOR));
		fbModelo600.getFbModelo600Presentador().setTelefono(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTelefono());
		DateUtil.setFechasDia_Mes_Anyo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getIdentificacion().getFecha(), fbModelo600.getFbModelo600Presentador());
		fbModelo600.getFbModelo600Presentador().setSigla(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getTipo_via());
		fbModelo600.getFbModelo600Presentador().setCallePlazaAvd(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNombre_via());
		fbModelo600.getFbModelo600Presentador().setNumero(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getNumero());
		fbModelo600.getFbModelo600Presentador().setEsc(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getEscalera());
		fbModelo600.getFbModelo600Presentador().setPiso(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPlanta());
		fbModelo600.getFbModelo600Presentador().setPuerta(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPuerta());
		fbModelo600.getFbModelo600Presentador().setMunicipio(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().getNombre());
		fbModelo600.getFbModelo600Presentador().setProvincia(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().getNombre());
		fbModelo600.getFbModelo600Presentador().setCp(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getCodigo_postal());
		// Datos Notariales
		fbModelo600.getFbModelo600DatosNotariales().setNotarial(CirceImpuestoTrnsADJConstantes.TIPO_STRING_NOTARIAL);
		fbModelo600.getFbModelo600DatosNotariales().setExpAbreviado(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getExpresion_abreviada());
		fbModelo600.getFbModelo600DatosNotariales().setNotarioFederacion(StpawsUtil.generarNombreCompleto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce(), StpawsUtil.TIPO_INTERVINIENTE_DATOSNOTARIALES));
		fbModelo600.getFbModelo600DatosNotariales().setProtocolo(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().getNumero_protocolo());
		fbModelo600.getFbModelo600DatosNotariales().setConcepto(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().getConcepto());
		fbModelo600.getFbModelo600DatosNotariales().setMunicipioNotario(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().getLocalidad());
		// Dato liquidacion
		fbModelo600.getFbModelo600Liquidacion().setValorDeclarado(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getValores().getValor_declarado()));
		fbModelo600.getFbModelo600Liquidacion().setFundamentos(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().getFundamento());		
		fbModelo600.getFbModelo600Liquidacion().setExento_nosujeto(StpawsUtil.getTipoLiquidacion(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones()));
		fbModelo600 = comprobarAutoliquidacionComplementaria(fbModelo600, lCirceImpuestoTrnsADJ_Solicitud);
		fbModelo600.getFbModelo600Liquidacion().setBaseImponible(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_imponible()));
		fbModelo600.getFbModelo600Liquidacion().setPorcentajeReduccion(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getReduccion().getPorcentaje()));
		fbModelo600.getFbModelo600Liquidacion().setImporteReduccion(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getReduccion().getImporte()));
		fbModelo600.getFbModelo600Liquidacion().setBaseLiquidable(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBase_liquidable()));
		fbModelo600.getFbModelo600Liquidacion().setPorcentajeTipoImpositivo(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().getPorcentaje()));
		fbModelo600.getFbModelo600Liquidacion().setCuota(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getCuota()));
		fbModelo600.getFbModelo600Liquidacion().setPorcentajeBonificacioCuota(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBonificacion().getPorcentaje()));
		fbModelo600.getFbModelo600Liquidacion().setImporteBonificacioCuota(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBonificacion().getImporte()));
		fbModelo600.getFbModelo600Liquidacion().setImporteIngresar(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getImporte_ingresar()));
		fbModelo600.getFbModelo600Liquidacion().setRecargo(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getRecargo()));
		fbModelo600.getFbModelo600Liquidacion().setInteresdeDemora(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getIntereses()));
		fbModelo600.getFbModelo600Liquidacion().setImporteIngresadoenliquidacionAnterior(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().getImporte_ingresado()));
		fbModelo600.getFbModelo600Liquidacion().setTotalIngresar(formateaImporte(lCirceImpuestoTrnsADJ_Solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTotal_ingresar()));		
		
		//Retorno del Objeto Modelo 600.
		com.stpa.ws.server.util.Logger.debug("Fin rellenarBeanModelo600(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ)",Logger.LOGTYPE.APPLOG);
		return fbModelo600;
	}
	/**
	 * Genera PDF Modelo 600 para CIRCE
	 * @param beanIn Bean de entrada con datos Modelo 600
	 * @param pdfSalida Buffer de escritura con el Modelo 600
	 * @return Boolean indicando generación correcta o incorrecta del PDF
	 * @throws StpawsException Error controlado del Sistema CIRCE
	 */
	private static boolean escribePDFModelo600(Object beanIn, OutputStream pdfSalida) throws StpawsException {
		com.stpa.ws.server.util.Logger.info("Ini escribePDFModelo600(Object beanIn, OutputStream pdfSalida).",Logger.LOGTYPE.APPLOG);
		
		PdfStamper stamp = null;
		Preferencias pref = new Preferencias();		
		PdfReader reader = null;
		String modeloPDF = new String("");
		
		try {			
			if (beanIn instanceof FormBeanModelo600) {
				modeloPDF = ((FormBeanModelo600)beanIn).getNombreModelo600();		
			} else {
				com.stpa.ws.server.util.Logger.error("escribePDFModelo600(Object beanIn, OutputStream pdfSalida) instanceof nulo",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
			}
			String uristrOrigen = pref.getM_pdfmodelo() + modeloPDF + ".pdf";			
			reader = new PdfReader(uristrOrigen);
			stamp = new PdfStamper(reader, pdfSalida);
			
			// relleno los campos
			AcroFields form1 = stamp.getAcroFields();		
			if (beanIn instanceof FormBeanModelo600) {
				FormBeanModelo600 beanM600 = (FormBeanModelo600)beanIn;
				//Datos generales
				form1.setField("numerodeserie", beanM600.getNumAutoliquidacion());
				form1.setField("fecha_dia1", beanM600.getDiaFecha());
				form1.setField("fecha_mes1", beanM600.getMesFecha());
				form1.setField("fecha_anyo1", beanM600.getAnyoFecha());
				form1.setField("de1", beanM600.getDatoEspecifico1());
				form1.setField("de2", beanM600.getDatoEspecifico2());			
				//Datos Sujeto Pasivo
				form1.setField("1", beanM600.getFbModelo600SujetoPasivo().getNifCIF());
				form1.setField("2", beanM600.getFbModelo600SujetoPasivo().getApellidoNombreRazonSocial());
				form1.setField("3", beanM600.getFbModelo600SujetoPasivo().getTelefono());
				form1.setField("4a", beanM600.getFbModelo600SujetoPasivo().getSigla());
				form1.setField("4", beanM600.getFbModelo600SujetoPasivo().getCallePlazaAvd());
				form1.setField("5", beanM600.getFbModelo600SujetoPasivo().getNumero());
				form1.setField("6", beanM600.getFbModelo600SujetoPasivo().getEsc());
				form1.setField("7", beanM600.getFbModelo600SujetoPasivo().getPiso());
				form1.setField("8", beanM600.getFbModelo600SujetoPasivo().getPuerta());
				form1.setField("9", beanM600.getFbModelo600SujetoPasivo().getMunicipio());
				form1.setField("10", beanM600.getFbModelo600SujetoPasivo().getProvincia());
				form1.setField("11", beanM600.getFbModelo600SujetoPasivo().getCp());
				form1.setField("12", beanM600.getFbModelo600SujetoPasivo().getNSujetoPasivo());
				//Datos Transmitente
				form1.setField("24", beanM600.getFbModelo600Transmitentes().getNumTransmitente());
				//Datos Presentador
				form1.setField("25", beanM600.getFbModelo600Presentador().getNifCIF());
				form1.setField("26", beanM600.getFbModelo600Presentador().getApellidoNombreRazonSocial());
				form1.setField("27", beanM600.getFbModelo600Presentador().getTelefono());
				form1.setField("d1", beanM600.getFbModelo600Presentador().getDiaFecha());
				form1.setField("m1", beanM600.getFbModelo600Presentador().getMesFecha());
				form1.setField("a1", beanM600.getFbModelo600Presentador().getAnyoFecha());
				form1.setField("28a", beanM600.getFbModelo600Presentador().getSigla());
				form1.setField("28", beanM600.getFbModelo600Presentador().getCallePlazaAvd());			
				form1.setField("29", beanM600.getFbModelo600Presentador().getNumero());
				form1.setField("30", beanM600.getFbModelo600Presentador().getEsc());
				form1.setField("31", beanM600.getFbModelo600Presentador().getPiso());
				form1.setField("32", beanM600.getFbModelo600Presentador().getPuerta());
				form1.setField("33", beanM600.getFbModelo600Presentador().getMunicipio());
				form1.setField("34", beanM600.getFbModelo600Presentador().getProvincia());
				form1.setField("35", beanM600.getFbModelo600Presentador().getCp());
				//Datos Notariales
				form1.setField("36", beanM600.getFbModelo600DatosNotariales().getNotarial());
				form1.setField("41", beanM600.getFbModelo600DatosNotariales().getExpAbreviado());
				form1.setField("42", beanM600.getFbModelo600DatosNotariales().getNotarioFederacion());
				form1.setField("43", beanM600.getFbModelo600DatosNotariales().getProtocolo());
				form1.setField("44", beanM600.getFbModelo600DatosNotariales().getConcepto());
				form1.setField("45", beanM600.getFbModelo600DatosNotariales().getMunicipioNotario());
				//Datos Liquidacion
				form1.setField("47", beanM600.getFbModelo600Liquidacion().getValorDeclarado());
				form1.setField("48", beanM600.getFbModelo600Liquidacion().getExento_nosujeto());
				form1.setField("52", beanM600.getFbModelo600Liquidacion().getFundamentos());
				//Stamp los objetos de Autoliquidacion Complementaria.
				if (bAutoliquidacionComplementaria) {
					form1.setField("53", beanM600.getFbModelo600Liquidacion().getAutoliquidacionComplementaria());
					form1.setField("54a", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getDiaFecha(), 0));
					form1.setField("54b", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getDiaFecha(), 1));
					form1.setField("54c", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getMesFecha(), 0));
					form1.setField("54d", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getMesFecha(), 1));
					form1.setField("54e", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getAnyoFecha(), 0));
					form1.setField("54f", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getAnyoFecha(), 1));
					form1.setField("54g", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getAnyoFecha(), 2));
					form1.setField("54h", DateUtil.getFechaFraccionada(beanM600.getFbModelo600Liquidacion().getAnyoFecha(), 3));
					form1.setField("55", beanM600.getFbModelo600Liquidacion().getNumeroPrimeraAutoliquidacion());
				}
				form1.setField("56", beanM600.getFbModelo600Liquidacion().getBaseImponible());
				form1.setField("57", beanM600.getFbModelo600Liquidacion().getPorcentajeReduccion());
				form1.setField("58", beanM600.getFbModelo600Liquidacion().getImporteReduccion());
				form1.setField("59", beanM600.getFbModelo600Liquidacion().getBaseLiquidable());
				form1.setField("60", beanM600.getFbModelo600Liquidacion().getPorcentajeTipoImpositivo());
				form1.setField("61", beanM600.getFbModelo600Liquidacion().getCuota());
				form1.setField("62", beanM600.getFbModelo600Liquidacion().getPorcentajeBonificacioCuota());
				form1.setField("63", beanM600.getFbModelo600Liquidacion().getImporteBonificacioCuota());
				form1.setField("64", beanM600.getFbModelo600Liquidacion().getImporteIngresar());
				form1.setField("65", beanM600.getFbModelo600Liquidacion().getRecargo());
				form1.setField("66", beanM600.getFbModelo600Liquidacion().getInteresdeDemora());	
				form1.setField("67", beanM600.getFbModelo600Liquidacion().getImporteIngresadoenliquidacionAnterior());
				form1.setField("68", beanM600.getFbModelo600Liquidacion().getTotalIngresar());			
				// Codigo de barras
				// numerodeserie2 es un campo OBLIGATORIO, determina la posicion del codigo de barras
				float[] posCodBarras = form1.getFieldPositions("numerodeserie2");
				if (posCodBarras == null)
					posCodBarras = new float[0];
				stamp.setFormFlattening(true);
				for (int h = 0; h < posCodBarras.length; h += 5) {
					float xinit = posCodBarras[h + 1];
					float yinit = posCodBarras[h + 2];
					crearCodigoBarras(CirceImpuestoTrnsADJConstantes.constante_Emisor, beanM600.getNumAutoliquidacion(), pref.getM_pdfmodelo(), stamp.getOverContent((int) posCodBarras[h]), xinit, yinit);
					stamp.getOverContent(1).stroke();
				}
				
			} else {
				com.stpa.ws.server.util.Logger.error("escribePDFModelo600(Object beanIn, OutputStream pdfSalida) instanceof nulo",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),null);
			} 		
			
			reader.close();
			stamp.close();
			com.stpa.ws.server.util.Logger.info("Fin escribePDFModelo600(Object beanIn, OutputStream pdfSalida).",Logger.LOGTYPE.APPLOG);
			return true;			
			
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("escribePDFModelo600(Object beanIn, OutputStream pdfSalida)", e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			try {
				stamp.close();
			} catch (Throwable t) {
				com.stpa.ws.server.util.Logger.error("escribePDFModelo600(Object beanIn, OutputStream pdfSalida). Error al cerrar el stamper", t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
		}
		
		return false;
	}
	/**
	 * Chequea si CIRCE manda la solicitud con datos de Autoliquidacion Complementaria
	 * @param fbModelo600 Objeto Bean Modelo 600
	 * @param lCirceImpuestoTrnsADJ Objeto Respuesta a CIRCE
	 * @return Objeto Bean Modelo 600 para integrar en el PDF
	 */
	private static FormBeanModelo600 comprobarAutoliquidacionComplementaria(FormBeanModelo600 fbModelo600, CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ) throws StpawsException {
		
		if (!CirceValidation.isEmpty(lCirceImpuestoTrnsADJ.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().getNumero_justificante())) {
			bAutoliquidacionComplementaria = true;
			fbModelo600.getFbModelo600Liquidacion().setNumeroPrimeraAutoliquidacion(lCirceImpuestoTrnsADJ.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().getNumero_justificante());
			DateUtil.setFechasDia_Mes_Anyo(lCirceImpuestoTrnsADJ.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().getFecha_presentacion(), fbModelo600.getFbModelo600Liquidacion());
			fbModelo600.getFbModelo600Liquidacion().setAutoliquidacionComplementaria(CirceImpuestoTrnsADJConstantes.TIPO_STRING_AUTOLIQUIDACION);
		} else {
			com.stpa.ws.server.util.Logger.info("comprobarAutoliquidacionComplementaria(FormBeanModelo600 fbModelo600). Carece de datos de Autoliquidacion Complementaria.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
	
		return fbModelo600;
	}
	/**
	 * Obtencion del Stream XSL con la codificion adecuada según ruta facilitada.
	 * @param fb PDF Base.
	 * @return Extraccion hacia Stream desde la ruta facilitada.
	 * @throws Exception
	 */
	private static String getXsl(FormPDFBase fb) throws Exception{
		
		String uristring = fb.getPlantilla();
		
		String xslplantilla = getContents(uristring, "ISO-8859-1");
		String encoding = XMLUtils.getEncoding(xslplantilla);
		if (encoding != null && !encoding.equals("") && !encoding.equals("ISO-8859-1")){
			xslplantilla = getContents(uristring, encoding);
		}		
		return xslplantilla;
	}
	/**
	 * Obtiene desde el fichero el stream en formato String
	 * @param aFile Ruta origen del fichero a devolver en formato String.
	 * @param encoding Codificacion del String obtenido.
	 * @return Contendo del fichero en formato String.
	 * @throws Exception Error en la obtencion del fichero o sus datos.
	 */
	public static String getContents(String aFile, String encoding) throws Exception {
		// ...checks on aFile are elided
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			try{
				if (!new File(aFile).exists()) {
					URL url = new URL(aFile);
					return getContents(url, encoding);
				}
			}catch(Exception e){
				//Continuamos ya que no es una url				
				com.stpa.ws.server.util.Logger.error("Continuamos ya que no es una url " + e.getMessage(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(aFile));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buf = new byte[512];
			int len;
			while ((len = bis.read(buf)) > 0) {
				output.write(buf, 0, len);
			}
			output.close();
			bis.close();
			return output.toString(encoding);
		} catch (IOException ex) {
			com.stpa.ws.server.util.Logger.error("Error obteniendo XSL "+ ex.getMessage(), ex,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}

		return contents.toString();
	}
	/**
	 * Obtiene desde la URL facilitada el fichero el stream en formato String.
	 * @param aFile URL Origen del fichero a obtener.
	 * @param encoding Codificacion del String obtenido. 
	 * @return Contendo del fichero en formato String.
	 */
	public static String getContents(URL aFile, String encoding) {
		ByteArrayOutputStream contents = new ByteArrayOutputStream();
		try {
			if (aFile == null)
				return "";
			InputStream is = aFile.openStream();
			byte[] buf = new byte[512];
			int len;
			while ((len = is.read(buf)) > 0) {
				contents.write(buf, 0, len);
			}
		} catch (IOException ex) {
			com.stpa.ws.server.util.Logger.error("Error obteniendo XSL "+ex.getMessage(), ex,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		try {
			return contents.toString(encoding);
		} catch (UnsupportedEncodingException e) {			
			com.stpa.ws.server.util.Logger.error("Error obteniendo XSL "+e.getMessage(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return contents.toString();
	}
	/**
	 * Agrega una serie de ByteArrayOutputStreams para formar un solo PDF en outFile.
	 * 
	 * @param lstDocs
	 * @param outFile
	 * @param docProperties - Mapa con Author, Creator, Keywords, Subject, Title
	 */
	private static OutputStream agregarDocumentos(ByteArrayOutputStream[] lstDocs, OutputStream outFile, HashMap<String,String> docProperties) {
		InputStream[] argsin = new ByteArrayInputStream[lstDocs.length];
		for (int i = 0; i < lstDocs.length; i++) {			
			if (lstDocs[i] != null) {
				if (lstDocs[i] instanceof ByteArrayOutputStream)
					argsin[i] = new ByteArrayInputStream(((ByteArrayOutputStream) lstDocs[i]).toByteArray());
			}
		}
		return agregarDocumentos(argsin, outFile,docProperties);
	}
	
	/**
	 * Agrega una serie de InputStreamS para formar un solo PDF en outFile. NOTA: crear multiples copias del mismo
	 * Stream da IOException si el metodo markSupported() retorna false o no es un FileInputStream.
	 * 
	 * @param lstDocs
	 * @param outFile
	 * @param docProperties - Mapa con Author, Creator, Keywords, Subject, Title
	 */
	@SuppressWarnings("unchecked")
	private static OutputStream agregarDocumentos(InputStream[] lstDocs, OutputStream outFile, HashMap<String,String> docProperties) {
		
		try {
			if (outFile == null)
				outFile = new ByteArrayOutputStream();
			int pageOffset = 0;
			ArrayList master = new ArrayList();
			int f = 0;
			// String outFile = args[args.length-1];
			Document document = null;
			PdfCopy writer = null;
			while (f < lstDocs.length) {
				if (lstDocs[f] == null) {
					f++;
					System.out.println("documento pdf "+f+" es null");
					continue;
				}
				// we create a reader for a certain document
				if (lstDocs[f].markSupported())
					lstDocs[f].reset();
				else if (lstDocs[f] instanceof FileInputStream)
					((FileInputStream) lstDocs[f]).getChannel().position(0);
				PdfReader reader = null;
				try {
					reader = new PdfReader(lstDocs[f]);
				} catch (IOException e) {
					f++;
					System.out.println("documento pdf "+f+" es invalido por IOEXCEPTION");
					continue;
				}
				reader.consolidateNamedDestinations();
				// we retrieve the total number of pages
				int n = reader.getNumberOfPages();
				List bookmarks = SimpleBookmark.getBookmark(reader);
				if (bookmarks != null) {
					if (pageOffset != 0)
						SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
					master.addAll(bookmarks);
				}
				pageOffset += n;

				if (f == 0||document==null) {
					// step 1: creation of a document-object
					document = new Document(reader.getPageSizeWithRotation(1));
					// step 2: we create a writer that listens to the document
					writer = new PdfCopy(document, outFile);// new FileOutputStream(outFile));
					HashMap<String, String> pdfinfo = (HashMap<String, String>) reader.getInfo();
					if (docProperties != null)
						pdfinfo = docProperties;
					if (pdfinfo.containsKey("Author"))
						document.addAuthor(pdfinfo.get("Author"));
					if (pdfinfo.containsKey("Creator"))
						document.addCreator(pdfinfo.get("Creator"));
					if (pdfinfo.containsKey("Subject"))
						document.addSubject(pdfinfo.get("Subject"));
					if (pdfinfo.containsKey("Title"))
						document.addTitle(pdfinfo.get("Title"));
					if (pdfinfo.containsKey("Keywords"))
						document.addKeywords(pdfinfo.get("Keywords"));
					document.addCreationDate();

					// step 3: we open the document
					document.open();
				}
				// step 4: we add content
				PdfImportedPage page;
				for (int i = 0; i < n;) {
					++i;
					page = writer.getImportedPage(reader, i);
					writer.addPage(page);
				}
				String js = reader.getJavaScript();
				if (js != null && js.length() != 0)
					writer.addJavaScript(js);
				PRAcroForm form = reader.getAcroForm();
				if (form != null)
					writer.copyAcroForm(reader);
				if (lstDocs[f].markSupported())
					lstDocs[f].reset();
				else if (lstDocs[f] instanceof FileInputStream)
					((FileInputStream) lstDocs[f]).getChannel().position(0);
				f++;
			}
			if (!master.isEmpty())
				writer.setOutlines(master);
			// step 5: we close the document
			if (document != null)
				document.close();
		} catch (Exception e) {			
			com.stpa.ws.server.util.Logger.error("EXCEPCION POR AGREGADOCUMENTOS: "+e.getMessage(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return outFile;
	}
}
