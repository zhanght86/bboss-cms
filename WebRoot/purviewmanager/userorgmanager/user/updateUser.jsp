<%
/*
 * <p>Title: 更新用户信息主页面</p>
 * <p>Description: 更新用户信息主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@	page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.sql.Date" %>



<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    
    UserManager userManager = SecurityDatabase.getUserManager();
    User user = new User();
    
    String userId = request.getParameter("userId");
    String userName = request.getParameter("userName");
    String userType = request.getParameter("userType");
    String userSex = request.getParameter("userSex");
    String userIsvalid = request.getParameter("userIsvalid");
    String remark3 = request.getParameter("remark3");
   // String istaxmanagerStr = request.getParameter("istaxmanager");
    String remark1 = request.getParameter("remark1");
    String userRealname = request.getParameter("userRealname");
    String userPassword = request.getParameter("userPassword");
    String userIdcard = request.getParameter("userIdcard");
    String userWorktel = request.getParameter("userWorktel");
    String homePhone = request.getParameter("homePhone");
    String mail = request.getParameter("mail");
    String mobile = request.getParameter("mobile");
    String remark4 = request.getParameter("remark4");
    String userMobiletel2 = request.getParameter("userMobiletel2");
    String remark5 = request.getParameter("remark5");
    String ou = request.getParameter("ou");
    String userPinyin = request.getParameter("userPinyin");
    String postalCode = request.getParameter("postalCode");
    String userFax = request.getParameter("userFax");
    String userOicq = request.getParameter("userOicq");
    String userBirthday = request.getParameter("userBirthday");
    String userAddress = request.getParameter("userAddress");
    String userLogincount = request.getParameter("userLogincount");
    String userRegdate = request.getParameter("userRegdate");
    String shortMobile = request.getParameter("shortMobile");
    String userSn = request.getParameter("userSn");
    String userWorknumber = request.getParameter("userWorknumber");
   // int isTax = Integer.parseInt(istaxmanagerStr.trim());
	user.setUserId(new Integer(userId));
	user.setUserName(userName);
	user.setUserType(userType);
	user.setUserSex(userSex);
	user.setUserIsvalid(new Integer(userIsvalid));
	user.setRemark3(remark3);
	//user.setIstaxmanager(isTax);
	user.setRemark1(remark1);
	user.setUserRealname(userRealname);
	user.setUserPassword(userPassword);
	user.setUserIdcard(userIdcard);	
	user.setUserWorktel(userWorktel);
	user.setUserHometel(homePhone);
	user.setUserEmail(mail);
	user.setUserMobiletel1(mobile);
	user.setRemark4(remark4);
	user.setUserMobiletel2(userMobiletel2);
	user.setRemark5(remark5);
	user.setUserPinyin(userPinyin);
	user.setUserPostalcode(postalCode);
	user.setUserFax(userFax);
	user.setUserOicq(userOicq);
	user.setUserAddress(userAddress);
	user.setUserLogincount(new Integer(userLogincount));
	
	if(!"".equals(userBirthday)){
		user.setUserBirthday(Date.valueOf(userBirthday));
	}
	if(!"".equals(userRegdate)){
		user.setUserRegdate(Date.valueOf(userRegdate));
	}
	user.setRemark2(shortMobile);
	user.setUserSn(new Integer(userSn));
	user.setUserWorknumber(userWorknumber);
	String passwordDualedTime_ = request.getParameter("passwordDualedTime");
	int passwordDualedTime = userManager.getDefaultPasswordDualTime();
	try
	{
		passwordDualedTime = Integer.parseInt(passwordDualedTime_);
	}
	catch(Exception e)
	{
		
	}
	user.setPasswordDualedTime(passwordDualedTime);
    boolean state = userManager.updateUser(user);
   
%>

<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
	<%if(state)
	{
	%>
		W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		
	<%
	}
	else
	{
	%>
		W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");	
	<%
	}
	%>
</script>
