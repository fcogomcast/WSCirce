package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Intervinientes implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String NUM_PAS;
	private Sujeto_Pasivo SUJETO_PASIVO = new Sujeto_Pasivo();
	private Sujeto_Presentador SUJETO_PRESENTADOR = new Sujeto_Presentador();
	
	/*
	 * Getter and Setter.
	 */
	
	public String getNum_pas() {
		return NUM_PAS;
	}
	public Sujeto_Pasivo getSujeto_pasivo() {
		return SUJETO_PASIVO;
	}
	public Sujeto_Presentador getSujeto_presentador() {
		return SUJETO_PRESENTADOR;
	}
	public void setNum_pas(String num_pas) {
		this.NUM_PAS = num_pas;
	}
	public void setSujeto_pasivo(Sujeto_Pasivo sujeto_pasivo) {
		this.SUJETO_PASIVO = sujeto_pasivo;
	}
	public void setSujeto_presentador(Sujeto_Presentador sujeto_presentador) {
		this.SUJETO_PRESENTADOR = sujeto_presentador;
	}
	
}
