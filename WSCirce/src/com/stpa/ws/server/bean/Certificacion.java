package com.stpa.ws.server.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class Certificacion implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String Origen;
	@SuppressWarnings("unchecked")
	@XmlElementWrapper(name = "Destinos")
	@XmlElement(type=String.class)
	private List Destino = new ArrayList();
	private String Formato;
	private String Nombre;
	private String Tamanyo;
	private String Fecha;
	private String Hora;	
	//@XmlMimeType("bin.base64")
	private String Contenido;
	private String Firma;
	
	/*
	 * Getter and Setter
	 */
	
	public String getOrigen() {
		return Origen;
	}	
	public String getFormato() {
		return Formato;
	}
	public String getNombre() {
		return Nombre;
	}
	public String getTamanyo() {
		return Tamanyo;
	}
	public String getHora() {
		return Hora;
	}
	public String getFecha() {
		return Fecha;
	}
	public String getContenido() {
		return Contenido;
	}
	public String getFirma() {
		return Firma;
	}
	public void setOrigen(String origen) {
		this.Origen = origen;
	}	
	public void setFormato(String formato) {
		this.Formato = formato;
	}
	public void setNombre(String nombre) {
		this.Nombre = nombre;
	}
	public void setTamanyo(String tamanyo) {
		this.Tamanyo = tamanyo;
	}
	public void setHora(String hora) {
		this.Hora = hora;
	}
	public void setFecha(String fecha) {
		this.Fecha = fecha;
	}
	public void setContenido(String contenido) {
		this.Contenido = contenido;
	}
	public void setFirma(String firma) {
		Firma = firma;
	}
	@SuppressWarnings("unchecked")
	public List getDestinos() {
		return Destino;
	}
	@SuppressWarnings("unchecked")
	public void setDestinos(List destinos) {
		this.Destino = destinos;
	}		
}
