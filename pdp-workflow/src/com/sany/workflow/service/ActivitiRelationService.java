package com.sany.workflow.service;

import java.sql.SQLException;
import java.util.List;

import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.WfAppProcdefRelation;

public interface ActivitiRelationService {
	
	/**
     * 添加流程和应用的关联关系
     * @param processDef
     * @param wf_app_id
     */
    public void addAppProcRelation(ProcessDef processDef, String wf_app_id) throws SQLException;
    
    /**
     * 查询流程和应用的关联关系
     * @param relation
     * @return
     * @throws SQLException
     */
    public List<WfAppProcdefRelation> selectAppProcRelation(WfAppProcdefRelation relation) throws SQLException;
    
    /**
     * 删除流程和应用的关联关系
     * @param relation
     * @throws SQLException
     */
    public void deleteAppProcRelation(WfAppProcdefRelation relation) throws SQLException;

}
