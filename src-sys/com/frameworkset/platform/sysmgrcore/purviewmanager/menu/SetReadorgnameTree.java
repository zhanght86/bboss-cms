package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 机构 可访问税务机关 授权树
 * 
 * @author Administrator gao.tang
 */
// 角色管理资源操作授予－－机构资源授权树
public class SetReadorgnameTree extends COMTree implements Serializable {
	
	String resTypeId = "";

	String roleId = "";

	String roleTypeId = "";

	String currOrgId = "";
	private  Map checks;
	private Map permissionsSource;
	private final static  Object obj = new Object();
	public void setPageContext(PageContext pageContext)
	{
		super.setPageContext(pageContext);
//		permissions = (Map)session.getAttribute("com.frameworkset.platform.sysmgrcore.purviewmanager.menu.SetReadorgnameTree.permissions");
		
		checks = new HashMap();
		permissionsSource = new HashMap();
		
		 resTypeId = request.getParameter("resTypeId");

         roleId = request.getParameter("currRoleId");

		 roleTypeId = request.getParameter("role_type");

		 currOrgId = request.getParameter("currOrgId");
		//是否批量权限授予
		String isBatch = request.getParameter("isBatch");
		if("false".equals(isBatch) && "user".equals(roleTypeId))
		{
			this.checks = super.accessControl.getRoleSelfResource(roleId, resTypeId, roleTypeId,"readorgname");
			permissionsSource = super.accessControl.getSourceUserRes_Role(currOrgId, roleId, resTypeId, "readorgname");
			
		}
//		if(!super.accessControl.isAdmin())
//		{
//			if(permissions == null)
//			{
//				
//				Map temp_permissions = new HashMap();
//				
//				String sql = "select ORG_ID  from V_TB_RES_ORG_USER b where b.OP_ID='readorgname' and b.user_id = ?";
//				
//				PreparedDBUtil db = new PreparedDBUtil();
//				
//				try {
//					db.preparedSelect(sql);
//					db.setString(1, super.accessControl.getUserID());
//					db.executePrepared();	
//					for(int i = 0; i < db.size(); i ++)
//					{
//						temp_permissions.put(db.getString(i, "ORG_ID"), obj);
//					}
//					permissions = temp_permissions;
//					session.setAttribute("com.frameworkset.platform.sysmgrcore.purviewmanager.menu.SetReadorgnameTree.permissions", permissions);
//					
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		
	}
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		if (father.isRoot())
			return true;

