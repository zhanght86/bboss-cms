package  com.frameworkset.platform.cms.channelmanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class ChannelTree extends COMTree implements java.io.Serializable {

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		

		String type = father.getType();

		try {  
			if (father.isRoot()) {
				
				String parentsiteid = "0";
				return SiteCacheManager.getInstance().hasSubSite(parentsiteid);
		    }
		
			
			if (type.equals("site") || type.equals("site_true")) { 
			
				//SiteManager siteManager = new SiteManagerImpl();
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
				if(cm.hasSubChannel("0"))
				{
					return true;
				}
				else if(SiteCacheManager.getInstance().hasSubSite(tmp[1]))
				{
					return true;
				}
				else
				{
					return false;
				}

			} 
			else if (type.equals("channel") || type.equals("channel_true")) {
				//ChannelManager channelManager = new ChannelManagerImpl();
				String[] tmp = treeID.split(":");
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(tmp[1]);
				return cm.hasSubChannel(tmp[0]);
				//return SiteChnlCacheManager.getInstance().hasSubChnl(tmp[0]);
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
	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		String roleId = (String)session.getAttribute("currRoleId");
		String roleTypeId = (String)session.getAttribute("role_type");
		try {
			String parentSiteid = "0"; 
			String type = father.getType();
			//加载子站点
			if (father.isRoot() || father.getType().equals("site") || father.getType().equals("site_true")) 
			{
				//SiteManager smi = new SiteManagerImpl();
				List sitelist;
				if (!father.isRoot()) {
					parentSiteid = treeID.split(":")[1];
					
					sitelist = SiteCacheManager.getInstance().getSubSites(parentSiteid);
				} else {
					sitelist = SiteCacheManager.getInstance().getSubSites("0");
				}

				if (sitelist != null) {
					Iterator iterator = sitelist.iterator();

					while (iterator.hasNext()) {
						Site site = (Site) iterator.next();
						String treeName = site.getName();
						Map map = new HashMap();
						map.put("siteId", ""+site.getSiteId());
						map.put("siteName", site.getName());
						map.put("resName", treeName);
						if (accessControl.checkPermission(
								""+site.getSiteId(),
								AccessControl.WRITE_PERMISSION,
								AccessControl.SITE_RESOURCE)) {
								addNode(father, "site:" + site.getSiteId(),
										treeName, "site", false, curLevel,
										(String) null, (String) null,
										(String) null, map);
						}else{
							if (accessControl.checkPermission(
									""+site.getSiteId(),
									AccessControl.READ_PERMISSION,
									AccessControl.SITE_RESOURCE)) {
								addNode(father, "site:" + site.getSiteId(),
										treeName, "site", false, curLevel,
										(String) null, (String) null,
										(String) null, map);
							}
							
						}
					
					}
				}
				
				if(!parentSiteid.equals("0")){
					//SiteManager siteManager = new SiteManagerImpl();
					String sitename = SiteCacheManager.getInstance().getSite(parentSiteid).getName();
					ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(parentSiteid);
					List channels = cm.getSubChannels("0");
					//List channels = SiteChnlCacheManager.getInstance().getSubChnls(parentSiteid);
					if (channels != null) {
						Iterator iterator = channels.iterator();

						while (iterator.hasNext()) {
							Channel channel = (Channel) iterator.next();
							String treeName = channel.getName();
							Map map = new HashMap();
							map.put("channelId", String.valueOf(channel.getChannelId()));
							map.put("channelName",channel.getName());
							map.put("siteid",parentSiteid);
							map.put("sitename",sitename);
							map.put("resName", treeName);
							map.put("resTypeId", "channel");
							String nodeType = "channel";
							if(AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(channel.getChannelId()),"channel")||
							   AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(channel.getChannelId()),"channeldoc")){
			                    nodeType = "channel_true";
			                }else{
			                    nodeType = "channel";
			                }
							if (accessControl.checkPermission(String.valueOf(channel.getChannelId()),AccessControl.WRITE_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
								addNode(father, channel.getChannelId() + ":" + parentSiteid, treeName, nodeType,
										true, curLevel, (String) null, 
										parentSiteid + ":" +  String.valueOf(channel.getChannelId()),
										parentSiteid + ":" +  String.valueOf(channel.getChannelId()), map);
							}else{
								 if (accessControl.checkPermission(String.valueOf(channel.getChannelId()),
			                                AccessControl.READ_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
			                        	
									 addNode(father, channel.getChannelId() + ":" + parentSiteid, treeName, nodeType,
												true, curLevel, (String) null, 
												parentSiteid + ":" +  String.valueOf(channel.getChannelId()),
												parentSiteid + ":" +  String.valueOf(channel.getChannelId()), map);
			                           
			                        }
							}
						}
					}
				}
			}
			
			else if(type.equals("channel") || type.equals("channel_true"))
			{
				String[] tmp = treeID.split(":");
				String siteid = tmp[1];
				String channelid = tmp[0];
				//SiteManager siteManager = new SiteManagerImpl();
				String sitename = SiteCacheManager.getInstance().getSite(siteid).getName();
				ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(siteid);
				List channels = cm.getSubChannels(channelid);
				if (channels != null) {
					Iterator iterator = channels.iterator();

					while (iterator.hasNext()) {
						Channel channel = (Channel) iterator.next();
						String treeName = channel.getName();
						Map map = new HashMap();
						map.put("channelId", String.valueOf(channel.getChannelId()));
						map.put("channelName", channel.getName());
						map.put("resName", treeName);
						map.put("siteid",tmp[1]);
						map.put("sitename",sitename);
						map.put("resTypeId", "channel");
						String nodeType = "channel";
		                if(AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(channel.getChannelId()),"channel")||
		                   AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(channel.getChannelId()),"channeldoc")){
		                    nodeType = "channel_true";
		                }else{
		                    nodeType = "channel";
		                }
		            	if (accessControl.checkPermission(String.valueOf(channel.getChannelId()),AccessControl.WRITE_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {

						    addNode(father, String.valueOf(channel.getChannelId()) + ":" + siteid, treeName, nodeType,
									true, curLevel, (String) null, 
									parentSiteid + ":" +  String.valueOf(channel.getChannelId()),
									parentSiteid + ":" +  String.valueOf(channel.getChannelId()), map);
		            	}else{
		            		 if (accessControl.checkPermission(String.valueOf(channel.getChannelId()),
		                                AccessControl.READ_PERMISSION, AccessControl.CHANNEL_RESOURCE)) {
		            			 addNode(father, String.valueOf(channel.getChannelId()) + ":" + siteid, treeName, nodeType,
											true, curLevel, (String) null, 
											parentSiteid + ":" +  String.valueOf(channel.getChannelId()),
											parentSiteid + ":" +  String.valueOf(channel.getChannelId()), map);
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
}
