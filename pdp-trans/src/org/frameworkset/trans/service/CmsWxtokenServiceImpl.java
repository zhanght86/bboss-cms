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
 * <p>
 * Title: CmsWxtokenServiceImpl
 * </p>
 * <p>
 * Description: token管理业务处理类
 * </p>
 * <p>
 * wowo
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2016-11-12 15:44:26 @author suwei @version v1.0
 */
public class CmsWxtokenServiceImpl implements CmsWxtokenService {

	private static Logger log = Logger.getLogger(org.frameworkset.trans.service.CmsWxtokenServiceImpl.class);

	private ConfigSQLExecutor executor;

	public void addCmsWxtoken(CmsWxtoken cmsWxtoken) throws CmsWxtokenException {
		// 业务组件
		try {
			executor.insertBean("addCmsWxtoken", cmsWxtoken);
		} catch (Throwable e) {
			throw new CmsWxtokenException("add CmsWxtoken failed:", e);
		}

	}

	public void deleteCmsWxtoken(long id) throws CmsWxtokenException {
		try {
			executor.delete("deleteByKey", id);
		} catch (Throwable e) {
			throw new CmsWxtokenException("delete CmsWxtoken failed::id=" + id, e);
		}

	}

	public void deleteBatchCmsWxtoken(long... ids) throws CmsWxtokenException {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteByLongKeys("deleteByKey", ids);
			tm.commit();
		} catch (Throwable e) {

			throw new CmsWxtokenException("batch delete CmsWxtoken failed::ids=" + ids, e);
		} finally {
			tm.release();
		}

	}

	public void updateCmsWxtoken(CmsWxtoken cmsWxtoken) throws CmsWxtokenException {
		try {
			executor.updateBean("updateCmsWxtoken", cmsWxtoken);
		} catch (Throwable e) {
			throw new CmsWxtokenException("update CmsWxtoken failed::", e);
		}

	}
	public void updateCmsWxtokenState(CmsWxtoken cmsWxtoken) throws CmsWxtokenException {
		try {
			executor.updateBean("updateCmsWxtokenState", cmsWxtoken);
		} catch (Throwable e) {
			throw new CmsWxtokenException("update CmsWxtoken failed::", e);
		}

	}
	public CmsWxtoken getCmsWxtoken(long id) throws CmsWxtokenException {
		try {
			CmsWxtoken bean = executor.queryObject(CmsWxtoken.class, "selectById", id);
			return bean;
		} catch (Throwable e) {
			throw new CmsWxtokenException("get CmsWxtoken failed::id=" + id, e);
		}

	}

	public ListInfo queryListInfoCmsWxtokens(long offset, int pagesize) throws CmsWxtokenException {
		ListInfo datas = null;
		try {
			datas = executor.queryListInfoBean(CmsWxtoken.class, "queryListCmsWxtoken", offset, pagesize, null);
		} catch (Exception e) {
			throw new CmsWxtokenException("pagine query CmsWxtoken failed:", e);
		}
		return datas;

	}

	public List<CmsWxtoken> queryListCmsWxtokens() throws CmsWxtokenException {
		try {
			List<CmsWxtoken> beans = executor.queryListBean(CmsWxtoken.class, "queryListCmsWxtoken", null);
			return beans;
		} catch (Exception e) {
			throw new CmsWxtokenException("query CmsWxtoken failed:", e);
		}

	}

	@Override
	public CmsWxtoken getCmsWxtoken() throws CmsWxtokenException {
		try {
			List<CmsWxtoken> beans = executor.queryListBean(CmsWxtoken.class, "queryListCmsWxtokenBySysdateAndState", null);
			return beans==null || beans.size()<1?null:beans.get(0);
		} catch (Exception e) {
			throw new CmsWxtokenException("query CmsWxtoken failed:", e);
		}
	}
}