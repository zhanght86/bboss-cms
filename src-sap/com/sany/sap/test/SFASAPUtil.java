/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.sany.sap.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.frameworkset.util.StringUtil;
import com.sany.sap.connection.SAPConf;
import com.sany.sap.connection.SapConnectFactory;
import com.sany.sap.connection.SapException;
import com.sany.sap.connection.SapResult;
import com.sap.conn.jco.JCoDestination;

/**
 * <p>
 * SFASAPUtil.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2012-9-26 上午9:53:46
 * @author biaoping.yin
 * @version 1.0
 */
public class SFASAPUtil {
	private SapConnectFactory sapConnectFactory;
	public static final Map<String,String> NEWBSCODE_TABLENAMES = new HashMap();
	static
	{
		/**
		 * 取同一凭证序列号第一条行记录的抬头字段数据输入到凭证抬头DOCUMENTHEADER对应字段。
当行记录中的记账码（BSEG-BSCHL）属于01-1Y时，对应客户ACCOUNTRECEIVABLE字段。
当行记录中的记账码（BSEG-BSCHL）属于21-39时，对应客户供应商ACCOUNTPAYABLE字段
当行记录中的记账码（BSEG-BSCHL）属于40-50时，对应总账项目 ACCOUNTGL字段。

		 */
		for(int i = 40; i <=50; i ++)
		{
			NEWBSCODE_TABLENAMES.put(i+"", "ACCOUNTGL");
		}
		for(int i = 21; i <=39; i ++)
		{
			NEWBSCODE_TABLENAMES.put(i+"", "ACCOUNTPAYABLE");
		}
		
		for(int i = 1; i <=9; i ++)
		{
			NEWBSCODE_TABLENAMES.put("0"+i, "ACCOUNTRECEIVABLE");
		}
		for(int i = 10; i <=19; i ++)
		{
			NEWBSCODE_TABLENAMES.put(""+i, "ACCOUNTRECEIVABLE");
		}
		for(char i = 'a'; i <='y'; i ++)
		{
			NEWBSCODE_TABLENAMES.put("1"+i, "ACCOUNTRECEIVABLE");
		}
		
	}
	private String handleKemu(String kemu)
	{		
		if(!kemu.substring(0, 1).matches("[0-9]"))
			return kemu;
		if(kemu.length() < 10)
		{
			int append = 10 - kemu.length();
			StringBuffer ret = new StringBuffer();
			for(int i = 0; i < append; i ++)
			{
				ret.append("0");
			}
			return ret.append(kemu).toString();
		}
		else
		{
			return kemu;
		}
	}
	public String getAccountTable(List<BorrowLend> borrowLend,String NEWBS){
		for(int i=0;i<borrowLend.size();i++){
			BorrowLend bl=borrowLend.get(i);
			if(bl.getNewbs().equals(NEWBS)){
				return bl.getAccountTable();
			}
		}
		return "";
	}
	public SapResult zongzhangjizhang(List<GeneralLedger> dataList,List<BorrowLend> borrowLend) {
		if(dataList == null || dataList.size() == 0)
			return null;
		SapResult sapResult = null;
		// 凭证抬头
		GeneralLedger generalLedger = dataList.get(0);
		String[] inParamsStructureNames = new String[1];
		Map<String, Object>[] inParamsStructureValues = new Map[1];
		inParamsStructureNames[0] = "DOCUMENTHEADER";
		inParamsStructureValues[0] = new HashMap<String, Object>();
		inParamsStructureValues[0].put("DOC_DATE", generalLedger.getDocDate());// 凭证日期

		inParamsStructureValues[0].put("PSTNG_DATE", generalLedger.getPstngDate());// 记账日期

		inParamsStructureValues[0].put("BUS_ACT", generalLedger.getBusAct());
		inParamsStructureValues[0].put("FIS_PERIOD", generalLedger.getPeriod());// 记账期间

		inParamsStructureValues[0].put("COMP_CODE", generalLedger.getCompCode());// 公司代码

		inParamsStructureValues[0].put("DOC_TYPE", generalLedger.getDocType());// 凭证类型

		inParamsStructureValues[0].put("REF_DOC_NO", generalLedger.getDocNo());// 参照

		inParamsStructureValues[0].put("HEADER_TXT",generalLedger.getHeaderTxt());// 凭证抬头文本

		inParamsStructureValues[0].put("USERNAME", generalLedger.getUserName());// 用户名

		

//		List<Map<String, Object>> inTableValue1 = new ArrayList<Map<String, Object>>();
		
		Map<String,List<Map<String, Object>>> inTableValues = new HashMap<String,List<Map<String, Object>>>();
		List<Map<String, Object>> inTableValue2 = new ArrayList<Map<String, Object>>();

		String CURRENCYAMOUNT = "CURRENCYAMOUNT";
		String tablename = "";
		for(GeneralLedger zongzhang:dataList)
		{
			String NEWBS = zongzhang.getNewBS();
			//tablename = NEWBSCODE_TABLENAMES.get(NEWBS);
			tablename = getAccountTable(borrowLend,NEWBS);
			if ("ACCOUNTGL".equals(tablename))// 总账项目
	
			{
			
				List<Map<String, Object>> inTableValue1 = inTableValues.get(tablename);
				if(inTableValue1 == null)
				{
					inTableValue1 = new ArrayList<Map<String, Object>>();
					inTableValues.put(tablename, inTableValue1);
				}
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("ITEMNO_ACC", zongzhang.getItemNoAcc());
	
				row.put("GL_ACCOUNT", zongzhang.getHkont());// 科目
				// row.put("CS_TRANS_T", "");//事务类型
				row.put("COSTCENTER", zongzhang.getCostCenter());// 成本中心
				row.put("ORDERID", zongzhang.getOrderId());// 订单
				row.put("SALES_ORD", zongzhang.getSalesOrd());// 销售订单
	
				row.put("BUS_AREA", zongzhang.getBusArea());// 业务范围
				row.put("FUNC_AREA_LONG", zongzhang.getFuncArea());// 功能范围
				row.put("PROFIT_CTR", zongzhang.getProfitCtr());// 利润中心
				row.put("ALLOC_NMBR", zongzhang.getAllocNmbr());// 分配
				row.put("ITEM_TEXT", zongzhang.getItemText());// 行项目文本
				row.put("PART_PRCTR", zongzhang.getPartPrctr());// 伙伴利润中心
	
				row.put("TRADE_ID", zongzhang.getTradeId());// 贸易伙伴
				row.put("RSTGR", zongzhang.getAppend());//原因代码
				row.put("REF_KEY_1", zongzhang.getRefKey1());// 参考参数1
				row.put("REF_KEY_2", zongzhang.getRefKey2());// 参考参数2
				row.put("REF_KEY_3", zongzhang.getRefKey3());// 参考参数3
	
				inTableValue1.add(row);
	
			} else if ("ACCOUNTRECEIVABLE".equals(tablename))// 客户
	
			{
				
				
				List<Map<String, Object>> inTableValue1 = inTableValues.get(tablename);
				if(inTableValue1 == null)
				{
					inTableValue1 = new ArrayList<Map<String, Object>>();
					inTableValues.put(tablename, inTableValue1);
				}
				Map<String, Object> row = new HashMap<String, Object>();
	
				row.put("ITEMNO_ACC", zongzhang.getItemNoAcc());
				row.put("CUSTOMER", handleKemu(zongzhang.getHkont()));// 科目
				
				// row.put("CUSTOMER", handleKemu(zongzhang.getHkont()));// 科目
				row.put("BUS_AREA", zongzhang.getBusArea());// 事务类型
				row.put("PROFIT_CTR", zongzhang.getProfitCtr());// 事务类型
				row.put("ALLOC_NMBR", zongzhang.getAllocNmbr());// 事务类型
				row.put("BLINE_DATE", zongzhang.getBlineDate());// 事务类型
	
				row.put("ITEM_TEXT", zongzhang.getItemText());// 事务类型
	
				row.put("REF_KEY_1", zongzhang.getRefKey1());// 事务类型
				row.put("REF_KEY_2", zongzhang.getRefKey2());// 事务类型
				row.put("REF_KEY_3", zongzhang.getRefKey3());// 事务类型
				row.put("SP_GL_IND", zongzhang.getSpglind());// 事务类型
	
				inTableValue1.add(row);
										 
			} else if ("ACCOUNTPAYABLE".equals(tablename))// 客户供应商
			{
				
				
				List<Map<String, Object>> inTableValue1 = inTableValues.get(tablename);
				if(inTableValue1 == null)
				{
					inTableValue1 = new ArrayList<Map<String, Object>>();
					inTableValues.put(tablename, inTableValue1);
				}
				Map<String, Object> row = new HashMap<String, Object>();
	
				row.put("ITEMNO_ACC", zongzhang.getItemNoAcc());
			//	row.put("VENDOR_NO", zongzhang.getHkont());// 科目
				row.put("VENDOR_NO", handleKemu(zongzhang.getHkont()));// 科目
				
				row.put("BUS_AREA", zongzhang.getBusArea());// 事务类型
				row.put("PROFIT_CTR", zongzhang.getProfitCtr());// 事务类型
				row.put("ALLOC_NMBR", zongzhang.getAllocNmbr());// 事务类型
				row.put("BLINE_DATE", zongzhang.getBlineDate());// 事务类型
	
				row.put("ITEM_TEXT", zongzhang.getItemText());// 事务类型
	
				row.put("REF_KEY_1", zongzhang.getRefKey1());// 事务类型
				row.put("REF_KEY_2", zongzhang.getRefKey2());// 事务类型
				row.put("REF_KEY_3", zongzhang.getRefKey3());// 事务类型
				row.put("SP_GL_IND", zongzhang.getSpglind());// 事务类型
	
				inTableValue1.add(row);
			}

			
			Map<String, Object> row1 = new HashMap<String, Object>();
			row1.put("ITEMNO_ACC",zongzhang.getItemNoAcc());
			row1.put("AMT_DOCCUR", zongzhang.getAmtDocCur());// 金额
			row1.put("CURRENCY", zongzhang.getCurrency().toUpperCase());// 货币
			row1.put("EXCH_RATE", zongzhang.getExchRate());// 汇率
			inTableValue2.add(row1);

		}

		
		List<Map<String, Object>> inTableValue3 = new ArrayList<Map<String, Object>>();
		String EXTENSION2 = "EXTENSION2";
		Map<String, Object> row2 = new HashMap<String, Object>();
		row2.put("VALUEPART4", "2");// 页数
		inTableValue3.add(row2);
		String[] inParamsTableNames = new String[inTableValues.size() + 2];
		List<Map<String, Object>>[] inParamsTableValues = new List[inTableValues.size() + 2] ;
		inParamsTableNames[0] = CURRENCYAMOUNT;		
		inParamsTableNames[1] = EXTENSION2;
		inParamsTableValues[0] = inTableValue2;
		inParamsTableValues[1] = inTableValue3;
		Set<String> keys = inTableValues.keySet();
		int i = 2;
		for(String key:keys)
		{
			inParamsTableNames[i] = key;
			inParamsTableValues[i] = inTableValues.get(key);
			i ++;
		}
		
		

		String[] returnParameterColumns = new String[] { "OBJ_TYPE", "OBJ_KEY",
				"OBJ_SYS" };

		String[] returnTableNames = new String[1];
		returnTableNames[0] = "RETURN";
		List<String>[] returnTableColumns = new List[1];
		returnTableColumns[0] = new ArrayList<String>();
		returnTableColumns[0].add("ID");
		returnTableColumns[0].add("NUMBER");
		returnTableColumns[0].add("TYPE");
		returnTableColumns[0].add("MESSAGE");
		returnTableColumns[0].add("LOG_NO");
		returnTableColumns[0].add("LOG_MSG_NO");
		returnTableColumns[0].add("MESSAGE_V1");
		returnTableColumns[0].add("MESSAGE_V2");
		returnTableColumns[0].add("MESSAGE_V3");
		returnTableColumns[0].add("MESSAGE_V4");
		returnTableColumns[0].add("PARAMETER");
		returnTableColumns[0].add("ROW");
		returnTableColumns[0].add("FIELD");
		returnTableColumns[0].add("SYSTEM");
		

		String objKey = "";
		JCoDestination destination = null;
		
		try {
			destination = sapConnectFactory.begin();
			sapResult = sapConnectFactory
					.callFunctionForParameterInTableAndStructure(
							SAPConf.rfcName_BAPI_ACC_DOCUMENT_POST,
							null, inParamsStructureNames,
							inParamsStructureValues, inParamsTableNames,
							inParamsTableValues, returnParameterColumns, null,
							null, returnTableNames, returnTableColumns);

			objKey = (String) sapResult.getResultParams().get("OBJ_KEY");

			if ((objKey == null || objKey.length() < 18)
					&& sapResult.getResultTables().get("RETURN") != null
					&& sapResult.getResultTables().get("RETURN").size() > 0) {
				throw new SapException(
						sapResult.getResultTables().get("RETURN").get(0)
								.get("MESSAGE") == null ? sapResult
								.getResultTables().get("RETURN").get(0)
								.get("ID").toString() : sapResult
								.getResultTables().get("RETURN").get(0)
								.get("MESSAGE").toString());

			}
			sapConnectFactory.commit();
		}
		catch (SapException e) {
			sapConnectFactory.rollback();

		}
		catch (Exception e) {
			if(sapResult == null)
			{
				sapResult = new SapResult();
			}
			sapResult.setException(StringUtil.formatBRException(e));
			sapConnectFactory.rollback();

		}
		finally
		{

			sapConnectFactory.end(destination);
		}

//		String belnr = "";
//		String bukrs = "";
//		String gjahr = "";
//		if (objKey != null) {
//			if (objKey.length() >= 10) {
//				belnr = objKey.substring(10);
//			}
//			if (objKey.length() >= 14) {
//				bukrs = objKey.substring(10, 14);
//			}
//			if (objKey.length() >= 18) {
//				gjahr = objKey.substring(14, 18);
//			}
//		}

//		List<Map<String, Object>> rmsgResult = sapResult.getResultTables().get(
//				"RETURN");
//
//		for (Map<String, Object> msg : rmsgResult) {
//
//			System.out.println("ID:" + msg.get("ID"));
//			System.out.println("NUMBER:" + msg.get("NUMBER"));
//			System.out.println("TYPE:" + msg.get("TYPE"));
//			System.out.println("MESSAGE:" + msg.get("MESSAGE"));
//			System.out.println("LOG_NO:" + msg.get("LOG_NO"));
//			System.out.println("LOG_MSG_NO:" + msg.get("LOG_MSG_NO"));
//			System.out.println("MESSAGE_V1:" + msg.get("MESSAGE_V1"));
//			System.out.println("MESSAGE_V2:" + msg.get("MESSAGE_V2"));
//			System.out.println("MESSAGE_V3:" + msg.get("MESSAGE_V3"));
//			System.out.println("MESSAGE_V4:" + msg.get("MESSAGE_V4"));
//			System.out.println("PARAMETER:" + msg.get("PARAMETER"));
//			System.out.println("ROW:" + msg.get("ROW"));
//			System.out.println("FIELD:" + msg.get("FIELD"));
//			System.out.println("SYSTEM:" + msg.get("SYSTEM"));
//
//		}
		return sapResult;
	}

	
	/**
     * 冲销
     * @author  caix3
     * @param   in 冲销账务信息
     * @return  SAP 返回结果
     *          表名 ：    PT_OUT 
     *          STATUS          消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
     *          R_BELNR         会计凭证号码 
     *          ERROR_MESSAGE   错误信息
	 * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public SapResult eliminateBill(List<WriteOff> billList) throws Exception {

        SapResult sapResult = null;
        JCoDestination destination = null;
        try {
            destination = sapConnectFactory.begin();

            String[] inParamsTableNames = new String[] { "PT_IN" };
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            List<Map<String, Object>> inTableValue = new ArrayList<Map<String, Object>>();
            for (WriteOff temp : billList) {
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("BELNR", temp.getDocNo());
                row.put("BUKRS", temp.getCompCode());
                row.put("GJAHR", temp.getAccountYear());
                row.put("STGRD", temp.getReason());
                if (temp.getDocDate() != null) {
                	row.put("BUDAT", dateFormat.format(temp.getDocDate()));
                }
                inTableValue.add(row);
            }
            List<Map<String, Object>>[] inParamsTableValues = new List[] { inTableValue };
            
            String[] returnParamsTableNames = new String[] { "PT_OUT" };
            
            List<String>[] returnTableColumns = new List[1];
            returnTableColumns[0] = new ArrayList<String>();
            returnTableColumns[0].add("R_BELNR");
            returnTableColumns[0].add("STATUS");
            returnTableColumns[0].add("ERROR_MESSAGE");
            
            sapResult = sapConnectFactory.callFunctionForParameterInTable("ZMSM007", 
                    inParamsTableNames, inParamsTableValues, returnParamsTableNames, returnTableColumns);
        } catch (Exception e) {
           throw e;
        } finally {
            sapConnectFactory.end(destination);
        }

        return sapResult;
    }
    
    /**
     * 重置冲销
     * @author  caix3
     * @param   in 冲销账务信息
     * @return  SAP 返回结果
     *          表名  FBRA_OUTPUT
     *          REF_DOC_NO      参考凭证编号
     *          STATUS          消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
     *          AC_DOC_NO       会计凭证号码 
     *          ERROR_MESSAGE   消息文本
     */
    @SuppressWarnings("unchecked")
    public SapResult resetAndEliminateBill(List<ClearAccount> billList) {

        SapResult sapResult = null;
        JCoDestination destination = null;
        try {
            destination = sapConnectFactory.begin();

            String[] inParamsTableNames = new String[] { "FBRA_INPUT" };
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            List<Map<String, Object>> inTableValue = new ArrayList<Map<String, Object>>();
            for (ClearAccount temp : billList) {
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("BELNR", temp.getDocno());
                row.put("BUKRS", temp.getCompcode());
                row.put("GJAHR", temp.getAccountyear());
                row.put("STGRD", temp.getReason());
                if (temp.getAccountdate() != null) {
                    row.put("BUDAT", dateFormat.format(temp.getAccountdate()));
                }
                row.put("FUNKY", temp.getFunc());
                inTableValue.add(row);
            }
           
            List<Map<String, Object>>[] inParamsTableValues = new List[] { inTableValue };
            
            String[] returnParamsTableNames = new String[] { "FBRA_OUTPUT" };
            
            List<String>[] returnTableColumns = new List[1];
            returnTableColumns[0] = new ArrayList<String>();
            returnTableColumns[0].add("REF_DOC_NO");
            returnTableColumns[0].add("STATUS");
            returnTableColumns[0].add("AC_DOC_NO");
            returnTableColumns[0].add("ERROR_MESSAGE");
            
            sapResult = sapConnectFactory.callFunctionForParameterInTable("ZMSM008", 
                    inParamsTableNames, inParamsTableValues, returnParamsTableNames, returnTableColumns);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapConnectFactory.end(destination);
        }

        return sapResult;
    }
    
