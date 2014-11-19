package com.sany.workflow.service.impl;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.transaction.RollbackException;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.frameworkset.spi.SOAApplicationContext;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.Group;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.OrganizationDTO;
import com.sany.workflow.entity.ProBusinessType;
import com.sany.workflow.entity.User;
import com.sany.workflow.entity.VariableResource;
import com.sany.workflow.service.ActivitiConfigException;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.util.WorkFlowConstant;

public class ActivitiConfigServiceImpl implements ActivitiConfigService {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;
	
	private ActivitiService activitiService;
	
	public List<OrganizationDTO> getOrgsByParentId(String parentId) {
		try {
			List<OrganizationDTO> orgList = executor.queryList(
					OrganizationDTO.class, "selectOrgByParentId", parentId);
			return orgList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询节点配置信息
	 * @param processKey 流程KEY
	 * @param taskKey 任务key
	 * @return
	 */
	public ActivitiNodeInfo getActivitiNodeByKeys(String processKey,String taskKey){
		try{
			ActivitiNodeInfo node = new ActivitiNodeInfo();
			node.setProcess_key(processKey);
			node.setNode_key(taskKey);
			List<ActivitiNodeInfo> list = executor.queryListBean(
					ActivitiNodeInfo.class, "selectActivitiNodeInfoByKeys", node);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询节点待办信息
	 * @param nodeId
	 * @return
	 *//*
	public ActivitiNodeCandidate getActivitiNodeCandidateByNodeId(
			String nodeId, String orgId) {
		try {
			ActivitiNodeCandidate activitiNodeCandidate = new ActivitiNodeCandidate();
			activitiNodeCandidate.setNode_id(nodeId);
			activitiNodeCandidate.setOrg_id(orgId);
			List<ActivitiNodeCandidate> list = executor.queryListBean(
					ActivitiNodeCandidate.class,
					"selectActivitiNodeCandidateByNodeId",
					activitiNodeCandidate);
			if (list != null && list.size() > 0) {
				activitiNodeCandidate = list.get(0);
				return activitiNodeCandidate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	/**
	 * 根据组织机构ID查询组织机构名称
	 * @param orgId
	 * @return
	 */
	public String getOrgNameByOrgId(String orgId){
		try{
			return executor.queryField(
					"selectOrgNameByOrgId",
					orgId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询流程的待办配置
	 * @param activitiNodeCandidate
	 * @return
	 */
	public List<ActivitiNodeCandidate> queryActivitiNodeCandidate(ActivitiNodeCandidate activitiNodeCandidate){
		try{
			List<ActivitiNodeCandidate> list = executor.queryListBean(ActivitiNodeCandidate.class, "queryActivitiNodeCandidate", activitiNodeCandidate);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据查询条件查询用户列表
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<User> queryUsers(User user){
		try{
			List<User> userListInfo = executor.queryListBean(User.class, "selectUsersByCondition", user);
			return userListInfo;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据查询条件查询用户列表
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<User> queryUsersForPage(User user, long offset, int pagesize) {
		try {
			ListInfo listInfo = executor.queryListInfoBean(User.class,
					"selectUsersByCondition", offset, pagesize, user);
			return listInfo.getDatas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据查询条件查询用户列表和下级部门列表
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<User> queryUsersAndOrgToJson(User user, long offset,
			int pagesize) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();
			
			List<User> userOrgList = new ArrayList<User>();

			// 用户列表
			ListInfo listInfo = executor.queryListInfoBean(User.class,
					"selectUsersByCondition", offset, pagesize, user);
			
			if (null != listInfo.getDatas()) {
				userOrgList.addAll(listInfo.getDatas());
			}
			
			// 部门列表
			if (StringUtil.isNotEmpty(user.getOrg_id())) {
				List<User> orgList = executor.queryList(User.class,
						"selectOrgsByCondition", user.getOrg_id(),
						user.getOrg_id());

				userOrgList.addAll(orgList);
			}

			tm.commit();
			return userOrgList;
			
		} catch (Exception e) {
			throw new Exception ("查询数据列表出错:"+e);
		} finally {
			tm.release();
		}
	}
	
	/**
	 * 根据查询条件查询用户列表
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public User getUserInfo(String userName) throws Exception{
		return executor.queryObject(User.class, "selectUsersByUserName_wf", userName);
	}
	
	/**
	 * 根据一组用户名查询User
	 * @param usernames 用户名以","隔开
	 * @return
	 */
	public List<User> queryUsersByNames(String usernames) {
		try {
			String[] names = usernames.split(",");
			List<User>  list = null;
			if (names.length > 0) {
				Map<String, String[]> usernames_ = new HashMap<String, String[]>();
				usernames_.put("usernames", names);
				list = executor.queryListBean(User.class, "selectUserInfoByNames", usernames_);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据一组用户名查询CandidateGroup
	 * @param groups 用户名以","隔开
	 * @return
	 */
	public List<Group> getGroupInfoByNames(String groups) {
		try {
			String[] groupnames = groups.split(",");
			List<Group> list = null;
			if (groupnames.length > 0) {
				Map<String, String[]> groups_ = new HashMap<String, String[]>();
				groups_.put("groups", groupnames);
				list = executor.queryListBean(Group.class,
						"selectGroupInfoByNames",groups_);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据条件查询用户组分页列表
	 * @param group 
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<Group> queryGroups(Group group){
		try{
			if(group.getGroup_name()!=null&&!group.getGroup_name().equals("")){
				group.setGroup_name("%"+group.getGroup_name()+"%");
			}
			if(group.getGroup_desc()!=null&&!group.getGroup_desc().equals("")){
				group.setGroup_desc("%"+group.getGroup_desc()+"%");
			}
			if(group.getUser_name()!=null&&!group.getUser_name().equals("")){
				group.setUser_name("%"+group.getUser_name()+"%");
			}
			List<Group> list = executor.queryListBean(Group.class, "queryGroup", group);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询节点配置信息
	 * @param id 节点配置ID
	 * @return
	 */
	public ActivitiNodeCandidate getActivitiNodeCandidateById(String id){
		try{
			ActivitiNodeCandidate node = new ActivitiNodeCandidate();
			node.setId(id);
			List<ActivitiNodeCandidate> list = executor.queryListBean(
					ActivitiNodeCandidate.class, "selectActivitiNodeCandidateById", node);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询节点信息
	 * @param id 节点ID
	 * @return
	 */
	public ActivitiNodeInfo getActivitiNodeInfoById(String id){
		try{
			ActivitiNodeInfo node = new ActivitiNodeInfo();
			node.setId(id);
			List<ActivitiNodeInfo> list = executor.queryListBean(
					ActivitiNodeInfo.class, "selectActivitiNodeInfoById", node);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据登陆名查询用户真实姓名
	 * @param username 用户登陆名
	 * @return
	 */
	public String getRealNameByName(String username){
		try{
			return executor.queryField("selectUserRealNameByName", username);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String getGroupName(String group){
		try{
			return executor.queryField("selectUserRealNameByName", group);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void deleteActivitiNodeInfo(String processKey) throws ActivitiConfigException
	{
		TransactionManager tm = new TransactionManager();
		
		try {
			tm.begin();
			Map param = new HashMap();
			param.put("process_key", processKey);
			executor.deleteBean("deleteActivitiNodeCandidataByKey", param);
			executor.deleteBean("deleteNodevariableByKey", param);
			executor.deleteBean("deleteActivitiNodeInfoByKey", param);
			executor.deleteBean("deleteProBusinessByKey", param);
			
			executor.deleteBean("deleteProcMesstemplateBykey", param);
			executor.deleteBean("deleteNodeWorktimeBykey", param);
			executor.deleteBean("deleteNodeChangeInfoBykey", param);
			executor.deleteBean("deleteEntrustTaskBykey", param);
			executor.deleteBean("deleteDealTaskBykey", param);			
			
			tm.commit();
		} catch (Exception e) {
			try {
				tm.rollback();
				throw new ActivitiConfigException(e);
			} catch (RollbackException e1) {
				throw new ActivitiConfigException(e);
			}
		}
	}
	/**
	 * 保存流程节点基本信息，清除重置待办人和变量
	 * @param processKey
	 */
	public void addActivitiNodeInfo(String processKey) throws ActivitiConfigException{
		TransactionManager tm = new TransactionManager();
		
		try {
			tm.begin();
			Map param = new HashMap();
			param.put("process_key", processKey);
			executor.deleteBean("deleteActivitiNodeCandidataByKey", param);
			executor.deleteBean("deleteNodevariableByKey", param);
			executor.deleteBean("deleteActivitiNodeInfoByKey", param);
			List<ActivityImpl> aList = activitiService
					.getActivitImplListByProcessKey(processKey);
			for(int i=0;i<aList.size();i++){
				ActivitiNodeInfo aNode = new ActivitiNodeInfo();
				aNode.setProcess_key(processKey);
				ActivityImpl actImpl = aList.get(i);
				String node_type = actImpl.getProperty("type").toString();
//				if (actImpl.getProperty("type").toString().equals("userTask")) {
//					
//				}
				aNode.setNode_type(node_type);
				aNode.setId(java.util.UUID.randomUUID().toString());
				aNode.setNode_key(actImpl.getId());
				aNode.setNode_name(actImpl.getProperty("name").toString());
				aNode.setOrder_num(i);
				executor.insertBean("insertActivitiNodeInfo", aNode);
			}
			tm.commit();
		} catch (Exception e) {
			try {
				tm.rollback();
				throw new ActivitiConfigException(e);
			} catch (RollbackException e1) {
				throw new ActivitiConfigException(e);
			}
		}
	}
	
	/**
	 * 部署流程时，更新旧版流程节点基本信息，保留原有节点的待办人和变量
	 * @param processKey
	 */
	public void updateActivitiNodeInfo(String processKey,int deploypolicy) throws ActivitiConfigException{
		TransactionManager tm = new TransactionManager();
//		ActivitiNodeInfo aNode = new ActivitiNodeInfo();
//		aNode.setProcess_key(processKey);
		try {
			tm.begin();
			
			
//			executor.deleteBean("deleteActivitiNodeCandidataByKey", aNode);
//			executor.deleteBean("deleteNodevariableByKey", aNode);
			
			List<ActivityImpl> aList = activitiService
					.getActivitImplListByProcessKey(processKey);
			List<String> nodekey = new ArrayList<String>();//节点新的nodekey,根据新key清除旧key的配置信息
			
			for(int i=0;i<aList.size();i++){//添加新的节点信息
				ActivityImpl actImpl = aList.get(i);
				String type = actImpl.getProperty("type").toString();
//				if (actImpl.getProperty("type").toString().equals("userTask")) {
//					
//				}
				String id = executor.queryObject(String.class, "existNodeinfo",processKey,actImpl.getId());
				if(StringUtil.isEmpty(id))
				{
					ActivitiNodeInfo aNode = new ActivitiNodeInfo();
					aNode.setProcess_key(processKey);
					aNode.setNode_type(type);
					aNode.setId(java.util.UUID.randomUUID().toString());
					aNode.setNode_key(actImpl.getId());					
					aNode.setNode_name(actImpl.getProperty("name").toString());
					aNode.setOrder_num(i);
					executor.insertBean("insertActivitiNodeInfo", aNode);
					
				}else {
					
					ActivitiNodeInfo aNode = new ActivitiNodeInfo();
					aNode.setId(id);
					aNode.setProcess_key(processKey);
					aNode.setNode_type(type);
					aNode.setNode_key(actImpl.getId());					
					aNode.setNode_name(actImpl.getProperty("name").toString());
					aNode.setOrder_num(i);
					executor.insertBean("updateActivitiNodeInfo", aNode);
					
				}
				nodekey.add(actImpl.getId());
			}
			if(deploypolicy != DeploymentBuilder.Deploy_policy_default )
			{
				Map deleteparams = new HashMap();
				deleteparams.put("nodekey", nodekey);
				deleteparams.put("process_key", processKey);
				
				
				/**
				 * 清理垃圾数据
				 */
				executor.deleteBean("deleteNotexistNodevariableByKey", deleteparams);
				executor.deleteBean("deleteNotexistActivitiNodeCandidataByKey", deleteparams);
			}
			
			
			
			tm.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
				throw new ActivitiConfigException(e);
			} catch (RollbackException e1) {
				throw new ActivitiConfigException(e);
			}
			
		}
	}
	
	/**
	 * 保存节点待办信息
	 * @param activitiNodeCandidate
	 */
	public void addActivitiNodeCandidate(List<ActivitiNodeCandidate> activitiNodeCandidates)  throws ActivitiConfigException{
		TransactionManager tm = new TransactionManager();
		try{
			tm.begin();
			if(activitiNodeCandidates != null && activitiNodeCandidates.size() > 0)
			{
				String processKey = activitiNodeCandidates.get(activitiNodeCandidates.size() -1).getProcess_key();
				String bussinesstype = activitiNodeCandidates.get(activitiNodeCandidates.size() -1).getBusiness_type();
				String bussinessId = activitiNodeCandidates.get(activitiNodeCandidates.size() -1).getBusiness_id();
				Map<String,String> params = new HashMap<String,String>();
				params.put("business_type", bussinesstype);
				params.put("business_id", bussinessId);
				params.put("process_key", processKey);
				executor.deleteBean("deleteActivitiNodeCandidate", params);
				for(ActivitiNodeCandidate activitiNodeCandidate: activitiNodeCandidates)
				{
					activitiNodeCandidate.setId(java.util.UUID.randomUUID().toString());
//					if (!StringUtil.isEmpty(activitiNodeCandidate.getDuration_node())) {
						
						// 转毫秒值
//						double duration_node = Double.parseDouble(activitiNodeCandidate.getDuration_node());
//						activitiNodeCandidate.setDuration_node(activitiNodeCandidate.getDuration_node()*60*60*1000);
//					}
				}
				executor.insertBeans("insertActivitiNodeCandidate", activitiNodeCandidates);
			}
			tm.commit();
		}catch(Throwable e){
			
			throw new ActivitiConfigException(e);
		}
		finally
		{
			tm.release();
		}
	}
	
	/**
	 * 查询节点参数配置列表
	 * @param nodevariable
	 * @return
	 */
	public List<Nodevariable> queryNodevariable(Nodevariable nodevariable){
		try {
			
			List<Nodevariable> list = executor.queryListBean(Nodevariable.class, "queryNodevariable", nodevariable);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 查询节点参数配置对象
	 * @param nodevariable
	 * @return
	 */
	public Nodevariable getNodevariableById(Nodevariable nodevariable){
		return getNodevariableById(nodevariable.getId());
	}
	
	public Nodevariable getNodevariableById(String variableid)
	{
		try {
			Nodevariable nodevariable = executor.queryObject(Nodevariable.class, "selectNodevariableById", variableid);
			return nodevariable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 新增节点参数配置
	 * @param nodevariable
	 */
	public String addNodevariable(Nodevariable nodevariable){
		try {
			if(nodevariable.getId()!=null&&!nodevariable.getId().equals("")){
				executor.updateBean("updateNodeVariable", nodevariable);
				return "success";
			}
			List<Nodevariable> list = executor.queryListBean(Nodevariable.class, "queryNodevariableByParamName", nodevariable);
			if(list!=null&&list.size()>0){
				nodevariable.setId(list.get(0).getId());
				this.updateNodevariableParamvalue(nodevariable);
			}
			nodevariable.setId(java.util.UUID.randomUUID().toString());
			executor.insertBean("addNodevariable", nodevariable);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@Override
	public String saveNodevariable(List<Nodevariable> nodevariableList,String business_id,String business_type,String process_key){
		TransactionManager tm = new TransactionManager();
		try{
//			for(int ii=0;nodevariableList != null && ii<nodevariableList.size();ii++){
//				Nodevariable b = nodevariableList.get(ii);
//				b.setRowno_(ii);
//				if(b!=null&&b.getNode_id()!=null){
//					if(b.getParam_name()==null||b.getParam_name().isEmpty()){
//						
//						return "参数名称不能为空";
//					}
//
//				}
//			}
			tm.begin();
			

			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", process_key);
			params.put("business_id", business_id);
			params.put("business_type", business_type);
			
			executor.deleteBean("batchDeleteNodeVariable", params);
			
			for(int ii=0;ii<nodevariableList.size();ii++){
				Nodevariable b = nodevariableList.get(ii);
				if(b!=null&&b.getNode_id()!=null){
					if(b.getParam_name()==null||b.getParam_name().isEmpty()){
						tm.rollback();
						return "参数名称不能为空";
					}
//					for(int i=0;i<nodevariableList.size();i++){
//						if(ii!=i&&b.getParam_name().equals(nodevariableList.get(i).getParam_name())){
//							tm.rollback();
//							return "存在重复的参数名称";
//						}
//					}
					b.setId(UUID.randomUUID().toString());
					b.setBusiness_id(business_id);
					b.setBusiness_type(business_type);
					executor.insertBean("addNodevariable", b);
				}
			}
			tm.commit();
			return "参数配置保存成功";
		}catch(Exception e){
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			
			return StringUtil.exceptionToString(e);
		}
		
	}
	
	/**
	 * 修改节点参数配置
	 * @param nodevariable
	 */
	public void updateNodevariableParamvalue(Nodevariable nodevariable){
		try {
			executor.updateBean("updateNodevariable", nodevariable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除节点参数配置
	 * @param nodevariable
	 */
	public void deleteNodevariable(Nodevariable nodevariable) throws Exception{
		executor.deleteBean("deleteNodeVariable", nodevariable);
	}
	
	/**
	 * 查询流程节点信息列表
	 * @param processKey
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<ActivitiNodeCandidate> selectNodeInfo(String processKey) {
		try {
			ActivitiNodeCandidate activitiNodeCandidate = new ActivitiNodeCandidate();
			activitiNodeCandidate.setProcess_key(processKey);
			List<ActivitiNodeCandidate> list = executor.queryListBean(
					ActivitiNodeCandidate.class, "queryActivitiNodeCandidate",
					activitiNodeCandidate);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ActivitiNodeCandidate> queryActivitiNodeCandidate(
			String process_key, String business_id, String business_type) {
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", process_key);
			params.put("business_id", business_id);
			params.put("business_type", business_type);
			List<ActivitiNodeCandidate> list = executor.queryListBean(ActivitiNodeCandidate.class, "queryActivitiNodeCandidate", params);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ActivitiNodeCandidate> queryActivitiNodeCandidate(
			String process_key) {
		return queryActivitiNodeCandidate(process_key,null,null);
	}
	
	@Override
	public List<ActivitiNodeCandidate> queryActivitiNodeInfo(String process_key){
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", process_key);
			params.put("business_type", "0");
			List<ActivitiNodeCandidate> list = executor.queryListBean(ActivitiNodeCandidate.class, "queryActivitiNodeCandidate", params);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<ActivitiNodeInfo> queryAllActivitiNodeInfo(String process_key) {
		try {

			List<ActivitiNodeInfo> list = executor.queryList(ActivitiNodeInfo.class,
					"queryAllActivitiNodes", process_key);
			
			if (list != null && list.size() > 0) {
				List<ActivityImpl> activties = activitiService
						.getActivitImplListByProcessKey(process_key);

				for (int i = 0; i < list.size(); i++) {
					ActivitiNodeInfo nodeInfo = list.get(i);
					
					for (ActivityImpl activtie : activties) {
						if (activtie.getId().equals(nodeInfo.getNode_key())) {

							if (activtie.isMultiTask()) {
								nodeInfo.setIS_MULTI_DEFAULT(1);
							} else {
								nodeInfo.setIS_MULTI_DEFAULT(0);
							}
							
							//邮件任务
							if (activtie.isMailTask()) {
								nodeInfo.setNode_type("mailTask");
								nodeInfo.setNodeTypeName("邮件任务");
							}
							
							break;
						}
					}
				}
			}
			return list;
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	/**
	 * 获取给定流程所有节点信息已经每个节点对应的业务处理人信息
	 * @param bussinessType
	 * @param bussinessid
	 * @param process_key
	 * @return
	 */
	@Override
	public List<ActivitiNodeCandidate> queryActivitiNodesCandidates(String bussinessType,String bussinessid,String process_key){
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", process_key);
			params.put("business_type", bussinessType);
			params.put("bussinessid", bussinessid);
			List<ActivitiNodeCandidate> list = executor.queryListBean(ActivitiNodeCandidate.class, "queryProcessNodesCandidates", params);
			
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/*@Overridebbbb
	public ActivitiNodeCandidate queryActivitiNodeCandidate(String process_key,
			String activityKey) {
		return queryActivitiNodeCandidate(process_key,activityKey,null,null);
	}*/

	@Override
	public ActivitiNodeCandidate queryActivitiNodeCandidate(String process_key,
			String activityKey, String business_id, String business_type) {
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", process_key);
			params.put("node_key", activityKey);
			params.put("business_id", business_id);
			params.put("business_type", business_type);
			ActivitiNodeCandidate activitiNodeCandidate= executor.queryObjectBean(ActivitiNodeCandidate.class, "queryProcessNodeCandidates", params);
			return activitiNodeCandidate;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Nodevariable> selectNodevariable(String processKey) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", processKey);
			List<Nodevariable> listInfo = executor.queryListBean(Nodevariable.class, "queryNodevariable", params);
			return listInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Nodevariable> selectNodevariable(String processKey,
			String activityKey) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", processKey);
			params.put("node_key", activityKey);
			List<Nodevariable> listInfo = executor.queryListBean(Nodevariable.class, "queryNodevariable", params);
			return listInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Nodevariable> selectNodevariable(String processKey, String business_id,
			String business_type) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", processKey);
			params.put("business_id", business_id);
			params.put("business_type", business_type);
			List<Nodevariable> listInfo = executor.queryListBean(Nodevariable.class, "queryNodevariable", params);
			return listInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Nodevariable> selectNodevariable(String processKey,
			String activityKey, String business_id, String business_type) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("process_key", processKey);
			params.put("node_key", activityKey);
			params.put("business_id", business_id);
			params.put("business_type", business_type);
			List<Nodevariable> listInfo = executor.queryListBean(
					Nodevariable.class, "queryNodevariable", params);
			return listInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ActivitiNodeCandidate> queryCandidataVariable(String processKey,String processInstanceId){
		List<ActivitiNodeCandidate> list = this.queryActivitiNodeInfo(processKey);
		for(int i=0;i<list.size();i++){
			@SuppressWarnings("unchecked")
			List<String> userList = (List<String>)activitiService.getRuntimeService().getVariable(processInstanceId, list.get(i).getNode_key()+"_users");
			String users = "";
			for(int u=0;u<userList.size();u++){
				if(u==0){
					users += userList.get(u);
				}else{
					users += ","+userList.get(u);
				}
			}
			list.get(i).setCandidate_users_name(users);
		}
		return list;
	}
	
	@Override
	public String addNodeParams(InputStream paramFileInputStream,String process_key)
			throws ActivitiConfigException {
		TransactionManager tm = new TransactionManager();
		try {
			SOAApplicationContext file = new SOAApplicationContext(
					paramFileInputStream);
			Set<String> keys = file.getPropertyKeys();
			if (keys != null && keys.size() > 0) {
				Iterator<String> it = keys.iterator();
				tm.begin();
				while (it.hasNext()) {
					String key = it.next();
					Pro pro = file.getProBean(key);
					String node_key = pro.getStringExtendAttribute("node_key");
					String param_value = pro.getString();
					boolean readonly = pro.getBooleanExtendAttribute(
							"readonly", false);
					String description = pro.getDescription();
					
					if (process_key == null || process_key.equals("")) {
						return "参数配置:参数所属流程-process_key为空";
					}
					if (node_key == null || node_key.equals("")) {
						return "参数配置:参数所属环节-node_key为空";
					}
					String node_id = executor.queryField(
							"selectActivitiNodeInfoIDByNodeKey",
							pro.getStringExtendAttribute("process_key"),
							pro.getStringExtendAttribute("node_key"));
					
					if (node_id == null || node_id.isEmpty()) {
						return "参数配置:" + key + " 所属环节"+node_key+"不存在";
					}
					
					VariableResource variableResource = new VariableResource();
					variableResource.setNode_id(node_id);
					if(readonly){
						variableResource.setIs_edit_param(WorkFlowConstant.PARAM_READONLY);
					}else{
						variableResource.setIs_edit_param(WorkFlowConstant.PARAM_EDIT);
					}
					variableResource.setParam_name(key);
					variableResource.setParam_value(param_value);
					variableResource.setParam_des(description);
					executor.deleteBean("deleteNodevariableresource", variableResource);
					variableResource.setId(UUID.randomUUID().toString());
					executor.insertBean("addNodevariableresource", variableResource);
				}
				tm.commit();
			}
		} catch (Throwable e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			return e.getMessage();
		}
		return "success";
	}
	
	@Override
	public List<Nodevariable> loadVariableResource(String process_key) throws ActivitiConfigException{
		try{
			if(process_key==null||process_key.equals("")){
				throw new ActivitiConfigException("加载参数配置资源异常:参数process_key为空");
			}
			VariableResource vr = new VariableResource();
			vr.setProcess_key(process_key);
			return executor.queryListBean(Nodevariable.class, "selectNodevariableresource", vr);
		}catch(Exception e){
			throw new ActivitiConfigException("加载参数配置资源异常"+e.getMessage());
		}
	}
	
	@Override
	public void addNodeVariableFromResource(String resourceId,String businessId,String businessType){
		try{
			if(resourceId!=null&&!resourceId.equals("ALL")){
				VariableResource vr = null;
				List<VariableResource> vrList = executor.queryList(VariableResource.class, "selectNodevariableresourceById", resourceId);
				if(vrList!=null&&vrList.size()>0){
					vr = vrList.get(0);
					Nodevariable nodevariable = new Nodevariable();
					nodevariable.setId(UUID.randomUUID().toString());
					nodevariable.setBusiness_id(businessId);
					nodevariable.setBusiness_type(businessType);
					nodevariable.setNode_id(vr.getNode_id());
					nodevariable.setParam_name(vr.getParam_name());
					nodevariable.setParam_value(vr.getParam_value());
					nodevariable.setParam_des(vr.getParam_des());
					nodevariable.setIs_edit_param(vr.getIs_edit_param());
					this.addNodevariable(nodevariable);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void addProBusinessType(String processKey,String businessTypeId) throws ActivitiConfigException{
		try{
			if(businessTypeId == null || businessTypeId.equals(""))
			{
				throw new ActivitiConfigException("保存流程业务类型失败：processKey="+processKey+",没有指定businessTypeId.");
			}
			ProBusinessType bpt = new ProBusinessType();
			bpt.setBusinessType_id(businessTypeId);
			bpt.setProcess_key(processKey);
			String id = executor.queryFieldBean("queryProBusinessTypeByProKey", bpt);
			if(id!=null&&!id.isEmpty()){
				executor.updateBean("updateProBusinessType", bpt);
			}else{
				bpt.setId(UUID.randomUUID().toString());
				executor.insertBean("insertProBusinessType", bpt);
			}
		}catch(Exception e){
			throw new ActivitiConfigException(e);
		}
	}
	
	@Override
	public String queryBusinessName(String process_key) throws ActivitiConfigException {
		try{
			String businessName = executor.queryField("queryBusinessNameByProKey", process_key);
			return businessName;
		}catch(Exception e){
			throw new ActivitiConfigException(e);
		}
	}

	@Override
	public List<Nodevariable> queryNodeVariable(String business_type,
			String business_id, String processKey) {
		
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("process_key", processKey);
			params.put("business_type", business_type);
			params.put("bussinessid", business_id);
			
			List<Nodevariable> list = executor.queryListBean(Nodevariable.class, "queryNodeVariable_wf", params);
			
			return list;
		}catch(Exception e){
			throw new ProcessException(e);
		}
	}

	@Override
	public Map queryMessageTempleById(String processKey) {

		try {
			return (HashMap)executor.queryObject(HashMap.class,
					"getMessTamplateById_wf", processKey);

		} catch (SQLException e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public List<NodeControlParam> queryAllActivitiNodes(String processKey) {
		try {
			return executor.queryList(NodeControlParam.class,
					"queryNodes", processKey);
		} catch (SQLException e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public String saveNodeContralParam(
			List<NodeControlParam> nodeControlParamList, String business_id,
			String business_type, String process_key) {
		
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			Map<String, String> params = new HashMap<String, String>();
			params.put("process_key", process_key);
			params.put("business_id", business_id);
			params.put("business_type", business_type);

			executor.deleteBean("deleteNodeContralParam", params);

			for (int i = 0; i < nodeControlParamList.size(); i++) {
				NodeControlParam nodeControlParam = nodeControlParamList.get(i);

				nodeControlParam.setID(UUID.randomUUID().toString());
				nodeControlParam.setBUSINESS_ID(business_id);
				nodeControlParam.setBUSINESS_TYPE(business_type);
				nodeControlParam.setDURATION_NODE(nodeControlParam
						.getDURATION_NODE() * 60 * 60 * 1000);
			}

			executor.insertBeans("addNodeControlParam", nodeControlParamList);

			tm.commit();
			return "success";
		} catch (Exception e) {
			tm.release();
			return "fail：" + e.getMessage();
		}
	}

	@Override
	public List<NodeControlParam> getNodeContralParamList(String processKey,
			String business_id, String business_type) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("process_key", processKey);
		params.put("business_id", business_id);
		params.put("business_type", business_type);

		List<NodeControlParam> list = executor.queryListBean(
				NodeControlParam.class, "queryNodeContralParam_wf", params);

		if (list != null && list.size() > 0) {
			List<ActivityImpl> activties = activitiService
					.getActivitImplListByProcessKey(processKey);

			for (int i = 0; i < list.size(); i++) {
				NodeControlParam nodeInfo = list.get(i);

				for (ActivityImpl activtie : activties) {
					if (activtie.getId().equals(nodeInfo.getNODE_KEY())) {

						if (activtie.isMultiTask()) {
							nodeInfo.setIS_MULTI_DEFAULT(1);
						} else {
							nodeInfo.setIS_MULTI_DEFAULT(0);
						}

						break;
					}
				}
			}
		}

		return list;
	}

	@Override
	public NodeControlParam getNodeContralParam(String processKey,
			String business_id, String business_type, String taskKey)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("process_key", processKey);
		params.put("business_id", business_id);
		params.put("business_type", business_type);
		params.put("taskKey", taskKey);

		NodeControlParam controlParam = executor.queryObjectBean(
				NodeControlParam.class, "queryNodeContralParam_wf", params);

		if (controlParam != null) {
			List<ActivityImpl> activties = activitiService
					.getActivitImplListByProcessKey(processKey);

			for (ActivityImpl activtie : activties) {
				if (activtie.getId().equals(taskKey)) {
					if (activtie.isMultiTask()) {
						controlParam.setIS_MULTI_DEFAULT(1);
					} else {
						controlParam.setIS_MULTI_DEFAULT(0);
					}
				}
			}
		}

		return controlParam;
	}

	@Override
	public void saveNodeOrderNum(List<NodeControlParam> controlParamList) throws Exception{
		executor.updateBeans("updateNodeOrderNum", controlParamList);
	}

	@Override
	public boolean isCopyNode(String nodeKey, String processKey)
			throws Exception {

		NodeControlParam controlParam = executor.queryObject(
				NodeControlParam.class, "iscopynode_wf", nodeKey, processKey);

		if (null != controlParam && controlParam.getIS_COPY() == 1) {
			return true;
		} else {
			return false;
		}
	}
}