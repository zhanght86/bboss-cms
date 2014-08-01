package com.sany.workflow.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.frameworkset.util.CollectionUtils;

import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.WfAppProcdefRelation;
import com.sany.workflow.service.ActivitiRelationService;

public class ActivitiRelationServiceImpl implements ActivitiRelationService {
	
	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;
	
	/**
     * 添加流程和应用的关联关系
     * @param processDef
     * @param wf_app_id
     */
    public void addAppProcRelation(ProcessDef processDef, String wf_app_id) throws SQLException {
    	
    	if(StringUtils.isNotEmpty(wf_app_id) && StringUtils.isNotEmpty(processDef.getID_())){
    		
    		WfAppProcdefRelation relation = new WfAppProcdefRelation();
    		
//    		relation.setProcdef_id(processDef.getID_()); edit by gw_tanx 20140731
    		relation.setProcdef_id(processDef.getKEY_());
    		
    		List<WfAppProcdefRelation> relationList = selectAppProcRelation(relation);
    		
    		relation.setId(UUID.randomUUID().toString());
    		relation.setWf_app_id(wf_app_id);
    		
    		if(!CollectionUtils.isEmpty(relationList)){
    			
    			WfAppProcdefRelation temp = relationList.get(0);
    			relation.setId(temp.getId());
    			
    			executor.updateBean("updateAppProcdefRelation", relation);
    		}else{
    			executor.insertBean("insertAppProcdefRelation", relation);
    		}
    	}
    }
    
    /**
     * 查询流程和应用的关联关系
     * @param relation
     * @return
     * @throws SQLException
     */
    public List<WfAppProcdefRelation> selectAppProcRelation(WfAppProcdefRelation relation) throws SQLException{
    	
         List<WfAppProcdefRelation> relationList = new ArrayList<WfAppProcdefRelation>();
         
         relationList = executor.queryListBean(WfAppProcdefRelation.class, "selectAppProcdefRelation", relation);
    	
         return relationList;
    }
    
    /**
     * 删除流程和应用的关联关系
     * @param relation
     * @throws SQLException
     */
    public void deleteAppProcRelation(WfAppProcdefRelation relation) throws SQLException{
    	
    	if(relation != null){
    		
    		if(StringUtils.isNotEmpty(relation.getId()) || StringUtils.isNotEmpty(relation.getWf_app_id())
    				|| StringUtils.isNotEmpty(relation.getProcdef_id())){
    			
    			executor.deleteBean("deleteAppProcdefRelation", relation);
    		}
    	}
    }
	
}
