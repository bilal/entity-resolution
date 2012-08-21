package com.techtraits;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

public class Driver {
	public static void main(String[] args) throws IOException {
	
		if (args.length != 2){
			System.out.println("Error: Invalid input.");
			System.out.println("Usage: <path_to_products_file> <path_to_listings_file>");
			System.exit(0);
		}			
		
		Tagger tagger = new PlainTagger();
		ObjectMapper mapper = new ObjectMapper();
		List<Product> products = Product.readProducts(args[0], mapper, tagger);
		System.out.println("Read all products.");
		List<Listing> listings = Listing.readListings(args[1], mapper, tagger);
		System.out.println("Read all listings.");
		Matcher matcher = new PlainMatcher(tagger);
		List<Result> results = matcher.match(products, listings);
		writeResults(results, mapper, "results.txt");
		System.out.println("Results written to results.txt");	
	}
	
	public static void writeResults(List<Result> results, ObjectMapper mapper, String outputFile) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
		
		for (Result result: results){
			writer.write(result.print(mapper));
			writer.newLine();
		}
		
		writer.close();
	}
	
}
