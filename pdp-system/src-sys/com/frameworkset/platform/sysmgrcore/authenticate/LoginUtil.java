/**
 *  Copyright 2008 biaoping.yin
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
package com.frameworkset.platform.sysmgrcore.authenticate;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.util.StringUtil;


/**
 * <p>Title: LoginUtil.java</p>
 *
 * <p>Description: 判断客户端IP是否是特权用户</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-8-26
 * @author biaoping.yin
 * @version 1.0
 */
public class LoginUtil {
	private static Logger log = LoggerFactory.getLogger(LoginUtil.class);
	private static String specialusers = "specialusers" ;
	public static String isSpesialUser(String ip)
	{
		try
		{
			Data data = DataManagerFactory.getDataManager().getData(specialusers);
			if(data == null || data.size() == 0)
				return null;
			String user = data.getItemNameByValue(ip);
			if(user != null && !user.equals(""))
				return user;
			return null;
		}
		catch(Exception e)
		{
			log.debug("获取字典数据异常:"+specialusers,e);
			return null;
		}
	}
	
	public static String isSpesialUser(HttpServletRequest request)
	{
		String machineIP = StringUtil.getClientIP(request);
		return isSpesialUser( machineIP);
	}

}
