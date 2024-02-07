package com.stpa.ws.server.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Errores implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	private String CODERROR;
	private String TEXTERROR;
	
	/*
	 * Getter and Setter.
	 */
	
	public String getCoderror() {
		return CODERROR;
	}
	public String getTexterror() {
		return TEXTERROR;
	}
	public void setCoderror(String coderror) {
		this.CODERROR = coderror;
	}
	public void setTexterror(String texterror) {
		this.TEXTERROR = texterror;
	}
	
	
	
}
