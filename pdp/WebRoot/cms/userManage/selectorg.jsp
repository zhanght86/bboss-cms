<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<html>
<head>  
  <title>属性容器</title>
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
  
  <script language="javascript">
    function disableButton(){        
        while(!getPropertiesToolbar().tools1){
            ;
        }
        getPropertiesToolbar().tools1.disabled = true;            
        getPropertiesToolbar().tools2.disabled = true;
        getPropertiesToolbar().tools3.disabled = true;           
    }
  </script>

</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">

<table>
  <tr>
    <td>
      请选择一个机构
    </td>
  </tr>
</table>

</div>

</body>
</html>
  
     
 
 

