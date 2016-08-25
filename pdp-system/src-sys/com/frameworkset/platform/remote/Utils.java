package com.frameworkset.platform.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.JGroupHelper;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;

import bboss.org.jgroups.Channel;
import bboss.org.jgroups.util.RspList;

/**
 * 
 * 
 * <p>
 * Title: Utils.java
 * </p>
 * 
 * <p>
 * Description: 远程处理工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date Jul 1, 2008 6:15:40 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class Utils {

	private static final Logger log = Logger.getLogger(Utils.class);
	
	
	
	public static Channel getChannel() {

		return JGroupHelper.getJGroupHelper().getChannel();
	}

	public static void main(String[] args) {

		// System.out.println("Utils.clusterstarted() = " +
		// Utils.clusterstarted());
		// System.out.println("Utils.getAppservers() = " +
		// Utils.getAppservers());
		//		
		// RspList eventrsps =
		// Utils.getRemoteDispatcher().callRemoteMethods(null,
		// "remoteEventChange", new Object[] { "test" },
		// new Class[] { String.class }, GroupRequest.GET_ALL, 0);
		// System.out.println(eventrsps);
		//		
		// RspList poolmanrsps =
		// Utils.getRemoteDispatcher().callRemoteMethods(null,
		// "getDataSourceInfos", new Object[] { },
		// new Class[] { }, GroupRequest.GET_ALL, 0);
		// System.out.println(poolmanrsps);
		//		
		// RspList poolmanstatusrsps =
		// Utils.getRemoteDispatcher().callRemoteMethods(null,
		// "getDataSourceStatus", new Object[] { },
		// new Class[] { }, GroupRequest.GET_ALL, 0);
		// System.out.println(poolmanstatusrsps);
		// RemoteHandler remoteHander =
		// (RemoteHandler)Utils.getEventDispatcher().getServerObject();
		// System.out.println("得到数据库信息 = " + remoteHander.getDataSourceInfos());
		// Utils.getEventDispatcher().callRemoteMethods(null, "remotechange",
		// new Object[] { "test" },
		// new Class[] { String.class }, GroupRequest.GET_ALL, 0);
		// getDataSourceInfos();
		// 服务器ip于端口
//		Vector vector = getAppservers();
//		boolean state = refreshReadorgname("dddd");
//		System.out.println("state = " + state);
		
		Map t = new HashMap();
		t.put("w", null);
		System.out.println("www");
		
	}

	
	//	
	// public static RpcDispatcher getPoolmanDispatcher()
	// {
	// return poolmanDispatcher;
	// }

	/**
	 * 获取对应服务器的链接池使用状况
	 * 
	 * @param ip
	 * @param port
	 * @return Map<dbname,Object[idleconnections,usedconnections,maxusedconnections,poolmetaData]>
	 */
	public static Map getDataSourceStatus(String ip, int port) {
		if (clusterstarted()) {
//			try {
//				RspList poolmanstatusrsps = Utils.getRemoteDispatcher()
//						.callRemoteMethods(buildAddresses(ip, port),
//								"getDataSourceStatus", new Object[] {},
//								new Class[] {}, GroupRequest.GET_ALL, 0);
//				if (poolmanstatusrsps != null) {
//					Map infos = (Map) poolmanstatusrsps.get(ip + ":" + port);
//					return infos;
//				}
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    try {
		        RemoteHandlerInf remoteService = ClientProxyContext.getApplicationClientBean("("+ip + ":" + port+ ")/base.serivce",RemoteHandlerInf.class);
		        return remoteService.getDataSourceStatus();
		    
		    } catch (Exception e) {
//                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      }
		}
		    
		return null;
	}

//	private static Vector buildAddresses(String ip, int port)
//			throws UnknownHostException {
//	    return JGroupHelper.getJGroupHelper().buildAddresses(ip, port);
////		IpAddress address = new IpAddress(ip, port);
////		Vector dests = new Vector();
////		dests.add(address);
////		return dests;
//	}

	/**
	 * 获取对应服务器的链接池使用状况及信息
	 * 
	 * @param ip
	 * @param port
	 * @return Map<dbname,Object[idleconnections,usedconnections,maxusedconnections,poolmetaData]>
	 */
	public static Map getDataSourceInfos(String ip, int port) {
		if (Utils.clusterstarted()) {
			try {
//
//				RspList poolmanrsps = Utils.getRemoteDispatcher()
//						.callRemoteMethods(buildAddresses(ip, port),
//								"getDataSourceInfos", new Object[] {},
//								new Class[] {}, GroupRequest.GET_ALL, 0);
//				if (poolmanrsps != null) {
//					Map infos = (Map) poolmanrsps.get(ip + ":" + port);
//					return infos;
//				}
			    RemoteHandlerInf remoteService = ClientProxyContext.getApplicationClientBean("("+ip + ":" + port+ ")/base.serivce",RemoteHandlerInf.class);
	                    return remoteService.getDataSourceInfos();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 获取集群中每台服务器的链接池的链接使用情况
	 * 
	 * @return Map<ip:port,Map<dbname,Object[idleconnections,usedconnections,maxusedconnections]>>
	 */
	public static Map getDataSourceStatus() {
		if (clusterstarted()) {
//			RspList poolmanstatusrsps = Utils.getRemoteDispatcher()
//					.callRemoteMethods(null, "getDataSourceStatus",
//							new Object[] {}, new Class[] {},
//							GroupRequest.GET_ALL, 0);
//			return poolmanstatusrsps;
		    try {
                        RemoteHandlerInf remoteService = ClientProxyContext.getApplicationClientBean("(all)/base.serivce",RemoteHandlerInf.class);
                        return remoteService.getDataSourceStatus();
                    
                    } catch (Exception e) {
//                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      }
		}
		return new HashMap();
	}

	/**
	 * 获取集群中每台服务器的数据库链接池配置信息及链接池的使用情况
	 * 
	 * @return Map<ip:port,Map<dbname,Object[idleconnections,usedconnections,maxusedconnections,poolmetaData]>>
	 */
	public static Map getDataSourceInfos() {
		if (Utils.clusterstarted()) {
//			RspList poolmanrsps = Utils.getRemoteDispatcher()
//					.callRemoteMethods(null, "getDataSourceInfos",
//							new Object[] {}, new Class[] {},
//							GroupRequest.GET_ALL, 0);
//			// Set sets = poolmanrsps.entrySet();
//			// Iterator iterator = sets.iterator();
//			//
//			// // while(iterator.hasNext()){
//			// // Map.Entry ff=(Entry) iterator.next();
//			// // org.jgroups.util.Rsp rsp =
//			// (org.jgroups.util.Rsp)ff.getValue();
//			// //
//			// System.out.println("rsp.getValue().getClass():"+rsp.getValue().getClass());
//			// // System.out.println("value:" +ff.getValue());
//			// // System.out.println("key:" +ff.getKey());
//			// // }
//			return poolmanrsps;
		    RemoteHandlerInf remoteService = ClientProxyContext.getApplicationClientBean("(all)/base.serivce",RemoteHandlerInf.class);
		    return remoteService.getDataSourceInfos();
		}
		return new HashMap();
	}
	
	/**
	 * 获取集群中每台服务器的在线用户数统计信息
	 * 
	 * @return Map<ip:port,int userCount>
	 */
	public static Map getOnlineUserCount() {
		if (Utils.clusterstarted()) {
//			RspList poolmanrsps = Utils.getRemoteDispatcher()
//					.callRemoteMethods(null, "getOnlineUserCount",
//							new Object[] {}, new Class[] {},
//							GroupRequest.GET_ALL, 0);
//			return poolmanrsps;
		    RemoteHandlerInf remoteService = ClientProxyContext.getApplicationClientBean("(all)/base.serivce",RemoteHandlerInf.class);
//		    RemoteHandlerInf remoteService = (RemoteHandlerInf)BaseSPIManager                   
//                    .getProvider("(base.serivce");
		    return (RspList)remoteService.getOnlineUserCount();
		}
		return new HashMap();
	}
	
	public static boolean validateAddress(RPCAddress address)
	{
//		Vector servers = getAppservers();
//		for(int i = 0; i < servers.size() ; i ++)
//		{
//			IpAddress ipAddress = (IpAddress)servers.get(0);
//			if(ipAddress.equals(address))
//				return true;
//			
//		}
//		return false;
//	    return JGroupHelper.getJGroupHelper().validateAddress(address);
	    return RPCHelper.getRPCHelper().validateAddress(address);
	}

//	public static RpcDispatcher getRemoteDispatcher() {
//		// if(eventDispatcher == null)
//
//		return remoteDispatcher;
//	}

	/**
	 * 获取集群/多实例各服务器的信息
	 * 
	 * @return
	 */
	public static List<RPCAddress> getAppservers() {
//		if (clusterstarted()) {
//			return JGroupHelper.getJGroupHelper().getChannel().getView().getMembers();
//		}
	        return ClientProxyContext.getAllNodes();
//		return new Vector();
	}
	
	public static List<RPCAddress> getAppservers(String protocol) {
//          if (clusterstarted()) {
//                  return JGroupHelper.getJGroupHelper().getChannel().getView().getMembers();
//          }
            return ClientProxyContext.getAllNodes(protocol);
//          return new Vector();
    }

	/**
	 * 判断系统是否启用了集群功能	  
	 * @return
	 */
	public static boolean clusterstarted() {
	    return ClientProxyContext.clusterstarted();
	}
	
	/**
         * 判断系统是否启用了集群功能          
         * @return
         */
        public static boolean clusterstarted(String protocol) {
            return ClientProxyContext.clusterstarted(protocol);
        }

	public static boolean refreshReadorgname(String eventtype) {
		if (clusterstarted()) {
			List<RPCAddress> servers = getAppservers();
			RPCAddress ipAddress = null;
			if(servers.size() > 0){
				// 获取最后一台服务器。
				ipAddress = (RPCAddress)servers.get(servers.size()-1);				
				try
				{
//					Object poolmanrsps = remoteDispatcher.callRemoteMethod(
//							ipAddress, "refreshReadorgname",
//							new Object[] { eventtype, ipAddress.toString() }, new Class[] {
//									String.class, String.class },
//							GroupRequest.GET_ALL, 0);
				    RemoteHandlerInf remoteService = ClientProxyContext.getApplicationClientBean("("+ ipAddress.getIp() + ":" + ipAddress.getPort() +")/base.serivce",RemoteHandlerInf.class);
				    
		                    remoteService.refreshReadorgname(eventtype, ipAddress.toString());
				    
				}
				catch(Exception e)
				{
					e.printStackTrace();
					log.error(e);
					UserOrgChangeHandle.getInstance().addRequests(eventtype,
					"localhost");
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				UserOrgChangeHandle.getInstance().addRequests(eventtype,
				"localhost");
			}
		} else {
			UserOrgChangeHandle.getInstance().addRequests(eventtype,
					"localhost");
		}
		return true;
	}
	
	public static boolean cluster_enable_mbean()
	{
	    return false;
//	    return JGroupHelper.getJGroupHelper().cluster_enable_mbean();
	}
	
	public static String getClusterName()
        {
            return ClientProxyContext.getClusterName();
        }

}
