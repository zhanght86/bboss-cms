package com.sany.workflow.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.KPI;
import org.activiti.engine.KPIService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.platform.holiday.area.bean.FlowWarning;
import com.frameworkset.platform.holiday.area.util.WorkTimeUtil;
import com.sany.workflow.entity.NodeInfoEntity;
import com.sany.workflow.service.ActivitiService;

public class PlatformKPIServiceImpl implements KPIService {
	private WorkTimeUtil workTimeUtil;
	private ActivitiService activitiService;
	private static ThreadLocal<List<Map<String, Object>>> worktimelist = new ThreadLocal<List<Map<String, Object>>>();
	private static Logger log = Logger.getLogger(PlatformKPIServiceImpl.class);
	public PlatformKPIServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setWorktimelist(List<Map<String, Object>> worktimeList)
	{
		worktimelist.set(worktimeList);
	}
	
	@Override
	public KPI buildKPI(DelegateExecution execution,
			Collection<String> candiates,Date taskCreateTime) throws ActivitiException{
		NodeInfoEntity nodeInfoEntity = null;
		try {
			if(this.workTimeUtil == null)
				this.workTimeUtil = WebApplicationContextUtils.getWebApplicationContext().getTBeanObject("sysmanager.holiday.util", WorkTimeUtil.class);
			if(this.activitiService == null)
				this.activitiService = WebApplicationContextUtils.getWebApplicationContext().getTBeanObject("activitiService", ActivitiService.class);
			String activieKey = execution.getCurrentActivityId();
			String pinstanceid = execution.getProcessInstanceId();
			nodeInfoEntity = activitiService.getNodeWorktime(pinstanceid, activieKey);
			if(nodeInfoEntity == null)
			{
				nodeInfoEntity = activitiService.getNodeInfoEntity(worktimelist.get(), activieKey);
			}
			if(nodeInfoEntity == null || nodeInfoEntity.getDURATION_NODE() == 0)
				return null;
			String userAccount = candiates.iterator().next();
			
			FlowWarning bean;
				//工号或域账户, 任务创建时间, 完成时间类型（0全年为工作日,1去除双休和法定节假日,2提出工作日休息时间）, 工时, 预警比例
				bean = workTimeUtil.getCompleteAndWarnTime(userAccount, 
															taskCreateTime, 
															nodeInfoEntity.getIS_CONTAIN_HOLIDAY(), 
															nodeInfoEntity.getDURATION_NODE(), 
															nodeInfoEntity.getNOTICERATE());
			if(bean == null)
				return null;
			KPI kpi = new KPI();
			kpi.setALERTTIME(bean.getWarnTime());
			kpi.setDURATION_NODE(bean.getInterval());
			kpi.setOVERTIME(bean.getCompleteTime());
			kpi.setIS_CONTAIN_HOLIDAY(nodeInfoEntity.getIS_CONTAIN_HOLIDAY());
			kpi.setNOTICERATE(nodeInfoEntity.getNOTICERATE());
			return kpi;
		} catch (NumberFormatException e) {
			
			throw new ActivitiException("Change HOLIDAY Policy to int error:"+(nodeInfoEntity != null?nodeInfoEntity.getIS_CONTAIN_HOLIDAY():0),e);
		} catch (Exception e) {
			throw new ActivitiException("Change HOLIDAY Policy to int error:"+(nodeInfoEntity != null?nodeInfoEntity.getIS_CONTAIN_HOLIDAY():0),e);
		}
	
	
	}

}
