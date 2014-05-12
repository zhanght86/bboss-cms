package com.sany.workflow.entrust.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.entrust.entity.WfEntrustProcRelation;
import com.sany.workflow.entrust.service.EntrustService;

public class EntrustAction {
	
	private static Logger logger = Logger.getLogger(EntrustAction.class);
	
	private EntrustService entrustService;
	
	/**
	 * 首页
	 * @return
	 */
	public String index(){
		
		return "path:index";
		
	}
	
	/**
     * 分页查询列表
     * @param offset
     * @param pagesize
     * @return
     */
    public String entrustList(@PagerParam(name = PagerParam.OFFSET) long offset,
            @PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
            WfEntrust wfEntrust, ModelMap model) throws Exception {
    	
        ListInfo listInfo = entrustService.findListPage(offset, pagesize, wfEntrust);
        
        model.addAttribute("entrustList", listInfo);
        
        return "path:queryListPage";
        
    }
    
    /**
	 * 保存委托待办
	 * @param WfEntrust
	 * @return
	 */
	public @ResponseBody String saveWfEntrust(WfEntrust wfEntrust, String entrust_type, String entrust_desc, List<String> procdef_id){
		
		if(wfEntrust != null){
			
			TransactionManager tm = new TransactionManager();
			
			try {
				
				tm.begin();
				
				wfEntrust.setCreate_date(new Timestamp(new Date().getTime()));
				
				wfEntrust = entrustService.saveWfEntrust(wfEntrust);
				
				entrustService.saveWfEntrustProcRelation(wfEntrust, entrust_type, entrust_desc, procdef_id);
				
				tm.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e);
				
				return e.getMessage();
				
			}finally{
				tm.release();
			}
		}else{
			
			return "save data is null";
		}
		
		return "success";
	}
	
	/**
     * 查看委托信息
     * @param appInfoId
     * @param model
     * @return
     */
    public String viewEntrustInfo(String entrustInfoId, ModelMap model) throws Exception {
    	
    	if(StringUtils.isNotEmpty(entrustInfoId)){
    		
    		WfEntrust wfEntrust = entrustService.queryWfEntrustById(entrustInfoId);
    		
    		List<WfEntrustProcRelation> entrustProcRelationList = entrustService.queryRelationByEntrustId(entrustInfoId);
    		
    		if(!CollectionUtils.isEmpty(entrustProcRelationList)){
    			
    			WfEntrustProcRelation entrustRelation = entrustProcRelationList.get(0);
    			
    			model.put("entrustRelation", entrustRelation);
    		}
    		
    		model.put("wfEntrust", wfEntrust);
    		
    		model.put("entrustProcRelationList", entrustProcRelationList);
    		
    	}
    	
    	return "path:viewEntrustInfo";
    }
    
    /**
     * 修改委托信息
     * @param entrustInfoId
     * @param model
     * @return
     * @throws Exception
     */
    public String entrustInfoModify(String entrustInfoId, ModelMap model) throws Exception {
    	
    	if(StringUtils.isNotEmpty(entrustInfoId)){
    		
    		WfEntrust wfEntrust = entrustService.queryWfEntrustById(entrustInfoId);
    		
    		List<WfEntrustProcRelation> entrustProcRelationList = entrustService.queryRelationByEntrustId(entrustInfoId);
    		
    		if(!CollectionUtils.isEmpty(entrustProcRelationList)){
    			
    			WfEntrustProcRelation entrustRelation = entrustProcRelationList.get(0);
    			
    			model.put("entrustRelation", entrustRelation);
    		}
    		
    		model.put("wfEntrust", wfEntrust);
    		
    		model.put("entrustProcRelationList", entrustProcRelationList);
    		
    	}
    	
    	return "path:modifyEntrustInfo";
    }
	
	/**
	 * 删除委托信息
	 * @param entrustInfoId
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String deleteEntrustInfo(String entrustInfoId) throws Exception {
		
		String actionResult = "fail";
		
		if(StringUtils.isNotEmpty(entrustInfoId)){
			
			entrustService.deleteWfEntrustById(entrustInfoId);
			
			actionResult = "success";
		}
		
		return actionResult;
	}
	
	/**
	 * 修改委托信息状态
	 * @param entrustInfoId
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String unUseEntrustInfo(String entrustInfoId,String sts) throws Exception {
		
		String actionResult = "fail";
		
		if(StringUtils.isNotEmpty(entrustInfoId)){
			
			entrustService.unUseEntrustInfo(entrustInfoId,sts);
			
			actionResult = "success";
		}
		
		return actionResult;
	}
	

}
