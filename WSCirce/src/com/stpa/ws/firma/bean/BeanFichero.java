package com.stpa.ws.firma.bean;

public class BeanFichero {
	
	private static final long serialVersionUID = 1L;
	private String rutaFichero = "";
	private String rutap7s = "";
	private boolean firmado = false;
	
	public String getRutaFichero() {
		return rutaFichero;
	}
	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}
	public String getRutap7s() {
		return rutap7s;
	}
	public void setRutap7s(String rutap7s) {
		this.rutap7s = rutap7s;
	}
	public boolean isFirmado() {
		return firmado;
	}
	public void setFirmado(boolean firmado) {
		this.firmado = firmado;
	}
	
}
