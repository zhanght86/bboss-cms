<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>

<head>  
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  
  <script language="javascript">
    function disableButton(){        
        while(!getPropertiesToolbar().tools1){
            ;
        }
        getPropertiesToolbar().tools1.disabled = true;            
        getPropertiesToolbar().tools2.disabled = true;
        getPropertiesToolbar().tools3.disabled = true;           
    }
    //window.onload=disableButton;
    
  </script>
</head>
<body class="contentbodymargin">
<div id="contentborder">

<table>
  <tr>
    <td>
      请选择一个机构
    </td>
  </tr>
</table>

</div>
<%@ include file="../sysMsg.jsp"%>
</body>
</html>
  
     
 
 


