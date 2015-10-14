package org.frameworkset.wx.enterprise;

import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxUserToken;
import org.frameworkset.wx.common.service.WXSecurityService;
import org.frameworkset.wx.common.util.WXHelper;

import com.frameworkset.util.StringUtil;

public class WXSecurityServiceImp implements WXSecurityService {

    public WXSecurityServiceImp() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public WxAccessToken getWxAccessToken(String corpid, String corpsecret) throws Exception {
        String url = WXHelper.getEnterpriseAccessTokenURL() + "?corpid=" + corpid + "&corpsecret=" + corpsecret;

        System.out.println("微信getWxAccessToken=" + url);

        String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
        WxAccessToken token = StringUtil.json2Object(response, WxAccessToken.class);
        return token;
    }

    @Override
    public WxUserToken getWxUserToken(String accesstoken, String code) throws Exception {
        String url = WXHelper.getEnterpriseUserInfoURL() + "?access_token=" + accesstoken + "&code=" + code;

        System.out.println("微信getWxUserToken=" + url);

        String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
        WxUserToken user = StringUtil.json2Object(response, WxUserToken.class);
        return user;
    }

    public static void main(String args[]) {
        String response = "{\"UserId\":\"gw_tanx\",\"deviceId\":\"94ab58a699a41828b7da1a6fc9535be2\"}";
        WxUserToken user = StringUtil.json2Object(response, WxUserToken.class);
       System.out.println(user.getUserId());
    }
}
