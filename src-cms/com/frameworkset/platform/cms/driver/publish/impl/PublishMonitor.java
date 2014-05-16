package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.Context;

/**
 * <p>
 * Title: com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor.java
 * </p>
 * 
 * <p>
 * Description: 对发布的整个过程进行监控，包括监控产生的所有日志（成功和失败） 发布的过程如下： 发布开始 |
 * 执行发布过程--->执行发布过程失败 | | 分发发布结果 | | | 发布结束<----------|
 * 
 * 
 * 发布的过程当中需要考虑跨多个站点发布的问题，所以在记录发布过程中已经发布的页面时需要区分不同的站点
 * 页面
 * 
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: iSany
 * </p>
 * 
 * @Date 2007-1-25
 * @author biaoping.yin
 * @version 1.0
 */

public class PublishMonitor implements java.io.Serializable {
	public final static Object traceObject = new Object();
	private String uuid ;
	private String pageUrl;
	private String multidocspub;
	

	/**
	 * 子监控器列表，封装派生的子发布任务的监控器
	 */
	private List submonitors = null;

	private PublishMonitor parentMonitor;

	private Map sitePageTemplate;

	private PublishMonitor() {
//		this.tempMonitor = Collections.synchronizedList(new ArrayList());
		this.totalFailedMessage = Collections.synchronizedList(new ArrayList());
		this.totalSuccessMessage = Collections
				.synchronizedList(new ArrayList());
		this.totalMessages = Collections.synchronizedList(new ArrayList());
		this.distributeTemplateIndexs = Collections
				.synchronizedMap(new HashMap());
		distributePageIndexs = Collections.synchronizedMap(new HashMap());
		this.sitePageTemplate = Collections.synchronizedMap(new HashMap());
		this.tempFileOfPublishObject = Collections
				.synchronizedMap(new HashMap());
	}

	private PublishMonitor(List tempMonitor, List totalFailedMessage,
			List totalSuccessMessage, List totalMessages,
			Map distributeTemplateIndexs, Map distributePageIndexs,
			Map sitePageTemplate, Map tempFileOfPublishObject) {
//		this.tempMonitor = tempMonitor;
		this.totalFailedMessage = totalFailedMessage;
		this.totalSuccessMessage = totalSuccessMessage;
		this.totalMessages = totalMessages;
		this.distributeTemplateIndexs = distributeTemplateIndexs;
		this.distributePageIndexs = distributePageIndexs;
		this.sitePageTemplate = sitePageTemplate;
		this.tempFileOfPublishObject = tempFileOfPublishObject;
	}

	/**
	 * 发布开始状态
	 */
	public static final int PUBLISH_STARTED = 0;

	/**
	 * 发布过程完成
	 */
	public static final int PUBLISH_COMPLETED = 1;

	/**
	 * 分发过程完成
	 */
	public static final int DISTRIBUTE_COMPLETED = 2;

	/**
	 * 发布过程执行失败
	 */
	public static final int PUBLISH_FAILED = 3;

	/**
	 * 分发过程执行失败
	 */
	public static final int DISTRIBUTE_FAILED = 4;

	// public static final int DISTRIBUTE_ENDED = 6;

	/**
	 * 发布结束
	 */
	public static final int PUBLISH_ENDED = 5;

	public static final int ALL_FAILED = 6;

	/**
	 * 首页模版信息不存在
	 */
	public static final int SCRIPT_TEMPLATE_NOEXIST = 7;

	public static final int SCRIPT_TEMPLATEFILENOEXIST = 8;

	/**
	 * // * 首页模版文件不存在 //
	 */
	// public static final int SCRIPT_INDEXTEMPLATEFILENOEXIST = 8;
	//	
	// /**
	// * 概览模版信息不存在
	// */
	// public static final int SCRIPT_OUTLINETEMPLATE_NOEXIST = 9;
	// /**
	// * 概览模版文件不存在
	// */
	// public static final int SCRIPT_OUTLINETEMPLATEFILENOEXIST = 10;
	//	
	// /**
	// * 细览模版信息不存在
	// */
	// public static final int SCRIPT_DETAILTEMPLATE_NOEXIST = 11;
	// /**
	// * 细览模版文件不存在
	// */
	// public static final int SCRIPT_DETAILTEMPLATEFILENOEXIST = 12;

