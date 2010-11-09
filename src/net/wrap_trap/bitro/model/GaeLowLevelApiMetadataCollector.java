package net.wrap_trap.bitro.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import net.wrap_trap.bitro.annotation.Bind;
import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.aspectj.lang.ProceedingJoinPoint;

public class GaeLowLevelApiMetadataCollector implements MetadataCollector {

	@Bind(name="net.wrap_trap.bitro.model.GaeLowLevelApiMetadataCollector.modelMetadataMap")
	private Map<Class<?>, Map<String, PropertyDescriptor>> modelMetadataMap;
	
	@Override
	public void collectMetadata(ProceedingJoinPoint joinPoint) {
		Class<?> clazz = joinPoint.getTarget().getClass();
		if(modelMetadataMap.containsKey(clazz)){
			return;
		}
		try{
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			Map<String, PropertyDescriptor> propMap = new HashMap<String, PropertyDescriptor>();
			for(PropertyDescriptor prop : beanInfo.getPropertyDescriptors()){
				propMap.put(prop.getName(), prop);
			}
			if(modelMetadataMap == null){
				ApplicationContainer container = ApplicationContainer.getContainer();
				container.registerComponent(
					"net.wrap_trap.bitro.model.GaeLowLevelApiMetadataCollector.modelMetadataMap", 
					new HashMap<Class<?>, Map<String, PropertyDescriptor>>(), 
					Scope.APPLICATION
				);
			}
			modelMetadataMap.put(clazz, propMap);
		}catch(IntrospectionException e){
			throw new RuntimeException(e);
		}
	}
}
