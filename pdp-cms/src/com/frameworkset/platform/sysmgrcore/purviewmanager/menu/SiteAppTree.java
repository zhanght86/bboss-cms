package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class SiteAppTree extends COMTree implements Serializable {
	
	String parentPath = "cms::menu://sysmenu$root/appManager$module";
	String siteId = "";
	String siteName = "";

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		String type = father.getType();
		
		try{
			if (father.isRoot()) {
				String parentsiteid = "0";
				return SiteCacheManager.getInstance().hasSubSite(parentsiteid);
		    }
			
			if("site".equals(type)){
				ModuleQueue modules = Framework.getInstance().getSubModules(parentPath);        
		        ItemQueue items = Framework.getInstance().getSubItems(parentPath);
				return ((items != null && items.size() > 0) || (modules != null && modules.size() > 0));
			}
			
			if("module".equals(type)){
				String path = father.getId().split("\\^")[0];
				ModuleQueue modules = Framework.getInstance().getSubModules(path);
				ItemQueue items = Framework.getInstance().getSubItems(path);
				return ((items != null && items.size() > 0) || (modules != null && modules.size() > 0));
			}else if("item".equals(type)){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		String type = father.getType();
		
		try {
			if(father.isRoot()){
				List sitelist = SiteCacheManager.getInstance().getSubSites("0");
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
								addNode(father, String.valueOf(site.getSiteId()),
										treeName, "site", false, curLevel,
										(String) null, (String) null,
										(String) null, map);
						}else{
							if (accessControl.checkPermission(
									""+site.getSiteId(),
									AccessControl.READ_PERMISSION,
									AccessControl.SITE_RESOURCE)) {
								addNode(father, String.valueOf(site.getSiteId()),
										treeName, "site", false, curLevel,
										(String) null, (String) null,
										(String) null, map);
							}
							
						}
					
					}
				}
			}
		
		
			if("site".equals(type)){
				siteId = father.getId().trim();
				siteName = father.getName().trim();
				List modules = Framework.getInstance().getSubModules(parentPath).getList();        
		        List items = Framework.getInstance().getSubItems(parentPath).getList();
		        for(int i = 0; modules != null && i < modules.size(); i++){
		        	Module module = (Module)modules.get(i);
		        	String moduleId = module.getId().trim();
		        	String moduleName = module.getName(request).trim();
		        	String path = module.getPath().trim();
		        	//判断应用是否在站点下
		            if(!this.appInSite(path,siteId) ) continue;
		        	addNode(father, path+"^"+siteId+"^"+siteName,
		        			moduleName, "module", false, curLevel,
							(String) null, siteId+"$$"+moduleId+":"+siteName+"$$"+moduleName,
							(String) null, (Map)null);
		        }
		        
		        for(int i = 0; items != null && i < items.size(); i++){
		        	Item item = (Item)items.get(i);
		        	String itemId = item.getId().trim();
		        	String itemName = item.getName(request).trim();
		        	String path = item.getPath().trim();
		        	//判断应用是否在站点下
		            if(!this.appInSite(path,siteId) ) continue;
		        	addNode(father, path+"^"+siteId+"^"+siteName,
		        			itemName, "item", false, curLevel,
		        			(String) null, siteId+"$$"+itemId+":"+siteName+"$$"+itemName,
		        			(String) null, (Map)null);
		        }
			}else if("module".equals(type)){
				String path = father.getId().split("\\^")[0];
				String siteId = father.getId().split("\\^")[1];
				String siteName = father.getId().split("\\^")[2];
				List modules = Framework.getInstance().getSubModules(path).getList();
				List items = Framework.getInstance().getSubItems(path).getList();
				for(int i = 0; modules != null && i < modules.size(); i++){
		        	Module module = (Module)modules.get(i);
		        	String moduleId = module.getId().trim();
		        	String moduleName = module.getName(request).trim();
		        	String modulepath = module.getPath().trim();
		        	//判断应用是否在站点下
		            if(!this.appInSite(modulepath, siteId) ) continue;
		        	addNode(father, modulepath+"^"+siteId+"^"+siteName,
		        			moduleName, "module", false, curLevel,
							(String) null, siteId+"$$"+moduleId+":"+siteName+"$$"+moduleName,
							(String) null, (Map)null);
		        }
				for(int i = 0; items != null && i < items.size(); i++){
		        	Item item = (Item)items.get(i);
		        	String itemId = item.getId().trim();
		        	String itemName = item.getName(request).trim();
		        	String itempath = item.getPath().trim();
		        	//判断应用是否在站点下
		            if(!this.appInSite(itempath, siteId) ) continue;
		        	addNode(father, itempath+"^"+siteId+"^"+siteName,
		        			item.getName(request), "item", false, curLevel,
		        			(String) null, siteId+"$$"+itemId+":"+siteName+"$$"+itemName,
		        			(String) null, (Map)null);
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}
	public boolean appInSite(String appPath,String siteId){
		boolean flag = false;
		StringBuffer sql = new StringBuffer();		
		DBUtil db = new DBUtil();
		
//		AccessControl accesscontroler = AccessControl.getInstance();
//		accesscontroler.checkAccess(request, response);
		
//		Site site = cmsManager.getCurrentSite();
		if(siteId != null && !"".equals(siteId))
		{
			
			sql.append("select t.site_id,t.app_id,t.app_path from  TD_CMS_SITEAPPS t  ");
			sql.append("where t.site_id=").append(siteId);
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
}
