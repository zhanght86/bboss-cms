package com.frameworkset.platform.sysmgrcore.web.struts.action;



import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.web.struts.form.GroupInfoForm;

/**
 * MyEclipse Struts Creation date: 03-15-2006
 * 
 * XDoclet definition:
 * 
 * @struts.action validate="true"
 */
public class GroupInfoAction extends Action implements Serializable{

	private static Logger logger = Logger.getLogger(GroupInfoAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, GroupInfoForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="用户组管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
        
		String flag = "0";

		Group g = null;
		try {
			GroupInfoForm gif = (GroupInfoForm) form;
			GroupManager gm = SecurityDatabase.getGroupManager();
			HttpSession session = request.getSession();			
			switch (gif.getAction()) {
			case 0:
				// 取组信息的操作
				String groupId = request.getParameter("groupId");
				if (groupId != null && !groupId.equals("null")) {
					g = gm.getGroupByID(groupId);
					if (g != null) {
						gif.setGroupId(g.getGroupId()+"");
						gif.setGroupName(g.getGroupName());
						gif.setGroupDesc(g.getGroupDesc());
						gif.setParentId(g.getParentId()+"");
						session.setAttribute("currGroupId", groupId);
					}
					if(g == null){
						gif.setParentId("0");
					}
				}
				
				break;

//			case 1:
//				// 保存组信息的操作
//				Group pg = null;
//				if (!gif.getParentId().equals("0"))
//					pg = gm.getGroup("groupId", gif.getParentId());
//
//				g = new Group();
//				g.setGroupDesc(gif.getGroupDesc());
//				g.setGroupName(gif.getGroupName());
//				g.setParentId(Integer.parseInt(gif.getParentId()));
//				g.setParentGroup(pg);
//
//				if (gif.getGroupId() != null && !gif.getGroupId().equals("0")
//						&& gif.getGroupId().length() > 0) {
//					if (!gif.getGroupId().equals("null"))
//						g.setGroupId(Integer.parseInt(gif.getGroupId()));
//				}
//				// 创建新的用户组
//				if (g.getGroupId() == null) {
//					// 判断是否有相同的组名称
//					Group g1 = gm.getGroup("groupName", gif.getGroupName());
//					if (g1 != null) {
//						logger.info("已经有名称为" + gif.getGroupName() + "的用户组了。");
//						return mapping.findForward("tab");
//					}
//				}
//				if (g.getGroupId() != null) {
//					// 当groupname为disabled时groupname会为null
//					Group tmpg = gm.getGroup("groupId", gif.getGroupId());
//					g.setGroupName(tmpg.getGroupName());
//					gif.setGroupName(tmpg.getGroupName());
//				}
//				if (gm.storeGroup(g)) {
//					
//					//--用户组管理写操作日志	
//					operContent="存储用户组信息,用户组名称："+g.getGroupName(); 						
//					description="";
//					logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
//					//--
//					
//					gif.setGroupId(g.getGroupId()+"");
//					flag = "1";
//				}
//				//新增用户组时给组添加人赋权
//				AccessControl accesscontroler = AccessControl.getInstance();
//		        accesscontroler.checkAccess(request, response);
//		        String roleid = accesscontroler.getUserID();
//		        String resid = gif.getGroupId();
//		        String resname = gif.getGroupName();
//		        String opid1 = "write";
//		        String opid2 = "read";
//		        String resTypeid ="group";
//		        RoleManager roleManager = SecurityDatabase.getRoleManager();
//		    	roleManager.storeRoleresop(opid1,resid,roleid,resTypeid,resname,"user");
//		    	roleManager.storeRoleresop(opid2,resid,roleid,resTypeid,resname,"user");
//		    	//---------------------------
//				request.setAttribute("groupId", g.getGroupId()+"");
//				request.setAttribute("act", "update");
//				request.setAttribute("parentId", g.getParentId()+"");
//				return mapping.findForward("tab");

			case 2:
				// 删除组信息的操作
				
				g = new Group();
				g.setGroupId(Integer.parseInt(gif.getGroupId()));
				g.setGroupName(gif.getGroupName());
				g.setParentId(Integer.parseInt(gif.getParentId()));
				//System.out.println("groupId..........."+g.getGroupId());
				
				//--用户组管理写操作日志	
				operContent="删除用户组,用户组名称："+gif.getGroupName(); 						
				description="";
				logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
				//--
				
				gm.deleteGroup(g);	
				//gm.deleteGroup(g);
				
				
				request.setAttribute("groupId", g.getGroupId()+"");
				request.setAttribute("act", "update");
				request.setAttribute("parentId", g.getParentId()+"");
				return mapping.findForward("tab");
			
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		request.setAttribute("flag", flag);

		return mapping.findForward("tab");
	}
}
