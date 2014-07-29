package com.frameworkset.platform.holiday.area.util.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.WorkTime;

public interface UtilManager {
	
	public List<String> queryAreaIds()throws Exception;
	
	public Map<String,String> queryOrgToArea()throws Exception;
	
	public Set<String> querySubOrg(String str)throws Exception;

	public Map<String,String> getThisAndNextYearArrange(String areaId,String thisYear)throws Exception;
	
	public List<WorkDate> getWorkDateIn(String  areaId,String startTime)throws Exception;
	
	public List<WorkTime> getWorkTimeByPid (String pid)throws Exception;
	
	public List<WorkTime> getWorkTimeByWorkDate(String areaId,String date)throws Exception;
	
	public List<OrgLeaf> getAllOrgLeaf()throws Exception;
	
	public String getOrgIdByuserAccount(String userAccount)throws Exception;
	
	public String getOrgIdByUserId(String userId)throws Exception;
	
	public String getOrgIdByuserAccountOrWorkNo(String userAccount) throws Exception;
	
	public String getDefaultAreaId() throws Exception;
}
