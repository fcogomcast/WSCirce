package com.stpa.ws.server.bean;

import java.io.Serializable;

public class CirceImpuestoTrnsADJ_Respuesta implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Xml xml = new Xml();
	private String sCirceRespuesta = "";

	/*
	 * Getter and Setter.
	 */
	
	public Xml getXml() {
		return xml;
	}

	public void setXml(Xml xml) {
		this.xml = xml;
	}

	public String getSCirceRespuesta() {
		return sCirceRespuesta;
	}

	public void setSCirceRespuesta(String circeRespuesta) {
		sCirceRespuesta = circeRespuesta;
	}
		
}
