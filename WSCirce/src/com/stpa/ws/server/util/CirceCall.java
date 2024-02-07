package com.stpa.ws.server.util;


import static org.w3c.dom.Node.TEXT_NODE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.stpa.ws.server.bean.CirceImpuestoTrnsADJ_Solicitud;

public class CirceCall {
	private static List<String> argumentoDestino = new ArrayList<String>();
	/**
	 * Constructor.
	 */	
	public 	CirceCall() {
		super();
	}	
	/**
	 * Metodo parse String procedente de la SOLICITUD de CIRCE.
	 * @param lcirce_solicitud objeto solicitud procedente de CIRCE
	 * @param xml_solicitud Objeto String con el XML de la solicitud.
	 * @return Objeto del tipo CirceImpuestoTrnsADJ_Solicitud con la SOLICITUD construida.
	 */
	public static CirceImpuestoTrnsADJ_Solicitud createObjetCIRCE (CirceImpuestoTrnsADJ_Solicitud lcirce_solicitud, String xml_solicitud) {
		com.stpa.ws.server.util.Logger.info("PASO 1 -- CIRCE CirceCall.createObjetCIRCE().", Logger.LOGTYPE.APPLOG);
		//Llamamos al costructor para parsear el XML de entrada.
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    builderFactory.setIgnoringElementContentWhitespace(false);

	    DocumentBuilder builder = null;
	    try {
	      builder = builderFactory.newDocumentBuilder();  // Create the parser
	    } catch(ParserConfigurationException e) {
	    	com.stpa.ws.server.util.Logger.error(e.getMessage(), e,Logger.LOGTYPE.APPLOG);
	    }
	    Document xmlDoc = null;

	    try {      
	        xmlDoc = builder.parse(new InputSource(new StringReader(xml_solicitud)));
	        listNodes(xmlDoc.getDocumentElement(), lcirce_solicitud);
	    } catch(SAXException e) {
	    	com.stpa.ws.server.util.Logger.error(e.getMessage(), e,Logger.LOGTYPE.APPLOG);
	    } catch(IOException e) {
	    	com.stpa.ws.server.util.Logger.error(e.getMessage(), e,Logger.LOGTYPE.APPLOG);
	    } catch(Exception e) {
	    	com.stpa.ws.server.util.Logger.error(e.getMessage(), e,Logger.LOGTYPE.APPLOG);
	    }
	     
		
		//Retornamos el objeto CIRCE de solicitud hacia el servicio Web.
	    com.stpa.ws.server.util.Logger.info("PASO 2 -- CIRCE Fin CirceCall.createObjetCIRCE().", Logger.LOGTYPE.APPLOG);
		return lcirce_solicitud;
	}
	/**
	 * Metodo que recorre los nodos del XML SOLICITUD a parsear.
	 * @param node NODO entrante.
	 * @param lcirce_solicitud objeto SOLICITUD.
	 * @throws Exception error controlado del sistema.
	 */
	private static void listNodes(Node node, CirceImpuestoTrnsADJ_Solicitud lcirce_solicitud) throws Exception {    
	    short type = node.getNodeType();    
	    if(type == TEXT_NODE){
	      Node nodeparent =  node.getParentNode();
	      Node nodegrandparent =  nodeparent.getParentNode();
	      Node nodegrandgrandparent =  nodegrandparent.getParentNode(); 
	      //CRUBENCVS. En este código había un caso en que podía fallar.
	      //Un nodo texto puede o bien existir porque hay datos que queremos tratar,
	      //o por formateo del XML. Este formateo puede introducir espacios en blanco
	      //para hacer más legible el texto. En el primer caso tendríamos
	      // <xml><nodo1><dato>Dato</dato></nodo1></xml>
	      // en que nodeparent=dato, nodegrandparent=nodo1 y nodegrandgrandparent=xml
	      //Pero si se introducen espacios en blanco por formatear, 
	      //tenemos 
	      // <xml>
	      //   <nodo1>
	      //     <dato>Dato</dato>
	      //   </nodo1>
	      // </xml>
	      // Como puede verse, antes de "nodo1" hay espacios en blanco para hacer que el contenido se lea mejor
	      //, pero estos espacios en blanco son texto también, con lo que en este caso tenemos
	      // nodeparent=xml nodegrandparent=Nodo raíz documento (no se ve, pero por definición existe) nodegrandgrandparent=null
	      // y en cuanto intentemos acceder a una propiedad del nodo nodegrandgrandparent, fallará.
	      // Como en este xml sólo debemos hacer caso a los nodos que tengan
	      // nodeparent, nodegrandparent y nodegrandgrandparent, sólo trataremos los nodos que 
	      // tengan estas propiedades, y no a los demás.
	      if (nodeparent!=null && nodegrandparent!=null && nodegrandgrandparent!=null)
	      {
		      if ("Identificacion".equalsIgnoreCase(nodegrandparent.getNodeName())) {
		          if ("Tipo_Sociedad".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setTipo_Sociedad(((Text)node).getWholeText());
		          }
		          if ("Id_comunicacion".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setId_comunicacion(((Text)node).getWholeText());	        	  
		          }
		          if ("Id_tramite".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setId_tramite(((Text)node).getWholeText());	              
		          }
		          if ("Descripcion".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setDescripcion(((Text)node).getWholeText());
		          }
		          if ("Fecha".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setFecha(((Text)node).getWholeText());	        	  
		          }
		          if ("Hora".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setHora(((Text)node).getWholeText());
		          }
		          if ("Acuse".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setAcuse(((Text)node).getWholeText());
		          }
		          if ("Sincronia".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setSincronia(((Text)node).getWholeText());
		          }
		          if ("Sistema_emisor".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setSistema_emisor(((Text)node).getWholeText());
		          }
		          if ("Sistema_receptor".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	  lcirce_solicitud.getXml().getFichero().getIdentificacion().setSistema_receptor(((Text)node).getWholeText());
		          }
		      }        
		      if ("Escritura".equalsIgnoreCase(nodeparent.getParentNode().getNodeName())) {
		        if ("Origen".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setOrigen(((Text)node).getWholeText());
		          }
		        if ("Formato".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setFormato(((Text)node).getWholeText());
		          }
		        if ("Nombre".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setNombre(((Text)node).getWholeText());
		          }
		        if ("Tamanyo".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setTamanyo(((Text)node).getWholeText());  
		          }
		        if ("Fecha".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setFecha(((Text)node).getWholeText());  
		         }
		        if ("Hora".equalsIgnoreCase(nodeparent.getNodeName())) {
		        	lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setHora(((Text)node).getWholeText());  
		        }
		      }      
		      if ("Destinos".equalsIgnoreCase(nodegrandparent.getNodeName()) && "Destino".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  argumentoDestino.add(((Text)node).getWholeText());	    	  
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().setDestinos(argumentoDestino);
		      }
		      if ("Contenido".equalsIgnoreCase(nodegrandparent.getNodeName()) && "Documento".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getContenido().setDocumento(((Text)node).getWholeText());
		      }
		      if ("Contenido".equalsIgnoreCase(nodegrandparent.getNodeName()) && "Firma".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_interlocutores().getEscritura().getContenido().setFirma(((Text)node).getWholeText());
		      }
		      if ("CABECERA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "APLICACION".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().setAplicacion(((Text)node).getWholeText());
		      }
		      if ("CABECERA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "ID_PAIT".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCabecera().setId_pait(((Text)node).getWholeText());
		      }
		      if ("ID_NOTARIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NIF_NOTARIO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().setNif_notario(((Text)node).getWholeText());
		      }
		      if ("ID_NOTARIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO_NOTARIO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().setCodigo_notario(((Text)node).getWholeText());
		      }
		      if ("ID_NOTARIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().setNombre(((Text)node).getWholeText());
		      }
		      if ("ID_NOTARIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "APELLIDOS".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().setApellidos(((Text)node).getWholeText());
		      }
		      if ("ID_NOTARIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE_COMPLETO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().setNombre_completo(((Text)node).getWholeText());
		      }
		      if ("ID_NOTARIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "LOCALIDAD".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getId_notario().setLocalidad(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "MODALIDAD".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setModalidad(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "ANEXO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setAnexo(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FECHA_PRESENTACION".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setFecha_presentacion(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FECHA_DEVENGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setFecha_devengo(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "EXPRESION_ABREVIADA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setExpresion_abreviada(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CONCEPTO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setConcepto(((Text)node).getWholeText());
		      }
		      if ("HECHO_IMPONIBLE".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUM_ITPAJD".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getHecho_imponible().setNum_itpajd(((Text)node).getWholeText());
		      }
		      if ("DATOS_NOTARIALES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TIPO_DOCUMENTO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setTipo_documento(((Text)node).getWholeText());
		      }
		      if ("DATOS_NOTARIALES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO_PROTOCOLO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setNumero_protocolo(((Text)node).getWholeText());
		      }
		      if ("DATOS_NOTARIALES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "ANYO_PROTOCOLO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setAnyo_protocolo(((Text)node).getWholeText());
		      }
		      if ("DATOS_NOTARIALES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "OBSERVACIONES".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_notariales().setObservaciones(((Text)node).getWholeText());
		      }

		      if ("INTERVINIENTES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUM_PAS".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().setNum_pas(((Text)node).getWholeText());
		      }
		      //Sujeto Pasivo
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PRIMER_APELLIDO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setPrimer_apellido(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "SEGUNDO_APELLIDO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setSegundo_apellido(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setNombre(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE_COMPLETO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setNombre_completo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NIF".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setNif(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "E-MAIL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setEmail(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TELEFONO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setTelefono(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FAX".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setFax(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TIPO_VIA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setTipo_via(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE_VIA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setNombre_via(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setNumero(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO_LOCAL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setNumero_local(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "ESCALERA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setEscalera(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PLANTA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setPlanta(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PUERTA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setPuerta(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "BLOQUE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setBloque(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "DUPLICADO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setDuplicado(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PORTAL_BLOQUE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setPortal_bloque(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PUERTA_GENERICA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setPuerta_generica(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PUERTA_ESPECIFICA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setPuerta_especifica(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "KILOMETRO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setKilometro(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO_POSTAL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().setCodigo_postal(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "MUNICIPIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().setCodigo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "MUNICIPIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getMunicipio().setNombre(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PROVINCIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().setCodigo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PROVINCIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getProvincia().setNombre(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PAIS".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPais().setCodigo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PASIVO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PAIS".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_pasivo().getPais().setNombre(((Text)node).getWholeText());
		      }
		      //Sujeto Presentador
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PRIMER_APELLIDO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setPrimer_apellido(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "SEGUNDO_APELLIDO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setSegundo_apellido(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setNombre(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE_COMPLETO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setNombre_completo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NIF".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setNif(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "E-MAIL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setEmail(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TELEFONO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setTelefono(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FAX".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setFax(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TIPO_VIA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setTipo_via(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE_VIA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setNombre_via(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setNumero(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO_LOCAL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setNumero_local(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "ESCALERA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setEscalera(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PLANTA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setPlanta(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PUERTA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setPuerta(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "BLOQUE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setBloque(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "DUPLICADO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setDuplicado(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PORTAL_BLOQUE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setPortal_bloque(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PUERTA_GENERICA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setPuerta_generica(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PUERTA_ESPECIFICA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setPuerta_especifica(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "KILOMETRO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setKilometro(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO_POSTAL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().setCodigo_postal(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "MUNICIPIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().setCodigo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "MUNICIPIO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getMunicipio().setNombre(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PROVINCIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().setCodigo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PROVINCIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getProvincia().setNombre(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PAIS".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CODIGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPais().setCodigo(((Text)node).getWholeText());
		      }
		      if ("SUJETO_PRESENTADOR".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "PAIS".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getIntervinientes().getSujeto_presentador().getPais().setNombre(((Text)node).getWholeText());
		      }
		      if ("DATOS_LIQUIDACION".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "VALORES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "VALOR_DECLARADO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getValores().setValor_declarado(((Text)node).getWholeText());
		      }
		      if ("EXENCIONES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TIPO_EXENCION".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().setTipo_exencion(((Text)node).getWholeText());
		      }
		      if ("EXENCIONES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "EXENTO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().setExento(((Text)node).getWholeText());
		      }
		      if ("EXENCIONES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NO_SUJETO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().setNo_sujeto(((Text)node).getWholeText());
		      }
		      if ("EXENCIONES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FUNDAMENTO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().setFundamento(((Text)node).getWholeText());
		      }
		      if ("EXENCIONES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CLAVE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getExenciones().setClave(((Text)node).getWholeText());
		      }
		      if ("COMPLEMENTARIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "COMPLEMENTARIA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().setComplementaria(((Text)node).getWholeText());
		      }
		      if ("COMPLEMENTARIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO_PRESENTACION".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().setNumero_presentacion(((Text)node).getWholeText());
		      }
		      if ("COMPLEMENTARIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FECHA_PRESENTACION".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().setFecha_presentacion(((Text)node).getWholeText());
		      }
		      if ("COMPLEMENTARIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NUMERO_JUSTIFICANTE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().setNumero_justificante(((Text)node).getWholeText());
		      }
		      if ("COMPLEMENTARIA".equalsIgnoreCase(nodegrandparent.getNodeName()) && "IMPORTE_INGRESADO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getDatos_liquidacion().getComplementaria().setImporte_ingresado(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandgrandparent.getNodeName()) && "VALORES".equalsIgnoreCase(nodegrandparent.getNodeName()) && "VALOR_DECLARADO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getValores().setValor_declarado(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "BASE_IMPONIBLE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setBase_imponible(((Text)node).getWholeText());
		      }
		      if ("REDUCCION".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PORCENTAJE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getReduccion().setPorcentaje(((Text)node).getWholeText());
		      }
		      if ("REDUCCION".equalsIgnoreCase(nodegrandparent.getNodeName()) && "IMPORTE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getReduccion().setImporte(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "BASE_LIQUIDABLE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setBase_liquidable(((Text)node).getWholeText());
		      }
		      if ("TIPO_IMPOSITIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PORCENTAJE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().setPorcentaje(((Text)node).getWholeText());
		      }
		      if ("TIPO_IMPOSITIVO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "IMPORTE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getTipo_impositivo().setImporte(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CUOTA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setCuota(((Text)node).getWholeText());
		      }
		      if ("BONIFICACION".equalsIgnoreCase(nodegrandparent.getNodeName()) && "PORCENTAJE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBonificacion().setPorcentaje(((Text)node).getWholeText());
		      }
		      if ("BONIFICACION".equalsIgnoreCase(nodegrandparent.getNodeName()) && "IMPORTE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().getBonificacion().setImporte(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "IMPORTE_INGRESAR".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setImporte_ingresar(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "RECARGO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setRecargo(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "INTERESES".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setIntereses(((Text)node).getWholeText());
		      }
		      if ("CALCULO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TOTAL_INGRESAR".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getCalculo().setTotal_ingresar(((Text)node).getWholeText());
		      }
		      if ("PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "TIPO_CONFIRMACION".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().setTipo_confirmacion(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "ENTIDAD".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setEntidad(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "OFICINA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setOficina(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CONTROL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setControl(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "CUENTA".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setCuenta(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NIF_TITULAR".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setNif_titular(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "NOMBRE_COMPLETO_TITULAR".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setNombre_completo_titular(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "BANCO_SUCURSAL".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setBanco_sucursal(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "FECHA_INGRESO".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setFecha_ingreso(((Text)node).getWholeText());
		      }
		      if ("DATOS_PAGO".equalsIgnoreCase(nodegrandparent.getNodeName()) && "INGRESO_IMPORTE".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFichero().getElementos().getDatos_STTCirce().getPago().getDatos_pago().setIngreso_importe(((Text)node).getWholeText());
		      }
		      //Firma Fichero
		      if ("Firma_fichero".equalsIgnoreCase(nodegrandparent.getNodeName()) && "Firma".equalsIgnoreCase(nodeparent.getNodeName())) {
		    	  lcirce_solicitud.getXml().getFirma_fichero().setFirma(((Text)node).getWholeText());
		      }
	      }
	    }
	    if (node!=null)
	    {
		    NodeList list = node.getChildNodes();
		    if(list.getLength() > 0) {
		      for(int i = 0 ; i<list.getLength() ; i++) {
		        listNodes(list.item(i), lcirce_solicitud);
		      }
		    }
	    }
	  }
	/**
	 * Metodo de lectura desde disco del fichero definido como parametro Filename
	 * @param Filename Ruta del fichero con nombre incluido a retornar.
	 * @return byte array del fichero leido.
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
			com.stpa.ws.server.util.Logger.error("CirceCall,readFile():error:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
		return bytes;
	}
}
