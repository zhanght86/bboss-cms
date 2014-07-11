package com.sany.workflow.job.action;

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
	private WorkTimeUtil workTimeUtil ;
	private static String rexp_completeTime = "\\d{4}\\-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	private Calendar instance = Calendar.getInstance();
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private JobService jobService;

	public static void main(String [] args0){
		String s1 = "2014-06-16 17:14:14.233";
		System.out.print(s1.split("\\.")[0]);
	}
	
	
	/**检查流程超时,预警节点任务
	*1.查出数据库中所有正在处理和未签收的节点任务
	*2.检查参数是否存在为空；
	*3.根据用户名找到用户所属的部门ID；
	*4.计算最后工作完成时间
	*5.如果超时则发邮件
	*/
	public void checkProcessOvertimeJob() throws Exception{
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
						String taskId = process.get("taskId");
						String orgId = process.get("orgId");
						long interval = Long.parseLong(process.get("duration"));
						String createTime = process.get("createTime").matches(rexp_completeTime)?process.get("createTime"):process.get("createTime").split("\\.")[0];
						String noticeRate = process.get("noticeRate");
						String isContainHoliday = process.get("isContainHoliday");
						
						/*计算任务完成时间    */
						String completeTime = getNextTime(isContainHoliday,orgId, createTime, interval);
						if(!completeTime.matches(rexp_completeTime)){
							throw new ProcessParamCheckException("taskId="+taskId+"的节点任务完成时间返回错误!------"+completeTime);
						}
						/*
						 * 如果当前时间小于完成时间，任务没有超时，则判断是否需要预警,如果大于则需要发消息提醒，并设置消息主题
						*/
						if(compareTime( instance, completeTime)){
							if(null == noticeRate||"".equals(noticeRate)){
								delList.add(process);
								throw new ProcessParamCheckException("taskId="+taskId+"的节点任务--预警比例为空");
							}else{
								/*计算任务预警时间    */
								long intervalNotice = (interval*Integer.parseInt(noticeRate))/100;
								String noticeTime = getNextTime(isContainHoliday,orgId, createTime, intervalNotice);
								
     
								if(!noticeTime.matches(rexp_completeTime)){
									throw new ProcessParamCheckException("taskId="+taskId+"的节点任务预警时间返回错误!------"+noticeTime);
								}
								/*
								 * 如果当前时间小于预警时间，任务不要预警，如果大于则需要发消息提醒，并设置消息主题
								*/
								if(compareTime( instance, noticeTime)){
									delList.add(process);
									continue;
								}else{
									process.put("subject", "流程预警提醒");
								}
							}
						}else{
							process.put("subject", "流程超时提醒");
						}
					}catch(Exception e){
						delList.add(process);
						e.printStackTrace();
						
					}
				}
				list.removeAll(delList);
				templeService.sendNotice(list);
			}
		
		
		
	}
	
	/**
	 * 计算完成时间   
	 * 工时规则0 ：24时工作制 1： 自然日 2 ：正常工作制
	 * */
	private String getNextTime(String isContainHoliday,String orgId,String createTime,long interval)throws Exception{
		
		if("0".equals(isContainHoliday)){
		return	workTimeUtil.getNextTimeNoRest(createTime, interval);
		}else if("1".equals(isContainHoliday)){
			return	workTimeUtil.getNextTimeNoRestDay(orgId, createTime, interval);
		}else if("2".equals(isContainHoliday)){
			return	workTimeUtil.getNextTimeNoRestTime(orgId, createTime, interval);
		}
		return null;
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
		
		
		if(null ==process.get("taskName") ||"".equals(process.get("taskName"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--节点名称为空");
		}
		if(null ==process.get("userId") ||"".equals(process.get("userId"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--用户名为空");
		}
		
		if(null ==process.get("orgId") ||"".equals(process.get("orgId"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--部门编码为空");
		}
		if(null ==process.get("isContainHoliday") ||"".equals(process.get("isContainHoliday"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--完成时间计算方式为空");
		}
		/*if(null ==process.get("noticeRate") ||"".equals(process.get("noticeRate"))){
			throw new ProcessParamCheckException("taskId="+id+"的节点任务--预警比例为空");
		}*/
	}
}
