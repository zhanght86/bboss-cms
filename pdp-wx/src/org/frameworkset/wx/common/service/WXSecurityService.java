package org.frameworkset.wx.common.service;

import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxSendMessage;
import org.frameworkset.wx.common.entity.WxUserToken;
import org.frameworkset.wx.common.enums.EnumWeiXinAccountFlag;

public interface WXSecurityService {

    public WxAccessToken getWxAccessToken(String corpid, String corpsecret) throws Exception;

    public WxUserToken getWxUserToken(String accesstoken, String code) throws Exception;

    public String sendWeChatMsg(WxSendMessage sendMes, String accessToken) throws Exception;
}
