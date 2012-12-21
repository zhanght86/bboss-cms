package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.synchronize.httpclient.ApachePostMethodClient;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.web.struts.form.UserInfoForm;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title:
 * </p> 
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author weilin.pan
 * @version 1.0
 */
public class UserManagerAction extends DispatchAction implements Serializable {
	/**当前时间*/
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime);
	private static Logger logger = Logger.getLogger(UserManagerAction.class
			.getName());

	public ActionForward getUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String orgId = request.getParameter("orgId");

		Organization org = new Organization();
		org.setOrgId(orgId);
		request.getSession().setAttribute("orgId", org.getOrgId());
		return (mapping.findForward("userList"));
	}

	public ActionForward getUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String queryflag = request.getParameter("advQuery");
		String uid = request.getParameter("userId");
	
		Integer userId = Integer.valueOf(uid);
	
		if (userId == null) {
			return mapping.findForward("noUser");
		}
		request.setAttribute("userId",userId.toString());
		User user = null;
		UserManager userManager = SecurityDatabase.getUserManager();
		try {
			user = userManager.getUserById(userId.toString());
			String username = user.getUserRealname();
			request.setAttribute("username",username);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		UserInfoForm userInfoForm = new UserInfoForm();
		userInfoForm.setHomePhone(user.getUserHometel());
		userInfoForm.setUserId(user.getUserId().toString());
		userInfoForm.setUserName(user.getUserName());
		//userInfoForm.setUserPassword(user.getUserPassword());
		//取用户信息的时候对密码解密
		String pwd = EncrpyPwd.decodePassword(user.getUserPassword());
		userInfoForm.setUserPassword(pwd);

		userInfoForm.setUserRealname(user.getUserRealname());
		userInfoForm.setUserSn(user.getUserSn());
		userInfoForm.setUserWorknumber(user.getUserWorknumber());
		userInfoForm.setUserWorktel(user.getUserWorktel());
		userInfoForm.setUserSex(user.getUserSex());
		userInfoForm.setMail(user.getUserEmail());
		userInfoForm.setRemark1(StringUtil.replaceNull(user.getRemark1()));
		userInfoForm.setRemark3(StringUtil.replaceNull(user.getRemark3()));
		if (user.getUserIsvalid() != null)
			userInfoForm.setUserIsvalid(user.getUserIsvalid().intValue());
		 if (user.getRemark2() != null)
		userInfoForm.setShortMobile(user.getRemark2());
		 if (user.getUserPinyin() != null)
		userInfoForm.setUserPinyin(user.getUserPinyin());
		 if (user.getUserMobiletel2() != null)
		userInfoForm.setUserMobiletel2(user.getUserMobiletel2());
		userInfoForm.setMobile(user.getUserMobiletel1());
		userInfoForm.setRemark4(user.getRemark4());
		userInfoForm.setRemark5(user.getRemark5());
		userInfoForm.setPostalCode(user.getUserPostalcode());
		 if (user.getUserFax() != null)
		userInfoForm.setUserFax(user.getUserFax());
		 if (user.getUserOicq() != null)
		userInfoForm.setUserOicq(user.getUserOicq());
		if (user.getUserBirthday() != null)		
			userInfoForm.setUserBirthday(user.getUserBirthday().toString());
		 if (user.getUserAddress() != null)
		userInfoForm.setUserAddress(user.getUserAddress());
		 if (user.getUserIdcard() != null)
		userInfoForm.setUserIdcard(user.getUserIdcard());
		if (user.getUserRegdate() != null)
			userInfoForm.setUserRegdate(user.getUserRegdate().toString());
		if (user.getUserLogincount() != null)
			userInfoForm.setUserLogincount(user.getUserLogincount().intValue());
		userInfoForm.setWorklength(user.getWorklength());
		userInfoForm.setPolitics(user.getPolitics());
		
		String ou="";
		OrgManager orgMgr = SecurityDatabase.getOrgManager();
		List list = orgMgr.getOrgList(user);
		boolean flag = false;
		for(int i=0;i<list.size();i++){
			Organization o = (Organization)list.get(i);
			if(flag)
				ou += "," + o.getOrgName();
			else
			{
				ou += o.getOrgName();
				flag = true;
			}
		}
		userInfoForm.setUserType(user.getUserType());
		userInfoForm.setIstaxmanager(user.getIstaxmanager());
		userInfoForm.setOu(ou);
		request.setAttribute("currUser", userInfoForm);
		request.getSession().setAttribute("currUserForm", userInfoForm);
		request.setAttribute("reFlush", "false");
		request.getSession().setAttribute("currUserId", user.getUserId());

		request.setAttribute("userNameDisable", "true");
		// Forward control to the specified success URI
		// 这个queryflag是查询时显示的用户信息的页面，同用户里面分割开。
		// 具体页面是userinfo2.jsp，当登陆用户是自己时将是userinfo4.jsp页面
		if (queryflag != null && queryflag.equals("1")) {
			// 得到用户的机构列表
			try {
				
					
					userInfoForm.setOu(StringUtil.replaceAll(ou,"\\,",";"));
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		   
			return (mapping.findForward("queryUserInfo"));
		}
		// 离散用户基本信息forward页面 对应userinfo3.jsp
		String dUserInfo = request.getParameter("dUserInfo");
		if (dUserInfo != null && dUserInfo.equals("1")) {
			return (mapping.findForward("discreteUserInfo"));
		}
		// 离散完毕
		// 个人模块个人用户信息跳转页面
		String personinfo = (String) request.getAttribute("personinfo");
		if (personinfo != null) {
			

			return (mapping.findForward("userInfopersoninfo"));

		}
		// 系统管理用户信息跳转页面
		return (mapping.findForward("qtab"));
	}

	public ActionForward getUserInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);

		String userId = accesscontroler.getUserID();
		HttpSession session = request.getSession();
		int iuserid = Integer.parseInt(userId);
		Integer IuserId = new Integer(iuserid);
		session.setAttribute("currUserId", IuserId);
		request.setAttribute("personinfo", "personinfo");
		return getUser_info(mapping, form, request, response);
	}
	
	public ActionForward getUser_info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String queryflag = request.getParameter("advQuery");
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("currUserId");
		
	
		if (userId == null) {
			return mapping.findForward("noUser");
		}
		request.setAttribute("userId",userId.toString());
		User user = null;
		UserManager userManager = SecurityDatabase.getUserManager();
		try {
			user = userManager.getUserById(userId.toString());
			String username = user.getUserRealname();
			request.setAttribute("username",username);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		UserInfoForm userInfoForm = new UserInfoForm(); 
		userInfoForm.setHomePhone(user.getUserHometel());
		userInfoForm.setUserId(user.getUserId().toString());
		userInfoForm.setUserName(user.getUserName());
		//userInfoForm.setUserPassword(user.getUserPassword());
		//取用户信息的时候对密码解密
		String pwd = EncrpyPwd.decodePassword(user.getUserPassword());
		userInfoForm.setUserPassword(pwd);

		userInfoForm.setUserRealname(user.getUserRealname());
		userInfoForm.setUserSn(user.getUserSn());
		userInfoForm.setUserWorknumber(user.getUserWorknumber());
		userInfoForm.setUserWorktel(user.getUserWorktel());
		userInfoForm.setUserSex(user.getUserSex());
		userInfoForm.setMail(user.getUserEmail());
		userInfoForm.setRemark1(StringUtil.replaceNull(user.getRemark1()));
		userInfoForm.setRemark3(StringUtil.replaceNull(user.getRemark3()));
		if (user.getUserIsvalid() != null)
			userInfoForm.setUserIsvalid(user.getUserIsvalid().intValue());
		 if (user.getRemark2() != null)
		userInfoForm.setShortMobile(user.getRemark2());
		 if (user.getUserPinyin() != null)
		userInfoForm.setUserPinyin(user.getUserPinyin());
		 if (user.getUserMobiletel2() != null)
		userInfoForm.setUserMobiletel2(user.getUserMobiletel2());
		userInfoForm.setMobile(user.getUserMobiletel1());
		userInfoForm.setRemark4(user.getRemark4());
		userInfoForm.setRemark5(user.getRemark5());
		userInfoForm.setPostalCode(user.getUserPostalcode());
		 if (user.getUserFax() != null)
		userInfoForm.setUserFax(user.getUserFax());
		 if (user.getUserOicq() != null)
		userInfoForm.setUserOicq(user.getUserOicq());
		if (user.getUserBirthday() != null)		
			userInfoForm.setUserBirthday(user.getUserBirthday().toString());
		 if (user.getUserAddress() != null)
		userInfoForm.setUserAddress(user.getUserAddress());
		 if (user.getUserIdcard() != null)
		userInfoForm.setUserIdcard(user.getUserIdcard());
		if (user.getUserRegdate() != null)
			userInfoForm.setUserRegdate(user.getUserRegdate().toString());
		if (user.getUserLogincount() != null)
			userInfoForm.setUserLogincount(user.getUserLogincount().intValue());
		
		userInfoForm.setIstaxmanager(user.getIstaxmanager());
		userInfoForm.setWorklength(user.getWorklength());
		userInfoForm.setPolitics(user.getPolitics());
		
		String ou="";
		OrgManager orgMgr = SecurityDatabase.getOrgManager();
		List list = orgMgr.getOrgList(user);
		boolean flag = false;
		for(int i=0;i<list.size();i++){
			Organization o = (Organization)list.get(i);
			if(flag)
				ou += "," + o.getOrgName();
			else
			{
				ou += o.getOrgName();
				flag = true;
			}
		}
		userInfoForm.setUserType(user.getUserType());
		userInfoForm.setOu(ou);

		request.setAttribute("currUser", userInfoForm);
		request.getSession().setAttribute("currUserForm", userInfoForm);
		request.setAttribute("reFlush", "false");
		request.getSession().setAttribute("currUserId", user.getUserId());

		request.setAttribute("userNameDisable", "true");
		// Forward control to the specified success URI
		// 这个queryflag是查询时显示的用户信息的页面，同用户里面分割开。
		// 具体页面是userinfo2.jsp，当登陆用户是自己时将是userinfo4.jsp页面
		if (queryflag != null && queryflag.equals("1")) {
			// 得到用户的机构列表
			try {
				
					
					userInfoForm.setOu(StringUtil.replaceAll(ou,"\\,",";"));
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		   
			return (mapping.findForward("queryUserInfo"));
		}
		// 离散用户基本信息forward页面 对应userinfo3.jsp
		String dUserInfo = request.getParameter("dUserInfo");
		if (dUserInfo != null && dUserInfo.equals("1")) {
			return (mapping.findForward("discreteUserInfo"));
		}
		// 离散完毕
		// 个人模块个人用户信息跳转页面
		String personinfo = (String) request.getAttribute("personinfo");
		if (personinfo != null) {
			

			return (mapping.findForward("userInfopersoninfo"));

		}
		// 系统管理用户信息跳转页面
		return (mapping.findForward("qtab"));
	}

//	public ActionForward storeUser(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		ActionForward forward = new ActionForward();
//		String forwardStr = "";	
//		try {
//			String orgId = (String) request.getSession().getAttribute("orgId");
//			
//			request.setAttribute("orgId",orgId);
//			UserInfoForm userInfoForm = (UserInfoForm) form;
//			//--新增用户是记录日志
//			AccessControl control = AccessControl.getInstance();
//			control.checkAccess(request,response);
//			String operContent="";        
//	        String operSource=control.getMachinedID();//control.getMachinedID();
//	        String openModle="用户管理";
//	        String userName = control.getUserName();
//	        LogManager logManager = SecurityDatabase.getLogManager(); 
//			operContent=userName +" 新增了用户: "+userInfoForm.getUserName(); 
//	        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
//	        //-------------
//			int newFlag = 0;
//
//			User user = new User();
//			if (userInfoForm.getUserId().equals("")) {
//				userInfoForm.setUserId(null);
//				newFlag = 1;
//			}
//			user.setUserId(userInfoForm.getUserId() != null ? Integer
//					.valueOf(userInfoForm.getUserId()) : null);
//			user.setUserName(userInfoForm.getUserName());
//			user.setUserPassword(userInfoForm.getUserPassword());
//			user.setUserRealname(userInfoForm.getUserRealname());
//			user.setUserSn(userInfoForm.getUserSn());
//			user.setRemark1(userInfoForm.getRemark1());
//			user.setRemark3(userInfoForm.getRemark3());
//			user.setUserSex(userInfoForm.getUserSex());
//			
//			user.setUserIsvalid(new Integer(userInfoForm.getUserIsvalid()));
//			user.setUserHometel(userInfoForm.getHomePhone());
//			user.setUserMobiletel1(userInfoForm.getMobile());
//			user.setUserPostalcode(userInfoForm.getPostalCode());
//			user.setRemark2(userInfoForm.getShortMobile());
//			user.setUserEmail(userInfoForm.getMail());
//			user.setUserMobiletel2(userInfoForm.getUserMobiletel2());
//			user.setRemark4(userInfoForm.getRemark4());
//			user.setRemark5(userInfoForm.getRemark5());
//		
//
//			if (userInfoForm.getUserType().length() > 0) {
//				user.setUserType(userInfoForm.getUserType());
//			}
//			user.setUserPinyin(userInfoForm.getUserPinyin());
//			user.setUserWorktel(userInfoForm.getUserWorktel());
//			user.setUserFax(userInfoForm.getUserFax());
//			user.setUserOicq(userInfoForm.getUserOicq());
//			if (userInfoForm.getUserBirthday() != null
//					&& userInfoForm.getUserBirthday().length() > 0)
//				user.setUserBirthday(Date.valueOf(userInfoForm
//						.getUserBirthday()));
//
//			user.setUserAddress(userInfoForm.getUserAddress());
//
//			user.setUserIdcard(userInfoForm.getUserIdcard());
//			if (userInfoForm.getUserRegdate() != null
//					&& userInfoForm.getUserRegdate().length() > 0)
//			{
//				user.setUserRegdate(Date.valueOf(userInfoForm.getUserRegdate()));
//
//			}
//			else
//			{
//				
//				user.setUserRegdate(Date.valueOf(riqi));
//			}
//							
//			if (userInfoForm.getUserLogincount()>0) {
//				user.setUserLogincount(new Integer(userInfoForm.getUserLogincount()));
//						
//			} else
//				user.setUserLogincount(new Integer(0));
//			
//			user.setIstaxmanager(userInfoForm.getIstaxmanager());
//			user.setWorklength(userInfoForm.getWorklength());
//			user.setPolitics(userInfoForm.getPolitics());
//			// add end
//			UserManager userManager = SecurityDatabase.getUserManager();
//
//			// 增加：判断用户是否存在，存在则转入操作失败页面
//			// 修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名
//			if (user.getUserId() == null) {
//				if (userManager.isUserExist(user)) {
//					// return mapping.findForward("fail");
//					userInfoForm.setUserName("");
//					request.setAttribute("currUser", userInfoForm);
//					request.setAttribute("isUserExist", "true");
//					request.setAttribute("reFlush", "false");
//					return mapping.findForward("userInfo");
//				}
//
//			} else {
//				// add by pwl 增加修改邮箱用户密码
//				String mailValidata = ConfigManager.getInstance()
//						.getConfigValue("mailValidata");
//				if (mailValidata != null && mailValidata.equals("1")) {
//					User oldUser = userManager.getUserById(user.getUserId().toString());
//					if (!oldUser.getUserPassword().equals(
//							user.getUserPassword())) {
//						String url = "http://"
//								+ ConfigManager.getInstance().getConfigValue(
//										"mailServer")
//								+ "/creator_changepw.asp?username="
//								+ user.getUserEmail() + "&pw1="
//								+ user.getUserPassword();
//						ApachePostMethodClient client = new ApachePostMethodClient(
//								url);
//						try {
//							String clientResponse = client.sendRequest();
//						} catch (Exception ex) {
//							System.out.println(ex.toString());
//						} finally {
//							client = null;
//						}
//					}
//				}
//			}			
//	
//			userManager.creatorUser(user,orgId,"1");	
//			userInfoForm.setUserId(user.getUserId().toString());
//			request.setAttribute("currUser", userInfoForm);
//			request.getSession().setAttribute("currUserForm", userInfoForm);
//			request.getSession().setAttribute("currUserId",
//					Integer.valueOf(userInfoForm.getUserId()));
//			request.setAttribute("userNameDisable", "true");
//			request.setAttribute("userId",userInfoForm.getUserId());
//			request.setAttribute("ischecked","checked");
//		
//			String persontype = request.getParameter("person");
//			if (persontype != null && persontype.equals("person")) {
//				return mapping.findForward("userInfopersoninfo");
//				//forwardStr = "userInfopersoninfo";
//			}
//			String addcmsuser = request.getParameter("addcmsuser");
//			if (addcmsuser != null && addcmsuser.equals("addcmsuser")) {//用于标识CMS中的用户管理
//				return mapping.findForward("addcmsuser");
//				//forwardStr = "addcmsuser";
//			}			
//			//return (mapping.findForward("userList"));
//			//增加"&isSucceed=1"判断增加用户成功操作，gao.tang
//			forwardStr = "/sysmanager/user/userList.jsp?orgId="+orgId+"&isSucceed=1";
//		} catch (ManagerException e) {
//			logger.error(e);
//			return mapping.findForward("fail");
//			//forwardStr = "fail";
//		}
//		forward.setPath(forwardStr);
//		forward.setRedirect(true);
//		return forward;
//	}

	public ActionForward deleteUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		
		String userNameC = control.getUserName();
		
		String orgId = null;
		if(orgId == null){//内容管理中的orgId
			orgId = request.getParameter("orgId");
		}
		request.setAttribute("orgId",orgId);
		UserManager userManager = SecurityDatabase.getUserManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org1 = orgManager.getOrgById(orgId);
		String orgname = "";
		if(org1==null)
		{
			orgname ="离散用户";
		}
		else
		{
			orgname = org1.getOrgName();
		}
		String[] id = request.getParameterValues("checkBoxOne");
		DBUtil db = new DBUtil();
		//添加，在离散用户删除成功后的forward
		String disuser = request.getParameter("disuser");
		// CMS标识 ，add by xinwang.jiao
		String delcmsuser = request.getParameter("delcmsuser");
		//删除用户时没有成功的用户信息
		String userNamesNo = "";
		//删除操作开始
		if (id != null) {
			User user = null;
			//判断是否税管员
			for(int i = 0; i < id.length; i++){
				boolean state = userManager.isTaxmanager(id[i]);
				user = userManager.getUserById(id[i]);
				String userName = user.getUserName();
				if(state){
					ActionForward forward = new ActionForward();
					//如果移除的用户为自己则返回 forward。。。isAdminOrSelf=true页面提示标志
					forward.setPath("/sysmanager/user/userList.jsp?istaxmanager=true&taxmanagerName="+userName);
					forward.setRedirect(true);
					return forward;
				}
			}
			
			//(1)循环一次，过滤系统管理员与自己
			for (int i = 0; i < id.length; i++) {
				user = userManager.getUserById(id[i]);//获取用户，判断用户id是否为"1(超级管理员)"与用户帐号是否为当前用户
				if(String.valueOf(user.getUserId()).equals("1") || control.getUserAccount().equals(user.getUserName()))
				{
					ActionForward forward = new ActionForward();
					String parameters = request.getParameter("queryString");
					//如果移除的用户为自己则返回 forward。。。isAdminOrSelf=true页面提示标志
					forward.setPath("/sysmanager/user/userList.jsp?isSelf=true&orgId=" + orgId + "&" + parameters);
					forward.setRedirect(true);
					return forward;
				}
//				if(String.valueOf(user.getUserId()).equals("1")){
//					if(disuser != null && "1".equals(disuser) ){
//						ActionForward forward = new ActionForward();
//						//String parameters = request.getParameter("queryString");
//						//如果删除的用户为超级管理员与自己则返回 forward。。。isAdminOrSelf=true页面提示标志
//						forward.setPath("/sysmanager/user/discreteUserList.jsp?isAdmin=true");
//						forward.setRedirect(true);
//						return forward;
//					}
//				}
				
			}
			//(2)循环二次
			
			for (int i = 0; i < id.length; i++) {
				user = userManager.getUserById(id[i]);
				String userOrgInfo = userManager.userOrgInfo(control,user.getUserId()+"");
				if(null != userOrgInfo && !"".equals(userOrgInfo)){
					if("".equalsIgnoreCase(userNamesNo)){
						userNamesNo += user.getUserRealname() + "("+ user.getUserName() +")" +":"+userOrgInfo;
					}else{
						userNamesNo += "\\n" + user.getUserRealname()+":"+userOrgInfo;
					}
					continue;
				}
				String sql ="select count(*) from td_sm_orguser where org_id ='"+ orgId +"' and user_id ="+ id[i] +"";
				//判断删除的该用户是否隶属多个机构 gao.tang
				if(orgManager.getOrgList(user) != null && orgManager.getOrgList(user).size()>1){
					db.executeSelect(sql);
					if(db.getInt(0,0)>0){//如果db.getInt(0,0)的值大于0，则该机构是该用户的主机构且该用户存在于其他机构--不能删除
						String userNameMain = user.getUserName();
						//则当用户还存在其他隶属机构时就不能删除主机构，必须为他指定另一个主机构才能删除 gao.tang 2007.11.14
						ActionForward forward = new ActionForward();
						//如果该用户的主机构是该机构且其他机构下也存在该用户则返回forward。。。isMain=main页面提示标志
						if(disuser != null && "1".equals(disuser) && delcmsuser.equals("delcmsuser")){
							forward.setPath("/cms/orgManage/org_userlist.jsp?userNameMain="+userNameMain+"&isMain=main");
						}else{
							forward.setPath("/sysmanager/user/userList.jsp?userNameMain="+userNameMain+"&isMain=main");
						}
						forward.setRedirect(true);
						return forward;
					}
					Organization org=new Organization();
					org.setOrgId(orgId);
					
					//--从机构中删除用户记录日志
					String operContent="";        
			        String operSource=control.getMachinedID();
			        String openModle="用户管理";
			        LogManager logManager = SecurityDatabase.getLogManager(); 
					operContent=userNameC +" 从机构： "+ orgname +" 中删除了用户: "+user.getUserName(); 
			        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
			        //-------------
			        //删除该机构下的用户，该用户在其他机构下存在---关联表关系（td_sm_userjoborg,td_sm_orgmanager）
					userManager.deleteUserjoborg(org,user);
				}else{//用户不存在其他机构下时
					List list = userManager.getUserjoborgList(id[i],orgId);
					if(list.size()>=1){
						//-----如果该用户的主要单位为该机构，而且没有其他隶属机构时，删除用户和该机构关系
						db.executeSelect(sql);
						if(db.getInt(0,0)>0){
							//处理主机构情况使用统一接口resetUserMainOrg,weida---
							//gao.tang 2007.11.19删除主机构,重置主机构接口废除---关联表关系（td_sm_orguser）
							orgManager.deleteMainOrgnazitionOfUser(id[i]);
							//userManager.resetUserMainOrg(id[i], orgId);
						}
						Organization org=new Organization();
						org.setOrgId(orgId);
						
//						if("3".equals(request.getParameter("isRes"))){
//				        	ActionForward forward = new ActionForward();
//							//如果该用户的主机构是该机构且其他机构下也存在该用户则返回forward。。。isMain=main页面提示标志
//							forward.setPath("/sysmanager/user/userList.jsp?isRes=" + request.getParameter("isRes") +"&checkBoxOne="+id[i]);
//							forward.setRedirect(true);
//							return forward;
//				        }
						if("4".equals(request.getParameter("isRes")))
						{
							userManager.deleteUser(user);
							
							//--从机构中彻底删除用户记录日志
							String operContent="";        
					        String operSource=control.getMachinedID();
					        String openModle="用户管理";
					        
					        LogManager logManager = SecurityDatabase.getLogManager(); 
							operContent=userNameC +" 从机构： "+ orgname +" 中彻底删除了用户: "+user.getUserName(); 
					        logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");       
					        //-------------
						}
						else
						{
							if("3".equals(request.getParameter("isRes"))){
					        	if(!String.valueOf(user.getUserId()).equals("1")){//如果不是系统管理员则删除资源
					        		userManager.deleteUserRes(user);
					        	}else{//将系统管理员变为离散用户
					        		userManager.deleteUserjoborg(org,user);
					        	}
					        }
					        if("2".equals(request.getParameter("isRes"))){
					        	userManager.deleteUserjoborg(org,user);
					        }
					        
							//--从机构中删除用户记录日志
							String operContent="";        
					        String operSource=control.getMachinedID();
					        String openModle="用户管理";
					        
					        LogManager logManager = SecurityDatabase.getLogManager(); 
							operContent=userNameC +" 从机构： "+ orgname +" 中删除了用户: "+user.getUserName(); 
					        logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");       
					        //-------------
					        //删除变成离散用户
						}
				        
					}else
					{
						//-----如果该用户的主要单位为该机构，删除用户和该机构关系
						db.executeSelect(sql);
						if(db.getInt(0,0)>0){
							
//							处理主机构情况使用统一接口resetUserMainOrg,weida
//							orgManager.deleteMainOrgnazitionOfUser(id[i]);
							userManager.resetUserMainOrg(id[i], orgId);
							
						}
						//--删除用户是记录日志
						
						String operContent="";        
				        String operSource=control.getMachinedID();
				        String openModle="用户管理";
				        
				        LogManager logManager = SecurityDatabase.getLogManager(); 
						operContent=userNameC +" 删除了用户: "+user.getUserName(); 
				        logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");       
				        //-------------
						userManager.deleteUser(user);
					}
					//判断是否为内容管理用户删除，如果是则彻底删除
					if(disuser != null && disuser.equals("1")){
						//--删除用户是记录日志
						String operContent="";        
				        String operSource=control.getMachinedID();
				        String openModle="组织管理";
				        
				        LogManager logManager = SecurityDatabase.getLogManager(); 
						operContent=userNameC +" 删除了用户: "+user.getUserName(); 
				        logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");       
				        //-------------
						userManager.deleteUser(user);
					}
				}
			}
		}
		request.setAttribute("reFlush", "true");
		ActionForward af = mapping.findForward("userList");
		String path = af.getPath();
		
		
		if (disuser != null && disuser.equals("1")) {
			if(delcmsuser != null && delcmsuser.equals("delcmsuser"))
			{
				ActionForward forward = mapping.findForward("cmsdiscreteUserList");
				String pathstr = forward.getPath();
				String parameters = request.getParameter("queryString");
				pathstr = pathstr + "?" + parameters + "&isSuccess=1";
				ActionForward redirect = new ActionForward(pathstr);
				redirect.setRedirect(true);
				return redirect;
				//return (mapping.findForward("cmsdiscreteUserList"));
			}
			return (mapping.findForward("discreteUserList"));
		}
		// 添加结束
		String params = request.getParameter("queryString");
		if("1".equals(request.getParameter("isRes")) || "2".equals(request.getParameter("isRes"))){
			params += "&orgId="+orgId; 
		}
		//增加“delSucceed=2”判断删除成功操作，gao.tang
		String realUrl = path + "?" + params + "&delSucceed=2";
		ActionForward redirect = new ActionForward(realUrl);
		HttpSession session = request.getSession();
		session.removeAttribute("promptString");
		session.setAttribute("promptString", userNamesNo);
		
		redirect.setRedirect(true);//重新刷新页面,da.wei
		
		return redirect;
		//return (mapping.findForward("userList"));
	}

	public ActionForward newUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("currUser", null);
		request.setAttribute("reFlush", "false");
		request.getSession().setAttribute("currUserId", null);
		request.setAttribute("isNew", "1");
		// Forward control to the specified success URI
		return (mapping.findForward("userInfo"));
	}

