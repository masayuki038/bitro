package net.wrap_trap.bitro.servlet;

import java.util.HashMap;
import java.util.Map;

import net.wrap_trap.bitro.action.Action;

public class ActionController {

	Map<String, Action> pathMapping = new HashMap<String, Action>();
	
	public ActionController(){
	}
	
	public Action getAction(String path){
		return pathMapping.get(path);
	}
	
	public void registerAction(String path, Action action){
		pathMapping.put(path, action);
	}
}
