package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 站内（整站文档查询）
 * <p>Title: SiteDocumentList</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-12 13:19:53
 * @author xingwang.jiao
 * @version 1.0
 */
public class SiteDocumentList extends DataInfoImpl implements java.io.Serializable
{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {	
		 
		 DocumentManagerImpl 	  docManager = new DocumentManagerImpl();
			try {
				return  docManager.getSiteDocumentList( (int)offset,maxPagesize);
			 
		}catch(Exception e){
	         e.printStackTrace();
		}
		return null;	
	}	
      
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
	
	protected String getChnlIds(String siteid)
	{
		String chlIds = "";
		//取当前用户username
		//String curUserId = accessControl.getUserID();
		DBUtil db = new DBUtil();
		String sql = "select channel_id from td_cms_channel where site_id = " + siteid;
		try
		{
			db.executeSelect(sql);
			if( db.size() > 0)
			{
				for( int i=0;i<db.size();i++)
				{
					String chlid = db.getInt(i,0) + "";
					if( accessControl.checkPermission(chlid,AccessControl.WRITE_PERMISSION,AccessControl.CHANNEL_RESOURCE)
						|| accessControl.checkPermission(siteid,AccessControl.WRITE_PERMISSION,AccessControl.SITECHANNEL_RESOURCE))
					{
						if( !chlIds.equals(""))
							chlIds += "," + db.getInt(i,0);
						else
							chlIds += db.getInt(i,0);
					}
				}
				if( chlIds.equals(""))
					chlIds = "''";
			}
			else
				chlIds = "''";
		}
		catch(Exception e)
		{
			chlIds = "''";
			e.printStackTrace();
		}
		return chlIds;
	}
}