//	/**
//	 * 用户组中支持用户查询
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward userQuery(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		UserInfoForm userInfoForm = (UserInfoForm) form;
//		String userName = userInfoForm.getUserName();
//		// 没有关键字
//		if (userName == null) {
//			request.setAttribute("hasKey", "0");
//			return mapping.findForward("userQueryResult");
//		} else {
//			request.setAttribute("hasKey", "1");
//			request.setAttribute("userName", userName);
//			return mapping.findForward("userQueryResult");
//		}
//
//	}

//	/**
//	 * 机构管理中新增用户时调用的方法
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward storeUser1(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		try {
//			String orgId = request.getParameter("orgId");
//			UserInfoForm userInfoForm = (UserInfoForm) form;
//			//--新增用户是记录日志
//			AccessControl control = AccessControl.getInstance();
//			control.checkAccess(request,response);
//			String operContent="";        
//	        String operSource=control.getMachinedID();
//	        String openModle="用户管理";
//	        String userName = control.getUserName();
//	        LogManager logManager = SecurityDatabase.getLogManager(); 
//			operContent=userName +" 新增了用户: "+userInfoForm.getUserName(); 
//			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
//	        //-------------
//
//			int newFlag = 0;
//			User user = new User();
//			if (userInfoForm.getUserId().equals("")) {
//				userInfoForm.setUserId(null);
//				newFlag = 1;
//			}
//			user.setUserId(userInfoForm.getUserId() != null ? Integer
//					.valueOf(userInfoForm.getUserId()) : null);
//			user.setUserName(userInfoForm.getUserName());
//			user.setUserPassword(userInfoForm.getUserPassword());
//			user.setUserRealname(userInfoForm.getUserRealname());
//			user.setUserSn(userInfoForm.getUserSn());
//			user.setUserSex(userInfoForm.getUserSex());
//			
//			user.setUserIsvalid(new Integer(userInfoForm.getUserIsvalid()));
//			user.setUserHometel(userInfoForm.getHomePhone());
//			user.setUserMobiletel1(userInfoForm.getMobile());
//			user.setUserPostalcode(userInfoForm.getPostalCode());
//		
//			user.setRemark1(userInfoForm.getRemark1());
//			user.setRemark3(userInfoForm.getRemark3());
//			user.setUserEmail(userInfoForm.getMail());
//			user.setRemark2(userInfoForm.getShortMobile());
//			user.setUserMobiletel2(userInfoForm.getUserMobiletel2());
//			user.setRemark4(userInfoForm.getRemark4());
//			user.setRemark5(userInfoForm.getRemark5());
//
//			user.setUserType(userInfoForm.getUserType());
//
//			user.setUserPinyin(userInfoForm.getUserPinyin());
//
//			user.setUserWorktel(userInfoForm.getUserWorktel());
//
//			user.setUserFax(userInfoForm.getUserFax());
//
//			user.setUserOicq(userInfoForm.getUserOicq());
//			if (userInfoForm.getUserBirthday() != null
//					&& userInfoForm.getUserBirthday().length() > 0)
//				user.setUserBirthday(Date.valueOf(userInfoForm
//						.getUserBirthday()));
//
//			user.setUserAddress(userInfoForm.getUserAddress());
//
//			user.setUserIdcard(userInfoForm.getUserIdcard());
//			if (userInfoForm.getUserRegdate() != null
//					&& userInfoForm.getUserRegdate().length() > 0)
//			{
//				user.setUserRegdate(Date.valueOf(userInfoForm.getUserRegdate()));
//			}
//			else
//			{
//				user.setUserRegdate(Date.valueOf(riqi));
//			}
//				
//			if (request.getParameter("userLogincount")!=null) {
//				user.setUserLogincount(new Integer(request.getParameter("userLogincount")));
//						
//			} else
//				user.setUserLogincount(new Integer(0));
//			
//			user.setIstaxmanager(userInfoForm.getIstaxmanager());
//			user.setWorklength(userInfoForm.getWorklength());
//			user.setPolitics(userInfoForm.getPolitics());
//			// add end
//			UserManager userManager = SecurityDatabase.getUserManager();
//
//			// 增加：判断用户是否存在，存在则转入操作失败页面
//			// 修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名
//			if (user.getUserId() == null) {
//				if (userManager.isUserExist(user)) {
//					// return mapping.findForward("fail");
//					userInfoForm.setUserName("");
//					request.setAttribute("currUser", userInfoForm);
//					request.setAttribute("isUserExist", "true");
//					request.setAttribute("reFlush", "false");
//					return mapping.findForward("userInfo1");
//				}
//
//			} else {
//				// add by pwl 增加修改邮箱用户密码
//				String mailValidata = ConfigManager.getInstance()
//						.getConfigValue("mailValidata");
//				if (mailValidata != null && mailValidata.equals("1")) {
//					User oldUser = userManager.getUserById(user.getUserId().toString());
//					if (!oldUser.getUserPassword().equals(
//							user.getUserPassword())) {
//						String url = "http://"
//								+ ConfigManager.getInstance().getConfigValue(
//										"mailServer")
//								+ "/creator_changepw.asp?username="
//								+ user.getUserEmail() + "&pw1="
//								+ user.getUserPassword();
//						ApachePostMethodClient client = new ApachePostMethodClient(
//								url);
//						try {
//							String clientResponse = client.sendRequest();
//						} catch (Exception ex) {
//							System.out.println(ex.toString());
//						} finally {
//							client = null;
//						}
//					}
//				}
//			}
//			userManager.updateUser(user);
//			
//			// 增加用户主要所属单位到td_sm_orguser表2006-08-17
//			OrgManager orgmanager = SecurityDatabase.getOrgManager();
//			String userId = user.getUserId().toString();
//			orgmanager.addMainOrgnazitionOfUser(userId,orgId);
//			//-----------------------------------------------
//			//如果机构下没有岗位，默认添加待岗岗位
//			DBUtil db = new DBUtil();
//			DBUtil db1 = new DBUtil();
//			String sql ="select count(*) from td_sm_orgjob where org_id='"+ orgId +"'";
//			db.executeSelect(sql);
//			if(db.getInt(0,0)<1){
//				String addorgjob ="insert into td_sm_orgjob(org_id,job_id,job_sn) values('"+ orgId +"','1','1')";
//				db1.executeInsert(addorgjob);
//			}
//			//------------------------------------------------------
//			userInfoForm.setUserId(user.getUserId().toString());
//			if (newFlag == 1) {
//				JobManager jobmanager = SecurityDatabase.getJobManager();
////				Job job = jobmanager.getJob("jobId", "1");
//				Job job = jobmanager.getJobById("1");
//
//				Userjoborg userjoborg = new Userjoborg();
//				// 修改：需要完整 userjoborg 对象
//				OrgManager orgMgr = SecurityDatabase.getOrgManager();
//				Organization org = orgMgr.getOrgById(orgId);
////				-----------------------------------------------------
//				String sqlsn ="select max(same_job_user_sn) as sn from  td_sm_userjoborg" +
//							  " where job_id ='1' and org_id ='"+ orgId +"'";
//				DBUtil dbutil = new DBUtil();
//				dbutil.executeSelect(sqlsn);
//				int samesn;
//				
//				if (dbutil != null && dbutil.getInt(0,0) > 0) {
//						samesn=dbutil.getInt(0,"sn")+1;
//					    userjoborg.setUser(user);
//						userjoborg.setJob(job);
//						userjoborg.setOrg(org);
//						userjoborg.setJobSn(Integer.valueOf("999"));
//						userjoborg.setSameJobUserSn(new Integer(samesn));
//						userjoborg.setStartTime(new Timestamp(new java.util.Date().getTime()));
//						userjoborg.setFettle(new Integer(1));
//						userManager.storeUserjoborg(userjoborg);
//
//				}else{
//					
//					samesn = 1;
//				    userjoborg.setUser(user);
//					userjoborg.setJob(job);
//					userjoborg.setOrg(org);
//					userjoborg.setJobSn(Integer.valueOf("999"));
//					userjoborg.setSameJobUserSn(new Integer(samesn));
//					userjoborg.setStartTime(new Timestamp(new java.util.Date().getTime()));
//					userjoborg.setFettle(new Integer(1));
//					userManager.storeUserjoborg(userjoborg);
//				}
//			}
//			request.setAttribute("currUser", userInfoForm);
//			request.getSession().setAttribute("currUserId",
//					userInfoForm.getUserId());
//			request.setAttribute("userNameDisable", "true");
//
//			return mapping.findForward("orgsubuserlist");
//
//		} catch (ManagerException e) {
//			logger.error(e);
//			return mapping.findForward("fail");
//		}
//	}

//	/**
//	 * 用户的高级查询
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward advQueryUser(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		UserInfoForm userInfoForm = (UserInfoForm) form;
//		HttpSession session = request.getSession();
//		request.setAttribute("advQuery", "1");
//		request.setAttribute("advUserform", userInfoForm);
//		session.setAttribute("advUserform", userInfoForm);
//		return (mapping.findForward("advQuerytab"));
//	}

//	/**
//	 * 保存离散用户的资料
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward storeDiscreteUser(ActionMapping mapping,
//			ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		try {
//			String qqstring = request.getParameter("qstring");
//
//			UserInfoForm userInfoForm = (UserInfoForm) form;
//			User user = new User();
//			user.setUserId(userInfoForm.getUserId() != null ? Integer
//					.valueOf(userInfoForm.getUserId()) : null);
//			user.setUserName(userInfoForm.getUserName());
//			user.setUserPassword(userInfoForm.getUserPassword());
//			user.setUserRealname(userInfoForm.getUserRealname());
//			user.setUserSn(userInfoForm.getUserSn());
//			// add by feng.jing
//			// user.setUserWorknumber(userInfoForm.getUserWorknumber());
//			user.setUserSex(userInfoForm.getUserSex());
//			user.setUserIsvalid(new Integer(userInfoForm.getUserIsvalid()));
//			user.setUserHometel(userInfoForm.getHomePhone());
//			user.setUserMobiletel1(userInfoForm.getMobile());
//			user.setUserPostalcode(userInfoForm.getPostalCode());
//			user.setRemark2(userInfoForm.getShortMobile());
//			user.setUserEmail(userInfoForm.getMail());
//			user.setUserMobiletel2(userInfoForm.getUserMobiletel2());
//			user.setRemark1(userInfoForm.getRemark1());
//			user.setRemark3(userInfoForm.getRemark3());
//
//			user.setRemark4(userInfoForm.getRemark4());
//			user.setRemark5(userInfoForm.getRemark5());
//
//			user.setUserType(userInfoForm.getUserType());
//
//			user.setUserPinyin(userInfoForm.getUserPinyin());
//
//			user.setUserWorktel(userInfoForm.getUserWorktel());
//
//			user.setUserFax(userInfoForm.getUserFax());
//
//			user.setUserOicq(userInfoForm.getUserOicq());
//			if (userInfoForm.getUserBirthday() != null
//					&& userInfoForm.getUserBirthday().length() > 0)
//				user.setUserBirthday(Date.valueOf(userInfoForm
//						.getUserBirthday()));
//
//			user.setUserAddress(userInfoForm.getUserAddress());
//
//			user.setUserIdcard(userInfoForm.getUserIdcard());
//			if (userInfoForm.getUserRegdate() != null
//					&& userInfoForm.getUserRegdate().length() > 0)
//				user
//						.setUserRegdate(Date.valueOf(userInfoForm
//								.getUserRegdate()));
//			if (userInfoForm.getUserLogincount() > 0) {
//				user.setUserLogincount(new Integer(userInfoForm
//						.getUserLogincount()));
//			} else
//				user.setUserLogincount(new Integer(0));
//			
//			user.setIstaxmanager(userInfoForm.getIstaxmanager());
//			user.setWorklength(userInfoForm.getWorklength());
//			user.setPolitics(userInfoForm.getPolitics());
//			// add end
//			UserManager userManager = SecurityDatabase.getUserManager();
//
//			// add by pwl 增加修改邮箱用户密码
//			String mailValidata = ConfigManager.getInstance().getConfigValue(
//					"mailValidata");
//			if (mailValidata != null && mailValidata.equals("1")) {
//				User oldUser = userManager.getUserById(user.getUserId()
//						.toString());
//				if (!oldUser.getUserPassword().equals(user.getUserPassword())) {
//					String url = "http://"
//							+ ConfigManager.getInstance().getConfigValue(
//									"mailServer")
//							+ "/creator_changepw.asp?username="
//							+ user.getUserEmail() + "&pw1="
//							+ user.getUserPassword();
//					ApachePostMethodClient client = new ApachePostMethodClient(
//							url);
//					try {
//						String clientResponse = client.sendRequest();
//					} catch (Exception ex) {
//						System.out.println(ex.toString());
//					} finally {
//						client = null;
//					}
//				}
//			}
//
//			//更新离散用户信息---gao.tang 2007.11.10
//			userManager.updateUser(user);
//			userInfoForm.setUserId(user.getUserId().toString());
//			request.setAttribute("currUser", userInfoForm);
//			request.setAttribute("userNameDisable", "true");
//			request.setAttribute("qstring", qqstring);
//
//			return mapping.findForward("discreteUserInfo");
//
//		} catch (ManagerException e) {
//			logger.error(e);
//			return mapping.findForward("fail");
//		}
//	}
	//恢复用户初始密码
	public ActionForward defaultpass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request,response);
			
			UserManager userManager = SecurityDatabase.getUserManager();
			String userId=request.getParameter("userId");
			
				
			User user = userManager.getUserById(userId);
			
			user.setUserPassword("123456");
		
			if (user.getUserId() != null) {
				

			
				// add by pwl 增加修改邮箱用户密码
				String mailValidata = ConfigManager.getInstance()
						.getConfigValue("mailValidata");
				if (mailValidata != null && mailValidata.equals("1")) {
						String url = "http://"
								+ ConfigManager.getInstance().getConfigValue(
										"mailServer")
								+ "/creator_changepw.asp?username="
								+ user.getUserEmail() + "&pw1="
								+ user.getUserPassword();
						ApachePostMethodClient client = new ApachePostMethodClient(
								url);
						try {
							String clientResponse = client.sendRequest();
						} catch (Exception ex) {
							System.out.println(ex.toString());
						} finally {
							client = null;
						}
				}
			}
			userManager.updateUserPassword(user);
			if(control.getUserID().equals(userId))
				control.refreshPassword("123456");
			
			
	    	return mapping.findForward("qtab");
		
	}
	
	/**
	 * 得到岗位列表和已隶属的机构
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward getOrgList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		
		String orgId=request.getParameter("orgId");
		request.setAttribute("orgId",orgId);
		String userId=request.getParameter("uid");
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		UserManager userManager=SecurityDatabase.getUserManager();
		JobManager jobManager=SecurityDatabase.getJobManager();
		Organization org = orgManager.getOrgById(orgId);
		User user=userManager.getUserById(userId);
		List allOrg =jobManager.getJobList(org);		//根据机构取岗位列表
		List existOrg = jobManager.getJobList(org,user);						
		request.setAttribute("existOrg", existOrg);
		request.setAttribute("allOrg", allOrg);
		
		String currentOrgId=request.getParameter("currentOrgId");
		String mainOrgId=request.getParameter("mainOrgId");
		request.setAttribute("currentOrgId",currentOrgId);
		request.setAttribute("mainOrgId",mainOrgId);

		return mapping.findForward("orgajax1");
		
	}
	
	public ActionForward getOrgList2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		
		String orgId=request.getParameter("orgId");
		request.setAttribute("orgId",orgId);
		String userId=request.getParameter("uid");
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
		UserManager userManager=SecurityDatabase.getUserManager();
		JobManager jobManager=SecurityDatabase.getJobManager();
//		Organization org = orgManager.getOrgById(orgId);
		Organization org = OrgCacheManager.getInstance().getOrganization(orgId);
		User user=userManager.getUserById(userId);
		List allOrg =jobManager.getJobList(org);		//根据机构取岗位列表
		List existOrg = jobManager.getJobList(org,user);						
		request.setAttribute("existOrg", existOrg);
		request.setAttribute("allOrg", allOrg);

		return mapping.findForward("distajax");
		
	}
	/**
	 * 用户权限复制
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getuserCopy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		String userId = request.getParameter("uid");
		request.setAttribute("userId",userId);
		return mapping.findForward("usercopy");
	}
	
	public ActionForward userResCopy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String userId = request.getParameter("userId");
		request.setAttribute("userId",userId);
		
		String[] userid = request.getParameterValues("ID");//要复制权限的用户id
		UserManager userManager=SecurityDatabase.getUserManager();
		userManager.userResCopy(userId,userid);
		return mapping.findForward("usercopy");	
		
	}
//	public ActionForward updateUser(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//			try {
//			String orgId = (String) request.getSession().getAttribute("orgId");
//			request.setAttribute("orgId",orgId);
//		
//			UserInfoForm userInfoForm = (UserInfoForm) form;
//			//--修改用户是记录日志
//			AccessControl control = AccessControl.getInstance();
//			control.checkAccess(request,response);
//			String operContent="";        
//	        String operSource=control.getMachinedID();
//	        String openModle="用户管理";
//	        String userName = control.getUserName();
//	        LogManager logManager = SecurityDatabase.getLogManager(); 
//			operContent=userName +" 修改了用户: "+userInfoForm.getUserName(); 
//	        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
//	        //-------------
//
//			int newFlag = 0;
//
//			User user = new User();
//			if (userInfoForm.getUserId().equals("")) {
//				userInfoForm.setUserId(null);
//				newFlag = 1;
//			}
//			user.setUserId(userInfoForm.getUserId() != null ? Integer
//					.valueOf(userInfoForm.getUserId()) : null);
//			user.setUserName(userInfoForm.getUserName());
//			user.setUserPassword(userInfoForm.getUserPassword());
//			user.setUserRealname(userInfoForm.getUserRealname());
//			user.setUserSn(userInfoForm.getUserSn());
//			user.setRemark1(userInfoForm.getRemark1());
//			user.setRemark3(userInfoForm.getRemark3());
//			user.setUserSex(userInfoForm.getUserSex());
//			
//			user.setUserIsvalid(new Integer(userInfoForm.getUserIsvalid()));
//			/*
//			 * DREDGE_TIME
//			 * */
//			int userIsValid = userInfoForm.getUserIsvalid();
//			boolean isValidTag = false;
//			if(userIsValid == 2){
//				String userIsValidSql = "select t.user_isvalid from td_sm_user t where t.user_id='" + userInfoForm.getUserId() + "'";
//				DBUtil db = new DBUtil();
//				try{
//					db.executeSelect(userIsValidSql);
//					if(db.size() !=0 ){
//						if(db.getInt(0, "user_isvalid") == 1){
//							isValidTag = true;
//						}						
//					}
//				}catch(Exception e){}
//			}
//			if(isValidTag){
//				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
//				java.util.Date currentTime = new java.util.Date(); 
//				String riqi = formatter.format(currentTime);
//				
//				user.setDredgeTime(riqi);
//			}
//			
//			user.setUserHometel(userInfoForm.getHomePhone());
//			user.setUserMobiletel1(userInfoForm.getMobile());
//			user.setUserPostalcode(userInfoForm.getPostalCode());
//			user.setRemark2(userInfoForm.getShortMobile());
//			user.setUserEmail(userInfoForm.getMail());
//			user.setUserMobiletel2(userInfoForm.getUserMobiletel2());
//			user.setRemark4(userInfoForm.getRemark4());
//			user.setRemark5(userInfoForm.getRemark5());
//		
//
//			if (userInfoForm.getUserType().length() > 0) {
//				user.setUserType(userInfoForm.getUserType());
//			}
//			user.setUserPinyin(userInfoForm.getUserPinyin());
//			user.setUserWorktel(userInfoForm.getUserWorktel());
//			user.setUserFax(userInfoForm.getUserFax());
//			user.setUserOicq(userInfoForm.getUserOicq());
//			if (userInfoForm.getUserBirthday() != null
//					&& userInfoForm.getUserBirthday().length() > 0)
//				user.setUserBirthday(Date.valueOf(userInfoForm
//						.getUserBirthday()));
//
//			user.setUserAddress(userInfoForm.getUserAddress());
//
//			user.setUserIdcard(userInfoForm.getUserIdcard());
//			if (userInfoForm.getUserRegdate() != null
//					&& userInfoForm.getUserRegdate().length() > 0)
//				user
//						.setUserRegdate(Date.valueOf(userInfoForm
//								.getUserRegdate()));
//			if (userInfoForm.getUserLogincount()>0) {
//				user.setUserLogincount(new Integer(userInfoForm.getUserLogincount()));
//						
//			} else
//				user.setUserLogincount(new Integer(0));
//			
//			user.setIstaxmanager(userInfoForm.getIstaxmanager());
//			user.setWorklength(userInfoForm.getWorklength());
//			user.setPolitics(userInfoForm.getPolitics());
//			// add end
//			UserManager userManager = SecurityDatabase.getUserManager();
//
//			// 增加：判断用户是否存在，存在则转入操作失败页面
//			// 修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名
//			if (user.getUserId() == null) {
//				if (userManager.isUserExist(user)) {
//					// return mapping.findForward("fail");
//					userInfoForm.setUserName("");
//					request.setAttribute("currUser", userInfoForm);
//					request.setAttribute("isUserExist", "true");
//					request.setAttribute("reFlush", "false");
//					return mapping.findForward("userInfo");
//				}
//
//			} else {
//				// add by pwl 增加修改邮箱用户密码
//				String mailValidata = ConfigManager.getInstance()
//						.getConfigValue("mailValidata");
//				if (mailValidata != null && mailValidata.equals("1")) {
//					User oldUser = userManager.getUserById(user.getUserId().toString());
//					if (!oldUser.getUserPassword().equals(
//							user.getUserPassword())) {
//						String url = "http://"
//								+ ConfigManager.getInstance().getConfigValue(
//										"mailServer")
//								+ "/creator_changepw.asp?username="
//								+ user.getUserEmail() + "&pw1="
//								+ user.getUserPassword();
//						ApachePostMethodClient client = new ApachePostMethodClient(
//								url);
//						try {
//							String clientResponse = client.sendRequest();
//						} catch (Exception ex) {
//							System.out.println(ex.toString());
//						} finally {
//							client = null;
//						}
//					}
//				}
//			}
//
//			userManager.updateUser(user);
//		    
//			
//			//增加用户主要所属单位到td_sm_orguser表2006-08-17
////			delete by ge.tao 2007-08-31
////			OrgManager orgmanager = SecurityDatabase.getOrgManager();
////			String userId = user.getUserId().toString();
////			orgmanager.addMainOrgnazitionOfUser(userId,orgId);
//			//-----------------------------------------------
//			//如果机构下没有岗位，默认添加待岗岗位
//			DBUtil db = new DBUtil();
//			DBUtil db1 = new DBUtil();
//			String sql ="select * from td_sm_orgjob where org_id='"+ orgId +"'";
//			db.executeSelect(sql);
//			if(db.size()<1){
//				String addorgjob ="insert into td_sm_orgjob(org_id,job_id,job_sn) values('"+ orgId +"','1','1')";
//				db1.executeInsert(addorgjob);
//			}
//			//-----------------------------------------------
//			userInfoForm.setUserId(user.getUserId().toString());
//			if (newFlag == 1) {
//				JobManager jobmanager = SecurityDatabase.getJobManager();
////				Job job = jobmanager.getJob("jobId", "1");
//				Job job = jobmanager.getJobById("1");
//
//				Userjoborg userjoborg = new Userjoborg();
//		
//				OrgManager orgMgr = SecurityDatabase.getOrgManager();
//				Organization org = orgMgr.getOrgById(orgId);
//
//				
//				//-----------------------------------------------------
//				String sqlsn ="select * from td_sm_orgjob where job_id ='1' and org_id ='"+ orgId+"'";
//				DBUtil dbutil = new DBUtil();
//				dbutil.executeSelect(sqlsn);
//				int jobsn;
//				if (dbutil != null && dbutil.size() > 0) {
//					 jobsn=dbutil.getInt(0,"job_sn");// 得待岗岗位排序号
//					    userjoborg.setUser(user);
//						userjoborg.setJob(job);
//						userjoborg.setOrg(org);
//						userjoborg.setJobSn(new Integer(jobsn));
//						userjoborg.setSameJobUserSn(new Integer(0));
//						userjoborg.setStartTime(new Timestamp(new java.util.Date().getTime()));
//						userjoborg.setFettle(new Integer(1));
//						userManager.storeUserjoborg(userjoborg);
//
//				}
//			}
//			request.setAttribute("currUser", userInfoForm);
//			request.getSession().setAttribute("currUserForm", userInfoForm);
//			request.getSession().setAttribute("currUserId",
//					Integer.valueOf(userInfoForm.getUserId()));
//			request.setAttribute("userNameDisable", "true");
//						String persontype = request.getParameter("person");
//			if (persontype != null && persontype.equals("person")) {
//
//				return mapping.findForward("userInfopersoninfo");
//
//			}
//			return mapping.findForward("qtab");
//		} catch (ManagerException e) {
//			logger.error(e);
//			return mapping.findForward("fail");
//		}
//
//		
//	}
	//离散用户批量加入机构
	public ActionForward addAllOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String[] id = request.getParameterValues("checkBoxOne");
			
            request.setAttribute("id", id);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return mapping.findForward("allorg");
		
	}
	public ActionForward getBatchOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		
		String orgId=request.getParameter("orgId");
		request.setAttribute("orgId",orgId);
		String userId=request.getParameter("uid");
		request.setAttribute("userId",userId);
		String uname = request.getParameter("uname");
		request.setAttribute("uname",uname);
		OrgManager orgManager = SecurityDatabase.getOrgManager();
	
		JobManager jobManager=SecurityDatabase.getJobManager();
		Organization org = orgManager.getOrgById(orgId);
		
		List allOrg =jobManager.getJobList(org);		//根据机构取岗位列表
							

		
		request.setAttribute("allOrg", allOrg);

		return mapping.findForward("batch_org");
		
	}
}
