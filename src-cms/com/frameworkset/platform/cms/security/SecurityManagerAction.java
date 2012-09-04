package com.frameworkset.platform.cms.security;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.templatemanager.TemplateManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.web.struts.form.RoleManagerForm;
import com.frameworkset.common.poolman.DBUtil;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:站点频道授权的action
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
public class SecurityManagerAction extends DispatchAction implements java.io.Serializable {
	public SecurityManagerAction() {
	}

	private static Logger log = Logger.getLogger(SecurityManagerAction.class
			.getName());

	
	/**
	 * 角色管理＝资源操作授予＝站点授权，包括是否递归 add 王卓
	 * 
	 * @param resId
	 * @param resTypeId
	 * @param opId
	 * @param roleId
	 * @param Flag
	 * @param isRecursion
	 * @return
	 */
	public ActionForward siteAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("siteid",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//根据站点id取站点名称
			SiteManager sm = new SiteManagerImpl();
			String resname = sm.getSiteInfo(resid).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
				//操作和递归为空的话删除当前站点所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前站点和子站点所有操作
				if(opid==null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					if(subsite.size()==0){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					}
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
					
					}
				}
				//操作不为空和递归为空的话授权当前站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
					//递归给该站点的父站点授可见权限
					String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
						 " t.site_id="+ resid +" CONNECT BY PRIOR t.mainsite_ID=t.site_id";
					db.executeSelect(str);
					if(db.size()>1){
						for(int j=0;j<db.size();j++)
						{
							String siteid = db.getInt(j,"site_id")+"";
							String name = db.getString(j,"name");
							roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
						
						}
					}
				}
				//操作不为空和递归不为空的话授权当前站点及子站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,""+site.getSiteId(),tmp[k],resTypeId,site.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
						//递归给该站点的父站点授可见权限
						String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
							 " t.site_id="+ resid +" CONNECT BY PRIOR t.site_id = t.mainsite_ID";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String siteid = db.getInt(j,"site_id")+"";
								String name = db.getString(j,"name");
								roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
							
							}
						}
				}
			}
		
			return mapping.findForward("opsitetab");
	}
	
	/**
	 * 角色管理＝资源操作授予＝站点应用授权 add xinwang.jiao
	 * 
	 * @param resId
	 * @param resTypeId
	 * @param opId
	 * @param roleId
	 * @param Flag
	 * @param isRecursion
	 * @return
	 */
	public ActionForward siteAppAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			//String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("siteid",resid);
			request.setAttribute("isOk","1");
			
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			
			
			DBUtil db = new DBUtil();
			//根据站点id取站点名称
			//SiteManager sm = new SiteManagerImpl();
			String resname = (String)session.getAttribute("resname");//sm.getSiteInfo(resid).getName();
			session.removeAttribute("resname");
			request.setAttribute("resname",resname);
			
			
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
				//操作和递归为空的话删除当前站点所有操作
				if(opid==null){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				
				//操作不为空和递归为空的话授权当前站点相关操作（先删后存）
				if(opid!=null){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
					try
					{
						Integer.parseInt(resid);
						//递归给该站点的父站点授可见权限
						String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
							 " t.site_id="+ resid +" CONNECT BY PRIOR t.mainsite_ID=t.site_id";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String siteid = db.getInt(j,"site_id")+"";
								String name = db.getString(j,"name");
								roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
							
							}
						}
					}
					catch(Exception e)
					{
						
					}
				}
			}
		
			return mapping.findForward("opsiteapptab");
	}
	
	public ActionForward sitecnlAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper1");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("siteid",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//根据站点id取站点名称
			SiteManager sm = new SiteManagerImpl();
			String resname = sm.getSiteInfo(resid).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
				//操作和递归为空的话删除当前站点所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前站点和子站点所有操作
				if(opid==null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					if(subsite.size()==0){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					}
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
					
					}
				}
				//操作不为空和递归为空的话授权当前站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);

				}
				//操作不为空和递归不为空的话授权当前站点及子站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,""+site.getSiteId(),tmp[k],resTypeId,site.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);

				}
			}
		
			return mapping.findForward("opsitetab");
	}
	
	
	
	public ActionForward sitetplAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper2");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("siteid",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//根据站点id取站点名称
			SiteManager sm = new SiteManagerImpl();
			String resname = sm.getSiteInfo(resid).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
				//操作和递归为空的话删除当前站点所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前站点和子站点所有操作
				if(opid==null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					if(subsite.size()==0){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					}
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
					
					}
				}
				//操作不为空和递归为空的话授权当前站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
