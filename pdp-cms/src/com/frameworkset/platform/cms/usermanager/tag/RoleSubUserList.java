package com.frameworkset.platform.cms.usermanager.tag;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * 查看角色下包含的用户
 * @author zhuo.wang
 *
 */
public class RoleSubUserList extends DataInfoImpl implements java.io.Serializable{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {
	    	
	        ListInfo listInfo = new ListInfo();
	        String roleId = request.getParameter("roleId");
	       
	      
			DBUtil dbUtil = new DBUtil();
	
			try {
				StringBuffer hsql = new StringBuffer("select * from td_sm_user where user_id in "+
							" (select user_id from td_sm_userrole where role_id ='"+ roleId +"')");
				
				dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
				System.out.println(hsql.toString());
			
				User user = null;
				List users = new ArrayList();
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					user = new User();
					user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
					user.setUserName(dbUtil.getString(i,"user_name"));
					user.setUserRealname(dbUtil.getString(i,"user_realname"));
					user.setUserEmail(dbUtil.getString(i,"USER_EMAIL"));
					user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
					
					//注册日期
					java.text.SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
  						user.setUser_Regdate("不详");
  					}
  					else
  					{
  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
  						user.setUser_Regdate(regtime);
  					}
					users.add(user);
					
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
