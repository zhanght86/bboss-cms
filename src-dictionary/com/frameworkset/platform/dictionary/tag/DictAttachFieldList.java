/**
 * 字典附加字段列表 
 */
package com.frameworkset.platform.dictionary.tag;

import org.apache.log4j.Logger;

import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: DictAttachFieldList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-8 11:14:46
 * @author ge.tao
 * @version 1.0
 */
public class DictAttachFieldList extends DataInfoImpl implements java.io.Serializable {
	private static final Logger logger = Logger.getLogger(DictAttachFieldList.class.getName());
	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
			
		try {
			ListInfo listInfo = new ListInfo();
			String dictId = request.getParameter("dicttypeId");

			if (dictId == null || dictId.equals("")) {
				dictId = (String)request.getAttribute("did");
			}
			
			if (dictId == null || dictId.equals("")) {
				return listInfo;
			}
			DictManager dictManager = new DictManagerImpl();			
            /**
             * dictId 是字典类型ID 
             */
			listInfo = dictManager.getDictdataAttachFieldList(dictId,(int)offset,maxPagesize);
			return listInfo;
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}
