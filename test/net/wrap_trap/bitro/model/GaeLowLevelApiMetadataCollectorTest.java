package net.wrap_trap.bitro.model;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Map;

import net.wrap_trap.bitro.annotation.Bind;
import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.junit.Before;
import org.junit.Test;

public class GaeLowLevelApiMetadataCollectorTest {
	
	@Bind(name="net.wrap_trap.bitro.model.GaeLowLevelApiMetadataCollector.modelMetadataMap")
	private Map<Class<?>, Map<String, PropertyDescriptor>> modelMetadataMap;
	
	@Before
	public void setUp(){
		ApplicationContainer.init();
		ApplicationContainer container = ApplicationContainer.getContainer();
		container.registerComponent(MetadataCollector.class, new GaeLowLevelApiMetadataCollector(), Scope.APPLICATION);
	}
	
	@Test
	public void testMetadataCollect(){
		new TestModel();
		Map<String, PropertyDescriptor> propMap = modelMetadataMap.get(TestModel.class);
		assertNotNull(propMap);
		assertEquals(2, propMap.size());
		assertTrue(propMap.containsKey("name"));
		assertTrue(propMap.containsKey("value"));
	}

}