	/**
	 * 模版文件初时化失败
	 */
	public static final int SCRIPT_INITFAILED = 9;

	/**
	 * 模版文件初时化成功
	 */
	public static final int SCRIPT_INITED = 10;

	/**
	 * 页面生成
	 */
	public static final int PAGE_GENERATED = 11;

	/**
	 * 页面不存在，针对页面的发布任务的状态，其他发布对象不存在这个状态
	 */
	public static final int PAGE_NOTEXIST = 12;

	public int status = -1;

	/**
	 * 存储发布成功的监控信息
	 */
	private List totalSuccessMessage = null;

	/**
	 * 存储失败监控信息
	 */
	private List totalFailedMessage = null;

	/**
	 * 存储全部发布监控信息
	 */
	private List totalMessages;

//	/**
//	 * 存储最新的发布监控信息
//	 */
//	private List tempMonitor = null;

	/**
	 * 模版附件拷贝记录,模版发布到标识具体的频道下面时只需要拷贝一次模版的附件 Map<templateid+":"+identity,traceObject>
	 */
	private Map distributeTemplateIndexs;

	/**
	 * 特定标识的模版的发布临时文件是否已经生成 Map<templateid+":"+identity,traceObject>
	 */
	private Map tempFileOfPublishObject;

	/**
	 * 发布页面索引，对于站点下需要发布的页面在一次发布任务中只发布一次，对于后续发布该页面的请求忽略不计 Map<pageUrl,traceObject>
	 */
	private Map distributePageIndexs;

	/**
	 * 是否记录日志的标示 缺省false 记录日志
	 */
	private boolean notRecordMsg;

	/**
	 * 添加发布成功消息
	 * 
	 * @param msg
	 * @param time
	 */
	public void addSuccessMessage(String msg, Date time, String[] publisher) {
		System.out.println("Success msg=" + msg );
		if (this.notRecordMsg) {
			// System.out.println("drop success Msg1");
		} else {
			//System.out.println("Success msg=" + msg );
			PublishMessage message = new PublishMessage(msg, time, publisher);
			this.totalSuccessMessage.add(message);
			this.totalMessages.add(message);
//			tempMonitor.add(message);
		}
	}

	/**
	 * 添加发布成功消息
	 * 
	 * @param msg
	 * @param time
	 */
	public void addSuccessMessage(String msg, String[] publisher) {
		System.out.println("Success msg=" + msg );
		if (this.notRecordMsg) {
//			System.out.println("drop success Msg2");
		} else {
//			 System.out.println("Success msg=" + msg + ",publisher=" +
//					 publisher[0]);
			PublishMessage message = new PublishMessage(msg, new Date(),
					publisher);
			this.totalSuccessMessage.add(message);
			this.totalMessages.add(message);
//			tempMonitor.add(message);
		}
	}

	/**
	 * 获取发布过程中所有失败的消息
	 * 
	 * @return List<PublishMessage>
	 */
	public List getAllFailedMessages() {
		return this.totalFailedMessage;
	}

	/**
	 * 获取发布过程中所有发布成功的信息
	 * 
	 * @return List<PublishMessage>
	 */
	public List getAllSuccessMessages() {
		return this.totalSuccessMessage;
	}

	/**
	 * 获取发布过程中产生的所有消息
	 * 
	 * @return List<PublishMessage>
	 */
	public List getAllMessages() {
		return this.totalMessages;
	}

	/**
	 * 添加发布失败日志
	 * 
	 * @param msg
	 * @param time
	 */
	public void addFailedMessage(String msg, Date time, String[] publisher) {
		System.out.println("Failed msg=" + msg );
		if (this.notRecordMsg) {
//			System.out.println("drop faild Msg1");
		} else {
//			 System.out.println("Failed msg=" + msg + ",publisher=" +
//			 publisher[0]);
			PublishMessage message = new PublishMessage(msg, time, publisher);
			this.totalFailedMessage.add(message);
			this.totalMessages.add(message);
//			tempMonitor.add(message);
		}
	}

