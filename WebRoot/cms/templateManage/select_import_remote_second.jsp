<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.Template"%>
<%@ page import="com.frameworkset.platform.cms.container.TmplateExport"%>
<%@ page import="java.sql.*,java.util.*,java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%
    /**
     * 解压压缩包,分析获得模板包对象,根据模板id保存到session, 通过选择模板ID,
     * 在下一步中,通过模板ID, 决定那些模板真正导入
     * session的清除: 被选中的模板,在下一步中清除
     *               没被选中的模板,在点下一步这个动作的时候清除 
     * 一次只能选择一个模板包
     */
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request, response);
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", -1);
    response.setDateHeader("max-age", 0);
    //压缩文件绝对路径
    String zipAbsPath = application.getRealPath("cms/siteResource/siteTemplate");
    zipAbsPath = zipAbsPath.replaceAll("\\\\","/");
    
    String siteId = request.getParameter("siteId");
    siteId = siteId==null?"":siteId;   
    //保存模板
    List tmplConnection = new ArrayList();
        
    TemplatePackageManager packageManager = new TemplatePackageManager();
    String tmpziplname = request.getParameter("checkValue");
    //模板描述文件
    String xmlFileName = "";  
    String zipPath = CMSUtil.getPath(zipAbsPath,tmpziplname+".zip");  
    //建立一个临时目录
	File temp = new File(zipAbsPath,tmpziplname+Math.random());
	temp.mkdirs();	
    //解压 构造模板包对象
    TemplatePackage tmplPackage = packageManager.unzipTemplatePackage(temp.getAbsolutePath(),zipPath);
    //导出模板对象列表<TemplateInfo>
    List templateInfos = new ArrayList();
    if(tmplPackage.isTemplatePackage()){
        //分析出模板包对象
        //模板包对象包含多个模板
        //模板对象包行多个附件
        //同时,根据模板描述文件,把解压后的文件按模板分离,用目录结构组织
        templateInfos = tmplPackage.getTemplateInfos();
        session.removeAttribute(tmpziplname); 
        session.setAttribute(tmpziplname,tmplPackage); 
    }else{
        //肯定有描述文件,分支不存在
    }    
%>
<html>
<head>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<title>服务器端模板导入</title>
<base target="_self">
<body>
    <form name="importForm" method="POST">
    <input type="hidden" name="tmpziplname" value="<%=tmpziplname%>">    
    <table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">    
        <tr>
            <td width="4%" height="30" align="right">
                <img src="../images/ico_point.gif" width="25" height="25">
            </td>
            <td height="30" class="cms_title_blue">
                  选中导入模板
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height:9px; background-color:#3266B1"></td>
        </tr>
    </table>
    <table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
        <tr>
			<td width="100" height="25" readonly="true" align="left" nowrap class="STYLE5">模板重复:</td>
			<td>
			    &nbsp<INPUT TYPE="radio" NAME="repeatTmplName" localvalue="cover">覆盖已有模板&nbsp&nbsp
			    <INPUT TYPE="radio" NAME="repeatTmplName" localvalue="rename" checked>重命名导入模板
			</td>
		</tr>
		<tr>
			<td width="100" height="25" readonly="true" align="left" nowrap class="STYLE5">模板附件重复:</td>
			<td>
				&nbsp<INPUT TYPE="radio" NAME="repeatTmplAtt" localvalue="cover">覆盖已有附件&nbsp&nbsp
				<INPUT TYPE="radio" NAME="repeatTmplAtt" localvalue="useold" checked>使用已有附件
			</td>
		</tr>
        <tr>
            <td colspan="2" style="height:9px; background-color:#3266B1"></td>
        </tr>
        <tr>
            <td colspan="2" style="height:10px; background-color:#fff"></td>
        </tr>
    </table>
    <table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">    
        <tr class="cms_report_tr">
			<td width="2%" align=center style="width:5%"><input class="checkbox" name="tmplname" value="on" checked type="checkbox" id="tmplname" onclick="selectall()"></td>
			<td width="8%">模板名称</td>
		</tr>   
        <%
        //构造列表:
        //list<模板对象>
        for(int i=0;i<templateInfos.size();i++){
            TemplateInfo tmplinfo = (TemplateInfo)templateInfos.get(i);
            Template inTmpl = tmplinfo.getTemplate();
        %>
        <tr>
            <td><input type="checkbox" id="tmplname" name="tmplname" checked value="<%=inTmpl.getTemplateId()%>"></td>
            <td><%=inTmpl.getName()%></a></td>            
        </tr>    
        <%
        }
        %>        
        </table>
        
    <table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">   
        <tr>
            <td colspan="2" style="height:20px; background-color:#fff"></td>
        </tr>        
        <tr>       
            <td colspan="2" align="center">
                <input name="button1" onClick="preStep()" type="button" class="cms_button" value="上一步" />&nbsp;&nbsp;
                <input name="button2" onClick="nextStep()" type="button" class="cms_button" value="下一步" />
            </td>
        </tr>
	</table>
	</form>
	<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150px;top:250px;display:none">
		<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
		    <tr>
			    <td bgcolor=#3A6EA5>
				    <marquee align="middle" behavior="alternate" scrollamount="5">
					    <font color=#FFFFFF>...处理中...请等待...</font>
					</marquee>
				</td>
			</tr>
		</table>
	</div>
</body>
<iframe id="hidForm" name="hidForm" src="" style="display:none"></iframe>
<script>
    function getRadioValue(radioName){
	    var radioValue = "";
	    var arr = new Array();
	    arr = document.all(radioName);
	    for(var i=0;i<arr.length;i++){
	        if(arr[i].checked){
	            radioValue = arr[i].value;
	            break;
	        }   
	    }
	    return radioValue;
	}
    function nextStep(){
        var selectOne = false;
        var o = document.getElementsByName("tmplname");
        //add by ge.tao
        //date 2008-01-15
        //选择从服务器端上传时，列出了所有空模板包，选择一项后，继续下一步会出现异常.
        //新增判断 o[i].value!= "on"
        for (var i=0; i<o.length; i++){
            
            if(o[i].checked && o[i].value!= "on"){
                selectOne = true;
            }
        }
        if(!selectOne){
            alert("请选择要导入的模板!");
            return false;
        }       
        
		document.all.button1.disabled = true;
		document.all.button2.disabled = true;
		document.all("divProcessing").style.display="";
		//document.importForm.target="hidForm"; 		
		document.importForm.action = "import_template_do.jsp?siteId=<%=siteId%>&tmplzipname=<%=tmpziplname%>"
		                           + "&repeatTmplName="+getRadioValue("repeatTmplName")
		                           + "&repeatTmplAtt="+getRadioValue("repeatTmplAtt");
        document.importForm.submit();         
    }
    function preStep(){
        document.importForm.action = "select_import_remote.jsp?siteId=<%=siteId%>";
        document.importForm.submit();
    }
    var checkflag = false;  
    //全选中复选框
    function selectall(){
        var o = document.getElementsByName("tmplname");
        if(checkflag==false){
            for (var i=0; i<o.length; i++){
                if(!o[i].disabled){
                        o[i].checked=true;
                }
            }
            checkflag = true;
        }else{
            for (var i=0; i<o.length; i++){
                if(!o[i].disabled){
                        o[i].checked=false;
                }
            }
            checkflag = false;
        }
    }
    //单个选中复选框
    function checkOne(id){
        var o = document.getElementsByName("tmplname");
        for (var i=0;i<o.length;i++){
            if(!o[i].disabled){
                if (o[i].checked==false){
                    cbs=false;
                }
            }
        }
    }
    
</script>
</html>