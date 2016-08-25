package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgrole;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.util.EventUtil;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
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
 * @author hongyu.deng
 * @version 1.0
 */
public class OrgManAction   {
	private Logger logger = Logger.getLogger(OrgManAction.class.getName());

	public OrgManAction() {
	}

//	public ActionForward newsubOrg(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		Organization org = (Organization) form;
//		String parentId = org.getOrgId();
//		
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		Organization parentOrg = new Organization();
//		parentOrg.setOrgId(parentId);
//       
//		List sublist = orgManager.getChildOrgList(parentOrg);
//		String suborgstr = "";
//		String suborgremark5 = "";//子机构显示名称列表
//		if (sublist != null && sublist.size() > 0) {
//			for (int i = 0; i < sublist.size(); i++) {
//				Organization suborg = (Organization) sublist.get(i);
//				suborgstr += suborg.getOrgName() + ",";
//				suborgremark5 += suborg.getRemark5() + ",";
//			}
//		}
//		if (!suborgstr.equals(""))
//			suborgstr = suborgstr.substring(0, suborgstr.length() - 1);
//		
//		if (!suborgremark5.equals(""))
//			suborgremark5 = suborgremark5.substring(0, suborgremark5.length() - 1);
//		if ((parentId == null) || parentId.equals("")) {
//			parentId = "0";
//		}
//		Organization org1 = orgManager.getOrgById(org.getOrgId());	
//	    String orgNumber=org1.getOrgnumber();       
//	    request.setAttribute("orgNumber",orgNumber);
//		String layer = org1.getLayer();
//		//如果父机构的layer为空，默认指定为2
//		if ((layer == null) || layer.equals("") || "null".equals(layer)) {
//			layer = "2";
//		} else {
//			layer = String.valueOf(Integer.parseInt(layer) + 1);
//		}
//	
//		org = new Organization();
//		// 修改：为了同步机构到LDAP中去
//		org.setParentId(parentId);
//		org.setParentOrg(parentOrg);
//		// 修改结束
//
//		org.setLayer(layer);
//		request.removeAttribute(mapping.getInput());
//		HttpSession session = request.getSession();
//		session.setAttribute("suborgstr", suborgstr);	
//		session.setAttribute("suborgremark5",suborgremark5);
//		request.setAttribute("Organization", org);
//		if("iscms".equals(request.getParameter("iscms")))
//			return (mapping.findForward("newCMSsubOrg"));
//		return (mapping.findForward("newsubOrg"));
//	}

//	public ActionForward savesubOrg(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//		Organization org = (Organization) form;
//		
//		DBUtil db = new DBUtil();
//		db.executeSelect("select orgnumber from TD_SM_ORGANIZATION where orgnumber='"+ org.getOrgnumber()+"'");
//		if(db.size()>0){
//			request.setAttribute("isExist","true");
//			return mapping.findForward("neworg");
//		}		
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		JobManager jobManager = SecurityDatabase.getJobManager();
//		org.setOrgId(null);
//
//		// 添加：为了实现与LDAP同步时将当前机构添加到父机构的 member 属性下
//		if (org.getParentId() != null && org.getParentId().length() > 0) {
//			Organization parentOrg = orgManager.getOrgById( org
//					.getParentId());
//			org.setParentOrg(parentOrg);
//		} else
//			org.setParentId("0");
//		//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
//		
//		boolean r = orgManager.insertOrg(org);
//		if(!r)
//		{
//			return mapping.findForward("fail");
//		}
//		org = orgManager.getOrgByName(org.getOrgName());
//		
////		//新增子机构重新加载缓冲
////			OrgCacheManager.getInstance().reset();
//			
//		// 王卓添加，添加机构时添加一个待岗岗位
//		
//		Orgjob oj = new Orgjob();
//		Job job = new Job();
//		job.setJobId("1");
//		oj.setJob(job);
//		oj.setJobSn(Integer.valueOf("999"));
//		Organization org1 = new Organization();
//		org1.setOrgId(org.getOrgId());
//		request.setAttribute("orgId",org.getOrgId());
//		oj.setOrganization(org1);
////		try{
////				orgManager.deleteOrgjob(oj);
////			}catch(Exception e)
////				{
////					e.printStackTrace();
////				}
//		orgManager.storeOrgjob(oj);
//		
//		//--记日志---------
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=request.getRemoteAddr();
//        String openModle="机构管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 		
//		operContent=userName+" 新增了子机构 "+org.getOrgName(); 
//		 description="";
//        logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
//		//----------------------
//		
//		//新增机构时给机构添加人赋权
//		AccessControl accesscontroler = AccessControl.getInstance();
//        accesscontroler.checkAccess(request, response);
//        String roleid = accesscontroler.getUserID();
//        String resid = org1.getOrgId();
//        String resname = org.getOrgName();
////      String opid1 = "write";
////      String opid2 = "read";
//      
//      String resTypeid = "orgunit";
//      ResourceManager resManager = new ResourceManager();
//      List oplist = resManager.getOperations(resTypeid);
//      String[] opids = new String[oplist.size()];
//      Operation op = null;
//		
//		for(int i=0;i<oplist.size();i++){
//			op = (Operation)oplist.get(i);
//			opids[i] = op.getId();
//		}
//      RoleManager roleManager = SecurityDatabase.getRoleManager();
////  	roleManager.storeRoleresop(opid1,resid,roleid,resTypeid,resname,"user");
////  	roleManager.storeRoleresop(opid2,resid,roleid,resTypeid,resname,"user");
//      
//      //modify by xinwang.jiao 2007-9-17 批量操作
//      roleManager.storeRoleresop(opids,resid,roleid,resTypeid,resname,"user");
//      	
//      /*
//       * 危达
//       * 200711071037
//       * 如果创建人是机构的上级部门管理员，不做操作
//       * 如果创建人是系统管理员，不做操作
//       * 如果不是，则给创建人赋予本级机构部门管理员身份
//      */
//      String userId = accesscontroler.getUserID();
//      String orgId = org.getOrgId();
//      OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
//      List list = orgAdministrator.getAllAdministorsOfOrg(orgId);
//      boolean tag = true;
//      if(list != null){
//      	for(int i=0;i<list.size();i++){
//          	User user = (User)list.get(i);
//          	if(user != null){
//          		if(user.getUserId().equals(userId)){
//              		tag = false;
//              	}
//          	}          	
//          }
//      }    
//      if(accesscontroler.isAdmin())
//      {
//      	tag = false;
//      }
//      if(tag){
//      	orgAdministrator.addOrgAdmin(userId, orgId);
//      }
//      
//        StringBuffer path = new StringBuffer("/sysmanager/orgmanager/savesuccess.jsp");
//		
//		path.append("?orgId=" + org.getOrgId()
//				+ "&action=update&parentId=" + org.getParentId());
//		return new ActionForward(path.toString());
//	}
	
//	public ActionForward saveOrg(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//	
//        
//		Organization org = (Organization) form;
//		String iscms = request.getParameter("iscms") ;
//		DBUtil db = new DBUtil();
//		db.executeSelect("select orgnumber from TD_SM_ORGANIZATION where orgnumber='"+ org.getOrgnumber()+"'");
//		if(db.size()>0){
//			request.setAttribute("isExist","true");
//			if("cms".equals(iscms)){
//				return mapping.findForward("newCMSsubOrg");
//			}else{
//				return new ActionForward("/sysmanager/orgmanager/new_org.jsp");
//			}
//			
//		}
//		//新建一级机构时，  判断机构名称是否存在重名
//		//baowen.liu 2008-4-23
//		db.executeSelect("select org_name from TD_SM_ORGANIZATION where org_name='" + org.getOrgName()+"'");
//		if(db.size()>0){
//			request.setAttribute("isExistOrgName","true");
//			if("cms".equals(iscms)){
//				return mapping.findForward("newCMSsubOrg");
//			}else{
//				return new ActionForward("/sysmanager/orgmanager/new_org.jsp");
//			}
//			
//		}
//		
//        //新建一级机构时，  判断机构显示名称是否存在重名，新建子机构时不操作！
//		//baowen.liu 2008-4-23
//		 String flag=request.getParameter("suborg");
//         flag= flag==null ? "" : flag;
//         if("sub".equals(flag)){
//		db.executeSelect("select remark5 from TD_SM_ORGANIZATION where remark5='" + org.getRemark5() + "' and parent_id='0'");
//		if(db.size()>0){
//			request.setAttribute("isExistRemark5","true");
//			if("cms".equals(iscms)){
//				return mapping.findForward("newCMSsubOrg");
//			}else{
//				
//			}
//			
//		}
//       }
// 
//    	OrgManager orgManager = SecurityDatabase.getOrgManager();
//		JobManager jobManager = SecurityDatabase.getJobManager();
//		org.setOrgId(null);
//
//		// 添加：为了实现与LDAP同步时将当前机构添加到父机构的 member 属性下
//		if (org.getParentId() != null && org.getParentId().length() > 0) {
//			Organization parentOrg = orgManager.getOrgById(org
//					.getParentId());
//			org.setParentOrg(parentOrg);
//		} else
//			org.setParentId("0");
//		// 添加结束
//		
//		boolean r = orgManager.insertOrg(org);
//		if(!r)
//		{
//			return mapping.findForward("fail");
//		}
//		org = orgManager.getOrgByName(org.getOrgName());
//		
//		// 王卓添加，添加机构时添加一个待岗岗位		
//		Orgjob oj = new Orgjob();
//		Job job = new Job();
//		job.setJobId("1");
//		oj.setJob(job);
//		oj.setJobSn(Integer.valueOf("999"));
//		Organization org1 = new Organization();
//		org1.setOrgId(org.getOrgId());
//		request.setAttribute("orgId",org.getOrgId());
//		oj.setOrganization(org1);
//		
//		//保存机构和岗位关系,方法中已经判断机构和岗位的关系是否存在,如果存在就是更新,否则就是新增
//		orgManager.storeOrgjob(oj);
//		
//		//--记日志-----------------
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=request.getRemoteAddr();
//        String openModle="机构管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 	
//		operContent=userName+" 新增了机构 "+org.getOrgName(); 
//		 description="";
//        logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);          
//		//-----------------------------
//		
//        
//		AccessControl accesscontroler = AccessControl.getInstance();
//        accesscontroler.checkAccess(request, response);
//        
////		//新增机构时给机构添加人赋权
////        String roleid = accesscontroler.getUserID();
////        String resid = org1.getOrgId();
////        String resname = org.getOrgName();
//////      String opid1 = "write";
//////      String opid2 = "read";        
////        String resTypeid = "orgunit";
////        ResourceManager resManager = new ResourceManager();
////        List oplist = resManager.getOperations(resTypeid);
////        String[] opids = new String[oplist.size()];
////        Operation op = null;		
////		for(int i=0;i<oplist.size();i++){
////			op = (Operation)oplist.get(i);
////			opids[i] = op.getId();
////		}
////        RoleManager roleManager = SecurityDatabase.getRoleManager();
//////    	roleManager.storeRoleresop(opid1,resid,roleid,resTypeid,resname,"user");
//////    	roleManager.storeRoleresop(opid2,resid,roleid,resTypeid,resname,"user");        
//////      modify by xinwang.jiao 2007-9-17 批量操作
////        roleManager.storeRoleresop(opids,resid,roleid,resTypeid,resname,"user");
//        
//        /*
//         * 危达
//         * 200711071037
//         * 如果创建人是机构的上级部门管理员，不做操作
//         * 如果创建人是系统管理员，不做操作
//         * 如果不是，则给创建人赋予本级机构部门管理员身份
//        */
//        String userId = accesscontroler.getUserID();
//        String orgId = org.getOrgId();
//        OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
//        List list = orgAdministrator.getAllAdministorsOfOrg(orgId);
//        boolean tag = true;
//        if(list != null){
//        	for(int i=0;i<list.size();i++){
//            	User user = (User)list.get(i);
//            	if(user.getUserId().equals(userId)){
//            		tag = false;
//            	}
//            }
//        }
//        if(accesscontroler.isAdmin())
//        {
//        	tag = false;
//        }
//        if(tag){
//        	orgAdministrator.addOrgAdmin(userId, orgId);
//        }
//
//    	StringBuffer path = new StringBuffer("");
//    	if("iscms".equals(request.getParameter("iscms")))
//    		path.append("/sysmanager/orgmanager/savesuccess_refresh.jsp");
//    	else
//    		path.append("/sysmanager/orgmanager/savesuccess.jsp");
//		
//		
//			path.append("?orgId=" + org.getOrgId()
//					+ "&action=update&parentId=" + org.getParentId());
//		return new ActionForward(path.toString());
//	}
	
//	public ActionForward updateOrg(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		Organization org = (Organization) form;
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		request.setCharacterEncoding("GBK");
//		
//		String iscms = request.getParameter("iscms");
//		
//		String orgnumber = request.getParameter("orgNumber");
//		
//		// 增加：为了实现与LDAP同步时将当前机构添加到父机构的 member 属性下
//		if (org.getParentId() != null) {
//			Organization parentOrg = orgManager.getOrgById(org.getParentId());
//			org.setParentOrg(parentOrg);
//		}
//		// 结束
//		DBUtil db = new DBUtil();
//		String sql ="select orgnumber from TD_SM_ORGANIZATION where" +
//		" orgnumber='"+ org.getOrgnumber()+"' and orgnumber<>'"+ orgnumber +"'";
//		db.executeSelect(sql);
//		
//		if(db.size()>0)
//		{
//			request.setAttribute("isExist","true");
//			request.setAttribute("updateOrgInfo", org);
//			
//			if("iscms".equals(iscms))
//			{
//				return mapping.findForward("updateCMSOrgInfo");
//			}
//			else
//			{
//				return mapping.findForward("updateOrgInfo");
//			}
//			
//		}	
//		
//		//更新机构时，判断机构名称是否存在重名
//		//baowen.liu 2008-4-23
//		db.executeSelect("select ORG_NAME from TD_SM_ORGANIZATION where ORG_NAME='" 
//								+ org.getOrgName()+"' and ORG_ID <> '"+ org.getOrgId() +"'");
//		if(db.size()>0)
//		{
//			request.setAttribute("isExistOrgName","true");
//			//对cms系统处理
//			if("iscms".equals(iscms))
//			{
//				return mapping.findForward("updateCMSOrgInfo");
//			}
//			
//		}
//		//更新机构时，  判断机构显示名称是否存在重名
//		//baowen.liu 2008-4-23
//		db.executeSelect("select REMARK5 from TD_SM_ORGANIZATION where REMARK5 = '" 
//							+ org.getRemark5() + "' and PARENT_ID = '" + org.getParentId() + "' and ORG_ID <>'"
//							+ org.getOrgId()+ "'");
//		
//		if(db.size() > 0)
//		{
//			request.setAttribute("isExistRemark5","true");
//			if("iscms".equals(iscms))
//			{
//				return mapping.findForward("updateCMSOrgInfo");
//			}
//		}
//		Organization org_old = (Organization) form;	  
//		org_old = orgManager.getOrgById(org.getOrgId());
//		String orgName_old = org_old.getOrgName();
//		boolean r = orgManager.storeOrg(org);
//		if(!r)
//		{
//			return mapping.findForward("fail");
//		}
//		if(orgName_old!=null&&!orgName_old.equals(org.getOrgName()))
//		{
//			DBUtil db_td_cms_document = new DBUtil();
//			String sql_td_cms_document = "update td_cms_document set ext_org='"+org.getOrgName()+"' where ext_org='"+org_old.getOrgName()+"'";
//			db_td_cms_document.executeUpdate(sql_td_cms_document);
//			
//			String sql_TD_COMM_EMAIL_DISPOSEDEP = "update TD_COMM_EMAIL_DISPOSEDEP set DISPOSEDEP='"+org.getRemark5()+"' where orgid='"+org_old.getOrgId()+"'";
//			db_td_cms_document.executeUpdate(sql_TD_COMM_EMAIL_DISPOSEDEP);
//			
//			String sql_TD_SP_ORGANIZATION = "update TD_SP_ORGANIZATION set name='"+org.getOrgName()+"',name_jc='"+org.getRemark5()+"' where org_id='"+org_old.getOrgId()+"'";
//			db_td_cms_document.executeUpdate(sql_TD_SP_ORGANIZATION);
//		}
//		org = orgManager.getOrgByName(org.getOrgName());
//		
////		修改机构重新加载缓冲
////		OrgCacheManager.getInstance().reset();
//		//--记日志--------------------------------
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=request.getRemoteAddr();
//        String openModle="机构管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 		
//		operContent=userName+" 修改了机构 "+org.getOrgName(); 
//		description="";
//        logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
//		//-------------------------------------------
//        
//		StringBuffer path = new StringBuffer("");
//    	if("iscms".equals(request.getParameter("iscms")))
//    		path.append("/sysmanager/orgmanager/savesuccess_refresh.jsp");
//    	else
//    		path.append("/sysmanager/orgmanager/savesuccess.jsp");    		
//		
//		path.append("?orgId=" + org.getOrgId()
//				+ "&action=update&parentId=" + org.getParentId());
//	
//		return new ActionForward(path.toString());
//	}

//	public ActionForward deleteOrg(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//	
//		Organization org = (Organization) form;
//		String orgId = org.getOrgId();
//		String parentId = org.getParentId();
//		request.setAttribute("orgId", orgId);
//		request.setAttribute("parentId", parentId);
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		
//		//--记日志-----
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=request.getRemoteAddr();
//        String openModle="机构管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 
//		operContent=userName+" 删除了机构 "+LogGetNameById.getOrgNameByOrgId(orgId); 
//		description="";
//        logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
//		//--------
//        
//		orgManager.deleteOrg(org);
////		删除机构重新加载缓冲
////		OrgCacheManager.getInstance().reset();
//		
//		if("iscms".equals(request.getParameter("iscms")))
//			return (mapping.findForward("delOrg"));
//		
//		ActionForward forward = mapping.findForward("successaddorg");
//		StringBuffer path = new StringBuffer(forward.getPath());
//
//		boolean isQuery = (path.indexOf("?") >= 0);
//
//		if (isQuery) {
//			path.append("&orgId=" + orgId + "&parentId=" + parentId);
//		} else {
//			path.append("?orgId=" + orgId + "&parentId=" + parentId);
//		}
//		return new ActionForward(path.toString());
//
//	}
//
//	public ActionForward getOrgInfo(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		Organization org = (Organization) form;
//		HttpSession session = request.getSession();
//		String selected = request.getParameter("orgId");
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		org = orgManager.getOrgById(selected);
//		session.setAttribute("Organization", org);
//		if (selected.equals("0")) {
//			org = new Organization();
//			org.setOrgName("机构树");
//		}
//		return (mapping.findForward("info"));
//	}

	 

