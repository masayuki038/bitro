package net.wrap_trap.bitro.container;

public class ComponentDefinition {
	
	public Class<?> componentType;
	public Class<?> componentImplType;
	public Object component;
	
	public ComponentDefinition(Class<?> componentType, Class<?> componentImplType) {
		super();
		this.componentType = componentType;
		this.componentImplType = componentImplType;
	}

	public ComponentDefinition(Object component) {
		super();
		this.component = component;
		Class<?> componentClass = component.getClass();
		this.componentType = componentClass;
		this.componentImplType = componentClass;
	}

	public Object getComponent() {
		return component;
	}

	public void setComponent(Object component) {
		this.component = component;
	}

	public Class<?> getComponentType() {
		return componentType;
	}

	public Class<?> getComponentImplType() {
		return componentImplType;
	}
}
