package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.platform.cms.driver.publish.NestedPublishException;
import com.frameworkset.platform.cms.driver.publish.PublishCallBack;
import com.frameworkset.platform.cms.driver.publish.PublishEngine;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.PublishEngineImpl.java</p>
 *
 * <p>Description: 发布引擎实现类，对发布任务进行管理、调度、执行等工作</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-25
 * @author biaoping.yin
 * @version 1.0
 */

public class PublishEngineImpl implements PublishEngine {
	private final static Logger log = LoggerFactory.getLogger(PublishEngineImpl.class);
	
	/**
	 * 保存系统当前正在执行的发布任务对象
	 */
	private PublishTaskThread currentTaskThread ;
	
	/**
	 * 当前正在执行的发布任务的子任务跟踪索引，
	 * 通过跟踪索引可以避免子任务的重复发布，同步发布时
	 * Map<rootpubobjid,Map<pubobjid,Pubobject>>
	 */
	private Map<String,Map<String,Object>> currentTasktraceIndexs = new ConcurrentHashMap<String,Map<String,Object>>();	
	private Map<String,String> currentUUITaskIndexs = new HashMap<String,String>();
	private Map<String,PublishMonitor> currentMonitorIndexs = new HashMap<String,PublishMonitor>();
	public PublishMonitor getPublishMonitor(String uuid)
	{
		String jobid = currentUUITaskIndexs.get(uuid);
		if(jobid == null)
			return null;
		Map obj = this.currentTasktraceIndexs.get(jobid);
		if(obj == null)
		{
			currentMonitorIndexs.remove(jobid);
			return null;
		}
		else
		{
			return currentMonitorIndexs.get(jobid);
		}
	}
	
	
	
