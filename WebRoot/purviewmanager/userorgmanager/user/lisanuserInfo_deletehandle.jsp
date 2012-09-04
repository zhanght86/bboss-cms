
/**
 * 
 * <p>Title: 离散用户删除处理页面</p>
 *
 * <p>Description: 离散用户删除处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author biaoping.yin
 * @version 1.0
 */
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager
				,com.frameworkset.platform.sysmgrcore.manager.LogManager,com.frameworkset.platform.sysmgrcore.manager.OrgManager,com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl" %>
<%@ page import="com.frameworkset.util.StringUtil,com.frameworkset.platform.security.*,java.sql.Date,com.frameworkset.platform.config.ConfigManager,com.frameworkset.platform.security.event.ACLEventType"%>


<%	
String msg= null;
List allMessage = new ArrayList();
int flag = 0;
try
{
	//action start
			
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			//获取要删除的用户
			String[] ids = request.getParameterValues("checkBoxOne");
			//System.out.println(ids);
			//System.out.println(ids.length);
			UserManager userManager = SecurityDatabase.getUserManager();
			
			
			User user = null;
			
			
			
			
			for(int i = 0;  i < ids.length; i ++)
			{
				
				user = userManager.getUserById(ids[i]);
				if(user == null)
				{
					allMessage.add("用户【" + ids[i] + "】不存在,无需删除！");
					continue;
				}
				//获取用户是否可以被删除,调用系统校验接口，验证用户是否可以被删除
				List message = PurviewManagerImpl.getBussinessCheck().userDeleteCheck(control,ids[i]);
				if(message != null && message.size()> 0)
				{
					allMessage.addAll(message);
					continue;
				}
				
				
				userManager.deleteUser(user);
				flag ++;
				//--从机构中删除用户记录日志
				String operContent="";        
		        String operSource=control.getMachinedID();
		        String openModle="用户管理";
		        LogManager logManager = SecurityDatabase.getLogManager(); 
				operContent=control.getUserAccount() +" 删除了离散用户: "+user.getUserName(); 
		        logManager.log(control.getUserAccount(),operContent,openModle,operSource);       
		        //-------------
		        //删除该机构下的用户，该用户在其他机构下存在---关联表关系（td_sm_userjoborg,td_sm_orgmanager）
			}
			if(flag > 0) //发出离散用户删除事件
			{
				ConfigManager.getEventHandle().change("删除离散用户",ACLEventType.USER_INFO_DELETE);
			}
			
			
			
	//action end
	}
	catch(Exception e)
	{
			//e.printStackTrace();
			allMessage.add(e.getMessage());
	}
	
	if(allMessage != null)
	{
		if(flag > 0)
		{		
			allMessage.add("实际删除用户[" + flag + "]个。");
		}
		if(allMessage.size() > 0)
		{
			//System.out.println("allMessage.size():" + allMessage.size());
			
			StringBuffer str = new StringBuffer();
			
			msg =  StringUtil.buildStringMessage(allMessage);
			
			//System.out.println("allMessage:" + msg);
			%>
		<script language="javascript">
			parent.parent.$.dialog.alert("操作完成：\n<%=msg%>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			//parent.resetwindow();
		</script>
		<%
		}
		else
		{
			%>
		<script language="javascript">
		parent.parent.$.dialog.alert("操作完成",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			//parent.resetbutton();
		</script>
		<%
		}	
		
	
	}
	
	
%>

