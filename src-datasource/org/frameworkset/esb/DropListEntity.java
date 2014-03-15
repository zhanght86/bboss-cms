package org.frameworkset.esb;

import java.io.Serializable;

/**
 * 下拉列表的里面的对象元素Entity
 *<p>Title:DropListModel.java</p>
 *<p>Description:</p>
 *<p>Copyright:Copyright (c) 2010</p>
 *<p>Company:湖南科创</p>
 *@author 刘剑峰
 *@version 1.0
 *2011-4-13
 */
public class DropListEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8224961408042725919L;

	//值，对应option的value值
	private String value;
	
	//文本,对应option标签中间的文字
	private String text;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
