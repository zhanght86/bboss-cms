package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.config.ResourceInfoQueue;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;

public class ResourceType1Tree extends COMTree implements Serializable{
	private static final Logger log = LoggerFactory.getLogger(ColumnTree.class);
//    private MenuHelper menuHelper;
    private ResourceManager resourceManager;
    String subsystem = "";
  

    
    
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
//        if(menuHelper == null )
//        
//            menuHelper = new MenuHelper(subsystem,accessControl);
        if(resourceManager == null)
            resourceManager = new ResourceManager();
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
        String id = father.getId();

        boolean hasson = false;
        if(father.isRoot())
        {
            return resourceManager.getResources().getResourceQueue().size() > 0;
        }
        else
        {
            return resourceManager.getResourceInfoByType(id).getSubResources().size() > 0;
        }
        
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
        String id = father.getId();
        ResourceInfoQueue resQueue = null;
        
        if(father.isRoot())
        {
            resQueue = this.resourceManager.getResourceInfoQueue();
        }
        else
        {            
            resQueue = this.resourceManager.getResourceInfoByType(id).getSubResources();            
        }

        String treeid = "";
        String treeName = "";
        String resType = "resourceType";        

        boolean showHref = true;
        String memo = null;
        String radioValue = null;
        String checkboxValue = null;
        String path = "";
        for(int i = 0; i < resQueue.size(); i ++)
        {

            Map params = new HashMap();
            ResourceInfo res = resQueue.getResourceInfo(i);
            if(!res.isUsed())
                continue;
            treeid = res.getId();
            treeName = res.getName();
            
            if(treeName.equals("资源类资源")||treeName.equals("用户资源")||treeName.equals("用户组资源"))
            	continue;
            
           params.put("restypeName",treeName);          
           params.put("restypeId",treeid); 
           params.put("auto",res.isAuto()+"");

            showHref = true;
            if(res.isAuto())
                
	            addNode(father, treeid, treeName, resType + "_auto", showHref, curLevel, memo,
	                    radioValue, checkboxValue, params);
            else
                addNode(father, treeid, treeName, resType, showHref, curLevel, memo,
	                    radioValue, checkboxValue, params);
            
        }

        
        return true;


    }

}
