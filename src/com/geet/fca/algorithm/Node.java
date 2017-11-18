package com.geet.fca.algorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author chandradasdipok
 * This class represents a node of FCA graph , i.e., a concept
 * Each Node contains a event set which is a closed set,
 * failed transactions and succeeded transactions 
 * 
 */
public class Node {

	private Set<Attribute> attributes;
	private int numberOfObjects;
	private Set<Object> objects;
	public Node() {
		setAttributes(new HashSet<Attribute>());
		setObjects(new HashSet<Object>());
		setNumberOfObjects(0);
	}
	public void setObjects(Set<Object> objects) {
		this.objects = objects;
	}
	public Set<Object> getObjects() {
		return objects;
	}
	public Set<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void setNumberOfObjects(int numberOfObjects) {
		this.numberOfObjects = numberOfObjects;
	}
	
	public int getNumberOfObjects() {
		return numberOfObjects;
	}
	
	@Override
	public String toString() {
		return attributes+"["+getNumberOfObjects()+"]";
	}
	
	// clone of Node
	public Node toClone(Context issue){
		Node clone = new Node();
		clone.setAttributes(Attribute.getClonedEvents(attributes));
		clone.setNumberOfObjects(getNumberOfObjects());
		return clone;
	}
	
	// check whether this node is child of the Node node
	public boolean isChildtOf(Node node){
		if (!getAttributes().equals(node.getAttributes()) && getAttributes().containsAll(node.getAttributes()) ) {
			return true;
		} 
		return false;
	}
	
	public static Set<Node> clonedNodes(Set<Node> nodes,Context issue){
		Set<Node> clonedNodes = new HashSet<Node>();
		for (Node node : nodes) {
			clonedNodes.add(node.toClone(issue));
		}
		return clonedNodes;
	}
}
