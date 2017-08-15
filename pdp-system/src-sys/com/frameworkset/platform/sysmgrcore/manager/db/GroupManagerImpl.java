/*
 * Created on 2006-2-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.RollbackException;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.tag.pager.ListInfo;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Groupexp;
import com.frameworkset.platform.sysmgrcore.entity.Grouprole;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
/**
 * 项目：SysMgrCore <br>
 * 描述：组管理实现类 <br>
 * 版本：1.0 <br>
 * 
 * @author
 */  
public class GroupManagerImpl extends EventHandle implements GroupManager {
	
	private List userList;
	private Logger logger = LoggerFactory.getLogger(GroupManagerImpl.class.getName());

	
	/**
	 * 获取用户组的层级关系码
	 * 根为：0
	 * 一级为：0|1,0|2
	 * 二级为：0|1|1,0|1|2,0|1|3,....,0|1|100
	 * 三级：0|1|100|1
	 * @return
	 * @throws SQLException 
	 */
	public static String getGroupTreeLevel(String parentid,String current) throws SQLException
	{
		String group_tree_level = getParentGroupTreeLevel(parentid) + "|" + current;
		
		return group_tree_level;
	}
	
	
	public static String getParentGroupTreeLevel(String parentid) throws SQLException
	{
		String sqlMaxSn = "select group_tree_level from td_sm_group where GROUP_ID='"+parentid+"'";
		if(parentid == null || parentid.equals("") || parentid.equals("0") || parentid.equals("null"))
			return "0";
		DBUtil db = new DBUtil();
		db.executeSelect(sqlMaxSn);
		return db.getString(0,"group_tree_level");	
		
	}


	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用的方法，方法实现已被注释掉
	 */
	public boolean storeGroup(Group group) throws ManagerException { // 存储组
		boolean r = false;

//		// 保存
//		Parameter p = new Parameter();
//		p.setCommand(Parameter.COMMAND_STORE);
//		p.setObject(group);
//		try {
//			cb.execute(p);
//			r = true;
//			Event event = new EventImpl("",
//					ACLEventType.GROUP_ROLE_INFO_CHANGE);
//			super.change(event);
//		} catch (ControlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return r;
	}
	
