package net.wrap_trap.bitro.container;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;

import org.aspectj.lang.annotation.Aspect;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Aspect
public class ApplicationContainer {
	
	private static ApplicationContainer container;
	
	public synchronized static ApplicationContainer getContainer(){
		if(container == null){
			throw new InstantiationError("container is not initialized.");
		}
		if(container.threadContainer.get() == null){
			container.threadContainer.set(new HashMap<Object, ComponentDefinition>());
		}
		return container;
	}

	private ThreadLocal<Map<Object, ComponentDefinition>> threadContainer;
	
	private Map<Object, ComponentDefinition> applicationContainer;
	
	private ApplicationContainer(){
		applicationContainer = new HashMap<Object, ComponentDefinition>();
		threadContainer = new ThreadLocal<Map<Object, ComponentDefinition>>();
	}

	public synchronized static void init(){
		container = new ApplicationContainer();
	}
	
	public Map<Object, ComponentDefinition> getComponentDefinitionMap(Scope scope){
		Map<Object, ComponentDefinition> ret = null;
		switch(scope){
			case APPLICATION:
				ret = applicationContainer;
			case THREAD:
				ret = threadContainer.get();
			default:
		}
		if(ret == null){
			throw new InstantiationError("container is not found.");
		}
		return ret;
	}
	
	public void registerComponentDef(Class<?> componentType, Class<?> componentImplType, Scope scope){
		getComponentDefinitionMap(scope).put(componentType.getName(), new ComponentDefinition(componentType, componentImplType));
	}
	
	public void registerComponent(Class<?> componentType, Object component, Scope scope){
		registerComponent(componentType.getName(), component, scope);
	}

	public void registerComponent(String name, Object component, Scope scope){
		getComponentDefinitionMap(scope).put(name, new ComponentDefinition(component));
	}
		
	public Object getComponent(Class<?> componentClass, Scope scope){
		ComponentDefinition def = getComponentDefinitionMap(scope).get(componentClass.getName());
		if(def == null){
			return null;
		}
		try{
			Object component = null;
			synchronized(this){
				component = def.getComponent();
				if(component == null){
					Class<?> clazz = def.getComponentImplType();
					component = clazz.newInstance();
					def.setComponent(component);
				}
			}
			return component;
		}catch(InstantiationException e){
			throw new RuntimeException(e);
		}catch(IllegalAccessException e){
			throw new RuntimeException(e);
		}
	}
	
	public Object getComponent(String name, Scope scope){
		ComponentDefinition def = getComponentDefinitionMap(scope).get(name);
		if(def == null){
			return null;
		}
		return def.getComponent();
	}
}
