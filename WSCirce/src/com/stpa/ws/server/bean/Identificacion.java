package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Identificacion implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String Tipo_Sociedad;
	private String Id_comunicacion;
	private String Id_tramite;
	private String Descripcion;
	private String Fecha;
	private String Hora;
	private String Acuse;
	private String Sincronia;
	private String Sistema_emisor;
	private String Sistema_receptor;
	private String Retorno;
	
	/*
	 * Getter and Setter
	 */
	
	public String getTipo_Sociedad() {
		return Tipo_Sociedad;
	}
	public String getId_comunicacion() {
		return Id_comunicacion;
	}
	public String getId_tramite() {
		return Id_tramite;
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public String getFecha() {
		return Fecha;
	}
	public String getHora() {
		return Hora;
	}
	public String getAcuse() {
		return Acuse;
	}
	public String getSincronia() {
		return Sincronia;
	}
	public String getSistema_emisor() {
		return Sistema_emisor;
	}
	public String getSistema_receptor() {
		return Sistema_receptor;
	}
	public String getRetorno() {
		return Retorno;
	}
	public void setTipo_Sociedad(String tipo_Sociedad) {
		Tipo_Sociedad = tipo_Sociedad;
	}
	public void setId_comunicacion(String id_comunicacion) {
		Id_comunicacion = id_comunicacion;
	}
	public void setId_tramite(String id_tramite) {
		Id_tramite = id_tramite;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	public void setFecha(String fecha) {
		Fecha = fecha;
	}
	public void setHora(String hora) {
		Hora = hora;
	}
	public void setAcuse(String acuse) {
		Acuse = acuse;
	}
	public void setSincronia(String sincronia) {
		Sincronia = sincronia;
	}
	public void setSistema_emisor(String sistema_emisor) {
		Sistema_emisor = sistema_emisor;
	}
	public void setSistema_receptor(String sistema_receptor) {
		Sistema_receptor = sistema_receptor;
	}
	public void setRetorno(String retorno) {
		Retorno = retorno;
	}			
	
}
