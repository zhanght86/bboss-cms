package com.frameworkset.platform.cms.rssmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.pager.tags.PagerDataSet;
/**
 * RSS实现类 实现邮件订阅
 * <p>Title: RssManagerImpl.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jul 3, 2007 3:27:52 PM
 * @author ge.tao
 * @version 1.0
 */
public class RssManagerImpl implements RssManager{
	protected static String filepath = "D:/workspace/CMS/creatorcms/rss.xml";
	
	protected PagerDataSet dataSet;	
	
	/**
	 * 生成RSS种子
	 * @param dataSet 源数据
	 * @param context 上下文
	 * @param channel 频道
	 * @param rssPath rss种子保存路径
	 * RssManagerImpl.java
	 * @author: 陶格
	 */
	public void createRSS(PagerDataSet dataSet,Context context,Channel channel,String rssPath){
//		rsschannel.addItem("链接","描述","标题").setDcContributor("发送者");
//		rsschannel.addItem("链接","描述","标题").setDcCreator("创建者");
//		String channelPublishedDir = CMSUtil.getChannelPubDestinction(context.getSiteID(),String.valueOf(channel.getChannelId()));
		System.out.println("---------------------com.frameworkset.platform.cms.rssmanager----------------------");
		System.out.println("rssPath---------------2007-11-26--------------------"+rssPath);
		com.rsslibj.elements.Channel rsschannel = new com.rsslibj.elements.Channel();		
		rsschannel.setDescription("chenzhoumenhu");
		rsschannel.setLink(channel.getChannelPath());
		rsschannel.setTitle(channel.getDisplayName());
		System.out.println("rss channel mame---------------2007-11-26--------------------"+channel.getDisplayName());
		String desc = "";
		String linkPath = "";
		String siteName = context.getSite().getName();
		/* 站点首页地址 */
		String indexURL = context.getSite().getIndexFileName();
		indexURL = CMSUtil.getPublishedSitePath(context,indexURL);
		if(context instanceof DefaultContextImpl){			
			for(int i=0;i<dataSet.size();i++){
				Object obj = dataSet.getOrigineObject(i);
				if(obj instanceof Channel){
					Channel chl = (Channel)obj;
					linkPath = CMSUtil.getRssPublishedChannelPath(context,chl);
					desc = "<br>来自：<a href='"+indexURL+"'>"+siteName+"</a><br>时间： "+chl.getCreateTime()+"<br><hr size=1>";
					desc += "该频道地址 : <img src=http://www.blogcn.com/xml.gif border=0> ";
					desc += linkPath;
					rsschannel.addItem(linkPath,
							           desc,
							           chl.getDisplayName()).setDcContributor(siteName);				
				}else if(obj instanceof Document){
					Document doc = (Document)obj;
					String docid = String.valueOf(doc.getDocument_id());
					linkPath =  CMSUtil.getRssPublishedContentPath(context,channel.getChannelPath(),docid);					
					desc = "<br>来自：<<a href='"+indexURL+"'>"+siteName+"</a><br>时间： "+doc.getPublishTime()+"<br><hr size=1>";
					desc += "该频道地址 : <img src=http://www.blogcn.com/xml.gif border=0> ";		
					desc += linkPath;
					rsschannel.addItem(linkPath,
							           desc,
							           doc.getTitle()).setDcContributor(siteName);
				}
			}
		}else{			
			DefaultContextImpl defctx = new DefaultContextImpl(context.getSite().getSecondName(),context.getRequestContext());
			for(int i=0;i<dataSet.size();i++){
				Object obj = dataSet.getOrigineObject(i);
				if(obj instanceof Channel){
					Channel chl = (Channel)obj;
					linkPath = CMSUtil.getRssPublishedChannelPath(defctx,chl);
					desc = "<br>来自：<a href='"+indexURL+"'>"+siteName+"</a><br>时间： "+chl.getCreateTime()+"<br><hr size=1>";
					desc += "该频道地址 : <img src=http://www.blogcn.com/xml.gif border=0> ";
					desc += linkPath;
					rsschannel.addItem(linkPath,
							           desc,
							           chl.getDisplayName()).setDcContributor(siteName);				
				}else if(obj instanceof Document){
					Document doc = (Document)obj;
					String docid = String.valueOf(doc.getDocument_id());
					linkPath = CMSUtil.getRssPublishedContentPath(defctx,channel.getChannelPath(),docid);
					desc = "<br>来自：<a href='"+indexURL+"'>"+siteName+"</a><br>时间： "+doc.getPublishTime()+"<br><hr size=1>";
					desc += "该频道地址 : <img src=http://www.blogcn.com/xml.gif border=0> ";	
					desc += linkPath;
					rsschannel.addItem(linkPath,
							           desc,
							           doc.getTitle()).setDcContributor(siteName);
				}
			}
		}	
		rsschannel.setDcDate(new java.util.Date());
		rsschannel.setLanguage("zh-cn");
		try {
			//System.out.println(rsschannel.getFeed("rdf"));
			String xml = rsschannel.getFeed("rdf");
			xml = xml.replaceAll("UTF-8","gbk");
			System.out.println("rss xml conetent---------------2007-11-26--------------------");
			System.out.println(xml);
			System.out.println("---------------end--------------------");
			writeToFile(xml,rssPath);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 向文件输出
	 * @param info 
	 * RssManagerImpl.java
	 * @author: 陶格
	 */
	public void writeToFile(String info,String filepath) {
		try {			
			//FileUtil.writeFile(filepath,info);
			info = new String(info.getBytes("GB2312"));
			File file = new File(filepath);
			if(!file.exists()) file.createNewFile();
			else {
				file.delete();
				file.createNewFile();
			}
			FileOutputStream p = new FileOutputStream(filepath, true);
			PrintWriter fos = new PrintWriter(p);
			fos.write(info);
			fos.println();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test(String info,String filepath) {
		try {
			info = new String(info.getBytes("GB2312"));
			File file = new File(filepath);
			if(!file.exists()) file.createNewFile();
			FileOutputStream p = new FileOutputStream(filepath, true);
			PrintWriter fos = new PrintWriter(p);
			fos.write(info);
			fos.println();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param args
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 *             RssManagerImpl.java
	 * @author: 陶格
	 */
	public static void main(String[] args) throws InstantiationException,
			ClassNotFoundException, IllegalAccessException {
		RssManagerImpl impl = new RssManagerImpl();
		String content = "哈哈";
		String p1 = "d:/sitepulishtemp/1183684158609/site200/bmfw/rczp/人才招聘.xml";
		String p2 = "C:/文件.txt";
		impl.test(content,p1);
		impl.test(content,p2);
//		com.rsslibj.elements.Channel channel = new com.rsslibj.elements.Channel();
//		channel.setDescription("This is my sample channel.");
//		channel.setLink("http://localhost/");
//		channel.setTitle("My Channel");
//		channel.setImage("http://localhost/", "The Channel Image",
//				"http://localhost/foo.jpg");
//		channel.setTextInput("http://localhost/search",
//				"Search The Channel Image", "The Channel Image", "s");
//		channel.addItem("http://localhost/item1",
//				"The First Item covers details on the first item>",
//				"The First Item").setDcContributor("Joseph B. Ottinger");
//		channel.addItem("http://localhost/item2",
//				"The Second Item covers details on the second item",
//				"The Second Item").setDcCreator("Jason Bell");
		
//		com.rsslibj.elements.Channel rsschannel = new com.rsslibj.elements.Channel();
//		rsschannel.setDescription("aaaaa");
//		rsschannel.setLink("bbbbb");
//		rsschannel.setTitle("#");
//		rsschannel.addItem("http://localhost/item2",
//				"The Second Item covers details on the second item",
//				"The Second Item").setDcCreator("Jason Bell");
//		//System.out.println(channel.getFeed("rdf"));
//		new RssManagerImpl().writeToFile(rsschannel.getFeed("rdf"),filepath);
		//new RssManagerImpl().writeToFile(rsschannel.getFeed("rdf"),filepath);
		
	}
}
