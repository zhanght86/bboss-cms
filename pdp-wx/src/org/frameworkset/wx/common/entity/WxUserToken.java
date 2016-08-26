package org.frameworkset.wx.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WxUserToken {

	
    private String UserId;

    private String errcode;

    private String errmsg;

    private String deviceId;

    private String openId;

    public WxUserToken() {
        // TODO Auto-generated constructor stub
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    @JsonProperty("UserId")
    public String getUserId() {
        return UserId;
    }
    @JsonProperty("UserId")
    public void setUserId(String userId) {
        UserId = userId;
    }

}
