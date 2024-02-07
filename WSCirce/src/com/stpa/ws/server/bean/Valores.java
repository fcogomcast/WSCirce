package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Valores implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String VALOR_DECLARADO;

	/*
	 * Getter and Setter.
	 */
	
	public String getValor_declarado() {
		return VALOR_DECLARADO;
	}

	public void setValor_declarado(String valor_declarado) {
		VALOR_DECLARADO = valor_declarado;
	}
	
}
