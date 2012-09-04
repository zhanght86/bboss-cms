package com.frameworkset.dictionary;

import java.io.Serializable;
import java.util.List;

public interface DataManager extends Serializable{
	
	
	/**
	 * 根据字典类型名称,获取字典类型对象
	 * @param dictionname
	 * @return Data
	 * @throws ProfessionDataManagerException 
	 * DataManager.java
	 * @author: ge.tao
	 */
	public Data getData(String dictionname) throws ProfessionDataManagerException;
	
	
	
	/**
	 * 根据字典类型ID,获取字典类型对象
	 * @param dictionaryID
	 * @return
	 * @throws ProfessionDataManagerException 
	 * DataManager.java
	 * @author: ge.tao
	 */
	public Data getDataByID(String dictionaryID) throws ProfessionDataManagerException;

	/**
	 * 根据字典类型名称,获取字典的数据项列表
	 * @param dictionname
	 * @return List<Item>
	 * @throws ProfessionDataManagerException 
	 * DataManager.java
	 * @author: ge.tao
	 */
	public List getDataItems(String dictionname) throws ProfessionDataManagerException;
    
	/**
	 * 根据字典类型ID和数据项名称,获取数据项的真实值
	 * @param dictionname
	 * @param itemname
	 * @return 
	 * @throws ProfessionDataManagerException 
	 * DataManager.java
	 * @author: ge.tao
	 */
	public String getItemValue(String dictionname, String itemname)	throws ProfessionDataManagerException;
    
	/**
	 * 根据字典类型ID和数据项真实值,获取数据项的名称
	 * @param dictionname
	 * @param itemvalue
	 * @return
	 * @throws ProfessionDataManagerException 
	 * DataManager.java
	 * @author: ge.tao
	 */
	public String getItemName(String dictionname, String itemvalue) throws ProfessionDataManagerException;
	
	/**
	 * 根据用户ID,获取用户所在机构的指定税种的编码
	 * @param userId 用户ID
	 * @param dicttypeName 税种类型名称(字典类型名称) 如果dicttypeName=="" 取出所有的税种编码
	 * @return List<com.frameworkset.dictionary.Item>
	 * DataManager.java
	 * @author: ge.tao
	 */
	public List getTaxCodesByUserId(String userId,String dicttypeId);
	
	/**
	 * 根据机构ID,获取机构的指定税种的编码
	 * @param orgId 机构ID
	 * @param dicttypeName 税种类型名称(字典类型名称) 如果dicttypeName=="" 取出所有的税种编码
	 * @return List<com.frameworkset.dictionary.Item>
	 * DataManager.java
	 * @author: ge.tao
	 */
	public List getTaxCodesByOrgId(String orgId,String dicttypeName);
	
	/**
	 * 判断机构是否设置了
	 * @param orgid
	 * @param dictypeid
	 * @param dictdatavalue
	 * @return 
	 * DataManager.java
	 * @author: ge.tao
	 */
	public boolean hasOrgTaxcodeRelation(String orgid,String dictypeid,String dictdatavalue,String opcode);
	
	
	
	public void init()  throws ProfessionDataManagerException;
	public void reinit();
	
	public boolean removeDictFromCacheByID(String dictionaryID) throws ProfessionDataManagerException;

}
