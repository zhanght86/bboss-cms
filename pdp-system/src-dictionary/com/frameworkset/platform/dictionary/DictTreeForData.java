package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;
/**
 * 所有字典类型 数据采集页面的 左边字典类型树的实现类
 * 通过request.getParameter("dicttype_type")来区别不同的字典类型
 * 其中对于: 基础类型和通用字典类型, 不用过字典资源的权限过滤
 * <p>Title: DictTreeForData.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2008-1-15 17:24:35
 * @author ge.tao
 * @version 1.0
 */
public class DictTreeForData extends COMTree implements java.io.Serializable{
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		String dicttype_type = request.getParameter("dicttype_type");
		if("2".equals(dicttype_type)){//授权业务数据字典部门管理员与拥有超级管理员角色的用户能看到
			OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
			String curUserId = super.accessControl.getUserID();
			List list = orgAdministrator.getManagerOrgsOfUserByID(curUserId);//list.size()>0时，当前用户拥有部门管理员角色
			if(list.size() > 0 || super.accessControl.isAdmin()){//判断是否是部门管理员与拥有超级管理员角色的用户
				try {
					DictManager dictManager = new DictManagerImpl();
					return dictManager.isContainChildDicttype(treeID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(super.accessControl.isAdmin()){//判断是否是部门管理员与拥有超级管理员角色的用户
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
		int dicttypeType = 0;
		try {
			dicttypeType = Integer.parseInt(dicttype_type);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			dicttypeType = 0;
		}
		try {
			DictManager dictManager = new DictManagerImpl();
			//modify by ge.tao
			//2007-01-15			
			List dicttypeList = dictManager.getPartChildDicttypeList(treeID,dicttypeType);

			if (dicttypeList != null) {
				Iterator iterator = dicttypeList.iterator();
				while (iterator.hasNext()) {
					Data son = (Data) iterator.next();
					Map map = new HashMap();
					map.put("dicttypeId", son.getDataId().toString());
					map.put("resName", son.getName().trim());
					
					if(dicttypeType == DictManager.PARTREAD_BUSINESS_DICTTYPE){//只有授权字典, 采集的时候才需要做资源权限过滤
						if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.WRITE_PERMISSION, AccessControl.DICT_RESOURCE)){
							addNode(father, son.getDataId().toString(), son
									.getDescription().trim(), "org", true, curLevel,
									(String) null, (String) null, (String) null, map);
						}else if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.READ_PERMISSION, AccessControl.DICT_RESOURCE)){
							addNode(father, son.getDataId().toString(), son
									.getDescription().trim(), "org", false, curLevel,
									(String) null, (String) null, (String) null, map);
						}
					}else if(dicttypeType != DictManager.BUSINESS_DICTTYPE_POWERONLY && dicttypeType != DictManager.BUSINESS_DICTTYPE_USUALONLY){//其他类型的字典 除了不维护数据的两种类型 其他都允许采集
						addNode(father, son.getDataId().toString(), son
								.getDescription().trim(), "org", true, curLevel,
								(String) null, (String) null, (String) null, map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
