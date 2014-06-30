package com.frameworkset.platform.holiday.area.util.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.WorkTime;
import com.frameworkset.platform.holiday.area.util.service.UtilManager;

public class UtilManagerImpl implements UtilManager{
	private ConfigSQLExecutor executor;
	@Override
	public List<String> queryAreaIds() throws Exception {
		List<String> list = new ArrayList<String> ();
		try{
			list = 	executor.queryList(String.class, "queryAreaIds");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Map<String, String> queryOrgToArea() throws Exception {
		
	final	Map<String, String> datas = new HashMap<String, String>();
	try{
		executor.queryByNullRowHandler(new NullRowHandler() {

			@Override
			public void handleRow(Record origine) throws Exception {
				datas.put(origine.getString("orgId"), origine.getString("areaId"));
			}
		}, "queryOrgToArea");
	}catch(Exception e){
		e.printStackTrace();
	}
		
		return datas;
	}
	@Override
	public Set<String> querySubOrg(String str) throws Exception {
		
		final Set<String> set = new HashSet<String> ();
		try{
			executor.queryByNullRowHandler(new NullRowHandler() {

				@Override
				public void handleRow(Record origine) throws Exception {
					set.add(origine.getString("orgId"));
				}
			}, "querySubOrg",str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return set;
	}
	@Override
	public Map<String, String> getThisAndNextYearArrange(String areaId,String thisYear)
			throws Exception {
		
	final Map<String, String> datas = new HashMap<String, String>();
	
	String nextYear = String.valueOf(Integer.parseInt(thisYear)+1);
	try{
		executor.queryByNullRowHandler(new NullRowHandler() {

			@Override
			public void handleRow(Record origine) throws Exception {
				datas.put(origine.getString("dateOfYear"), origine.getString("type"));
			}
		}, "getThisAndNextYearArrange",areaId,thisYear,nextYear);
	}catch(Exception e){
		e.printStackTrace();
	}
		
		return datas;
	}
	@Override
	public List<WorkDate> getWorkDateIn(String areaId,
			String startTime) throws Exception {
		List<WorkDate> list = new ArrayList<WorkDate>();
		Map<String,String> map = new HashMap<String,String> ();
		map.put("areaId",areaId );
		map.put("startTime",startTime );
		try{
			list = executor.queryListBean(WorkDate.class, "getWorkDateIn", map);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<WorkTime> getWorkTimeByPid(String pid) throws Exception {
		List<WorkTime> list = new ArrayList<WorkTime>();
		try{
			list = executor.queryList(WorkTime.class, "getWorkTimeByPid", pid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<WorkTime> getWorkTimeByWorkDate(String areaId, String date)
			throws Exception {
		List<WorkTime> list = new ArrayList<WorkTime>();
		Map<String,Object> map = new HashMap<String,Object> ();
		map.put("areaId",areaId );
		map.put("date",date );
		try{
			list = executor.queryListBean(WorkTime.class, "getWorkTimeByWorkDate", map);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<OrgLeaf> getAllOrgLeaf() throws Exception {
		List<OrgLeaf> list = new ArrayList<OrgLeaf>();
		try  
        {              
			list = executor.queryList(OrgLeaf.class, "getAllOrgLeaf");
            
        }   
        catch(Exception e)   
        {   
        	e.printStackTrace();
        }   
		return list;
	}

	

}
