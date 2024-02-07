package com.stpa.ws.server.formularios;

import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import com.stpa.ws.server.exception.StpawsException;

public abstract class FormPDFBase {
	
	public String plantilla;
	/** parametros de sesion. Se asignan o añaden directamente (FormPDFBase.Session.add("idioma","ES"). Ver constructor. */
	public Map<String, String> Session = new HashMap<String, String>();

	/**
	 * METODO DE UTILIZACION
	 * <br>
	 * FormPDFBase ff = FormPDFBase.generaFormulario("E1", "N", "1");
	 * <br>
	 * xslx = FormFillBase.getContents(WEBSERVICES_PATH+"/pdf/Portal/xml/impresos/" + ff.getPlantilla() + ".xml"); 
	 * <br>
	 * xml = Datos de Webservice;
	 * <br>
	 * ff.compila("", xml, xslx);
	 * <br>
	 * Constructor
	 */
	public FormPDFBase() {
		Session.put("idioma", "");
		Session.put("Justificante.numoperori", "");
		Session.put("Justificante.nrc", "");
		Session.put("Justificante.fechaPago", "");
		Session.put("Justificante.referec", "");
		Session.put("Justificante.nifcontrib", "");
		Session.put("cgestor", "");
		Session.put("recibo.referencia", "");
		Session.put("recibo.identificacion", "");
		Session.put("recibo.importe", "");
		Session.put("recibo.emisora", "");
		//nueva variable
		Session.put("Justificante.descrCopr", "");
		
	};

	/**
	 * Devuelve el nombre de la plantilla XML a usar
	 * @return 
	 */
	public abstract String getPlantilla();
	/**
	 * Imprime un formulario al stream de salida.
	 * @param xsl Plantilla XML.
	 * @param output Stream de salida, preferiblemente ByteArrayOutputStream o FileOutputStream.
	 * @throws RemoteException Excepcion por conexion remota a la ruta facilitada.
	 */
	public abstract void compila(String xsl, OutputStream output) throws StpawsException;

	/**
	 * Devuelve el nombre de la clase de formulario segun un numero
	 * @param codigo
	 * @return Nombre de la clase del formulario
	 */
	public static String getClaseFormulario(int codigo) {
		switch (codigo) {
		case 1:
			return "com.stpa.ws.server.formularios.AutoliquidacionNotificada";
		case 2:
			return "com.stpa.ws.server.formularios.Autoliquidacion";
		case 3:
			return "com.stpa.ws.server.formularios.LiquidacionNotif";
		case 4:
			return "com.stpa.ws.server.formularios.RecEjecutivaValorNoApremio";
		case 5:
			return "com.stpa.ws.server.formularios.RecEjecutivaValorNotif";
		case 6:
			return "com.stpa.ws.server.formularios.RecEjecutivaValor";
		case 7:
			return "com.stpa.ws.server.formularios.RecVoluntariaOnline";
		case 8:
			return "com.stpa.ws.server.formularios.Autoliquidacioniv";
		case 9:
			return "com.stpa.ws.server.formularios.JustificanteCobroExpInternet";
		case 10:
			return "com.stpa.ws.server.formularios.JustificanteCobroInternet";
		case 11:
			return "com.stpa.ws.server.formularios.JustificanteCobroInternet2";
		case 12:
			return "com.stpa.ws.server.formularios.RecEjecutivaValorNoApremio";
		case 13:
			return "com.stpa.ws.server.formularios.RecibosOnlineGrupoInternet";
		case 14:
			return "com.stpa.ws.server.formularios.TasasLiquidacion";
		case 15:
			return "com.stpa.ws.server.formularios.TasasLiquidacionNotif";
		case 16:
			return "com.stpa.ws.server.formularios.InfEmisionIC";
		case 117:
			return "com.stpa.ws.server.formularios.InfLiquidaTrans";
		case 17:
			return "com.stpa.ws.server.formularios.InfLiquidaTransm";
		case 18:
			return "com.stpa.ws.server.formularios.InfLisLiqTransNotifEje";
		case 19:
			return "com.stpa.ws.server.formularios.InfLisLiqTransEje";
		case 20:
			return "com.stpa.ws.server.formularios.InfLisLiqTransNotif";
		case 21:
			return "com.stpa.ws.server.formularios.InfLisLiqTransNotifEje";
		case 22:
			return "com.stpa.ws.server.formularios.PropLiqCed";
		case 23:
			return "com.stpa.ws.server.formularios.ComparecenciaCedidos";
		case 24:
			return "com.stpa.ws.server.formularios.ReqDoc";
		case 25:
			return "com.stpa.ws.server.formularios.JustificanteCobro";
		case 26:
			return "com.stpa.ws.server.formularios.TasasRecEjecutivaValor";
		case 27:
			return "com.stpa.ws.server.formularios.TasasRecEjecutivaValorNotif";
		case 28:
			return "com.stpa.ws.server.formularios.JustificantePresentacion";
		}
		return "";
	}

