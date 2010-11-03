package net.wrap_trap.bitro.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectForwarder extends Forwarder{
	private String redirectTo;
	
	public RedirectForwarder(String redirectTo){
		this.redirectTo = redirectTo;
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	    if(redirectTo.toLowerCase().indexOf("http") == 0){
	    	response.sendRedirect(redirectTo);
	    	return;
	    }
		response.sendRedirect(getRedirectUrl(request));
		return;
	}
	
	public String getRedirectUrl(HttpServletRequest request){
	    String scheme = request.getScheme().toLowerCase();
	    String portStr = "";
	    int port = request.getServerPort();
	    if( !(scheme.equals("http") && (port == 80)) &&
	    	!(scheme.equals("https") && (port == 443))){
	    	portStr = ":" + port;
	    }
	    String url = 	scheme + "://" + 
						request.getServerName() + portStr + 
						request.getContextPath();
	    
	    if(url.endsWith("/") && redirectTo.startsWith("/")){
	    	url = url.substring(0, url.length() - 1);
	    }
	    	
		return url + redirectTo;
	}
}
