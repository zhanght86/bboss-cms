/*
 * Created on 2006-3-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.menu;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;



/**
 * @author ok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResourceTree extends COMTree implements Serializable {

      /* (non-Javadoc)
       * @see com.frameworkset.common.tag.tree.COMTree#hasSon(com.frameworkset.common.tag.tree.itf.ITreeNode)
       */
      public boolean hasSon(ITreeNode father) {
            // TODO Auto-generated method stub
            String treeID = father.getId();
            
            try {
                  ResManager resourceManager = SecurityDatabase.getResourceManager();
                  Res resource = new Res();
                  resource.setResId(treeID);
                  return resourceManager.isContainChildRes(resource);
                 
                 
              } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
            return false;
      }

      /* (non-Javadoc)
       * @see com.frameworkset.common.tag.tree.COMTree#setSon(com.frameworkset.common.tag.tree.itf.ITreeNode, int)
       */
      public boolean setSon(ITreeNode father, int curLevel) {
            String treeID = father.getId();
            ResManager resourceManager;
           
            try {
                resourceManager = SecurityDatabase.getResourceManager();
                Res resource = new Res();
                resource.setResId(treeID);
                List reslist = resourceManager.getChildResList(resource);
                if(reslist!=null){
                Iterator iterator = reslist.iterator();
                while (iterator.hasNext()) {
                    Res res = (Res) iterator.next();    
                    Map map = new HashMap();
                    map.put("resId",res.getResId());  
                  
                    addNode(father, res.getResId(), res.getTitle(), "res", true,curLevel,
                        (String) null, (String) null, (String) null,map);
                }
              
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            

            return true;
        }
      }


