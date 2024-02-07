package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Municipio implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String CODIGO;
	private String NOMBRE;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getCodigo() {
		return CODIGO;
	}
	public String getNombre() {
		return NOMBRE;
	}
	public void setCodigo(String codigo) {
		this.CODIGO = codigo;
	}
	public void setNombre(String nombre) {
		this.NOMBRE = nombre;
	}

}
