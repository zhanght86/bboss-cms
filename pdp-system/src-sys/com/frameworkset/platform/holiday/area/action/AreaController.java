package com.frameworkset.platform.holiday.area.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.holiday.area.bean.Area;
import com.frameworkset.platform.holiday.area.bean.Arrange;
import com.frameworkset.platform.holiday.area.bean.Day;
import com.frameworkset.platform.holiday.area.bean.Org;
import com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.bean.WorkDate;
import com.frameworkset.platform.holiday.area.bean.WorkTime;
import com.frameworkset.platform.holiday.area.service.AreaManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;

public class AreaController {
	private AreaManager areaManager;
//	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	private static Logger logger = LoggerFactory.getLogger(AreaController.class);

	
		
	public String index(){
		return "path:index";
	}
public String queryAreaList(
		@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
		@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
		@PagerParam(name = PagerParam.OFFSET) long offset,
		@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "20") int pagesize, ModelMap model) throws Exception {
			
	ListInfo datas = areaManager.queryAreaList((int) offset, pagesize);
	model.addAttribute("datas", datas);
	return "path:areaList";
}


@SuppressWarnings("unchecked")
public  @ResponseBody String updateArea(String areaId,String areaName,String areaDesc,String areaDefault) throws Exception {
	/*判断默认区域是否重复设置   */
	if("1".equals(areaDefault)){
		if(areaManager.checkDuplicateDefaultArea(areaId,2)){//true 表示重复
			return "error";
		}
	}
	try{
		areaManager.updateArea( areaId, areaName, areaDesc,areaDefault);
	 	 
		 return "success";
	 }catch(Exception e){
		 e.printStackTrace();
		 logger.error("更新区域信息报错",e);
		 return "error"; 
	 }
}

@SuppressWarnings("unchecked")
public  @ResponseBody List<OrgLeaf> getOrgTree() throws Exception {  
	
	
	List<OrgLeaf> list = areaManager.getAllOrgLeaf();
	
	
	return list; 
}

public  String queryOrgList(String areaId,ModelMap model) throws Exception {  
	
	List<Org> list = areaManager.queryOrgList(areaId);
	
    model.addAttribute("datas", list);	
	
    return "path:orgList"; 
}

@SuppressWarnings("unchecked")
public  @ResponseBody String addOrg(String orgId,String orgName,String areaId) throws Exception {  
	
	 try{
		 List<Org> list = areaManager.querySingleOrg(areaId,orgId);
		 if(null != list && list.size()>0){
			 return "机构重复!";
		 }
		 areaManager.addOrg(orgId, orgName, areaId);
		 	 
		 return "success";
	 }catch(Exception e){
		 logger.error("添加机构报错",e);
		 e.printStackTrace();
		 return "添加失败"; 
	 }
	
	
}

@SuppressWarnings("unchecked")
public  @ResponseBody String deleteOrg(String orgId,String areaId) throws Exception {  
	
	 try{
		 areaManager.deleteOrg(orgId, areaId);
		 	 
		 return "success";
	 }catch(Exception e){
		 logger.error("删除机构报错",e);
		 e.printStackTrace();
		 return "error"; 
	 }
	
	
}

@SuppressWarnings("unchecked")
public  @ResponseBody Area querySingleArea(String areaId) throws Exception {  
	
	 try{
		List<Area> list = areaManager.querySingleArea(areaId);
		 	 
		 return (null != list && list.size()>0)?list.get(0):null;
	 }catch(Exception e){
		 logger.error("查询单个区域报错",e);
		 e.printStackTrace();
		 return null; 
	 }
	
	
}

@SuppressWarnings("unchecked")
public  @ResponseBody String addArea(String areaName , String areaDesc,String areaDefault) throws Exception {  
	String areaId = UUID.randomUUID().toString();
	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	String createTime = df.format(new Date());
	String creator = AccessControl.getAccessControl().getUserAccount();
	/*判断默认区域是否重复设置   */
	if("1".equals(areaDefault)){
		if(areaManager.checkDuplicateDefaultArea(areaId,1)){//true 表示重复
			return "error";
		}
	}
	 try{
		 areaManager.addArea(areaId, areaName , areaDesc, creator , createTime,areaDefault);
		 	 
		 return "success";
	 }catch(Exception e){
		 logger.error("添加报错",e);
		 return "error"; 
	 }
	
	
}
/**
*初始化全年的日历结构
*@param  year 年份
*@param  areaId 区域Id
*
*/

