package com.frameworkset.dictionary.bean;
/**
 * Description:字典条目bean
 * @author qingl2
 * @version 2013-07-04
 */
public class Item {
	/**
	 * Description:
	 * dictDataId 主键ID
	 dictTypeId   字典类型ID
	 dictDataName 字典条目名称
	 dictDataValue 字典条目值
	 dictDataOrder 字典条目排序
	 * @author qingl2
	 * @version 2013-07-04
	 */
	  private String dictDataId;
	private String dictTypeId;
	private String dictDataName;
	private String dictDataValue;
	private String dictDataOrder;
	public String getDictDataId() {
		return dictDataId;
	}
	public void setDictDataId(String dictDataId) {
		this.dictDataId = dictDataId;
	}
	public String getDictTypeId() {
		return dictTypeId;
	}
	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}
	public String getDictDataName() {
		return dictDataName;
	}
	public void setDictDataName(String dictDataName) {
		this.dictDataName = dictDataName;
	}
	public String getDictDataValue() {
		return dictDataValue;
	}
	public void setDictDataValue(String dictDataValue) {
		this.dictDataValue = dictDataValue;
	}
	public String getDictDataOrder() {
		return dictDataOrder;
	}
	public void setDictDataOrder(String dictDataOrder) {
		this.dictDataOrder = dictDataOrder;
	}
	
}
