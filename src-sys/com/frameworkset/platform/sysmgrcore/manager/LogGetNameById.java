package com.frameworkset.platform.sysmgrcore.manager;
import java.io.Serializable;

import com.frameworkset.common.poolman.DBUtil;
/**
 * 方便记录日志的时候，从ID到NAME的转换。
 * 
 * @author hongguang.zhao 
 *
 */
public class LogGetNameById implements Serializable {
	/**
	 * 通过用户ID返回用户名称
	 * @param userId
	 * @return 用户名称
	 */
	public static String getUserNameByUserId(String userId)
	{
		String  returnValue="";	
		try
		{
		returnValue = SecurityDatabase.getUserManager().getUserByName(userId).getUserRealname();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	/**
	 * 通过用户ID数组，返回用户名称集合字符串
	 * @param userIds
	 * @return 用户名称集合
	 */
	public static String getUserNamesByUserIds(String userIds[])
	{
		String  returnValue="";	
		try
		{
			for(int i=0;i<userIds.length;i++)
			{
				returnValue += SecurityDatabase.getUserManager().getUserById(userIds[i]).getUserRealname()+" ";
			}		
		}catch(Exception e){ }
		return returnValue;
	}
	public static String getGroupNameByGroupId(String groupId)
	{
		String  returnValue="";
	
		try
		{
		returnValue = SecurityDatabase.getGroupManager().getGroupByID(groupId).getGroupName();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getGroupNamesByGroupIds(String groupIds[])
	{
		String  returnValue="";
		
		try
		{
			for(int i=0;i<groupIds.length;i++)
			{
				returnValue += SecurityDatabase.getGroupManager().getGroupByID(groupIds[i]).getGroupName()+" ";
			}					
		}catch(Exception e){ }		
		return returnValue;
	}
	public static String getJobNameByJobId(String jobId)
	{
		String  returnValue="";
	
		try
		{
			DBUtil db = new DBUtil();
			db.executeSelect("select JOB_NAME from TD_SM_JOB where JOB_ID ='"+jobId+"'");
			returnValue = db.getString(0,"JOB_NAME");		
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getJobNamesByJobIds(String jobIds[])
	{
		String  returnValue="";
		String jobidsIn="";
		for(int i=0;i<jobIds.length;i++)
		{
			if(jobIds[i]!=null || jobIds[i]!="")
			{	
				jobidsIn+="'"+jobIds[i]+"',";			
			}
		}
		jobidsIn=jobidsIn.substring(0,jobidsIn.length()-1);	
		//System.out.println(jobidsIn);
		try
		{	
			DBUtil db = new DBUtil();
			db.executeSelect("select JOB_NAME from TD_SM_JOB where JOB_ID in ("+jobidsIn+")");
			
			for(int i=0;i<db.size();i++)
			{
				returnValue+=db.getString(i,"JOB_NAME")+" ";
			}
			
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return returnValue;
	}
	public static String getOrgNameByOrgId(String orgId)
	{
		String  returnValue="";	
		try
		{
		returnValue = SecurityDatabase.getOrgManager().getOrgById(orgId).getRemark5();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getOrgNamesByOrgIds(String orgIds[])
	{
		String  returnValue="";	
		try
		{
			for(int i=0;i<orgIds.length;i++)
			{
				returnValue += SecurityDatabase.getOrgManager().getOrgById(orgIds[i]).getRemark5()+" ";
			}
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getRoleNameByRoleId(String roleId)
	{
		String  returnValue="";
	
		try
		{
		returnValue = SecurityDatabase.getRoleManager().getRoleById(roleId).getRoleName();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getRoleNamesByRoleIds(String roleIds[])
	{
		String  returnValue="";
	
		try
		{
			for(int i=0;i<roleIds.length;i++)
			{
				returnValue += SecurityDatabase.getRoleManager().getRoleById(roleIds[i]).getRoleName()+" ";
			}
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getResNameByResId(String resId)
	{
		String  returnValue="";
	
		try
		{
		returnValue = SecurityDatabase.getResourceManager().getRes("res_Id",resId).getRestypeName();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getResNamesByResIds(String resIds[])
	{
		String  returnValue="";
	
		try
		{
			for(int i=0;i<resIds.length;i++)
			{
				returnValue += SecurityDatabase.getResourceManager().getRes("res_Id",resIds[i]).getRestypeName()+" ";
			}
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getDictNameByDictId(String dictdataId)
	{
		String  returnValue="";
	
		try
		{
			//DBUtil db = new DBUtil();
			//db.executeSelect("select dictdata_name  from td_sm_dictdata where dictdata_id = "+dictdataId);
			//returnValue = db.getString(0,"dictdata_name");	
			return "1111";
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
}
