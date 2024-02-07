package com.stpa.ws.firma.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.stpa.ws.firma.bean.BeanFichero;
import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;

public class UtilsWBFirma {	
	/**
	 * Procesar el contenido pasado como String en parte CONTENIDO y FIRMA contenido para su verificaion.
	 * @param sFile Strinf fichero completo.
	 * @return Bean con los datos del proceso Verificacion.
	 * @throws Exception Error durante el proceso.
	 */
	public static BeanFichero ProcessCharacters(String sFile) throws Exception {
		// Inicializar preferencias para carga de  variables.
		Preferencias pref = new Preferencias();
		BeanFichero bF = new BeanFichero();
		bF.setRutaFichero(pref.getM_pathSolresp() + CirceImpuestoTrnsADJConstantes.nombreFicheroContenido + getDate() + ".txt");
		bF.setRutap7s(pref.getM_pathSolresp() + CirceImpuestoTrnsADJConstantes.nombreFicheroFirma + getDate() + ".p7s");
		bF.setFirmado(ExtraccionYTratamientoFicheros.procesarFichero(sFile, bF.getRutaFichero(), bF.getRutap7s()));
		return bF;
	}	
	
	/**
	 * Formato fecha para tratamiento fichero CONTENIDO y FIRMA.
	 * @return String resultante.
	 */
	public static String getDate() {
		String DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_SSS";
		String numUnico = Double.toString(Math.random());
		getDateTime(DATE_FORMAT_NOW);
		return getDateTime(DATE_FORMAT_NOW)+numUnico;
	}
	/**
	 * Tramitaremos el objeto date bajo el formato de fecha y hora demandado.
	 * @param formato
	 * @return
	 */
	public static String getDateTime(String formato) {
        DateFormat dateFormat = new SimpleDateFormat(formato);
        Date date = new Date();
        return dateFormat.format(date);
    }	
	
}
