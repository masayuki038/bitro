package net.wrap_trap.bitro.container;

import java.lang.reflect.Field;

import net.wrap_trap.bitro.annotation.Bind;

import org.aspectj.runtime.reflect.FieldSignatureImpl;

public aspect Binder {

	Object around() : get(@net.wrap_trap.bitro.annotation.Bind * *){
		ApplicationContainer container = ApplicationContainer.getContainer();
		Field field = ((FieldSignatureImpl)thisJoinPoint.getSignature()).getField();
		Bind bind = field.getAnnotation(Bind.class);
		String name = bind.name();
		if((name != null) && (name.length() > 0)){
			return container.getComponent(name, bind.scope());
		}
		return container.getComponent(field.getType(), bind.scope());
	}
}
