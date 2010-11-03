package net.wrap_trap.bitro.action;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.junit.Before;
import org.junit.Test;

import net.wrap_trap.bitro.annotation.Bind;
import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;
import net.wrap_trap.bitro.test.TestCaseBase;
import net.wrap_trap.bitro.test.TestContext;
import net.wrap_trap.bitro.test.UseMock;

@Aspect
@UseMock
public class RedirectForwarderTestCase extends TestCaseBase {
	
	@Bind
	private TestContext testContext;
	
	@Before
	public void setUp(){
		ApplicationContainer.init();
		ApplicationContainer container = ApplicationContainer.getContainer();
		TestContext testContext = new TestContext();
		
		container.registerComponent(Context.class, testContext, Scope.THREAD);
		container.registerComponent(TestContext.class, testContext, Scope.THREAD);
	}
	
	@Test
	public void testReturningValueOfMockObject(){
		HttpServletRequest request = testContext.getRequest();
		String serverName = request.getServerName();
		assertEquals("www.wrap-trap.net", serverName);
	}
	
	@Test
	public void testGetRedirectUrlHttp(){
		testContext.setMockAttribute("http.req.scheme", "http");
		testContext.setMockAttribute("http.req.serverPort", 80);
		testContext.setMockAttribute("http.req.contextPath", "/foo");
		HttpServletRequest request = testContext.getRequest();
		RedirectForwarder redirectForwarder = new RedirectForwarder("/path");
		assertEquals("http://www.wrap-trap.net/foo/path", redirectForwarder.getRedirectUrl(request));
	}

	@Test
	public void testGetRedirectUrlHttp8080(){
		testContext.setMockAttribute("http.req.scheme", "http");
		testContext.setMockAttribute("http.req.serverPort", 8080);
		testContext.setMockAttribute("http.req.contextPath", "/foo");
		HttpServletRequest request = testContext.getRequest();
		RedirectForwarder redirectForwarder = new RedirectForwarder("/bar");
		assertEquals("http://www.wrap-trap.net:8080/foo/bar", redirectForwarder.getRedirectUrl(request));
	}
	
	@Test
	public void testGetRedirectUrlSsl(){
		testContext.setMockAttribute("http.req.scheme", "https");
		testContext.setMockAttribute("http.req.serverPort", 443);
		testContext.setMockAttribute("http.req.contextPath", "/foo");
		HttpServletRequest request = testContext.getRequest();
		RedirectForwarder redirectForwarder = new RedirectForwarder("/path");
		assertEquals("https://www.wrap-trap.net/foo/path", redirectForwarder.getRedirectUrl(request));
	}

	@Test
	public void testGetRedirectUrlSsl8843(){
		testContext.setMockAttribute("http.req.scheme", "https");
		testContext.setMockAttribute("http.req.serverPort", 8443);
		testContext.setMockAttribute("http.req.contextPath", "/foo");
		HttpServletRequest request = testContext.getRequest();
		RedirectForwarder redirectForwarder = new RedirectForwarder("/path");
		assertEquals("https://www.wrap-trap.net:8443/foo/path", redirectForwarder.getRedirectUrl(request));
	}
}
