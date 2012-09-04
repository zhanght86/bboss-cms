package  com.frameworkset.platform.cms.channelmanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
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
public class SiteChnlSelTreeOfTask extends COMTree implements java.io.Serializable
{ 
	public boolean hasSon(ITreeNode father) 
	{
		String treeID = father.getId();		
		

		String type = father.getType();

		try {  
			if (father.isRoot()) {  
				return true;
			}
			if (type.equals("site")) { 
				/**
				 * 1.频道 2.图片库 3.样式库 4. 模板
				 */
				return true;
				
			} else if (type.equals("channel")) {
				//ChannelManager channelManager = new ChannelManagerImpl();
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
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
		String taskType = request.getParameter("taskType");
		String permission = AccessControl.AUDIT_PERMISSION;
		if(taskType.equals("deliver"))
			permission = AccessControl.DELIVER_PERMISSION;
		else if(taskType.equals("publish"))
			permission = AccessControl.PUBLISH_CHNLBYINC;
		else if(taskType.equals("cite"))
			permission = AccessControl.TRANSMIT_PERMISSION;
		try {
			String parentSiteid = "0";
			String type = father.getType();			
			//加载站点
			if(type.equals("site"))
			{										
				String[] tmp = treeID.split(":");
				String siteid = tmp[1];
				//SiteManager siteManager = new SiteManagerImpl();
				//Site site = siteManager.getSiteInfo(siteid);
				Site site = SiteCacheManager.getInstance().getSite(siteid);
				String sitename = site.getName();
				Map map = new HashMap();
				map.put("siteid",siteid);
				map.put("sitename",sitename);
				addNode(father, "channel:" + treeID, "频道", "channelroot",
						false, curLevel, (String) null, (String) null,
						(String) null, map);
			}
			
			
			//加载子站点
			if (father.isRoot() || father.getType().equals("site")) 
			{
				//SiteManager smi = new SiteManagerImpl();
				List sitelist = null;

				if (!father.isRoot()){
					parentSiteid = treeID.split(":")[1];
					//sitelist = smi.getDirectSubSiteList(parentSiteid);
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
								addNode(father, "site:" + site.getSiteId(),
									siteName, "site", true, curLevel,
									(String) null, (String) null,
									(String) null, map);						
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
			else if(type.equals("channelroot")){
				String[] tmp = treeID.split(":");
				String siteId = tmp[2];
				//SiteManager siteManager = new SiteManagerImpl();
				String siteName = SiteCacheManager.getInstance().getSite(siteId).getName();
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(siteId);
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
						if (accessControl.checkPermission(channelId,permission, AccessControl.CHANNEL_RESOURCE)) {
							addNode(father, channel.getChannelId() + ":" + siteId, treeName, "channel",
								true, curLevel, (String) null, 
								siteId + ":" + channel.getName() + ":" +  String.valueOf(channel.getChannelId()),
								siteId + ":" + channel.getName() + ":" +  String.valueOf(channel.getChannelId()), 
								map);
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
				String siteName = SiteCacheManager.getInstance().getSite(siteId).getName();
				//ChannelManager channelManager = new ChannelManagerImpl();
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(siteId);
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
						if (accessControl.checkPermission(currChannelId,permission, AccessControl.CHANNEL_RESOURCE)) {
							addNode(father, String.valueOf(channel.getChannelId()) + ":" + siteId, treeName, "channel",
								true, curLevel, (String) null, 
								siteId + ":" + channel.getName() + ":" +  String.valueOf(channel.getChannelId()),
								siteId + ":" + channel.getName() + ":" +  String.valueOf(channel.getChannelId()),
								map);
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
