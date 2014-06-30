package com.sany.workflow.job.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.holiday.area.util.WorkTimeUtil;
import com.sany.workflow.job.exception.ProcessParamCheckException;
import com.sany.workflow.job.service.JobService;
import com.sany.workflow.service.TempleService;
public class CheckProcessOvertime {
	
	private TempleService templeService;
	private WorkTimeUtil workTimeUtil = new WorkTimeUtil();
	private static String rexp_completeTime = "\\d{4}\\-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	private Calendar instance = Calendar.getInstance();
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private JobService jobService;

	public static void main(String [] args0){
		String s1 = "2014-06-16 17:14:14.233";
		System.out.print(s1.split("\\.")[0]);
	}
	public void testMail(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("state","state");
		map.put("taskId","id");
		map.put("taskName","taskName");
		map.put("createTime","createTime");
		map.put("userId","userId");
		map.put("processName","processName");
		map.put("duration","duration");
		map.put("simpleDate","simpleDate");
		map.put("messageTempleId","messageTempleId");
		map.put("emailTempleId","emailTempleId");
		map.put("realName","realName");
		map.put("orgId","orgId");
		map.put("mobile","mobile");
		map.put("mailAddress", "qingl2@sany.com.cn");
		map.put("subject", "节点任务超时通知");
		list.add(map);
		templeService.sendNotice(list);
	}
	
	/**检查流程超时节点
	*1.查出数据库中所有正在处理和未签收的节点任务
	*2.检查参数是否存在为空；
	*3.根据用户名找到用户所属的部门ID；
	*4.计算最后工作完成时间
	*5.如果超时则发邮件
	*/
	@SuppressWarnings("finally")
	public void checkProcessOvertimeJob() throws SQLException{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> delList = new ArrayList<Map<String, String>>();
			try {
			 list = jobService.getProcessNodeUnComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(null != list && list.size()>0){
				for(Map<String, String> process :list){
					try{
						checkPara( process);
						String orgId = process.get("orgId");
						String interval = process.get("duration")+process.get("simpleDate");
						String createTime = process.get("createTime").matches(rexp_completeTime)?process.get("createTime"):process.get("createTime").split("\\.")[0];
						String completeTime = workTimeUtil.getNextTime(orgId, createTime, interval);
						if(!completeTime.matches(rexp_completeTime)){
							throw new ProcessParamCheckException("完成时间返回错误!------"+completeTime);
						}
						if(compareTime( instance, completeTime)){
							delList.add(process);
							continue;
						}
					}catch(ProcessParamCheckException e){
						e.printStackTrace();
						
					}catch(Exception e){
						e.printStackTrace();
						
					}finally{
						delList.add(process);
						continue;
					}
				}
				list.removeAll(delList);
				templeService.sendNotice(list);
			}
		
		
		
	}
	
	/**
	 * 比较当前时间和完成时间
	 * 当前时间小于完成时间返回true；大于则false
	*/

	private boolean compareTime(Calendar instance,String completeTime)throws Exception{
		
		return instance.getTime().before(df.parse(completeTime));
		
	}
	/**
	 * 参数检查
	*/
	private void checkPara(Map<String, String> process) throws ProcessParamCheckException{
		String id = process.get("taskId");
		if(null ==process.get("createTime") || "".equals(process.get("createTime"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--创建时间为空");
		}
		if(null ==process.get("duration") ||"".equals(process.get("duration"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--工时长度为空");	
				}
		
		if(null ==process.get("processName") ||"".equals(process.get("processName"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--流程名称为空");
		}
		if(null ==process.get("simpleDate") ||"".equals(process.get("simpleDate"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--时间单位为空");
		}
		
		if(null ==process.get("taskName") ||"".equals(process.get("taskName"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--节点名称为空");
		}
		if(null ==process.get("userId") ||"".equals(process.get("userId"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--用户名为空");
		}
		
		if(null ==process.get("orgId") ||"".equals(process.get("orgId"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--部门编码为空");
		}
	}
}
