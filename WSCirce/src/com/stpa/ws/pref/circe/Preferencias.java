package com.stpa.ws.pref.circe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

import com.stpa.ws.server.constantes.CirceImpuestoTrnsADJConstantes;
import com.stpa.ws.server.util.Logger;

public class Preferencias {
	
	private Preferences m_preferencias;
	private String m_wslanzadormasivowsdlurl;
	private String m_wslanzadorwsdlurl;
	private String m_wslanzadorservicenamespace;
	private String m_wslanzadorservicename;
	private String m_wslanzadormasivoservicename;
	private String m_wslanzadorentornoBDD;	
	private String m_debug;
	//PDF Modelo
	private String m_pdfmodelo;	
	private String m_apppath;
	private String m_pdfplantillauri;
	private String m_pathJustificatePresentacion;
	//comprobar firma
	private String m_endPointFirma;
	private String m_endPointAutenticacionPA;
	private String m_certificadoFirma;
	private String m_claveFirma;
	private String m_xmlAutorizacion;
	private String m_ipAutorizacion;
	private String m_firmaDigital;
	private String m_validaFirma;
	// Path ficheros Solicitudes y respuestas
	private String m_pathSolresp;
	private String m_integrarTributas;
	
    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsCirce.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos/WSCirce";
    private final String DIRECTORIO_solicitudes_respuestas = "solicresp";
    private final String DIRECTORIO_path_solicitudes_respuestas = DIRECTORIO_PREFERENCIAS + "//" + DIRECTORIO_solicitudes_respuestas + "//";
    
    private final String wslanzadormasivowsdlurl = "wslanzadormasivo.wsdl.url";
    private final String wslanzadorwsdlurl = "wslanzador.wsdl.url";
    private final String wslanzadorservicenamespace = "wslanzador.service.namespace";
    private final String wslanzadorservicename = "wslanzador.service.name";
    private final String wslanzadormasivoservicename = "wslanzadormasivo.service.name";
    private final String wslanzadorentornoBDD = "wslanzador.entornoBDD";    
    private final String debug = "debug"; 
    private final String pdfmodelo = "pdf.modelo";
    private final String apppath = "app.path";
    private final String pathJustificatePresentacion = "pdf.plantilla.justificantepresentacion";
    //Comprobacion servicio firmado
    private final String endPointFirma = "EndPointFirma";
    private final String endPointAutenticacionPA = "EndPointAutenticacionPA";
    private final String certificadoFirma = "CertificadoFirma";
    private final String claveFirma = "ClaveFirma";
    private final String xmlAutorizacion = "XmlAutorizacion";
    private final String ipAutorizacion = "IpAutorizacion";
    private final String firmaDigital = "FirmaDigital";
    private final String validaFirma = "ValidaFirma";
    // Path Solicitud y respuestas
    private final String pathsolicresp = "path.solicresp";
    //Indica si se integrará en Tributas. S= se integrará, N= no se integrará. Útil para pruebas.
    private final String integrarTributas="IntegrarTributas";
        
