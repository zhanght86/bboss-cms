package org.frameworkset.wx.publics;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.trans.entity.ApiXml;
import org.frameworkset.trans.entity.CmsWxtoken;
import org.frameworkset.trans.enums.EnumTransDataType;
import org.frameworkset.trans.service.ApiXmlService;
import org.frameworkset.trans.service.CmsWxtokenService;
import org.frameworkset.trans.service.CmsWxtokenServiceImpl;
import org.frameworkset.wx.common.WXConstant;
import org.frameworkset.wx.common.entity.OAuthSnsAPIBase;
import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxJsapiTicket;
import org.frameworkset.wx.common.entity.WxOrderMessage;
import org.frameworkset.wx.common.entity.WxServiceCompany;
import org.frameworkset.wx.common.enums.EnumWeiXinOAuthScope;
import org.frameworkset.wx.common.mp.aes.WXBizMsgCrypt;
import org.frameworkset.wx.common.mp.aes.XMLParse;
import org.frameworkset.wx.common.service.WXAPIService;
import org.frameworkset.wx.common.util.RandomUtil;
import org.frameworkset.wx.common.util.WXHelper;

import com.frameworkset.orm.transaction.TransactionManager;
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
	private CmsWxtokenService cmsWxtokenService;

	@Override
	public WxAccessToken getWxAccessToken(String appid, String appsecret)
			throws Exception {
		TransactionManager tm = new TransactionManager();
		String url = WXHelper.getServiceAccessTokenURL() + "?grant_type=client_credential" + "&appid=" + appid
				+ "&secret=" + appsecret;
		WxAccessToken token = new WxAccessToken();
		if (cmsWxtokenService == null) {
			BaseApplicationContext context = DefaultApplicationContext
					.getApplicationContext("org/frameworkset/trans/util/bboss-token.xml");
			cmsWxtokenService = context.getTBeanObject("token.cmsWxtokenService", CmsWxtokenServiceImpl.class);
		}
		CmsWxtoken cmsWxToken = cmsWxtokenService.getCmsWxtoken();
		if (cmsWxToken == null) { // 获取系统当前的accestoken值
			try {
				tm.begin(); // 事物开始
				CmsWxtoken cmsWxtokenUpdate = new CmsWxtoken();
				cmsWxtokenUpdate.setState(1);
				cmsWxtokenService.updateCmsWxtokenState(cmsWxtokenUpdate);
				String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
				token = StringUtil.json2Object(response, WxAccessToken.class);
				token.setCreateTime(new Date());
				cmsWxToken = new CmsWxtoken();
				cmsWxToken.setAccessToken(token.getAccess_token());
				cmsWxToken.setExpiresIn(token.getExpires_in());
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				cmsWxToken.setCreateTime(ts);
				cmsWxToken.setState(0);
				Timestamp endts = new Timestamp(System.currentTimeMillis() + cmsWxToken.getExpiresIn() * 1000);
				cmsWxToken.setEndTime(endts);
				cmsWxtokenService.addCmsWxtoken(cmsWxToken);
				tm.commit();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			} finally {
				tm.release(); // 释放
			}
		} else {
			token.setAccess_token(cmsWxToken.getAccessToken());
			token.setExpires_in(cmsWxToken.getExpiresIn());
			token.setCreateTime(cmsWxToken.getCreateTime());
		}
		log.debug("当前系统的access token ：" + cmsWxToken.getAccessToken());
		log.debug("微信getWxAccessToken=" + url);

		return token;
	}

	/**
	 * 调用微信统一下单接口
	 */
	@Override
	public String unifiedorder(WxOrderMessage orderMsg) throws Exception {

		String sendUrl = WXHelper.getServiceUnifiedorderMsgURL();
		orderMsg.setAppid(WXHelper.getServiceAppId());
		orderMsg.setMchId(WXHelper.getServiceMchId());
		orderMsg.setNonceStr(RandomUtil.generateString(24)); //
		String apiKey = WXHelper.getServiceMchKey();
		String sign = WXBizMsgCrypt.getWXSign(orderMsg, apiKey); // 获得随机数

		// String sendUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		log.debug("微信发送消息URL=" + sendUrl);
		orderMsg.setSign(sign);
		// 记录对微信的发送数据包
		String xml = XMLParse.generateUnifiedOrderXML(orderMsg);
		ApiXml apiXml = new ApiXml();
		apiXml.setData(xml);
		apiXml.setType(EnumTransDataType.WEIXIN_TRANSDATA_OUT.getCode());
		apiXml.setSrcIp(orderMsg.getSpbillCreateIp());
		apiXmlService.addApiXml(apiXml);
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
			apiXml.setType(EnumTransDataType.WEIXIN_TRANSDATA_OUT_RES.getCode());
			apiXml.setData(new String(jsonBytes, "UTF-8"));
			apiXmlService.addApiXml(apiXml);

			// return apiXml.getData();
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
		String url = WXHelper.getServiceOauthURL() + "?appid=" + appId + "&secret=" + secret + "&code=" + code
				+ "&grant_type=" + grant_type;

		System.out.println("微信getWxAccessToken=" + url);
		log.debug("微信getWeiXinSnsAPIBase=" + url);
		String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
		System.out.println("微信getWxAccessToken back json :" + response);
		OAuthSnsAPIBase token = StringUtil.json2Object(response, OAuthSnsAPIBase.class);
		log.debug(token.getErrmsg());
		return token;
	}

	@Override
	public OAuthSnsAPIBase getWeiXinSnsAPIBase(String code) throws Exception {
		String appid = WXHelper.getServiceAppId();
		String secret = WXHelper.getServiceAppSecret();
		return getWeiXinSnsAPIBase(appid, secret, code, WXConstant.AUTHOIZATION_CODE);
	}

	@Override
	public WxJsapiTicket getJsapiTicket(String accessToken) throws Exception {
		if (accessToken == null || StringUtil.isEmpty(accessToken)) {
			String appid = WXHelper.getServiceAppId();
			String appsecret = WXHelper.getServiceAppSecret();
			WxAccessToken wxAccessToken = getWxAccessToken(appid, appsecret);
			accessToken = wxAccessToken != null && wxAccessToken.getAccess_token() != null
					? wxAccessToken.getAccess_token() : null;
		}
		String url = WXHelper.getServiceTicketURL() + "?access_token=" + accessToken + "&type=jsapi";
		System.out.println("微信getJsapiTicket=" + url);
		String response = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
		WxJsapiTicket ticket = StringUtil.json2Object(response, WxJsapiTicket.class);
		log.debug(ticket.getErrmsg());
		return ticket;
	}

	@Override
	public String getPaySign(long timestamp, String nodeStr) throws Exception {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String getJSSDKSign(String nonceStr, String jsapiTicket, long timestamp, String currentUrl)
			throws Exception {
		// String appid = WXHelper.getServiceAppId();
		// String appsecret = WXHelper.getServiceAppSecret();
		// String accessToken = getWxAccessToken(appid,
		// appsecret).getAccess_token();
		// String jsapiTicket = getJsapiTicket(accessToken).getTicket();
		// long timestamp = System.currentTimeMillis() / 1000;
		String sign = WXBizMsgCrypt.getJSSDKSign(nonceStr, jsapiTicket, timestamp, currentUrl);
		return sign;
	}
	/**
	 * 调用微信公众号的商户号支付接口
	 */
	@Override
	public String ServiceCompanyPay(WxServiceCompany wxServiceCompany) throws Exception {

		String sendUrl = WXHelper.getServiceCompanyURL();
		wxServiceCompany.setMchAppid(WXHelper.getServiceAppId());
		wxServiceCompany.setMchid(WXHelper.getServiceMchId());
		wxServiceCompany.setNonceStr(RandomUtil.generateString(24)); //
		String apiKey = WXHelper.getServiceMchKey();
		String columnName[] = { "mch_appid", "mchid", "device_info", "nonce_str", "sign", "partner_trade_no", "openid",
				"check_name", "re_user_name", "amount", "desc", "spbill_create_ip" };
		String sign = WXBizMsgCrypt.getWXSign(wxServiceCompany.getClass(), wxServiceCompany, apiKey, columnName); // 获得随机数
		log.debug("微信发送消息URL=" + sendUrl);
		wxServiceCompany.setSign(sign);
		String xml = XMLParse.generateServiceCompanyPayXML(wxServiceCompany, columnName);
		ApiXml apiXml = new ApiXml();
		apiXml.setData(xml);
		apiXml.setType(EnumTransDataType.WEIXIN_TRANSDATA_OUT.getCode());
		apiXml.setSrcIp(wxServiceCompany.getSpbillCreateIp());
//		apiXmlService.addApiXml(apiXml);
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
			apiXml.setType(EnumTransDataType.WEIXIN_TRANSDATA_OUT_RES.getCode());
			apiXml.setData(new String(jsonBytes, "UTF-8"));
//			apiXmlService.addApiXml(apiXml);
			return new String(jsonBytes, "UTF-8");
		} catch (Exception e) {
			log.debug("推送消息给微信端出错：" + e.getMessage(), e);
			return "";
		}
	}

}
