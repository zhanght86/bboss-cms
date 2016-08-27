package com.sany.hrm.common.service;

import com.frameworkset.platform.security.AccessControl;

/**
 * 登录
 * 
 * @author gw_yaoht
 * @version 2012-8-11
 **/
public class DefaultLoginService implements LoginService {
	
	@Override
	public String getWarrantId() {
		AccessControl control = AccessControl.getAccessControl();

//		if (control.getSession() == null) {
//			return StringUtils.EMPTY;
//		}
//
//		String warrantId = (String) control.getSession().getAttribute("warrantId");
//
//		if (StringUtils.isNotBlank(warrantId)) {
//			return warrantId;
//		}
//
//		try {
//			DESCipher desCipher = new DESCipher("HRM");
//
//			String userCode = desCipher.encrypt(control.getUserAccount());
//
//			String userId = desCipher.encrypt(control.getUserID());
//
//			String json = (String) this.webServiceSupport.getCallForObject("getWarrantId", userCode, userId);
//
//			if (StringUtils.isNotBlank(json)) {
//				MessageDto<String> messageDto = JsonBinder.buildNormalBinder().getMapper()
//						.readValue(json, new TypeReference<MessageDto<String>>() {
//						});
//
//				warrantId = messageDto.getData();
//
//				control.getSession().setAttribute("warrantId", warrantId);
//			}
//
//			return warrantId;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return StringUtils.EMPTY;
		return control.getUserAccount();
	}

	@Override
	public String getWarrantId(String userCode) {
//		if (StringUtils.isBlank(userCode)) {
//			return StringUtils.EMPTY;
//		}
//
//		try {
//			DESCipher desCipher = new DESCipher("HRM");
//
//			userCode = desCipher.encrypt(userCode);
//
//			String json = (String) this.webServiceSupport.getCallForObject("getWarrantId", userCode, userCode);
//
//			if (StringUtils.isNotBlank(json)) {
//				MessageDto<String> messageDto = JsonBinder.buildNormalBinder().getMapper()
//						.readValue(json, new TypeReference<MessageDto<String>>() {
//						});
//
//				String warrantId = messageDto.getData();
//
//				return warrantId;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return StringUtils.EMPTY;
		AccessControl control = AccessControl.getAccessControl();
		return control.getUserAccount();
	}

//	/**
//	 * 设置
//	 * 
//	 * @param webServiceSupport
//	 *            WebService支持
//	 */
//	public void setWebServiceSupport(WebServiceSupport webServiceSupport) {
//		this.webServiceSupport = webServiceSupport;
//	}
}