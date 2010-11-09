package net.wrap_trap.bitro.model;

import org.aspectj.lang.ProceedingJoinPoint;

public interface MetadataCollector {
	void collectMetadata(ProceedingJoinPoint joinPoint);
}
