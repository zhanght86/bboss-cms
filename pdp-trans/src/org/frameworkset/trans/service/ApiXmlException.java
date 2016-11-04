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

package org.frameworkset.trans.service;

/**
 * <p>Title: ApiXmlException</p> <p>Description: 传输数据管理异常处理类 </p> <p>wowo</p>
 * <p>Copyright (c) 2007</p> @Date 2016-10-26 23:57:01 @author suwei @version
 * v1.0
 */
public class ApiXmlException extends RuntimeException {

	public ApiXmlException() {
		super();
	}
	public ApiXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiXmlException(String message) {
		super(message);
	}

	public ApiXmlException(Throwable cause) {
		super(cause);
	}

}