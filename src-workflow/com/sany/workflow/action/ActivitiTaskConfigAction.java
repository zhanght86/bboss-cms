package com.sany.workflow.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.codehaus.jackson.map.ObjectMapper;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.Group;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.OrganizationDTO;
import com.sany.workflow.entity.User;
import com.sany.workflow.service.ActivitiConfigException;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.util.WorkFlowConstant;

public class ActivitiTaskConfigAction {
	private ActivitiConfigService activitiConfigService;

	private ActivitiService activitiService;

	public String taskConfigMain(String deploymentId, ModelMap model) {
		model.addAttribute("processDef",
				activitiService.getProcessDefByDeploymentId(deploymentId));
		List<ActivityImpl> aList = activitiService
				.getActivitImplListByProcessKey(activitiService
						.getPorcessKeyByDeployMentId(deploymentId));
		for (int i = 0; i < aList.size(); i++) {
			if (!aList.get(i).getProperty("type").equals("userTask")) {
				aList.remove(i);
			}
		}
		model.addAttribute("aList", aList);
		model.addAttribute("deploymentId", deploymentId);
		model.addAttribute("submitMethod", "editActivitiNode.page");
		return "path:main";
	}

	public @ResponseBody(datatype = "json")
	List<OrganizationDTO> getOrgs(String id) {
		if (id == null || id.equals("")) {
			id = "0";
		}
		List<OrganizationDTO> list = activitiConfigService
				.getOrgsByParentId(id);
		return list;
	}
	
	/*public String showActivitiNodeEdit(String nodeinfoId,
			String nodeCandidateId, String usernames, String groups,
			ModelMap model) {
		ActivitiNodeCandidate activitiNodeCandidate = activitiConfigService
				.getActivitiNodeCandidateById(nodeCandidateId);
		if (activitiNodeCandidate == null) {
			activitiNodeCandidate = new ActivitiNodeCandidate();
			activitiNodeCandidate.setNode_id(nodeinfoId);
		}
		if (usernames != null && !usernames.equals("")) {
			activitiNodeCandidate.setCandidate_users_id(usernames);
			String[] usernameArry = usernames.split(",");
			String realName = "";
			for (int i = 0; i < usernameArry.length; i++) {
				if (realName.equals("")) {
					realName = activitiConfigService
							.getRealNameByName(usernameArry[i]);
				} else {
					realName += ","
							+ activitiConfigService
									.getRealNameByName(usernameArry[i]);
				}
			}
			activitiNodeCandidate.setCandidate_users_name(realName);
		}
		if (groups != null && !groups.equals("")) {
			activitiNodeCandidate.setCandidate_groups_id(groups);
			activitiNodeCandidate.setCandidate_groups_name(groups);
		}
		model.addAttribute("orgName", activitiConfigService.getOrgNameByOrgId(activitiNodeCandidate.getOrg_id()));
		model.addAttribute("nodeInfo", activitiConfigService
				.getActivitiNodeInfoById(activitiNodeCandidate.getNode_id()));
		model.addAttribute("activitiNodeCandidate", activitiNodeCandidate);
		return "path:nodeinfo";
	}*/

