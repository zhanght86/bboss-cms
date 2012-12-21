package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 机构岗位树。显示在角色管理中资源操作授予的岗位和人员资源里面。
 * 
 * @author 
 * @file OrgJobTree.java Created on: Apr 17, 2006
 */
public class OrgJobTree extends COMTree implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		String type = father.getType();
		try {
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			JobManager jobManager = SecurityDatabase.getJobManager();
			Organization org = new Organization();
			org.setOrgId(treeID);
			// 根结点
			if (father.isRoot()) {
				// 判断有没有子机构
				if (OrgCacheManager.getInstance().hasSubOrg(treeID)) {
					return true;
				}
				// 判断是否有岗位
				else {
					return jobManager.isContainJob(org);
				}
			} else {
				if (type.equals("job"))
					return false;
				else if (type.equals("org")) {
					// 判断有没有子机构
					if (OrgCacheManager.getInstance().hasSubOrg(treeID)) {
						return true;
					}
					// 判断是否有岗位
					else {
						return jobManager.isContainJob(org);
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
			
			JobManager jobManager = SecurityDatabase.getJobManager();

			List orgList = OrgCacheManager.getInstance().getSubOrganizations(treeID);
			List jobList = null;
			if(treeID!="0"){
				jobList = jobManager.getJobList(org);
			}
			// 先加子机构
			if (orgList != null) {
				Iterator iterator = orgList.iterator();
				while (iterator.hasNext()) {
					Organization son = (Organization) iterator.next();
					String treeName  = "";
                	if(son.getRemark5() == null || son.getRemark5().trim().equals(""))
                		treeName = son.getOrgName();
                    else                    	
                    	treeName = son.getRemark5();

					Map map = new HashMap();
					map.put("resId", son.getOrgId());
					map.put("resTypeId", "orgunit");
					map.put("resName", treeName);
	                
	               
	                	if (super.accessControl.checkPermission(son.getOrgId(),
								AccessControl.WRITE_PERMISSION,
								AccessControl.ORGUNIT_RESOURCE)) {
							addNode(father, son.getOrgId(),treeName,
									"org", false, curLevel, (String) null,
									(String) null, (String) null, map);
						} else {
							if (accessControl.checkPermission(son.getOrgId(),
									AccessControl.READ_PERMISSION,
									AccessControl.ORGUNIT_RESOURCE)) {
								addNode(father, son.getOrgId(), treeName,
										"org", false, curLevel, (String) null,
										(String) null, (String) null, map);
							}
						}
	              
				}
					
				
			}
			
			
			
			
			// 加岗位
			if (jobList != null) {
				Iterator iterator = jobList.iterator();
				while (iterator.hasNext()) {
					Job son = (Job) iterator.next();
					Map map = new HashMap();
					map.put("resId", org.getOrgId() + ":" + son.getJobId());
					map.put("orgId",org.getOrgId());
					map.put("resTypeId", "job");
					//2006-04-28
					map.put("resName",son.getJobName());
					//
					addNode(father, org.getOrgId() + ":" + son.getJobId(), son.getJobName(), "job",
							true, curLevel, (String) null, (String) null,
							(String) null, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
