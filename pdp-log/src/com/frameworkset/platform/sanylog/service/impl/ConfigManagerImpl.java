/*
 * @(#)ConfigManagerImpl.java
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
package com.frameworkset.platform.sanylog.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.UserAuthorize;
import com.frameworkset.platform.sanylog.service.ConfigManager;
import com.frameworkset.util.ListInfo;

/**
 * 配置实现类
 * @author caix3
 * @version 2012-10-15,v1.0
 */
public class ConfigManagerImpl implements ConfigManager {

    private static Logger logger = Logger.getLogger(ConfigManagerImpl.class);

    private ConfigSQLExecutor executor;

    @Override
    public String saveAppInfo(App app) {
        String result = "success";
        try {
            executor.insertBean("saveAppInfo", app);
        } catch (Exception e) {
            logger.error("save app info error", e);
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public String updateAppInfo(App app) {
        String result = "success";
        try {
            executor.update("updateAppInfo", app);
        } catch (Exception e) {
            logger.error("update app info error", e);
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public ListInfo selectAppInfo(long offset, int pagesize, App app) {
        ListInfo appList = null;
        try {
            if (app.getAppName() != null && !app.getAppName().equals("")) {
                app.setAppName("%" + app.getAppName() + "%");
            }
            appList = executor.queryListInfoBean(App.class, "selectAppInfo", offset, pagesize, app);
        } catch (Exception e) {
            logger.error("select all app info error", e);
        }
        return appList;
    }

    @Override
    public String saveModuleInfo(Module module) {
        String result = "success";
        try {
            executor.insertBean("saveModuleInfo", module);
        } catch (Exception e) {
            logger.error("save module info error", e);
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public String updateAppInfo(Module module) {
        String result = "success";
        try {
            executor.update("updateModuleInfo", module);
        } catch (Exception e) {
            logger.error("update module info error", e);
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public ListInfo selectModuleInfo(long offset, int pagesize, Module module) {
        ListInfo moduleList = null;
        try {
            if (module.getAppName() != null && !module.getAppName().equals("")) {
                module.setAppName("%" +module.getAppName() + "%");
            }
            if (module.getModuleName() != null && !module.getModuleName().equals("")) {
                module.setModuleName("%" +module.getModuleName() + "%");
            }
            moduleList = executor.queryListInfoBean(Module.class, "selectModuleInfo", offset, pagesize, module);
        } catch (Exception e) {
            logger.error("select all app info error", e);
        }
        return moduleList;
    }
    
    @Override
    public List<App> getUnSelectAppList(String userId) {
        List<App> appList = null;
        try {
            appList = executor.queryList(App.class, "getUnSelectAppList", userId);
        } catch (Exception e) {
            logger.error("select all app list by user error", e);
        }
        return appList;
    }

    @Override
    public List<App> getSelectedAppList(String userId) {
        List<App> appList = null;
        try {
            appList = executor.queryList(App.class, "getSelectedAppList", userId);
        } catch (Exception e) {
            logger.error("select all app list by user error", e);
        }
        return appList;
    }

    @Override
    public String deleteUserAllApp(String userId) {
        String result = "success";
        try {
            executor.delete("deleteUserAllApp", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String addUserAppList(List<UserAuthorize> appList) {
        String result = "success";
        try {
            executor.insertBeans("addUserAppList", appList);
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public Integer getUserAuthorizeKeyValue() {
        Integer result = 0;
        try {
            String res = executor.queryField("getUserAuthorizeKeyValue");
            result = Integer.parseInt(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getAppName(String appId) {
        String result = "";
        try {
            result = executor.queryField("getAppName", appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Module checkAppModule(String appName, String moduleName, String moduleCode, String modulePath) {
        Module module = null;
        App app = null;
        try {
            List<App> appList = executor.queryList(App.class, "getAppInfoByName", appName);
            if (appList.size() <= 0) {
                app = new App();
                Integer keyValue = getAppKeyValue() + 1;
                app.setAutoId(keyValue + "");
                app.setAppId(keyValue + "");
                app.setAppName(appName);
                app.setUseFlag("1");
                saveAppInfo(app);
            } else {
                app = appList.get(0);
            }
            
            List<Module> moduleList = executor.queryList(Module.class, "getModuleInfoByName", app.getAppId(), moduleName);
            if (moduleList.size() <= 0) {
                module = new Module();
                Integer keyValue = getModuleKeyValue() + 1;
                module.setAutoId(keyValue + "");
                module.setModuleId(keyValue + "");
                module.setAppId(app.getAppId());
                module.setModuleName(moduleName);
                module.setModuleCode(moduleCode);
                module.setModulePath(modulePath);
                module.setUseFlag("1");
                saveModuleInfo(module);
            } else {
                module = moduleList.get(0);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        return module;
    }
    
    @Override
    public Module checkAppModule(String appName, String moduleName) {
        Module module = null;
        App app = null;
        try {
            List<App> appList = executor.queryList(App.class, "getAppInfoByName", appName);
            if (appList.size() <= 0) {
                app = new App();
                Integer keyValue = getAppKeyValue() + 1;
                app.setAutoId(keyValue + "");
                app.setAppId(keyValue + "");
                app.setAppName(appName);
                app.setUseFlag("1");
                saveAppInfo(app);
            } else {
                app = appList.get(0);
            }
            
            List<Module> moduleList = executor.queryList(Module.class, "getModuleInfoByName", moduleName);
            if (moduleList.size() <= 0) {
                module = new Module();
                Integer keyValue = getModuleKeyValue() + 1;
                module.setAppId(keyValue + "");
                module.setModuleId(keyValue + "");
                module.setAppId(app.getAppId());
                module.setModuleName(moduleName);
                module.setUseFlag("1");
                saveModuleInfo(module);
            } else {
                module = moduleList.get(0);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        return module;
    }
    
    @Override
    public Integer getAppKeyValue() {
        Integer result = 0;
        try {
            String res = executor.queryField("getAppKeyValue");
            result = Integer.parseInt(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public Integer getModuleKeyValue() {
        Integer result = 0;
        try {
            String res = executor.queryField("getModuleKeyValue");
            result = Integer.parseInt(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String modifyAppFlag(String autoId, String type) {
        String result = "success";
        try {
            executor.update("modifyAppFlag", type , autoId);
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public String modifyModuleFlag(String autoId, String type) {
        String result = "success";
        try {
            executor.update("modifyModuleFlag", type , autoId);
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }
}
