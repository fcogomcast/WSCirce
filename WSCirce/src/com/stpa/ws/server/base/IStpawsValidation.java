package com.stpa.ws.server.base;

import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;
import com.stpa.ws.server.exception.StpawsException;

public interface IStpawsValidation {
	public boolean isAllowed(CirceImpuestoTrnsADJ_Solicitud lCirceImpuestoTrnsADJ) throws StpawsException;
}
