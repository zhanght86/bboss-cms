package org.frameworkset.wx.common.util;

import java.util.Arrays;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.util.ParamsHandler;
import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.wx.common.service.SmUserWxService;
import org.frameworkset.wx.common.service.WXAPIService;
import org.frameworkset.wx.common.service.WXSecurityService;

import com.frameworkset.util.StringUtil;

public class WXHelper {
	/**
	 * 微信统一下单字段
	 */
	public static String[] UNIFIED_ORDER_COLUMN = { "appid", "mch_id", "device_info", "nonce_str", "sign", "body",
			"detail", "attach", "out_trade_no", "fee_type", "total_fee", "spbill_create_ip", "time_start",
			"time_expire", "goods_tag", "notify_url", "trade_type", "product_id", "limit_pay", "openid" };
	public static String[] PAYSIGN_COLUMN = { "appId", "timeStamp", "nonceStr", "package", "signType" };

	public WXHelper() {
		// TODO Auto-generated constructor stub
	}

	public static WXSecurityService getEnterpriseWXSecurityService() {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
		return context.getTBeanObject("wx.enterprise.securityService", WXSecurityService.class);
	}

	public static WXAPIService getServiceWXAPIService() {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
		return context.getTBeanObject("wx.enterprise.APIService", WXAPIService.class);
	}

	public static SmUserWxService getSmUserWxService() {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("org/frameworkset/wx/common/util/bboss-wx.xml");
		return context.getTBeanObject("wx.smUserWxService", SmUserWxService.class);

	}

	public static boolean uselocalsession() {
		return ParamsHandler.getParamsHandler("cms.siteparamshandler").getParams("weixin", "weixin")
				.getAttributeBoolean(0, "wx.uselocalsession", true);
	}

	public static String getEnterpriseUserInfoURL() {

		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.userid.url", "weixin");
		return (String) p.getValue();
	}

	public static String getEnterpriseAccessTokenURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.token.url", "weixin");
		return (String) p.getValue();
	}

	public static String getServiceAccessTokenURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.service.token.url",
				"weixin");
		return (String) p.getValue();
	}

	public static String getServiceOauthURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.service.oauth.url",
				"weixin");
		return (String) p.getValue();
	}

	public static String getEnterpriseSendWeChatMsgURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.sendMsg.url",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信统一下单地址
	 * 
	 * @return
	 */
	public static String getServiceUnifiedorderMsgURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.unifiederOrderMsg.url",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信公众号的appSecret
	 * 
	 * @return
	 */
	public static String getServiceAppSecret() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.key.appSecret",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信公众的网页授权后的重定向页面
	 * 
	 * @return
	 */
	public static String getServiceRedictURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.key.appId", "weixin");
		return (String) p.getValue();
	}

	public static String getServiceTicketURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.service.ticket.url",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获得公众号的商户支付地址（提现使用）
	 * 
	 * @return
	 */
	public static String getServiceCompanyURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.service.company.url",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获得微信语音下载地址
	 * 
	 * @return
	 */
	public static String getServiceVoiceDownloadURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.voicie.download.url",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信语音下载保存到服务器的地址
	 * 
	 * @return
	 */
	public static String getServiceVoiceDownloadPath() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.voice.download.path",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信公众的AppId
	 * 
	 * @return
	 */
	public static String getServiceAppId() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.key.appId", "weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信公众号的商户id
	 * 
	 * @return
	 */
	public static String getServiceMchId() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.key.mchId", "weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信公众号的商户名称
	 * 
	 * @return
	 */
	public static String getServiceMchName() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.key.mchName",
				"weixin");
		return (String) p.getValue();
	}

	/**
	 * 获取微信的商户key
	 * 
	 * @return
	 */
	public static String getServiceMchKey() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "wx.key.mchKey", "weixin");
		return (String) p.getValue();
	}

	public static String getEnterpriseToken(String app) {
		if (StringUtil.isEmpty(app)) {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.token",
					"weixin");
			return (String) p.getValue();
		} else {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", app + ".qywx.token",
					"weixin");
			return (String) p.getValue();
		}

	}

	public static String getEnterpriseCorpid(String app) {
		if (StringUtil.isEmpty(app)) {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.corpid",
					"weixin");
			return (String) p.getValue();
		} else {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", app + ".qywx.corpid",
					"weixin");
			return (String) p.getValue();
		}
	}

	public static String getEnterpriseCorpsecret(String app) {
		if (StringUtil.isEmpty(app)) {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.corpsecret",
					"weixin");
			return (String) p.getValue();
		} else {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin",
					app + ".qywx.corpsecret", "weixin");
			return (String) p.getValue();
		}
	}

	public static String getEnterpriseAeskey(String app) {
		if (StringUtil.isEmpty(app)) {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", "qywx.aeskey",
					"weixin");
			return (String) p.getValue();
		} else {
			Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin", app + ".qywx.aeskey",
					"weixin");
			return (String) p.getValue();
		}
	}

	/**
	 * 获得微信统一下单回调url
	 * 
	 * @return
	 */
	public static String getServiceUnifiederOrderNotifyURL() {
		Param p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParam("weixin",
				"wx.unifiederOrderNotify.url", "weixin");
		return (String) p.getValue();
	}

	public static String[] getMethodByASCIIsort() {
		return getMethodByASCIIsort("unified_order");
	}

	public static String[] getMethodByASCIIsort(String flag) {
		String[] rs = null;
		if ("unified_order".equals(flag)) {
			Arrays.sort(UNIFIED_ORDER_COLUMN);
			rs = new String[UNIFIED_ORDER_COLUMN.length];
			for (int i = 0; i < UNIFIED_ORDER_COLUMN.length; i++) {
				rs[i] = "get" + captureName(getBeanName(UNIFIED_ORDER_COLUMN[i]));
			}
		} else if ("jsapi".equals(flag)) {
			Arrays.sort(PAYSIGN_COLUMN);
			rs = new String[PAYSIGN_COLUMN.length];
			for (int i = 0; i < PAYSIGN_COLUMN.length; i++) {
				if (PAYSIGN_COLUMN[i].equals("package"))
					rs[i] = "getPrepayId";
				else
					rs[i] = "get" + captureName(getBeanName(PAYSIGN_COLUMN[i]));
			}
		}
		return rs;

	}

	public static String[] getMethodByASCIIsort(String[] columns) {
		String[] rs = null;
		Arrays.sort(columns);
		rs = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals("package"))
				rs[i] = "getPrepayId";
			else
				rs[i] = "get" + captureName(getBeanName(columns[i]));
		}
		return rs;

	}

	/**
	 * 将列表映射为javabean的规范对象命名
	 * 
	 * @param columnName
	 * @return
	 */
	public static String getBeanName(String columnName) {
		int index = columnName.indexOf("_");
		if (index < 0) {
			return columnName;
		}
		String tmpName = columnName;
		if (index > 0) {
			tmpName = tmpName.substring(0, index);
			tmpName = tmpName + captureName(columnName.substring(index + 1));
		}
		index = tmpName.indexOf("_");
		if (index > 0) {
			tmpName = getBeanName(tmpName);
		}
		return tmpName;
	}

	/**
	 * 首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String captureName(String name) {
		if (!Character.isLowerCase(name.charAt(0)))
			return name;
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}

	public static void main(String args[]) {
		// getMethodByASCIIsort();
		System.out.println(PropertiesUtil.getUrl("weixin.service.download.url"));
	}
}
