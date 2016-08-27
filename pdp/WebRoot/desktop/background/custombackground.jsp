<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%@ include file="/common/jsp/commonCssScript.jsp"%>
	<%--
	描述：桌面背景设置
	作者：qian.wang
	版本：1.0
	日期：2011-09-20
	 --%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title></title>
		<%@ include file="/common/jsp/jqureyEasyui.jsp"%>
		<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>
  </head>
  
  <script type="text/javascript">
		  $(document).ready(function(){
		  queryList();
		});
    var ImgObj=new Image();                                   //建立一个图像对象
	var AllImgExt=".jpg|.jpeg|.gif|.bmp|.png|";        //全部图片格式类型
	var FileObj,ImgFileSize,ImgWidth,ImgHeight,FileExt,ErrMsg,FileMsg,HasCheked,IsImg;
	/*以下为限制变量*/
	var AllowExt=".jpg|.jpeg|.gif|.bmp|.png|";                   //允许上传的文件类型,0为无限制
	//var AllowImgFileSize=2048;                                //允许上传图片文件的大小,0为无限制,单位:KB 
	////var AllowImgWidth=500;                                  //允许上传的图片的宽度,0为无限制,单位:px(像素)
	//var AllowImgHeight=500;                                //允许上传的图片的高度,0为无限制,单位:px(像素)
	HasChecked=false;
	
	ImgObj.onerror=function(){ErrMsg='\n图片格式不正确或者图片已损坏!'}
	function CheckExt(obj){
	     ErrMsg="";
	     FileMsg="";
	     FileObj = "";
	     FileObj=obj;
	     IsImg=false;
	     HasChecked=false;
	     if(obj.value==""){return false;}
	     FileExt=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();
	     if(AllowExt!=0&&AllowExt.indexOf(FileExt+"|")==-1){                 //判断文件类型是否允许上传
	         ErrMsg="\n该文件类型不允许上传!请上传"+AllowExt+"类型的文件,当前文件类型为"+FileExt;
	         $.messager.alert("提示对话框" , ErrMsg);
	         return false;
	     }else
	      return true;
	     
	}
  function addBackGround(obj){
     
     if($("#cn_name").val() == ""){
       $.messager.alert("提示对话框" , "描述不能为空!");
       return;	
     }else if($("#file").val() == ""){
       $.messager.alert("提示对话框" , "请添加附件!");
       return;	
     }
     var flag = CheckExt(document.getElementById("file"));
     if(!flag){
      return;
     }
     $("#addbackground").form('submit', {
				    "url": "uploadBackGround.page",
				    onSubmit:function(){			
						//显示遮罩							
						blockUI();	
				    },
				    success:function(responseText){	
				    	//去掉遮罩	
						unblockUI();
						if(responseText == "ok"){
							$.messager.alert("提示对话框" , "增加背景图片成功!");
							queryList();
						}
						else if(responseText == "typeError"){
						 $.messager.alert("提示对话框" , "桌面背景只能是.jpg或.png或.gif格式!");
						}else if(responseText == "exit"){
						 $.messager.alert("提示对话框" , "该桌面背景已经存在!");
						}
						else
						$.messager.alert("提示对话框" , "增加背景图片失败:"+responseText);	
				    }
				});	
  	}
  
    function deleteCustom(filename){
     $.messager.confirm('提示对话框', "确定删除该背景图片记录？",function(r){
         if(r){
           $.post("deleteDesktopStyleCustom.page",{filename: filename},function(data){
           if(data == "ok"){
	           $.messager.alert("提示对话框" , "删除成功！");
	           queryList();
           }
           else
           	$.messager.alert("提示对话框" , "删除失败："+data);
       });
    
         }
       });
    }
  
  function queryList(){					
				$("#custombackContainer").load("getAllListBackGrounds.page #customContent");
			}		
  
  function saveCustom(filename,rowId){
           if($("#"+rowId).val() == ""){
             $.messager.alert("提示对话框" , "名称不能为空!");
             return;
           }
           $.post("updateDesktopStyleCustom.page",{filename: filename,cn_name:$("#"+rowId).val()},function(data){
             if(data == "ok"){
	           $.messager.alert("提示对话框" , "修改成功！");
	           //queryList();
           }
           else
           	$.messager.alert("提示对话框" , "修改失败："+data);
       });
  
  }  
  
  function saveListBackGrounds(){
     var flag = true;
     $(".cn_name").each(function(i){
         if(this.value == ""){
          flag = false;
         }
     }); 
     if(!flag){
          $.messager.alert("提示对话框" , "列表中自定义名称不能为空!");
          return;
     }
     
     
     $("#listForm").form('submit', {
				    "url": "saveBackGrounds.page",
				    onSubmit:function(){			
						//显示遮罩							
						blockUI();	
				    },
				    success:function(responseText){	
				    	//去掉遮罩	
						unblockUI();
						if(responseText == "ok"){
							$.messager.alert("提示对话框" , "批量修改成功!");
							queryList();
						}
						else
						$.messager.alert("提示对话框" , "批量修改失败:"+responseText);	
				    }
				});	
  
  }
  
  </script>
  
  
  <body>
    
    <div id="custombackContainer"></div>
    <form action=""  method="POST" id="addbackground" enctype="multipart/form-data">
    <table>
    <tr>
       <td>
    	名称：<input type="text" name="cn_name" id="cn_name"/>
       </td>
    </tr>
     <tr>
     	<td>
   		 附件：<input type="file" name="file" id="file" />
     	</td>
   	 </tr>
    <tr>
    	<td>
    <input type="button" class="button" value="添加背景图片" onclick="addBackGround(this)" />
	 	</td>
 	</tr>									
	</table>									
	</form>									
  </body>
</html>

