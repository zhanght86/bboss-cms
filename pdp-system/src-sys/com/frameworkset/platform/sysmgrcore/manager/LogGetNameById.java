package com.frameworkset.platform.sysmgrcore.manager;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.platform.sysmgrcore.manager.db.GroupCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.RoleCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
/**
 * 方便记录日志的时候，从ID到NAME的转换。
 * 
 * @author hongguang.zhao 
 *
 */
public class LogGetNameById{
	private static Logger log = LoggerFactory.getLogger(LogGetNameById.class);
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
			
		returnValue = (String)UserCacheManager.getInstance().getUserByID(userId).getUserAttribute("userName");
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
		StringBuilder  returnValue=new StringBuilder();	
		try
		{
			for(int i=0;i<userIds.length;i++)
			{
				returnValue.append((String)UserCacheManager.getInstance().getUserByID(userIds[i]).getUserAttribute("userName")).append(" ");//SecurityDatabase.getUserManager().getUserById(userIds[i]).getUserRealname()+" ";
			}		
		}catch(Exception e){ }
		return returnValue.toString();
	}
	public static String getGroupNameByGroupId(String groupId)
	{
		String  returnValue="";
	
		try
		{
		returnValue = GroupCacheManager.getInstance().getGroupByID(groupId).getGroupName();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getGroupNamesByGroupIds(String groupIds[])
	{
		StringBuilder  returnValue=new StringBuilder();	
		
		try
		{
			for(int i=0;i<groupIds.length;i++)
			{
				
				returnValue.append(GroupCacheManager.getInstance().getGroupByID(groupIds[i]).getGroupName()).append(" ");
			}					
		}catch(Exception e){ }		
		return returnValue.toString();
	}
	public static String getJobNameByJobId(String jobId)
	{
		String  returnValue="";
	
		try
		{
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect("select JOB_NAME from TD_SM_JOB where JOB_ID =?");
			db.setString(1, jobId);
			db.executePrepared();
			returnValue = db.getString(0,"JOB_NAME");		
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getJobNamesByJobIds(String jobIds[])
	{
		if(jobIds == null || jobIds.length == 0)
			return "";
		final StringBuilder returnValue = new StringBuilder();
		 
		//System.out.println(jobidsIn);
		try
		{	
			StringBuilder sb = new StringBuilder();
			sb.append("select JOB_NAME from TD_SM_JOB where JOB_ID in (#foreach($jid in $jobIds)  #if($velocityCount == 0) ")
			.append(" #[jobIds[$velocityCount]] ")
			.append("    #else ")
			.append(" ,#[jobIds[$velocityCount]] ")
			.append(" #end  #end)");
			Map params = new HashMap();
			params.put("jobIds", jobIds);
			SQLExecutor.queryBeanByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					returnValue.append(origine.getString("JOB_NAME")).append(" ");
					
				}
				
			}, sb.toString(), params);
		 
			
		}catch(Exception e)
		{
			log.error("getJobNamesByJobIds:"+jobIds, e);
		}
		return returnValue.toString();
	}
	public static String getOrgNameByOrgId(String orgId)
	{
		String  returnValue="";	
		try
		{
		returnValue = OrgCacheManager.getInstance().getOrganization(orgId).getRemark5();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getOrgNamesByOrgIds(String orgIds[])
	{
		StringBuilder  returnValue=new StringBuilder();	
		try
		{
			for(int i=0;i<orgIds.length;i++)
			{
				returnValue.append(OrgCacheManager.getInstance().getOrganization(orgIds[i]).getRemark5()).append(" ");
			}
		}catch(Exception e)
		{			
		}
		return returnValue.toString();
	}
	public static String getRoleNameByRoleId(String roleId)
	{
		String  returnValue="";
	
		try
		{
		returnValue = RoleCacheManager.getInstance().getRoleByID(roleId).getRoleName();
		}catch(Exception e)
		{			
		}
		return returnValue;
	}
	public static String getRoleNamesByRoleIds(String roleIds[])
	{
		StringBuilder  returnValue=new StringBuilder();	
	
		try
		{
			for(int i=0;i<roleIds.length;i++)
			{
				returnValue .append(RoleCacheManager.getInstance().getRoleByID(roleIds[i]).getRoleName()).append(" ");
			}
		}catch(Exception e)
		{			
		}
		return returnValue.toString();
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