@SuppressWarnings("unchecked")
public  @ResponseBody(datatype = "json")List<Day>  initArrangeTable(String year) throws Exception  {
    /* 获取全年每月第一天对应是该月第几周的星期几     */	
	List<Map<String,String>> initId = getInitId(year);
   
	/*根据每月第一天对应是该月第几周的星期几 ,推算出每月剩余天数所在的星期	      */
	List<Day> list =  getYearDays( year, initId);
    
	
	return list; 
	
}
/**
*初始化全年的工作/修改安排
*@param  year 年份
*@param  areaId 区域Id
*/
@SuppressWarnings("unchecked")
public  @ResponseBody List<Arrange> initArrangeDays(String year,String areaId) throws Exception {
	List<Arrange> list =  areaManager.getArrangeByAreaId( year, areaId);
	return list; 
	
}	

/**
 * 更新工作日期安排
 * @param year 年份
 * @param week  example  ：731 7月份第3个星期的第1天     1031  10月份第3个星期的第1天
 * @param type  example  ：1 为工作日  2 为休息日
 * @param date  example  ：所在月份的第几天  从1号 到 31号
*/
@SuppressWarnings("unchecked")
public  @ResponseBody String updateSingleArrange(String year,String areaId , String week , String type,String date) {
	String dateOfYear = "";
	/**
	 * 根据year  week  和 date  拼出当天的日期  返回结果为 例如2014-6-26
	*/
	if(week.length() == 4){
		dateOfYear = year + "-" + week.substring(0, 2) + "-" + makeUpZero(date);
	}else{
		dateOfYear = year + "-" + makeUpZero(week.substring(0, 1)) + "-" + makeUpZero(date);
	}
	TransactionManager tm = new TransactionManager();
	try{
		tm.begin();
		
		List<Arrange> list =  areaManager.getSingleArrange( year, areaId,week);
		if(null != list && list.size()>0){
			areaManager.updateSingleArrange( year,dateOfYear, areaId,week,type);
		}else{
			areaManager.insertSingleArrange( year, areaId,week,type,dateOfYear);
		}
		tm.commit();
		
		return "success";
	}catch(Exception e){
		
		logger.error("更新日期类型报错",e);
		
		return  "false";
	}finally{
		tm.release();
	}
	
	
}	

        private String makeUpZero(String str){
        	if(1==str.length()){
        		return "0"+str;
        	}
        	return str;
        }
        
        /**
         * 根据每月第一天对应是该月第几周的星期几 ,推算出每月剩余天数所在的星期	      
         * @param  initId初始化参数
         * @param 年份
          */
		private List<Day> getYearDays(String year,List<Map<String,String>> initId){
			List<Day> list = new ArrayList<Day> ();
			String yearType = getLeapYear(year);//判断闰年
			int [] days = getDaysOfYear(yearType);//获取每月天数
			for(int i = 1;i<=12;i++){
				int m = Integer.parseInt(initId.get(i-1).get("m"));
				int d = Integer.parseInt(initId.get(i-1).get("d"));
				int w = Integer.parseInt(initId.get(i-1).get("w"));
				for(int j = 1 ; j<=days[i-1];j++){
					Day day = new Day();
					int cur = j;
					day.setId(getCurId( m ,  d , w ,  cur));
					day.setType("");
					day.setValue(year+"-"+i+"-"+j);
					day.setDayOfMonth(String.valueOf(j));
					list.add(day);
				}
			}
			return list;
		}
		
		/**
		 * 获取月份中的天数对应页面上的ID值  
		 * @param m 初始的月份
		 * @param d  初始天数
		 * @param w  初始星期
		 * @param cur 当前的天数
		  */
		private String getCurId(int m , int d ,int w , int cur){
			String id = "";
			int re = (cur-1)/7;
			int mod = (cur-1)%7;
			if(d+mod<=7){
				id = String.valueOf(m)+String.valueOf(w+re)+String.valueOf(d+mod);
			}else{
				id = String.valueOf(m)+String.valueOf(w+re+1)+String.valueOf(d+mod-7);
			}
			return id;
		}
		/**
		 * 获取全年每月第一天对应是该月第几周的星期几     
		  */	
		private List<Map<String,String>> getInitId(String year){
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			Calendar ins = Calendar.getInstance();
			for(int i = 1; i<=12;i++){
				ins.set(Integer.parseInt(year), i-1, 1);
				int dayOfToday = ins.get(Calendar.DAY_OF_WEEK);
				Map<String,String> map = new HashMap<String,String>();
				map.put("m", String.valueOf(i));
				map.put("w", "1");
				map.put("d", String.valueOf(dayOfToday));
				list.add(map);
			}
			return list;
		}
		/**
		 * 根据是否闰年获取每月的天数     
		  */
		private int [] getDaysOfYear(String yearType){
			if("1".equals(yearType)){
				int [] days = {31,29,31,30,31,30,31,31,30,31,30,31};
				return days;
			}else{
				int [] days = {31,28,31,30,31,30,31,31,30,31,30,31};
				return days;
			}
		}
		
		/**
		 * 判断是否闰年    
		  */
		private String getLeapYear(String year){
			java.util.GregorianCalendar c = new java.util.GregorianCalendar();
			if (c.isLeapYear(Integer.parseInt(year))){
				return "1";
			}else{
				return "0";
			}
		}
		public static void main(String [] args0){
			Calendar ins = Calendar.getInstance();
			ins.set(2014, 1, 20);
			int value = ins.get(Calendar.WEEK_OF_MONTH);
			System.out.print(6/7);
			
			
		}
		
		public  String queryWorkDateList(String areaId,ModelMap model) throws Exception {  
			
			List<WorkDate> list = areaManager.queryWorkDateList(areaId);
			
		    model.addAttribute("datas", list);	
			
		    return "path:workDateList"; 
		}
		
		@SuppressWarnings("unchecked")
		public  @ResponseBody String addWorkDate(String areaId,String name,String desc,String startDate,String endDate) throws Exception {  
			String id = UUID.randomUUID().toString();
			 try{
				 areaManager.addWorkDate(id,  areaId, name, desc, startDate, endDate);
				 	 
				 return "success";
			 }catch(Exception e){
				 logger.error("新增工作日期报错",e);
				 e.printStackTrace();
				 return "error"; 
			 }
			
			
		}
		
		@SuppressWarnings("unchecked")
		public  @ResponseBody String updateWorkDate(String id,String name,String desc,String startDate,String endDate) throws Exception {  
			 try{
				 areaManager.updateWorkDate(id, name, desc, startDate, endDate);
				 	 
				 return "success";
			 }catch(Exception e){
				 logger.error("更新工作日期报错",e);
				 e.printStackTrace();
				 return "error"; 
			 }
			
			
		}
		
