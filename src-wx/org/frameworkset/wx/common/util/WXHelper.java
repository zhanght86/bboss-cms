package org.frameworkset.wx.common.util;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.wx.common.service.WXAPIService;
import org.frameworkset.wx.common.service.WXSecurityService;

public class WXHelper {

    public WXHelper() {
        // TODO Auto-generated constructor stub
    }

    public static WXSecurityService getEnterpriseWXSecurityService() {
        BaseApplicationContext context = DefaultApplicationContext
                .getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
        return context.getTBeanObject("wx.enterprise.securityService", WXSecurityService.class);
    }

    public static WXAPIService getEnterpriseWXAPIService() {
        BaseApplicationContext context = DefaultApplicationContext
                .getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
        return context.getTBeanObject("wx.enterprise.APIService", WXAPIService.class);
    }

    public static String requestCheckcode() {
        BaseApplicationContext context = DefaultApplicationContext
                .getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
        return context.getProperty("wx.enterprise.request.checkcode", "everytime");
    }

    public static String getEnterpriseUserInfoURL() {
        BaseApplicationContext context = DefaultApplicationContext
                .getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
        return context.getProperty("wx.enterprise.getUserInfo", "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo");
    }

    public static String getEnterpriseAccessTokenURL() {
        BaseApplicationContext context = DefaultApplicationContext
                .getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
        return context.getProperty("wx.enterprise.accessToken", "https://qyapi.weixin.qq.com/cgi-bin/gettoken");
    }

}
