package com.frameworkset.platform.cms.workflowmanager;

import java.util.List;
import java.util.Map;

/**
 * 流程操作接口
 * @author jxw
 *
 * 2007-9-25
 */
public interface WorkflowManager extends java.io.Serializable {
	/**
	 * 新建流程
	 * @param workflow
	 * @return 返回新建成功的流程id
	 * @throws WorkflowManagerException
	 */
	public int createWorkflow(Workflow workflow) throws WorkflowManagerException;
	/**
	 * get流程Info
	 * <Workflow>对象中只有基本的信息，id和name
	 * @param workflow
	 * @return 
	 * @throws WorkflowManagerException
	 */
	public Workflow getWorkflow(String workflowId) throws WorkflowManagerException;
	/**
	 * update流程
	 * @param workflow
	 * @return  
	 * @throws WorkflowManagerException
	 */
	public boolean updateWorkflow(Workflow workflow) throws WorkflowManagerException;
	/**
	 * del流程
	 * @param workflow
	 * @return  
	 * @throws WorkflowManagerException
	 */
	public boolean delWorkflow(String[] workflowIds) throws WorkflowManagerException;
	/**
	 * get所有流程的节点Node
	 * @return List<Workflow>
	 * @throws WorkflowManagerException
	 */
	public List getWorkflowAllNodeList() throws WorkflowManagerException;
	/**
	 * 通过流程id get该流程的节点Node
	 * @return List<Workflow>
	 * @throws WorkflowManagerException
	 */
	public List getWorkflowNodeListById(String workflowId) throws WorkflowManagerException;
	/**
	 * add流程的节点Node
	 * 先全部删除，再add
	 * @return 
	 * @throws WorkflowManagerException
	 */
	public boolean addWorkflowNodes(String workflowId,String[] nodeIds) throws WorkflowManagerException;
	/**
	 * 获取流程被站点、频道、文档的引用情况
	 * 
	 * @return Map
	 * @throws WorkflowManagerException
	 */
	public Map getWorkflowCiteInfo(String workflowId) throws WorkflowManagerException;
	/**
	 * get流程列表信息
	 * @return List <Workflow>
	 * @throws WorkflowManagerException
	 */
	public List getWorkflowList() throws WorkflowManagerException;

}
