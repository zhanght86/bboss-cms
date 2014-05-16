package com.sany.workflow.entrust.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.frameworkset.util.CollectionUtils;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.entrust.entity.WfEntrustProcRelation;
import com.sany.workflow.entrust.service.EntrustService;
import com.sany.workflow.entrust.util.EntrustConstant;

public class EntrustServiceImpl implements EntrustService {
	
	private ConfigSQLExecutor executor;
	
	/**
     * 分页读取委托待办
     * @param offset
     * @param pagesize
     * @param condition
     * @return
     */
	@Override
    public ListInfo findListPage(long offset, int pagesize, WfEntrust condition) throws Exception{
		
		ListInfo listInfo = null;

    	if (StringUtils.isNotEmpty(condition.getEntrust_user())) {
    		condition.setEntrust_user("%" + condition.getEntrust_user() + "%");
        }
    	
        if (StringUtils.isNotEmpty(condition.getCreate_user())) {
            condition.setCreate_user("%" + condition.getCreate_user() + "%");
        }
        
        if (StringUtils.isNotEmpty(condition.getSts())) {
            condition.setSts("%" + condition.getSts() + "%");
        }
        
        listInfo = executor
                .queryListInfoBean(WfEntrust.class, "selectEntrustList", offset, pagesize, condition);

        return listInfo;
    	
    }
	
	/**
     * 保存委托代办信息
     * @param condition
     * @throws Exception
     */
	@Override
    public WfEntrust saveWfEntrust(WfEntrust wfEntrust) throws Exception{
		
		if(wfEntrust!=null){
    	
    		if(StringUtils.isNotEmpty(wfEntrust.getId())){
    			
    			executor.updateBean("updateWfEntrust", wfEntrust);
    		}else{
    			
    			wfEntrust.setId(UUID.randomUUID().toString());
    			
    			executor.insertBean("insertWfEntrust", wfEntrust);
    		}
    	}
		
		return wfEntrust;
	}
	
	/**
     * 删除委托待办信息
     * @param entrustId
     * @throws Exception
     */
    public void deleteWfEntrustById(String entrustId) throws Exception{
    	
    	if(!StringUtils.isEmpty(entrustId)){
    		
    		executor.delete("deleteEntrustProcRelationByEntrustId", entrustId);
        	
        	executor.delete("deleteWfEntrustById", entrustId);
        	
    	}else{
    		
    		throw new Exception("entrustId is empty");
    	}
    	
    }
    
    /**
     * 修改委托待办状态
     * @param entrustId
     * @param sts
     * @throws Exception
     */
    public void unUseEntrustInfo(String entrustId, String sts) throws Exception{
    	
    	if(!StringUtils.isEmpty(entrustId)){
    		
    		executor.update("updateWfEntrustStsById", sts, entrustId);
        	
    	}else{
    		
    		throw new Exception("entrustId is empty");
    	}
    	
    }
	

	/**
     * 保存委托待办流程信息
     * @throws Exception
     */
	@Override
    public void saveWfEntrustProcRelation(WfEntrust wfEntrust, 
    		String entrust_type, String entrust_desc, List<String> procdef_id) throws Exception{
		
		executor.delete("deleteEntrustProcRelationByEntrustId", wfEntrust.getId());
		
		List<WfEntrustProcRelation> entrustRelation = new ArrayList<WfEntrustProcRelation>();
		
		if(EntrustConstant.getENTRUST_TYPE_ALL().equals(entrust_type)){
			
			WfEntrustProcRelation relation = new WfEntrustProcRelation();
			
			relation.setId(UUID.randomUUID().toString());
			
			relation.setEntrust_id(wfEntrust.getId());
			
			relation.setEntrust_type(entrust_type);
			
			relation.setEntrust_desc(entrust_desc);
			
			entrustRelation.add(relation);
		}else{
			
			if(!CollectionUtils.isEmpty(procdef_id)){
				
				for(String procdef : procdef_id){
					
					WfEntrustProcRelation relation = new WfEntrustProcRelation();
					
					relation.setId(UUID.randomUUID().toString());
					
					relation.setEntrust_id(wfEntrust.getId());
					
					relation.setEntrust_type(entrust_type);
					
					relation.setEntrust_desc(entrust_desc);
					
					relation.setProcdef_id(procdef);
					
					entrustRelation.add(relation);
				}
			}
			
		}
		
		if(CollectionUtils.isEmpty(entrustRelation)){
			throw new Exception("entrustRelation is empty");
		}
		
		executor.insertBeans("insertEntrustProcRelation", entrustRelation);
		
	}
	
	/**
     * 保存委托待办前验证委托流程信息
     * @param wfEntrust
     * @param entrust_type
     * @param procdef_id
     * @return
     * @throws Exception
     */
    public Map<String,String> validateSaveWfEntrust(WfEntrust wfEntrust, String entrust_type, List<String> procdef_id) throws Exception{
    	
    	Map<String,String> validateMap = new HashMap<String,String>();
    	
    	validateMap.put("validateResult", "fail");
    	
    	validateMap.put("validateMsg", "fail");
    	
    	List<WfEntrust> validateList = executor.queryListBean(WfEntrust.class, "selectValidateEntrustList", wfEntrust);
    	
    	if(CollectionUtils.isEmpty(procdef_id)){
    		
    		if(CollectionUtils.isEmpty(validateList)){
    			
    			validateMap.put("validateResult", "success");
    	    	
    	    	validateMap.put("validateMsg", "success");
    		}
    		
    	}else{
    		
    		if(CollectionUtils.isEmpty(validateList)){
    			
    			validateMap.put("validateResult", "success");
    	    	
    	    	validateMap.put("validateMsg", "success");
    		}else{
    			
    			List<String> wfEntrustIdList = new ArrayList<String>();
        		
        		for(WfEntrust validateEntrust : validateList){
        			
        			wfEntrustIdList.add(validateEntrust.getId());
        			
        		}
        		
        		wfEntrust.setWfEntrustIdList(wfEntrustIdList);
        		wfEntrust.setWfProcdefIdList(procdef_id);
        		
        		List<WfEntrustProcRelation> relationList = executor.queryListBean(WfEntrustProcRelation.class, "selectValidateEntrustRelation", wfEntrust);
        		
        		if(CollectionUtils.isEmpty(relationList)){
        			validateMap.put("validateResult", "success");
        	    	
        	    	validateMap.put("validateMsg", "success");
        		}
    		}
    	}
    	
    	return validateMap;
    }
    
	
    /**
     * 根据ID加载委托待办信息
     * @param wfEntrustId
     * @return
	 * @throws Exception 
     */
    public WfEntrust queryWfEntrustById(String wfEntrustId) throws Exception{
    	
    	return executor.queryObject(WfEntrust.class, "selectWfEntrust", wfEntrustId);
    	
    }
    
    /**
     * 根据委托ID加载委托流程关联关系
     * @param wfEntrustId
     * @return
     * @throws Exception
     */
    public List<WfEntrustProcRelation> queryRelationByEntrustId(String wfEntrustId) throws Exception{
    	
    	return executor.queryList(WfEntrustProcRelation.class, "queryRelationByEntrustId", wfEntrustId);
    	
    }
	

}









