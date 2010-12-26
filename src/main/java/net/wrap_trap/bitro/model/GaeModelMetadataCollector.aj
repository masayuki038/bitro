package net.wrap_trap.bitro.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.wrap_trap.bitro.annotation.Bind;
import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.aspectj.lang.reflect.InitializerSignature;

public abstract aspect GaeModelMetadataCollector {

	@Bind(name="net.wrap_trap.bitro.model.GaeLowLevelApiMetadataCollector.modelMetadataMap")
	private Map<Class<?>, Map<String, PropertyDescriptor>> modelMetadataMap;

	abstract pointcut metadtaCollect();
	
	after() : metadtaCollect(){
		InitializerSignature initsig =
			(InitializerSignature)thisJoinPoint.getSignature();
		collectMetadata(initsig.getDeclaringType());
	}
	
	public void collectMetadata(Class<?> klass) {
		if(modelMetadataMap == null){
			ApplicationContainer container = ApplicationContainer.getContainer();
			container.registerComponent(
				"net.wrap_trap.bitro.model.GaeLowLevelApiMetadataCollector.modelMetadataMap", 
				new HashMap<Class<?>, Map<String, PropertyDescriptor>>(), 
				Scope.APPLICATION
			);
		}
		if(modelMetadataMap.containsKey(klass)){
			return;
		}
		try{
			Field[] fields = klass.getDeclaredFields();
			BeanInfo beanInfo = Introspector.getBeanInfo(klass);
			Map<String, PropertyDescriptor> propMap = new HashMap<String, PropertyDescriptor>();
			for(PropertyDescriptor prop : beanInfo.getPropertyDescriptors()){
				String name = prop.getName();
				if(isFieldExists(name, fields)){
					propMap.put(name, prop);
				}
			}
			modelMetadataMap.put(klass, propMap);
		}catch(IntrospectionException e){
			throw new RuntimeException(e);
		}
	}
	
	protected boolean isFieldExists(String name, Field[] fields){
		for(Field field : fields){
			if(name.equals(field.getName())){
				return true;
			}
		}
		return false;
	}
}
