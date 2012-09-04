package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.ApplicationContext;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.config.ResourceInfoQueue;
import com.frameworkset.platform.config.model.OperationGroup;
import com.frameworkset.platform.config.model.OperationQueue;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ExcludeResource;
import com.frameworkset.platform.resource.ExcludeResourceQueue;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.resource.UNProtectedResource;
import com.frameworkset.platform.resource.UNProtectedResourceQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.control.DataControl;
import com.frameworkset.platform.sysmgrcore.control.PageConfig;
import com.frameworkset.platform.sysmgrcore.control.Parameter;
import com.frameworkset.platform.sysmgrcore.entity.Attrdesc;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Permission;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.ResOpOriginObj;
import com.frameworkset.platform.sysmgrcore.entity.Restype;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ControlException;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：资源管理接口 <br>
 * 版本：1.0 <br>
 * --没有被使用的类
 * @author 潘伟林
 */
public class ResManagerImpl extends EventHandle implements ResManager {

	private static Logger logger = Logger.getLogger(ResManagerImpl.class
			.getName());

	private DataControl cb = DataControl
			.getInstance(DataControl.CONTROL_INSTANCE_DB);
	
	public static SQLUtil sqlUtilInsert = SQLUtil
	.getInstance("org/frameworkset/insert.xml");

	/**
	 * 存储资源类型
	 * 没有用的方法
	 * @param resType
	 * @return boolean
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public boolean storeResType(Restype resType) throws ManagerException {
		boolean r = false;

//		if (resType != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(resType);
//
//				if (cb.execute(p) != null)
//					r = true;
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 存储资源
	 * 
	 * 去掉hibernate后的方法
	 * 
	 * @param res
	 * @return boolean
	 */
	public boolean storeRes(Res res) throws ManagerException {
		boolean r = false;

//		if (res != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(res);
//
//				if (cb.execute(p) != null)
//					r = true;
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}
//		DBUtil db = new DBUtil();
		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
		try {
			String resId = preparedDBUtil.getNextStringPrimaryKey("td_sm_res");
			StringBuffer sql = new StringBuffer()
				.append("insert into td_sm_res(res_id,RESTYPE_ID,TITLE,ROLE_USAGE,PARENT_ID,PATH,MARKER,RESERVED1,")
				.append("RESERVED3,RESERVED4,RESERVED5,ATTR1,ATTR2,ATTR3,ATTR4,ATTR5,ATTR6,ATTR7,ATTR8,ATTR9,")
				.append("ATTR10,ATTR11,ATTR12,ATTR13,ATTR14,ATTR15,ATTR16,ATTR17,ATTR18,ATTR19,ATTR20,ATTR21,")
				.append("ATTR22,ATTR23,ATTR24,ATTR25,ATTR26,ATTR27) values(")
				.append("?,?,?,?,?,?,?,?,?,?,")
				.append("?,?,?,?,?,?,?,?,?,?,")
				.append("?,?,?,?,?,?,?,?,?,?,")
				.append("?,?,?,?,?,?,?,?)");
			preparedDBUtil.preparedInsert(sql.toString());
			
			preparedDBUtil.setString(1, resId);
			preparedDBUtil.setString(2, res.getRestypeId());
			preparedDBUtil.setString(3, res.getTitle());
			preparedDBUtil.setString(4, res.getRoleUsage());
			preparedDBUtil.setString(5, res.getParentId());
			preparedDBUtil.setString(6, res.getPath());
			preparedDBUtil.setString(7, res.getMarker());
			preparedDBUtil.setString(8, res.getReserved1());
			preparedDBUtil.setString(9, res.getReserved3());
			preparedDBUtil.setString(10, res.getReserved4());
			
			preparedDBUtil.setString(11, res.getReserved5());
			preparedDBUtil.setString(12, res.getAttr1());
			preparedDBUtil.setString(13, res.getAttr2());
			preparedDBUtil.setString(14, res.getAttr3());
			preparedDBUtil.setString(15, res.getAttr4());
			preparedDBUtil.setString(16, res.getAttr5());
			preparedDBUtil.setString(17, res.getAttr6());
			preparedDBUtil.setString(18, res.getAttr7());
			preparedDBUtil.setString(19, res.getAttr8());
			preparedDBUtil.setString(20, res.getAttr9());
			
			preparedDBUtil.setString(21, res.getAttr10());
			preparedDBUtil.setString(22, res.getAttr11());
			preparedDBUtil.setString(23, res.getAttr12());
			preparedDBUtil.setString(24, res.getAttr13());
			preparedDBUtil.setString(25, res.getAttr14());
			preparedDBUtil.setString(26, res.getAttr15());
			preparedDBUtil.setString(27, res.getAttr16());
			preparedDBUtil.setString(28, res.getAttr17());
			preparedDBUtil.setString(29, res.getAttr18());
			preparedDBUtil.setString(30, res.getAttr19());
			
			preparedDBUtil.setString(31, res.getAttr20());
			preparedDBUtil.setString(32, res.getAttr21());
			preparedDBUtil.setString(33, res.getAttr22());
			preparedDBUtil.setString(34, res.getAttr23());
			preparedDBUtil.setString(35, res.getAttr24());
			preparedDBUtil.setString(36, res.getAttr25());
			preparedDBUtil.setString(37, res.getAttr26());
			preparedDBUtil.setString(38, res.getAttr27());
			
//				.append(resId).append("','")
//				.append(res.getRestypeId()==null?"":res.getRestypeId().trim()).append("','")
//				.append(res.getTitle()==null?"":res.getTitle().trim()).append("','")
//				.append(res.getRoleUsage()==null?"":res.getRoleUsage().trim()).append("','")
//				.append(res.getParentId()==null?"":res.getParentId().trim()).append("','")
//				.append(res.getPath()==null?"":res.getPath().trim()).append("','")
//				.append(res.getMarker()==null?"":res.getMarker().trim()).append("','")
//				.append(res.getReserved1()==null?"":res.getReserved1().trim()).append("','")
//				.append(res.getReserved3()==null?"":res.getReserved3().trim()).append("','")
//				.append(res.getReserved4()==null?"":res.getReserved4().trim()).append("','")
//				.append(res.getReserved5()==null?"":res.getReserved5().trim()).append("','")
//				.append(res.getAttr1()==null?"":res.getAttr1().trim()).append("','")
//				.append(res.getAttr2()==null?"":res.getAttr2().trim()).append("','")
//				.append(res.getAttr3()==null?"":res.getAttr3().trim()).append("','")
//				.append(res.getAttr4()==null?"":res.getAttr4().trim()).append("','")
//				.append(res.getAttr5()==null?"":res.getAttr5().trim()).append("','")
//				.append(res.getAttr6()==null?"":res.getAttr6().trim()).append("','")
//				.append(res.getAttr7()==null?"":res.getAttr7().trim()).append("','")
//				.append(res.getAttr8()==null?"":res.getAttr8().trim()).append("','")
//				.append(res.getAttr9()==null?"":res.getAttr9().trim()).append("','")
//				.append(res.getAttr10()==null?"":res.getAttr10().trim()).append("','")
//				.append(res.getAttr11()==null?"":res.getAttr11().trim()).append("','")
//				.append(res.getAttr12()==null?"":res.getAttr12().trim()).append("','")
//				.append(res.getAttr13()==null?"":res.getAttr13().trim()).append("','")
//				.append(res.getAttr14()==null?"":res.getAttr14().trim()).append("','")
//				.append(res.getAttr15()==null?"":res.getAttr15().trim()).append("','")
//				.append(res.getAttr16()==null?"":res.getAttr16().trim()).append("','")
//				.append(res.getAttr17()==null?"":res.getAttr17().trim()).append("','")
//				.append(res.getAttr18()==null?"":res.getAttr18().trim()).append("','")
//				.append(res.getAttr19()==null?"":res.getAttr19().trim()).append("','")
//				.append(res.getAttr20()==null?"":res.getAttr20().trim()).append("','")
//				.append(res.getAttr21()==null?"":res.getAttr21().trim()).append("','")
//				.append(res.getAttr22()==null?"":res.getAttr22().trim()).append("','")
//				.append(res.getAttr23()==null?"":res.getAttr23().trim()).append("','")
//				.append(res.getAttr24()==null?"":res.getAttr24().trim()).append("','")
//				.append(res.getAttr25()==null?"":res.getAttr25().trim()).append("','")
//				.append(res.getAttr26()==null?"":res.getAttr26().trim()).append("','")
//				.append(res.getAttr27()==null?"":res.getAttr27().trim()).append("')");
			preparedDBUtil.executePrepared();
			r = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		return r;
	}
	
