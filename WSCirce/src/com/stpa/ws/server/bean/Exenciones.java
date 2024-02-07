package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.stpa.ws.server.validation.CirceValidation;

@XmlAccessorType(XmlAccessType.FIELD)
public class Exenciones implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String TIPO_EXENCION;
	private String EXENTO;
	private String NO_SUJETO;
	private String FUNDAMENTO;
	private String CLAVE;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getTipo_exencion() {
		return TIPO_EXENCION;
	}
	public String getExento() {
		return EXENTO;
	}
	public String getNo_sujeto() {
		return NO_SUJETO;
	}
	public String getFundamento() {
		return FUNDAMENTO;
	}
	public String getClave() {
		return CLAVE;
	}
	public void setTipo_exencion(String tipo_exencion) {
		if (!CirceValidation.isEmpty(tipo_exencion)) {
			tipo_exencion = tipo_exencion.toUpperCase();
		}
		this.TIPO_EXENCION = tipo_exencion;
	}
	public void setExento(String exento) {
		this.EXENTO = exento;
	}
	public void setNo_sujeto(String no_sujeto) {
		if (!CirceValidation.isEmpty(no_sujeto)) {
			no_sujeto = no_sujeto.toUpperCase();
		}
		this.NO_SUJETO = no_sujeto;
	}
	public void setFundamento(String fundamento) {
		this.FUNDAMENTO = fundamento;
	}
	public void setClave(String clave) {
		this.CLAVE = clave;
	}
	
}
