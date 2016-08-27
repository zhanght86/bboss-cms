<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="components/slide/slide.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/common/scripts/log.js"></script>

<script type="text/javascript">

     $(document).ready(function() {      
        pageCounter('内网宣传平台','新闻');
   });
     
     function test(){
    	operateCount('内网宣传平台','新闻','测试操作');
    } 

     function test1(){
    	 operateCountDetail('内网宣传平台','新闻','测试操作','test001','/common/scripts/log.js');
    } 
</script>
</head>
<body>
<h1>BrowserTest</h1>
<form>
<button onclick="test()" id="" name="" >点击记录操作</button>
<button onclick="test1()" id="" name="" >点击记录操作</button>

</form>

</body>
</html>