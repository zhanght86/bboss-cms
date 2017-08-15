package com.frameworkset.platform.security;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * <p>Title: LineUser.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date May 6, 2008 9:50:35 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 * @@org.jboss.cache.aop.AopMarker
 */
public class LineUser implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String userName;
	Map  lineContextsByIdx = new HashMap();
	public LineUser()
	{
		
	}
	public LineUser(String userName)
	{
		this.userName = userName;
	}
//	{
//		this.userName = userName;
//		addLineContext(sessionId,onLineUser,userName,ipaddress,loginTime);
//	}
//	public LineUser(String sessionId,OnLineUser onLineUser,String userName, String ipaddress, Date loginTime)
//	{
//		this.userName = userName;
//		addLineContext(sessionId,onLineUser,userName,ipaddress,loginTime);
//	}
	public boolean addLineContext(String sessionId,OnLineUser onLineUser,String userName,String ipaddress,
			Date loginTime, String macaddr,String DNSName,
			String serverIp,String serverport) {
		String key = sessionId;
		if(this.lineContextsByIdx.containsKey(key))
		{
			LineContext context = (LineContext)lineContextsByIdx.get(key);
			
			System.out.println("用户[" + userName + "]已经在[" + context.getLoginTime() + "]登录系统。");	
			return false;
		}
		try
		{
			lineContextsByIdx.put(key,new LineContext(sessionId,userName, ipaddress, loginTime,macaddr,DNSName,
					serverIp,serverport));
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
		
	}

	public Collection getLineContexts() {
		return lineContextsByIdx.values();
	}
	public int getContextSize()
	{
		return getLineContexts().size();
	}
	
	public LineContext getIncludeLineContextsByIdx(String key){
		return (LineContext)this.lineContextsByIdx.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param lineContext
	 */
	public void putLineContext(String key,LineContext lineContext){
		this.lineContextsByIdx.put(key, lineContext);
	}
	
	public String toString()
	{
		String ret = "User[" + userName + "],online information:";
		
		Iterator entry = lineContextsByIdx.values().iterator();
		
        while(entry.hasNext())
		{
			LineContext c = (LineContext)entry.next();
			ret += "[ip=" + c.ipaddress + ",logintime=" + format.format(c.loginTime) + "]";
		}
		return ret; 
	}
	
	/**
	 * 判定已登录用户是否处于活动状态
	 * @param ip
	 * @return
	 */
	public boolean isActive(OnLineUser onLineUser) throws NullUserException
	{
		
		Iterator temps = lineContextsByIdx.entrySet().iterator();
		List lsit = new ArrayList();
		while(temps.hasNext())
		{
			Map.Entry es = (Map.Entry)temps.next();
			
			LineContext c = (LineContext)es.getValue();
			if(c.isValid())
			{
				;
			}
			else
			{

				lsit.add(es.getKey());
			}
		}
		UserCount c = onLineUser.getUserCount();
		int j = c.getOnlineuserCount();
		for(int i = 0; i < lsit.size(); i ++)
		{
			lineContextsByIdx.remove(lsit.get(i));

//			onLineUser.getUserCount().onlineuserCount --;
			
			j --;
		}
		c.setOnlineuserCount(j>0?j:0);
		onLineUser.setUserCount(c);
		return lineContextsByIdx.size() > 0;
	}
	public boolean deleteContext(String sessionId,OnLineUser onLineUser,String ip,String macaddr)
	{
		
		
		
		String key = sessionId;
		UserCount c = onLineUser.getUserCount();
		int j = c.getOnlineuserCount();
		if(lineContextsByIdx.containsKey(key))
		{
			lineContextsByIdx.remove(key);
//			onLineUser.getUserCount().onlineuserCount --;
			j --;
			 
		}
		c.setOnlineuserCount(j>0?j:0);
		onLineUser.setUserCount(c);
		if(lineContextsByIdx.size() <= 0)
			return false;
		else
			return true;
		
		
	}
	
	public void  deleteAllContext(OnLineUser onLineUser)
	{		
		int size = lineContextsByIdx.size(); 
		if(size > 0)
		{
			lineContextsByIdx.clear();
			UserCount c = onLineUser.getUserCount();
			int j = c.getOnlineuserCount();
//			int i = onLineUser.getUserCount().onlineuserCount - size;
			j = j - size;
//			onLineUser.getUserCount().onlineuserCount = i > 0?i:0;
			c.setOnlineuserCount(j > 0?j:0);
			onLineUser.setUserCount(c);
			
			 
		}
		//if(lineContextsByIdx.size() == 0)
//			return false;
//		else
//			return true;
		
		
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
	public boolean containSession(String sessionId)
	{
		Set kes = this.lineContextsByIdx.keySet();
		if(kes == null || kes.size() == 0)
			return false;
		Iterator keys = kes.iterator();
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			if(key.equals(sessionId))
				return true;
		}
		return false;
			
	}
}
