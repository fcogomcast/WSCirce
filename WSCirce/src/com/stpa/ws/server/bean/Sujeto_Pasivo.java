package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Sujeto_Pasivo implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String PRIMER_APELLIDO;
	private String SEGUNDO_APELLIDO;
	private String NOMBRE;
	private String NOMBRE_COMPLETO;
	private String NIF;
	private String EMAIL;
	private String TELEFONO;
	private String FAX;
	private String TIPO_VIA;
	private String NOMBRE_VIA;
	private String NUMERO;
	private String NUMERO_LOCAL;
	private String ESCALERA;
	private String PLANTA;
	private String PUERTA;
	private String BLOQUE;
	private String DUPLICADO;
	private String PORTAL_BLOQUE;
	private String PUERTA_GENERICA;
	private String PUERTA_ESPECIFICA;
	private String KILOMETRO;
	private String CODIGO_POSTAL;
	private Municipio MUNICIPIO = new Municipio();
	private Provincia PROVINCIA = new Provincia();
	private Pais PAIS = new Pais();
	
	/*
	 * Getter and Setter
	 */
	
	public String getPrimer_apellido() {
		if (PRIMER_APELLIDO==null)
			return "";
		return PRIMER_APELLIDO;
	}
	public String getSegundo_apellido() {
		if (SEGUNDO_APELLIDO==null)
			return "";
		return SEGUNDO_APELLIDO;
	}
	public String getNombre() {
		if (NOMBRE==null)
			return "";
		return NOMBRE;
	}
	public String getNombre_completo() {
		if (NOMBRE_COMPLETO==null)
			return "";
		return NOMBRE_COMPLETO;
	}
	public String getNif() {
		if (NIF==null)
			return "";
		return NIF;
	}
	public String getEmail() {
		if (EMAIL==null)
			return "";
		return EMAIL;
	}
	public String getTelefono() {
		if (TELEFONO==null)
			return "";
		return TELEFONO;
	}
	public String getFax() {
		if (FAX==null)
			return "";
		return FAX;
	}
	public String getTipo_via() {
		if (TIPO_VIA==null)
			return "";
		return TIPO_VIA;
	}
	public String getNombre_via() {
		if (NOMBRE_VIA==null)
			return "";
		return NOMBRE_VIA;
	}
	public String getNumero() {
		if (NUMERO==null)
			return "";
		return NUMERO;
	}
	public String getNumero_local() {
		if (NUMERO_LOCAL==null)
			return "";
		return NUMERO_LOCAL;
	}
	public String getEscalera() {
		if (ESCALERA==null)
			return "";
		return ESCALERA;
	}
	public String getPlanta() {
		if (PLANTA==null)
			return "";
		return PLANTA;
	}
	public String getPuerta() {
		if (PUERTA==null)
			return "";
		return PUERTA;
	}
	public String getBloque() {
		if (BLOQUE==null)
			return "";
		return BLOQUE;
	}
	public String getDuplicado() {
		if (DUPLICADO==null)
			return "";
		return DUPLICADO;
	}
	public String getPortal_bloque() {
		if (PORTAL_BLOQUE==null)
			return "";
		return PORTAL_BLOQUE;
	}
	public String getPuerta_generica() {
		if (PUERTA_GENERICA==null)
			return "";
		return PUERTA_GENERICA;
	}
	public String getPuerta_especifica() {
		if (PUERTA_ESPECIFICA==null)
			return "";
		return PUERTA_ESPECIFICA;
	}
	public String getKilometro() {
		if (KILOMETRO==null)
			return "";
		return KILOMETRO;
	}
	public String getCodigo_postal() {
		if (CODIGO_POSTAL==null)
			return "";
		return CODIGO_POSTAL;
	}
	public Municipio getMunicipio() {
		return MUNICIPIO;
	}
	public Provincia getProvincia() {
		return PROVINCIA;
	}
	public Pais getPais() {
		return PAIS;
	}
	public void setPrimer_apellido(String primer_apellido) {
		this.PRIMER_APELLIDO = primer_apellido;
	}
	public void setSegundo_apellido(String segundo_apellido) {
		this.SEGUNDO_APELLIDO = segundo_apellido;
	}
	public void setNombre(String nombre) {
		this.NOMBRE = nombre;
	}
	public void setNombre_completo(String nombre_completo) {
		this.NOMBRE_COMPLETO = nombre_completo;
	}
	public void setNif(String nif) {
		this.NIF = nif;
	}
	public void setEmail(String email) {
		this.EMAIL = email;
	}
	public void setTelefono(String telefono) {
		this.TELEFONO = telefono;
	}
	public void setFax(String fax) {
		this.FAX = fax;
	}
	public void setTipo_via(String tipo_via) {
		this.TIPO_VIA = tipo_via;
	}
	public void setNombre_via(String nombre_via) {
		this.NOMBRE_VIA = nombre_via;
	}
	public void setNumero(String numero) {
		this.NUMERO = numero;
	}
	public void setNumero_local(String numero_local) {
		this.NUMERO_LOCAL = numero_local;
	}
	public void setEscalera(String escalera) {
		this.ESCALERA = escalera;
	}
	public void setPlanta(String planta) {
		this.PLANTA = planta;
	}
	public void setPuerta(String puerta) {
		this.PUERTA = puerta;
	}
	public void setBloque(String bloque) {
		this.BLOQUE = bloque;
	}
	public void setDuplicado(String duplicado) {
		this.DUPLICADO = duplicado;
	}
	public void setPortal_bloque(String portal_bloque) {
		this.PORTAL_BLOQUE = portal_bloque;
	}
	public void setPuerta_generica(String puerta_generica) {
		this.PUERTA_GENERICA = puerta_generica;
	}
	public void setPuerta_especifica(String puerta_especifica) {
		this.PUERTA_ESPECIFICA = puerta_especifica;
	}
	public void setKilometro(String kilometro) {
		this.KILOMETRO = kilometro;
	}
	public void setCodigo_postal(String codigo_postal) {
		this.CODIGO_POSTAL = codigo_postal;
	}
	public void setMunicipio(Municipio municipio) {
		this.MUNICIPIO = municipio;
	}
	public void setProvincia(Provincia provincia) {
		this.PROVINCIA = provincia;
	}
	public void setPais(Pais pais) {
		this.PAIS = pais;
	}	
	
}
