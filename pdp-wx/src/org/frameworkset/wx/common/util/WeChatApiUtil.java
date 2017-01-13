/**
 * 
 */
package org.frameworkset.wx.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpException;
import org.frameworkset.spi.remote.http.HttpRequestUtil;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

/**
 * @author suwei
 * @date 2017年1月6日
 *
 */
public class WeChatApiUtil {
	// token 接口(GET)
	private static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	// 素材上传(POST)
	private static final String UPLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/upload";
	// 素材下载:不支持视频文件的下载(GET)
	private static final String DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";

	public static String getTokenUrl(String appId, String appSecret) {
		return String.format(ACCESS_TOKEN, appId, appSecret);
	}

	public static String getDownloadUrl(String token, String mediaId) {
		return String.format(DOWNLOAD_MEDIA, token, mediaId);
	}

	/**
	 * 微信服务器素材上传
	 * 
	 * @param file
	 *            表单名称media
	 * @param token
	 *            access_token
	 * @param type
	 *            type只支持四种类型素材(video/image/voice/thumb)
	 */
	public static void uploadMedia(File file, String token, String type) {
		if (file == null || token == null || type == null) {
			return  ;
		}

		if (!file.exists()) {
			System.out.println("上传文件不存在,请检查!");
			return  ;
		}

		String url = UPLOAD_MEDIA;
//		JSONObject jsonObject = null;
//		PostMethod post = new PostMethod(url);
//		post.setRequestHeader("Connection", "Keep-Alive");
//		post.setRequestHeader("Cache-Control", "no-cache");
//		FilePart media = null;
//		HttpClient httpClient = new HttpClient();
//		// 信任任何类型的证书 Protocol("https", new MySSLProtocolSocketFactory(), 443);
//		Protocol myhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
//		Protocol.registerProtocol("https", myhttps);

		try {
//			media = new FilePart("media", file);
			Map  params= new HashMap();
			params.put("access_token", token);
			params.put("type", type);
			Map files = new HashMap();
			files.put("media", file);
			Map headers = new HashMap();
			headers.put("Connection", "Keep-Alive");
			headers.put("Cache-Control", "no-cache");
			String text = HttpRequestUtil.httpPostFileforString("default", url, null, null, params, files, headers);
			
//			Part[] parts = new Part[] { new StringPart("access_token", token),
//					new StringPart("type", type), media };
//			MultipartRequestEntity entity = new MultipartRequestEntity(parts,
//					post.getParams());
//			post.setRequestEntity(entity);
//			int status = httpClient.executeMethod(post);
//			if (status == HttpStatus.SC_OK) {
//				String text = post.getResponseBodyAsString();
//				// jsonObject = JSONObject.fromObject(text);
//			} else {
//				System.out.println("upload Media failure status is:" + status);
//			}
		} catch (FileNotFoundException execption) {
			System.out.println(execption);
		} catch (HttpException execption) {
			System.out.println(execption);
		} catch (IOException execption) {
			System.out.println(execption);
		}
//		return jsonObject;
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 多媒体下载接口
	 * 
	 * @comment 不支持视频文件的下载
	 * @param fileName
	 *            素材存储文件路径
	 * @param token
	 *            认证token
	 * @param mediaId
	 *            素材ID（对应上传后获取到的ID）
	 * @return 素材文件
	 */
	public static File downloadMedia(String fileName, String token,
			String mediaId) {
		String url = getDownloadUrl(token, mediaId);
		return httpRequestToFile(fileName, url, "GET", null);
	}

	/**
	 * 以http方式发送请求,并将请求响应内容输出到文件
	 * 
	 * @param path
	 *            请求路径
	 * @param method
	 *            请求方法
	 * @param body
	 *            请求数据
	 * @return 返回响应的存储到文件
	 */
	public static File httpRequestToFile(String fileName, String path, String method, String body) {
		if (fileName == null || path == null || method == null) {
			return null;
		}

		File file = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		FileOutputStream fileOut = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.close();
			}

			inputStream = conn.getInputStream();
			if (inputStream != null) {
				file = new File(fileName);
			} else {
				return file;
			}

			// 写入到文件
			fileOut = new FileOutputStream(file);
			if (fileOut != null) {
				int c = inputStream.read();
				while (c != -1) {
					fileOut.write(c);
					c = inputStream.read();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}

			/*
			 * 必须关闭文件流 否则JDK运行时，文件被占用其他进程无法访问
			 */
			try {
				inputStream.close();
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 发送请求以https方式发送请求并将请求响应内容以String方式返回
	 * 
	 * @param path
	 *            请求路径
	 * @param method
	 *            请求方法
	 * @param body
	 *            请求数据体
	 * @return 请求响应内容转换成字符串信息
	 */
	public static String httpsRequestToString(String path, String method, String body) {
		if (path == null || method == null) {
			return null;
		}

		String response = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpsURLConnection conn = null;
		try {
			TrustManager[] tm = { new JEEWeiXinX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			System.out.println(path);
			URL url = new URL(path);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.close();
			}

			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			response = buffer.toString();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			try {
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
			} catch (IOException execption) {
				System.out.println(execption);
			}
		}
		return response;
	}

	/**
	 * 把amr格式的语音转换成MP3 
	 * @Title: changeToMp3 
	 * @Description:
	 * TODO(把amr格式的语音转换成MP3) 
	 * @param sourcePath amr格式文件路径 
	 * @param targetPath 存放mp3格式文件路径 
	 * @return void 返回类型 
	 * @throws
	 */
	public static void changeToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();
		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File f = new File("D:/test.png");
		// 下载刚刚上传的图片以id命名
		String media_id = "gwxFHudktor_7IVjHuufZ8pMWd659eDapzF9yjBatrMXylT_dwIOHOkYUqF1ZvvJ";
		String accessToken = "naGCK8_jld7GDFFyaAHBU6t4Ogn4uQBR5ZLZr9FOtAGWL1H7ck5CBQGIwUIHGWmPpL7Ck8m1e97QWW9y_wqlffdPkTXQBlzNbsmK5KffTm3Z8GAimb_zYr7A7moia0XjQJJcAIAEXX";
		File t = WeChatApiUtil.downloadMedia("D:/" + media_id + ".arm", accessToken, media_id);
		changeToMp3("D:/" + media_id + ".arm", "D:/11111111.arm");
	}

}

class JEEWeiXinX509TrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}