package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Datos_STTCirce implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Cabecera CABECERA = new Cabecera();
	private Id_Notario ID_NOTARIO = new Id_Notario();
	private Hecho_Imponible HECHO_IMPONIBLE = new Hecho_Imponible();
	private Datos_Notariales DATOS_NOTARIALES = new Datos_Notariales();
	private Intervinientes INTERVINIENTES = new Intervinientes();
	private Datos_Liquidacion DATOS_LIQUIDACION = new Datos_Liquidacion();
	private Calculo CALCULO = new Calculo();
	private Sujeto SUJETO = new Sujeto();
	private Pago PAGO = new Pago();	
	private Resultado RESULTADO = new Resultado();
	
	/*
	 * Getter and Setter.
	 */
	
	public Cabecera getCabecera() {
		return CABECERA;
	}
	public Id_Notario getId_notario() {
		return ID_NOTARIO;
	}
	public Hecho_Imponible getHecho_imponible() {
		return HECHO_IMPONIBLE;
	}
	public Datos_Notariales getDatos_notariales() {
		return DATOS_NOTARIALES;
	}
	public Intervinientes getIntervinientes() {
		return INTERVINIENTES;
	}
	public Datos_Liquidacion getDatos_liquidacion() {
		return DATOS_LIQUIDACION;
	}
	public Calculo getCalculo() {
		return CALCULO;
	}
	public Pago getPago() {
		return PAGO;
	}
	public void setCabecera(Cabecera cabecera) {
		this.CABECERA = cabecera;
	}
	public void setId_notario(Id_Notario id_notario) {
		this.ID_NOTARIO = id_notario;
	}
	public void setHecho_imponible(Hecho_Imponible hecho_imponible) {
		this.HECHO_IMPONIBLE = hecho_imponible;
	}
	public void setDatos_notariales(Datos_Notariales datos_notariales) {
		this.DATOS_NOTARIALES = datos_notariales;
	}
	public void setIntervinientes(Intervinientes intervinientes) {
		this.INTERVINIENTES = intervinientes;
	}
	public void setDatos_liquidacion(Datos_Liquidacion datos_liquidacion) {
		this.DATOS_LIQUIDACION = datos_liquidacion;
	}
	public void setCalculo(Calculo calculo) {
		this.CALCULO = calculo;
	}
	public void setPago(Pago pago) {
		this.PAGO = pago;
	}
	public Sujeto getSujeto() {
		return SUJETO;
	}
	public Resultado getResultado() {
		return RESULTADO;
	}
	public void setSujeto(Sujeto sujeto) {
		this.SUJETO = sujeto;
	}
	public void setResultado(Resultado resultado) {
		this.RESULTADO = resultado;
	}
	
}
