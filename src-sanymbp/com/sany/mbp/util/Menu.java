package com.sany.mbp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单类
 * @author Administrator
 *
 */
public class Menu {
	/**
	 * 菜单标题
	 */
	private String title;
	/**
	 * 菜单ID
	 */
	private String menuId;
	/**
	 * 菜单父ID，用于生成树结构
	 */
	private String parentId;
	
	/**
	 * 子菜单的集合
	 */
	private List<Menu> childs;

	/**
	 * 父菜单
	 */
	private Menu parent;

	/**
	 * 构造函数 初始化标题和子菜单集合
	 * @param menuId 
	 * @param parentId 
	 */
	public Menu(String title, String menuId, String parentId) {
		this();
		this.title=title;
		this.menuId=menuId;
		this.parentId=parentId;
	}
	/**
	 * 构造函数 创建一个虚拟的父菜单(零级菜单) 所有的一级菜单都归属于一个虚拟的零级菜单
	 *
	 */
	public Menu() {
		this.childs = new ArrayList<Menu>();
	}
	/**
	 * 获取子菜单
	 * @return
	 */
	public List<Menu> getChilds() {
		return childs;
	}
	/**
	 * 获取标题
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 获取父菜单
	 * @return
	 */
	public Menu getParent() {
		return parent;
	}
	/**
	 * 添加子菜单并返回该子菜单对象
	 * @param child
	 * @return
	 */
	public Menu addChild(Menu child){
		this.childs.add(child);
		return child;
	}
	/**
	 * 设置父菜单
	 * @param parent
	 */
	public void setParent(Menu parent) {
		this.parent = parent;
	}
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}