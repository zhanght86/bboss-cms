package com.frameworkset.platform.cms.mailmanager.tag;

import com.frameworkset.platform.cms.mailmanager.*;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class MailServerList extends DataInfoImpl implements java.io.Serializable {

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize){				
			String sql = "select * from TD_CMS_MAILSERVERINFO order by id desc";
			ListInfo listInfo=null;
			try {
				MailServerManager msm = new MailServerManager();
				listInfo =msm.getMailServerList(sql,(int)offset,maxPagesize);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			return listInfo;
		}

		protected ListInfo getDataList(String arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

