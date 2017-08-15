package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;
/**
 * 字典定义  字典类型树.
 * <p>Title: DictTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2008-1-16 10:01:11
 * @author ge.tao
 * @version 1.0
 */
public class DictTree extends COMTree implements java.io.Serializable{
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		//OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
		//String curUserId = super.accessControl.getUserID();
		//List list = orgAdministrator.getManagerOrgsOfUserByID(curUserId);//list.size()>0时，当前用户拥有部门管理员角色
		if(super.accessControl.isAdmin()){//判断是否是部门管理员与拥有超级管理员角色的用户
			try {
				DictManager dictManager = new DictManagerImpl();
				
				return dictManager.isContainChildDicttype(treeID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		String dicttype_type = request.getParameter("dicttype_type");
		if(dicttype_type == null ){
			dicttype_type = "-1";
		}
		try {
			DictManager dictManager = new DictManagerImpl();
			int type = -1;
			try {
				type = Integer.parseInt(dicttype_type);
			} catch (Exception e) {
				type = -1;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List dicttypeList = dictManager.getPartChildDicttypeList(treeID, type);			
			
			if (dicttypeList != null) {
				Iterator iterator = dicttypeList.iterator();
				while (iterator.hasNext()) {
					Data son = (Data) iterator.next();
					String daraId = son.getDataId();
					Map map = new HashMap();
					map.put("dicttypeId", daraId.toString());
					map.put("resName", son.getName().trim());
					//不做任何权限过滤, 全部放开, 直接用菜单权限
//					if(type == DictManager.PARTREAD_BUSINESS_DICTTYPE || type == DictManager.BUSINESS_DICTTYPE_POWERONLY){//只有授权字典和不维护数据的授权字典, 要权限过滤
//						if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.WRITE_PERMISSION, AccessControl.DICT_RESOURCE)){
//							addNode(father, son.getDataId().toString(), son.getDescription().trim(), "org", true, curLevel,
//									(String) null, (String) null, (String) null, map);
//						}else if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.READ_PERMISSION, AccessControl.DICT_RESOURCE)){
//							addNode(father, son.getDataId().toString(),son.getDescription().trim(), "org", false, curLevel,
//									(String) null, (String) null, (String) null, map);
//						}
//					}else{//基础字典 和 通用字典 和其他...
					ITreeNode node = addNode(father, daraId, son.getDescription().trim(), "org", true, curLevel,
							(String) null, (String) null, (String) null, map);
//					}
					Menu dictmenu = new Menu();
            		if(son.isCachable()){
						dictmenu.setIdentity(daraId);
							
	                	Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
	        			sitemenuitem1.setName("缓冲刷新");
	        			sitemenuitem1.setLink("javascript:refreshDict('" + daraId + "');");
	        			sitemenuitem1.setIcon("../images/dialog-reset.gif");
	        			dictmenu.addContextMenuItem(sitemenuitem1);
	        			if(dictmenu.getContextMenuItems().size() > 0){
	        				super.addContextMenuOfNode(node, dictmenu);
	        			}
            		}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
