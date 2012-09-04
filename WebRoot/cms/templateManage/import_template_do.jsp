<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.util.zip.*,com.frameworkset.platform.cms.util.FileUtil"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>

<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:330px;display:none">
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
<%
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request, response);
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", -1);
    response.setDateHeader("max-age", 0);
    
    //local本地/remote远程导入
    String importType = request.getParameter("importType"); 
    importType = importType==null?"":importType;
    
    //模板去重标志
    String repeatTmplName = request.getParameter("repeatTmplName");
    //附件去重标志
    String repeatTmplAtt = request.getParameter("repeatTmplAtt"); 
     
    //com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
    //cmsManager.init(request,session,response,control);  
    String siteId = request.getParameter("siteId"); 
    siteId = siteId==null?"":siteId;
   
    //模板ID
    String[] tmplnames = request.getParameterValues("tmplname");
    //模板包名称
    String tmplzipname = request.getParameter("tmplzipname");
    //从session中取出TemplatePackage模板包对象
    TemplatePackage tmplpg = (TemplatePackage)session.getAttribute(tmplzipname);
    session.removeAttribute("tmplzipname");
    
     //将路径中"\"转换为"\\"
   String tmpPath=tmplpg.getUnpackage_path().replaceAll("\\\\","\\\\\\\\");
     
    //模板列表
    List tmplInfos = tmplpg.getTemplateInfos();
        
    TemplatePackageManager pgManager = new TemplatePackageManager();    
    for(int i=0;i<tmplnames.length;i++){
        //tmplname是模板ID
        String tmplname = tmplnames[i];
        if("".equals(tmplname) || "on".equals(tmplname)) 
            continue;     
        
        for(int j=0;j<tmplInfos.size();j++){
            TemplateInfo tmplInfo = (TemplateInfo)tmplInfos.get(j);
            Template template = tmplInfo.getTemplate();
            if(tmplname.equals(String.valueOf(template.getTemplateId()))){
                tmplpg.addSelectTemplateInfo(tmplInfo);
                //break;
            }
        }
    }
    int copytype = pgManager.getFileCopyType(repeatTmplName,repeatTmplAtt);
    pgManager.importTmpls(request,tmplpg,siteId,importType,copytype);
    
    
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<base target="_self">
<body>
<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">    
        <tr>
            <td width="4%" height="30" align="right">
                <img src="../images/ico_point.gif" width="25" height="25">
            </td>
            <td height="30" class="cms_title_blue">
                  模板导入成功
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height:9px; background-color:#3266B1"></td>
        </tr>
        <tr>
            <td colspan="2" style="height:10px; background-color:#fff"></td>
        </tr>
        <tr>
            <td colspan="2">
                <table width="100%" align="center" border="0"  bordercolor="#f0f0f0">
                    <tr>                        
                        <td height="24" align="center">
                               模板导入成功! 
                        </td>                          
                    </tr>
                    <tr>
                       <td colspan=2 height="54" align="center">
		                   <input type="button" type="button" class="cms_button" value="关闭" onclick="closeit()" >
		               </td> 
                    </tr>                    
                </table>
            </td>
        </tr>
    </table>
    <form name="form1" method="post" ></form>
</body>

<script language="javascript">    
    if("<%=importType%>" == "local")//本地导入,刷新左边的树        
        parent.parent.TreeFrm.location.href = parent.parent.TreeFrm.location;
    function closeit(){
	    if(parent.document.all("divProcessing"))
	        parent.document.all("divProcessing").style.display="none";
	    if("<%=importType%>" == "local"){ 
			if(confirm("您确定要关闭吗?") ){
				deleteTmpDir();  
			}
		}else{
		    deleteTmpDir(); 
		    window.returnValue="ok";			
			window.close();
		}
		 
	}  	
	function deleteTmpDir(){
	   var tmpPath = "<%=tmpPath%>";
	    document.form1.action = "deleteTmpDir.jsp?tmpPath="+tmpPath;
	    document.form1.submit();	    
	}  
	
</script>
</html>