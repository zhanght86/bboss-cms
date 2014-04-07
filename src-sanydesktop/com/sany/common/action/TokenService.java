package com.sany.common.action;

import javax.jws.WebService;

@WebService(name="TokenService",targetNamespace="com.sany.common.action.TokenService")
public interface TokenService {
	public String getAuthTempToken(String appid,String secret,String account) throws Exception;
	public String genDualToken(String appid,String secret,String account) throws Exception;

}
