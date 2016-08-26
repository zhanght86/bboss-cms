package com.frameworkset.platform.cms.sitemanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
/**
 * 站点模板树，在模板授权中用到
 * @author Administrator
 *
 */
public class SiteTemplateTree extends COMTree implements java.io.Serializable{
	
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		

		String type = father.getType();

		try {  
			if (father.isRoot()) {
				
				String parentsiteid = "0";
				return SiteCacheManager.getInstance().hasSubSite(parentsiteid);
		    }
		
			
			if (type.equals("site") || type.equals("site_true")) { 
			
				SiteManager siteManager = new SiteManagerImpl();
				String[] tmp = treeID.split(":");
				if(siteManager.hasChannelOfSite(tmp[1]))
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
				SiteManager smi = new SiteManagerImpl();
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
					SiteManager siteManager = new SiteManagerImpl();
					String sitename = SiteCacheManager.getInstance().getSite(parentSiteid).getName();
					List templates = siteManager.getTemplatesOfSite(parentSiteid);
				
					if (templates != null) {
						Iterator iterator = templates.iterator();

						while (iterator.hasNext()) {
							Template template = (Template) iterator.next();
							String treeName = template.getName();
							Map map = new HashMap();
							map.put("templateId", String.valueOf(template.getTemplateId()));
							map.put("templageName",template.getName());
							map.put("siteid",parentSiteid);
							map.put("sitename",sitename);
							map.put("resName", treeName);
							map.put("resTypeId", "template");
							String nodeType = "template";
							if(AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(template.getTemplateId()),"template")){
			                    nodeType = "template_true";
			                }else{
			                    nodeType = "template";
			                }
						    addNode(father, template.getTemplateId() + ":" + parentSiteid, treeName, nodeType,
									true, curLevel, (String) null, 
									parentSiteid + ":" +  String.valueOf(template.getTemplateId()),
									parentSiteid + ":" +  String.valueOf(template.getTemplateId()), map);
							
							
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
