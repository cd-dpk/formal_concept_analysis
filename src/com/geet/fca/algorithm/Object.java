package com.geet.fca.algorithm;

import java.util.HashSet;
import java.util.Set;

public class Object {
	public String objectName;
	public Set<Attribute> attributes = new HashSet<Attribute>(); 

	public Object() {
		// TODO Auto-generated constructor stub
	}
	public Object(String objectName){
		this.objectName = objectName;
	}
	
	@Override
	public String toString() {
		String str="";
		str += objectName+",{";
		for (Attribute attribute : attributes) {
			str+=attribute+" ";
		}
		str += "}";
		return str;
	}
	
	public Object toClone(){
		Object object = new Object();
		object.objectName = objectName;
		object.attributes = Attribute.getClonedEvents(attributes);
		return object;
	}
}
