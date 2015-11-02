package org.frameworkset.wx.common.util;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.util.ParamsHandler;
import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.wx.common.service.WXAPIService;
import org.frameworkset.wx.common.service.WXSecurityService;

import com.frameworkset.util.StringUtil;


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

    public static boolean uselocalsession() {
        return ParamsHandler.getParamsHandler("cms.siteparamshandler").getParams("weixin", "weixin")
                .getAttributeBoolean(0, "wx.uselocalsession", true);
    }

    public static String getEnterpriseUserInfoURL() {

        Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.userid.url", "weixin");
        return (String) p.getValue();
    }

    public static String getEnterpriseAccessTokenURL() {
        Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.token.url", "weixin");
        return (String) p.getValue();
    }
    
    public static String getEnterpriseSendWeChatMsgURL() {
        Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.sendMsg.url", "weixin");
        return (String) p.getValue();
    }

    public static String getEnterpriseToken(String app) {
        if (StringUtil.isEmpty(app)) {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler")
                    .getParam("weixin", "qywx.token", "weixin");
            return (String) p.getValue();
        } else {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", app + ".qywx.token",
                    "weixin");
            return (String) p.getValue();
        }

    }

    public static String getEnterpriseCorpid(String app) {
        if (StringUtil.isEmpty(app)) {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.corpid",
                    "weixin");
            return (String) p.getValue();
        } else {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", app + ".qywx.corpid",
                    "weixin");
            return (String) p.getValue();
        }
    }

    public static String getEnterpriseCorpsecret(String app) {
        if (StringUtil.isEmpty(app)) {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.corpsecret",
                    "weixin");
            return (String) p.getValue();
        } else {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin",
                    app + ".qywx.corpsecret", "weixin");
            return (String) p.getValue();
        }
    }

    public static String getEnterpriseAeskey(String app) {
        if (StringUtil.isEmpty(app)) {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.aeskey",
                    "weixin");
            return (String) p.getValue();
        } else {
            Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", app + ".qywx.aeskey",
                    "weixin");
            return (String) p.getValue();
        }
    }

}
