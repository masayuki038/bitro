package net.wrap_trap.bitro.test;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ServletTestEnvironment extends TestEnvironment {
	private Map<String, String> requestParams;
	
	public ServletTestEnvironment(){
		requestParams = new HashMap<String, String>();
	}
	
	@Around("call(* javax.servlet.http.HttpServletRequest.getParameter(..)) && args( key ) && cflow( execution(* org.vwiki.control.*.test*(..)) )")
	public String getParameter(String key){
		return requestParams.get(key);
	}
	
	@Around("call(* javax.servlet.http.HttpServletRequest.getParameterMap()) && cflow( execution(* org.vwiki.control.*.test*(..)) )")
	public Map<String, String> getParameterMap(){
		return requestParams;
	}
}
