package net.wrap_trap.bitro.model;

import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class ModelMetadataAspect {

	@Pointcut("call(net.wrap_trap.bitro.model.Model+.new())")
	public void metadataCollectPointcut(){};

	
	@Before("metadataCollectPointcut()")
	public void collectMetadata(ProceedingJoinPoint joinPoint){
		ApplicationContainer container = ApplicationContainer.getContainer();
		MetadataCollector collector = 
			(MetadataCollector)container.getComponent(MetadataCollector.class, Scope.APPLICATION);
		collector.collectMetadata(joinPoint);
	}
}
