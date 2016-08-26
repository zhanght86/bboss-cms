package  com.frameworkset.platform.cms.sitemanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
//站点授权树
public class SitePurviewTree extends COMTree implements java.io.Serializable {

	 public boolean hasSon(ITreeNode father) {
	        String treeID = father.getId();
	       
	        //SiteManager siteManager = new SiteManagerImpl();
	        try {
	             
	        	
	        	//判断站点是否有子站点
	          
	            return SiteCacheManager.getInstance().hasSubSite(treeID);
	            
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        return false;
	    }

	    public boolean setSon(ITreeNode father, int curLevel) {
	        String treeID = father.getId();
	    	String roleId = (String)session.getAttribute("currRoleId");
			String roleTypeId = (String)session.getAttribute("role_type");
	       try {
	    
//	    		   SiteManager siteManager = new SiteManagerImpl();
//	    		   List sitelist;
//	    		   if(treeID.equals("0")){
//	    			  sitelist = siteManager.getTopLevelSite();
//	    		   }else{
//		             sitelist = siteManager.getDirectSubSiteList(treeID);
//	    		   }
	    	   
	    		   List sitelist = SiteCacheManager.getInstance().getSubSites(treeID);
	    		   
		            if (sitelist != null) {
		                Iterator iterator = sitelist.iterator();

		                while (iterator.hasNext()) {
		                
		                    Site sonsite = (Site) iterator.next();
		                	String treeName = sonsite.getName().trim();   
		                	String treeId = String.valueOf(sonsite.getSiteId()).trim();
		                    Map map = new HashMap();
		                    map.put("resId",treeId);
		                    map.put("resName", treeName);
		                    map.put("siteid",treeId);
		                    map.put("sitename",treeName);
		                    map.put("resTypeId", "site");
		                    String nodeType = "site";
//		                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,treeId,"site")||
//		                       AccessControl.hasGrantedRole(roleId,roleTypeId,treeId,"sitetpl")||
//		                       AccessControl.hasGrantedRole(roleId,roleTypeId,treeId,"sitefile")||
//		                       AccessControl.hasGrantedRole(roleId,roleTypeId,treeId,"site.channel")||
//		                       AccessControl.hasGrantedRole(roleId,roleTypeId,treeId,"site.doc")){
//		                    	nodeType = "site_true";
//		                    }else{
		                    	nodeType = "site";
//		                    }
		                    if(super.accessControl.checkPermission(treeId,AccessControl.WRITE_PERMISSION,AccessControl.SITE_RESOURCE))
			                    //第5个参数为是否可点
			                    addNode(father,treeId, treeName, nodeType,
										true, curLevel, (String) null, treeId + ":" + treeName,
										(String) null, map);
		                }
		            }
	    	
	    	  
	            
	      
	          
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return true;
	    }
}
