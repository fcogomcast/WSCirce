package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Firma_Fichero implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String Firma;

	/*
	 * Getter and Setter.
	 */
	
	public String getFirma() {
		return Firma;
	}

	public void setFirma(String firma) {
		this.Firma = firma;
	}	
	
}
