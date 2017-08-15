/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.entity.CSMenuModel;
import com.frameworkset.platform.sysmgrcore.manager.CSMenuManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

/**
 * <p>Title: CSMenuManagerImpl.java</p>
 *
 * <p>Description: cs菜单管理,整合bs与cs菜单权限控制
 * cs菜单权限在bs平台中维护,提供相关接口给cs调用
 * cs菜单资源类型为cs_column
 *  </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-19 8:42:10
 * @author ge.tao
 * @version 1.0
 */
public class CSMenuManagerImpl implements CSMenuManager{
	public static final String REPORT_DATABASE = ConfigManager.getInstance().getConfigValue("REPORT_DATABASE","bspf");
	 /**
     * 根据父亲id得到CS菜单资源的子列表
     * 
     * @return 
     * ColumnTree.java
     * @author: ge.tao
     */
    public List getCSMenuItems(String parent_id){
    	List list = new ArrayList();
    	DBUtil db = new DBUtil();
    	String sql = "select id,parent_id,title,orderno,type,owner_table from v_cs_menu_tree " +
    			"where parent_id = '" + parent_id + "'";
    	try {
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				CSMenuModel model = new CSMenuModel();
				model.setId(db.getString(i,"id"));
				model.setParent_id(db.getString(i,"parent_id"));
				model.setTitle(db.getString(i,"title"));
				model.setOrderno(db.getString(i,"orderno"));
				model.setType(db.getString(i,"type"));
				model.setOwner_table(db.getString(i,"owner_table"));
				list.add(model);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return list;
    }
    
    /**
     * 根据父亲id得到CS菜单资源的子,孙子...列表
     * @param parent_id
     * @return 
     * CSMenuManagerImpl.java
     * @author: ge.tao
     */
    public List getRecursionCSMenuItems(String parent_id){
    	List list = new ArrayList();
    	DBUtil db = new DBUtil();
    	String sql = "select id,parent_id,title,orderno,type,owner_table from v_cs_menu_tree " +
    			"start with id = '" + parent_id + "' connect by prior id = parent_id  ";
    	try {
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				CSMenuModel model = new CSMenuModel();
				model.setId(db.getString(i,"id"));
				model.setParent_id(db.getString(i,"parent_id"));
				model.setTitle(db.getString(i,"title"));
				model.setOrderno(db.getString(i,"orderno"));
				model.setType(db.getString(i,"type"));
				model.setOwner_table(db.getString(i,"owner_table"));
				list.add(model);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return list;
    }
    
    
    /**
     * 得到CS菜单资源的列表
     * 树形结构组织的
     * @return 
     * ColumnTree.java
     * @author: ge.tao
     */
    public List getCSMenuItems(){
    	List list = new ArrayList();
    	DBUtil db = new DBUtil();
    	String sql = "select id,parent_id,title,orderno,type,owner_table from v_cs_menu_tree ";
    	try {
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				CSMenuModel model = new CSMenuModel();
				model.setId(db.getString(i,"id"));
				model.setParent_id(db.getString(i,"parent_id"));
				model.setTitle(db.getString(i,"title"));
				model.setOrderno(db.getString(i,"orderno"));
				model.setType(db.getString(i,"type"));
				model.setOwner_table(db.getString(i,"owner_table"));
				list.add(model);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return list;
    }
    
    /**
     * 获取cs菜单子系统列表
     * @return List<CSMenuModel> 
     * @author: ge.tao
     */
    public List getSubSystems()
    {
    	
    	return  getCSMenuItems("0");
    }
    
    /**
     * 获取cs菜单子系统的菜单条
     * @return List<CSMenuModel> 
     * @author: ge.tao
     */
    public List getMenubarsOfSubSystem(String subsystemid)
    {
    	return  getCSMenuItems(subsystemid);
    
    }
    
    
    /**
     * 获取cs菜单子系统的菜单条的菜单项
     * @return List<CSMenuModel> 
     * @author: ge.tao
     */
    public List getMenubarItemsOfMenubar(String menubarid)
    {
    	return  getCSMenuItems(menubarid);
    }
    
    public boolean hasSon(String parent_id)
    {
    	DBUtil db = new DBUtil();
    	String sql = "select count(id) from v_cs_menu_tree where parent_id='" + parent_id + "'";
    	try {
			db.executeSelect(sql);
			if(db.getInt(0,0) > 0)
			{
				return true;
			}
//			else
//			{
//				List rescList = getRecursionCSMenuItems(parent_id);
//				if(rescList.size()>0)
//				{
//					return true;
//				}
//			}
			return false;
		} catch (SQLException e) 
		{
			return false;
		}
    }
    
    /**
     * 通过CS菜单资源ID,后去菜单资源对象的信息
     * @param id
     * @return 
     * CSMenuManager.java
     * @author: ge.tao
     */
    public CSMenuModel getCSMenuModelInfoById(String id){
    	DBUtil db = new DBUtil();
    	CSMenuModel model = null;
    	String sql = "select id,parent_id,title,orderno,type,owner_table from v_cs_menu_tree" +
    			" where id='" + id + "'";    	
    	try {
			db.executeSelect(sql);
			if(db.size()>0){
				model = new CSMenuModel();
				model.setId(db.getString(0,"id"));
				model.setParent_id(db.getString(0,"parent_id"));
				model.setTitle(db.getString(0,"title"));
				model.setOrderno(db.getString(0,"orderno"));
				String menuType = db.getString(0,"type");
				if("subsys".equalsIgnoreCase(menuType)){
					menuType = "子系统";
				}else if("menu_bar".equalsIgnoreCase(menuType)){
					menuType = "菜单条";
				}else{
					menuType = "菜单项";
				}
				model.setType(menuType);
				model.setOwner_table(db.getString(0,"owner_table"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return model;
    }
    
    /**
     * 通过CS菜单资源ID,获取当前菜单在菜单树上的路径
     * @param menuid
     * @return 
     * CSMenuManager.java
     * @author: ge.tao
     */
    public String getCSMenuPath(String menuid){
    	if(menuid==null || menuid.trim().length()==0) return "";
    	String path = "";
    	String connectStr= "-->";
    	DBUtil db = new DBUtil();
    	String sql = "select title from v_cs_menu_tree start with id='" + menuid + "' " +
    			"connect by  prior parent_id=  id ";
    	try {
			db.executeSelect(sql);
			for(int i=db.size()-1;i>=0;i--){
				String nodeName = db.getString(i,"title");
				if(path.trim().length()==0) 
					path += "CS菜单管理" + connectStr +nodeName;
				else 
					path += connectStr + nodeName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return path;
    }
    
    /**
     * 通过CS菜单资源ID,获取当前菜单在菜单树上的路径
     * @param menuid
     * @return 
     * CSMenuManager.java
     * @author: ge.tao
     */
    public List getCSMenuPathList(String menuid){
    	List list = new ArrayList();
    	
    	if(menuid==null || menuid.trim().length()==0) return null;
//    	String path = "";
//    	String connectStr= "-->";
    	DBUtil db = new DBUtil();
    	String sql = "select * from v_cs_menu_tree start with id='" + menuid + "' " +
    			"connect by  prior parent_id=  id ";
    	try {
			db.executeSelect(sql);
			if(db != null && db.size()>0)
			{
				for(int i=0;i<db.size();i++){
//					String nodeName = db.getString(i,"title");
//					if(path.trim().length()==0) 
//						path += "CS菜单管理" + connectStr +nodeName;
//					else 
//						path += connectStr + nodeName;
					CSMenuModel model = new CSMenuModel();
					model.setId(db.getString(i,"ID"));
					model.setOrderno(db.getString(i,"ORDERNO"));
					model.setOwner_table(db.getString(i,"OWNER_TABLE"));
					model.setParent_id(db.getString(i,"PARENT_ID"));
					model.setTitle(db.getString(i,"title"));
					model.setType(db.getString(i,"TYPE"));
					list.add(model);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
    }
    
    /**
     * 通过report报表菜单资源ID，获取当前菜单在菜单树上的路径
     * @param menuid
     * @return
     * @author gao.tang
     */
	public static String getReportMenuPath(String menuid) {
		StringBuffer sql = new StringBuffer("select r.id,r.name")
    	   .append(" from tb_rpt_basic_info r ")
    	   .append("  start with r.id = '").append(menuid).append("'  connect by prior r.parent_id = r.id");
    	String path = "";
    	String connectStr= "-->";
    	PreparedDBUtil dbUtil = new PreparedDBUtil();
    	try {
    		dbUtil.preparedSelect(REPORT_DATABASE,sql.toString());
    		dbUtil.executePrepared();
			for(int i=dbUtil.size()-1;i>=0;i--){
				String nodeName = dbUtil.getString(i,"name");
				if(path.trim().length()==0) 
					path += "门户查询菜单管理" + connectStr +nodeName;
				else 
					path += connectStr + nodeName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return path;
	}
    
    public static ReportMenus getReportMenus()
    {
    	
    	StringBuffer sql = new StringBuffer("select id,name,status,parent_id,hasChild from (select r.id,r.name,status,case when "); 
    	sql.append("exists (select 1 from tb_rpt_basic_info b where b.id = r.PARENT_ID)")
    	   .append(" then r.parent_id ")
    	   .append(" else '' end parent_id,")
    	   .append("case when ")
    	   .append(" exists (select 1 from tb_rpt_basic_info b where b.parent_id = r.id)")
    	   .append(" then 1")
    	   .append(" else 0 end hasChild")
    	   .append(" from tb_rpt_basic_info r start with r.parent_id = '0011' connect by prior r.id = r.parent_ID")
    	   .append(" order siblings by r.order_no) where status='1' ");
    	PreparedDBUtil dbUtil = new PreparedDBUtil();
    	try {
    		//System.out.println(sql.toString());
			dbUtil.preparedSelect(REPORT_DATABASE,sql.toString());
			dbUtil.executePrepared();
			ReportMenus rptMenus = new ReportMenus();
			
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				ReportMenu rptMenu = new ReportMenu();
				rptMenu.setHasChild(dbUtil.getInt(i,"hasChild") == 0?false:true);
				rptMenu.setParentid(dbUtil.getString(i,"parent_id"));
				rptMenu.setId(dbUtil.getString(i,"id"));
				rptMenu.setName(dbUtil.getString(i,"name"));		
				if(rptMenu.getParentid().equals(""))
				{
					rptMenus.topLevels.add(rptMenu);
				}
				else
				{
					ReportMenu parentMenu = (ReportMenu)rptMenus.getReportMenu(rptMenu.getParentid());
					if(parentMenu != null){
						parentMenu.subReportMenus.add(rptMenu);
					}
				}
				rptMenus.all.put(rptMenu.getId(),rptMenu);
					
			}
			return rptMenus;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ReportMenus();
    }
    
    public static class ReportMenus
    {
    	/**
    	 * 所有菜单项的索引
    	 */
    	Map all = new HashMap();
    	
    	/**
    	 * 顶级节点列表
    	 */
    	List topLevels = new ArrayList();
    	
    	public ReportMenu getReportMenu(String id)
    	{
    		return (ReportMenu)all.get(id);
    	}

    	public List getTopLevels()
    	{
    		return this.topLevels;
    	}
    	
    	
    }
    
    public static class ReportMenu
    {
    	private boolean hasChild = false;
    	private String parentid = null;
    	private String id = null;
    	private String name = null;
    	
    	
    	List subReportMenus = new ArrayList();
		public boolean isHasChild() {
			return hasChild;
		}
		public void setHasChild(boolean hasChild) {
			this.hasChild = hasChild;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getParentid() {
			return parentid;
		}
		public void setParentid(String parentid) {
			this.parentid = parentid;
		}
		public List getSubReportMenus() {
			return subReportMenus;
		}
    	
    	
    }

	public List getReportMenuItems(String parent_id) {
		StringBuffer sql = new StringBuffer("select r.id,r.name,r.parent_id ")
			.append("from tb_rpt_basic_info r start with r.parent_id = '")
			.append(parent_id).append("' connect by prior r.id = r.parent_ID");
    	PreparedDBUtil dbUtil = new PreparedDBUtil();
    	List list = new ArrayList();
    	try {
    		dbUtil.preparedSelect(REPORT_DATABASE,sql.toString());
    		dbUtil.executePrepared();
			for(int i = 0; i < dbUtil.size() ; i++){
				ReportMenu reportMenu = new ReportMenu();
				reportMenu.setId(dbUtil.getString(i, "id"));
				reportMenu.setName(dbUtil.getString(i, "name"));
				reportMenu.setParentid(dbUtil.getString(i, "parent_id"));
				list.add(reportMenu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
    
 

}


