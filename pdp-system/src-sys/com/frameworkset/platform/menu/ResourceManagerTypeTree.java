// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2006-6-4 10:49:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   ResourceTypeTree.java

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
import com.frameworkset.platform.security.AccessControl;

/**
 * 
 * <p>Title: ResourceManagerTypeTree</p>
 *
 * <p>Description: 资源管理授权树</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-29 11:08:04
 * @author biaoping.yin
 * @version 1.0
 */
public class ResourceManagerTypeTree extends COMTree implements Serializable
{

    public ResourceManagerTypeTree()
    {
    }

    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);

        if(resourceManager == null)
            resourceManager = new ResourceManager();
    }

    public boolean hasSon(ITreeNode father)
    {
       boolean hasson = false;
       try
       {
	        String id = father.getId();
	
	       
	        //根节点判断
	        if(father.isRoot())
	        {
	        	if( id.equals("0"))
	        	{
	        		return resourceManager.getResources().getResourceQueue().size() > 0;
	        	}
	        	else
	        	{
	        		return resourceManager.getResourceInfoByType(id).getSubResources().size() > 0;
	        	}
	        		
	        }
	        
	        
	        //判断是否是资源
	        if(father.getType().equals("resourceType") || father.getType().equals("resourceType_auto"))
	        {
	            hasson = resourceManager.getResourceInfoByType(id).getSubResources() != null && resourceManager.getResourceInfoByType(id).getSubResources().size() > 0;
	            
	        }
       }
       catch(Exception e)
       {
    	   e.printStackTrace();
       }
        
        return hasson;
    }



    public boolean setSon(ITreeNode father, int curLevel)
    {

        String treeid = "";
        String treeName = "";
       
        boolean showHref = true;
        String memo = null;
        String radioValue = null;
        String checkboxValue = null;
      

        String id = father.getId();
       
        if(father.isRoot() || father.getType().equals("resourceType") || father.getType().equals("resourceType_auto"))
        {
            ResourceInfoQueue resQueue = null;
          
           
            //System.out.println("resourceInfo:" + resourceInfo);

            if (father.isRoot())
            {
            	if(id.equals("0"))
            	{
            		resQueue = resourceManager.getResourceInfoQueue();
            	}
            	else
            	{
            		try
            		{
            			resQueue = resourceManager.getResourceInfoByType(id).getSubResources();
            		}
            		catch(Exception e)
            		{
            			e.printStackTrace();
            		}
            	}
            }
            else
            {
                resQueue = resourceManager.getResourceInfoByType(id).
                           getSubResources();
            }
            String roleId = (String)session.getAttribute("currRoleId");
			String roleTypeId = (String)session.getAttribute("role_type");

            for (int i = 0; resQueue != null && i < resQueue.size(); i++) 
            {
            	ResourceInfo res = resQueue.getResourceInfo(i);
            	if (super.accessControl.checkPermission(res.getId(),
						AccessControl.READ_PERMISSION,
						"resmanager"))
				{
					
	                Map params = new HashMap();
	                
	                if (res.isUsed()) 
	                {
	                    treeid = res.getId();
	                    treeName = res.getName();
	                    
	//                    if(treeName.equals("菜单资源")|| treeName.equals("tab资源")||treeName.equals("频道资源"))
	//                    	continue;
	                    
	                    //不显示“用户自定义资源授权”
	                    if(!(res.getId().equals("rescustom")) && !(res.getId().equals("resmanager"))){
	                    	params.put("resName",treeName);
		                    params.put("resId",treeid);
		                    params.put("resTypeId", "resmanager");
		                    params.put("auto",
		                               (new StringBuffer(String.valueOf(res.isAuto()))).
		                               toString());
		                    showHref = true;
		                    String resType = "resourceType";
		                    
		                    
	                    	if(AccessControl.hasGrantedRole(roleId,roleTypeId,res.getId(),"resmanager"))
	    					{
	                    		resType = resType + "_true";
	    					}
	                    	else
	                    	{
	                    		
	                    	}
	                        addNode(father, treeid, treeName, resType, showHref,
	                                curLevel, memo, radioValue, checkboxValue,
	                                params);
	                    }             	
					}
				}
            }
        }
        
        return true;
    }



    private static final Logger log;
    private ResourceManager resourceManager;

    static
    {
        log = LoggerFactory.getLogger(ResourceManagerTypeTree.class);
    }
}
