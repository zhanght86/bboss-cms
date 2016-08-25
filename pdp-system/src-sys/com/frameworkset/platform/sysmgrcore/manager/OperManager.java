package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Opergpoper;
import com.frameworkset.platform.sysmgrcore.entity.Opergprestype;
import com.frameworkset.platform.sysmgrcore.entity.Opergpry;
import com.frameworkset.platform.sysmgrcore.entity.Opergroup;
import com.frameworkset.platform.sysmgrcore.entity.Operres;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ControlException;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;

/**
 * 项目：SysMgrCore <br>
 * 描述：操作的管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 
 */
public interface OperManager extends Provider, Serializable {

	/**
	 * TYPE_OPERRES
	 */
	public static final int TYPE_OPERRES = 4;

	/**
	 * TYPE_ROLERESOP
	 */
	public static final int TYPE_ROLERESOP = 3;

	/**
	 * TYPE_TEMPACCREDIT
	 */
	public static final int TYPE_TEMPACCREDIT = 2;

	/**
	 * TYPE_USERRESOP
	 */
	public static final int TYPE_USERRESOP = 1;

	public boolean deleteOper(String opId, String groupId)
			throws ManagerException;

	/**
	 * 删除操作
	 * 
	 * @param oper
	 * @return boolean
	 * @throws ManagerException
	 * @throws ControlException
	 */
	// public boolean deleteOper(Operation oper) throws ManagerException;
	public boolean deleteOperres(String opId, String resId)
			throws ManagerException;

	/**
	 * 删除操作资源实体
	 * 
	 * @param operres
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteOperres(Operres operres) throws ManagerException;

	/**
	 * 根据操作对象实体的属性与值取操作对象实例
	 * 
	 * @param propName
	 * @param value
	 * @return
	 * @throws ManagerException
	 */
	public Operation getOper(String propName, String value)
			throws ManagerException;

	// 删除：该函数已经更名为 getOperList(Res res, int type);
	// /**
	// * 根据资源取操作
	// *
	// * @param res
	// * @return List
	// */
	// public List getOperByRes(Res res);

	/**
	 * 取操作列表
	 * 
	 * @return List
	 * @throws ManagerException
	 */
	public List getOperList() throws ManagerException;

	/**
	 * 根据资源取操作列表
	 * 
	 * @param res
	 * @param type
	 *            用于指示根据那一个类型的表中取操作列表，有关系的表：TD_SM_USERRESOP、TD_SM_TEMPACCREDIT，可以直接使用常量
	 *            TYPE_XXX
	 * @return
	 * @throws ManagerException
	 */
	public List getOperList(Res res, int type) throws ManagerException;

	/**
	 * 根据角色取操作列表
	 * 
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public List getOperList(Role role) throws ManagerException;

	/**
	 * 根据用户取操作列表
	 * 
	 * @param user
	 * @param type
	 *            表示将从那一个表中取操作列表，有关系表：TD_SM_USERRESOP、TD_SM_TEMPACCREDIT，
	 *            可以直接使用常量 OperManager.TYPE_XXXX 。
	 * @return
	 * @throws ManagerException
	 */
	public List getOperList(User user, int type) throws ManagerException;

	/**
	 * 根据操作描述判断指定的操作是事存在
	 * 
	 * @param operDes
	 * @return
	 * @throws ManagerException
	 */
	public boolean isOperExist(String operName) throws ManagerException;

	/**
	 * 装载
	 * 
	 * @param oper
	 * @param associated
	 * @return
	 * @throws ManagerException
	 */
	public Operation loadAssociatedSet(String opId, String associated)
			throws ManagerException;

	/**
	 * 存储操作
	 * 
	 * @param oper
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean storeOper(Operation oper) throws ManagerException;

	/**
	 * 存储操作资源
	 * 
	 * @param operres
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeOperres(Operres operres) throws ManagerException;



	/**
	 * 取操作组列表
	 * 
	 * @return List
	 */
	public List getOpergroupList() throws ManagerException;

	public Opergroup getOpergroup(String propName, String value)
			throws ManagerException;

	public List getOperList(String restypeId) throws ManagerException;

	public List getOperList(Opergroup opergroup) throws ManagerException;

	public List getOperList(String roleId, String resId, String restypeId)
			throws ManagerException;
	
	/**
	 * 取资源的所有的授予的角色和对应的操作列表
	 * @param resId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 */
	public List getRoleOperList(String resId, String restypeId)
	throws ManagerException;

	public boolean storeOpergpry(Opergpry opergpry) throws ManagerException;

	public boolean storeOpergpoper(Opergpoper opergpoper)
			throws ManagerException;

	public boolean storeOpergroup(Opergroup opergroup) throws ManagerException;

	public boolean deleteOpergroup(String groupId) throws ManagerException;

	public List getExistRestypeList(String groupId) throws ManagerException;

	public boolean deleteOpergprestype(String groupId) throws ManagerException;

	public boolean storeOpergprestype(Opergprestype opergprestype)
			throws ManagerException;
	
	/**
	 * 取资源授予角色的操作列表
	 * @param roleId
	 * @param resId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 */
	public List getOperResRoleList(String roleId, String resId, String restypeId)
    throws ManagerException ;
	
	/**
	 * 取资源授予机构的操作列表
	 * @param roleId
	 * @param resId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 */
	public List getOperResOrgList(String roleId, String resId, String restypeId)throws ManagerException ;
	
	/**
	 * 取资源授予用户的操作列表
	 * @param roleId
	 * @param resId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 */
	public List getOperResUserList(String roleId, String resId, String restypeId)throws ManagerException ;
	
	/**
	 * 检测一个rro对象是否存在
	 * 
	 * @param rro
	 * @return
	 * @throws ManagerException
	 */
	public Roleresop getRoleresop(Roleresop rro) throws ManagerException;
	
	public List getOperResRoleList(String roleType,String roleId, String resId, String restypeId)
	throws ManagerException; 
//	/**
//	 * 删除一个rro
//	 * @param rro
//	 * @return
//	 * @throws ManagerException
//	 */
//	public boolean deleteRoleresop(Roleresop rro) throws ManagerException;
	
	public Map getResOperMapOfRole(String roleId, String resId,
			String restypeId, String roleType) throws ManagerException;
}
