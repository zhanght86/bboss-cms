<!--菜单资源的操作列表，由于要递归所以分开页面 -->
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.sysmgrcore.manager.CSMenuManager
				,com.frameworkset.platform.sysmgrcore.manager.RoleManager
				,com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl
				,com.frameworkset.platform.framework.MenuHelper
				,com.frameworkset.platform.framework.ModuleQueue
				,com.frameworkset.platform.framework.ItemQueue
				,com.frameworkset.platform.framework.Item
				,com.frameworkset.platform.framework.Module
				,com.frameworkset.platform.sysmgrcore.web.struts.action.RoleManagerAction"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    boolean tag = false;
	
	String[] opid = request.getParameterValues("alloper");
			
	String isRecursion = request.getParameter("isRecursion");
	
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	String resTypeId = request.getParameter("resTypeId");
//	String resTypeId2 = request.getParameter("resTypeId2");
	String resid = request.getParameter("resid");
	String roleid = request.getParameter("roleid");
	String resName = request.getParameter("resName");
	System.out.println("resName:"+resName);
	
	String[] tmp = roleid.split(",");
	String role_type = request.getParameter("role_type");
	String menuPath =  request.getParameter("menuPath");

	request.setAttribute("resTypeId",resTypeId);
	request.setAttribute("resId",resid);
	request.setAttribute("isOk","1");
	//HttpSession session = request.getSession();
	session.setAttribute("currRoleId", roleid);
	session.setAttribute("role_type", role_type);
	
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	List resids = new ArrayList();
			
