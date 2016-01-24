<%@page import="com.frameworkset.platform.util.EventUtil"%>
<%@page import="com.frameworkset.orm.transaction.TransactionManager"%>
<%@page import="com.frameworkset.common.poolman.PreparedDBUtil"%>
<%@page import="com.frameworkset.platform.config.ConfigManager"%><%
/**
 * <p>Title: 删除机构处理页面</p>
 * <p>Description: 删除机构处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %><%@ page language="java" contentType="text/html; charset=UTF-8"%><%@ page import="com.frameworkset.util.StringUtil,
			com.frameworkset.platform.security.AccessControl,
			com.frameworkset.platform.sysmgrcore.entity.Organization,
			com.frameworkset.common.poolman.DBUtil,
			com.frameworkset.platform.sysmgrcore.manager.OrgManager,
			com.frameworkset.platform.sysmgrcore.manager.UserManager,
		    com.frameworkset.platform.sysmgrcore.manager.LogManager,
		    com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
		    com.frameworkset.platform.sysmgrcore.manager.LogGetNameById,org.frameworkset.web.servlet.support.RequestContextUtils"%><%
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			boolean tag = true;
			
			String notice = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.remove.fail.admin", request);

			OrgManager orgManager = SecurityDatabase.getOrgManager();
			String orgId = StringUtil
					.replaceNull(request.getParameter("orgId"));
			 
			//Organization org = orgManager.getOrgById(orgId);

			//String orgId = org.getOrgId();
			//String parentId = org.getParentId();
			//request.setAttribute("orgId", orgId);
			//request.setAttribute("parentId", parentId);

			//--记日志-----
			String operContent = "";
			String operSource = control.getMachinedID();
			String openModle = RequestContextUtils.getI18nMessage("sany.pdp.organization.manage", request);
			String userName = control.getUserName();
			String description = "";
			LogManager logManager = SecurityDatabase.getLogManager();
			operContent = userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.remove.log", request)
					+ LogGetNameById.getOrgNameByOrgId(orgId);
			description = "";
			//--------

			
			
			//获取当前机构下的所有用户的ID
			//String  sql = " select distinct b.USER_ID from TD_SM_USERJOBORG b where b.ORG_ID in ( "
		//	+ "select distinct a.ORG_ID from TD_SM_ORGANIZATION a start with a.ORG_ID = '" + orgId 
			//+ "' connect by prior a.ORG_ID = a.PARENT_ID)";
			String sql = "select distinct b.USER_ID from TD_SM_USERJOBORG b where b.ORG_ID in ("

      +"select a.ORG_ID from TD_SM_ORGANIZATION a where a.org_tree_level like  "
	+"	(select concat(org_tree_level, '%') from TD_SM_ORGANIZATION c where c.ORG_ID = ?))" ;
  


			//根据用户ID删除用户所拥有的一切资源(除超级管理员外)
			UserManager userManager = SecurityDatabase.getUserManager() ;
			TransactionManager tm = new TransactionManager();
			String[] userIds = null;
			try
			{
				tm.begin();
				PreparedDBUtil db = new PreparedDBUtil();
				db.preparedSelect(sql);
				db.setString(1, orgId);
				db.executePrepared();
				//如果使用了离散用户，删除机构只将机构下的用户的资源和关系删掉，变为离散用户。如果没有离散用户将机构下的用户彻底删除
				boolean islisan = ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true);
				if(db.size()>0)
				{
					 userIds = new String[db.size()];
					for(int i= 0; i<db.size(); i++)
					{
						userIds[i] = String.valueOf(db.getInt(i,"USER_ID"));
					}
					if(userIds.length > 0){
						if(islisan){
							userManager.deleteBatchUserRes(userIds,false);
						}else{
							userManager.deleteBatchUser(userIds,false);
						}
					}
				}
				//递归删除机构
				tag = orgManager.deleteOrg(orgId,false);
				tm.commit();
				if(userIds != null)
					EventUtil.sendUSER_INFO_DELETEEvent(userIds);
				EventUtil.sendUSER_ROLE_INFO_CHANGEEvent(orgId);
				EventUtil.sendORGUNIT_INFO_DELETEEvent(orgId);
			}
			catch(Exception e)
			{
				tag = false ;
				notice = StringUtil.exceptionToString(e);
			}
			finally
			{
				tm.release();
			}
			if (tag) 
			{
				logManager.log(control.getUserAccount() ,
					operContent, openModle, operSource, description);
			out.print("success");
			}
			else
		    {
				out.print("error:"+notice);
			}
			%>