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

import org.frameworkset.trans.entity.*;
import com.frameworkset.util.ListInfo;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import org.apache.log4j.Logger;
import java.util.List;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * <p>Title: ApiXmlServiceImpl</p> <p>Description: 传输数据管理业务处理类 </p> <p>wowo</p>
 * <p>Copyright (c) 2007</p> @Date 2016-10-26 23:57:01 @author suwei @version
 * v1.0
 */
public class ApiXmlServiceImpl implements ApiXmlService {

	private static Logger log = Logger.getLogger(org.frameworkset.trans.service.ApiXmlServiceImpl.class);

	private ConfigSQLExecutor executor;
	public void addApiXml(ApiXml apiXml) throws ApiXmlException {
		// 业务组件
		try {
			executor.insertBean("addApiXml", apiXml);
		} catch (Throwable e) {
			throw new ApiXmlException("add ApiXml failed:", e);
		}

	}
	public void deleteApiXml(int id) throws ApiXmlException {
		try {
			executor.delete("deleteByKey", id);
		} catch (Throwable e) {
			throw new ApiXmlException("delete ApiXml failed::id=" + id, e);
		}

	}
	public void deleteBatchApiXml(int... ids) throws ApiXmlException {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteByKeys("deleteByKey", ids);
			tm.commit();
		} catch (Throwable e) {

			throw new ApiXmlException("batch delete ApiXml failed::ids=" + ids, e);
		} finally {
			tm.release();
		}

	}
	public void updateApiXml(ApiXml apiXml) throws ApiXmlException {
		try {
			executor.updateBean("updateApiXml", apiXml);
		} catch (Throwable e) {
			throw new ApiXmlException("update ApiXml failed::", e);
		}

	}
	public ApiXml getApiXml(int id) throws ApiXmlException {
		try {
			ApiXml bean = executor.queryObject(ApiXml.class, "selectById", id);
			return bean;
		} catch (Throwable e) {
			throw new ApiXmlException("get ApiXml failed::id=" + id, e);
		}

	}
	public ListInfo queryListInfoApiXmls(long offset, int pagesize) throws ApiXmlException {
		ListInfo datas = null;
		try {
			datas = executor.queryListInfoBean(ApiXml.class, "queryListApiXml", offset, pagesize, null);
		} catch (Exception e) {
			throw new ApiXmlException("pagine query ApiXml failed:", e);
		}
		return datas;

	}
	public List<ApiXml> queryListApiXmls() throws ApiXmlException {
		try {
			List<ApiXml> beans = executor.queryListBean(ApiXml.class, "queryListApiXml", null);
			return beans;
		} catch (Exception e) {
			throw new ApiXmlException("query ApiXml failed:", e);
		}

	}
}