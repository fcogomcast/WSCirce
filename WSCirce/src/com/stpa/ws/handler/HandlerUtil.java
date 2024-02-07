package com.stpa.ws.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;

import com.stpa.ws.server.util.Logger;

public class HandlerUtil {

	@SuppressWarnings("unchecked")
	public static void setHandlerClient(javax.xml.ws.BindingProvider bpr){
		try {
			Binding bi = bpr.getBinding();
		
			List<Handler> handlerList = bi.getHandlerChain();
			if(handlerList == null)
				handlerList = new ArrayList<Handler>();
			
			com.stpa.ws.handler.ClientHandler ch = new com.stpa.ws.handler.ClientHandler();							
			ch.setlogFile(Logger.LOGTYPE.CLIENTLOG);			
			handlerList.add(ch);
			bi.setHandlerChain(handlerList);			
			
		} catch(Exception e) {
			Logger.debug("Escribo en el log: " + getStackTrace(e), Logger.LOGTYPE.CLIENTLOG);
		}
	}
	
	public static String getStackTrace(Throwable t) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		t.printStackTrace(printWriter);
		return result.toString();
	}
		
}
