package com.stpa.ws.server.formularios;

import com.stpa.ws.server.util.DateUtil;

public class FormBeanAplPTel {

	private String catidadIngreso;
	private String fechaIngreso;
	private String fechaAplazamiento;
	private String sujeto_pasivo;
	private String nif;
	private String numAutoliquidacion;
	private String fecha;
	private String tipoCentificado;
	private String nombrePDF;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getNombrePDF() {
		return nombrePDF;
	}
	public void setNombrePDF(String nombrePDF) {
		this.nombrePDF = nombrePDF;
	}
	public String getCatidadIngreso() {
		return catidadIngreso;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public String getFechaAplazamiento() {
		return fechaAplazamiento;
	}
	public String getSujeto_pasivo() {
		return sujeto_pasivo;
	}
	public String getNif() {
		return nif;
	}
	public String getNumAutoliquidacion() {
		return numAutoliquidacion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setCatidadIngreso(String catidadIngreso) {
		this.catidadIngreso = catidadIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public void setFechaAplazamiento(String fechaAplazamiento) {
		this.fechaAplazamiento = fechaAplazamiento;
	}
	public void setSujeto_pasivo(String sujeto_pasivo) {
		this.sujeto_pasivo = sujeto_pasivo;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public void setNumAutoliquidacion(String numAutoliquidacion) {
		this.numAutoliquidacion = numAutoliquidacion;
	}
	public void setFecha() {
		this.fecha = DateUtil.fechaSalida();
	}
	public String getTipoCentificado() {
		return tipoCentificado;
	}
	public void setTipoCentificado(String tipoCentificado) {
		this.tipoCentificado = tipoCentificado;
	}	
	
}
