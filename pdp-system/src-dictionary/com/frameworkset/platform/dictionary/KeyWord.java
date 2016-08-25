package com.frameworkset.platform.dictionary;

public class KeyWord implements java.io.Serializable {
	
	private String keywordId = null;
	
	private String fieldName = null;
	
	private String dictypeId = null;
	
	private String javaProperty = null;

	public String getDictypeId() {
		return dictypeId;
	}

	public void setDictypeId(String dictypeId) {
		this.dictypeId = dictypeId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getJavaProperty() {
		return javaProperty;
	}

	public void setJavaProperty(String javaProperty) {
		this.javaProperty = javaProperty;
	}

	public String getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}

}
