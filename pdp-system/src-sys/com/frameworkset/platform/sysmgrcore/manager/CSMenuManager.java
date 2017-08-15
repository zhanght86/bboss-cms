/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.CSMenuModel;

/**
 * <p>Title: CSMenuManager.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-19 8:45:07
 * @author ge.tao
 * @version 1.0
 */
public interface CSMenuManager extends Serializable {
	/**
     * 根据父亲id得到CS菜单资源的子列表
     * 
     * @return 
     * ColumnTree.java
     * @author: ge.tao
     */
    public List getCSMenuItems(String parent_id);
    
    /**
     * 根据父亲ID得到报表菜单资源的子列表
     * 
     * @param parent_id
     * @return
     * @author gao.tang
     */
    public List getReportMenuItems(String parent_id);
    
    
    /**
     * 得到CS菜单资源的列表
     * 树形结构组织的
     * @return 
     * ColumnTree.java
     * @author: ge.tao
     */
    public List getCSMenuItems();
    
    /**
     * 获取cs菜单子系统列表
     * @return List<CSMenuModel> 
     * @author: ge.tao
     */
    public List getSubSystems();
    
    /**
     * 获取cs菜单子系统的菜单条
     * @return List<CSMenuModel> 
     * @author: ge.tao
     */
    public List getMenubarsOfSubSystem(String subsystemid);
    
    
    /**
     * 获取cs菜单子系统的菜单条的菜单项
     * @return List<CSMenuModel> 
     * @author: ge.tao
     */
    public List getMenubarItemsOfMenubar(String menubarid);
    
    public boolean hasSon(String parent_id);
    
    /**
     * 通过CS菜单资源ID,后去菜单资源对象的信息
     * @param id
     * @return 
     * CSMenuManager.java
     * @author: ge.tao
     */
    public CSMenuModel getCSMenuModelInfoById(String menuid);
    
    /**
     * 通过CS菜单资源ID,获取当前菜单在菜单树上的路径
     * @param menuid
     * @return 
     * CSMenuManager.java
     * @author: ge.tao
     */
    public String getCSMenuPath(String menuid);
    
    
    /**
     * 通过CS菜单资源ID,获取当前菜单在菜单树上的路径
     * @param menuid
     * @return 
     * CSMenuManager.java
     * @author: ge.tao
     */
    public List getCSMenuPathList(String menuid);
    
    /**
     * 根据父亲id得到CS菜单资源的子,孙子...列表
     * @param parent_id
     * @return 
     * CSMenuManagerImpl.java
     * @author: ge.tao
     */
    public List getRecursionCSMenuItems(String parent_id);

}
