/*
 * @(#)AppBomServiceImpl.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.appmonitor.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.frameworkset.task.Execute;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.sany.appmonitor.entity.AppInfo;
import com.sany.appmonitor.entity.PortalPendMonitor;
import com.sany.appmonitor.entity.PortalPendMonitorCondition;
import com.sany.mail.MailInfo;
import com.sany.mail.SendMail;


public class AppMonitorServiceImpl  implements Execute {
	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;
	private static final int DEFAULT_TIMEOUT = 3000;

	/**
	 * 分页查找记录
	 * @param idmap
	 * @return
	 */
	public ListInfo queryList(long offset, int pagesize, PortalPendMonitorCondition condition){
		ListInfo datas=null;
		try{
			datas=executor.queryListInfoBean(PortalPendMonitor.class, "queryList",  offset, pagesize, condition);
		}catch(Throwable e){
			
		}
		return datas;
	}
	/**
	 * 
	 * @param idmap
	 * @return
	 */
	public List queryReport(PortalPendMonitorCondition condition){
		List datas=null;
		try{
			datas=executor.queryListBean(Map.class,"queryReport", condition);
		}catch(Throwable e){
			
		}
		return datas;
	}
	
	public Map<Integer,String> getAppInfoMap(){

		final Map<Integer,String> datas=new HashMap<Integer,String>();
		try{
			executor.queryByNullRowHandler(new NullRowHandler() {

				@Override
				public void handleRow(Record origine) throws Exception {
					datas.put(origine.getInt("appId"), origine
							.getString("appName"));

				}
			}, "getAllAppInfo");
			}catch(Exception e){
				
			}
			return datas;
			
	}
	public List<AppInfo> getAllAppInfo(){
		List datas=null;
		try{
			datas=executor.queryList(AppInfo.class,"getAllAppInfo");
		}catch(Throwable e){
			
		}
		return datas;
	}
	public List<AppInfo> getAppInfoUse(){
		List datas=null;
		try{
			datas=executor.queryList(AppInfo.class,"getAppInfoUse");
		}catch(Throwable e){
			
		}
		return datas;
	}
	
	public List<HashMap> queryReportList(PortalPendMonitorCondition condition) {
		final List<HashMap> datas = new ArrayList<HashMap>();
		try{
		executor.queryBeanByNullRowHandler(new NullRowHandler() {

			@Override
			public void handleRow(Record origine) throws Exception {
				HashMap map=new HashMap();
				map.put("appName", origine.getString("appName"));
				map.put("avgUseTime", origine.getLong("avgUseTime"));
				map.put("maxUseTime", origine.getLong("maxUseTime"));
				datas.add(map);

			}
		}, "queryReportList", condition);
		}catch(Exception e){
			
		}
		return datas;
	}
	
	public void PendingMonitor(){
		List<AppInfo> appInfo=getAppInfoUse();
		if(appInfo==null || appInfo.size()==0)
			return;
		Date dt=new Date();
		List<PortalPendMonitor>beans=new ArrayList<PortalPendMonitor>();
		TransactionManager tm = new TransactionManager();
		try {
			List<HashMap> list=new ArrayList<HashMap>();
			for(AppInfo info:appInfo){
				long start = System.currentTimeMillis();
				Integer useTime=0;
				try{
					getAppPending("xusy3",(info.getAppId()));
					long end = System.currentTimeMillis();
					useTime=Integer.valueOf((int)(end -start));
				}catch(Exception ex){
					useTime=3000;
					HashMap map=new HashMap();
					map.put("appId", info.getAppId());
					map.put("appName",info.getAppName()); 
					map.put("monitorTime", dt);
					list.add(map);
				}
				PortalPendMonitor bean=new PortalPendMonitor();
				String uuid=java.util.UUID.randomUUID().toString();
				bean.setId(uuid);
				bean.setAppId(info.getAppId());
				bean.setMonitorTime(new Timestamp(dt.getTime()));
				bean.setUseTime(useTime);
				beans.add(bean);
			}
			tm.begin();
			executor.insertBeans("insertPortalMonitor", beans);
			tm.commit();
			//邮件发送
			for(HashMap map:list){ 
				portalPendingSendMail((Integer)map.get("appId"),(String)map.get("appName"),(Date)map.get("monitorTime"));
			}
		}catch(Exception ex){
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * 从数据库读取代办ESB上web services地址
	 * @param appId
	 * @return
	 */
	private String getAppUrl(Integer appId) {
		AppInfo bean=new AppInfo();
		bean.setAppId(appId);
		try{
			bean = executor.queryObjectBean(AppInfo.class,"getAppInfoByKey",bean);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return bean.getWsUrl();
	}
	/**
	 * 获取单个用户代办
	 * @param user
	 * @param appId
	 * @return application pending num
	 * @throws Exception 
	 */
	public Integer getAppPending(String username, Integer appId) throws Exception {
		
		int timeOut = PendingMappingUtils.getUrl("MBPendingTimeout")==null?DEFAULT_TIMEOUT:Integer.parseInt(PendingMappingUtils.getUrl("MBPendingTimeout"));
		String requsetBody = PendingMappingUtils.getUrl("MBPendingRequsetBody");
		
		requsetBody = requsetBody.replace("[USERNAME]", username);
		
		GenericClient gClient = new GenericClient();
		String responseBody = "";
		try {
			responseBody = gClient.requestAsString(getAppUrl(appId), requsetBody, timeOut);
			
			int st = responseBody.indexOf("<NS1:out>") + 9;
			int en = responseBody.indexOf("</NS1:out>");
			if (st <= en) responseBody = responseBody.substring(st, en);
			
		} catch (Exception e) {
			throw e;
		}

		
		return Integer.parseInt(responseBody);	
	}
	
	/**
	 * 定时检测待办并在超时情况下发送邮件给指定人
	 */
	public void portalPendingSendMail(Integer appId,String appName,Date monitorTime){
		List<String> datas=new ArrayList<String>();
		try{
			datas = SQLExecutor.queryList(String.class, "select userName as userName from td_portal_monitor_mailUser where appId= "+appId+" or appId is null");
		
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		if(datas.size()==0){
			return;
		}
		try  
		{  
			MailInfo mailinfo=new MailInfo();     
			mailinfo.setValidate(true);   
			mailinfo.setUserName("");   
			mailinfo.setPassword(""); 
			//--设置邮件服务器结束   
			mailinfo.setFromAddress("");//邮件发送者的地址   
			//设置接受用户   
			for(String user:datas){
				String[] ToAddress=new String[]{""};
				//ToAddress[0]=datas.toArray(ToAddress);
				ToAddress[0]=user+"@sany.com.cn";
				mailinfo.setToAddress(ToAddress);   
				//设置附件   
	//			String []attach={"F:\\login.properties"};   
	//			mailinfo.setAttachFileNames(attach);   
				mailinfo.setSubject("["+appName+"]待办超时");   
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				mailinfo.setContent("<div style='font-weight: bold;	color: #0066CC;	padding-bottom: 15px;'>尊敬的"+user+":</div><div style='font-size: 14px;text-indent: 30px;'>[<font color='#063EBD'>"+appName+"</font>]取待办超时。检测时间："+simpleDateFormat.format(monitorTime) +"<p>请您及时跟踪查找原因并妥善解决。</div>");//网页内容   
				SendMail sm=new SendMail();   
				if(sm.sendHtmlMail(mailinfo))   
					System.out.println("邮件发送成功");   
				else  
					System.out.println("邮件发送失败");   
			}
		}   catch   (Exception   ex)  
		{  
			ex.printStackTrace();
		}  
	}
	/**
	 * 注入使用
	 * @return
	 */
	public com.frameworkset.common.poolman.ConfigSQLExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(
			com.frameworkset.common.poolman.ConfigSQLExecutor executor) {
		this.executor = executor;
	}
	public void execute(Map arg0) {
		
	}
}
