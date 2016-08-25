package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
public class TestTree extends COMTree implements Serializable{


    public boolean hasSon(ITreeNode father) {
        String treeID = father.getId();
        if(father.isRoot() || treeID.equals("node1")  )
              return true;
        else
            return false;
    }

    public boolean setSon(ITreeNode father, int curLevel) {
        String treeID = father.getId();
        if(father.isRoot())
        {
              for(int i = 0; i < 20;i ++)
              {
                  this.addNode(father, father.getId() + i, father.getName() + i, "postion", true,
                             curLevel, null, null, (String)null);
              }
        }

        if(treeID.equals("node1"))
        {
              Map params = new HashMap();
              params.put("key","value");
              params.put("key1","value1");
              for(int i = 0; i < 100;i ++)
              {
                  this.addNode(father, father.getId() + i, father.getName() + i, null, true,
                             curLevel, null, null, (String)null,params);
              }
        }


        return true;
    }
}