	/**
	 * 
	 * @param processKey 流程id
	 * @param taskKey 任务id
	 * @param bussinessId 业务id
	 * @param businesstypeid 业务类型id :
	 * organization 机构机构
	 * business 业务类别配置
	 * l
	 * 
	 * @param model
	 * @return
	 */
	public String showActivitiNodeInfo(String processKey, String taskKey,
			String business_id, String business_type, ModelMap model) {
	
		List<ActivitiNodeCandidate> list = activitiConfigService.queryActivitiNodeInfo(processKey);
		for(int i=0;i<list.size();i++){
			if(i==0){
				list.get(i).setCandidate_users_name("流程启动者");
				list.get(i).setCandidate_groups_name("流程启动者");
				continue;
			}
			ActivitiNodeCandidate activitiNodeCandidate = activitiConfigService.queryActivitiNodeCandidate(processKey, list.get(i).getNode_key(), business_id, business_type);
			if(activitiNodeCandidate!=null){
				list.get(i).setCandidate_groups_id(activitiNodeCandidate.getCandidate_groups_id());
				list.get(i).setCandidate_groups_name(activitiNodeCandidate.getCandidate_groups_name());
				list.get(i).setCandidate_users_id(activitiNodeCandidate.getCandidate_users_id());
				list.get(i).setCandidate_users_name(activitiNodeCandidate.getCandidate_users_name());
				list.get(i).setIs_edit_candidate(activitiNodeCandidate.getIs_edit_candidate());
				list.get(i).setIs_valid(activitiNodeCandidate.getIs_valid());
			}
			list.get(i).setBusiness_type(business_type);
			list.get(i).setBusiness_id(business_id);
			
		}
		List<Nodevariable> nodevariableList = activitiConfigService.selectNodevariable(processKey, business_id, business_type);
		/*List<Nodevariable> vrList = null;;
		try {
			vrList = activitiConfigService.loadVariableResource(processKey);
			model.addAttribute("vrList",vrList);
		} catch (ActivitiConfigException e) {
			e.printStackTrace();
		}*/
		//nodevariableList.addAll(vrList);
		model.addAttribute("nodevariableList", nodevariableList);
		model.addAttribute("activitiNodeCandidateList", list);
		model.addAttribute("business_id", business_id);
		model.addAttribute("business_type", business_type);
		model.addAttribute("process_key", processKey);
		List<ActivitiNodeCandidate> nodeInfoList = activitiConfigService.queryActivitiNodeInfo(processKey);
		model.addAttribute("nodeInfoList", nodeInfoList);
		if(nodevariableList==null||nodevariableList.isEmpty()){
			model.addAttribute("loadvariableresource","true");
		}else{
			model.addAttribute("loadvariableresource","false");
		}
		return "path:nodeinfo";
	}
	
	
	public String queryUsers(User user,
			@PagerParam(name = PagerParam.OFFSET) int offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ModelMap model) {
		if(user.getUser_realname()!=null&&!user.getUser_realname().equals("")){
			user.setUser_realname("%"+user.getUser_realname()+"%");
		}
		if(user.getUser_name()!=null&&!user.getUser_name().equals("")){
			user.setUser_name("%"+user.getUser_name()+"%");
		}
		if(user.getUser_worknumber()!=null&&!user.getUser_worknumber().equals("")){
			user.setUser_worknumber("%"+user.getUser_worknumber()+"%");
		}
		List<User> userList = activitiConfigService.queryUsers(user);
		model.addAttribute("userList", userList);
		return "path:userlist";
	}
	
	/**
	 * 根据用户名查询用户
	 * @param usernames
	 * @param model
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public String queryUserByNames(
			String usernames,
			ModelMap model,
			@PagerParam(name = PagerParam.OFFSET) int offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "3") int pagesize) {
		if (usernames != null && !usernames.equals("")) {
			List<User>  list = activitiConfigService.queryUsersByNames(usernames);
			model.addAttribute("chooseuserlist", list);
		}
		return "path:chooseuserlist";
	}
	
	/**
	 * 保存节点待办配置
	 * @param activitiNodeCandidate
	 * @return
	 *//*
	public @ResponseBody(datatype = "json")
	String editActivitiNode(ActivitiNodeCandidate activitiNodeCandidate) {
		activitiNodeCandidate.setCreate_date(new Timestamp(System
				.currentTimeMillis()));
		activitiNodeCandidate.setCreate_person_id("admin");
		activitiNodeCandidate.setCreate_person_name("admin");
		activitiNodeCandidate.setOwner_type("0");
		activitiConfigService.addActivitiNodeCandidate(activitiNodeCandidate);
		return "success";
	}*/
	
	public  @ResponseBody String updateActivitiNodeCandidate(List<ActivitiNodeCandidate> activitiNodeCandidateList){
			try {
				activitiConfigService.addActivitiNodeCandidate(activitiNodeCandidateList);
				return "流程处理人/组保存成功.";
			} catch (ActivitiConfigException e) {
				return "流程处理人/组保存失败：" + StringUtil.formatException(e);
			}
		
	}
	
	public static class node
	{
		private String[] a;
		private String[] b;
		private String[] c;
		private String aa;
		public String[] getA() {
			return a;
		}
		public void setA(String[] a) {
			this.a = a;
		}
		public String[] getB() {
			return b;
		}
		public void setB(String[] b) {
			this.b = b;
		}
		public String[] getC() {
			return c;
		}
		public void setC(String[] c) {
			this.c = c;
		}
		public String getAa() {
			return aa;
		}
		public void setAa(String aa) {
			this.aa = aa;
		}
	}
	public static class node1
	{
		private String[] a;
		private String[] b;
		private String[] c;
		
