package com.frameworkset.platform.cms.driver.dataloader;

import java.net.URL;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.util.DaemonThread;
import com.frameworkset.util.ResourceInitial;

/**
 * 内容管理中概览数据加载器管理接口
 * <p>Title: CMSDataLoaderManager</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-5-8 14:02:15
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSDataLoaderManager implements ResourceInitial,java.io.Serializable{
	public static CMSDataLoaderManager instance;
	private static final String configFile = "CMSDataloader.xml";
	static final CMSBaseListData default_ = new com.frameworkset.platform.cms.driver.dataloader.CMSDefaultListData();
	
	
	public Map loaders = new  ConcurrentHashMap();
	
	static 
	{
		instance = new CMSDataLoaderManager();
		try {
			instance.parseXML();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private File configure;
	
	private boolean flag = false;
	
	private CMSDataLoaderManager()
	{
		
	}
	
	public CMSBaseListData getDataLoader(String type) throws CMSDataLoadException
	{
		if(type == null || type.equals("") || type.equals("default"))
			return getDefaultDataLoader(); 
		else
			return ((DataLoaderInfo)loaders.get(type)).getDataInfo();
	}
	public CMSBaseListData getDefaultDataLoader()
	{
		return default_;
	}
	
	/**
	 * 判断给定类型的数据加载器相关的数据源的变动是否需要触发引用了这个数据加载器
	 * 的页面的重新发布
	 * @param datatype 数据加载器的名称
	 * @return
	 */
	public boolean isRecursive(String datatype) 	
	{
		if(datatype == null || datatype.equals("default"))
		{
			return true;
		}
		DataLoaderInfo information = (DataLoaderInfo)this.loaders.get(datatype);
		return information != null && information.isRecursive();
	}
	
	private void parseXML() {
		try
		{
	        /* CHANGED TO USE JAXP */
			DataLoaderParser handler = new DataLoaderParser();
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setNamespaceAware(false);
	        factory.setValidating(false);
	        SAXParser parser = factory.newSAXParser();
	        
	        URL confURL = ConfigManager.class.getClassLoader().getResource(
	            configFile);
	        if (confURL == null) {
	            ConfigManager.class.getClassLoader().getResource("/" +
	                configFile);
	        }
	  
	        
	        if (confURL == null) {
	            confURL = ClassLoader.getSystemResource(configFile);
	        }
	        if (confURL == null) {
	            confURL = ClassLoader.getSystemResource("/" + configFile);
	        }
	        
	        //configRealpath = confURL.getPath();
	        
	       
	        //System.out.println("configRealpath:"+configRealpath);
	        parser.parse(confURL.openStream(), handler);
	        Map _loaders = handler.getDataLoaders();
	        
	        loaders.putAll(_loaders);
//	        loaders.put("default",default_);
	        if (!flag ) {
//	        	this.configure = new File(configFile);
	        	
	            DaemonThread listen = new DaemonThread(configFile,
	                instance);
	            listen.start();
	            flag = true;
	        }
		}
		catch(Exception e)
		{
			
//			DataInfo default_ = new com.frameworkset.platform.cms.driver.dataloader.CMSDefaultListData();
//	        loaders.put("default",default_);
	        
		}
        
	}

	public void reinit() {
		this.loaders.clear();
		try {
			parseXML();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

	public static CMSDataLoaderManager getInstance() {
		return instance;
	}
	
	

}
