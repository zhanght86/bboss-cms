package com.frameworkset.platform.cms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.platform.security.AccessControl;

/**
 * 内容管理全局信息、当前用户访问站点信息获取类
 * @author biaoping.yin
 *
 */
public final class CMSManager {
	

	HttpServletRequest request;

	

	HttpSession session;

	

	HttpServletResponse response;
	AccessControl accessControl;
	
	public static final String SESSION_SITEINFO_KEY = "SESSION_SITEINFO_KEY";
	public static final String COOKIE_SITEINFO_KEY = "COOKIE_SITEINFO_KEY";
	
	/**
	 * 初时化方法
	 * @param request
	 * @param session
	 * @param response
	 */
	public void init(HttpServletRequest request,
					 HttpSession session,
					 HttpServletResponse response,AccessControl accessControl)
	{
		this.request = request;
		this.session = session;
		this.response = response;
		this.accessControl = accessControl;
	}
	
	/**
	 * 初时化方法
	 * @param request
	 * @param session
	 * @param response
	 */
	public void init(HttpServletRequest request,
					 HttpSession session,
					 AccessControl accessControl)
	{
		this.request = request;
		this.session = session;
		
		this.accessControl = accessControl;
	}
	/**
	 * 返回站点id
	 * @return
	 */
	public String getSiteID()
	{
		Site currentSite = getCurrentSite();
		return currentSite == null ?"" : currentSite.getSiteId() + "";
		
	}
	
	/**
	 * 更新session和cookie中存放的当前站点信息
	 * @param currentSite
	 */
	public void updateCurrentSite(Site currentSite)
	{
		session.setAttribute(SESSION_SITEINFO_KEY,currentSite);
	}
	
	/**
	 * 获取当前用户有权限操作的开通的站点列表
	 * @return
	 */
	public List getSiteAllRuningList()
	{
		SiteManager siteimpl=new SiteManagerImpl();
		
		List sitelist = siteimpl.getSiteAllRuningList(accessControl);
		return sitelist;
		
		
	}
	public Site getCurrentSite()
	{
		
		Site currentSite = (Site)this.session.getAttribute(SESSION_SITEINFO_KEY);

		if(currentSite == null)
		{
			//获取缺省的当前站点（cookie，系统设置）
		
			//设置缺省站点到session中，置为当前站点
			SiteManager smi = new SiteManagerImpl();
			String userId = this.accessControl.getUserID();
			try {
				String siteid = smi.userDefaultSite(userId);
				//如果没有取到默认站点，加载用户有权限、站点状态为开通的站点
				if(siteid ==null || siteid.equals(""))
				{
					List sitelist = this.getSiteAllRuningList();
					for(int i = 0; sitelist != null && i < sitelist.size(); i ++)
					{
						currentSite = (Site)sitelist.get(i);
						updateCurrentSite(currentSite);
						break;
					}
				}
				else
				{
					currentSite = smi.getSiteInfo(siteid);
					updateCurrentSite(currentSite);
				}
								
			} catch (SiteManagerException e) {
				
				e.printStackTrace();
			}
		    
		
			
			
			//session.setAttribute(SESSION_SITEINFO_KEY,currentSite);
			//不是从cookie中获取的则设置当前站点到cookie
		}
//		else
//		{
//			List sitelist = this.getSiteAllRuningList();
//			for(int i = 0; sitelist != null && i < sitelist.size(); i ++)
//			{
//				currentSite = (Site)sitelist.get(i);
//				updateCurrentSite(currentSite);
//				break;
//			}
//				
//		}
//		if(currentSite == null)
//		{
//			//可能当前系统没有站点，提示创建站点
//			//可能没有权限，提示联系管理授权
//			return null;
//		}
		return currentSite;
	}
	
	public String getSiteDBName()
	{
		Site currentSite = getCurrentSite();
		return currentSite == null ?"" : currentSite.getDbName();
	}
	
	/**
	 * 获取当前的显示的菜单模块路径
	 * @return
	 */
	public String getCurrentModule()
	{
		String moudlePath = FrameworkServlet.getCurrentMenuPath(this.request,this.response);
		//System.out.println("...."+moudlePath);
		if(moudlePath == null)
		{
			//取列表中的第一个菜单路径
			moudlePath = "com.frameworkset.platform.framework.Item@7db430";
		}
		else
		{
			//moudlePath是下拉列表中的一个则返回
			
			//如果不是下拉列表中的一个则返回第一个菜单的路径
		}
	
		return moudlePath ;
	}
	

}
