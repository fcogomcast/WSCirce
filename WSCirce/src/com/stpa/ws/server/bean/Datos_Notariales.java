package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Datos_Notariales implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String TIPO_DOCUMENTO;
	private String NUMERO_PROTOCOLO;
	private String ANYO_PROTOCOLO;
	private String OBSERVACIONES;
	private String NOMBRE_COMPLETO;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getNombre_completo() {
		if (NOMBRE_COMPLETO==null)
			return "";
		return NOMBRE_COMPLETO;
	}
	public void setNombre_completo(String nombre_completo) {
		NOMBRE_COMPLETO = nombre_completo;
	}
	public String getTipo_documento() {
		if (TIPO_DOCUMENTO==null)
			return "";
		return TIPO_DOCUMENTO;
	}
	public String getNumero_protocolo() {
		if (NUMERO_PROTOCOLO==null)
			return "";
		return NUMERO_PROTOCOLO;
	}
	public String getAnyo_protocolo() {
		if (ANYO_PROTOCOLO==null)
			return "";
		return ANYO_PROTOCOLO;
	}
	public String getObservaciones() {
		if (OBSERVACIONES==null)
			return "";
		return OBSERVACIONES;
	}
	public void setTipo_documento(String tipo_documento) {
		this.TIPO_DOCUMENTO = tipo_documento;
	}
	public void setNumero_protocolo(String numero_protocolo) {
		this.NUMERO_PROTOCOLO = numero_protocolo;
	}
	public void setAnyo_protocolo(String anyo_protocolo) {
		this.ANYO_PROTOCOLO = anyo_protocolo;
	}
	public void setObservaciones(String observaciones) {
		this.OBSERVACIONES = observaciones;
	}
	
}
