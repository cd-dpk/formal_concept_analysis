package com.geet.fca.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * 	@author chandradasdipok 
 *	@date 11-Nov-2017 
 *	This class analyzes a context and
 *      helps to formulate the Formal Concept Analysis (FCA)
 *
 */
public class ConceptAnalyzer {

	// the anylzed context
	Context context = null;

	public ConceptAnalyzer(Context context) {
		this.context = context;
		int i=0;
		// gives each attribute a value for ordering
		// which is used in "lectic order"
		for (Attribute attribute : context.getAttributes()) {
			attribute.setValue(i);
			for (String moduleKey : context.getObjects().keySet()) {
				for (Attribute attr : context.getObjects().get(moduleKey).attributes) {
					if (attr.getAttributeString().equals(attribute.getAttributeString())) {
						attr.setValue(i);
					}
				}
			}
			i++;
		}
	}

	// Generate all the nodes of fca graph of context
	// using next closure algorithms
	// The algorithm is proposed by Ganter et. al at 1992
	// The algorithms can be find at 
	//<a href="https://link.springer.com/chapter/10.1007%2F978-3-642-11928-6_22?LI=true">Two basic alogorithms in Concept Analysis</a>
	public Set<Node> generateNodesOfGraph() {
		Set<Node> nodes = new HashSet<Node>();
		List<Attribute> attributes = new ArrayList<>(Attribute.getClonedEvents(context.getAttributes()));
		Collections.sort(attributes);
		Set<Attribute> closedSet = getFirstClosure();
		int i = 0;
		while (true) {
			Node node = generateNodeFromAClosedSet(Attribute.getClonedEvents(closedSet));
			nodes.add(node);
			if (closedSet.equals(context.getAttributes())) {
				break;
			}
			closedSet = Attribute.getClonedEvents(getNextClosedSet(Attribute.getClonedEvents(closedSet),
					attributes));
			i++;
			
			// if the total number of closures greater 
			//than 1000000 then force break;
			if(i>=1000000){
				break;
			}
		}
		return nodes;
	}
	
	// this is the first closure
	// by default, the first closure is empty set
	private Set<Attribute> getFirstClosure() {
		return new HashSet<Attribute>();
	}

	// next closure algorithms
	// The algorithm is proposed by Ganter et. al at 1992
	private Set<Attribute> getNextClosedSet(Set<Attribute> closedSet, List<Attribute> attributes) {
		for (int i = attributes.size() - 1; i >= 0; i--) {
			Set<Attribute> nextClosedSet = new HashSet<Attribute>();
			Attribute m = attributes.get(i);
			System.out.println("Closed Set "+closedSet);
			System.out.println("Element "+m);
			if (closedSet.contains(m)) {
				closedSet.remove(m);
				System.out.println("Closed Set after remove "+closedSet);
			} else {
				nextClosedSet.addAll(closedSet);
				nextClosedSet.add(m);
				System.out.println("Next Closed Set "+nextClosedSet);
				nextClosedSet = closureOfEvents(nextClosedSet);
				System.out.println("Closures of Next Closed Set "+nextClosedSet);
				if (!hasLessThanElementM(Attribute.getClonedEvents(nextClosedSet), Attribute.getClonedEvents(closedSet), m)) {
					return nextClosedSet;
				}else{
					
				}
			}
		}
		return new HashSet<Attribute>();
	}

	// It takes the attribute set
	// returns the closures of given attributes
	private Set<Attribute> closureOfEvents(Set<Attribute> attributes) {
		Set<Attribute> closure = new HashSet<Attribute>();
		Set<String> transactionsID = new HashSet<String>();

		// collects the objects which has common attributes 
		for (String moduleKey : context.getObjects().keySet()) {
			if (context.getObjects().get(moduleKey).attributes.containsAll(attributes)) {
				transactionsID.add(moduleKey);
			}
		}

		// if the object set of common attributes is empty then
		// return all the attributes
		if (transactionsID.size() == 0) {
			return Attribute.getClonedEvents(context.getAttributes());
		}
		// other wise take the intersection of all the attributes of objects
		else {
			for (String transactionKey : transactionsID) {
				if (closure.size() == 0) {
					// store all attributes as closure
					closure = Attribute.getClonedEvents(context.getObjects().get(transactionKey).attributes);
				} else {
					// intersection of objects's  attributes 
					// closure
					closure.retainAll(Attribute.getClonedEvents(context.getObjects().get(transactionKey).attributes));
				}
			}
		}
		return closure;
	}

	

	// detect whether there is difference between closed set and
	// next closed set less than m
	private boolean hasLessThanElementM(Set<Attribute> nextClosedSet, Set<Attribute> closedSet, Attribute attributeM) {
		Set<Attribute> diff = nextClosedSet;
		diff.removeAll(closedSet);
		// if has elements less than eventM
		// return true
		// else return false
		for (Attribute attr : diff) {
			if(attributeM.getValue() > attr.getValue()){
				return true;
			}
		}
		return false;
	}

	// generate a node of FCA graph from a closed set
	private Node generateNodeFromAClosedSet(Set<Attribute> closedSet) {
		Node generatedNode = new Node();
		generatedNode.setAttributes(closedSet);
		for (String moduleKey : context.getObjects().keySet()) {
			Object object = context.getObjects().get(moduleKey);
			// System.out.println(transactionModule.toString());
			if (object.attributes.containsAll(closedSet)) {
				generatedNode.setNumberOfObjects(generatedNode.getNumberOfObjects()+1);
			}
		}
		// System.out.println(generatedNode);
		return generatedNode;
	}

}
