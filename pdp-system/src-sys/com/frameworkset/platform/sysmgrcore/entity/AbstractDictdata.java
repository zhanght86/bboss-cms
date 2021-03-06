package com.frameworkset.platform.sysmgrcore.entity;

/**
 * AbstractTdSmDictdata generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractDictdata implements java.io.Serializable {

	// Fields

	private String dictdataId;

	private String dicttypeId;

	private String dictdataName;

	private String dictdataValue;

	private Dicttype dicttype;
	
	private long dictdataOrder;

	// Constructors



	/** default constructor */
	public AbstractDictdata() {
	}

	/** full constructor */
	public AbstractDictdata(String dicttypeId, String dictdataName,
			String dictdataValue,long dictdataOrder) {
		this.dicttypeId = dicttypeId;
		this.dictdataName = dictdataName;
		this.dictdataValue = dictdataValue;
		this.dictdataOrder = dictdataOrder;
	}

	// Property accessors

	public String getDictdataId() {
		return this.dictdataId;
	}

	public void setDictdataId(String dictdataId) {
		this.dictdataId = dictdataId;
	}

	public String getDicttypeId() {
		return this.dicttypeId;
	}

	public void setDicttypeId(String dicttypeId) {
		this.dicttypeId = dicttypeId;
	}

	public String getDictdataName() {
		return this.dictdataName;
	}

	public void setDictdataName(String dictdataName) {
		this.dictdataName = dictdataName;
	}

	public String getDictdataValue() {
		return this.dictdataValue;
	}

	public void setDictdataValue(String dictdataValue) {
		this.dictdataValue = dictdataValue;
	}

	public Dicttype getDicttype() {
		return dicttype;
	}

	public void setDicttype(Dicttype dicttype) {
		this.dicttype = dicttype;
		
		if (dicttypeId == null && dicttype != null)
			dicttypeId = dicttype.getDicttypeId();
	}

	public long getDictdataOrder() {
		return dictdataOrder;
	}

	public void setDictdataOrder(long dictdataOrder) {
		this.dictdataOrder = dictdataOrder;
	}

}