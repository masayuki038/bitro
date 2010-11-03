package net.wrap_trap.bitro.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Context {

	HttpServletRequest getRequest();
	
	HttpServletResponse getResponse();
}
