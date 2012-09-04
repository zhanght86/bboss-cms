<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String docpath = "publicAttachment";
	String type = request.getParameter("type");//判断是图片还是附件
	String typeName = "";
	if("2".equals(type))
		typeName = "附件";
	if("3".equals(type))
		typeName = "图片";
	if("4".equals(type))
		typeName = "标签附件";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>:::::相关<%=typeName%>的选择::::::::::::::::::::::.</title>
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.operStyle{
	width:17;
	height:16;
	}
.style7 {font-size: 5px}
</style>
</head>
<body bgcolor="#e7e7f7">	
    <input type="hidden" name="filenum" id="filenum" value="0" />
	<form target="uploadattach" method="post" name="uploadform" enctype="multipart/form-data">
		
		<input type="hidden" id="subMutiFile"  />
		<table width="100%" bgcolor="#e7e7f7">
			<tr>
				<td width="100%">
					<font color="#000000">
						1、点“
						<font color="#8080ff">浏览...</font>
						”按钮，选择你想要上传的文件，
					</font>
					<br>
	        <table name="EWTable" id="EWTable"></table>
					<br>
					<br>
					<font color="#000000">
						2、选定一个文件后，点击“
						<font color="#8080ff">确定</font>
						”按钮，将该文件上传；
						<br>
					</font>
					<center>
						<input type="button" name="add" value="确定" onclick="subform()">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="close" value="取消" onclick="window.close();">
					</center>
				</td>
			</tr>
		</table>
	</form>
	<div id="divProcessing" style="width:180px;height:30px;position:absolute;left:83px;top:190px;display:none">
		<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			<tr>
				<td bgcolor=#3A6EA5>
					<marquee align="middle" behavior="alternate" scrollamount="5">
						<font color=#FFFFFF>...文件上传中...请等待...</font>
					</marquee>
				</td>
			</tr>
		</table>
	</div>
	<iframe name="uploadattach" height="0" width="0"></iframe> 
</body>
<SCRIPT LANGUAGE="JavaScript">
    function addRow(){
			var filenmu = document.all("filenum").value;
			var etable = document.all("EWTable");
			var row1 = etable.insertRow();
			var td1 = document.createElement("td");
			var td2 = document.createElement("td");
			var td3 = document.createElement("td");
			td1.innerHTML = "<tr id='tr'><td><input type='file' id='file"+filenmu+"' name='file"+filenmu+"'></td>";
			td2.innerHTML = "<td><input type='button' onclick='addRow(this)' value='更多'></td>";
			td3.innerHTML = "<td><input type='button' onclick='deleteRow(this)' value='删除'></td></tr>";
            row1.appendChild(td1);
			row1.appendChild(td2);
			row1.appendChild(td3);
            document.all("filenum").value = Number(filenmu)+1;
		}
		function deleteRow(e){
			var tbody1=e.parentElement.parentElement.parentElement;
		    tbody1.removeChild(e.parentElement.parentElement);
		    document.all("filenum").value = Number(document.all("filenum").value)-1;
		}
        function subform(){
        
            var filenmu = document.all("filenum").value;
            var type = "<%=type%>";
            
            var size = 0;
            for(var i=0;i<=filenmu;i++)
            {
            	//var obj = document.all("file"+i).value;
                var obj = document.all("file"+i);
                
                if(obj != null && obj != "")
                {
                	if(obj.value =="")
                	{
                      alert("请选择上传文件！");
                      return false;
                    }
                    
                    if(type=="4")
               		{
	                    var reg =/.+\.([Zz][Ii][Pp])\b/;
	                    if(!reg.test(obj.value))
	                    {
	                        alert("提示：\n请选择一个有效的文件，\n支持的格式有（zip）！");
	                        return false;
	                    }
	               }
	               size ++ ; 
                }
                  
            }
            
            if(size > 0)
            {
	            var tmp = "<%=docpath%>";
	            uploadform.action = "tag_attach_upload.jsp?docpath="+tmp;
	            uploadform.submit();
	            document.all.divProcessing.style.display = "";
	        }
        }
		addRow();
</SCRIPT>	

</html>