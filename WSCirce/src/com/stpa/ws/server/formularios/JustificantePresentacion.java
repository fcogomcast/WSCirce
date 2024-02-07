package com.stpa.ws.server.formularios;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.WebServicesUtil;
import com.stpa.ws.server.validation.CirceValidation;

import es.tributasenasturias.documentos.DatosSalidaImpresa;
import es.tributasenasturias.documentos.util.NumberUtil;
import es.tributasenasturias.documentos.util.SHAUtils;

public class JustificantePresentacion extends FormPDFBase {

	private String numeroAutoliquidacion = new String();
	private String codVerificacion = new String();
	
	public JustificantePresentacion(String p_numAutoliq) {

		try {
			//Accedo a las propiedades.
			Preferencias pref = new Preferencias();
			
			Session.put("cgestor", "");
			plantilla = pref.getM_pathJustificatePresentacion();
			this.numeroAutoliquidacion = p_numAutoliq;
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("Error al cargar preferencias y plantailla al dar de alta el documento. " + e.getMessage(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}

	}

	public String getPlantilla() {
		return plantilla;
	}
	public void setCodVerificacion(String codVerificacion) {
		this.codVerificacion = codVerificacion;
	}
	public String getCodVerificacion() {
		return this.codVerificacion;
	}
	/**
	 * Genera la MAC en Hexadecimal para la clave facilitada.
	 * @param valor Valor de la clave.
	 * @return Substring con la clave.
	 */
	public String codigoVerificacion(String valor) {
		try {
			String resultado = SHAUtils.hex_hmac_sha1("clave               ", valor);
			return resultado.substring(resultado.length() - 16, resultado.length());
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("Error en codigoVerificacion(String valor)" + e.toString(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			return null;
		}
	}
	public void compila(String xsl, OutputStream output) throws StpawsException {
		presentacion(xsl, output);
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	public void presentacion(String xsl, OutputStream output) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("Inicio presentacion(String id, String xml, String xsl, OutputStream output). xsl:" + xsl,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		
		String modelo = this.numeroAutoliquidacion.substring(0, 3);
		Integer totalIngresado = 0;

		String persona = "0";
		String numAutoliquidacion =  this.numeroAutoliquidacion;
		String nifPdb = "";
		
		String peticion_recuperarDatosJustificantePr = WebServicesUtil.callRecuperarDatosJustificantePr(this.numeroAutoliquidacion);
		String xmlOut_recuperarDatosJustificantePr = WebServicesUtil.wsCall(peticion_recuperarDatosJustificantePr, CirceImpuestoTrnsADJConstantes.constante_TIPOPETICION_LANZADOR_WSDL);
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xmlOut_recuperarDatosJustificantePr, xsl, output);
		Map<Integer, Map<String, String>> rsCanu = s.findWs(new String[] { "CANU_CADENAS_NUMEROS" }, null, "");
		Map<Integer, Map<String, String>> rsPedb = s.findWs(new String[] { "PEDB_PERSONA_DATOS_BASICOS" }, null, "");
		Map<Integer, Map<String, String>> rsMemo = s.findWs(new String[] { "MEMO_MEMO" }, null, "");

		Iterator itRsCanu = rsCanu.entrySet().iterator();
		if (itRsCanu.hasNext()) {
			Map.Entry objCanu1 = (Map.Entry) itRsCanu.next();
			Map mapaCanu1 = (Map) objCanu1.getValue();

			// Oficina de alta
			s.Campo("Texto103_2", s.campo(mapaCanu1, "string2_canu") + "");
			// Organismo
			s.Campo("Texto101_2", s.campo(mapaCanu1, "string4_Canu") + "");
			// Nº Expediente
			s.Campo("Texto70_2", s.campo(mapaCanu1, "string1_canu") + "");
			// Fecha de alta
			s.Campo("Texto67_2", s.campo(mapaCanu1, "fecha1_canu") + "");
			// Oficina de destino
			s.Campo("Texto10_2", s.campo(mapaCanu1, "string3_canu") + "");
			// Hora de alta
			s.Campo("Texto160_2", s.campo(mapaCanu1, "fecha2_canu") + "");
			// Fecha Otros organismos
			s.Campo("tFechaAltaOtraAdmi_2", s.campo(mapaCanu1, "fecha3_canu") + "");
			// Fecha de presentación
			s.Campo("tFechaPresent_2", s.campo(mapaCanu1, "fecha1_canu") + "");

			// Tipo/subtipo de expediente
			s.Campo("Texto4_2", s.campo(mapaCanu1, "string5_canu"));

			if (itRsCanu.hasNext()) {
				Map.Entry objCanu2 = (Map.Entry) itRsCanu.next();
				Map mapaCanu2 = (Map) objCanu2.getValue();
				s.Campo("Texto6_2", s.campo(mapaCanu2, "string1_canu"));

				s.Campo("tnumDOCORI_2", s.campo(mapaCanu2, "string2_canu"));
				s.Campo("tnombrenotDOCORI_2", s.campo(mapaCanu2, "string3_canu"));
				s.Campo("ttipoDOCORI_2", s.campo(mapaCanu2, "string5_canu"));
				s.Campo("tfechaprotDOCORI_2", s.campo(mapaCanu2, "fecha1_canu") + "");
			}
		}

		Iterator itRsPedb = rsPedb.entrySet().iterator();
		while (itRsPedb.hasNext()) {
			Map.Entry objPedb1 = (Map.Entry) itRsPedb.next();
			Map mapaPedb1 = (Map) objPedb1.getValue();
			persona = s.campo(mapaPedb1, "aeat_pedb");
			if ("1".equals(persona)) {
				
				s.Campo("Texto20_2", s.campo(mapaPedb1, "nombre_pedb"));
				s.Campo("Texto26_2", s.campo(mapaPedb1, "nif_pedb"));
				s.Campo("Texto29_2", s.campo(mapaPedb1, "telefono_pedb"));
				s.Campo("Texto23_2", s.campo(mapaPedb1, "sigla_pedb") + " " + s.campo(mapaPedb1, "calle_pedb") + ". " + s.campo(mapaPedb1, "cp_pedb")
						+ " - " + s.campo(mapaPedb1, "poblacion_pedb") + ", " + s.campo(mapaPedb1, "provincia_pedb") + ".");
			} else if ("2".equals(persona)) {
				if ("600".equals(modelo)) {
					s.Campo("tetiSP", "SUJETO PASIVO");
					s.Campo("tnomSP_2", s.campo(mapaPedb1, "nombre_pedb"));
					s.Campo("tnifSP_2", s.campo(mapaPedb1, "nif_pedb"));
				}
				if ("650".equals(modelo)) {
					s.Campo("tetiSP", "CAUSANTE");
					s.Campo("tnomSP_2", s.campo(mapaPedb1, "nombre_pedb"));
					s.Campo("tnifSP_2", s.campo(mapaPedb1, "nif_pedb"));
				}
				nifPdb = s.campo(mapaPedb1, "nif_pedb");
			} else if ("3".equals(persona)) {
				if ("651".equals(modelo)) {
					s.Campo("tetiSP", "DONANTE");
					s.Campo("tnomSP_2", s.campo(mapaPedb1, "nombre_pedb"));
					s.Campo("tnifSP_2", s.campo(mapaPedb1, "nif_pedb"));
				}
			}
			if (CirceValidation.isEmpty(nifPdb)){
				nifPdb = s.campo(mapaPedb1, "nif_pedb");
			}
		}

		Map.Entry objCanu2 = (Map.Entry) itRsCanu.next();
		Map mapaCanu2 = (Map) objCanu2.getValue();
		//if ("S".equals(s.campo(mapaCanu2, "string2_canu"))) {
		//}
		
		String stTotalIngresado = s.campo(mapaCanu2, "nume1_canu");
		if(NumberUtil.isNumberValid(stTotalIngresado)){
			totalIngresado = new Integer(stTotalIngresado);
		}

		s.Campo("autoliq1", numAutoliquidacion);
		if (totalIngresado != 0) {
			s.Campo("tipoautoliq1", NumberUtil.toEuro(s.campo(mapaCanu2, "nume1_canu")) + " €");
			
			stTotalIngresado = s.campo(mapaCanu2, "nume1_canu");
			if(NumberUtil.isNumberValid(stTotalIngresado)){
				totalIngresado = new Integer(stTotalIngresado);
			}
			s.campo(mapaCanu2, "fecha1_canu");
		} else {			
			if ("S".equals(s.campo(mapaCanu2, "string2_canu")) && ("600".equals(modelo))) {
				s.Campo("tipoautoliq1", "0,00 €");
			} else {
				s.Campo("tipoautoliq1", s.campo(mapaCanu2, "string3_canu"));
			}
		}
		if ("600".equals(modelo))
		{
			if (itRsCanu.hasNext())
			{
				Map.Entry objCanu3 = (Map.Entry) itRsCanu.next();
				Map mapaCanu3 = (Map) objCanu3.getValue();
				s.Campo("concepto",s.campo(mapaCanu3, "string1_canu"));
				s.Campo("adicional",s.campo(mapaCanu3, "string2_canu"));
			}
		}
		else 
		{
			s.BorrarCampo("cuadroExp");
			s.BorrarCampo("etiExp");
			s.BorrarCampo("txtconcep1");
			s.BorrarCampo("tIdenAdic");
			s.MueveBloque("tpiePagina",-30+"");
			
		}		    
		Iterator itRsMemo = rsMemo.entrySet().iterator();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();
		itRsMemo.next();

		if (itRsMemo.hasNext()){
			Map.Entry objMemo1 = (Map.Entry) itRsMemo.next();
			Map mapaMemo1 = (Map) objMemo1.getValue();
			String textoTitulo = s.campo(mapaMemo1, "string_memo");
	
			if (itRsMemo.hasNext()){
				Map.Entry objMemo2 = (Map.Entry) itRsMemo.next();
				Map mapaMemo2 = (Map) objMemo2.getValue();
				String texto = s.campo(mapaMemo2, "string_memo") + "\n\n";
		
				if (itRsMemo.hasNext()){
					Map.Entry objMemo3 = (Map.Entry) itRsMemo.next();
					Map mapaMemo3 = (Map) objMemo3.getValue();
					texto = texto + s.campo(mapaMemo3, "string_memo") + "\n\n";

				}
				s.Campo("titulopagina2", textoTitulo);				
				s.Campo("textoPpal", texto.replace(", y siendo la fecha de ingreso", "€, y siendo la fecha de ingreso"));
			}
		}

		s.Campo("jefe", "JEFE DEL ÁREA DE GESTIÓN TRIBUTARIA");
		s.Campo("firma", "Carlos Franco García");
		// Codigo de verificacion.
		this.codVerificacion = codigoVerificacion("C" + this.numeroAutoliquidacion + nifPdb);
		s.Campo("TextoVeri", "C" + this.numeroAutoliquidacion + "-"	+ this.codVerificacion);

		//Imprime el PDF.
		s.Mostrar();
		com.stpa.ws.server.util.Logger.debug("Fin presentacion(String id, String xml, String xsl, OutputStream output).",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
	}
	
}