    /**
     * 

     */

    public SapResult createUser()
    {
    	SapResult sapResult = null;
        JCoDestination destination = null;
        try {
            destination = sapConnectFactory.begin();
        
          String rfcName = "BAPI_USER_CREATE1";
 
            Map<String, Object> inParams = new HashMap<String,Object>();
            inParams.put("USERNAME", "yinbp");
            
            
            Map<String, Object>[] inParamsStructureValues = new Map[4];
            String[] inParamsStructureNames = new String[4];
    		inParamsStructureNames[0] = "LOGONDATA";
    		inParamsStructureValues[0] = new HashMap<String, Object>();
    		inParamsStructureValues[0].put("GLTGV", "20150827");// 凭证日期

    		inParamsStructureValues[0].put("GLTGB","20150927");// 记账日期

    		inParamsStructureValues[0].put("USTYP", "A");
    		
    		inParamsStructureNames[1] = "PASSWORD";
    		inParamsStructureValues[1] = new HashMap<String, Object>();
    		inParamsStructureValues[1].put("BAPIPWD", "Sany_002");// 凭证日期

    		inParamsStructureNames[2] = "ADDRESS";
    		inParamsStructureValues[2] = new HashMap<String, Object>();
    		inParamsStructureValues[2].put("TITLE_P", "先生");// 凭证日期
    		inParamsStructureValues[2].put("FIRSTNAME", "尹");// 凭证日期
    		inParamsStructureValues[2].put("LASTNAME", "标平");// 凭证日期
    		inParamsStructureValues[2].put("DEPARTMENT", "架构科");// 凭证日期
    		inParamsStructureValues[2].put("FUNCTION", "架构工程师");// 凭证日期
    		inParamsStructureValues[2].put("ROOM_NO_P", "10006673");// 凭证日期
    		
    		inParamsStructureNames[3] = "UCLASS";
    		inParamsStructureValues[3] = new HashMap<String, Object>();
    		inParamsStructureValues[3].put("LIC_TYPE", "06");// 凭证日期
    		inParamsStructureValues[3].put("SPEC_VERS", "00");// 凭证日期
    		
    		
    		String[] inParamsTableNames = {"ADDTEL"};
            List<Map<String, Object>> inTableValue = new ArrayList<Map<String, Object>>();
	         
	        Map<String, Object> row = new HashMap<String, Object>();
	        row.put("STD_NO", "X");
	        row.put("TELEPHONE", "18807409059");	
	        row.put("R_3_USER", "3");
	        row.put("CONSNUMBER", "001");	              
	        inTableValue.add(row);	        
			List<Map<String, Object>>[] inParamsTableValues = new List[]{inTableValue};
    		
			String[] returnTableNames = {"RETURN"};
			
			List<String>[] returnTableColumns = new List[1];
			returnTableColumns[0] = new ArrayList<String>();
	          returnTableColumns[0].add("TYPE");
	          returnTableColumns[0].add("ID");
	          returnTableColumns[0].add("NUMBER");
	          returnTableColumns[0].add("MESSAGE");
	          returnTableColumns[0].add("LOG_NO");
	          returnTableColumns[0].add("LOG_MSG_NO");
	          returnTableColumns[0].add("MESSAGE_V1");
	          returnTableColumns[0].add("MESSAGE_V2");
	          returnTableColumns[0].add("MESSAGE_V3");
	          returnTableColumns[0].add("MESSAGE_V4");
	          returnTableColumns[0].add("PARAMETER");
	          returnTableColumns[0].add("ROW");
	          
	  
	          returnTableColumns[0].add("FIELD");
	        returnTableColumns[0].add("SYSTEM");
	         
	        String[] returnParamsStructureNames = null;
			List<String>[] returnParamsStructureColumns = null;
			
			String[] returnParameterColumns = null;
			
			
            sapResult = sapConnectFactory.callFunctionForParameterInTableAndStructure(rfcName, inParams, inParamsStructureNames, inParamsStructureValues, 
            		inParamsTableNames, inParamsTableValues, returnParameterColumns, 
            		returnParamsStructureNames, returnParamsStructureColumns,
            		returnTableNames, returnTableColumns);

            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapConnectFactory.end(destination);
        }

        return sapResult;
    }
    
