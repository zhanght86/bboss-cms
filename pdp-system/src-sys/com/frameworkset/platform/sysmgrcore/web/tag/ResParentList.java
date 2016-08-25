package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class ResParentList extends DataInfoImpl implements Serializable{
	private Logger logger = Logger.getLogger(ResSearchList.class.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		String restypeId = request.getParameter("restypeId");
		String resId = request.getParameter("resId");
		String parent_resId = request.getParameter("parent_resId");
	
		//System.out.println(".........."+restypeId);
		//System.out.println(".........."+resId);
		try {
//			ResManager resManager = SecurityDatabase.getResourceManager();
//			
//
//			List list = null;
//			PageConfig pageConfig = resManager.getPageConfig();
//			pageConfig.setPageSize(maxPagesize);
//			pageConfig.setStartIndex((int) offset);

			listInfo = getResList("select * from td_sm_Res r where r.parent_Id='" + parent_resId + "'",offset,maxPagesize);
//			String restypeID = null;
//			ResourceManager rm = new ResourceManager();			
//			for(int i = 0; i < list.size();i ++)
//			{
//			    Res res = (Res)list.get(i);
//			    restypeID = res.getRestypeId();
//			    
//			    res.setRestypeName(rm.getResourceInfoByType(restypeID).getName());
//			    
//			    
//			}
//			// 根据资源类型得资源列表
//			
//			listInfo.setTotalSize(pageConfig.getTotalSize());
//			listInfo.setDatas(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	 *      boolean)
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ListInfo getResList(String sql,long offset,
			int maxPagesize) {
		ListInfo listinfo = new ListInfo();
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		try {
			db.executeSelect(sql, offset, maxPagesize);
			list = new ResManagerImpl().getResList(db);
			listinfo.setDatas(list);
			listinfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listinfo;
	}
}
