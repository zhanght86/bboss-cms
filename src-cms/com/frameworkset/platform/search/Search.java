package com.frameworkset.platform.search;

import java.io.IOException;
import java.io.Serializable;

public abstract class Search implements Serializable
{
	
	public static void main(String[] args) throws IOException
	{
//		Search search = new Search();
//		String result[] = search.searchway("");
//		for(int i = 0; result != null && i < result.length; i ++)
//		{
//			System.out.println(i + ":" + result[i]);
//		}
	} 
	public abstract String[] searchway(String condition,String datasource) throws IOException; 
		
}

