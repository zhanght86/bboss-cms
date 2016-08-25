package com.frameworkset.dictionary.service.impl;


import java.util.List;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.dictionary.bean.Item;
import com.frameworkset.dictionary.service.DictManager;

/**
 * 数据字典管理接口实现
 * @author qingl2
 * @version 2013-07-04
 */
public class DictManagerImpl implements DictManager{
	private ConfigSQLExecutor executor;
	/**
	 * Description:通过字典ID获取字典内容
	 * @author qingl2
	 * @version 2013-07-04
	 */
	@Override
	public List<Item> getDictItemsById(String did) throws Exception {
		List<Item> datas = executor.queryList(Item.class, "getDictItemsById", did);
		return datas;
	}
	/**
	 * Description:通过字典ID获取字典名称
	 * @author qingl2
	 * @version 2013-07-04
	 */
	@Override
	public List<String> getDictTypeById(String did) throws Exception {
		List<String> datas = executor.queryList(String.class, "getDictTypeById", did);
		return datas;
	}
	
}
