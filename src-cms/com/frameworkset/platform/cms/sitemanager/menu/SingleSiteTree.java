package com.frameworkset.platform.cms.sitemanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
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
 * 只显示单个站点节点的站点树
 * 
 * @author Administrator
 * 
 */
public class SingleSiteTree extends COMTree implements java.io.Serializable {
	private MenuHelper menuHelper;

	private String subsystem;

	private CMSManager cmsmanager;

	

	/**
	 * 内容管理视图模块路径
	 */
	private String parentPath = "cms::menu://sysmenu$root/contentview$module";
	/**
	 * 个人工作台的模块路径
	 */
	private String myworkParentPath = "cms::menu://sysmenu$root/contentview$module/personalconsole$module";

	/**
	 * 归档文档的模块路径
	 */
	private String pigeonholeParentPath = "cms::menu://sysmenu$root/contentview$module/pigeonholeManager$module";

	/**
	 * 回收文档的模块路径
	 */
	private String garbageStationParentPath = "cms::menu://sysmenu$root/contentview$module/garbageStation$module";

	/**
	 * 模板管理的模块路径
	 */
	private String publishParentPath = "cms::menu://sysmenu$root/contentview$module/publishManager$module";

	/**
	 * 稿源管理的模块路径
	 */
	private String docsourceParentPath = "cms::menu://sysmenu$root/contentview$module/docsourceManager$module";

	/**
	 * 表单管理的模块路径
	 */
	// private String formParentPath =
	// "cms::menu://sysmenu$root/formManager$module";
	public void setPageContext(PageContext context) {
		super.setPageContext(context);
//		subsystem = FrameworkServlet.getSubSystem(this.request, this.response,
//				this.accessControl.getUserAccount());
		subsystem = super.accessControl.getCurrentSystemID();
		if (menuHelper == null)
			menuHelper = MenuHelper.getMenuHelper(request);
		else
			menuHelper.resetControl(accessControl);
	}

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		// System.out.print(">>>>>>>"+treeID);
		String type = father.getType();

