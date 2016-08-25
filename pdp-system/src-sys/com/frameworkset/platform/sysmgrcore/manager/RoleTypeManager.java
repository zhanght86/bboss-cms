package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.RoleType;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

public class RoleTypeManager implements Serializable
{
	//判断是否存在同名的角色类型
	public boolean isRoleTypeExist(RoleType rt) throws Exception
	{
		boolean ret = false;	
		DBUtil db = new DBUtil();
		String sql ="select count(*) from td_sm_roletype where type_name ='"+ rt.getTypeName() +"'";
			db.executeSelect(sql);
			if(db.getInt(0,0)>0)
			{
				ret = true;
			}
		return ret;		
	}
	
	//修改角色类型信息时，判断是否存在同名的角色类型
	public boolean isRoleTypeExistForUpdate(RoleType rt) throws Exception
	{
		boolean ret = false;	
		if(rt != null)
		{
			DBUtil db = new DBUtil();
			String sql ="select count(*) from td_sm_roletype where type_name ='"+ rt.getTypeName() +"' and type_id <> '" + rt.getRoleTypeID() + "'";
			db.executeSelect(sql);
			if(db.getInt(0,0)>0)
			{
				ret = true;
			}
		}		
		return ret;		
	}
	
	/**
	 * 
	 * @param  增加角色类别
	 * @return ret
	 * @throws Exception
	 */
	public boolean addRoleType(RoleType rt) throws Exception
	{
		boolean ret = false;		
		try {
	    	StringBuffer sqlBuffer = new StringBuffer();
	    	PreparedDBUtil conn = new PreparedDBUtil();	 
	    	int type_id;
	    	type_id=Integer.parseInt(conn.getNextStringPrimaryKey("TD_SM_ROLETYPE"));
	    	sqlBuffer.append( "insert into TD_SM_ROLETYPE(TYPE_NAME,TYPE_DESC,CREATOR_USER_ID,CREATOR_ORG_ID,TYPE_ID)");
	    	sqlBuffer.append( " values(?,?,?,?,?)");
	    	conn.preparedInsert(sqlBuffer.toString());			
	    	conn.setString(1,rt.getTypeName());	
	    	conn.setString(2,rt.getTypeDesc());
	    	conn.setString(3,rt.getCreatorUserId());
	    	conn.setString(4,rt.getCreatorOrgId());
	    	conn.setInt(5,type_id);
			conn.executePrepared();			
			ret=true;
		    } 
		catch (SQLException e) 
			{
			ret=false;
			e.printStackTrace();
			}
		return ret;   	
    }
	
	/**
	 * 
	 * @param  修改角色类别
	 * @return ret
	 * @throws Exception
	 */
	public boolean modifyRoleType(RoleType rt) throws Exception
	{
		boolean ret = false;	
		if(rt != null)
		{
			try 
			{
		    	StringBuffer sqlBuffer = new StringBuffer();
		    	PreparedDBUtil conn = new PreparedDBUtil();	    
		    	sqlBuffer.append( "update TD_SM_ROLETYPE " +
		    			"set TYPE_NAME=? " +
		    			", TYPE_DESC=? " +
		    			"where type_id=?");
		    	conn.preparedUpdate(sqlBuffer.toString());			
		    	conn.setString(1,rt.getTypeName());	
		    	conn.setString(2,rt.getTypeDesc());	  
		    	conn.setString(3,rt.getRoleTypeID());	
				conn.executePrepared();			
				ret=true;
		    } 
			catch (SQLException e) 
			{
				ret=false;
				e.printStackTrace();
			}
		}		
		return ret;   	
    }
	
