package com.techtraits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlainTagger implements Tagger{

	/*
	 * Convert a string into tags (tokens/features). 
	 * Converts "_" to " ", splits by space and converts to lowercase 
	 */
	@Override
	public List<String> getTags(String string) {
		if (string == null){
			return new ArrayList<String>();
		}else{
			String lString = string.toLowerCase().trim().replaceAll("_", " ");
			String[] spacedTokens = lString.split(" ");
			return Arrays.asList(spacedTokens);
		}
	}

	@Override
	public List<String> getTags(String string, List<String> excludeTags) {
		List<String> tags = getTags(string);
		Set<String> tagSet = new HashSet<String>(tags);
		tagSet.removeAll(excludeTags);
		return Arrays.asList((String[]) tagSet.toArray());
	}

	@Override
	public boolean hasTag(String tag, List<String> tags) {
		return this.hasAllTags(Arrays.asList(sanitizeTag(tag)), tags);
	}
	
	@Override
	public boolean hasAllTags(List<String> tagsToFind, List<String> tags){
		Set<String> stringTags = new HashSet<String>(tags);
		for (String tag : tagsToFind){
			if (!stringTags.contains(tag)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int commonTagCount(List<String> tags1 , List<String> tags2) {
		Set<String> tagsSet = new HashSet<String>(tags1);
		int count = 0;
		for(String tag : tags2){
			if (tagsSet.contains(tag)){
				++count;
			}
		}
		
		return count;
	}
	
	public String sanitizeTag(String tag){
		return tag.toLowerCase().trim();
	}

}
