package com.frameworkset.platform.cms.documentmanager.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.util.ListInfo;

public class DocumentList extends DataInfoImpl implements java.io.Serializable
{
     protected Logger log = LoggerFactory.getLogger(DocumentList.class);
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {	
		 DocumentManagerImpl 	  docManager = new DocumentManagerImpl();
		try {
			return  docManager.getDocList((int)offset,maxPagesize);
		} catch (DocumentManagerException e) {
			log.error("",e);
		}
		return null;
				
       
			
 }	
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
           
            return null;
      }
}