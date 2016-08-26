package com.frameworkset.platform.cms.driver.distribute;

import java.util.Date;
import java.util.List;

import com.frameworkset.platform.cms.driver.context.BatchContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.jsp.JspFile;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.distribute.DistributeWraper.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-24
 * @author biaoping.yin
 * @version 1.0
 */
public class DistributeWraper extends Distribute implements java.io.Serializable {
	private List distributes;
	
	
	public DistributeWraper() {
		// TODO Auto-generated constructor stub
	}
	public void init(Context context,JspFile file) throws DistributeException
	{
		
		try {
			super.init(context,file);
		} catch (DistributeException e) {
			
			e.printStackTrace();
			throw e;
		}
		StringBuffer mes = new StringBuffer();
		mes.append("分发")
		   .append(context)
		   .append("到临时目录[" + context.getPublishTemppath() + "]" );
		context.getPublishMonitor().addSuccessMessage(mes.toString(),new Date(),context.getPublisher());
		distributes = context.getEnabledDistributes();
		
		for(int i = 0; distributes != null && i < distributes.size(); i ++)
		{
			try {
				((Distribute)distributes.get(i)).init(context,file);
			} catch (DistributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * 分发发布结果
	 */
	public void distribute() throws DistributeException {
//		this.before();
		for(int i = 0; distributes != null && i < distributes.size(); i ++)
		{
			Distribute instance = (Distribute)distributes.get(i);
			
			instance.distribute();
			
		}
//		this.context.getPublishMonitor().addSuccessMessage(context +" All distributes finished.", new Date(),context.getPublisher());
//		this.after();
		
	}
	
	public void before() throws DistributeException
	{
		/**
		 * 记录发布过的站点，同一个发布任务可涉及多个站点，每个站点的发布目的地配置可能都是不一样的，
		 * 因此在发布的过程中，需要记录每个站点的目的地信息，以便所有的发布任务都完成后分发所有的发布产生的文档
		 * 到不同的目录当中
		 */
		DistributeDestination distributeDestination = new DistributeDestination(context.getSite(),
															 context.getPublishTemppath(),
															 context.getPublishRootPath(),
															 context.getFTPConfig());
		context.recordSiteDistributeDestination(distributeDestination);		
		for(int i = 0; distributes != null && i < distributes.size(); i ++)
		{
			Distribute instance = (Distribute)distributes.get(i);
			
			try {
				instance.before();
			} catch (DistributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	public void after() throws DistributeException
	{
		for(int i = 0; distributes != null && i < distributes.size(); i ++)
		{
			Distribute instance = (Distribute)distributes.get(i);
			
			try {
				instance.after();
			} catch (Exception e) {
				this.context.getPublishMonitor().addFailedMessage(context + "分发" + instance + "失败：" + e.getMessage(),
																		 new Date(), 
																		 context.getPublisher());
				e.printStackTrace();
				throw new DistributeException(e.getMessage());
			}
		}
		this.context.getPublishMonitor().addSuccessMessage("分发" + context + "结束。",new Date(), context.getPublisher());
	}
	
	public void dofinally()
	{
		for(int i = 0; distributes != null && i < distributes.size(); i ++)
		{
			Distribute instance = (Distribute)distributes.get(i);
			
			try {
				instance.dofinally();
			} catch (Exception e) {
				this.context.getPublishMonitor().addFailedMessage(context + "分发" + instance + "失败：" + e.getMessage(),
																		 new Date(), 
																		 context.getPublisher());
				
			
			}
		}
	}
}