		try {
			if (treeID.equals("lisan"))
				return false;
			if (treeID.equals("00"))
				return false;
			return OrgCacheManager.getInstance().hasSubOrg(treeID);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		//对应表td_sm_roleresop中的RESTYPE_ID字段
		String resTypeId = request.getParameter("resTypeId");
		//对应表td_sm_roleresop中的ROLE_ID字段
		String roleId = request.getParameter("currRoleId");
		//对应表td_sm_roleresop中的TYPES字段
		String roleTypeId = request.getParameter("role_type");
		//所选用户对应机构ID
		String currOrgId = request.getParameter("currOrgId");
		//是否批量权限授予
		String isBatch = request.getParameter("isBatch");

		try {

			List orglist = OrgCacheManager.getInstance().getSubOrganizations(
					treeID);

			if (orglist != null) {
				Iterator iterator = orglist.iterator();
				boolean isadmin = this.accessControl.isAdmin() ;
				while (iterator.hasNext()) {
					Organization sonorg = (Organization) iterator.next();
					//System.out.println("sonorg.getRemark5() = " + sonorg.getRemark5());
					String treeName = "";
					String orgId = sonorg.getOrgId();
					String remark5 = sonorg.getRemark5();
					if (remark5 == null
							|| remark5.trim().equals(""))
						treeName = sonorg.getOrgName();
					else
						treeName = remark5;
					Map map = new HashMap();
					map.put("orgId", orgId);
					map.put("resId", orgId);
					map.put("resName", treeName);
					// res_id:restype_id:res_name
					String checkValue = orgId + "#" + resTypeId + "#"
							+ treeName;
					String nodeType = "org";
					// if(AccessControl.hasGrantedRole(roleId,roleTypeId,orgId,resTypeId)){
					// nodeType = "org_true";
					// }else{
					// nodeType = "org";
					// }
					// 已经授权的复选框显示选中状态
					//给机构授权，给用户授权
					if("false".equals(isBatch) && "user".equals(roleTypeId)){
						String ms = (String)this.permissionsSource.get(orgId);
						Object self = (Object)checks.get(orgId);
						boolean isDesabled = false;
						if(ms != null && self == null)
							isDesabled = true;
						if(self != null)
						{
							ms = ms == null?"用户自身拥有资源":"用户自身拥有资源；" + ms;
						}
						if(ms == null)
						{
							ms = "";
						}
						
//						ms = accessControl.getSourceUserRes_jobRoleandRoleandSelf(currOrgId,
//										roleId, treeName, resTypeId,
//										orgId, "readorgname");
//						isDesabled = accessControl.getUserRes_jobRoleandRoleandSelf(currOrgId, 
//										roleId, treeName, resTypeId,
//										orgId, "readorgname");
					
						// System.out.println("ms = " + ms);
						boolean hasper = !"".equals(ms) && ms != null;
						if (hasper) {
							ms = "-->资源来源：" + ms;
						}
						
						
						
//						if(!isadmin)
//							hasper = accessControl.checkPermission(orgId, "readorgname", "orgunit");
						
						if ( isadmin || hasper  
								|| accessControl.isOrganizationManager(orgId)
								|| super.accessControl.checkPermission(orgId,
			                            "readorgname", AccessControl.ORGUNIT_RESOURCE)) {
							if(hasper  )
							{
								if(!isDesabled )
								{
									map.put("node_checkboxchecked", new Boolean(true));
								}
								else {
									map.put("node_checkboxchecked",new Boolean(true));
			     					map.put("node_checkboxdisabled",new Boolean(true));
			     					map.put("node_checkboxname", "isDesabledCheckbox");
								}
							}
							addNode(father, orgId, (treeName + ms).trim(), nodeType, true, curLevel,
									(String) null, (String) orgId,
									checkValue, map);
						} else {
							if (accessControl.isSubOrgManager(orgId)) {
								addNode(father, orgId, treeName, nodeType, false,
										curLevel, (String) null, (String) orgId,
										(String) null, map);
							}
						}
					}
					else //给角色授权，给用户批量授权，给角色批量授权
					{
						/*
						 * 1.管理员本身拥有这个机构访问权限，可以授给其管理的用户
						 * 2.超级管理员可以将所有机构访问权限授给其他用户
						 * 3.管理员可以将自己可管理的机构的可访问权限授给他可管理的用户
						*/
						boolean hasper = accessControl.checkPermission(orgId, "readorgname", "orgunit");
						boolean isCheck = AccessControl.hasGrantedRole(roleId,roleTypeId,orgId,"orgunit");
						//System.out.println("isCheck = " + isCheck);
						if(isCheck){
							map.put("node_checkboxchecked", new Boolean(true));
						}
						if ( isadmin || hasper || accessControl.isOrganizationManager(orgId) ) {
							
							addNode(father, orgId, (treeName).trim(), nodeType, true, curLevel,
									(String) null, (String) orgId,
									checkValue, map);
						} else {
							
							if (accessControl.isSubOrgManager(orgId)) {
								addNode(father, orgId, treeName, nodeType, false,
										curLevel, (String) null, (String) orgId,
										(String) null, map);
							}
						}
					}
				}

				// if(father.isRoot())
				// {
				// Map rootparam = new HashMap();
				// // rootparam.put("nodeLink",request.getContextPath() +
				// "/sysmanager/accessmanager/role/operList_org_ajax.jsp?resTypeId=orgunit");
				// String treeName ="顶级机构";
				// String resId ="00";
				// rootparam.put("resName",treeName);
				// rootparam.put("resId",resId);
				// String nodeType = "lisan";
				// if(AccessControl.hasGrantedRole(roleId,roleTypeId,"00",resTypeId)){
				// nodeType = nodeType + "_true";
				// }else{
				// // nodeType = "org";
				// }
				// // if (accessControl.checkPermission("00",
				// // "addsuborg", AccessControl.ORGUNIT_RESOURCE)) {
				// // addNode(father, resId, treeName,
				// // nodeType, true, curLevel, (String) null,
				// // "00", "00", rootparam);
				// // }
				//	                    
				// Map map = new HashMap();
				// map.put("nodeLink",request.getContextPath() +
				// "/sysmanager/accessmanager/role/operList_org_ajax.jsp?resTypeId=orgunit");
				// treeName ="离散用户管理";
				// resId ="lisan";
				// map.put("resName",treeName);
				// map.put("resId",resId);
				// String lisannodeType = "lisan";
				// if(AccessControl.hasGrantedRole(roleId,roleTypeId,resId,resTypeId)){
				// lisannodeType = "lisan_true";
				// }else{
				//	                    	
				// }
				//	                    
				// if (super.accessControl.checkPermission("lisan",
				// "lisanusermanager", AccessControl.ORGUNIT_RESOURCE) ||
				// super.accessControl.checkPermission("lisan",
				// "lisanusertoorg", AccessControl.ORGUNIT_RESOURCE )) {
				// addNode(father, resId, treeName,
				// lisannodeType, true, curLevel, (String) null,
				// (String) resId, (String) resId, map);
				// }
				//	                    
				//	                    
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
