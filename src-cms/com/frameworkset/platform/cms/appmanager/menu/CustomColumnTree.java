package com.frameworkset.platform.cms.appmanager.menu;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.util.StringUtil;
/**
 * 根据module－id取单个模块   
 * @author Administrator
 *
 */
public class CustomColumnTree extends COMTree implements java.io.Serializable {
	MenuHelper menuHelper = null;
	 public void setPageContext(PageContext context){
	      super.setPageContext(context);
	      menuHelper = new MenuHelper(accessControl);
	        	
	}


	public boolean hasSon(ITreeNode parent) {
	
		String parentID = parent.getId();
		String parentType = parent.getType();
		if(parentType.equals("item")||parentID==null || parentType==null)
			return false;
		
		
		
		return menuHelper.getSubItems(parentID).size() > 0 || menuHelper.getSubModules(parentID).size() > 0; 
		
	}

	public boolean setSon(ITreeNode parent, int level) {
		String parentID = parent.getId();
		ItemQueue items = menuHelper.getSubItems(parentID);
		ModuleQueue submodules =  menuHelper.getSubModules(parentID);
		
		
		String treeid = "";
        String treeName = "";
        String moduleType = "module";
        String itemType = "item";

        boolean showHref = true;
        String memo = null;
        String radioValue = null;
        String checkboxValue = null;
        String path = "";
		for(int i = 0; i < submodules.size(); i ++)
        {

            Map params = new HashMap();
            Module submodule = submodules.getModule(i);
           
            treeid = submodule.getPath();
            treeName = submodule.getName(request);
            path = submodule.getPath();
            params.put("columnID",treeid);
            params.put("resId",submodule.getId());
           	params.put("resName",submodule.getName(request));
           
            addNode(parent, treeid, treeName, moduleType, false, level, memo,
                    radioValue, checkboxValue, path,params);
         
        }

        for(int i = 0; i < items.size(); i ++)
        {

            Map params = new HashMap();
            Item subitem = (Item)items.getItem(i);
            
            treeid = subitem.getPath();
            treeName = subitem.getName(request);
            path = subitem.getPath();
            showHref = true;
            params.put("columnID",treeid);
            params.put("resId",subitem.getId());
            params.put("resName",subitem.getName(request));
            
            String workspaceContent = null;
            if(!subitem.hasWorkspaceContentVariables()){
            	 workspaceContent = subitem.getWorkspaceContent();
            }else{
            	workspaceContent = Framework.getWorkspaceContent(subitem, accessControl);
            }
            params.put("nodeLink",StringUtil.getRealPath(request,workspaceContent));
          
            addNode(parent, treeid, treeName, itemType, showHref, level, memo,
                    radioValue, checkboxValue, path,params);     
        }
		return true;
	}

}
