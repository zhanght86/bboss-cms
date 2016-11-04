/**
 * 
 */
package test.wxorder;

import java.net.URLEncoder;

import org.frameworkset.wx.common.entity.OAuthSnsAPIBase;
import org.frameworkset.wx.common.entity.WxOrderMessage;
import org.frameworkset.wx.common.service.WXAPIService;
import org.frameworkset.wx.publics.WXAPIServiceImpl;

/**
 * @author Administrator
 *
 */
public class WxOrderTest {

	public static void main(String args[]) {
		String urs = "https://www.becst.com.cn:9081/wowo-frontend-1.0.0";
		System.out.println(URLEncoder.encode(urs));
		WxOrderMessage orderMsg = new WxOrderMessage();
		orderMsg.setAppid("wxe88cfaa46b73c192");
		orderMsg.setDeviceInfo("013467007045764");
		orderMsg.setAttach("wowo");
		orderMsg.setBody("wowocharge");
		// orderMsg.setDetail(
		// "<![CDATA[{\"goods_detail\":[{\"goods_id\":\"iphone6s_32G\",\"wxpay_goods_id\":\"1002\",\"goods_name\":\"iPhone6s
		// 32G\",\"quantity\":1,\"price\":608800,\"goods_category\":\"123789\",\"body\":\"苹果手机\"}]}]]>");
		orderMsg.setAttach("changshazongdian");
		orderMsg.setFeeType("CNY");
		orderMsg.setOutTradeNo("w1000001");
		orderMsg.setTotalFee("0.01");
		orderMsg.setSpbillCreateIp("139.224.19.207");
		orderMsg.setNotifyUrl("http://www.becst.com.cn/wowo-service-server-1.0.0/hessian/payNotify");
		orderMsg.setTradeType("JSAPI");

		// orderMsg.setTimeStart("20161013024210");
		// orderMsg.setTimeExpire("20161013034220");
		// orderMsg.setGoodsTag("WXG");

		orderMsg.setOpenid("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
		WXAPIServiceImpl sb = new WXAPIServiceImpl();

		// OAuthInfo obj= sb.getOAuthOpenId("wxe88cfaa46b73c192",
		// "ec81fb87c5436d44c5faf1edf47bc5a9", "snsapi_base ");
		WXAPIService wservice = new WXAPIServiceImpl();
		try {
			OAuthSnsAPIBase obj = sb.getWeiXinSnsAPIBase("001y9FUn1zH9km08jqVn1DcBUn1y9FUS");
			// String result = wservice.unifiedorder(orderMsg);
			System.out.println(obj.getOpenId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String accessToken = null;
		// WXAPIService service = new WXAPIServiceImp();
		// try {
		// System.out.println(service.unifiedorder(orderMsg, accessToken));
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
