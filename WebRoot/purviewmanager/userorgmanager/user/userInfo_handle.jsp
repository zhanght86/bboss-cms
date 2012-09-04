
<%
/*
 * 
 * <p>Title: 离散用户修改处理页面</p>
 *
 * <p>Description: 离散用户修改处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author biaoping.yin
 * @version 1.0
 */
 %>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>				
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.security.authentication.EncrpyPwd,
				com.frameworkset.platform.security.*,
				java.sql.Date,
				com.frameworkset.platform.sysmgrcore.entity.*,
				java.util.*" %>



<%	
String msg= null;
try
{
	//action start
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			
			UserManager userManager = SecurityDatabase.getUserManager();
			String userId=request.getParameter("userId");
			
			User user = new User();
			user = userManager.getUserById(userId);
			String userName = user.getUserName();
			//保存  更新操作
			if(request.getParameter("isSave") != null){
				
				user.setUserPassword(request.getParameter("userPassword"));
				user.setUserRealname(request.getParameter("userRealname"));
				user.setUserSn(new Integer(request.getParameter("userSn")));
				if(!"NaN".equals(request.getParameter("userSex")))
					user.setUserSex(request.getParameter("userSex"));
				String userIsvalid =  request.getParameter("userIsvalid");
				if(userIsvalid != null && !"NaN".equals(userIsvalid))
				{
					try
					{
						user.setUserIsvalid(new Integer(userIsvalid));
					}
					catch(Exception e)
					{
					   user.setUserIsvalid(new Integer(2)) ;
					}
				}
				user.setUserHometel(request.getParameter("homePhone"));
				user.setUserMobiletel1(request.getParameter("mobile"));
				user.setUserPostalcode(request.getParameter("postalCode"));
				user.setRemark2(request.getParameter("shortMobile"));
				user.setUserEmail(request.getParameter("mail"));
				user.setUserMobiletel2(request.getParameter("userMobiletel2"));
				user.setRemark1(request.getParameter("remark1"));
				user.setRemark3(request.getParameter("remark3"));
				user.setRemark4(request.getParameter("remark4"));
				user.setRemark5(request.getParameter("remark5"));
				
				if(!"NaN".equals(request.getParameter("userType")))
					user.setUserType(request.getParameter("userType"));
	
				user.setUserPinyin(request.getParameter("userPinyin"));
	
				user.setUserWorktel(request.getParameter("userWorktel"));
	
				user.setUserFax(request.getParameter("userFax"));
	
				user.setUserOicq(request.getParameter("userOicq"));
				if (request.getParameter("userBirthday") != null
						&& request.getParameter("userBirthday").length() > 0)
					user.setUserBirthday(Date.valueOf(request.getParameter("userBirthday")));
	
				user.setUserAddress(request.getParameter("userAddress"));
	
				user.setUserIdcard(request.getParameter("userIdcard"));
				if (request.getParameter("userRegdate") != null
						&& request.getParameter("userRegdate").length() > 0)
					user.setUserRegdate(Date.valueOf(request.getParameter("userRegdate")));
				
				if(request.getParameter("istaxmanager")==null){
					user.setIstaxmanager(0);
				}else{
					user.setIstaxmanager(Integer.valueOf(request.getParameter("istaxmanager")).intValue());
				}
				userManager.updateUser(user);
				
			}else{
				user = userManager.getUserById(userId);
			}
			
			//密码恢复
			if(request.getParameter("dispersePwd") != null){
				user.setUserPassword("123456");
				userManager.updateUserPassword(user);
			}
	//action end
	}
	catch(Exception e)
	{
			msg = e.getMessage();
	}
	
	if(msg != null)
	{
		%>
		<script language="javascript">
			$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/><%=msg%>", function() {
				parent.resetbutton();
			});
		</script>
		<%
	}
	else
	{
	%>
		<script language="javascript">
			$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		</script>
		<%
		}
	
	
%>

