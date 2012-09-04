
<%
/*
 * <p>Title: 用户排序</p>
 * <p>Description: 用户排序</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
%>
<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    String orgId = request.getParameter("orgId");
%>
<html>
    <head>
        <title>机构下用户排序</title>
        <SCRIPT LANGUAGE="JavaScript">
        var http_request = false;
        //初始化，指定处理的函数，发送请求的函数
        function send_request(url){
            //http_request = false;
            //开始初始化XMLHttpRequest对象
            //if(window.XMLHttpRequest){//Mozilla
            //    http_request = new XMLHttpRequest();
            //    if(http_request.overrideMimeType){//设置MIME类别
            //        http_request.overrideMimeType("text/xml");                      
            //    }
            //}
            //else if(window.ActiveXObject){//IE
           //     try{
            //        http_request = new ActiveXObject("Msxml2.XMLHTTP");
            //    }catch(e){
            //        try{
            //            http_request = new ActiveXObject("Microsoft.XMLHTTP");                          
           //         }catch(e){
            //        }
            //    }
            //}
           // if(!http_request){
            //    alert("不能创建XMLHttpRequest对象");
            //    return false;
            //}
           // http_request.onreadystatechange = processRequest;
           // http_request.open("GET",url,true);
           // http_request.send(null);
			document.form1.action = url;
			document.form1.target = "hiddenFrame";
			document.form1.submit();
        }
        
        var dflag = false;
        function init(){
	        if(dflag){
	            //document.forms[0].b1new.disabled=true;   
	            //document.forms[0].b3del.disabled=true;   
	            //document.forms[0].button1.disabled=true; 
	          //  document.forms[0].button2.disabled=true; 
	           // document.forms[0].button3.disabled=true; 
	           // document.forms[0].button4.disabled=true; 
	        }
        }        
        //持久化
        function sendUser(){    
           var len=document.all("userList").options.length;
           var orgId = document.all("orgId").value;           
           var userId=new Array() 
           for (var i=0;i<len;i++){       
             userId[i]=document.all("userList").options[i].value;
           }
           send_request('userorderchange.jsp?orgId='+orgId+'&userId='+userId);
        }
   
        function up1() {
            var len=document.all("userList").options.length;
            var isselected = 0;
            for (var i=0;i<len;i++){
                var op=document.all("userList").options[i];
                if(op.selected){            
                    isselected = 1;
                }
            }
            if(isselected == 0){
                return;
            }
            for (var i=0;i<len;i++){
                var op=document.all("userList").options[i];
                if(op.selected){            
                    var tmp = new Option(op.text,op.value);
                    if(i>0){
                        var dest = document.all("userList").options[i-1];
                        var userId1 = tmp.value; 
                        var userId2 = dest.value;                   
                        
                        document.all("userList").options[i-1] = tmp;
                        document.all("userList").options[i] = dest;
                        document.all("userList").options[i-1].selected=true;
                        
                    }
                }
            }
            //sendUser()
        }
        
        
        function down1() {
            var len=document.all("userList").options.length;
            var isselected = 0;
            for (var i=0;i<len;i++){
                var op=document.all("userList").options[i];
                if(op.selected){            
                    isselected = 1;
                }
            }
            if(isselected == 0){
                return;
            }
            var flag = 0;
            for (var i=0;i<len;i++){
                var op=document.all("userList").options[i];
                if(op.selected){            
                    var tmp = new Option(op.text,op.value);             
                    op.selected=false;
                    if(i == len-1){
                        flag = len-2;
                    }
                    else {
                        flag = i;
                    }
                    if(i<len-1){
                        var dest = document.all("userList").options[i+1];
                        var userId2 = tmp.value;   
                        var userId1 = dest.value;     
                        var userSn = i;
                        document.all("userList").options[i+1] = tmp;
                        document.all("userList").options[i] = dest;
                                         
                    }
                }                
            }
            //sendUser()
            document.all("userList").options[flag+1].selected=true;
        }
        
        function upall() {
			var len=document.all("userList").options.length;	
			var isselected = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			isselected = 1;
		   		}
		   	}
		   	if(isselected == 0){
		   		return;
		   	}
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			var tmp = new Option(op.text,op.value);
		   			op.selected = false;   			
		   			var j=i;   			
		   			for(;j>=1;j--){     				
		   				var atmp =  document.all("userList").options[j-1];   				
		   				var btmp = new Option(atmp.text,atmp.value);   				
		   				document.all("userList").options[j] = btmp;   				
		   			}   			
		   			document.all("userList").options[0] = tmp;	
		   		}
		    }
		    //sendUser();
		    document.all("userList").options[0].selected=true;
		}
        
        function downall() {
			var len=document.all("userList").options.length;
			var isselected = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			isselected = 1;
		   		}
		   	}
		   	if(isselected == 0){
		   		return;
		   	}	
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			var tmp = new Option(op.text,op.value);
		   			op.selected = false;   			
		   			var j=i;   			
		   			for(;j<len-1;j++){     				
		   				var atmp =  document.all("userList").options[j+1];   				
		   				var btmp = new Option(atmp.text,atmp.value);   				
		   				document.all("userList").options[j] = btmp;   				
		   			}   			
		   			document.all("userList").options[len-1] = tmp;	
		   		}
		    }
		    //sendUser();
		    document.all("userList").options[len-1].selected=true;
		}
		function deleteUser(){}
		
		
        
        
    </SCRIPT>
    </head>
    
    <body onload=init() onunload="window.returnValue = 'ok'">
        <form name="form1" method="post">
        <div align="center">
            <input type="hidden" name="orgId" value="<%=orgId%>">
            <table width="450" height="20" border="0" align="center">
                <tr>
                    <td height="16" colspan="15" valign="top" width="60%">
                        <div align="center">
                           <pg:message code="sany.pdp.workflow.user.list"/>
                        </div>
                    </td>
                    <td width="40%"></td>                  
                </tr>
                
                <tr>
                    <td height="400" colspan="15" width="60%" valign="top">
                        <div align="center">
                            <select name="userList" multiple style="width:90%;height:400" onDBLclick="deleteUser()" size="18">
                                <pg:listdata dataInfo="OrgSubUserList" keyName="OrgSubUserList" />
                                <pg:pager  scope="request" data="OrgSubUserList" isList="true">
                                <pg:list>
                                    <option value="<pg:cell colName="userId"/>">
                                        <pg:cell colName="userName" />(<pg:cell colName="userRealname" />)
                                    </option>
                                </pg:list>
                                </pg:pager>
                            </select>
                        </div>
                    </td>
                    <td width="40%">
                        <table align="left" width="100%" border="0"  valign="top" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="center" class="detailcontent">
                                    <a class="bt_2" onClick="upall()" ><span><pg:message code='sany.pdp.top'/></span></a>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" class="detailcontent">&nbsp;
                                    
                              </td>
                            </tr>
                            <tr>
                                <td align="center">
                                    <a class="bt_2" onClick="up1()" ><span><pg:message code='sany.pdp.up'/></span></a>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" class="detailcontent">&nbsp;
                                    
                              </td>
                            </tr>
                            <tr>
                                <td align="center" class="detailcontent">
                                    <a class="bt_2"  onClick="down1()"><span><pg:message code='sany.pdp.down'/></span></a>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" class="detailcontent">&nbsp;
                                    
                              </td>
                            </tr>
                            <tr>
                                <td align="center" class="detailcontent">
                                    <a class="bt_2" onClick="downall()"><span><pg:message code='sany.pdp.bottom'/></span></a>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" class="detailcontent">&nbsp;
                                    
                              </td>
                            </tr>
                        </table>
                    </td>
                </tr> 
            </table>
            <table width="450" height="33" border="0" align="center">
                <tr>
					<td height="33">
						<div align="center">
							<a class="bt_1" onclick="sendUser()"><span><pg:message code="sany.pdp.common.confirm"/></span></a>
							<a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
						</div>
					</td>
			    </tr>             
          </table>
          </div>
        </form>
        <div id=divProcessing style="width:200px;height:25px;position:absolute;left:340px;top:298px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			    <tr>
				    <td bgcolor=#3A6EA5>
					    <marquee align="middle" behavior="alternate" scrollamount="5">
						    <font color=#FFFFFF><pg:message code="sany.pdp.common.operation.processing"/></font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
		<div style="display:none">	
		<iframe name="hiddenFrame" width=0 height=0 border=0></iframe>
		</div>
    </body>
</html>

