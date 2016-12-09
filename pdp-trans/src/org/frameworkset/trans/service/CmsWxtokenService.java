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
import java.util.List;

/**
 * <p>
 * Title: CmsWxtokenService
 * </p>
 * <p>
 * Description: token管理服务接口
 * </p>
 * <p>
 * wowo
 * </p>
 * <p>
 * Copyright (c) 2015
 * </p>
 * 
 * @Date 2016-11-12 15:44:26 @author suwei @version v1.0
 */
public interface CmsWxtokenService {
	public void addCmsWxtoken(CmsWxtoken cmsWxtoken) throws CmsWxtokenException;

	public void deleteCmsWxtoken(long id) throws CmsWxtokenException;

	public void deleteBatchCmsWxtoken(long... ids) throws CmsWxtokenException;

	public void updateCmsWxtoken(CmsWxtoken cmsWxtoken) throws CmsWxtokenException;

	public CmsWxtoken getCmsWxtoken(long id) throws CmsWxtokenException;

	public ListInfo queryListInfoCmsWxtokens(long offset, int pagesize) throws CmsWxtokenException;

	public List<CmsWxtoken> queryListCmsWxtokens() throws CmsWxtokenException;

	public CmsWxtoken getCmsWxtoken() throws CmsWxtokenException;

	public void updateCmsWxtokenState(CmsWxtoken cmsWxtoken) throws CmsWxtokenException;

}