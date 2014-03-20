package com.sany.mbp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 测试类
 * @author Administrator
 *
 */
public class MobileTree {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 创建一个虚拟的父菜单 用于存放一级菜单 menu01 和 menu02
		 */
//		Menu root = new Menu();
//		/**
//		 * 创建两个一级菜单
//		 */
//		Menu menu01 = new Menu("一级菜单01","","");
//		Menu menu02 = new Menu("一级菜单02","","");
//		/**
//		 * 加入虚拟菜单
//		 */
//		root.addChild(menu01);
//		root.addChild(menu02);
//		/**
//		 * 为两个一级菜单分别添加两个子菜单 并返回该子菜单 需要进一步处理的时候 才接收返回的对象 否则只要调用方法
//		 */
//		Menu menu0101 = menu01.addChild(new Menu("二级菜单0101","",""));
//		menu01.addChild(new Menu("二级菜单0102","",""));
//		menu01.addChild(new Menu("二级菜单0103","",""));
//		menu02.addChild(new Menu("二级菜单0201","",""));
//
//		Menu menu0202 = menu02.addChild(new Menu("二级菜单0202","",""));
//		Menu menu0203 = menu02.addChild(new Menu("二级菜单0203","",""));
//		/**
//		 * 添加三级菜单
//		 */
//		menu0101.addChild(new Menu("三级菜单010101","",""));
//		menu0101.addChild(new Menu("三级菜单010102","",""));
//		menu0202.addChild(new Menu("三级菜单020201","",""));
		/**
		 * 打印树形结构
		 */
		//		showMenu(root);
	}

	/**
	 * 递归遍历某个菜单下的菜单树
	 * 
	 * @param menu
	 *            根菜单
	 */
	private String showMenu(Menu menu) {
		StringBuffer restr = new StringBuffer();
		for (Menu child : menu.getChilds()) {
			restr.append(showMenu(child, 0));
		}
		return restr.toString();
	}

	private String showMenu(Menu menu, int tabNum) {
		StringBuffer restr = new StringBuffer();
		//		for (int i = 0; i < tabNum; i++)
		//			System.out.print("\t");
		if(menu.getChilds().size()>0){
			restr.append("<div data-role=\"collapsible\" data-content-theme=\"c\"><h3>"
					+menu.getTitle()+"</h3>");
			for (Menu child : menu.getChilds()){
				restr.append(showMenu(child, tabNum + 1));// 递归调用
			}
			restr.append("</div>");
		}else{
			restr.append("<a id=\""+menu.getMenuId() +"\" href=\"#\" data-role=\"button\" data-rel=\"back\" "+
					"onclick=\"typeupdate('"+menu.getMenuId()+"')\">"+menu.getTitle()+"</a>");
		}
		return restr.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getMobileTree(List<Menu> treeList){
		Menu root = new Menu();
		HashMap hm = new HashMap();
		List<Menu> noparent = new ArrayList();
		for (Menu menu : treeList){
			hm.put(menu.getMenuId(), menu);
			if("".equals(menu.getParentId())){
				root.addChild(menu);
			}else{
				if(hm.get(menu.getParentId())!=null&&!"".equals(hm.get(menu.getParentId()))){
					Menu menucParent = (Menu)hm.get(menu.getParentId());
					menucParent.addChild(menu);
				}else{
					noparent.add(menu);
				}
			}
		}
		if(noparent.size()>0){
			getMobileTree(noparent,hm);	// 递归调用
		}
		String restr = showMenu(root);
		return restr;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getMobileTree(List<Menu> treeList,HashMap hm){
		List<Menu> noparent = new ArrayList();
		for (Menu menu : treeList){
			if(hm.get(menu.getParentId())!=null&&!"".equals(hm.get(menu.getParentId()))){
				Menu menucParent = (Menu)hm.get(menu.getParentId());
				menucParent.addChild(menu);
			}else{
				noparent.add(menu);
			}
		}
		if(noparent.size()>0){
			getMobileTree(noparent,hm);	// 递归调用
		}
	}

}
