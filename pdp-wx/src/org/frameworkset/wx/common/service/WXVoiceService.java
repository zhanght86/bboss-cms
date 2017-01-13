/**
 * 
 */
package org.frameworkset.wx.common.service;

/**
 * 微信语音接口
 * 
 * @author suwei
 * @date 2017年1月5日
 *
 */
public interface WXVoiceService {
	/**
	 * 下载语音接口
	 * 
	 * @param accessToken
	 * @param mediaId
	 */
	public void downloadVoice(String accessToken, String mediaId);

}
