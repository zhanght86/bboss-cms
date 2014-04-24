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

import org.frameworkset.web.token.TokenException;
import org.frameworkset.web.token.ValidateApplication;

/**
 * <p>Title: SYSValidationApplication.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月24日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SYSValidationApplication implements ValidateApplication{

	@Override
	public boolean checkApp(String appid, String secret) throws TokenException {
		
		return false;
	}

}
