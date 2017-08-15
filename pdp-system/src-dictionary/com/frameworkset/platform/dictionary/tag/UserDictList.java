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

public class UserDictList extends DataInfoImpl implements java.io.Serializable {

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
			String realitydata = (String)request.getParameter("realitydata");
			if(realitydata == null || "".equals(realitydata)){
				realitydata = "";
			}
			String occurOrg = (String)request.getParameter("occurOrg");
			if(occurOrg == null || "".equals(occurOrg)){
				occurOrg = "";
			}
			String isaVailability = (String)request.getParameter("isaVailability");
			if(isaVailability == null || "".equals(isaVailability)){
				isaVailability = "-1";
			}
			//机构ID			
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(this.session);
			if(accesscontroler.isAdmin()){//是管理员,全部的
				return dictManager.getDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize);				
			}else{//其他用户
				String userId = accesscontroler.getUserID();
//				//判断用户是否部门管理员
//				OrgAdministrator orgAdminImpl = new OrgAdministratorImpl();
//				if(orgAdminImpl.isOrgAdmin(userId,"")){//是部门管理员 能看到 所管理的机构 可见编码
//					return dictManager.getOrgManagerDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,userId);
//				}else{//普通用户 能看到 所属机构 可见编码 
//					return dictManager.getNormalUserDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,userId);
//				}
				//不判断用户是否部门管理员,只判断用户是否 admin
				//用户 能看到 所属机构 可见编码 用户==机构管理员
				return dictManager.getNormalUserDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,userId);
				
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
