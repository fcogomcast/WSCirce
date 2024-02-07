package com.stpa.ws.firma.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.util.Logger;


public class ExtraccionYTratamientoFicheros {    

    /**
     * Procesado de datos
     * @param FicheroOrigen
     * @param FicheroContenido
     * @param FicheroFirma
     */
    public static boolean procesarFichero(String FicheroOrigen, String FicheroContenido, String FicheroFirma) {

    	boolean resultadoGrabacionContenido = false;
    	boolean resultadoGrabacionFirma = false;
    	boolean resultado = false;
        
        try {
            byte[] arrayFichero = FicheroOrigen.getBytes(CirceImpuestoTrnsADJConstantes.xmlEncodig);
            String ficheroLectura = new String(arrayFichero, CirceImpuestoTrnsADJConstantes.xmlEncodig);

            //Contenido
            int posicionInicialContenido = ficheroLectura.indexOf(CirceImpuestoTrnsADJConstantes.constante_campoInicialContenido);
            int posicionFinalContenido = ficheroLectura.indexOf(CirceImpuestoTrnsADJConstantes.constante_campoFinalContenido);
            String ficheroContenido = ficheroLectura.substring(posicionInicialContenido, posicionFinalContenido+CirceImpuestoTrnsADJConstantes.constante_campoFinalContenido.length());
            resultadoGrabacionContenido = crearFicheroSalida(ficheroContenido, FicheroContenido);
                        
            //Firma
            int posicionInicialFirma = ficheroLectura.indexOf(CirceImpuestoTrnsADJConstantes.constante_campoInicialFirma);
            int posicionFinalFirma = ficheroLectura.indexOf(CirceImpuestoTrnsADJConstantes.constante_campoFinalFirma);
            String ficheroFirma = ficheroLectura.substring(posicionInicialFirma + CirceImpuestoTrnsADJConstantes.constante_campoInicialFirma.length(), posicionFinalFirma);
            resultadoGrabacionFirma = crearFicheroSalida(ficheroFirma, FicheroFirma);
             
            if(resultadoGrabacionContenido && resultadoGrabacionFirma) {
            	resultado = true;
            }

        } catch (Exception ex) {
        	com.stpa.ws.server.util.Logger.error("ExtraccionYTratamientoFicheros:procesarFichero:Exception:" + ex.getMessage(), ex,Logger.LOGTYPE.APPLOG);
        }
        
        return resultado;
    }
    /**
     * Creamos fichero salida.
     * @param contenido String contenido a guardar en fichero.
     * @param ficherodestino Fichero destino.
     * @return Boolean indicando si se ha guardado correctamente el fichero.
     */
    public static boolean crearFicheroSalida(String contenido, String ficherodestino) {
        boolean resultadoGrabacion = false;
        String ficheroDestino = ficherodestino;

        try {
            File destinationFile = new File(ficheroDestino);
            if (!destinationFile.exists()) {
                destinationFile.createNewFile();
            }

            OutputStream fout = new FileOutputStream(ficheroDestino);
            OutputStream bout= new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout, CirceImpuestoTrnsADJConstantes.xmlEncodig);
            out.write(contenido);
            out.flush();  
            out.close();
            
            resultadoGrabacion = true;

        } catch (Exception e) {
        	com.stpa.ws.server.util.Logger.error("ExtraccionYTratamientoFicheros:crearFicheroSalida:Exception:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
        }

        return resultadoGrabacion;
    }
    /**
     * Get String from File
     * @param pathFile Ruta del fuchero.
     * @return String del contenido del fichero.
     * @throws IOException error en el tratamiento de datos.
     */
    public static String getStringFromFile(String pathFile) throws IOException {
        File file = new File(pathFile);
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return new String(bytes, CirceImpuestoTrnsADJConstantes.xmlEncodig);
    }
    /**
	 * Lectura del fichero y su paso a byte array.
	 * @param Filename Ruta del fichero a leer.
	 * @return Byte array del fichero leido.
	 */
	public static byte[] readFile(String Filename){
		byte[] bytes=null;
		try{
			// Returns the contents of the file in a byte array.
			File file= new File(Filename);
			file = file.getCanonicalFile();
			InputStream is = new FileInputStream(file);
			
			long length = file.length();
			bytes = new byte[(int)length];
 
			// Leemos bytes.
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
 
			// Hemos leido todo el fichero?.
			if (offset < bytes.length) {
				throw new IOException("No se ha podido leer por completo "+file.getName());
			}
			is.close();
		}
		catch(Exception e){
			com.stpa.ws.server.util.Logger.error("Pkcs7Verify,readFile:error:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
		return bytes;
	}    
    /**
     * Borrado de un fichero segun ruta facilitada.
     * @param pathFile Ruta fichero a borrar.
     * @return Boolean indicando si se ha realizado el borrado.
     */
    public static boolean borradoFichero(String pathFile) {
    	boolean resultado = false;
    	
    	try {
    		File ficheroBorrar = new File(pathFile);
    		if (ficheroBorrar.delete()) {
    			resultado = true;
    		} else {
    			com.stpa.ws.server.util.Logger.error("ExtraccionYTratamientoFicheros:borradoFichero:Error borrado fichero:" + pathFile,Logger.LOGTYPE.APPLOG);
    		}
    	} catch(Exception e) {
    		com.stpa.ws.server.util.Logger.error("ExtraccionYTratamientoFicheros:borradoFichero:Exception:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
    	}
    	
    	return resultado;    	
    }
}