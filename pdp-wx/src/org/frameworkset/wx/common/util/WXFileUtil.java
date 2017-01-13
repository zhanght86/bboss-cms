/**
*
*/
package org.frameworkset.wx.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @authorsuwei
 * @date2017年1月6日
 *
 */
public class WXFileUtil {

	public static void writeFile(String fileName, String text) throws IOException {
		FileWriter fw = new FileWriter(fileName, true);
		try {
			fw.write(text, 0, text.length());
		} catch (IOException ioe) {
			throw new IOException("Writetextto" + fileName + "fail!");
		} finally {
			fw.close();
		}

	}

	/**
	 * 获取媒体文件
	 * 
	 * @param accessToken
	 *            接口访问凭证
	 * @param media_id
	 *            媒体文件id
	 * @param savePath
	 *            文件在服务器上的存储路径
	 */
	public static String downloadMedia(String accessToken, String mediaId, String savePath) {
		String filePath = null;
		// 拼接请求地址
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
		System.out.println(requestUrl);
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");

			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			// 根据内容类型获取扩展名
//			String fileExt = WeixinUtil.getFileEndWitsh(conn.getHeaderField("Content-Type"));
			String fileExt = "arm";
			// 将mediaId作为文件名
			filePath = savePath + mediaId + fileExt;
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();

			conn.disconnect();
			String info = String.format("下载媒体文件成功，filePath=" + filePath);
			System.out.println(info);
		} catch (Exception e) {
			filePath = null;
			String error = String.format("下载媒体文件失败：%s", e);
			System.out.println(error);
		}
		return filePath;
	}

	public static void main(String[] args) {
		/*FileOutputStream out = null;
		FileOutputStream outSTr = null;
		BufferedOutputStream Buff = null;
		FileWriter fw = null;
		int count = 1000;// 写文件行数
		try {
			out = new FileOutputStream(new File("C:/add.txt"));
			long begin = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				out.write("测试java文件操作\r\n".getBytes());
			}
			out.close();
			long end = System.currentTimeMillis();
			System.out.println("FileOutputStream执行耗时:" + (end - begin) + "豪秒");
			outSTr = new FileOutputStream(new File("C:/add0.txt"));
			Buff = new BufferedOutputStream(outSTr);
			long begin0 = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				Buff.write("测试java文件操作\r\n".getBytes());
			}
			Buff.flush();
			Buff.close();
			long end0 = System.currentTimeMillis();
			System.out.println("BufferedOutputStream执行耗时:" + (end0 - begin0) + "豪秒");
			fw = new FileWriter("C:/add2.txt");
			long begin3 = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				fw.write("测试java文件操作\r\n");
			}
			fw.close();
			long end3 = System.currentTimeMillis();
			System.out.println("FileWriter执行耗时:" + (end3 - begin3) + "豪秒");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				Buff.close();
				outSTr.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		String accessToken="naGCK8_jld7GDFFyaAHBU6t4Ogn4uQBR5ZLZr9FOtAGWL1H7ck5CBQGIwUIHGWmPpL7Ck8m1e97QWW9y_wqlffdPkTXQBlzNbsmK5KffTm3Z8GAimb_zYr7A7moia0XjQJJcAIAEXX";
		String mediaId="kxWEa7IQnRcuGGWP5IoacTE3-Z54WK4miTInJq7_Zk8BKDe7BT5Iq1cnEizFgHPe";
		String savePath="D:/voice";
		downloadMedia(accessToken, mediaId, savePath);
		
	}
}
