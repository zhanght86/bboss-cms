/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

/**
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */
package org.frameworkset.wx.common.mp.aes;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.frameworkset.wx.common.entity.WxJSAPISign;
import org.frameworkset.wx.common.entity.WxNotify;
import org.frameworkset.wx.common.entity.WxOrderMessage;
import org.frameworkset.wx.common.entity.WxServiceCompany;
import org.frameworkset.wx.common.enums.EnumWeiXinEncodeType;
import org.frameworkset.wx.common.util.Md5;
import org.frameworkset.wx.common.util.WXHelper;
 

/**
 * 提供接收和推送给公众平台消息的加解密接口(UTF8编码的字符串).
 * <ol>
 * <li>第三方回复加密消息给公众平台</li>
 * <li>第三方收到公众平台发送的消息，验证消息的安全性，并对消息进行解密。</li>
 * </ol>
 * 说明：异常java.security.InvalidKeyException:illegal Key Size的解决方案
 * <ol>
 * <li>在官方网站下载JCE无限制权限策略文件（JDK7的下载地址：
 * http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html</li>
 * <li>下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt</li>
 * <li>如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件</li>
 * <li>如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件</li>
 * </ol>
 */
public class WXBizMsgCrypt {
	private static Logger log = Logger.getLogger(WXBizMsgCrypt.class);

	static Charset CHARSET = Charset.forName("utf-8");
	Base64 base64 = new Base64();
	byte[] aesKey;
	String token;
	String corpId;

