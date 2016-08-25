package com.frameworkset.platform.holiday.area.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.holiday.area.bean.FlowWarning;
import com.frameworkset.platform.holiday.area.bean.WorkTime;
import com.frameworkset.platform.holiday.area.util.action.UtilAction;

public class WorkTimeUtil {

	private static String rexp_userId = "[0-9]+";
	private static String rexp_orgId = "[0-9]+";
	private static String rexp_startTime = "\\d{4}\\-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	private static String rexp_interval = "[0-9]+\\.{0,1}[0-9]{0,1}[DHm]{1}";
	private static String error_orgId= "1";
	private static String error_startTime= "2";
	private static String error_interval= "3";
	private static String right_para = "4";
	private static String no_workDate_set = "5";
	private static String no_workTime_set = "6";
	private static String no_erea_set = "7";
	private static String over_bound = "8";
	private static String error_areaId = "9";
	private static String error_endTime= "10";
	private static String error_timeSet = "11";//开始时间大于结束时间
	private static String not_end_calculate = "12";
	private static String end_calculate = "13";	
	private static float interval_bound_day = 79536;
	private static float interval_bound_hour = 596523;
	private static float interval_bound_min = 35791380;
	private static int the_whole_day_millisecond = 86400;
	
	private UtilAction util;
	
	
	public static void main(String [] args0) throws ParseException{
		SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s1 = "20111D";
		if(s1.matches(rexp_interval)){
			//System.out.print("ok");
		}else{
			//System.out.print("not--ok");
		}
		
		Date date = df_dateAndTime.parse("2014-07-02 11:08:00");
		System.out.print(date.getTime());
	}
	
	
	/**
	*计算工作完成时间和预警时间
	*
	*/
	@SuppressWarnings("static-access")
	public FlowWarning getCompleteAndWarnTime( String userAccount,Date createTime,int dicipline,long interval, int warnPercent)throws Exception{
		if(0 == interval){
			return null;
		}
		
		SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());   

		FlowWarning bean = new FlowWarning();
		bean.setCreateTime(createTime);
		bean.setDicipline(dicipline);
		bean.setInterval(interval);
		bean.setUserAccount(userAccount);
		bean.setWarnPercent(warnPercent);
		
		long intervalNotice = 0;
		
