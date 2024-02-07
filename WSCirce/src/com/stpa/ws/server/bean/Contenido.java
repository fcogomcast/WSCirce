package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Contenido implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String Documento;
	private String Firma;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getDocumento() {
		return Documento;
	}
	public String getFirma() {
		return Firma;
	}
	public void setDocumento(String documento) {
		this.Documento = documento;
	}
	public void setFirma(String firma) {
		this.Firma = firma;
	}	
	
}
