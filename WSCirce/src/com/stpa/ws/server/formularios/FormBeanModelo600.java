package com.stpa.ws.server.formularios;

import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;

public class FormBeanModelo600 {
	
	private String numAutoliquidacion;
	private String diaFecha;
	private String mesFecha;
	private String anyoFecha;
	private final String datoEspecifico1 = "T";
	private final String datoEspecifico2 = "O";
	private FormBeanModelo600SujetoPasivo fbModelo600SujetoPasivo		= new FormBeanModelo600SujetoPasivo();
	private FormBeanModelo600Transmitentes fbModelo600Transmitentes		= new FormBeanModelo600Transmitentes();
	private FormBeanModelo600Presentador fbModelo600Presentador			= new FormBeanModelo600Presentador();
	private FormBeanModelo600DatosNotariales fbModelo600DatosNotariales	= new FormBeanModelo600DatosNotariales();
	private FormBeanModelo600Liquidacion fbModelo600Liquidacion	= new FormBeanModelo600Liquidacion();
	private final String nombreModelo600 = CirceImpuestoTrnsADJConstantes.constantModelo_600;
	
	/*
	 * Getter and Setter
	 */
	
	public String getNumAutoliquidacion() {
		return numAutoliquidacion;
	}
	public String getDiaFecha() {
		return diaFecha;
	}
	public String getMesFecha() {
		return mesFecha;
	}
	public String getAnyoFecha() {
		return anyoFecha;
	}
	public String getDatoEspecifico1() {
		return datoEspecifico1;
	}
	public String getDatoEspecifico2() {
		return datoEspecifico2;
	}
	public FormBeanModelo600SujetoPasivo getFbModelo600SujetoPasivo() {
		return fbModelo600SujetoPasivo;
	}
	public FormBeanModelo600Transmitentes getFbModelo600Transmitentes() {
		return fbModelo600Transmitentes;
	}
	public FormBeanModelo600Presentador getFbModelo600Presentador() {
		return fbModelo600Presentador;
	}
	public FormBeanModelo600DatosNotariales getFbModelo600DatosNotariales() {
		return fbModelo600DatosNotariales;
	}
	public FormBeanModelo600Liquidacion getFbModelo600Liquidacion() {
		return fbModelo600Liquidacion;
	}
	public String getNombreModelo600() {
		return nombreModelo600;
	}
	public void setNumAutoliquidacion(String numAutoliquidacion) {
		this.numAutoliquidacion = numAutoliquidacion;
	}
	public void setDiaFecha(String diaFecha) {
		this.diaFecha = diaFecha;
	}
	public void setMesFecha(String mesFecha) {
		this.mesFecha = mesFecha;
	}
	public void setAnyoFecha(String anyoFecha) {
		this.anyoFecha = anyoFecha;
	}
	public void setFbModelo600SujetoPasivo(
			FormBeanModelo600SujetoPasivo fbModelo600SujetoPasivo) {
		this.fbModelo600SujetoPasivo = fbModelo600SujetoPasivo;
	}
	public void setFbModelo600Transmitentes(
			FormBeanModelo600Transmitentes fbModelo600Transmitentes) {
		this.fbModelo600Transmitentes = fbModelo600Transmitentes;
	}
	public void setFbModelo600Presentador(
			FormBeanModelo600Presentador fbModelo600Presentador) {
		this.fbModelo600Presentador = fbModelo600Presentador;
	}
	public void setFbModelo600DatosNotariales(
			FormBeanModelo600DatosNotariales fbModelo600DatosNotariales) {
		this.fbModelo600DatosNotariales = fbModelo600DatosNotariales;
	}
	public void setFbModelo600Liquidacion(
			FormBeanModelo600Liquidacion fbModelo600Liquidacion) {
		this.fbModelo600Liquidacion = fbModelo600Liquidacion;
	}	
	
}
