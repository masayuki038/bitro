package net.wrap_trap.bitro.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wrap_trap.bitro.action.Action;
import net.wrap_trap.bitro.action.ActionContext;
import net.wrap_trap.bitro.action.Context;
import net.wrap_trap.bitro.action.Forwarder;
import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

public class ApplicationServlet extends HttpServlet{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8418809259962240101L;

	private ActionController controller;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContainer.init();
	}
	
	public void setActionController(ActionController actionController){
		this.controller = actionController;
	}

	protected boolean processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Action action = controller.getAction(request.getPathInfo());
		if(action == null){
			return false;
		}
		
		ActionContext context = prepareContext(request, response);
		ApplicationContainer container = ApplicationContainer.getContainer();
		container.registerComponent(Context.class, context, Scope.THREAD);
		
		if(!action.prepare()){
			setForwarder(request, response, action);
			return true;
		}
		
		if(request.getMethod().toLowerCase().equals("post")){
			String event = request.getParameter("event");
			if(event == null || event.length() == 0){
				throw new IllegalArgumentException("failed to detect the event. event:" + event);
			}
			try {
				Method method = action.getClass().getDeclaredMethod(event);
				method.invoke(action);
			} catch (SecurityException e) {
				throw new ServletException(e);
			} catch (NoSuchMethodException e) {
				throw new ServletException(e);
			} catch (IllegalArgumentException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			} catch (InvocationTargetException e) {
				throw new ServletException(e);
			}
		}else{
			action.init();
		}
		setForwarder(request, response, action);
		return true;
	}

	private void setForwarder(HttpServletRequest request,
			HttpServletResponse response, Action action)
			throws ServletException, IOException {
		Forwarder forwarder = action.getForwarder();
		if(forwarder != null){
			forwarder.forward(request, response);
		}
	}
	
	protected ActionContext prepareContext(HttpServletRequest request, HttpServletResponse response){
		ActionContext context = new ActionContext();
		context.setRequest(request);
		context.setResponse(response);
		return context;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(!processRequest(request, response)){
			super.doGet(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(!processRequest(request, response)){
			super.doPost(request, response);
		}
	}
}
