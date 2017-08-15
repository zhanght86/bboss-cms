/**  
* @Title: BackgroundImage.java
* @Package com.frameworkset.platform.esb.datareuse.common.entity
* @Description: TODO(用一句话描述该文件做什么)
* @Copyright:Copyright (c) 2011
* @Company:bbossgroups
* @author qian.wang
* @date 2011-9-20 上午10:02:45
* @version V1.0  
*/
package com.frameworkset.platform.esb.datareuse.common.entity;


public class BackgroundImage {	
	private String img;
	private String text;
	private String iconCls;
	private boolean leaf;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}
