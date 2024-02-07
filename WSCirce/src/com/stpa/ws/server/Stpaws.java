package com.stpa.ws.server;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Respuesta;
import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.op.CirceImpuestoTrnsADJop;
import com.stpa.ws.server.util.Logger;

@WebService (name = "Stpaws")
@HandlerChain (file="HandlerChainCirce.xml")
public class Stpaws {

	@WebMethod
	public CirceImpuestoTrnsADJ_Respuesta doCirceImpuestoTrnsADJSolicitud(CirceImpuestoTrnsADJ_Solicitud circeImpuestoTrnsADJ_Solicitud) {
		com.stpa.ws.server.util.Logger.info("INICIO ----- Servicio Web CIRCE Impuesto de Transmisiones y ADJ -------",Logger.LOGTYPE.APPLOG);
		CirceImpuestoTrnsADJop lcirceImpuestoTrnsADJop = new CirceImpuestoTrnsADJop();
		return (CirceImpuestoTrnsADJ_Respuesta)lcirceImpuestoTrnsADJop.doOwnAction(circeImpuestoTrnsADJ_Solicitud);
	}
}