		public String[] getA() {
			return a;
		}
		public void setA(String[] a) {
			this.a = a;
		}
		public String[] getB() {
			return b;
		}
		public void setB(String[] b) {
			this.b = b;
		}
		public String[] getC() {
			return c;
		}
		public void setC(String[] c) {
			this.c = c;
		}
		
	}
	public  @ResponseBody String test1(String[] a ,String[] b,String[] c,String aa){
		try {
			ObjectMapper mapper = new ObjectMapper();
			node n = mapper.readValue(aa,node.class);
//			JsonNode node = mapper.readTree(aa);
			
			return "流程处理人/组保存成功.";
		} catch (Exception e) {
			return "流程处理人/组保存失败：" + StringUtil.formatException(e);
		}
	
}
	
	public  @ResponseBody String test3(String aa){
		try {
			ObjectMapper mapper = new ObjectMapper();
			node1 n = mapper.readValue(aa,node1.class);
//			JsonNode node = mapper.readTree(aa);
			
			return "流程处理人/组保存成功.";
		} catch (Exception e) {
			return "流程处理人/组保存失败：" + StringUtil.formatException(e);
		}
	
}
	
	public @ResponseBody
	String submitNodeVariable(List<Nodevariable> nodevariableList,
			String business_id, String business_type, String process_key) {
		return activitiConfigService.saveNodevariable(nodevariableList,
				business_id, business_type, process_key);
	}
	
	/**
	 * 查询节点参数配置列表
	 * @param nodevariable
	 * @param model
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public String queryNodevariable(Nodevariable nodevariable, ModelMap model) {
		List<Nodevariable> nodevariableList = activitiConfigService
				.selectNodevariable(nodevariable.getProcessKey(),
						nodevariable.getBusiness_id(),
						nodevariable.getBusiness_type());
		model.addAttribute("nodevariablelist", nodevariableList);
		return "path:nodevariablelist";
	}

	/**
	 * 跳转到新增参数配置页面
	 * @param nodevariable
	 * @param model
	 * @return
	 */
	public String toAddNodevariable(Nodevariable nodevariable ,ModelMap model){
		List<ActivitiNodeCandidate> nodeInfoList = activitiConfigService.queryActivitiNodeInfo(nodevariable.getProcessKey());
		model.addAttribute("nodeInfoList", nodeInfoList);
		model.addAttribute("nodevariable", nodevariable);
		return "path:addnodevariable";
	}
	
	/**
	 * 跳转到修改参数配置页面
	 * @param nodevariable
	 * @param model
	 * @return
	 */
	public String toUpdateNodevariable(Nodevariable nodevariable ,ModelMap model){
		nodevariable = activitiConfigService.getNodevariableById(nodevariable);
		model.addAttribute("nodevariable", nodevariable);
		return "path:updatenodevariable";
	}
	
	public @ResponseBody(datatype = "json")
		String addNodevariable(Nodevariable nodevariable){
		String result= activitiConfigService.addNodevariable(nodevariable);
		return result;
	}
	
