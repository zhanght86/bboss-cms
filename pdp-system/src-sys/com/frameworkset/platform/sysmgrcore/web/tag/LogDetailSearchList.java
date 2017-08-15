package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.sysmgrcore.entity.LogDetail;
import com.frameworkset.util.ListInfo;

public class LogDetailSearchList  extends DataInfoImpl implements Serializable {

	private Logger log = LoggerFactory.getLogger(LogDetailSearchList.class);
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

	protected ListInfo getDataList(String arg0, boolean desc, long offset,
			int maxPagesize) {
		//日志ID
		String logId = request.getParameter("logId");
		//日志操作的表
		String operTable = request.getParameter("operTable");
		//操作记录内容
		String detailContent = request.getParameter("detailContent");
		
		StringBuffer sql = new StringBuffer()
			.append("select * from td_sm_logdetail where LOG_ID='").append(logId).append("' ");
		if(operTable != null && !"".equals(operTable)){
			sql.append(" and upper(OPER_TABLE) like '%").append(operTable.toUpperCase()).append("%' ");
		}
		if(detailContent != null && !"".equals(detailContent)){
			sql.append(" and DETAIL_CONTENT like '%").append(detailContent).append("%' ");
		}
		sql.append(" order by DETAIL_ID");
		
		
		return getLogDetailListInfo(sql.toString(),offset,maxPagesize);
	}
	
	private ListInfo getLogDetailListInfo(String sql,long offset,int maxItem){
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql,offset,maxItem);
			List list = new ArrayList();
			LogDetail logDetail = null;
			for(int i = 0; i < db.size(); i++){
				logDetail = new LogDetail();
				logDetail.setDetailContent(db.getString(i, "DETAIL_CONTENT"));
				logDetail.setOperTable(db.getString(i, "OPER_TABLE"));
				logDetail.setOperType(db.getInt(i, "OP_TYPE"));
				logDetail.setOperKeyID(db.getString(i, "OP_KEY_ID"));
				list.add(logDetail);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

}
