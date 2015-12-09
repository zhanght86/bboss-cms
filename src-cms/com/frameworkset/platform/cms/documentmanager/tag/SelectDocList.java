package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.util.ListInfo;

/**
 * 频道》引用管理》文档引用的文档列表数据获取接口
 * <p>Title: SelectDocList</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-12 13:19:01
 * @author huiqiong.zeng
 * @version 1.0
 */
public class SelectDocList extends DataInfoImpl implements java.io.Serializable
{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {	
		 DocumentManagerImpl 	  docManager = new DocumentManagerImpl();
			ListInfo listInfo = null;
			try {
				listInfo = docManager.getSelectDocList((int)offset,maxPagesize);
			} catch (DocumentManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				 
		return listInfo;	
 }	
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
           
            return null;
      }
}