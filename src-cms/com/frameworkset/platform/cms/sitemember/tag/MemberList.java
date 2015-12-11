package com.frameworkset.platform.cms.sitemember.tag;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * 取注册用户列表
 * @author Administrator
 *
 */
public class MemberList  extends DataInfoImpl implements java.io.Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String membername = request.getParameter("membername");
		String realname = request.getParameter("realname");
		String address = request.getParameter("address");
//		未登陆天数
		String nologin = request.getParameter("nologin");
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		try {
			  //查询条件为空取所有会员信息
			   if((membername==null && realname==null  && address==null && nologin==null )||(address.equals("") && realname.equals("") && membername.equals("")&& nologin.equals(""))){
      			
				   String sql ="select a.* from td_sm_user a,TD_SM_USER_ADDONS b" +
      						" where a.user_id = b.user_id and "+
      						" a.user_type = 1 and b.ISPAY='0' order by a.user_REGDATE desc";
				//String sql ="select * from td_sm_user where user_type =1 order by user_REGDATE desc ";   
      			
      			dbUtil.executeSelect(sql,(int)offset,maxPagesize);
    			List list = new ArrayList();
    			if(dbUtil.size()>0){
    				for (int i = 0; i < dbUtil.size(); i++) {
    					User user = new User();
    					user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
    					user.setUserName(dbUtil.getString(i,"user_name"));
    					user.setUserRealname(dbUtil.getString(i,"user_realname"));
    					user.setUserSex(dbUtil.getString(i,"user_sex"));
    					user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
    					user.setUserType(dbUtil.getString(i,"user_TYPE"));
    					user.setUserAddress(dbUtil.getString(i,"user_address"));
    					user.setUserPassword(dbUtil.getString(i,"user_password"));
    					
    					java.text.SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
    					//最后登陆时间
        				if(dbUtil.getDate(i,"LASTLOGIN_DATE")!=null)
    					{
    						String lasttime = formatter.format(dbUtil.getDate(i,"LASTLOGIN_DATE"));
    						user.setLastlogindate(Date.valueOf(lasttime));
    					}
      					//注册日期
      					if(dbUtil.getDate(i,"USER_REGDATE")==null){
      						user.setUser_Regdate("不详");
      					}
      					else
      					{
      						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
      						user.setUser_Regdate(regtime);
      					}
						list.add(user);
    				}
    				listInfo.setDatas(list);
    				listInfo.setTotalSize(dbUtil.getTotalSize());
    				return listInfo;
    			}
    			
      		}
			else
			{
      			//根据查询条件取会员列表
      			StringBuffer sb = new StringBuffer();
          		sb.append("select u.* from td_sm_user u,TD_SM_USER_ADDONS b ");
          		sb.append(" where u.user_id = b.user_id and ");
          		sb.append(" u.user_type = 1 and b.ISPAY='0' ");
          		//登陆名
      			if (membername != null && membername.length() > 0) {
    				sb.append(" and u.user_name like '%"+ membername +"%'");
    			}
      			//实名
          		if (realname != null && realname.length() > 0) {
          			sb.append(" and u.user_realname like '%"+ realname +"%'");
    			}
          		//联系地址
          		if (address != null && address.length() > 0 ) {
    				sb.append(" and u.user_address like '%"+ address +"%'");
    			}
          		//未登陆天数
          		if (nologin != null && nologin.length() > 0) {
          			String date = this.dateAdd( "-"+nologin);
    				sb.append(" and u.LASTLOGIN_DATE like to_date('"+ date +"','YYYY-MM-DD')");
    			}
          		sb.append(" order by u.user_REGDATE desc");
          		//System.out.println("----"+sb.toString());
          		dbUtil.executeSelect(sb.toString(),(int)offset,maxPagesize);
          		
    			List list = new ArrayList();
    			if(dbUtil.size()>0){
    				for (int i = 0; i < dbUtil.size(); i++) {
    					User user = new User();
    					user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
    					user.setUserName(dbUtil.getString(i,"user_name"));
    					user.setUserRealname(dbUtil.getString(i,"user_realname"));
    					user.setUserSex(dbUtil.getString(i,"user_sex"));
    					user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
    					user.setUserType(dbUtil.getString(i,"user_TYPE"));
    					user.setUserAddress(dbUtil.getString(i,"user_address"));
    					user.setUserPassword(dbUtil.getString(i,"user_password"));
    					user.setUser_Regdate(String.valueOf(dbUtil.getDate(i,"user_REGDATE")));
    					java.text.SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
    					//最后登陆时间
        				if(dbUtil.getDate(i,"LASTLOGIN_DATE")!=null)
    					{
    						String lasttime = formatter.format(dbUtil.getDate(i,"LASTLOGIN_DATE"));
    						user.setLastlogindate(Date.valueOf(lasttime));
    					}
      					//注册日期
      					if(dbUtil.getDate(i,"USER_REGDATE")==null){
      						user.setUser_Regdate("不详");
      					}
      					else
      					{
      						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
      						user.setUser_Regdate(regtime);
      					}
						list.add(user);
    				}
    				listInfo.setDatas(list);
    				listInfo.setTotalSize(dbUtil.getTotalSize());
    				return listInfo;
    			}
			}	
		}
		catch(Exception e){
			
		}
		return listInfo;
	}
	
	
	/* 将当前日期加减n天数。 
	* 如传入字符型"-5" 意为将当前日期减去5天的日期
	* 如传入字符型"5" 意为将当前日期加上5天后的日期
	* 返回字串 例(1999-02-03)
	*/
	public String dateAdd(String to)
	{
//	日期处理模块 (将日期加上某些天或减去天数)返回字符串
	int strTo;
	try {
	   strTo = Integer.parseInt(to);}
	catch (Exception e) {
	   System.out.println("日期标识转换出错! : \n:::" + to + "不能转为数字型");
	   e.printStackTrace();
	   strTo = 0;
	}
	Calendar strDate = Calendar.getInstance(); //java.util包,设置当前时间
	strDate.add(strDate.DATE, strTo); //日期减 如果不够减会将月变动 //生成 (年-月-日)
	String meStrDate = strDate.get(strDate.YEAR) + "-" +String.valueOf(strDate.get(strDate.MONTH)+1) + "-" + strDate.get(strDate.DATE);
	return meStrDate;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}
