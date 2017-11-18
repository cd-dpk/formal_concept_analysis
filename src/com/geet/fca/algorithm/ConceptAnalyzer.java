package com.geet.fca.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * @author chandradasdipok 11-Nov-2017 This class analyzes a single issue and
 *         helps to formulate the Formal Concept Analysis (FCA)
 *
 */
public class ConceptAnalyzer {

	// the anylzed issue
	Context context = null;

	public ConceptAnalyzer(Context context) {
		this.context = context;
		int i=0;
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
	///*
		for (Attribute attr : context.getAttributes()) {
			System.out.println(attr.getAttributeString()+","+attr.getValue());
		}
		System.out.println("Relations");
		for (String moduleKey : context.getObjects().keySet()) {
			System.out.println(moduleKey);
			for (Attribute attr : context.getObjects().get(moduleKey).attributes) {
				System.out.println(attr.getAttributeString()+","+attr.getValue());
			}
		}
	//*/
	}

	// Generate all the nodes of fca graph of an issue
	// using next closure algorithms
	// The algorithm is proposed by Ganter et. al at 1992
	public Set<Node> generateNodesOfGraph() {
		Set<Node> nodes = new HashSet<Node>();
		List<Attribute> attributes = new ArrayList<>(Attribute.getClonedEvents(context.getAttributes()));
		Collections.sort(attributes);
		//Collections.sort(attributes);
		//	/*
		System.out.println("Eventsssssssss");
		for (Attribute attr : attributes) {
			System.out.println(attr+","+attr.getValue());
		}
	//	*/
		Set<Attribute> closedSet = getFirstClosure();
		int i = 0;
		while (i < 100) {
			System.out.println("No. " + i + ": Closed Sets " + closedSet);
			Node node = generateNodeFromAClosedSet(Attribute.getClonedEvents(closedSet));
			System.out.println("No. " + i + ": Node "+node);
			nodes.add(node);
			if (closedSet.equals(context.getAttributes())) {
				break;
			}
			closedSet = Attribute.getClonedEvents(getNextClosedSet(Attribute.getClonedEvents(closedSet),
					attributes));
			i++;
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
				// System.out.println("Total Events "+issue.getEvents());
				// System.out.println("Closures:
				// "+closureOfEvents(nextClosedSet, CONTEXT_TABLE));
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

	// Here events are attributes of FCA
	// It takes the attribute set
	// returns the closures of given attributes
	private Set<Attribute> closureOfEvents(Set<Attribute> attributes) {
		Set<Attribute> closure = new HashSet<Attribute>();
		Set<String> transactionsID = new HashSet<String>();

		// collects the transactions which has common attributes i.e., events
		for (String moduleKey : context.getObjects().keySet()) {
			if (context.getObjects().get(moduleKey).attributes.containsAll(attributes)) {
				transactionsID.add(moduleKey);
				// System.out.println("Module Key"+moduleKey);
			}
		}

		// if the object set of common attributes i.e., events is empty then
		// return all the attributes
		if (transactionsID.size() == 0) {
			return Attribute.getClonedEvents(context.getAttributes());
		}
		// other wise take the intersection of all the events of objects
		else {
			for (String transactionKey : transactionsID) {
				if (closure.size() == 0) {
					// store first module's events as closure
					closure = Attribute.getClonedEvents(context.getObjects().get(transactionKey).attributes);
				} else {
					// intersection of module's events given transactions with
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
		// System.out.println("Element Check "+eventM);
		// System.out.println(nextClosedSet);
		// System.out.println(closedSet);
		Set<Attribute> diff = nextClosedSet;
		diff.removeAll(closedSet);
		// System.out.println(diff);
		// if has elements less than eventM
		// return true
		// else return false
		for (Attribute attr : diff) {
			//if (eventM.getEventString().compareTo(event.getEventString()) > 0) {
			//System.out.println(eventM.getValue()+","+event.getValue());
			if(attributeM.getValue() > attr.getValue()){
				// System.out.println("Smallest New Element "+ event);
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
