package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;


public class BrowseResList extends DataInfoImpl implements Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		
		ResManager rsManager = new ResManagerImpl(); 
		String auto = request.getParameter("auto");
		String orgId = (String)request.getParameter("orgId");
		String userId = (String)request.getParameter("userId");
		
		
		String name = (String)request.getParameter("name");
		
		
		String restypeId = (String)request.getParameter("restypeId");		
		String resId = (String)request.getParameter("resId");
		String resName = (String)request.getParameter("resName");
		String opId = (String)request.getParameter("opId");
		String orgjob = (String)request.getParameter("orgjob");

		
		String special = (String)request.getParameter("special");

		//String id = (String)request.getParameter("Id");
										
		String id = "";
		String type = "";
	
		if(userId==null)userId="";
		if(orgId==null)orgId="";
		if(userId.equals("null"))userId="";
		if(orgId.equals("null"))orgId="";
		
	
		 if(!(userId == "")){  
			id = userId;
			type = "user";
		}else if(!(orgId == "")){
			id = orgId;
			type = "org";
		}else{
			type = (String)request.getParameter("type");
			try{
				if("user".equals(type)){
					UserManager userManager = SecurityDatabase.getUserManager();
					User user = userManager.getUserByName(name);
					if(user != null)
					{
						id = user.getUserId().toString();
					}
				}
				if("org".equals(type)){
					OrgManager orgManager = SecurityDatabase.getOrgManager();
					String orgid__ = request.getParameter("orgid__"); 
					if(orgid__ != null && !"".equals(orgid__)){
						
//						name = name.split(" ")[1];
						id = orgid__;
					}
//					Organization org = orgManager.getOrgByName(name);
//					if(org != null)
//					{
//						id = org.getOrgId();
//					}
				}
				if("role".equals(type)){
					RoleManager roleManager = SecurityDatabase.getRoleManager();
					
					Role role = roleManager.getRoleByName(name);
					if(role != null)
					{
						id = role.getRoleId();
					}
				}
				if("orgjob".equals(type))
				{
					id = orgjob;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		 
		if(id == null)
			id = "";
			
		ListInfo listInfo = new ListInfo();
		
		try{
//			当unprotected参数有值的时候，查询未受保护的资源；
			if("unprotected".equals(special)){
				listInfo = rsManager.getUnprotectedRes(restypeId,resId,resName,opId);
			}else if("exclude".equals(special)){
				listInfo = rsManager.getAdminRes(restypeId,resId,resName,opId);
			}else if(id.length() == 0 && "".equals(id) ){
				return listInfo;
			}else{
				Map paramMap = new HashMap();
				paramMap.put("type", type);
				paramMap.put("id", id);
				paramMap.put("name", name);
				paramMap.put("resId", resId);
				paramMap.put("resName", resName);
				paramMap.put("restypeId", restypeId);
				paramMap.put("opId", opId);
				paramMap.put("auto", auto);
				listInfo = rsManager.getResList(paramMap,offset,maxPagesize);
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		return listInfo;
		

	}
	
	
	
	protected ListInfo getDataList(String sortKey, boolean desc) {
		return getDataList(sortKey, desc, 0,0);
	}
	
}