    public SapResult createUserUnTX(String userAccount,String worknumber)
    {
    	SapResult sapResult = null;
        
        try {
           
        
          String rfcName = "BAPI_USER_CREATE1";
 
            Map<String, Object> inParams = new HashMap<String,Object>();
            inParams.put("USERNAME", userAccount);
            
            
            Map<String, Object>[] inParamsStructureValues = new Map[4];
            String[] inParamsStructureNames = new String[4];
    		inParamsStructureNames[0] = "LOGONDATA";
    		inParamsStructureValues[0] = new HashMap<String, Object>();
    		inParamsStructureValues[0].put("GLTGV", "20150827");// 凭证日期

    		inParamsStructureValues[0].put("GLTGB","20150927");// 记账日期

    		inParamsStructureValues[0].put("USTYP", "A");
    		
    		inParamsStructureNames[1] = "PASSWORD";
    		inParamsStructureValues[1] = new HashMap<String, Object>();
    		inParamsStructureValues[1].put("BAPIPWD", "Sany_002");// 凭证日期

    		inParamsStructureNames[2] = "ADDRESS";
    		inParamsStructureValues[2] = new HashMap<String, Object>();
    		inParamsStructureValues[2].put("TITLE_P", "先生");// 凭证日期
    		inParamsStructureValues[2].put("FIRSTNAME", "尹");// 凭证日期
    		inParamsStructureValues[2].put("LASTNAME", "标平");// 凭证日期
    		inParamsStructureValues[2].put("DEPARTMENT", "架构科");// 凭证日期
    		inParamsStructureValues[2].put("FUNCTION", "架构工程师");// 凭证日期
    		inParamsStructureValues[2].put("ROOM_NO_P", worknumber);// 凭证日期
    		
    		inParamsStructureNames[3] = "UCLASS";
    		inParamsStructureValues[3] = new HashMap<String, Object>();
    		inParamsStructureValues[3].put("LIC_TYPE", "06");// 凭证日期
    		inParamsStructureValues[3].put("SPEC_VERS", "00");// 凭证日期
    		
    		
    		String[] inParamsTableNames = {"ADDTEL"};
            List<Map<String, Object>> inTableValue = new ArrayList<Map<String, Object>>();
	         
	        Map<String, Object> row = new HashMap<String, Object>();
	        row.put("STD_NO", "X");
	        row.put("TELEPHONE", "18807409059");	
	        row.put("R_3_USER", "3");
	        row.put("CONSNUMBER", "001");	              
	        inTableValue.add(row);	        
			List<Map<String, Object>>[] inParamsTableValues = new List[]{inTableValue};
    		
			String[] returnTableNames = {"RETURN"};
			
			List<String>[] returnTableColumns = new List[1];
			returnTableColumns[0] = new ArrayList<String>();
	          returnTableColumns[0].add("TYPE");
	          returnTableColumns[0].add("ID");
	          returnTableColumns[0].add("NUMBER");
	          returnTableColumns[0].add("MESSAGE");
	          returnTableColumns[0].add("LOG_NO");
	          returnTableColumns[0].add("LOG_MSG_NO");
	          returnTableColumns[0].add("MESSAGE_V1");
	          returnTableColumns[0].add("MESSAGE_V2");
	          returnTableColumns[0].add("MESSAGE_V3");
	          returnTableColumns[0].add("MESSAGE_V4");
	          returnTableColumns[0].add("PARAMETER");
	          returnTableColumns[0].add("ROW");
	          
	  
	          returnTableColumns[0].add("FIELD");
	        returnTableColumns[0].add("SYSTEM");
	         
	        String[] returnParamsStructureNames = null;
			List<String>[] returnParamsStructureColumns = null;
			
			String[] returnParameterColumns = null;
			
			
            sapResult = sapConnectFactory.callFunctionForParameterInTableAndStructure(rfcName, inParams, inParamsStructureNames, inParamsStructureValues, 
            		inParamsTableNames, inParamsTableValues, returnParameterColumns, 
            		returnParamsStructureNames, returnParamsStructureColumns,
            		returnTableNames, returnTableColumns);

            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             
        }

        return sapResult;
    }
    public SapResult test() {

        SapResult sapResult = null;
        JCoDestination destination = null;
        try {
            destination = sapConnectFactory.begin();
//
//            String[] inParamsTableNames = new String[] { "FBRA_INPUT" };
//            
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//            List<Map<String, Object>> inTableValue = new ArrayList<Map<String, Object>>();
//            for (ClearAccount temp : billList) {
//                Map<String, Object> row = new HashMap<String, Object>();
//                row.put("BELNR", temp.getDocno());
//                row.put("BUKRS", temp.getCompcode());
//                row.put("GJAHR", temp.getAccountyear());
//                row.put("STGRD", temp.getReason());
//                if (temp.getAccountdate() != null) {
//                    row.put("BUDAT", dateFormat.format(temp.getAccountdate()));
//                }
//                row.put("FUNKY", temp.getFunc());
//                inTableValue.add(row);
//            }
//           
//            List<Map<String, Object>>[] inParamsTableValues = new List[] { inTableValue };
//            
//            String[] returnParamsTableNames = new String[] { "FBRA_OUTPUT" };
//            
//            List<String>[] returnTableColumns = new List[1];
//            returnTableColumns[0] = new ArrayList<String>();
//            returnTableColumns[0].add("REF_DOC_NO");
//            returnTableColumns[0].add("STATUS");
//            returnTableColumns[0].add("AC_DOC_NO");
//            returnTableColumns[0].add("ERROR_MESSAGE");
//            
          String rfcName = "ZRFC_MM_DDXD";
//			Map<String, Object> inParams, String[] returnParameterColumns,
//			String[] returnTableNames, List<String>[] returnTableColumns
            Map<String, Object> inParams = new HashMap<String,Object>();
            inParams.put("I_GETALL", "X");
            inParams.put("I_LOW", 1);
            inParams.put("I_HIGH", 2);
            
            String[] inParamsTableNames = {"IT_DATE"};
            List<Map<String, Object>> inTableValue = new ArrayList<Map<String, Object>>();
	         
	              Map<String, Object> row = new HashMap<String, Object>();
	              row.put("LOW", "2013-11-18");
//	              row.put("HIGH", "2013-11-18");	             
	              inTableValue.add(row);
	        
			List<Map<String, Object>>[] inParamsTableValues = new List[]{inTableValue};
			String[] returnParameterColumns = null;
			String[] returnTableNames = {"OT_DDXD"};
			
			List<String>[] returnTableColumns = new List[1];
			returnTableColumns[0] = new ArrayList<String>();
	          returnTableColumns[0].add("EBELN");
	          returnTableColumns[0].add("EBELP");
	          returnTableColumns[0].add("AEDAT");
	          returnTableColumns[0].add("UTIME");
	          returnTableColumns[0].add("MATNR");
	          returnTableColumns[0].add("WERKS");
	          returnTableColumns[0].add("LIFNR");
	          returnTableColumns[0].add("MENGE");
	          returnTableColumns[0].add("MEINS");
	          returnTableColumns[0].add("LMENG");
	          returnTableColumns[0].add("LMEIN");
	          returnTableColumns[0].add("STPRS");
	          
	  
	          returnTableColumns[0].add("BRTWR");
	        returnTableColumns[0].add("WAERS");
	        returnTableColumns[0].add("LOEKZ");
	        returnTableColumns[0].add("FRGRL");
	        returnTableColumns[0].add("FRGKE");
	        returnTableColumns[0].add("BUKRS");

//			String[] returnParamsStructureNames = {"E_RETURN"};
//			List<String>[] returnParamsStructureColumns = new List[1];
//			returnParamsStructureColumns[0] = new ArrayList<String>();
//			returnParamsStructureColumns[0].add("ZTYPE");
//			returnParamsStructureColumns[0].add("ZMESG");
	        String[] returnParamsStructureNames = null;
			List<String>[] returnParamsStructureColumns = null;
			

			
			String[] inParamsStructureNames = null; Map<String, Object>[] inParamsStructureValues = null;
            sapResult = sapConnectFactory.callFunctionForParameterInTableAndStructure(rfcName, inParams, inParamsStructureNames, inParamsStructureValues, 
            		inParamsTableNames, inParamsTableValues, returnParameterColumns, 
            		returnParamsStructureNames, returnParamsStructureColumns,
            		returnTableNames, returnTableColumns);
//            orParameterInTableAndStructure(null,rfcName,
//        			inParams, null,
//        			null,
//        			inParamsTableNames,
//        			inParamsTableValues,
//        			null,
//        			returnParamsStructureNames,
//        			returnParamsStructureColumns,
//        			returnTableNames, returnTableColumns);
//        			callFunctionForParameterAndTable(rfcName, inParams, 
//            		inParamsTableNames, 
//            		inParamsTableValues, 
//            		returnParameterColumns,
//            		returnTableNames,
//            		returnTableColumns);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapConnectFactory.end(destination);
        }

        return sapResult;
    }
}
