package com.stpa.ws.server.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class Escritura implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String Origen;
	@SuppressWarnings("unchecked")
	@XmlElementWrapper(name="Destinos")
	@XmlElement(type=String.class)
	private List Destino = new ArrayList();
	private String Formato;
	private String Nombre;
	private String Tamanyo;
	private String Fecha;
	private String Hora;	
	private Contenido Contenido = new Contenido();
		
	/*
	 * Getter and Setter.
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
	public Contenido getContenido() {
		return Contenido;
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
	public void setContenido(Contenido contenido) {
		this.Contenido = contenido;
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
