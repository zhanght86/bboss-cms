package com.frameworkset.platform.cms.workflowmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

/**
 * 流程操作接口实现类
 * @author jxw
 *
 * 2007-9-25
 */
public class WorkflowManagerImpl implements WorkflowManager {
	
	/**
	 * 新建流程
	 * @param workflow
	 * @return 返回新建成功的流程id
	 * @throws WorkflowManagerException
	 */
	public int createWorkflow(Workflow workflow) throws WorkflowManagerException
	{
		int workflowId = 0;
		
		StringBuffer sql = new StringBuffer();
		PreparedDBUtil db = new PreparedDBUtil();
		
		try
		{
			sql.append("insert into tb_cms_flow(name) values (?)");
			
			db.preparedInsert(sql.toString());
			
			db.setString(1,workflow.getWorkflowName());
			
			workflowId = ((Long)db.executePrepared()).intValue();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
		return workflowId;
	}
	
	/**
	 * get流程Info
	 * <Workflow>对象中只有基本的信息，id和name
	 * @param workflow
	 * @return 
	 * @throws WorkflowManagerException
	 */
	public Workflow getWorkflow(String workflowId) throws WorkflowManagerException
	{
		Workflow workflow = new Workflow();
		
		StringBuffer sql = new StringBuffer();
		DBUtil db = new DBUtil();
		
		try
		{
			sql.append("select * from tb_cms_flow where id = ")
				.append(workflowId);
			db.executeSelect(sql.toString());
			if(db.size() > 0)
			{
				workflow.setWorkflowId(db.getInt(0,"id"));
				workflow.setWorkflowName(db.getString(0,"name"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
		return workflow;
	}
	
	/**
	 * update流程
	 * @param workflow
	 * @return 
	 * @throws WorkflowManagerException
	 */
	public boolean updateWorkflow(Workflow workflow) throws WorkflowManagerException
	{
		boolean flag = false;
		
		PreparedDBUtil db = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer();
		
		try
		{
			sql.append("update tb_cms_flow set name=? where id=?");
			
			db.preparedUpdate(sql.toString());
			
			db.setString(1,workflow.getWorkflowName());
			db.setPrimaryKey(2,workflow.getWorkflowId());
			
			db.executePrepared();
			
			flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
	
		return flag;
	}
	
	/**
	 * del流程
	 * @param workflow
	 * @return  
	 * @throws WorkflowManagerException
	 */
	public boolean delWorkflow(String[] workflowIds) throws WorkflowManagerException
	{
		boolean flag = false;
		
		StringBuffer sql;
		DBUtil db = new DBUtil();
		
		try
		{
			for(int i = 0;i < workflowIds.length;i ++)
			{
				sql = new StringBuffer();
				sql.append("delete from tb_cms_flow_doc_trans where flow_id = ")
					.append(workflowIds[i]);
				
				db.addBatch(sql.toString());
				
				sql = new StringBuffer();
				sql.append("delete from tb_cms_flow where id = ")
					.append(workflowIds[i]);
				
				db.addBatch(sql.toString());
				
			}
			db.executeBatch();//批处理操作数据库
			
			flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
		return flag;
	}
	
	/**
	 * get所有流程的节点Node
	 * @return List<Workflow>
	 * @throws WorkflowManagerException
	 */
	public List getWorkflowAllNodeList() throws WorkflowManagerException
	{
		List nodelist = new ArrayList();
		
		StringBuffer sql = new StringBuffer();
		DBUtil db = new DBUtil();
		
		try
		{
			sql.append("select a.id,a.src_status,")
			.append("(select b.name from tb_cms_doc_status b where b.id = a.src_status) as src_name,")
			.append("a.dest_status,")
			.append("(select b.name from tb_cms_doc_status b where b.id = a.dest_status) as dest_name ")
			.append("from tb_cms_doc_status_trans a order by a.src_status asc");
			
			db.executeSelect(sql.toString());
			
			if(db.size() > 0)
			{
				Workflow workflow = null;
				for(int i = 0;i < db.size();i ++)
				{
					workflow = new Workflow();
					
					workflow.setTransisionId(db.getInt(i,"id"));
					workflow.setSrcStatusId(db.getInt(i,"src_status"));
					workflow.setSrcStatusName(db.getString(i,"src_name"));
					workflow.setDestStatusId(db.getInt(i,"dest_status"));
					workflow.setDestStatusName(db.getString(i,"dest_name"));
					
					nodelist.add(workflow);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
		
		return nodelist;
	}
	
	/**
	 * 通过流程id get该流程的节点Node
	 * @return List<Workflow>
	 * @throws WorkflowManagerException
	 */
	public List getWorkflowNodeListById(String workflowId) throws WorkflowManagerException
	{
		List nodelist = new ArrayList();
		
		StringBuffer sql = new StringBuffer();
		DBUtil db = new DBUtil();
		
		try
		{
			sql.append("select c.id,c.name,d.transision_id,a.src_status,")
			.append("(select b.name from tb_cms_doc_status b where b.id = a.src_status) as src_name,")
			.append("a.dest_status,")
			.append("(select b.name from tb_cms_doc_status b where b.id = a.dest_status) as dest_name ")
			.append("from tb_cms_doc_status_trans a,tb_cms_flow c,tb_cms_flow_doc_trans d ")
			.append("where c.id = d.flow_id and d.transision_id = a.id and c.id = ")
			.append(workflowId)
			.append(" order by a.src_status asc");
			
			db.executeSelect(sql.toString());
			
			if(db.size() > 0)
			{
				Workflow workflow = null;
				for(int i = 0;i < db.size();i ++)
				{
					workflow = new Workflow();
					
					workflow.setWorkflowId(db.getInt(i,"id"));
					workflow.setWorkflowName(db.getString(i,"name"));
					workflow.setTransisionId(db.getInt(i,"transision_id"));
					workflow.setSrcStatusId(db.getInt(i,"src_status"));
					workflow.setSrcStatusName(db.getString(i,"src_name"));
					workflow.setDestStatusId(db.getInt(i,"dest_status"));
					workflow.setDestStatusName(db.getString(i,"dest_name"));
					
					nodelist.add(workflow);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
		
		return nodelist;
	}
	
	/**
	 * add流程的节点Node
	 * 先全部删除，再add
	 * @return 
	 * @throws WorkflowManagerException
	 */
	public boolean addWorkflowNodes(String workflowId,String[] nodeIds) throws WorkflowManagerException
	{
		boolean flag = false;
		if(nodeIds == null)
			return flag;
		StringBuffer sql = new StringBuffer();
		DBUtil db = new DBUtil();
		
		try
		{
			sql.append("delete from tb_cms_flow_doc_trans where flow_id = ").append(workflowId);
			db.addBatch(sql.toString());//先全部删除
			
			for(int i = 0;i < nodeIds.length;i ++)
			{
				if(!nodeIds[i].trim().equals(""))
				{
					sql = new StringBuffer();
					sql.append("insert into tb_cms_flow_doc_trans(flow_id,transision_id) values (")
						.append(workflowId)
						.append(",")
						.append(nodeIds[i].trim())
						.append(")");
					db.addBatch(sql.toString());
				}
					
			}
			db.executeBatch();//批处理操作数据库
			
			flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage()); 
		}
		return flag;
	}
	
	/**
	 * 获取流程被站点、频道、文档的引用情况
	 * 
	 * @return Map
	 * @throws WorkflowManagerException
	 */
	public Map getWorkflowCiteInfo(String workflowId) throws WorkflowManagerException
	{
		Map workflowCiteInfo = new HashMap();
		
		String title = "";
		StringBuffer siteCite = new StringBuffer();
		StringBuffer channelCite = new StringBuffer();
		StringBuffer documentCite = new StringBuffer();
		
		StringBuffer sql = new StringBuffer();
		DBUtil db = new DBUtil();
		
		try
		{
			//title
			sql.append("select name from tb_cms_flow where id = ")
				.append(workflowId);
			db.executeSelect(sql.toString());
			
			if(db.size() > 0)
			{
				for(int i = 0;i < db.size();i ++)
				{
					title = db.getString(i,"name");
				}
				
			}
			//站点引用情况
			sql = new StringBuffer();
			sql.append("select a.name from td_cms_site a inner join tb_cms_flow b ")
				.append("on a.flow_id = b.id where a.flow_id = ")
				.append(workflowId);
			db.executeSelect(sql.toString());
			
			if(db.size() > 0)
			{
				for(int i = 0;i < db.size();i ++)
				{
					siteCite.append(db.getString(i,"name") + "\\n");
				}
				
			}
			
			//频道引用情况
			sql = new StringBuffer();
			sql.append("select (select b.name from td_cms_site b where b.site_id = a.site_id) as sitename,")
				.append("a.display_name from td_cms_channel a ")
				.append("inner join tb_cms_flow c on a.workflow = c.id where a.workflow = ")
				.append(workflowId);
			db.executeSelect(sql.toString());
			
			if(db.size() > 0)
			{
				for(int i = 0;i < db.size();i ++)
				{
					channelCite.append("站点[" + db.getString(i,"sitename") + "]下的频道");
					channelCite.append("[" + db.getString(i,"display_name") + "]\\n");
				}
			}
			
			//文档引用情况
			sql = new StringBuffer();
			sql.append("select count(1) from td_cms_document a where a.flow_id = ")
				.append(workflowId);
			db.executeSelect(sql.toString());
			
			if(db.size() > 0 && db.getInt(0,0) >= 1)
			{
				documentCite.append("共" + db.getInt(0,0) + "条文档引用了改流程");
				/*for(int i = 0;i < db.size();i ++)
				{
					siteCite.append("站点[" + db.getString(i,"sitename") + "]下的频道");
					siteCite.append("[" + db.getString(i,"display_name") + "]\\n");
				}*/
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new WorkflowManagerException(e.getMessage());
		}
		
		workflowCiteInfo.put("title",title);
		workflowCiteInfo.put("siteCite",siteCite.toString());
		workflowCiteInfo.put("channelCite",channelCite.toString());
		workflowCiteInfo.put("documentCite",documentCite.toString());
		
		return workflowCiteInfo;
	}
	
	/**
	 * get流程列表信息
	 * @return List <Workflow>
	 * @throws WorkflowManagerException
	 */
	public List getWorkflowList() throws WorkflowManagerException
	{
		return null;
	}

}