    private final String VALOR_INICIAL_PREF_wslanzadorwsdlurl = "http://bus:7101/WSInternos/ProxyServices/PXLanzador?WSDL";
    private final String VALOR_INICIAL_PREF_wslanzadormasivowsdlurl = "http://bus:7101/WSInternos/ProxyServices/PXLanzadorMasivo?WSDL";
    private final String VALOR_INICIAL_PREF_wslanzadorservicenamespace = "http://stpa/services";
    private final String VALOR_INICIAL_PREF_wslanzadorservicename = "lanzaPLService";
    private final String VALOR_INICIAL_PREF_wslanzadormasivoservicename = "LanzaPLMasivoService";
    private final String VALOR_INICIAL_PREF_wslanzadorentornoBDD = "EXPLOTACION"; 
    private final String VALOR_INICIAL_PREF_apppath = "http://bus:7101/WSCirce/";
    private final String VALOR_INICIAL_PREF_pathsolicresp = "proyectos/WSCirce/solicresp/";
  //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_debug = "0";
    private final String VALOR_INICIAL_PREF_pdfmodelo = "/recursos/impresos/pdf/";
    private final String VALOR_INICIAL_PREF_pdfJustificante = "recursos/impresos/xml/justificantePresentacionCirce.xml";
    //Comprobacion servicio Firmado
    private final String VALOR_INICIAL_PREF_endPointFirma = "http://bus:7101/WSInternos/ProxyServices/PXFirmaDigital";
    private final String VALOR_INICIAL_PREF_endPointAutenticacionPA = "http://bus:7101/WSAutenticacionPA/ProxyServices/PXAutenticacionPA";
    private final String VALOR_INICIAL_PREF_certificadoFirma = "tributasfnmtcert";
    private final String VALOR_INICIAL_PREF_claveFirma = "hola123";
    private final String VALOR_INICIAL_PREF_xmlAutorizacion = "proyectos/WSCirce/PeticionAutorizacion.xml";
    private final String VALOR_INICIAL_PREF_ipAutorizacion = "10.112.10.35";
    private final String VALOR_INICIAL_PREF_firmaDigital = "S";
    private final String VALOR_INICIAL_PREF_validaFirma = "S";    
    private final String VALOR_INICIAL_PREF_integrarTributas = CirceImpuestoTrnsADJConstantes.constantNoIntegrarTributas;
	    
    public Preferencias(){
    	CompruebaFicheroPreferencias();
		CargarPreferencias();
	}

