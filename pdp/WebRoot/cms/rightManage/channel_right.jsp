<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0); 
%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.STYLE4 {color: #799BFF}
.style5 {font-size: 16px}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 站点授权</title>
</head>
<script language="javascript">
//站点窗口输入参数校验
function check_form(){
	
	if(fm_site.sitename.value==""){
		alert("请输入站点中文名称!");
		fm_site.sitename.focus();
		return false
	}
    if(fm_site.sitedesc.value==""){
		alert("请输入站点描述!");
		fm_site.sitedesc.focus();
		return false
	}
   if(fm_site.sitepath.value==""){
		alert("请输入站点存放路径!");
		fm_site.sitepath.focus();
		return false
	}
	
	 // if(fm_site.hometemplateid.value==""){
	//	alert("请选择站点首页模板!");
	//	fm_site.hometemplateid.focus();
	//	return false
	 //}
	return true
}

 
  function adduser(){	    	
	    	//window.open('<%=rootpath%>/sysryxz.jsp?type1=document.forms[0].type1&type2=document.forms[0].type2','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
			var orgId = "1";
			var jobId = "1";
			
			window.open('../../sysmanager/orgmanager/org2user.jsp?orgId='+orgId+'&jobId='+jobId,'newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
			
		}

  function addrole(){
	  	   window.open('../../sysmanager/accessmanager/role/roleQuery_cms.jsp','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
	  		
  }
  function addorg(){
  		   var jobId = "1";
	  	   window.open('../../sysmanager/jobmanager/A03/job2org.jsp?jobId='+jobId,'newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
	  		
  }		
 
  
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  background="../images/grayline_bg.jpg" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0">
<a id="reload" href="channel_right.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form  name="fm_site" method="post" action="site_createdo.jsp"  onsubmit="return check_form()">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  
   
	<!--来源于别的网站才有父站点-->
    <tr >
      <td height="22" style="padding-top:3px"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="2" bgcolor="D7D7D7"></td>
        </tr>
        <tr>
          <td bgcolor="D7D7D7"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
            
            <!--来源于别的网站才有父站点-->
            
            <tr >
              <td height="77" colspan="12" valign="top" background="../images/toolbar_common_func_site.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 频道类授权</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px">&nbsp;</td>
                </tr>
              </table>                <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div></td>
			  </tr>
            
			    <tr>
			      <td colspan="11"></td>
			      <td height="20" colspan="10">&nbsp;</td>
		        </tr>
			    <tr>
			      <td colspan="11"> &nbsp;
			        <input type="button" name="btn_adduser" value="添加用户" class="cms_button" onClick="adduser()">
                    <input name="btn_addrole"  onClick="addrole()" type="button" class="cms_button" id="btn_addrole" value="添加角色" />
                    <input name="btn_addgroup" onClick="addorg()" type="button" class="cms_button" id="btn_addgroup"  value="添加组织" />
                    <input type="checkbox" name="checkbox2" value="checkbox">
重新设置子对象权限并全部设为继承父系权限 </td>
              <td height="20" colspan="10"><div align="center"></div>                <div align="center">
			      </div>              <div align="center"></div>			      <div align="center"></div>			      <div align="center">
			        </div>			      <div align="center">
			        </div>			        <div align="center">
			          </div>			        <div align="center">
		          </div></td>
			  </tr>
			    <tr>
			      <td colspan="11"></td>
			      <td height="20" colspan="10">&nbsp;</td>
		        </tr>
            
            <tr style="display:none">
              <td height="22" colspan="12"><div align="center"></div>			  	</td>
              </tr>
            <!--来源于别的网站才有父站点-->
            <tr class="cms_report_tr">
              <td width="27" height="20"><div align="center">
               <input type="checkbox" name="checkbox" value="checkbox">
             </div></td>
			 <td width="45"><div align="center">删除</div></td>
				<td width="61"><div align="center">用户/角色</div></td>
				<td width="62"><div align="center">添加频道</div></td>
				<td width="65"><div align="center">删除频道</div></td>
				<td width="62"><div align="center">修改频道</div></td>
				<td width="63"><div align="center">浏览频道</div></td>
				<td width="79"><div align="center">浏览频道首页</div></td>
				<td width="84"><div align="center">完全发布频道</div></td>
				<td width="74"><div align="center">增量发布频道</div></td>
				<td width="65"><div align="center">废稿管理</div></td>
				<td width="70"><div align="center">扩展字段</div></td>
            </tr>
            <tr class="cms_data_tr">
			   <td height="20"><div align="center">
				<input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">删除</div></td>
				<td><div align="center">admin</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td>
				  <div align="center">
				    <input type="checkbox" name="checkbox" value="checkbox">
				    </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
            </tr>
            <tr class="cms_data_tr">
              <td height="21"><div align="center">
				<input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">删除</div></td>
				<td><div align="center">admin</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td>
				  <div align="center">
				    <input type="checkbox" name="checkbox" value="checkbox">
				    </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
            </tr>
            <tr class="cms_data_tr">
             <td height="20"><div align="center">
				<input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">删除</div></td>
				<td><div align="center">admin</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td>
				  <div align="center">
				    <input type="checkbox" name="checkbox" value="checkbox">
				    </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
            </tr>
            <tr class="cms_data_tr">
              <td height="20"><div align="center">
				<input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">删除</div></td>
				<td><div align="center">admin</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td>
				  <div align="center">
				    <input type="checkbox" name="checkbox" value="checkbox">
				    </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
            </tr>
            <tr class="cms_data_tr">
             <td height="20"><div align="center">
				<input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">删除</div></td>
				<td><div align="center">admin</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td>
				  <div align="center">
				    <input type="checkbox" name="checkbox" value="checkbox">
				    </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
            </tr>
            <tr class="cms_data_tr">
			  <td height="20"><div align="center">
				<input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">删除</div></td>
				<td><div align="center">admin</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
				<td>
				  <div align="center">
				    <input type="checkbox" name="checkbox" value="checkbox">
				    </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
                <td><div align="center">
                  <input type="checkbox" name="checkbox" value="checkbox">
                </div></td>
            </tr>
            <tr>
              <td height="20"><div align="center">
				</div></td>
				<td><div align="center"></div></td>
				<td><div align="center"></div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				</div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				  <p>&nbsp;				</p>
				  <p>&nbsp;    </p>
				</div></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
              <td height="22" colspan="12"><div align="center">
                <input name="btn_ok" type="submit" class="cms_button"  onclick="javascript:window.close();" value="应用" />
                <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="退出" />
              </div>                <div align="center">
			      </div>              <div align="center"></div>			      <div align="center"></div>			      <div align="center">
			        </div>			      <div align="center">
			        </div>			        <div align="center">
			          </div>			        <div align="center">
			          </div></td>
			  </tr>
            <tr>
              <td height="11" colspan="12" background="../images/bottom_gray.jpg">&nbsp;</td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td height="3" bgcolor="D7D7D7"></td>
        </tr>
      </table></td>
    </tr>
    
    
  <!--来源于别的网站才有父站点-->
</table>   
</form>

<map name="Map">
<area shape="rect" coords="80,-2,81,3" href="javascript:void(0)"><area shape="rect" coords="87,3,153,23" href="template_right.jsp">
<area shape="rect" coords="160,3,224,32" href="workflow_right.jsp" >
<area shape="rect" coords="160,3,224,32" href="channel_right.jsp" >
<area shape="rect" coords="160,3,224,32" href="doc_right.jsp" >
</map></body>
</html>