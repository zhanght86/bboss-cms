package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.control.PageConfig;
import com.frameworkset.platform.sysmgrcore.entity.Attrdesc;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Restype;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：资源管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public interface ResManager extends Provider, Serializable {

	public static final int TYPE_OPERRES = 2;

	public static final int TYPE_ROLERESOP = 1;

	public static final int TYPE_USERRESOP = 3;
	
	/**
	 * 用户角色
	 */
	public static final String ROLE_TYPE_USER = "user";
	/**
	 * 用户组角色
	 */
	public static final String ROLE_TYPE_USERGROUP = "user_group";
	/**
	 * 机构岗位角色
	 */
	public static final String ROLE_TYPE_ORGJOB = "org_job";
	
	/**
	 * 资源 是原始资源
	 */
	public static final int RESOPORIGIN_ORIGIN = 0;
	/**
	 * 资源来自用户资源
	 */
	public static final int RESOPORIGIN_USER = 1;
	/**
	 * 资源来自角色资源
	 */
	public static final int RESOPORIGIN_ROLE = 2;
	/**
	 * 资源来自机构资源
	 */
	public static final int RESOPORIGIN_ORG = 3;	
	/**
	 * 资源来自用户资源和角色资源
	 */
	public static final int RESOPORIGIN_USER_ROLE = 4;
	/**
	 * 资源来自用户资源和机构资源
	 */
	public static final int RESOPORIGIN_USER_ORG = 5;
	/**
	 * 资源来自角色资源和机构资源
	 */
	public static final int RESOPORIGIN_ROLE_ORG = 6;	
	/**
	 * 资源来自用户资源和角色资源和机构资源
	 */
	public static final int RESOPORIGIN_USER_ROLE_ORG = 7;

	/**
	 * 存储资源类型
	 * 
	 * @param resType
	 * @return boolean
	 * @throws ManagerException
	 * @throws ManagerException
	 */
	public boolean storeResType(Restype resType) throws ManagerException,
			ManagerException;

	/**
	 * 存储资源
	 * 
	 * @param res
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean storeRes(Res res) throws ManagerException;

	/**
	 * 存储属性描述
	 * 
	 * @param attrdesc
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean storeAttrdesc(Attrdesc attrdesc) throws ManagerException;

	/**
	 * 根据属性描述对象的属性和值来取用户实例 来取值
	 * 
	 * @param propName
	 * @param value
	 * @return List
	 * @throws ManagerException
	 */
	public List getAttrdescList(String propName, String value)
			throws ManagerException;

	/**
	 * 取所有属性描述
	 * 
	 * @return List
	 * @throws ManagerException
	 */
	public List getAttrdescList() throws ManagerException;

	/**
	 * 取所有资源
	 * 
	 * @return com.frameworkset.platform.sysmgrcore.entity.Res
	 * @throws ManagerException
	 */
	public List getResList() throws ManagerException;

	/**
	 * 根据资源对象的属性和值取资源列表
	 * 
	 * @param propName
	 * @param value
	 * @return
	 * @throws ManagerException
	 */
	public Res getRes(String propName, String value) throws ManagerException;

	/**
	 * 根据操作取资源
	 * 
	 * @param oper
	 * @return List
	 * @throws ManagerException
	 */
	public List getResList(Operation oper) throws ManagerException;

	/**
	 * 根据角色取资源
	 * 
	 * @param role
	 * @return List
	 * @throws ManagerException
	 */
	public List getResList(Role role) throws ManagerException;

	/**
	 * 根据角色和资源类型取资源
	 * 
	 * @param role
	 * @return List
	 * @throws ManagerException
	 */
	public ListInfo getResList(String roleId, String restypeId, long offset,
			int maxItems) throws ManagerException;

	/**
	 * 根据用户取资源
	 * 
	 * @param user
	 * @return List
	 * @throws ManagerException
	 */
	public List getResList(User user) throws ManagerException;

	/**
	 * 根据 Hibernate 的 HQL 语言取资源列表（王卓修改）
	 * 
	 * @param hsql
	 * @return
	 * @throws ManagerException
	 */
	public List getResList(String hql) throws ManagerException;

	/**
	 * 根据属性说明取资源类型
	 * 
	 * @param attrdesc
	 * @return com.frameworkset.platform.sysmgrcore.entity.Restype
	 * @throws ManagerException
	 */
	public Restype getResType(Attrdesc attrdesc) throws ManagerException;

	/**
	 * 根据名称取资源类型
	 * 
	 * @param resTypeName
	 * @return com.frameworkset.platform.sysmgrcore.entity.Restype
	 * @throws ManagerException
	 */
	public Restype getResType(String resTypeName) throws ManagerException;

	/**
	 * 根据资源取资源类型
	 * 
	 * @param res
	 * @return com.frameworkset.platform.sysmgrcore.entity.Restype
	 * @throws ManagerException
	 */
	public Restype getResType(Res res) throws ManagerException;

	/**
	 * 取所有资源类型
	 * 
	 * @return List
	 * @throws ManagerException
	 */
	public List getResTypeList() throws ManagerException;

	/**
	 * 根据 Restype 对象取所有的子资源类型列表
	 * 
	 * @param resType
	 * @return
	 * @throws ManagerException
	 */
	public List getChildResTypeList(Restype resType) throws ManagerException;

	/**
	 * 取所有子资源列表
	 * 
	 * @return
	 * @throws ManagerException
	 */
	public List getChildResList(Res res) throws ManagerException;

	/**
	 * 删除资源类型。当前删除支持级联删除。
	 * 
	 * @param resType
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteResType(Restype resType) throws ManagerException;

	/**
	 * 删除资源
	 * 
	 * @param res
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteRes(Res res) throws ManagerException;

	/**
	 * 删除指定的属性描述
	 * 
	 * @param attrdesc
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteAttrdesc(Attrdesc attrdesc) throws ManagerException;

	/**
	 * 装载与资源相关的对象
	 * 
	 * @param res
	 * @param associated
	 * @return
	 * @throws ManagerException
	 */
	public Res loadAssociatedSet(String resId, String associated)
			throws ManagerException;

	/**
	 * 根据资源ID描述判断指定的资源是否存在
	 * 
	 * @param resId
	 * @return
	 * @throws ManagerException
	 */
	public boolean isResExist(String resId) throws ManagerException;

	/**
	 * 根据资源名称描述判断指定的资源是否存在
	 * 
	 * @param resId
	 * @return
	 * @throws ManagerException
	 */
	public boolean isResExistitle(String title) throws ManagerException;

	/**
	 * 判断指定的资源是否包含子资源
	 * 
	 * @param resId
	 * @return
	 * @throws ManagerException
	 */
	public boolean isContainChildRes(Res res) throws ManagerException;

	/**
	 * 判断指定的资源类型是否包含子资源类型
	 * 
	 * @param restype
	 * @return
	 * @throws ManagerException
	 */
	public boolean isContainChildResType(Restype restype)
			throws ManagerException;

	/**
	 * 返回页面配置管理类
	 * 
	 * @return
	 * @throws ManagerException
	 */
	public PageConfig getPageConfig() throws ManagerException;

	/**
	 * 删除按岗位查询出的冗余资源数据 gao.tang
	 * 
	 * @param opId---操作ID
	 * @param resId---资源ID
	 * @param roleId---角色ID
	 * @param resTypeId---资源类型ID
	 * @param types---资源分类
	 * @return
	 */
	public boolean deleteRoleResOp(String opId, String resId, String roleId,
			String resTypeId, String types) throws ManagerException;

     /**
	 * 根据用户、岗位、机构或角色及相应的资源类型和操作来取资源
	 * @param type--要查询的资源
	 * @param id--要操作的资源类型标识
	 * @param resId--要操作的资源标识
	 * @param resName--要操作的资源名称
	 * @param restypeId--要操作的资源类型标识
	 * @param opId--要查询的操作标识
	 * @return
	 * @throws ManagerException
	 */
	 public ListInfo getResList(Map paramMap,long offset,int maxItems) throws ManagerException;
	 
	 /**
	  * 按岗位ID查询出岗位对应角色下的所有资源 gao.tang 2007.10.25
	  * @param jobId
	  * @param resName
	  * @param resid
	  * @param resTypeId
	  * @param opId
	  * @param offset
	  * @param maxPagesize
	  * @return
	  * @throws ManagerException
	  */
	 public ListInfo getJobRoleList(String jobId, String resName, String resid,
				String resTypeId, String opId, long offset, int maxPagesize) throws ManagerException;
    
	 /**  chunqiu.zhao   2007.10.25
		 * 根据用户、岗位、机构或角色删除对应的数据冗余
		 * @param type--要删除的资源类型
		 * @return
		 * @throws ManagerException
	 */ 
	 public boolean delRedundance(String type) throws ManagerException;
	 
	 /**  chunqiu.zhao   2007.10.25
		 * 查询系统中未受保护的资源
		 * @param 
		 * @return
		 * @throws ManagerException
	 */ 
	 public ListInfo getUnprotectedRes(String restypeId,String resId,String resName,String opId) throws ManagerException;
	 
	 /**  chunqiu.zhao   2007.10.25
		 * 查询系统中只有超级管理员才有的资源
		 * @param 
		 * @return
		 * @throws ManagerException
	 */ 
	 public ListInfo getAdminRes(String restypeId,String resId,String resName,String opId) throws ManagerException;
	 
	 /**
	  * 2008-01-02
	  * 根据资源的信息, 获取授权人该资源来源 来源的类型和ID等信息
	  * @param restypeId
	  * @param resId
	  * @param resName
	  * @param opId
	  * @param types
	  * @return List<ResOpOriginObj>  
	  * @throws ManagerException 
	  * ResManager.java
	  * @author: ge.tao
	  */
	public List getResOpOriginInfo(String restypeId,String resId,String opId,String roleId, String types)throws ManagerException;
	
	/**
	 * 保存被授予资源 的来源 来源包括: 资源来源类型, 资源来源的对象ID
	 * @param restypeId 资源类型ID. 菜单资源,..
	 * @param resId
	 * @param opId
	 * @param roleId
	 * @param resType 资源类型 用户.角色.机构
	 * @param userId
	 * @throws ManagerException 
	 * ResManager.java
	 * @author: ge.tao
	 */
	public boolean saveResOpOrigin( String restypeId,String resId,String opId,String roleId, String resType, String userId)throws ManagerException;
	
	/**
	 * 操作资源回收
	 * @param restypeId
	 * @param resId
	 * @param resName
	 * @param opId
	 * @param types 
	 * @return
	 * @throws ManagerException 
	 * ResManager.java
	 * @author: ge.tao
	 */
	public void reclaimResOp(DBUtil deleteDB, String restypeId, String resId,  String opId, String types, String setObjId)throws ManagerException; 	
	
	/**
	 * 用户角色回收, 递归回收
	 * @param deleteDB 外部传入的DB, 保证在同一事务.
	 * @param roleId 角色ID
	 * @param userId 授权的用户ID
	 * @param roleTypes 角色类型 用户角色(td_sm_userrole),用户组角色(td_sm_grouprole)
	 * @return boolean
	 * @throws ManagerException 
	 * ResManager.java
	 * @author: ge.tao
	 */
	public boolean reclaimUserRole(String roleId, String userId,String roleTypes)throws ManagerException; 	
	
	/**
	 * 机构岗位角色角色回收,递归回收 (td_sm_orgjobrole)
	 * @param orgId
	 * @param jobId
	 * @param roleId
	 * @param userId
	 * @return
	 * @throws ManagerException 
	 * ResManager.java
	 * @author: ge.tao
	 */
	public boolean reclaimOrgJobRole(String orgId, String jobId, String roleId, String userId)throws ManagerException; 
	
	/**
	 * 批量删除,联动删除td_sm_roleresop权限表的所有记录
	 * @param resIds
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteResandAuth(String[] resIds) throws ManagerException ;

}