package net.wrap_trap.bitro.model;

import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.InitializerSignature;

public aspect ModelMetadataCollectAspect {

	pointcut modelLoaded() : staticinitialization(net.wrap_trap.bitro.model.Model+);
	
	after() : modelLoaded(){
		InitializerSignature initsig =
			(InitializerSignature)thisJoinPoint.getSignature();
		Class klass = initsig.getDeclaringType();
		ApplicationContainer container = ApplicationContainer.getContainer();
		MetadataCollector collector = 
			(MetadataCollector)container.getComponent(MetadataCollector.class, Scope.APPLICATION);
		collector.collectMetadata(klass);
	}
}
