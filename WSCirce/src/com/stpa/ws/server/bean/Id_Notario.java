package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Id_Notario implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String NIF_NOTARIO;
	private String CODIGO_NOTARIO;
	private String NOMBRE;
	private String APELLIDOS;
	private String NOMBRE_COMPLETO;
	private String LOCALIDAD;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getNif_notario() {
		return NIF_NOTARIO;
	}
	public String getCodigo_notario() {
		return CODIGO_NOTARIO;
	}
	public String getNombre() {
		return NOMBRE;
	}
	public String getApellidos() {
		return APELLIDOS;
	}
	public String getNombre_completo() {
		return NOMBRE_COMPLETO;
	}
	public String getLocalidad() {
		return LOCALIDAD;
	}
	public void setNif_notario(String nif_notario) {
		this.NIF_NOTARIO = nif_notario;
	}
	public void setCodigo_notario(String codigo_notario) {
		this.CODIGO_NOTARIO = codigo_notario;
	}
	public void setNombre(String nombre) {
		this.NOMBRE = nombre;
	}
	public void setApellidos(String apellidos) {
		this.APELLIDOS = apellidos;
	}
	public void setNombre_completo(String nombre_completo) {
		this.NOMBRE_COMPLETO = nombre_completo;
	}
	public void setLocalidad(String localidad) {
		this.LOCALIDAD = localidad;
	}	
	
}