	public @ResponseBody(datatype = "json")
		String deleteNodevariable(Nodevariable nodevariable){
		try{
			activitiConfigService.deleteNodevariable(nodevariable);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 检查并返回当前访问控制
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return AccessControl
	 */
	private AccessControl checkAndGetAccessControl(HttpServletRequest request, HttpServletResponse response) {
		AccessControl accessControl = AccessControl.getInstance();
		return accessControl.checkAccess(request, response) ? accessControl : null;
	}

	
	private String getCurrentUserAccount(HttpServletRequest request, HttpServletResponse response) {
		AccessControl accessControl = checkAndGetAccessControl(request, response);
		return accessControl != null ? accessControl.getUserAccount() : null;
	}

	private String getCurrentUserName(HttpServletRequest request, HttpServletResponse response) {
		AccessControl accessControl = checkAndGetAccessControl(request, response);
		return accessControl != null ? accessControl.getUserName() : null;
	}
	
	/**
	 * 任务进行时 修改节点待办
	 * @param taskId
	 * @param orgId
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String runTaskConfigMain(String taskId,String processKey ,String orgId,String userRealName,
 String businesstypeid, ModelMap model) {
		ProcessDefinition processDefinition = activitiService
				.getProcessDefinitionByKey(processKey);
		String processInstanceId = null;
		Deployment deployment = activitiService
				.getDeploymentById(processDefinition.getDeploymentId());
		ActivityImpl act = null;
		Task task = null;
		String deploymentId = deployment.getId();
		List<ActivityImpl> aList = activitiService
				.getActivitImplListByProcessKey(processKey);
		if (taskId != null && !taskId.equals("")) {
			task = activitiService.getTaskById(taskId);
			processInstanceId = task.getProcessInstanceId();
			model.addAttribute("taskId", taskId);
		} else {
			taskId = null;
		}
		for (int i = 0; i < aList.size(); i++) {
			if (task != null) {
				if (aList.get(i).getId().equals(task.getTaskDefinitionKey())) {
					act = aList.get(i);
				}
			}
			if (!aList.get(i).getProperty("type").equals("userTask")) {
				aList.remove(i);
			}

		}

		/*********************************************/
		String business_id = null;
		String business_type = WorkFlowConstant.BUSINESS_TYPE_COMMON;

		if (orgId != null && !orgId.equals("")) {
			business_id = orgId;
			business_type = WorkFlowConstant.BUSINESS_TYPE_ORG;
		}
		if (businesstypeid != null && !businesstypeid.equals("")) {
			business_id = businesstypeid;
			business_type = WorkFlowConstant.BUSINESS_TYPE_BUSINESSTYPE;
		}
		List<ActivitiNodeCandidate> list = activitiConfigService
				.queryActivitiNodeInfo(processKey);
		for (int i = 0; i < list.size(); i++) {
			if (processInstanceId != null) {
				if(i==0){
					String initor = (String)activitiService
							.getRuntimeService().getVariable(processInstanceId,"initor");
					list.get(i).setCandidate_users_id(initor);
					list.get(i).setCandidate_users_name(activitiConfigService.getRealNameByName(initor));
					continue;
				}
				List<String> userList = (List<String>) activitiService
						.getRuntimeService().getVariable(processInstanceId,
								list.get(i).getNode_key() + "_users");
				List<String> groupList = (List<String>) activitiService
						.getRuntimeService().getVariable(processInstanceId,
								list.get(i).getNode_key() + "_groups");
				String users = "";
				String realUsername = "";
				String groups = "";
				for (int u = 0; userList!=null&&u < userList.size(); u++) {
					if (u == 0) {
						users += userList.get(u);
						realUsername += activitiConfigService
								.getRealNameByName(userList.get(u));
					} else {
						users += "," + userList.get(u);
						realUsername += ","
								+ activitiConfigService
										.getRealNameByName(userList.get(u));
					}
				}
				for (int g = 0; groupList!=null&&g < groupList.size(); g++) {
					if (g == 0) {
						groups += groupList.get(g);
					} else {
						groups += "," + groupList.get(g);
					}
				}
				list.get(i).setCandidate_users_id(users);
				list.get(i).setCandidate_users_name(realUsername);
				list.get(i).setCandidate_groups_id(groups);
				list.get(i).setCandidate_groups_name(groups);
			} else {
				if(i==0){
					list.get(i).setCandidate_groups_id("");
					list.get(i).setCandidate_groups_name("");
					//list.get(i).setCandidate_users_id(getCurrentUserAccount(request,response));
					list.get(i).setCandidate_users_name(userRealName);
					list.get(i).setIs_edit_candidate(1);
				}else{
					ActivitiNodeCandidate activitiNodeCandidate = activitiConfigService
					.queryActivitiNodeCandidate(processKey, list.get(i)
							.getNode_key(), business_id, business_type);
					if (activitiNodeCandidate != null) {
							list.get(i).setCandidate_groups_id(
									activitiNodeCandidate.getCandidate_groups_id());
							list.get(i).setCandidate_groups_name(
									activitiNodeCandidate.getCandidate_groups_name());
							list.get(i).setCandidate_users_id(
									activitiNodeCandidate.getCandidate_users_id());
							list.get(i).setCandidate_users_name(
									activitiNodeCandidate.getCandidate_users_name());
							list.get(i).setIs_edit_candidate(
									activitiNodeCandidate.getIs_edit_candidate());
					}
				}
				
				list.get(i).setBusiness_type(business_type);
				list.get(i).setBusiness_id(business_id);

			}
		}
		List<Nodevariable> nodevariableList = activitiConfigService
				.selectNodevariable(processKey, business_id, business_type);
		if (processInstanceId != null) {
			for(int i =0 ;i<nodevariableList.size();i++){
				String param_value = (String)activitiService
						.getRuntimeService().getVariable(processInstanceId,nodevariableList.get(i).getParam_name());
				if(param_value!=null&&!param_value.equals("")){
					nodevariableList.get(i).setParam_value(param_value);
				}
			}
		}
		model.addAttribute("nodevariableList", nodevariableList);
		model.addAttribute("activitiNodeCandidateList", list);
		model.addAttribute("business_id", business_id);
		model.addAttribute("business_type", business_type);
		model.addAttribute("process_key", processKey);
		/*********************************************/

		model.addAttribute("processDef",
				activitiService.getProcessDefByDeploymentId(deploymentId));
		model.addAttribute("nodeListInfo", list);
		model.addAttribute("orgId", orgId);
		model.addAttribute("aList", aList);
		model.addAttribute("act", act);
		model.addAttribute("processKey", processKey);
		return "path:runmain";
	}
	
	
	/**
	 * 查询流程节点信息列表
	 * @param processKey
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public String selectNodeInfo(String processKey,ModelMap model){
		List<ActivitiNodeCandidate> list = activitiConfigService.selectNodeInfo(processKey);
		model.addAttribute("nodeListInfo", list);
		return "path:nodeinfolist";
		
	}
	
	/**
	 * 跳转到待办用户选择页面
	 * @param users
	 * @param nodeinfoId
	 * @param nodeCandidateId
	 * @param model
	 * @return
	 */
	public String toChooseUserPage(String users,String node_key,String user_realnames,
			ModelMap model) {
		if (users != null && !users.equals("")) {
			List<User> list = activitiConfigService.queryUsersByNames(users);
			model.addAttribute("chooseuserlist", list);
		}
		model.addAttribute("usernames", users);
		model.addAttribute("user_realnames", user_realnames);
		model.addAttribute("node_key", node_key);
		return "path:chooseusers";
	}

	/**
	 * 跳转到待办组选择页面
	 * @param groups
	 * @param nodeinfoId
	 * @param model
	 * @return
	 */
	public String toChooseGroupPage(Group group,String groups,String node_key,String group_realnames,
			ModelMap model) {
		List<Group> groupList =activitiConfigService.queryGroups(group);
		List<Group> chooseGroupList = activitiConfigService.getGroupInfoByNames(groups);
		for(int i=0;i<groupList.size();i++){
			for(Group chooseGroup:chooseGroupList){
				if(groupList.get(i).getGroup_name().equals(chooseGroup.getGroup_name())){
					groupList.remove(groupList.get(i));
					continue;
				}
			}
		}
		model.addAttribute("grouplist", groupList);
		model.addAttribute("chooseGroupList", chooseGroupList);
		model.addAttribute("groups", groups);
		model.addAttribute("node_key", node_key);
		model.addAttribute("group_realnames", group_realnames);
		return "path:choosegroups";
	}
	
	public String runqueryUsers(User user,
			@PagerParam(name = PagerParam.OFFSET) int offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ModelMap model) {
		if(user.getUser_realname()!=null&&!user.getUser_realname().equals("")){
			user.setUser_realname("%"+user.getUser_realname()+"%");
		}
		if(user.getUser_name()!=null&&!user.getUser_name().equals("")){
			user.setUser_name("%"+user.getUser_name()+"%");
		}
		if(user.getUser_worknumber()!=null&&!user.getUser_worknumber().equals("")){
			user.setUser_worknumber("%"+user.getUser_worknumber()+"%");
		}
		List<User> userList = activitiConfigService.queryUsers(user);
		model.addAttribute("userList", userList);
		return "path:runuserlist";
	}
	
	public String loadVariableResource(String isClear,String processKey,String business_id,String business_type,ModelMap model){
		try{
			List<Nodevariable> vrList = null;
			vrList = activitiConfigService .loadVariableResource(processKey);
			if(isClear!=null&&isClear.equals("true")){
				model.addAttribute("nodevariablelist", vrList);
			}else{
				List<Nodevariable> nodevariableList = activitiConfigService.selectNodevariable(processKey, business_id, business_type);
				nodevariableList.addAll(vrList);
				model.addAttribute("nodevariablelist", nodevariableList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return"path:nodevariablelist";
	}
	
}