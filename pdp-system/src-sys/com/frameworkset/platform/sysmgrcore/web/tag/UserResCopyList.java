package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UserResCopyList extends DataInfoImpl implements Serializable{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {
	    	
	        ListInfo listInfo = new ListInfo();
	        String orgId = request.getParameter("orgId");
	        String userId = (String)request.getAttribute("userId");
	       	      
			DBUtil dbUtil = new DBUtil();
	
			try {
//				String sql = "select a.USER_ID,a.USER_NAME,a.USER_REALNAME from " +
//						"TD_SM_USER a,TD_SM_USERJOBORG b where a.USER_ID = b.USER_ID and b.ORG_ID='"+ orgId +"'";
				  String sql="select a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"+
		           "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"+
		           "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"+
		           "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,a.REMARK2,a.REMARK3,a.REMARK4,"+
		           "a.REMARK5,max(b.same_job_user_sn) aa,max(b.job_sn) bb "+
		            " from td_sm_user a, td_sm_userjoborg b "+
		            "where a.user_id = b.user_id and a.user_id <>" + userId + " and  b.org_id='"+orgId+"' "+
		            "group by a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"+
		            "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"+
		            "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"+
		            "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,"+
		            "a.REMARK2,a.REMARK3,a.REMARK4,a.REMARK5 "+
		            " order by bb asc,aa asc";  
	
				dbUtil.executeSelect(sql,(int)offset,maxPagesize);
//				System.out.println(sql);
			
				User user = null;
				List users = new ArrayList();
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					user = new User();
					user.setUserId(Integer.valueOf(dbUtil.getString(i,"USER_ID")));
					user.setUserName(dbUtil.getString(i,"USER_NAME"));
					user.setUserRealname(dbUtil.getString(i,"USER_REALNAME"));
					
					/*
					 * 按bug836要求，用户如果是系统管理员，别人不能复制权限给这个用户，因此在列表中屏蔽系统管理员用户
					 * 危达，200711140856
					 * */
//					try
//					{
//						String adminSql ="select count(*) from (select a.user_id from td_sm_userrole a " +
//								"where a.user_id = '"+user.getUserId()+"' and a.role_id = '1' " +
//								"union select b.user_id from td_sm_orgmanager b where b.user_id = '"+user.getUserId()
//								+"' and b.org_id = '"+orgId+"')";
//						DBUtil adminDBUtil = new DBUtil();
//						adminDBUtil.executeSelect(adminSql);
//						if(adminDBUtil.getInt(0,0)>0)
//						{
//							//该用户是系统管理员
//						}
//						else
//						{
							users.add(user);	//该用户不是系统管理员
//						}
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//					}					
					
				}
				listInfo.setDatas(users);
				listInfo.setTotalSize(dbUtil.getTotalSize());
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			return listInfo;
		}

		protected ListInfo getDataList(String arg0, boolean arg1) {
			return null;
		}
}
