package com.frameworkset.platform.search;

import java.io.IOException;
import java.io.Serializable;

public class SearchService implements Serializable{
	public static String[] search(String type,String condition,String datasource)
	{
		Search exesearch = getSearch(type);
		if(exesearch == null)
			exesearch = new DefaultSearch();	
		Search search = new CommonSearch(exesearch);
		try {
			return search.searchway(condition,datasource);
		} catch (IOException e) {
		
			return null;
		}
	}
	
	private static Search getSearch(String type) {
		if(type == null)
			return null;
		try {
			Search ret = (Search)Class.forName(type).newInstance();
			return ret;
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
		return null;
	}

	public static String[] search(String condition,String datasource)
	{
		return search(null,condition, datasource);		
	}

}
