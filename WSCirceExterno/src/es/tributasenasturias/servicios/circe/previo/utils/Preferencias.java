package es.tributasenasturias.servicios.circe.previo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;



public class Preferencias {
	
	private Preferences m_preferencias;
	private String m_endPointCirce;
	private String m_debug;
    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsCirceExterno.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos/WSCirce";
    
    private final String endPointCirce = "EndPointCirce";
    private final String debug = "debug";
        
    private final String VALOR_INICIAL_PREF_endPointCirce = "http://bus:7101/WSCIRCE/ProxyServices/PXCIRCE";
    private final String VALOR_INICIAL_PREF_debug = "0";
    
    public Preferencias(){
    	CompruebaFicheroPreferencias();
		CargarPreferencias();
	}

	public void CompruebaFicheroPreferencias()
    {
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
        m_preferencias.put(endPointCirce, VALOR_INICIAL_PREF_endPointCirce);
        m_preferencias.put(debug, VALOR_INICIAL_PREF_debug);
               
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
        	Logger.error(e.getMessage(),e,Logger.LOGTYPE.APPLOG);
        }finally{
            try{
                if(outputStream != null)
                    outputStream.close();
            }catch(Exception e){
            	Logger.error("Error cerrando fichero de preferencias -> " + e.getMessage(),e,Logger.LOGTYPE.APPLOG);
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

            m_endPointCirce = m_preferencias.get(endPointCirce, "");
            m_debug = m_preferencias.get(debug, "");
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



	public String getEndpointCirce() {
		return m_endPointCirce;
	}

	public String getM_debug() {
		return m_debug;
	}
	public void setM_preferencias(Preferences m_preferencias) {
		this.m_preferencias = m_preferencias;
	}
}
