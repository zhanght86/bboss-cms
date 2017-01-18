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

import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.wx.common.entity.SmUserWx;
import org.frameworkset.wx.common.entity.SmUserWxCondition;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.GetCUDResult;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;

/**
 * <p>
 * Title: SmUserWxServiceImpl
 * </p>
 * <p>
 * Description: wx管理业务处理类
 * </p>
 * <p>
 * wowo
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2017-01-18 23:42:32 @author suwei @version v1.0
 */
public class SmUserWxServiceImpl implements SmUserWxService {

	private static Logger log = Logger.getLogger(org.frameworkset.wx.common.service.SmUserWxServiceImpl.class);

	private ConfigSQLExecutor executor;

	public void addSmUserWx(SmUserWx smUserWx) throws SmUserWxException {
		// 业务组件
		try {
			executor.insertBean("addSmUserWx", smUserWx);
		} catch (Throwable e) {
			throw new SmUserWxException("add SmUserWx failed:", e);
		}

	}

	public void deleteSmUserWx(long wxid) throws SmUserWxException {
		try {
			executor.delete("deleteByKey", wxid);
		} catch (Throwable e) {
			throw new SmUserWxException("delete SmUserWx failed::wxid=" + wxid, e);
		}

	}

	public void deleteBatchSmUserWx(long... wxids) throws SmUserWxException {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteByLongKeys("deleteByKey", wxids);
			tm.commit();
		} catch (Throwable e) {

			throw new SmUserWxException("batch delete SmUserWx failed::wxids=" + wxids, e);
		} finally {
			tm.release();
		}

	}

	public void updateSmUserWx(SmUserWx smUserWx) throws SmUserWxException {
		try {
			executor.updateBean("updateSmUserWx", smUserWx);
		} catch (Throwable e) {
			throw new SmUserWxException("update SmUserWx failed::", e);
		}

	}

	public SmUserWx getSmUserWx(long wxid) throws SmUserWxException {
		try {
			SmUserWx bean = executor.queryObject(SmUserWx.class, "selectById", wxid);
			return bean;
		} catch (Throwable e) {
			throw new SmUserWxException("get SmUserWx failed::wxid=" + wxid, e);
		}

	}

	public ListInfo queryListInfoSmUserWxs(SmUserWxCondition conditions, long offset, int pagesize)
			throws SmUserWxException {
		ListInfo datas = null;
		try {
			datas = executor.queryListInfoBean(SmUserWx.class, "queryListSmUserWx", offset, pagesize, conditions);
		} catch (Exception e) {
			throw new SmUserWxException("pagine query SmUserWx failed:", e);
		}
		return datas;

	}

	public List<SmUserWx> queryListSmUserWxs(SmUserWxCondition conditions) throws SmUserWxException {
		try {
			List<SmUserWx> beans = executor.queryListBean(SmUserWx.class, "queryListSmUserWx", conditions);
			return beans;
		} catch (Exception e) {
			throw new SmUserWxException("query SmUserWx failed:", e);
		}

	}

	@Override
	public Long insertUserWeixin(SmUserWx smUserWx) throws SmUserWxException {
		// 业务组件
		Long key = null;
		try {
			GetCUDResult ret = new GetCUDResult();
			executor.insertBean("addSmUserWx", smUserWx, ret);
			key = (Long) ret.getKeys();
		} catch (Throwable e) {
			throw new SmUserWxException("add UserWeixin failed:", e);
		}
		return key;
	}
}