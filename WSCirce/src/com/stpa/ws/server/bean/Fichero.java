package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Fichero implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Identificacion Identificacion = new Identificacion();
	private Elementos Elementos = new Elementos();
	
	/*
	 * Getter and Setters.
	 */
	
	public Identificacion getIdentificacion() {
		return Identificacion;
	}
	public Elementos getElementos() {
		return Elementos;
	}
	public void setIdentificacion(Identificacion identificacion) {
		this.Identificacion = identificacion;
	}
	public void setElementos(Elementos elementos) {
		this.Elementos = elementos;
	}	
	
}
