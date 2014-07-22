package com.frameworkset.platform.holiday.area.util.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



import com.frameworkset.platform.holiday.area.util.service.UtilManager;
import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.WorkTime;
public class UtilAction {

	private UtilManager utilManager;
	
	 /** 
     *根据工号返回部门编码   
     */
	public String getOrgIdByUserId(String userId)throws Exception{
		return utilManager.getOrgIdByuserAccount(userId);
	}
	
	 /** 
     *根据工号返回部门编码   
     */
	public String getOrgIdByuserAccountOrWorkNo(String userAccount) throws Exception{
		return utilManager.getOrgIdByuserAccountOrWorkNo(userAccount);
	}
	
	
    /** 
     *根据域账号返回部门编码   
     */ 	
	public String getOrgIdByuserAccount(String userAccount)throws Exception{
		
		return utilManager.getOrgIdByuserAccount(userAccount);
	}
	public String getAreaByOrgId(String orgId){
		try {
			Map<String,String> orgToArea = utilManager.queryOrgToArea();
			if(null != orgToArea.get(orgId)&& !"".equals(orgId)){
				return  orgToArea.get(orgId);
				
			}
			Set<String> set = orgToArea.keySet();
			for (String str : set) {
				
				Set<String> subOrg = utilManager.querySubOrg(str);
				
					if(null != subOrg && subOrg.size()>0){
						if(subOrg.contains(orgId)){
							return  orgToArea.get(str);
							
						}
					}
				
				}   

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public Map<String,String> getThisAndNextYearArrange(String areaId,String thisYear){
		Map<String,String> map = new HashMap<String,String>();
		try{
			map = utilManager.getThisAndNextYearArrange( areaId,thisYear);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	public List<WorkDate> getWorkDateIn(String areaId,String startTime){
		List<WorkDate> list = new ArrayList<WorkDate>();
		try{
			list = utilManager.getWorkDateIn( areaId,startTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<WorkTime> getWorkTimeByWorkDate(String areaId,String date){
		List<WorkTime> list = new ArrayList<WorkTime>();
		try{
			list = utilManager.getWorkTimeByWorkDate( areaId,date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public List<OrgLeaf> getOrgTree(){  
		try{
			List<OrgLeaf> list = utilManager.getAllOrgLeaf();
			
			return list; 
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
}
}
