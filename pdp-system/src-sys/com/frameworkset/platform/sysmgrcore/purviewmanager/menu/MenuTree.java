package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.sysmgrcore.entity.CSMenuModel;
import com.frameworkset.platform.sysmgrcore.manager.CSMenuManager;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl.ReportMenu;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl.ReportMenus;

public class MenuTree extends COMTree {
	private static final Logger log = LoggerFactory.getLogger(MenuTree.class);
	private Map menuHelpers = null;
	private String restypeId = "";
	private ReportMenus reportMenus = null;

	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		restypeId = request.getParameter("restypeId");
		if(restypeId.equals("column")){
			if(menuHelpers == null )
	        {
	        	menuHelpers = new HashMap();
	        	MenuHelper menuHelper = new MenuHelper(accessControl);
	        	menuHelpers.put("module",menuHelper);
	        	Map subsystems = Framework.getInstance().getSubsystems();
	        	if(subsystems != null && !subsystems.isEmpty())
	        	{
	        		
		        	Set set = subsystems.keySet();
		        	for(Iterator it = set.iterator();it.hasNext();)
		        	{
		        		String subsystem = (String)it.next();
		        		menuHelper = new MenuHelper(subsystem,accessControl);
		        		menuHelpers.put(subsystem,menuHelper);
		        		
		        	}
	        	}
	        }
	        else
	        {
	        	Collection set = menuHelpers.values();
	        	for(Iterator it = set.iterator();it.hasNext();)
	        	{
	        		MenuHelper menuHelper = (MenuHelper)it.next();
	        		menuHelper.resetControl(accessControl);
	        	}
	            
	        }
		}
		if(restypeId.equals("report_column")){
			if(ConfigManager.getInstance().getConfigBooleanValue("report.menu.swtich",false))
	    	{
		        reportMenus = (ReportMenus)session.getAttribute("hnds.reportMenu");
		        if(reportMenus == null)        
		        {
		        	ReportMenus temp = CSMenuManagerImpl.getReportMenus();
		        	if(reportMenus == null)
		        	{
		        		reportMenus = temp;
		        		session.setAttribute("hnds.reportMenu",reportMenus);
		        	}        	
		        } 
	    	}
		}
	}

	public boolean hasSon(ITreeNode father) {
		MenuHelper menuHelper = null;
		String path = father.getPath();
		boolean hasson = false;
		if(father.isRoot()){
			return true;
		}else if(father.getType().equals("subsystem")){
			if(!father.getId().equals("csmenu") && !father.getId().equals("reportmenu")){
				ModuleQueue queue = null;
	            ItemQueue iqueue = null;  
	            menuHelper = (MenuHelper)this.menuHelpers.get(father.getId());
	            queue = menuHelper.getModules();
	            iqueue = menuHelper.getItems();
	            if(iqueue.size() > 0)
	            	return true;
	            if(queue.size() > 0)
	            	return true;
			}else if(father.getId().equals("reportmenu")){
				return reportMenus.getTopLevels().size() > 0 ;
			}else{
				CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		return csMenuManager.hasSon("0");
			}
		}else if(restypeId.equals("column")){
			int idx = path.indexOf("::");
        	String subsystem = path.substring(0,idx);
        	menuHelper = (MenuHelper)this.menuHelpers.get(subsystem);
            MenuItem menuItem = menuHelper.getMenu(path);
            if(menuItem instanceof Item)
            {
                return hasson =  false;
            }
            else if(menuItem instanceof Module)
            {
                Module module  = (Module)menuItem;                    
                ItemQueue iqueue = null;
                ModuleQueue mqueue = null;
                if(ConfigManager.getInstance().securityEnabled())
                {
                    iqueue = menuHelper.getSubItems(module.getPath());
                    mqueue = menuHelper.getSubModules(module.getPath());
                }
                else
                {
                    iqueue = module.getItems();
                    mqueue = module.getSubModules();
                }
                    
                if(iqueue.size() > 0)
                	return true;
                if(mqueue.size() > 0)
                	return true;                
            }
		}else if(restypeId.equals("cs_column")){
			CSMenuManager csMenuManager = new CSMenuManagerImpl();
    		return csMenuManager.hasSon(father.getId());
		}else if(restypeId.equals("report_column")){
			return reportMenus.getReportMenu(father.getId()).isHasChild();
		}
		return hasson;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String parentPath = father.getPath();
		MenuHelper menuHelper = null;
		ModuleQueue submodules  = null;
        ItemQueue items = null;
		if(father.isRoot()){
			if(restypeId.equals("column")){
				addNode(father, "module", Framework.getInstance().getDescription(request).trim(), "subsystem", false, curLevel, "",
	    				(String)null, (String)null, (String)null);
			}else if(restypeId.equals("cs_column")){
				addNode(father, "csmenu", "CS菜单管理", "subsystem", false, curLevel, "",
	    				(String)null, (String)null, (String)null);
			}else if(restypeId.equals("report_column")){
				addNode(father, "reportmenu", "门户查询菜单管理", "subsystem", false, curLevel, "",
	    				(String)null, (String)null, (String)null);
			}
		}else if(father.getType().equals("subsystem")){
			if(!father.getId().equals("csmenu") && !father.getId().equals("reportmenu")){
				menuHelper = (MenuHelper)this.menuHelpers.get(father.getId());
	        	submodules  = menuHelper.getModules();
	            items = menuHelper.getItems();
			}else if(father.getId().equals("reportmenu")){
				List topLevels = reportMenus.getTopLevels();
				for(int i = 0; i < topLevels.size(); i ++)
            	{
            		ReportMenu rpt = (ReportMenu)topLevels.get(i);
            		String treeid = rpt.getId().trim();
            		String resName = rpt.getName().trim();
            		addNode(father, treeid, resName, "reportmenu", false, curLevel, "",
            				treeid+":"+resName, treeid+":"+resName, (String)null,(Map)null);
            	}
			}else{
				CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		List csmenus = csMenuManager.getCSMenuItems("0");
        		String menuId = "";
        		String menuTitle = "";
        		if(csmenus != null){
        			for(int i = 0; i < csmenus.size(); i++){
        				CSMenuModel menu  = (CSMenuModel)csmenus.get(i);
        				menuId = menu.getId().trim();
        				menuTitle = menu.getTitle().trim();
        				addNode(father, menu.getId().trim(), menuTitle, menu.getType().trim(), false, curLevel, "",
        	    				menuId+":"+menuTitle, menuId, (String)null);
        			}
        		}
			}
		}else{
			if(restypeId.equals("column")){
				int idx = parentPath.indexOf("::");
	        	String subsystem = parentPath.substring(0,idx);
	        	menuHelper = (MenuHelper)menuHelpers.get(subsystem);
	            
	            if(ConfigManager.getInstance().securityEnabled())
	            {                
	                items = menuHelper.getSubItems(parentPath);
	                submodules = menuHelper.getSubModules(parentPath);
	            }
			}if(restypeId.equals("cs_column")){
				CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		List csmenus = csMenuManager.getCSMenuItems(father.getId());
        		String menuId = "";
        		String menuTitle = "";
        		if(csmenus != null){
        			for(int i = 0; i < csmenus.size(); i++){
        				CSMenuModel menu  = (CSMenuModel)csmenus.get(i);
        				menuId = menu.getId().trim();
        				menuTitle = menu.getTitle().trim();
        				addNode(father, menu.getId().trim(), menuTitle, menu.getType().trim(), false, curLevel, "",
        	    				menuId+":"+menuTitle, menuId, (String)null);
        			}
        		}
        		
			}if(restypeId.equals("report_column")){
				List topLevels = reportMenus.getReportMenu(father.getId()).getSubReportMenus();
	        	for(int i = 0; i < topLevels.size(); i ++)
	        	{
	        		ReportMenu rpt = (ReportMenu)topLevels.get(i);
	        		String treeid = rpt.getId().trim();
            		String treeName = rpt.getName().trim();
	        		addNode(father, treeid, treeName, "reportmenu", false, curLevel, "",
	        				treeid+":"+treeName, treeid+":"+treeName, (String)null,(Map)null);
	        	}
			}
			
		}
		
		String treeid = null;
		String treeName = null;
		String path = null;
		if(submodules != null){
			for(int i = 0; i < submodules.size(); i ++){
				Module submodule = submodules.getModule(i);
				treeid = submodule.getId().trim();
				treeName = submodule.getName(request).trim();
				path = submodule.getPath().trim();
				addNode(father, treeid, treeName, "module", true, curLevel, "",
	                    treeid+":"+treeName, (String)null, path,(Map)null);
			}
		}
		if(items != null){
			for(int i = 0; i < items.size(); i++){
				Item subitem = (Item)items.getItem(i);
				treeid = subitem.getId();
				treeName = subitem.getName(request);
				path = subitem.getPath().trim();
				addNode(father, treeid, treeName, "item", true, curLevel, "",
						treeid+":"+treeName, (String)null, path,(Map)null);
			}
		}
		return true;
	}

}
