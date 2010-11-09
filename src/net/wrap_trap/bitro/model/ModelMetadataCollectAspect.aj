package net.wrap_trap.bitro.model;

import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.aspectj.lang.ProceedingJoinPoint;

public aspect ModelMetadataCollectAspect {

	pointcut modelMetadataCollectPointcut() : call(net.wrap_trap.bitro.model.Model+.new());
	
	after() : modelMetadataCollectPointcut(){
		ApplicationContainer container = ApplicationContainer.getContainer();
		MetadataCollector collector = 
			(MetadataCollector)container.getComponent(MetadataCollector.class, Scope.APPLICATION);
		collector.collectMetadata((ProceedingJoinPoint)thisJoinPoint);
	}
}
