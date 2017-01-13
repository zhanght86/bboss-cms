/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package org.frameworkset.wx.common.mp.aes;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.frameworkset.wx.common.entity.WxJSAPISign;
import org.frameworkset.wx.common.entity.WxOrderMessage;
import org.frameworkset.wx.common.entity.WxServiceCompany;
import org.frameworkset.wx.common.util.WXHelper;
 

/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParse {
	private static Logger log = Logger.getLogger(XMLParse.class);
	/**
	 * 提取出xml数据包中的加密消息
	 * 
	 * @param xmltext
	 *            待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws AesException
	 */
	public static Object[] extract(String xmltext) throws AesException {
		Object[] result = new Object[3];
		try {
			Document document = DocumentHelper.parseText(xmltext);

			Element root = document.getRootElement();
			// NodeList nodelist1 = root.elementText("Encrypt")("Encrypt");
			// NodeList nodelist2 = root.getElementsByTagName("ToUserName");
			result[0] = 0;
			result[1] = root.elementText("Encrypt");// nodelist1.item(0).getPrefix();//.getTextContent();
			result[2] = root.elementText("ToUserName");// nodelist2.item(0).getPrefix();//.getTextContent();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ParseXmlError);
		}
	}

	/**
	 * 生成xml消息
	 * 
	 * @param encrypt
	 *            加密后的消息密文
	 * @param signature
	 *            安全签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n" + "<TimeStamp>%3$s</TimeStamp>\n"
				+ "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);
	}

	public static String generateJsAPIXML(WxJSAPISign wxJSAPISign) throws UnsupportedEncodingException {
		List<String> xmllist = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>\n");
		if (wxJSAPISign.getAppId() != null)
			xmllist.add("<appId>" + wxJSAPISign.getAppId() + "</appId>\n");
		if (wxJSAPISign.getTimeStamp() != null)
			xmllist.add("<timeStamp>" + wxJSAPISign.getTimeStamp() + "</timeStamp>\n");
		if (wxJSAPISign.getNonceStr() != null)
			xmllist.add("<nonceStr>" + wxJSAPISign.getNonceStr() + "</nonceStr>\n");
		if (wxJSAPISign.getPrepayId() != null)
			xmllist.add("<package>prepay_id=" + wxJSAPISign.getPrepayId() + "</package>\n");
		if (wxJSAPISign.getSignType() != null)
			xmllist.add("<signType>" + wxJSAPISign.getSignType() + "</signType>\n");
		Collections.sort(xmllist);
		for (String str : xmllist) {
			sb.append(str);
		}
		sb.append("</xml>");

		return new String(sb.toString().getBytes(), "ISO8859-1");
	}

	/**
	 * @param encrypt
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String generateUnifiedOrderXML(WxOrderMessage orderMsg) throws UnsupportedEncodingException {
		List<String> xmllist = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>\n");
		if (orderMsg.getAppid() != null)
			xmllist.add("<appid>" + orderMsg.getAppid() + "</appid>\n");
		if (orderMsg.getAttach() != null)
			xmllist.add("<attach>" + orderMsg.getAttach() + "</attach>\n");
		if (orderMsg.getBody() != null)
			xmllist.add("<body>" + orderMsg.getBody() + "</body>\n");
		if (orderMsg.getMchId() != null)
			xmllist.add("<mch_id>" + orderMsg.getMchId() + "</mch_id>\n");
		if (orderMsg.getDetail() != null)
			xmllist.add("<detail>" + orderMsg.getDetail() + "</detail>\n");
		if (orderMsg.getDeviceInfo() != null)
			xmllist.add("<device_info>" + orderMsg.getDeviceInfo() + "</device_info>\n");
		if (orderMsg.getNonceStr() != null)
			xmllist.add("<nonce_str>" + orderMsg.getNonceStr() + "</nonce_str>\n");
		if (orderMsg.getNotifyUrl() != null)
			xmllist.add("<notify_url>" + orderMsg.getNotifyUrl() + "</notify_url>\n");
		if (orderMsg.getOpenid() != null)
			xmllist.add("<openid>" + orderMsg.getOpenid() + "</openid>\n");
		if (orderMsg.getOutTradeNo() != null)
			xmllist.add("<out_trade_no>" + orderMsg.getOutTradeNo() + "</out_trade_no>\n");
		if (orderMsg.getSign() != null)
			xmllist.add("<sign>" + orderMsg.getSign() + "</sign>\n");
		if (orderMsg.getSpbillCreateIp() != null)
			xmllist.add("<spbill_create_ip>" + orderMsg.getSpbillCreateIp() + "</spbill_create_ip>\n");
		if (orderMsg.getTotalFee() != null)
			xmllist.add("<total_fee>" + orderMsg.getTotalFee() + "</total_fee>\n");
		if (orderMsg.getTradeType() != null)
			xmllist.add("<trade_type>" + orderMsg.getTradeType() + "</trade_type>\n");
		if (orderMsg.getFeeType() != null)
			xmllist.add("<fee_type>" + orderMsg.getFeeType() + "</fee_type>\n");
		if (orderMsg.getTimeStart() != null)
			xmllist.add("<time_start>" + orderMsg.getTimeStart() + "</time_start>\n");
		if (orderMsg.getTimeExpire() != null)
			xmllist.add("<time_expire>" + orderMsg.getTimeExpire() + "</time_expire>\n");
		if (orderMsg.getGoodsTag() != null)
			xmllist.add("<goods_tag>" + orderMsg.getGoodsTag() + "</goods_tag>\n");
		if (orderMsg.getProductId() != null)
			xmllist.add("<product_id>" + orderMsg.getProductId() + "</product_id>\n");
		if (orderMsg.getLimitPay() != null)
			xmllist.add("<limit_pay>" + orderMsg.getLimitPay() + "</limit_pay>\n");
		Collections.sort(xmllist);
		for (String str : xmllist) {
			sb.append(str);
		}
		sb.append("</xml>");
		return new String(sb.toString().getBytes(), "ISO8859-1");
	}

	public static String generateServiceCompanyPayXML(WxServiceCompany wxServiceCompany, String[] columnNames) {
		String xml = null;
		try {
			Arrays.sort(columnNames);
			Class t=WxServiceCompany.class;
			int i = 0;
			StringBuffer sb = new StringBuffer();
			sb.append("<xml>\n");
			int j=0;
			for (String column : WXHelper.getMethodByASCIIsort(columnNames)) {
				Method m = null;
				try {
					m =t.getMethod(column);
				} catch (NoSuchMethodException e) {
					log.debug("the method:  " + column + " not found in class:" +t.getName());
					i++;
					continue;
				}
				Object value = m.invoke(wxServiceCompany, null);
				sb.append("<");
				sb.append(columnNames[j]);
				sb.append(">");
				sb.append(value);
				sb.append("</");
				sb.append(columnNames[j]);
				sb.append(">\n");
				j++;
			}
			sb.append("</xml>");
			xml=sb.toString();
			System.out.println(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml;
	}

}
