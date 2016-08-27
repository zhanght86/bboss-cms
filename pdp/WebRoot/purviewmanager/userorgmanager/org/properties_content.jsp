<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<html>
<head>  
  <title>属性容器</title>
  
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
<body class="contentbodymargin" scroll="no">
<div id="">

<table>
  <tr>
    <td>
   	<pg:message code="sany.pdp.afterchooseoption"/>
    </td>
  </tr>
</table>

</div>
</body>
</html>
  
     
 
 


