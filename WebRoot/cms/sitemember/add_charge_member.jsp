<%@ include file="../../sysmanager/include/global1.jsp"%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	String reFlush = "true";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "false";
	}
	
	String setDisbaled = "";
	if ( request.getAttribute("userNameDisable") != null ){
		setDisbaled = "disabled = true";
	}		
	
	UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	String userId = null;
	if(user!=null)
	{
		 userId  = user.getUserId();
	}
	
	if(user == null)
		user = new UserInfoForm();
		
	String isNew = (String)request.getAttribute("isNew");
	if(isNew == null ){
		isNew = "0";
	}
	String serviceType ="";
	if((String)request.getAttribute("serviceType")!=null){
		serviceType = (String)request.getAttribute("serviceType");
	}
	
	
	String isaddorupdate = (String)request.getAttribute("isaddorupdate");
	
	if(isaddorupdate == null ){
		isaddorupdate = "1";
	}
%>

<html>
	<head>
		<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../../sysmanager/scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>userInfo</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
	    <style type="text/css">
<!--
.STYLE1 {color: #FF0000}
.style2 {
	font-size: 16px;
	font-weight: bold;
}
-->
        </style>
</head>

<script language="JavaScript">

var reFlush = "<%=reFlush%>";
var userErr = 0;



function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
 
function storeUser()
{
	 	var isSelect = false;
	    for (var i=0;i<document.forms[0].elements.length;i++) {
			var e = document.forms[0].elements[i];
				
			if (e.name == 'roleId'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			 }
	    }
	    
	    for (var i=0;i<document.forms[0].elements.length;i++) {
			var e = document.forms[0].elements[i];
				
			if (e.name == 'drugId'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			 }
	    }


	var UserInfoForm = document.all.UserInfoForm;
	var city = UserInfoForm.city.value;
	var area = UserInfoForm.area.value;
	if (UserInfoForm.city.value.length<1){
       alert("请选择市(县)!");
	   UserInfoForm.city.focus();
	   return false
    }	
	if (UserInfoForm.area.value.length<1){
       alert("请选择地区!");
	   UserInfoForm.area.focus();
	   return false
    }	

	if(UserInfoForm.userRealname.value.search(/[\\\/\|:\*\?<>"']/g)!=-1){
		alert("单位名称不能有\\/|:*?<>\"'!等特殊字符");
		UserInfoForm.userRealname.focus();
		return false;
	}
	document.UserInfoForm.userRealname.disabled = false;
	
	if (validateForm(UserInfoForm) )
	{
		var userRealname= document.forms[0].userRealname.value;
		if (trim(userRealname).length == 0 ){
    	alert("请输入单位名称！"); 
    	return false;
    } 
    	
   		
		document.UserInfoForm.action="../member/memberManager.do?method=storeChargeMember&city="+city+"area="+area;
		document.UserInfoForm.submit();
	}
}
function updateUser()
{
	
	document.UserInfoForm.userName.disabled = false;
	
	if (validateForm(UserInfoForm) )
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    	alert("请输入登陆名！"); 
    	return false;
    } 
    	
   		if (confirm("您确定要修改该用户信息吗？")){
		document.UserInfoForm.action="../member/memberManager.do?method=updateMember&flag=2";
		document.UserInfoForm.submit();
		}
	}
}
function defaultpass()//恢复用户初始密码
{
		var userId= document.forms[0].userId.value;
		if (confirm("您确定要恢复为初始密码吗？")){
			document.UserInfoForm.action="../member/memberManager.do?method=defaultpass&userName="+userId+"&flag=2";
			document.UserInfoForm.submit();
		}
}

function reloadUser(){
	document.UserInfoForm.submit();
}

function newUser()
{
	document.UserInfoForm.action="../user/userManager.do?method=newUser";
	document.UserInfoForm.submit();
}

function back()
{
	document.location.href="../member/memberManager.do?method=getMemberList&flag=2";	
}

function resetuser()
{
	var userId= document.forms[0].userId.value;
	document.location.href="../member/memberManager.do?method=updateMember&memberId="+userId;	
}

function setDisable(v){
	v.disabled = false;
	if  ( document.UserInfoForm.userId.value != "" )
	{
		v.disabled = true;
	}
}


//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.UserInfoForm.city.focus();
	}
	

	//复选框全部选中
	function checkAll(totalCheck,checkName){	
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	//复选框全部选中
	function checkAll2(totalCheck,checkName){	
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	</script>
	<script language="JavaScript"> 
<!-- 
var g_selProvince; 
var g_selCity; 
var Provinces=new Array( 
new Array("A","长沙市"), 
new Array("B","株洲市"), 
new Array("C","湘潭市"), 
new Array("D","衡阳市"), 
new Array("E","邵阳市"), 
new Array("F","岳阳市"), 
new Array("G","常德市"), 
new Array("I","益阳市"), 
new Array("J","娄底地区"), 
new Array("K","郴州市"), 
new Array("H","张家界市"), 
new Array("L","永州市"), 
new Array("M","怀化市"), 
new Array("N","自治州"), 
new Array("P","省中心")

); 

var Citys=new Array( 

new Array("A01","长沙市直"), 
new Array("A02","芙蓉区"), 
new Array("A03","天心区"),
new Array("A04","岳麓区"),
new Array("A05","开福区"),
new Array("A06","雨花区"),
new Array("A07","长沙县"),
new Array("A08","望城县"),
new Array("A09","浏阳县"),
new Array("A10","宁乡县"),

new Array("B01","株洲市直"), 
new Array("B02","天元区"), 
new Array("B03","荷塘区"),
new Array("B04","石峰区"),
new Array("B05","芦淞区"),
new Array("B06","醴陵市"),
new Array("B07","株洲县"),
new Array("B08","攸县"),
new Array("B09","茶陵县"),
new Array("B10","炎陵县"),

new Array("C01","湘潭市直"), 
new Array("C02","西湖区"), 
new Array("C03","岳塘区"),
new Array("C04","韶山市"),
new Array("C05","湘乡市"),
new Array("C06","湘潭县"),

new Array("D01","衡阳市直"), 
new Array("D02","江东区"), 
new Array("D03","城南区"),
new Array("D04","城北区"),
new Array("D05","郊区"),
new Array("D06","南岳区"),
new Array("D07","耒阳市"), 
new Array("D08","衡阳县"), 
new Array("D09","衡南县"),
new Array("D10","衡山县"),
new Array("D11","衡东县"),
new Array("D12","常宁县"),
new Array("D13","祁东县"),

new Array("E01","邵阳市直"), 
new Array("E02","双清区"), 
new Array("E03","大祥区"),
new Array("E04","北塔区"),
new Array("E05","邵东县"),
new Array("E06","新邵县"),
new Array("E07","邵阳县"), 
new Array("E08","隆回县"), 
new Array("E09","武冈市"),
new Array("E10","洞口县"),
new Array("E11","新宁县"),
new Array("E12","绥宁县"),
new Array("E13","城步县"),

new Array("F01","岳阳市直"), 
new Array("F02","岳阳楼区"), 
new Array("F03","云溪区"),
new Array("F04","汨罗市"),
new Array("F05","岳阳县"),
new Array("F06","平江县"),
new Array("F07","湘阴县"), 
new Array("F08","华容县"), 
new Array("F09","临湘市"),
new Array("F10","君山区"),
new Array("F11","屈原区"),

new Array("G01","常德市直"), 
new Array("G02","武陵区"), 
new Array("G03","鼎城区"),
new Array("G04","德山市"),
new Array("G05","津市市"),
new Array("G06","安乡县"),
new Array("G07","汉寿县"), 
new Array("G08","澧县"), 
new Array("G09","临澧县"),
new Array("G10","桃源县"),
new Array("G11","石门县"),

new Array("I01","益阳市直"), 
new Array("I02","赫山区"), 
new Array("I03","资阳区"),
new Array("I04","沅江市"),
new Array("I05","南县"),
new Array("I06","桃花县"),
new Array("I07","安化县"), 

new Array("J01","娄底地直"), 
new Array("J02","娄底市"), 
new Array("J03","冷水江市"),
new Array("J04","涟源市"),
new Array("J05","双峰县"),
new Array("J06","新化县"),

new Array("K01","郴州市直"), 
new Array("K02","北湖区"), 
new Array("K03","苏仙区"),
new Array("K04","资兴市"),
new Array("K05","桂阳县"),
new Array("K06","永兴县"),
new Array("K07","宜章县"), 
new Array("K08","嘉禾县"), 
new Array("K09","临武县"),
new Array("K10","汝城县"),
new Array("K11","桂东县"),
new Array("K12","安仁县"),
 
new Array("H01","张家界市直"), 
new Array("H02","永定区"), 
new Array("H03","武陵源区"),
new Array("H04","慈利县"),
new Array("H05","桑植县"),

new Array("L01","永州市直"), 
new Array("L02","芝山区"), 
new Array("L03","冷水滩区"),
new Array("L04","东安县"),
new Array("L05","道县"),
new Array("L06","宁远县"),
new Array("L07","江永县"), 
new Array("L08","江华县"), 
new Array("L09","蓝山县"),
new Array("L10","新田县"),
new Array("L11","双牌县"),
new Array("L12","祁阳县"),

new Array("M01","怀化市直"), 
new Array("M02","鹤城区"), 
new Array("M03","洪江市"),
new Array("M04","中方县"),
new Array("M05","沅陵县"),
new Array("M06","辰溪县"),
new Array("M07","溆浦县"), 
new Array("M08","麻阳县"), 
new Array("M09","新晃县"),
new Array("M10","芷江县"),
new Array("M11","会同县"),
new Array("M12","靖州市"),
new Array("M13","通道县"),
   
new Array("N01","自治州直"), 
new Array("N02","吉首市"), 
new Array("N03","泸溪县"),
new Array("N04","凤凰县"),
new Array("N05","花垣县"),
new Array("N06","保靖县"),
new Array("N07","古丈县"), 
new Array("N08","永顺县"), 
new Array("N09","龙山县"),

new Array("P01","自来"), 
new Array("P02","发行部"), 
new Array("P03","咨询部"),
new Array("P04","办公室"),
new Array("P05","开发部"),
new Array("P06","广告部"),
new Array("P07","报纸编辑"), 
new Array("P08","药品办"), 
new Array("P09","社会力量"),
new Array("P10","赠阅交换")
); 

function FillProvinces(selProvince) 
{ 
	selProvince.options[0]=new Option("请选择",""); 
	for(i=0;i<Provinces.length;i++) 
	{ 
	selProvince.options[i+1]=new Option(Provinces[i][1],Provinces[i][0]); 
	} 
	selProvince.options[0].selected=true; 
	selProvince.length=i+1; 
} 

function FillCitys(selCity,ProvinceCode) 
{ 
	//if the city is a direct-managed area, like Beijing, shanghai, tianjin, chongqin,hongkong, macro 
	//need not "请选择选项" 
	if(ProvinceCode=="A"||ProvinceCode=="B"||ProvinceCode=="C" 
	||ProvinceCode=="D"||ProvinceCode=="E"||ProvinceCode=="F") 
	count=0; 
	else 
	{selCity.options[0]=new Option("请选择",""); 
	count=1;} 
	for(i=0;i<Citys.length;i++) 
	{ 
		if(Citys[i][0].toString().substring(0,1)==ProvinceCode.substring(0,1)) 
		{ 
		selCity.options[count]=new Option(Citys[i][1],Citys[i][0]); 
		count=count+1; 
		} 
	} 
	selCity.options[0].selected=true; 
	selCity.length=count; 
} 

function Province_onchange() 
{ 
	FillCitys(g_selCity,g_selProvince.value); 
} 

function InitCitySelect(selProvince,selCity) 
{ 
	//alert("begin"); 
	g_selProvince=selProvince; 
	g_selCity=selCity; 
	selProvince.onchange=Function("Province_onchange();"); 
	FillProvinces(selProvince); 
	Province_onchange(); 
} 
function InitCitySelect2(selProvince,selCity,CityCode) 
{ 
	InitCitySelect(selProvince,selCity) 
	for(i=0;i<selProvince.length;i++) 
	{ 
		if(selProvince.options[i].value.substring(0,2)==CityCode.substring(0,2)) 
		{ 
		selProvince.options[i].selected=true; 
		} 
	} 
	Province_onchange(); 
	for(i=0;i<selCity.length;i++) 
	{ 
		if(selCity.options[i].value==CityCode) 
		{ 
		selCity.options[i].selected=true; 
		} 
	} 
} 
//--> 
</script>
	<body class="info"  onload="">
		<form   name="UserInfoForm" method="post">
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden" name="remark3" value="<pg:cell colName="remark3"  defaultValue=""/>" />
		<input type="hidden" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" />
		
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" >
			 <tr height="35">
				<td colspan=4 height="25" class="detailtitle">
				  <div align="center">
			        <%if(isNew.equals("1")){%>
					  <span class="style2">新增收费用户</span>
			        <%}else{%>
					  <span class="style2">修改收费用户信息</span>
			        <%}%>
				    </div></td>
			</tr>
			 <tr height="35">
				<td colspan=4 height="25" class="detailtitle">
				</td>
			</tr>
			<%if(isNew.equals("1")){%>
				 <tr>
				 	  <td height="25" class="detailtitle" width="23%"></td>
	                  <td height="25" class="detailtitle" width="7%"><strong>会员所在市(县)：</strong></td>
	                  <td colspan=2 height="25">  
	           						<SELECT id=city size=1 name=city style="width:160px">
	           						<OPTION selected></OPTION>
	           						</SELECT> 
	           						<span class="STYLE1">*</span>
	  				  </td>
	  				
  				  </tr> 
  				  <tr>
  				  	  <td height="25" class="detailtitle" width="23%"></td>
	                  <td height="25" width="20%" class="detailtitle" width="23%"><strong>地区:</strong></td>
	  				  <td colspan=3 height="25" >
					  		
	                    			<SELECT id=area size=1 name=area style="width:160px"><OPTION selected></OPTION></SELECT> 
										<SCRIPT language=javascript> 
												InitCitySelect(document.UserInfoForm.city,document.UserInfoForm.area); 
									</SCRIPT> 
									<span class="STYLE1">*请先选择市(县)</span>
					  </td>
					
  				 </tr> 
			<%}%>
			<%if(!isNew.equals("1")){%>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="23%"><strong>帐号:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" id="userName" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="100" <%=setDisbaled%> onfocus="setDisable(this)"><span class="STYLE1">*</span>
				</td>
				
			</tr>
			<%}%>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>会员名称:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="用户姓名" maxlength="100"><span class="STYLE1">*</span>
				</td>
				
			</tr>
		                
			<tr>	
			    <td height="25" class="detailtitle" width="23%"></td>					
				<td height="25" class="detailtitle"><strong> 口令:</strong></td>
				<td colspan=3 height="25">
					<input type="password" style="width:160px" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令" maxlength="40">
					<span class="STYLE1">*缺省口令为123456</span>
				</td>
				
			</tr>
			
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
						<strong>联系地址:</strong>
				</td>
				<td  colspan=3 height="25">
						<input type="text" style="width:160px" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" >
				</td>
				
			</tr>
			<tr>
			  <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>单位名称:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="enterpriseName" value="<pg:cell colName="enterpriseName"  defaultValue=""/>" maxlength="100">
			  </td>
			
			 </tr> 
			<tr>
			  <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>联系人:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="enterpriseLinkman" value="<pg:cell colName="enterpriseLinkman"  defaultValue=""/>">
			  </td>
			 
			 </tr>
			 <tr>
			  <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>联系电话:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  cnname="单位电话" maxlength="13">
			  </td>
			  
			 </tr>
			 <tr>
			  <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>电子邮件:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40">
			  </td>
			 
			 </tr>
			  <tr>
			   <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>忘记密码时问题:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="question" value="<pg:cell colName="question"  defaultValue=""/>" maxlength="100">
			  </td>
			  
			 </tr>
			  <tr>
			   <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>忘记密码时答案:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="answer" value="<pg:cell colName="answer"  defaultValue=""/>" maxlength="40">
			  </td>
			  
			 </tr>
			  <tr>
			   <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>单位网址:</strong></td>
			  <td colspan=3 height="25" >
			  		<input type="text" style="width:160px" name="network_address" value="<pg:cell colName="network_address"  defaultValue=""/>" >
			  </td>
			 
			 </tr>
			  <tr>
			   <td height="25" class="detailtitle" width="23%"></td>
              <td height="25" width="20%" class="detailtitle" width="23%"><strong>用户状态:</strong></td>
			  <td colspan=3 height="25" >
			  		<dict:select style="width:100px" dictTypeName="status" name="status" defaultValue="1" value="<%=user.getStatus()%>"/>
			  </td>
			 
			 </tr>    
			<tr>
				 <td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle"><strong> 开始时间:</strong></td>
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="dredgeTime" onclick="showdate(document.all('dredgeTime'))" readonly="true" value="<pg:cell colName="dredgeTime"  defaultValue=""/>">
				</td>
				
			</tr>
			<tr>
				 <td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle"><strong> 结束时间:</strong></td>
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="past_Time"onclick="showdate(document.all('past_Time'))" readonly="true" value="<pg:cell colName="past_Time"  defaultValue="" />" validator="stringNull" cnname="结束时间" maxlength="40">
					
				</td>
				
			</tr>
			<tr>
				 <td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle"><strong> 会员类别:</strong></td>
				<td colspan=3 height="25">
							<select name="serviceType" style="width:160px" >
       							<option value="A"<%if(serviceType.equals("A")){%>selected<%}%>>
									A类会员
								</option>
								<option value="B"<%if(serviceType.equals("B")){%>selected<%}%>>
									B类会员
								</option>
								<option value="C"<%if(serviceType.equals("C")){%>selected<%}%>>
									C类会员
								</option>
								<option value="D"<%if(serviceType.equals("D")){%>selected<%}%>>
									D类会员
								</option>
								<option value="E"<%if(serviceType.equals("E")){%>selected<%}%>>
									E类会员
								</option>
								<option value="F"<%if(serviceType.equals("F")){%>selected<%}%>>
									F类会员
								</option>
							</select>
				</td>
				
			</tr>
			
			<tr>
				 <td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
						<strong>申请资源说明:</strong>
				</td>
				<td colspan=3 height="25">
						<input type="text" style="width:160px" name="serverexplain" value="<pg:cell colName="serverexplain"  defaultValue=""/>" maxlength="100">
				</td>
				
			</tr>
			
			<tr>
				 <td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
					<strong>邮政编码:</strong>
				</td>
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="8">
				</td>
		   
				
		   </tr>
		   
		 
		 <tr>
		 	 <td height="25" class="detailtitle" width="23%"></td>
			<td height="25" class="detailtitle">
				<strong>单位简介:</strong>
			</td>
			<td colspan=3 height="25">
				<textarea name="enterpriseIntro"   cols="40" rows="2" defaultValue=""><pg:cell colName="enterpriseIntro" defaultValue=""/></textarea>
			</td>
		</tr>
		 <tr>
		 	 <td height="25" class="detailtitle" width="23%"></td>
			<td height="25" class="detailtitle">
				<strong>备注:</strong>
			</td>
			<td colspan=3 height="25">
				<textarea name="user_desc"   cols="40" rows="2" defaultValue=""><pg:cell colName="user_desc" defaultValue=""/></textarea>
			</td>
		</tr>
		
		 <tr>
		 	 <td height="25" class="detailtitle" width="23%"></td>
			<td height="25" class="detailtitle">
				<strong>用户授权:</strong>
			</td>
			<td colspan=3 height="25">
					<%
						
						RoleManager roleManager = SecurityDatabase.getRoleManager();
						List listrole = roleManager.getRoleList("select * from td_sm_Role r where r.role_Type='member' and r.role_Id<>'15' and r.remark1='1' order by r.role_Name");
						List listdrug = roleManager.getRoleList("select * from td_sm_Role r where r.role_Type='member' and r.role_Id<>'15' and r.remark1='2' order by r.role_Name");
						request.setAttribute("listrole",listrole);
						request.setAttribute("listdrug",listdrug);           							
					%>
					
					<font color="#FF6600">价格在线栏目：</font> 
					</br>
					全选<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','roleId')">
					</br>
					<pg:list requestKey="listrole">
					
					<input name="roleId" type="checkbox" 
					<%
			        	DBUtil db = new DBUtil();
			        	String sql ="select role_id from td_sm_userrole where user_id ="+userId;
			        	db.executeSelect(sql);
			        	String eid = dataSet.getString("roleId");
			        	if(db.size()>0)
			        	{
			        		for(int i=0;i<db.size();i++)
			        		{
			        			if(eid.equals(db.getString(i,"role_id")))
			        			{
			        				out.println("checked");
			        				break;
			        			}
			        		
			        		}
			        	}
					%> 
					value="<pg:cell colName="roleId"/>" onclick="" >
					<pg:cell colName="roleName"/>
					</pg:list>
			</td>
       </tr>
        <tr>
        	 <td height="25" class="detailtitle" width="23%"></td>
			<td height="25" class="detailtitle">
				
			</td>
			<td colspan=3 height="25">
				 <font color="#FF6600">医药网栏目：</font>	
					      
					</br>
					全选<input type="checkBox" name="checkBoxAll2" onClick="checkAll2('checkBoxAll2','drugId')">
					</br>
					<pg:list requestKey="listdrug">
					
					<input name="drugId" type="checkbox" 
					<%
			        	DBUtil db1 = new DBUtil();
			        	String sql ="select role_id from td_sm_userrole where user_id ="+userId;
			        	db1.executeSelect(sql);
			        	String gid = dataSet.getString("roleId");
			        	if(db1.size()>0)
			        	{
			        		for(int i=0;i<db1.size();i++)
			        		{
			        			if(gid.equals(db1.getString(i,"role_id")))
			        			{
			        				out.println("checked");
			        				break;
			        			}
			        		
			        		}
			        	}
					%> 
					value="<pg:cell colName="roleId"/>" onclick="" >
					<pg:cell colName="roleName"/>
					</pg:list>
			</td>
       </tr>
     
			<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
			<input type="hidden" name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue=""/>" />
		</table>
		</pg:beaninfo>
		</form>
		
		
		<table width="100%" cellpadding="2" cellspacing="0" >
			<tr>
				<td align="center">
				
						<%
							//新增没有重置按钮
							if(isNew.equals("0")){
						%>
						<input name="update" type="button" class="input" value="修改" onClick="updateUser()">
						<input name="default" type="button" onClick="defaultpass()" class="input" value="密码恢复">
						<%
						}
						else {
						%>
						<input name="resave" type="button" class="input" value="保存" onClick="storeUser()">
						<%}%>
						<input name="Submit32" type="submit" class="input" value="返回" onClick="back()">
				
				</td>
			</tr>
		</table>
		
	</body>
</html>