//					递归给该站点的父站点授可见权限
					String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
						 " t.site_id="+ resid +" CONNECT BY PRIOR t.site_id = t.mainsite_ID";
					db.executeSelect(str);
					if(db.size()>1){
						for(int j=0;j<db.size();j++)
						{
							String siteid = db.getInt(j,"site_id")+"";
							String name = db.getString(j,"name");
							roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
						
						}
					}
				}
				//操作不为空和递归不为空的话授权当前站点及子站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,""+site.getSiteId(),tmp[k],resTypeId,site.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
//						递归给该站点的父站点授可见权限
						String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
							 " t.site_id="+ resid +" CONNECT BY PRIOR t.site_id = t.mainsite_ID";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String siteid = db.getInt(j,"site_id")+"";
								String name = db.getString(j,"name");
								roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
							
							}
						}
				}
			}
		
			return mapping.findForward("opsitetab");
	}
	
	public ActionForward sitedocAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper3");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("siteid",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//根据站点id取站点名称
			SiteManager sm = new SiteManagerImpl();
			String resname = sm.getSiteInfo(resid).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
				//操作和递归为空的话删除当前站点所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前站点和子站点所有操作
				if(opid==null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					if(subsite.size()==0){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					}
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
					
					}
				}
				//操作不为空和递归为空的话授权当前站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);

				}
				//操作不为空和递归不为空的话授权当前站点及子站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,""+site.getSiteId(),tmp[k],resTypeId,site.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);

				}
			}
		
			return mapping.findForward("opsitetab");
	}
	
	public ActionForward sitefileAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper4");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("siteid",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//根据站点id取站点名称
			SiteManager sm = new SiteManagerImpl();
			String resname = sm.getSiteInfo(resid).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
				//操作和递归为空的话删除当前站点所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前站点和子站点所有操作
				if(opid==null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					if(subsite.size()==0){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					}
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
					
					}
				}
				//操作不为空和递归为空的话授权当前站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
//					递归给该站点的父站点授可见权限
					String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
						 " t.site_id="+ resid +" CONNECT BY PRIOR t.site_id = t.mainsite_ID";
					db.executeSelect(str);
					if(db.size()>1){
						for(int j=0;j<db.size();j++)
						{
							String siteid = db.getInt(j,"site_id")+"";
							String name = db.getString(j,"name");
							roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
						
						}
					}
				}
				//操作不为空和递归不为空的话授权当前站点及子站点相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					SiteManager siteManager = new SiteManagerImpl();
					List subsite = siteManager.getAllSubSiteList(resid);
					for (int i = 0; subsite != null && i < subsite.size(); i++) {
						Site site = (Site) subsite.get(i);
						roleManager.deletePermissionOfRole(""+site.getSiteId(),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,""+site.getSiteId(),tmp[k],resTypeId,site.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
//						递归给该站点的父站点授可见权限
						String str="SELECT t.site_id,t.name FROM TD_cms_site t START WITH "+
							 " t.site_id="+ resid +" CONNECT BY PRIOR t.site_id = t.mainsite_ID";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String siteid = db.getInt(j,"site_id")+"";
								String name = db.getString(j,"name");
								roleManager.storeRoleresop("write",siteid,tmp[k],resTypeId,name,role_type);
							
							}
						}
				}
			}
		
			return mapping.findForward("opsitetab");
	}
	
	/**
	 * 角色管理＝资源操作授予＝频道授权，包括是否递归 add 王卓
	 * 
	 * @param resId
	 * @param resTypeId
	 * @param opId
	 * @param roleId
	 * @param Flag
	 * @param isRecursion
	 * @return
	 */
	public ActionForward channelAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String channelId = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("channelId",channelId);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//session.setAttribute("isOk","1111");
			//根据频道id取频道名称
			ChannelManager cm = new ChannelManagerImpl();
			String resname = cm.getChannelInfo(channelId).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
//				操作和递归为空的话删除当前频道所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前频道和子频道所有操作
				if(opid==null && isRecursion.equals("1")){
					
					List subchannel = cm.getAllSubChannels(channelId);
					for (int i = 0; subchannel != null && i < subchannel.size(); i++) {
						Channel channel = (Channel) subchannel.get(i);
						roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(String.valueOf(channel.getChannelId()),resTypeId,tmp[k],role_type);
					}
				}
				//操作不为空和递归为空的话授权当前频道相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,channelId,tmp[k],resTypeId,resname,role_type);
					//递归给该频道的父频道授只读权限