	public void CompruebaFicheroPreferencias()
    {
        // Fichero Solicitudes y Respuestas
		File ficheroSolicresp = new File(DIRECTORIO_path_solicitudes_respuestas);
        if (ficheroSolicresp.exists() == false) {
            ficheroSolicresp.mkdirs();
        }
		// Fichero Preferencias
		File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists() == false){
            CrearFicheroPreferencias();
        }
    }
    
    /***********************************************************
     * 
     * Creamos el fichero de preferencias con los valores por 
     * defecto
     * 
     ***************************************************************/
    private void CrearFicheroPreferencias()
    {
    	//preferencias por defecto
        m_preferencias = Preferences.systemNodeForPackage(this.getClass());
        m_preferencias.put(wslanzadormasivowsdlurl, VALOR_INICIAL_PREF_wslanzadormasivowsdlurl);
        m_preferencias.put(wslanzadorwsdlurl, VALOR_INICIAL_PREF_wslanzadorwsdlurl);
        m_preferencias.put(wslanzadorservicenamespace, VALOR_INICIAL_PREF_wslanzadorservicenamespace);
        m_preferencias.put(wslanzadorservicename, VALOR_INICIAL_PREF_wslanzadorservicename);
        m_preferencias.put(wslanzadormasivoservicename, VALOR_INICIAL_PREF_wslanzadormasivoservicename);        
        m_preferencias.put(wslanzadorentornoBDD, VALOR_INICIAL_PREF_wslanzadorentornoBDD);        
        m_preferencias.put(apppath, VALOR_INICIAL_PREF_apppath);      
        m_preferencias.put(debug, VALOR_INICIAL_PREF_debug);
        //PDF Modelo
        m_preferencias.put(pdfmodelo, VALOR_INICIAL_PREF_pdfmodelo);
        m_preferencias.put(pathJustificatePresentacion, VALOR_INICIAL_PREF_pdfJustificante);
        //Comprobacion servicio Firmado
        m_preferencias.put(endPointFirma, VALOR_INICIAL_PREF_endPointFirma);
        m_preferencias.put(endPointAutenticacionPA, VALOR_INICIAL_PREF_endPointAutenticacionPA);
        m_preferencias.put(certificadoFirma, VALOR_INICIAL_PREF_certificadoFirma);
        m_preferencias.put(claveFirma, VALOR_INICIAL_PREF_claveFirma);
        m_preferencias.put(xmlAutorizacion, VALOR_INICIAL_PREF_xmlAutorizacion);
        m_preferencias.put(ipAutorizacion, VALOR_INICIAL_PREF_ipAutorizacion);
        m_preferencias.put(firmaDigital, VALOR_INICIAL_PREF_firmaDigital);
        m_preferencias.put(validaFirma, VALOR_INICIAL_PREF_validaFirma);
        m_preferencias.put(pathsolicresp, VALOR_INICIAL_PREF_pathsolicresp);
        m_preferencias.put(validaFirma, VALOR_INICIAL_PREF_integrarTributas);
               
        FileOutputStream outputStream = null;
        File fichero;
        try
        {
            fichero = new File(DIRECTORIO_PREFERENCIAS);
            if(fichero.exists() == false)
                fichero.mkdirs();
            
            outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            m_preferencias.exportNode(outputStream);
        }catch (Exception e){
        	com.stpa.ws.server.util.Logger.error(e.getMessage(),e,Logger.LOGTYPE.APPLOG);
        }finally{
            try{
                if(outputStream != null)
                    outputStream.close();
            }catch(Exception e){
            	com.stpa.ws.server.util.Logger.error("Error cerrando fichero de preferencias -> " + e.getMessage(),e,Logger.LOGTYPE.APPLOG);
            }
        }
    }
    
 // Obtencion de las preferencias que especificaran el almacen y su contraseña
    public void CargarPreferencias()
    {
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists()){
            //si existe el fichero de preferencias lo cargamos
            try{
            	FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            	Preferences.importPreferences(inputStream);
            	inputStream.close();            	
            }catch(Exception e){
            	CrearFicheroPreferencias();
                //throw new StpawsException("Debe especificar primero las preferencias en el fichero: " + f.getAbsolutePath() + " (parar el servicio)",null);
            }

            m_preferencias = Preferences.systemNodeForPackage(this.getClass());

            //obtenemos las preferencias
            m_wslanzadormasivowsdlurl = m_preferencias.get(wslanzadormasivowsdlurl, "");
            m_wslanzadorwsdlurl = m_preferencias.get(wslanzadorwsdlurl, "");
            m_wslanzadorservicenamespace = m_preferencias.get(wslanzadorservicenamespace, "");
            m_wslanzadorservicename = m_preferencias.get(wslanzadorservicename, "");
            m_wslanzadormasivoservicename = m_preferencias.get(wslanzadormasivoservicename, "");
            m_wslanzadorentornoBDD= m_preferencias.get(wslanzadorentornoBDD, "");
            m_apppath = m_preferencias.get(apppath, "");
            m_debug = m_preferencias.get(debug, "");
            //PDF modelo
            m_pdfmodelo = m_preferencias.get(pdfmodelo, "");
            m_pathJustificatePresentacion = m_preferencias.get(pathJustificatePresentacion, "");
            //Comprobacion servicio Firmado
            m_endPointFirma = m_preferencias.get(endPointFirma, "");
            m_endPointAutenticacionPA = m_preferencias.get(endPointAutenticacionPA, "");
            m_certificadoFirma = m_preferencias.get(certificadoFirma, "");
            m_claveFirma = m_preferencias.get(claveFirma, "");
            m_xmlAutorizacion = m_preferencias.get(xmlAutorizacion, "");
            m_ipAutorizacion = m_preferencias.get(ipAutorizacion, "");
            m_firmaDigital = m_preferencias.get(firmaDigital, "");
            m_validaFirma = m_preferencias.get(validaFirma, "");
            m_pathSolresp = m_preferencias.get(pathsolicresp, "");
            m_integrarTributas = m_preferencias.get(integrarTributas, "");
                                   
        }
        else
        {
            //si no existe el fichero de preferencias lo crearemos
            CrearFicheroPreferencias();
        }
    }
    
	public Preferences getM_preferencias() {
		return m_preferencias;
	}

	public String getM_wslanzadormasivowsdlurl() {
		return m_wslanzadormasivowsdlurl;
	}

	public String getM_wslanzadorwsdlurl() {
		return m_wslanzadorwsdlurl;
	}

	public String getM_wslanzadorservicenamespace() {
		return m_wslanzadorservicenamespace;
	}

	public String getM_wslanzadorservicename() {
		return m_wslanzadorservicename;
	}

	public String getM_wslanzadormasivoservicename() {
		return m_wslanzadormasivoservicename;
	}

	public String getM_wslanzadorentornoBDD() {
		return m_wslanzadorentornoBDD;
	}

	public String getM_debug() {
		return m_debug;
	}

	public String getM_pdfmodelo() {
		return m_pdfmodelo;
	}

	public String getM_apppath() {
		return m_apppath;
	}

	public String getM_pdfplantillauri() {
		return m_pdfplantillauri;
	}

	public String getM_endPointFirma() {
		return m_endPointFirma;
	}

	public String getM_endPointAutenticacionPA() {
		return m_endPointAutenticacionPA;
	}

	public String getM_certificadoFirma() {
		return m_certificadoFirma;
	}

	public String getM_claveFirma() {
		return m_claveFirma;
	}

	public String getM_xmlAutorizacion() {
		return m_xmlAutorizacion;
	}

	public String getM_ipAutorizacion() {
		return m_ipAutorizacion;
	}

	public String getM_firmaDigital() {
		return m_firmaDigital;
	}

	public String getM_validaFirma() {
		return m_validaFirma;
	}
	public String getM_integrarTributas() {
		return m_integrarTributas;
	}
	public void setM_preferencias(Preferences m_preferencias) {
		this.m_preferencias = m_preferencias;
	}

	public void setM_wslanzadormasivowsdlurl(String m_wslanzadormasivowsdlurl) {
		this.m_wslanzadormasivowsdlurl = m_wslanzadormasivowsdlurl;
	}

	public void setM_wslanzadorwsdlurl(String m_wslanzadorwsdlurl) {
		this.m_wslanzadorwsdlurl = m_wslanzadorwsdlurl;
	}

	public void setM_wslanzadorservicenamespace(String m_wslanzadorservicenamespace) {
		this.m_wslanzadorservicenamespace = m_wslanzadorservicenamespace;
	}

	public void setM_wslanzadorservicename(String m_wslanzadorservicename) {
		this.m_wslanzadorservicename = m_wslanzadorservicename;
	}

	public void setM_wslanzadormasivoservicename(
			String m_wslanzadormasivoservicename) {
		this.m_wslanzadormasivoservicename = m_wslanzadormasivoservicename;
	}

	public void setM_wslanzadorentornoBDD(String m_wslanzadorentornobdd) {
		m_wslanzadorentornoBDD = m_wslanzadorentornobdd;
	}

	public void setM_debug(String m_debug) {
		this.m_debug = m_debug;
	}

	public void setM_pdfmodelo(String m_pdfmodelo) {
		this.m_pdfmodelo = m_pdfmodelo;
	}

	public void setM_apppath(String m_apppath) {
		this.m_apppath = m_apppath;
	}

	public void setM_pdfplantillauri(String m_pdfplantillauri) {
		this.m_pdfplantillauri = m_pdfplantillauri;
	}

	public void setM_endPointFirma(String pointFirma) {
		m_endPointFirma = pointFirma;
	}

	public void setM_endPointAutenticacionPA(String pointAutenticacionPA) {
		m_endPointAutenticacionPA = pointAutenticacionPA;
	}

	public void setM_certificadoFirma(String firma) {
		m_certificadoFirma = firma;
	}

	public void setM_claveFirma(String firma) {
		m_claveFirma = firma;
	}

	public void setM_xmlAutorizacion(String autorizacion) {
		m_xmlAutorizacion = autorizacion;
	}

	public void setM_ipAutorizacion(String autorizacion) {
		m_ipAutorizacion = autorizacion;
	}

	public void setM_firmaDigital(String digital) {
		m_firmaDigital = digital;
	}

	public void setM_validaFirma(String firma) {
		m_validaFirma = firma;
	}

	public String getM_pathJustificatePresentacion() {
		return m_pathJustificatePresentacion;
	}

	public void setM_pathJustificatePresentacion(String justificatePresentacion) {
		m_pathJustificatePresentacion = justificatePresentacion;
	}

	public String getM_pathSolresp() {
		return m_pathSolresp;
	}

	public void setM_pathSolresp(String solresp) {
		m_pathSolresp = solresp;
	}
	
	public void setM_integrarTributas(String m_integrarTributas) {
		this.m_integrarTributas = m_integrarTributas;
	}
}
