package com.techtraits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

public class PlainMatcher implements Matcher{
	
	private Tagger tagger;
	
	public PlainMatcher(Tagger tagger){
		this.tagger = tagger;
	}
	
	
	@Override
	public List<Result> match(List<Product> products, List<Listing> listings){
		Map<Product, List<Listing>> matches = matchListings(products, listings);
		List<Result> results = new ArrayList<Result>();
		for(Map.Entry<Product, List<Listing>> entry : matches.entrySet()){
			results.add(new Result(entry.getKey(), entry.getValue()));
		}
		return results;
	}
	
	/*
	 * A restrictive naive matcher:
	 * 
	 * The matching is done on two fields: Manufacturer and Model
	 * - Both manufacturer and Model must match and
	 * - There should be only one product matching a listing
	 * 
	 * @return product matching a specific listing
	 */
	@Override
	public Product matchListingWithProduct(Listing listing, List<Product> products) {	
		List<Product> productMatches = new ArrayList<Product>();
		for (Product product : products){
			if (doManufacturersMatch(product, listing) && doModelsMatch(product, listing)){
				productMatches.add(product);
			}
		}
		if (productMatches.size() == 1){
			return productMatches.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public Map<Product, List<Listing>> matchListings(List<Product> products, List<Listing> listings){	
		
		Map<Product, List<Listing>> matchedListings = new HashMap<Product, List<Listing>>();
		for (Product p : products){
			matchedListings.put(p, new ArrayList<Listing>());
		}
		
		List<Listing> unmatchedListings = new ArrayList<Listing>();
		
		
		for (Listing listing : listings){
			Product product = matchListingWithProduct(listing, products);
			if (product == null){ // no product match
				unmatchedListings.add(listing);
			}else{ // product matched
				List<Listing> pListing = matchedListings.get(product);
				pListing.add(listing);
				matchedListings.put(product, pListing);
			}
		}
		
		System.out.println("Unmatched Listings: " + unmatchedListings.size());
		/* print out unmatched listings
		 *  
		ObjectMapper mapper = new ObjectMapper();
		for(Listing l : unmatchedListings){
			try {
				System.out.println(mapper.writeValueAsString(l));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
		return matchedListings;
	}
	
	/*
	 * Checks if the manufacturer matches for the product and the listing. The criteria
	 * is quite forgiving. 
	 * 
	 * - Any word matches in the manufacturer fields of the product or the listing OR
	 * - Any word matches in the product name with the listing manufacturer
	 */
	public boolean doManufacturersMatch(Product product, Listing listing){
		if (product == null || listing == null){
			return false;
		}else{
			int matchCount = this.tagger.commonTagCount(product.getManufacturerTags(), listing.getManufacturerTags());
			matchCount += this.tagger.commonTagCount(product.getNameTags(), listing.getManufacturerTags());
			return matchCount > 0 ? true:false;
		}
	}
	
	/*
	 * Bulk of the "logic" resides in the following heuristic function
	 * 
	 * Criteria for rejection:
	 * - Model must exist in the product, otherwise, no match
	 * - If model is a single token without any space or '-' 
	 * 	 (e.g., 5000A), make sure family matches if it exists, otherwise, reject
	 * Criteria for acceptance: 
	 * - A Model is converted into a number of its variants. Product and Listing is considered
	 *   a match if any of the variant exists in the listing's title, otherwise, we reject
	 *   E.g., variants of "DSC 1000 S1" are "DSC 1000 S1", "DSC1000S1" and "DSC-1000-S1" and
	 *   variants of "DSC-K10" are "DSK K10", "DSKK10" and "DSC-K10"
	 */
	public boolean doModelsMatch(Product product, Listing listing){
		if (product == null || listing == null){
			return false;
		}else if (product.getModel() != null){
			List<String> productModelTags = product.getModelTags();
			int hIndex = StringUtils.join(productModelTags, "").indexOf("-");
			if (product.getModelTags().size() == 1 && (hIndex == -1) && (product.getFamily() != null)){
					if (!this.tagger.hasTag(product.getFamily(), listing.getTitleTags())){
						return false;
					}
			}
			if(this.tagger.hasAllTags(productModelTags, listing.getTitleTags())){
				return true;
			}else{
				String combinedModel = StringUtils.join(productModelTags,"");
				if (this.tagger.hasTag(combinedModel, listing.getTitleTags())){
					return true;
				}else if(this.tagger.hasTag(StringUtils.join(productModelTags, "-"), listing.getTitleTags())){
					return true;
				}else{
					List<String> hTags = Arrays.asList(combinedModel.split("-"));
					String combinedHModel = StringUtils.join(hTags, "");
					if (this.tagger.hasTag(combinedHModel, listing.getTitleTags())){
						return true;
					}else if(this.tagger.hasAllTags(hTags, listing.getTitleTags())){
						return true;
					}else{
						return false;
					}
				}
			}
		}else{
			return false;
		}
		
	}
	
	
}
