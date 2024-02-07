package com.stpa.ws.server.bean;

import java.io.Serializable;

public class TratamientoErrores implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String retorno;
	private String codigoError;
	private String descripcionError;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getRetorno() {
		return retorno;
	}
	public String getCodigoError() {
		return codigoError;
	}
	public String getDescripcionError() {
		return descripcionError;
	}
	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}
	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}
	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	
	
	
}
