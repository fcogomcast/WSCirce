package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Hecho_Imponible implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String MODALIDAD;
	private String ANEXO;
	private String FECHA_PRESENTACION;
	private String FECHA_DEVENGO;
	private String EXPRESION_ABREVIADA;
	private String CONCEPTO;
	private String NUM_CERTIFICADO;
	private String NUM_ITPAJD;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getModalidad() {
		return MODALIDAD;
	}
	public String getAnexo() {
		return ANEXO;
	}
	public String getFecha_presentacion() {
		return FECHA_PRESENTACION;
	}
	public String getFecha_devengo() {
		return FECHA_DEVENGO;
	}
	public String getExpresion_abreviada() {
		return EXPRESION_ABREVIADA;
	}
	public String getConcepto() {
		return CONCEPTO;
	}
	public String getNum_itpajd() {
		return NUM_ITPAJD;
	}
	public void setModalidad(String modalidad) {
		this.MODALIDAD = modalidad;
	}
	public void setAnexo(String anexo) {
		this.ANEXO = anexo;
	}
	public void setFecha_presentacion(String fecha_presentacion) {
		this.FECHA_PRESENTACION = fecha_presentacion;
	}
	public void setFecha_devengo(String fecha_devengo) {
		this.FECHA_DEVENGO = fecha_devengo;
	}
	public void setExpresion_abreviada(String expresion_abreviada) {
		this.EXPRESION_ABREVIADA = expresion_abreviada;
	}
	public void setConcepto(String concepto) {
		this.CONCEPTO = concepto;
	}
	public void setNum_itpajd(String num_itpajd) {
		this.NUM_ITPAJD = num_itpajd;
	}
	public String getNum_certificado() {
		return NUM_CERTIFICADO;
	}
	public void setNum_certificado(String num_certificado) {
		NUM_CERTIFICADO = num_certificado;
	}

}
