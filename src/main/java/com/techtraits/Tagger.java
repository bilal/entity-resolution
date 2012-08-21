package com.techtraits;

import java.util.List;

public interface Tagger {

	public List<String> getTags(String string);
	
	public List<String> getTags(String string, List<String> excludeTags);
	
	public boolean hasTag(String tag, List<String> tags);

	public boolean hasAllTags(List<String> tagsToFind, List<String> tags);

	public int commonTagCount(List<String> tags1, List<String> tags2);
}
