/*
 * Created on 2006-6-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sysmgrcore.entity.UserJobs;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 * 用户管理 机构下的用户排序
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserListSn extends DataInfoImpl implements Serializable{
	private Logger log = Logger.getLogger(UserListSn.class);
    private ConfigSQLExecutor executor_ = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/web/tag/userListSn.xml");
    //sql盲注修复：将sql硬编码改成sql模板形式 houtt2 2014.04.29
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {

		String userName = request.getParameter("userName");
		String userRealname = request.getParameter("userRealname");		
		String flag = request.getParameter("flag");
		String orgId = request.getParameter("orgId");
		//是否税管员
		String taxmanager = request.getParameter("taxmanager");
		/*是否递归*/
		String recursion = request.getParameter("intervalType");		
		recursion = recursion==null?"":recursion;
		ListInfo listInfo = new ListInfo();		
		 Map<String, String> paramters = new HashMap<String, String>();
         paramters.put("userName", "%"+userName+"%");
         paramters.put("userRealname", "%"+userRealname+"%");
         paramters.put("orgId", orgId);
         paramters.put("taxmanager", taxmanager);
		try {
			if (flag == null && userName == null && userRealname == null
					&& recursion == null) {//一般的查询
				listInfo = executor_.queryListInfoBean(UserJobs.class, "getUserList_normal", offset, maxPagesize, paramters);
				log.warn("用户管理 机构下的用户排序 不递归---------------------------"+ executor_.getSql("getUserList_normal"));
			} else if (flag != null && flag.equals("1")) //
			{
				listInfo = executor_.queryListInfoBean(UserJobs.class, "getUserList", offset, maxPagesize, paramters);
				log.warn("用户管理 机构下的用户排序 不递归---------------------------"+ executor_.getSql("getUserList"));
			} else if ((flag != null && flag.equals("2"))
					|| (flag != null && flag.equals("3"))) {
				
				listInfo = executor_.queryListInfoBean(UserJobs.class, "getUserList_normal", offset, maxPagesize, paramters);
				log.warn("用户管理 机构下的用户排序 不递归---------------------------"+ executor_.getSql("getUserList_normal"));

			}else {
				listInfo = executor_.queryListInfoBean(UserJobs.class, "getUserList", offset, maxPagesize, paramters);
				log.warn("用户管理 机构下的用户排序 不递归---------------------------"+ executor_.getSql("getUserList"));
			}
			/*递归查询*/
			if (recursion != null && recursion != "" && recursion.equals("1")) {
				listInfo = getUserList_recursion(paramters, offset, maxPagesize);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 递归查询用户列表  
	 * houtt2  2014.04.30
	 */
	protected  ListInfo getUserList_recursion(Map<String, String> paramters, long offset, int maxItem)
			throws ManagerException {
		TransactionManager tm = new TransactionManager();
		try {
			 final UserManager userManager = SecurityDatabase.getUserManager();
			tm.begin();
			
			ListInfo listInfo = null;
			
				listInfo = new ListInfo(); 
				List data = executor_.queryListBeanByRowHandler(new RowHandler<UserJobs>(){
					@Override
					public void handleRow(UserJobs uj, Record record)
							throws Exception {
						int userid = record.getInt("user_id");
						String orgjob = FunctionDB.getUserorgjobinfos(userid);
						if(orgjob.endsWith("、"))
						{
							orgjob = orgjob.substring(0, orgjob.length() - 1);
						}	
						uj.setUserId(new Integer(userid));
						uj.setUserName(record.getString( "USER_NAME"));
						uj.setUserRealname(record.getString( "USER_REALNAME"));
						uj.setUserMobiletel1(record.getString( "USER_MOBILETEL1"));
						uj.setUserType(record.getString("USER_TYPE"));
						uj.setUserEmail(record.getString( "USER_EMAIL"));
						uj.setUserSex(record.getString( "USER_SEX"));
						uj.setUser_isvalid(record.getString( "USER_ISVALID"));
						uj.setUSER_IDCARD(record.getString("USER_IDCARD"));
						uj.setWorkNumber(record.getString("USER_WORKNUMBER"));
						uj.setUser_regdate(record.getString("USER_REGDATE"));
						uj.setDredge_time(record.getString( "DREDGE_TIME"));
						uj.setIstaxmanager(new Integer(record.getInt( "ISTAXMANAGER")));
						uj.setPasswordUpdatetime(record.getTimestamp("password_updatetime"));
						
						uj.setPasswordDualedTime(record.getInt("Password_DualTime"));
						uj.setPasswordExpiredTime((Timestamp)userManager.getPasswordExpiredTime(uj.getPasswordUpdatetime(),uj.getPasswordDualedTime()));
						uj.setOrgName(orgjob);
						uj.setJobName(orgjob);
						uj.setOrg_Name(orgjob);
						uj.setOrgId(record.getString( "org_id"));
					}
				}, UserJobs.class, "getUserList_recursion",paramters);
				listInfo.setDatas(data);
			tm.commit();
			log.warn("用户管理 机构下的用户排序 递归---------------------------"+ executor_.getSql("getUserList_recursion"));
			return listInfo;
		} catch (Throwable e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new ManagerException(e.getMessage());

		}

	}
	
}
