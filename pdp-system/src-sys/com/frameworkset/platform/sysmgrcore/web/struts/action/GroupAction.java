package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.util.EventUtil;

public class GroupAction   implements Serializable{
	private Logger logger = Logger.getLogger(GroupAction.class.getName());

	public GroupAction() {
	}
	
//	用户组管理－－用户组基本信息－－用户组授权情况
	public static String groupRoleOper(String resId,String resTypeId,String opId,String checked,String title,String isRecursion) {
//		System.out.println("resId......."+resId);
//		System.out.println("opId......."+opId);
//		System.out.println("checked........."+checked);
		if(isRecursion==null){
			isRecursion ="0";
		}
		
		String[] tmp = opId.split(";");
		String roleid = tmp[0];
		String opid = tmp[1];
		if (tmp != null && tmp.length == 2) {
			try {
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				boolean sendevent = false;
				if(checked != null && checked.equals("1")){
					sendevent = true;
					roleManager.storeRoleresop(opid,resId,roleid,resTypeId,title,"role",false);
				}else if(checked != null && checked.equals("0")){
					sendevent = true;
					roleManager.deletePermissionOfRole(resId,resTypeId,roleid,"role",false);
				}
			//	递归保存子组
				if (isRecursion.equals("1")) {
					List grouplist = groupManager.getChildGroupList(resId);
					for (int i = 0; grouplist != null && i < grouplist.size(); i++) {
						Group group = (Group) grouplist.get(i);
					
						if(checked != null && checked.equals("1")){
							sendevent = true;
							roleManager.storeRoleresop(opid,String.valueOf(group.getGroupId()),roleid,resTypeId,group.getGroupName(),"role",false);
						}else if(checked != null && checked.equals("0")){
							sendevent = true;
							roleManager.deletePermissionOfRole(String.valueOf(group.getGroupId()),resTypeId,roleid,"role",false);
						}
					}
//				

			 }
			if(sendevent)
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