		String completeTime = null;
		Date completeTime0 = null;
		if(dicipline==0){
			completeTime0 = getNextTimeNoRest(createTime,interval);
			bean.setCompleteTime(new Timestamp(completeTime0.getTime()));
			Calendar instance = Calendar.getInstance();
			if(0 != warnPercent){
				intervalNotice = (interval*warnPercent)/100;
				instance.setTimeInMillis(completeTime0.getTime()-intervalNotice);
				bean.setWarnTime(new Timestamp(instance.getTimeInMillis()));
			}
		
		}else if(dicipline==1){
			String orgId = util.getOrgIdByuserAccountOrWorkNo(userAccount);
			String createTimeString = df_dateAndTime.format(createTime);
			completeTime = getNextTimeNoRestDay(orgId, createTimeString, interval);
			/*返回的完成时间不符合日期时间格式,则写bean的errorCode属性       */
//			if(!completeTime.matches(rexp_startTime)){yyyy-MM-dd HH:mm:ss
			if(completeTime.length() != 19){
				bean.setErrorCode(completeTime);
				
			}else{
				Calendar instance = Calendar.getInstance();
				if(0 != warnPercent){
					intervalNotice = (interval*warnPercent)/100;
					Date date = df_dateAndTime.parse(completeTime);
					instance.setTimeInMillis(date.getTime()-intervalNotice);
					bean.setCompleteTime(ts.valueOf(completeTime));
				}
				bean.setWarnTime(ts.valueOf(df_dateAndTime.format(instance.getTime())));
			}
		}else if(dicipline==2){
			String orgId = util.getOrgIdByuserAccountOrWorkNo(userAccount);
			String createTimeString = df_dateAndTime.format(createTime);
			completeTime = getNextTimeNoRestTime(orgId, createTimeString, interval);
			/*返回的完成时间不符合日期时间格式,则写bean的errorCode属性       */
			if(completeTime.length() != 19){
				bean.setErrorCode(completeTime);
				
			}else{
				Calendar instance = Calendar.getInstance();
				if(0 != warnPercent){
					intervalNotice = (interval*warnPercent)/100;
					Date date = df_dateAndTime.parse(completeTime);
					instance.setTimeInMillis(date.getTime()-intervalNotice);
					bean.setCompleteTime(ts.valueOf(completeTime));
				}
				bean.setWarnTime(ts.valueOf(df_dateAndTime.format(instance.getTime())));
			}
		}
		
		
		return bean;
	}
	/**
	*计算工作完成时间，包含休息日和法定节假日
	 * @throws ParseException 
	*/
	public Date getNextTimeNoRest(Date startTime,long millisecond) throws ParseException{
		SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(startTime.getTime()+millisecond);
		return instance.getTime();
	}
	
	/**
	*计算工作完成时间，包含休息日和法定节假日
	 * @throws ParseException 
	*/
	public String getNextTimeNoRest(String startTime,long millisecond) throws ParseException{
		SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar instance = Calendar.getInstance();
		Date date = df_dateAndTime.parse(startTime);
		instance.setTimeInMillis(date.getTime()+millisecond);
		return df_dateAndTime.format(instance.getTime());
	}
	
	/**
	*计算工作完成时间接口，不包含休息日和法定节假日，包含工作日的休息时间
	 * @throws Exception 
	 * @throws ParseException 
	*/
	public String getNextTimeNoRestDay(String orgId,String startTime,long millisecond) throws Exception{
		
        String areaId = util.getAreaByOrgId(orgId);
      //找默认的产业园
      		if(null == areaId||"".equals(areaId)){
      			areaId = util.getDefaultAreaId();
      		}
		
		//如果没有发现部门挂靠的产业园则启用默认配置
		if(null == areaId||"".equals(areaId)){
			return getNextTimeNoRest(startTime, millisecond);
		}
		
		return getNextTimeNoRestDay( areaId, startTime, millisecondToSecond(millisecond));
	}
	
	
	
	/**
	 * 封装getNextTime,校验参数,并把单位为 millisecond(毫秒)的时间间隔转换为 (int)秒
	 * @throws Exception 
	*/
	public String getNextTimeNoRestTime(String orgId,String startTime,long millisecond) throws Exception{
		
		String areaId = util.getAreaByOrgId(orgId);
		
		//如果没有发现部门挂靠的产业园则启用默认配置
		if(null == areaId||"".equals(areaId)){
			return getNextTimeNoRest(startTime, millisecond);
		}
		
		return getNextTimeNoRestTime( areaId, startTime, millisecondToSecond(millisecond));
	}
	
	/**
	 * 封装getNextTime,校验参数,并把单位为 D(天) , H(小时) , m(分钟)的时间间隔转换为 (int)秒
	*/
	@SuppressWarnings("unchecked")
	public @ResponseBody String getNextTimeNoRestTime(String orgId,String startTime,String interval){
		String reCheck = checkParameters( orgId, startTime, interval);
		if(right_para != reCheck){
			return reCheck;
		}
		
		return getNextTimeNoRestTime( orgId, startTime, changeIntervalToSecond(interval));
	}
	
	/**获取工作完成时间步骤(剔除休息日，法定节假日，工作日中的休息时间)
	 * 接口主要逻辑:从开始时间起算，得出一个特定的时间点消耗掉所有的工作时长
	 * @param  orgId      部门编码
	 * @param  startTime  开始时间
	 * @param  interval  时间间隔，单位秒
	 * 
	 * 计算步骤：
	 *           1.根据用户输入的部门编码获取该部门所在产业园的工作日期设置
	 *           while(工作时长){
		 *           2.获取开始时间的所在日期的类型，若为休息日，则跳到下一天，若为工作日则计算消耗时长
		 *           3.循环上一步，直到某天没有工作类型设置，或者工作时长消耗为 '0',则程序退出
	 *           }
	 */
	private String getNextTimeNoRestDay(String areaId,String startTime, int int_interval_second)throws Exception{

		
		/*String areaId = util.getAreaByOrgId(orgId);
		if(null == areaId||"".equals(areaId)){
			return no_erea_set;
		}*/
		String thisYear = startTime.substring(0, 4);
		
		Map<String,String> getThisAndNextYearArrange = util.getThisAndNextYearArrange(areaId, thisYear);
		
		String thisDate = getDate(startTime);//循环变量，日期
		String thisTime = startTime.split(" ")[1];//循环变量，时间
		
		
		
		while(0 != int_interval_second){//程序在工作剩余秒数为"0"时退出
			Map<String,String> map = new HashMap<String,String>();
			String type = getThisAndNextYearArrange.get(thisDate);
			if(null == type||"".equals(type)){//没有设置类型，返回错误代码
				return no_workDate_set;
			}else if("1".equals(type)){//'1'表示工作日，计算工作时间消耗 
				map = getSurplusSecondNoRestDay( thisDate,thisTime,int_interval_second);
				if("error" == map.get("re")){
					return no_workTime_set;
				}else{
					int_interval_second = Integer.parseInt(map.get("int_interval_second"));
					thisDate = map.get("date");
					thisTime = map.get("time");
				}
				
            }else{//休息日，跳到下一天
            	thisDate = getNextDate(thisDate);
            	thisTime = "00:00:00";
            }
		}
		
		
		
		return thisDate+" "+thisTime;
	
	}
	
	private Map<String,String> getSurplusSecondNoRestDay(String thisDate,String thisTime,int int_interval_second){
		Map<String,String> map = new HashMap<String,String>();
		if(int_interval_second <= the_whole_day_millisecond ){
			map.put("date", thisDate);
			map.put("time", changeToTime(changeToSecond(thisTime)+int_interval_second));
			map.put("int_interval_second", "0");
		}else{
			map.put("date", getNextDate(thisDate));
			map.put("time", "00:00:00");
			map.put("int_interval_second", String.valueOf(int_interval_second-the_whole_day_millisecond));
		}
		return map;
	}
	
	/**获取工作完成时间步骤(剔除休息日，法定节假日，工作日中的休息时间)
	 * 接口主要逻辑:从开始时间起算，得出一个特定的时间点消耗掉所有的工作时长
	 * @param  orgId      部门编码
	 * @param  startTime  开始时间
	 * @param  interval  时间间隔，单位秒
	 * 
	 * 计算步骤：1.检查参数是否正确；
	 *           2.根据用户输入的部门编码获取该部门所在产业园的工作日期设置
	 *           while(工作时长){
		 *           3.获取开始时间的所在日期的类型，若为休息日，则跳到下一天，若为工作日则计算消耗时长
		 *           4.循环上一步，直到某天没有工作类型设置，或者工作时长消耗为 '0',则程序退出
	 *           }
	 */
	private String getNextTimeNoRestTime(String areaId,String startTime,int int_interval_second){
		
		/*String areaId = util.getAreaByOrgId(orgId);
		if(null == areaId||"".equals(areaId)){
			return no_erea_set;
		}*/
		String thisYear = startTime.substring(0, 4);
		
		Map<String,String> getThisAndNextYearArrange = util.getThisAndNextYearArrange(areaId, thisYear);
		
		String thisDate = getDate(startTime);//循环变量，日期
		String thisTime = startTime.split(" ")[1];//循环变量，时间
		
		//int int_interval_second = changeIntervalToSecond(interval);//循环变量，剩余工作时间
		
		
		while(0 != int_interval_second){//程序在工作剩余秒数为"0"时退出
			Map<String,String> map = new HashMap<String,String>();
			String type = getThisAndNextYearArrange.get(thisDate);
			if(null == type||"".equals(type)){//没有设置类型，返回错误代码
				return no_workDate_set;
			}else if("1".equals(type)){//'1'表示工作日，计算工作时间消耗 
				map = getSurplusSecondNoRestTime( areaId,thisDate,thisTime,int_interval_second);
				if("error" == map.get("re")){
					return no_workTime_set;
				}else{
					int_interval_second = Integer.parseInt(map.get("int_interval_second"));
					thisDate = map.get("date");
					thisTime = map.get("time");
				}
				
            }else{//休息日，跳到下一天
            	thisDate = getNextDate(thisDate);
            	thisTime = "00:00:00";
            }
		}
		
		
		
		return thisDate+" "+thisTime;
	}
	
	/**计算每天消耗的工作时长，并返回明天的工作起始时间和剩余工作时长
	 * @param areaId  区域编码
	 * @param date  开始日期
	 * @param time  开始时间
	 * @param int_interval_second 剩余工作时长
	*/
	private Map<String,String> getSurplusSecondNoRestTime(String areaId,String date,String time,int int_interval_second){
		Map<String,String> map = new HashMap<String,String>();
		List<WorkTime> list = util.getWorkTimeByWorkDate(areaId, date);//获取当日的时间安排
	    if(null == list || list.size()==0){
	    	map.put("re","error");
	    	return map;
	    }else{
	    	map.put("re","success");
	    	
	    	int t = changeToSecond(time);
	    	for(int i = 0;i<list.size();i++){
	    		int totalWorkTimeToday = 0;
	    		int s= changeToSecond(list.get(i).getStartTime());
	    		int e = changeToSecond(list.get(i).getEndTime());
	    			    		 
	    		if(t<s){
	    			totalWorkTimeToday = totalWorkTimeToday + (e-s);
	    			t = s;
	    		}else if(s<=t&&t<=e){
	    			totalWorkTimeToday = totalWorkTimeToday + (e-t);
	    		}
	    		if(int_interval_second <= totalWorkTimeToday){
	    			map.put("int_interval_second", "0");
	    			map.put("date", date);
	    			map.put("time", changeToTime(t+int_interval_second));
	    			return map;
	    		}
	    		int_interval_second = int_interval_second-totalWorkTimeToday;
	    		if(i < list.size()-1&&t<=changeToSecond(list.get(i+1).getStartTime())){
	    			t = changeToSecond(list.get(i+1).getStartTime());
	    		}
	    		if(i == list.size()-1){
	    			map.put("int_interval_second", String.valueOf(int_interval_second));
	    			map.put("date", getNextDate(date));
	    			map.put("time", "00:00:00");
	    			return map;
	    		}
	    	}
	    }
	    return null;
	}
	
	/*把毫秒转换成秒
	*/
	private int millisecondToSecond(long  millisecond){
		
		int second = new Long(millisecond).intValue() /1000;
		
		return second;
	}
	/*把天，小时，分钟转换成秒
	*/
	private int changeIntervalToSecond(String interval){
		float value =  Float.valueOf(interval.substring(0,interval.length()-1)) ;
		String piece = interval.substring(interval.length()-1,interval.length());
		if("D".equals(piece)){
			return (int)(7.5*3600*value);
		}else if("H".equals(piece)){
			return (int)(3600*value);
		}else{
			return (int)(60*value);
		}
	}
	
	private String changeToTime(int time){
		int s = time%60;
		int m = (time/60)%60;
		int h = (time/60)/60;
		
		String second = String.valueOf(s).length()==1?"0"+String.valueOf(s):String.valueOf(s);
		String min =   String.valueOf(m).length()==1?"0"+String.valueOf(m):String.valueOf(m);
		String hour = String.valueOf(h).length()==1?"0"+String.valueOf(h):String.valueOf(h);
		return hour + ":" +min+":"+second;
	}
	private int changeToSecond (String time){
		String [] arr = time.split(":");
		if(arr.length==2){
			return (Integer.parseInt(arr[0])*3600+Integer.parseInt(arr[1])*60);
		}else if(arr.length==3){
			return (Integer.parseInt(arr[0])*3600+Integer.parseInt(arr[1])*60+Integer.parseInt(arr[2]));
		}
		return -1;
	}
	
	private  String getNextDate(String thisDate){
		SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int y = Integer.parseInt(thisDate.split("-")[0]);
		int m=Integer.parseInt(thisDate.split("-")[1]);
		int d=Integer.parseInt(thisDate.split("-")[2]);
		calendar.set(Calendar.YEAR,y);
		calendar.set(Calendar.MONTH,m-1);
		calendar.set(Calendar.DAY_OF_MONTH,d);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		
		return df_date.format(calendar.getTime());
	}
	
	private String getDate(String dateAndTime){
		String date = dateAndTime.split(" ")[0];
		String [] arr = date.split("-");
		String year = arr[0];
		String month = 1== arr[1].length()?"0"+arr[1]:arr[1];
		String day = 1== arr[2].length()?"0"+arr[2]:arr[2];
		return year +"-"+month+"-"+day;
	}
	
	/**输入参数校验
	*/
	private String checkParameters(String orgId,String startTime,String interval){
		if(null == orgId ||"".equals(orgId)){
			return error_orgId;
		}else{
			    if(!orgId.matches(rexp_orgId)){
			    	return error_orgId;
			    }
		}
		if(null == startTime ||"".equals(startTime)){
			return error_startTime;
		}else{
			    if(!startTime.matches(rexp_startTime)){
			    	return error_startTime;
			    }
		}
		if(null == interval ||"".equals(interval)){
			return error_interval;
		}else{
			    if(!interval.matches(rexp_interval)){
			    	return error_interval;
			    }
			    float value =  Float.valueOf(interval.substring(0,interval.length()-1)) ;
				String piece = interval.substring(interval.length()-1,interval.length());
				if("D".equals(piece)){
					if(value > interval_bound_day){
						return over_bound;
					}
				}else if("H".equals(piece)){
					if(value > interval_bound_hour){
						return over_bound;
					}
				}else{
					if(value > interval_bound_min){
						return over_bound;
					}
				}
		}
		return right_para;
	}
	
	public void testGetWorkTime(){
		
		String s_start = "2014-07-02 06:55:55";
		String s_end = "2014-07-02 18:40:25";
		
		String s_start1 = "2014-07-02 08:55:55";
		String s_end1 = "2014-07-02 12:00:00";
		
		String s_start2 = "2014-07-02 14:30:55";
		String s_end2 = "2014-07-02 18:40:25";
		
        SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        try {
        	Map<String,Object> map = getWorkTime("30010652-11ee-447f-a59e-bdfb3058871b",df_dateAndTime.parse(s_start),df_dateAndTime.parse(s_end));
			System.out.println(changeToTime(Long.valueOf((Long)map.get("workTime"))/1000));
		}catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
        	Map<String,Object> map = getWorkTime("30010652-11ee-447f-a59e-bdfb3058871b",df_dateAndTime.parse(s_start1),df_dateAndTime.parse(s_end1));
        	System.out.println(changeToTime(Long.valueOf((Long)map.get("workTime"))/1000));
		}catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
        	Map<String,Object> map = getWorkTime("30010652-11ee-447f-a59e-bdfb3058871b",df_dateAndTime.parse(s_start2),df_dateAndTime.parse(s_end2));
        	System.out.println(changeToTime(Long.valueOf((Long)map.get("workTime"))/1000));
		}catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
			getWorkTime("30010652-11ee-447f-a59e-bdfb3058871b",null,df_dateAndTime.parse(s_end2));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String changeToTime(long time){
		long s = time%60;
		long m = (time/60)%60;
		long h = (time/60)/60;
		
		String second = String.valueOf(s).length()==1?"0"+String.valueOf(s):String.valueOf(s);
		String min =   String.valueOf(m).length()==1?"0"+String.valueOf(m):String.valueOf(m);
		String hour = String.valueOf(h).length()==1?"0"+String.valueOf(h):String.valueOf(h);
		return hour + ":" +min+":"+second;
	}
	/**根据部门编码，任务开始时间和结束时间获取中间的工时
	 * 输入
	*orgId 部门编码
	*startTime 开始时间
	*endTime 结束时间
	*输出map
	*status 状态    13 ：正常返回结束计算  其他值均为异常，具体含义参见静态参数
	*workTime 工时
	*/
	public Map<String,Object> getWorkTime(String orgId,Date startTime,Date endTime)throws Exception{
		
		 String areaId = util.getAreaByOrgId(orgId);
        //找默认的产业园
  		if(null == areaId||"".equals(areaId)){
  			areaId = util.getDefaultAreaId();
  		}
		Map<String,Object> map = new HashMap<String,Object>();
		String legalParas = checkParameter( areaId, startTime, endTime);
		map.put("status",legalParas);
		if(!right_para.equals(legalParas)){
			map.put("status",null);
			map.put("workTime",null);
		}else{
			getWorkTime( map, areaId, startTime, endTime);
		}
		return map;
	}
	
	//检查参数
	private String checkParameter(String areaId,Date startTime,Date endTime){
		String re = right_para;
		if(null == areaId || "".equals(areaId)){
			re = error_areaId;
			return re;
	    }
		if(null == startTime){
			re = error_startTime;
			return re;
		}
		if(null == endTime){
			re = error_endTime;
			return re;
		}
        if(endTime.before(startTime)){
        	re = error_timeSet;
			return re;
		}
		
        return re;
	}
	/**根据部门编码，任务开始时间和结束时间获取中间的工时
	 * 外围算法，遍历每天的设置类型，工作日还是休息日
	 * 输入
	*areaId 区域编码
	*startTime 开始时间
	*endTime 结束时间
	*输出map
	*status 状态    13 ：正常返回结束计算  其他值均为异常，具体含义参见静态参数
	*workTime 工时
	*/
	private void getWorkTime(Map<String,Object> map,String areaId,Date startTime,Date endTime)throws Exception{
		Long interval = new Long(0);

		SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string_startTime = df_dateAndTime.format(startTime);
		
		//获得今年剩余和明年全年的工作/休息安排
		String thisYear = string_startTime.substring(0, 4);
		
		Map<String,String> getThisAndNextYearArrange = util.getThisAndNextYearArrange(areaId, thisYear);
		
		//开始的时间基点
		String thisDate = string_startTime.split(" ")[0];//循环变量，日期
		String thisTime = string_startTime.split(" ")[1];//循环变量，时间
		
		while(true){
			String type = getThisAndNextYearArrange.get(thisDate);
			if(null == type||"".equals(type)){//没有设置类型，返回错误代码
				map.put("status",no_workDate_set);
				map.put("workTime",null);
				break;
			}else if("1".equals(type)){//'1'表示工作日，计算工作时间消耗 
				 Map<String ,Object> re = calculateInterval( areaId, thisDate, thisTime, endTime);
				 String status = String.valueOf( re.get("status"));
				 
				if(end_calculate .equals(status)){//结束计算
					interval = interval + Long.valueOf((Long)re.get("interval"));
					map.put("status",re.get("status"));
					map.put("workTime",interval);
					break;
					
				}else if(not_end_calculate.equals(status)){//继续计算
					interval = interval + Long.valueOf((Long)re.get("interval"));
					thisDate = getNextDate(thisDate);
		        	thisTime = "00:00:00";
					
				}else{//计算过程报错
					map.put("status",re.get("status"));
					map.put("workTime",new Long(0));
					break;
				}
				
	        }else{//休息日，跳到下一天
	        	thisDate = getNextDate(thisDate);
	        	thisTime = "00:00:00";
	        }
		}
		
		
	
	}
	/**根据部门编码，任务开始时间和结束时间获取中间的工时
	 * 核心算法，遍历每天的工作时间段
	 * 输入
	*areaId 区域编码
	*startTime 开始时间
	*endTime 结束时间
	*输出map
	*status 状态    13 ：正常返回结束计算  其他值均为异常，具体含义参见静态参数
	*workTime 工时
	*/
	private Map<String ,Object> calculateInterval(String areaId,String thisDate,String thisTime,Date endTime)throws Exception{
		
		SimpleDateFormat df_dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long interval = new Long(0);
		
		
		Map<String ,Object> map = new HashMap<String,Object>();
		List<WorkTime> list = util.getWorkTimeByWorkDate(areaId, thisDate);//获取当日的时间安排
	    if(null == list || list.size()==0){
	    	map.put("status",no_workTime_set);
	    	return map;
	    }else{
	    	
	    	long t =df_dateAndTime.parse(thisDate+" "+thisTime).getTime();//开始时间
	    	long end = endTime.getTime();//结束时间
	    	for(int i = 0;i<list.size();i++){
	    		long s= df_dateAndTime.parse(thisDate+" "+(list.get(i).getStartTime().length()==8?list.get(i).getStartTime():list.get(i).getStartTime())+":00").getTime();//工作区开始时间
	    		long e = df_dateAndTime.parse(thisDate+" "+(list.get(i).getEndTime().length() == 8?list.get(i).getEndTime():list.get(i).getEndTime()+":00")).getTime();//工作区结束时间
	    		
	    			    		 
	    		if(s>=end){//开始时间   大于   工作区间结束时间，结束循环
	    			map.put("interval", interval);
	    			map.put("status", end_calculate);
	    			return map;
	    		}else if(t>=e){
	    			
	    		}else if(t<=s && e>=end){
	    			interval = interval +(end -s);
	    			map.put("interval", interval);
	    			map.put("status", end_calculate);
	    			return map;
	    		}else if(t<=s && end >=e){
	    			interval = interval +(e -s);
	    			t = e;
	    		}else if(t>=s && e>=end){
	    			interval = interval +(end -t);
	    			map.put("interval", interval);
	    			map.put("status", end_calculate);
	    			return map;
	    		}else if(t>=s && end>=e){
	    			interval = interval +(e -t);
	    			t = e;
	    		}
	    		
	    	}
	    	map.put("interval", interval);
	    	map.put("status", not_end_calculate);
			return map;
	    }
		
	}
}

  
