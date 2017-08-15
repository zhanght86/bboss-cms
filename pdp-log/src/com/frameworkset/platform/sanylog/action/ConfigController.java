/*
 * @(#)ConfigController.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.sanylog.action;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.UserAuthorize;
import com.frameworkset.platform.sanylog.service.ConfigManager;
import com.frameworkset.util.StringUtil;

/**
 * 配置控制器
 * @author caix3
 * @version 2012-10-15,v1.0
 */
public class ConfigController {

    private ConfigManager configManager;

    public String getAppList(@PagerParam(name = PagerParam.OFFSET) long offset,
            @PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, 
            App app, ModelMap model) {
        ListInfo appList = configManager.selectAppInfo(offset, pagesize, app);
        model.addAttribute("appList", appList);
        return "path:appList";
    }

    public String getModuleList(@PagerParam(name = PagerParam.OFFSET) long offset,
            @PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, 
            Module module, ModelMap model) {
        ListInfo moduleList = configManager.selectModuleInfo(offset, pagesize, module);
        model.addAttribute("moduleList", moduleList);
        return "path:moduleList";
    }

    public @ResponseBody
    String modifyAppInfo(String autoId, String type, ModelMap model) {
        String response = configManager.modifyAppFlag(autoId, type);
        return response;
    }
    
    public @ResponseBody
    String modifyModuleInfo(String autoId, String type, ModelMap model) {
        String response = configManager.modifyModuleFlag(autoId, type);
        return response;
    }
    
    public String getUnSelectAppList(String userId, ModelMap model) {
        List<App> responseBody = configManager.getUnSelectAppList(userId);
        model.addAttribute("unSelectAppList", responseBody);
        return "path:unSelectApp";
    }

    public String getSelectedAppList(String userId, ModelMap model) {
        List<App> responseBody = configManager.getSelectedAppList(userId);
        model.addAttribute("selectedAppList", responseBody);
        return "path:selectedApp";
    }

    public @ResponseBody
    String modifySelectedList(String usernames, String selectedApp) {

        String response = "";
        String[] users = usernames.split(",");
        String[] appCodes = null;
        if (!StringUtil.isEmpty(selectedApp)) {
            appCodes = selectedApp.split(",");
        }
        Integer authorizeKeyValue = configManager.getUserAuthorizeKeyValue();
        List<UserAuthorize> saveList = new ArrayList<UserAuthorize>();
        for (String user : users) {
            configManager.deleteUserAllApp(user);
            for (String appCode : appCodes) {

                UserAuthorize temp = new UserAuthorize();
                authorizeKeyValue++;
                temp.setAutoId(authorizeKeyValue + "");
                temp.setUserId(user);
                temp.setAppId(appCode);
                temp.setAppName(configManager.getAppName(appCode));
                saveList.add(temp);

            }
        }

        response = configManager.addUserAppList(saveList);

        return response;
    }
}
