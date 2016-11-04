package org.frameworkset.wx.publics;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.frameworkset.trans.entity.ApiXml;
import org.frameworkset.trans.enums.EnumTransDataType;
import org.frameworkset.trans.service.ApiXmlService;
import org.frameworkset.trans.util.CusAccessObjectUtil;
import org.frameworkset.wx.common.WXConstant;
import org.frameworkset.wx.common.entity.OAuthSnsAPIBase;
import org.frameworkset.wx.common.entity.WxOrderMessage;
import org.frameworkset.wx.common.enums.EnumWeiXinOAuthScope;
import org.frameworkset.wx.common.mp.aes.WXBizMsgCrypt;
import org.frameworkset.wx.common.mp.aes.XMLParse;
import org.frameworkset.wx.common.service.WXAPIService;
import org.frameworkset.wx.common.util.RandomUtil;
import org.frameworkset.wx.common.util.WXHelper;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;

/**
 * 微信公众号
 * 
 * @author suwei
 * @date 2016-10-10
 *
 */
public class WXAPIServiceImpl implements WXAPIService {
	private static Logger log = Logger.getLogger(WXAPIServiceImpl.class);
	
	private ApiXmlService apiXmlService;

	/**
	 * 调用微信统一下单接口
	 */
	@Override
	public String unifiedorder(WxOrderMessage orderMsg) throws Exception {
		 String sendUrl = WXHelper.getServiceUnifiedorderMsgURL();
		 orderMsg.setAppid(WXHelper.getServiceAppId());
		 orderMsg.setMchId(WXHelper.getServiceMchId());
		 orderMsg.setNonceStr(RandomUtil.generateString(24)); //
		 String sign = WXBizMsgCrypt.getWXSign(orderMsg,WXHelper.getServiceMchKey()); // 获得随机数
//		String sendUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		log.debug("微信发送消息URL=" + sendUrl);
//		orderMsg.setAppid("wxe88cfaa46b73c192");
//		orderMsg.setMchId("1379567102");
//		String sign = WXBizMsgCrypt.getWXSign(orderMsg, "ec81fb87c5436d44c5faf1edf47bc5a9"); // 获得随机数
//		System.out.println(sign);
		orderMsg.setSign(sign);
		//记录对微信的发送数据包
		String xml = XMLParse.generateUnifiedOrderXML(orderMsg);
		/*ApiXml apiXml=new ApiXml();
		apiXml.setData(xml);
		apiXml.setType(EnumTransDataType.WEIXIN_TRANSDATA_OUT.getCode());
		apiXml.setSrcIp(orderMsg.getSpbillCreateIp());
		apiXmlService.addApiXml(apiXml);*/
		// 封装发送消息请求json
		log.debug("微信发送消息xml=\n" + xml);
		System.out.println(xml);
		try {
			URL url = new URL(sendUrl);
			HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			// 读取超时30秒
			http.connect();
			OutputStream os = http.getOutputStream();
			os.write(xml.getBytes("UTF-8"));// 传入参数
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			os.flush();
			os.close();
		/*	apiXml.setType(EnumTransDataType.WEIXIN_TRANSDATA_OUT_RES.getCode());
			apiXml.setData(new String(jsonBytes, "UTF-8"));
			apiXmlService.addApiXml(apiXml);
			return apiXml.getData();*/
			return new String(jsonBytes, "UTF-8");
		} catch (Exception e) {
			log.debug("推送消息给微信端出错：" + e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 获得用户授权后重定向页面
	 */
	@Override
	public String getUserOAuthURL(EnumWeiXinOAuthScope enumWeiXinOAuthScope) throws Exception {
		String o_auth_openid_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		String appid = WXHelper.getServiceAppId();
		String redictUrl = WXHelper.getServiceRedictURL();
		String requestUrl = o_auth_openid_url.replace("APPID", appid).replace("REDIRECT_URI", redictUrl)
				.replace("SCOPE", enumWeiXinOAuthScope.getCode());
		return requestUrl;
	}

	@Override
	public OAuthSnsAPIBase getWeiXinSnsAPIBase(String appId, String secret, String code, String grant_type)
			throws Exception {
		String url = WXHelper.getServiceAccessTokenURL() + "?appid=" + appId + "&secret=" + secret + "&code=" + code
				+ "&grant_type=" + grant_type;

		System.out.println("微信getWxAccessToken=" + url);
		log.debug("微信getWeiXinSnsAPIBase=" + url);
		String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
		System.out.println("微信getWxAccessToken back json :" + response);
		OAuthSnsAPIBase token = StringUtil.json2Object(response, OAuthSnsAPIBase.class);
//		System.out.println(token.getErrmsg());
		log.debug(token.getErrmsg());
		return token;
	}

	@Override
	public OAuthSnsAPIBase getWeiXinSnsAPIBase(String code) throws Exception {
		String appid = WXHelper.getServiceAppId();
		String secret = WXHelper.getServiceAppSecret();
		return getWeiXinSnsAPIBase(appid, secret, code, WXConstant.AUTHOIZATION_CODE);
	}

}