//					String str="SELECT t.channel_id FROM TD_cms_channel t START WITH "+
//					" t.channel_id="+ channelId +" CONNECT BY PRIOR t.channel_id = t.parent_ID";
//					db.executeSelect(str);
//					if(db.size()>1){
//						for(int j=0;j<db.size();j++)
//						{
//							String parentid = db.getInt(j,"channel_id")+"";
//							roleManager.storeRoleresop("write",parentid,tmp[k],resTypeId,resname,role_type);
//						
//						}
//					}
				}
				//操作不为空和递归不为空的话授权当前频道及子频道相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					
					List subchannel = cm.getAllSubChannels(channelId);
					for (int i = 0; subchannel != null && i < subchannel.size(); i++) {
						Channel channel = (Channel) subchannel.get(i);
						roleManager.deletePermissionOfRole(String.valueOf(channel.getChannelId()),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,String.valueOf(channel.getChannelId()),tmp[k],resTypeId,channel.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,channelId,tmp[k],resTypeId,resname,role_type);
						//递归给该频道的父频道授只读权限
						String str="SELECT t.channel_id FROM TD_cms_channel t START WITH "+
						" t.channel_id="+ channelId +" CONNECT BY PRIOR t.channel_id = t.parent_ID";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String parentid = db.getInt(j,"channel_id")+"";
								roleManager.storeRoleresop("write",parentid,tmp[k],resTypeId,resname,role_type);
							
							}
						}
				}
			}
			
			return mapping.findForward("opchanneltab");
	}
	
	public ActionForward docAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper2");
			String isRecursion = request.getParameter("isRecursion");
			String resTypeId = request.getParameter("resTypeId");
			String channelId = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("channelId",channelId);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			//session.setAttribute("isOk","1111");
			//根据频道id取频道名称
			ChannelManager cm = new ChannelManagerImpl();
			String resname = cm.getChannelInfo(channelId).getName();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			for(int k=0;k<tmp.length;k++){
//				操作和递归为空的话删除当前频道所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前频道和子频道所有操作
				if(opid==null && isRecursion.equals("1")){
					
					List subchannel = cm.getAllSubChannels(channelId);
					for (int i = 0; subchannel != null && i < subchannel.size(); i++) {
						Channel channel = (Channel) subchannel.get(i);
						roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
						roleManager.deletePermissionOfRole(String.valueOf(channel.getChannelId()),resTypeId,tmp[k],role_type);
					}
				}
				//操作不为空和递归为空的话授权当前频道相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,channelId,tmp[k],resTypeId,resname,role_type);
					//递归给该频道的父频道授只读权限
					String str="SELECT t.channel_id FROM TD_cms_channel t START WITH "+
					" t.channel_id="+ channelId +" CONNECT BY PRIOR t.channel_id = t.parent_ID";
					db.executeSelect(str);
					if(db.size()>1){
						for(int j=0;j<db.size();j++)
						{
							String parentid = db.getInt(j,"channel_id")+"";
							roleManager.storeRoleresop("write",parentid,tmp[k],resTypeId,resname,role_type);
						
						}
					}
				}
				//操作不为空和递归不为空的话授权当前频道及子频道相关操作（先删后存）
				if(opid!=null && isRecursion.equals("1")){
					
					List subchannel = cm.getAllSubChannels(channelId);
					for (int i = 0; subchannel != null && i < subchannel.size(); i++) {
						Channel channel = (Channel) subchannel.get(i);
						roleManager.deletePermissionOfRole(String.valueOf(channel.getChannelId()),resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,String.valueOf(channel.getChannelId()),tmp[k],resTypeId,channel.getName(),role_type);
					}
						roleManager.deletePermissionOfRole(channelId,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,channelId,tmp[k],resTypeId,resname,role_type);
						//递归给该频道的父频道授只读权限
						String str="SELECT t.channel_id FROM TD_cms_channel t START WITH "+
						" t.channel_id="+ channelId +" CONNECT BY PRIOR t.channel_id = t.parent_ID";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String parentid = db.getInt(j,"channel_id")+"";
								roleManager.storeRoleresop("write",parentid,tmp[k],resTypeId,resname,role_type);
							
							}
						}
				}
			}
			
			return mapping.findForward("opchanneltab");
	}
	/**
	 * 角色管理＝资源操作授予＝模板授权， add 王卓
	 * 有确定按钮
	 */
	public ActionForward tplAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			TemplateManager tplm = new TemplateManagerImpl();
			Template template = tplm.getTemplateInfo(resid);
			String resName = template.getName();
		
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("templateId",resid);
			request.setAttribute("resName",resName);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
		
			RoleManager roleManager = SecurityDatabase.getRoleManager();
		
			for(int k=0;k<tmp.length;k++){
				//操作为空的话删除当前模板资源所有操作
				if(opid==null){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}

				//操作不为空的话授权当前模板资源相关操作（先删后存）
				if(opid!=null){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resName,role_type);
					
				}
			}
		
			return mapping.findForward("optemplate");
	}
	
	/**
	 * cms权限管理 >> 资源管理 >> 授权组织， add by xinwang.jiao
	 * 有确定按钮
	 */
	public ActionForward resAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] opid = request.getParameterValues("alloper");
		String resTypeId = request.getParameter("resTypeId");
		String resid = request.getParameter("resid");
		String roleid = request.getParameter("roleid");
		String[] tmp = roleid.split(",");
		String role_type = request.getParameter("role_type");
		String isRecursion = request.getParameter("isRecursion");
		//TemplateManager tplm = new TemplateManagerImpl();
		//Template template = tplm.getTemplateInfo(resid);
		String resName = request.getParameter("resname");//template.getName();
	
		request.setAttribute("resTypeId",resTypeId);
		request.setAttribute("templateId",resid);
		request.setAttribute("resName",resName);
		request.setAttribute("isOk","1");
		HttpSession session = request.getSession();
		session.setAttribute("currRoleId", roleid);
		session.setAttribute("role_type", role_type);
	
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		DBUtil db = new DBUtil();
	
		for(int k=0;k<tmp.length;k++){
			//操作和递归为空的话删除当前机构所有操作
			if(opid==null && isRecursion.equals("0")){
				roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
				
			}
			
			//操作为空和递归不为空的话删除当前机构和子机构所有操作
			if(opid==null && isRecursion.equals("1")){
				String sqlstr = "select org_id from td_sm_organization t START WITH t.org_id = " + tmp[k] +
						" CONNECT BY PRIOR t.org_id = t.parent_id";
				
				db.executeSelect(sqlstr);
				
				if(db.size()>0){
					for(int j=0;j<db.size();j++)
					{
						String parentid = db.getInt(j,"org_id")+"";
						roleManager.deletePermissionOfRole(resid,resTypeId,parentid,role_type);
					
					}
				}
			}

			//操作不为空的话授权当前机构资源相关操作（先删后存）
			if(opid!=null && isRecursion.equals("0")){
				roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
				roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resName,role_type);
				
			}
			
			//操作不为空和递归不为空的话授权当前机构及子机构相关操作（先删后存）
			if(opid!=null && isRecursion.equals("1")){
				String sqlstr = "select org_id from td_sm_organization t START WITH t.org_id = " + tmp[k] +
					" CONNECT BY PRIOR t.org_id = t.parent_id";
				
				db.executeSelect(sqlstr);
				
				if(db.size()>0){
					for(int j=0;j<db.size();j++)
					{
						String parentid = db.getInt(j,"org_id")+"";
						roleManager.deletePermissionOfRole(resid,resTypeId,parentid,role_type);
						roleManager.storeRoleresop(opid,resid,parentid,resTypeId,resName,role_type);
					}
				}
			}
		}
	
		return mapping.findForward("opres");
	}
	
	/**
	 * cms权限管理 >> 资源管理 >> 授权用户， add by xinwang.jiao
	 * 有确定按钮
	 */
	public ActionForward resUserAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			//TemplateManager tplm = new TemplateManagerImpl();
			//Template template = tplm.getTemplateInfo(resid);
			
			//去掉session
			//HttpSession session = request.getSession();			
			//String resName = (String)session.getAttribute("resname");
			String resName = (String) request.getParameter("resname") ;
			
			
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("templateId",resid);
			request.setAttribute("resName",resName);
			request.setAttribute("isOk","1");
			
			//去掉session 使用request
			//session.setAttribute("currRoleId", roleid);
			//session.setAttribute("role_type", role_type);
			request.setAttribute("currRoleId",roleid) ;
			request.setAttribute("role_type",role_type) ;
			
			request.setAttribute("resId",roleid) ;
			
			
		
			RoleManager roleManager = SecurityDatabase.getRoleManager();
		
			for(int k=0;k<tmp.length;k++){
				//操作为空的话删除当前模板资源所有操作
				if(opid==null){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}

				//操作不为空的话授权当前模板资源相关操作（先删后存）
				if(opid!=null){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resName,role_type);
					
				}
			}
		
			return mapping.findForward("opresuser");
	}
	
}
