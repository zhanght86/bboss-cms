package com.frameworkset.platform.dictionary;

import org.frameworkset.event.EventType;

/**
 * 
 * <p>Title: DictionaryChangeEvent</p>
 *
 * <p>Description: 字典信息变化事件</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-11-10 23:09:12
 * @author biaoping.yin
 * @version 1.0
 */
public interface DictionaryChangeEvent extends EventType {
	/**
	 * 字典删除事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_DELETE = new DictionaryChangeEventImpl("DICTIONARY_DELETE"); 
	/**
	 * 字典添加事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_ADD = new DictionaryChangeEventImpl("DICTIONARY_ADD"); 
	/**
	 * 字典数据项删除事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_DATA_DELETE = new DictionaryChangeEventImpl("DICTIONARY_DATA_DELETE"); 
	/**
	 * 字典数据项添加事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_DATA_ADD = new DictionaryChangeEventImpl("DICTIONARY_DATA_ADD");
	
	/**
	 * 字典数据项排序事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_DATA_ORDERCHANGE = new DictionaryChangeEventImpl("DICTIONARY_DATA_ORDERCHANGE");
	
	/**
	 * 树形字典数据项排序事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_TREE_DATA_ORDERCHANGE = new DictionaryChangeEventImpl("DICTIONARY_TREE_DATA_ORDERCHANGE");
	
	/**
	 * 字典类型更新事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_INFO_UPDATE = new DictionaryChangeEventImpl("DICTIONARY_INFO_UPDATE");
	
	/**
	 * 字典类型更新事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_DATA_INFO_UPDATE = new DictionaryChangeEventImpl("DICTIONARY_DATA_INFO_UPDATE");
	/**
	 * 字典启用停用状态更新
	 */
	public static final DictionaryChangeEvent DICTIONARY_DATA_VALIDATE_UPDATE = new DictionaryChangeEventImpl("DICTIONARY_DATA_VALIDATE_UPDATE");
	
	/**
	 * 用户-机构-编码_可见关系事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_USERORG_CHANGE_READ = new DictionaryChangeEventImpl("DICTIONARY_USERORG_CHANGE_READ");
	
	/**
	 * 用户-机构-编码_常用关系事件
	 */
	public static final DictionaryChangeEvent DICTIONARY_USERORG_CHANGE_USUAL = new DictionaryChangeEventImpl("DICTIONARY_USERORG_CHANGE_USUAL");
}