	/**
	 * 添加发布失败日志
	 * 
	 * @param msg
	 * @param time
	 */
	public void addFailedMessage(String msg, String[] publisher) {
		System.out.println("Failed msg=" + msg );
		if (this.notRecordMsg) {
//			System.out.println("drop faild Msg2");
		} else {
//			 System.out.println("Failed msg=" + msg + ",publisher=" +
//			 publisher[0]);
			PublishMessage message = new PublishMessage(msg, new Date(),
					publisher);
			this.totalFailedMessage.add(message);
			this.totalMessages.add(message);
//			tempMonitor.add(message);
		}
	}

	/**
	 * 获取所有最新产生的监控消息
	 * 
	 * @return List<PublishMessage>
	 */
	public List getNewestMessages() {
//		synchronized (tempMonitor) {
//			List newest = new ArrayList(this.tempMonitor);
//			this.tempMonitor.clear();
//			return newest;
//		}
		return new ArrayList();
	}

	public static void main(String[] args) {
		String pageUrl = "i.html";
		int index = pageUrl.lastIndexOf('/');
		if (index == -1)
			index = pageUrl.lastIndexOf('\\');

		if (index == -1) {
			String fileName = pageUrl.substring(index + 1);
			// String uri = pageUrl.substring(0,index);
			System.out.println(fileName);
			// System.out.println(uri);
		} else {
			String fileName = pageUrl.substring(index + 1);
			String uri = pageUrl.substring(0, index);
			System.out.println(fileName);
			System.out.println(uri);
		}

		// List l = new ArrayList();
		// l.add("1");
		// l.add("2");
		// List ll = new ArrayList(l);
		// l.clear();
		// System.out.println("size:" + ll.size());
		// System.out.println(ll.get(0));
		// System.out.println(ll.get(1));
	}

	/**
	 * 判断当前的发布任务是否成功完成
	 * 
	 * @return
	 */
	public boolean isPublishCompleted() {
		return status == PUBLISH_COMPLETED;
	}

	public boolean isAllFailed() {
		return this.status == ALL_FAILED;
	}

	/**
	 * 判断当前的发布任务是否已经结束
	 * 
	 * @return
	 */
	public boolean isPublishEnded() {
		return (status == PUBLISH_ENDED || this.isPublishCompleted()
				|| this.isPublishFailed() || this.isDistributeFailed()
				|| this.isAllFailed() || status == -1)
				&& (this.submonitors == null || this.submonitors.size() == 0);
	}

	/**
	 * 判断发布是否已经启动
	 * 
	 * @return
	 */
	public boolean isPublishStarted() {
		return status == PUBLISH_STARTED || isPublishCompleted();
	}

	/**
	 * 判断发布过程是否失败
	 * 
	 * @return
	 */
	public boolean isPublishFailed() {
		return status == PUBLISH_FAILED;
	}

	/**
	 * 判断分发过程是否失败
	 * 
	 * @return
	 */
	public boolean isDistributeFailed() {
		return status == DISTRIBUTE_FAILED;
	}

	/**
	 * 判断分发过程是否完成
	 * 
	 * @return
	 */
	public boolean isDistributeCompleted() {
		return status == DISTRIBUTE_COMPLETED;
	}

	/**
	 * 设置当前发布任务的执行状态 如果当前任务已经完成，则从父监控器中删除当前任务对应的监控器
	 * 
	 * @param status
	 */
	public void setPublishStatus(int status) {
		this.status = status;
		if (this.isPublishEnded() && parentMonitor != null)
			this.parentMonitor.removeSubMonitor(this);

	}

	public PublishMonitor createSubPublishMonitor() {
		PublishMonitor monitor = new PublishMonitor(null,
				this.totalFailedMessage, this.totalSuccessMessage,
				this.totalMessages, this.distributeTemplateIndexs,
				this.distributePageIndexs, sitePageTemplate,
				this.tempFileOfPublishObject);
		monitor.setParentMonitor(parentMonitor);
		if (submonitors == null)
			submonitors = new ArrayList();
		submonitors.add(monitor);
		return monitor;
	}

	private void setParentMonitor(PublishMonitor parentMonitor) {
		this.parentMonitor = parentMonitor;

	}

