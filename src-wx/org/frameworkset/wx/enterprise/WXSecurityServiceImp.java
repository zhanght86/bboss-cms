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
    public WxAccessToken getWxAccessToken(String agentid, String corpid, String corpsecret) throws Exception {
        String url = WXHelper.getEnterpriseAccessTokenURL() + "?corpid=" + corpid + "&corpsecret=" + corpsecret
                + "&agentID=" + agentid;
        String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
        WxAccessToken token = StringUtil.json2Object(response, WxAccessToken.class);
        return token;
    }

    @Override
    public WxUserToken getWxUserToken(String accesstoken, String code, String agentID, String state) throws Exception {
        String url = WXHelper.getEnterpriseUserInfoURL() + "?accesstoken=" + accesstoken + "&code=" + code
                + "&agentID=" + agentID;
        String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
        WxUserToken user = StringUtil.json2Object(response, WxUserToken.class);
        return user;
    }

}
