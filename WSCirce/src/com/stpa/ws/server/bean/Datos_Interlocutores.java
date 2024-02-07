package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Datos_Interlocutores implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Escritura Escritura = new Escritura();
	private Certificacion Certificacion = new Certificacion();
	
	/*
	 * Getter and Setter.
	 */
	
	public Escritura getEscritura() {
		return Escritura;
	}
	public void setEscritura(Escritura escritura) {
		this.Escritura = escritura;
	}
	public Certificacion getCertificacion() {
		return Certificacion;
	}
	public void setCertificacion(Certificacion certificacion) {
		this.Certificacion = certificacion;
	}

}