	//根据角色类型ID删除，同时将该类型的下的角色的角色类型设为空
    public boolean delRoleType(String id) throws Exception
    {
    	boolean ret=false;		
		try {			
			StringBuffer sqlBuffer = new StringBuffer();
			StringBuffer sqlBuffer1 = new StringBuffer();
			sqlBuffer.append("delete from TD_SM_ROLETYPE where type_id='"+id+"'");
			sqlBuffer1.append("update TD_SM_ROLE set role_type='' where role_type='"+id+"'");
			PreparedDBUtil conn = new PreparedDBUtil();
			PreparedDBUtil conn1 = new PreparedDBUtil();
			conn.preparedDelete(sqlBuffer.toString());
			conn1.preparedDelete(sqlBuffer1.toString());
			conn.executePrepared();
			conn1.executePrepared();
			ret=true;			
			
		} catch (SQLException e) {
			ret=false;
			e.printStackTrace();			
		}
		
		return ret;
    }
	//获取角色类型列表
	public List getTypeNameList() throws Exception
	{
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "select * from td_sm_roletype order by type_id";
		try
		{
			dbUtil.executeSelect(sql);
	   		if (dbUtil.size()>0)
	   		{
	   			for(int i=0;i<dbUtil.size();i++)
	   			{
	   				RoleType rt = new RoleType();
	   				rt.setRoleTypeID(dbUtil.getString(i,"type_id"));
	   				rt.setTypeName(dbUtil.getString(i,"type_name"));
	   				rt.setTypeDesc(dbUtil.getString(i,"type_desc"));
	   				rt.setCreatorOrgId(dbUtil.getString(i, "creator_org_id"));
	   				rt.setCreatorUserId(dbUtil.getString(i, "creator_user_id"));
	   				list.add(rt);
	   			}
	   		}
	   		
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public List getTypeNameList(String curOrgId) throws Exception
	{
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "select * from td_sm_roletype where creator_org_id='"+curOrgId+"' order by type_id";
		try
		{
			dbUtil.executeSelect(sql);
	   		if (dbUtil.size()>0)
	   		{
	   			for(int i=0;i<dbUtil.size();i++)
	   			{
	   				RoleType rt = new RoleType();
	   				rt.setRoleTypeID(dbUtil.getString(i,"type_id"));
	   				rt.setTypeName(dbUtil.getString(i,"type_name"));
	   				rt.setTypeDesc(dbUtil.getString(i,"type_desc"));
	   				rt.setCreatorOrgId(dbUtil.getString(i, "creator_org_id"));
	   				rt.setCreatorUserId(dbUtil.getString(i, "creator_user_id"));
	   				list.add(rt);
	   			}
	   		}
	   		
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean hasType()
	{
		
		DBUtil dbUtil = new DBUtil();
		String sql = "select count(*) from td_sm_roletype ";
		try
		{
			dbUtil.executeSelect(sql);
	   		return dbUtil.getInt(0,0) > 0;
	   		
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean hasRoles(String typeid)
	{
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "select count(*) from td_sm_role where role_type ='"+typeid+"'";
		try
		{
			dbUtil.executeSelect(sql);
			return dbUtil.getInt(0,0) > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	//根据角色类型ID，获取该角色类型下的角色列表
	public List getRoleList(String id) throws Exception
	{
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "select * from td_sm_role where role_type ='"+id+"' order by role_name";
		try
		{
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0)
			{
				for(int i=0;i<dbUtil.size();i++)
				{
					Role r = new Role();
					r.setRoleId(dbUtil.getString(i,"role_id"));
					r.setRoleName(dbUtil.getString(i,"role_name"));
					r.setOwner_id(dbUtil.getInt(i, "OWNER_ID"));
					//r.setTypeDesc(dbUtil.getString(i,"type_desc"));
					list.add(r);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	return list;
	}
	
	public RoleType getRoleType(String id) throws Exception
	{
		DBUtil dbUtil = new DBUtil();
		RoleType rt = new RoleType();
		String sql = "select * from td_sm_roletype where type_id ='"+id+"'";
		try
		{
			dbUtil.executeSelect(sql);
			if(dbUtil.size()>0)
			{	
				rt.setRoleTypeID(dbUtil.getString(0,"type_id"));
				rt.setTypeName(dbUtil.getString(0,"type_name"));
				rt.setTypeDesc(dbUtil.getString(0,"type_desc"));
				rt.setCreatorOrgId(dbUtil.getString(0,"creator_org_id"));
				rt.setCreatorUserId(dbUtil.getString(0,"creator_user_id"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rt;
	}
	
	public List getRoleSealedRole(){
		DBUtil dbUtil = new DBUtil();
		List list = null;
		String sql = "select * from td_sm_role where role_id not in (select a.role_id from td_sm_role a, " +
				"td_sm_roletype b where a.ROLE_type=b.TYPE_ID)";
		try {
			dbUtil.executeSelect(sql);
			if(dbUtil.size() > 0){
				list = new ArrayList();
				Role role = null;
				for(int i = 0; i < dbUtil.size(); i++){
					role = new Role();
					role.setRoleId(dbUtil.getString(i,"role_id"));
					role.setRoleName(dbUtil.getString(i,"role_name"));
					list.add(role);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 
	 * @param sql
	 * @return Map<roleTypeId,roleTypeName>
	 */
	public Map getRoleTypeMap(){
		Map map = null ;
		DBUtil db = new DBUtil();
		String sql = "select TYPE_ID,TYPE_NAME from td_sm_roletype";
		try {
			db.executeSelect(sql);
			if(db.size() > 0){
				map = new HashMap();
				for(int i = 0; i < db.size(); i++){
					map.put(db.getString(i, "TYPE_ID"), db.getString(i, "TYPE_NAME"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
