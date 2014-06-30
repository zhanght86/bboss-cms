package com.frameworkset.platform.holiday.area.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;

public class SimpleOrgTree extends COMTree {
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
	                
	                	
	                	map.put("nodeLink", "javascript:orgClick('"+orgId+"','"+treeName+"');");
	                	
                        addNode(father, sonorg.getOrgId(),
                        		treeName, "org", true, curLevel,
                            (String) null, (String) null, (String) null, map);
	                   
	                }
	            }

	        
	            
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return true;
	    }
	    
	   
}


