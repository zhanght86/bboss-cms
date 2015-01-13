package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;


/**
 * 
 * <p>
 * Title:
 * </p>
 * 
 * <p> 
 * Description: 用户岗位管理Action
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author feng.jing
 * @version 1.0
 */
public class UserJobManagerAction   implements Serializable {

	private static Logger logger = Logger.getLogger(UserJobManagerAction.class
			.getName());

	public UserJobManagerAction() {
	}

	 
	/**隶属岗位－－保存用户岗位机构的关系
	 * store and delete userOrgJob by ajax the reference page is refresh auto (hongyu.deng)
	 * @param uid
	 * @param orgId
	 * @param jobid
	 * @return
	 */
	public static String storeUserOrgJob(Integer uid,String orgId,String[] jobid){
		
//			System.out.println("uid..........."+uid);
//			System.out.println("orgId............"+orgId);

			try {
				UserManager userManager = SecurityDatabase.getUserManager();
				return userManager.storeUserOrgJob(uid, orgId, jobid);
					
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		
	}
	/**
	 * 存储人员岗位和机构的关系，用户管理隶属机构中的调入
	 */
	public static String storeUJOAjax(String uid, String[] jobIds,String orgId) {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			return userManager.storeUJOAjax(uid, jobIds, orgId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		

		return "success";
	}
	/**隶属岗位－－删除用户岗位机构的关系
	 * store and delete userOrgJob by ajax the reference page is refresh auto (hongyu.deng)
	 * @param uid
	 * @param orgId
	 * @param jobid
	 * @return
	 */
	public static String deleteUserOrgJob(Integer uid,String orgId,String[] jobid){
	
	try {
		UserManager userManager = SecurityDatabase.getUserManager();
		return userManager.deleteUserOrgJob(uid, orgId, jobid);
	} catch (Exception e) {
		e.printStackTrace();
		return "fail";
	}
	}
	/**
	 * 删除人员岗位和机构的关系，用户管理隶属机构中的调入
	 */
	public static String deleteUJOAjax(String uid, String[] jobIds,String orgId) {
     
       try
       {
			UserManager userManager = SecurityDatabase.getUserManager();
			return userManager.deleteUJOAjax(uid,jobIds,orgId);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	 
	public void storeUJOAjax_batch(String[] ids, String[] jobid,String orgid) throws ManagerException, SPIException {
	
		UserManager userManager = SecurityDatabase.getUserManager();
		try {
			userManager.storeUJOAjax_batch(ids, jobid, orgid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId) throws ManagerException {
		
		
		try {
			
			UserManager userManager = SecurityDatabase.getUserManager();
			userManager.deleteUJOAjax_batch(ids, jobid, orgId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
