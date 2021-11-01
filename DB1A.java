// DB1A.java CS5151/6051 2020 Yizong Cheng
// turn Computer Store into RDF
// Usage: java DB1A

import java.io.*;
import java.util.*;

public class DB1A{

	HashSet<HashMap<String, String>> Product = new HashSet<HashMap<String, String>>();
	HashSet<HashMap<String, String>> PC = new HashSet<HashMap<String, String>>();
	HashSet<HashMap<String, String>> Laptop = new HashSet<HashMap<String, String>>();
	HashSet<HashMap<String, String>> Printer = new HashSet<HashMap<String, String>>();
	HashMap<String, HashMap<String, String>> RDF = new HashMap<String, HashMap<String, String>>();
	HashSet<String> attributeSet = new HashSet<String>();
	boolean isFirst = true;

    void readRelation(String filename, HashSet<HashMap<String, String>> store){
	Scanner in = null;
	try {
		in = new Scanner(new File(filename));
	} catch (FileNotFoundException e){
		System.err.println(filename + " not found.");
		System.exit(1);
	}
	String[] terms = in.nextLine().split("\t");
	int cols = Integer.parseInt(terms[1]);
	int rows = Integer.parseInt(terms[2]);
	String[] attributes = in.nextLine().split("\t");
	for (int r = 0; r < rows; r++){
		HashMap<String, String> hmap = new HashMap<String, String>();
		String[] cells = in.nextLine().split("\t");
		for (int c = 0; c < cols; c++) hmap.put(attributes[c], cells[c]);
		store.add(hmap);
	}
	in.close();
    }

  void readRelations(){
	readRelation("Product.txt", Product);
	readRelation("PC.txt", PC);
	readRelation("Laptop.txt", Laptop);
	readRelation("Printer.txt", Printer);
  }

  void toRDF(HashSet<HashMap<String, String>> relation, String key, String relationName){
	relation.forEach(x -> {
		String subject = x.get(key);
		x.forEach((k, v) -> {
			if (!k.equals(key)){ 
				if (!RDF.containsKey(subject)) RDF.put(subject, new HashMap<String, String>());
				if (RDF.get(subject).containsKey(k)) RDF.get(subject).put(relationName + "." + k, v);
				else RDF.get(subject).put(k, v);
			}
		});
	});
  }

  void allToRDF(){
	toRDF(Product, "model", "Product");
	toRDF(PC, "model", "PC");
	toRDF(Laptop, "model", "Laptop");
	toRDF(Printer, "model", "Printer");
  }

  void turtle(){
	RDF.forEach((subject, map) -> {
		System.out.print(subject);
		isFirst = true;
		map.forEach((predicate, object) -> {
			if (isFirst){ System.out.print(" " + predicate + " " + object); isFirst = false; 
			}else System.out.print(";\n\t" + predicate + " " + object);
		});
		System.out.println(" .");
	});
  }

  void oneTable(String name, String key){  // turn all four relations into one table
	RDF.forEach((subject, map) -> attributeSet.addAll(map.keySet()));  // find all attributes
	String[] attributes = attributeSet.toArray(new String[0]);
	System.out.println(name + "\t" + (attributes.length + 1) + "\t" + RDF.size());
	System.out.print(key);  // key is also an attribute
	for (int i = 0; i < attributes.length; i++) System.out.print("\t" + attributes[i]);  // the other attributes
	System.out.println();
	RDF.forEach((subject, map) -> { // for each tuple
		System.out.print(subject);
		for (int i = 0; i < attributes.length; i++) 
 		 {      
			if(map.get(attributes[i])==null)
			{
			System.out.print("\t" + "N/A");
			continue;
			}
			
			System.out.print("\t" + map.get(attributes[i]));


		}




		// Your code for printing cells for a tuple instead of "null", print "N/A" when attribute has no value
		System.out.println();
	});
  }

 public static void main(String[] args){
	DB1A db1 = new DB1A();
	db1.readRelations();
	db1.allToRDF();
	db1.oneTable("ComputerStore", "model");
//	db1.turtle();
 }
}


	