package com.geet.fca.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author chandradasdipok 
 * This class is a model to store the information of
 * logs, healing action and the signature of issue Issue represents the
 * document vector in similar issue retrieval
 */
public class Context {

	
	private boolean [][] CONTEXT_TABLE;
	// nodes of FCA graph representation of an issue
	private Set<Node> nodes;
	// failed transaction and succeeded transactions given a issue
	private int numberOfRelations;
	// log messages of an issue
	private List<Relation> relations;
	//objects
	private Map<String,Object> objects;
	
	public Map<String,Object> getObjects() {
		return objects;
	}


	public void setObjects(Map<String,Object> objects) {
		this.objects = objects;
	}

	// set of events
	private Set<Attribute> attributes;

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}


	private Set<Node> getNodes() {
		return nodes;
	}

	private void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public void setNumberOfRelations(int numberOfRelations) {
		this.numberOfRelations = numberOfRelations;
	}
	
	public int getNumberOfRelations() {
		return numberOfRelations;
	}
	
	// build the context table of issue
		private void setContextTable() {
			CONTEXT_TABLE = new boolean[getObjects().size()][getAttributes().size()];
			// event set is converted to array
			Attribute[] attributesArray = new Attribute[getAttributes().size()];
			int flag = 0;
			for (Attribute attr : getAttributes()) {
				attributesArray[flag] = attr;
				flag++;
			}
			// transaction modules keys set is converted into an array
			flag = 0;
			String[] objectArray = new String[getObjects().keySet().size()];
			for (String transaction : getObjects().keySet()) {
				objectArray[flag] = transaction;
				flag++;
			}
			flag = 0;
			System.out.println("\t"+attributesArray.toString());
			for (int i = 0; i < objectArray.length; i++) {
				for (int j = 0; j < attributesArray.length; j++) {
					if (getObjects().get(objectArray[i]).attributes.contains(attributesArray[j])) {
						CONTEXT_TABLE[i][j] = true;
					}
				}
			}
		}
		// print the context table of issue
		private void printContextTable() {
			setContextTable();
			System.out.println(getAttributes().toString());
			System.out.println(getObjects().keySet().toString());
			for (int i = 0; i < CONTEXT_TABLE.length; i++) {
				for (int j = 0; j < CONTEXT_TABLE[i].length; j++) {
					// print 1 for true and 0 for false
					System.out.print((CONTEXT_TABLE[i][j] ? 1 : 0) + " ");
				}
				System.out.println();
			}
			CONTEXT_TABLE = null;
		}

	// generate the graphs for the given context
	public void generateGraphs() {
		ConceptAnalyzer conceptAnalyzer = new ConceptAnalyzer(this);
		// generate  the nodes of a FCA graph
		setNodes(conceptAnalyzer.generateNodesOfGraph());
		// traverse all the nodes
		// store the traversed nodes in previousNodes
		printContextTable();
		System.out.println("Generating Graphs...");
		for (Node node : getNodes()) {
			System.out.println(node);
		}
		
		for (Node currentNode : Node.clonedNodes(getNodes(),this)) {
			// retrieve the nodes those are super concept of the current Node
			// A node is super of current Node if the node is sub set of current Node
			// store all the super nodes as candidate nodes from the previous
			// nodes
			System.out.println("Current Node :" + currentNode);
			Set<Node> parentNodes = new HashSet<Node>();
			for (Node storedNode : Node.clonedNodes(getNodes(),this)) {
				if (currentNode.isChildtOf(storedNode)) {
					// add stored node to parent nodes
					// System.out.println(currentNode+" is child of "+storedNode);
					// // check whether stored node is present as subset in
					// parent nodes
					boolean isStoredNodePresentAsParent = false;
					// delete the nodes from parent nodes which are parent of
					// stored node
					Set<Node> toDeletenodes = new HashSet<Node>();
					for (Node node : parentNodes) {
						if (storedNode.isChildtOf(node)) {
							toDeletenodes.add(node);
						}
						if (node.isChildtOf(storedNode)) {
							isStoredNodePresentAsParent = true;
						}
					}
					for (Node node : toDeletenodes) {
						parentNodes.remove(node);
						// System.out.println("Removed "+node);
					}
					if (!isStoredNodePresentAsParent) {
						// System.out.println("added "+storedNode);
						parentNodes.add(storedNode.toClone(this));
					} else {
						// System.out.println("ignored "+storedNode);
					}
				}
			}
			System.out.println("Parents :" + parentNodes);			
		}
	}
	

	public Context() {
		relations = new ArrayList<Relation>();
		attributes = new HashSet<Attribute>();
		objects = new HashMap<String,Object>();
		nodes = new HashSet<Node>();
	}

	public Context (List<Relation> rels){
		this();
		setRelations(rels);
		for (Relation relation : getRelations()) {
			System.out.println(relation);
			Object object = null;
			if (getObjects().containsKey(relation.getObject().objectName)) {
				object = getObjects().get(relation.getObject().objectName);
			} else {
				object = new Object();
				object.objectName = (relation.getObject().objectName);
			}
			object.attributes.add(relation.getAttribute());
			// set the full event set
			if (!getAttributes().contains(relation.getAttribute())) {
				getAttributes().add(relation.getAttribute());
			}
			// set the transaction types
			getObjects().put(relation.getObject().objectName, object);
		}
		System.out.println(relations.size());
		for (String key : getObjects().keySet()) {
			System.out.println(key);
		}
		printContextTable();
		generateGraphs();
	}
}
