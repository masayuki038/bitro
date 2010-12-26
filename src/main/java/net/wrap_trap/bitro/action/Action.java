package net.wrap_trap.bitro.action;

public abstract class Action {
	
	private Forwarder forwarder;
	
	public boolean prepare(){
		return true;
	}
	
	public void init(){
	}
	
	public Forwarder getForwarder(){
		return forwarder;
	}
	
	public void setForwarder(Forwarder forwarder){
		this.forwarder = forwarder;
	}
}
