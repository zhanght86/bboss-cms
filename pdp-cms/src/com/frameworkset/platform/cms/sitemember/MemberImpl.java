package  com.frameworkset.platform.cms.sitemember;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

public class MemberImpl implements java.io.Serializable { 
    
	
    //取会员帐号流水号  3位省市县代码+1位年份+4位流水号，如：A011001+0000
	public String getAccountNo(String citycode) throws Exception
	{ 
	   String ret="";
	   try
		{    
		 PreparedDBUtil conn = new PreparedDBUtil();
		 String sql="select to_char(sysdate,'y') today,lpad(seq_user.nextval,4,'0') file_no from dual";
		 
		 conn.executeSelect(sql);
	  	 if(conn.size()>0){    
			ret=citycode+conn.getString(0,"today")+conn.getString(0,"file_no"); 
		 }	 		
		
		}catch (Exception e){
		 System.out.print("会员帐号序号生成失败!"+e);
		}		 
	     
       return ret;
	 }
	
	
	/* 会员注册*/
	public String memberApply(SiteMember member) throws Exception {
		String ret="";
		
		try {
			
			PreparedDBUtil conn = new PreparedDBUtil();
			
			long userId = conn.getNextPrimaryKey("td_sm_user") ;
			
			//检测注册的帐号是否存在
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("insert into td_sm_user(");
			sqlBuffer.append("USER_SN,USER_PASSWORD,USER_NAME,USER_REALNAME,USER_ADDRESS,USER_MOBILETEL1,");
			sqlBuffer.append("USER_POSTALCODE,USER_EMAIL,USER_REGDATE,USER_ID");
			sqlBuffer.append(") values(?,?,?,?,?,?,?,?,?,?)");		

			
			//会员编号
			String account=getAccountNo(member.getCitycode());
			conn.preparedInsert(sqlBuffer.toString());			
			conn.setInt       (1, 100);
			conn.setString    (2, member.getPassword());
			conn.setString    (3, account);
			conn.setString    (4, member.getUserchs());
			conn.setString    (5, member.getAddress());
			conn.setString    (6, member.getMobile());
			conn.setString    (7, member.getUserPostalcode());
			conn.setString    (8, member.getEmail());
			conn.setTimestamp (9, new Timestamp(new java.util.Date().getTime()));
			conn.setLong(10,userId);
			
			conn.executePrepared();
			
			//会员附属表	
			StringBuffer sqlBuffer1 = new StringBuffer();
			sqlBuffer1.append("insert into td_sm_user_addons(");
			sqlBuffer1.append("USER_ID,QUESTION,ANSWER,MEMBER_TYPE,");
			sqlBuffer1.append("ENABLEOPEN,SERVICE_TYPE");
			sqlBuffer1.append(") values(?,?,?,?,?,?)");	
			
			conn.preparedInsert(sqlBuffer1.toString());			
			conn.setLong      (1, userId);
			conn.setString    (2, member.getQuestion());
			conn.setString    (3, member.getAnswer());
			conn.setString    (4, "1");
			conn.setString    (5, "0");
			conn.setString    (6, member.getServiceType());
			conn.executePrepared();
			
			ret=account;   //返回会员帐号
			
			
		} catch (SQLException e) {
			ret="error";
			e.printStackTrace();
		
		    
		}
     return ret;
	}
	
	/* 会员帐号检测*/
	public int memberAccountCheck(String useraccount) throws Exception {
		int ret=0;
		
		try {
			PreparedDBUtil conn = new PreparedDBUtil();
			//会员编号
			conn.executeSelect("select * from td_sm_user where user_name='"+useraccount+"'");
			if(conn.size()>0){
			  ret=1;
			}else{
			  ret=2;
			}
			
		} catch (SQLException e) {
			ret=0;
			e.printStackTrace();
			throw new Exception("SQL异常,帐号监测出错,异常信息为:"+ e.getMessage());
		    
		}
     return ret;
	}
	
	
	public String pwdGetBack(String account,String question,String answer) throws Exception {
		String ret="";
		try {
			PreparedDBUtil conn = new PreparedDBUtil();
			PreparedDBUtil pd   = new PreparedDBUtil();
			//会员帐号判断
			pd.executeSelect("select * from td_sm_user where USER_NAME='"+account+"' ");
			if(pd.size()==0){
				ret="您输入的帐号不对或者没有这个帐号！请检查";  
			}
			else{
			   String sql="select * from td_sm_user a,td_sm_user_addons b "
				      +"where b.question='"+question+"' and b.answer='"+answer+"' and a.USER_NAME='"+account+"' ";
			   conn.executeSelect(sql);
			   if(conn.size()>0){
			      ret="您的密码是:"+conn.getString(0,"USER_PASSWORD");  //返回密码
			   }else{
			      ret="您输入的密码提示问题或者答案不对！请检查";
		 	   }
			}
			
		} catch (SQLException e) {
			ret="系统异常,执行密码取回出错！";
			e.printStackTrace();
			
		    
		}
     return ret;
	}
	
	
	public String pwdChange(String account,String oldpwd,String newpwd) throws Exception {
		String ret="";
		try {
			 
			 PreparedDBUtil conn = new PreparedDBUtil();
			 PreparedDBUtil pd   = new PreparedDBUtil();
			 
			 pd.executeSelect("select * from td_sm_user where USER_NAME='"+account+"' and USER_PASSWORD='"+oldpwd+"'");
			 if(pd.size()==0){
				 ret="error oldpassword";
			 }
			 else{
			    String sql="update td_sm_user set USER_PASSWORD='"+newpwd+"' where USER_NAME='"+account+"'";
			    conn.executeUpdate(sql);
			    ret="success"; 
			 }
			 
		} catch (SQLException e) {
			ret="fail";
			e.printStackTrace();
			
		    
		}
     return ret;
	}
	/**
	 * 给用户授所有会员角色
	 * @param userId
	 * @throws Exception
	 */
	public void UserMemberRole(String userId) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql ="select role_id from td_sm_role where role_type ='member'";
		try {
			
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					String roleid = db.getString(i,"role_id");
					String str ="insert into td_sm_userrole(user_id,role_id) values("+ userId +",'"+roleid+"')";
					String select ="select count(*) from td_sm_userrole where " +
							" user_id="+ userId +" and role_id ='"+roleid+"'";
					db1.executeSelect(select);
					if(db1.getInt(0,0)==0)
					{
						db1.executeInsert(str);
					}
					
				}
			}
		}catch (Exception e) {
			
			e.printStackTrace();
			
		    
		}
	}
	/**
	 * 新建会员角色自动给所有内部用户加上
	 * @param roleId
	 * @throws Exception
	 */
	public void MemberByRole(String roleId) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql ="select a.user_id from td_sm_user a,td_sm_user_addons b "+
					" where a.user_id = b.user_id and b.ISPAY='2' ";
		try {
			
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					String userid = db.getInt(i,"user_id")+"";
					String str ="insert into td_sm_userrole(user_id,role_id) values("+ userid +",'"+roleId+"')";
					String select ="select count(*) from td_sm_userrole where " +
							" user_id="+ userid +" and role_id ='"+roleId+"'";
					db1.executeSelect(select);
					if(db1.getInt(0,0)==0)
					{
						db1.executeInsert(str);
					}
					
				}
			}
		}catch (Exception e) {
			
			e.printStackTrace();
			
		    
		}
	}
}
