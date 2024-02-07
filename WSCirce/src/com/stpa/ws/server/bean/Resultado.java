package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Resultado implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String RETURNCODE;
	private Errores ERRORES = new Errores();
	
	/**
	 * Getter and setter
	 */
	
	public String getReturncode() {
		return RETURNCODE;
	}
	public Errores getErrores() {
		return ERRORES;
	}
	public void setReturncode(String returncode) {
		this.RETURNCODE = returncode;
	}
	public void setErrores(Errores errores) {
		this.ERRORES = errores;
	}
	
	
	
}
