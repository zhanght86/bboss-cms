package com.frameworkset.platform.cms.sitemanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 显示所有站点节点的站点树
 * 不包括频道
 * @author jxw
 *
 */
public class AllSiteTree extends COMTree  implements java.io.Serializable
{
	private MenuHelper menuHelper;
	private String subsystem;
	/**
	 * 内容管理视图模块路径
	 */
	private String parentPath = "cms::menu://sysmenu$root/siteview$module";
    /**
     * 表单管理的模块路径
     */
    private String formParentPath = "cms::menu://sysmenu$root/siteview$module/formManager$module";
    /**
     * 标签管理的模块路径
     */
    private String tagParentPath = "cms::menu://sysmenu$root/siteview$module/tagManager$module";
    /**
     * 字典管理模块路径
     */
    private String dictParentPath = "cms::menu://sysmenu$root/siteview$module/dictManager$module";
    /**
     * 文档采集模板管理模块路径
     */
    private String docTplParentPath = "cms::menu://sysmenu$root/siteview$module/docTplManager$module";
    
    /**
     * 水印图片管理模块路径
     */
    private String imageParentPath = "cms::menu://sysmenu$root/siteview$module/imageManager$module";
    
    /**
     * 流程管理模块路径
     */
    private String workflowParentPath = "cms::menu://sysmenu$root/siteview$module/workflowManager$module";
    
