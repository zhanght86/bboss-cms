package com.sany.common.action;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "TokenService", targetNamespace = "com.sany.common.action.TokenService")
public interface TokenService {
	public @WebResult(name = "authTempToken", partName = "partAuthTempToken")
	String genAuthTempToken(
			@WebParam(name = "appid", partName = "partAppid") String appid,
			@WebParam(name = "secret", partName = "partSecret") String secret,
			@WebParam(name = "account", partName = "partAccount") String account)
			throws Exception;

	public @WebResult(name = "dualToken", partName = "partDualToken")
	String genDualToken(
			@WebParam(name = "appid", partName = "partAppid") String appid,
			@WebParam(name = "secret", partName = "partSecret") String secret,
			@WebParam(name = "account", partName = "partAccount") String account)
			throws Exception;

	public @WebResult(name = "publicKey", partName = "partPublicKey")
	String getPublicKey(
			@WebParam(name = "appid", partName = "partAppid") String appid,
			@WebParam(name = "secret", partName = "partSecret") String secret)
			throws Exception;
	public @WebResult(name = "tempToken", partName = "partTempToken") String genTempToken() throws Exception;
}
