package org.frameworkset.wx.common.service;

import org.frameworkset.wx.common.entity.OAuthSnsAPIBase;
import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxJsapiTicket;
import org.frameworkset.wx.common.entity.WxOrderMessage;
import org.frameworkset.wx.common.enums.EnumWeiXinAccountFlag;
import org.frameworkset.wx.common.enums.EnumWeiXinOAuthScope;

public interface WXAPIService {
	/**
	 * Appid（数据库设置好）、 MchId（数据库设置好）、 sign（自动计算）、 NonceStr（自动计算） 以上四个参数自动赋值，不需要传。
	 * 
	 * @param orderMsg
	 *            统一下单的对象
	 * @return
	 * @throws Exception
	 */
	public String unifiedorder(WxOrderMessage orderMsg) throws Exception;

	/**
	 * 获取授权URL 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）， snsapi_userinfo
	 * （弹出授权页面，可通过openid拿到昵称、性别、所在地。 并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
	 * 
	 * @param enumWeiXinOAuthScope
	 * @return
	 * @throws Exception
	 */
	public String getUserOAuthURL(EnumWeiXinOAuthScope enumWeiXinOAuthScope) throws Exception;

	/**
	 * 不弹窗口，获得用户授权信息 appid 为null时，从数据库中获取 secret 为null时，从数据库中获取 appid
	 * 为null时，从数据库中获取 appid 为null时，从数据库中获取
	 * 
	 * @param appId
	 * @param secret
	 * @param code
	 * @param grant_type
	 *            取自WXConstans.AUTHOIZATION_CODE
	 * @return
	 * @throws Exception
	 */
	public OAuthSnsAPIBase getWeiXinSnsAPIBase(String appId, String secret, String code, String grant_type)
			throws Exception;

	/**
	 * 不弹窗口，获得用户授权信息
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public OAuthSnsAPIBase getWeiXinSnsAPIBase(String code) throws Exception;

	/**
	 * 获取企业号和公众号的accsesstoken不一样。
	 * 
	 * @param appid
	 * @param appsecret
	 * @param enumWeiXinAccountFlag
	 * @return
	 * @throws Exception
	 */
	public WxAccessToken getWxAccessToken(String appid, String appsecret, EnumWeiXinAccountFlag enumWeiXinAccountFlag)
			throws Exception;

	/**
	 * 根据access token 获取ticket
	 * 
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public WxJsapiTicket getJsapiTicket(String accessToken) throws Exception;

	public String getPaySign(long timestamp, String nodeStr) throws Exception;
}
