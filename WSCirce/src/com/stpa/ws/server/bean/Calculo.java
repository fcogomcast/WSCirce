package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Calculo implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Valores VALORES = new Valores();
	private String BASE_IMPONIBLE;
	private Reduccion REDUCCION = new Reduccion();
	private String BASE_LIQUIDABLE;
	private Tipo_Impositivo TIPO_IMPOSITIVO = new Tipo_Impositivo();
	private String CUOTA;
	private Bonificacion BONIFICACION = new Bonificacion();
	private String IMPORTE_INGRESAR;
	private String RECARGOR;
	private String INTERESES;
	private String TOTAL_INGRESAR;
	
	/*
	 * Getter and Setter.
	 */
	
	public Valores getValores() {
		return VALORES;
	}
	public String getBase_imponible() {
		return BASE_IMPONIBLE;
	}
	public Reduccion getReduccion() {
		return REDUCCION;
	}
	public String getBase_liquidable() {
		return BASE_LIQUIDABLE;
	}
	public Tipo_Impositivo getTipo_impositivo() {
		return TIPO_IMPOSITIVO;
	}
	public String getCuota() {
		return CUOTA;
	}
	public Bonificacion getBonificacion() {
		return BONIFICACION;
	}
	public String getImporte_ingresar() {
		return IMPORTE_INGRESAR;
	}
	public String getRecargo() {
		return RECARGOR;
	}
	public String getIntereses() {
		return INTERESES;
	}
	public String getTotal_ingresar() {
		return TOTAL_INGRESAR;
	}
	public void setValores(Valores valores) {
		this.VALORES = valores;
	}
	public void setBase_imponible(String base_imponible) {
		this.BASE_IMPONIBLE = base_imponible;
	}
	public void setReduccion(Reduccion reduccion) {
		this.REDUCCION = reduccion;
	}
	public void setBase_liquidable(String base_liquidable) {
		this.BASE_LIQUIDABLE = base_liquidable;
	}
	public void setTipo_impositivo(Tipo_Impositivo tipo_impositivo) {
		this.TIPO_IMPOSITIVO = tipo_impositivo;
	}
	public void setCuota(String cuota) {
		this.CUOTA = cuota;
	}
	public void setBonificacion(Bonificacion bonificacion) {
		this.BONIFICACION = bonificacion;
	}
	public void setImporte_ingresar(String importe_ingresar) {
		this.IMPORTE_INGRESAR = importe_ingresar;
	}
	public void setRecargo(String recargo) {
		this.RECARGOR = recargo;
	}
	public void setIntereses(String intereses) {
		this.INTERESES = intereses;
	}
	public void setTotal_ingresar(String total_ingresar) {
		this.TOTAL_INGRESAR = total_ingresar;
	}
	
}
