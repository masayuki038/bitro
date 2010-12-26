package net.wrap_trap.bitro.annotation;

import java.lang.reflect.Method;

import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class AsyncSupport {

	@Pointcut("call(@net.wrap_trap.bitro.annotation.Async * *.*(..))")
	public void asyncCallPointcut(){};

	@Around("asyncCallPointcut()")
	public Object doParallels(final ProceedingJoinPoint thisJoinPoint) throws Throwable{
		final Callback callback = getCallback(thisJoinPoint);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					Object ret = thisJoinPoint.proceed();
					if(callback != null){
						callback.doCallback(ret);
					}
				}catch(Throwable t){
					throw new RuntimeException(t);
				}
			}
		}).start();
		return null;
	}

	private Callback getCallback(final ProceedingJoinPoint thisJoinPoint) {
		MethodSignature signature = (MethodSignature)thisJoinPoint.getSignature();
		Async concurrent = signature.getMethod().getAnnotation(Async.class);
		return (Callback)ApplicationContainer.getContainer().getComponent(
			concurrent.callbackClass(), Scope.APPLICATION
		);
	}
}
