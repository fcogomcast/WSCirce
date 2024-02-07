package com.stpa.ws.server.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PropertiesUtils;


public class AutenticacionPAHelper {
	Preferencias pref = new Preferencias();
	/**
	 * Construye la petición en función del esqueleto XML que tendremos en fichero.
	 * @return String que representa el XML de la petición a enviar al servicio de autenticación.
	 */
	private String construyePeticion(String certificado)
	{
		String xml=null;
		try
		{
			pref.CargarPreferencias();					
			
			String nombreFichero = pref.getM_xmlAutorizacion();
						
			if (nombreFichero!=null && !nombreFichero.equals(""))
			{
				File fXml = new File (nombreFichero);
				Document doc =null; 
				doc = XMLDOMUtils.parseXML(doc,fXml);
				NodeList nodos = XMLDOMUtils.getAllNodes(doc,"certificate");
				Node nodoCer=null;
				if (nodos.getLength()!=0)
				{
					nodoCer = nodos.item(0);
					XMLDOMUtils.setNodeText(doc,nodoCer, certificado);
				}				
				//Ahora se sustituye el valor de la IP.
				nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'IP')]"); 
				//Dentro de los objetos "item", queremos aquel que tenga un hijo "Name" con valor "IP".
				if (nodos.getLength()!=0)
				{
					Node nodo=nodos.item(0); //Sólo esperamos un "item" que contenga un "name" "IP".
					Node nodoIpValor = XMLDOMUtils.getFirstChildNode(nodo, "value");
					XMLDOMUtils.setNodeText(doc,nodoIpValor, pref.getM_ipAutorizacion());
					
				}
				xml = XMLDOMUtils.getXMLText(doc);
				
			}			
		} catch (Exception ex) {
			com.stpa.ws.server.util.Logger.error("AutenticacionPAHelper:construyePeticion:Error de XML:" + ex.getMessage(), ex,Logger.LOGTYPE.APPLOG);
			xml=null;
		}
		
		return xml;
	}
	
	public String login (String certificado) throws StpawsException {
		InputStreamReader in = null;
		OutputStream out=null;
        BufferedReader buf = null;
		try
		{									 					
			String peti = construyePeticion(certificado);																
			pref.CargarPreferencias();							
			String dirServicio = pref.getM_endPointAutenticacionPA();				
			
			com.stpa.ws.server.util.Logger.debug("LOGIN::Certificado:" +certificado ,Logger.LOGTYPE.CLIENTLOG);
			com.stpa.ws.server.util.Logger.debug("endopint autentica:" +dirServicio ,Logger.LOGTYPE.CLIENTLOG);
			com.stpa.ws.server.util.Logger.debug("Peticion construida para certificado:" +peti ,Logger.LOGTYPE.CLIENTLOG);
			
			if (dirServicio!=null && !dirServicio.equals(""))
			{
					URL url= new URL(dirServicio);
					HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
					//se trae el contenido del fichero a pasar como petición.
			        //Se recuperan los bytes que conforman la petición.
					byte []petiB = peti.getBytes("UTF-8");
					//Se monta la conexión
			        con.setRequestProperty( "Content-Length", String.valueOf( petiB.length ) );
			        con.setRequestProperty("Content-Type","text/xml; charset=utf-8");
			        con.setRequestMethod( "POST" );
			        con.setDoOutput(true); //Se usa la conexion para salida
			        con.setDoInput(true); // Se usa para la entrada.
			        //Se envían los datos a la URL remota.
			        out = con.getOutputStream();
			        out.write(petiB); //Enviamos la peticion.
			        out.close(); 
			        //Leemos lo que nos ha devuelto.
			        in = new InputStreamReader(con.getInputStream());
			        buf = new BufferedReader (in);
			        StringBuilder xmlResp = new StringBuilder();
			        String linea;
			        while ((linea=buf.readLine())!=null)
			        {
			        	xmlResp.append(linea);
			        }
			        
			        buf.close();
			        //Recuperamos el CIF o NIF.
			        String id=getIdentificadorRespuesta(xmlResp.toString());
			        return id;
			}
		} catch (MalformedURLException ex) {			
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.validacion") + "AutenticacionPAHelper:Error al verificar la autenticación.Exception de servicio." + ex.getMessage(), ex, Logger.LOGTYPE.APPLOG);
			throw new StpawsException (PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), ex);
		} catch (IOException ex) {			
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.validacion") + "AutenticacionPAHelper:Error al verificar la autenticación.Exception de servicio." + ex.getMessage(), ex, Logger.LOGTYPE.APPLOG);
			throw new StpawsException (PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), ex);	
		} catch (Exception ex) {			
			com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.validacion") + "AutenticacionPAHelper:Error al verificar la autenticación.Exception de servicio." + ex.getMessage(), ex, Logger.LOGTYPE.APPLOG);
			throw new StpawsException (PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), ex);	
		}
		finally
		{
			if  (buf!=null)
			{
				try
				{
					buf.close();
				}
				catch (IOException ex)
				{
					
				}
			}
			if (in!=null)
			{
				try
				{
					in.close();
				}
				catch (IOException ex)
				{
					
				}
			}
			if (out!=null)
			{
				try
				{
					out.close();
				}
				catch (IOException ex)
				{
					
				}
			}
		}
		return null;
	}
	/**
	 * Obtiene la respuesta del identificador.
	 * @param xml XML de entrada con la respuesta.
	 * @return
	 * @throws StpawsException
	 */
	private String getIdentificadorRespuesta (String xml) throws StpawsException 
	{
		Document doc =null; 
		NodeList nodos=null;
		Node nodoId=null;
		String identificador =null;
		doc = XMLDOMUtils.parseXml (doc,xml);
		//Recuperamos el nodo "item" que contiene el identificador. Probamos primero con CIF.
		nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'CIF')]");
		//Si no existe, probamos con NIF
		if (nodos.getLength()!=0) 
		{
			Node nodo = nodos.item(0);//Sólo se espera uno.
			nodoId = XMLDOMUtils.getFirstChildNode(nodo, "value");
			identificador = XMLDOMUtils.getNodeText(nodoId);
		}
		else //NIF
		{
			nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'NIF')]");
			if (nodos.getLength()!=0)
			{
				Node nodo = nodos.item(0);
				nodoId = XMLDOMUtils.getFirstChildNode(nodo, "value");
				identificador = XMLDOMUtils.getNodeText(nodoId);
			}
			else
			{
				identificador=null;
			}
		}
		return identificador;
		
	}
}
