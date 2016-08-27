<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.util.zip.*,com.frameworkset.platform.cms.util.FileUtil,java.io.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.container.Template"%>
<%@ page import="com.frameworkset.platform.cms.container.TmplateExport"%>
<html>
<script language="javascript">
//window.dialogTop = "px"; 
//window.dialogLeft = "px"; 
window.dialogHeight = "450px"; 
window.dialogWidth = "550px";
</script>
<%
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request, response);
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", -1);
    response.setDateHeader("max-age", 0);
       
    com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
	cmsManager.init(request,session,response,control);	
	String siteId = cmsManager.getSiteID();	
    siteId = siteId==null?"":siteId;
    //建立一个临时目录
    String tmplzipname = "unziptemp"+Math.random();    
    File temp = new File(application.getRealPath("cms/siteResource/siteTemplate"),tmplzipname);
    temp.mkdirs();
    
    List upzipList = null;
    FileItemFactory fileitemfactory = new DefaultFileItemFactory(2048000,temp);
    FileUpload fu = new FileUpload(fileitemfactory);
    List fields = fu.parseRequest(request);
      
    String zipPath = "";    
    TemplatePackageManager packageManager = new TemplatePackageManager();
    TemplatePackage tmplPackage = null;
    for(int i=0;fields!=null&&i<fields.size();i++){
        FileItem file = (FileItem)fields.get(i);
        if("impfile".equalsIgnoreCase(file.getFieldName())) {            
            zipPath = file.getName();
            zipPath = zipPath.replaceAll("\\\\","/");
            tmplPackage = packageManager.unzipTemplatePackage(temp.getAbsolutePath(),zipPath); 
            break; 
        }        
    }
    
      
    
    /**
    * 判断压缩包里面是否有xml文件。
    * 有则 认为是上传的原来导出的模板，构造模板包对象
    * 没有 则认为是新增模板，跳转到template_import.jsp页面
    */    
    List templateInfos = new ArrayList();
    if(tmplPackage.isTemplatePackage()){//导入现有模板包        
        templateInfos = tmplPackage.getTemplateInfos();
        session.removeAttribute(tmplzipname); 
        session.setAttribute(tmplzipname,tmplPackage);
%>


<html>
<head>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<title>本地模板包导入</title>
<base target="_self">
<body>
    <form name="importForm" method="POST">
    <input type="hidden" name="tmplzipname" value="<%=tmplzipname%>">    
    <table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">    
        <tr>
            <td width="4%" height="30" align="right">
                <img src="../images/ico_point.gif" width="25" height="25">
            </td>
            <td height="30" class="cms_title_blue">
                  导入模板
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height:9px; background-color:#3266B1"></td>
        </tr>
        <tr>
            <td colspan="2" style="height:10px; background-color:#fff"></td>
        </tr>
        <tr>
            <td>                
            </td>
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
    
    function nextStep(){
        var selectOne = false;
        var o = document.getElementsByName("tmplname");
        for (var i=0; i<o.length; i++){
            if(o[i].checked){
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
		document.importForm.action = "import_template_do.jsp?siteId=<%=siteId%>&tmplzipname=<%=tmplzipname%>";
        document.importForm.submit();         
    }
    function preStep(){
        document.importForm.action = "select_import_local.jsp?siteId=<%=siteId%>";
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

    
<%  //--------------------------------------------------------------------------------------------------------------      
    }else{//采集模板描述文件
%>
<script language="javascript">
window.dialogTop = "100px"; 
window.dialogLeft = "100px"; 
window.dialogHeight = "500px"; 
window.dialogWidth = "720px";
</script>
<frameset cols="200,*" frameborder="no" border="0" name="imglist" framespacing="0">
	<frame src="tmplFilesTree.jsp?rootPath=<%=tmplzipname%>&siteId=<%=siteId%>" name="TreeFrm" scrolling="auto" noresize>
	<frame src="template_import.jsp?tmplzipname=<%=tmplzipname%>&siteId=<%=siteId%>" name="ImageListFrm" scrolling="auto">	
</frameset>

<%        
    }    
	
%>
</html>