public  String queryWorkTimeList(String pid,ModelMap model) throws Exception {  
			
			List<WorkTime> list = areaManager.queryWorkTimeList(pid);
			
		    model.addAttribute("datas", list);	
			
		    return "path:workTimeList"; 
		}

@SuppressWarnings("unchecked")
public  @ResponseBody Map<String,String> saveOrUpdateWorkTime(String pid,String id,String name,String startTime,String endTime) throws Exception {
	Map<String,String> map = new HashMap<String, String>();
	
	 try{
		 if(null == id || "".equals(id)){
			 id = UUID.randomUUID().toString();
			 areaManager.saveWorkTime(pid,id, name, startTime, endTime);
		 }else{
			 areaManager.updateWorkTime(pid,id, name, startTime, endTime);
		 }
		 map.put("id", id);
		 map.put("result", "success");
		 
	 }catch(Exception e){
		 logger.error("更新或保存工作时间设置报错",e);
		 e.printStackTrace();
		 map.put("id", id);
		 map.put("result", "error");
	 }
	
	return map;
}

@SuppressWarnings("unchecked")
public  @ResponseBody String deleteWorkTime(String id) throws Exception {
	
	 try{
		 areaManager.deleteWorkTime(id);
		 return "success";
	 }catch(Exception e){
		 logger.error("删除工作时间设置报错",e);
		 e.printStackTrace();
		 return "error";
	 }
	
}

public String toEditWorkDate(String id,ModelMap model) throws Exception {
	
		 WorkDate workDate = areaManager.toEditWorkDate(id);
		 
		 model.addAttribute("workDate",workDate);
		 
		 return "path:editWorkDate"; 
	
}
}