		try {
			if (father.isRoot()) {
				return true;
			}

			if (type.equals("mywork")) {
				Module mywork = menuHelper.getModule(myworkParentPath);
				if (mywork == null)
					return false;
				if (mywork.getItems().size() == 0)
					return false;

				return true;
			}
			if (type.equals("pigeonhole")) {
				Module mywork = menuHelper.getModule(pigeonholeParentPath);
				if (mywork == null)
					return false;
				if (mywork.getItems().size() == 0)
					return false;
				return true;
			}
			if (type.equals("garbage")) {
				Module mywork = menuHelper.getModule(garbageStationParentPath);
				if (mywork == null)
					return false;
				if (mywork.getItems().size() == 0)
					return false;
				return true;
			}
			if (type.equals("publish")) {
				Module publish = menuHelper.getModule(publishParentPath);
				if (publish == null)
					return false;
				if (publish.getItems().size() == 0)
					return false;

				return true;
			}
			if (type.equals("docsource")) {
				Module publish = menuHelper.getModule(docsourceParentPath);
				if (publish == null)
					return false;
				if (publish.getItems().size() == 0)
					return false;

				return true;
			}
			/*
			 * if (type.equals("form")) { Module form =
			 * menuHelper.getModule(formParentPath); if(form == null) return
			 * false; if(form.getItems().size() == 0) return false;
			 * 
			 * return true; }
			 */

			if (type.equals("myworkitem"))
				return false;

			if (type.equals("pigeonholeitem"))
				return false;
			if (type.equals("garbageitem"))
				return false;

			if (type.equals("publishitem"))
				return false;
			/*
			 * if(type.equals("formitem")) return false;
			 */
			if (type.equals("site")) {
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
				return cm.hasSubChannel("0");//由ChannelCacheManager中规定，根为0
			}

			else if (type.equals("channel")) {
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
				return cm.hasSubChannel(tmp[0]);//由ChannelCacheManager中规定，根为0
			} else if (type.equals("channelroot")) {
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
				return cm.hasSubChannel("0");//由ChannelCacheManager中规定，根为0
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 资源管理树id：ResourceManager 站点id=site:siteid 频道id=channelid:site:siteid
	 * 频道根id=channel:site:siteid
	 */
	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		SiteManager smi = new SiteManagerImpl();
		//DocCommentManager ddm = new DocCommentManagerImpl();
		//开通/关闭文档评论控制开关
		String isCommentsOpen = ConfigManager.getInstance().getConfigValue("cms.document.comments");
		try {
			//String parentSiteid = "0";
			String type = father.getType();

			// 添加个人工作台
			if (father.isRoot()) {
				ModuleQueue modules = menuHelper.getSubModules(this.parentPath);
				
				for(int i = 0; i < modules.size(); i++)
				{
					//添加"个人工作台"节点
					if(modules.getModule(i).getPath().equals(this.myworkParentPath))
						addNode(father, "myWork:1", "个人工作台", "mywork", false, curLevel,
								(String) null, (String) null, (String) null, (Map) null);
	
					// 添加"归档管理"节点
					if(modules.getModule(i).getPath().equals(this.pigeonholeParentPath))
						addNode(father, "pigeonhole:1", "归档管理", "pigeonhole", false,
							curLevel, (String) null, (String) null, (String) null,
							(Map) null);
	
					// 添加"回收站"节点
					if(modules.getModule(i).getPath().equals(this.garbageStationParentPath))
						addNode(father, "garbage:1", "回收站", "garbage", false, curLevel,
							(String) null, (String) null, (String) null, (Map) null);
					// 稿源管理
					if(modules.getModule(i).getPath().equals(this.docsourceParentPath))
						addNode(father, "docsource:1", "稿源管理", "docsource", false,
							curLevel, (String) null, (String) null, (String) null,
							(Map) null);
				}

				// 添加"发布管理"节点
				// addNode(father, "Publish:1", "发布管理", "publish",
				// false, curLevel,
				// (String)null,(String)null,(String)null,(Map)null);

				// 添加"表单管理"节点
				/*
				 * addNode(father, "Form:1", "表单管理", "form", false, curLevel,
				 * (String)null,(String)null,(String)null,(Map)null);
				 */

				// 添加顶级站点
				cmsmanager = new CMSManager();
				cmsmanager.init(request, session, response, super.accessControl);

				String siteid = cmsmanager.getSiteID();
				if (siteid.equals("")) {
					return true;
				}

				Site site = (Site) smi.getSiteInfo(siteid);
				if(site == null)
					return true;
				String siteName = site.getName();
				String siteId = "" + site.getSiteId();
				Map map = new HashMap();
				map.put("siteId", siteId);
				map.put("siteName", site.getName());
				map.put("resName", siteName);

				if (accessControl.checkPermission("" + site.getSiteId(),
						AccessControl.WRITE_PERMISSION,
						AccessControl.SITE_RESOURCE)) {
					ITreeNode tt = addNode(father, "site:" + site.getSiteId(),
							siteName, "site", true, curLevel, (String) null,
							(String) null, (String) null, map);

					// 加载站点节点的时候加载右键菜单
					Menu sitemenu = new Menu();
					sitemenu.setIdentity("site");
					sitemenu.addContextMenuItem(Menu.MENU_EXPAND);
					sitemenu.addSeperate();
					// 判断是否有新建子站点权限
					// if (super.accessControl.checkPermission(siteId,
					// AccessControl.ADD_PERMISSION,
					// AccessControl.SITE_RESOURCE)) {
					// Menu.ContextMenuItem sitemenuitem = new
					// Menu.ContextMenuItem();
					// sitemenuitem.setName("新建子站点");
					// sitemenuitem.setLink("javascript:addSubSite('"+
					// site.getSiteId()+"','"+site.getName()+"','"+
					// siteName+"');");
					// sitemenuitem.setIcon(request.getContextPath()
					// + "/sysmanager/images/actions.gif");
					// sitemenu.addContextMenuItem(sitemenuitem);
					// }
					// 判断是否有新建频道权限
					if (super.accessControl.checkPermission(siteId,
							AccessControl.CHANNELROOT_ADDCHANNEL_PERMISSION,
							AccessControl.SITE_RESOURCE)) {
						Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
						sitemenuitem5.setName("新建频道");
						sitemenuitem5.setLink("javascript:addChannel('"
								+ siteId + "','" + siteName + "')");
						sitemenuitem5
								.setIcon(request.getContextPath()
										+ "/sysmanager/images/rightMemu/site_newchl.gif");
						sitemenu.addContextMenuItem(sitemenuitem5);
					}
					//判断是否移动频道的权限
					if (super.accessControl.isAdmin()
							|| smi.hasSiteManager(siteId, super.accessControl
									.getUserID())) {
						Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
						sitemenuitem5.setName("移动频道");
						sitemenuitem5.setLink("javascript:moveChl('" + siteId + "')");
						sitemenuitem5.setIcon(request.getContextPath()
								+ "/sysmanager/images/actions.gif");
						sitemenu.addContextMenuItem(sitemenuitem5);
					}
					// 判断是否有权限管理的权限(站点)
//					if (super.accessControl.isAdmin()
//							|| smi.hasSiteManager(siteId, super.accessControl
//									.getUserID())) {
//						Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
//						sitemenuitem5.setName("设置权限");
//						sitemenuitem5.setLink("javascript:siteSysManager('"
//								+ siteId + "','" + siteName + "')");
//						sitemenuitem5.setIcon(request.getContextPath()
//								+ "/sysmanager/images/actions.gif");
//						sitemenu.addContextMenuItem(sitemenuitem5);
//					}
					// 判断是否有权限管理的权限(站点)
					/*
					 * if
					 * (super.accessControl.isAdmin()||smi.hasSiteManager(siteId,super.accessControl.getUserID())) {
					 * Menu.ContextMenuItem sitemenuitem5 = new
					 * Menu.ContextMenuItem(); sitemenuitem5.setName("用户授权");
					 * sitemenuitem5.setLink("javascript:siteSysManaUsers('"+siteId+"','"+siteName+"')");
					 * sitemenuitem5.setIcon(request.getContextPath()
					 * +"/sysmanager/images/actions.gif");
					 * sitemenu.addContextMenuItem(sitemenuitem5); }
					 */
					// 判断是否有编辑站点权限
					// if (super.accessControl.checkPermission(siteId,
					// AccessControl.UPDATE_PERMISSION,
					// AccessControl.SITE_RESOURCE)) {
					// Menu.ContextMenuItem sitemenuitem0 = new
					// Menu.ContextMenuItem();
					// sitemenuitem0.setName("编辑站点");
					// sitemenuitem0.setLink("javascript:editSite('"+siteId+"')");
					// sitemenuitem0.setIcon(request.getContextPath() +
					// "/sysmanager/images/actions.gif");
					// sitemenu.addContextMenuItem(sitemenuitem0);
					// }
					// 判断是否有设置站点流程权限
					if (super.accessControl.checkPermission(siteId,
							AccessControl.SITE_WORKFLOW_PERMISSION,
							AccessControl.SITE_RESOURCE)) {
						Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
						sitemenuitem1.setName("站点流程");
						sitemenuitem1.setLink("javascript:setSiteFlow('"
								+ siteId + "','" + siteName + "')");
						sitemenuitem1.setTarget("base_properties_content");
						sitemenuitem1
								.setIcon(request.getContextPath()
										+ "/sysmanager/images/rightMemu/site_workflow.gif");
						sitemenu.addContextMenuItem(sitemenuitem1);
					}
					// 判断是否有设置站点首页模板权限（其实就是是否有编辑站点的权限）
					if (super.accessControl.checkPermission(siteId,
							AccessControl.UPDATE_PERMISSION,
							AccessControl.SITE_RESOURCE)) {
						Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
						sitemenuitem0.setName("首页模板");
						sitemenuitem0
								.setLink("javascript:setSiteIndexTemplate('"
										+ siteId + "')");
						sitemenuitem0
								.setIcon(request.getContextPath()
										+ "/sysmanager/images/rightMemu/site_indextpl.gif");
						sitemenu.addContextMenuItem(sitemenuitem0);
					}
					// 判断是否有模板管理的权限
					// if (super.accessControl.checkPermission(siteId,
					// AccessControl.SITE_TEMPMANAGER_PERMISSION,
					// AccessControl.SITE_RESOURCE)) {
					// Menu.ContextMenuItem sitemenuitem2 = new
					// Menu.ContextMenuItem();
					// sitemenuitem2.setName("模板管理");
					// sitemenuitem2.setLink(request.getContextPath() +
					// "/cms/templateManage/template_main.jsp");
					// sitemenuitem2.setTarget("base_properties_content");
					// sitemenuitem2.setIcon(request.getContextPath() +
					// "/sysmanager/images/actions.gif");
					// sitemenu.addContextMenuItem(sitemenuitem2);
					// addContextMenuOfType(sitemenu);
					// }
					/*
					 * //判断是否有完全发布的权限 if
					 * (super.accessControl.checkPermission(siteId,
					 * AccessControl.SITEBYALL_PERMISSION,
					 * AccessControl.SITE_RESOURCE)) { Menu.ContextMenuItem
					 * sitemenuitem3 = new Menu.ContextMenuItem();
					 * sitemenuitem3.setName("完全发布");
					 * sitemenuitem3.setLink("javascript:publishSiteByAll('"+siteId+"')");
					 * sitemenuitem3.setTarget("base_properties_content");
					 * sitemenuitem3.setIcon(request.getContextPath() +
					 * "/sysmanager/images/actions.gif");
					 * sitemenu.addContextMenuItem(sitemenuitem3);
					 * addContextMenuOfType(sitemenu); } // 判断是否有增量发布的权限 if
					 * (super.accessControl.checkPermission(siteId,
					 * AccessControl.SITEBYINC_PERMISSION,
					 * AccessControl.SITE_RESOURCE)) { Menu.ContextMenuItem
					 * sitemenuitem4 = new Menu.ContextMenuItem();
					 * sitemenuitem4.setName("增量发布");
					 * sitemenuitem4.setLink("javascript:publishSiteByInc('"+siteId+"')");
					 * sitemenuitem4.setTarget("base_properties_content");
					 * sitemenuitem4.setIcon(request.getContextPath() +
					 * "/sysmanager/images/actions.gif");
					 * sitemenu.addContextMenuItem(sitemenuitem4);
					 * addContextMenuOfType(sitemenu); }
					 */
					// 判断是否有发布的权限
					if (super.accessControl.checkPermission(siteId,
							AccessControl.SITEPUBLISH_PERMISSION,
							AccessControl.SITE_RESOURCE)
							&& smi.isActive(siteId)) {
						Menu.ContextMenuItem sitemenuitem3 = new Menu.ContextMenuItem();
						sitemenuitem3.setName("站点发布");
						sitemenuitem3.setLink("javascript:publishSite('"
								+ siteId + "')");
						sitemenuitem3.setTarget("base_properties_content");
						sitemenuitem3
								.setIcon(request.getContextPath()
										+ "/sysmanager/images/rightMemu/site_publish.gif");
						sitemenu.addContextMenuItem(sitemenuitem3);
					}
					// 判断是否有预览的权限
					if (super.accessControl.checkPermission(siteId,
							AccessControl.SITE_VIEW,
							AccessControl.SITE_RESOURCE)
							&& smi.isActive(siteId)) {
						Menu.ContextMenuItem sitemenuitem4 = new Menu.ContextMenuItem();
						sitemenuitem4.setName("站点预览");
						sitemenuitem4.setLink("javascript:viewSite('" + siteId
								+ "')");
						sitemenuitem4.setTarget("base_properties_content");
						sitemenuitem4
								.setIcon(request.getContextPath()
										+ "/sysmanager/images/rightMemu/site_viewer.gif");
						sitemenu.addContextMenuItem(sitemenuitem4);
					}
					
					Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
					sitemenuitem5.setName("站点浏览");
					sitemenuitem5.setLink("javascript:siteUrlView('" + site.getSecondName() + "')");
					sitemenuitem5.setTarget("base_properties_content");
					sitemenuitem5.setIcon(request.getContextPath()
									+ "/sysmanager/images/rightMemu/site_viewer.gif");
					sitemenu.addContextMenuItem(sitemenuitem5);
					
					super.addContextMenuOfNode(tt, sitemenu);

				} else {

					if (accessControl.checkPermission("" + site.getSiteId(),
							AccessControl.READ_PERMISSION,
							AccessControl.SITE_RESOURCE)) {

						addNode(father, "site:" + site.getSiteId(), siteName,
								"site", true, curLevel, (String) null,
								(String) null, (String) null, map);

					}

				}
			}

			else if (type.equals("mywork")) {
				Module mywork = menuHelper.getModule(myworkParentPath);
				ItemQueue items = mywork.getItems();
				for (int i = 0; i < items.size(); i++) {
					Item item = items.getItem(i);
					Map map = new HashMap();
					// map.put("nodeLink", menuHelper.getWorkspaceUrl(null,
					// item, null));
					String workspaceContent = null;
		            if(!item.hasWorkspaceContentVariables()){
		            	workspaceContent = item.getWorkspaceContent();
		            }else{
		            	workspaceContent = Framework.getWorkspaceContent(item, accessControl);
		            }
					map.put("nodeLink", workspaceContent);
					addNode(father, "myworkitem:" + item.getId(), item
							.getName(request), "myworkitem", true, curLevel,
							(String) null, (String) null, (String) null, map);

				}

				return true;
			} else if (type.equals("pigeonhole")) {
				Module pigeonhole = menuHelper.getModule(pigeonholeParentPath);
				ItemQueue items = pigeonhole.getItems();
				for (int i = 0; i < items.size(); i++) {
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "pigeonholeitem:" + item.getId(), item
							.getName(request), "pigeonholeitem", true, curLevel,
							(String) null, (String) null, (String) null, map);
				}

				return true;
			} else if (type.equals("garbage")) {
				Module pigeonhole = menuHelper
						.getModule(garbageStationParentPath);
				ItemQueue items = pigeonhole.getItems();
				for (int i = 0; i < items.size(); i++) {
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "garbageitem:" + item.getId(), item
							.getName(request), "garbageitem", true, curLevel,
							(String) null, (String) null, (String) null, map);
				}

				return true;
			} else if (type.equals("docsource")) {
				Module pigeonhole = menuHelper.getModule(docsourceParentPath);
				ItemQueue items = pigeonhole.getItems();
				for (int i = 0; i < items.size(); i++) {
					Item item = items.getItem(i);
					Map map = new HashMap();
					map.put("nodeLink", item.getWorkspaceContent());
					addNode(father, "docsourceitem:" + item.getId(), item
							.getName(request), "docsourceitem", true, curLevel,
							(String) null, (String) null, (String) null, map);
				}

				return true;
			}
			// else if(type.equals("publish"))
			// {
			// Module publish = menuHelper.getModule(publishParentPath);
			// ItemQueue items = publish.getItems();
			// for(int i = 0; i < items.size();i ++)
			// {
			// Item item = items.getItem(i);
			// Map map = new HashMap();
			// map.put("nodeLink", item.getWorkspaceContent());
			// addNode(father, "publishitem:" + item.getId(), item.getName(),
			// "publishitem",
			// true, curLevel, (String)null,(String)null,(String)null,map);
			//						
			// }
			//					
			// return true;
			// }
			/*
			 * else if(type.equals("form")) { Module publish =
			 * menuHelper.getModule(formParentPath); ItemQueue items =
			 * publish.getItems(); for(int i = 0; i < items.size();i ++) { Item
			 * item = items.getItem(i); Map map = new HashMap();
			 * map.put("nodeLink", item.getWorkspaceContent()); addNode(father,
			 * "formitem:" + item.getId(), item.getName(), "formitem", true,
			 * curLevel, (String)null,(String)null,(String)null,map);
			 *  }
			 * 
			 * return true; }
			 */

			// 加载站点下的频道
			else if (type.equals("site")) {

				String[] tmp = treeID.split(":");

//				String siteId = cmsmanager.getSiteID();
				String siteId = tmp[1];
				// System.out.print(">>>>>>>"+cmsmanager.getSiteID());
				SiteManager siteManager = new SiteManagerImpl();
				//String siteName = siteManager.getSiteInfo(siteId).getName();
				ChannelManager channelManager = new ChannelManagerImpl();
				//List channels = null;//ChannelCacheManager.getInstance().;//siteManager.getDirectChannelsOfSite(siteId);
				Site subsite = SiteCacheManager.getInstance().getSite(siteId);
				String siteName = subsite.getName();
				ChannelCacheManager cm = (ChannelCacheManager)(SiteCacheManager.getInstance()
						 													   .getChannelCacheManager(siteId));
				List channels = cm.getSubChannels("0");
				if (channels != null) {
					Iterator iterator = channels.iterator();

					while (iterator.hasNext()) {
						Channel channel = (Channel) iterator.next();
						//String treeName = channel.getDisplayName();
						String treeName = channel.getName();//为了频道显示名称能去其它路径名称，将原来的displayName和channelName进行对调
						String channelId = String.valueOf(channel
								.getChannelId());
						Map map = new HashMap();
						map.put("channelId", String.valueOf(channel
								.getChannelId()));
						map.put("channelName", channel.getDisplayName());
						map.put("siteid", siteId);
						map.put("sitename", siteName);
						map.put("resName", treeName);
						map.put("nodeLink",
								"/cms/channelManage/channel_doc_list.jsp");
						if (accessControl.checkPermission(channelId,
								AccessControl.WRITE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)
								|| accessControl.checkPermission(siteId,
										AccessControl.WRITE_PERMISSION,
										AccessControl.SITECHANNEL_RESOURCE)) {
							ITreeNode channelNode = addNode(father, channel
									.getChannelId()
									+ ":" + siteId, treeName, "channel", true,
									curLevel, (String) null, (String) null,
									(String) null, map);
							// Menu channelmenu =
							// this.addContextMenuOffChannel(siteId,siteName,""+channel.getChannelId(),channel.getName());
							// super.addContextMenuOfNode(channelNode,menu);
							Menu channelmenu = new Menu();
							channelmenu.setIdentity("channel");
							//if (channelManager.hasSubChannel(channelId)) {
							if (cm.hasSubChannel(channelId)) {
								channelmenu.addContextMenuItem(Menu.MENU_EXPAND);
								channelmenu.addSeperate();
							}

							// 判断是否有新建子频道权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.ADD_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.ADD_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem = new Menu.ContextMenuItem();
								channelmenuitem.setName("建子频道");
								channelmenuitem
										.setLink("javascript:addChannel('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem
										.setTarget("base_properties_content");
								channelmenuitem
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_newson.gif");
								channelmenu.addContextMenuItem(channelmenuitem);
							}
							// 判断是否有删除频道权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.DELETE_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.DELETE_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem1 = new Menu.ContextMenuItem();
								channelmenuitem1.setName("删除频道");
								channelmenuitem1
										.setLink("javascript:deleteChannel('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem1
										.setTarget("base_properties_content");
								channelmenuitem1
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_del.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem1);
							}
							// 判断是否有修改频道权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.UPDATE_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
								channelmenuitem2.setName("编辑频道");
								channelmenuitem2
										.setLink("javascript:editChannel('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem2
										.setTarget("base_properties_content");
								channelmenuitem2
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_edit.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem2);

							}
							// 判断是否有权限管理的权限(频道)
//							if (super.accessControl.isAdmin()
//									|| smi.hasSiteManager(siteId,
//											super.accessControl.getUserID())) {
//								Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
//								sitemenuitem5.setName("设置权限");
//								sitemenuitem5
//										.setLink("javascript:channelSysManager('"
//												+ channelId
//												+ "','"
//												+ treeName
//												+ "')");
//								sitemenuitem5.setIcon(request.getContextPath()
//										+ "/sysmanager/images/actions.gif");
//								channelmenu.addContextMenuItem(sitemenuitem5);
//							}
							// 判断是否有权限管理的权限(频道)
							/*
							 * if
							 * (super.accessControl.isAdmin()||smi.hasSiteManager(siteId,super.accessControl.getUserID())) {
							 * Menu.ContextMenuItem sitemenuitem5 = new
							 * Menu.ContextMenuItem();
							 * sitemenuitem5.setName("用户授权");
							 * sitemenuitem5.setLink("javascript:channelSysManaUsers('"+
							 * channelId + "','" + treeName + "')");
							 * sitemenuitem5.setIcon(request.getContextPath()
							 * +"/sysmanager/images/actions.gif");
							 * channelmenu.addContextMenuItem(sitemenuitem5); }
							 */
							// 判断是否有设置频道模板的权限（其实就是是否有编辑的权限）
							if (super.accessControl.checkPermission(channelId,
									AccessControl.UPDATE_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
								channelmenuitem2.setName("频道模板");
								channelmenuitem2
										.setLink("javascript:editChannelTemp('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem2
										.setTarget("base_properties_content");
								channelmenuitem2
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_tpl.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem2);

							}
							// 判断是否有改变频道流程权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.CHANNEL_WORKFLOW_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl
										.checkPermission(
												siteId,
												AccessControl.CHANNEL_WORKFLOW_PERMISSION,
												AccessControl.SITECHANNEL_RESOURCE)
									|| super.accessControl.checkPermission(channelId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.CHANNEL_RESOURCE)
											|| accessControl.checkPermission(siteId,
													AccessControl.UPDATE_PERMISSION,
													AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
								channelmenuitem3.setName("频道流程");
								channelmenuitem3
										.setLink("javascript:changeWorkflow('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem3
										.setTarget("base_properties_content");
								channelmenuitem3
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_workflow.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem3);
							}
							//判断是否有改变频道概览图片的权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.CHANNEL_INDEXPIC_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl
										.checkPermission(
												siteId,
												AccessControl.CHANNEL_INDEXPIC_PERMISSION,
												AccessControl.SITECHANNEL_RESOURCE)
									|| super.accessControl.checkPermission(channelId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.CHANNEL_RESOURCE)
											|| accessControl.checkPermission(siteId,
													AccessControl.UPDATE_PERMISSION,
													AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
								channelmenuitem3.setName("频道概览图片");
								channelmenuitem3
										.setLink("javascript:changeIndexPic('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem3
										.setTarget("base_properties_content");
								channelmenuitem3
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/actions.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem3);
							}
							/*
							 * //判断是否有完全发布的权限 if
							 * (super.accessControl.checkPermission(channelId,
							 * AccessControl.CHNLBYALL_PERMISSION,
							 * AccessControl.CHANNEL_RESOURCE)) {
							 * Menu.ContextMenuItem channelmenuitem4 = new
							 * Menu.ContextMenuItem();
							 * channelmenuitem4.setName("完全发布");
							 * channelmenuitem4.setLink("javascript:publishChannelByAll('"+channelId+"')");
							 * channelmenuitem4.setTarget("base_properties_content");
							 * channelmenuitem4.setIcon(request.getContextPath() +
							 * "/sysmanager/images/actions.gif");
							 * channelmenu.addContextMenuItem(channelmenuitem4); }
							 * //判断是否有增量发布的权限 if
							 * (super.accessControl.checkPermission(channelId,
							 * AccessControl.CHNLBYINC_PERMISSION,
							 * AccessControl.CHANNEL_RESOURCE)) {
							 * Menu.ContextMenuItem channelmenuitem5 = new
							 * Menu.ContextMenuItem();
							 * channelmenuitem5.setName("增量发布");
							 * channelmenuitem5.setLink("javascript:publishChannelByInc('"+channelId+"')");
							 * channelmenuitem5.setTarget("base_properties_content");
							 * channelmenuitem5.setIcon(request.getContextPath() +
							 * "/sysmanager/images/actions.gif");
							 * channelmenu.addContextMenuItem(channelmenuitem5); }
							 */
							// 判断是否有发布的权限
							if ((super.accessControl.checkPermission(channelId,
									AccessControl.CHNLPUBLISH_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									&& siteManager.isActive(siteId)
									&& channelManager.isActive(channelId))|| 
									accessControl.checkPermission(siteId,
									AccessControl.CHNLPUBLISH_PERMISSION,
									AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem4 = new Menu.ContextMenuItem();
								channelmenuitem4.setName("频道发布");
								channelmenuitem4
										.setLink("javascript:publishChannel('"
												+ siteId + "','" + channelId
												+ "')");
								channelmenuitem4
										.setTarget("base_properties_content");
								channelmenuitem4
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_publish.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem4);
							}
							// 判断是否有预览的权限
							if ((super.accessControl.checkPermission(channelId,
									AccessControl.SITE_VIEW,
									AccessControl.SITE_RESOURCE)
									&& siteManager.isActive(siteId)
									&& channelManager.isActive(channelId))||
									accessControl.checkPermission(siteId,
									AccessControl.SITE_VIEW,
									AccessControl.SITECHANNEL_RESOURCE)
								) {
								Menu.ContextMenuItem channelmenuitem5 = new Menu.ContextMenuItem();
								channelmenuitem5.setName("频道预览");
								channelmenuitem5
										.setLink("javascript:viewChannel('"
												+ siteId + "','" + channelId
												+ "')");
								channelmenuitem5
										.setTarget("base_properties_content");
								channelmenuitem5
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_viewer.gif");
								channelmenu.addContextMenuItem(channelmenuitem5);
							}
							
							Menu.ContextMenuItem channelmenuitem66 = new Menu.ContextMenuItem();
							channelmenuitem66.setName("频道浏览");
							channelmenuitem66.setLink("javascript:channelUrlView('" + subsite.getSiteId() + "','" + channel.getChannelId()+ "')");
							channelmenuitem66.setTarget("base_properties_content");
							channelmenuitem66.setIcon(request.getContextPath()
											+ "/sysmanager/images/rightMemu/site_viewer.gif");
							channelmenu.addContextMenuItem(channelmenuitem66);
							
							// 判断是否有转发文档的权限（对频道转发文档只包括复制和引用）
							if (super.accessControl.checkPermission(channelId,
									AccessControl.TRANSMIT_PERMISSION,
									AccessControl.CHANNELDOC_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.TRANSMIT_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem6 = new Menu.ContextMenuItem();
								channelmenuitem6.setName("复制文档");
								channelmenuitem6.setLink("javascript:copyDoc('"
										+ siteId + "','" + channelId + "')");
								channelmenuitem6
										.setTarget("base_properties_content");
								channelmenuitem6
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/doc_copy.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem6);

								Menu.ContextMenuItem channelmenuitem9 = new Menu.ContextMenuItem();
								channelmenuitem9.setName("剪切文档");
								channelmenuitem9.setLink("javascript:moveDoc('"
										+ siteId + "','" + channelId + "')");
								channelmenuitem6
										.setTarget("base_properties_content");
								channelmenuitem9
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/doc_cut.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem9);
							}

							// 判断是否有引用文档管理的权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.CITEDOC_MANAGER,
									AccessControl.CHANNELDOC_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.CITEDOC_MANAGER,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem8 = new Menu.ContextMenuItem();
								channelmenuitem8.setName("引用管理");
								channelmenuitem8
										.setLink("javascript:citeDocManager('"
												+ siteId + "','" + siteName
												+ "','" + channelId + "','"
												+ treeName + "')");
								channelmenuitem8
										.setTarget("base_properties_content");
								channelmenuitem8
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/doc_cite.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem8);
							}
							// 判断是否有置顶管理的权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.ARRANGE_DOCM,
									AccessControl.CHANNELDOC_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.ARRANGE_DOCM,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem7 = new Menu.ContextMenuItem();
								channelmenuitem7.setName("置顶管理");
								channelmenuitem7
										.setLink("javascript:arrangeDocM('"
												+ siteId + "','" + channelId + "')");
								channelmenuitem7
										.setTarget("base_properties_content");
								channelmenuitem7
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_arrange.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem7);
							}
							// 判断是否有扩展字段管理的权限
							if (super.accessControl.checkPermission(channelId,
									AccessControl.EXT_FIELD,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.EXT_FIELD,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem8 = new Menu.ContextMenuItem();
								channelmenuitem8.setName("扩展字段");
								channelmenuitem8
										.setLink("javascript:extfieldmanage('"
												+ channelId + "')");
								channelmenuitem8.setTarget("base_properties_content");
								channelmenuitem8
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_extfield.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem8);
							}
							if(isCommentsOpen != null && isCommentsOpen.equals("true"))
							{
								if(super.accessControl.checkPermission(channelId,
										 AccessControl.MANAGE_DOCCOMMENT_PERMISSION,
										 AccessControl.CHANNELDOC_RESOURCE)
										 || accessControl.checkPermission(siteId,
												 AccessControl.MANAGE_DOCCOMMENT_PERMISSION,
												 AccessControl.SITEDOC_RESOURCE)){	 
									
									 Menu.ContextMenuItem channelmenuitem9 = new Menu.ContextMenuItem();
									 channelmenuitem9.setName("开/关评论");
									 channelmenuitem9.setLink("javascript:switchComments('"
														+ channelId + "')");
									 channelmenuitem9
												.setTarget("base_properties_content");
									 channelmenuitem9
												.setIcon(request.getContextPath()
														+ "/sysmanager/images/rightMemu/chl_extfield.gif");
									 channelmenu
												.addContextMenuItem(channelmenuitem9);
									 
									 //增加评论审核
									 Menu.ContextMenuItem channelmenuitem10 = new Menu.ContextMenuItem();
									 channelmenuitem10.setName("开/关评论审核");
									 channelmenuitem10.setLink("javascript:switchCommentAudit('"
														+ channelId + "')");
									 channelmenuitem10
												.setTarget("base_properties_content");
									 channelmenuitem10
												.setIcon(request.getContextPath()
														+ "/sysmanager/images/rightMemu/chl_extfield.gif");
									 channelmenu
												.addContextMenuItem(channelmenuitem10);
								}
							}
							super.addContextMenuOfNode(channelNode,channelmenu);

						} else {
							if (accessControl.checkPermission(channelId,
									AccessControl.READ_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)) {

								addNode(father, channel.getChannelId() + ":"
										+ siteId, treeName, "channel", true,
										curLevel, (String) null, (String) null,
										(String) null, map);

							}
						}
					}
				}

			}

			// 加载子频道
			else if (type.equals("channel")) {
				String[] tmp = treeID.split(":");
				String siteId = tmp[1];
				String channelId = tmp[0];
				SiteManager siteManager = new SiteManagerImpl();
				Site subsite = SiteCacheManager.getInstance().getSite(siteId);
				String siteName = subsite.getName();
				ChannelManager channelManager = new ChannelManagerImpl();
				ChannelCacheManager cm = (ChannelCacheManager)(SiteCacheManager.getInstance()
														  .getChannelCacheManager(siteId));
				List channels = cm.getSubChannels(channelId);
				if (channels != null) {
					Iterator iterator = channels.iterator();

					while (iterator.hasNext()) {
						Channel channel = (Channel) iterator.next();
						//String treeName = channel.getDisplayName();
						String treeName = channel.getName();//为了频道显示名称能去其它路径名称，将原来的displayName和channelName进行对调 
						String currChannelId = String.valueOf(channel
								.getChannelId());
						Map map = new HashMap();
						map.put("channelId", currChannelId);
						map.put("channelName", channel.getDisplayName());
						map.put("resName", treeName);
						map.put("siteid", tmp[1]);
						map.put("sitename", siteName);
						map.put("nodeLink",
								"/cms/channelManage/channel_doc_list.jsp");
						if (accessControl.checkPermission(currChannelId,
								AccessControl.WRITE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)
								|| accessControl.checkPermission(siteId,
										AccessControl.WRITE_PERMISSION,
										AccessControl.SITECHANNEL_RESOURCE)) {
							ITreeNode channelNode = addNode(father, String
									.valueOf(channel.getChannelId())
									+ ":" + siteId, treeName, "channel", true,
									curLevel, (String) null, (String) null,
									(String) null, map);
							// Menu menu =
							// this.addContextMenuOffChannel(siteid,sitename,""+channel.getChannelId(),channel.getName());
							// super.addContextMenuOfNode(channelNode,menu);
							Menu channelmenu = new Menu();
							channelmenu.setIdentity("channel");
							if (cm.hasSubChannel(currChannelId)) {
								channelmenu
										.addContextMenuItem(Menu.MENU_EXPAND);
								channelmenu.addSeperate();
							}
							// 判断是否有新建子频道权限
							if (super.accessControl.checkPermission(
									currChannelId,
									AccessControl.ADD_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.ADD_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem = new Menu.ContextMenuItem();
								channelmenuitem.setName("建子频道");
								channelmenuitem
										.setLink("javascript:addChannel('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem
										.setTarget("base_properties_content");
								channelmenuitem
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_newson.gif");
								channelmenu.addContextMenuItem(channelmenuitem);
							}
							// 判断是否有删除频道权限
							if (super.accessControl.checkPermission(
									currChannelId,
									AccessControl.DELETE_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.DELETE_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem1 = new Menu.ContextMenuItem();
								channelmenuitem1.setName("删除频道");
								channelmenuitem1
										.setLink("javascript:deleteChannel('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem1
										.setTarget("base_properties_content");
								channelmenuitem1
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_del.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem1);
							}
							// 判断是否有修改频道权限
							if (super.accessControl.checkPermission(
									currChannelId,
									AccessControl.UPDATE_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
								channelmenuitem2.setName("编辑频道");
								channelmenuitem2
										.setLink("javascript:editChannel('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem2
										.setTarget("base_properties_content");
								channelmenuitem2
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_edit.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem2);
							}
							// 判断是否有权限管理的权限(频道)
//							if (super.accessControl.isAdmin()
//									|| smi.hasSiteManager(siteId,
//											super.accessControl.getUserID())) {
//								Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
//								sitemenuitem5.setName("设置权限");
//								sitemenuitem5
//										.setLink("javascript:channelSysManager('"
//												+ currChannelId
//												+ "','"
//												+ treeName + "')");
//								sitemenuitem5.setIcon(request.getContextPath()
//										+ "/sysmanager/images/actions.gif");
//								channelmenu.addContextMenuItem(sitemenuitem5);
//							}
							// 判断是否有权限管理的权限(频道)
							/*
							 * if
							 * (super.accessControl.isAdmin()||smi.hasSiteManager(siteId,super.accessControl.getUserID())) {
							 * Menu.ContextMenuItem sitemenuitem5 = new
							 * Menu.ContextMenuItem();
							 * sitemenuitem5.setName("用户授权");
							 * sitemenuitem5.setLink("javascript:channelSysManaUsers('"+
							 * currChannelId + "','" + treeName + "')");
							 * sitemenuitem5.setIcon(request.getContextPath()
							 * +"/sysmanager/images/actions.gif");
							 * channelmenu.addContextMenuItem(sitemenuitem5); }
							 */
							// 判断是否有设置频道模板的权限（其实就是是否有编辑的权限）
							if (super.accessControl.checkPermission(channelId,
									AccessControl.UPDATE_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem2 = new Menu.ContextMenuItem();
								channelmenuitem2.setName("频道模板");
								channelmenuitem2
										.setLink("javascript:editChannelTemp('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem2
										.setTarget("base_properties_content");
								channelmenuitem2
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_tpl.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem2);

							}
							// 判断是否有改变频道流程权限
							if (super.accessControl.checkPermission(
									currChannelId,
									AccessControl.CHANNEL_WORKFLOW_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl
										.checkPermission(
													siteId,
													AccessControl.CHANNEL_WORKFLOW_PERMISSION,
													AccessControl.SITECHANNEL_RESOURCE)
									|| super.accessControl.checkPermission(
											currChannelId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.CHANNEL_RESOURCE)
											|| accessControl.checkPermission(siteId,
													AccessControl.UPDATE_PERMISSION,
													AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
								channelmenuitem3.setName("频道流程");
								channelmenuitem3
										.setLink("javascript:changeWorkflow('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem3
										.setTarget("base_properties_content");
								channelmenuitem3
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_workflow.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem3);
							}
							//判断是否有改变频道概览图片的权限
							if (super.accessControl.checkPermission(currChannelId,
									AccessControl.CHANNEL_INDEXPIC_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl
										.checkPermission(
												siteId,
												AccessControl.CHANNEL_INDEXPIC_PERMISSION,
												AccessControl.SITECHANNEL_RESOURCE)
									|| super.accessControl.checkPermission(
											currChannelId,
											AccessControl.UPDATE_PERMISSION,
											AccessControl.CHANNEL_RESOURCE)
											|| accessControl.checkPermission(siteId,
													AccessControl.UPDATE_PERMISSION,
													AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem3 = new Menu.ContextMenuItem();
								channelmenuitem3.setName("频道概览图片");
								channelmenuitem3
										.setLink("javascript:changeIndexPic('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem3
										.setTarget("base_properties_content");
								channelmenuitem3
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/actions.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem3);
							}
							/*
								 * // 判断是否有完全发布的权限 if
								 * (super.accessControl.checkPermission(channelId,
								 * AccessControl.CHNLBYALL_PERMISSION,
								 * AccessControl.CHANNEL_RESOURCE)) {
								 * Menu.ContextMenuItem channelmenuitem4 = new
								 * Menu.ContextMenuItem();
								 * channelmenuitem4.setName("完全发布");
								 * channelmenuitem4.setLink("javascript:publishChannelByAll('"+channelId+"')");
								 * channelmenuitem4.setTarget("base_properties_content");
								 * channelmenuitem4.setIcon(request.getContextPath() +
								 * "/sysmanager/images/actions.gif");
								 * channelmenu.addContextMenuItem(channelmenuitem4); } //
								 * 判断是否有增量发布的权限 if
								 * (super.accessControl.checkPermission(channelId,
								 * AccessControl.CHNLBYINC_PERMISSION,
								 * AccessControl.CHANNEL_RESOURCE)) {
								 * Menu.ContextMenuItem channelmenuitem5 = new
								 * Menu.ContextMenuItem();
								 * channelmenuitem5.setName("增量发布");
								 * channelmenuitem5.setLink("javascript:publishChannelByInc('"+channelId+"')");
								 * channelmenuitem5.setTarget("base_properties_content");
								 * channelmenuitem5.setIcon(request.getContextPath() +
								 * "/sysmanager/images/actions.gif");
								 * channelmenu.addContextMenuItem(channelmenuitem5); }
								 */
							// 判断是否有发布的权限
							if ((super.accessControl.checkPermission(
									currChannelId,
									AccessControl.CHNLPUBLISH_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)
									&& siteManager.isActive(siteId)
									&& channelManager.isActive(currChannelId))||
									super.accessControl.checkPermission(
									siteId,
									AccessControl.CHNLPUBLISH_PERMISSION,
									AccessControl.SITECHANNEL_RESOURCE)
									
								) {
								Menu.ContextMenuItem channelmenuitem5 = new Menu.ContextMenuItem();
								channelmenuitem5.setName("频道发布");
								channelmenuitem5
										.setLink("javascript:publishChannel('"
												+ siteId + "','"
												+ currChannelId + "')");
								channelmenuitem5
										.setTarget("base_properties_content");
								channelmenuitem5
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_publish.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem5);
							}
							// 判断是否有预览的权限
							if ((super.accessControl.checkPermission(siteId,
									AccessControl.CHNL_VIEW,
									AccessControl.SITE_RESOURCE)
									&& siteManager.isActive(siteId)
									&& channelManager.isActive(currChannelId))||
									super.accessControl.checkPermission(
									siteId,
									AccessControl.CHNL_VIEW,
									AccessControl.SITECHANNEL_RESOURCE)
								){
								Menu.ContextMenuItem channelmenuitem5 = new Menu.ContextMenuItem();
								channelmenuitem5.setName("频道预览");
								channelmenuitem5
										.setLink("javascript:viewChannel('"
												+ siteId + "','"
												+ currChannelId + "')");
								channelmenuitem5
										.setTarget("base_properties_content");
								channelmenuitem5
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_viewer.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem5);
							}
							
							Menu.ContextMenuItem channelmenuitem66 = new Menu.ContextMenuItem();
							channelmenuitem66.setName("频道浏览");
							channelmenuitem66.setLink("javascript:channelUrlView('" + subsite.getSiteId() + "','" + channel.getChannelId() + "')");
							channelmenuitem66.setTarget("base_properties_content");
							channelmenuitem66.setIcon(request.getContextPath()
											+ "/sysmanager/images/rightMemu/site_viewer.gif");
							channelmenu.addContextMenuItem(channelmenuitem66);
							
							// 判断是否有复制文档的权限
							if (super.accessControl.checkPermission(
									currChannelId,
									AccessControl.COPYDOC_PERMISSION,
									AccessControl.CHANNELDOC_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.COPYDOC_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem6 = new Menu.ContextMenuItem();
								channelmenuitem6.setName("复制文档");
								channelmenuitem6
										.setLink("javascript:copyDoc('"
												+ siteId + "','"
												+ currChannelId + "')");
								channelmenuitem6
										.setTarget("base_properties_content");
								channelmenuitem6
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/doc_copy.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem6);

								Menu.ContextMenuItem channelmenuitem9 = new Menu.ContextMenuItem();
								channelmenuitem9.setName("剪切文档");
								channelmenuitem9
										.setLink("javascript:moveDoc('"
												+ siteId + "','"
												+ currChannelId + "')");
								channelmenuitem6
										.setTarget("base_properties_content");
								channelmenuitem9
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/doc_cut.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem9);
							}
							// 判断是否有引用文档管理的权限
							if (super.accessControl.checkPermission(
									currChannelId,
									AccessControl.CITEDOC_MANAGER,
									AccessControl.CHANNELDOC_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.CITEDOC_MANAGER,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem8 = new Menu.ContextMenuItem();
								channelmenuitem8.setName("引用文档");
								channelmenuitem8
										.setLink("javascript:citeDocManager('"
												+ siteId + "','" + siteName
												+ "','" + currChannelId + "','"
												+ treeName + "')");
								channelmenuitem8
										.setTarget("base_properties_content");
								channelmenuitem8
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/doc_cite.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem8);
							}
							// 判断是否有置顶管理的权限
							if (super.accessControl.checkPermission(
									currChannelId, AccessControl.ARRANGE_DOCM,
									AccessControl.CHANNELDOC_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.ARRANGE_DOCM,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem7 = new Menu.ContextMenuItem();
								channelmenuitem7.setName("置顶管理");
								channelmenuitem7
										.setLink("javascript:arrangeDocM('"
												+ siteId + "','" + currChannelId + "')");
								channelmenuitem7
										.setTarget("base_properties_content");
								channelmenuitem7
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_arrange.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem7);
							}

							// 判断是否有扩展字段管理的权限
							if (super.accessControl.checkPermission(
									currChannelId, AccessControl.EXT_FIELD,
									AccessControl.CHANNEL_RESOURCE)
									|| accessControl.checkPermission(siteId,
											AccessControl.EXT_FIELD,
											AccessControl.SITECHANNEL_RESOURCE)) {
								Menu.ContextMenuItem channelmenuitem8 = new Menu.ContextMenuItem();
								channelmenuitem8.setName("扩展字段");
								channelmenuitem8
										.setLink("javascript:extfieldmanage('"
												+ currChannelId + "')");
								channelmenuitem8
										.setTarget("base_properties_content");
								channelmenuitem8
										.setIcon(request.getContextPath()
												+ "/sysmanager/images/rightMemu/chl_extfield.gif");
								channelmenu
										.addContextMenuItem(channelmenuitem8);
							}
							if(isCommentsOpen != null && isCommentsOpen.equals("true"))
							{
								if(super.accessControl.checkPermission(channelId,
										 AccessControl.MANAGE_DOCCOMMENT_PERMISSION,
										 AccessControl.CHANNELDOC_RESOURCE)
										 || accessControl.checkPermission(siteId,
												 AccessControl.MANAGE_DOCCOMMENT_PERMISSION,
												 AccessControl.SITEDOC_RESOURCE)){
												
									 Menu.ContextMenuItem channelmenuitem9 = new Menu.ContextMenuItem();
									 channelmenuitem9.setName("开/关评论");
									 channelmenuitem9.setLink("javascript:switchComments('"
														+ channel.getChannelId() + "')");
									 channelmenuitem9
												.setTarget("base_properties_content");
									 channelmenuitem9
												.setIcon(request.getContextPath()
														+ "/sysmanager/images/rightMemu/chl_extfield.gif");
									 channelmenu
												.addContextMenuItem(channelmenuitem9);		
									 
									 Menu.ContextMenuItem channelmenuitem10 = new Menu.ContextMenuItem();
									 channelmenuitem10.setName("开/关评论审核");
									 channelmenuitem10.setLink("javascript:switchCommentAudit('"
														+ channel.getChannelId() + "')");
									 channelmenuitem10
												.setTarget("base_properties_content");
									 channelmenuitem10
												.setIcon(request.getContextPath()
														+ "/sysmanager/images/rightMemu/chl_extfield.gif");
									 channelmenu
												.addContextMenuItem(channelmenuitem10);			
								}
							}
							super
									.addContextMenuOfNode(channelNode,
											channelmenu);

						} else {
							if (accessControl.checkPermission(currChannelId,
									AccessControl.READ_PERMISSION,
									AccessControl.CHANNEL_RESOURCE)) {
								addNode(father, String.valueOf(channel
										.getChannelId())
										+ ":" + siteId, treeName, "channel",
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

//package com.frameworkset.platform.cms.sitemanager.menu;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.jsp.PageContext;
//
//import com.frameworkset.platform.cms.CMSManager;
//import com.frameworkset.platform.cms.channelmanager.Channel;
//import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
//import com.frameworkset.platform.cms.channelmanager.ChannelManager;
//import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
//import com.frameworkset.platform.cms.docCommentManager.DocCommentManager;
//import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerImpl;
//import com.frameworkset.platform.cms.sitemanager.Site;
//import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
//import com.frameworkset.platform.cms.sitemanager.SiteManager;
//import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
//import com.frameworkset.platform.config.ConfigManager;
////import com.frameworkset.platform.cms.util.CMSUtil;
//import com.frameworkset.platform.framework.FrameworkServlet;
//import com.frameworkset.platform.framework.Item;
//import com.frameworkset.platform.framework.ItemQueue;
//import com.frameworkset.platform.framework.MenuHelper;
//import com.frameworkset.platform.framework.Module;
//import com.frameworkset.platform.framework.ModuleQueue;
//import com.frameworkset.platform.security.AccessControl;
//import com.frameworkset.common.tag.contextmenu.Menu;
//import com.frameworkset.common.tag.tree.COMTree;
//import com.frameworkset.common.tag.tree.itf.ITreeNode;
//
///**
// * 只显示单个站点节点的站点树
// * 
// * @author Administrator
// * 
// */
//public class ExampleTree extends COMTree {
//	
//
//	public boolean hasSon(ITreeNode father) {
//		
//		String fatherid = father.getId();
//		//调用业务方法获取，根据父节点id判断父节点是否有儿子
//		boolean hasson = bussinessservice.hasSons(fatherid);
//		return hasson;
//	}
//
//	/**
//	 * 
//	 */
//	public boolean setSon(ITreeNode father, int curLevel) {
//		
//
//			
//			
//				
//		Map map = new HashMap();
//		map.put("nodeLink", "link");
//		map.put("nodeTarget", "target");
//	    ITreeNode site = addNode(father, //父节点对象
//	    			"nodeid",//节点id
//					"nodename", //节点名称
//					"site", //节点类型
//					true, //是否显示节点的链接
//					curLevel, //节点的层级
//					(String) "节点备注信息",//节点备注信息
//					(String) "radiovalue", //指定单选按钮的值
//					(String) "checkboxvalue",//指定节点复选框的值
//					map//指定节点链接所带的参数，内置参数不会被添加到节点的链接参数中
//					);
//
//		// 加载站点节点的时候加载右键菜单
//		Menu sitemenu = new Menu();
////		sitemenu.setIdentity("site");//为节点添加右键菜单时无需指定菜单的唯一标识，即使指定了也会被忽略掉，树将自动为其添加唯一标识
//		sitemenu.addContextMenuItem(Menu.MENU_EXPAND);//为节点添加默认的展开菜单项，只有当节点有儿子节点时，该项才有作用
//		sitemenu.addSeperate(); //添加节点的分隔线，以便对菜单项进行分组
//		
//		Menu.ContextMenuItem sitemenuitem = new Menu.ContextMenuItem();//定义自己的菜单项
//		sitemenuitem.setName("站点发布");//指定菜单项的名称
//		String siteId = "200";
//		sitemenuitem.setLink("javascript:publishSite('"+ siteId + "')");//指定菜单项的链接，可以使javascript函数，也可以是超链接
//		sitemenuitem.setTarget("base_properties_content");//指定菜单项链接target
//		sitemenuitem.setIcon(request.getContextPath()
//						+ "/sysmanager/images/rightMemu/site_publish.gif");//指定菜单项的图标
//		sitemenu.addContextMenuItem(sitemenuitem);//将菜单项添加添加的菜单中
//		super.addContextMenuOfNode(site,sitemenu);	//将菜单添加到菜单上下文中		
//		return true;
//
//	}
//
//	protected void buildContextMenus() {
//		
//		//定义类型相同节点的右键菜单
//		Menu sitemenu = new Menu();
//		sitemenu.setIdentity("site");//指定菜单的标识为节点的类型
//		sitemenu.addContextMenuItem(Menu.MENU_EXPAND);//为节点添加默认的展开菜单项，只有当节点有儿子节点时，该项才有作用
//		sitemenu.addSeperate(); //添加节点的分隔线，以便对菜单项进行分组
//		
//		Menu.ContextMenuItem sitemenuitem = new Menu.ContextMenuItem();//定义自己的菜单项
//		sitemenuitem.setName("站点发布");//指定菜单项的名称
//		String siteId = "200";
//		sitemenuitem.setLink("javascript:publishSite('"+ siteId + "')");//指定菜单项的链接，可以使javascript函数，也可以是超链接
//		sitemenuitem.setTarget("base_properties_content");//指定菜单项链接target
//		sitemenuitem.setIcon(request.getContextPath()
//						+ "/sysmanager/images/rightMemu/site_publish.gif");//指定菜单项的图标
//		sitemenu.addContextMenuItem(sitemenuitem);//将菜单项添加添加的菜单中
//		super.addContextMenuOfType(sitemenu);	//将菜单添加到菜单上下文中	
//
//	}
//}

