package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.platform.security.AccessControl;

public class ResDictTree extends COMTree implements java.io.Serializable{
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		try {
			DictManager dictManager = new DictManagerImpl();
			
			return dictManager.isContainChildDicttype(treeID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
//		String dicttype_type = request.getParameter("dicttype_type");
//		if(dicttype_type == null 
//				|| (!dicttype_type.equals("0")&&!dicttype_type.equals("1")&&!dicttype_type.equals("2"))){
//			dicttype_type = "-1";
//		}
//		int dicttypeType = 0;
//		try {
//			dicttypeType = Integer.parseInt(dicttype_type);
//		} catch (NumberFormatException e1) {
//			// TODO Auto-generated catch block
//			dicttypeType = 0;
//		}		
		try {
			DictManager dictManager = new DictManagerImpl();
			//设置资源的时候, 只设置 授权业务字典 dicttype_type = POWER_DICTTYPE(7) 包括采集数据 和不采集数据的 2 和 4
			//modify by ge.tao
			//2007-01-15
			List dicttypeList = dictManager.getPartChildDicttypeList(treeID, DictManager.PART_DELETE_SUCCESS);			
			if (dicttypeList != null) {
				Iterator iterator = dicttypeList.iterator();
				while (iterator.hasNext()) {
					Data son = (Data) iterator.next();
					Map map = new HashMap();
					map.put("dicttypeId", son.getDataId().toString());
					map.put("resName", son.getName().trim());
					String resTypeId=request.getParameter("resTypeId");
					String roleId = (String)session.getAttribute("currRoleId");
					String roleTypeId = (String)session.getAttribute("role_type");
					
					String nodeType = "";
					if(AccessControl.hasGrantedRole(roleId,roleTypeId,son.getDataId().trim().toString(),resTypeId)){
                    	nodeType = "org_true";
                    }else{
                    	nodeType = "org";
                    }
					
					if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.WRITE_PERMISSION, AccessControl.DICT_RESOURCE))
					{
						addNode(father, son.getDataId().toString(), son
								.getDescription(), nodeType, true, curLevel,
								(String) null, (String) null, (String) null, map);
					}	
					else if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.READ_PERMISSION, AccessControl.DICT_RESOURCE))
					{
						addNode(father, son.getDataId().toString(),son
								.getDescription(), nodeType, false, curLevel,
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
