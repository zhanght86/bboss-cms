package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class ResTree extends COMTree implements Serializable{
	public boolean hasSon(ITreeNode father) {
		String resID = father.getId();
		try {
			Res res = new Res();
			res.setResId(resID);
			if (resID.equals("0")) {
				return true;
			} else
				return false;
			// return role.isContainChildRole(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String resID = father.getId();
		String roleId = (String)session.getAttribute("currRoleId");
		String roleTypeId = (String)session.getAttribute("role_type");
		try {
			ResManager resManager = SecurityDatabase.getResourceManager();
			Res res = new Res();
			res.setResId(resID);
			
			List roleList = resManager.getResList("select * from td_sm_Res res where res.restypeId<>'site' order by res.restypeId");
			if (roleList != null) {
				Iterator iterator = roleList.iterator();
				while (iterator.hasNext()) {
					Res sonrole = (Res) iterator.next();
					Map map = new HashMap();
					
					map.put("resId", sonrole.getResId());
					map.put("resName", sonrole.getTitle());
					map.put("restypeId",sonrole.getRestypeId());
					if(sonrole.getPath()==null || sonrole.getPath().equals("")){
						map.put("resPath",sonrole.getRestypeId());
					}else{
						map.put("resPath",sonrole.getPath());
					}
                    String nodeType = "org";
                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,sonrole.getTitle(),sonrole.getRestypeId())){
                    	nodeType = "org_true";
                    }else{
                    	nodeType = "org";
                    }
					if (super.accessControl.checkPermission(sonrole.getTitle(),
							AccessControl.WRITE_PERMISSION,
							sonrole.getRestypeId())) {
						addNode(father, sonrole.getResId(), sonrole.getPath()
								, nodeType, true, curLevel,
								(String) null, (String) null, (String) null,
								map);
					}
					else if (super.accessControl.checkPermission(sonrole.getTitle(),
							AccessControl.READ_PERMISSION,
							sonrole.getRestypeId())) {
						addNode(father, sonrole.getResId(), sonrole.getPath()
								, nodeType, false, curLevel,
								(String) null, (String) null, (String) null
					,
								map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}


