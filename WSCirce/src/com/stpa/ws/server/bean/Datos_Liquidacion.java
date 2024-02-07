package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Datos_Liquidacion implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Valores VALORES = new Valores();
	private Exenciones EXENCIONES = new Exenciones();
	private Complementaria COMPLEMENTARIA = new Complementaria();
	
	/*
	 * Getter and Setter.
	 */
	
	public Valores getValores() {
		return VALORES;
	}
	public Exenciones getExenciones() {
		return EXENCIONES;
	}
	public Complementaria getComplementaria() {
		return COMPLEMENTARIA;
	}
	public void setValores(Valores valores) {
		this.VALORES = valores;
	}
	public void setExenciones(Exenciones exenciones) {
		this.EXENCIONES = exenciones;
	}
	public void setComplementaria(Complementaria complementaria) {
		this.COMPLEMENTARIA = complementaria;
	}
	
}
