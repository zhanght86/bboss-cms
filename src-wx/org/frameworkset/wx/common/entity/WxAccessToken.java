package org.frameworkset.wx.common.entity;

import java.util.Date;

/**
 * 微信企业号的token
 * WxAccessToken.java
 * 
 * comments.
 * 
 * @author liud44
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2015年3月9日
 * @version $Revision: 3 $
 */
public class WxAccessToken {

    private String accessTokenId;

    private String corpid;

    private String corpsecret;

    private String accessToken;

    private long expires_in;

    private String errcode;

    private String errmsg;

    /**
     * accesstoken申请时间
     */
    private Date createTime;

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public void setCorpsecret(String corpsecret) {
        this.corpsecret = corpsecret;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
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

}
