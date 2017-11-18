package com.geet.fca.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author chandradasdipok
 * @date Oct 24, 2017
 * 
 *       This class is the model of transactions of issues A transaction has
 *       attributes namely transaction time @time, event @eventID, transaction
 *       ID @transactionID, log message @log, status (success or
 *       failure) @status
 */
public class Relation {
	private Object object;
	private Attribute attribute;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	private Relation() {
	};

	public static class RelationBuilder {
		private String object;
		private String attribute;

		public RelationBuilder object(String obj) {
			this.object = obj;
			return this;
		}

		public RelationBuilder attribute(String attr) {
			this.attribute = attr;
			return this;
		}

		public Relation build() {
			Relation relation = new Relation();
			relation.setObject(new Object(object));
			relation.setAttribute(new Attribute(attribute));
			return relation;
		}
	}

	public Relation toClone() {
		return new RelationBuilder().object(object.objectName).attribute(attribute.getAttributeString()).build();
	}

	public static List<Relation> toCloneTransactions(List<Relation> toCloneList) {
		List<Relation> relations = new ArrayList<Relation>();
		for (Relation relation : toCloneList) {
			relations.add(relation.toClone());
		}
		return relations;
	}

	@Override
	public String toString() {
		return object.objectName + "," + attribute;
	}

}
