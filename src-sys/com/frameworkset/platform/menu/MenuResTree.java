package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.framework.SubSystem;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.CSMenuModel;
import com.frameworkset.platform.sysmgrcore.manager.CSMenuManager;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl.ReportMenu;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl.ReportMenus;
import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class MenuResTree extends COMTree implements Serializable{
	private static final Logger log = Logger.getLogger(AuthorColumnTree.class);    
    private Map menuHelpers = null;
    public static boolean isCS = false;//标示是否有CS菜单
    private ReportMenus reportMenus = null;
    public MenuResTree(){
    	
    }
    
    public void setPageContext(PageContext context){
        super.setPageContext(context);        
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
    
    /**
     * hasSon
     *
     * @param iTreeNode ITreeNode
     * @return boolean
     * @todo Implement this com.frameworkset.common.tag.tree.itf.ActiveTree
     *   method
     */
    public boolean hasSon(ITreeNode father) { 
        String path = father.getPath();
        boolean hasson = false;
        MenuHelper menuHelper = null;
        
        if(father.isRoot()){
        	return true;
        }
        else if(father.getType().equals("subsystem"))
    	{
        	if(!father.getId().equals("csmenu") && !father.getId().equals("reportmenu")) //判断是否是cs菜单管理的根
        	{
	    		ModuleQueue queue = null;
	            ItemQueue iqueue = null;  
	            menuHelper = (MenuHelper)this.menuHelpers.get(father.getId());
	            queue = menuHelper.getModules();
	            iqueue = menuHelper.getItems();
	            if(iqueue.size() > 0)
	            	return true;
	            if(queue.size() > 0)
	            	return true;
        	}
        	else if(father.getId().equals("reportmenu"))
        	{
        		return reportMenus.getTopLevels().size() > 0 ;
        	}
        	else //是cs菜单管理的根
        	{
        		CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		return csMenuManager.hasSon("0");
        	}
    	}
        else 
        {
        	
        	if(father.getType().equals("subsys") 
        			|| father.getType().equals("menu_bar") 
        			|| father.getType().equals("menu_item")
        			|| father.getType().equals("subsys_true") 
        			|| father.getType().equals("menu_bar_true") 
        			|| father.getType().equals("menu_item_true"))//如果是cs的菜单,调用cs的接口进行判断father是否有下级菜单
        	{
        		CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		return csMenuManager.hasSon(father.getId());
        	}
        	else if(father.getType().equals("reportmenu"))//如果是cs的菜单,调用cs的接口进行判断father是否有下级菜单
        	{
        		
        		return reportMenus.getReportMenu(father.getId()).isHasChild();
        	}
        	else
        	{
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
        	}
        }

        return hasson;
    }

    
    /**
     * setSon
     *
     * @param iTreeNode ITreeNode
     * @param _int int
     * @return boolean
     * @todo Implement this com.frameworkset.common.tag.tree.itf.ActiveTree
     *   method
     * CS菜单资源,作为节点添加的菜单管理树中  
     */
    public boolean setSon(ITreeNode father, int curLevel) {
        String parentPath = father.getPath();
        ModuleQueue submodules  = null;
        ItemQueue items = null;
        MenuHelper menuHelper = null;
        String resTypeId=(String)request.getAttribute("resTypeId");
    	String roleId = (String)session.getAttribute("currRoleId");
    	String roleTypeId = (String)request.getAttribute("role_type");
    	List roleIds = (List)request.getAttribute("roleIds");
    	String treeid = "";
        String treeName = "";
        String moduleType = "module";
        String itemType = "item";
        //System.out.println("resTypeId:"+resTypeId+" ,roleTypeId:"+roleTypeId+" ,roleIds1:"+roleIds);
        if(father.isRoot())
        {
        	addNode(father, "module", Framework.getInstance().getDescription(request), "subsystem", false, curLevel, "",
                    (String)null, (String)null, (String)null);
        	Map subsystems = Framework.getInstance().getSubsystems();
        	if(subsystems != null && !subsystems.isEmpty())
        	{
	        	Set set = subsystems.keySet();
	        	for(Iterator it = set.iterator();it.hasNext();)
	        	{
	        		String subsystem = (String)it.next();
	        		SubSystem sys = (SubSystem)subsystems.get(subsystem);
	        		addNode(father, subsystem, sys.getName(request), "subsystem", false, curLevel, "",
	        				(String)null, (String)null, (String)null);
	        		
	        	}
        	}
        	//如果系统启用了cs菜单,添加cs菜单根节点
        	if(ConfigManager.getInstance().getConfigBooleanValue("cs.menu.swtich",false))
        	{
	        	addNode(father, "csmenu", "CS菜单管理", "subsystem", false, curLevel, "",
	    				(String)null, (String)null, (String)null);
	        	
				this.isCS = true;
        	}
        	if(ConfigManager.getInstance().getConfigBooleanValue("report.menu.swtich",false)){
        		addNode(father, "reportmenu", "门户查询菜单管理", "subsystem", false, curLevel, "",
	    				(String)null, (String)null, (String)null);
        	}
         	return true;
        }
        else if(father.getType().equals("subsystem"))
        {
        	if(!father.getId().equals("csmenu") && !father.getId().equals("reportmenu")) //判断是否是cs菜单管理的根节点
        	{
	        	menuHelper = (MenuHelper)this.menuHelpers.get(father.getId());
	        	submodules  = menuHelper.getModules();
	            items = menuHelper.getItems();
        	}
        	else if(father.getId().equals("reportmenu"))
        	{
        		List topLevels = reportMenus.getTopLevels();
            	for(int i = 0; i < topLevels.size(); i ++)
            	{
            		ReportMenu rpt = (ReportMenu)topLevels.get(i);
            		 Map params = new HashMap();
                     
                     treeid = rpt.getId();
                     treeName = rpt.getName().trim();
                     
                     
                     params.put("resTypeId","report_column");
                     params.put("columnID",treeid);
                     params.put("resId",rpt.getId());
                     params.put("resName",treeName);
                     String cbValue = rpt.getId()+";"+treeName+";report_column";
                     if(this.hasMenuChecked(roleIds,roleTypeId,rpt.getId(),"report_column")){//AccessControl.hasGrantedRole(roleId,roleTypeId,menu.getId(),"cs_column")){
     					//如果已经赋权复选框选中
     					params.put("node_checkboxchecked",new Boolean(true));
     				}
            		if(super.accessControl.checkPermission(rpt.getId(), "visible", "report_column"))
    				{
        				addNode(father, rpt.getId(), treeName, "reportmenu", true, curLevel, "",
        	                    (String)null, cbValue, cbValue,params);
    				}
            		else
            		{
            			addNode(father, rpt.getId(), treeName, "reportmenu", false, curLevel, "",
        	                    (String)null, (String)null, (String)null,params);
            		}
            	}
            	return true;
        	}
        	else  
        	{
        		CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		List csmenus = csMenuManager.getCSMenuItems("0");
        		if(csmenus != null)
        		{
        			for(int i = 0; i < csmenus.size(); i ++)
        			{
        				Map params = new HashMap();
        				
        				CSMenuModel menu  = (CSMenuModel)csmenus.get(i);
        				String type = menu.getType();        				
        				/**
        				 *  String resId = request.getParameter("resId");
							String resName = request.getParameter("resName");
							String columnID = request.getParameter("columnID");
							String menuPath = request.getParameter("nodePath");
							String resTypeId2 = "column"; //资源类型id
							String resId2 = resId; //资源id
							String title = resName; //资源名称
        				 */
        				
        				params.put("resTypeId","cs_column");
//        				params.put("resId2",menu.getId());        				
        				params.put("resId",menu.getId());
        				params.put("resName",menu.getTitle());
        				params.put("columnID","cs_column");
        				params.put("menuPath","");
        				
        				//params.put("nodeLink",CMSUtil.getPath(request.getContextPath() , "/sysmanager/menumanager/resCSMenu_tab.jsp"));
        				String cbValue = menu.getId()+";"+menu.getTitle()+";cs_column";
        				if(this.hasMenuChecked(roleIds,roleTypeId,menu.getId(),"cs_column")){//AccessControl.hasGrantedRole(roleId,roleTypeId,menu.getId(),"cs_column")){
        					//如果已经赋权复选框选中
        					params.put("node_checkboxchecked",new Boolean(true));
        				}
        				if(super.accessControl.checkPermission(menu.getId(), "visible", "cs_column"))
        				{
	        				addNode(father, menu.getId(), menu.getTitle(), type, true, curLevel, "",
	        	                    (String)null, cbValue, cbValue,params);
        				}
        			}
        		}
        		return true;
        	}
        }  
        else if(father.getType().equals("reportmenu"))
        {
        	List topLevels = reportMenus.getReportMenu(father.getId()).getSubReportMenus();
        	for(int i = 0; i < topLevels.size(); i ++)
        	{
        		ReportMenu rpt = (ReportMenu)topLevels.get(i);
        		 Map params = new HashMap();
                 
                 treeid = rpt.getId().trim();
                 treeName = rpt.getName().trim();
                 
                 
                 params.put("resTypeId","report_column");
                 params.put("columnID",treeid);
                 params.put("resId",rpt.getId());
                 params.put("resName",rpt.getName());
                 String cbValue = rpt.getId()+";"+rpt.getName()+";report_column";
                 if(this.hasMenuChecked(roleIds,roleTypeId,rpt.getId(),"report_column")){//AccessControl.hasGrantedRole(roleId,roleTypeId,menu.getId(),"cs_column")){
  					//如果已经赋权复选框选中
  					params.put("node_checkboxchecked",new Boolean(true));
  				}
        		if(super.accessControl.checkPermission(rpt.getId(), "visible", "report_column"))
				{
    				addNode(father, rpt.getId(), rpt.getName().trim(), "reportmenu", true, curLevel, "",
    	                    (String)null, cbValue, cbValue,params);
				}
        		else
        		{
        			addNode(father, rpt.getId(), rpt.getName().trim(), "reportmenu", false, curLevel, "",
    	                    (String)null, (String)null, (String)null,params);
        		}
        	}
        	return true;
        }
        else
        {  
        	if(father.getType().equals("subsys") 
        			|| father.getType().equals("menu_bar") 
        			|| father.getType().equals("menu_item")
        			|| father.getType().equals("subsys_true") 
        			|| father.getType().equals("menu_bar_true") 
        			|| father.getType().equals("menu_item_true"))//如果是cs的菜单,调用cs的接口获取cs下级菜单
        	{
        		CSMenuManager csMenuManager = new CSMenuManagerImpl();
        		List csmenus = csMenuManager.getCSMenuItems(father.getId());
        		if(csmenus != null)
        		{
        			for(int i = 0; i < csmenus.size(); i ++)
        			{
        				Map params = new HashMap();
        				CSMenuModel menu  = (CSMenuModel)csmenus.get(i);
        				String type = menu.getType().trim();
        				String menuId = menu.getId().trim();
        				String title = menu.getTitle().trim();
        				String cbValue = menuId+";"+title+";cs_column";
        				if(this.hasMenuChecked(roleIds,roleTypeId,menuId,"cs_column")){//AccessControl.hasGrantedRole(roleId,roleTypeId,menu.getId(),"cs_column")){
        					//如果已经赋权显示不同的图标
        					params.put("node_checkboxchecked",new Boolean(true));
        				}
        				params.put("resTypeId","cs_column");
        				params.put("resId",menuId);
        				params.put("resName",title);
        				params.put("columnID","cs_column");
        				params.put("menuPath","");
        				
        				//params.put("nodeLink",CMSUtil.getPath(request.getContextPath() , "/sysmanager/menumanager/resCSMenu_tab.jsp"));
        				if(super.accessControl.checkPermission(menuId, "visible", "cs_column"))
        				{
	        				addNode(father, menuId, title, type, true, curLevel, "",
	        	                    (String)null, cbValue, cbValue,params);
        				}
        			}
        		}
        		return true;        		
        	}        	
        	else
        	{
	        	int idx = parentPath.indexOf("::");
	        	String subsystem = parentPath.substring(0,idx);
	        	menuHelper = (MenuHelper)menuHelpers.get(subsystem);
	            
	            if(ConfigManager.getInstance().securityEnabled())
	            {                
	                items = menuHelper.getSubItems(parentPath);
	                submodules = menuHelper.getSubModules(parentPath);
	            }
        	}

        }
        boolean showHref = true;
        String memo = null;
        String radioValue = null;
        String checkboxValue = null;
        String path = "";     
        for(int i = 0; i < submodules.size(); i ++)        {

            Map params = new HashMap();
            Module submodule = submodules.getModule(i);
            treeid = submodule.getPath().trim();
            treeName = submodule.getName(request).trim();
            path = submodule.getPath().trim();
            params.put("resTypeId","column");
            params.put("columnID",treeid);
            params.put("resId",submodule.getId());
           	params.put("resName",treeName);
           	checkboxValue = submodule.getId()+";"+treeName+";column";
			if(this.hasMenuChecked(roleIds,roleTypeId,submodule.getId(),resTypeId)){
				params.put("node_checkboxchecked",new Boolean(true));
			}
            showHref = true;
            addNode(father, treeid, treeName, moduleType, showHref, curLevel, memo,
                    radioValue, checkboxValue, path,params);
         
        }
        for(int i = 0; i < items.size(); i ++)
        {

            Map params = new HashMap();
            Item subitem = (Item)items.getItem(i);
            treeid = subitem.getPath().trim();
            treeName = subitem.getName(request).trim();
            path = subitem.getPath().trim();
            showHref = true;
            params.put("resTypeId","column");
            params.put("columnID",treeid);
            params.put("resId",subitem.getId());
            params.put("resName",treeName);
//          AccessControl.hasGrantedRole(roleId,roleTypeId,subitem.getId(),resTypeId)){
            checkboxValue = subitem.getId()+";"+treeName+";column";
			if(this.hasMenuChecked(roleIds,roleTypeId,subitem.getId(),resTypeId)){
				params.put("node_checkboxchecked",new Boolean(true));
			}
            addNode(father, treeid, treeName, itemType, showHref, curLevel, memo,
                    radioValue, checkboxValue, path,params);
	    }
        return true;        

    }
    
    protected void buildContextMenus()
    {
    	Menu menu = new Menu();
    	menu.setIdentity("module");
    	menu.addContextMenuItem(Menu.MENU_OPEN);
    	menu.addContextMenuItem(Menu.MENU_EXPAND);
    	menu.addSeperate();
    	Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
    	menuitem.setName("保存");
    	menu.addContextMenuItem(menuitem);
    	addContextMenuOfType(menu);
    	
    	Menu menu1 = new Menu();
    	menu1.setIdentity("item");
    	menu1.addContextMenuItem(Menu.MENU_OPEN);
    	menu1.addContextMenuItem(Menu.MENU_EXPAND);
    	menu1.addSeperate();
    	Menu.ContextMenuItem item = new Menu.ContextMenuItem();
    	item.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
    	item.setName("子菜单");
    	Menu submenu = new Menu();
    	Menu.ContextMenuItem _item = new Menu.ContextMenuItem();
    	_item.setIcon(request.getContextPath() + "/sysmanager/images/actions.gif");
    	_item.setName("菜单");
    	_item.setTarget("base_properties_container");
    	_item.setLink(request.getContextPath() + "/testFrame_workspace.jsp");
    	submenu.addContextMenuItem(_item);
    	submenu.setIdentity("submenu");
    	item.setSubMenu(submenu);
    	menu1.addContextMenuItem(item);
    	menu1.addSeperate();
    	Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
    	menuitem1.setName("保存");
    	menu1.addContextMenuItem(menuitem1);
    	addContextMenuOfType(menu1);
    }
    
    /**
     * 判断
     * @param orgId 机构ID
     * @return
     */
    private boolean hasMenuChecked(List roleIds,String roleType,String mId,String resTypeId){
    	
    	
    	//List resIds = (List)request.getAttribute("resIds");
    	//System.out.println(orgIds);
    	int count = 0;
    	if(roleIds!=null && roleIds.size()>0){
    		for(int i=0;i<roleIds.size();i++){
    			String roleId = (String)roleIds.get(i);
    			//if(roleType.equals("user")){
    				//if(AccessControl.hasGrantedRole(roleId,"role",mId,t)) return true ;
    			//}
    			if(AccessControl.hasGrantedRole(roleId,roleType,mId,resTypeId)) return true ;
    		}
    	}
    	return false;
    }
}
