package net.wrap_trap.bitro.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspForwarder extends Forwarder{

	private String JSP_ROOT = "/WEB-INF";
	
	private String jspPath;
	
	public JspForwarder(String jspPath){
		this.jspPath = jspPath;
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String filePath = createJspFilePath();
		request.getRequestDispatcher(filePath).forward(request, response);
	}
	
	protected String createJspFilePath(){
		if(jspPath.charAt(0) != '/'){
			return JSP_ROOT + '/' + jspPath;
		}
		return JSP_ROOT + jspPath;
	}
}
