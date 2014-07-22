package com.sany.workflow.pending.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.sany.greatwall.WorkflowService;
import com.sany.greatwall.domain.AccessToken;
import com.sany.greatwall.domain.ToDoPro;
import com.sany.workflow.entity.NoHandleTask;
import com.sany.workflow.pending.bean.App;
import com.sany.workflow.pending.bean.SysPending;
import com.sany.workflow.pending.bean.SysTitle;
import com.sany.workflow.pending.service.PendingService;
import com.sany.workflow.service.ActivitiTaskService;
import com.frameworkset.util.StringUtil;
public class PendingAction {

	private PendingService pendingService;
	private WorkflowService gwPendingService;
	private ActivitiTaskService activitiTaskService;
	private static String GW_PENDING_TYPE = "workflow";
	private static Logger logger = Logger.getLogger(PendingAction.class);
	

	/**
	 * 获取用户所有的待办系统
	 */
	
	@SuppressWarnings("unchecked")
	public String pendingIndex(ModelMap model)throws Exception{
		String userId = AccessControl.getAccessControl().getUserID();
		List<SysTitle> datas = pendingService.getAllSysTitleByUsed("1","1",userId);
		model.addAttribute("datas", datas);
		return "path:main";
	}
	/**
	 * 获取用户所有的待办数目
	 */
	
	public @ResponseBody(datatype = "jsonp")
	Map<String,String> getSysPendingNum(String id,String name , String type) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", id+"_num");
		String userId = AccessControl.getAccessControl().getUserID();
		String userAccount = AccessControl.getAccessControl().getUserAccount();
		if("1".equals(type)){
			//工号，系统名称
			map.put("num", getPendingNumFromGW(userId,name));
		}else if("2".equals(type)){
			//域账号，系统名称
			map.put("num", getPendingNumFromLocal(userAccount,name));
		}else if("3".equals(type)){
			map.put("num", "0");
		}else{
			map.put("num", "0");
		}
		return map;
	}
	/*从GW里面取待办*/
	@SuppressWarnings("unchecked")
	private String getPendingNumFromGW(String userId,String name)throws Exception{
		AccessToken token = new AccessToken();
		String jsonString = gwPendingService.getTaskCount(null, "7183", null,GW_PENDING_TYPE);
		Map<String,Object> params = StringUtil.json2Object(jsonString, HashMap.class);
		String num = String.valueOf(params.get(name));
		return (null == num ||"".equals(num))?"0":num;
	}
	/*从本地取待办*/
    private String getPendingNumFromLocal(String userAccount,String name)throws Exception{
    	String num = String.valueOf(activitiTaskService.countTaskNum(userAccount, name));//本地使用域账号
    	return (null == num ||"".equals(num))?"0":num;
	}
	
	/**
	*跳转到待办订阅的主界面
	*/
	public String subscribeIndex(){
		return "path:subscribeIndex";
	}
	/**
	 * 获取所有的系统待办订阅
	 */
	public String querySubscribeList(ModelMap model)throws Exception{
		String userId = AccessControl.getAccessControl().getUserID();		
		List<SysTitle> datas = pendingService.getAllSysTitleForScribe(userId);
		model.addAttribute("datas", datas);
		return "path:subscribeList";
	}
	/**
	 * 新增或保存用户的待办订阅
	*/
	@SuppressWarnings("unchecked")
	public @ResponseBody String insertOrUpdatePendingSubscribe(String appId , String pendingType)throws Exception{
		String userId = AccessControl.getAccessControl().getUserID();
		TransactionManager tm = new TransactionManager();
		try{
			tm.begin();
			
			List<Map<String,String>> list =  pendingService.getSingleSub( appId, userId);
			if(null != list && list.size()>0){
				pendingService.updateSingleSub(appId, userId,pendingType);
			}else{
				pendingService.insertSingleSub(appId, userId,pendingType);
			}
			tm.commit();
			
			return "success";
		}catch(Exception e){
			
			logger.error("更新订阅状态报错",e);
			
			return  "false";
		}finally{
			tm.release();
		}
	}
	/**获取用户的待办系统（工号不为空则按用户的配置来取，若为空则全部取出来）
	*/
	public List<App> getAllPendingApp(String userId)throws Exception{
		if(null == userId || "".equals(userId)){
			return pendingService.getAllPendingApp();
		}else{
			return pendingService.getAllPendingAppByUserId(userId);
		}
	}
	/**
	*用户订阅所需要的待办
	*/
	
	public String subscribePending(ModelMap model)throws Exception{
		String userId = AccessControl.getAccessControl().getUserID();		
		List<SysTitle> datas = pendingService.getAllSysTitleForScribe(userId);
		model.addAttribute("datas", datas);
		return "path:selectPending";
	}
	/**
	 * 获取应用系统具体的待办列表(根据用户传来参数)
	*/
	@SuppressWarnings("unchecked")
	public @ResponseBody List<SysPending> getSysPending(String id,HttpServletRequest request)throws Exception{
		String userId = AccessControl.getAccessControl().getUserID();
		String userAccount = AccessControl.getAccessControl().getUserAccount();
		SysTitle sys = pendingService.getSysTitleById(id);
		String type = sys.getPendingType();
		String pendingUrl = sys.getPendingUrl();
		//取GW接口的
		if("1".equals(type)){
			return getPendingFromGW(id,"7183",1,20);
		}else if("2".equals(type)){
			return getPendingFromLocal(id,userAccount,0,0,request);
		}else if("3".equals(type)){
			return null;
		}else{
			return null;
		}
		
	}
	/**
	 * 获取GW待办的详细信息
	*/
	private  List<SysPending> getPendingFromGW(String system_id,String pernr,int low,int high)throws Exception{
		List<SysPending> list = new ArrayList<SysPending>();
		List<ToDoPro> datas = gwPendingService.getTaskList(null, pernr, system_id, low, high,GW_PENDING_TYPE);
		if(null != datas && datas.size()>0){
			for(int i=0;i<datas.size();i++){
				list.add(new SysPending(datas.get(i)));
			}
		}
		return list;
	}
	/**
	 * 获取本地待办的详细信息
	*/
	private List<SysPending> getPendingFromLocal(String system_id,String userAccount,long offset,int pagesize,HttpServletRequest request)throws Exception{
		List<SysPending> list = new ArrayList<SysPending>();
		List<NoHandleTask> datas = activitiTaskService.getNoHandleTask(userAccount, system_id, offset, pagesize,request);
		if(null != datas && datas.size()>0){
			for(int i=0;i<datas.size();i++){
				list.add(new SysPending(datas.get(i)));
			}
		}
		return list;
	}
	
}
