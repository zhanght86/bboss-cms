package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Restype;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 资源管理中其他资源的树行展示 (角色管理->资源角色授予->其他类型的资源)
 * 
 * @author feng.jing
 * @file OtherTree.java Created on: Mar 23, 2006
 */
public class OtherTree extends COMTree implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		String type = father.getType();
		try {
			ResManager resManager = SecurityDatabase.getResourceManager();
			if (father.isRoot()) {
				// 判断有没有子类型
				Restype restype = new Restype();
				restype.setRestypeId(treeID);
				Res res = new Res();
				res.setRestypeId(treeID);
				if (resManager.isContainChildResType(restype)) {
					return true;
				}
				// 判断是否有资源
				else {
					return resManager.isContainChildRes(res);
				}
			} else {
				if (type.equals("resource"))
					return false;
				else if (type.equals("resourceType")) {

					Restype restype = new Restype();
					restype.setRestypeId(treeID);
					Res res = new Res();
					res.setRestypeId(treeID);
					// 判断有没有子类型
					if (resManager.isContainChildResType(restype)) {
						return true;
					}
					// 判断是否有资源
					else {
						return resManager.isContainChildRes(res);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();		
		
		
		try {
			ResManager resManager = SecurityDatabase.getResourceManager();			
			Restype restype = new Restype();
			restype.setRestypeId(treeID);
			Res res = new Res();
			res.setRestypeId(treeID);
			
			List resList = resManager.getChildResList(res);
			List resTypeList = resManager.getChildResTypeList(restype);
			//先加子类型
			if (resTypeList != null) {
				Iterator iterator = resTypeList.iterator();
				while (iterator.hasNext()) {
					Restype sonRes = (Restype) iterator.next();
					Map map = new HashMap();					
					map.put("resTypeId", sonRes.getRestypeId());
					addNode(father, sonRes.getRestypeId(), sonRes.getRestypeName(),
							"resourceType", false, curLevel, (String) null,
							(String) null, (String) null, map);
				}
			}
			//加资源资源
			if (resList != null) {
				Iterator iterator = resList.iterator();
				while (iterator.hasNext()) {
					Res sonRes = (Res) iterator.next();
					Map map = new HashMap();
					map.put("resId", sonRes.getResId());
					map.put("resTypeId", sonRes.getRestype().getRestypeId());
					addNode(father, sonRes.getResId(), sonRes.getTitle(),
							"resource", true, curLevel, (String) null,
							(String) null, (String) null, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
