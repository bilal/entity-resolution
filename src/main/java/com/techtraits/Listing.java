package com.techtraits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Listing {

	@JsonProperty
	private String title;

	@JsonProperty
	private String manufacturer;

	@JsonProperty
	private String currency;

	@JsonProperty
	private String price;
	
	@JsonIgnore
	private List<String> titleTags;
	
	@JsonIgnore
	private List<String> manufacturerTags;
	
	public void generateTags(Tagger tagger){
		this.titleTags = tagger.getTags(this.title);
		this.manufacturerTags = tagger.getTags(this.manufacturer);
	}
	
	public List<String> getTitleTags() {
		return titleTags;
	}

	public void setTitleTags(List<String> titleTags) {
		this.titleTags = titleTags;
	}

	public List<String> getManufacturerTags() {
		return manufacturerTags;
	}

	public void setManufacturerTags(List<String> manufacturerTags) {
		this.manufacturerTags = manufacturerTags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String print(ObjectMapper mapper){
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public boolean equals(Object arg) {
		if(arg instanceof Listing) {
			Listing l = (Listing) arg;
			return super.equals(arg) || 
					(
					 this.title.equals(l.getTitle()) && 
					 this.manufacturer.equals(l.getManufacturer()) &&
					 this.price.equals(l.getPrice()) &&
					 this.currency.equals(l.getCurrency())
					);
		} else {
			return  false;
		}
	}
	
	public static List<Listing> readListings(String fileName, ObjectMapper mapper, Tagger tagger) 
			throws JsonParseException, IOException {
		
		File listFile = new File(fileName);
		FileReader fRead = new FileReader(listFile);
		BufferedReader bRead = new BufferedReader(fRead);

		List<Listing> listings = new ArrayList<Listing>();
		
		String line;
		while ((line = bRead.readLine()) != null) {
			Listing listing = mapper.readValue(line, Listing.class);
			listing.generateTags(tagger);
			listings.add(listing);
		}

		return listings;

	}
}
