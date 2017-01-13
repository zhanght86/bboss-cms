/**
 * 
 */
package org.frameworkset.wx.publics;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.frameworkset.wx.common.service.WXVoiceService;
import org.frameworkset.wx.common.util.WXHelper;

import sun.net.www.protocol.https.Handler;

/**
 * 微信语音接口
 * 
 * @author suwei
 * @date 2017年1月5日
 *
 */
public class WXVoiceServiceImpl implements WXVoiceService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.frameworkset.wx.common.service.WXVoiceService#downloadVoice(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void downloadVoice(String accessToken, String mediaId) {
		String voiceDownloadURL = WXHelper.getServiceVoiceDownloadURL() + "?access_token=" + accessToken + "&media_id="
				+ mediaId;
		String voiceDownloadSavePath = WXHelper.getServiceVoiceDownloadPath();
		try {
			URL url = new URL(null, voiceDownloadURL, new Handler());
			HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("jsse.enableSNIExtension", "false");
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			// 读取超时30秒
			http.connect();
			// 获取文件转化为byte流
			InputStream is = null;
			// 获取文件转化为byte流
			is = http.getInputStream();
			// 存储到硬盘，原本音频格式为amr
			FileWriter fw = new FileWriter("d:/1111111111111.amr");
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			int byteCount = 0;
			while ((byteCount = is.read(jsonBytes)) != -1) {
				fw.write(byteCount);
			}
			fw.close();
			is.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static String getConnection(String sendUrl) {
		try {
			URL url = new URL(sendUrl);
			HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			// 读取超时30秒
			http.connect();
			OutputStream os = http.getOutputStream();
			InputStream is = http.getInputStream();
			int size = is.available();
			int bytesWritten = 0;
			int byteCount = 0;
			byte[] jsonBytes = new byte[size];
			while ((byteCount = is.read(jsonBytes)) != -1) {
				os.write(jsonBytes, bytesWritten, byteCount);
				bytesWritten += byteCount;
			}
			os.flush();
			os.close();
			return new String(jsonBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {

		try {
			System.out.println(getConnection(
					"https://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
							+ WXHelper.getServiceWXAPIService()
									.getWxAccessToken("wxe88cfaa46b73c192", "ec81fb87c5436d44c5faf1edf47bc5a9")
									.getAccess_token()
							+ "&media_id=kxWEa7IQnRcuGGWP5IoacTE3-Z54WK4miTInJq7_Zk8BKDe7BT5Iq1cnEizFgHPe"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
