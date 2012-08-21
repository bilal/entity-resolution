package com.techtraits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Product {

	@JsonProperty
	private String product_name;

	@JsonProperty
	private String manufacturer;

	@JsonProperty
	private String family;

	@JsonProperty
	private String model;

	@JsonProperty("announced-date")
	private String announceddate;
	
	@JsonIgnore
	private List<String> nameTags;
	
	@JsonIgnore
	private List<String> manufacturerTags;
	
	@JsonIgnore
	private List<String> modelTags;
	
	public void generateTags(Tagger tagger){
		this.nameTags = tagger.getTags(this.product_name);
		this.manufacturerTags = tagger.getTags(this.manufacturer);
		if (this.model == null){
			this.setModelTags(new ArrayList<String>());
		}else{
			this.setModelTags(tagger.getTags(this.model));
		}
	}
	
	public List<String> getNameTags() {
		return nameTags;
	}

	public void setNameTags(List<String> nameTags) {
		this.nameTags = nameTags;
	}

	public List<String> getManufacturerTags() {
		return manufacturerTags;
	}

	public void setManufacturerTags(List<String> manufacturerTags) {
		this.manufacturerTags = manufacturerTags;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAnnounceddate() {
		return announceddate;
	}

	public void setAnnounceddate(String announceddate) {
		this.announceddate = announceddate;
	}
	
	@Override
	public boolean equals(Object arg) {
		if(arg instanceof Product) {
			return super.equals(arg) || this.getProduct_name().equals(((Product)arg).getProduct_name());
		} else {
			return  false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
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
	
	public static List<Product> readProducts(String fileName, ObjectMapper mapper, Tagger tagger)
			throws IOException {

		File productFile = new File(fileName);
		FileReader fRead = new FileReader(productFile);
		BufferedReader bRead = new BufferedReader(fRead);
	
		List<Product> products = new ArrayList<Product>();
		
		String line;
		while ((line = bRead.readLine()) != null) {
			Product product = mapper.readValue(line, Product.class);
			product.generateTags(tagger);
			products.add(product);
		}
		
		return products;
	}

	public List<String> getModelTags() {
		return modelTags;
	}

	public void setModelTags(List<String> modelTags) {
		this.modelTags = modelTags;
	}

}
