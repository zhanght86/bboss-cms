package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.event.EventHandle;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Opergpoper;
import com.frameworkset.platform.sysmgrcore.entity.Opergprestype;
import com.frameworkset.platform.sysmgrcore.entity.Opergpry;
import com.frameworkset.platform.sysmgrcore.entity.Opergroup;
import com.frameworkset.platform.sysmgrcore.entity.Operres;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.entity.RoleresopKey;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OperManager;

/**
 * 项目：SysMgrCore <br>
 * 描述：操作的管理接口 <br>
 * 版本：1.0 <br>
 * ---没有被使用的类
 * 
 * @author 
 */
public class OperManagerImpl extends EventHandle implements OperManager {
	private static Logger logger = Logger.getLogger(OperManagerImpl.class
			.getName());



	/**
	 * 存储操作
	 * 
	 * @param oper
	 * @return boolean
	 */
	public boolean storeOper(Operation oper) throws ManagerException {
		boolean r = false;

		// if (oper != null) {
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(oper);
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	public boolean storeOpergroup(Opergroup opergroup) throws ManagerException {
		boolean r = false;

		// if (opergroup != null) {
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(opergroup);
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	public boolean storeOperres(Operres operres) throws ManagerException {
		boolean r = false;

		// if (operres != null) {
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(operres);
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	public boolean storeOpergpry(Opergpry opergpry) throws ManagerException {
		boolean r = false;

		// if (opergpry != null) {
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(opergpry);
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	public boolean storeOpergpoper(Opergpoper opergpoper)
			throws ManagerException {
		boolean r = false;
		//
		// if (opergpoper != null) {
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(opergpoper);
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	public boolean storeOpergprestype(Opergprestype opergprestype)
			throws ManagerException {
		boolean r = false;

		// if (opergprestype != null) {
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(opergprestype);
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	/**
	 * 取操作列表
	 * 
	 * @return List
	 */
	public List getOperList() throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Operation");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	/**
	 * 取操作组列表
	 * 
	 * @return List
	 */
	public List getOpergroupList() throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Opergroup");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public Operation getOper(String propName, String value)
			throws ManagerException {
		// Operation operation = null;

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Operation o where o." + propName + "='" + value
		// + "'");
		//
		// List list = (List) cb.execute(p);
		//
		// if ((list != null) && !list.isEmpty()) {
		// operation = (Operation) list.get(0);
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		// return operation;
		return null;
	}

	public Opergroup getOpergroup(String propName, String value)
			throws ManagerException {
		// Opergroup opergroup = null;
		//
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Opergroup o where o." + propName + "='" + value
		// + "'");
		//
		// List list = (List) cb.execute(p);
		//
		// if ((list != null) && !list.isEmpty()) {
		// opergroup = (Opergroup) list.get(0);
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		//
		// return opergroup;
		return null;
	}

	public List getOperList(Res res, int type) throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		//
		// String strSelect = "";
		//
		// if (type == TYPE_ROLERESOP) {
		// strSelect = "from Operation o where o.opId in ("
		// + "select rro.id.opId from Roleresop rro where rro.id.resId = '"
		// + res.getResId() + "')";
		// } else if (type == TYPE_OPERRES) {
		// strSelect = "from Operation o where o.opId in ("
		// + "select or.id.opId from Operres or where or.id.resId = '"
		// + res.getResId() + "')";
		// } else if (type == TYPE_USERRESOP) {
		// strSelect = "from Operation o where o.opId in ("
		// + "select uro.id.opId from Userresop uro where uro.id.resId = '"
		// + res.getResId() + "')";
		// }
		//
		// p.setObject(strSelect);
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public List getOperList(Role role) throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p
		// .setObject("from Operation o where o.opId in ("
		// + "select rro.id.opId from Roleresop rro where rro.id.roleId = '"
		// + role.getRoleId() + "')");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public List getOperList(String restypeId) throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p
		// .setObject("from Operation o where o.opId in ("
		// + "select rro.id.opId from Opergpry rro where rro.id.restypeId = '"
		// + restypeId
		// + "') order by length(o.priority), o.priority");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public List getOperList(String roleId, String resId, String restypeId)
			throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p
		// .setObject("from Operation o where o.opId in ("
		// + "select rro.id.opId from Roleresop rro where rro.id.restypeId = '"
		// + restypeId + "' and rro.id.resId = '" + resId
		// + "' and rro.id.roleId = '" + roleId + "')");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public List getOperResUserList(String roleId, String resId, String restypeId)
			throws ManagerException {
		return getResOperListOfRole(roleId, resId, restypeId, "user");
	}

	public List getOperResOrgList(String roleId, String resId, String restypeId)
			throws ManagerException {
		return getResOperListOfRole(roleId, resId, restypeId, "organization");

	}

	public List getResOperListOfRole(String roleId, String resId,
			String restypeId, String roleType) throws ManagerException {
		List list = new ArrayList();

		DBUtil dbUtil = new DBUtil();
		String sql = "select * from TD_SM_ROLERESOP where RES_ID ='" + resId
				+ "' and RESTYPE_ID='" + restypeId + "' and role_id='" + roleId
				+ "' and (types='" + roleType + "')";
		// list = new ArrayList();
		try {
			dbUtil.executeSelect(sql);
			for (int i = 0; i < dbUtil.size(); i++) {

				RoleresopKey id = new RoleresopKey();
				id.setOpId(dbUtil.getString(i, "op_id"));
				id.setResId(dbUtil.getString(i, "res_id"));
				id.setRestypeId(dbUtil.getString(i, "restype_id"));
				id.setRoleId(dbUtil.getString(i, "role_id"));
				list.add(id);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ManagerException(e.getMessage());
		}

		//	
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Roleresop rro where rro.id.restypeId = '"
		// + restypeId + "' and rro.id.resId = '" + resId
		// + "' and rro.id.roleId = '" + roleId + "'");
		// list = (List) cb.execute(p);

		return list;
	}

	public Map getResOperMapOfRole(String roleId, String resId,
			String restypeId, String roleType) throws ManagerException {
		Map list = new HashMap();

		DBUtil dbUtil = new DBUtil();
		String sql = "select * from TD_SM_ROLERESOP where RES_ID ='" + resId
				+ "' and RESTYPE_ID='" + restypeId + "' and role_id='" + roleId
				+ "' and (types='" + roleType + "')";
		// list = new ArrayList();
		try {
			dbUtil.executeSelect(sql);
			for (int i = 0; i < dbUtil.size(); i++) {

				RoleresopKey id = new RoleresopKey();
				id.setOpId(dbUtil.getString(i, "op_id"));
				id.setResId(dbUtil.getString(i, "res_id"));
				id.setRestypeId(dbUtil.getString(i, "restype_id"));
				id.setRoleId(dbUtil.getString(i, "role_id"));
				list.put(dbUtil.getString(i, "op_id"), id);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ManagerException(e.getMessage());
		}

		//	
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Roleresop rro where rro.id.restypeId = '"
		// + restypeId + "' and rro.id.resId = '" + resId
		// + "' and rro.id.roleId = '" + roleId + "'");
		// list = (List) cb.execute(p);

		return list;
	}

	public List getOperResRoleList(String roleId, String resId, String restypeId)
			throws ManagerException {
		return getResOperListOfRole(roleId, resId, restypeId, "role");
	}

	public List getOperResRoleList(String roleType, String roleId,
			String resId, String restypeId) throws ManagerException {
		List list = new ArrayList();

		DBUtil dbUtil = new DBUtil();

		String sql = "select * from TD_SM_ROLERESOP where RES_ID ='" + resId
				+ "' and RESTYPE_ID='" + restypeId + "' and role_id='" + roleId
				+ "' and types='" + roleType + "'";
		list = new ArrayList();
		try {
			dbUtil.executeSelect(sql);
			for (int i = 0; i < dbUtil.size(); i++) {

				RoleresopKey id = new RoleresopKey();
				id.setOpId(dbUtil.getString(i, "op_id"));
				id.setResId(dbUtil.getString(i, "res_id"));
				id.setRestypeId(dbUtil.getString(i, "restype_id"));
				id.setRoleId(dbUtil.getString(i, "role_id"));
				list.add(id);
			}

		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());
		}
		return list;
	}

	public List getOperList(Opergroup opergroup) throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p
		// .setObject("from Operation o where o.opId in ("
		// + "select rgo.id.opId from Opergpoper rgo where rgo.id.groupId = '"
		// + opergroup.getGroupId()
		// + "') order by length(o.priority), o.priority");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public List getOperList(User user, int type) throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		//
		// String strSelect = "";
		//
		// if (type == TYPE_TEMPACCREDIT) {
		// strSelect = "from Operation o where o.opId in ("
		// + "select ta.id.opId from Tempaccredit ta where ta.id.userId = '"
		// + user.getUserId() + "')";
		// } else if (type == TYPE_USERRESOP) {
		// strSelect = "from Operation o where o.opId in ("
		// + "select uro.id.opId from Userresop uro where uro.id.userId = '"
		// + user.getUserId() + "')";
		// }
		//
		// p.setObject(strSelect);
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public List getExistRestypeList(String groupId) throws ManagerException {
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p
		// .setObject("from Restype rt where rt.restypeId in ("
		// + "select ogr.id.restypeId from Opergprestype ogr where
		// ogr.id.groupId = '"
		// + groupId + "')");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	public boolean deleteOperres(String opId, String resId)
			throws ManagerException {
		boolean r = false;

		// if ((opId != null) && (resId != null)) {
		// try {
		// // 删除当前操作资源关系对象
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_DELETE);
		// p.setObject("from Operres o where o.id.opId = '" + opId
		// + "' and o.id.resId = '" + resId + "'");
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	public boolean deleteOperres(Operres operres) throws ManagerException {
		return deleteOperres(operres.getId().getOpId(), operres.getId()
				.getResId());
	}

	public boolean deleteOpergroup(String groupId) throws ManagerException {
		boolean r = false;

		// try {
		// Opergroup opergroup = new Opergroup();
		// opergroup.setGroupId(groupId);
		//
		// List list = getOperList(opergroup);
		//
		// for (int i = 0; i < list.size(); i++) {
		// String opId = ((Operation) list.get(i)).getOpId();
		// deleteOper(opId, groupId);
		// }
		//
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_DELETE);
		//
		// // 删除当前操作与资源类型关系对象
		// p.setObject("from Opergprestype ogt where ogt.id.groupId = '"
		// + groupId + "'");
		// cb.execute(p);
		//
		// // 删除当前操作与操作组关系对象
		// p.setObject("from Opergroup og where og.groupId = '" + groupId
		// + "'");
		// cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return r;
	}

	public boolean deleteOpergprestype(String groupId) throws ManagerException {
		boolean r = false;

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_DELETE);
		//
		// // 删除当前操作组与资源类型关系对象
		// p.setObject("from Opergprestype ogr where ogr.id.groupId = '"
		// + groupId + "'");
		// cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return r;
	}

	public boolean deleteOper(String opId, String groupId)
			throws ManagerException {
		boolean r = false;

		// if (opId != null) {
		// try {
		// cb.setAutoCommit(false);
		//
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_DELETE);
		//
		// // 删除当前操作与操作组关系对象
		// p.setObject("from Opergpoper ogo where ogo.id.opId = '" + opId
		// + "' and ogo.id.groupId='" + groupId + "'");
		// cb.execute(p);
		//
		// // 删除当前操作资源关系对象
		// p.setObject("from Operres o where o.id.opId = '" + opId + "'");
		// cb.execute(p);
		//
		// // 删除当前操作所关联的 Roleresop 对象
		// p.setObject("from Roleresop rro where rro.id.opId = '" + opId
		// + "'");
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// cb.commit(true);
		// }
		//
		// // 删除指定的资源实例
		// // 有可能在其他组中
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Opergpoper ogo where ogo.id.opId='" + opId
		// + "'");
		//
		// List list = (List) cb.execute(p);
		//
		// if ((list == null) || (list.size() < 1)) {
		// p.setCommand(Parameter.COMMAND_DELETE);
		// p.setObject("from Operation o where o.opId = '" + opId
		// + "'");
		//
		// if (cb.execute(p) != null) {
		// r = true;
		// }
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		//
		// try {
		// cb.rollback(true);
		// } catch (ControlException e1) {
		// logger.error(e1);
		// }
		//
		// throw new ManagerException(e.getMessage());
		// }
		// }

		return r;
	}

	/**
	 * 删除操作
	 * 
	 * @param oper
	 * @return boolean
	 */

	// public boolean deleteOper(Operation oper) throws ManagerException {
	// return deleteOper(oper.getOpId());
	// }
	public Operation loadAssociatedSet(String opId, String associated)
			throws ManagerException {
		// Operation operation = null;
		//
		// try {
		// Parameter par = new Parameter();
		// par.setCommand(Parameter.COMMAND_GET);
		// par.setObject("from Operation op left join fetch op." + associated
		// + " where op.opId = '" + opId + "'");
		//
		// List list = (List) cb.execute(par);
		//
		// if ((list != null) && !list.isEmpty()) {
		// operation = (Operation) list.get(0);
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		//
		// return operation;
		return null;
	}

	public boolean isOperExist(String operName) throws ManagerException {
		boolean r = false;

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Operation o where o.opName='" + operName + "'");
		//
		// List list = (List) cb.execute(p);
		//
		// if ((list != null) && (list.size() > 0)) {
		// r = true;
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return r;
	}



	/**
	 * 取资源对应的角色及操作列表 没有被使用的方法
	 */
	public List getRoleOperList(String resId, String restypeId)
			throws ManagerException {
		List list = new ArrayList();
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Roleresop rro where rro.id.restypeId = '"
		// + restypeId + "' and rro.id.resId = '" + resId
		// + "' order by rro.id.roleId");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }
		// String sql = "";

		return list;
	}

	/**
	 * 没有被使用的方法 检查rro对象是否存在
	 * 
	 * @param rro
	 * @return
	 * @throws ManagerException
	 */
	public Roleresop getRoleresop(Roleresop rro) throws ManagerException {
		Roleresop r = null;
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Roleresop rro where rro.id.restypeId = '"
		// + rro.getId().getRestypeId() + "' and rro.id.resId = '"
		// + rro.getId().getResId() + "' and rro.id.roleId='"
		// + rro.getId().getRoleId() + "' and rro.id.opId='"
		// + rro.getId().getOpId() + "' ");
		// List list = (List) cb.execute(p);
		//
		// if ((list != null) && !list.isEmpty()) {
		// r = (Roleresop) list.get(0);
		// }
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return r;
	}

	// /**
	// * 删除一个rro
	// */
	// public boolean deleteRoleresop(Roleresop rro) throws ManagerException {
	// boolean r = false;
	//
	// try {
	// Parameter p = new Parameter();
	// p.setCommand(Parameter.COMMAND_DELETE);
	//
	//			
	// p.setObject("from Roleresop rro where rro.id.restypeId = '"
	// + rro.getId().getRestypeId() + "' and rro.id.resId = '"
	// + rro.getId().getResId() + "' and rro.id.roleId='"
	// + rro.getId().getRoleId() + "' and rro.id.opId='"
	// + rro.getId().getOpId() + "' ");
	// cb.execute(p);
	// } catch (ControlException e) {
	// logger.error(e);
	// throw new ManagerException(e.getMessage());
	// }
	//
	// return r;
	// }
}
