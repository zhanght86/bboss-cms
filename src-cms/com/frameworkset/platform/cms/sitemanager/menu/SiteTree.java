package com.frameworkset.platform.cms.sitemanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
/**
 * 显示所有站点节点的站点树
 * @author Administrator
 *
 */
public class SiteTree extends COMTree  implements java.io.Serializable
{
	 private MenuHelper menuHelper;
	 private String subsystem;
    /**
     * 个人工作台的模块路径
     */
    private String myworkParentPath = "cms::menu://sysmenu$root/personalconsole$module";
    /**
     * 归档文档的模块路径
     */
    private String pigeonholeParentPath = "cms::menu://sysmenu$root/pigeonholeManager$module";   
    /**
     * 归档文档的模块路径
     */
    private String garbageStationParentPath = "cms::menu://sysmenu$root/garbageStation$module";
    /**
     * 模板管理的模块路径
     */
    private String publishParentPath = "cms::menu://sysmenu$root/publishManager$module";
    /**
     * 表单管理的模块路径
     */
    private String formParentPath = "cms::menu://sysmenu$root/formManager$module";
	
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        subsystem = FrameworkServlet.getSubSystem(this.request,this.response,this.accessControl.getUserAccount());
        if(menuHelper == null )
            menuHelper = new MenuHelper(subsystem,accessControl);
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
			
			if (type.equals("mywork")) {  
				Module mywork = menuHelper.getModule(myworkParentPath);
				if(mywork == null)
					return false;
				if(mywork.getItems().size() == 0)
					return false;
				
				return true;
			}
			if (type.equals("pigeonhole")) {  
				Module mywork = menuHelper.getModule(pigeonholeParentPath);
				if(mywork == null)
					return false;
				if(mywork.getItems().size() == 0)
					return false;
				return true;
			}
			if (type.equals("garbage")) {  
				Module mywork = menuHelper.getModule(garbageStationParentPath);
				if(mywork == null)
					return false;
				if(mywork.getItems().size() == 0)
					return false;
				return true;
			}
			if (type.equals("publish")) {  
				Module publish = menuHelper.getModule(publishParentPath);
				if(publish == null)
					return false;
				if(publish.getItems().size() == 0)
					return false;
				
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
			
			
			if(type.equals("myworkitem"))
				return false;
			
			if(type.equals("pigeonholeitem"))
				return false;
			if(type.equals("garbageitem"))
				return false;
			
			if(type.equals("publishitem"))
				return false;
			
			if(type.equals("formitem"))
				return false;
			
			if (type.equals("site")) { 
				/**
				 * 1.频道 2.图片库 3.样式库 4. 模板
				 */
				return true;
				
			} else if (type.equals("channel")) {
				//ChannelManager channelManager = new ChannelManagerImpl();
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[2]);
				
				return cm.hasSubChannel(tmp[0]);
			}else if (type.equals("channelroot")) {
				//System.out.println("treeId:"+treeID);
				//SiteManager siteManager = new SiteManagerImpl();
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[2]);
				return cm.hasSubChannel("0");
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
				//添加"个人工作台"节点
				addNode(father, "myWork:1", "个人工作台", "mywork",
						false, curLevel, (String)null,(String)null,(String)null,(Map)null);
				
				//添加"归档管理"节点
				addNode(father, "pigeonhole:1", "归档管理", "pigeonhole",
						false, curLevel, (String)null,(String)null,(String)null,(Map)null);
				
				//添加"回收站"节点
				addNode(father, "garbage:1", "回收站", "garbage",
						false, curLevel, (String)null,(String)null,(String)null,(Map)null);
				
				//添加"发布管理"节点
				addNode(father, "Publish:1", "发布管理", "publish",
						false, curLevel, (String)null,(String)null,(String)null,(Map)null);
				
				//添加"表单管理"节点
				addNode(father, "Form:1", "表单管理", "form",
						false, curLevel, (String)null,(String)null,(String)null,(Map)null);
				
