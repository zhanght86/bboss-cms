<%
/*
 * <p>Title: 用户调入下的保存数据的处理页面</p>
 * <p>Description: 用户调入下的保存数据的处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*,
				 com.frameworkset.platform.sysmgrcore.entity.User,
				 com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				 com.frameworkset.platform.config.ConfigManager,
				 com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl,
				 java.util.List"%>
				 
				 
<%
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String CurorgId = request.getParameter("CurorgId");
		
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		LogManager logManager = SecurityDatabase.getLogManager();
        UserManager userManagerInfo = SecurityDatabase.getUserManager();
        UserManager userManager = SecurityDatabase.getUserManager();
        
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();//getUserName();
        String description="";
		
		//多个userId
		String userId = request.getParameter("userId")==null?"":request.getParameter("userId");
		String[] folduserIds = userId.split(",");
		//返回提示信息
		String userReturnInfo = "";
		//符合条件的userid串
		String userIdsStr = "";
		String classType = request.getParameter("classType");
		for(int u = 0; u < folduserIds.length; u++){
			List list = PurviewManagerImpl.getBussinessCheck().userMoveCheck(control,folduserIds[u]);
			
		    String userOrgInfoMsg = PurviewManagerImpl.buildMessage(list);
			if(!"".equals(userOrgInfoMsg) && !classType.equals("lisan")){
			    //如果返回值！= "" ， 累加提示信息
			    User user = userManager.getUserById(folduserIds[u]);
				if(userReturnInfo.equals("")){
					userReturnInfo = user.getUserRealname()+"("+user.getUserName()+"):"+userOrgInfoMsg;
				}else{
					userReturnInfo += "\\n" + user.getUserRealname()+"("+user.getUserName()+"):" + userOrgInfoMsg;
				}
			}else{
			    //符合调动条件的userid
				if(userIdsStr.equals("")){
					userIdsStr = folduserIds[u];
				}else{
					userIdsStr += "," + folduserIds[u];
				}
			}
		}
		
		// 保存符合条件的userid
		String[] userIds = userIdsStr.split(",");
		
		String orgId = request.getParameter("orgId");
		String flag = request.getParameter("flag");
		
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);	
		
		//一系列的调动处理
		
		if(!"".equals(userId) && userIds.length>0 && !"".equals(userIds[0]) ){
			if(flag.equals("1")){
			    
				for(int i = 0; i < userIds.length; i++){
					User user = userManager.getUserById(userIds[i]);
					operContent=userName + " 把用户:["+user.getUserName()+"] 调入到 ["+ orgName_log+"] 机构下面"; 						
					logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);    
				}
				
				if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
				//调入多个机构开关，如果sys.user.enablemutiorg为false则执行下列语句
					if(!classType.equals("lisan")){//不为离散用户调入时执行下列语句
						//先删除用户主机构关系和机构岗位关系
						boolean state2 = false;
						if(ConfigManager.getInstance().getConfigBooleanValue("isdelUserRes", true)){//调离用户时删除用户所有资源
							orgManager.deleteOrg_UserJob(CurorgId, userIds);
							state2 = userManager.deleteBatchUserRes(userIds);
						}else{
							state2 = orgManager.deleteOrg_UserJob(CurorgId, userIds);
						}
						if(state2){	
							//然后添加用户主机构关系和机构岗位关系
							userManager.addUserOrg(userIds, orgId, "lisan");
						}
					}else{
						//是离散用户时直接添加用户主机构和机构岗位关系
						userManager.addUserOrg(userIds, orgId, classType);	
					}
				}else{//调入多个机构开关，如果sys.user.enablemutiorg为true则执行下列语句
					userManager.addUserOrg(userIds, orgId, classType);
				}
			}
			if(flag.equals("0")){		
				System.out.println("删除操作！");
			}
		}
		
		//处理不符合调动的用户。
		if(!"".equals(userReturnInfo))
		{
			
%>
			<script language="javascript">
				var api = parent.parent.frameElement.api, W = api.opener;
			    var msg = "以下用户调入失败。"
			    msg += "\n<%=userReturnInfo%>";
			    W.$.dialog.alert(msg,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				parent.document.all.divProcessing.style.display = "none";
				parent.document.all.button1.disabled = false;
				parent.document.all.button2.disabled = false;
				parent.document.all.exit.disabled = false;
			</script> 	
<%	
		}else{
%>
		<script language="javascript">
			var api = parent.parent.frameElement.api, W = api.opener;
			W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			//parent.document.all.divProcessing.style.display = "none";
			//parent.document.all.button1.disabled = false;
			//parent.document.all.button2.disabled = false;
			//parent.document.all.exit.disabled = false;
		</script>
<%		
		}
%>
		    <script language="javascript">
		    var op;
		    var n = parent.document.all("userIds").options.length-1;
		    
	<%    
	    for(int m=0;m<userIds.length;m++){
	        String userIdValue = userIds[m];        
	%>    
		    for(var i=0;i<parent.document.all("allist").options.length;i++){
		        op = parent.document.all("allist").options[i];	        
		        if(op.value=="<%=userIdValue%>"){
		            parent.addone(op.text,op.value,n);
		            parent.document.all("allist").remove(i);
		        }
		    }
	   
	<%
	}	
	%>
</script> 



