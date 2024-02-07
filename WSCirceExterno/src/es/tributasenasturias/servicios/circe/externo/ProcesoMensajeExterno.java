package es.tributasenasturias.servicios.circe.externo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.tributasenasturias.servicios.circe.externo.proceso.CirceCall;

/**
 * Servlet implementation class for Servlet: ProcesoMensajeExterno
 *
 */
 public class ProcesoMensajeExterno extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ProcesoMensajeExterno() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletInputStream in=request.getInputStream();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int c;
		while ((c=in.read())!=-1)
		{
			bout.write(c);
		}
		response.setContentType("text/xml");
		response.setCharacterEncoding(request.getCharacterEncoding());
		String ca = new String(bout.toByteArray(), request.getCharacterEncoding());
		ServletOutputStream out = response.getOutputStream();
		out.write(CirceCall.call(ca).getBytes(request.getCharacterEncoding()));
	}   	  	    
}