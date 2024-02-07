package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Complementaria implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String COMPLEMENTARIA;
	private String NUMERO_PRESENTACION;
	private String FECHA_PRESENTACION;
	private String NUMERO_JUSTIFICANTE;
	private String IMPORTE_INGRESADO;
	
	/*
	 * Getter and Setter
	 */
	
	public String getComplementaria() {
		return COMPLEMENTARIA;
	}
	public String getNumero_presentacion() {
		return NUMERO_PRESENTACION;
	}
	public String getFecha_presentacion() {
		return FECHA_PRESENTACION;
	}
	public String getNumero_justificante() {
		return NUMERO_JUSTIFICANTE;
	}
	public String getImporte_ingresado() {
		return IMPORTE_INGRESADO;
	}
	public void setComplementaria(String complementaria) {
		this.COMPLEMENTARIA = complementaria;
	}
	public void setNumero_presentacion(String numero_presentacion) {
		this.NUMERO_PRESENTACION = numero_presentacion;
	}
	public void setFecha_presentacion(String fecha_presentacion) {
		this.FECHA_PRESENTACION = fecha_presentacion;
	}
	public void setNumero_justificante(String numero_justificante) {
		this.NUMERO_JUSTIFICANTE = numero_justificante;
	}
	public void setImporte_ingresado(String importe_ingresado) {
		this.IMPORTE_INGRESADO = importe_ingresado;
	}	
	
}
