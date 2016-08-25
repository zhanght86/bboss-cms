package com.frameworkset.platform.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.frameworkset.platform.config.ConfigManager;

/**
 * 在线用户管理类
 * @author biaoping.yin
 * @date create on 2006-8-10
 * @version 1.0
 */
public class OnLineUser {

	boolean ischecking = false;
	private Map users ;
	public static final String cluster_session_synchronize = "cluster.session.synchronize";
	public static boolean enablesessioncluster = ConfigManager.getInstance().getConfigBooleanValue(cluster_session_synchronize, false);
//	AopMapContainer countMap ; 
	public static final String cluster_session_aop = "cluster.session.aop";
	public static boolean enablesessionaop = ConfigManager.getInstance().getConfigBooleanValue(cluster_session_aop, false);
	public OnLineUser() {
		if(enablesessioncluster)
		{
//			countMap = new AopMapContainer("/onlineusers");
//			try {
//				UserCount c = (UserCount)countMap.getJBOSSTreecacheAOPContainer()
//				.getTreeCacheAop()
//				.getObject("/onlineusercount");
//				if(c == null)
//				{
//					countMap.getJBOSSTreecacheAOPContainer()
//							.getTreeCacheAop()
//							.putObject("/onlineusercount", new UserCount());
//				}
//
//			} catch (CacheException e) {
//				
//				e.printStackTrace();
//			}
//			users = countMap;
			users = new HashMap();
		}
		else
		{
			users = new HashMap();
		}
	}
	
	private UserCount onlineuserCount = new UserCount();

	public int getCount() {

		return getUserCount().getOnlineuserCount();
	}

	public boolean existUser(String userName) {		
		init();
		synchronized(users)
		{
			return users.get(userName) != null;
		}
	}
	
	/**
	 * 判断用户sessionId是否被注销掉
	 * @param userName
	 * @param sessionId
	 * @return
	 */
	public boolean existUser(String userName,String sessionId) {		
		init();
		synchronized(users)
		{
			LineUser user = (LineUser)users.get(userName) ;
			if(user == null)
				return false;
			return user.containSession(sessionId);
			
		}
	}

	public boolean deleteUser(String sessionId,String userName,String ip,String macaddr) {
		init();
		synchronized(users)
		{
			if (existUser(userName)) {
				LineUser user = (LineUser)users.get(userName);
				boolean succ = user.deleteContext(sessionId,this,ip,macaddr);
				if(!succ)
				{
					users.remove(userName);
				}
				return true;
			}
		}
		
		return false;
	}

	public void valueBound(String sessionId,String userName,String ip, String macaddr,String DNSName,
			String serverIp,String serverport) {
		init();
		Date loginTime = new Date();
//		checkValid();
		synchronized(users)
		{
			UserCount c = getUserCount();
			int j = c.getOnlineuserCount();
			if (!existUser(userName)) {
				LineUser lineuser = new LineUser(userName);
				boolean suc = lineuser.addLineContext(sessionId,this,userName,ip,loginTime,  macaddr,DNSName,
						 serverIp, serverport);
				if(suc)
				{
					users.put(userName,lineuser);
//					getUserCount().onlineuserCount ++;
					j ++;
				}
				
			} 
			else
			{
				if(ConfigManager.getInstance().getConfigBooleanValue("enablemutilogin",true) 
						|| AccessControl.isAdmin(userName)
						|| AccessControl.isOrgManager(userName))
				{
					
					LineUser lineUser = (LineUser)users.get(userName);
					boolean success = lineUser.addLineContext(sessionId,this,userName,ip,loginTime,  macaddr,DNSName,
							serverIp,serverport);
					if(success)
					{
//						getUserCount().onlineuserCount ++;
						j ++;
					}
				}
			}
			c.setOnlineuserCount(j);
			setUserCount(c);
		}
	}
	
//	/**
//	 * 检测用户是否有效
//	 */
//	private synchronized void checkValid() {	
//		init();
//		if(ischecking)
//			return ;
//		else
//		{
//			ischecking = true;
////			this.notify();
//		}
//		
//	}

	public void valueUnbound(String sessionId,String userName,String ip,String macaddr) {
		init();
		deleteUser(sessionId,userName,ip,macaddr);
	}
	
	public String getUserLoginInfo(String userName)
	{
		init();
//		synchronized(users){
			Object userLoginInfo = users.get(userName);
			return userLoginInfo == null ?"":userLoginInfo.toString();
//		}
	}

	public Collection getOnLineUser() {
		init();
		synchronized(users){	
			return this.users.values();
		}
	}
	
