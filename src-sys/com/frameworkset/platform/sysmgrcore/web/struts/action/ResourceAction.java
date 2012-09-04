/*
 * Created on 2006-3-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Restype;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;



/**
 * @author ok
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ResourceAction extends BasicAction {
	public ActionForward newresource(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resId = request.getParameter("resId");
		String restypeName = request.getParameter("restypeName");
		request.setAttribute("restypeName", restypeName);
		//System.out.println(restypeName);
		Res res = new Res();
		res.setParentId(resId);
		ResManager resManager = SecurityDatabase.getResourceManager();
		List restypelist = resManager.getResTypeList();
		List list = new ArrayList();
		for (int i = 0; restypelist != null && i < restypelist.size(); i++) {
			Restype restype = (Restype) restypelist.get(i);
			if (restype.getRestypeName().equals("机构资源")
					|| restype.getRestypeName().equals("栏目资源"))
				continue;
			list.add(restype);
		}
		request.setAttribute("newRes", res);
		request.setAttribute("restypelist", list);
		return (mapping.findForward("newresource"));
	}

	public ActionForward saveresource(ActionMapping mapping, Res form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String restypeId=request.getParameter("restypeId");
		request.setAttribute("restypeId",restypeId);
		Res res = (Res) form;
		ResManager resManager = SecurityDatabase.getResourceManager();
		String title = res.getTitle();
		boolean b = false;
		try {
			b = resManager.isResExistitle(title);

		} catch (ManagerException e1) {
			e1.printStackTrace();
		}

		if (b == true) {
			ActionForward forward = new ActionForward();
			forward.setPath("/sysmanager/resmanager/newres.jsp?isSuccess=0");
			return forward;
		}

		Restype restype = new Restype();
		restype.setRestypeId(res.getRestypeId());
		res.setRestype(restype);
		res.setResId(null);

		resManager.storeRes(res);
		
		//--资源管理写操作日志	
		operContent="存储资源: "+res.getTitle(); 
		 description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--

		ActionForward forward = mapping.findForward("resopertionsuccess");
		StringBuffer path = new StringBuffer(forward.getPath());
		boolean isQuery = (path.indexOf("?") >= 0);
		if (isQuery) {
			path.append("&method=getResInfo&resId=" + res.getResId()
					+ "&parentId=" + res.getParentId()
					+ "&restypeId=" + res.getRestypeId()
					+ "&restypeName=" + res.getRestypeName());
		} else {
			path.append("?method=getResInfo&resId=" + res.getResId()
					+ "&parentId=" + res.getParentId()
					+ "&restypeId=" + res.getRestypeId()
					+ "&restypeName=" + res.getRestypeName());
		}
		return new ActionForward(path.toString());

	}

	
	public ActionForward savecmsresource(ActionMapping mapping, Res form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String restypeId=request.getParameter("restypeId");
		request.setAttribute("restypeId",restypeId);
		Res res = (Res) form;
		ResManager resManager = SecurityDatabase.getResourceManager();
		String title = res.getTitle();
		boolean b = false;
		try {
			b = resManager.isResExistitle(title);

		} catch (ManagerException e1) {
			e1.printStackTrace();
		}

		if (b == true) {
			ActionForward forward = new ActionForward();
			forward.setPath("/sysmanager/resmanager/cmsnewres.jsp?isSuccess=0");
			return forward;
		}

		Restype restype = new Restype();
		restype.setRestypeId(res.getRestypeId());
		res.setRestype(restype);
		res.setResId(null);

		resManager.storeRes(res);
		
		//--资源管理写操作日志	
		operContent="存储资源: "+res.getTitle(); 
		 description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
        return mapping.findForward("resopertioncmssuccess");

	}
	
	public ActionForward modifyresource(ActionMapping mapping, Res form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String restypeId=request.getParameter("restypeId");
		request.setAttribute("restypeId",restypeId);
		Res res = (Res) form;
		if (res.getResId() == null) {
			throw new Exception();
		}

		Restype restype = new Restype();
		restype.setRestypeId(res.getRestypeId());
		res.setRestype(restype);
		ResManager resManager = SecurityDatabase.getResourceManager();
		resManager.storeRes(res);
		ActionForward forward = mapping.findForward("resopertionsuccess");
		StringBuffer path = new StringBuffer(forward.getPath());
		boolean isQuery = (path.indexOf("?") >= 0);
		if (isQuery) {
			path.append("&method=getResInfo&resId=" + res.getResId()
					+ "&parentId=" + res.getParentId() + "&action=update"
					+ "&restypeId=" + res.getRestypeId()
					+ "&restypeName=" + res.getRestypeName());
		} else {
			path.append("?method=getResInfo&resId=" + res.getResId()
					+ "&parentId=" + res.getParentId() + "&action=update"
					+ "&restypeId=" + res.getRestypeId()
					+ "&restypeName=" + res.getRestypeName());
		}
		return new ActionForward(path.toString());

	}

	public ActionForward deleteresource(ActionMapping mapping, Res form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
        
		String resId = request.getParameter("resId");
		//System.out.println("resId......"+resId);
		Res res = (Res) form;
		String parentId = res.getParentId();
		res.setResId(resId);
		Restype restype = new Restype();
		restype.setRestypeId(res.getRestypeId());
		res.setRestype(restype);
		ResManager resManager = SecurityDatabase.getResourceManager();
		resManager.deleteRes(res);
		//如果有子资源一起删除---------------
		DBUtil db = new DBUtil();
		String sql="delete from TD_SM_RES where  PARENT_ID ='"+resId+"'";
		
		
		//--资源管理写操作日志	
		operContent="删除资源: "+LogGetNameById.getResNameByResId(resId); 
		 description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
        
        
		db.executeDelete(sql);
		//		-----------------------------
		

		
		ActionForward forward = mapping.findForward("resopertionsuccess");
		StringBuffer path = new StringBuffer(forward.getPath());
		boolean isQuery = (path.indexOf("?") >= 0);
		if (isQuery) {
			path.append("&resId=" + resId + "&parentId=" + parentId
					+ "&restypeId=" + res.getRestypeId()
					+ "&restypeName=" + res.getRestypeName());
		} else {
			path.append("?resId=" + resId + "&parentId=" + parentId
					+ "&restypeId=" + res.getRestypeId()
					+ "&restypeName=" + res.getRestypeName());
		}
		return new ActionForward(path.toString());

	}

	public ActionForward getResInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resId = request.getParameter("resId");
		String restypeId = request.getParameter("restype");
		String restypeName = request.getParameter("restypeName");

		request.setAttribute("restype", restypeId);
		request.setAttribute("restypeName", restypeName);

		ResManager resManager = SecurityDatabase.getResourceManager();
		Res res = null;

		if (resId.equals("0")) {
			res = new Res();
			res.setResId("0");
			res.setTitle("资源树");
		} else {
			res = resManager.getRes("res_Id", resId);
			if (res != null) {
				Restype restype = res.getRestype();
				request.setAttribute("restype", restype);
			}
		}
		List restypelist = resManager.getResTypeList();
		List list = new ArrayList();
		for (int i = 0; restypelist != null && i < restypelist.size(); i++) {
			Restype restype = (Restype) restypelist.get(i);
			if (restype.getRestypeName().equals("机构资源")
					|| restype.getRestypeName().equals("栏目资源"))
				continue;
			list.add(restype);
		}
		request.setAttribute("restypelist", list);
		request.setAttribute("Res", res);

		return mapping.findForward("resinfo");
	}

	public ActionForward modifyInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resId = request.getParameter("resId");
		ResManager resManager = SecurityDatabase.getResourceManager();
		// Res res = null;
		Res res = resManager.getRes("res_Id", resId);
		if (res != null) {
			// res = (Res) list.get(0);
			Restype restype = res.getRestype();
			request.setAttribute("restype", restype);
		}
		List restypelist = resManager.getResTypeList();
		request.setAttribute("restypelist", restypelist);
		request.setAttribute("modifyresinfo", res);

		return mapping.findForward("modifyInfo");
	}

	public ActionForward deleteres(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {// 删除资源
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		ResManager resManager = SecurityDatabase.getResourceManager();
		String[] resId = request.getParameterValues("checkBoxOne");
		

		if (resId != null) {
			for (int i = 0; i < resId.length; i++) {

				// Res res=new Res();
				// res.setResId(resId[i]);
				Res res = resManager.getRes("res_Id", resId[i]);
				resManager.deleteRes(res);
//				DBUtil db = new DBUtil();//如果有子资源一起删除
//				String sql="delete from TD_SM_RES where  PARENT_ID ='"+resId[i]+"'";
//				db.executeDelete(sql);
//				String sql1="delete from TD_SM_ROLERESOP where RES_ID='"+ res.getTitle() +"'";
//				db.executeDelete(sql1);//删除角色中关联资源
				//--资源管理写操作日志	
				operContent="删除资源: "+LogGetNameById.getResNameByResId(resId[i])+"  删除角色中关联资源: "+res.getTitle(); 

				 description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--
			}
		}

		return mapping.findForward("succeed");
	}
	
	
	public ActionForward deleteCmsRes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {// 删除cms资源
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		ResManager resManager = SecurityDatabase.getResourceManager();
		String[] resId = request.getParameterValues("checkBoxOne");
		

		if (resId != null) {
			for (int i = 0; i < resId.length; i++) {

				// Res res=new Res();
				// res.setResId(resId[i]);
				Res res = resManager.getRes("res_Id", resId[i]);
				resManager.deleteRes(res);
				DBUtil db = new DBUtil();//如果有子资源一起删除
				String sql="delete from TD_SM_RES where  PARENT_ID ='"+resId[i]+"'";
				db.executeDelete(sql);
				String sql1="delete from TD_SM_ROLERESOP where RES_ID='"+ res.getTitle() +"'";
				db.executeDelete(sql1);//删除角色中关联资源
				//--资源管理写操作日志	
				operContent="删除资源: "+LogGetNameById.getResNameByResId(resId[i])+"  删除角色中关联资源: "+res.getTitle(); 
		
				 description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--
			}
		}

		return mapping.findForward("cmssucceed");
	}
	
	/**
	 * 获得资源对应的所有角色及操作列表
	 * @param resId
	 * @param restypeId
	 * @return
	 */
