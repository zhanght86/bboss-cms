package com.frameworkset.platform.holiday.area.service;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.Area;
import com.frameworkset.platform.holiday.area.bean.Arrange;
import com.frameworkset.platform.holiday.area.bean.Org;
import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkTime;

import com.frameworkset.util.ListInfo;

public interface AreaManager {
	public ListInfo queryAreaList(int offset,int pagesize)throws SQLException;
	
	public void updateArea(String areaId,String areaName,String areaDesc)throws SQLException;
	
	public List<OrgLeaf> getAllOrgLeaf()throws SQLException;
	
	public List<Org> queryOrgList(String areaId)throws SQLException;
	
	public void addOrg(String orgId,String  orgName,String  areaId)throws SQLException;
	
	public void deleteOrg(String orgId,String areaId)throws SQLException;
	
	public List<Area> querySingleArea(String areaId)throws SQLException;
	
	public void addArea(String areaId,String areaName ,String areaDesc,String creator ,String createTime)throws SQLException;
	
	public List<Org> querySingleOrg(String areaId,String orgId)throws SQLException;

	public List<Arrange> getArrangeByAreaId(String year,String areaId)throws SQLException;
	
	public List<Arrange> getSingleArrange(String year,String areaId,String week)throws SQLException;
	
	public void updateSingleArrange(String year,String dateOfYear,String areaId,String week,String type)throws SQLException;
	
	public void insertSingleArrange(String year,String areaId,String week,String type,String dateOfYear)throws SQLException;
	
	public List<WorkDate> queryWorkDateList(String areaId)throws SQLException;
	
	public void addWorkDate(String id,String  areaId,String name,String desc,String startDate,String endDate)throws SQLException;
	
	
	public void updateWorkDate(String id,String  name, String desc,String  startDate,String  endDate)throws SQLException;
	
	public List<WorkTime> queryWorkTimeList(String pid)throws SQLException;
	
	public void saveWorkTime(String pid,String id,String name,String startTime,String endTime)throws SQLException;
	
	public void updateWorkTime(String pid,String id,String name,String startTime,String endTime)throws SQLException;
	
	public void deleteWorkTime(String id)throws SQLException;
}
