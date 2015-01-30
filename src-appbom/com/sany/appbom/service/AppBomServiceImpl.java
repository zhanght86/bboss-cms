/*
 * @(#)AppBomServiceImpl.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.appbom.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.sany.appbom.entity.AppBom;
import com.sany.appbom.entity.AppBomCondition;

public class AppBomServiceImpl {
	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	/**
	 * 用同样的sql只删一条记录，根据主键删除台账信息
	 * @throws AppBomException 
	 */
	public boolean delete(String id) throws AppBomException {
		try {
			executor.delete("deleteByKey", id);
		} catch (Throwable e) {
			throw new AppBomException("delete appbom failed::id="+id,e);
		}
		return true ;
	}

	/**
	 * 用同样的sql批量删除记录，受事务管理控制，如果有记录删除失败，则全部回滚之前的记录，当然也可以不要事务，也就是出错后
	 * 前面的记录成功、后面的记录不入库
	 * @param beans
	 * @return
	 * @throws AppBomException 
	 */
	public boolean deletebatch(String ... ids) throws AppBomException {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteByKeys("deleteByKey", ids);
			tm.commit();
		} catch (Throwable e) {
			
			throw new AppBomException("batch delete appbom failed::ids="+ids,e);
		}
		finally
		{
			tm.release();
		}

		return true;
	}
	/**
	 * 根据主键查找唯一记录
	 * @param idmap
	 * @return
	 * @throws AppBomException 
	 */
	public AppBom getAppBom(String id) throws AppBomException{
		try{
		AppBom bean=executor.queryObject(AppBom.class, "selectById", id);
		return bean;
		}catch(Throwable e){
			throw new AppBomException("get appbom failed:id="+id,e);
		}
		 
	}
	/**
	 * 修改记录（先删后增）
	 * @param bean
	 * @return
	 * @throws AppBomException 
	 */
	public boolean update(AppBom bean) throws AppBomException {
		
		try {
			executor.updateBean("updateAppBom", bean);
		} catch (Throwable e) {
			
			throw new AppBomException("update appbom failed:id="+bean.getId(),e);
		}
		return true;
	}
	
	
	/**
	 * 增加台账
	 * @param bean
	 * @return
	 * @throws AppBomException 
	 */
	public boolean addBom(AppBom bean) throws AppBomException {
		
		try {
			//通过PrimaryKey注解自动生成主键id
//			String uuid="";
//			if(bean.getId()==null||"".equals(bean.getId())){
//				uuid=DBUtil.getPool().getIdGenerator().getNextId();
//				bean.setId(uuid);
//			}
			executor.insertBean("batchsave", bean);
		} catch (Throwable e) {
			throw new AppBomException("add appbom failed:",e);
		}
		return true;
	}
	/**
	 * 根据条件查找记录，
	 * @param offset 偏移量
	 * @param pagesize 每页记录数
	 * @param appcondition
	 * @return
	 * @throws AppBomException 
	 */
	public ListInfo queryListInfoBean(long offset, int pagesize, AppBomCondition appcondition ) throws AppBomException{
		ListInfo datas=null;
		try{
			datas=executor.moreListInfoBean(AppBom.class,"queryListAppBom", offset, pagesize, appcondition);
		}catch(Exception e){
			throw new AppBomException("pagine query appbom failed:",e);
		}
		return datas;
	}
	/**
	 * 根据条件查找所有记录，不分页
	 * @param appcondition
	 * @return
	 * @throws AppBomException 
	 */
	public List<AppBom> queryListBean( AppBomCondition appcondition ) throws AppBomException{
		
		List<AppBom> beans = null;
		try{
			beans=executor.queryListBean(AppBom.class,"queryListAppBom", appcondition);
		}catch(Exception e){
			throw new AppBomException("query appbom failed:",e);
		}
		return beans;
	}
	/**
	 * 根据条件查找所有记录条数
	 * @param appcondition
	 * @return
	 * @throws AppBomException 
	 */
	public int queryCntByCondition( AppBomCondition appcondition ) throws AppBomException{
		int cnt=0;
		try{
			cnt=executor.queryTFieldBean(int.class,"queryCntByCondition", appcondition);
		}catch(Exception e){
			throw new AppBomException("count appbom failed:",e);
		}
		return cnt;
	}
	
	/**
	 * 判断编码是否存在
	 * @param map
	 * @return
	 * @throws AppBomException 
	 */
	public int checkBmExist(String bm) throws AppBomException{
		int cnt=0;
		try{
			cnt=executor.queryTField(int.class, "CheckBmExist", bm);
		}catch(Exception e){
			throw new AppBomException("check Bm Exist failed:",e);
		}
		return cnt;
	}
	/**
	 * 将软件级别从数字转换为中文字
	 * @param soft_level
	 * @return
	 */
	public String getSoftLevel(Integer soft_level){
		String ret="";
		switch(soft_level)
		{
		case 1:
			ret="一级";
			break;
		case 2:
			ret="二级";
			break;
		case 3:
			ret="三级";
			break;
		case 10:
			ret="待定";
			break;
		}
		return ret;
	}
	/**
	 * 将状态从数字转换为中文字
	 * @param soft_level
	 * @return
	 */
	public String getState(Integer state){
		String ret="";
		switch(state)
		{
		case 1:
			ret="在用";
			break;
		case 2:
			ret="试运行";
			break;
		case 3:
			ret="升级研发中";
			break;
		case 4:
			ret="停用";
			break;
		case 5:
			ret="暂用";
			break;
		case 6:
			ret="研发中";
			break;
		case 7:
			ret="试验中";
			break;		
		}
		return ret;
	}
	/**
	 * 将研发类型从数字转换为中文字
	 * @param soft_level
	 * @return
	 */
	public String getRdType(Integer rd_type){
		String ret="";
		switch(rd_type)
		{
		case 1:
			ret="自研";
			break;
		case 2:
			ret="外购";
			break;
		case 3:
			ret="免费";
			break;
		case 4:
			ret="试用";
			break;
		case 5:
			ret="外购+定制";
			break;
		}
		return ret;
	}
	/**
	 * 将规划类型从数字转换为中文字
	 * @param soft_level
	 * @return
	 */
	public String getPlanType(Integer plan_type){
		String ret="";
		switch(plan_type)
		{
		case 1:
			ret="目标";
			break;
		case 2:
			ret="演进";
			break;
		case 3:
			ret="临时";
			break;
		case 10:
			ret="待定";
			break;
		}
		return ret;
	}
	/**
	 * 将研发管理范围从数字转换为中文字
	 * @param soft_level
	 * @return
	 */
	public String getManageScope(Integer manage_scope){
		String ret="";
		switch(manage_scope)
		{
		case 1:
			ret="管理列表";
			break;
		case 2:
			ret="管理软件+版本号";
			break;
		case 3:
			ret="管理软件+版本号+测试";
			break;
		case 4:
			ret="管理软件+版本号+测试+UI";
			break;
		}
		return ret;
	}
	public void setExcelData(Workbook  workbook,boolean is2003,List<AppBom> beans){
		try{
			Sheet sheet=null;
			CellStyle cellStyle=null; 
			Font font=null;
			if(is2003){
				 sheet =(HSSFSheet)workbook.getSheetAt(0);

			}else{
				 sheet =(XSSFSheet)workbook.getSheetAt(0);
				 cellStyle=(XSSFCellStyle)workbook.createCellStyle();
			}
			 for(int i=0;i<beans.size();i++){
				 AppBom bom=beans.get(i);
				 Row row=null;
				 row = sheet.createRow(i+2);	 
				 row.setHeight((short)450);
				 row.createCell(0).setCellValue(bom.getBm());
				 row.createCell(1).setCellValue(bom.getApp_name_en());
				 row.createCell(2).setCellValue(bom.getApply_domain());
				 row.createCell(3).setCellValue(bom.getApp_name());
				 row.createCell(4).setCellValue(bom.getDescription());
				 row.createCell(5).setCellValue(getSoftLevel(bom.getSoft_level()));
				 row.createCell(6).setCellValue(bom.getSupplier());
				 row.createCell(7).setCellValue(bom.getStart_year());
				 row.createCell(8).setCellValue(getState(bom.getState()));
				 row.createCell(9).setCellValue(getRdType(bom.getRd_type()));
				 row.createCell(10).setCellValue(bom.getVersion_no());
				 row.createCell(11).setCellValue(bom.getDomain_url());
				 row.createCell(12).setCellValue(bom.getStruct_mode());
				 row.createCell(13).setCellValue(bom.getSoft_language());
				 row.createCell(14).setCellValue(bom.getDevelop_tool());
				 row.createCell(15).setCellValue(bom.getDb_type());
				 row.createCell(16).setCellValue(bom.getDepartment_develop());
				 row.createCell(17).setCellValue(bom.getProduct_manager());
				 row.createCell(18).setCellValue(bom.getDepartment_maintain());
				 row.createCell(19).setCellValue(bom.getSys_manager());
				 row.createCell(20).setCellValue(getPlanType(bom.getPlan_type()));
				 row.createCell(21).setCellValue(bom.getEvolve_strategy());
				 row.createCell(22).setCellValue(bom.getEvolve_plan());
				 row.createCell(23).setCellValue(bom.getEvolve_depart());
				 row.createCell(24).setCellValue(getManageScope(bom.getManage_scope()));
				 row.createCell(25).setCellValue(bom.getMain_description());
			 }
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{

		}
	}

	/**
	 * 注入使用
	 * @return
	 */
	public com.frameworkset.common.poolman.ConfigSQLExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(
			com.frameworkset.common.poolman.ConfigSQLExecutor executor) {
		this.executor = executor;
	}
	
	public String sayMvsHello(String duoduo)
	{
		return "Hello,"+duoduo;
	}
}
