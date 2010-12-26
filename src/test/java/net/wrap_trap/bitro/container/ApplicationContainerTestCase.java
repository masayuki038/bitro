package net.wrap_trap.bitro.container;

import org.junit.Before;
import org.junit.Test;

import net.wrap_trap.bitro.annotation.Bind;
import net.wrap_trap.bitro.test.TestCaseBase;

public class ApplicationContainerTestCase extends TestCaseBase{
	
	@Bind(name="test string")
	private String injectedStringForApplication;
	
	@Bind(name="test string", scope=Scope.THREAD)
	private String injectedStringForThread;

	@Bind
	private Foo fooForApplication;
	
	@Bind(scope=Scope.THREAD)
	private Foo fooForThread;

	@Before
	public void setUp(){
		ApplicationContainer.init();
	}
	
	@Test
	public void testStringInjectionWithApplicationScope(){
		assertNull(injectedStringForApplication);
		ApplicationContainer container = ApplicationContainer.getContainer();
		container.registerComponent("test string", "hoge", Scope.APPLICATION);
		assertEquals("hoge", injectedStringForApplication);
	}
	
	@Test
	public void testObjectInjectionWithAppliactionScope(){
		assertNull(fooForApplication);
		ApplicationContainer container = ApplicationContainer.getContainer();
		Foo localFoo = new Foo();
		container.registerComponent(Foo.class, localFoo, Scope.APPLICATION);
		assertEquals(fooForApplication, localFoo);
		assertNull(fooForApplication.getBarWithApplication());
		Bar bar = new Bar("test");
		container.registerComponent(Bar.class, bar, Scope.APPLICATION);
		assertEquals(fooForApplication, localFoo);
		assertEquals(bar, fooForApplication.getBarWithApplication());
		assertEquals("test", fooForApplication.getBarWithApplication().getHoge());
	}
	
	@Test
	public void testStringInjectionWithThreadScope(){
		assertNull(injectedStringForThread);
		ApplicationContainer container = ApplicationContainer.getContainer();
		container.registerComponent("test string", "hogehoge", Scope.THREAD);
		assertEquals("hogehoge", injectedStringForThread);
	}

	@Test
	public void testObjectInjectionWithThread(){
		assertNull(fooForThread);
		ApplicationContainer container = ApplicationContainer.getContainer();
		Foo localFoo = new Foo();
		container.registerComponent(Foo.class, localFoo, Scope.THREAD);
		assertEquals(fooForThread, localFoo);
		assertNull(fooForThread.getBarWithThread());
		Bar bar = new Bar("testtest");
		container.registerComponent(Bar.class, bar, Scope.THREAD);
		assertEquals(fooForThread, localFoo);
		assertEquals(bar, fooForThread.getBarWithThread());
		assertEquals("testtest", fooForThread.getBarWithThread().getHoge());
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					assertNull(fooForThread.getBarWithThread());
					fail("expected occuring InstatiationException since there is not a container bound with this thread.");
				}catch(InstantiationError ex){
					assertEquals("container is not found." , ex.getMessage());
				}
			}
		}).start();
	}

	static public class Foo	{
		@Bind
		private Bar barWithApplication;

		@Bind(scope=Scope.THREAD)
		private Bar barWithThread;

		public Foo(){
		}
		
		public Bar getBarWithApplication(){
			return barWithApplication;
		}
		
		public Bar getBarWithThread(){
			return barWithThread;
		}
	}
	
	static public class Bar {
		private String hoge;

		public Bar(String hoge) {
			super();
			this.hoge = hoge;
		}

		public String getHoge() {
			return hoge;
		}
	}
}
