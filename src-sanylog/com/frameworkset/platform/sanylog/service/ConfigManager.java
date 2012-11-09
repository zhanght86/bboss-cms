/*
 * @(#)ConfigManager.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.sanylog.service;

import java.util.List;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.UserAuthorize;
import com.frameworkset.util.ListInfo;

/**
 * 配置
 * @author caix3
 * @version 2012-10-15,v1.0
 */
public interface ConfigManager {

    public Module checkAppModule(String appName, String moduleName, String moduleCode, String modulePath);
    
    public Module checkAppModule(String appName, String moduleName);
    
    public String modifyAppFlag(String autoId, String type);
    
    public String saveAppInfo(App app);

    public String updateAppInfo(App app);

    public ListInfo selectAppInfo(long offset, int pagesize, App app);

    public String saveModuleInfo(Module module);

    public String updateAppInfo(Module module);

    public ListInfo selectModuleInfo(long offset, int pagesize, Module module);

    public List<App> getUnSelectAppList(String userId);

    public List<App> getSelectedAppList(String userId);
    
    public String deleteUserAllApp(String userId);
    
    public String addUserAppList(List<UserAuthorize> appList);
    
    public Integer getUserAuthorizeKeyValue();
    
    public String getAppName(String appId);

    public Integer getAppKeyValue();

    public Integer getModuleKeyValue();

    public String modifyModuleFlag(String autoId, String type);
}
