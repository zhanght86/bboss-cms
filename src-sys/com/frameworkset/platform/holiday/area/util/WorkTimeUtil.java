package com.frameworkset.platform.holiday.area.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.holiday.area.bean.WorkTime;
import  com.frameworkset.platform.holiday.area.bean.OrgLeaf;
import com.frameworkset.platform.holiday.area.util.action.UtilAction;

public class WorkTimeUtil {
	private  SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
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
	private static float interval_bound_day = 79536;
	private static float interval_bound_hour = 596523;
	private static float interval_bound_min = 35791380;
	private static float  hour_of_workday = 7.5f;
	
	private UtilAction util;
	
	
	public static void main(String [] args0){
		String s1 = "20111D";
		if(s1.matches(rexp_interval)){
			System.out.print("ok");
		}else{
			System.out.print("not--ok");
		}
	}
	
	public String index(){
		return "path:index";
	}
	@SuppressWarnings("unchecked")
	public @ResponseBody()List<OrgLeaf> getOrgTree() throws Exception{
		
		return util.getOrgTree();
	}
	@SuppressWarnings("unchecked")
	public @ResponseBody(datatype = "json")String getNextTimeNode(String orgId,String startTime,String interval){
		return getNextTime( orgId, startTime, interval);
	}
	/**获取工作完成时间步骤
	 * 接口主要逻辑:从开始时间起算，得出一个特定的时间点消耗掉所有的工作时长
	 * 计算步骤：1.检查参数是否正确；
	 *           2.根据用户输入的部门编码获取该部门所在产业园的工作日期设置
	 *           while(工作时长){
		 *           3.获取开始时间的所在日期的类型，若为休息日，则跳到下一天，若为工作日则计算消耗时长
		 *           4.循环上一步，直到某天没有工作类型设置，或者工作时长消耗为 '0',则程序退出
	 *           }
	 */
	public String getNextTime(String orgId,String startTime,String interval){
		String reCheck = checkParameters( orgId, startTime, interval);
		if(right_para != reCheck){
			return reCheck;
		}
		String areaId = util.getAreaByOrgId(orgId);
		if(null == areaId||"".equals(areaId)){
			return no_erea_set;
		}
		String thisYear = startTime.substring(0, 4);
		
		Map<String,String> getThisAndNextYearArrange = util.getThisAndNextYearArrange(areaId, thisYear);
		
		String thisDate = getDate(startTime);//循环变量，日期
		String thisTime = startTime.split(" ")[1];//循环变量，时间
		
		int int_interval_second = changeIntervalToSecond(interval);//循环变量，剩余工作时间
		
		
		while(0 != int_interval_second){//程序在工作剩余秒数为"0"时退出
			Map<String,String> map = new HashMap<String,String>();
			String type = getThisAndNextYearArrange.get(thisDate);
			if(null == type||"".equals(type)){//没有设置类型，返回错误代码
				return no_workDate_set;
			}else if("1".equals(type)){//'1'表示工作日，计算工作时间消耗 
				map = getSurplusSecond( areaId,thisDate,thisTime,int_interval_second);
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
	private Map<String,String> getSurplusSecond(String areaId,String date,String time,int int_interval_second){
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
}

  
