package com.stpa.ws.acceso.bean;

public class BeanFicheroSolicitudRespuesta {

	private static final long serialVersionUID = 1L;
	private String ficheroSolicitud = "";
	private boolean creadoFicheroSolicitud = false;
	private String ficheroRespuesta = "";
	private boolean creadoFicheroRespuesta = false;
	
	/**
	 * Getter and Setter.
	 */	
	
	public String getFicheroSolicitud() {
		return ficheroSolicitud;
	}
	public boolean isCreadoFicheroSolicitud() {
		return creadoFicheroSolicitud;
	}
	public String getFicheroRespuesta() {
		return ficheroRespuesta;
	}
	public boolean isCreadoFicheroRespuesta() {
		return creadoFicheroRespuesta;
	}
	public void setFicheroSolicitud(String ficheroSolicitud) {
		this.ficheroSolicitud = ficheroSolicitud;
	}
	public void setCreadoFicheroSolicitud(boolean creadoFicheroSolicitud) {
		this.creadoFicheroSolicitud = creadoFicheroSolicitud;
	}
	public void setFicheroRespuesta(String ficheroRespuesta) {
		this.ficheroRespuesta = ficheroRespuesta;
	}
	public void setCreadoFicheroRespuesta(boolean creadoFicheroRespuesta) {
		this.creadoFicheroRespuesta = creadoFicheroRespuesta;
	}	
	
}
