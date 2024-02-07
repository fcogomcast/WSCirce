package com.stpa.ws.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class SoapHandler implements SOAPHandler<SOAPMessageContext>{
		
	private com.stpa.ws.server.util.Logger.LOGTYPE logFile = com.stpa.ws.server.util.Logger.LOGTYPE.SERVERLOG;	

	public void setlogFile(com.stpa.ws.server.util.Logger.LOGTYPE logFile){
		this.logFile = logFile;
	}
	
	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() { 
	     return Collections.emptySet(); 
	} 

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}
	
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		com.stpa.ws.server.util.Logger.debug("Escribo en el log: " + logFile, com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty.booleanValue()){
			com.stpa.ws.server.util.Logger.debug("OUTBOUND SOAP MESSAGE: ", logFile);
		}else{
			com.stpa.ws.server.util.Logger.debug("INBOUND SOAP MESSAGE: ", logFile);
	    }
		try{
			SOAPMessage message = context.getMessage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			message.writeTo(baos);
			com.stpa.ws.server.util.Logger.debug(baos.toString(), logFile);	
		}catch(IOException ioe){
			com.stpa.ws.server.util.Logger.error("Error escribiendo soapmessage: ",ioe, logFile);
		}catch(SOAPException se){
			com.stpa.ws.server.util.Logger.error("Error escribiendo soapmessage: ",se, logFile);
		}catch(Throwable t){
			com.stpa.ws.server.util.Logger.error("Error escribiendo soapmessage: ",t, logFile);
		}
	    
	    return true;
	}
}