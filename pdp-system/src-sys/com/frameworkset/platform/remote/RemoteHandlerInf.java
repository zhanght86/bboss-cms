package com.frameworkset.platform.remote;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public interface RemoteHandlerInf {
    public Map getDataSourceInfos();
    
    /**
     * 返回本机链接池链接使用状态
     * @return Map<dbName,Object[idleconnections,usedconnections,,maxusedconnections]>
     */
    public Map getDataSourceStatus();
    
    public Object refreshReadorgname(String event,String requestServer);
    /**
     * 返回服务器当前在线用户数
     * @return
     */
    public Object getOnlineUserCount();

}
