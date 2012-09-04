package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class ColumnCmsTree extends COMTree {
    private static final Logger log = Logger.getLogger(ColumnCmsTree.class);
    
    MenuHelper menuHelper ;
    
    public ColumnCmsTree(){
    }
    
    public void setPageContext(PageContext context){
        super.setPageContext(context);
        menuHelper = new MenuHelper("cms",accessControl);
        
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
        //System.out.println("-------------"+path);
        boolean hasson = false;
       
        if(father.isRoot())
    	{
    		ModuleQueue queue = null;
            ItemQueue iqueue = null;
            queue = menuHelper.getModules();
            iqueue = menuHelper.getItems();
            if(iqueue.size() > 0)
            	return true;
            if(queue.size() > 0)
            	return true;
    	}
        else
        {
        	
            MenuItem menuItem = MenuHelper.getMenu(path);
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

    /**
     * setSon
     *
     * @param iTreeNode ITreeNode
     * @param _int int
     * @return boolean
     * @todo Implement this com.frameworkset.common.tag.tree.itf.ActiveTree
     *   method
     */
    public boolean setSon(ITreeNode father, int curLevel) {
        String parentPath = father.getPath();
        ModuleQueue submodules  = null;
        ItemQueue items = null;
       
        String resTypeId=request.getParameter("resTypeId");
		String roleId = (String)session.getAttribute("currRoleId");
		/**
		 * user
		 * role
		 * oranization
		 */
		String roleTypeId = (String)session.getAttribute("role_type");
        
        if(father.isRoot())
        {
        	
        	submodules  = menuHelper.getModules();
            items = menuHelper.getItems();
        }
        	
        else
        {            
            items = menuHelper.getSubItems(parentPath);
            submodules = menuHelper.getSubModules(parentPath);
        }

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
           	checkboxValue = submodule.getId()+";"+submodule.getName(request)+";"+"column";
           	if(this.hasMenuChecked(roleId,roleTypeId,submodule.getId(),resTypeId)){
				params.put("node_checkboxchecked",new Boolean(true));
			}
			if(AccessControl.hasGrantedRole(roleId,roleTypeId,submodule.getId(),resTypeId)){
				moduleType = "module_true";
			}else{
				moduleType = "module";
			}
            showHref = true;
            addNode(father, treeid, treeName, moduleType, showHref, curLevel, memo,
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
            checkboxValue = subitem.getId()+";"+subitem.getName(request)+";"+"column";
            if(this.hasMenuChecked(roleId,roleTypeId,subitem.getId(),resTypeId)){
				params.put("node_checkboxchecked",new Boolean(true));
			}
            if(AccessControl.hasGrantedRole(roleId,roleTypeId,subitem.getId(),resTypeId)){
				itemType = "item_true";
			}else{
				itemType = "item";
			}
            addNode(father, treeid, treeName, itemType, showHref, curLevel, memo,
                    radioValue, checkboxValue, path,params);     
        }
        
        return true;
        

    }
    
    protected void buildContextMenus()
    {
    	
    }
    
    /**
     * 判断
     * @param orgId 机构ID
     * @return
     */
    private boolean hasMenuChecked(String roleId,String roleType,String mId,String resTypeId){
    	if(AccessControl.hasGrantedRole(roleId,roleType,mId,resTypeId)){
    		return true ;
    	}else{
    		return false;
    	}
    }
    
}