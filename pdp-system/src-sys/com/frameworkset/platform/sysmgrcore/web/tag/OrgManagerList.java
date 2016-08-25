package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 * 机构管理 机构下的用户排序
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrgManagerList extends DataInfoImpl implements Serializable {
      private Logger log = Logger.getLogger(OrgSubUserList.class);
      
      protected ListInfo getDataList(String sortKey, boolean desc, long offset,
                  int maxPagesize) {
    	  ListInfo listInfo = new ListInfo();
          String orgId= request.getParameter("orgId");
          try{
	            OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
	            listInfo = orgAdministrator.getAdministorsOfOrg(orgId,(int)offset,maxPagesize); 			
          }catch(Exception e){
                e.printStackTrace();
          }
		  return listInfo;	
      }
      
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
    	  ListInfo listInfo = new ListInfo();
          String orgId= request.getParameter("orgId");
          try{
        	  List list = new ArrayList();
        	  OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
        	  list = orgAdministrator.getAdministorsOfOrg(orgId);
	          listInfo.setDatas(list);			
          }catch(Exception e){
                e.printStackTrace();
          }
			return listInfo;
      }

}