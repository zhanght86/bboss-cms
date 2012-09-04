package com.frameworkset.platform.dictionary;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * <p>Title: DictKeyWordManager.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date  2008-3-22 10:24:27
 * @author gao.tang
 * @version 1.0
 */
public interface DictKeyWordManager extends java.io.Serializable {
	
	public boolean defineKeyField(KeyWord keyWord);
	
	public boolean defineKeyFields(List newkeyWord, String dicttypeId);
	
	public boolean getKeyFields(String dicttypeId);
	
	public String getJavaProperty(KeyWord keyWord);
	
	/**
	 * 获取javaProperty字段值
	 * @param dicttypeid
	 * @return Map<String columnName,KeyWord keyword>
	 */
	public Map getAllKeyWords(String dicttypeid);
	
	/**
	 * 获取字典中已经定义的表的字段：
	 * @param dicttypeid
	 * @return Map<String columnName,KeyWord keyword>
	 */
	public Map getAllDictFields(String dicttypeid) ;
	
	/**
	 * 得到该字典换表后已失效的关键字段
	 * @param dicttypeId
	 * @return
	 */
	public Map getInvalidationKeys(String dicttypeId, String dbname, String tablename);
	
	/**
	 * 删除失效的关键字段
	 * @param keyWord
	 * @return
	 */
	public boolean delDefineKeyFields(List keyWord);
	
	/**
	 * 得到高级字段设置的关键字段名称
	 * @param dicttypeId
	 * @return
	 */
	public String getDicattachfieldKey(String dicttypeId);


}
