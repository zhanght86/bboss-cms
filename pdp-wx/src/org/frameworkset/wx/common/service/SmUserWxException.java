/**
 *  Copyright 2008-2010 biaoping.yin
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

package org.frameworkset.wx.common.service;

/**
 * <p>Title: SmUserWxException</p> <p>Description: wx管理异常处理类 </p> <p>wowo</p>
 * <p>Copyright (c) 2007</p> @Date 2017-01-18 23:42:32 @author suwei @version
 * v1.0
 */
public class SmUserWxException extends RuntimeException {

	public SmUserWxException() {
		super();
	}
	public SmUserWxException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmUserWxException(String message) {
		super(message);
	}

	public SmUserWxException(Throwable cause) {
		super(cause);
	}

}