	/**
	 * 外部用户发布请求入口
	 */
	public void publish(PublishObject publishObject,PublishCallBack callBack) throws PublishException {
		if(callBack != null)
			try
			{
					callBack.setPublishMonitor(publishObject.getPublishMonitor());
			}
			
			catch(NestedPublishException e)
			{
				callBack.setPublishMonitor(publishObject.getInnerPublishMonitor());
				publishObject.getInnerPublishMonitor().addFailedMessage(e.getMessage(),null);
//				e.printStackTrace();
				throw e;
			}
			catch(Exception e)
			{
				callBack.setPublishMonitor(publishObject.getInnerPublishMonitor());
				publishObject.getInnerPublishMonitor().addFailedMessage(e.getMessage(),null);
//				e.printStackTrace();
				throw new NestedPublishException(e);
			}
			
			
		if(!publishObject.needPublish(callBack))
		{
			return ;
		}
		/**
		 * 判别当前任务同步执行还是异步执行
		 */
		if(publishObject.isSynchronized() 
					|| publishObject.getActionType() == PublishObject.ACTIONTYPE_VIEW)
		{
			((BasePublishCallback)callBack).setMessage("发布任务启动！");
			try{
				publishObject.getPublishMonitor()
				.addSuccessMessage("启动发布任务[" + publishObject.getPublishObjectID() + "]",publishObject.getPublisher());
				new PublishWorkThread(new PublishTask(publishObject,callBack)).rundirect();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		}
		else
		{
			if(currentTaskThread == null)
			{
				currentTaskThread = new PublishTaskThread();
				currentTaskThread.start();
				this.currentTaskThread.addPublishTask(publishObject,callBack);
				((BasePublishCallback)callBack).setMessage("发布任务启动！");
				try {
					currentTaskThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
			else if(currentTaskThread.contain(publishObject.getPublishObjectID()))
			{
				((BasePublishCallback)callBack).setMessage("发布任务已经存在于发布队列中，当前的发布被忽略了。");
				return;
			}
			else if(currentTaskThread.getCurrentPublishObject() != null 
					&& currentTaskThread.getCurrentPublishObject().publishObject.getPublishObjectID()
										.equals(publishObject.getPublishObjectID()))
			{
				((BasePublishCallback)callBack).setMessage("该文档/频道/站点发布任务正在执行。请稍后再发布！");
				return;
			}

			else
			{
				if(currentTaskThread.isRunning())
				{
					currentTaskThread.addPublishTask(publishObject,callBack);				
					((BasePublishCallback)callBack).setMessage("系统正在执行其他的发布任务。发布请求已经放入待发布队列，稍后将被执行！");
				}
				else
				{
					currentTaskThread.addPublishTask(publishObject,callBack);
					((BasePublishCallback)callBack).setMessage("发布任务启动！");
				}
			}
		}
		
		
		
	}
	
	/**
	 * 判断指定的任务是否已经发布过
	 * @param taskid
	 * @return
	 */
	public  boolean isPublishedTask(PublishObject task)
	{
		synchronized(currentTasktraceIndexs)
		{
			PublishObject root = task.getContext().getRootContext().getPublishObject();
			String rootid = root.getPublishObjectID();
			Map<String, Object> map = (Map<String, Object>)this.currentTasktraceIndexs.get(rootid);
			if(map == null)
			{
				map = new HashMap<String, Object>();
				map.put(rootid,PublishMonitor.traceObject);
				this.currentTasktraceIndexs.put(rootid,map);
				if(root.getPublishMonitor() != null)
				{
					this.currentUUITaskIndexs.put(root.getPublishMonitor().getUuid(), rootid);
					this.currentMonitorIndexs.put(rootid, root.getPublishMonitor());
				}
				
				return false;
			}
			String taskid = task.getPublishObjectID();
			if(taskid.equals(rootid))
				return true;
			boolean contain = map.containsKey(taskid);
			if(!contain)
			{
				map.put(taskid,PublishMonitor.traceObject);
			}
			
			return contain;
		}
	}
	 
	 /**
	  * 执行当前正在执行的publishObject发布任务的子任务
	  */
	public void publish(PublishObject publishObject) throws PublishException {
//		if(publishObject.getPublishObjectID() == null)
//			System.out.println(publishObject.getPublishObjectID());
		if(isPublishedTask(publishObject))
		{
			log.debug("发布任务已经执行过了：" + publishObject.getPublishObjectID());
			return;
		}
//		if(!publishObject.needPublish(null))
//			return;
		
		new PublishWorkThread(new PublishTask(publishObject,null)).rundirect();
	}
	
	
	/**
	 * 清空当前任务的在全局任务中的索引
	 */
	public void removePublishedObject(PublishObject publishObject)
	{
//		synchronized(currentTasktraceIndexs)
//		{
//			if(publishObject.isRoot())
//			{
//				
//				/**
//				 * 清空当前任务的在全局任务中的索引
//				 */
//				currentTasktraceIndexs.remove(publishObject.getPublishObjectID());
//			}
//		}
		if(publishObject.isRoot())
		{
			
			/**
			 * 清空当前任务的在全局任务中的索引
			 */
			String objid = publishObject.getPublishObjectID();
			currentTasktraceIndexs.remove(objid);
			PublishMonitor pm = this.currentMonitorIndexs.remove(objid);
			if(pm != null)
				this.currentUUITaskIndexs.remove(pm.getUuid());
		}
	}
	

//	public void view(PublishObject publishObject, PublishCallBack callBack) throws PublishException {
//		((BasePublishCallback)callBack).setMessage("开始生成预览页面！");
//		new PublishWorkThread(new PublishTask(publishObject,callBack)).run();
//		//判断生成页面是否成功，如果完成则设置生成的预览页面到回调接口中
//		if(publishObject.getPublishMonitor().isPublishCompleted())
//		{
//			
//			((BasePublishCallback)callBack).setMessage("生成预览页面完成！");
//		}
//		else
//		{
//			((BasePublishCallback)callBack).setMessage("生成预览页面失败！");
//		}		
//	}
	
	/**
	 * 
	 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.PublishEngineImpl.java</p>
	 *
	 * <p>Description: 发布任务的执行线程</p>
	 *
	 * <p>Copyright (c) 2006.10 </p>
	 *
	 * <p>Company: iSany</p>
	 * @Date 2007-2-9 9:45:29
	 * @author biaoping.yin
	 * @version 1.0
	 */
	class PublishWorkThread implements Runnable
	{
		private PublishTask currentPublishTask;
		boolean directory = false;
		public PublishWorkThread(PublishTask currentPublishTask)
		{
			this.currentPublishTask = currentPublishTask;
		}
		
		
		public void rundirect() {
			this.directory = true;
			this.run();
		
		}
		public void run() {
			try {

				currentPublishTask.publishObject.publish();
				
				if(currentPublishTask.callBack != null && 
						currentPublishTask.publishObject.getPublishMonitor().isPublishCompleted())
				{
					if(currentPublishTask.publishObject.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
					{
						((BasePublishCallback)currentPublishTask.callBack).setPageUrl(currentPublishTask.publishObject.getPublishPageUrl());
					}
					else if(currentPublishTask.publishObject.getActionType() == PublishObject.ACTIONTYPE_VIEW)
					{
						((BasePublishCallback)currentPublishTask.callBack).setViewUrl(currentPublishTask.publishObject.getPublishViewPageUrl());
					}
				}
				
//				if(!currentPublishTask.publishObject.getPublishMonitor().isPublishCompleted() && !currentPublishTask.publishObject.getPublishMonitor().isPublishFailed())
//				{
					
					if(currentPublishTask.publishObject.getPublishMonitor().isPageNotExist())
					{
						currentPublishTask.publishObject.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
					}
					
					if(currentPublishTask.publishObject.getPublishMonitor().isTemplateNoexist())
					{
						currentPublishTask.publishObject.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
					}
					
					if(currentPublishTask.publishObject.getPublishMonitor().isTemplateFileNoexist())
					{
						currentPublishTask.publishObject.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
					}
					
					if(currentPublishTask.publishObject.getPublishMonitor().isScriptInited())
					{
						currentPublishTask.publishObject.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
					}
					
					
					
//				}
					
				currentPublishTask.publishObject.getPublishMonitor()
				.addSuccessMessage("发布[" + currentPublishTask.publishObject.getPublishObjectID() + "]完成",currentPublishTask.publishObject.getPublisher());
				
			} catch (PublishException e) {
				//e.printStackTrace();
				currentPublishTask.publishObject.getPublishMonitor()
				.addFailedMessage("执行发布任务[" + currentPublishTask.publishObject.getPublishObjectID() + "]失败。",currentPublishTask.publishObject.getPublisher());
				currentPublishTask.publishObject.getPublishMonitor()
									.setPublishStatus(PublishMonitor.ALL_FAILED);
			} catch (Exception e) {
				e.printStackTrace();
				currentPublishTask.publishObject.getPublishMonitor()
				.addFailedMessage("执行发布任务[" + currentPublishTask.publishObject.getPublishObjectID() + "]失败。",currentPublishTask.publishObject.getPublisher());
				currentPublishTask.publishObject.getPublishMonitor()
				.setPublishStatus(PublishMonitor.ALL_FAILED);
			}
			finally
			{
			
				/**
				 * 清空当前任务的在全局任务中的索引
				 */
				removePublishedObject(currentPublishTask.publishObject);
				if(currentPublishTask.publishObject.isRoot())
				{
					currentPublishTask.publishObject.destroy();
				}
			}
//			if(currentPublishTask.publishObject.isRoot())
//			{
//				
//				currentTasktraceIndexs.remove(currentPublishTask.publishObject.getPublishObjectID());
//			}
//			else
//			{
//				
//			}
		}
	}
	
	

	/**
	 * 
	 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.PublishEngineImpl.java</p>
	 *
	 * <p>Description: 发布任务的调度线程</p>
	 *
	 * <p>Copyright (c) 2006.10 </p>
	 *
	 * <p>Company: iSany</p>
	 * @Date 2007-2-9 9:46:10
	 * @author biaoping.yin
	 * @version 1.0
	 */
	class PublishTaskThread extends Thread
	{
		/**
		 * 发布队列，内容管理系统同时只存在一个发布任务运行，对于其他的发布请求先存放到发布队列中
		 * 当当前发布任务结束后，如果队列不为空，则继续发布下一个任务
		 * 注意：由当前任务派生出来的所有子任务不入发布任务队列
		 */
		PublishTaskQueue publishTaskQueue = new PublishTaskQueue();
		
		/**
		 * 已经执行完毕的发布任务队列，以根任务为起点，成树状结构延伸
		 * ArrayList<PublishObject>
		 */
		List publishedTaskQueue = new ArrayList();
		
		
		
		
		public synchronized void addPublishTask(PublishObject publishObject,PublishCallBack callBack)
		{
			
			{
				this.publishTaskQueue.addPublishTask(new PublishTask(publishObject,callBack));
				notifyAll();
			}
			
		}
		
		public boolean contain(String taskid) {
			return this.publishTaskQueue.contain(taskid);
		}
		
		
		private PublishTask currentPublishTask;
		private boolean running = false;
		
		public PublishTaskThread() throws PublishException
		{

			
			
		}
		
		public void run() {
			while(true)
			{
				try {
					if(publishTaskQueue.isEmpty() 
							|| this.isRunning())
					{
						
						synchronized(this)
						{
							wait(5000);
						}
					}
				} catch (InterruptedException e) {
	
					e.printStackTrace();
				}
				if(currentPublishTask != null && currentPublishTask.publishObject.getPublishMonitor().isPublishEnded())
				{
					currentPublishTask.publishObject.getPublishMonitor()
					.addSuccessMessage("执行发布任务[" + currentPublishTask.publishObject.getPublishObjectID() + "]完成。",currentPublishTask.publishObject.getPublisher());
					running = false;
					/**
					 * 为了提升系统性能，暂时屏蔽这段代码
					 */
//					publishedTaskQueue.add(currentPublishTask.publishObject);

					/**
					 * 清空当前任务的
					 */
					removePublishedObject(currentPublishTask.publishObject);
//					currentTasktraceIndexs.remove(currentPublishTask.publishObject.getPublishObjectID());
					currentPublishTask = null ;
				}
				
				if(!this.isRunning())
					
				{
					if((currentPublishTask = publishTaskQueue.next()) != null)
					{
						
						Thread worker = new Thread(new PublishWorkThread(currentPublishTask));
						worker.start();
						running = true;
						
						
					}
					
				}
			}
			
			
		}
		
		/**
		 * 判断当前是否有发布任务正在运行
		 * @return
		 */
		public boolean isRunning()
		{
			return running;
		}
		/**
		 * 获取当前正在执行的发布任务
		 * @return
		 */
		public PublishTask getCurrentPublishObject() {
			return currentPublishTask;
		}
	}
	
	
	class PublishTask
	{
		PublishObject publishObject;
		PublishCallBack callBack;
		PublishTask(PublishObject publishObject,PublishCallBack callBack)
		{
			this.publishObject = publishObject;
			this.callBack = callBack;
		}
	}
	/**
	 * 
	 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.PublishEngineImpl.java</p>
	 *
	 * <p>Description: 待发布任务队列</p>
	 *
	 * <p>Copyright (c) 2006.10 </p>
	 *
	 * <p>Company: iSany</p>
	 * @Date 2007-2-9 9:47:48
	 * @author biaoping.yin
	 * @version 1.0
	 */
	 class PublishTaskQueue
	{
		 
		 /**
		  * 待发布任务队列
		  * List<PublishObject>
		  */
		private List publishTaskQueue = new ArrayList();
		
		/**
		 * 获取待发布队列中的等待发布任务个数
		 * @return
		 */
		public synchronized int size()
		{
			return this.publishTaskQueue.size();
		}
		
		/**
		 * 判断当前的发布任务队列是否为空
		 * @return
		 */
		public synchronized boolean isEmpty() {
			// TODO Auto-generated method stub
			return publishTaskQueue.size() == 0;
		}

		/**
		 * 从任务队列中获取下一个发布任务
		 * @return
		 */
		public synchronized PublishTask next()
		{
			if(this.publishTaskQueue.size() > 0)
				return (PublishTask)publishTaskQueue.remove(0);
			else
				return null;
		}
		
		/**
		 * 发布任务入队列
		 * @param po
		 */
		public synchronized void addPublishTask(PublishTask task)
		{
			this.publishTaskQueue.add(task);
		
		}
		
		/**
		 * 判断给定的任务id是否包含在发布队列中
		 * @param taskid
		 * @return
		 */
		public synchronized boolean contain(String taskid)
		{
			for(int i = 0; i < this.publishTaskQueue.size(); i ++)
			{
				PublishTask po = (PublishTask)this.publishTaskQueue.get(i);
				if(po.publishObject.getPublishObjectID().equals(taskid))
					return true;
				
			}
			return false;
		}		
	}
	
	public void clearTasks() {
		this.currentTasktraceIndexs.clear();
		
	}
	 
	
}
