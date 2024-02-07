package com.stpa.ws.firma.bean;

import java.io.Serializable;

public class BeanRespValidar implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private boolean validacionCertificado;
    private boolean validacionFirma;
    private String X509Certificate;

    public BeanRespValidar() {
    }

    public BeanRespValidar(           
           boolean validacionCertificado,
           boolean validacionFirma) {
           this.validacionCertificado = validacionCertificado;
           this.validacionFirma = validacionFirma;
    }
    
    /**
     * Gets the validacionCertificado value for this BeanRespValidar.
     * 
     * @return validacionCertificado
     */
    public boolean isValidacionCertificado() {
        return validacionCertificado;
    }


    /**
     * Sets the validacionCertificado value for this BeanRespValidar.
     * 
     * @param validacionCertificado
     */
    public void setValidacionCertificado(boolean validacionCertificado) {
        this.validacionCertificado = validacionCertificado;
    }


    /**
     * Gets the validacionFirma value for this BeanRespValidar.
     * 
     * @return validacionFirma
     */
    public boolean isValidacionFirma() {
        return validacionFirma;
    }


    /**
     * Sets the validacionFirma value for this BeanRespValidar.
     * 
     * @param validacionFirma
     */
    public void setValidacionFirma(boolean validacionFirma) {
        this.validacionFirma = validacionFirma;
    }
    /**
     * Gets the X509Certificate value for this BeanRespValidar.
     * 
     * @return X509Certificate
     */
	public String getX509Certificate() {
		return X509Certificate;
	}
	/**
     * Sets the certificate value for this BeanRespValidar.
     * 
     * @param certificate
     */
	public void setX509Certificate(String certificate) {
		X509Certificate = certificate;
	}
}
