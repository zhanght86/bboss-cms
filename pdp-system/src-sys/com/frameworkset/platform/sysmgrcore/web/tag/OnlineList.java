package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.LineUser;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class OnlineList extends DataInfoImpl implements Serializable{

	private Logger logger = Logger.getLogger(OnlineList.class.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		String userName = request.getParameter("userName");
		//判断是否是部门管理员或admin
		boolean state = (super.accessControl.isAdmin() || ((AccessControl)accessControl).isOrgManager(accessControl.getUserAccount()));
		if(!state){
			userName = accessControl.getUserAccount(); 
		}
		try {
			List temp = new ArrayList();
			//如果查询条件不为空
			if(userName != null && !"".equals(userName)){
				LineUser onLineUser = ((AccessControl)accessControl).getLineUser(userName);
				if(onLineUser != null)
				{
					temp.add(onLineUser);
				}
			}else{
				Collection users = ((AccessControl)accessControl).getLoginUsers();
			
				temp = new ArrayList(users);
			}
			//listInfo.setTotalSize(temp.size());
			
			listInfo = pagerList(temp,(int)offset,maxPagesize);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	 *      boolean)
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) {
		
		return null;
	}
}