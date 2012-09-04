//Source file: D:\\environment\\eclipse\\workspace\\cjxerpsecurity\\src\\com\\westerasoft\\common\\security\\websphere\\authorization\\impl\\AppPermissionRoleMap.java

package com.frameworkset.platform.sysmgrcore.authorization;

import java.io.Serializable;
import java.util.List;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.security.authorization.AuthRole;
import com.frameworkset.platform.security.authorization.impl.PermissionRoleMap;
import com.frameworkset.platform.security.authorization.impl.SecurityException;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;

/**
 *
 * 获取相应资源许可角色；
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class AppPermissionRoleMap extends PermissionRoleMap 
				implements Serializable{
//    private static TraceComponent tc;

 
//    static {
//        tc = Tr.register(AppPermissionRoleMap.class, null,
//                         "com.ibm.ejs.resources.security");
//    }
 
    /**
     * @since 2004.12.15
     */
    public AppPermissionRoleMap() {

    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @return Role[]
     */
    protected AuthRole[] getRequiredRoles(
                                      java.lang.String resource,
                                      java.lang.String action,
                                      String resourceType) throws SecurityException{

    	AuthRole[] asecurityroles = null;
        try {
            List list = SecurityDatabase.getRoleManager(getProviderType()).getAllRoleHasPermissionInResource(resource,action,resourceType);
            if (list != null && list.size() > 0) {

                asecurityroles = new AuthRole[list.size()];
                for (int i = 0; i < list.size(); i++) {
                	asecurityroles[i] = new AuthRole();
                    asecurityroles[i].setRoleName(((Role) list.get(i)).getRoleName());
                    asecurityroles[i].setRoleType(((Role) list.get(i)).getRoleType());
                }
            }

            
        }
        //add by 20080721 gao.tang 添加ManagerException异常处理
        catch(ManagerException me){
        	me.printStackTrace();
			throw new SecurityException(me);
			
        }catch (Exception e) {
        	//add by 20080721 gao.tang 异常信息抛出
        	e.printStackTrace();
        	throw new SecurityException(e);
        }
        return asecurityroles;
    }
    
    /**
     * 判断特定类型的资源是否授过权
     * true:标识已授过权限
     * false:标识没有受过权限
     * @param resourceType
     * @param resource
     * @return
     * @throws SecurityException
     * add by 20080721 gao.tang 方法抛出SecurityException异常
     */
    public boolean hasGrantedRoles(String resource,String resourceType) throws SecurityException
    {
    	boolean hasGranted = false;
		try
		{
			hasGranted = SecurityDatabase
			.getRoleManager(getProviderType())
			.hasGrantedRoles(resourceType,resource);
		}
		catch (SPIException e)
		{
			//add by 20080721 gao.tang 异常信息抛出
			e.printStackTrace();
			throw new SecurityException(e);
		}
		//add by 20080721 gao.tang 添加Exception异常处理
		catch (Exception e) {
        	
        	e.printStackTrace();
        	throw new SecurityException(e);
        }
    	return hasGranted;
    }

    /**
     * add by 20080721 gao.tang 方法抛出SecurityException异常
     */
	public boolean hasGrantRole(AuthRole role, String resource, String resourceType) throws SecurityException
	{
		boolean hasGranted = false;
		try
		{
			hasGranted = SecurityDatabase
			.getRoleManager(getProviderType())
			.hasGrantRole(role,resource,resourceType);
		}
		catch (SPIException e)
		{
			//add by 20080721 gao.tang 异常信息抛出
			e.printStackTrace();
			throw new SecurityException(e);
		}
		catch (ManagerException e)
		{
			//add by 20080721 gao.tang 异常信息抛出
			e.printStackTrace();
			throw new SecurityException(e);
		}catch(Exception e){
			e.printStackTrace();
			throw new SecurityException(e);
		}
    	return hasGranted;
	}

}
