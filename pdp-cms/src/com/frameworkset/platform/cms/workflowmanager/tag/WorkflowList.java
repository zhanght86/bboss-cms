package com.frameworkset.platform.cms.workflowmanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.workflowmanager.Workflow;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class WorkflowList extends DataInfoImpl implements java.io.Serializable {
	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,int maxPagesize)
	{
		return null;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) 
	{
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		List list = new ArrayList();
		
		StringBuffer sql = new StringBuffer();
		//0有特殊含义，为“引用站点流程”
		sql.append("select * from tb_cms_flow t where t.id !=0 order by t.id asc");
		
		try
		{
			dbUtil.executeSelect(sql.toString());
			
			if(dbUtil.size() > 0)
			{
				Workflow workflow = null;
				
				for(int i = 0;i < dbUtil.size();i ++)
				{
					workflow = new Workflow();
					
					workflow.setWorkflowId(dbUtil.getInt(i,"id"));
					workflow.setWorkflowName(dbUtil.getString(i,"name"));
					
					list.add(workflow);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.size());
				return listInfo;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return listInfo;
	}

}
