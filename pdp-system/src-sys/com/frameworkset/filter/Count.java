package com.frameworkset.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * <p>Title: Count.java</p>
 *
 * <p>Description: 计数统计器</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Sep 4, 2008 4:53:56 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class Count
{
    /**
     *  定义计数器
     *  Map<自定义key,数字>
     */
    private static final Map count = new HashMap();
    /**
     * 允许的最大下载数
     */
    private static final int maxcount = 100;
    
    /**
     * 如果允许下载则计数增1，并返回true
     * 如果达到最大下载数，计数器不变，返回false，表示不允许下载
     * @param key 可能是报表的id也可能是报表表示的页面地址，根据实际情况来定
     * @return
     */
    public static synchronized boolean increament(String key)
    {
    	Object value = count.get(key);
    	if(value == null)
    	{
    		value = new Integer(1);
    		count.put(key, value);
    		return true;
    	}
    	else
    	{
    		int i = ((Integer)value).intValue();
    		if(i < maxcount)
    		{
    			count.put(key, new Integer( i +1));
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    }
    
    public static synchronized void decreament(String key)
    {
    	Object value = count.get(key);
    	if(value == null)
    	{
    		
    		
    	}
    	else
    	{
    		int i = ((Integer)value).intValue();    		
    		count.put(key, new Integer( i - 1));
    		
    	}
    }
    

}
