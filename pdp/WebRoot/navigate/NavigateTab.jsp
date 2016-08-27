<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
			String flag="";	
			flag=request.getParameter("flag");	
			request.getSession().removeAttribute("configmodule");
			request.getSession().removeAttribute("theModelId");
			request.getSession().removeAttribute("thePersonName");
			request.getSession().removeAttribute("theDmId");
			request.getSession().removeAttribute("parentName");
%>
	<%
			//String id = "";
			//id =  (String)request.getAttribute("theModelId");	
			//String dmId =  (String)request.getAttribute("theDmId");	
			//String url1 = request.getContextPath() + "/warningmodel/openModelConfigPage.action?reqObj.modelId=" + id;
			//String url2 = request.getContextPath() + "/warningmodel/disposalsuggestion/disposalsuggestion.jsp?modelid=" + id;
			//String url3 = request.getContextPath() + "/warningmodel/queryModelInfo.action?modelid=" + dmId;
			//String url4 = request.getContextPath() + "/warningmodel/modelversion/modelversionlist.jsp?modelid=" + id;
			String url1=request.getContextPath() +"/manageguide/manageguide_main.jsp";
			String url2=request.getContextPath() +"/pw/rulesmanager/rulesmain.page";
			String url3=request.getContextPath() +"/warningmodel/mainNavigate.jsp?configModule=1";
			String url4=request.getContextPath() +"/warningmodel/mainNavigate.jsp?configModule=2";
			String url5=request.getContextPath() +"/warningmodel/mainNavigate.jsp?configModule=3";
			
		%>
<html>
<head>
<title>配置导航</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/button.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/bz.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/input.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/jf.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/table.css">


