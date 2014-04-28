package com.sany.application.util;

import org.frameworkset.util.ParamsHandler;
import org.frameworkset.util.ParamsHandler.Params;

import com.caucho.hessian.client.HessianProxyFactory;
import com.sany.common.action.TokenService;

public class AppHelper {
	
	private static ParamsHandler paramHandler = ParamsHandler.getParamsHandler("cms.siteparamshandler");
	
	public static String getTicket(String account, String workNum) throws Exception{
		
		Params params = paramHandler.getParams("Token", "Token");
		//String paramHandle,String paramid, String paramName,String paramType
		String token_server = params.getAttributeString("token_server");
		
		String token_app_id = params.getAttributeString("token_app_id");
		
		String token_app_secret = params.getAttributeString("token_app_secret");
		
		HessianProxyFactory factory = new HessianProxyFactory();
		
		TokenService tokenService = (TokenService) factory.create(TokenService.class, token_server);
		
		String ticket = tokenService.genTicket(account, workNum, token_app_id, token_app_secret);
	
		return ticket;
		
	}
	
	public static String getToken(String ticket) throws Exception  {
		
		Params params = paramHandler.getParams("Token", "Token");
		//String paramHandle,String paramid, String paramName,String paramType
		String token_server = params.getAttributeString("token_server");
		
		String token_app_id = params.getAttributeString("token_app_id");
		
		String token_app_secret = params.getAttributeString("token_app_secret");
		
		HessianProxyFactory factory = new HessianProxyFactory();
		
		TokenService tokenService = (TokenService) factory.create(TokenService.class, token_server);
		
		String token = tokenService.genAuthTempToken(token_app_id, token_app_secret, ticket);
	
		return token;
	}
	
	public static String[] getAppInfo(){
		
		Params params = paramHandler.getParams("Token", "Token");
		//String paramHandle,String paramid, String paramName,String paramType
		
		String token_app_id = params.getAttributeString("token_app_id");
		
		String token_app_secret = params.getAttributeString("token_app_secret");
		
		return new String[]{token_app_id,token_app_secret};		
	}

}