	public boolean updateRes(String resId, String path, String title){
		boolean state = false;
		String sql = "update td_sm_res set TITLE='" + title + "',PATH='" + path + "' "
			+ "where RES_ID ='" + resId + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeUpdate(sql);
			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	/**
	 * 存储属性描述
	 * 
	 * @param attrdesc
	 * @return boolean
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public boolean storeAttrdesc(Attrdesc attrdesc) throws ManagerException {
		boolean r = false;

//		if (attrdesc != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(attrdesc);
//
//				if (cb.execute(p) != null)
//					r = true;
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 取所有属性描述
	 * 
	 * @return List
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getAttrdescList() throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Attrdesc");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 根据属性描述对象的属性和值来取用户实例 来取值
	 * 
	 * @param propName
	 * @param value
	 * @return List
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getAttrdescList(String propName, String value)
			throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Attrdesc a where a." + propName + "='" + value
//					+ "'");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 取所有资源
	 * 没有被使用的方法
	 * @return List
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getResList() throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Res res order by res.restypeId");
//			list = (List) cb.execute(p);
//
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	public List getResList(String sql) throws ManagerException {
//		Parameter p = new Parameter();
//		p.setCommand(Parameter.COMMAND_GET);
//		p.setObject(hql);
//
		List list = new ArrayList();
//
//		try {
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = getResList(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List getResList(DBUtil db){
		List list = new ArrayList();
		Res res = null;
		ResourceManager rm = new ResourceManager();	
		for(int i = 0; i < db.size(); i++){
			res = new Res();
			try {
				res.setResId(db.getString(i, "RES_ID"));
				res.setRestypeId(db.getString(i, "RESTYPE_ID"));
				res.setTitle(db.getString(i, "TITLE"));
				res.setRoleUsage(db.getString(i, "ROLE_USAGE"));
				res.setParentId(db.getString(i, "PARENT_ID"));
				res.setPath(db.getString(i, "PATH"));
				res.setMarker(db.getString(i, "MARKER"));
				res.setReserved1(db.getString(i, "RESERVED1"));
				res.setReserved3(db.getString(i, "RESERVED3"));
				res.setReserved4(db.getString(i, "RESERVED4"));
				res.setReserved5(db.getString(i, "RESERVED5"));
				res.setAttr1(db.getString(i, "ATTR1"));
				res.setAttr2(db.getString(i, "ATTR2"));
				res.setAttr3(db.getString(i, "ATTR3"));
				res.setAttr4(db.getString(i, "ATTR4"));
				res.setAttr5(db.getString(i, "ATTR5"));
				res.setAttr6(db.getString(i, "ATTR6"));
				res.setAttr7(db.getString(i, "ATTR7"));
				res.setAttr8(db.getString(i, "ATTR8"));
				res.setAttr9(db.getString(i, "ATTR9"));
				res.setAttr10(db.getString(i, "ATTR10"));
				res.setAttr11(db.getString(i, "ATTR11"));
				res.setAttr12(db.getString(i, "ATTR12"));
				res.setAttr13(db.getString(i, "ATTR13"));
				res.setAttr14(db.getString(i, "ATTR14"));
				res.setAttr15(db.getString(i, "ATTR15"));
				res.setAttr16(db.getString(i, "ATTR16"));
				res.setAttr17(db.getString(i, "ATTR17"));
				res.setAttr18(db.getString(i, "ATTR18"));
				res.setAttr19(db.getString(i, "ATTR19"));
				res.setAttr20(db.getString(i, "ATTR20"));
				res.setAttr21(db.getString(i, "ATTR21"));
				res.setAttr22(db.getString(i, "ATTR22"));
				res.setAttr23(db.getString(i, "ATTR23"));
				res.setAttr24(db.getString(i, "ATTR24"));
				res.setAttr25(db.getString(i, "ATTR25"));
				res.setAttr26(db.getString(i, "ATTR26"));
				res.setAttr27(db.getString(i, "ATTR27"));
				try{
					res.setRestypeName(rm.getResourceInfoByType(db.getString(i, "RESTYPE_ID")).getName());
				}catch(Exception e){
			    	res.setRestypeName("未知");
			    }
				list.add(res);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public Res getRes(DBUtil db){
		Res res =  new Res();
		ResourceManager rm = new ResourceManager();	
		try {
			res.setResId(db.getString(0, "RES_ID"));
			res.setRestypeId(db.getString(0, "RESTYPE_ID"));
			res.setTitle(db.getString(0, "TITLE"));
			res.setRoleUsage(db.getString(0, "ROLE_USAGE"));
			res.setParentId(db.getString(0, "PARENT_ID"));
			res.setPath(db.getString(0, "PATH"));
			res.setMarker(db.getString(0, "MARKER"));
			res.setReserved1(db.getString(0, "RESERVED1"));
			res.setReserved3(db.getString(0, "RESERVED3"));
			res.setReserved4(db.getString(0, "RESERVED4"));
			res.setReserved5(db.getString(0, "RESERVED5"));
			res.setAttr1(db.getString(0, "ATTR1"));
			res.setAttr2(db.getString(0, "ATTR2"));
			res.setAttr3(db.getString(0, "ATTR3"));
			res.setAttr4(db.getString(0, "ATTR4"));
			res.setAttr5(db.getString(0, "ATTR5"));
			res.setAttr6(db.getString(0, "ATTR6"));
			res.setAttr7(db.getString(0, "ATTR7"));
			res.setAttr8(db.getString(0, "ATTR8"));
			res.setAttr9(db.getString(0, "ATTR9"));
			res.setAttr10(db.getString(0, "ATTR10"));
			res.setAttr11(db.getString(0, "ATTR11"));
			res.setAttr12(db.getString(0, "ATTR12"));
			res.setAttr13(db.getString(0, "ATTR13"));
			res.setAttr14(db.getString(0, "ATTR14"));
			res.setAttr15(db.getString(0, "ATTR15"));
			res.setAttr16(db.getString(0, "ATTR16"));
			res.setAttr17(db.getString(0, "ATTR17"));
			res.setAttr18(db.getString(0, "ATTR18"));
			res.setAttr19(db.getString(0, "ATTR19"));
			res.setAttr20(db.getString(0, "ATTR20"));
			res.setAttr21(db.getString(0, "ATTR21"));
			res.setAttr22(db.getString(0, "ATTR22"));
			res.setAttr23(db.getString(0, "ATTR23"));
			res.setAttr24(db.getString(0, "ATTR24"));
			res.setAttr25(db.getString(0, "ATTR25"));
			res.setAttr26(db.getString(0, "ATTR26"));
			res.setAttr27(db.getString(0, "ATTR27"));
			try{
				res.setRestypeName(rm.getResourceInfoByType(db.getString(0, "RESTYPE_ID")).getName());
			}catch(Exception e){
		    	res.setRestypeName("未知");
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Res getRes(String propName, String value) throws ManagerException {
		Res res = new Res();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Res r where r." + propName + "='" + value + "'");
//			List resList = (List) cb.execute(p);
//
//			if (resList != null && !resList.isEmpty()) {
//				res = (Res) resList.get(0);
//			}
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
//
//		return res;
		String sql = "select * from td_sm_res where " + propName + "='" + value + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			res = getRes(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 根据操作取资源
	 * 没有被使用的方法
	 * @param oper
	 * @return List
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getResList(Operation oper) throws ManagerException {
		List list =  new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Res r where r.resId in ("
//							+ "select rro.id.resId from Roleresop rro where rro.id.opId = '"
//							+ oper.getOpId() + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 根据角色取资源
	 * 没有被使用的方法
	 * @param role
	 * @return List
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getResList(Role role) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Res r where r.resId in ("
//							+ "select rro.id.resId from Roleresop rro where rro.id.roleId = '"
//							+ role.getRoleId() + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 根据用户取资源
	 * 没有被使用的方法
	 * @param user
	 * @return List
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getResList(User user) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Res r where r.resId in ("
//							+ "select ur.id.resId from Userrole ur where ur.id.userId = '"
//							+ user.getUserId() + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 根据属性说明取资源类型
	 * 
	 * @param attrdesc
	 * @return com.frameworkset.platform.sysmgrcore.entity.Restype
	 */
	public Restype getResType(Attrdesc attrdesc) throws ManagerException {
		Restype restype = attrdesc.getRestype();
		return restype;
	}

	/**
	 * 根据资源取资源类型
	 * 没有被使用的方法
	 * @param res
	 * @return com.frameworkset.platform.sysmgrcore.entity.Restype
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public Restype getResType(Res res) throws ManagerException {
//		Restype restype = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Restype r where r.restypeId='"
//					+ res.getRestypeId() + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null)
//				restype = (Restype) list.get(0);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
//
//		return restype;
		return null;
	}

	/**
	 * 根据名称取资源类型
	 * 无效的方法
	 * @param resTypeName
	 * @return com.frameworkset.platform.sysmgrcore.entity.Restype
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public Restype getResType(String resTypeName) throws ManagerException {
//		Restype restype = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Restype r where r.restypeName='" + resTypeName
//					+ "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null)
//				restype = (Restype) list.get(0);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
//
//		return restype;
		return null;
	}

	/**
	 * 取所有资源类型
	 * 去掉hibernate后的方法
	 * @return List
	 */
	public List getResTypeList() throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Res");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select * from td_sm_res";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = getResList(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 无效的方法
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public List getChildResTypeList(Restype resType) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Restype rt where rt.parentRestypeId='"
//					+ resType.getRestypeId() + "'");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getChildResList(Res res) throws ManagerException {
		List list = new ArrayList();

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Res r where r.restypeId = '" + res.getRestypeId()
//					+ "'");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select * from td_sm_res where RESTYPE_ID='"+res.getRestypeId()+"'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = getResList(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 删除资源类型。当前删除支持级联删除。
	 * 无效的方法
	 * @param resType
	 * @return boolean
	 * @deprecated 不推荐使用该方法，方法实现已被注释掉
	 */
	public boolean deleteResType(Restype resType) throws ManagerException {
		boolean r = false;

//		if (resType != null) {
//			try {
//				// 级联操作
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				// 删除指定的资源实例
//				p.setObject("from Restype rt where rt.restypeId = '"
//						+ resType.getRestypeId() + "'");
//				// p.setObject(resType);
//				if (cb.execute(p) != null) {
//					r = true;
//				}
//
//				Event event = new EventImpl("",
//						ACLEventType.RESOURCE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 删除资源
	 * 
	 * @param res
	 * @return boolean
	 */
	public boolean deleteRes(Res res) throws ManagerException {
		boolean r = false;

//		if (res != null) {
//			try {
//				// 删除当前资源所关联的 Roleresop 对象
//				cb.setAutoCommit(false);
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Roleresop rro where rro.id.resId = '"
//						+ res.getResId() + "'");
//				cb.execute(p);
//
//				// 删除当前资源所关联的 Userresop 对象
//				// p.setObject("from Userresop uro where uro.id.resId = '"
//				// + res.getResId() + "'");
//				// cb.execute(p);
//
//				// 删除指定的资源实例
//				p.setObject(res);
//				// p.setObject("from Res r where r.resId = '" + res.getResId()
//				// + "'");
//				if (cb.execute(p) != null) {
//					r = true;
//					cb.commit(true);
//				}
//
//				// 触发资源删除事件
//				// String ress[] = new String[] { res.getTitle(),
//				// res.getRestype().getRestypeName() };
//				Event event = new EventImpl("",
//						ACLEventType.RESOURCE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				try {
//					cb.rollback(true);
//				} catch (ControlException e1) {
//					logger.error(e1);
//				}
//				throw new ManagerException(e.getMessage());
//			}
//		}
		TransactionManager tm = new TransactionManager();
		String delRoleresopsql = "delete from td_sm_roleresop where res_id='" + res.getResId() + "'";
		String delResParentsql = "delete from TD_SM_RES where  PARENT_ID ='" + res.getResId() + "'";
		String delRessql = "delete from td_sm_res where res_id='" + res.getResId() + "'";
		DBUtil db = new DBUtil();
		try {
			tm.begin();
			db.addBatch(delRoleresopsql);
			db.addBatch(delResParentsql);
			db.addBatch(delRessql);
			db.executeBatch();
			tm.commit();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
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
		
		return r;
	}
	
	/**
	 * 批量删除
	 * @param resIds
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteRes(String[] resIds) throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			if(resIds != null && resIds.length > 0){
				tm.begin();
				for(int i = 0; i < resIds.length; i ++ ){
					String delRoleresopsql = "delete from td_sm_roleresop where res_id='" + resIds[i] + "'";
					String delResParentsql = "delete from TD_SM_RES where  PARENT_ID ='" + resIds[i] + "'";
					String delRessql = "delete from td_sm_res where res_id='" + resIds[i] + "'";
					db.addBatch(delRoleresopsql);
					db.addBatch(delResParentsql);
					db.addBatch(delRessql);
				}
				db.executeBatch();
				tm.commit();
			}
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_INFO_CHANGE);
			super.change(event);
			r = true;
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
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
		
		return r;
	}
	
	/**
	 * 批量删除
	 * @param resIds
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteResandAuth(String[] resIds) throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		
//		StringBuffer delResIds = new StringBuffer();
//		boolean isFirst = false;
//		
//		
//		if(resIds != null && resIds.length > 0)
//		{
//			PreparedDBUtil pe = new PreparedDBUtil();
//			for(int j = 0; j < resIds.length; j ++ )
//			{
//				String sql = "select ATTR26 from td_sm_res where RES_ID ='" + resIds[j] + "'";
//				try {
//					pe.preparedSelect(sql);
//					pe.executePrepared();
//					if (Integer.parseInt(pe.getString(0, 0)) == 0) {
//						
//						if(!isFirst)
//						{
//							delResIds.append("'").append(resIds[j]).append("'");
//							isFirst = true;
//						}
//						else
//						{
//							delResIds.append(",").append("'").append(resIds[j]).append("'");
//						}
//						
//					
//					}
//					
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//			
//			
//		}
//	
//		
		TransactionManager tm = new TransactionManager();
		try {
//			tm.begin();
//			
//			if(isFirst)
//			{
//				String delRoleresopsql = "delete from td_sm_roleresop where (res_id,restype_id) in (select title,restype_id from td_sm_res where res_id in (" + delResIds.toString() + "))";
//				String delResParentsql = "delete from TD_SM_RES where  PARENT_ID in (" + delResIds.toString() + ")";
//				String delRessql = "delete from td_sm_res where res_id in (" + delResIds.toString() + ")";
//				db.addBatch(delRoleresopsql);
//				db.addBatch(delResParentsql);
//				db.addBatch(delRessql);
//			}
//			
//			db.executeBatch();
//			tm.commit();
//			
			if(resIds != null && resIds.length > 0){
				
				tm.begin();
				for(int i = 0; i < resIds.length; i ++ ){
					String delRoleresopsql = "delete from td_sm_roleresop where (res_id,restype_id) in (select title,restype_id from td_sm_res where res_id='" + resIds[i] + "' and ATTR26='0')";
					String delResParentsql = "delete from TD_SM_RES where  PARENT_ID ='" + resIds[i] + "' and ATTR26='0'";
					String delRessql = "delete from td_sm_res where res_id='" + resIds[i] + "' and ATTR26='0'";
					
					db.addBatch(delRoleresopsql);
					db.addBatch(delResParentsql);
					db.addBatch(delRessql);
				}
				db.executeBatch();
				tm.commit();
			}
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_INFO_CHANGE);
			super.change(event);
			r = true;
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
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
		
		return r;
	}

	/**
	 * 删除指定的属性描述
	 * 
	 * @param attrdesc
	 * @return boolean
	 * @deprecated 不推荐使用该方法，该方法由hibernate实现
	 */
	public boolean deleteAttrdesc(Attrdesc attrdesc) throws ManagerException {
		boolean r = false;

		if (attrdesc != null) {
			try {
				// 删除当前资源所关联的 Roleresop 对象
				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_DELETE);
				// 删除指定的资源实例
				p.setObject(attrdesc);
				if (cb.execute(p) != null) {
					r = true;
				}
				Event event = new EventImpl("",
						ACLEventType.RESOURCE_INFO_CHANGE);
				super.change(event);
			} catch (ControlException e) {
				logger.error(e);
				throw new ManagerException(e.getMessage());
			}
		}

		return r;
	}

	/**
	 * @deprecated 不推荐使用该方法，该方法由hibernate实现
	 */
	public Res loadAssociatedSet(String resId, String associated)
			throws ManagerException {
		Res res = null;

		try {
			Parameter par = new Parameter();
			par.setCommand(Parameter.COMMAND_GET);
			par.setObject("from Res r left join fetch r." + associated
					+ " where r.resId = '" + resId + "'");

			List list = (List) cb.execute(par);
			if (list != null && !list.isEmpty()) {
				res = (Res) list.get(0);
			}
		} catch (ControlException e) {
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return res;
	}

	/**
	 * @deprecated 不推荐使用该方法，该方法由hibernate实现
	 */
	public boolean isResExist(String resId) throws ManagerException {
		boolean r = false;

		try {
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);
			p.setObject("from Res r where r.resId='" + resId + "'");
			List list = (List) cb.execute(p);

			if (list != null && list.size() > 0)
				r = true;
		} catch (ControlException e) {
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean isResExistitle(String title) throws ManagerException {
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Res r where r.title='" + title + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select count(1) from td_sm_res where title='" + title + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.getInt(0, 0) > 0){
				r = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;

	}

	/**
	 * @deprecated 不推荐使用该方法，该方法由hibernate实现
	 */
	public boolean isContainChildRes(Res res) throws ManagerException {
		boolean r = false;

		try {
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);
			p.setObject("select count(*) from Res r where r.restypeId = '"
					+ res.getRestypeId() + "'");
			List list = (List) cb.execute(p);
			if (list != null) {
				if (!list.isEmpty()) {
					int count = ((Integer) list.get(0)).intValue();
					if (count > 0)
						r = true;
				}
			} else
				throw new ManagerException("由于意外错误，暂时无法计算出资源“" + res.getResId()
						+ "”是否包含子资源");
		} catch (ControlException e) {
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	/**
	 * @deprecated 不推荐使用该方法，该方法由hibernate实现
	 */
	public boolean isContainChildResType(Restype restype)
			throws ManagerException {
		boolean r = false;

		try {
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);
			p
					.setObject("select count(*) from Restype r where r.parentRestypeId = '"
							+ restype.getRestypeId() + "'");
			List list = (List) cb.execute(p);
			if (list != null) {
				if (!list.isEmpty()) {
					int count = ((Integer) list.get(0)).intValue();
					if (count > 0)
						r = true;
				}
			} else
				throw new ManagerException("由于意外错误，暂时无法计算出资源“"
						+ restype.getRestypeId() + "”是否包含子资源类型");
		} catch (ControlException e) {
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	/**
	 * @deprecated 不推荐使用该方法，该方法由hibernate实现
	 */
	public PageConfig getPageConfig() throws ManagerException {
		try {
			return cb.getPageConfig();
		} catch (ControlException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public List getResList(String roleId,String restypeId) throws
	// ManagerException{
	// List list = null;
	// try {
	// Parameter p = new Parameter();
	// p.setCommand(Parameter.COMMAND_GET);
	// // p
	// // .setObject(" from Roleresop rro where rro.id.roleId = '"
	// // + roleId + "' and rro.id.restypeId='" + restypeId + "'");
	// p
	// .setObject(" from Roleresop rro");
	// list = (List) cb.execute(p);
	// } catch (ControlException e) {
	// logger.error(e);
	// throw new ManagerException(e.getMessage());
	// }
	//
	// return list;
	// }

	public ListInfo getResList(String roleId, String restypeId, long offset,
			int maxItems) throws ManagerException {
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		String sql = "select roleresop0_.OP_ID as OP_ID, "
				+ "roleresop0_.RES_ID as RES_ID, "
				+ "roleresop0_.ROLE_ID as ROLE_ID, "
				+ "roleresop0_.RESTYPE_ID as RESTYPE_ID, "
				+ "roleresop0_.RES_NAME as RES_NAME, "
				+ "roleresop0_.auto as auto "
				+ "from td_sm_roleresop roleresop0_ where "
				+ " roleresop0_.ROLE_ID='" + roleId
				+ "' and roleresop0_.RESTYPE_ID='" + restypeId + "'";
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(sql, offset, maxItems);
			for (int i = 0; i < dbUtil.size(); i++) {
				Roleresop roo = new Roleresop();
				roo.setAuto(dbUtil.getString(i, "auto"));
				roo.setOpId(dbUtil.getString(i, "OP_ID"));
				roo.setResId(dbUtil.getString(i, "RES_ID"));
				roo.setRoleId(dbUtil.getString(i, "ROLE_ID"));
				roo.setRestypeId(dbUtil.getString(i, "RESTYPE_ID"));
				roo.setResName(dbUtil.getString(i, "RES_NAME"));
				list.add(roo);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ManagerException(e.getMessage());
		}
		return listInfo;
	}

	public boolean deleteRoleResOp(String opId, String resId, String roleId,
			String resTypeId, String types) throws ManagerException {
		DBUtil db = new DBUtil();
		boolean state = false;
		StringBuffer hsql = new StringBuffer(
				"delete from td_sm_roleresop t where " + "t.op_id = '" + opId
						+ "' " + "and t.res_id = '" + resId + "' "
						+ "and t.role_id = '" + roleId + "' "
						+ "and t.restype_id = '" + resTypeId + "' "
						+ "and t.types = '" + types + "'");
		try {
			db.executeDelete(hsql.toString());
			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	/**
	 * 获取资源列表 2007.10.22 chunqiu.zhao
	 */ 
	public ListInfo getResList(Map paramMap, long offset,
			int maxItems) {
		String type = (String)paramMap.get("type");
		String id = (String)paramMap.get("id");
		String name = (String)paramMap.get("name");
		String resId = (String)paramMap.get("resId");
		String resName = (String)paramMap.get("resName");
		String restypeId = (String)paramMap.get("restypeId");
		String opId = (String)paramMap.get("opId");
		String auto = (String)paramMap.get("auto");
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		ResourceManager rsManager = new ResourceManager();
		StringBuffer hsql = new StringBuffer();
		DB dbconcat = DBUtil.getDBAdapter();

		// 查用户资源
		if ("user".equals(type)) {
			
//			StringBuffer roleres = new StringBuffer(
//					"select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id,concat(concat('来自用户自身角色【',b.role_name), '】') as jobname,a.AUTHORIZATION_STIME,a.auto  ");
			String concat = dbconcat.concat("'来自用户自身角色【'","b.role_name","'】'");
			String concat1 = dbconcat.concat("'来自【'","o.remark5","'】机构的角色【'","b.role_name","'】'");
			String concat2 = dbconcat.concat("'来自【'","g.group_name","'】用户组角色【'","b.role_name","'】'");
			String concat3 = dbconcat.concat("'来自岗位【'","infos.job_name","'】的角色【'","b.role_name","'】'");
			String concat4 = dbconcat.concat("'来自机构【'","b.org_name","'】'");
			String concat5 = dbconcat.concat("'普通角色:'","b.role_name");
			
			StringBuffer roleres = new StringBuffer(
			"select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id,").append(concat).append(" as jobname,a.AUTHORIZATION_STIME,a.auto  ");
			 roleres.append(" from td_sm_roleresop a,td_sm_role b where a.types='role' and a.role_id=b.role_id and a.role_id in")
					.append(" (select  b.role_id")
					.append(" from td_sm_userrole b")
					.append(" where b.user_id= '")
					.append(id)
					.append("') union")
					.append(" select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id,").append(concat1).append(" as jobname,a.AUTHORIZATION_STIME,a.auto ")
					.append(" from td_sm_orgrole c left join td_sm_organization o on c.org_id=o.org_id,td_sm_roleresop a,td_sm_role b ")
					.append(" where a.role_id=c.role_id and a.types='role' and c.role_id=b.role_id and c.org_id in (")
					.append("select  org_id from td_sm_userjoborg where user_id ='")
					.append(id)
					.append("') union ")
					.append(" select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id,").append(concat2).append(" as jobname,a.AUTHORIZATION_STIME,a.auto ")
					.append(" from td_sm_grouprole d left join td_sm_group g on d.group_id=g.group_id,td_sm_roleresop a,td_sm_role b where a.role_id=d.role_id and d.role_id=b.role_id and a.types='role' and d.group_id in (")
					.append(" select  group_id from td_sm_usergroup  where user_id = '")
					.append(id)
					.append("') union ")
					
//					.append(" select org_job_role.role_id from td_sm_userjoborg u_job ")
//					.append("inner join td_sm_orgjobrole org_job_role ")
//					.append("on u_job.job_id=org_job_role.job_id and u_job.org_id=org_job_role.org_id and u_job.user_id='")
//					.append(id)
//					.append("'")
//					.append(")");
					.append("select a.restype_id, a.res_id, a.res_name, a.op_id, a.types,a.role_id ,").append(concat3).append(" as jobname,a.AUTHORIZATION_STIME,a.auto ")
					.append("from (select aa.role_id, aa.job_id, job.job_name from td_sm_orgjobrole aa , td_sm_userjoborg bb ,")
					.append("  td_sm_job job ")
					.append("where aa.org_id=bb.org_id and aa.job_id=bb.job_id and job.job_id=bb.job_id ")
					.append("and bb.user_id=").append(id)
					.append(" )infos , td_sm_roleresop a, td_sm_role b where a.role_id=infos.role_id and a.types='role' and a.role_id=b.role_id ");

			StringBuffer userres = new StringBuffer(
					"select a.restype_id,a.res_id,a.res_name,a.op_id,a.types, a.role_id,'用户自身资源' as jobname,a.AUTHORIZATION_STIME,a.auto ");
			userres.append(	" from td_sm_roleresop a where a.types='user' and a.role_id='")
					.append(id).append("'");

			StringBuffer userorgres = new StringBuffer(
					"select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types, a.role_id,").append(concat4).append(" as jobname,a.AUTHORIZATION_STIME,a.auto ");
			userorgres.append(" from td_sm_roleresop a left join td_sm_organization b on a.role_id = b.org_id  where a.types='organization' and a.role_id in (")
					.append("select  org_id from td_sm_userjoborg where user_id ='")
					.append(id).append("')  ");

			StringBuffer commonuserres = new StringBuffer(
					"select   a.restype_id,a.res_id,a.res_name,a.op_id,a.types, a.role_id, ").append(concat5).append(" as jobname,a.AUTHORIZATION_STIME,a.auto");
			commonuserres.append(
							" from td_sm_roleresop a inner join td_sm_role b on a.role_id = b.role_id where b.role_name='")
					.append(AccessControl.getEveryonegrantedRoleName()).append(
							"' and a.types = 'role' ");

			hsql.append("select  * from (").append(roleres).append(" union ")
					.append(userres).append(" union ").append(userorgres)
					.append(" union ").append(commonuserres).append(") e where 1 = 1 ");
		}
		// 查机构资源
		else if ("org".equals(type)) {
			String concat6 = dbconcat.concat("'来自机构角色【'","b.role_name","'】'");
			hsql.append("select * from ")
				.append(" (select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id,").append(concat6).append(" AS jobname,a.AUTHORIZATION_STIME,a.auto ")
				.append(" from td_sm_roleresop a,td_sm_role b where a.types='role' and a.role_id in(")
				.append(" select role_id from td_sm_orgrole where org_id='")
				.append(id)
				.append("') and a.role_id=b.role_id " +
						"union  " +
						"select  a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id")
				.append(",'机构自身拥有的资源' AS jobname,a.AUTHORIZATION_STIME,a.auto from td_sm_roleresop a left join td_sm_organization b on a.role_id=b.org_id where 1 = 1 and a.types='organization' and a.role_id='")
				.append(id).append("') e where 1 = 1 ");
		}
		// 查角色资源
		else if ("role".equals(type)) {
//			hsql.append("select  distinct a.restype_id,a.res_id,a.res_name,a.op_id,a.types,a.role_id,a.AUTHORIZATION_STIME,'角色自身资源' as jobname,a.auto ")
//				.append(" from td_sm_roleresop a where 1 = 1 and a.types='role' and a.role_id='")
//				.append(id).append("'");
			hsql.append("select  distinct e.restype_id,e.res_id,e.res_name,e.op_id,e.types,e.role_id,e.AUTHORIZATION_STIME,'角色自身资源' as jobname,e.auto ")
			.append(" from td_sm_roleresop e where 1 = 1 and e.types='role' and e.role_id='")
			.append(id).append("'");
		}
		//查询机构岗位资源
		else if("orgjob".equals(type))
			
		{
			String concat7 = dbconcat.concat("'来自角色【'","r.role_name","'】'");
			String orgid = id.substring(0, id.indexOf(":"));
			String jobid = id.substring(id.indexOf(":") + 1, id.length());
			hsql.append("select a.*,").append(concat7).append(" as jobname from ( select rp.restype_id,rp.res_id,rp.res_name,rp.op_id,rp.types,rp.role_id,rp.AUTHORIZATION_STIME,rp.auto from td_sm_roleresop rp ")
				.append("inner join td_sm_orgjobrole ojr ")
				.append("on rp.role_id = ojr.role_id ")
				.append("where ojr.org_id='" + orgid + "' and ojr.job_id='" + jobid + "' and rp.types='role' )")
				.append(" a left join  td_sm_role r on a.role_id = r.role_id ")
				.append(" where 1 = 1 ");
		}

//		boolean flag = false;
		if (restypeId != null && restypeId.length() > 0) {

			hsql.append(" and restype_id = '" + restypeId + "'");
//			flag = true;
		}
		if (resId != null && resId.length() > 0) {
//			if (flag) {
				hsql.append(" and res_id like '%" + resId + "%'");
//			} else {
//				hsql.append(" where res_id like '%" + resId + "%'");
//				flag = true;
//			}
		}
		if (resName != null && resName.length() > 0) {
//			if (flag) {
				hsql.append(" and res_name like '%" + resName + "%'");
//			} else {
//				hsql.append(" where res_name like '%" + resName + "%'");
//				flag = true;
//			}
		}
		if (opId != null && opId.length() > 0) {
//			if (flag) {
				hsql.append(" and op_id = '" + opId + "'");
//			} else {
//				hsql.append(" where op_id = '" + opId + "'");
//				flag = true;
//			}
		}
		if(auto != null && !"".equals(auto)){
			if("0".equals(auto)){//查询出系统资源，系统资源包含auto字段值为0和为null
				hsql.append(" and (auto = '0' or auto is null) ");
			}else{//查询自定义资源，auto字段值为1
				hsql.append(" and auto = '").append(auto).append("' ");
			}
			
		}
		if("orgjob".equals(type)){
			hsql.append(" order by restype_id,a.role_id,res_id,res_name,op_id ");
		}else{
			hsql.append(" order by e.restype_id,e.role_id,e.res_id,e.res_name,e.op_id ");
		}
//	System.out.println("hsql = " + hsql.toString());
		try {
			if(offset == 0 && maxItems == 0){//不翻页

				dbUtil.executeSelect(hsql.toString());
			}else{

				dbUtil.executeSelect(hsql.toString(), (int) offset, maxItems);
			}
			
			for (int i = 0; i < dbUtil.size(); i++) {
				Permission permission = new Permission();
				permission.setId(id);
				permission.setResId(resId);
				permission.setResName(resName);
				permission.setType(type);
				permission.setName(name);
				permission.setAuto(dbUtil.getInt(i, "AUTO"));
				
				String restypeid = dbUtil.getString(i, "restype_id");
				String opid = dbUtil.getString(i, "op_id");
				String resid = dbUtil.getString(i, "res_id");
				String resname = dbUtil.getString(i, "res_name");
				String types = dbUtil.getString(i, "types");
				permission.setSDate(dbUtil.getDate(i, "AUTHORIZATION_STIME"));
				String resResource = "";
				if ("user".equals(type) || "org".equals(type)){ 
					resResource = dbUtil.getString(i, "jobname");
				}
//					if(resResource.equals("0"))
//					{
//						resResource = new String("来自用户自身资源");
//					}
				if(restypeid == null)
				{
					restypeid = "";
				}
				if(opid == null)
				{
					opid = "";
				}
				if(resid == null)
				{
					resid = "";
				}
				if(resname == null)
				{
					resname = "";
				}
				
				permission.setResTypeId(restypeid);
				permission.setOpId(opid);
				permission.setResId(resid);
				permission.setResName(resname);
//				permission.setTypes(types);
				permission.setResResource(resResource);
//				permission.setRoleId(dbUtil.getString(i, "role_id"));
				
				
				try{
					permission.setResTypeName(rsManager.getResourceInfoByType(restypeid).getName());
				}catch(Exception e){
					permission.setResTypeName("未知");
				}

				try{
					com.frameworkset.platform.config.model.Operation operation  = null;
					try {
						String gresid  = rsManager.getGlobalResourceid(dbUtil.getString(i,"restype_id"));
						if(gresid == null || !gresid.equals(dbUtil.getString(i,"RES_ID")))
						{
							operation  = rsManager.getOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
							 
						}
						else
						{
							operation  = rsManager.getGlobalOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
						}
					} catch (ConfigException e) {
						e.printStackTrace();
					}
					permission.setOpName(operation.getName());
				}catch(Exception e){
					permission.setOpName("未知");
				}

				list.add(permission);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listInfo;
	}

	// 删除冗余数据 2007.10.22 chunqiu.zhao
	// 删除 冗余用户的 资源
	// 删除 冗余角色的 资源
	// 删除 冗余机构的 资源
	public boolean delRedundance(String type) throws ManagerException {
		// //删除冗余用户 的 用户角色关系
		// String sql1 = "delete from td_sm_userrole a where a.user_id in( " +
		// " select user_id from td_sm_userrole " +
		// " minus" +
		// " select user_id from td_sm_user )" ;
		// //删除冗余角色 的 用户角色关系
		// String sql2 = "delete from td_sm_userrole b where b.role_id in(" +
		// " select role_id from td_sm_userrole " +
		// " minus" +
		// " select role_id from td_sm_role )";
		// String sql5 = "delete from td_sm_orgrole e where e.org_id in(" +
		// " select org_id from td_sm_orgrole" +
		// " minus" +
		// " select org_id from td_sm_organization)";
		// String sql6 = "delete from td_sm_orgrole f where f.role_id in(" +
		// " select role_id from td_sm_orgrole " +
		// " minus" +
		// " select role_id from td_sm_role)";

		// 删除资源-------------------------------------------------------------------------------
		// 删除直接分配给 冗余用户 的资源
//		String sql3 = "delete from td_sm_roleresop c where c.types='user' and c.role_id in("
//				+ " select role_id from td_sm_roleresop where types='user'"
//				+ " minus" + " select to_char(user_id) from td_sm_user)";
//		// 删除分配给 冗余角色 的资源
//		String sql4 = "delete from td_sm_roleresop d "
//			+ " where d.types='role' and d.role_id in" + "("
//			+ "select role_id from td_sm_roleresop where types='role' "
//			+ " minus" + " select role_id from td_sm_role " + ")";
//
//
//		// 删除分配给 冗余机构 的资源
//		String sql7 = "delete from td_sm_roleresop g where types='organization' and g.role_id in("
//				+ " select role_id from td_sm_roleresop where types='organization'"
//				+ " minus" + " select org_id from td_sm_organization)";
		 
		StringBuffer sql3 = new StringBuffer().append("delete from td_sm_roleresop  where types='user' and role_id in")
		.append("( select role_id from td_sm_roleresop c where types='user' and not exists (")
		.append("select user_id from td_sm_user where user_id = c.role_id))");
				
		// 删除分配给 冗余角色 的资源
		
		StringBuffer sql4 = new StringBuffer().append("delete from td_sm_roleresop  where types='role' and role_id in")
		.append(" (select a.role_id from td_sm_roleresop  a  where types='role' and not exists (")
				.append("select b.role_id from td_sm_role b where a.role_id=b.role_id )) ");
				
//				+ " where types='role' and role_id in" + "("
//				+ "select a.role_id from td_sm_roleresop  a  where types='role' and not exists ( "
//				+ " 	select b.role_id from td_sm_role b where a.role_id=b.role_id ) " + ")";

		// 删除分配给 冗余机构 的资源
	
		StringBuffer sql7 = new StringBuffer().append("delete from td_sm_roleresop  where types='organization' and role_id in")
		.append(" (select a.role_id from td_sm_roleresop a where types='organization' and not exists (")
		.append("select b.org_id from td_sm_organization b where a.role_id=b.org_id ))");
//				+ " select a.role_id from td_sm_roleresop a where types='organization' and not exists ("
//				+  " select b.org_id from td_sm_organization b where a.role_id=b.org_id ))";
		
		PreparedDBUtil pb = new PreparedDBUtil();
		try {
			pb.addBatch(sql3.toString());
			pb.addBatch(sql4.toString());
			pb.addBatch(sql7.toString());
			delOperation();
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		
		return true;
	}
	
	public void delOperation() throws SQLException
	{
		ResourceManager resourceManag = new ResourceManager();
    	ResourceInfoQueue queue = resourceManag.getResourceInfoQueue();
    	
    	PreparedDBUtil pe = new PreparedDBUtil();
    	boolean set = false;
    	StringBuffer sb_resource = new StringBuffer().append("delete from td_sm_roleresop where restype_id not in ( ");
    	for(int i=0;i<queue.size();i++)
    	{
    		
        	StringBuffer sb_origine = new StringBuffer();
        	
    		ResourceInfo resourceinfo = queue.getResourceInfo(i);
    		String id = resourceinfo.getId();
    		if(!set)
    		{
    			sb_resource.append("'").append(id).append("'");
    			set = true;
    		}
    		else
    		{
    			sb_resource.append(",'").append(id).append("'");
    		}
    		
    		OperationGroup opgroup = resourceinfo.getOperationGroup();

    		OperationQueue opqueue = opgroup.getOperationQueue();
    		boolean flag = false;
    		for(int j=0;j<opqueue.size();j++)
    		{
    			com.frameworkset.platform.config.model.Operation op = opqueue.getOperation(j);
    			if(!flag )
    			{
	    			
	    			sb_origine.append("'").append(op.getId()).append("'");
	    			flag = true;
    			}
    			else
    			{
    				sb_origine.append(",'").append(op.getId()).append("'");
    			}
    			
    		}
    		
    		opqueue = resourceinfo.getGlobalOperationQueue();
    		
    		for(int j=0;opqueue != null && j<opqueue.size();j++)
    		{
    			com.frameworkset.platform.config.model.Operation op = opqueue.getOperation(j);
    			if(!flag )
    			{
	    			
	    			sb_origine.append("'").append(op.getId()).append("'");
	    			flag = true;
    			}
    			else
    			{
    				sb_origine.append(",'").append(op.getId()).append("'");
    			}
    		}
    		
    		if(flag)
    		{
	    		String sql_del = "delete from td_sm_roleresop where restype_id='"+id+"' and op_id not in("+sb_origine.toString()+")";    		
				pe.preparedDelete(sql_del);
    		}
    		else
    		{
    			String sql_del = "delete from td_sm_roleresop where restype_id='"+id+"'";    		
				pe.preparedDelete(sql_del);
    		}
    		
			pe.addPreparedBatch();
			
    		
    	}
    	if(set)
    	{
    		sb_resource.append(")");
    		pe.preparedDelete(sb_resource.toString());
    	}
    	else
    	{
    		pe.preparedDelete("delete from td_sm_roleresop");
    		
    	}
    	pe.addPreparedBatch();
    	pe.executePreparedBatch();
    	
	}

	public ListInfo getJobRoleList(String jobId, String resName, String resid,
			String resTypeId, String opId, long offset, int maxPagesize) {
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		
		StringBuffer hsql = new StringBuffer("select t.role_id,t.op_id,t.res_id,r.role_name,t.restype_id,t.res_name,t.types from "
				+ "td_sm_roleresop t inner join td_sm_role r on t.role_id = r.role_id where t.role_id "
				+ "in ( select jr.role_id from td_sm_orgjobrole jr where jr.job_id = '"
				+ jobId + "') and t.types = 'role' ");

		if(resName != null && !resName.equals("")){
			hsql.append(" and t.res_name like '%" + resName +"%' ");
		}
		if(resid != null && !resid.equals("")){
			hsql.append(" and t.res_id like '%" + resid +"%' ");
		}
		if(resTypeId != null && resTypeId.length() >0){
			hsql.append(" and t.restype_id = '" + resTypeId +"' ");
		}
		if(opId != null && opId.length() > 0){
			hsql.append(" and t.op_id = '" + opId +"' ");
		}
		hsql.append(" order by r.role_name");
		
		ResourceManager rsManager = new ResourceManager();	
		try {
			
			db.executeSelect(hsql.toString(), (int)offset, maxPagesize);
			for(int i = 0; i < db.size(); i++){
				String typeName = "";
				ResourceInfo resourceInfo = rsManager.getResourceInfoByType(db.getString(i,"restype_id"));
				if(resourceInfo != null)
					typeName = resourceInfo.getName();		
				else
				{
					typeName = "未知"; 
				}
				String opName = "";
				com.frameworkset.platform.config.model.Operation operation  = rsManager.getOperation(db.getString(i,"restype_id"),db.getString(i, "op_id"));
				if(operation != null)
				{
				    opName = operation.getName();
				}
				else
				{
					opName = "未知";
				}
				
				Permission p = new Permission();
				p.setOpId(db.getString(i, "op_id"));
				p.setResTypeId(db.getString(i, "restype_id"));
				p.setTypes(db.getString(i, "types"));
				p.setRoleId(db.getString(i, "role_id"));
				p.setResName(db.getString(i, "res_name"));
				p.setResId(db.getString(i, "res_id"));
				p.setResTypeName(typeName);
				p.setOpName(opName);
				p.setRoleName(db.getString(i, "role_name"));
				list.add(p);
				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}
	
/**
 * 查询系统未受保护资源  2007.10.26  chunqiu.zhao
 * 
 */	
	public ListInfo getUnprotectedRes(String restypeId,String resId,String resName,String opId){
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		

		ResourceInfoQueue queue = ConfigManager.getInstance().getResourceInfoQueue();
		for(int i = 0;i < queue.size();i++){
			ResourceInfo res = queue.getResourceInfo(i);
			UNProtectedResourceQueue unProtectedResourceQueue = res.getUNProtectedResourceQueue();
			if(unProtectedResourceQueue.size()==0){
				System.out.println("has no UNProtectedResource:"+res.getName());
			}
			for (int j = 0; j < unProtectedResourceQueue.size(); j++)
			{   				
				UNProtectedResource unProtectedResource = unProtectedResourceQueue
						.getUNProtectedResource(j);
				Permission permission = new Permission();
				permission.setSpecial("unprotected");
				permission.setResName("");
				permission.setResId(unProtectedResource.getResourceID());
				permission.setResTypeName(unProtectedResource.getResouceInfo().getName());
				permission.setResTypeId(unProtectedResource.getResouceInfo().getId());
				OperationQueue opts = unProtectedResource.getUnprotectedoperations();
				StringBuffer nameBuffer = new StringBuffer();
				StringBuffer idBuffer = new StringBuffer();
				for(int n = 0;n < opts.size();n++){
					nameBuffer.append(opts.getOperation(n).getName());
					idBuffer.append(opts.getOperation(n).getId());
					if(n != opts.size()-1){
						nameBuffer.append(" | ");
						idBuffer.append(" | ");
					}
				}
				permission.setOpName(nameBuffer.toString());
				permission.setOpId(idBuffer.toString());
				if (restypeId != null && restypeId.trim().length() > 0){	
					if (!permission.getResTypeId().trim().equalsIgnoreCase(restypeId.trim())){
						continue;						
					}
				}
				if(resId != null && resId.length()>0){
					if (!(permission.getResId().indexOf(resId)>=0)) {
						continue;
					}
				}
				if(resName != null && resName.length()>0){
					if(!(permission.getResName().indexOf(resName)>=0)){	
						continue;
					}
				}
				if(opId != null && opId.length()>0){
					if(!(permission.getOpId().indexOf(opId)>=0)){
						continue;
					}
				}				
				list.add(permission);
			}
		}		
		listInfo.setDatas(list);
		listInfo.setTotalSize(list.size());
		
		return listInfo;
	}
	
	/**
	 * 查询系统中只有超级用户才有的资源   2007.10.26  chunqiu.zhao
	 */
	public ListInfo getAdminRes(String restypeId,String resId,String resName,String opId){
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();

		ResourceInfoQueue queue = ConfigManager.getInstance().getResourceInfoQueue();
		for(int i = 0;i < queue.size();i++){
			ResourceInfo res = queue.getResourceInfo(i);
			ExcludeResourceQueue excludeResourceQueue = res.getExcludeResources();
			for (int j = 0; j < excludeResourceQueue.size(); j++)
			{
				ExcludeResource excludeResource = excludeResourceQueue
						.getExcludeResource(j);
				Permission permission = new Permission();
				permission.setSpecial("exclude");
				permission.setResName("");
				permission.setResId(excludeResource.getResourceID());
				permission.setResTypeName(excludeResource.getResouceInfo().getName());
				permission.setResTypeId(excludeResource.getResouceInfo().getId());
				OperationQueue opts = excludeResource.getExcludeoperations();
				StringBuffer nameBuffer = new StringBuffer();
				StringBuffer idBuffer = new StringBuffer();
				for(int n = 0;n < opts.size();n++){
					nameBuffer.append(opts.getOperation(n).getName());
					idBuffer.append(opts.getOperation(n).getId());
					if(n != opts.size()-1){
						nameBuffer.append(" | ");
						idBuffer.append(" | ");
					}
				}
				permission.setOpName(nameBuffer.toString());
				permission.setOpName(idBuffer.toString());
				
				if (restypeId != null && restypeId.trim().length() > 0){	
					if (!permission.getResTypeId().trim().equalsIgnoreCase(restypeId.trim())){
						continue;						
					}
				}
				if(resId != null && resId.length()>0){
					if (!(permission.getResId().indexOf(resId)>0)) {
						continue;
					}
				}
				if(resName != null && resName.length()>0){
					if(!(permission.getResName().indexOf(resName)>0)){	
						continue;
					}
				}
				if(opId != null && opId.length()>0){
					if(!(permission.getOpId().indexOf(opId)>0)){
						continue;
					}
				}
				list.add(permission);
			}
		}
		

		listInfo.setDatas(list);
		listInfo.setTotalSize(list.size());
		
		return listInfo;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.ResManager#getResOpOriginId(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 2008-01-02
	 * 根据资源的信息, 获取授权人该资源来源 来源的类型和ID等信息
	 * @return List<ResOpOriginObj>
	 */
	public List getResOpOriginInfo(String restypeId, String resId, String opId,  String roleId, String types ) throws ManagerException {
		List list = null;
		if(restypeId==null || resId==null ||  opId==null) return list;
		list = new ArrayList();
		DBUtil db = new DBUtil();
		StringBuffer belongs_sql = new StringBuffer();
		belongs_sql.append("select origine_id,origine_type from TD_SM_PERMISSION_ORIGINE where OP_ID=")
		                .append("'").append(opId).append("' and RES_ID=")
		                .append("'").append(resId).append("' and RESTYPE_ID=")
		                .append("'").append(restypeId).append("' and ROLE_ID=")
		                .append("'").append(roleId).append("' and types=")		                
		                .append("'").append(types).append("' ");		
		try {
			db.executeSelect(belongs_sql.toString());
			//构造ResOpOriginObj对象的属性
			for(int i=0;i<db.size();i++){
				ResOpOriginObj resOpOriginObj = new ResOpOriginObj();
				resOpOriginObj.setOrigineId(db.getString(i,"origine_id"));
				resOpOriginObj.setOrigineType(db.getString(i,"origine_type"));
				list.add(resOpOriginObj);
			}			
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public boolean hasOtherOrigine(String resId,
								   String opId,
								   String restypeId,
								   String roleid,
								   String roleType,
								   String currOrigine,
								   String currOrigineType) throws ManagerException{
		
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append("select count(OP_ID) ")
			.append("from TD_SM_PERMISSION_ORIGINE where ")
			.append("OP_ID='").append(opId).append("' ")
			.append("and RES_ID='").append(resId).append("' ")
			.append("and RESTYPE_ID='").append(restypeId).append("' ")
			.append("and TYPES='").append(roleType).append("' ")
			.append("and ORIGINE_ID <>'").append(currOrigine).append("' ")
			.append("and ORIGINE_TYPE <>'").append(currOrigineType).append("' ");

		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			return db.getInt(0,0) > 0; 
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.ResManager#reclaimResOp()
	 * 资源操作回收, 递归
	 * 资源来源记录表 TD_SM_PERMISSION_ORIGINE
	 */
	public void reclaimResOp(DBUtil deleteDB,
			                 String restypeId, 
							 String resId, 
							 String opId, 
			                 String originId,
			                 String origineType) throws ManagerException {

//		该分派资源是否还分派给了其他对象
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append("select OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,TYPES,ORIGINE_TYPE,ORIGINE_ID,ORIGINE_TYPE ")
			.append("from TD_SM_PERMISSION_ORIGINE where ")
			.append("OP_ID='").append(opId).append("' ")
			.append("and RES_ID='").append(resId).append("' ")
			.append("and RESTYPE_ID='").append(restypeId).append("' ")
			.append("and ORIGINE_ID='").append(originId).append("' ")
			.append("and ORIGINE_TYPE='").append(origineType).append("' ");

		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//递归保存符合条件的记录, 批量执行删除
		List deletePermissionList = new ArrayList();
		Permission permission_ = new Permission();
		permission_.setOpId(opId);
		permission_.setResTypeId(restypeId);
		permission_.setResId(resId);
		permission_.setTypes(origineType);
		permission_.setRoleId(originId);
		deletePermissionList.add(permission_);
		List deleteOrigineList = new ArrayList();
		if(db.size()>0){//该资源还分派给了其他对象, 继续递归,找接受对象 	
			try {				
				for(int i=0;i<db.size();i++){
					String restypeId_ = db.getString(i, "RESTYPE_ID");
					String resId_ = db.getString(i, "RES_ID");
					String opId_ = db.getString(i, "OP_ID");
					String roleType  = db.getString(i, "TYPES");
					String role_id = db.getString(i, "ROLE_ID");//把被分派的对象, 作为下次递归的分派对象
					String origineId_ = db.getString(i,"ORIGINE_ID");
					String origineType_ = db.getString(i,"ORIGINE_TYPE");
					ResOpOriginObj resorigine = new ResOpOriginObj();
					resorigine.setOpId(opId_);
					resorigine.setResTypeId(restypeId_);
					resorigine.setResId(resId_);
					resorigine.setRoleType(roleType);
					resorigine.setRoleId(role_id);
					resorigine.setOrigineId(origineId_);
					resorigine.setOrigineType(origineType_);
					deleteOrigineList.add(resorigine);//删除来源
					//判断是否还有其他的来源,有的话停止查找,不需要回收被授予人的权限,否则回收并继续执行递归查找和递归回收
					if(hasOtherOrigine(resId_,opId_,
										restypeId_,role_id,
										roleType,
										originId,origineType)){
						continue;
					}else{
						
						//递归
						reclaimResOp(deleteDB,restypeId_,  resId_ ,  opId_,role_id, roleType);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			//找不到其他分派,递归结束,开始删除 
			//堆栈, 后进先出,			
			//先删除分派关系.
			StringBuffer delete_sql = new StringBuffer();			
			for(int i = 0;  i < deletePermissionList.size(); i ++){
				Permission permission = (Permission)deletePermissionList.get(i);
				delete_sql.append("delete TD_SM_ROLERESOP where ")
				.append("OP_ID='").append(permission.getOpId()).append("' ")
				.append("and RES_ID='").append(permission.getResId()).append("' ")
				.append("and RESTYPE_ID='").append(permission.getResTypeId()).append("' ")
				.append("and TYPES='").append(permission.getTypes()).append("' ")
				.append("and ROLE_ID='").append(permission.getRoleId()).append("' ");
				deleteDB.addBatch(delete_sql.toString());
				delete_sql.setLength(0);				
			}
			
			for(int i = 0;  i < deleteOrigineList.size(); i ++){
				ResOpOriginObj resorigine = (ResOpOriginObj)deletePermissionList.get(i);
				delete_sql.append("delete TD_SM_PERMISSION_ORIGINE where ")
				.append("OP_ID='").append(resorigine.getOpId()).append("' ")
				.append("and RES_ID='").append(resorigine.getResId()).append("' ")
				.append("and RESTYPE_ID='").append(resorigine.getResTypeId()).append("' ")
				.append("and TYPES='").append(resorigine.getRoleType()).append("' ")
				.append("and Role_ID='").append(resorigine.getRoleId()).append("' ")
				.append("and ORIGINE_TYPE='").append(resorigine.getOrigineType()).append("' ")
				.append("and ORIGINE_ID='").append(resorigine.getOrigineId()).append("' ");
				deleteDB.addBatch(delete_sql.toString());
				delete_sql.setLength(0);				
			}				
	        //到外面去执行批量处理
			//deleteDB.executeBatch();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.ResManager#reclaimUserRole(java.lang.String, java.lang.String)
	 * 角色回收.
	 * 角色类型 用户角色(td_sm_userrole),用户组角色(td_sm_grouprole),机构岗位角色(td_sm_orgjobrole)
	 */
	public boolean reclaimUserRole(String roleId, String userId,String roleTypes) throws ManagerException {
		if(roleId == null || userId == null ) return false;
//		if(roleTypes.equalsIgnoreCase(ROLE_TYPE_USER)){//用户角色 TD_SM_USERROLE
//			StringBuffer sql = new StringBuffer();
//			sql.append("delete from TD_SM_USERROLE t where t.resop_origin_userid in ")
//			    .append("(")
//			    .append("select resop_origin_userid from TD_SM_USERROLE ")
//			    .append("start with t.resop_origin_userid=")
//			    .append("'").append(userId).append("' ")
//			    .append("connect by prior t.resop_origin_userid = t.user_id ")
//			    .append(")")
//			    .append(" and t.role_id=")
//			    .append("'").append(roleId).append("' ");
//			DBUtil db = new DBUtil();
//			try {
//				db.executeDelete(sql.toString());
//				return true;
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else if(roleTypes.equalsIgnoreCase(ROLE_TYPE_USERGROUP)){//用户组角色 TD_SM_GROUPROLE
//			StringBuffer sql = new StringBuffer();
//			sql.append("delete from TD_SM_GROUPROLE t where t.resop_origin_userid in ")
//		    	.append("(")
//			    .append("select resop_origin_userid from TD_SM_GROUPROLE ")
//			    .append("start with t.resop_origin_userid=")
//			    .append("'").append(userId).append("' ")
//			    .append("connect by prior t.resop_origin_userid = t.user_id ")
//			    .append(")")
//			    .append(" and t.role_id=")
//			    .append("'").append(roleId).append("' ");
//			DBUtil db = new DBUtil();
//			try {
//				db.executeDelete(sql.toString());
//				return true;
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.ResManager#reclaimOrgJobRole(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 机构岗位角色角色回收,递归回收 (td_sm_orgjobrole)
	 */
	public boolean reclaimOrgJobRole(String orgId, String jobId, String roleId, String userId) throws ManagerException {		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from TD_SM_ORGJOBROLE t where t.resop_origin_userid in ")
	    	.append("(")
		    .append("select resop_origin_userid from TD_SM_ORGJOBROLE ")
		    .append("start with t.resop_origin_userid=")
		    .append("'").append(userId).append("' ")
		    .append("connect by prior t.resop_origin_userid = t.user_id ")
		    .append(")")
		    .append(" and t.role_id=")
		    .append("'").append(roleId).append("' ")
		    .append(" and t.org_id=")
		    .append("'").append(orgId).append("' ");
		DBUtil db = new DBUtil();
		try {
			db.executeDelete(sql.toString());
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.ResManager#saveResOpOrigin(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 保存被授予资源 的来源 来源包括: 资源来源类型, 资源来源的对象ID
	 */
	public boolean saveResOpOrigin(String restypeId, 
			                    String resId, 
			                    String opId, 
			                    String roleId, 
			                    String roleType, 
			                    String userId 
			                    ) throws ManagerException {
		StringBuffer sql = new StringBuffer();		
		List list = this.getResOpOriginInfo(restypeId,resId,opId,userId,roleType);		
		sql.append("insert into TD_SM_PERMISSION_ORIGINE(ORIGINE_TYPE,ORIGINE_ID,OP_ID,RES_ID,")
		    .append("ROLE_ID,RESTYPE_ID,TYPES)values(?,?,?,?,?,?,?)");
		
		try{
			for(int i=0;i<list.size();i++){
				ResOpOriginObj resOpOriginInfo = (ResOpOriginObj)list.get(i);
				PreparedDBUtil pd = new PreparedDBUtil();
				pd.preparedInsert(sql.toString());
				pd.setString(1,resOpOriginInfo.getOrigineType());
				pd.setString(2,resOpOriginInfo.getOrigineId());
				pd.setString(3,opId);			
				pd.setString(4,resId);
				pd.setString(5,roleId);
				pd.setString(6,restypeId);
				pd.setString(7,roleType);
				pd.executePrepared();
			}
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}

	
}