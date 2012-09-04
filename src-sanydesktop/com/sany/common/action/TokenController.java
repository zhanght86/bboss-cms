package com.sany.common.action;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.token.MemTokenManager;

/**
 * <p>
 * Title: TokenController.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-8-27 上午11:43:42
 * @author biaoping.yin
 * @version 1.0.0
 */
public class TokenController {
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 */
	public @ResponseBody String getToken(HttpServletRequest request)
	{
		MemTokenManager memTokenManager = org.frameworkset.web.token.MemTokenManagerFactory.getMemTokenManagerNoexception();
		if(memTokenManager != null)//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  memTokenManager.buildDToken(request);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获取令牌请求
	 * http://localhost:8081/SanyPDP/token/getParameterToken.freepage
	 * @param request
	 * @return
	 */
	public @ResponseBody String getParameterToken(HttpServletRequest request)
	{
		MemTokenManager memTokenManager = org.frameworkset.web.token.MemTokenManagerFactory.getMemTokenManagerNoexception();
		if(memTokenManager != null)//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  memTokenManager.buildParameterDToken(request);
		}
		else
		{
			return null;
		}
	}
}
