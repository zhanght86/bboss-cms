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
.style5 {font-size: 16px}
.STYLE6 {color: #0000FF}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 站点授权</title>
</head>
<script language="javascript">
 function validBox(eleName)
{
        var ele = document.getElementsByName(eleName); //add by eyes
        //alert(ele[2].mis);
        //alert("ele length:"+ele.length);
        //   eval("var chks="+name+"."+ele.name);
        var selectCount=0;
        for(var i=0;i<ele.length;i++)
        {
                if(ele[i].checked)
                {
                        selectCount++;
                }
        }

        if(ele[0].mis!=null && !isNaN(ele[0].mis))//注意这里用ele[0]，而不是ele.minsel
        {
                //alert("有mis");
                if(selectCount<parseInt(ele[0].mis))
                {
                        alert(convertNullToSpace(ele.on)+"至少选择"+ele[0].mis+"项");
                        ele[0].focus();
                        return false;
                }
        }
        if(ele[0].mas!=null && !isNaN(ele[0].mas))
        {
                if(selectCount>parseInt(ele[0].mas))
                {
                        alert(convertNullToSpace(ele[0].on)+"至多选择"+ele[0].mas+"项");
                        ele[0].focus();
                        return false;
                }
        }
        return true;
}
function selectAll(form)
{
  for (var i=0;i<form.elements.length;i++)
    {
     var e = form.elements[i];
     e.checked = 1;
    }
	div_all.style.display="none";
	div_no.style.display="block";
	
}

function selectNo(form)
{
  for (var i=0;i<form.elements.length;i++)
    {
     var e = form.elements[i];
     e.checked = 0;
    }
	div_all.style.display="block";
	div_no.style.display="none";
	
}



  function adduser(){	    	
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
<a id="reload" href="site_right.jsp" style="display:none">reload...</a>
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
              <td height="77" colspan="8" valign="top" background="../images/toolbar_common_func_site.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 站点授权</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px">&nbsp;</td>
                </tr>
              </table>                <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div>              <div align="center"></div></td>
			  </tr>
            <tr >
              <td height="20" colspan="8">&nbsp;
                             <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div>                <div align="center"></div></td>
			  </tr>
			    <tr>
              <td height="20" colspan="10"><div align="center">
              	<input type="button" name="btn_adduser" value="添加用户" class="cms_button" onClick="adduser()">
              	<input name="btn_addrole"  onClick="addrole()" type="button" class="cms_button" id="btn_addrole" value="添加角色" />
                <input name="btn_addgroup" onClick="addorg()" type="button" class="cms_button" id="btn_addgroup"  value="添加组织" />                
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="checkbox" name="checkbox" value="checkbox">
                <span class="STYLE6">清除子对象权限设置,并将子对象权限设为继承父系权限</span><img src="../images/tan.gif" width="11" height="11"></div>                <div align="center">
			      </div>              <div align="center"></div>			      <div align="center"></div>			      <div align="center">
			        </div>			      <div align="center">
			        </div>			        <div align="center">
			          </div>			        <div align="center">
		          </div></td>
			  </tr>
            
            <tr>
              <td colspan="8" height="20"><div  id="div_all" onClick="selectAll(fm_site)" align="left" style="cursor:hand">
                <div align="left">&nbsp;全选</div>
              </div>
              <div  style='display:none;cursor:hand' id='div_no' onClick='selectNo(fm_site)' align='left' >&nbsp;全不选</div></td>
              </tr>
            
            <!--来源于别的网站才有父站点-->
            <tr class="cms_report_tr">
              <td width="59" height="20"><div align="center">删除</div></td>
			<td width="106"><div align="center">用户/角色/组织</div></td>
			<td width="61"><div align="center">修改站点</div></td>
			<td width="62"><div align="center">删除站点</div></td>
			<td width="88"><div align="center">预览站点首页</div></td>
			<td width="101"><div align="center">完全发布站点</div></td>
			<td width="103"><div align="center">增量发布站点</div></td>
			<td width="125"><div align="center">设定站点用户</div></td>
            </tr>
            <tr  class="cms_data_tr">
			   <td height="20"><div align="center">删除</div></td>
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
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
            </tr>
            <tr  class="cms_data_tr">
              <td height="20"><div align="center">删除</div></td>
				<td><div align="center">user1</div></td>
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
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
            </tr>
            <tr  class="cms_data_tr">
             <td height="20"><div align="center">删除</div></td>
				<td><div align="center">user2</div></td>
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
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
            </tr>
            <tr  class="cms_data_tr">
              <td height="20"><div align="center">删除</div></td>
				<td><div align="center">user3</div></td>
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
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
            </tr>
            <tr  class="cms_data_tr">
             <td height="20"><div align="center">删除</div></td>
				<td><div align="center">user4</div></td>
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
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
            </tr>
            <tr  class="cms_data_tr">
			  <td height="20"><div align="center">删除</div></td>
				<td><div align="center">user5</div></td>
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
				<td><div align="center">
				  <input type="checkbox" name="checkbox" value="checkbox">
				</div></td>
            </tr>
            <tr>
              <td height="227"><div align="center"></div></td>
				<td><div align="center"></div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				</div></td>
				<td height="227"><div align="center">
				  </div></td>
				<td><div align="center">
				  </div></td>
				<td><div align="center">
				  <p>&nbsp;				</p>
				  <p>&nbsp;    </p>
				</div></td>
              </tr>
            <tr>
              <td height="22" colspan="8"><div align="center">
                <input name="btn_ok" type="submit" class="cms_button"  onclick="javascript:window.close();" value="应用" />
                <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="关闭" />
              </div>                <div align="center">
			      </div>              <div align="center"></div>			      <div align="center"></div>			      <div align="center">
			        </div>			      <div align="center">
			        </div>			        <div align="center">
			          </div>			        <div align="center">
			          </div></td>
			  </tr>
            <tr>
              <td height="11" colspan="8" background="../images/bottom_gray.jpg">&nbsp;</td>
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
<area shape="rect" coords="80,-2,81,3" href="#"><area shape="rect" coords="87,3,153,23" href="template_right.jsp">
<area shape="rect" coords="160,3,224,32" href="workflow_right.jsp" >
<area shape="rect" coords="160,3,224,32" href="channel_right.jsp" >
<area shape="rect" coords="160,3,224,32" href="doc_right.jsp" >
</map></body>
</html>