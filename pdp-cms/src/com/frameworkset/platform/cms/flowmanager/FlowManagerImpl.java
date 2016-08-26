package com.frameworkset.platform.cms.flowmanager;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;

public class FlowManagerImpl implements FlowManager
{
	public List getFlowList() throws FlowManagerException
	{
		String sql = "select id,name from tb_cms_flow where id<>0";
		List list = null;
		
		try{
    		DBUtil db1 = new DBUtil();			
	    	db1.executeSelect(sql);
	    	list = new ArrayList();
	    	for(int i=0;i<db1.size();i++){
	    		List oneRow = new ArrayList();
	    		oneRow.add(db1.getString(i, 0));
	    		oneRow.add(db1.getString(i, 1));
	    		list.add(oneRow);
	    	}
    	}catch(Exception e){
	    	e.printStackTrace();
	    	throw new FlowManagerException(e.toString());
	    }
	    
    	return list;
	}
	public List getFlowList2() throws FlowManagerException
	{
		String sql = "select id,name from tb_cms_flow where id<>0";
		List list = null;
		
		try{
    		DBUtil db1 = new DBUtil();			
	    	db1.executeSelect(sql);
	    	list = new ArrayList();
	    	for(int i=0;i<db1.size();i++){
	    		Flow flow = new Flow();
	    		flow.setFlowid(db1.getInt(i, "id"));
	    		flow.setFlowName(db1.getString(i, "name"));
	    		list.add(flow);
	    	}
    	}catch(Exception e){
	    	e.printStackTrace();
	    	throw new FlowManagerException(e.toString());
	    }
	    
    	return list;
	}
}
