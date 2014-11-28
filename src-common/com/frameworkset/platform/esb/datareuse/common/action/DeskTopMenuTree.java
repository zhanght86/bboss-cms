package com.frameworkset.platform.esb.datareuse.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopMenuBean;
import com.frameworkset.platform.esb.datareuse.common.service.DeskTopMenuShorcutManager;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.menu.ColumnTree;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class DeskTopMenuTree extends COMTree {
	private static final Logger log = Logger.getLogger(ColumnTree.class);
	private MenuHelper menuHelper;
	private Map<String,String> defaults;
	
	 public void setPageContext(PageContext context){
	        super.setPageContext(context);        
	        if(menuHelper == null )
	        {
	        	String customtype = request.getParameter("customtype");
	        	
	        	
	        	menuHelper = MenuHelper.getMenuHelper(request);
	        	DeskTopMenuShorcutManager m = WebApplicationContextUtils.getWebApplicationContext().getTBeanObject("deskTopMenuShorcutManager", DeskTopMenuShorcutManager.class);
	    		DeskTopMenuBean bean = new DeskTopMenuBean();
	    		if(customtype != null && customtype.equals("default"))
	        	{
	    			bean.setUserid("-1");
	        	}
	    		else
	    		{
	    			bean.setUserid(accessControl.getUserID());
	    		}
	    		
	    		bean.setSubsystem(accessControl.getCurrentSystemID());
	    		try
				{
	    			
	    			defaults = m.getUserDeskMapMenus(bean);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        else
	        {
	        	menuHelper.resetControl(accessControl);
	        }
	    }
	@Override
	public boolean hasSon(ITreeNode father) {
		 String path = father.getPath();
	        boolean hasson = false;
	       
	        
	        if(father.isRoot()){
	        	ItemQueue items = this.menuHelper.getItems();
	        	ModuleQueue moudules = this.menuHelper.getModules();
	        	
	        	return (items != null && items.size() > 0) || (moudules != null && moudules.size() > 0);
	       
	    	}
	    	
	        else 
	        {        	
	        	
	        	
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

	        return hasson;
	}

	@Override
	public boolean setSon(ITreeNode father, int curLevel) {
		String parentPath = father.getPath();
        ModuleQueue submodules  = null;
        ItemQueue items = null;
        
	
		String treeid = "";
        String treeName = "";
        String moduleType = "module";
        String itemType = "item";        
        if(father.isRoot())
        {       
            items = menuHelper.getItems();
            submodules = menuHelper.getModules();
	            
        	

        }
		else
		{
			items = menuHelper.getSubItems(parentPath);
            submodules = menuHelper.getSubModules(parentPath);
		}
			
        boolean showHref = true;
        String memo = null;
        String radioValue = null;
        String checkboxValue = null;
        String path = "";     
        for(int i = 0; i < submodules.size(); i ++)        {

            Map params = new HashMap();
            Module submodule = submodules.getModule(i);
            treeid = submodule.getPath();
            treeName = submodule.getName(request);
            path = submodule.getPath();
            params.put("resTypeId","column");
            params.put("columnID",treeid);
            params.put("resId",submodule.getId());
           	params.put("resName",submodule.getName(request));
           	radioValue = null;
           	checkboxValue = null;
            showHref = true;
            if(submodule.isUsed())
            {
            	addNode(father, treeid, treeName, moduleType, showHref, curLevel, memo,
                        radioValue, checkboxValue, path,params);
            }
        }
        for(int i = 0; i < items.size(); i ++)
        {

            Map params = new HashMap();
            Item subitem = (Item)items.getItem(i);
            treeid = subitem.getPath();
            treeName = subitem.getName(request);
            path = subitem.getPath();
            showHref = true;
            params.put("resTypeId","column");
            params.put("columnID",treeid);
            params.put("resId",subitem.getId());
            params.put("resName",subitem.getName(request));
            if(defaults.containsKey(treeid))
            {
	            params.put("node_checkboxchecked", true);
	            params.put("node_checkboxdisabled", true);
            }
         	radioValue = treeid;
           	checkboxValue = treeid;
			if(subitem.isUsed())
			{
				addNode(father, treeid, treeName, itemType, showHref, curLevel, memo,
	                    radioValue, checkboxValue, path,params); 
			}    
	    }
        return true;       
	}

}
