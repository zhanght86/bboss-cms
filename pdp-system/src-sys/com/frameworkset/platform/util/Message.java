package com.frameworkset.platform.util;

import java.sql.SQLException;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;




public class Message {	
	public int getPrimaryKey()
	{
		int ret = 1;
		DBUtil db = new DBUtil();
		String sql = "select max(id) as id from sms_boxsending";
		try {
			db.executeSelect("message",sql);
			if(db.size()>0)
			{
				ret = db.getInt(0,0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret+1;
	}
	/**
	 * 
	 * @param sender
	 * @param receiver
	 * @param content
	 * @param sendtime
	 * @param inserttime
	 * @param retrytimes 0
	 * @param pri 1
	 * @param inpool 0
	 * @param sendmode 1
	 * @return
	 */
	public  boolean sendMsg(String sender,String receiver,String content,String sendtime,String inserttime,int retrytimes,String pri,String inpool,String sendmode)
	{
		boolean ret = false;
		String[] array = receiver.split(",");
		for(int i=0;i<array.length;i++)
		{
			String sql = "insert into sms_boxsending(id,sender,receiver,content,sendtime,inserttime,retrytimes,pri,inpool,sendmode)"
				+" values("+getPrimaryKey()+",'"+sender+"','"+array[i]+"','"+content+"','"+sendtime+"','"+inserttime+"',"
				+retrytimes+",'"+pri+"','"+inpool+"','"+sendmode+"')";
			DBUtil db = new DBUtil();
			try {
				db.executeInsert("message",sql);
				ret = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	public boolean doDelete(String zftsID,String table_name,String module_name,String account,String userName,String ipAddress) throws SPIException, ManagerException, Exception
    {
    	boolean ret=false;		
		try {
            //---------------START--删除操作类日志
			String operContent="";        
	        String openModle="短信删除";
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="删除操作: "+	"ID为 "+getTitlesById(zftsID,table_name)+" 的短信被删除 ";
	        //---------------END
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("delete from "+table_name+" where id in ("+zftsID+")");		
			PreparedDBUtil conn = new PreparedDBUtil();
			conn.preparedDelete("message",sqlBuffer.toString());
			conn.executePrepared();
			ret=true;
            //--删除操作类日志
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);       
			//--
			
		} catch (SQLException e) {
			ret=false;
			e.printStackTrace();
			throw new Exception("SQL异常:"+ e.getMessage());
		}
		return ret;
    }
	public String getTitlesById(String id,String table_name)
 	{
 		String ret = "";
		String sql = "select id from "+table_name+"  where id in ("+id+")";
 		 DBUtil db = new DBUtil();
 		 try {
			db.executeSelect("message",sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					if(ret.equals(""))
					{
						ret = db.getString(0,"id");
					}
					else
					{
						ret += " || " + db.getString(0,"id");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return ret;
 	}
	public String getTelFromUser(String roleName,String userType)
	{
		String ret = "";
		String sql = "select a.user_mobiletel1,a.user_mobiletel2 from td_sm_user a,td_sm_userrole b,td_sm_role c where a.user_id=b.user_id and b.role_id=c.role_id and a.user_isvalid=2 and a.user_type='"+userType+"' and role_name='"+roleName+"' and (a.user_mobiletel1 is not null or a.user_mobiletel2 is not null)";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					if(ret.equals(""))
					{
						if(db.getString(i,"user_mobiletel1")==null||db.getString(i,"user_mobiletel1").equals(""))
							ret = db.getString(i,"user_mobiletel2");
						else
							ret = db.getString(i,"user_mobiletel1");
					}
					else
					{
						if(db.getString(i,"user_mobiletel1")==null||db.getString(i,"user_mobiletel1").equals(""))
							ret += "," + db.getString(i,"user_mobiletel2");
						else
							ret += "," + db.getString(i,"user_mobiletel1");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
