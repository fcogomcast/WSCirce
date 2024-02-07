package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Pago implements Serializable {

	private static final long serialVersionUID = 1L;
	private String TIPO_CONFIRMACION;
	private Datos_Pago DATOS_PAGO = new Datos_Pago();
	private String FECHA_INGRESO;
	private String INGRESO_IMPORTE;
	private String CODIGO_PAGO;
	private String TEXTO_PAGO;	
	/*
	 * Getter and Setter.
	 */
	
	public String getTipo_confirmacion() {
		return TIPO_CONFIRMACION;
	}
	public Datos_Pago getDatos_pago() {
		return DATOS_PAGO;
	}
	public void setTipo_confirmacion(String tipo_confirmacion) {
		TIPO_CONFIRMACION = tipo_confirmacion;
	}
	public void setDatos_pago(Datos_Pago datos_pago) {
		this.DATOS_PAGO = datos_pago;
	}
	public String getFecha_ingreso() {
		return FECHA_INGRESO;
	}
	public String getIngreso_importe() {
		return INGRESO_IMPORTE;
	}
	public String getCodigo_pago() {
		return CODIGO_PAGO;
	}
	public String getTexto_pago() {
		return TEXTO_PAGO;
	}
	public void setFecha_ingreso(String fecha_ingreso) {
		FECHA_INGRESO = fecha_ingreso;
	}
	public void setIngreso_importe(String ingreso_importe) {
		INGRESO_IMPORTE = ingreso_importe;
	}
	public void setCodigo_pago(String codigo_pago) {
		CODIGO_PAGO = codigo_pago;
	}
	public void setTexto_pago(String texto_pago) {
		TEXTO_PAGO = texto_pago;
	}	
	
}
