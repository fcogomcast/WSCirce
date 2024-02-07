package es.tributasenasturias.servicios.circe.previo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilError {
	/**
	 * Construye la respuesta de error a enviar a CIRCE.
	 * @return
	 */
	public static String getError ()
	{
		String xmlEncoding = "ISO-8859-1";
		Date fecha = new Date();
        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateadorHora = new SimpleDateFormat("HH:mm:ss");
        return "<?xml version=\"1.0\" encoding=\""+xmlEncoding+"\"?><xml><Fichero><Identificacion><Tipo_Sociedad></Tipo_Sociedad><Id_comunicacion></Id_comunicacion><Id_tramite></Id_tramite><Descripcion></Descripcion><Fecha>"+formateadorFecha.format(fecha)+"</Fecha><Hora>"+formateadorHora.format(fecha)+"</Hora><Acuse></Acuse><Sincronia></Sincronia><Sistema_emisor>Principado de Asturias</Sistema_emisor><Sistema_receptor>CIRCE</Sistema_receptor><Retorno>1</Retorno></Identificacion><Elementos><Datos_interlocutores><Certificacion><Origen></Origen><Destinos><Destino></Destino><Destino></Destino></Destinos><Formato></Formato><Nombre/><Tamanyo/><Fecha></Fecha><Hora></Hora><Contenido/><Firma/></Certificacion></Datos_interlocutores><Datos_STTCirce><CABECERA><APLICACION></APLICACION><ID_PAIT></ID_PAIT><COMPONENTE></COMPONENTE></CABECERA><HECHO_IMPONIBLE><MODALIDAD></MODALIDAD><FECHA_PRESENTACION></FECHA_PRESENTACION><FECHA_DEVENGO></FECHA_DEVENGO><EXPRESION_ABREVIADA></EXPRESION_ABREVIADA><CONCEPTO></CONCEPTO><NUM_CERTIFICADO/><NUM_ITPAJD></NUM_ITPAJD></HECHO_IMPONIBLE><DATOS_NOTARIALES><TIPO_DOCUMENTO></TIPO_DOCUMENTO><NUMERO_PROTOCOLO></NUMERO_PROTOCOLO><NOMBRE_COMPLETO></NOMBRE_COMPLETO></DATOS_NOTARIALES><SUJETO><NOMBRE_COMPLETO></NOMBRE_COMPLETO><NIF></NIF><TIPO_VIA></TIPO_VIA><NOMBRE_VIA></NOMBRE_VIA><NUMERO></NUMERO><ESCALERA></ESCALERA><PLANTA></PLANTA><PUERTA></PUERTA><DUPLICADO></DUPLICADO><CODIGO_POSTAL></CODIGO_POSTAL><MUNICIPIO><CODIGO></CODIGO><NOMBRE></NOMBRE></MUNICIPIO><PROVINCIA><CODIGO></CODIGO><NOMBRE></NOMBRE></PROVINCIA></SUJETO><PAGO><TIPO_CONFIRMACION></TIPO_CONFIRMACION><FECHA_INGRESO></FECHA_INGRESO><INGRESO_IMPORTE></INGRESO_IMPORTE><CODIGO_PAGO/><TEXTO_PAGO/></PAGO><RESULTADO><RETURNCODE>1</RETURNCODE><ERRORES><CODERROR/><TEXTERROR>Error comunicacion con Principado de Asturias</TEXTERROR></ERRORES></RESULTADO></Datos_STTCirce></Elementos></Fichero><Firma_fichero><Firma/></Firma_fichero></xml>";
	}

}
