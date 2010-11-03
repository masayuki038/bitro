package net.wrap_trap.bitro.model;

import java.io.Serializable;
import java.util.Collection;

import javax.jdo.PersistenceManager;

public abstract class Model implements Serializable{

	protected static PersistenceManager getPersistenceManager(){
		return null;
	}
	
	public void beforeSave(){	
	}
	
	public void save(){
		beforeSave();
		getPersistenceManager().makePersistent(this);
	}

	public void delete(){
		getPersistenceManager().deletePersistent(this);
	}
	
	public static <T> T getObjectById(Class<T> clazz, Object id){
		return getPersistenceManager().getObjectById(clazz, id);
	}
	
	public static <T> Collection<T> detachCopyAll(Collection<T> collection){
		return getPersistenceManager().detachCopyAll(collection);
	}
}
