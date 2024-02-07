package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Sujeto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String NOMBRE_COMPLETO;
	private String NIF;
	private String TIPO_VIA;
	private String NOMBRE_VIA;
	private String NUMERO;
	private String ESCALERA;
	private String PLANTA;
	private String PUERTA;
	private String DUPLICADO;
	private String CODIGO_POSTAL;
	private Municipio MUNICIPIO = new Municipio();
	private Provincia PROVINCIA = new Provincia();
	
	/**
	 * Getter and Setter
	 */
	
	public String getNombre_completo() {
		return NOMBRE_COMPLETO;
	}
	public String getNif() {
		return NIF;
	}
	public String getTipo_via() {
		return TIPO_VIA;
	}
	public String getNombre_via() {
		return NOMBRE_VIA;
	}
	public String getNumero() {
		return NUMERO;
	}
	public String getEscalera() {
		return ESCALERA;
	}
	public String getPlanta() {
		return PLANTA;
	}
	public String getPuerta() {
		return PUERTA;
	}
	public String getDuplicado() {
		return DUPLICADO;
	}
	public String getCodigo_postal() {
		return CODIGO_POSTAL;
	}
	public Municipio getMunicipio() {
		return MUNICIPIO;
	}
	public Provincia getProvincia() {
		return PROVINCIA;
	}
	public void setNombre_completo(String nombre_completo) {
		NOMBRE_COMPLETO = nombre_completo;
	}
	public void setNif(String nif) {
		NIF = nif;
	}
	public void setTipo_via(String tipo_via) {
		TIPO_VIA = tipo_via;
	}
	public void setNombre_via(String nombre_via) {
		NOMBRE_VIA = nombre_via;
	}
	public void setNumero(String numero) {
		NUMERO = numero;
	}
	public void setEscalera(String escalera) {
		ESCALERA = escalera;
	}
	public void setPlanta(String planta) {
		PLANTA = planta;
	}
	public void setPuerta(String puerta) {
		PUERTA = puerta;
	}
	public void setDuplicado(String duplicado) {
		DUPLICADO = duplicado;
	}
	public void setCodigo_postal(String codigo_postal) {
		CODIGO_POSTAL = codigo_postal;
	}
	public void setMunicipio(Municipio municipio) {
		this.MUNICIPIO = municipio;
	}
	public void setProvincia(Provincia provincia) {
		this.PROVINCIA = provincia;
	}
	
}
