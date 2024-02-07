package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Elementos implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private Datos_Interlocutores Datos_interlocutores = new Datos_Interlocutores();
	private Datos_STTCirce Datos_STTCirce = new Datos_STTCirce();
	
	/*
	 * Getter and Setters.
	 */
	
	public Datos_Interlocutores getDatos_interlocutores() {
		return Datos_interlocutores;
	}
	public Datos_STTCirce getDatos_STTCirce() {
		return Datos_STTCirce;
	}
	public void setDatos_interlocutores(Datos_Interlocutores datos_interlocutores) {
		this.Datos_interlocutores = datos_interlocutores;
	}
	public void setDatos_STTCirce(Datos_STTCirce datos_STTCirce) {
		this.Datos_STTCirce = datos_STTCirce;
	}
	
}
