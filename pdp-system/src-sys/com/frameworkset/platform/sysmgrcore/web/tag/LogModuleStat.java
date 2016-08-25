package com.frameworkset.platform.sysmgrcore.web.tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.Escape;
import com.frameworkset.util.ListInfo;

public class LogModuleStat extends DataInfoImpl {

	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		//是否是历史日志模块统计查询
		String isHis = request.getParameter("isHis");
		String table = "td_sm_log";
		if(isHis != null && isHis.equals("n")){
			table = "td_sm_log_his";
		}
		//操作起止时间
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //日志所属机构查询
        String opOrgid = request.getParameter("opOrgid");
        // 1 为递归查询      0 不递归查询
        String isRecursion = request.getParameter("isRecursion");	//是否递归查询日志所属机构下的子机构
        //日志来源
        String logVisitorial = Escape.unescape(request.getParameter("logVisitorial"));
		StringBuffer sql = new StringBuffer()
			.append("select count(l.log_id) as count, l.oper_module as module from ")
			.append(table).append(" l where 1=1 ");
		if(opOrgid != null && !opOrgid.equals("")){
			if(isRecursion.equals("0")){
				sql.append(" and trim(l.OP_ORGID) = '").append(opOrgid).append("' ");
			}else{
				String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
				sql.append(" and trim(l.OP_ORGID) in (select t.org_id from TD_SM_ORGANIZATION t where t.org_tree_level like ")
	    			.append(" (select ").append(concat_).append(" from TD_SM_ORGANIZATION c where c.org_id ='").append(opOrgid).append("' ) or t.org_id ='").append(opOrgid).append("')");
	    			
			}
		}
		if(logVisitorial != null && !"".equals(logVisitorial)){
			sql.append(" and l.LOG_VISITORIAL like '%").append(logVisitorial).append("%' ");
		}
		if (startDate!=null && !startDate.equals("")) {
			String dateString = DBUtil.getDBDate(startDate+ " 00:00:00");
        	sql.append(" and l.log_operTime >=").append(dateString);
        }
        if (endDate!=null && !endDate.equals("")) {
        	String dateString = DBUtil.getDBDate(endDate+ " 23:59:59");
        	sql.append(" and l.log_operTime <=").append(dateString);
        }
		
        sql.append(" GROUP BY l.OPER_MODULE order by count desc");
		//sql.append("SELECT   COUNT (l.log_id) as count, l.oper_module as module FROM td_sm_log l GROUP BY l.OPER_MODULE");
		return getLogModuleStatListInfo(sql.toString(), offset, maxPagesize);
	}
	
	private ListInfo getLogModuleStatListInfo(String sql,long offset,int maxItem){
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			if(db.size() > 0){
				List list = new ArrayList();
				for(int i = 0; i < db.size(); i++){
					Map record = new HashMap();
					record.put("count", db.getString(i, "count"));
					record.put("module", db.getString(i, "module"));
					list.add(record);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(db.getTotalSize());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

}