				//添加顶级站点
			}
			
			if(type.equals("mywork"))
			{
				Module mywork = menuHelper.getModule(myworkParentPath);
				ItemQueue items = mywork.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					String workspaceContent = null;
		            if(!item.hasWorkspaceContentVariables()){
		            	workspaceContent = item.getWorkspaceContent();
		            }else{
		            	workspaceContent = Framework.getWorkspaceContent(item, accessControl);
		            }
//					map.put("nodeLink", menuHelper.getWorkspaceUrl(null, item, null));
					map.put("nodeLink", workspaceContent);
					addNode(father, "myworkitem:" + item.getId(), item.getName(request), "myworkitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("pigeonhole"))
			{
				Module pigeonhole = menuHelper.getModule(pigeonholeParentPath);
				ItemQueue items = pigeonhole.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i); 
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "pigeonholeitem:" + item.getId(), item.getName(request), "pigeonholeitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
				}
				
				return true;
			}
			if(type.equals("garbage"))
			{
				Module pigeonhole = menuHelper.getModule(garbageStationParentPath);
				ItemQueue items = pigeonhole.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i); 
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "garbageitem:" + item.getId(), item.getName(request), "garbageitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
				}
				
				return true;
			}
			if(type.equals("publish"))
			{
				Module publish = menuHelper.getModule(publishParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "publishitem:" + item.getId(), item.getName(request), "publishitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			if(type.equals("form"))
			{
				Module publish = menuHelper.getModule(formParentPath);
				ItemQueue items = publish.getItems();
				for(int i = 0; i < items.size();i ++)
				{
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "formitem:" + item.getId(), item.getName(request), "formitem",
							true, curLevel, (String)null,(String)null,(String)null,map);
					
				}
				
				return true;
			}
			
			
			//加载站点
			if(type.equals("site"))
			{
						
				
				
				String[] tmp = treeID.split(":");
				String siteid = tmp[1];
				SiteManager siteManager = new SiteManagerImpl();
				Site site = siteManager.getSiteInfo(siteid);
				String sitename = site.getName();
				Map map = new HashMap();
				map.put("siteid",siteid);
				map.put("sitename",sitename);
				ITreeNode channelRoot = addNode(father, "channel:" + treeID, "频道", "channelroot",
						false, curLevel, (String) null, (String) null,
						(String) null, map);
				//增加频道管理的右键菜单
				
				//判断频道管理节点是否有新建普通频道的右键菜单
				Menu channelrootmenu = new Menu();
				if (super.accessControl.checkPermission(siteid, AccessControl.CHANNELROOT_ADDCHANNEL_PERMISSION,
						AccessControl.SITE_RESOURCE)) {
				
				channelrootmenu.setIdentity("channelroot");
				Menu.ContextMenuItem addOrdinaryCh = new Menu.ContextMenuItem();
				addOrdinaryCh.setName("新建频道");
				addOrdinaryCh.setLink("javascript:addChannel('"+siteid+"','"+site.getName()+"')");
				addOrdinaryCh.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
				channelrootmenu.addContextMenuItem(addOrdinaryCh);
				}
				super.addContextMenuOfNode(channelRoot, channelrootmenu);
				
			}
			
			
			//加载子站点
			if (father.isRoot() || father.getType().equals("site")) 
			{
				//SiteManager smi = new SiteManagerImpl();
				List sitelist = null;

				if (!father.isRoot()){
					parentSiteid = treeID.split(":")[1];
					sitelist = SiteCacheManager.getInstance().getSubSites(parentSiteid);
				}else{
					sitelist = SiteCacheManager.getInstance().getSubSites("0");
				}

				if (sitelist != null) {
					Iterator iterator = sitelist.iterator();

					while (iterator.hasNext()) {
						Site site = (Site) iterator.next();
						String siteName = site.getName();
						String siteId = ""+site.getSiteId();
						Map map = new HashMap();
						map.put("siteId", siteId);
						map.put("siteName", site.getName());
						map.put("resName", siteName);


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
								//判断是否有新建子站点权限
								if (super.accessControl.checkPermission(siteId, AccessControl.ADD_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem = new Menu.ContextMenuItem();
									sitemenuitem.setName("新建子站点");
									sitemenuitem.setLink("javascript:addSubSite('"+
											site.getSiteId()+"','"+site.getName()+"','"+
											siteName+"');");
									sitemenuitem.setIcon(request.getContextPath()
											+ "/sysmanager/images/actions.gif");
									sitemenu.addContextMenuItem(sitemenuitem);
								}
								//判断是否有编辑站点权限
								if (super.accessControl.checkPermission(siteId, AccessControl.UPDATE_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
									sitemenuitem0.setName("编辑站点");
									sitemenuitem0.setLink("javascript:editSite('"+siteId+"')");
									sitemenuitem0.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
									sitemenu.addContextMenuItem(sitemenuitem0);
								}
								//判断是否有设置站点流程权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITE_WORKFLOW_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
									Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
									sitemenuitem1.setName("设置站点流程");
									sitemenuitem1.setLink("javascript:setSiteFlow('"+siteId+"','"+siteName+"')");
									sitemenuitem1.setTarget("base_properties_content");
									sitemenuitem1.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
									sitemenu.addContextMenuItem(sitemenuitem1);
								}
								//判断是否有模板管理的权限
								if (super.accessControl.checkPermission(siteId, AccessControl.SITE_TEMPMANAGER_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
								Menu.ContextMenuItem sitemenuitem2 = new Menu.ContextMenuItem();
								sitemenuitem2.setName("模板管理");
								sitemenuitem2.setLink(request.getContextPath() + "/cms/templateManage/template_main.jsp");
								sitemenuitem2.setTarget("base_properties_content");
								sitemenuitem2.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
								sitemenu.addContextMenuItem(sitemenuitem2);
								addContextMenuOfType(sitemenu);
								}
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
//								判断是否有增量发布的权限
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
//			else if(type.equals("image"))
//			{
//				
//			}
//			else if(type.equals("style"))
//			{
//				
//			}
			else if(type.equals("channelroot")){
				String[] tmp = treeID.split(":");
				String siteId = tmp[2];
				//SiteManager siteManager = new SiteManagerImpl();
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
				String siteName = SiteCacheManager.getInstance().getSite(siteId).getName();
				//ChannelManager channelManager = new ChannelManagerImpl();
				List channels = cm.getSubChannels("0");
				if (channels != null) {
					Iterator iterator = channels.iterator();

					while (iterator.hasNext()) {
						Channel channel = (Channel) iterator.next();
						String treeName = channel.getName();
						String channelId = String.valueOf(channel.getChannelId());
						Map map = new HashMap();
						map.put("channelId", String.valueOf(channel.getChannelId()));
						map.put("channelName",channel.getName());
						map.put("siteid",tmp[2]);
						map.put("sitename",siteName);
						map.put("resName", treeName);
						map.put("nodeLink","/cms/channelManage/channel_doc_list.jsp");
						if (accessControl.checkPermission(channelId,AccessControl.WRITE_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
						ITreeNode channelNode = addNode(father, channel.getChannelId() + ":" + siteId, treeName, "channel",
								true, curLevel, (String) null, (String) null,
								(String) null, map);
						//Menu channelmenu = this.addContextMenuOffChannel(siteId,siteName,""+channel.getChannelId(),channel.getName());
						//super.addContextMenuOfNode(channelNode,menu);
						Menu channelmenu = new Menu();
						channelmenu.setIdentity("channel");
						if(cm.hasSubChannel("0")){
							channelmenu.addContextMenuItem(Menu.MENU_EXPAND);
							channelmenu.addSeperate();
						}
					
						//判断是否有新建子频道权限
						if (super.accessControl.checkPermission(channelId, AccessControl.ADD_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem = new Menu.ContextMenuItem();
						channelmenuitem.setName("新建子频道");
						channelmenuitem.setLink("javascript:addChannel('"+siteId+"','"+siteName+"','"+channelId+"','"+treeName+"')");
						channelmenuitem.setTarget("base_properties_content");
						channelmenuitem.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem);
						}
						//判断是否有删除频道权限
						if (super.accessControl.checkPermission(channelId, AccessControl.DELETE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem1 = new Menu.ContextMenuItem();
						channelmenuitem1.setName("删除频道");
						channelmenuitem1.setLink("javascript:deleteChannel('"+siteId+"','"+siteName+"','"+channelId+"','"+treeName+"')");
						channelmenuitem1.setTarget("base_properties_content");
						channelmenuitem1.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem1);
						}
						//判断是否有修改频道权限
						if (super.accessControl.checkPermission(channelId, AccessControl.UPDATE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
						channelmenuitem2.setName("编辑频道");
						channelmenuitem2.setLink("javascript:editChannel('"+siteId+"','"+siteName+"','"+channelId+"','"+treeName+"')");
						channelmenuitem2.setTarget("base_properties_content");
						channelmenuitem2.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem2);
					
						}
						//判断是否有改变频道流程权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CHANNEL_WORKFLOW_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
						channelmenuitem3.setName("改变频道流程");
						channelmenuitem3.setLink("javascript:changeWorkflow('"+siteId+"','"+siteName+"','"+channelId+"','"+treeName+"')");
						channelmenuitem3.setTarget("base_properties_content");
						channelmenuitem3.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem3);
						}
						//判断是否有完全发布的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CHNLBYALL_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem4 = new Menu.ContextMenuItem();
						channelmenuitem4.setName("完全发布");
						channelmenuitem4.setLink("javascript:publishChannelByAll('"+channelId+"')");
						channelmenuitem4.setTarget("base_properties_content");
						channelmenuitem4.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem4);
						}
						//判断是否有增量发布的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CHNLBYINC_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem5 = new Menu.ContextMenuItem();
						channelmenuitem5.setName("增量发布");
						channelmenuitem5.setLink("javascript:publishChannelByInc('"+channelId+"')");
						channelmenuitem5.setTarget("base_properties_content");
						channelmenuitem5.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem5);
						}
						//判断是否有复制文档的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.COPYDOC_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem6 = new Menu.ContextMenuItem();
						channelmenuitem6.setName("复制文档");
						channelmenuitem6.setLink("javascript:copyDoc('"+siteId+"','"+channelId+"')");						
						channelmenuitem6.setTarget("base_properties_content");
						channelmenuitem6.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem6);
						}
						
						//判断是否有置顶管理的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.ARRANGE_DOCM,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem7 = new Menu.ContextMenuItem();
						channelmenuitem7.setName("置顶管理");
						channelmenuitem7.setLink("javascript:arrangeDocM('"+channelId+"')");						
						channelmenuitem7.setTarget("base_properties_content");
						channelmenuitem7.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem7);
						}
						//	判断是否有引用文档管理的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CITEDOC_MANAGER,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem8 = new Menu.ContextMenuItem();
						channelmenuitem8.setName("引用文档管理");
						channelmenuitem8.setLink("javascript:citeDocManager('"+channelId+"')");						
						channelmenuitem8.setTarget("base_properties_content");
						channelmenuitem8.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem8);
						}
						
						super.addContextMenuOfNode(channelNode,channelmenu);
						
						}else{
							 if (accessControl.checkPermission(channelId,
		                                AccessControl.READ_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
		                        	
		                        	addNode(father, channel.getChannelId() + ":" + siteId, treeName, "channel",
		    								true, curLevel, (String) null, (String) null,
		    								(String) null, map);
		                           
		                        }
						}
					}
				}
			}
			
			else if(type.equals("channel"))
			{
				String[] tmp = treeID.split(":");
				String siteId = tmp[1];
				String channelId = tmp[0];
				//SiteManager siteManager = new SiteManagerImpl();
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(siteId);
				String siteName = SiteCacheManager.getInstance().getSite(siteId).getName();
				//ChannelManager channelManager = new ChannelManagerImpl();
				
				List channels = cm.getSubChannels(channelId);
				if (channels != null) {
					Iterator iterator = channels.iterator();

					while (iterator.hasNext()) {
						Channel channel = (Channel) iterator.next();
						String treeName = channel.getName();
						String currChannelId = String.valueOf(channel.getChannelId());
						Map map = new HashMap();
						map.put("channelId", currChannelId);
						map.put("channelName", channel.getName());
						map.put("resName", treeName);
						map.put("siteid",tmp[1]);
						map.put("sitename",siteName);
						map.put("nodeLink","/cms/channelManage/channel_doc_list.jsp");
						if (accessControl.checkPermission(currChannelId,AccessControl.WRITE_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
						ITreeNode channelNode = addNode(father, String.valueOf(channel.getChannelId()) + ":" + siteId, treeName, "channel",
								true, curLevel, (String) null, (String) null,
								(String) null, map);
						//Menu menu = this.addContextMenuOffChannel(siteid,sitename,""+channel.getChannelId(),channel.getName());
						//super.addContextMenuOfNode(channelNode,menu);
						Menu channelmenu = new Menu();
						channelmenu.setIdentity("channel");
						if(cm.hasSubChannel(currChannelId)){
						channelmenu.addContextMenuItem(Menu.MENU_EXPAND);
						channelmenu.addSeperate();
						}
						//判断是否有新建子频道权限
						if (super.accessControl.checkPermission(currChannelId, AccessControl.ADD_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem = new Menu.ContextMenuItem();
						channelmenuitem.setName("新建子频道");
						channelmenuitem.setLink("javascript:addChannel('"+siteId+"','"+siteName+"','"+currChannelId+"','"+treeName+"')");
						channelmenuitem.setTarget("base_properties_content");
						channelmenuitem.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem);
						}
						//判断是否有删除频道权限
						if (super.accessControl.checkPermission(currChannelId, AccessControl.DELETE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem1 = new Menu.ContextMenuItem();
						channelmenuitem1.setName("删除频道");
						channelmenuitem1.setLink("javascript:deleteChannel('"+siteId+"','"+siteName+"','"+currChannelId+"','"+treeName+"')");
						channelmenuitem1.setTarget("base_properties_content");
						channelmenuitem1.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem1);
						}
						//判断是否有修改频道权限
						if (super.accessControl.checkPermission(currChannelId, AccessControl.UPDATE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
						channelmenuitem2.setName("编辑频道");
						channelmenuitem2.setLink("javascript:editChannel('"+siteId+"','"+siteName+"','"+currChannelId+"','"+treeName+"')");
						channelmenuitem2.setTarget("base_properties_content");
						channelmenuitem2.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem2);
						}
						//判断是否有改变频道流程权限
						if (super.accessControl.checkPermission(currChannelId, AccessControl.CHANNEL_WORKFLOW_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
						channelmenuitem3.setName("改变频道流程");
						channelmenuitem3.setLink("javascript:changeWorkflow('"+siteId+"','"+siteName+"','"+currChannelId+"','"+treeName+"')");
						channelmenuitem3.setTarget("base_properties_content");
						channelmenuitem3.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem3);
						}
//						判断是否有完全发布的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CHNLBYALL_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem4 = new Menu.ContextMenuItem();
						channelmenuitem4.setName("完全发布");
						channelmenuitem4.setLink("javascript:publishChannelByAll('"+channelId+"')");
						channelmenuitem4.setTarget("base_properties_content");
						channelmenuitem4.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem4);
						}
//						判断是否有增量发布的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CHNLBYINC_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem5 = new Menu.ContextMenuItem();
						channelmenuitem5.setName("增量发布");
						channelmenuitem5.setLink("javascript:publishChannelByInc('"+channelId+"')");
						channelmenuitem5.setTarget("base_properties_content");
						channelmenuitem5.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem5);
						}
						//判断是否有复制文档的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.COPYDOC_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem6 = new Menu.ContextMenuItem();
						channelmenuitem6.setName("复制文档");
						channelmenuitem6.setLink("javascript:copyDoc('"+siteId+"','"+channelId+"')");
						channelmenuitem6.setTarget("base_properties_content");
						channelmenuitem6.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem6);
						}
						//判断是否有置顶管理的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.ARRANGE_DOCM,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem7 = new Menu.ContextMenuItem();
						channelmenuitem7.setName("置顶管理");
						channelmenuitem7.setLink("javascript:arrangeDocM('"+channelId+"')");						
						channelmenuitem7.setTarget("base_properties_content");
						channelmenuitem7.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem7);
						}
						//判断是否有引用文档管理的权限
						if (super.accessControl.checkPermission(channelId, AccessControl.CITEDOC_MANAGER,
								AccessControl.CHANNEL_RESOURCE)) {
						Menu.ContextMenuItem channelmenuitem8 = new Menu.ContextMenuItem();
						channelmenuitem8.setName("引用文档管理");
						channelmenuitem8.setLink("javascript:citeDocManager('"+channelId+"')");						
						channelmenuitem8.setTarget("base_properties_content");
						channelmenuitem8.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
						channelmenu.addContextMenuItem(channelmenuitem8);
						}
						
						super.addContextMenuOfNode(channelNode,channelmenu);
						
						}else{
							 if (accessControl.checkPermission(currChannelId,
		                                AccessControl.READ_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
								 addNode(father, String.valueOf(channel.getChannelId()) + ":" + siteId, treeName, "channel",
											true, curLevel, (String) null, (String) null,
											(String) null, map);
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
	
	/**
	 * 增加频道的右键菜单
	 */
	
//	private Menu addContextMenuOffChannel(String siteId,String siteName,String channelId,String channelName){
//		String defaultTarget = "base_properties_content";
		
		//TODO 可能还要添加权限控制
//		Menu channelmenu = new Menu();
//		channelmenu.setIdentity("channel");
//		channelmenu.addContextMenuItem(Menu.MENU_OPEN);
//		channelmenu.addContextMenuItem(Menu.MENU_EXPAND);
//		channelmenu.addSeperate();
//		Menu.ContextMenuItem channelmenuitem = new Menu.ContextMenuItem();
//		channelmenuitem.setName("新建子频道");
//		channelmenuitem.setLink("javascript:addChannel('"+siteId+"','"+siteName+"','"+channelId+"','"+channelName+"')");
//		channelmenuitem.setTarget(defaultTarget);
//		channelmenuitem.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
//		channelmenu.addContextMenuItem(channelmenuitem);
//		
//		Menu.ContextMenuItem channelmenuitem1 = new Menu.ContextMenuItem();
//		channelmenuitem1.setName("删除频道");
//		channelmenuitem1.setLink("javascript:deleteChannel('"+siteId+"','"+siteName+"','"+channelId+"','"+channelName+"')");
//		channelmenuitem1.setTarget(defaultTarget);
//		channelmenuitem1.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
//		channelmenu.addContextMenuItem(channelmenuitem1);
//		
//		Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
//		channelmenuitem2.setName("编辑频道");
//		channelmenuitem2.setLink("javascript:editChannel('"+siteId+"','"+siteName+"','"+channelId+"','"+channelName+"')");
//		channelmenuitem2.setTarget(defaultTarget);
//		channelmenuitem2.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
//		channelmenu.addContextMenuItem(channelmenuitem2);
//		addContextMenuOfType(channelmenu);	
//
//		Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
//		channelmenuitem3.setName("改变频道流程");
//		channelmenuitem3.setLink("javascript:changeWorkflow('"+siteId+"','"+siteName+"','"+channelId+"','"+channelName+"')");
//		channelmenuitem3.setTarget(defaultTarget);
//		channelmenuitem3.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
//		channelmenu.addContextMenuItem(channelmenuitem3);
//		addContextMenuOfType(channelmenu);			
//		return channelmenu;
//	}
	
	protected void buildContextMenus() {
 
	}
}
