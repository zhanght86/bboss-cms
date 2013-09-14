/*
 * @(#)ConfigManager.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
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
package com.frameworkset.dictionary.service;

import java.util.List;

import com.frameworkset.dictionary.bean.Item;

/**
 * 数据字典管理接口
 * @author qingl2
 * @version 2013-07-04
 */
public interface DictManager {
	/**
	 * Description:通过字典ID获取字典内容
	 * @author qingl2
	 * @version 2013-07-04
	 */
	public List<Item> getDictItemsById(String did)throws Exception;
	/**
	 * Description:通过字典ID获取字典名称
	 * @author qingl2
	 * @version 2013-07-04
	 */
	public List<String> getDictTypeById(String did)throws Exception;
}
