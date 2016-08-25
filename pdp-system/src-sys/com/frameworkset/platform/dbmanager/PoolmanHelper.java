package com.frameworkset.platform.dbmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
/**
 * 用JDOM解析poolman.xml的帮助类
 * <p>Title: PoolmanHelper.java</p>
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

public class PoolmanHelper {
	
	private static ParsedPoolman poolman ;
	
	static {
		try {
			 poolman = getPoolman(PoolmanHelper.class.getResourceAsStream("/poolman.xml"));
		} catch (Exception e) {
			System.out.println("读取poolman.xml出现异常!");
			e.printStackTrace();
		};
	}
	/**
	 * 获取ParsedPoolman实例
	 * @param inputstream 
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	private static ParsedPoolman getPoolman(InputStream inputstream) 
												throws IOException,JDOMException{
		ParsedPoolman poolmanConfig = new ParsedPoolman();
		if(inputstream == null){
			throw new IOException("没有发现poolman.xml");
		}
		
		SAXBuilder builder = new SAXBuilder();
		
		Document document = builder.build(inputstream);
		
		Element root = document.getRootElement();
		
		//获取management-mode元素值
		String managementModelValue = root.getChildText("management-mode");
		poolmanConfig.setManagementModel(managementModelValue);
		
		
		List dataSourceList = root.getChildren("datasource");
		
		Iterator itr = dataSourceList.iterator();
		
		while(itr.hasNext()){
			Element dataSourceElement = (Element)itr.next();
			poolmanConfig.addDataSource(getDataSource(dataSourceElement));
		}
		
		
		return poolmanConfig ;
	}
	
	/**
	 * 用JDOM获取ParsedDataSource
	 * @param 
	 * @return
	 */
	private static ParsedDataSource getDataSource(Element e){
		ParsedDataSource ds = new ParsedDataSource();
		
		ds.setDbName(e.getChildText("dbname"));
		ds.setLoadMetaData(e.getChildText("loadmetadata"));
		ds.setJndiName(e.getChildText("jndiName"));
		ds.setDriver(e.getChildText("driver"));
		ds.setUrl(e.getChildText("url"));
		ds.setUserName(e.getChildText("username"));
		ds.setPassword(e.getChildText("password"));
		ds.setTxIsolationLevel(e.getChildText("txIsolationLevel"));
		ds.setNativeResults(e.getChildText("nativeResults"));
		ds.setPoolPreparedStatements(e.getChildText("poolPreparedStatements"));
		ds.setInitialConnections(e.getChildText("initialConnections"));
		ds.setMinimumSize(e.getChildText("minimumSize"));
		ds.setMaximumSize(e.getChildText("maximumSize"));
		ds.setMaximumSoft(e.getChildText("maximumSoft"));
		ds.setMaxWait(e.getChildText("maxWait"));
		ds.setRemoveAbandoned(e.getChildText("removeAbandoned"));
		ds.setUserTimeout(e.getChildText("userTimeout"));
		ds.setSkimmerFrequency(e.getChildText("skimmerFrequency"));
		ds.setConnectionTimeout(e.getChildText("connectionTimeout"));
		ds.setShrinkBy(e.getChildText("shrinkBy"));
		ds.setKeygenerate(e.getChildText("keygenerate"));
		ds.setLogFile(e.getChildText("logFile"));
		ds.setDebugging(e.getChildText("debugging"));
		
		return ds ;
	}
	
	/**
	 * 对外提供ParsedPoolman实例
	 * @return
	 */
	public static ParsedPoolman getParsedPoolman(){
		return poolman;
	}

}
