package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * gao.tang
 * 显示机构下的用户树
 * @author Administrator
 *
 */
public class ChargeUserTree extends COMTree implements Serializable{

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();// 得到树ID
		
		String type = father.getType();// 得到类型
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			Organization org = new Organization();
			org.setOrgId(treeID);
			
			if(father.isRoot()){
				//判断有没有子机构
				if (OrgCacheManager.getInstance().hasSubOrg(treeID)) {
					return true;
				}
				//判断是否有用户
				else {
						return userManager.isContainUser(org);
				}
			} else {
				if (type.equals("user"))
					return false;
				else if (type.equals("org")) {
					// 判断有没有子机构
					if (OrgCacheManager.getInstance().hasSubOrg(treeID)) {
						return true;
					}
					// 判断是否有岗位
					else {
						return userManager.isContainUser(org);
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
		Organization org = new Organization();
		org.setOrgId(treeID);
		try {
			
			UserManager userManager = SecurityDatabase.getUserManager();

			List orgList = OrgCacheManager.getInstance().getSubOrganizations(treeID);
			List userList = userManager.getUserList(org);
			// 先加子机构
			if (orgList != null) {
				for(int i = 0; i < orgList.size(); i ++)
				{
					Organization son = (Organization) orgList.get(i);
					String treeName  = "";
                	if(son.getRemark5() == null || son.getRemark5().trim().equals(""))
                		treeName = son.getOrgName();
                    else                    	
                    	treeName = son.getRemark5();

					Map map = new HashMap();
					map.put("resId", son.getOrgId());
					map.put("resTypeId", "orgunit");
					map.put("resName", treeName);

	                	if (super.accessControl.isOrganizationManager(son.getOrgId()) ||
	                			super.accessControl.isAdmin()) {
							addNode(father, 
									son.getOrgId().trim(),
									treeName.trim(),
									"org", 
									false, 
									curLevel, 
									(String) null,
									(String) null, 
									(String) null, 
									map);
						} else {
							if (super.accessControl.isSubOrgManager(son.getOrgId())) {
								addNode(father,
										son.getOrgId().trim(), 
										treeName.trim(),
										"org", 
										false, 
										curLevel, 
										(String) null,
										(String) null, 
										(String) null, 
										map);
							}
						}
				}
			}			
			
			// 加用户
			if(super.accessControl.isOrganizationManager(father.getId()) ||
        			super.accessControl.isAdmin())
			{
				if (userList != null) {
					Iterator iterator = userList.iterator();
					while (iterator.hasNext()) {
						User son = (User) iterator.next();
						Map map = new HashMap();
						map.put("userId",son.getUserId());
						map.put("orgId",org.getOrgId());
						map.put("resTypeId", "user");

						addNode(father, 
								"u" + String.valueOf(son.getUserId()).trim(), 
								son.getUserName().trim()+" "+son.getUserRealname().trim(), 
								"user",
								true, 
								curLevel, 
								(String) null, 
								son.getUserName(),
								son.getUserName(), 
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
