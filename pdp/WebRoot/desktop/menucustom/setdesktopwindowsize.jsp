<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%@ include file="/common/jsp/commonCssScript.jsp"%>
	<%--
	描述：桌面窗口大小设置
	作者：qian.wang
	版本：1.0
	日期：2011-09-20
	 --%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>窗口大小设置</title>
    	<%@ include file="/common/jsp/commonCssScript.jsp"%>
		<%@ include file="/common/jsp/jqureyEasyui.jsp"%>
		<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>
  
  <script type="text/javascript">
  
  
    function blockUI(msgContent){
	if (!msgContent){
		msgContent = '正在处理，请稍候...';
	}
	$("<div class=\"datagrid-mask\"></div>").css({display:"block", width:"100%",height:$(window).height()}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\"></div>").html(msgContent).appendTo("body").css({display:"block",left: ($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
	}
	
	
	function unblockUI(){
		$(".datagrid-mask-msg").remove();
		$(".datagrid-mask").remove();
	}
    function save(){
      
      if($("#menupath").val() == null||$("#menupath").val() == ""){
        $.messager.alert("提示对话框" , "保存失败，请选择一个快捷选项！");
     	 return;
      }
      var width = $("#width").val();
      var height = $("#height").val();
      if(width == null||width == ""||height == null||height == "")
      {
     	 $.messager.alert("提示对话框" , "宽度或高度不能为空!");
     	 return;
      }else if(width< 1||height < 1){
         $.messager.alert("提示对话框" , "宽度或高度不能设置小于0!");
         return ;
      }
      var reg = "^[0-9]*[1-9][0-9]*$";
      var re = new RegExp(reg);
      if(!re.test(width)){
           $.messager.alert("提示对话框" , "宽度或高度只能为正整数!");
	      return;
      }
    
    $("#menusize").form('submit', {
				    "url": "../desktop/savemenusize.page",
				    onSubmit:function(){			
						//显示遮罩							
						blockUI();	
				    },
				    success:function(responseText){	
				    	//去掉遮罩	
						unblockUI();
						if(responseText == "ok")
						$.messager.alert("提示对话框" , "保存成功!");
						else 
						$.messager.alert("提示对话框" , "保存失败!");	
				    }
				});	
    
    }
  
    function reset(){
	    if($("#menupath").val() == null||$("#menupath").val() == ""){
	        $.messager.alert("提示对话框" , "重置失败，请选择一个快捷选项！");
	     	 return;
	      }
       $("#menusize").form('submit', {
				    "url": "../desktop/reset.page",
				    onSubmit:function(){			
						//显示遮罩							
						blockUI();	
				    },
				    success:function(responseText){	
				    	//去掉遮罩	
						unblockUI();
						if(responseText == "ok"){
						$.messager.alert("提示对话框" , "重置成功!");
						$("#width").val("");
      					$("#height").val("");
						}else 
						$.messager.alert("提示对话框" , "重置失败!");	
				    	}
				});	
    		}
  
  
  </script>
    </head>
  <body>
    当前位置>><font color="green">${operaName}</font>
    <table>
    <form action="" id="menusize" name="menusize">
    <pg:beaninfo requestKey="menuBean">
     <tr align="center"><td>
    窗口宽度：<input name="width" id="width" value="<pg:cell colName="width"  />"/><br />
    </td></tr>
    <tr align="center"><td>
    窗口高度：<input name="height" id="height" value="<pg:cell colName="height"  />"/><br/>
    </td></tr>
    <input type="hidden" name="userid" value="<pg:cell colName="userid" />"/>
    <input type="hidden" name="menupath" id="menupath" value="<pg:cell colName="menupath" />"/>
    <input type="hidden" name="subsystem" value="<pg:cell colName="subsystem" />"/>
    </pg:beaninfo>
    </form>
    <tr align="center"><td>
    <button onclick="save()" class="button">保存</button>
    <button onclick="reset()" class="button">重置</button>
    </td></tr>
    </table>
  </body>
</html>