	/**
	 * 新增用户组信息
	 * @param group 组实体对象
	 */
	public String insertGroup(Group group,String currentUserId) throws ManagerException {
		String exceptionInfo = "操作失败";
		
		// 创建新的用户组
		// 判断是否有相同的组名称
		Group g1 = getGroupByName(group.getGroupName());
		
		if (g1 != null) {
			exceptionInfo="已经有名称为" + group.getGroupName() + "的用户组了。";
			logger.info(exceptionInfo);
			return exceptionInfo;
		}
		DBUtil db = new DBUtil();
		PreparedDBUtil pe = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try 
		{
            int parentId = group.getParentId();
			
			PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
			String groupid = preparedDBUtil.getNextStringPrimaryKey("TD_SM_Group");
			String grouprreeLevel = getGroupTreeLevel(String.valueOf(parentId), String.valueOf(groupid));;
			
			//执行新增操作
			tm.begin();
			String sql="insert into TD_SM_Group(GROUP_NAME,GROUP_DESC,PARENT_ID,OWNER_ID,group_tree_level,GROUP_ID) values(?,?,?,?,?,?)";
			
//			+group.getGroupName()+"','"+group.getGroupDesc()+"',"+group.getParentId()+"," + group.getOwner_id() + "," + grouprreeLevel + ")";
//			String groupid = db.executeInsert(sql).toString();	
//			String groupid = pe.getNextStringPrimaryKey("TD_SM_Group");
	        String resid = groupid;
	        String resname = group.getGroupName();
	        String opid1 = "write";
	        String opid2 = "read";
	        String resTypeid ="group";
	        
	        pe.preparedInsert(sql);
			pe.setString(1, group.getGroupName());
			pe.setString(2, group.getGroupDesc());
			pe.setInt(3, group.getParentId());
			pe.setInt(4, group.getOwner_id());
			pe.setString(5, grouprreeLevel);
			pe.setString(6, groupid);
			
			pe.executePrepared();
	        
	        //将存权限的方法改为sql语句，进行批处理，执行完毕发permission_change事件
	        //危达200711171454
	        String sql1 = "insert into td_sm_roleresop" +
	        		" (OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)" +
	        		" values ('" + opid1 + "','" + resid + "','" + currentUserId + "'," +
	        				"'" + resTypeid + "','" + resname + "','user')";
	        String sql2 = "insert into td_sm_roleresop" +
    		" (OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)" +
    		" values ('" + opid2 + "','" + resid + "','" + currentUserId + "'," +
    				"'" + resTypeid + "','" + resname + "','user')";	
			db.addBatch(sql1);
			db.addBatch(sql2);
			db.executeBatch();
			tm.commit();
	    	Event event = new EventImpl(groupid,
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			
	    	exceptionInfo = "操作成功";
		}
		catch (Exception e) 
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			db.resetBatch();
		}
		return exceptionInfo;
	}

	
	/**
	 * 修改用户组信息
	 * @param group 组实体对象
	 */
	public String updateGroup(Group group,String oldName) throws ManagerException {
		String exceptionInfo = "操作失败";
		// 判断当前名称是否发生改变
		if(!group.getGroupName().equals(oldName))
		{
			// 判断是否有相同的组名称
			Group g1 = getGroupByName(group.getGroupName());
			if (g1 != null) 
			{
				exceptionInfo="已经有名称为" + group.getGroupName() + "的用户组了。";
				logger.info(exceptionInfo);
				return exceptionInfo;
			}
		}
		try 
		{
			DBUtil db = new DBUtil();
			//执行更新操作
			String sql="update TD_SM_Group t set t.GROUP_NAME='"+group.getGroupName()+"',t.GROUP_DESC='"+group.getGroupDesc()+"' where t.GROUP_ID="+ group.getGroupId();
			db.executeUpdate(sql);
			
			Event event = new EventImpl(String.valueOf(group.getGroupId()),
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			
			exceptionInfo="操作成功";
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return exceptionInfo;
	}
	
	/**
	 * 新增当前组下的角色集合（支持批量处理）
	 * @param groupId 组ID
	 * @param roleId 角色ID数组
	 */
	public boolean insertGroupRole(String groupId,String[] roleId) throws ManagerException 
	{
		boolean success=false;
		DBUtil db = new DBUtil();
		try 
		{
			
			int i=0,count=roleId.length;
			for(i=0;i<count;i++)
			{
				//执行更新操作
				String sql="insert into TD_SM_GROUPROLE(GROUP_ID,ROLE_ID) values(" + groupId + ", " + roleId[i] + ")";
				db.addBatch(sql);
			}
			db.executeBatch();
			
			//发组－角色变化事件
			//危达200711171459
			Event event = new EventImpl("",
					ACLEventType.GROUP_ROLE_INFO_CHANGE);
			super.change(event);
			
			success=true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			db.resetBatch();
		}
		return success;		
	}
	
	/**
	 * 删除当前组下的角色集合（支持批量处理）
	 * @param groupId 组ID
	 * @param roleIds 角色ID组合串（注：以","为分割符的组合字符串）
	 */
	public boolean deleteGroupRole(String groupId,String roleIds) throws ManagerException 
	{
		boolean success=false;
		try 
		{		
			DBUtil db = new DBUtil();
			DBUtil dbUnit = new DBUtil();
			dbUnit.executeDelete("delete from TD_SM_GROUPROLE  where GROUP_ID="+groupId+" and ROLE_ID in ("+roleIds+")");
			
//			发组－角色变化事件
			//危达200711171459
			Event event = new EventImpl("",
					ACLEventType.GROUP_ROLE_INFO_CHANGE);
			super.change(event);
			
			success=true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return success;	
	}
	
	/**
	 * 新增当前组下的用户集合（支持批量处理）
	 * @param groupId 组ID
	 * @param userIds 用户ID数组
	 */
	public boolean insertGroupUser(String groupId,String[] userId) throws ManagerException 
	{
		boolean success=false;
		DBUtil db = new DBUtil();
		try 
		{
			
			int i=0,count=userId.length;
			for(i=0;i<count;i++)
			{
				//执行新增操作
				String sql="insert into td_sm_usergroup(GROUP_ID,USER_ID) values(" + groupId + ", " + userId[i] + ")";
				db.addBatch(sql);
			}
			db.executeBatch();
			
			//user－角色变化事件
			//危达200711171500
			Event event = new EventImpl("",
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
			
			success=true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			db.resetBatch();
		}
		return success;		
	}
	/**
	 * 删除当前组下的用户集合（支持批量处理）
	 * @param groupId 组ID
	 * @param userIds 用户ID组合串（注：以","为分割符的组合字符串）
	 */
	public boolean deleteGroupUser(String groupId,String userIds) throws ManagerException 
	{
		boolean success=false;
		try 
		{		
			DBUtil dbUnit = new DBUtil();
			dbUnit.executeDelete("delete from td_sm_usergroup  where GROUP_ID="+groupId+" and USER_ID in ("+userIds+")");

//			user－角色变化事件
			//危达200711171500
			Event event = new EventImpl("",
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
			
			success=true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * 得到一个用户组对应的角色列表
	 * 
	 * @param groupId 用户组ID
	 * @return 
	 * @throws Exception
	 */
	public List getGroupRolesByGroupId(String groupId) throws Exception{
		List containRole = new ArrayList();
		Role role = null;
		String sql = "select t.* from td_sm_role t,td_sm_grouprole t1 where t1.ROLE_ID=t.ROLE_ID and t1.group_id="+groupId+" order by t.ROLE_ID";
		DBUtil dbUtil = new DBUtil();
		dbUtil.executeSelect(sql);
		int i=0,count=dbUtil.size();
		//角色列表加权限
		for (i = 0; i < count; i++) 
		{
			role = new Role();
			role.setRoleId(dbUtil.getString(i, "ROLE_ID"));
			role.setRoleName(dbUtil.getString(i, "ROLE_NAME"));
			containRole.add(role);
		}
		return containRole;
	}
	
	/**
	 * 得到某一机构的用户列表和隶属某一用户组的用户列表
	 * 
	 * 修改：用户列表变为用户实名+工号
	 * 
	 * @param groupId
	 *            用户组ID
	 * @param orgid
	 *            部门ID
	 * @param isRecursive
	 *                   
	 * @throws Exception
	 */
	public List getUserList(String groupId,String orgid,String isRecursive)throws Exception 
	{
		List allUser = new ArrayList();
		isRecursive = isRecursive==null?"":isRecursive;
		OrgManagerImpl orgManager=new OrgManagerImpl();
		Group group = this.getGroupByID(groupId);
		if("true".equalsIgnoreCase(isRecursive))
		{
				userList=new ArrayList();
				allUser = this.sortUser(orgid);
		}
		else
		{ 
				DBUtil db = new DBUtil();				
				String sql="select a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"+
		           "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"+
		           "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"+
		           "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,a.REMARK2,a.REMARK3,a.REMARK4,"+
		           "a.REMARK5,min(b.same_job_user_sn) aa,min(b.job_sn) bb "+
		            " from td_sm_user a, td_sm_userjoborg b "+
		            "where a.user_id = b.user_id and b.org_id ='"+orgid+"' "+
		            "group by a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"+
		            "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"+
		            "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"+
		            "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,"+
		            "a.REMARK2,a.REMARK3,a.REMARK4,a.REMARK5 "+
		            " order by bb asc,aa asc";
				 db.executeSelect(sql);
				 User user=null;
				 int i=0,count=db.size();
				 for(i=0;i<count;i++)
				 {
					 user=new User();
					 int userid=db.getInt(i,"user_id");
					 user.setUserId(new Integer(userid));
					 user.setUserName(db.getString(i,"USER_NAME"));
					 user.setUserRealname(db.getString(i,"USER_REALNAME"));
					 allUser.add(user);
				 }
		}
		//UserManagerImpl userManager = new UserManagerImpl();
		//List existUser = userManager.getUserList(group);
		//if (existUser == null)existUser = new ArrayList();
		//totalUserList.add(existUser);
		//totalUserList.add(allUser);
		return allUser;
	}
	
	private List sortUser(String parent_id) {

		DBUtil db_org = new DBUtil();
		
		try {
			// 取出直接下级机构，按jorg_sn,orgnumber排序
			db_org.executeSelect("select org_id from TD_SM_ORGANIZATION where parent_id='"
				+ parent_id + "' order by org_sn,orgnumber");

			for (int i = 0; i < db_org.size(); i++) {
				//
				String org_id = db_org.getString(i, "org_id");
				DBUtil db_user = new DBUtil();
				StringBuffer sb_user = new StringBuffer();
				sb_user.append("select b.user_id, b.user_name, b.user_realname, b.USER_HOMETEL,");
				sb_user.append("b.USER_WORKTEL, b.USER_MOBILETEL1, ");
				sb_user.append("b.USER_MOBILETEL2, b.USER_EMAIL from v_user_one_org_one_job a ");
				sb_user.append("inner join td_sm_user b on a.user_id = b.user_id ");
				sb_user.append("where org_id ='" + org_id + "' ");
				sb_user.append("order by job_sn,SAME_JOB_USER_SN");

				// 取出一个机构的用户，按job_sn,SAME_JOB_USER_SN排序
				db_user.executeSelect(sb_user.toString());
				//
				for (int j = 0; j < db_user.size(); j++) {
					User user = new User();
					user.setUserId(new Integer(db_user.getInt(j, "user_id")));
					user.setUserName(db_user.getString(j, "user_name"));
					user.setUserRealname(db_user.getString(j, "user_realname"));
					user.setUserHometel(db_user.getString(j, "USER_HOMETEL"));
					user.setUserWorktel(db_user.getString(j, "USER_WORKTEL"));
					user.setUserMobiletel1(db_user.getString(j,
							"USER_MOBILETEL1"));
					user.setUserMobiletel2(db_user.getString(j,
							"USER_MOBILETEL2"));
					user.setUserEmail(db_user.getString(j, "USER_EMAIL"));
					userList.add(user);
				}
				// 递归调用
				sortUser(org_id);
			}
		} catch (SQLException e) {
			return null;
		}
		return userList;
	}

//	public Group getGroup(String propName, String value)
//			throws ManagerException { // 根据名称取组
//		Group group = null;
//		try {
//			DBUtil db = new DBUtil();
//			db.executeSelect("select * from TD_SM_Group t where t." + propName + "='"
//							+ value+"'");
//			if(db.size()>0)
//			{
//				group=new Group();
//				group.setGroupId(db.getInt(0, "GROUP_ID"));
//				group.setGroupName(db.getString(0, "GROUP_NAME"));
//				group.setGroupDesc(db.getString(0, "GROUP_DESC"));
//				group.setRemark1(db.getString(0, "REMARK1"));
//				group.setRemark2(db.getString(0, "REMARK2"));
//				group.setRemark3(db.getString(0, "REMARK3"));
//				group.setRemark4(db.getString(0, "REMARK4"));
//				group.setRemark5(db.getString(0, "REMARK5"));
//				group.setParentId(db.getInt(0, "PARENT_ID"));
//			}
//		} catch (SQLException e) {
//			logger.error(e);
//		}
//		return group;
//	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getGroupList(Role role) throws ManagerException { // 根据角色取组
		List list = null;

//		if (role != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p.setObject("from Group g where g.groupId in ("
//								+ "select gr.id.groupId from Grouprole gr where gr.id.roleId = '"
//								+ role.getRoleId() + "')");
//
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				logger.error(e);
//			}
//		}
		DBUtil db = new DBUtil();
		String sql = "select * from td_sm_group where group_id in " +
				"(select group_id from td_sm_grouprole where role_id='"+role.getRoleId()+"')";
		try {
			db.executeSelect(sql);
			list = this.dbutilToGroupList(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 去掉hibernate后的方法
	 */
	public List getGroupList(User user) throws ManagerException { // 根据用户取组
		List list = null;

//		if (user != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from Group g where g.groupId in ("
//								+ "select ug.id.groupId from Usergroup ug where ug.id.userId = '"
//								+ user.getUserId() + "')");
//
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				logger.error(e);
//			}
//		}
//		String sql = "select * from td_sm_group where group_id in(select group_id from td_sm_usergroup "
//			+ "where user_id='" + user.getUserId() + "')";
		String sql = "select * from td_sm_group where group_id in(select group_id from td_sm_usergroup "
				+ "where user_id=?)";
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(sql);
			db.setInt(1, user.getUserId());
			list = db.executePreparedForList(Group.class, new RowHandler<Group>(){

				@Override
				public void handleRow(Group group, Record db)
						throws Exception {
				
					group.setGroupId(db.getInt(  "GROUP_ID"));
					group.setGroupName(db.getString(  "GROUP_NAME"));
					group.setGroupDesc(db.getString(  "GROUP_DESC"));
					group.setRemark1(db.getString(  "REMARK1"));
					group.setRemark2(db.getString(  "REMARK2"));
					group.setRemark3(db.getString(  "REMARK3"));
					group.setRemark4(db.getString(  "REMARK4"));
					group.setRemark5(db.getString(  "REMARK5"));
					group.setParentId(db.getInt(  "PARENT_ID"));
					group.setOwner_id(db.getInt(  "OWNER_ID"));
					
				}
				
			});
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}


	

	public List getGroupList() throws ManagerException { // 返回所有组实例
		List list = new ArrayList();
		try {
			String sql = "select * from td_sm_group t";
			DBUtil db = new DBUtil();
			db.executeSelect(sql);
			list = this.dbutilToGroupList(db);
		} catch (Exception e) {
			logger.error("",e);
		}
		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getGroupList(String propName, String value, boolean isLike)
			throws ManagerException {
		List list = null;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//
//			if (!isLike)
//				p.setObject("from Group g where g." + propName + " = '" + value
//						+ "'");
//			else
//				p.setObject("from Group g where g." + propName + " like '"
//						+ value + "'");
//
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//		}
		String sql = "";
		if(!isLike){
			sql = "select * from td_sm_group where " + propName + " = '" + value + "'";
		}else{
			sql = "select * from td_sm_group where " + propName + " like '" + value + "'";
		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = dbutilToGroupList(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean deleteGroup(String groupid) throws Exception { // 删除组

		boolean b = false;
//		DBUtil dbUtil1 = new DBUtil();
		
//		DBUtil dbUtil = new DBUtil();
		
		PreparedDBUtil p_1 = new PreparedDBUtil();
		
		PreparedDBUtil p_3 = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			
			//删除用户组资源授予记录
			//res_id的类型是varchar2，group_id的类型是Integer
//			DBUtil dbUtil0 = new DBUtil();
//			StringBuffer sb = new StringBuffer();
			StringBuffer sb_num = new StringBuffer();
			tm.begin();
//			String sql4 = "SELECT t.group_id FROM TD_SM_group t START WITH " +
//					"t.group_id=" + groupid + " CONNECT BY PRIOR t.group_id=t.PARENT_ID";
			String concat_ = DBUtil.getDBAdapter().concat(" group_tree_level","'|%' ");
			String sql4 = "SELECT t.group_id FROM TD_SM_group t where t.group_tree_level like (select "+
			             concat_ + 
                         " from TD_SM_GROUP c where c.group_id= '"
			+ groupid + "') or t.group_id ='" + groupid + "'";
//			System.out.println("sql4:"+sql4);
			p_1.executeSelect(sql4);
//			dbUtil0.executeSelect(sql4);
			
			if(p_1.size() <= 0)
			{
//				sb.append("'").append(groupid).append("'");
				sb_num.append(groupid);
			}
			else
			{
				
				
//				dbUtil1.setAutoCommit(false);
				for(int i=0;i<p_1.size();i++)
				{
					
					if(i == 0)
					{
//						sb.append("'").append(groupid).append("'")
//						  .append(",")
//						  .append("'").append(p_1.getInt(i, "group_id")).append("'");
						sb_num.append(groupid)
						      .append(",")
						      .append(p_1.getInt(i, "group_id"));
					}
					else
					{
//						sb.append(",").append(p_1.getInt(i, "group_id")).append("'");
						sb_num.append(",").append(p_1.getInt(i, "group_id"));
					}
					
				}
				
			}

			
			
//			String sql5 = "delete from TD_SM_roleresop where restype_id='group' and res_id in( " + sb.toString() +" )";
//			System.out.println(sb.toString());
			String sql ="delete from td_sm_group  where group_id in ( " + sb_num.toString() +" )";
		
			String sql1 ="delete from TD_SM_GROUPROLE  where group_id in (" + sb_num.toString() +" )";
			
			String sql2 ="delete from TD_SM_USERGROUP  where group_id in (" + sb_num.toString() +" )";
//			System.out.println("sql5:"+sql5);
//			System.out.println("sql:"+sql);
//			System.out.println("1:"+sql1);
//			System.out.println("2:"+sql2);
			
//			p_3.addBatch(sql5);		
			p_3.addBatch(sql1);
			p_3.addBatch(sql2);
			p_3.addBatch(sql);
			
			p_3.executeBatch();
			tm.commit();
			b = true;
			Event event = new EventImpl(groupid, ACLEventType.GROUP_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			throw new ManagerException(e.getMessage());			
		}finally{
			
		}
		return b;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用的方法，方法实现已被注释掉
	 */
	public boolean deleteGroupexp(Groupexp groupexp) throws ManagerException { // 删除组与动态组表达式的关系
		boolean r = false;

//		if (groupexp != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(groupexp);
//				if (cb.execute(p) != null)
//				{				   
//					 r = true;
//					 
//				}
//				Event event = new EventImpl("",
//						ACLEventType.GROUP_INFO_CHANGE);
//				super.change(event);
//				
//			} catch (ControlException e) {
//				logger.error(e);
//			}
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用的方法，方法实现已被注释掉
	 */
	public boolean isGroupExist(Group group) throws ManagerException { // 根据组名判断该组是否存在
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Group g where g.groupName='"
//					+ group.getGroupName() + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			logger.error(e);
//		}

		return r;
	}
	
	/**
	 * @deprecated 不推荐使用的方法，方法实现已被注释掉
	 */
	public void refresh(Group group) throws ManagerException { // 刷新指定组的状态以及该组与其它实体的关联
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public Group loadAssociatedSet(String groupId, String associated)
			throws ManagerException { // 装载指定组对象的关联对象
//		Group groupRel = null;

//		try {
//
//			Parameter par = new Parameter();
//			par.setCommand(Parameter.COMMAND_GET);
//			par.setObject("from Group g left join fetch g." + associated
//					+ " where g.groupId = '" + groupId + "'");
//
//			List list = (List) cb.execute(par);
//			if (list != null && !list.isEmpty()) {
//				groupRel = (Group) list.get(0);
//			}
//		} catch (ControlException e) {
//			logger.error(e);
//		}
		String sql = "select * from td_sm_grouprole where group_id='"+groupId+"'";
		DBUtil db = new DBUtil();
		Set grouproleSet = new HashSet();
		Grouprole grouprole = null;
		Group group = null;
		Role role = null;
		Group returnGroup = new Group();
		try {
			db.executeSelect(sql);
			for(int i = 0; i < db.size(); i++){
				grouprole = new Grouprole();
				group = new Group();
				role = new Role();
				group = getGroupByID(db.getString(i, "group_id"));
				role = new RoleManagerImpl().getRoleById(db.getString(i, "role_id"));
				grouprole.setGroup(group);
				grouprole.setRole(role);
				grouproleSet.add(grouprole);
			}
			returnGroup.setGrouproleSet(grouproleSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnGroup;
	}



	/**
	 * 去掉hibernate后的方法
	 */
	public boolean deleteGrouprole(Grouprole grouprole) throws ManagerException {
		boolean r = false;

//		if (grouprole != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(grouprole);
//
//				if (cb.execute(p) != null)
//				{
//					r = true;
//					
//				}
//				Event event = new EventImpl("",
//						ACLEventType.GROUP_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(grouprole != null){
			String sql = "delete from td_sm_grouprole where group_id='" + grouprole.getGroup().getGroupId() 
				+ "' and role_id='" + grouprole.getRole().getRoleId() + "'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete(sql);
				Event event = new EventImpl("",
						ACLEventType.GROUP_ROLE_INFO_CHANGE);
				super.change(event);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean storeGrouprole(Grouprole grouprole) throws ManagerException {
		boolean r = false;

//		if (grouprole != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(grouprole);
//
//				if (cb.execute(p) != null)
//				{
//					r = true;
//					
//				}
//				Event event = new EventImpl("",
//						ACLEventType.GROUP_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(grouprole != null){
			String sql = "insert into TD_SM_GROUPROLE(group_id,role_id) values('"+grouprole.getGroup().getGroupId()
				+ "','"+grouprole.getRole().getRoleId() + "')";
			DBUtil db = new DBUtil();
			try {
				db.executeInsert(sql);
				r = true;
				Event event = new EventImpl("",
						ACLEventType.GROUP_ROLE_INFO_CHANGE);
				super.change(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 改为sql语句执行
	 * 危达200711171510
	 */
	public boolean isContainChildGroup(Group group) throws ManagerException {// 检查指定的组是否包含子组
		boolean r = false;
		try {
			String sql = "select count(1) from td_sm_group g where g.parent_id = '" + group.getGroupId() + "'";
			DBUtil dBUtil = new DBUtil();
			dBUtil.executeSelect(sql);
			if(dBUtil.getInt(0, 0) >0){
				r = true;
			}
		} catch (Exception e) {
			logger.error("",e);
			throw new ManagerException(e.getMessage());
		}
		return r;
	}

	public List getChildGroupList(Group group) throws ManagerException {// 取指定组所包含的子组列表
		return this.getChildGroupList(group.getGroupId() + "");
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean deleteGrouprole(Group group) throws ManagerException {
		boolean r = false;

//		try {
//			// 删除当前组角色对象
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_DELETE);
//			p.setObject("from Grouprole gr where gr.id.groupId = '"
//					+ group.getGroupId() + "'");
//			if (cb.execute(p) != null)
//			{
//				r = true;
//				
//			}
//			Event event = new EventImpl("",
//					ACLEventType.GROUP_ROLE_INFO_CHANGE);
//			super.change(event);
//			
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "delete from td_sm_grouprole where group_id='" + group.getGroupId() + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeDelete(sql);
			r = true;
			Event event = new EventImpl("",
					ACLEventType.GROUP_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean deleteGrouprole(Role role) throws ManagerException {
		boolean r = false;

//		try {
//			// 删除当前组角色对象
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_DELETE);
//			p.setObject("from Grouprole gr where gr.id.roleId = '"
//					+ role.getRoleId() + "'");
//			if (cb.execute(p) != null)
//			{
//				r = true;
//				
//			}
//			Event event = new EventImpl("",
//					ACLEventType.GROUP_ROLE_INFO_CHANGE);
//			super.change(event);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "delete from td_sm_grouprole where role_id='" + role.getRoleId() + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeDelete(sql);
			r = true;
			Event event = new EventImpl("",
					ACLEventType.GROUP_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}	
	
	public boolean deleteGroup(Group group) throws Exception{
		return this.deleteGroup(group.getGroupId() + "");
	}
	
	public List getGroupList(String hql) throws ManagerException {
		List list = new ArrayList();
		try {
			DBUtil db = new DBUtil();
			db.executeSelect(hql);
			list = this.dbutilToGroupList(db);
		} catch (Exception e) {
			logger.error("",e);
		}
		return list;
	}
	 public List getChildGroupList(String groupid) throws ManagerException{
		 List list = new ArrayList();
			try {
				String sql = "select * from td_sm_group t where t.PARENT_ID='" + groupid + "'";
				DBUtil db = new DBUtil();
				db.executeSelect(sql);
				list = this.dbutilToGroupList(db);
			} catch (Exception e) {
				logger.error("",e);
			}
			return list;		 
	 }

	public Group getGroupByID(String groupid) throws ManagerException {
		Group group=null;
		try {
			DBUtil db = new DBUtil();
			db.executeSelect("select * from TD_SM_Group t where t.group_id='" + groupid+"'");
			if(db.size()>0)
			{
				group=new Group();
				group = this.dbutilToGrou(db);
			}
		} catch (SQLException e) {
			logger.error("",e);
		}
		return group;
	}

	public Group getGroupByName(String groupName) throws ManagerException {
		Group group=null;
		try {
			DBUtil db = new DBUtil();
			db.executeSelect("select * from TD_SM_Group t where t.group_name='" + groupName+"'");
			if(db.size()>0)
			{
				group = new Group();
				group = this.dbutilToGrou(db);
			}
		} catch (SQLException e) {
			logger.error("",e);
		}
		return group;
	}
	
	private Group dbutilToGrou(DBUtil db) throws SQLException
	{
		if(db.size()>0){
			Group group = new Group();					
			group.setGroupId(db.getInt(0, "GROUP_ID"));
			group.setGroupName(db.getString(0, "GROUP_NAME"));
			group.setGroupDesc(db.getString(0, "GROUP_DESC"));
			group.setRemark1(db.getString(0, "REMARK1"));
			group.setRemark2(db.getString(0, "REMARK2"));
			group.setRemark3(db.getString(0, "REMARK3"));
			group.setRemark4(db.getString(0, "REMARK4"));
			group.setRemark5(db.getString(0, "REMARK5"));
			group.setParentId(db.getInt(0, "PARENT_ID"));
			group.setOwner_id(db.getInt(0, "OWNER_ID"));
			return group;
		}
		return null;
	}


	public ListInfo getGroupList(String hql, long offset, int maxsize) 
		throws ManagerException  {
			List list = new ArrayList();
			ListInfo listInfo = new ListInfo();
			try {
				DBUtil dBUtil = new DBUtil();
				dBUtil.executeSelect(hql, offset, maxsize);
				list = this.dbutilToGroupList(dBUtil);
				listInfo.setDatas(list);
				listInfo.setTotalSize(dBUtil.getTotalSize());
			} catch (Exception e) {
				logger.error("",e);
			}
			return listInfo;
		}

	
	public List dbutilToGroupList(DBUtil db) throws SQLException
	{
		List list = new ArrayList();
		for(int i = 0; i < db.size(); i ++)
		{
			Group group = new Group();					
			group.setGroupId(db.getInt(i, "GROUP_ID"));
			group.setGroupName(db.getString(i, "GROUP_NAME"));
			group.setGroupDesc(db.getString(i, "GROUP_DESC"));
			group.setRemark1(db.getString(i, "REMARK1"));
			group.setRemark2(db.getString(i, "REMARK2"));
			group.setRemark3(db.getString(i, "REMARK3"));
			group.setRemark4(db.getString(i, "REMARK4"));
			group.setRemark5(db.getString(i, "REMARK5"));
			group.setParentId(db.getInt(i, "PARENT_ID"));
			group.setOwner_id(db.getInt(i, "OWNER_ID"));
			list.add(group);
		}
		return list;
	}

	public String saveGroup(Group group, String currentUserId)
			throws ManagerException {
	String exceptionInfo = "操作失败";
		
		// 创建新的用户组
		// 判断是否有相同的组名称
		Group g1 = getGroupByName(group.getGroupName());
		
		if (g1 != null) {
			exceptionInfo="已经有名称为" + group.getGroupName() + "的用户组了。";
			logger.info(exceptionInfo);
			return exceptionInfo;
		}
		
		
//		if( (parentId == ) || (parentId == "") || ("".equals(parentId)) )//机构的根结点id必须保证是0
//		{
//			parentId = "0";
//		}
		
		
		try 
		{
			int parentId = group.getParentId();
			
			PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
			String groupId = preparedDBUtil.getNextStringPrimaryKey("TD_SM_Group");
			String grouprreeLevel = getGroupTreeLevel(String.valueOf(parentId), String.valueOf(groupId));
			//执行新增操作
//			String sql="insert into TD_SM_Group(GROUP_NAME,GROUP_DESC,PARENT_ID,OWNER_ID) values('"+group.getGroupName()+"','"+group.getGroupDesc()+"',"+group.getParentId()+"," + group.getOwner_id() + ")";
//			String groupid = db.executeInsert(sql).toString();			
			String sql="insert into TD_SM_Group(Group_id,GROUP_NAME,GROUP_DESC,PARENT_ID,OWNER_ID,GROUP_TREE_LEVEL) values(?,?,?,?,?,?)";
			preparedDBUtil.preparedInsert(sql);
			
			preparedDBUtil.setString(1, groupId);
			preparedDBUtil.setString(2, group.getGroupName());
			preparedDBUtil.setString(3, group.getGroupDesc());
			preparedDBUtil.setInt(4, group.getParentId());
			preparedDBUtil.setInt(5, group.getOwner_id());	
			preparedDBUtil.setString(6, grouprreeLevel);
			
			preparedDBUtil.executePrepared();
			
	    	Event event = new EventImpl(groupId,
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			
	    	exceptionInfo = "操作成功";
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return exceptionInfo;
	}
	
//	public static void main(String[] args){
//		User user = new User();
//		user.setUserId(new Integer(39));
//		try {
//			List list = new GroupManagerImpl().getGroupList(user);
//			for(int i = 0; i < list.size(); i ++){
//				Group u = (Group)list.get(i);
//				System.out.println("groupName = " + u.getGroupName());
//			}
//		} catch (ManagerException e) {
//			e.printStackTrace();
//		}
//	}
}