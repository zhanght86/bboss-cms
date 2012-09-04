package com.frameworkset.platform.search;

import java.io.IOException;
import java.util.Map;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

import com.frameworkset.util.StringUtil;

public class CommonSearch extends Search {
	private static final Map searchResult = new ConcurrentHashMap();
	private Search search = null; 
	protected static final String[] regxspecialchars = new String[] {"^","{","}","[","]","+","|","\\","*","?","(",")","-",".",","};
	public CommonSearch(Search search)
	{
		this.search = search;
	}
	
	public CommonSearch()
	{
		this.search = search;
	}
	
	public String[] searchway(String condition,String datasource) throws IOException
	{
		String key = search.getClass().getName() + ":" + condition;
		if(searchResult.containsKey(key))
			  return (String[])searchResult.get(key);
		 String condition1 = condition;
		 String datasource1=datasource;
		if(condition1  != null)
		  {
			  for(int i = 0;  i < regxspecialchars.length; i ++)
				  condition1 = StringUtil.replace(condition1,regxspecialchars[i],"\\" + regxspecialchars[i]);
			  
		  }
		  else
			  condition1 = "";
		  
		 
		 String[] ret = search.searchway(condition1,datasource1);
		 searchResult.put(key,ret);
		 return ret;
	}
	
	

}
