/*
 *  Copyright 2008 bbossgroups
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sany.common.action;

import javax.jws.WebService;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.token.MemTokenManager;
import org.frameworkset.web.token.TokenResult;
import org.frameworkset.web.token.TokenStore;

/**
 * <p>Title: CheckTokenContoller.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月16日
 * @author biaoping.yin
 * @version 3.8.0
 */
@WebService(name="CheckTokenService",targetNamespace="com.sany.common.action.CheckTokenService")
public class CheckTokenContoller implements CheckTokenService{
	
	public @ResponseBody(datatype="json") TokenResult checkToken(String appid,String secret,String token)
	{
		MemTokenManager memTokenManager = org.frameworkset.web.token.MemTokenManagerFactory.getMemTokenManagerNoexception();
		if(memTokenManager != null)//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  memTokenManager.checkToken(appid,secret,token);
		}
		else
		{
			return null;
		}
	}
	public @ResponseBody Integer checkTempToken(String token)
	{
		MemTokenManager memTokenManager = org.frameworkset.web.token.MemTokenManagerFactory.getMemTokenManagerNoexception();
		if(memTokenManager != null)//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  memTokenManager.checkTempToken(token);
		}
		else
		{
			return TokenStore.temptoken_request_validateresult_notenabletoken;
		}
	}


}
