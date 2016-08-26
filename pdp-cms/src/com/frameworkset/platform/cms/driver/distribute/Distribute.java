package com.frameworkset.platform.cms.driver.distribute;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.jsp.JspFile;

public abstract class Distribute {
	protected Context context;
	protected JspFile file;
	public abstract void distribute() throws DistributeException;
	/**
	 * 分发预处理
	 * @throws DistributeException
	 */
	public void before() throws DistributeException
	{
	}
	
	
	
	/**
	 * 分发环境的初始化
	 * @param context
	 * @param file
	 */
	public void init(Context context,
						JspFile file) throws DistributeException
	{
		this.context = context;
		this.file = file;
	}
	
	/**
	 * 分发完成后所作的处理，例如：如果是文档的发布则修改文档的发布状态，记录文档的
	 */
	public void after() throws DistributeException
	{
		
	}
	
	public void dofinally()
	{
		
	}
	
	
	
	
	
	
	
}