	public boolean removeUser(String userName){
		init();
		synchronized(users)
		{
			if (existUser(userName)) {
				LineUser user = (LineUser)users.get(userName);
				user.deleteAllContext(this);
				users.remove(userName);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param selectUserName <userName:sessionid:ip:macaddr>
	 * @param curUserName
	 * @return
	 */
	public boolean removeUsers(String[] selectUserName,String curUserName){
		init();
		boolean state = false;
		synchronized(users)
		{
			for(int i = 0; i < selectUserName.length; i++){
				String[] selNames = selectUserName[i].split("\\$");
				if(selNames.length >= 4){
					if (existUser(selNames[0])) {
//						if(selNames[0].equals(curUserName)){
							state = this.deleteUser(selNames[1], selNames[0], selNames[2],selNames[3]);
//						}else{
//							state = this.removeUser(selNames[0]);
//						}
					}
				}
				else if(selNames.length == 3)
				{
//					if (existUser(selNames[0])) {
//						if(selNames[0].equals(curUserName)){
							state = this.deleteUser(selNames[1], selNames[0], selNames[2],null);
//						}else{
//							state = this.removeUser(selNames[0]);
//						}
//					}
				}
				else if(selNames.length == 2)
				{
					state = this.deleteUser(selNames[1], selNames[0], null,null);
				}
				else{
					return false;
				}
			}
		}
		return state;
	}
	
	/**
	 * 删除所有用户会话
	 * @return
	 */
	public boolean removeAllUser(){
		init();
		synchronized(users)
		{
			users.clear();
			UserCount c = getUserCount();
			c.setOnlineuserCount(0);
			setUserCount(c);
			return true;
		}
	}
	
	/**
	 * 保留自己删除其他用户会话
	 * @param userName
	 * @return
	 */
	public boolean removeAllUserExcludeSelf(String userName,String sessionId, String ip, String macaddr,String DNSName,
			String serverIp,String serverport){
		init();
		synchronized(users)
		{
			LineUser lineUser = (LineUser)users.get(userName);
			String key = sessionId;
			LineContext lineContext = lineUser.getIncludeLineContextsByIdx(key);
			users.clear();
			if(lineContext != null){
//				System.out.println("lineUser.getContextSize()old = " + lineUser.getContextSize());
//				System.out.println("users.size()clear = " + users.size());
				lineUser.deleteAllContext(this);
				this.valueBound(sessionId, userName, ip,  macaddr,DNSName,
						serverIp,serverport);
				lineUser.putLineContext(key, lineContext);
				UserCount c = getUserCount();
				c.setOnlineuserCount(lineUser.getContextSize()); 
				this.setUserCount(c);
//				System.out.println("users.size()new = " + users.size());
				//this.valueBound(sessionId, userName, ip);
				//this.onlineuserCount = 1;+
//				System.out.println("lineUser.getContextSize()new = " + lineUser.getContextSize());
			}else{
				UserCount c = getUserCount();
				c.setOnlineuserCount(0);
				this.setUserCount(c);
			}
			return true;
		}
	}
	boolean inited = false;
	private void init()
	{
		if(users == null)
		{
			synchronized(users)
			{
				if(inited )
				{
					return;
				}
				if(enablesessioncluster)
				{
//					countMap = new AopMapContainer("/onlineusers");
//					try {
//						countMap.getJBOSSTreecacheAOPContainer()
//								.getTreeCacheAop()
//								.putObject("/onlineusercount", new UserCount());
//					} catch (CacheException e) {
//						
//						e.printStackTrace();
//					}
//					users = countMap;
					users = new HashMap();
				}
				else
				{
					users = new HashMap();
					
				}
				inited = true;
			}
		}
	}
	
	public UserCount getUserCount()
	{
		if(!enablesessioncluster)
		{
			
			return this.onlineuserCount;
		}
		else
		{
			return this.onlineuserCount;
			
//			try {
//				UserCount c = (UserCount) this.countMap.getJBOSSTreecacheAOPContainer()
//				.getTreeCacheAop()
//				.getObject("/onlineusercount");
//				return c;
//			} catch (CacheException e) {
//				
//				e.printStackTrace();
//				return this.onlineuserCount;
//			}
//			 catch (Exception e) {
//					
//				e.printStackTrace();
//				return this.onlineuserCount;
//			}
		}
	}
	
	public void setUserCount(UserCount uc)
	{
//		if(!enablesessionaop && enablesessioncluster)
//		{
//			try {
//				this.countMap.getJBOSSTreecacheAOPContainer()
//				.getTreeCacheAop()
//				.putObject("/onlineusercount",uc);
//			} catch (CacheException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public LineUser getLineUser(String userAccount)
	{
		return (LineUser)this.users.get(userAccount);
	}

}