package com.frameworkset.platform.cms.appmanager.menu;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.config.model.OperationGroup;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.util.StringUtil;

/**
 * 根据module－id取单个模块
 * 内容管理》应用管理 左边的应用菜单树
 * 需要经过应用的权限来过滤相关的菜单，无需读取菜单的权限
 * @author Administrator
 *
 */
public class SiteApp extends COMTree implements java.io.Serializable {
//	MenuHelper menuHelper = null;
	Framework framework;
	CMSManager cmsManager;
    private static final Logger log = Logger.getLogger(SiteApp.class);
	public void setPageContext(PageContext context) {
		super.setPageContext(context);
//		menuHelper = new MenuHelper(accessControl);//经过菜单权限过滤
		framework = Framework.getInstance();//不带菜单权限
		
		cmsManager = new CMSManager();
		cmsManager.init(request,session,super.accessControl); 

	}

	public boolean hasSon(ITreeNode parent) {

		String parentID = parent.getId();
		String parentType = parent.getType();
		if (parentType.equals("item") || parentID == null || parentType == null)
			return false;

		return framework.getSubItems(parentID).size() > 0
				|| framework.getSubModules(parentID).size() > 0;

	}

	public boolean setSon(ITreeNode parent, int level) {
		String parentID = parent.getId();
		ItemQueue items = framework.getSubItems(parentID);
		ModuleQueue submodules =  framework.getSubModules(parentID);
		
		
		String treeid = "";
        String treeName = "";
        String moduleType = "module";
        String itemType = "item";

        boolean showHref = true;
        String memo = null;
        String radioValue = null;
        String checkboxValue = null;
        String path = "";
        ResourceManager resourceManager = new ResourceManager();
		for(int i = 0; i < submodules.size(); i ++) {
			
            Map params = new HashMap();
            Module submodule = submodules.getModule(i);
            
            treeid = submodule.getPath();
            treeName = submodule.getName(request).trim();
            path = submodule.getPath();
            
            //判断应用是否在站点下
            if(!this.appInSite(path) ) continue;
           
//            OperationGroup operationgroup = resourceManager.getOperationGroup(submodule.getId()) ;
            if(!this.accessControl.checkPermission(cmsManager.getCurrentSite().getSiteId() + "$$" + submodule.getId(),"visible","app_column"))
            {
            	if(!accessControl.isAdmin())
            		continue;
            }
            
            params.put("columnID",treeid);
            params.put("resId",submodule.getId());
           	params.put("resName",treeName); 
           	params.put("nodeLink","../main.jsp");
           //System.out.println("submodule.getId(): " + submodule.getId());
           	/*
           	 * 修改:互动管理-->应用项管理菜单下的模块图标(/chiancreator/cms/images/tree_images/publishitem)
           	 */
           	ITreeNode tt = addNode(parent, treeid, treeName, "publishitem", true, level, memo,
                    radioValue, checkboxValue, path,params);
//           	if(operationgroup == null || 
//            		!this.accessControl.checkPermission(cmsManager.getCurrentSite().getSiteId() + "$$" + submodule.getId(),"permissionset","app_column"))            	
//            	continue;
//           	Menu sitemenu = new Menu();
//            Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
//			sitemenuitem0.setName("授予用户");	
//			sitemenuitem0.setLink("javascript:dispatch(0,\""+cmsManager.getCurrentSite().getSiteId()+"\",\""+submodule.getId()+"\",\""+submodule.getName()+"\")");
//			sitemenuitem0.setIcon(request.getContextPath() + "/cms/siteManage/new_doc.gif");
//			sitemenu.addContextMenuItem(sitemenuitem0);
//			
//			Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
//			sitemenuitem1.setName("授予角色");
//			sitemenuitem1.setLink("javascript:dispatch(1,\""+cmsManager.getCurrentSite().getSiteId()+"\",\""+submodule.getId()+"\",\""+submodule.getName()+"\")");
//			sitemenuitem1.setIcon(request.getContextPath() + "/cms/siteManage/new_doc.gif");
//			sitemenu.addContextMenuItem(sitemenuitem1);
//			
//			Menu.ContextMenuItem sitemenuitem2 = new Menu.ContextMenuItem();
//			sitemenuitem2.setName("授予组织");
//			sitemenuitem2.setLink("javascript:dispatch(2,\""+cmsManager.getCurrentSite().getSiteId()+"\",\""+submodule.getId()+"\",\""+submodule.getName()+"\")");
//			sitemenuitem2.setIcon(request.getContextPath() + "/cms/siteManage/new_doc.gif");
//			sitemenu.addContextMenuItem(sitemenuitem2);
//			
//			super.addContextMenuOfNode(tt,sitemenu);
        }
        
		
		
        for(int i = 0; i < items.size(); i ++) {

            Map params = new HashMap();
            Item subitem = (Item)items.getItem(i);
            
            treeid = subitem.getPath();
            treeName = subitem.getName(request);
            path = subitem.getPath();
            
            //判断应用是否在站点下
            if(!this.appInSite(path)) 
            	continue;
//            OperationGroup operationgroup = resourceManager.getOperationGroup(subitem.getId()) ;
            if(!this.accessControl.checkPermission(cmsManager.getCurrentSite().getSiteId() + "$$" + subitem.getId(),"visible","app_column"))
            {
            	if(!accessControl.isAdmin())
            	{
            		continue;
            	}
            }
            showHref = true;
            String workspaceContent = null;
            if(!subitem.hasWorkspaceContentVariables()){
            	workspaceContent = subitem.getWorkspaceContent();
            }else{
            	workspaceContent = Framework.getWorkspaceContent(subitem, accessControl);
            }
            params.put("columnID",treeid);
            params.put("resId",subitem.getId());
            params.put("resName",subitem.getName(request));            
            params.put("nodeLink",StringUtil.getRealPath(request,workspaceContent));   
           // System.out.println("subitem.getId(): " + subitem.getId());
            /*
             * 修改:互动管理-->应用项管理菜单下的Item图标(/chiancreator/cms/images/tree_images/publishitem)
             */
            ITreeNode tt = addNode(parent, treeid, treeName, "publishitem", showHref, level, memo,
                    radioValue, checkboxValue, path,params); 
//            if(operationgroup == null || 
//            		!this.accessControl.checkPermission(cmsManager.getCurrentSite().getSiteId() + "$$" + subitem.getId(),"permissionset","app_column"))
//            	
//            	continue;
//            Menu sitemenu = new Menu();
//            
//            Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
//			sitemenuitem0.setName("授予用户");	
//			sitemenuitem0.setLink("javascript:dispatch(0,\""+cmsManager.getCurrentSite().getSiteId()+"\",\""+subitem.getId()+"\",\""+subitem.getName() +"\")");
//			sitemenuitem0.setIcon(request.getContextPath() + "/cms/siteManage/new_doc.gif");
//			sitemenu.addContextMenuItem(sitemenuitem0);
//			
//			Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
//			sitemenuitem1.setName("授予角色");
//			sitemenuitem1.setLink("javascript:dispatch(1,\""+cmsManager.getCurrentSite().getSiteId()+"\",\""+subitem.getId()+"\",\""+subitem.getName()+"\")");
//			sitemenuitem1.setIcon(request.getContextPath() + "/cms/siteManage/new_doc.gif");
//			sitemenu.addContextMenuItem(sitemenuitem1);
//			
//			Menu.ContextMenuItem sitemenuitem2 = new Menu.ContextMenuItem();
//			sitemenuitem2.setName("授予组织");
//			sitemenuitem2.setLink("javascript:dispatch(2,\""+cmsManager.getCurrentSite().getSiteId()+"\",\""+subitem.getId()+"\",\""+subitem.getName()+"\")");
//			sitemenuitem2.setIcon(request.getContextPath() + "/cms/siteManage/new_doc.gif");
//			sitemenu.addContextMenuItem(sitemenuitem2);
//			
//			super.addContextMenuOfNode(tt,sitemenu);
        }
		return true;
	}
	
	/**
	 * 判断应用是否在当前站点下
	 * @param appPath
	 * @return 
	 * SiteApp.java
	 * @author: ge.tao
	 */
	public boolean appInSite(String appPath){
		boolean flag = false;
		StringBuffer sql = new StringBuffer();		
		DBUtil db = new DBUtil();
		
//		AccessControl accesscontroler = AccessControl.getInstance();
//		accesscontroler.checkAccess(request, response);
		
		Site site = cmsManager.getCurrentSite();
		if(site != null)
		{
			
			sql.append("select t.site_id,t.app_id,t.app_path from  TD_CMS_SITEAPPS t  ");
			sql.append("where t.site_id=").append(site.getSiteId());
			//log.warn(sql.toString());
			try {
				db.executeSelect(sql.toString());
				for (int i = 0; i < db.size(); i++) {
	                if(appPath.equalsIgnoreCase(db.getString(i,"app_path"))){
	                	flag = true;
	                	break;
	                }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public static void main(String ag[]) throws SQLException
	{
		DBUtil dbUtil = new DBUtil();
		System.out.print(dbUtil.getNextStringPrimaryKey("td_sm_organization"));
		System.out.print(dbUtil.getNextStringPrimaryKey("td_sm_organization"));
	}

}
