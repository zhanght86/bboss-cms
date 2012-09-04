/*
 * Created on 2006-3-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OperList extends DataInfoImpl implements Serializable {

      /* (non-Javadoc)
       * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String, boolean, long, int)
       */
      private Logger logger = Logger.getLogger(UserList.class.getName());
      protected ListInfo getDataList(String sortKey, boolean desc, long offset,
                  int maxPagesize) {
            ListInfo listInfo = new ListInfo();
//            String groupId = StringUtil
//            .replaceNull(request.getParameter("groupId"));            	
//            try{
//            OperManager operManager = SecurityDatabase.getOperManager();
// 			List list = null;			
// 			PageConfig pageConfig = operManager.getPageConfig();		
// 			pageConfig.setPageSize(maxPagesize);
// 			pageConfig.setStartIndex((int)offset);
// 					
//			Opergroup opergroup = new Opergroup();
//			opergroup.setGroupId(groupId);
//			list = operManager.getOperList(opergroup);
// 			listInfo.setTotalSize(pageConfig.getTotalSize());
// 			listInfo.setDatas(list);
//            }catch(Exception e){
//                  e.printStackTrace();
//            }
//            request.getSession().setAttribute("groupId", groupId);
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
