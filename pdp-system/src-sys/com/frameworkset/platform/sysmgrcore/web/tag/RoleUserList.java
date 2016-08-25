package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author 危达
 * 角色授予用户 角色已授予的用户列表
 */
public class RoleUserList extends DataInfoImpl implements Serializable{
      private Logger log = Logger.getLogger(OrgSubUserList.class);
      
      protected ListInfo getDataList(String sortKey, boolean desc, long offset,
                  int maxPagesize) {
    	  ListInfo listInfo = new ListInfo();
          String roleId= (String)session.getAttribute("currRoleId");;
          try{
	            RoleManager roleManager = new RoleManagerImpl();
	            if(super.accessControl.isAdmin())
	            {
	            	listInfo = roleManager.getRoleUserList(super.accessControl.getUserID(), roleId,(int)offset,maxPagesize,true);
	            }
	            else
	            {
	            	listInfo = roleManager.getRoleUserList(super.accessControl.getUserID(), roleId,(int)offset,maxPagesize,false);
	            }		
          }catch(Exception e){
                e.printStackTrace();
          }
		  return listInfo;	
      }

      protected ListInfo getDataList(String arg0, boolean arg1) {
    	  return null;
      }

}