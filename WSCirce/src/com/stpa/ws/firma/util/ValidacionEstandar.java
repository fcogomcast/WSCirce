package com.stpa.ws.firma.util;

import com.stpa.ws.firma.bean.BeanFichero;
import com.stpa.ws.firma.bean.BeanRespValidar;

public class ValidacionEstandar {
	/**
	 * Metodo de validación del contenido firmado.
	 * @param file Fichero entrada con el contenido y la firma
	 * @return Objeto salida de la verificación del contenido de la firma.
	 * @throws Exception Error controlado del sistema.
	 */
	public BeanRespValidar doValidacionFirmaCirce(String sfile) throws Exception {
		BeanRespValidar resultado = new BeanRespValidar();
		
		boolean resultFirma = false;
		boolean resultCertificado = false;
		resultado.setValidacionFirma(resultFirma);
		resultado.setValidacionCertificado(resultCertificado);
		
		BeanFichero bF = UtilsWBFirma.ProcessCharacters(sfile);
		
		String pathFirma = bF.getRutap7s();
		String pathFichero = bF.getRutaFichero();
		boolean tieneFirma = bF.isFirmado();
		
		if (tieneFirma) {
			Pkcs7Verify verificador = new Pkcs7Verify(pathFichero, pathFirma);			
			resultFirma = verificador.verifyPkcs7();
			//Rellenamos el objeto retornado como valido.
			resultado.setValidacionFirma(resultFirma);
			resultado.setX509Certificate(verificador.getX509Certificate());	
			if(resultFirma) {
				ExtraccionYTratamientoFicheros.borradoFichero(pathFichero);
				ExtraccionYTratamientoFicheros.borradoFichero(pathFirma);
			}
		} else {
			return resultado;
		}
		
		return resultado;
	}	
	
}
