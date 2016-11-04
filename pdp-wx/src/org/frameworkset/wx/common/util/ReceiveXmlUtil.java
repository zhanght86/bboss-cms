/**
 * 
 */
package org.frameworkset.wx.common.util;

import org.frameworkset.wx.common.entity.WxOrderResult;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author suwei
 * @date 2016年10月27日
 *
 */
public class ReceiveXmlUtil {
	/**
	 * 解析微信xml消息
	 * 
	 * @param strXml
	 * @return
	 */
	public static WxOrderResult getMsgEntity(String strXml) {
		WxOrderResult msg = null;
		try {
			if (strXml.length() <= 0 || strXml == null)
				return null;

			// 将字符串转化为XML文档对象
			Document document = DocumentHelper.parseText(strXml);
			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();

			// 遍历所有结点
			msg = new WxOrderResult();
			// 利用反射机制，调用set方法
			// 获取该实体的元类型
			Class<?> c = Class.forName("org.frameworkset.wx.common.entity.WxOrderResult");
			msg = (WxOrderResult) c.newInstance();// 创建这个实体的对象

			while (iter.hasNext()) {
				Element ele = (Element) iter.next();
				// 获取set方法中的参数字段（实体类的属性）
				String fieldName=WXHelper.getBeanName(ele.getName());
				Field field = c.getDeclaredField(fieldName);
				// 获取set方法，field.getType())获取它的参数数据类型
				Method method = c.getDeclaredMethod("set" + WXHelper.captureName(fieldName), field.getType());
				// 调用set方法
				method.invoke(msg, ele.getText());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("xml 格式异常: " + strXml);
			e.printStackTrace();
		}
		return msg;
	}

	public static void main(String args[]) {
		StringBuffer xml=new StringBuffer();
		xml.append("<xml>\n");
		xml.append("<return_code><![CDATA[SUCCESS]]></return_code>\n");
		xml.append("<return_msg><![CDATA[OK]]></return_msg>\n");
		xml.append("<appid><![CDATA[wx2421b1c4370ec43b]]></appid>\n");
		xml.append("<mch_id><![CDATA[10000100]]></mch_id>\n");
		xml.append("<nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>\n");
		xml.append("<sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>\n");
		xml.append("<result_code><![CDATA[SUCCESS]]></result_code>\n");
		xml.append("<prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>\n");
		xml.append("<trade_type><![CDATA[JSAPI]]></trade_type>\n");
		xml.append("</xml>");
		getMsgEntity(xml.toString());
	}
}
