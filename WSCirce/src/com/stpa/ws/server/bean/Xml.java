package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Xml implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Fichero Fichero = new Fichero();
	private Firma_Fichero Firma_fichero = new Firma_Fichero();
	
	/*
	 * Getter and Setter.
	 */
	
	public Fichero getFichero() {
		return Fichero;
	}
	public Firma_Fichero getFirma_fichero() {
		return Firma_fichero;
	}
	public void setFichero(Fichero fichero) {
		this.Fichero = fichero;
	}
	public void setFirma_fichero(Firma_Fichero firma_fichero) {
		this.Firma_fichero = firma_fichero;
	}	
	
}