//	操作和递归为空的话删除当前菜单所有操作,没有选择操作
	if(opid==null || opid.length == 0){
		if(isRecursion.equals("0"))
		{
			
			resids.add(new String[]{resid ,resName});
//			roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
		}
		else if(isRecursion.equals("1"))
		{
			 //add by ge.tao
		    //2007-10-19
		    //CS菜单
		if("cs_column".equalsIgnoreCase(resTypeId)){
//			List resids = new ArrayList();
			resids.add(new String[]{resid ,resName});
			CSMenuManager csmenu = new CSMenuManagerImpl();
//			roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
			List modelList = csmenu.getCSMenuPathList(resid);
			if((modelList != null) || (modelList.size() > 0))
			{
				for(int m=0;m<modelList.size();m++)
				{
					CSMenuModel myModel = (CSMenuModel)modelList.get(m);
					resids.add(new String[]{myModel.getId(), myModel.getTitle()});
//					roleManager.storeRoleresop(opid,myModel.getId(),tmp[k],resTypeId,myModel.getTitle(),role_type);
				}
			}
			
		    List csmenuModels =  csmenu.getRecursionCSMenuItems(resid);
		    for(int i=0;i<csmenuModels.size();i++){
		    	CSMenuModel model = (CSMenuModel)csmenuModels.get(i);
		        //CS子菜单授权
		        //权限判断
		        if (accessControl.checkPermission(model.getId(), "visible", "cs_column"))
		        {
		        	resids.add(new String[]{model.getId(), model.getTitle()});
		        }					    	
		    }		
//			roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);	
					    
			}else{
//				List resids = new ArrayList();
				resids.add(new String[]{resid , resName});
//				roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
				String subsystem1 = Framework.getSubsystemFromPath(menuPath);
				MenuHelper helper = new MenuHelper(subsystem1,accessControl);
				ModuleQueue modules = helper.getSubModules(menuPath);
				ItemQueue items = helper.getSubItems(menuPath);
				for (int i = 0; items != null && i < items.size(); i++) {
					Item item = items.getItem(i);	
					resids.add(new String[]{item.getId() , item.getName()});
					// 子栏目授权							
//					roleManager.deletePermissionOfRole(item.getId(),resTypeId,tmp[k],role_type);
				}
				for (int i = 0; modules != null && i < modules.size(); i++) {
					Module module = modules.getModule(i);
//					module.getId();	
					resids.add(new String[]{module.getId(),module.getName()});
					// 子模块授权
//					roleManager.deletePermissionOfRole(module.getId(),resTypeId,tmp[k],role_type);
					RoleManagerAction rma = new RoleManagerAction();
					rma.recursionMenuids(resids,modules.getModule(i));
				}
//				roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
				}
			}
			tag = roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
		}
			
		//操作不为空和递归为空的话授权当前菜单相关操作（先删后存）
		else if(opid!=null ){
			if(isRecursion.equals("0"))
			{
				if(resTypeId.equals("cs_column")){
	//				List resids = new ArrayList();
					resids.add(new String[]{resid ,resName});
	//				roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
						
	//				roleManager.storeRoleresop(opid,resids,tmp,resTypeId,resid,role_type);
					try{
						CSMenuManager csmenu = new CSMenuManagerImpl();
						List modelList = csmenu.getCSMenuPathList(resid);
						if((modelList != null) || (modelList.size() > 0))
						{
							for(int m=0;m<modelList.size();m++)
							{
								CSMenuModel myModel = (CSMenuModel)modelList.get(m);
								resids.add(new String[]{myModel.getId() ,myModel.getTitle()});
	//							roleManager.storeRoleresop(opid,myModel.getId(),tmp[k],resTypeId,myModel.getTitle(),role_type);
							}
						}
					}catch(Exception e){e.printStackTrace();}
				}else{
	//				List resids = new ArrayList();
					resids.add(new String[]{resid , resName});
	//				roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
	//				roleManager.storeRoleresop(opid,resids,tmp,resTypeId,resid,role_type);
				}
			}
			
				//操作不为空和递归不为空的话授权当前菜单及子站点相关操作（先删后存）
				else if(isRecursion.equals("1")){
					//add by ge.tao
				    //2007-10-19
				    //CS菜单
					if("cs_column".equalsIgnoreCase(resTypeId)){
	//						List resids = new ArrayList();
						resids.add(new String[]{resid , resName});
						CSMenuManager csmenu = new CSMenuManagerImpl();
						
						List modelList = csmenu.getCSMenuPathList(resid);
						
						for(int m=0;modelList != null && m<modelList.size();m++)
						{
							CSMenuModel myModel = (CSMenuModel)modelList.get(m);
							
							resids.add(new String[]{myModel.getId() , myModel.getTitle()});
	//								roleManager.storeRoleresop(opid,myModel.getId(),tmp[k],resTypeId,myModel.getTitle(),role_type);
						}
						
	//						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
	//						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resid,role_type);
						
					    List csmenuModels =  csmenu.getRecursionCSMenuItems(resid);
					    for(int i=0;i<csmenuModels.size();i++){
					    	CSMenuModel model = (CSMenuModel)csmenuModels.get(i);
					    	// CS子栏目授权
					    	//权限判断
					    	if (accessControl.checkPermission(model.getId(), "visible", "cs_column"))
                       		{
					    		resids.add(new String[]{model.getId() , model.getTitle()});
					    	}
	//							roleManager.deletePermissionOfRole(model.getId(),resTypeId,tmp[k],role_type);
	//							
	//							roleManager.storeRoleresop(opid,model.getId(),tmp[k],resTypeId,model.getTitle(),role_type);	
						
							
					    }
					}
					else
					{
						resids.add(new String[]{resid , resName});
	//						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
	//						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resid,role_type);
						try{
							String subsystem2 = Framework.getSubsystemFromPath(menuPath);
							MenuHelper helper = new MenuHelper(subsystem2,accessControl);
							ModuleQueue modules = helper.getSubModules(menuPath);
							ItemQueue items = helper.getSubItems(menuPath);
							for (int i = 0; items != null && i < items.size(); i++) {
								Item item = items.getItem(i);
								
								String resname = item.getName();	
								resids.add(new String[]{item.getId() , resname});
								// 子栏目授权
	//							roleManager.deletePermissionOfRole(item.getId(),resTypeId,tmp[k],role_type);
	//							roleManager.storeRoleresop(opid,item.getId(),tmp[k],resTypeId,resname,role_type);						
							}
							for (int i = 0; modules != null && i < modules.size(); i++) {
								Module module = modules.getModule(i);
	//								module.getId();
								
								String resname = module.getName();
								resids.add(new String[]{module.getId() ,resname});
								// 子模块授权
	//								roleManager.deletePermissionOfRole(module.getId(),resTypeId,tmp[k],role_type);
	//								roleManager.storeRoleresop(opid,module.getId(),tmp[k],resTypeId,resname,role_type);
								RoleManagerAction rma = new RoleManagerAction();
								rma.recursionMenuids(resids,modules.getModule(i));
							}
						}catch(Exception e){
							e.printStackTrace();
						}						
					}
				}

				tag = roleManager.restoreRoleresop(opid,resids,tmp,resTypeId,role_type);
			}
			
if(tag)
{
%>
<SCRIPT LANGUAGE="JavaScript">
alert("授予操作项成功！");
//刷新左边的资源树
parent.parent.orgTree.resource_bridge.location = parent.parent.orgTree.resource_bridge.location;
</SCRIPT>
<%
}
else
{
%>
<SCRIPT LANGUAGE="JavaScript">
alert("授予操作项失败！");
</SCRIPT>
<%
}
%>


