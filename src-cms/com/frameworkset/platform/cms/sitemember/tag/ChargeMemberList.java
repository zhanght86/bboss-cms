package com.frameworkset.platform.cms.sitemember.tag;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * 取收费用户列表
 * @author Administrator
 *
 */
public class ChargeMemberList  extends DataInfoImpl implements java.io.Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String membername = request.getParameter("membername");
		String realname = request.getParameter("realname");
		String address = request.getParameter("address");
		//状态
		String state = request.getParameter("state");
		//有效性
		String Isvalid = request.getParameter("Isvalid");
		//会员类别
		String rank = request.getParameter("rank");
		//未登陆天数
		String nologin = request.getParameter("nologin");
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		try {
			  //查询条件为空取所有会员信息
			   if((membername==null && realname==null  && Isvalid==null && rank ==null && state==null && address ==null && nologin==null)
					   ||(rank.equals("") && realname.equals("") && Isvalid.equals("")&&membername.equals("") && state.equals("") && address.equals("") && nologin.equals(""))){
      			String sql ="select a.*,b.member_type,b.ispay,b.ENTERPRISE_LINKMAN,b.SERVICE_TYPE,b.STATUS " +
      						" from td_sm_user a,TD_SM_USER_ADDONS b" +
      						" where a.user_id = b.user_id and "+
      						" a.user_type = 1 and ispay='1' order by a.PAST_TIME desc";
      			//System.out.println("............."+sql);
      			dbUtil.executeSelect(sql,(int)offset,maxPagesize);
    			List list = new ArrayList();
    			if(dbUtil.size()>0){
    				for (int i = 0; i < dbUtil.size(); i++) {
    					User user = new User();
    					user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
    					user.setUserName(dbUtil.getString(i,"user_name"));
    					user.setUserRealname(dbUtil.getString(i,"user_realname"));
    					user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
    					user.setUserType(dbUtil.getString(i,"user_TYPE"));
    					user.setUserAddress(dbUtil.getString(i,"user_address"));
    					user.setUserPassword(dbUtil.getString(i,"user_password"));
    					//会员状态
    					user.setRemark1(dbUtil.getString(i,"STATUS"));
    					//会员类别
    					user.setEnterpriseLinkman(dbUtil.getString(i,"ENTERPRISE_LINKMAN"));
    					user.setServiceType(dbUtil.getString(i,"SERVICE_TYPE"));
    				
    					
    					//判断会员是否已到过期时间SimpleDateFormat dateFormatter = DataFormatUtil.getSimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
    				    java.text.SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
    					java.text.SimpleDateFormat sdf = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd HH:mm:ss");
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
    					//开通停用日期
    					user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
    				
    					
    					if(dbUtil.getDate(i,"past_time")==null){
    						user.setPast_Time("不详");
    						
    					}else{
    						String past_time = formatter.format(dbUtil.getDate(i,"past_time"));
    						String pasttime = sdf.format(dbUtil.getDate(i,"past_time"));
    						user.setPast_Time(past_time);
    					}
    					//取会员等级(角色信息)-----
						String strsql = "select role_name from td_sm_role where role_id in" +
								"(select role_id from td_sm_userrole where " +
								" user_id ="+ dbUtil.getInt(i,"user_id")+" and role_id<>'15' and role_id<>'7')" +
								" order by role_name";
						db.executeSelect(strsql);
					
						boolean flag = false;
						String memberRank ="";
						for (int j = 0; j < db.size(); j++) {
							if (!flag) {
								
									memberRank = db.getString(j,"role_name");
								flag = true;
							} else {
									memberRank += ","+ db.getString(j,"role_name");
								
							}
						}
						user.setMemberRole(memberRank);
						list.add(user);
    				}
    				listInfo.setDatas(list);
    				listInfo.setTotalSize(dbUtil.getTotalSize());
    				return listInfo;
    			}
    			
      		}else{
      			//根据查询条件取会员列表
      			StringBuffer sb = new StringBuffer();
          		sb.append("select distinct b.member_type,u.user_id,u.user_name,u.LASTLOGIN_DATE,");
          		sb.append(" u.user_realname,u.user_sex,u.remark1,u.user_Password,b.status,");
          		sb.append(" u.USER_ISVALID,u.USER_TYPE,u.past_time,b.ENTERPRISE_LINKMAN,");
          		sb.append(" b.SERVICE_TYPE,u.USER_REGDATE,u.DREDGE_TIME,u.USER_ADDRESS");
          		sb.append(" from td_sm_user u,td_sm_userrole r,td_sm_USER_ADDONS b");
          		sb.append(" where u.user_id = r.user_id and u.user_id = b.user_id and u.user_type =1 and b.ispay='1'");
          		if (membername != null && membername.length() > 0) {
    				sb.append(" and u.user_name like '%"+ membername +"%'");
    			}
          		if (realname != null && realname.length() > 0) {
    				sb.append(" and u.user_realname like '%"+ realname +"%'");
    			}
          		if (address != null && address.length() > 0 ) {
    				sb.append(" and u.user_address like '%"+ address +"%'");
    			}
          		//有效性
          		if (Isvalid != null && Isvalid.length() > 0 && Isvalid.equals("1")) {
    				sb.append(" and u.past_time >sysdate and b.status ='2'");
    			}
          		if (Isvalid != null && Isvalid.length() > 0 && Isvalid.equals("0")) {
    				sb.append(" and u.past_time <sysdate");
    			}
          		//会员状态
          	
          		if (state != null && state.length() > 0) {
    				sb.append(" and b.status ="+ state +"");
    			}
          		//会员申请类别
          		if (rank != null && rank.length() > 0) {
    				sb.append(" and b.SERVICE_TYPE ='"+ rank +"'");
    			}
          		//未登陆天数
          		if (nologin != null && nologin.length() > 0) {
          			String date = this.dateAdd( "-"+nologin);
    				sb.append(" and u.LASTLOGIN_DATE like to_date('"+ date +"','YYYY-MM-DD')");
    			}
          		sb.append(" order by u.past_time desc");
          		
          		//System.out.println("+++"+sb.toString());
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
    					//会员状态
    					user.setRemark1(dbUtil.getString(i,"STATUS"));
    				
    					//会员类别
    					user.setEnterpriseLinkman(dbUtil.getString(i,"ENTERPRISE_LINKMAN"));
    					user.setServiceType(dbUtil.getString(i,"SERVICE_TYPE"));
    					
    					//判断会员是否已到过期时间
    				    java.text.SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
    					java.text.SimpleDateFormat sdf = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd HH:mm:ss");
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
    					//开通停用日期
    					user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
    				
    					
    					if(dbUtil.getDate(i,"past_time")==null){
    						user.setPast_Time("不详");
    						
    					}else{
    						String past_time = formatter.format(dbUtil.getDate(i,"past_time"));
    						String pasttime = sdf.format(dbUtil.getDate(i,"past_time"));
    						user.setPast_Time(past_time);
    					}
    					//取会员等级(角色信息)-----
						String strsql = "select role_name from td_sm_role where role_id in" +
								"(select role_id from td_sm_userrole where user_id ="+ dbUtil.getInt(i,"user_id")+" and role_id<>'15') order by role_name";
						db.executeSelect(strsql);
					
						boolean flag = false;
						String memberRank ="";
						for (int j = 0; j < db.size(); j++) {
							if (!flag) {
							
									memberRank = db.getString(j,"role_name");
								
								flag = true;
							} else {
								
								
									memberRank += ","+ db.getString(j,"role_name");
								
							}
						}
						user.setMemberRole(memberRank);
						list.add(user);
    				}
    				listInfo.setDatas(list);
    				listInfo.setTotalSize(dbUtil.getTotalSize());
    				return listInfo;
    			}
      		}
      		
      		

			
		}catch(Exception e){
			e.printStackTrace();
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
