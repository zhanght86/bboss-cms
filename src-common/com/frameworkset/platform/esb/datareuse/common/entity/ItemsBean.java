package com.frameworkset.platform.esb.datareuse.common.entity;

import java.util.List;

public class ItemsBean {
	private List<MenuItemU> listItems;
	private List<MenuItemU> listModules;
	private String parentId;
	private String parentName;
	private String parentPath;
	
	public List<MenuItemU> getListItems() {
		return listItems;
	}
	public void setListItems(List<MenuItemU> listItems) {
		this.listItems = listItems;
	}
	public List<MenuItemU> getListModules() {
		return listModules;
	}
	public void setListModules(List<MenuItemU> listModules) {
		this.listModules = listModules;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
	
}