<script language="JavaScript" src="<%=basePath%>/common/scripts/jquery-1.4.4.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	 var iiii="1";
	$(document).ready(function() {
		document.getElementById("ttttt").focus();
		var indexIdStr='<%=flag%>';
	    if(indexIdStr=="1"){
	    	document.getElementById("texttest").innerHTML="对指标定义和积分计算中用到的规则进行管理，可灵活、动态配置各类指标";
	    	iiii="1";
	    	$("#litest1").removeClass("bs2_1").addClass("bs1");
	    	$("#litest2").removeClass("bs1").addClass("bs2_1");
			$("#litest7").css("display","none");
			$('#ttttt').attr('src', '<%=url2%>');
	    }
	    //else if(indexIdStr=="2"){
	    //	document.getElementById("texttest").innerHTML="对所有预警指标的规则定义和维护";
	    //	iiii="2";
	    //	$("#litest2").removeClass("bs2_1").addClass("bs1");
	    //	$("#litest1").removeClass("bs1").addClass("bs1_1");
		//	$("#litest7").css("display","");
		//	$('#ttttt').attr('src', '<%=url1%>');
	    //}
	});
	   
	    
		function next(){
			document.getElementById("ttttt").focus();
				if(iiii=="1"){
				
				iiii="2";
				document.getElementById("texttest").innerHTML="对所有预警指标的规则定义和维护";
				$("#litest2").removeClass("bs2_1").addClass("bs1");
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url1%>');
				
			}else if(iiii=="2"){
				iiii="3";
				document.getElementById("texttest").innerHTML="按照人员类型和指标库建立积分预警的指标体系。";
				$("#litest3").removeClass("bs2_1").addClass("bs1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url3%>');
				
			}else if(iiii=="3"){
				iiii="4";
				document.getElementById("texttest").innerHTML="业务人员根据实际需要按照指标体系设置积分规则、分值。";
				$("#litest4").removeClass("bs2_1").addClass("bs1");
				$("#litest3").removeClass("bs1").addClass("bs2_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url4%>');
				
				
			}else if(iiii=="4"){
				iiii="5";
				document.getElementById("texttest").innerHTML="根据处置级别设置积分阀值，在积分输出时根据阀值自动显示处置建议";
				$("#litest5").removeClass("bs3_1").addClass("bs1");
				$("#litest4").removeClass("bs1").addClass("bs2_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url5%>');
				
			}
		}
		
		function pre(){
			document.getElementById("ttttt").focus();
			if(iiii=="5"){
				iiii="4";
				document.getElementById("texttest").innerHTML="业务人员根据实际需要按照指标体系设置积分规则、分值。";
				$("#litest4").removeClass("bs2_1").addClass("bs1");
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");	
				$("#litest3").removeClass("bs1").addClass("bs2_1");				
				$('#ttttt').attr('src', '<%=url4%>');
				$("#litest7").css("display","");
				
			}else if(iiii=="4"){
				iiii="3";
				document.getElementById("texttest").innerHTML="按照人员类型和指标库建立积分预警的指标体系。";
				$("#litest3").removeClass("bs2_1").addClass("bs1");
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");			
				$('#ttttt').attr('src', '<%=url3%>');
				$("#litest7").css("display","");
				
			}else if(iiii=="3"){
				iiii="2";
				document.getElementById("texttest").innerHTML="对所有预警指标的规则定义和维护";
				$("#litest2").removeClass("bs2_1").addClass("bs1");
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest3").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");			
				$('#ttttt').attr('src', '<%=url1%>');
				$("#litest7").css("display","");
				
			}else if(iiii=="2"){
				iiii="1";
				document.getElementById("texttest").innerHTML="对指标定义和积分计算中用到的规则进行管理，可灵活、动态配置各类指标";
				$("#litest1").removeClass("bs1_1").addClass("bs1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest3").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");				
				$('#ttttt').attr('src', '<%=url2%>');
				
			}
		}
		function locate(objvalue){
			document.getElementById("ttttt").focus();
			if(objvalue=="1"){
				iiii="1";
				document.getElementById("texttest").innerHTML="对指标定义和积分计算中用到的规则进行管理，可灵活、动态配置各类指标";
				$("#litest1").removeClass("bs1_1").addClass("bs1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");
				$("#litest3").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");	
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest7").css("display","none");
				$('#ttttt').attr('src', '<%=url2%>');
				
			}else if(objvalue=="2"){
				iiii="2";
				document.getElementById("texttest").innerHTML="对所有预警指标的规则定义和维护";
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest2").removeClass("bs2_1").addClass("bs1");
				$("#litest3").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");	
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url1%>');
				
			}else if(objvalue=="3"){
				iiii="3";
				document.getElementById("texttest").innerHTML="按照人员类型和指标库建立积分预警的指标体系。";
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest3").removeClass("bs2_1").addClass("bs1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");	
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url3%>');
				
			}else if(objvalue=="4"){
				iiii="4";
				document.getElementById("texttest").innerHTML="	业务人员根据实际需要按照指标体系设置积分规则、分值。";
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest4").removeClass("bs2_1").addClass("bs1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");	
				$("#litest3").removeClass("bs1").addClass("bs2_1");	
				$("#litest5").removeClass("bs1").addClass("bs3_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url4%>');
			
			}else if(objvalue=="5"){
				iiii="5";
				document.getElementById("texttest").innerHTML="	根据处置级别设置积分阀值，在积分输出时根据阀值自动显示处置建议。";
				$("#litest1").removeClass("bs1").addClass("bs1_1");
				$("#litest5").removeClass("bs3_1").addClass("bs1");
				$("#litest2").removeClass("bs1").addClass("bs2_1");	
				$("#litest3").removeClass("bs1").addClass("bs2_1");	
				$("#litest4").removeClass("bs1").addClass("bs2_1");
				$("#litest7").css("display","");
				$('#ttttt').attr('src', '<%=url5%>');
				
				
			}
		}
		</script>
</head>
<body>

<table width="100%" border="0" cellspacing="0">
  <tr>
    <td><table width="100%" border="0" align="center" cellspacing="0" class="xl1">
      <tr>
        <td width="189" id="litest1" class="bs1"><a href="javascript:locate('1')">1.规则配置</a> </td>
        <td width="189"  id="litest2" class="bs2_1"><a href="javascript:locate('2')">2.管控指标配置</a></td>
        <td width="189"  id="litest3" class="bs2_1"><a href="javascript:locate('3')">3.指标体系配置</a></td>
        <td width="189"  id="litest4" class="bs2_1"><a href="javascript:locate('4')">4.积分模型配置</a></td>
        <td width="189"  id="litest5" class="bs3_1"><a href="javascript:locate('5')">5.处置建议配置</a></td><!--
        <td>&nbsp;</td>
      --></tr>
    </table></td>
  </tr><tr>
    <td><table width="100%" border="0" align="center" cellspacing="0" class="btable">
      <tr>
        <td id="texttest">对指标定义和积分计算中用到的规则进行管理，可灵活、动态配置各类指标</td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
  <td><table width="100%" height="350"  align="center" style="padding: 5px;	border: 1px solid #43a9ea;" >
      <tr>
        <td><iframe src="" height="100%" width="100%" id="ttttt" name="ttttt" scrolling="auto" frameborder="0" /></iframe></td>
      </tr>
    </table></td>
 
  </tr>
  <tr>
  
   <td  colspan="2">
   <table width="200"   align="center" >
      <tr>
       <td  id="litest7" align="right" style="display: none" >
      <input name="pre" type="button" class="button" onclick="pre();" value="上一步" />
      <td  id="litest9" align="right" >
      
    </td>
        <td width="10"></td>
       <td id="litest6" align="left">
      <input name="next" type="button" class="button" onclick="next();" value="下一步" />
    </td>        
      </tr>
    </table></td>
   
   
  </tr>
</table>

</body>
</html>

