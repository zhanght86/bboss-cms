/**
 * 
 */
package test.wxorder;

import java.net.URLEncoder;

/**
 * @author Administrator
 *
 */
public class GetWeiXinCode {
	public static String GetCodeRequest = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

	public static String getCodeRequest() {
		String result = null;
		GetCodeRequest = GetCodeRequest.replace("APPID", "wxe88cfaa46b73c192");
		GetCodeRequest = GetCodeRequest.replace("REDIRECT_URI", urlEnodeUTF8("http://www.becst.com.cn/user/showRegisterPage.page"));
		GetCodeRequest = GetCodeRequest.replace("SCOPE","snsapi_base");
		result = GetCodeRequest;
		return result;
	}

	public static String urlEnodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		System.out.println(getCodeRequest());
	}

}
