package com.techtraits;

import java.util.List;
import java.util.Map;

public interface Matcher{
	
	public List<Result> match(List<Product> products, List<Listing> listings);
	
	public Map<Product, List<Listing>> matchListings(List<Product> products, List<Listing> listings);

	public Product matchListingWithProduct(Listing listing, List<Product> products);
	
}

