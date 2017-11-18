package com.geet.fca.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.geet.fca.algorithm.Context;
import com.geet.fca.algorithm.Relation;
import com.geet.fca.algorithm.Relation.RelationBuilder;


/**
 * @author chandradasdipok
 * This class process the inputs
 * Reads issues from files
 */
public class InputHandler {

	private List<Relation> relations;
	
	// read each issue from an directory
	public Context readContextFromFile(String filePath){
		relations = new ArrayList<Relation>();
		if (readRelationssFromFile(filePath)) {
			Context context = new Context(Relation.toCloneTransactions(getRelations()));
			return context;
		}else{
			System.err.println("Errrr");
			System.err.println(filePath);
			System.exit(0);
		}
		return new Context();
	}
	
	
	// read transactions
	private boolean readRelationssFromFile(String logsfilepath){
		File inputFile = new File(logsfilepath);
		Scanner inputScanner=null;
		try {
			inputScanner = new Scanner(inputFile);
			while (inputScanner.hasNext()) {
				String tokens[] = inputScanner.nextLine().split(",");
				if (tokens.length==2) {
					RelationBuilder relationBuilder = new RelationBuilder();
					Relation relation = relationBuilder.object((tokens[0])).attribute((tokens[1])).build();
					getRelations().add(relation);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			inputScanner.close();
		}
		return true;
	}
	
	public List<Relation> getRelations() {
		return relations;
	}
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public static void main(String[] args) {
		InputHandler inputHandler = new InputHandler();
		inputHandler.readContextFromFile("src/com/geet/fca/input/relations.txt");
	}
	
}
