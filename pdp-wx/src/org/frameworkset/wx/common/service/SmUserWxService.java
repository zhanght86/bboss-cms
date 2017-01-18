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

import org.frameworkset.wx.common.entity.SmUserWx;
import org.frameworkset.wx.common.entity.SmUserWxCondition;

import com.frameworkset.util.ListInfo;

/**
 * <p>
 * Title: SmUserWxService
 * </p>
 * <p>
 * Description: wx管理服务接口
 * </p>
 * <p>
 * wowo
 * </p>
 * <p>
 * Copyright (c) 2015
 * </p>
 * @Date 2017-01-18 23:42:32 @author suwei @version v1.0
 */
public interface SmUserWxService {
	public void addSmUserWx(SmUserWx smUserWx) throws SmUserWxException;

	public void deleteSmUserWx(long wxid) throws SmUserWxException;

	public void deleteBatchSmUserWx(long... wxids) throws SmUserWxException;

	public void updateSmUserWx(SmUserWx smUserWx) throws SmUserWxException;

	public SmUserWx getSmUserWx(long wxid) throws SmUserWxException;

	public ListInfo queryListInfoSmUserWxs(SmUserWxCondition conditions, long offset, int pagesize)
			throws SmUserWxException;

	public List<SmUserWx> queryListSmUserWxs(SmUserWxCondition conditions) throws SmUserWxException;

	public Long insertUserWeixin(SmUserWx smUserWx) throws SmUserWxException;
}