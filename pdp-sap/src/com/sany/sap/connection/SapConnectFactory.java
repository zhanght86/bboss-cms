package com.sany.sap.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

public class SapConnectFactory implements org.frameworkset.spi.InitializingBean,org.frameworkset.spi.DisposableBean {
	private static Logger logger = Logger.getLogger(SapConnectFactory.class);
	private  static SanyDestinationDataProvider sanyDestinationDataProvider;
	static 
	{
		sanyDestinationDataProvider = new SanyDestinationDataProvider();
		sanyDestinationDataProvider
				.setDestinationDataEventListener(new DestinationDataEventListener() {
					public void deleted(String arg0) {
						logger.debug("deleted:" + arg0);
					}

					public void updated(String arg0) {
						logger.debug("updated:" + arg0);
					}
				});
		Environment
		.registerDestinationDataProvider(sanyDestinationDataProvider);
	}
	private SAPConf sapconf;
	
	private DomainSAPConf domainSAPConf;

	private String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";
	public final SapResult callFunctionForParameterInTableAndStructure(String rfcName,Map<String, Object> inParams
			,String[] inParamsTableNames,List<Map<String, Object>>[] inParamsTableValues,String[] returnParamsStructureNames,List<String>[] returnParamsStructureColumns) throws SapException
		{
			return callFunctionForParameterInTableAndStructure(
					null, rfcName, inParams, null,
					null, inParamsTableNames, inParamsTableValues, null, returnParamsStructureNames, returnParamsStructureColumns, null, null);

		}
	