	/**
	 * 机构管理中保存机构角色，ajax－－
	 * @param orgId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */	
	public static String storeOrgRole(String orgId, String roleId,String flag)
			throws Exception {
		try {
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgId);
			Role role = roleManager.getRoleById( roleId);
			if (role != null) {
				if(flag.equals("0")){
				Orgrole orgrole = new Orgrole();
				orgrole.setOrganization(org);
				orgrole.setRole(role);
				orgManager.storeOrgrole(orgrole);
				}else{
					DBUtil db = new DBUtil();
					DBUtil db1 = new DBUtil();
					String sql="SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH"
						+ " t.org_id='" + orgId 
						+ "' CONNECT BY PRIOR t.org_id=t.PARENT_ID";
					db.executeSelect(sql);
					for(int i=0;i<db.size();i++){
						String str ="select * from td_sm_orgrole where " +
								"role_id ='"+ roleId +"' and org_id ='"+ db.getString(i,"org_id")+"'";
						db1.executeSelect(str);
						if(db1.size()>0){
							continue;
						}else{
							String Str ="insert into td_sm_orgrole(role_id,org_id) values('"+ roleId +"','"+db.getString(i,"org_id")+"')";
							db.executeInsert(Str);
						}
					}
				}
			}

		} catch (Exception e) {
			return "fail";
		}
		return "success";

	}
	
	/**
	 * 
	 * @param orgId
	 * @param roleId
	 * @param flag 0=不递归删除子机构角色; 1=递归删除子机构角色
	 * @return
	 * @throws Exception 
	 * OrgManAction.java
	 * @author: ge.tao
	 */
	public static String deleteOrgRole(String orgId, String roleId,String flag)
			throws Exception {
		try {
		
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgId);
			Role role = roleManager.getRoleById( roleId);
			if (role != null) {
				if(flag.equals("0")){
				Orgrole orgrole = new Orgrole();
				orgrole.setOrganization(org);
				orgrole.setRole(role);
				OrgManAction.deleteOrgJobRole(orgId, roleId);
				orgManager.deleteOrgrole(orgrole);
				}else{
					DBUtil db = new DBUtil();
				
					String sql="SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH"
						+ " t.org_id='" + orgId 
						+ "' CONNECT BY PRIOR t.org_id=t.PARENT_ID";
					db.executeSelect(sql);
					for(int i=0;i<db.size();i++){
						
							String Str ="delete from td_sm_orgrole where role_id='"+ roleId +"' and org_id ='"+db.getString(i,"org_id")+"'";
							db.executeDelete(Str);
						
					}	
				}
			}
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 机构管理中删除岗位下以设置的机构的角色 ---gao.tang 2007.10.26
	 * @param orgId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteOrgJobRole(String orgId, String roleId)
			throws Exception {
		DBUtil db = new DBUtil();
		String str = "delete from td_sm_orgjobrole where role_id='"+roleId+"' and org_id='"+orgId+"'";
		db.executeDelete(str);
		return true;
	}
	
	 
	
	/**
	 * 存储用户机构岗位关系（调入人员选择框） //no static
	 */
	public static String storeUJOAjax(String jobId, String[] userIds,
			String orgId) {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			JobManager jobManager = SecurityDatabase.getJobManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgId);
//			Job job=jobManager.getJob("jobId",jobId);
			Job job=jobManager.getJobById(jobId);
//			Job job1=jobManager.getJob("jobId","1");
			Job job1=jobManager.getJobById("1");
			int jobsn;
			for (int i = 0; (userIds != null) && (i < userIds.length); i++) 
			{
				//如果本机构的待岗下有这名用户，删除用户的待岗------------------------------------
				DBUtil dbUtil = new DBUtil();
				dbUtil.executeSelect("select *  from TD_SM_USERJOBORG where job_id ='1' and" +
						" org_id ='"+ orgId +"' and user_id ="+ userIds[i] +"");
				if(dbUtil.size()>0)
				{
					dbUtil.executeDelete("delete from TD_SM_USERJOBORG where job_id ='1' and" +
				" org_id ='"+ orgId +"' and user_id ="+ userIds[i] +"");
				}
				//---------------------------------------------------------------------
				
				User user = userManager.getUserById(userIds[i]);
				DBUtil db = new DBUtil();
				String sql ="select *  from TD_SM_USERJOBORG where job_id ='"+ jobId +"' and" +
				" org_id ='"+ orgId +"' and user_id ="+ userIds[i] +"";
				db.executeSelect(sql);
				String sqlsn ="select job_sn from td_sm_orgjob where job_id ='"+ jobId +"' and org_id ='"+ orgId+"'";
				DBUtil dbutil = new DBUtil();
				dbutil.executeSelect(sqlsn);
				if (dbutil != null && dbutil.size() > 0) 
				{
					jobsn=dbutil.getInt(0,"job_sn");// 得所在岗位排序号
					//如果记录已有，不进行操作
					if(db.size()>0)
					{
						continue;
					}
					else
					{
						//找出该机构该岗位下最大的用户排序号
						String sss ="select max(SAME_JOB_USER_SN) as SN from td_sm_userjoborg where job_id ='"+ jobId +"' and org_id ='"+ orgId+"'";
						DBUtil dbsn = new DBUtil();
						dbsn.executeSelect(sss);
						if (dbsn != null && dbsn.size() > 0) 
						{
							int jobusersn = dbsn.getInt(0,"SN");
							userManager.storeUserjoborg(userIds[i],jobId,orgId,String.valueOf(jobusersn+1),String.valueOf(jobsn),true);
							DBUtil db1 = new DBUtil();
							String str ="select *  from TD_SM_USERJOBORG where " +
							" user_id ="+ userIds[i] +"";
							db1.executeSelect(str);
							if(db1.size()==1)
							{
								orgManager.addMainOrgnazitionOfUser(userIds[i],orgId);
							}

						}
												
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	
	 
	/**
	 * 删除用户机构岗位关系（调入人员选择框）
	 */
	public static String deleteUJOAjax(String jobId, String[] userIds,
			String orgId) {
		try {
				
			for (int i = 0; (userIds != null) && (i < userIds.length); i++) {
				    UserManager userManager = SecurityDatabase.getUserManager();
					DBUtil db = new DBUtil();
					DBUtil db1 = new DBUtil();
					String sql1 ="select * from TD_SM_USERJOBORG where job_id ='"+ jobId +"' and" +
					" org_id ='"+ orgId +"' and user_id ="+ userIds[i] +"";
					db.executeSelect(sql1);
//					-----如果该用户的主要单位为该机构，删除用户和该机构关系
					OrgManager orgManager = SecurityDatabase.getOrgManager();
					String strsql ="select * from td_sm_orguser where org_id ='"+ orgId +"' and user_id =" + userIds[i] + "";
					db.executeSelect(strsql);
					if(db.size()>0){
						orgManager.deleteMainOrgnazitionOfUser(userIds[i]);
					}
					//------------------------------------------------------

				
//					存数据到历史表TD_SM_USERJOBORG_HISTORY
					db1.execute(sql1);
					for(int j= 0; j < db1.size(); j ++)
					{
						int userid=db1.getInt(j,"user_id");
						String jid = db1.getString(j,"JOB_ID");
						String oid = db1.getString(j,"ORG_ID");
						Date starttime = db1.getDate(j,"JOB_STARTTIME");
						String sql2 ="insert into TD_SM_USERJOBORG_HISTORY values("+ userid +",'"+ jid +"'," +
								"'"+ oid +"',"+ DBUtil.getDBDate(starttime)+"," +
								""+ DBUtil.getDBDate(new Date())+",0)";
						db1.executeInsert(sql2);
					}
					userManager.deleteUserjoborg(userIds[i],jobId,orgId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	//机构管理－－机构基本信息－－机构授权情况
	public static String editRoleOper(String resId,String resTypeId,String opId,String checked,String title,String isRecursion) {
		if(isRecursion==null){
			isRecursion ="0";
		}
		String[] tmp = opId.split(";");
		String roleid = tmp[0];
		String opid = tmp[1];
		
		if (tmp != null && tmp.length == 2) {
			boolean sendevent =false;
			try {
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				Organization org = orgManager.getOrgById(resId);
				if(checked != null && checked.equals("1")){
					sendevent = true;
					roleManager.storeRoleresop(opid,resId,roleid,resTypeId,title,"role",false);
				}else if(checked != null && checked.equals("0")){
					sendevent = true;
					roleManager.deletePermissionOfRole(opid,resId,resTypeId,roleid,"role",false);
				}
				//递归保存子机构
				if (isRecursion.equals("1")) {
					List orglist = orgManager.getChildOrgList(org,true);
					for (int i = 0; orglist != null && i < orglist.size(); i++) {
						Organization organization = (Organization) orglist.get(i);
					
						if(checked != null && checked.equals("1")){
							sendevent = true;
							roleManager.storeRoleresop(opid,organization.getOrgId(),roleid,resTypeId,organization.getOrgName(),"role",false);
						}else if(checked != null && checked.equals("0")){
							sendevent = true;
							roleManager.deletePermissionOfRole(opid,organization.getOrgId(),resTypeId,roleid,"role",false);
						}
					}
			 }
				if(sendevent )
					EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
				return "success";
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		}else
			return "error";
	}
	 

}