//	public static List getRoleOperList(String resId, String restypeId) {
//		List list = null;
//		try {
//			OperManager om = SecurityDatabase.getOperManager();
//			list = om.getRoleOperList(resId,restypeId);
//		} catch (Exception e) {
//			log.error(e);
//			list = null;
//		}
//		return list;
//	}
	public ActionForward getRes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {//
		
		String resId=request.getParameter("resId");

		return mapping.findForward("succeed");
	}
	public ActionForward deleteresId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String resId = request.getParameter("resId");
		ResManager resManager = SecurityDatabase.getResourceManager();
		Res res = resManager.getRes("res_Id", resId);
		
		//--资源管理写操作日志	
		operContent="删除资源: "+LogGetNameById.getResNameByResId(resId)+"   删除角色中关联资源: "+res.getTitle(); 

		 description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
        
		resManager.deleteRes(res);
		DBUtil db = new DBUtil();//如果有子资源一起删除
		String sql="delete from TD_SM_RES where  PARENT_ID ='"+resId+"'";
		db.executeDelete(sql);
		String sql1="delete from TD_SM_ROLERESOP where RES_ID='"+ res.getTitle() +"'";
		db.executeDelete(sql1);//删除角色中关联资源
		

        
		return mapping.findForward("succeed");

	}
	/**
	 * 修改资源内容
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateResource(ActionMapping mapping, Res form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String restypeId=request.getParameter("restypeId");
		String restypeName=request.getParameter("restypeName");
		request.setAttribute("restypeId",restypeId);
		request.setAttribute("restypeName",restypeName);
		Res res = (Res) form;
		String resId = request.getParameter("resId");
		DBUtil db = new DBUtil();
		String oldTitle = request.getParameter("oldTitle");
		String sql ="update td_sm_res set TITLE='"+ res.getTitle() +"',PATH='"+ res.getPath() +"' where" +
			" RES_ID ='" + resId + "'";
		if(!oldTitle.equals(res.getTitle())){
			DBUtil db1 = new DBUtil();
			String sqlselect ="select * from td_sm_res where title ='"+ res.getTitle()+"'";
			db1.executeSelect(sqlselect);
			if(db1.size()>0){
				request.setAttribute("reFlush","true");
				return mapping.findForward("update");
			}
		}
		db.executeUpdate(sql);
		
		//--资源管理写操作日志	
		operContent="修改资源，修改资源为"+LogGetNameById.getResNameByResId(resId)+" 的资源标题为："+res.getTitle(); 
	
		 description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
        
        
		return mapping.findForward("resopertionsuccess");

	}
	
	/**
	 * 修改cms资源内容
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateCmsResource(ActionMapping mapping, Res form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--资源管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String restypeId=request.getParameter("restypeId");
		String restypeName=request.getParameter("restypeName");
		request.setAttribute("restypeId",restypeId);
		request.setAttribute("restypeName",restypeName);
		Res res = (Res) form;
		String resId = request.getParameter("resId");
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql ="update td_sm_res set TITLE='"+ res.getTitle() +"',PATH='"+ res.getPath() +"' where" +
				" RES_ID ='" + resId + "'";
		String sqlselect ="select * from td_sm_res where title ='"+ res.getTitle()+"' and res_id<>'"+resId+"'";
		db1.executeSelect(sqlselect);
		if(db1.size()>0){
			request.setAttribute("reFlush","true");
			return mapping.findForward("cmsupdate");
		}
		db.executeUpdate(sql);
		
		//--资源管理写操作日志	
		operContent="修改资源，修改资源为"+LogGetNameById.getResNameByResId(resId)+" 的资源标题为："+res.getTitle(); 

		 description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
        
        
		return mapping.findForward("cmssucceed");

	}

	
	public ActionForward getAllResList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String[] id = request.getParameterValues("checkBoxOne");
			RoleManager roleManager = SecurityDatabase.getRoleManager();
		    List allRole = roleManager.getRoleList();
			request.setAttribute("allRole", allRole);
			String resTypeId = request.getParameter("restypeName");
			request.setAttribute("resTypeId",resTypeId);
            request.setAttribute("id", id);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		
		return mapping.findForward("accredit");
		
	}
	
	
}
