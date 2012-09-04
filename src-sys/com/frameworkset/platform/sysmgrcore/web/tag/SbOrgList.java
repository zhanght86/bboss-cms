/*
 * Created on 2006-3-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SbOrgList extends DataInfoImpl implements Serializable {

      /* (non-Javadoc)
       * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String, boolean, long, int)
       */
      private Logger logger = Logger.getLogger(SbOrgList.class.getName());
      protected ListInfo getDataList(String sortKey, boolean desc, long offset,
                  int maxPagesize) {
           
           ListInfo listInfo = new ListInfo();
           String orgId= request.getParameter("orgId");
           String userId = super.accessControl.getUserID();
           String userAccount = super.accessControl.getUserAccount();
           try{
            OrgManager orgManager = SecurityDatabase.getOrgManager();
            Organization org = new Organization();
            org.setOrgId(orgId);
            
			List list = null;			
			listInfo =orgManager.getUserCanWriteAndReadOrgList(userId,userAccount,(int)offset,maxPagesize," o.PARENT_ID='"+orgId +"'");
           }catch(Exception e){
                 e.printStackTrace();
           }
			return listInfo;							         
      }

      /* (non-Javadoc)
       * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String, boolean)
       */
      protected ListInfo getDataList(String arg0, boolean arg1) {
            // TODO Auto-generated method stub
            return null;
      }

}