	/**
	 * 删除已经完成的子发布任务的监控器
	 * 
	 * @param monitor
	 */
	public void removeSubMonitor(PublishMonitor monitor) {
		this.submonitors.remove(monitor);
	}

	public static PublishMonitor createPublishMonitor() {
		return new PublishMonitor();

	}

	/**
	 * 判断相应标识的模版的临时文件是否存在， 文档发布时如果同一频道下的细览模版临时文件已经存在，就不再生成该临时文件，否则生成临时文件
	 * 有助于提高文档批量处理时的发布效率
	 * 
	 * @param template
	 *            模版id
	 * @param channelid
	 *            频道id
	 * @return
	 */
	public boolean containTempFileOfPublishObject(String template,
			String identity) {

		return containTempFileOfPublishObject(template, identity, true);
	}
	
	/**
	 * 当站点中的频道下的模板第一次生成模板临时文件完成时，记录模板到堆栈中，以防在
	 * 一次发布任务的执行过程中，同一个模板临时文件在同一个站点的同一频道下生成多次
	 * @param template
	 * @param identity
	 */
	public void putTempFileOfPublishObject(String template, String identity) {
		this.tempFileOfPublishObject
				.put(template + ":" + identity, traceObject);
	}

	/**
	 * 判断相应标识的模版的临时文件是否存在， 文档发布时如果同一频道下的细览模版临时文件已经存在，就不再生成该临时文件，否则生成临时文件
	 * 有助于提高文档批量处理时的发布效率
	 * 
	 * @param template
	 *            模版id
	 * @param identity
	 *            频道id
	 * @param autoadd
	 *            是否记录该标识信息 true 记录 false 不记录
	 * @return
	 */
	public boolean containTempFileOfPublishObject(String template,
			String identity, boolean autoadd) {
		String key = template + ":" + identity;
		boolean contain = this.tempFileOfPublishObject.containsKey(key);
		if (!contain && autoadd)
			tempFileOfPublishObject.put(key, traceObject);
		return contain;
	}

	/**
	 * 判断模板在频道下是否已经发布过附件，如果有则返回true，否则返回false
	 * 如果在后续的发布过程中，出现错误，则需要将频道模版的索引清除，以便下次分发时继续分发模版的相关附件
	 * 
	 * @param template
	 *            模版id
	 * @param channelid
	 *            频道id
	 * @return
	 */
	public boolean containDistributeTemplate(String template, String identity) {
		String key = template + ":" + identity;
		boolean contain = this.distributeTemplateIndexs.containsKey(key);
		if (!contain)
			distributeTemplateIndexs.put(key, traceObject);
		return contain;
	}

	/**
	 * 将频道模版的索引清除
	 * 
	 * @param template
	 * @param channelid
	 */
	public void removeDistributeTemplate(String template, String identity) {
		String key = template + ":" + identity;
		distributeTemplateIndexs.remove(key);
	}

	/**
	 * 判断站点下的页面是否已经发布过，如果发布过则返回true，否则返回false 如果不存在则记录改页面地址，在后续的发布过程中本页面发布失败，
	 * 则将已发布页面索引中的页面清除，以便下次碰到相同的页面时继续发布这个页面
	 * @param siteid 站点id
	 * @param pagerUrl
	 *            例如：indexnews/index.html
	 * @return
	 */
	public boolean containDistributePage(String siteid,String pagerUrl, boolean autoadd) {
		String key = siteid + ":" + pagerUrl;
		boolean contain = this.distributePageIndexs.containsKey(key);
		if (!contain && autoadd)
			this.distributePageIndexs.put(key, traceObject);

		return contain;
	}

	/**
	 * 判断站点下的页面是否已经发布过，如果发布过则返回true，否则返回false 如果不存在则记录改页面地址，在后续的发布过程中本页面发布失败，
	 * 则将已发布页面索引中的页面清除，以便下次碰到相同的页面时继续发布这个页面
	 * @param siteid 站点id
	 * @param pagerUrl
	 *            例如：indexnews/index.html
	 * 
	 * @return
	 */
	public boolean containDistributePage(String siteid,String pagerUrl) {
		return containDistributePage(siteid,pagerUrl, true);
	}

