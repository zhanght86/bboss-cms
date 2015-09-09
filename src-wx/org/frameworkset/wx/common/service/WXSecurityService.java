package org.frameworkset.wx.common.service;

import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxUserToken;

public interface WXSecurityService {

    public WxAccessToken getWxAccessToken(String agentid, String corpid, String corpsecret) throws Exception;

    public WxUserToken getWxUserToken(String accesstoken, String code, String agentID, String state) throws Exception;

}
