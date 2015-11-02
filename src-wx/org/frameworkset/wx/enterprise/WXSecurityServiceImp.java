package org.frameworkset.wx.enterprise;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxSendMessage;
import org.frameworkset.wx.common.entity.WxUserToken;
import org.frameworkset.wx.common.service.WXSecurityService;
import org.frameworkset.wx.common.util.WXHelper;

import com.frameworkset.util.StringUtil;

public class WXSecurityServiceImp implements WXSecurityService {

    private static Logger log = Logger.getLogger(WXSecurityServiceImp.class);

    public WXSecurityServiceImp() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public WxAccessToken getWxAccessToken(String corpid, String corpsecret) throws Exception {
        String url = WXHelper.getEnterpriseAccessTokenURL() + "?corpid=" + corpid + "&corpsecret=" + corpsecret;

        // System.out.println("微信getWxAccessToken=" + url);
        log.debug("微信getWxAccessToken=" + url);

        String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
        WxAccessToken token = StringUtil.json2Object(response, WxAccessToken.class);
        return token;
    }

    @Override
    public WxUserToken getWxUserToken(String accesstoken, String code) throws Exception {
        String url = WXHelper.getEnterpriseUserInfoURL() + "?access_token=" + accesstoken + "&code=" + code;

        // System.out.println("微信getWxUserToken=" + url);
        log.debug("微信getWxUserToken=" + url);

        String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
        WxUserToken user = StringUtil.json2Object(response, WxUserToken.class);
        return user;
    }

    // public static void main(String args[]) {
    // String response = "{\"UserId\":\"gw_tanx\",\"deviceId\":\"94ab58a699a41828b7da1a6fc9535be2\"}";
    // WxUserToken user = StringUtil.json2Object(response, WxUserToken.class);
    // System.out.println(user.getUserId());
    // }

    @Override
    public String sendWeChatMsg(WxSendMessage sendMes, String accessToken) throws Exception {
        String sendUrl = WXHelper.getEnterpriseSendWeChatMsgURL() + "=" + accessToken;

        // System.out.println("微信发送消息URL=" + sendUrl);
        log.debug("微信发送消息URL=" + sendUrl);

        // 封装发送消息请求json
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"touser\":" + "\"" + sendMes.getToUser() + "\",");
        sb.append("\"toparty\":" + "\"" + sendMes.getToparty() + "\",");
        sb.append("\"totag\":" + "\"" + sendMes.getTotag() + "\",");
        if (sendMes.getMsgType().equals("text")) {
            sb.append("\"msgtype\":" + "\"text\",");
            sb.append("\"text\":" + "{");
            sb.append("\"content\":" + "\"" + sendMes.getContent() + "\"");
            sb.append("}");
        } else if (sendMes.getMsgType().equals("image")) {
            sb.append("\"msgtype\":" + "\"image\",");
            sb.append("\"image\":" + "{");
            sb.append("\"media_id\":" + "\"" + sendMes.getMediaId() + "\"");
            sb.append("}");
        } else if (sendMes.getMsgType().equals("voice")) {
            sb.append("\"msgtype\":" + "\"voice\",");
            sb.append("\"voice\":" + "{");
            sb.append("\"media_id\":" + "\"" + sendMes.getMediaId() + "\"");
            sb.append("}");
        } else if (sendMes.getMsgType().equals("video")) {
            sb.append("\"msgtype\":" + "\"video\",");
            sb.append("\"video\":" + "{");
            sb.append("\"media_id\":" + "\"" + sendMes.getMediaId() + "\",");
            sb.append("\"title\":" + "\"" + sendMes.getTitle() + "\",");
            sb.append("\"description\":" + "\"" + sendMes.getDescription() + "\"");
            sb.append("}");
        } else if (sendMes.getMsgType().equals("file")) {
            sb.append("\"msgtype\":" + "\"file\",");
            sb.append("\"file\":" + "{");
            sb.append("\"media_id\":" + "\"" + sendMes.getMediaId() + "\"");
            sb.append("}");
        } else if (sendMes.getMsgType().equals("news")) {
            sb.append("\"msgtype\":" + "\"news\",");
            sb.append("\"news\":" + "{");
            sb.append("\"articles\":" + "[");
            sb.append("{");
            sb.append("\"title\":" + "\"" + sendMes.getTitle() + "\",");
            sb.append("\"description\":" + "\"" + sendMes.getDescription() + "\",");
            sb.append("\"url\":" + "\"" + sendMes.getUrl() + "\",");
            sb.append("\"picurl\":" + "\"" + sendMes.getPicurl() + "\"");
            sb.append("}");
            sb.append("]");
            sb.append("}");
        }
        sb.append(",\"safe\":" + "\"" + sendMes.getSafe() + "\",");
        sb.append("\"agentid\":" + "\"" + sendMes.getAgentid() + "\"");
        sb.append("}");
        String json = sb.toString();

        // System.out.println("微信发送消息json=" + json);
        log.debug("微信发送消息json=" + json);

        try {

            URL url = new URL(sendUrl);

            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();

            http.setRequestMethod("POST");

            http.setRequestProperty("Content-Type",

            "application/json;charset=UTF-8");

            http.setDoOutput(true);

            http.setDoInput(true);

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            // 连接超时30秒

            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            // 读取超时30秒

            http.connect();

            OutputStream os = http.getOutputStream();

            os.write(json.getBytes("UTF-8"));// 传入参数

            InputStream is = http.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            os.flush();

            os.close();

            return new String(jsonBytes, "UTF-8");

        } catch (Exception e) {

            log.debug("推送消息给微信端出错：" + e.getMessage(), e);

            return "";
        }

    }
}
