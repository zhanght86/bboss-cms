package com.frameworkset.platform.sysmgrcore.web.tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class TestNoTransaction extends DataInfoImpl {


	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}


	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		String queryName = request.getParameter("queryName");
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		StringBuffer sql = new StringBuffer()
			.append(" select * from td_sm_test where 1=1 ");
		if(queryName != null && !"".equals(queryName)){
			sql.append(" and test_name like '%").append(queryName).append("%'");
		}
		sql.append(" order by test_name");
		try {
			db.executeSelect(sql.toString(), arg2,arg3);
			List list = new ArrayList();
			for(int i = 0; i < db.size(); i++){
				Map test = new HashMap();
				test.put("ID", db.getString(i, "test_ID"));
				test.put("testName", db.getString(i, "test_name"));
				list.add(test);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

}