	/**
	 * 将已发布页面索引中的页面清除
	 * 
	 * @param pageUrl 页面地址
	 * @param siteid 站点id
	 */
	public void removeDistributePage(String siteid,String pageUrl) {
		String key = siteid + ":" + pageUrl;
		distributeTemplateIndexs.remove(key);
	}

	/**
	 * 判断页面是否是模版
	 * 
	 * @param pateUrl
	 * @return
	 */
	public boolean isPageTemplate(Context context, String pageUrl) {
		// if(pageUrl == null || pageUrl.equals(""))
		// return false;
		String key = context.getSiteID() + ":" + pageUrl;
		Boolean contain = (Boolean) this.sitePageTemplate.get(key);

		if (contain == null) {
			try {
				if(pageUrl == null)
				{
					return false;
				}
				int index = pageUrl.lastIndexOf('/');
				if (index == -1)
					index = pageUrl.lastIndexOf('\\');

				if (index == -1) {
					boolean temp = context.getDriverConfiguration()
							.getCMSService().getTemplateManager()
							.isPageTemplate(context.getSiteID(), "", pageUrl);

					this.sitePageTemplate.put(key, new Boolean(temp));
					return temp;

				} else {
					String fileName = pageUrl.substring(index + 1);
					String uri = pageUrl.substring(0, index);
					boolean temp = context.getDriverConfiguration()
							.getCMSService().getTemplateManager()
							.isPageTemplate(context.getSiteID(), uri, fileName);

					this.sitePageTemplate.put(key, new Boolean(temp));
					return temp;
				}
			} catch (DriverConfigurationException e) {
				return false;
			}
		} else {
			return contain.booleanValue();
		}

	}

	public boolean isScriptInited() {
		return this.status == SCRIPT_INITED;
	}

	public boolean isScriptInitFailed() {
		return this.status == SCRIPT_INITFAILED;
	}

	/**
	 * 判断模版是否存在
	 * 
	 * @return
	 */
	public boolean isTemplateNoexist() {
		return this.status == SCRIPT_TEMPLATE_NOEXIST;
	}

	/**
	 * 判断模版文件是否存在
	 * 
	 * @return
	 */
	public boolean isTemplateFileNoexist() {
		return this.status == SCRIPT_TEMPLATEFILENOEXIST;
	}

	/**
	 * 判断页面是否生成
	 * 
	 * @return
	 */
	public boolean isPageGenerated() {

		return this.status == PAGE_GENERATED;
	}

	public boolean isPageNotExist() {

		return this.status == PAGE_NOTEXIST;
	}

	public boolean isNotRecordMsg() {
		return notRecordMsg;
	}
	public void clearMSGS()
	{
		if(totalFailedMessage != null)
		{
			this.totalFailedMessage.clear();
			totalFailedMessage = null;
		}
		if(totalSuccessMessage != null)
		{
			this.totalSuccessMessage.clear();
			totalSuccessMessage = null;
		}
		if(totalMessages != null)
		{
			this.totalMessages.clear();
			totalMessages = null;
		}
		
		
	}

	public void setNotRecordMsg(boolean notRecordMsg) {
		this.notRecordMsg = notRecordMsg;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getMultidocspub() {
		return multidocspub;
	}

	public void setMultidocspub(String multidocspub) {
		this.multidocspub = multidocspub;
	}

	// /**
	// * 判断模版是否存在
	// * @return
	// */
	// public boolean isTemplateNoexist()
	// {
	// return this.status == SCRIPT_DETAILTEMPLATE_NOEXIST || this.status ==
	// SCRIPT_INDEXTEMPLATE_NOEXIST
	// || this.status == SCRIPT_OUTLINETEMPLATE_NOEXIST;
	// }
	//	
	//	
	//	
	// /**
	// * 判断模版文件是否存在
	// * @return
	// */
	// public boolean isTemplateFileNoexist()
	// {
	// return this.status == SCRIPT_DETAILTEMPLATEFILENOEXIST || this.status ==
	// SCRIPT_INDEXTEMPLATEFILENOEXIST
	// || this.status == SCRIPT_OUTLINETEMPLATEFILENOEXIST;
	// }

}
