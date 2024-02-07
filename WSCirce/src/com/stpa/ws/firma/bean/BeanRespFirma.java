package com.stpa.ws.firma.bean;

import java.io.Serializable;

public class BeanRespFirma implements Serializable {

	private static final long serialVersionUID = 1L;
	private byte[] firma;
    private boolean resultado;

    public BeanRespFirma() {
    }

    public BeanRespFirma(
           byte[] firma,           
           boolean resultado) {
           this.firma = firma;
           this.resultado = resultado;
    }


    /**
     * Gets the firma value for this BeanRespFirma.
     * 
     * @return firma
     */
    public byte[] getFirma() {
        return firma;
    }


    /**
     * Sets the firma value for this BeanRespFirma.
     * 
     * @param firma
     */
    public void setFirma(byte[] firma) {
        this.firma = firma;
    }


    /**
     * Gets the resultado value for this BeanRespFirma.
     * 
     * @return resultado
     */
    public boolean isResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this BeanRespFirma.
     * 
     * @param resultado
     */
    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }
	
}
