package com.frameworkset.platform.cms.driver.dataloader;

import org.apache.log4j.Logger;

/**
 * 数据装载器元信息
 * <p>Title: DataLoaderInfo</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-5-8 14:33:43
 * @author biaoping.yin
 * @version 1.0
 */
public class DataLoaderInfo {
	private static final Logger log = Logger.getLogger(DataLoaderInfo.class);
	private String name;
	private String type;
	private boolean single = false;
	private CMSBaseListData dataInfo ;
	/**
	 * 相关数据源的数据发生变化后是否需要重新发布引用了这个数据加载器
	 * 的页面
	 * true表示要加载
	 * false不需要，缺省为不需要
	 */
	private boolean recursive = false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSingle() {
		return single;
	}
	public void setSingle(boolean single) {
		this.single = single;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public CMSBaseListData getDataInfo() throws CMSDataLoadException
	{
		try
		{
			if(this.isSingle())
			{
				if(this.dataInfo == null)
					dataInfo = (CMSBaseListData)Class.forName(this.type).newInstance();
				return dataInfo;
			}
			else
				return dataInfo = (CMSBaseListData)Class.forName(this.type).newInstance();
		}
		catch(Exception e)
		{
			log.error("获取数据加载器异常：" + type,e);
			throw new CMSDataLoadException("获取数据加载器异常[" + type + "]:" + e.getMessage());
//			e.printStackTrace();
			
		}
		
		
	}
	public boolean isRecursive() {
		return recursive;
	}
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
	
	
	
}
