package es.tributasenasturias.servicios.circe.externo.proceso;

import javax.xml.ws.BindingProvider;


import es.tributasenasturias.servicios.circe.previo.utils.Logger;
import es.tributasenasturias.servicios.circe.previo.utils.Preferencias;
import es.tributasenasturias.servicios.circe.previo.utils.UtilError;
import es.tributasenasturias.servicios.circe.previo.utils.Logger.LOGTYPE;
import es.tributasenasturias.servicios.client.circe.CirceImpuestoTrnsADJRespuesta;
import es.tributasenasturias.servicios.client.circe.CirceImpuestoTrnsADJSolicitud;
import es.tributasenasturias.servicios.client.circe.Stpaws;
import es.tributasenasturias.servicios.client.circe.StpawsService;

public class CirceCall {
	/**
	 * Realiza la llamada a CIRCE y devuelve el resultado en un mensaje en modo texto.
	 * @param mensaje Mensaje para CIRCE.
	 * @return mensaje que CIRCE devuelve o mensaje de error si no se puede llamar.
	 */
	public static String call (String mensaje)
	{
		String mensajeSalida="";
		Logger.debug("0.Recepción de mensaje", LOGTYPE.APPLOG);
		try
		{
			//Recuperamos el XML
			if (mensaje!=null && !"".equals(mensaje))
			{
				Logger.debug ("1.Mensaje recuperado:" + mensaje,LOGTYPE.APPLOG);
				Logger.debug ("2. Llamada a Servicio CIRCE SOAP.",LOGTYPE.APPLOG);
				CirceImpuestoTrnsADJSolicitud solic = buildCirceMsg(mensaje);
				CirceImpuestoTrnsADJRespuesta respuesta= callCirce(solic);
				//Transformamos la respuesta en un source
				if (respuesta!=null)
				{
					mensajeSalida=respuesta.getSCirceRespuesta();
					Logger.debug("3.Respuesta a devolver:"+mensajeSalida, LOGTYPE.APPLOG);
				}
				else
				{
					Logger.error("Error. El mensaje de CIRCE está vacío.", LOGTYPE.APPLOG);
					mensajeSalida=UtilError.getError();
				}
			}
			else
			{
				Logger.error("Error. El mensaje recibido está vacío.", LOGTYPE.APPLOG);
				mensajeSalida= UtilError.getError();
			}
		}
		catch (Exception ex)
		{
			Logger.error ("Error en conversión de mensaje entrante a mensaje de CIRCE:"+ ex.getMessage(), LOGTYPE.APPLOG);
			mensajeSalida = UtilError.getError();
		}
		return mensajeSalida;
	}
	
	/**
	 * Realiza la llamada al servicio web de circe.
	 * @param sol Solicitud al servicio
	 * @return {@link CirceImpuestoTrnsADJRespuesta} Respuesta del servicio
	 */
	private static CirceImpuestoTrnsADJRespuesta callCirce (CirceImpuestoTrnsADJSolicitud sol)
	{
		CirceImpuestoTrnsADJRespuesta respuesta=null;
		StpawsService srv = new StpawsService();
		Stpaws port = srv.getStpawsPort();
		//Modificamos el endpoint.
		Preferencias pref= new Preferencias();
		if (pref!=null && !"".equals(pref.getEndpointCirce()))
		{
			BindingProvider bpr = (BindingProvider) port;
			bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointCirce());
		}
		respuesta=port.doCirceImpuestoTrnsADJSolicitud(sol);
		return respuesta;
	}
	/**
	 * Construye la solicitud del servicio WSCIRCE.
	 * Consiste únicamente en una copia del mensaje de entrada en el nodo SCirceSolicitud
	 * @param mensaje
	 * @return
	 */
	private static CirceImpuestoTrnsADJSolicitud buildCirceMsg (String mensaje)
	{
		CirceImpuestoTrnsADJSolicitud mesg= new CirceImpuestoTrnsADJSolicitud();
		mesg.setSCirceSolicitud(mensaje);
		return mesg;
	}
}
