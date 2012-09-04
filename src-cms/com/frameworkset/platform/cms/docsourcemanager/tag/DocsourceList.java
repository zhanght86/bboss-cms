package com.frameworkset.platform.cms.docsourcemanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.docsourcemanager.Docsource;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class DocsourceList extends DataInfoImpl implements java.io.Serializable
{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
		String srcName = request.getParameter("srcName");
		String provider = request.getParameter("provider");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String sqlWhere = " where 1=1 ";
		

		if (null != srcName && !"".equals(srcName))
			sqlWhere += " and SRCNAME like '%" + srcName + "%' ";
		if (null != provider && !"".equals(provider))
			sqlWhere += " and user_name like '%" + provider + "%' ";
		if (null != fromDate && !"".equals(fromDate))
			sqlWhere += " and to_char(CRTIME,'YYYY-MM-DD') >= '" + fromDate + "' ";
		if (null != toDate && !"".equals(toDate))
			sqlWhere += " and to_char(CRTIME,'YYYY-MM-DD') <= '" + toDate + "' ";
		
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			
			String sql ="select t.*,to_char(crtime,'yyyy-mm-dd') as crdate,user_name from TD_CMS_DOCSOURCE t " +
					"left join td_sm_user b on t.cruser = b.user_id " +
					sqlWhere + " order by DOCSOURCE_ID";

			dbUtil.executeSelect(sql,(int)offset,maxPagesize);
			List list = new ArrayList();
			Docsource docsource;
			if(dbUtil.size()>0){
				for (int i = 0; i < dbUtil.size(); i++) {
					docsource = new Docsource();
					if (!"null".equals(dbUtil.getString(i,"srcname")))
						docsource.setSRCNAME(dbUtil.getString(i,"srcname"));
					if (!"null".equals(dbUtil.getString(i,"srcdesc")))
						docsource.setSRCDESC(dbUtil.getString(i,"srcdesc"));
					if (!"null".equals(dbUtil.getString(i,"srclink")))
						docsource.setSRCLINK(dbUtil.getString(i,"srclink"));
					docsource.setCRUSER(dbUtil.getInt(i,"CRUSER"));
					docsource.setUsername(dbUtil.getString(i,"user_name"));
					if (!"null".equals(dbUtil.getString(i,"crdate")))
						docsource.setCRTIME(dbUtil.getString(i,"crdate"));
					docsource.setDOCSOURCE_ID(dbUtil.getInt(i,"docsource_id"));
					list.add(docsource);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.getTotalSize());
				return listInfo;
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