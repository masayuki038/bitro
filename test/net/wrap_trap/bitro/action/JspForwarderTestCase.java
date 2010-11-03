package net.wrap_trap.bitro.action;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class JspForwarderTestCase extends TestCase{

	@Before
	public void setUp(){}
	
	@Test
	public void testJspPathCreation(){
		JspForwarder forwarder = new JspForwarder("/test.jsp");
		assertEquals("/WEB-INF/test.jsp", forwarder.createJspFilePath());
	}
	
	@Test
	public void testJspPathCreationWithoutSlashAbove(){
		JspForwarder forwarder = new JspForwarder("test.jsp");
		assertEquals("/WEB-INF/test.jsp", forwarder.createJspFilePath());
	}
}
