<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.sql.*,java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.TmplateExport"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%  
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);  
    ServletContext context =session.getServletContext();   
    String realPath = context.getRealPath("/");   
    //模板存放的路径
    String templatePath = application.getRealPath("cms/siteResource/siteTemplate");
    templatePath = templatePath.replaceAll("\\\\","/");
    TemplateManager tmplUtil = new TemplateManagerImpl();
    //所有公共模板包
    List list = tmplUtil.exportTmplRecordList(0,200);    
%>
<html>
    <head>
        <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
        <script src="../inc/js/func.js"></script>
        <script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
        <style type="text/css">
            a   {   cc:expression(window.status='')   }  
        </style>
    </head>
    <body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
        <table id="tag_attach_list" width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
            <tr>
                <td height='25' colspan="9" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
                    <div  class="DocumentOperT">导出模板操作</div>                    
                    <a style="cursor:hand" onClick="delTag()"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">删除</div></a>
                </td>
            </tr> 
        </table>
        <form name="tagOperate" target="taglist" method="POST">
        <table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
        <tr class="cms_report_tr">
            <td><input type="checkbox" id="id" class="checkbox" value="" onclick="selectall()"></td>
            <td class="cms_title_blue">模板名</td>
            <td class="cms_title_blue">模板描述</td>
            <td class="cms_title_blue">公共/私有【站点】</td>
            <td class="cms_title_blue">操作</td>
        </tr>
        <%
        for(int i=0;i<list.size();i++){
            TmplateExport tmplModel = (TmplateExport)list.get(i);
            String promptMsg = "公共模板";
            if("1".equals(tmplModel.getFlag())) promptMsg = "私有模板【" + tmplModel.getSiteid() + "】";
        %>
        <tr>
            <td><input type="checkbox" id="id" name="id" value="<%=tmplModel.getTmplname()%>"></td>
            <td><a href="javascript:downloadTmpl('<%=tmplModel.getTmplname()%>')"><%=tmplModel.getTmplname()%></a></td>
            <td><a href="javascript:downloadTmpl('<%=tmplModel.getTmplname()%>')"><%=tmplModel.getTmpldesc()%></a></td>
            <td><a href="javascript:downloadTmpl('<%=tmplModel.getTmplname()%>')"><%=promptMsg%></a></td>
            <td><input type="button" value="下载" class="cms_button" onclick="downloadTmpl('<%=tmplModel.getTmplname()%>')"></td>
        </tr>    
        <%
        }
        %>        
        </table>
        </form>
        <iframe name="taglist" width="0" height="0"></iframe>
    </body>
    <SCRIPT LANGUAGE="JavaScript">
    <!--
    var checkflag = false;    
    //全选中复选框
    function selectall(){
        var o = document.getElementsByName("id");
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
        var o = document.getElementsByName("id");
        for (var i=0;i<o.length;i++){
            if(!o[i].disabled){
                if (o[i].checked==false){
                    cbs=false;
                }
            }
        }
    }
    //响应附件下载标签事件
    function downloadTmpl(path)
    {   
        path = "<%=templatePath%>/" + path + ".zip";
        window.open("download.jsp?file="+path,"_blank","width=1,height=1,top=100001,left=100001");
    }
    //模态窗口打开模式 新增标签
    function addTag(url,swidth,sheight){
        //var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:no;status:no");
        //return w;
        var returnVal;
        returnVal = openWin(url,swidth,sheight);
        if(returnVal=="True"){
            window.location.reload();  
        }
    }
    //删除标签
    function delTag(){
        var o = document.getElementsByName("id");
        var flag  = false;
        for (var i=0; i<o.length; i++){
            if(!o[i].disabled){
                if(o[i].checked==true) {
                    flag = true; 
                }
            }
        }
        if(!flag){
            alert("请选择要删除的记录!");
        }else{
            if(confirm("您确定要删除该记录吗?")){
                document.tagOperate.target = "taglist";
                document.tagOperate.action = "tmplZipDelete.jsp";
                document.tagOperate.submit();
            }
        }
        
    }
    
    
    //-->
    </SCRIPT>
</html>


