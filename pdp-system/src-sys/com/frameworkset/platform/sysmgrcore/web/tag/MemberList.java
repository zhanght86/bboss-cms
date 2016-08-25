package com.frameworkset.platform.sysmgrcore.web.tag;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * 取会员列表
 * @author Administrator
 *
 */
public class MemberList  extends DataInfoImpl implements Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String membername = request.getParameter("membername");
		String memberRealname = request.getParameter("memberRealname");
		String memberType = request.getParameter("memberType");
		String Isvalid = request.getParameter("Isvalid");
//		System.out.println("...."+membername);
//		System.out.println("...."+memberRealname);
//		System.out.println("...."+memberType);
		ListInfo listInfo = new ListInfo();
		
		DBUtil dbUtil = new DBUtil();
		try {
			String sql ="select * from td_sm_user where 1=1";
      		if(membername==null && memberRealname==null && memberType==null && Isvalid==null){
      			sql ="select * from td_sm_user";
      		}
      		if (membername != null && membername.length() > 0) {
				sql = sql +" and user_name like '%"+ membername +"%'";
			}
      		if (memberRealname != null && memberRealname.length() > 0) {
				sql = sql +" and user_realname like '%"+ memberRealname +"%'";
			}
      		if (memberType != null && memberType.length() > 0) {
				sql = sql +" and user_type ='"+ memberType +"'";
			}
      		if (Isvalid != null && Isvalid.length() > 0) {
				sql = sql +" and  USER_ISVALID ="+ Isvalid +"";
			}
//      		System.out.println("...."+sql);
			dbUtil.executeSelect(sql + " order by user_sn,past_time,user_type",(int)offset,maxPagesize);
			List list = new ArrayList();
			
			if(dbUtil.size()>0){
				for (int i = 0; i < dbUtil.size(); i++) {
					User user = new User();
					user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
					user.setUserName(dbUtil.getString(i,"user_name"));
					user.setUserRealname(dbUtil.getString(i,"user_realname"));
					user.setUserSex(dbUtil.getString(i,"user_sex"));
					user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
					user.setUserType(dbUtil.getString(i,"USER_TYPE"));
					
				
						


					
					java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(dbUtil.getDate(i,"past_time")==null){
						user.setPast_Time("不详");
						user.setIsPast("不详");
					}else{
						String past_time = formatter.format(dbUtil.getDate(i,"past_time"));
						String pasttime = sdf.format(dbUtil.getDate(i,"past_time"));
						user.setPast_Time(past_time);
					
						
						java.util.Date date = new java.util.Date();
						java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance();
						if(date.before(df.parse(pasttime))){
							user.setIsPast("未过期");
						}else{
							user.setIsPast("已过期");
						}

			            

					}
					list.add(user);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.getTotalSize());
				return listInfo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}
