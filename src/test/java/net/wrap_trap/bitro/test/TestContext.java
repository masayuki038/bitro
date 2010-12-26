package net.wrap_trap.bitro.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wrap_trap.bitro.action.Context;

public class TestContext implements Context{
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	public void setMockAttribute(String key, Object value){
		attributes.put(key, value);
	}

	public Object getMockAttribute(String key){
		return attributes.get(key);
	}

	@Override
	public HttpServletRequest getRequest() {
		return new HttpServletRequestMock();
	}
	
	@Override
	public HttpServletResponse getResponse(){
		return new HttpServletResponseMock();
	}
}
