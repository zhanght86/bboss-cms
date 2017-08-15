package com.frameworkset.platform.dictionary.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;

/**
 * 可见: 管理员,全部可以看到
 *      其他人: 机构管理员, 普通用户
 *         机构管理员: 所管理机构的全部
 *         普通用户: 所在机构的全部
 * 可见机构编码关系维护, 不会维护 通用字典
 * <p>Title: UserDictList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-24 8:39:24
 * @author ge.tao
 * @version 1.0
 */

public class UserDictListSelected extends DataInfoImpl implements java.io.Serializable {

	private static final Logger logger = LoggerFactory.getLogger(UserDictList.class
			.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
			
		try {
			ListInfo listInfo = new ListInfo();
			String dictId = request.getParameter("did");

			if (dictId == null || dictId.equals("")) {
				dictId = (String)request.getAttribute("did");
			}
			
			if (dictId == null || dictId.equals("")) {
				dictId = (String)request.getParameter("selected");
			}
			
			if (dictId == null || dictId.equals("")) {
				return listInfo;
			}
			DictManager dictManager = new DictManagerImpl();			
            /**
             * dictId 是123:123:aa 字典类型ID:数据项ID:数据项名称 
             */
			String showdata = (String)request.getParameter("showdata");
			if(showdata == null || "".equals(showdata)){
				showdata = "";
			}
			//根据机构ID判断已设置项和未设置项
			String orgId = request.getParameter("orgId");
			//设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项
			String identifier = request.getParameter("identifier");
			//机构ID			
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(this.session);
			String curOrg = accesscontroler.getChargeOrgId();
			if(accesscontroler.isAdmin()){//是管理员,全部的
				return dictManager.getDictdataList(dictId,orgId,showdata,identifier,(int)offset,maxPagesize);				
			}else{//其他用户
				String userId = accesscontroler.getUserID();
				if(curOrg.equals(orgId)){//如果用户选择的是自己所属机构则显示用户所属机构的采集数据项
					return dictManager.getCurOrgGatherDictDataList(orgId,dictId,identifier,showdata,(int)offset,maxPagesize);
				}else{
					return dictManager.getUserDictdataList(dictId,orgId,showdata,identifier,(int)offset,maxPagesize,userId);
				}
				
			}
			
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
		
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
		
	}

}
