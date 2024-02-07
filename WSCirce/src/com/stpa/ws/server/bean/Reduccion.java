package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Reduccion implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String PORCENTAJE;
	private String IMPORTE;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getPorcentaje() {
		return PORCENTAJE;
	}
	public String getImporte() {
		return IMPORTE;
	}
	public void setPorcentaje(String porcentaje) {
		this.PORCENTAJE = porcentaje;
	}
	public void setImporte(String importe) {
		this.IMPORTE = importe;
	}

}
