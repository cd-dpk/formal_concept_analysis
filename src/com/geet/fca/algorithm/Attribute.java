package com.geet.fca.algorithm;

import java.util.HashSet;
import java.util.Set;

public class Attribute implements Comparable<Attribute>,Cloneable{

	private String attributeString;
	private int value;
	
	public Attribute(String attributeString) {
		super();
		this.attributeString = attributeString;
	}
	public String getAttributeString() {
		return attributeString;
	}
	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	    public int hashCode(){
	        int hashcode = 0;
	        hashcode += attributeString.hashCode();
	        return hashcode;
	    }

	@Override
	public boolean equals(java.lang.Object obj) {
			if (obj instanceof Attribute) {
				Attribute e = (Attribute) obj;
				return e.attributeString.equals(this.attributeString);
			}else{
				return false;			
			}
	}
	
	@Override
	public String toString() {
		return attributeString;
	}
	
	public static void main(String[] args) {
		Set<Attribute> events = new HashSet<Attribute>();
		Attribute e1 = new Attribute("a");
		System.out.println(e1+","+e1.hashCode());
		Attribute e2 = new Attribute("a");
		System.out.println(e2+","+e2.hashCode());
		Attribute e4 = new Attribute("a");
		System.out.println(events.add(e1));
		System.out.println(events.add(e2));
		System.out.println(events.add(e4));
		System.out.println(events.toString());
		
		System.out.println("a".compareTo("b"));
		
		
	}

	@Override
	public int compareTo(Attribute o) {
		return (value-o.value);
	}
	
	public Attribute clone(){
		Attribute event = new Attribute(attributeString);
		event.setValue(value);
		return event;
	}
	
	public static Set<Attribute> getClonedEvents(Set<Attribute> events){
		Set<Attribute> clonedEvents = new HashSet<Attribute>();
		for (Attribute event : events) {
			clonedEvents.add(event.clone());
		}
		return clonedEvents;
	}
	
}