	/**
	 * Devuelve el nombre de la clase de formulario segun los parametros
	 * @param tipoInforme Tipo del informe en mayusculas
	 * @param noti {"N",""}
	 * @param string_cade Valor del primer elemento de la tabla STRING_CADE de WS. {"1",""}
	 * @return Nombre de la clase del formulario
	 */
	public static String getClaseFormulario(String tipoInforme, String noti, String string_cade) {
		/* Por el momento solo aplicable a Justificantes de Presentacion.
		 
		if ((tipoInforme.equals("AV") && !"N".equals(noti)) || tipoInforme.equals("LF"))
			return AutoliquidacionNotificada.class.getName();// "com.stpa.ws.server.formularios.AutoliquidacionNotificada";
		if (tipoInforme.equals("AL"))
			return Autoliquidacion.class.getName();// "com.stpa.ws.server.formularios.Autoliquidacion";
		if ((tipoInforme.equals("LI") && !"N".equals(noti)))
			return LiquidacionNotif.class.getName();// "com.stpa.ws.server.formularios.LiquidacionNotif";
		if ((tipoInforme.equals("E1") && "N".equals(noti) && "1".equals(string_cade))
				|| (tipoInforme.equals("EE") && "1".equals(string_cade)))
			return RecEjecutivaValorNoApremio.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValorNoApremio";
		if ((tipoInforme.equals("E1") && !"N".equals(noti)) || tipoInforme.equals("EN"))
			return RecEjecutivaValorNotif.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValorNotif";
		if ((tipoInforme.equals("E1") && "N".equals(noti) && !"1".equals(string_cade))
				|| (tipoInforme.equals("EE") && "1".equals(string_cade)))
			return RecEjecutivaValor.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValor";
		if (tipoInforme.equals("VO"))
			return RecVoluntariaOnline.class.getName();// "com.stpa.ws.server.formularios.RecVoluntariaOnline";
		if (tipoInforme.equals("PT"))
			return JustificanteCobroInternet2.class.getName();// "com.stpa.ws.server.formularios.RecVoluntariaOnline";

		if (tipoInforme.equals("VO"))
			return RecVoluntariaOnline.class.getName();// "com.stpa.ws.server.formularios.RecVoluntariaOnline";
		if (tipoInforme.equals("E1")) {
			if ("N".equals(noti))
				if ("1".equals(string_cade)) {
					return RecEjecutivaValorNotif.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValorNotif";
				} else {
					return RecEjecutivaValor.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValor";
				}
			else
				return RecEjecutivaValorNotif.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValorNotif";
		}
		if (tipoInforme.equals("LI")) {
			if ("N".equals(noti))
				return Liquidacion.class.getName();// LIQUIDACION
			else
				return LiquidacionNotif.class.getName();// "com.stpa.ws.server.formularios.LiquidacionNotif";
		}
		if (tipoInforme.equals("AL")) {
			if ("N".equals(noti))
				return Autoliquidacion.class.getName();// "com.stpa.ws.server.formularios.Autoliquidacion";
			else
				return AutoliquidacionNotificada.class.getName();// "com.stpa.ws.server.formularios.AutoliquidacionNotificada";
		}
		if (tipoInforme.equals("AV")) {
			if ("N".equals(noti))
				return Autoliquidacioniv.class.getName();// AUTOLIQUIDACION
			else
				return AutoliquidacionNotificada.class.getName();// "com.stpa.ws.server.formularios.AutoliquidacionNotificada";
		}
		if (tipoInforme.equals("LF"))
			return LiquidacionNotif.class.getName();// "com.stpa.ws.server.formularios.LiquidacionNotif";

		if (tipoInforme.equals("AN"))
			return AutoliquidacionNotificada.class.getName();// "com.stpa.ws.server.formularios.AutoliquidacionNotificada";

		if (tipoInforme.equals("EE"))
			if ("1".equals(string_cade))
				return RecEjecutivaValorNoApremio.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValorNoApremio";
			else
				return RecEjecutivaValor.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValor";

		if (tipoInforme.equals("EN"))
			return RecEjecutivaValorNotif.class.getName();// "com.stpa.ws.server.formularios.RecEjecutivaValorNotif";
		if (tipoInforme.equals("CC"))
			return ComparecenciaCedidos.class.getName();// com.stpa.ws.server.formularios.ComparecenciaCedidos
		if (tipoInforme.equals("RD"))
			return ReqDoc.class.getName();// com.stpa.ws.server.formularios.ReqDoc
		if (tipoInforme.equals("JC"))
			return JustificanteCobro.class.getName();// com.stpa.ws.server.formularios.ReqDoc
			*/
		if (tipoInforme.equals("CERT"))
			return JustificantePresentacion.class.getName();// com.stpa.ws.server.formularios.JustificantePresentacion

		return "";
	}	

	/**
	 * Genera una instancia del formulario para el tipo de informe con 2 parametros extra.
	 * @see getClaseFormulario()
	 * @param tipoInforme
	 * @param noti
	 * @param string_cade
	 * @return
	 */
	public static FormPDFBase generaFormulario(String tipoInforme, String noti, String string_cade) throws Exception {
		String clase = getClaseFormulario(tipoInforme, noti, string_cade);
		try {
			if (!clase.equals(""))
				return (FormPDFBase) Class.forName(clase).newInstance();
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("Error en la generacion del forulario: generaFormulario(String tipoInforme, String noti, String string_cade):tipoInforme:" + tipoInforme + e.toString(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return null;
	}	
	/**
	 * Devuelve la clase del formulario de tipo segun los parametros:
	 * Recibos,Tasas,Informativos,Autoliquidaciones,Justificante de cobro/pago...
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Class getClaseForm(String className) throws Exception {
		Class clase = null;

		try {
			if(!className.equals(""))
				clase = Class.forName(className);
		} catch (ClassNotFoundException e) {
			clase = null;
			com.stpa.ws.server.util.Logger.error("Error en el casteo de la clase: getClaseForm(String className):className:" + className + e.toString(), e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return clase;
	}

}
