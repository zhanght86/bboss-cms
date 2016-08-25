package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.util.StringUtil;



/**
 * <p>Title: BaseColumnTree</p>
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
public class BaseColumnTree extends COMTree implements Serializable{
    private static final Logger log = Logger.getLogger(BaseColumnTree.class);
    
    private MenuHelper menuHelper;
    private String subsystem;
    
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        subsystem = FrameworkServlet.getSubSystem(this.request,this.response,this.accessControl.getUserAccount());
        if(menuHelper == null )
            menuHelper = new MenuHelper(subsystem,accessControl);
        else
            menuHelper.resetControl(accessControl);
    }
    
    public boolean hasSon(ITreeNode father) { 
        String path = father.getId();
        boolean hasson = false;
        int idx =  path.indexOf("::");
        if(idx == -1) path = subsystem + "::" + path;
        Framework framework = Framework.getInstance(subsystem);
        if(father.isRoot() && path.equals(Framework.getSuperMenu(subsystem)))
        {   
        	
            ModuleQueue queue = null;
            ItemQueue iqueue = null;
            if(ConfigManager.getInstance().securityEnabled())
            {
                queue = this.menuHelper.getModules();
                iqueue = this.menuHelper.getItems();
            }
            else
            {
                queue = framework.getModules();
                iqueue = framework.getItems();
            }
            for(int i = 0; i < queue.size(); i ++)
            {
                MenuItem item = queue.getModule(i);
                if(item.isUsed())
                {
                    return hasson = true;

                }
            }
            
            for(int i = 0; i < iqueue.size(); i ++)
            {
                MenuItem item = iqueue.getItem(i);
                if(item.isUsed())
                {
                    return hasson = true;
                }
            }

        }
        else
        {
            MenuItem menuItem = framework.getMenu(path);
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
                    iqueue = this.menuHelper.getSubItems(module.getPath());
                    mqueue = this.menuHelper.getSubModules(module.getPath());
                }
                else
                {
                    iqueue = module.getItems();
                    mqueue = module.getSubModules();
                }
                    
                for(int i = 0; i < iqueue.size(); i ++)
                {
                    if(iqueue.getItem(i).isUsed())
                        return true;
                }
                
                for(int i = 0; i < mqueue.size(); i ++)
                {
                    if(mqueue.getModule(i).isUsed())
                        return true;
                }
                
            }
        }
        return hasson;
    }


    public boolean setSon(ITreeNode father, int curLevel) {
        String parentPath = father.getId();

        ModuleQueue submodules  = null;
        ItemQueue items = null;
        int idx =  parentPath.indexOf("::");
        if(idx == -1) parentPath = subsystem + "::" + parentPath;
        Framework framework = Framework.getInstance(subsystem);
        if(father.isRoot() && parentPath.equals(Framework.getSuperMenu(subsystem)))
        {            
            if(ConfigManager.getInstance().securityEnabled())
            {
	            submodules  = this.menuHelper.getModules();
	            items = this.menuHelper.getItems();
            }
            else
            {
                submodules  = framework.getModules();
	            items = framework.getItems();
            }            
        }
        else
        {
            if(ConfigManager.getInstance().securityEnabled())
            {                
                items = this.menuHelper.getSubItems(parentPath);
                submodules = this.menuHelper.getSubModules(parentPath);
            }
            else
            {
                Module module = framework.getModule(parentPath);
                items = module.getItems();
                submodules = module.getSubModules();                
            }
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
        String nodeLink = null;

        for(int i = 0; i < submodules.size(); i ++)
        {

            Map params = new HashMap();
            Module submodule = submodules.getModule(i);
            if(!submodule.isUsed())
                continue;
            treeid = submodule.getPath();
            treeName = submodule.getName(request);
            path = submodule.getPath();

            showHref = false;
            addNode(father, treeid, treeName, moduleType, showHref, curLevel, memo,
                    radioValue, checkboxValue, params);
        }

        for(int i = 0; i < items.size(); i ++)
        {

            Map params = new HashMap();
            Item subitem = (Item)items.getItem(i);
            if(!subitem.isUsed())
                continue;
            treeid = subitem.getPath();
            treeName = subitem.getName(request);
            path = subitem.getPath();
            showHref = true;
            nodeLink = getNodeLink(subitem,null);
            //通过参数动态设置节点链接，nodeLink为树型结构中的保留的参数名称，其他应用不能覆盖或使用nodeLink作为参数名称
            params.put("nodeLink",nodeLink);
            addNode(father, treeid, treeName, itemType, showHref, curLevel, memo,
                    radioValue, checkboxValue, params);
        }
        return true;
    }

    private String getNodeLink(MenuItem menuItem,String sessionid)
    {
      
        String nodeLink = request.getContextPath() + "/" + Framework.getUrl(Framework.CONTENT_CONTAINER_URL,sessionid)
        + Framework.MENU_PATH + "=" +
        StringUtil.encode(menuItem.getPath(), null) + "&"
        + Framework.MENU_TYPE + "=" +
//            Framework.CONTENT_CONTAINER + "&ancestor=1";
        Framework.CONTENT_CONTAINER;

        return nodeLink;      

    }

}
