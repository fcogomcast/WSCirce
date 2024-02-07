package com.stpa.ws.server.exception;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import com.stpa.ws.server.util.Logger;

public class SimpleErrorHandler implements ErrorHandler {
	
	private static final long serialVersionUID = 1L;
	
    public void warning(SAXParseException e) {
    	com.stpa.ws.server.util.Logger.info(e.getMessage(),Logger.LOGTYPE.APPLOG);
    }

    public void error(SAXParseException e) {
    	com.stpa.ws.server.util.Logger.error(e.getMessage(),e,Logger.LOGTYPE.APPLOG);
    }

    public void fatalError(SAXParseException e) {
    	com.stpa.ws.server.util.Logger.error(e.getMessage(),e,Logger.LOGTYPE.APPLOG);
    }
}
