package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.spi.SPIException;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.security.service.entity.Result;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory;
import com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl;
import com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction;
import com.frameworkset.util.StringUtil;

public class UserOrgAction {

	public UserOrgAction() {
		// TODO Auto-generated constructor stub
	}
	public @ResponseBody Result addUser(HttpServletRequest request,HttpServletResponse response) throws ManagerException
	{
		Result result = new Result();
		AccessControl accesscontroler = AccessControl.getInstance();
		if(!accesscontroler.checkManagerAccess(request,response))
		{
			result.setErrormessage("没有新增用户权限!");
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
			if(!accesscontroler.checkManagerAccess(request,response))
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
	
	public @ResponseBody Result userorderchange(HttpServletRequest request,HttpServletResponse response)   
	{
		Result result = new Result();String errorMessage = "";
		try {
			AccessControl control = AccessControl.getInstance();
			if(!control.checkManagerAccess(request,response))
			{
				result.setErrormessage("没有用户排序权限!");
				result.setCode(result.fail);
			}
			
			 //记录日志
//		    String operContent="";        
//		    String operSource=control.getMachinedID();
//		    String userName = control.getUserName();
//		    String description="";
//		    LogManager logManager = SecurityDatabase.getLogManager();   
//		    
		    String userId3 = request.getParameter("userId");
		    String orgId = request.getParameter("orgId");
		    String[] userId = null;
		 
		    if(userId3 != null && userId3.length() >0){
		        userId = userId3.split(",");
		    }
		    
		    if(userId != null && userId.length > 0){
//		        String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
//		        description="";
		       // logManager.log(control.getUserAccount() ,operContent,"",operSource,description);
		      
		             OrgJobAction.storeOrgUserOrder(orgId,userId) ;
		       
		    }
		    result.setCode(result.ok);
		    result.setErrormessage("用户排序完毕");
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (Exception e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}
	
	
	public @ResponseBody Result foldDisperse(HttpServletRequest request,HttpServletResponse response)   
	{
		Result result = new Result();String errorMessage = "";
		try {
			AccessControl control = AccessControl.getInstance();
			if(!control.checkManagerAccess(request,response))
			{
				result.setErrormessage("没有用户调动权限!");
				result.setCode(result.fail);
			}
			
			UserManager userManager = SecurityDatabase.getUserManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			//被调出的机构id,从离散用户调入到机构时orgIdUser为空
			String orgIdUser = request.getParameter("orgId");
			String flag = request.getParameter("flag");
			
			String userIds = request.getParameter("userIds");
			//调入到的机构id
			String orgIds = request.getParameter("orgIds");
			String[] userId = userIds.split(",");
			String[] orgId = orgIds.split(",");
			boolean state = false;
			TransactionManager tm = new TransactionManager();
			try
			{
				tm.begin();
				if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
				//调入多个机构开关为false时执行下面代码
					if(orgIdUser!=null && !"".equals(orgIdUser)){
						//boolean state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);//删除用户主机构与岗位,部门管理员
						boolean state2 = false;
						if(ConfigManager.getInstance().getConfigBooleanValue("isdelUserRes", true)){//调离用户时删除用户所有资源
							orgManager.deleteAllOrg_UserJob( userId);
							state2 = userManager.deleteBatchUserRes(userId,false);
						}else{
							state2 = orgManager.deleteAllOrg_UserJob( userId);
						}
						if(state2){
							if(!"orgunit".equals(orgIds)){//如果是将用户调入离散用户就不需要保存主机构与岗位关系
								state = userManager.storeBatchUserOrg(userId, orgId, true,false);//保存用户主机构与岗位
							}else{
								state = true;
								userManager.fixuserorg(userId, orgIds);
							}
						}
					}else{
						state = userManager.storeBatchUserOrg(userId, orgId, true,false);//保存用户主机构与岗位
					}
				}else{
					if(orgIdUser!=null && !"".equals(orgIdUser)){
						if("0".equals(flag)){//flag的值为1时删除当前机构的用户
							boolean state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);
							if(state2){
								state = userManager.storeBatchUserOrg(userId, orgId, false,false);
							}
						}else{
							state = userManager.storeBatchUserOrg(userId, orgId, false,false);//保存用户岗位
						}
					}else{
						state = userManager.storeBatchUserOrg(userId, orgId, true,false);
					}
				}
				
				if(orgIdUser!=null && !"".equals(orgIdUser)){
					Organization orgold = orgManager.getOrgById(orgIdUser);	
					if("orgunit".equals(orgIds)){//如果条件成立则是将用户调入离散用户
						for(int j = 0; j < userId.length; j++){
							User user = userManager.getUserById(userId[j]);
							String userName = control.getUserName();
							String operContent="";        
						    String operSource=control.getMachinedID();
						    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
						    LogManager logManager = SecurityDatabase.getLogManager(); 
							operContent=userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.dispatch.out.org.to.free", new Object[] {orgold.getOrgName(), user.getUserName()}, request);
						    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
						}
					}else{
						for(int i = 0; i < orgId.length; i++){
							Organization org = orgManager.getOrgById(orgId[i]);
							//--用户调入其他机构记录日志
							for(int j = 0; j < userId.length; j++){
								User user = userManager.getUserById(userId[j]);
								String userName = control.getUserName();
								String operContent="";        
							    String operSource=control.getMachinedID();
							    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
							    LogManager logManager = SecurityDatabase.getLogManager(); 
								operContent=userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.dispatch.out.org.to.org", new Object[] {orgold.getOrgName(), user.getUserName(), org.getOrgName()}, request);
							    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
						    }
						    //-------------
						}
					}
				}else{
					for(int i = 0; i < orgId.length; i++){
						Organization org = orgManager.getOrgById(orgId[i]);
						//--用户调入机构记录日志
						for(int j = 0; j < userId.length; j++){
							User user = userManager.getUserById(userId[j]);
							String userName = control.getUserName();
							String operContent="";        
						    String operSource=control.getMachinedID();
						    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
						    LogManager logManager = SecurityDatabase.getLogManager(); 
							operContent=userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.dispatch.out.free.to.org", new Object[] {user.getUserName(), org.getOrgName()}, request);
						    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
					    }
					    //-------------
					}
				}
				tm.commit();
				Event eventUSER_INFO_DELETE = new EventImpl(userIds,
						ACLEventType.USER_INFO_DELETE);
				EventHandle.getInstance().change(eventUSER_INFO_DELETE);
				
				Event eventUSER_ROLE_INFO_CHANGE = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
				EventHandle.getInstance().change(eventUSER_ROLE_INFO_CHANGE);
			}
			finally
			{
				tm.release();
			}
		    result.setCode(result.ok);
		    result.setErrormessage("用户调动完毕");
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (Exception e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}
	public @ResponseBody Result reclaimUserRes_do(HttpServletRequest request,HttpServletResponse response)   
	{
		Result result = new Result();String errorMessage = "";
		try {
			AccessControl control = AccessControl.getInstance();
			if(!control.checkManagerAccess(request,response))
			{
				result.setErrormessage("没有回收操作权限!");
				result.setCode(result.fail);
			}
			
			 String userIds = request.getParameter("userIds") == null ? "":request.getParameter("userIds");
			    String[] userIdList = userIds.split(",");
			    //public parameter
			    String directRes = request.getParameter("directRes") == null ? "":request.getParameter("directRes");
			    String userRoleRes = request.getParameter("userRoleRes") ==null ? "":request.getParameter("userRoleRes");
			    String userOrgJobRes = request.getParameter("userOrgJobRes") == null ? "":request.getParameter("userOrgJobRes");
			    String userGroupRes = request.getParameter("userGroupRes") == null ? "":request.getParameter("userGroupRes");
			    
			    PurviewManager manager = new PurviewManagerImpl();
			    boolean isReclaimDirectRes = "".equals(directRes) ? false:true;
			    boolean isReclaimUserRoles = "".equals(userRoleRes) ? false:true;
			    boolean isReclaimUserJobs = "".equals(userOrgJobRes) ? false:true;
			    boolean isReclaimUserGroups = "".equals(userGroupRes)?false:true;
			    
			    List optFailedList = manager.reclaimUsersResources(control,userIdList,isReclaimDirectRes,isReclaimUserRoles,isReclaimUserJobs,isReclaimUserGroups);
			    
			    String promptStr = "";    
			    for(int i=0;i<optFailedList.size();i++){
			        //格式 admin:业务1,业务2,
			        if("".equals(promptStr)){
			            promptStr = RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.user.resource.recycle.fail", request)+"\\n";
			            promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
			        }else{
			            promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
			        }        
			    }
			    if(!"".equals(promptStr)){
			  
			    }else{
		 
			    }
			 
		    
		    result.setCode(result.ok);
		    result.setErrormessage("回收操作完毕");
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (Exception e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}
	
}
