package com.frameworkset.platform.holiday.area.service;

import java.util.List;

import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.Area;
import com.frameworkset.platform.holiday.area.bean.Arrange;
import com.frameworkset.platform.holiday.area.bean.Org;
import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkTime;

import com.frameworkset.util.ListInfo;

public interface AreaManager {
	public ListInfo queryAreaList(int offset,int pagesize)throws Exception;
	
	public void updateArea(String areaId,String areaName,String areaDesc,String areaDefault)throws Exception;
	
	public List<OrgLeaf> getAllOrgLeaf()throws Exception;
	
	public List<Org> queryOrgList(String areaId)throws Exception;
	
	public void addOrg(String orgId,String  orgName,String  areaId)throws Exception;
	
	public void deleteOrg(String orgId,String areaId)throws Exception;
	
	public List<Area> querySingleArea(String areaId)throws Exception;
	
	public void addArea(String areaId,String areaName ,String areaDesc,String creator ,String createTime,String areaDefault)throws Exception;
	
	public List<Org> querySingleOrg(String areaId,String orgId)throws Exception;

	public List<Arrange> getArrangeByAreaId(String year,String areaId)throws Exception;
	
	public List<Arrange> getSingleArrange(String year,String areaId,String week)throws Exception;
	
	public void updateSingleArrange(String year,String dateOfYear,String areaId,String week,String type)throws Exception;
	
	public void insertSingleArrange(String year,String areaId,String week,String type,String dateOfYear)throws Exception;
	
	public List<WorkDate> queryWorkDateList(String areaId)throws Exception;
	
	public void addWorkDate(String id,String  areaId,String name,String desc,String startDate,String endDate)throws Exception;
	
	
	public void updateWorkDate(String id,String  name, String desc,String  startDate,String  endDate)throws Exception;
	
	public List<WorkTime> queryWorkTimeList(String pid)throws Exception;
	
	public void saveWorkTime(String pid,String id,String name,String startTime,String endTime)throws Exception;
	
	public void updateWorkTime(String pid,String id,String name,String startTime,String endTime)throws Exception;
	
	public void deleteWorkTime(String id)throws Exception;
	
	public boolean checkDuplicateDefaultArea(String areaId,int num)throws Exception;
	
	public WorkDate toEditWorkDate(String id)throws Exception;
}
