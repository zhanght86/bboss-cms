package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class ChargeOrgJobTree extends COMTree implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		String type = father.getType();
		try {
		
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
		String displayNameInput = request.getParameter("displayNameInput");
		String displayValueInput = request.getParameter("displayValueInput");
		String displayNameInput1 = request.getParameter("displayNameInput1");
		String displayValueInput1 = request.getParameter("displayValueInput1");

		String treeID = father.getId();
		Organization org = new Organization();
		org.setOrgId(treeID);
		try {
			
			JobManager jobManager = SecurityDatabase.getJobManager();

			List orgList = OrgCacheManager.getInstance().getSubOrganizations(treeID);
			List jobList = jobManager.getJobList(org);
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
					map.put("displayNameInput", displayNameInput);
					map.put("displayValueInput", displayValueInput);
					map.put("displayNameInput1", displayNameInput1);
					map.put("displayValueInput1", displayValueInput1);

	                	if (super.accessControl.isOrganizationManager(son.getOrgId()) ||
	                			super.accessControl.isAdmin()) {
							addNode(father, son.getOrgId(),treeName,
									"org", false, curLevel, (String) null,
									(String) null, (String) null, map);
						} else {
							if (super.accessControl.isSubOrgManager(son.getOrgId())) {
								addNode(father, son.getOrgId(), treeName,
										"org", false, curLevel, (String) null,
										(String) null, (String) null, map);
							}
						}
	              
				}
					
				
			}
			
			
			
			
			// 加岗位
			if (super.accessControl.isOrganizationManager(father.getId()) ||
        			super.accessControl.isAdmin()){
				if (jobList != null) {
					Iterator iterator = jobList.iterator();
					while (iterator.hasNext()) {
						Job son = (Job) iterator.next();
						Map map = new HashMap();
						map.put("jobId",son.getJobId());
						map.put("orgId",org.getOrgId());
						map.put("resTypeId", "job");
						//2006-04-28
						map.put("jobName",son.getJobName());
						//
						map.put("displayNameInput", displayNameInput);
						map.put("displayValueInput", displayValueInput);
						map.put("displayNameInput1", displayNameInput1);
						map.put("displayValueInput1", displayValueInput1);
	
						addNode(father, "j" + son.getJobId(), son.getJobName(), "job",
								true, curLevel, (String) null, father.getId() + ":" +  son.getJobId() + "$" + father.getName() + ":" +  son.getJobName(),
								father.getId() + ":" +  son.getJobId() + "$" + father.getName() + ":" +  son.getJobName(), map);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
