package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
@XmlAccessorType(XmlAccessType.FIELD)
public class Cabecera implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String APLICACION;
	private String ID_PAIT;
	private String COMPONENTE;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getComponente() {
		return COMPONENTE;
	}
	public void setComponente(String componente) {
		COMPONENTE = componente;
	}
	public String getAplicacion() {
		return APLICACION;
	}
	public String getId_pait() {
		return ID_PAIT;
	}
	public void setAplicacion(String aplicacion) {
		this.APLICACION = aplicacion;
	}
	public void setId_pait(String id_pait) {
		this.ID_PAIT = id_pait;
	}

}
