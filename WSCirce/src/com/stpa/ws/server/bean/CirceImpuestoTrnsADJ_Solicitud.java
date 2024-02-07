package com.stpa.ws.server.bean;

import java.io.Serializable;

public class CirceImpuestoTrnsADJ_Solicitud implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Xml xml = new Xml();
	private String sCirceSolicitud = "";

	/*
	 * Getter and Setter.
	 */
	
	public Xml getXml() {
		return xml;
	}

	public void setXml(Xml xml) {
		this.xml = xml;
	}

	public String getSCirceSolicitud() {
		return sCirceSolicitud;
	}

	public void setSCirceSolicitud(String circeSolicitud) {
		sCirceSolicitud = circeSolicitud;
	}
	
}
