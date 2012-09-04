package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 根据用户ID得到用户可管理的机构列表
 * @author gao.tang 2007.12.10
 *
 */
public class UserManagerOrgList extends DataInfoImpl implements Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		String userId = request.getParameter("userId");
		String orgName = request.getParameter("orgName");
		String orgnumber = request.getParameter("orgnumber");
		OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
		try {
			listInfo = orgAdministrator.getAllManagerOrgsOfUserByID(userId, orgName, orgnumber, offset, maxPagesize);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
