package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.spi.SPIException;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.service.entity.Result;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory;
import com.frameworkset.util.StringUtil;

public class UserAction {

	public UserAction() {
		// TODO Auto-generated constructor stub
	}
	public @ResponseBody Result addUser(HttpServletRequest request,HttpServletResponse response) throws ManagerException
	{
		Result result = new Result();
		AccessControl accesscontroler = AccessControl.getInstance();
		if(!accesscontroler.checkManagerAccess(request,response));
		{
			result.setErrormessage("没有删除用户权限!");
			result.setCode(result.fail);
		}
		
		String newUserName = "";String errorMessage = "";
		boolean isAutoUserName = GenerateServiceFactory.getGenerateService().enableUserNameGenerate();
		  String currOrgId =  request.getParameter("orgId");
		    
			if(currOrgId == null)
			{
				currOrgId = (String)request.getAttribute("orgId");
			}
			
		 //保存用户
		User user = new User();
		if(isAutoUserName){
			try{
				Map map = new HashMap();
				map.put("orgId",currOrgId);
				user.setUserName(GenerateServiceFactory.getGenerateService().generateUserName(map));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			user.setUserName(request.getParameter("userName"));
		}
		user.setUserWorknumber(request.getParameter("userWorknumber"));
		user.setUserPassword(request.getParameter("userPassword"));
		user.setUserRealname(request.getParameter("userRealname"));
		user.setUserSn(new Integer(request.getParameter("userSn")));
		user.setUserSex(request.getParameter("userSex"));
		user.setUserIsvalid(new Integer(request.getParameter("userIsvalid")));
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
			
		user.setUserType(request.getParameter("userType"));
		user.setUserPinyin(request.getParameter("userPinyin"));

		user.setUserWorktel(request.getParameter("userWorktel"));
		user.setUserFax(request.getParameter("userFax"));
		user.setUserOicq(request.getParameter("userOicq"));
		if(!"".equals(request.getParameter("userBirthday")))
			user.setUserBirthday(Date.valueOf(request.getParameter("userBirthday")));

		user.setUserAddress(request.getParameter("userAddress"));
		user.setUserLogincount(new Integer(request.getParameter("userLogincount")));
		user.setUserIdcard(request.getParameter("userIdcard"));
		if(!"".equals(request.getParameter("userRegdate")))
			user.setUserRegdate(Date.valueOf(request.getParameter("userRegdate")));
		UserManager userManager = SecurityDatabase.getUserManager();
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
		
		
		
		// 吴卫雄增加：判断用户是否存在，存在则转入操作失败页面
		// 潘伟林修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名
		
		boolean isUserExist = false;
		if (userManager.isUserExist(user)) {
			request.setAttribute("isUserExist", "true");
			request.setAttribute("reFlush", "false");
				
			isUserExist = true ;
		}

		
		//System.out.println("isUserExist = " + isUserExist);
		if(isUserExist == false)//用户名存在不保存用户
		{
			newUserName = user.getUserName();
			try{
				if(newUserName != null && !"".equals(newUserName)){
					userManager.creatorUser(user,currOrgId,"1");
				}else{
					if(isAutoUserName){
		        		errorMessage = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.loginname.generate.system.exception", request);
		        	}else{
		        		errorMessage = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.loginname.null", request);
		        	}
					result.setErrormessage(errorMessage);
					result.setCode(result.fail);
				}
			}catch(Exception e){
				e.printStackTrace();
				errorMessage = StringUtil.exceptionToString(e);
				result.setErrormessage(errorMessage);
				result.setCode(result.fail);
//				if(errorMessage != null && !"".equals(errorMessage)){
//					errorMessage = errorMessage.replaceAll("\\n","\\\\n");
//					errorMessage = errorMessage.replaceAll("\\r","\\\\r");
//				}
			}
		}
		else
		{
			 
			if(isAutoUserName){
		 
				errorMessage = "自动生成的账号用户已经存在："+newUserName;
				result.setErrormessage(errorMessage);
				result.setCode(result.fail);
			}else{
	 
				errorMessage = "用户已经存在："+newUserName;
				result.setErrormessage(errorMessage);
				result.setCode(result.fail);
			}
		 
		}
		if(errorMessage.length() == 0)
		{
			
			if(isAutoUserName)
			{
				
				result.setErrormessage( "用户创建成功，自动生成的账号为:"+newUserName);
			}
			else
				result.setErrormessage( "用户创建成功:"+newUserName);
			result.setCode(result.ok);
			
		}
		return result;
	}
	
	public @ResponseBody Result quiteDelUser(HttpServletRequest request,HttpServletResponse response)  
	{
		Result result = new Result();String errorMessage = "";
		try {
			AccessControl accesscontroler = AccessControl.getInstance();
			if(!accesscontroler.checkManagerAccess(request,response));
			{
				result.setErrormessage("没有删除用户权限!");
				result.setCode(result.fail);
			}
			UserManager userManager = SecurityDatabase.getUserManager();
			
			String userId = request.getParameter("checks");
			String orgId = request.getParameter("orgId");
			
			String[] userIds = userId.split(",");
			String delUserIds = "";

			String userNamesNo = "";
			
				
			//日志记录start
			String curUserName = accesscontroler.getUserName();
			String operContent = "";        
			String operSource = accesscontroler.getMachinedID();
			String openModle = "用户管理";
			
			LogManager logManager = SecurityDatabase.getLogManager(); 
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			
			String orgname = orgManager.getOrgById(orgId).getRemark5();
			
			long startdate = System.currentTimeMillis();
			for(int i = 0; i < userIds.length; i++){
				User user = userManager.getUserById(userIds[i]);
				String userOrgInfo = userManager.userOrgInfo(accesscontroler,userIds[i]);
				if("".equals(userOrgInfo)){
					operContent=curUserName +" 从机构： "+ orgname +" 中彻底删除了用户: "+user.getUserName(); 
			
					logManager.log(accesscontroler.getUserAccount() ,operContent,openModle,operSource,""); 
					
					if("".equals(delUserIds)){
						delUserIds = userIds[i];
					}else{
						delUserIds += "," + userIds[i];
					}
				}else{
					if("".equals(userNamesNo)){
						userNamesNo = "以下用户删除失败:\\n" + user.getUserRealname() + ":" + userOrgInfo;
					}else{
						userNamesNo += "\\n" +  user.getUserRealname() + ":" + userOrgInfo;
					}
					 
					result.setErrormessage(userNamesNo);
					result.setCode(result.ok);
				}
			}
			long enddate = System.currentTimeMillis();
			//日志记录end
			//System.out.println((enddate - startdate)/1000);
			String[] delUserId = delUserIds.split(",");
			
			
			boolean state = userManager.deleteBatchUser(delUserId);
			 
			if ("".equals(userNamesNo)) {
				if (state) {
					result.setErrormessage("用户删除成功!");
					result.setCode(result.ok);
				} else {
					result.setErrormessage("用户删除失败!");
					result.setCode(result.fail);
				}
			} else {
				result.setErrormessage(userNamesNo);
				result.setCode(result.ok);
			}
			 
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (ManagerException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}

}
