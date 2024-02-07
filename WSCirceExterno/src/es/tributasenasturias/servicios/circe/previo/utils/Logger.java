package es.tributasenasturias.servicios.circe.previo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public final class Logger 
{
	private final static String LOG_FILE = "WSCircePrevio.log";
	private final static String LOG_DIR = "proyectos/WSCirce/logs";
	public enum LEVEL {TRACE, DEBUG, INFO, WARNING, ERROR}
	public enum LOGTYPE {APPLOG}
	// M�todo sincronizado. 
	/**
	 * M�todo que realiza log.
	 * 
	 */
	public final synchronized static void doLog(String message, LEVEL level, LOGTYPE logType)
	{
		File file;
        FileWriter fichero = null;
        PrintWriter pw;

        try{
            Date today = new Date();
	        String completeMsg = "WSCircePrevio :: " + today + " :: " + level + " :: " + message;
	            
	        file = new File(LOG_DIR);
	        if(file.exists() == false)
	        	file.mkdirs();
	            
	        fichero = new FileWriter(getLogFile(logType), true);//true para que agregemos al final del fichero
	        
	        pw = new PrintWriter(fichero);
	        pw.println(completeMsg);
        }catch (IOException e){
            System.out.println("Error escribiendo log '"+message+"' -> "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(fichero != null){
                try{
                    fichero.close();
                }catch(Exception e){
                    System.out.println("Error cerrando fichero de log -> "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
	}
	
	public final static OutputStream getOutput(LOGTYPE logType){
		File file;
		OutputStream os = null;
		try{
			file = new File(LOG_DIR);
	        if(file.exists() == false)
	        	file.mkdirs();
	        os = new FileOutputStream(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return os;
	}
	
	private final static String getLogFile(LOGTYPE logType){
		String result = LOG_DIR + "//";
		switch(logType){
			case APPLOG:
				result += LOG_FILE;
				break;
			default:
				result += LOG_FILE;
				break;
		}
		return result;
	}
	
	public final static void trace(String message, LOGTYPE logType){
		Preferencias pref = new Preferencias();
		if(pref.getM_debug()!=null && pref.getM_debug().equals("1"))
			doLog(message,LEVEL.TRACE,logType);
	}
	
	public final static void trace(StackTraceElement[] stackTraceElements, LOGTYPE logType){
		if (stackTraceElements == null)
			return;
		String result = "";
        for (int i = 0; i < stackTraceElements.length; i++){
        	result+="\n" + stackTraceElements[i].toString();
	    }
        doLog(result,LEVEL.TRACE,logType);
	}
	
	public final static void debug(String message, LOGTYPE logType){
		Preferencias pref = new Preferencias();
		if(pref.getM_debug()!=null && pref.getM_debug().equals("1"))
			doLog(message,LEVEL.DEBUG,logType);
	}
	
	public final static void info(String message, LOGTYPE logType){
		Preferencias pref = new Preferencias();
		if(pref.getM_debug()!=null && pref.getM_debug().equals("1"))
			doLog(message,LEVEL.INFO,logType);
	}
	
	public final static void warning(String message, LOGTYPE logType){
		Preferencias pref = new Preferencias();
		if(pref.getM_debug()!=null && pref.getM_debug().equals("1"))
			doLog(message,LEVEL.WARNING,logType);
	}
	
	public final static void error(String message, LOGTYPE logType){
		doLog(message,LEVEL.ERROR,logType);
	}
	
	public final static void error(String message, Throwable t, LOGTYPE logType){
		doLog(message,LEVEL.ERROR,logType);
		if(t!=null)
			trace(t.getStackTrace(),logType);
	}
}