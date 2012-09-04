package com.frameworkset.platform.dbmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析Poolman.xml实体,获取参数信息
 * <p>Title: ParsedPoolman.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2008-4-23
 * @author liangbing.tao
 * @version 1.0
 */

public class ParsedPoolman {
	/**
	 * 对应poolman.xml中的management-mode标签
	 */
	private String managementModel ;
	
	/**
	 * poolman.xml中连接池实例列表
	 */
	private List dataSourceList = new ArrayList();
	
	
	public void addDataSource(ParsedDataSource dataSource){
		this.dataSourceList.add(dataSource);
	}
	
	public List getDataSourceList(){
		return this.dataSourceList;
	}
	
	public void setDataSourceList(List dataSourceList){
		this.dataSourceList = dataSourceList ;
	}

	public String getManagementModel() {
		return managementModel;
	}

	public void setManagementModel(String managementModel) {
		this.managementModel = managementModel;
	}

	
}
