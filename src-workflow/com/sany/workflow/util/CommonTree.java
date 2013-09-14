/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.sany.workflow.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;

/**
 * <p>Title: CommonTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-9-9
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonTree extends COMTree {
	  public boolean hasSon(ITreeNode father) {
	        String treeID = father.getId();

	        try {
	        	  return OrgCacheManager.getInstance().hasSubOrg(treeID);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }

	        return false;
	    }

	    public boolean setSon(ITreeNode father, int curLevel) {
	        String treeID = father.getId();
	      
	       
	        try {
	        	List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);
	        	
	        	if (orglist != null) {
	        		 for(int i = 0;  i < orglist.size(); i ++)
	                 {                	
	                    Organization sonorg = (Organization) orglist.get(i);
	                    String treeName  = "";
	                	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals(""))
	                		treeName = sonorg.getOrgName();
	                    else                    	
	                    	treeName = sonorg.getRemark5();
	                    
	                    Map<String,String> map = new HashMap<String,String>();
	                    String orgId = sonorg.getOrgId();
	                
	                	
	                	map.put("nodeLink", "javascript:query('"+orgId+"','"+WorkFlowConstant.BUSINESS_TYPE_ORG+"','"+treeName+"');");
	                	
	                		ITreeNode node = addNode(father, orgId, treeName,
	                            "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);  
	                		
	                		
	                
	                }
	            }

	        
	            
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return true;
	    }

}
