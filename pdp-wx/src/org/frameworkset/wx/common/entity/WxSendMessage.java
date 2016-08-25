package org.frameworkset.wx.common.entity;

/**
 * 微信发送消息封装类
 * 
 * @todo
 * @author tanx
 * @date 2015年10月27日
 * 
 */
public class WxSendMessage {

    /*
     * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。
     * 特殊情况：指定为@all， 则向关注该企业应用的全部成员发送
     */
    private String toUser;

    /* 消息类型 text|image|voice|video|file|news */
    private String msgType;

    /* 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 */
    private String toparty;

    /* 标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数 */
    private String totag;

    /*
     * msgType=text时 ,文本消息内容
     */
    private String content;

    /* msgType=image|voice|video时 ,对应消息信息ID（--------） */
    private String mediaId;

    /* msgType=news|video时，消息标题 */
    private String title;

    /* msgType=news|video时，消息描述 */
    private String description;

    /* msgType=news时，消息链接 */
    private String url;

    /* msgType=news时，图片路径 */
    private String picurl;

    /* 表示是否是保密消息，0表示否，1表示是，默认0 */
    private String safe;

    /* 企业应用的id，整型。可在应用的设置页面查看 */
    private int agentid;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

}