	/**
	 * 构造函数
	 * 
	 * @param token
	 *            公众平台上，开发者设置的token
	 * @param encodingAesKey
	 *            公众平台上，开发者设置的EncodingAESKey
	 * @param corpId
	 *            企业的corpid
	 * 
	 * @throws AesException
	 *             执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public WXBizMsgCrypt(String token, String encodingAesKey, String corpId) throws AesException {
		if (encodingAesKey.length() != 43) {
			throw new AesException(AesException.IllegalAesKey);
		}

		this.token = token;
		this.corpId = corpId;
		aesKey = Base64.decodeBase64((encodingAesKey + "=").getBytes());
	}

	// 生成4个字节的网络字节序
	byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	// 还原4个字节的网络字节序
	int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	// 随机生成16位字符串
	String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 对明文进行加密.
	 * 
	 * @param text
	 *            需要加密的明文
	 * @return 加密后base64编码的字符串
	 * @throws AesException
	 *             aes加密失败
	 */
	String encrypt(String randomStr, String text) throws AesException {
		ByteGroup byteCollector = new ByteGroup();
		byte[] randomStrBytes = randomStr.getBytes(CHARSET);
		byte[] textBytes = text.getBytes(CHARSET);
		byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
		byte[] corpidBytes = corpId.getBytes(CHARSET);

		// randomStr + networkBytesOrder + text + corpid
		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(corpidBytes);

		// ... + pad: 使用自定义的填充方式对明文进行补位填充
		byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		// 获得最终的字节流, 未加密
		byte[] unencrypted = byteCollector.toBytes();

		try {
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			// 加密
			byte[] encrypted = cipher.doFinal(unencrypted);

			// 使用BASE64对加密后的字符串进行编码
			String base64Encrypted = new String(base64.encode(encrypted));

			return base64Encrypted;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.EncryptAESError);
		}
	}

	/**
	 * 对密文进行解密.
	 * 
	 * @param text
	 *            需要解密的密文
	 * @return 解密得到的明文
	 * @throws AesException
	 *             aes解密失败
	 */
	String decrypt(String text) throws AesException {
		byte[] original;
		try {
			// 设置解密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

			// 使用BASE64对密文进行解码
			byte[] encrypted = Base64.decodeBase64(text.getBytes());

			// 解密
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.DecryptAESError);
		}

		String xmlContent, from_corpid;
		try {
			// 去除补位字符
			byte[] bytes = PKCS7Encoder.decode(original);

			// 分离16位随机字符串,网络字节序和corpId
			byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

			int xmlLength = recoverNetworkBytesOrder(networkOrder);

			xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
			from_corpid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.IllegalBuffer);
		}

		// corpid不相同的情况
		if (!from_corpid.equals(corpId)) {
			throw new AesException(AesException.ValidateCorpidError);
		}
		return xmlContent;

	}

	/**
	 * 将公众平台回复用户的消息加密打包.
	 * <ol>
	 * <li>对要发送的消息进行AES-CBC加密</li>
	 * <li>生成安全签名</li>
	 * <li>将消息密文和安全签名打包成xml格式</li>
	 * </ol>
	 * 
	 * @param replyMsg
	 *            公众平台待回复用户的消息，xml格式的字符串
	 * @param timeStamp
	 *            时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce
	 *            随机串，可以自己生成，也可以用URL参数的nonce
	 * 
	 * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce,
	 *         encrypt的xml格式的字符串
	 * @throws AesException
	 *             执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String EncryptMsg(String replyMsg, String timeStamp, String nonce) throws AesException {
		// 加密
		String encrypt = encrypt(getRandomStr(), replyMsg);

		// 生成安全签名
		if (timeStamp == "") {
			timeStamp = Long.toString(System.currentTimeMillis());
		}

		String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt);

		// System.out.println("发送给平台的签名是: " + signature[1].toString());
		// 生成发送的xml
		String result = XMLParse.generate(encrypt, signature, timeStamp, nonce);
		return result;
	}

	/**
	 * 检验消息的真实性，并且获取解密后的明文.
	 * <ol>
	 * <li>利用收到的密文生成安全签名，进行签名验证</li>
	 * <li>若验证通过，则提取xml中的加密消息</li>
	 * <li>对消息进行解密</li>
	 * </ol>
	 * 
	 * @param msgSignature
	 *            签名串，对应URL参数的msg_signature
	 * @param timeStamp
	 *            时间戳，对应URL参数的timestamp
	 * @param nonce
	 *            随机串，对应URL参数的nonce
	 * @param postData
	 *            密文，对应POST请求的数据
	 * 
	 * @return 解密后的原文
	 * @throws AesException
	 *             执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String DecryptMsg(String msgSignature, String timeStamp, String nonce, String postData) throws AesException {

		// 密钥，公众账号的app secret
		// 提取密文
		Object[] encrypt = XMLParse.extract(postData);

		// 验证安全签名
		String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt[1].toString());

		// 和URL中的签名比较是否相等
		// System.out.println("第三方收到URL中的签名：" + msg_sign);
		// System.out.println("第三方校验签名：" + signature);
		if (!signature.equals(msgSignature)) {
			throw new AesException(AesException.ValidateSignatureError);
		}

		// 解密
		String result = decrypt(encrypt[1].toString());
		return result;
	}

	/**
	 * 验证URL
	 * 
	 * @param msgSignature
	 *            签名串，对应URL参数的msg_signature
	 * @param timeStamp
	 *            时间戳，对应URL参数的timestamp
	 * @param nonce
	 *            随机串，对应URL参数的nonce
	 * @param echoStr
	 *            随机串，对应URL参数的echostr
	 * 
	 * @return 解密之后的echostr
	 * @throws AesException
	 *             执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String VerifyURL(String msgSignature, String timeStamp, String nonce, String echoStr) throws AesException {
		String signature = SHA1.getSHA1(token, timeStamp, nonce, echoStr);
		System.out.println(signature);
		if (!signature.equals(msgSignature)) {
			throw new AesException(AesException.ValidateSignatureError);
		}

		String result = decrypt(echoStr);
		return result;
	}

	/**
	 * 验证微信公众号token
	 * 
	 * @param msgSignature
	 * @param timeStamp
	 * @param nonce
	 * @param echoStr
	 * @return
	 * @throws AesException
	 */
	public String VerifyServiceURL(String msgSignature, String timeStamp, String nonce, String echoStr)
			throws AesException {
		String signature = SHA1.getSHA1(token, timeStamp, nonce);
		System.out.println(signature);
		if (!signature.equals(msgSignature)) {
			throw new AesException(AesException.ValidateSignatureError);
		}
		return echoStr;
	}

	/**
	 * 获得微信统一下单的签名
	 * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
	 * 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。
	 * 
	 * @param orderMessage
	 *            订单信息
	 * @param apiKey
	 *            微信公众的AppSecret
	 * @return
	 */
	public static String getWXSign(WxOrderMessage orderMessage, String apiKey) throws AesException {
		verifyUnifiedOrderParam(orderMessage);
		String sign = null;
		try {
			Class t = Class.forName("org.frameworkset.wx.common.entity.WxOrderMessage");
			Arrays.sort(WXHelper.UNIFIED_ORDER_COLUMN);
			int i = 0;
			StringBuffer pingStr = new StringBuffer();
			for (String column : WXHelper.getMethodByASCIIsort()) {
				Method m = t.getMethod(column);
				String value = (String) m.invoke(orderMessage, null);
				if (value == null || "".equals(value)) {
					i++;
					continue;
				}
				pingStr.append(new BasicNameValuePair(WXHelper.UNIFIED_ORDER_COLUMN[i], value));
				System.out.println(pingStr.toString());
				if (i < WXHelper.UNIFIED_ORDER_COLUMN.length) {
					pingStr.append("&");
				}
				i++;
			}
			System.out.println(pingStr);
			String stringA = pingStr.toString() + "key=" + apiKey;
			System.out.println(stringA);
			sign = new Md5().getMD5ofStr(stringA).toUpperCase();
			System.out.println(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	public static String getWXPaySign(String className, WxJSAPISign wxJSAPISign, String apiKey) throws AesException {
		// verifyUnifiedOrderParam(orderMessage);
		String sign = null;
		try {
			Class t = Class.forName(className);
			Arrays.sort(WXHelper.PAYSIGN_COLUMN);
			int i = 0;
			StringBuffer pingStr = new StringBuffer();
			for (String column : WXHelper.getMethodByASCIIsort("jsapi")) {
				Method m = t.getMethod(column);
				String value = (String) m.invoke(wxJSAPISign, null);
				if (value == null || "".equals(value)) {
					i++;
					continue;
				}
				if (WXHelper.PAYSIGN_COLUMN[i].equals("package"))
					pingStr.append(new BasicNameValuePair(WXHelper.PAYSIGN_COLUMN[i], "prepay_id=" + value));
				else
					pingStr.append(new BasicNameValuePair(WXHelper.PAYSIGN_COLUMN[i], value));
				System.out.println(pingStr.toString());
				if (i < WXHelper.PAYSIGN_COLUMN.length) {
					pingStr.append("&");
				}
				i++;
			}
			System.out.println(pingStr);
			String stringA = pingStr.toString() + "key=" + apiKey;
			System.out.println(stringA);
			sign = new Md5().getMD5ofStr(stringA).toUpperCase();
			System.out.println(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	public static String getWXSign(Class t, Object obj, String apiKey, String[] COLUMN_NAME)
			throws AesException {
		String sign = null;
		try {
			Arrays.sort(COLUMN_NAME);
			// Class t = Class.forName(className);
			int i = 0;
			StringBuffer pingStr = new StringBuffer();
			for (String column : WXHelper.getMethodByASCIIsort(COLUMN_NAME)) {
				Method m = null;
				try {
					m = t.getMethod(column);
				} catch (NoSuchMethodException e) {
					log.debug("the method:  " + column + " not found in class:" + t.getName());
					i++;
					continue;
				}
				Object value = m.invoke(obj, null);
				if (value == null || "".equals(value) || "sign".equals(column)) {
					i++;
					continue;
				}
				if (COLUMN_NAME[i].equals("package"))
					pingStr.append(new BasicNameValuePair(COLUMN_NAME[i], "prepay_id=" + value));
				else
					pingStr.append(new BasicNameValuePair(COLUMN_NAME[i], value.toString()));
				System.out.println(pingStr.toString());
				if (i < COLUMN_NAME.length - 1) {
					pingStr.append("&");
				}
				i++;
			}
			System.out.println(pingStr);
			// if(enumWeiXinEncodeType==EnumWeiXinEncodeType.MD5){
			String stringA = "";
			stringA = pingStr.toString() + "&key=" + apiKey;
			System.out.println(stringA);
			sign = new Md5().getMD5ofStr(stringA).toUpperCase();
			// }else if(EnumWeiXinEncodeType.SHA1==enumWeiXinEncodeType){
			// SHA1.getSHA1(token, timestamp, nonce)
			// }

			System.out.println(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 验证微信统一下单的必填参数
	 * 
	 * @param 公众账号ID
	 *            appid 是
	 * @param 商户号
	 *            mch_id 是
	 * 
	 * @param 签名
	 *            sign 是
	 * @param 商品描述
	 *            body 是
	 * @param 商户订单号
	 *            out_trade_no 是
	 * @param 总金额
	 *            total_fee 是
	 * @param 终端IP
	 *            spbill_create_ip 是
	 * @param 通知地址
	 *            notify_url 是
	 * @param 交易类型
	 *            trade_type 是
	 * 
	 * @param orderMessage
	 * @return
	 * @throws AesException
	 */
	public static boolean verifyUnifiedOrderParam(WxOrderMessage orderMessage) throws AesException {
		if (orderMessage == null)
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getAppid() == null || "".equals(orderMessage.getAppid()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getMchId() == null || "".equals(orderMessage.getMchId()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		// if (orderMessage.getSign() == null ||
		// "".equals(orderMessage.getSign()))
		// throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getBody() == null || "".equals(orderMessage.getBody()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		// if (orderMessage.getOutTradeNo() == null ||
		// "".equals(orderMessage.getOutTradeNo()))
		// throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getTotalFee() == null || "".equals(orderMessage.getTotalFee()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getSpbillCreateIp() == null || "".equals(orderMessage.getSpbillCreateIp()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getNotifyUrl() == null || "".equals(orderMessage.getNotifyUrl()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		if (orderMessage.getTradeType() == null || "".equals(orderMessage.getTradeType()))
			throw new AesException(AesException.ValidateUnifiedOrderParamError);
		return true;
	}

	/**
	 * 验证支付成功后的回调通知签名
	 * 
	 * @param orderMessage
	 * @return
	 * @throws AesException
	 */
	public static boolean verifyNotifySign(WxNotify wxNotify) throws AesException {
		String COLUMN_NAME[] = {
				"appid", "mch_id", "device_info", "nonce_str",
				"sign", "sign_type", "result_code", "err_code",
				"err_code_des", "openid", "is_subscribe", "trade_type",
				"bank_type", "total_fee", "settlement_total_fee",
				"fee_type", "cash_fee", "cash_fee_type", "coupon_fee",
				"coupon_count", "transaction_id", "out_trade_no", "attach", "time_end"
		};
		// String paySign = getWXSign(wxNotify.getClass(), wxNotify,
		// WXHelper.getServiceMchKey(), COLUMN_NAME);
		String paySign = getWXSign(wxNotify.getClass(), wxNotify,
				"administratorwowo201612345678901", COLUMN_NAME);
		System.out.println(paySign);
		return true;
	}

	/**
	 * 获得签名
	 * 
	 * @param noncestr
	 * @param jsapiTicket
	 * @param timestamp
	 * @param currentUrl
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getJSSDKSign(String noncestr, String jsapiTicket, long timestamp, String currentUrl)
			throws NoSuchAlgorithmException {
		Map<String, Object> keyMap = new TreeMap<String, Object>();
		keyMap.put("noncestr", noncestr);
		keyMap.put("jsapi_ticket", jsapiTicket);
		keyMap.put("timestamp", timestamp);
		keyMap.put("url", currentUrl);
		return buildSign(keyMap, null, EnumWeiXinEncodeType.SHA1);
	}

	public static String buildSign(Map<String, Object> map, String key, EnumWeiXinEncodeType enumWeiXinEncodeType)
			throws NoSuchAlgorithmException {
		Set<Entry<String, Object>> set = map.entrySet();
		StringBuffer sb = new StringBuffer();
		// 取出排序后的参数，逐一连接起来
		for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, Object> me = it.next();
			sb.append(me.getKey() + "=" + me.getValue() + "&");
		}
		if (key != null)
			sb.append(key);
		else
			sb.deleteCharAt(sb.length() - 1); // 删除最后的&符号
		log.debug(sb.toString());
		System.out.println(sb);
		if (EnumWeiXinEncodeType.SHA1 == enumWeiXinEncodeType) { // sha1 加密算法
			return SHA1.getSha1(sb.toString());
		} else if (EnumWeiXinEncodeType.MD5 == enumWeiXinEncodeType) { // md5加密算法

		}
		return sb.toString().toUpperCase();// 返回最终排序后的结果，这里key不参与排序中，具体看接口规约
		// return
		// (Md5Encrypt.encrypt(sb.toString())).toUpperCase();//这个带有md5加密的，算法就不贴了，网上很多
	}

	/**
	 * 根据map对象，获得签名
	 * 
	 * @param keyMap
	 * @param sha1
	 * @return
	 */
	private static String getSignByMap(Map<String, Object> keyMap, EnumWeiXinEncodeType sha1) {
		Map<String, String> map = new TreeMap<String, String>();

		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) throws Exception {
		// System.out.println(getJSSDKSign("I6RnqxhBq0ypLBHNiDZSi68b",
		// "kgt8ON7yVITDhtdwci0qeed5e-WiIPDO9Mh0sbHhXyNG9ITKSwMpYjbjYCgvNt5F4GIcQvfGliBEbn3-0ie2dQ",
		// 1481640214,
		// "http://www.besct.com.cn/sd/testVoice.page"));
		// getJSSDKSign(noncestr, jsapiTicket, timestamp, currentUrl);
		// WxNotify wxNotify = new WxNotify();
		// wxNotify.setAppid("222");
		// wxNotify.setMchId("12331231232");
		// wxNotify.setDeviceInfo("fasfsad");
		// wxNotify.setNonceStr("ddddd");
		// wxNotify.setTotalFee("1");
		// // verifyNotifySign(wxNotify);
		//
		// String COLUMN_NAME[] = { "noncestr", "jsapi_ticket", "timestamp",
		// "url" };
		// WxJsapiTicket wxJsapiTicket = new WxJsapiTicket();
		// getWXPaySign(WxJsapiTicket.class, wxJsapiTicket, null, COLUMN_NAME)
		// WXBizMsgCrypt b=new WXBizMsgCrypt("wowo4l6zkjs1zimtdzswylhn",
		// "AxBhanBqb1hinGg3IwnOBbykN2q421m3mTCWbIiQuSq", "wxe88cfaa46b73c192");
		// b.VerifyURL("fc536012323ebd4d74947ec1da3d790064b53e86", "1477853717",
		// "1683104923", "701889282266065751");
		// WxConfig config = new WxConfig();
		// config.setAppId("wxf8b4f85f3a794e77");
		// config.setNonceStr("DMrOcbgYt0OIvICO");
		// config.setPrepayId("123456789");
		// config.setSignType("sha1");
		// config.setTimestamp("1481566139");
		// String payxml = XMLParse.generateJsAPIXML(config);
		// String paySign1 = getWXSign(config.getClass(), config,
		// "administratorwowo201612345678901", COLUMN_NAME);
		// System.out.println("---" + paySign1);

		// String paySign =
		// getWXPaySign("org.frameworkset.wx.common.entity.WxJSAPISign",
		// wxJSAPISign,
		// "administratorwowo201612345678901");
		// System.out.println(payxml + "---" + paySign);

		// WxOrderMessage orderMsg = new WxOrderMessage();
		// orderMsg.setAppid("wxe88cfaa46b73c192");
		// orderMsg.setMchId("1379567102");
		// orderMsg.setBody("wowo-charge");
		// // orderMsg.setBody("test");
		// orderMsg.setNotifyUrl("https://www.becst.com.cn/wowo-service-server-1.0.0/hessian/payNotify");
		// orderMsg.setOutTradeNo("w1000001");
		// orderMsg.setSpbillCreateIp("139.224.19.207");
		// orderMsg.setTotalFee("0.01");
		// orderMsg.setTradeType("JSAPI");
		// orderMsg.setDeviceInfo("1000");
		// orderMsg.setNonceStr("ibuaiVcKdpRxkhJA");
		// orderMsg.setOutTradeNo("w1000001" );
		// orderMsg.setSpbillCreateIp("139");
		// orderMsg.setTotalFee("001");
		// orderMsg.setNotifyUrl("htty");
		// orderMsg.setTradeType("JSAPI");
		// orderMsg.setNonceStr(RandomUtil.generateString(24)); //
		// // getWXUnifiedOrderNonce(orderMsg,
		// "ec81fb87c5436d44c5faf1edf47bc5a"));
		// String xml = XMLParse.generateUnifiedOrderXML(orderMsg);
		// System.out.println(xml);
		// String sign = getWXSign(orderMsg,
		// "ec81fb87c5436d44c5faf1edf47bc5a9");
		// System.out.println(sign);
		// System.out.println(MD5Util.toMD5(
		// "appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA&key=192006250b4c09247ec02edce69f6a2d"));
		String columnName[] = { "mch_appid", "mchid", "device_info", "nonce_str", "sign", "partner_trade_no", "openid",
				"check_name", "re_user_name", "amount", "desc", "spbill_create_ip" };

		WxServiceCompany wxServiceCompany = new WxServiceCompany();
		wxServiceCompany.setMchAppid("ec81fb87c5436d44c5faf1edf47bc5a9");
		wxServiceCompany.setMchid("1379567102");
		wxServiceCompany.setDeviceInfo("ddddddddd");
		wxServiceCompany.setNonceStr("DMrOcbgYt0OIvICO");
		wxServiceCompany.setSign("sign");
		wxServiceCompany.setPartnerTradeNo("partnerTradeNo");
		wxServiceCompany.setOpenid("dddddddddddddd");
		wxServiceCompany.setCheckName("etCheckName");
		wxServiceCompany.setReUserName("reUserName");
		wxServiceCompany.setAmount(100l);
		wxServiceCompany.setDesc("descddddddddddd");
		wxServiceCompany.setSpbillCreateIp("139.224.19.207");
		XMLParse.generateServiceCompanyPayXML(wxServiceCompany, columnName);
	}
}