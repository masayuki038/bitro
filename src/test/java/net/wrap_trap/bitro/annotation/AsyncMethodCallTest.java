package net.wrap_trap.bitro.annotation;

import net.wrap_trap.bitro.container.ApplicationContainer;
import net.wrap_trap.bitro.container.Scope;

import org.junit.Before;
import org.junit.Test;

public class AsyncMethodCallTest {

	@Before
	public void setUp(){
		ApplicationContainer.init();
		ApplicationContainer.getContainer().registerComponent(
			DisplayResultCallback.class,
			new DisplayResultCallback(),
			Scope.APPLICATION
		);
	}
	
	@Test
	public void testAsyncMethodCall() throws InterruptedException{
		Target target = new Target();
		target.add(1, 2);
		System.out.println("after call target.add(1,2).");
	}
	

	static class Target{
		@Async(callbackClass = DisplayResultCallback.class)
		public int add(int a, int b) throws InterruptedException{
			Thread.sleep(10);
			System.out.println("called Target.add");
			return a+b;
		}
	}
	
	static class DisplayResultCallback implements Callback{
		@Override
		public void doCallback(Object result) {
			System.out.println("result: " + result);
		}
	}
}
