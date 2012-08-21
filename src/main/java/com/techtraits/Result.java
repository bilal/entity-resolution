package com.techtraits;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Result {

	@JsonProperty
	String product_name;
	
	@JsonProperty
	List<Listing> listings;
	
	public Result(){
		
	}
	
	public Result(Product product, List<Listing> listings) {
		this.product_name = product.getProduct_name();
		this.listings = listings;
	}

	public String print(ObjectMapper mapper) throws JsonGenerationException, JsonMappingException, IOException{
			return mapper.writeValueAsString(this);
	}
	
	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public List<Listing> getListings() {
		return listings;
	}

	public void setListings(List<Listing> listings) {
		this.listings = listings;
	}
	
	@Override
	public boolean equals(Object arg) {
		if(arg instanceof Result) {
			try {
				String productNameUtf = new String(this.getProduct_name().getBytes("UTF-8"), "UTF-8");
				String argPNameUtf = new String(((Result)arg).getProduct_name().getBytes("UTF-8"), "UTF-8");
				return productNameUtf.equals(argPNameUtf);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return  false;
		}
	}

	
}
