package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Datos_Pago implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String ENTIDAD;
	private String OFICINA;
	private String CONTROL;
	private String CUENTA;
	private String NIF_TITULAR;
	private String NOMBRE_COMPLETO_TITULAR;
	private String BANCO_SUCURSAL;
	private String FECHA_INGRESO;
	private String INGRESO_IMPORTE;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getEntidad() {
		return ENTIDAD;
	}
	public String getOficina() {
		return OFICINA;
	}
	public String getControl() {
		return CONTROL;
	}
	public String getCuenta() {
		return CUENTA;
	}
	public String getNif_titular() {
		return NIF_TITULAR;
	}
	public String getNombre_completo_titular() {
		return NOMBRE_COMPLETO_TITULAR;
	}
	public String getBanco_sucursal() {
		return BANCO_SUCURSAL;
	}
	public String getFecha_ingreso() {
		return FECHA_INGRESO;
	}
	public String getIngreso_importe() {
		return INGRESO_IMPORTE;
	}
	public void setEntidad(String entidad) {
		this.ENTIDAD = entidad;
	}
	public void setOficina(String oficina) {
		this.OFICINA = oficina;
	}
	public void setControl(String control) {
		this.CONTROL = control;
	}
	public void setCuenta(String cuenta) {
		this.CUENTA = cuenta;
	}
	public void setNif_titular(String nif_titular) {
		this.NIF_TITULAR = nif_titular;
	}
	public void setNombre_completo_titular(String nombre_completo_titular) {
		this.NOMBRE_COMPLETO_TITULAR = nombre_completo_titular;
	}
	public void setBanco_sucursal(String banco_sucursal) {
		this.BANCO_SUCURSAL = banco_sucursal;
	}
	public void setFecha_ingreso(String fecha_ingreso) {
		this.FECHA_INGRESO = fecha_ingreso;
	}
	public void setIngreso_importe(String ingreso_importe) {
		this.INGRESO_IMPORTE = ingreso_importe;
	}
	
}
