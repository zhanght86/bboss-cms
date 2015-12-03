package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.CSMenuModel;
import com.frameworkset.platform.sysmgrcore.manager.CSMenuManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl.ReportMenu;
import com.frameworkset.platform.util.EventUtil;

public class MenuAction {
//	菜单管理－－菜单授权情况
	public static String editRoleOper(String resId,String resTypeId,
			String opId,String checked,String title,String isRecursion,String menuPath,String types) {
		return _editRoleOper(  resId,  resTypeId,
				  opId,  checked,  title,  isRecursion,  menuPath,  types,true);
	}
	
	private static String _editRoleOper(String resId,String resTypeId,
			String opId,String checked,String title,String isRecursion,String menuPath,String types,boolean sendevent) {
//		System.out.println("resId......."+resId);
//		System.out.println("opId......."+opId);
		AccessControl control = AccessControl.getAccessControl(); 
		HttpServletRequest request = control.getRequest();
		if(isRecursion==null){
			isRecursion ="0";
		}
//		System.out.println("isRecursion........"+checked);
//		System.out.println("isRecursion........"+menuPath);
		String[] tmp = opId.split(";");
		String roleid = tmp[0];
		String opid = tmp[1];
		if (tmp != null && tmp.length == 2) {
			boolean innersendevent = false;
			try {
			
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				if(checked != null && checked.equals("1")){
					innersendevent = true;
					roleManager.storeRoleresop(opid,resId,roleid,resTypeId,title,"role",false);
				}else if(checked != null && checked.equals("0")){
					innersendevent = true;
					roleManager.deletePermissionOfRole(resId,resTypeId,roleid,"role",false);
				}

				// 递归授权菜单的权限
				if (isRecursion.equals("1")) {
					if(!"cs_column".equalsIgnoreCase(resTypeId) && !"report_column".equalsIgnoreCase(resTypeId)){
						ModuleQueue modules = Framework.getInstance().getSubModules(
								menuPath);
						ItemQueue items = Framework.getInstance().getSubItems(menuPath);
						for (int i = 0; items != null && i < items.size(); i++) {
							Item item = items.getItem(i);
							innersendevent = true;
							// 子栏目授权
							_editRoleOper(item.getId(), resTypeId, opId, 
									checked, item.getName(request),isRecursion,item.getPath(),types,false);
						}
	
						for (int i = 0; modules != null && i < modules.size(); i++) {
							Module module = modules.getModule(i);
							innersendevent = true;
							// 子模块授权
							_editRoleOper(module.getId(), resTypeId, opId, 
									checked, module.getName(request),isRecursion,  module.getPath(),types,false);
						}					
					}
					else if("report_column".equalsIgnoreCase(resTypeId)){
						CSMenuManager csmenu = new CSMenuManagerImpl();
						List reportMenuModels = csmenu.getReportMenuItems(resId);
						for(int i = 0; i < reportMenuModels.size(); i++){
							innersendevent = true;
							ReportMenu reportMenu = (ReportMenu)reportMenuModels.get(i);
							//子报表菜单授权
							_editRoleOper(reportMenu.getId(), resTypeId, opId, 
									checked, reportMenu.getName(),isRecursion,  "",types,false);
						}
					}
					else{
						//add by ge.tao
						//2007-10-19
						//CS菜单递归授权
						CSMenuManager csmenu = new CSMenuManagerImpl();
					    List csmenuModels =  csmenu.getCSMenuItems(resId);
					    for(int i=0;i<csmenuModels.size();i++){
					    	CSMenuModel model = (CSMenuModel)csmenuModels.get(i);
					        //子菜单授权
					    	_editRoleOper(model.getId(), resTypeId, opId, 
									checked, model.getTitle(),isRecursion,  "",types,false);
					    	
					    }
					}
				}
				if(sendevent && innersendevent)
					EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
				return "success";
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		}else
			return "error";
	}

}
