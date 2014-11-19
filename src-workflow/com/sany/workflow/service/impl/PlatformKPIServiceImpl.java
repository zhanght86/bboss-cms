package com.sany.workflow.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ControlParam;
import org.activiti.engine.KPI;
import org.activiti.engine.KPIService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.platform.holiday.area.bean.FlowWarning;
import com.frameworkset.platform.holiday.area.util.WorkTimeUtil;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.NodeInfoEntity;
import com.sany.workflow.service.ActivitiService;

public class PlatformKPIServiceImpl implements KPIService {
	private WorkTimeUtil workTimeUtil;
	private ActivitiService activitiService;
	private static ThreadLocal<List<NodeControlParam>> nodeControlParamList = new ThreadLocal<List<NodeControlParam>>();
	private static Logger log = Logger.getLogger(PlatformKPIServiceImpl.class);
	public PlatformKPIServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setWorktimelist(List<NodeControlParam> controlParamList)
	{
		nodeControlParamList.set(controlParamList);
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
				nodeInfoEntity = activitiService.getNodeInfoEntity(nodeControlParamList.get(), activieKey);
			}
			if(nodeInfoEntity == null || nodeInfoEntity.getDURATION_NODE() == 0)
				return null;
			String userAccount = candiates.iterator().next();
			
			 
				//工号或域账户, 任务创建时间, 完成时间类型（0全年为工作日,1去除双休和法定节假日,2提出工作日休息时间）, 工时, 预警比例
			 FlowWarning	bean = workTimeUtil.getCompleteAndWarnTime(userAccount, 
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

	@Override
	public ControlParam getControlParam(DelegateExecution currentexecution,String activieKey)
			throws ActivitiException {
		return getControlParam( currentexecution.getProcessInstanceId(),activieKey);
	}
	
	@Override
	public ControlParam getControlParam(String processInstanceId,String activieKey)
			throws ActivitiException {
		try {
			if(this.activitiService == null)
				this.activitiService = WebApplicationContextUtils.getWebApplicationContext().getTBeanObject("activitiService", ActivitiService.class);
			NodeControlParam controlParam = activitiService.getNodeControlParam(
					processInstanceId,activieKey );
			List<NodeControlParam> controlParamList = nodeControlParamList.get();
			if(controlParam == null)
				controlParam = getNodeInfoEntity(controlParamList,activieKey);
			if(controlParam == null)
				return null;
			ControlParam param = new ControlParam();
			param.setID(controlParam.getID());
			param.setBUSSINESSCONTROLCLASS(controlParam.getBUSSINESSCONTROLCLASS());
			param.setIS_AUTO(controlParam.getIS_AUTO());
			param.setIS_AUTOAFTER(controlParam.getIS_AUTOAFTER());
			param.setIS_COPY(controlParam.getIS_COPY());
			param.setIS_MULTI(controlParam.getIS_MULTI());
			param.setIS_SEQUENTIAL(controlParam.getIS_SEQUENTIAL());
			param.setIS_VALID(controlParam.getIS_VALID());
			param.setNODE_KEY(activieKey);
			param.setNODE_NAME(controlParam.getNODE_NAME());
			param.setPROCESS_ID(processInstanceId);
			param.setPROCESS_KEY(controlParam.getPROCESS_KEY());
			param.setCopyersCNName(controlParam.getCOPYERSCNNAME());
			param.setCopyOrgs(controlParam.getCOPYORGS());
			param.setCopyUsers(controlParam.getCOPYUSERS());
			
			return param;
			
		} catch (Exception e) {
			throw new ActivitiException("get Node Control Param error:" , e);
		}
	}
	
	private NodeControlParam getNodeInfoEntity(
			List<NodeControlParam> controlParamList, String taskKey)
	{
		if(controlParamList == null)
			return null;
		for(NodeControlParam param:controlParamList)
		{
			if(param.getNODE_KEY().equals(taskKey))
			{
				return param;
			}
		}
		return null;
	}

	@Override
	public void archiveProcessRuntimedata(DelegateExecution currentexecution,
			String processInstanceID) throws ActivitiException {
		if(this.activitiService == null)
			this.activitiService = WebApplicationContextUtils.getWebApplicationContext().getTBeanObject("activitiService", ActivitiService.class);
		activitiService.backupDatasToWorktime(processInstanceID);
	}

}
