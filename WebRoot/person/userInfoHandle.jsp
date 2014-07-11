<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.sql.Date" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String userName = control.getUserName();
	
	String action = request.getParameter("action");
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime);

	int flag = 1;
	
	try{
		if("storeUser".equals(action)){
			//String orgId = (String) request.getSession().getAttribute("orgId");
			
			//request.setAttribute("orgId",orgId);
			//UserInfoForm userInfoForm = (UserInfoForm) form;
			//--新增用户是记录日志
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.manage.title", request);
	      //LogManager logManager = SecurityDatabase.getLogManager(); 
		  //operContent=userName +" 新增了用户: "+request.getParameter("userName");
	      //logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");       
	      //-------------
	        User user = new User();
	        
			user.setUserId(request.getParameter("userId") != null ? Integer
					.valueOf(request.getParameter("userId")) : null);
			user.setUserName(request.getParameter("userName"));
			user.setUserPassword(request.getParameter("password"));
			user.setUserRealname(request.getParameter("userRealname"));
			//user.setUserSn(userInfoForm.getUserSn());
			//user.setRemark1(userInfoForm.getRemark1());
			user.setRemark3(request.getParameter("remark3"));
			user.setUserSex(request.getParameter("userSex"));
			
			user.setUserIsvalid(new Integer(request.getParameter("isvalid")));
			user.setUserHometel(request.getParameter("homePhone"));
			user.setUserMobiletel1(request.getParameter("mobile"));
			user.setUserPostalcode(request.getParameter("postalCode"));
			user.setRemark2(request.getParameter("shortMobile"));
			user.setUserEmail(request.getParameter("mail"));
			user.setUserMobiletel2(request.getParameter("userMobiletel2"));
			user.setRemark4(request.getParameter("remark4"));
			user.setRemark5(request.getParameter("remark5"));
			String istaxmanager = request.getParameter("istaxmanager");
			if(null != istaxmanager)
				user.setIstaxmanager(Integer.parseInt(istaxmanager));
		

			//if (userInfoForm.getUserType().length() > 0) {
			user.setUserType(request.getParameter("userType"));
			//}
			user.setUserPinyin(request.getParameter("userPinyin"));
			user.setUserWorktel(request.getParameter("userWorktel"));
			user.setUserFax(request.getParameter("userFax"));
			user.setUserOicq(request.getParameter("userOicq"));
			
			if (request.getParameter("userBirthday") != null && !request.getParameter("userBirthday").equals(""))
			{
				user.setUserBirthday(Date.valueOf(request.getParameter("userBirthday")));
			}				

			user.setUserAddress(request.getParameter("userAddress"));
			user.setUserIdcard(request.getParameter("userIdcard"));
			
			if (request.getParameter("userRegdate") != null && !request.getParameter("userRegdate").equals(""))
			{
				user.setUserRegdate(Date.valueOf(request.getParameter("userRegdate")));
			}
			else
			{
				user.setUserRegdate(Date.valueOf(riqi));
			}
			
			user.setUserSn(new Integer(request.getParameter("userSn")));
			//System.out.println(".........................."+request.getParameter("password"));
			int loginCount = 0;
			if(request.getParameter("userLogincount")!=null)
				loginCount = Integer.parseInt(request.getParameter("userLogincount"));
			else
				user.setUserLogincount(new Integer(loginCount));
				
			UserManager userManager = SecurityDatabase.getUserManager();
			userManager.updateUser(user);
			
		} else {
			System.out.println("..........................");
		}
	}catch(Exception e){
		flag = 0;
		e.printStackTrace();
	}
%>

<script language = "javascript">
	if("<%=flag%>"=="1"){
		$.dialog.alert('<pg:message code="sany.pdp.common.operation.modfiy.success"/>', function() {
			parent.location.reload();
		});
	} else{
		$.dialog.alert('<pg:message code="sany.pdp.common.operation.modfiy.fail"/>', function() {
			parent.location.reload();
		});
	}
	
</script>