    /**
     * 全文检索管理
     */
    private String textSearchParentPath = "cms::menu://sysmenu$root/siteview$module/textsearchManager$module";
    
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        subsystem = this.accessControl.getCurrentSystemID();
//        subsystem = FrameworkServlet.getSubSystem(this.request,this.response,this.accessControl.getUserAccount());
        if(menuHelper == null )
            menuHelper = MenuHelper.getMenuHelper(request);//new MenuHelper(subsystem,accessControl);
        else
            menuHelper.resetControl(accessControl);
    }
    
	public boolean hasSon(ITreeNode father) 
	{
		String treeID = father.getId();		
		

		String type = father.getType();

		try {  
			if (father.isRoot()) {  
				return true;
			}
			
			if (type.equals("form")) {  
				Module form = menuHelper.getModule(formParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("tag")) {  
				Module form = menuHelper.getModule(tagParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("dict")) {  
				Module form = menuHelper.getModule(dictParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("docTpl")) {  
				Module form = menuHelper.getModule(docTplParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("image")) {  
				Module form = menuHelper.getModule(imageParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("workflow")) {  
				Module form = menuHelper.getModule(workflowParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("textSearch")) {  
				Module form = menuHelper.getModule(textSearchParentPath);
				if(form == null)
					return false;
				if(form.getItems().size() == 0)
					return false;
				
				return true;
			}
//			if (type.equals("exportTepl")) {  
//				Module form = menuHelper.getModule(exportTeplParentPath);
//				if(form == null)
//					return false;
//				if(form.getItems().size() == 0)
//					return false;
//				
//				return true;
//			}
			if (type.equals("site")) { 
				return SiteCacheManager.getInstance().hasSubSite(treeID);
//				SiteManager smi = new SiteManagerImpl();
//				if (!father.isRoot()){
//					treeID = treeID.split(":")[1];
//					List sitelist = smi.getDirectSubSiteList(treeID);
//					if(sitelist!=null&&sitelist.size()>0)
//						return true;
//					else
//						return false;
//				}
//				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 资源管理树id：ResourceManager
	 * 站点id=site:siteid
	 * 频道id=channelid:site:siteid
	 * 频道根id=channel:site:siteid
	 */
	public boolean setSon(ITreeNode father, int curLevel) 
	{
		String treeID = father.getId();
		
		try {
			String parentSiteid = "0";
			String type = father.getType();
			
			//添加个人工作台
			if(father.isRoot()){
				ModuleQueue modules = menuHelper.getSubModules(this.parentPath);
				
				for(int i = 0; i < modules.size(); i++)
				{
					//添加"表单管理"节点
					if(modules.getModule(i).getPath().equals(this.formParentPath))
						addNode(father, "Form:1", "表单管理", "form",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					//添加"表单管理"节点
					if(modules.getModule(i).getPath().equals(this.tagParentPath))
						addNode(father, "Tag:1", "标签管理", "tag",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					//添加"字典管理"节点
					if(modules.getModule(i).getPath().equals(this.dictParentPath))
						addNode(father, "dict:1", "字典管理", "dict",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					//添加"文档采集模板管理"节点
					if(modules.getModule(i).getPath().equals(this.docTplParentPath))
						addNode(father, "docTpl:1", "文档采集模板管理", "docTpl",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					//添加"水印图片管理"节点
					if(modules.getModule(i).getPath().equals(this.imageParentPath))
						addNode(father, "image:1", "图片管理", "image",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					//添加"流程管理"节点
					if(modules.getModule(i).getPath().equals(this.workflowParentPath))
						addNode(father, "workflow:1", "流程管理", "workflow",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					//添加"全文检索管理"节点
					if(modules.getModule(i).getPath().equals(this.textSearchParentPath))
						addNode(father, "textSearch:1", "全文检索管理", "textSearch",
							false, curLevel, (String)null,(String)null,(String)null,(Map)null);
					
				}
			}
			if(type.equals("form"))
			{
				Module publish = menuHelper.getModule(formParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					String workspaceContent = null;
		            if(!item.hasWorkspaceContentVariables()){
		            	workspaceContent = item.getWorkspaceContent();
		            }else{
		            	workspaceContent = Framework.getWorkspaceContent(item, accessControl);
		            }
					Map map = new HashMap();
					map.put("nodeLink", workspaceContent);
					addNode(father, "formitem:" + item.getId(), item.getName(request), "formitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("tag"))
			{
				Module publish = menuHelper.getModule(tagParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "tagitem:" + item.getId(), item.getName(request), "tagitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("dict"))
			{
				Module publish = menuHelper.getModule(dictParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "dictitem:" + item.getId(), item.getName(request), "dictitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("docTpl"))
			{
				Module publish = menuHelper.getModule(docTplParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "docTplitem:" + item.getId(), item.getName(request), "docTplitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("image"))
			{
				Module publish = menuHelper.getModule(imageParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "imageitem:" + item.getId(), item.getName(request), "imageitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("workflow"))
			{
				Module publish = menuHelper.getModule(workflowParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "workflowitem:" + item.getId(), item.getName(request), "workflowitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("textSearch"))
			{
				Module publish = menuHelper.getModule(textSearchParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "textSearchitem:" + item.getId(), item.getName(request), "textSearchitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			//exportTepl
//			if(type.equals("exportTepl"))
//			{
//				Module publish = menuHelper.getModule(exportTeplParentPath);
//				ItemQueue items = publish.getItems();
//				for(int i = 0; i < items.size();i ++)
//				{
//					Item item = items.getItem(i);
//					Map map = new HashMap();
//					map.put("nodeLink", item.getWorkspaceContent());
//					addNode(father, "exportTeplitem:" + item.getId(), item.getName(), "exportTeplitem",
//							true, curLevel, (String)null,(String)null,(String)null,map);
//					
//				}
//				
//				return true;
//			}
			
			//加载站点
			if (father.isRoot() || father.getType().equals("site") || type.equals("site")) 
			{
				//SiteManager smi = new SiteManagerImpl();
				List sitelist = null;

				if (!father.isRoot()){
					parentSiteid = treeID.split(":")[1];
					//sitelist = smi.getSubSiteList(parentSiteid);
					sitelist = SiteCacheManager.getInstance().getSubSites(parentSiteid);
				}else{
					//sitelist = smi.getTopSubSiteList();
					sitelist = SiteCacheManager.getInstance().getSubSites("0");
				}

				if (sitelist != null && sitelist.size()>0) {
					Iterator iterator = sitelist.iterator();

					while (iterator.hasNext()) {
						Site site = (Site) iterator.next();
						String siteName = site.getName();
						String siteId = ""+site.getSiteId();
						Map map = new HashMap();
						map.put("siteId", siteId);
						map.put("siteName", site.getName());
						map.put("resName", siteName);
						map.put("isShowModeWindow", "1");

						if (accessControl.checkPermission(
								""+site.getSiteId(),
								AccessControl.WRITE_PERMISSION,
								AccessControl.SITE_RESOURCE)) {
							ITreeNode tt = addNode(father, "site:" + site.getSiteId(),
									siteName, "site", true, curLevel,
									(String) null, (String) null,
									(String) null, map);
								
								//加载站点节点的时候加载右键菜单
								Menu sitemenu = new Menu();
								sitemenu.setIdentity("site");
								sitemenu.addContextMenuItem(Menu.MENU_EXPAND);
								sitemenu.addSeperate();
//								// 判断是否有新建子站点权限
//								if (super.accessControl.checkPermission(siteId,
//										AccessControl.ADD_PERMISSION,
//										AccessControl.SITE_RESOURCE)) {
//									Menu.ContextMenuItem sitemenuitem = new Menu.ContextMenuItem();
//									sitemenuitem.setName("建子站点");
//									sitemenuitem.setLink("javascript:addSubSite('"
//											+ site.getSiteId() + "','"
//											+ site.getName() + "','" + siteName
//											+ "');");
//									sitemenuitem
//									.setIcon(request.getContextPath()
//											+ "/sysmanager/images/rightMemu/site_newson.gif");
//									sitemenu.addContextMenuItem(sitemenuitem);
//								}
								//判断是否有编辑站点权限
								if (super.accessControl.checkPermission(siteId, AccessControl.UPDATE_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
									sitemenuitem0.setName("编辑站点");
									sitemenuitem0.setLink("javascript:editSite('"+siteId+"')");
									sitemenuitem0.setIcon(request.getContextPath() + "/sysmanager/images/rightMemu/site_edit.gif");
									sitemenu.addContextMenuItem(sitemenuitem0);
								}
								//判断是否有删除站点权限
								if (super.accessControl.checkPermission(siteId, AccessControl.DELETE_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
									sitemenuitem0.setName("删除站点");
									sitemenuitem0.setLink("javascript:deleteSite('"+siteId+"','"+siteName+"')");
									sitemenuitem0.setIcon(request.getContextPath() + "/sysmanager/images/rightMemu/site_del.gif");
									sitemenu.addContextMenuItem(sitemenuitem0);
								}
								//判断是否有设置站点流程权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITE_WORKFLOW_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
									sitemenuitem1.setName("站点流程");
									sitemenuitem1.setLink("javascript:setSiteFlow('"+siteId+"','"+siteName+"')");
									sitemenuitem1.setTarget("base_properties_content");
									sitemenuitem1.setIcon(request.getContextPath() + "/sysmanager/images/rightMemu/site_workflow.gif");
									sitemenu.addContextMenuItem(sitemenuitem1);
								}
								//判断是否有模板管理的权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITE_TEMPMANAGER_PERMISSION,
										AccessControl.SITETPL_RESOURCE)) {
								Menu.ContextMenuItem sitemenuitem2 = new Menu.ContextMenuItem();
								sitemenuitem2.setName("模板管理");
								sitemenuitem2.setLink(request.getContextPath() + "/cms/templateManage/template_main.jsp");
								sitemenuitem2.setTarget("base_properties_content");
								sitemenuitem2.setIcon(request.getContextPath() + "/sysmanager/images/rightMemu/site_tpl.gif");
								sitemenu.addContextMenuItem(sitemenuitem2);
								addContextMenuOfType(sitemenu);
								}
								/*
								//判断是否有完全发布的权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITEBYALL_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
								Menu.ContextMenuItem sitemenuitem3 = new Menu.ContextMenuItem();
								sitemenuitem3.setName("完全发布");
								sitemenuitem3.setLink("javascript:publishSiteByAll('"+siteId+"')");
								sitemenuitem3.setTarget("base_properties_content");
								sitemenuitem3.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
								sitemenu.addContextMenuItem(sitemenuitem3);
								addContextMenuOfType(sitemenu);
								}
								//判断是否有增量发布的权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITEBYINC_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
								Menu.ContextMenuItem sitemenuitem4 = new Menu.ContextMenuItem();
								sitemenuitem4.setName("增量发布");
								sitemenuitem4.setLink("javascript:publishSiteByInc('"+siteId+"')");
								sitemenuitem4.setTarget("base_properties_content");
								sitemenuitem4.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
								sitemenu.addContextMenuItem(sitemenuitem4);
								addContextMenuOfType(sitemenu);
								}
								//判断是否有发布的权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITEPUBLISH_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem3 = new Menu.ContextMenuItem();
									sitemenuitem3.setName("发布");
									sitemenuitem3.setLink("javascript:publishSite('"+siteId+"')");
									sitemenuitem3.setTarget("base_properties_content");
									sitemenuitem3.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
									sitemenu.addContextMenuItem(sitemenuitem3);
									addContextMenuOfType(sitemenu);
								}
								*/
								super.addContextMenuOfNode(tt,sitemenu);
							
						} else {

							if (accessControl.checkPermission(
									""+site.getSiteId(),
									AccessControl.READ_PERMISSION,
									AccessControl.SITE_RESOURCE)) {

								 addNode(father, "site:"
										+ site.getSiteId(), siteName, "site",
										true, curLevel, (String) null,
										(String) null, (String) null, map);
							
							}

						}
					}
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;

	}
	
	protected void buildContextMenus() {
 
	}
}
