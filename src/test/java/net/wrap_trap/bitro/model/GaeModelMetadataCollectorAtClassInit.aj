package net.wrap_trap.bitro.model;

public aspect GaeModelMetadataCollectorAtClassInit extends GaeModelMetadataCollector{
	pointcut metadtaCollect() : staticinitialization(net.wrap_trap.bitro.model.Model+);
}
