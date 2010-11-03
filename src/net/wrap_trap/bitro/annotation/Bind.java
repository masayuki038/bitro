package net.wrap_trap.bitro.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.wrap_trap.bitro.container.Scope;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Bind {
	String name() default "";
	Scope scope() default Scope.APPLICATION;
}
