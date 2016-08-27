<%
/*
 * <p>Title: 岗位主界面</p>
 * <p>Description: 岗位主界面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			String curUserId = control.getUserID(); 
			

			String QueryjobName = StringUtil.replaceNull(request.getParameter("jobName"));
			String QueryjobDesc = StringUtil.replaceNull(request.getParameter("jobDesc"));
			String QueryjobNumber = StringUtil.replaceNull(request.getParameter("jobNumber"));
			String QueryjobRank = StringUtil.replaceNull(request.getParameter("jobRank"));
			String QueryjobAmount = StringUtil.replaceNull(request.getParameter("jobAmount"));
			String QueryjobFunction = StringUtil.replaceNull(request.getParameter("jobFunction"));
			String QueryjobCondition = StringUtil.replaceNull(request.getParameter("jobCondition"));
		
			String QuerycreatorName = StringUtil.replaceNull(request.getParameter("creatorName"));
%>
<html>
	<head>
		<title>属性容器</title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>	
	
		<script language="JavaScript">
	
			function deletejob()
			{	
				var checkBoxOnes = document.getElementsByName("checkBoxOne");
				var size = 0;
				
				for(var i = 0; i<checkBoxOnes.length; i++)
				{
					if(checkBoxOnes[i].checked)
					{
						size ++;
					}
				}
				
				if(size == 0)
				{
					$.dialog.alert("<pg:message code='sany.pdp.jobmanage.choose.job'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return ;
				}
				
				$.dialog.confirm("<pg:message code='sany.pdp.delete.comfirm'/>?",function(){
				  	JobForm.action = "deletejob_do.jsp";
					JobForm.target = "hiddenFrame";
					JobForm.submit();
				},null,null,"<pg:message code='sany.pdp.common.alert'/>")
			}
	
			function modifyJob(jobId)
			{
				var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/modifyjob.jsp?jobId="+jobId;
				$.dialog({close:refresh,title:'<pg:message code="sany.pdp.jobmanage.modify.job"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
	        //岗位信息查看
	        //2008-3-24 baowen.liu
			function viewJobInfo(jobId)
				{
					var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/viewJobInfo.jsp?jobId="+jobId;
					$.dialog({title:'<pg:message code="sany.pdp.jobmanage.show.job.info"/>',width:760,height:560, content:'url:'+url,lock: true});
					
				}
				
			//岗位授予机构
			//2008-3-25 baowen.liu
	       function conferorg(jobId)
	             {
	             	var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/job2org.jsp?jobId="+jobId;
					$.dialog({close:refresh,title:'<pg:message code="sany.pdp.jobmanage.confer.org"/>',width:760,height:560, content:'url:'+url,lock: true});
	             }
	             
	         //用户任职情况    
	         //2008-3-25 baowen.liu
	        function post(jobId)
		        {
		        	var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/jobSee_tab.jsp?jobId="+jobId;
					$.dialog({title:'<pg:message code="sany.pdp.jobmanage.user.post.list"/>',width:760,height:560, content:'url:'+url,lock: true});
		        }
		        
		     //岗位权限资源查询    
	         //2008-3-26 baowen.liu
	        function resop(jobId)
		        {
		        	var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/jobres_queryframe.jsp?jobId="+jobId;
					$.dialog({close:refresh,title:'岗位权限资源查询 ',width:760,height:560, content:'url:'+url,lock: true});
		        
		        }  
		        
			function resetQuery()
			{
				document.JobForm.jobNumber.value="";
				document.JobForm.jobName.value="";
				document.JobForm.jobRank.value="";
				document.JobForm.jobAmount.value="";
				document.JobForm.jobFunction.value="";
				document.JobForm.jobDesc.value="";
				document.JobForm.jobCondition.value="";
				document.JobForm.creatorName.value="";
			}
	
			function query()
			{
				var jobName = JobForm.jobName.value;
				var jobNumber = JobForm.jobNumber.value;
				var jobRank = JobForm.jobRank.value;
				var jobAmount = JobForm.jobAmount.value;
				var creatorName = JobForm.creatorName.value;
				var jobFunction = JobForm.jobFunction.value;
				var jobDesc = JobForm.jobDesc.value;
				var jobCondition = JobForm.jobCondition.value;
				
				jobFunction = $.trim(jobFunction);
				jobDesc = $.trim(jobDesc);
				jobCondition = $.trim(jobCondition);
				
				
				 var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
				 if(jobName == "" || jobName.length<1 || jobName.replace(/\s/g,"")=="")
					 {
					 
					 }else{
						 if(!reg.test(jobName))
						   {
						   	   $.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
							   return false;
						   }
					 }
				if(jobNumber == "" || jobNumber.length<1 || jobNumber.replace(/\s/g,"")=="")
					{
					
					}else{
						if(jobNumber.search(/\W/g)!=-1 )
						{
							$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.number.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
							return false;
						}
					}
				if(jobRank == "" || jobRank.length<1 || jobRank.replace(/\s/g,"") == "")
					{
					
					}else{
						if(!reg.test(jobRank))
						{
							$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.level.invali'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
							form.jobRank.focus();
							return;
						}
					}
				if(jobAmount == "" || jobAmount.length<1 || jobAmount.replace(/\s/g,"") == "")   
					{
					
					}else{
						if(!reg.test(jobAmount))
						{
							$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.establishment.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
							form.jobAmount.focus();
							return;
						}
					}
				if(jobFunction == "" || jobFunction.length<1 || jobFunction.replace(/\s/g,"") == "")   
				{
					
				}
				else{
					if(jobFunction.search(/\%/g) != -1)
					{
						//$.dialog.alert('查询中不能包含特殊字符"%"!',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false;
					}
					if(jobFunction.search(/\s/g) != -1)
						{
							//$.dialog.alert("查询中不能包含空格!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
							//return false;
						}
				}
				if( jobDesc == "" || jobDesc.length<1 || jobDesc.replace(/\s/g,"") == "")   
				{
				
				}
				else{
					if(jobDesc.search(/\%/g) != -1)
					{
						//$.dialog.alert('查询中不能包含特殊字符"%"',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false;
					}
				}
				if(jobCondition == "" || jobCondition.length<1 || jobCondition.replace(/\s/g,"") == "")   
				{
				
				}
				else{
					if(jobCondition.search(/\%/g) != -1)
					{
						//$.dialog.alert('查询中不能包含特殊字符"%"',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false;
					}
				}
				if(creatorName == "" || creatorName.length<1 || creatorName.replace(/\s/g,"") == "") 
					{
					
					}else{
						var re_ = /^\w+$/; 
						if(!re_.test(creatorName))
						{
						//$.dialog.alert('创建人中不能有非数字、字母、下划线的字符!',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false; 
						}
					}
				document.JobForm.action = "jobinfo.jsp";
				document.JobForm.submit();
			}
	
			function showAddJob()
			{
				var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/addjob.jsp";
				$.dialog({close:refresh,title:'<pg:message code="sany.pdp.jobmanage.add.job"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
	
			function showJobList(jobId)
			{
				var url="${pageContext.request.contextPath}/purviewmanager/jobmanager/joborgquery.jsp?jobId="+jobId;
				$.dialog({close:refresh,title:'<pg:message code="sany.pdp.role.organization.query"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			function refresh(){
				window.location.reload();
			}
		</script>
	</head>
	<body class="contentbodymargin">
		<div id="" align="center">
		<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
			<form name="JobForm" action="" method="post">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<tr>
										<th><pg:message code="sany.pdp.jobmanage.job.name"/>：</th>
										<td><input type="text" name="jobName" value="<%=QueryjobName%>" size="17"></td>
										<th><pg:message code="sany.pdp.jobmanage.job.number"/>：</th>
										<td><input type="text" name="jobNumber" value="<%=QueryjobNumber%>" size="17"></td>
										<th><pg:message code="sany.pdp.jobmanage.job.level"/>：</th>
										<td><input type="text" name="jobRank" value="<%=QueryjobRank%>" size="17"></td>
										<th><pg:message code="sany.pdp.jobmanage.job.establishment"/>：</th>
										<td><input type="text" name="jobAmount" value="<%=QueryjobAmount%>" size="17"></td>
										</tr>
										<tr>
										<th><pg:message code="sany.pdp.jobmanage.job.duty"/>：</th>
										<td><input type="text" name="jobFunction" value="<%=QueryjobFunction%>" size="17"></td>
										<th><pg:message code="sany.pdp.jobmanage.job.description"/>：</th>
										<td><input type="text" name="jobDesc" value="<%=QueryjobDesc%>" size="17"></td>
										<th><pg:message code="sany.pdp.jobmanage.job.condition"/>：</th>
										<td><input type="text" name="jobCondition" value="<%=QueryjobCondition%>" size="17"></td>
										<th><pg:message code="sany.pdp.workflow.creater.username"/>：</th>
										<td><input type="text" name="creatorName" value="<%=QuerycreatorName%>" size="17"></td>
										<td style="text-align:right">
											<a class="bt_1" onClick='query()'><span><pg:message code="sany.pdp.common.operation.search"/></span></a> 
											<a class="bt_2" onClick='resetQuery()'><span><pg:message code="sany.pdp.common.operation.clear"/></span></a>
										</td>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
				</table>
			</div>
			<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="javascript:showAddJob()"><span><pg:message code="sany.pdp.jobmanage.add.job"/></span></a>
				<a href="javascript:void(0)" class="bt_small" id="delBatchButton" onclick="javascript:deletejob()"><span><pg:message code="sany.pdp.jobmanage.delete.job"/></span></a>
				</div>
			</div>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">

					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.JobSearchList" keyName="JobList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="10" scope="request" data="JobList" isList="false">
					<pg:equal actual="${JobList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${JobList.itemCount}"  value="0">
					<pg:header>
						<th><INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on"></th>
						<th><pg:message code="sany.pdp.jobmanage.job.name"/></th>
						<th><pg:message code="sany.pdp.jobmanage.job.number"/></th>
						<th><pg:message code="sany.pdp.jobmanage.job.level"/></th>
						<th><pg:message code="sany.pdp.jobmanage.job.establishment"/></th>
						<th><pg:message code="sany.pdp.jobmanage.job.duty"/></th>
						<th><pg:message code="sany.pdp.jobmanage.job.description"/></th>
						<th><pg:message code="sany.pdp.jobmanage.job.condition"/></th>
						<th><pg:message code="sany.pdp.workflow.creater.username"/></th>
					</pg:header>
						<pg:param name="jobName" />
						<pg:param name="jobDesc" />
						<pg:param name="jobFunction" />
						<pg:param name="jobAmount" />
						<pg:param name="jobCondition" />
						<pg:param name="jobRank" />
						<pg:param name="jobNumber" />
						<pg:param name="creatorName" />
						<pg:param name="jobId" />
						<%ContextMenu contextmenu = new ContextMenuImpl();%>
						<!--list标签循环输出每条记录-->
						<pg:list>
							<%
								int ownerId = dataSet.getInt("owner_id");
								UserManager userManager=SecurityDatabase.getUserManager();
								User user=userManager.getUserById(String.valueOf(ownerId));
								String userName=user.getUserName();
								String userRealName=user.getUserRealname();
								
								String isDisabled = "";
								//如果岗位不是当前用户创建的复选框不可选
								boolean isFound = curUserId.equals(String.valueOf(ownerId)); 
								if(!isFound && !control.isAdmin()){
									isDisabled = "disabled='true'";
								}
								String jobId = dataSet.getString("jobId");
								Menu menu = new Menu();
								menu.setIdentity("opjob_" + jobId);
								//选中岗位ID是否为1
								boolean isJobIdOne = "1".equals(jobId);
					            //查看岗位信息
					            //2008-3-24 baowen.liu
								Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
								menuitem1.setName(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.show.job.info", request));
								menuitem1.setLink("javascript:viewJobInfo('" + jobId + "')");
								menuitem1.setIcon("../../sysmanager/images/Connector.gif");
								menu.addContextMenuItem(menuitem1);
								//岗位信息修改
								if(!isJobIdOne){
									if(control.isAdmin()){
										Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
										menuitem2.setName(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.modify.job", request));
										menuitem2.setLink("javascript:modifyJob('" + jobId + "')");
										menuitem2.setIcon("../../sysmanager/images/bitmap.gif");
										menu.addContextMenuItem(menuitem2);
									}else{
										if(isFound &&
											control.checkPermission("globaljob","jobmanager",AccessControl.JOB_RESOURCE))
										{
											Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
											menuitem2.setName(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.modify.job", request));
											menuitem2.setLink("javascript:modifyJob('" + jobId + "')");
											menuitem2.setIcon("../../sysmanager/images/bitmap.gif");
											menu.addContextMenuItem(menuitem2);
										}	
									}
									menu.addSeperate();
									
									
									//显示开关
									//2008-3-25 baowen.liu
									//if(ConfigManager.getInstance().getConfigBooleanValue("enablejob", false))
					               // {
				                      //岗位授予机构菜单
				                      //2008-3-25 baowen.liu
				                   		//if(control.isAdmin()){
							                Menu.ContextMenuItem menuitem4=new Menu.ContextMenuItem();
											menuitem4.setName(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.confer.org", request));
											menuitem4.setLink("javascript:conferorg('" + jobId + "')");
											menuitem4.setIcon("../../sysmanager/images/forward_enabled.gif");
											menu.addContextMenuItem(menuitem4);
											menu.addSeperate();
										//}else{
										//	Menu.ContextMenuItem menuitem4=new Menu.ContextMenuItem();
										//	menuitem4.setName("岗位授予机构");
										//	menuitem4.setLink("javascript:conferorg('" + jobId + "')");
										//	menuitem4.setIcon("../images/rightmenu_images/new.gif");
										//	menu.addContextMenuItem(menuitem4);
										//}
									
					                    //隶属机构
									Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
									menuitem3.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.organization.query", request));
									menuitem3.setLink("javascript:showJobList('" + jobId + "')");
									menuitem3.setIcon("../../sysmanager/images/gantt.gif");
									menu.addContextMenuItem(menuitem3);    
					                    //岗位用户任职情况菜单
					                    //2008-3-25 baowen.liu
					                    if (control.checkPermission("globaljob","jobmanager",AccessControl.JOB_RESOURCE))
				                        {
					                         Menu.ContextMenuItem menuitem5=new Menu.ContextMenuItem();
											 menuitem5.setName(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.user.post.list", request));
											 menuitem5.setLink("javascript:post('" + jobId + "')");
											 menuitem5.setIcon("../../sysmanager/images/Host.gif");
											 menu.addContextMenuItem(menuitem5);
				                        }
			                        //}
					                        
			                        //岗位权限资源查询
			                        //2008-3-26 baowen.liu
			                     
			                         //Menu.ContextMenuItem menuitem6=new Menu.ContextMenuItem();
									 //menuitem6.setName("岗位权限查询");
									 //menuitem6.setLink("javascript:resop('" + jobId + "')");
									 //menuitem6.setIcon("../../sysmanager/images/iconAction.GIF");
									 //menu.addContextMenuItem(menuitem6);
								 }
				              
								contextmenu.addContextMenu(menu);
								
							%>
							
							
							<tr class="labeltable_middle_td"  
								onmouseover="this.className='mouseover'"
								onmouseout="this.className= 'mouseout'">
								<td class="tablecells" nowrap="true">
									<P align="left">
									<div align="center">
										<pg:notequal colName="jobId" value="1">
											<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="jobId" defaultValue=""/>' <%=isDisabled %>>
										</pg:notequal>
										<pg:equal colName="jobId" value="1">
											<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" disabled="disabled">
										</pg:equal>
									</div>
									</P>
								</td>
								<td id="opjob_<%=jobId%>"
									height='20' align=left class="tablecells" bgcolor="#F6FFEF">
									<pg:cell colName="jobName" defaultValue="" />
									(<pg:cell colName="jobId" />)
								</td>
								<td height='20' align=left class="tablecells">
									<pg:cell colName="jobNumber" defaultValue="" />
									
								</td>
								
								<td height='20' align=left class="tablecells">
									<pg:cell colName="jobRank" defaultValue="" />
								</td>
								<td height='20' align=left class="tablecells" >
									
									<pg:cell colName="jobAmount" defaultValue="" />
									
								</td>
								<td height='20' align=left class="tablecells">
									<pg:cell colName="jobFunction" defaultValue="" />
									
								</td>
								<td height='20' align=left class="tablecells">
									<pg:cell colName="jobDesc" defaultValue="" />
									
								</td>
								<td height='20' align=left class="tablecells">
								
									<pg:cell colName="jobCondition" defaultValue="" />
									
								</td>
								<td height='20' align=left class="tablecells">
									<%=userName%>【<%=userRealName%>】
								</td>
							</tr>
						</pg:list>
						<%
						request.setAttribute("opjob", contextmenu);
						%>
						<pg:contextmenu enablecontextmenu="true" context="opjob" scope="request" />

				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
						</pg:notequal>
					</pg:pager>

				<div align="center">
				</div>
			</form>
		</div>
	</body>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</html>