	public static List<String> getDefalutReturnParamsStructureColumns(String returnTableName)
	{
		List<String> structureColumns = new ArrayList<String>(); 
		if("RETURN".equals(returnTableName))
		{
			//返回表结构
			structureColumns.add("TYPE");
	        structureColumns.add("CODE");
	        structureColumns.add("MESSAGE");
		}
		if("RETURN1".equals(returnTableName))
		{
			//返回表结构
	        structureColumns= new ArrayList<String>();
	        structureColumns.add("TYPE");
	        structureColumns.add("ID");
	        structureColumns.add("NUM");
	        structureColumns.add("MESSAGE");
		}
		
		if("HR_RETURN".equals(returnTableName))
		{
			//返回表结构
	        structureColumns= new ArrayList<String>();
	        structureColumns.add("PERNR");
	        structureColumns.add("MSGTY");
	        structureColumns.add("INFTY");
	        structureColumns.add("SUBTY");
	        structureColumns.add("ACTIO");

		}
        return structureColumns;
		
	}
	@Override
	public void afterPropertiesSet() throws Exception {
//		sanyDestinationDataProvider = new SanyDestinationDataProvider();
//		sanyDestinationDataProvider
//				.setDestinationDataEventListener(new DestinationDataEventListener() {
//					public void deleted(String arg0) {
//						logger.debug("deleted:" + arg0);
//					}
//
//					public void updated(String arg0) {
//						logger.debug("updated:" + arg0);
//					}
//				});
//		
//		Environment
//		.registerDestinationDataProvider(sanyDestinationDataProvider);

		if(domainSAPConf == null)//通过ip连接sap服务器
		{
			Properties connectProperties = new Properties();
			connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,
					sapconf.getHost());
			connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,
					sapconf.getSysnr());
			connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
					sapconf.getClient());
			connectProperties.setProperty(DestinationDataProvider.JCO_USER,
					sapconf.getUser());
			connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
					sapconf.getPassward());
			connectProperties.setProperty(DestinationDataProvider.JCO_LANG,
					sapconf.getLang());
	
			connectProperties.setProperty(
					DestinationDataProvider.JCO_POOL_CAPACITY,
					sapconf.getJco_pool_capacity() + "");
			connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,
					sapconf.getJco_peak_limit() + "");
			this.addJcoDestinationData(ABAP_AS_POOLED, connectProperties);
		}
		else//通过域名连接sap服务器
		{
			
			  //connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "318");//集团号，生产机318，测试机150
			//  connectProperties.setProperty(DestinationDataProvider.JCO_USER," ");//用户名
			//  connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,"");//密码
			//  connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "en");//语言
			//  connectProperties.setProperty(DestinationDataProvider.JCO_MSHOST,"sapci.sany.com.cn");//消息服务器，生产机sapci.sany.com.cn
			//  connectProperties.setProperty(DestinationDataProvider.JCO_GROUP, "RFC");//登录组名称，测试机和生产机均为RFC
			//  connectProperties.setProperty(DestinationDataProvider.JCO_MSSERV,"3600");//消息服务器端口号，生产机：3600，测试机3601
			Properties connectProperties = new Properties();
			connectProperties.setProperty(DestinationDataProvider.JCO_MSHOST,
					domainSAPConf.getHost());
			connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,
					domainSAPConf.getHost());
			connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,
					domainSAPConf.getSysnr());
			connectProperties.setProperty(DestinationDataProvider.JCO_GROUP,
					domainSAPConf.getJco_group());
			connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
					domainSAPConf.getClient());
			connectProperties.setProperty(DestinationDataProvider.JCO_USER,
					domainSAPConf.getUser());
			connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
					domainSAPConf.getPassward());
			connectProperties.setProperty(DestinationDataProvider.JCO_LANG,
					domainSAPConf.getLang());
			connectProperties.setProperty(DestinationDataProvider.JCO_MSSERV,
					domainSAPConf.getJco_msserv());
	
			connectProperties.setProperty(
					DestinationDataProvider.JCO_POOL_CAPACITY,
					domainSAPConf.getJco_pool_capacity() + "");
			connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,
					domainSAPConf.getJco_peak_limit() + "");
			this.addJcoDestinationData(ABAP_AS_POOLED, connectProperties);
		}

		

	}

	private static Semaphore semaphore = new Semaphore(1);

	private void addJcoDestinationData(String destinationName,
			java.util.Properties jcoproperties) {
		try {
			semaphore.acquire();
			if (sanyDestinationDataProvider
					.getDestinationProperties(destinationName) == null) {
				logger.debug("Add destinationName:" + destinationName
						+ ",jcoproperties:" + jcoproperties);
				sanyDestinationDataProvider.addDestination(destinationName,
						jcoproperties);
			} else {
				logger.debug("destinationName:" + destinationName
						+ " alread exist");
			}
		} catch (Exception e) {
			logger.error("addJcoDestinationData error destinationName["+destinationName+"],"+jcoproperties ,e);
		} finally {
			semaphore.release();
		}
	}

	public final JCoDestination getJcoDestination(String destinationName)
			throws SapException {
		try {
			return JCoDestinationManager.getDestination(destinationName);
		} catch (JCoException e) {

			throw new SapException(e);
		}

	}

	public final JCoDestination getJcoDestination() throws SapException {
		try {
			return JCoDestinationManager.getDestination(this
					.getABAP_AS_POOLED());
		} catch (JCoException e) {

			throw new SapException(e);
		}

	}

	/**
	 * 调用RFC返回表格
	 * 
	 * @param rfcName
	 *            RFC名称
	 * @param inParams
	 *            传入参数
	 * @param returnTableNames
	 *            需要返回的表格名称
	 * @param returnTableColumns
	 *            各表格中需要返回的字段名称
	 * @return 
	 *         返回数据表格的列表，列表长度与returnTableNames的长度一致，各表格中的字段与returnTableColumns中的字段一致
	 * @throws SapException
	 */
	public List<Map<String, Object>>[] callFunctionForTable(String rfcName,
			Map<String, Object> inParams, String[] returnTableNames,
			String[][] returnTableColumns) throws SapException {

		if (rfcName == null || rfcName.length() <= 0) {
			logger.debug("rfc name is empty");
			return null;
		}

		if (returnTableNames == null || returnTableNames.length <= 0) {
			logger.debug("return table names are empty");
			return null;
		}

		List<Map<String, Object>>[] retRecords = null;

		JCoDestination destination = null;
		boolean needend = false;
		try {
			if(this.destination_ != null)
			{
				destination = destination_;
			}
			else
			{
				destination = JCoDestinationManager
						.getDestination(ABAP_AS_POOLED);
				needend = true;
			}
			JCoFunction function = destination.getRepository().getFunction(
					rfcName);
			if (function == null)
				throw new SapException("获取函数[" + rfcName
						+ "]失败：函数不存在，SAP服务器信息为\r\n" + destination.toString());
			if (inParams != null && inParams.size() > 0) {
				Set<String> inParamsKeys = inParams.keySet();
				for (String inParamsKey : inParamsKeys) {
					function.getImportParameterList().setValue(inParamsKey,
							inParams.get(inParamsKey));
				}
			}
			logger.debug("JCoFunction ["+rfcName+"]:" + function);
			function.execute(destination);

			retRecords = new ArrayList[returnTableNames.length];

			for (int i = 0; i < returnTableNames.length; i++) {
				String tableName = returnTableNames[i];

				List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
				JCoTable table = function.getTableParameterList().getTable(
						tableName);
				
				logger.debug("Return Table["+tableName+"]:" + table);
				if (table != null && table.getNumRows() > 0) {
					
					for (int k = 0; k < table.getNumRows(); k++) {
						table.setRow(k);

						Map<String, Object> recordRow = new HashMap<String, Object>();
						for (int j = 0; j < returnTableColumns[i].length; j++) {
							String columnName = returnTableColumns[i][j];
							Object value = table.getValue(columnName);
							recordRow.put(columnName, value);
						}

						records.add(recordRow);
					}
				}

				retRecords[i] = records;
			}

		} catch (SapException e) {

			throw e;
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error(e.getMessage(), e);
			throw new SapException(e);
		}
		finally
		{
			if(needend)
			{
				this.end(destination);
			}
		}

		return retRecords;
	}

	/**
	 * 调用RFC返回数据表格
	 * 
	 * @param rfcName
	 *            RFC名称
	 * @param inParams
	 *            传入的表单参数
	 * @param inTableNames
	 *            传入的表格参数的表格名称列表
	 * @param inTableValues
	 *            掺入的表格参数的值
	 * @param returnTableNames
	 *            返回的表格名称列表
	 * @param returnTableColumns
	 *            返回表单的参数名称列表
	 * @return 
	 *         返回数据表格的列表，列表长度与returnTableNames的长度一致，各表格中的字段与returnTableColumns中的字段一致
	 * @throws SapException
	 */
	public List<Map<String, Object>>[] callFunctionForTable(String rfcName,
			Map<String, Object> inParams, String[] inTableNames,
			List<Map<String, Object>>[] inTableValues,
			String[] returnTableNames, String[][] returnTableColumns)
			throws SapException {

		if (rfcName == null || rfcName.length() <= 0) {
			System.out.println("rfc name is empty");
			return null;
		}

		if (returnTableNames == null || returnTableNames.length <= 0) {
			System.out.println("return table names are empty");
			return null;
		}

		List<Map<String, Object>>[] retRecords = null;

		JCoDestination destination = null;
		boolean needend = false;
		try {
			if(this.destination_ != null)
			{
				destination = destination_;
			}
			else
			{
				destination = JCoDestinationManager
						.getDestination(ABAP_AS_POOLED);
				needend = true;
			}
			JCoFunction function = destination.getRepository().getFunction(
					rfcName);
			if (function == null)
				throw new SapException("获取函数[" + rfcName
						+ "]失败：函数不存在，SAP服务器信息为\r\n" + destination.toString());
			if (inParams != null && inParams.size() > 0) {
				Set<String> inParamsKeys = inParams.keySet();
				for (String inParamsKey : inParamsKeys) {
					function.getImportParameterList().setValue(inParamsKey,
							inParams.get(inParamsKey));
				}
			}

			for (int i = 0; i < inTableNames.length; i++) {
				String inTableName = inTableNames[i];
				List<Map<String, Object>> inTableValue = inTableValues[i];

				JCoTable inTable = function.getTableParameterList().getTable(
						inTableName);
				for (Map<String, Object> inMap : inTableValue) {
					inTable.appendRow();
					Set<String> inMapKey = inMap.keySet();
					for (String key : inMapKey) {
						Object keyValue = inMap.get(key);
						inTable.setValue(key, keyValue);
					}
				}
			}
			logger.debug("JCoFunction ["+rfcName+"]:" + function);
			function.execute(destination);

			retRecords = new ArrayList[returnTableNames.length];

			for (int i = 0; i < returnTableNames.length; i++) {
				String tableName = returnTableNames[i];

				List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
				JCoTable table = function.getTableParameterList().getTable(
						tableName);
				logger.debug("Return table ["+tableName+"]:" + table);
				if (table != null && table.getNumRows() > 0) {
					for (int k = 0; k < table.getNumRows(); k++) {
						table.setRow(k);

						Map<String, Object> recordRow = new HashMap<String, Object>();
						for (int j = 0; j < returnTableColumns[i].length; j++) {
							String columnName = returnTableColumns[i][j];
							Object value = table.getValue(columnName);
							recordRow.put(columnName, value);
						}

						records.add(recordRow);
					}
				}

				retRecords[i] = records;
			}

		} catch (SapException e) {
			throw e;
		} catch (Exception e) {

			// logger.error(e.getMessage(), e);
			throw new SapException(e);
		}
		finally
		{
			if(needend)
			{
				this.end(destination);
			}
		}
		return retRecords;
	}

	/**
	 * 调用RFC返回表单
	 * 
	 * @param rfcName
	 *            RFC名称
	 * @param inParams
	 *            传入参数
	 * @param returnParameterColumns
	 *            返回表单的参数名称列表
	 * @return 返回表单的键值对表格，键名称与returnParameterColumns对应
	 * @throws SapException
	 */
	public Map<String, Object> callFunctionForParameter(String rfcName,
			Map<String, Object> inParams, String[] returnParameterColumns)
			throws SapException {

		if (rfcName == null || rfcName.length() <= 0) {
			System.out.println("rfc name is empty");
			return null;
		}

		if (returnParameterColumns == null
				|| returnParameterColumns.length <= 0) {
			System.out.println("return parameter columns are empty");
			return null;
		}

		Map<String, Object> retRecords = null;

		JCoDestination destination = null;
		boolean needend = false;
		try {
			if(this.destination_ != null)
			{
				destination = destination_;
			}
			else
			{
				destination = JCoDestinationManager
						.getDestination(ABAP_AS_POOLED);
				needend = true;
			}
			JCoFunction function = destination.getRepository().getFunction(
					rfcName);
			if (function == null)
				throw new SapException("获取函数[" + rfcName
						+ "]失败：函数不存在，SAP服务器信息为\r\n" + destination.toString());
			
			if (inParams != null && inParams.size() > 0) {
				Set<String> inParamsKeys = inParams.keySet();
				for (String inParamsKey : inParamsKeys) {
					function.getImportParameterList().setValue(inParamsKey,
							inParams.get(inParamsKey));
				}
			}
			logger.debug("Function ["+rfcName+"]:" + function);
			function.execute(destination);

			retRecords = new HashMap<String, Object>();

			for (int i = 0; i < returnParameterColumns.length; i++) {
				String columnName = returnParameterColumns[i];
				Object value = function.getExportParameterList().getValue(
						columnName);

				retRecords.put(columnName, value);
			}

		} catch (SapException e) {

			throw e;
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error(e.getMessage(), e);
			throw new SapException(e);
		}
		finally
		{
			if(needend)
			{
				this.end(destination);
			}
		}
		return retRecords;
	}

	/**
	 * 传入表单及表格，调用RFC返回表单
	 * 
	 * @param rfcName
	 *            rfc名称
	 * @param inParams
	 *            传入表单参数
	 * @param inParamsTableNames
	 *            传入表格的表格名称列表
	 * @param inParamsTableValues
	 *            传入表格的值
	 * @param returnParameterColumns
	 *            返回表单的参数名称列表
	 * @return 返回表单的键值对列表，键与returnParameterColumns相对应
	 * @throws SapException
	 */
	public Map<String, Object> callFunctionForParameterInTable(String rfcName,
			Map<String, Object> inParams, String[] inParamsTableNames,
			List<Map<String, Object>>[] inParamsTableValues,
			String[] returnParameterColumns) throws SapException {

		if (rfcName == null || rfcName.length() <= 0) {
			logger.debug("rfc name is empty");
			return null;
		}

		if (returnParameterColumns == null
				|| returnParameterColumns.length <= 0) {
			logger.debug("return parameter columns are empty");
			return null;
		}

		Map<String, Object> retRecords = null;

		JCoDestination destination = null;
		boolean needend = false;
		try {
			if(this.destination_ != null)
			{
				destination = destination_;
			}
			else
			{
				destination = JCoDestinationManager
						.getDestination(ABAP_AS_POOLED);
				needend = true;
			}
			JCoFunction function = destination.getRepository().getFunction(
					rfcName);
			if (function == null)
				throw new SapException("获取函数[" + rfcName
						+ "]失败：函数不存在，SAP服务器信息为\r\n" + destination.toString());
			// 传入表单参数
			if (inParams != null && inParams.size() > 0) {
				Set<String> inParamsKeys = inParams.keySet();
				for (String inParamsKey : inParamsKeys) {
					function.getImportParameterList().setValue(inParamsKey,
							inParams.get(inParamsKey));
				}
			}
			// 传入表格参数
			if (inParamsTableNames != null && inParamsTableNames.length > 0) {
				for (int i = 0; i < inParamsTableNames.length; i++) {
					String inTableName = inParamsTableNames[i];
					List<Map<String, Object>> inTableValues = inParamsTableValues[i];

					JCoTable inTable = function.getTableParameterList()
							.getTable(inTableName);

					for (Map<String, Object> rowMap : inTableValues) {
						inTable.appendRow();
						Set<String> columnSet = rowMap.keySet();
						for (String columnName : columnSet) {
							inTable.setValue(columnName, rowMap.get(columnName));
						}
					}
				}
			}
			logger.debug("Function ["+rfcName+"]:" + function);
			function.execute(destination);

			retRecords = new HashMap<String, Object>();

			for (int i = 0; i < returnParameterColumns.length; i++) {
				String columnName = returnParameterColumns[i];
				JCoParameterList exportParamsList = function
						.getChangingParameterList() == null ? function
						.getExportParameterList() : function
						.getChangingParameterList();
				Object value = exportParamsList.getValue(columnName);

				retRecords.put(columnName, value);
			}

		} catch (SapException e) {

			throw e;
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error(e.getMessage(), e);
			throw new SapException(e);
		}
		finally
		{
			if(needend)
			{
				this.end(destination);
			}
		}
		return retRecords;
	}

	/**
	 * 调用RFC，返回表单和表格
	 * 
	 * @param rfcName
	 *            rfc名称
	 * @param inParams
	 *            传入表单数据
	 * @param inParamsTableNames
	 *            传入表格名称
	 * @param inParamsTableValues
	 *            传入表格列列表
	 * @param returnParameterColumns
	 *            输出表单参数列表
	 * @param returnTableNames
	 *            输出表格名称
	 * @param returnTableColumns
	 *            输出表格中的参数列表
	 * @return
	 * @throws SapException
	 */
	public SapResult callFunctionForParameterAndTable(String rfcName,
			Map<String, Object> inParams, String[] inParamsTableNames,
			List<Map<String, Object>>[] inParamsTableValues,
			String[] returnParameterColumns, String[] returnTableNames,
			List<String>[] returnTableColumns) throws SapException {

		return callFunctionForParameterInTableAndStructure(
				null, rfcName, inParams, (String[]) null,
				(Map<String, Object>[]) null, (String[]) inParamsTableNames,
				(List<Map<String, Object>>[]) inParamsTableValues,
				(String[]) returnParameterColumns, (String[]) null,
				(List<String>[]) null, (String[]) returnTableNames,
				(List<String>[]) returnTableColumns);
	}

	/**
	 * 调用RFC，返回表单和表格
	 * 
	 * @param rfcName
	 *            rfc名称
	 * @param inParams
	 *            传入表单数据
	 * @param returnParameterColumns
	 *            输出表单参数列表
	 * @param returnTableNames
	 *            输出表格名称
	 * @param returnTableColumns
	 *            输出表格中的参数列表
	 * @return
	 * @throws SapException
	 */
	public SapResult callFunctionForParameterAndTable(String rfcName,
			Map<String, Object> inParams, String[] returnParameterColumns,
			String[] returnTableNames, List<String>[] returnTableColumns)
			throws SapException {

		return callFunctionForParameterInTableAndStructure(
				null, rfcName, inParams, (String[]) null,
				(Map<String, Object>[]) null, (String[]) null,
				(List<Map<String, Object>>[]) null,
				(String[]) returnParameterColumns, (String[]) null,
				(List<String>[]) null, (String[]) returnTableNames,
				returnTableColumns);
	}

	public final SapResult callFunctionForParameterInTableAndStructure(
			String destinationName, String rfcName) throws SapException {
		return callFunctionForParameterInTableAndStructure(destinationName,
				rfcName, null, null, null, null, null, null, null, null, null,
				null);
	}

	public final SapResult callFunctionForParameterInTableAndStructure(
			String rfcName) throws SapException {
		return callFunctionForParameterInTableAndStructure(
				null, rfcName);
	}

	public final SapResult callFunctionForParameterInTableAndStructure(
			String destinationName, String rfcName, Map<String, Object> inParams)
			throws SapException {
		return callFunctionForParameterInTableAndStructure(destinationName,
				rfcName, inParams, null, null, null, null, null, null, null,
				null, null);
	}

	public final SapResult callFunctionForParameterInTableAndStructure(

	String rfcName, Map<String, Object> inParams) throws SapException {
		return callFunctionForParameterInTableAndStructure(
				null, rfcName, inParams, null, null, null,
				null, null, null, null, null, null);
	}

	public SapResult rollback() {
		try {

			SapResult result = callFunctionForParameterInTableAndStructure(SAPConf.rfcName_rollback);
			return result;
		} catch (Exception e1) {
			return null;
		}
	}

	public SapResult commit(String wait) throws SapException {
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("WAIT", wait);

		return callFunctionForParameterInTableAndStructure(
				SAPConf.rfcName_commit, inParams);
	}

	public SapResult commit() throws SapException {
		return commit("X");
	}

	public final SapResult callFunctionForParameterInTable(String rfcName,
			String[] inParamsTableNames,
			List<Map<String, Object>>[] inParamsTableValues,
			String[] returnTableNames, List<String>[] returnTableColumns)
			throws SapException {
		return callFunctionForParameterInTableAndStructure(rfcName, null, null,
				null, inParamsTableNames, inParamsTableValues, null, null,
				null, returnTableNames, returnTableColumns);
	}

	public final SapResult callFunctionForParameterInTableAndStructure(
			String rfcName, Map<String, Object> inParams,
			String[] inParamsStructureNames,
			Map<String, Object>[] inParamsStructureValues,
			String[] inParamsTableNames,
			List<Map<String, Object>>[] inParamsTableValues,
			String[] returnParameterColumns,
			String[] returnParamsStructureNames,
			List<String>[] returnParamsStructureColumns,
			String[] returnTableNames, List<String>[] returnTableColumns)
			throws SapException {
		return callFunctionForParameterInTableAndStructure(
				null, rfcName, inParams,
				inParamsStructureNames, inParamsStructureValues,
				inParamsTableNames, inParamsTableValues,
				returnParameterColumns, returnParamsStructureNames,
				returnParamsStructureColumns, returnTableNames,
				returnTableColumns);

	}

	public final SapResult callFunctionForParameterInTableAndStructure(
			String destinationName, String rfcName,
			Map<String, Object> inParams, String[] inParamsStructureNames,
			Map<String, Object>[] inParamsStructureValues,
			String[] inParamsTableNames,
			List<Map<String, Object>>[] inParamsTableValues,
			String[] returnParameterColumns,
			String[] returnParamsStructureNames,
			List<String>[] returnParamsStructureColumns,
			String[] returnTableNames, List<String>[] returnTableColumns)
			throws SapException {

		SapResult sapResult = null;
		JCoDestination destination = null;
		boolean needend = false;
		try {
			
			if(destinationName == null)
			{
				if(this.destination_ != null)
					destination= destination_;
				else
				{
					destination = JCoDestinationManager
							.getDestination(this.getABAP_AS_POOLED());
					needend = true;
				}
			}
			else
			{
				destination = JCoDestinationManager
						.getDestination(destinationName);
				needend = true;
			}
			JCoFunction function = destination.getRepository().getFunction(
					rfcName);
			if (function == null)
				throw new SapException("获取函数[" + rfcName
						+ "]失败：函数不存在，SAP服务器信息为\r\n" + destination.toString());
			if (inParams != null) {
				Set<String> inParamsKeys = inParams.keySet();
				for (String inParamsKey : inParamsKeys)
					function.getImportParameterList().setValue(inParamsKey,
							inParams.get(inParamsKey));
			}

			if (inParamsStructureNames != null) {
				String inStructureName = null;
				Set<String> columnSet;
				for (int i = 0; i < inParamsStructureNames.length; i++) {
					inStructureName = inParamsStructureNames[i];
					Map<String, Object> inStructureValues = inParamsStructureValues[i];

					JCoStructure inStructure = function
							.getImportParameterList().getStructure(
									inStructureName);
					columnSet = inStructureValues.keySet();
					for (String columnName : columnSet) {
						inStructure.setValue(columnName,
								inStructureValues.get(columnName));
					}
					logger.debug("inStructure ["+inStructureName+"]:" + inStructure);
				}
			}

			if (inParamsTableNames != null) {
				Set<String> columnSet;
				for (int i = 0; i < inParamsTableNames.length; i++) {
					String inTableName = inParamsTableNames[i];
					List<Map<String, Object>> inTableValues = inParamsTableValues[i];
					JCoTable inTable = function.getTableParameterList()
							.getTable(inTableName);
					for (Map<String, Object> rowMap : inTableValues) {
						inTable.appendRow();
						columnSet = rowMap.keySet();
						for (String columnName : columnSet)
							inTable.setValue(columnName, rowMap.get(columnName));
					}
					logger.debug("inTable ["+inTableName+"]:" + inTable);
				}

			}
//			logger.debug("Function ["+rfcName+"]:" + function);
			long be = System.currentTimeMillis();
			function.execute(destination);
			long en = System.currentTimeMillis();

			if (returnParameterColumns != null) {
				Map<String, Object> retRecords = new HashMap<String, Object>();
				for (int i = 0; i < returnParameterColumns.length; i++) {
					String columnName = returnParameterColumns[i];
					Object value = function.getExportParameterList().getValue(
							columnName);
					retRecords.put(columnName, value);
				}
				sapResult = new SapResult();
				sapResult.setResultParams(retRecords);
			}

			if (returnParamsStructureNames != null) {
				Map<String, Map<String, Object>> resultStructures = new HashMap<String, Map<String, Object>>();
				for (int i = 0; i < returnParamsStructureNames.length; i++) {
					resultStructures.put(returnParamsStructureNames[i],
							new HashMap<String, Object>());
					JCoStructure js = function.getExportParameterList()
							.getStructure(returnParamsStructureNames[i]);
					logger.debug("Return JCoStructure ["+returnParamsStructureNames[i]+"]:" + js);
					for (int j = 0; j < returnParamsStructureColumns[i].size(); j++) {
						resultStructures
								.get(returnParamsStructureNames[i])
								.put(returnParamsStructureColumns[i].get(j),
										js.getValue(returnParamsStructureColumns[i]
												.get(j)));
					}

				}
				if (sapResult == null)
					sapResult = new SapResult();
				sapResult.setResultStructures(resultStructures);
			}

			if (returnTableNames != null) {
				Map<String, List<Map<String, Object>>> retTableRecords = new HashMap<String, List<Map<String, Object>>>();
				for (int i = 0; i < returnTableNames.length; i++) {
					JCoTable table = function.getTableParameterList().getTable(
							returnTableNames[i]);
					logger.debug("Return table ["+returnTableNames[i]+"]:" + table);
					List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
					if (table != null)
						for (int k = 0; k < table.getNumRows(); k++) {
							table.setRow(k);
							Map<String, Object> recordRow = new HashMap<String, Object>();
							for (int j = 0; j < returnTableColumns[i].size(); j++) {
								recordRow.put(returnTableColumns[i].get(j),
										table.getValue(returnTableColumns[i]
												.get(j)));
							}
							records.add(recordRow);
						}
					retTableRecords.put(returnTableNames[i], records);

				}
				if (sapResult == null)
					sapResult = new SapResult();
				sapResult.setResultTables(retTableRecords);
			}
		} catch (SapException e) {

			throw e;
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error(e.getMessage(), e);
			throw new SapException(e);
		}
		finally
		{
			if(needend)
				this.end(destination);
		}

		return sapResult;
	}

	public String getABAP_AS_POOLED() {
		return ABAP_AS_POOLED;
	}
	private JCoDestination destination_;
	public JCoDestination begin(String destination) throws SapException {
		destination_ = getJcoDestination(destination);
		com.sap.conn.jco.JCoContext.begin(destination_);
		return destination_;
	}

	public JCoDestination begin() throws SapException {
		return begin(this.getABAP_AS_POOLED());
	}

	public void end(JCoDestination destination) {
		if (destination != null) {
			try {
				com.sap.conn.jco.JCoContext.end(destination);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void end() {
		if (destination_ != null) {
			try {
				com.sap.conn.jco.JCoContext.end(destination_);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy()  {
		try {
			sanyDestinationDataProvider.deleteDestination(this.ABAP_AS_POOLED);
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try
		{
			
			Environment.unregisterDestinationDataProvider(sanyDestinationDataProvider);
			
			
			
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
		
		
		
	}

}

