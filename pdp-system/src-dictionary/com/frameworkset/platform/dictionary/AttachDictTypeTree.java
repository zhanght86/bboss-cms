/**
 * 业务类型字典树
 */
package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;

/**
 * 获取能维护附加字段的所有字典类型
 * <p>Title: BusinessDictTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-6 17:41:30
 * @author ge.tao
 * @version 1.0
 */
public class AttachDictTypeTree extends COMTree implements java.io.Serializable{
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		try {
			DictManager dictManager = new DictManagerImpl();
			if(!super.accessControl.isAdmin()){
				return false;
			}
			return dictManager.isContainAttachDictType(treeID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		try {
			DictManager dictManager = new DictManagerImpl();
			List dicttypeList = dictManager.getAttachDictTypeList(treeID);			
			if (dicttypeList != null) {
				Iterator iterator = dicttypeList.iterator();
				while (iterator.hasNext()) {
					//过滤 第四种类型字典 不维护数据的字典类型					
					Data son = (Data) iterator.next();
					if(son.getDicttype_type()==DictManager.BUSINESS_DICTTYPE_POWERONLY ||
					   son.getDicttype_type()==DictManager.BUSINESS_DICTTYPE_USUALONLY){
						continue;
					}
					Map map = new HashMap();
					map.put("dicttypeId", son.getDataId().toString());
//					map.put("groupName", son.getDicttypeName());
//					if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.WRITE_PERMISSION, AccessControl.DICT_RESOURCE))
//					{
					//不做字典资源权限过滤 但是 不显示 不维护数据的字典类型
						addNode(father, son.getDataId().toString(), son.getDescription().trim(), "org", true, curLevel,
								(String) null, (String) null, (String) null, map);
//					}else if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.READ_PERMISSION, AccessControl.DICT_RESOURCE)){
//						addNode(father, son.getDataId().toString(),son
//								.getDescription().trim(), "org", false, curLevel,
//								(String) null, (String) null, (String) null, map);
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

