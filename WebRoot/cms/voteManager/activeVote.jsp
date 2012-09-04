<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<html>
 <head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>设置当前投票主题</title>
</head> 
  
  <body>
  
  <form name="form1" method="post" action="activeaction.jsp" onSubmit="javaScript: return validate();">


<table width=500 border="1" cellpadding="0" align="center" cellspacing="0"   class="query_box">
	<tr>
					<td>
						<P align="center">
							<b>设置活动投票主题
						</P>
					</td>
				</tr>
	<%
		VoteManager vote = new VoteManager();
		
		List questions = vote.getQuestion();
		 int active;
		 String outputString="";
     for (int i = 0; i<questions.size(); i++){
        active=((Question)questions.get(i)).getActive();
        if(active==0)
         {
         outputString="<tr height=20><td><input type='checkbox' name='lang' value='"+((Question)questions.get(i)).getId()+"'>"+((Question)questions.get(i)).getTitle()+"</td></tr>";
         out.println(outputString); }
         else
         {
         outputString="<tr height=20><td><input type='checkbox' name='lang' value='"+((Question)questions.get(i)).getId()+"' checked='true' >"+((Question)questions.get(i)).getTitle()+"</td></tr>";
          out.println(outputString);
         }
         }
		%>
<tr height=20>
 <td bordercolor="#eeeee" bgcolor="#336699"><P align="center"><input type="submit" value="确定" name="submit"></P>
 </td>
 </tr>
  	</table>
  	<a href=votelist.jsp>>>返回</a>
</form>
</body>
</html>
<script language="JavaScript">

   function validate(){ 
     var len = document.form1.lang.length;
	 for (var i=0; i<len; i++){
		var e = document.form1.lang[i];
		if ( e.checked ){
			return true;
		}
	 }
	  alert("请您至少选择一项！");
       return false;
   }
</script>




