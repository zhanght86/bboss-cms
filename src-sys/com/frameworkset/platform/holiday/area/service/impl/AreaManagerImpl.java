package com.frameworkset.platform.holiday.area.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.platform.holiday.area.bean.Area;
import com.frameworkset.platform.holiday.area.bean.Arrange;
import com.frameworkset.platform.holiday.area.bean.Org;
import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.WorkTime;
import com.frameworkset.platform.holiday.area.service.AreaManager;
import com.frameworkset.util.ListInfo;

public class AreaManagerImpl implements AreaManager{
	private ConfigSQLExecutor executor;

	@Override
	public ListInfo queryAreaList( int offset, int pagesize) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		ListInfo datas = executor.queryListInfoBean(Area.class, "queryAreaList", offset, pagesize, paramMap);
		return datas;
	}
	@Override
	public void updateArea(String areaId,String areaName,String areaDesc)throws Exception{
		
		             
			executor.update("updateArea", areaName,areaDesc,areaId);
			
			
       
		
	}
	@Override
	public List<OrgLeaf> getAllOrgLeaf() throws Exception {
		List<OrgLeaf> list = new ArrayList<OrgLeaf>();
		            
			list = executor.queryList(OrgLeaf.class, "getAllOrgLeaf");
            
       
		return list;
	}
	
	@Override
	public List<Org> queryOrgList(String areaId) throws Exception {
		List<Org> list = new ArrayList<Org>();
		            
			list = executor.queryList(Org.class, "queryOrgList",areaId);
            
        
		return list;
	}
	
	@Override
	public void addOrg(String orgId, String orgName, String areaId)
			throws Exception {
		             
			 executor.insert("addOrg",  orgId,  orgName,  areaId);
            
       
	}
	
	@Override
	public void deleteOrg(String orgId, String areaId) throws Exception {
		              
			 executor.delete("deleteOrg",  orgId,  areaId);
       
		
	}
	
	@Override
	public List<Area> querySingleArea(String areaId) throws Exception {
		List<Area> list = new ArrayList<Area>();
		             
			list = executor.queryList(Area.class, "querySingleArea",areaId);
            
        
		return list;
	}
	@Override
	public void addArea(String areaId,String areaName ,String areaDesc,String creator ,String createTime) throws Exception {
		             
			 executor.insert("addArea",  areaId, areaName , areaDesc, creator , createTime);
            
      
	}
	@Override
	public List<Org> querySingleOrg(String areaId, String orgId)
			throws Exception {
		List<Org> list = new ArrayList<Org>();
		              
			list = executor.queryList(Org.class, "querySingleOrg",areaId,orgId);
        
		return list;
	}
	@Override
	public List<Arrange> getArrangeByAreaId(String year, String areaId)
			throws Exception {
		List<Arrange> list = new ArrayList<Arrange>();
		             
			list = executor.queryList(Arrange.class, "getArrangeByAreaId",areaId,year);
        
		return list;
	}
	@Override
	public List<Arrange> getSingleArrange(String year, String areaId,
			String week) throws Exception {
		List<Arrange> list = new ArrayList<Arrange>();
		            
			list = executor.queryList(Arrange.class, "getSingleArrange",areaId,year,week);
            
        
		return list;
	}
	@Override
	public void updateSingleArrange(String year,String dateOfYear, String areaId, String week,
			String type) throws Exception {
		             
			executor.update("updateSingleArrange",  type,dateOfYear, year,  areaId,  week);
            
       
	}
	@Override
	public void insertSingleArrange(String year, String areaId, String week,
			String type, String dateOfYear) throws Exception {
		             
			executor.insert("insertSingleArrange",  type, year,  areaId,  week,dateOfYear);
        
	}
	@Override
	public List<WorkDate> queryWorkDateList(String areaId) throws Exception {
		List<WorkDate> list = new ArrayList<WorkDate>();
		              
			list = executor.queryList(WorkDate.class, "queryWorkDateList",areaId);
            
       
		return list;
	}
	@Override
	public void addWorkDate(String id, String areaId, String name, String desc,
			String startDate, String endDate) throws Exception {
		              
			executor.insert("addWorkDate",   id,  areaId,  name,  desc,
					 startDate,  endDate);
        
	}
	@Override
	public void updateWorkDate(String id, String name, String desc,
			String startDate, String endDate) throws Exception {
		              
			executor.update("updateWorkDate", name,  desc,startDate, endDate,id);
            
       
	}
	@Override
	public List<WorkTime> queryWorkTimeList(String pid) throws Exception {
		List<WorkTime> list = new ArrayList<WorkTime>();
		              
			list = executor.queryList(WorkTime.class, "queryWorkTimeList",pid);
       
		return list;
	}
	@Override
	public void saveWorkTime(String pid,String id, String name, String startTime,
			String endTime) throws Exception {
		             
			executor.insert("saveWorkTime",pid,id, name,  startTime,endTime);
      
	}
	@Override
	public void updateWorkTime(String pid,String id, String name, String startTime,
			String endTime) throws Exception {
		              
			executor.update("updateWorkTime", name,  startTime,endTime,id);
            
       
	}
	@Override
	public void deleteWorkTime(String id) throws Exception {
		             
			executor.delete("deleteWorkTime",id);
        
	}
	

}
