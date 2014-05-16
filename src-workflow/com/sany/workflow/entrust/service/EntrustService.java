package com.sany.workflow.entrust.service;

import java.util.List;
import java.util.Map;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.entrust.entity.WfEntrustProcRelation;

public interface EntrustService {
	
	/**
     * 分页读取委托待办
     * @param offset
     * @param pagesize
     * @param condition
     * @return
     */
    public ListInfo findListPage(long offset, int pagesize, WfEntrust condition) throws Exception;
    
    /**
     * 保存委托待办信息
     * @param condition
     * @throws Exception
     */
    public WfEntrust saveWfEntrust(WfEntrust condition) throws Exception;
    
    /**
     * 删除委托待办信息
     * @param entrustId
     * @throws Exception
     */
    public void deleteWfEntrustById(String entrustId) throws Exception;
    
    /**
     * 修改委托待办状态
     * @param entrustId
     * @param sts
     * @throws Exception
     */
    public void unUseEntrustInfo(String entrustId, String sts) throws Exception;
    
    /**
     * 保存委托待办流程信息
     * @throws Exception
     */
    public void saveWfEntrustProcRelation(WfEntrust wfEntrust, String entrust_type, String entrust_desc, List<String> procdef_id) throws Exception;

    /**
     * 根据ID加载委托待办信息
     * @param wfEntrustId
     * @return
	 * @throws Exception 
     */
    public WfEntrust queryWfEntrustById(String wfEntrustId) throws Exception;
    
    /**
     * 根据委托ID加载委托流程关联关系
     * @param wfEntrustId
     * @return
     * @throws Exception
     */
    public List<WfEntrustProcRelation> queryRelationByEntrustId(String wfEntrustId) throws Exception;
    
    /**
     * 保存委托待办前验证委托流程信息
     * @param wfEntrust
     * @param entrust_type
     * @param procdef_id
     * @return
     * @throws Exception
     */
    public Map<String,String> validateSaveWfEntrust(WfEntrust wfEntrust, String entrust_type, List<String> procdef_id) throws Exception;
    
}
