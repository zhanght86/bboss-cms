
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page import="java.math.*"%>
<%!
  /*取得投票选项颜色*/
  private String getColor(int i){    //变量i表示第几种颜色
    String[] Color = {
      "#ff0000",  //红色
      "#00ff00",  //绿色
      "#FEFFF0",  //白色
      "#ffff00",  //黄色
      "#ff00ff",  //粉红
      "#00ffff",  //蓝色
      "#f0f0f0",  //灰白
      "#0f0f0f",  //褐黑
      "#ff0ff0",  //玫瑰红
      "#0ff0ff",  //天蓝
      "#666666",  //灰褐
      "#999999",  //灰色
    };

    if (i<0)
      return Color[1];

    String mod1 = Integer.toString(i);
    String mod2 = Integer.toString(11);
    BigInteger big1 = new BigInteger(mod1);
    BigInteger big2 = new BigInteger(mod2);

    i = Integer.parseInt(big1.mod(big2).toString());
    return Color[i];
  }

  /*得到变量i除以变量total的百分比，结果带一位小数*/
  private String getPercent(double i,double total){
    if (total==0||i==0)
      return "0%";

    double percent = Double.parseDouble(Integer.toString((int)Math.round((i/total)*1000)))/Double.parseDouble("10");
    String strPercent = Double.toString(percent);
    strPercent = strPercent.substring(0,strPercent.length());
    strPercent = strPercent+"%";

    return strPercent;
  }
%>


<html>
	<head>
		<title>统计结果</title>
		<link href="style.css" rel="stylesheet" type="text/css">
		<script>
<!--
function returnListVote(){
  if( window.opener && window.opener.refreshSelf ){
        window.opener.refreshSelf();
        window.opener.focus();
  }
  window.close();
}
-->
</script>
	</head>

	<body onload="javascript:self.focus()" bgcolor="#FFFFFF" style="font-size:9pt;overflow-x: hidden;">
			<div align="center">
			<center>
				<table border="0" width="100%" bordercolor="#000000" cellpadding="3">
				<tr>
						<td  class="c1" width="120" color="#000000">
                </td>
				</tr>
					<%  
		
        int voteTotal = 0;
		VoteManager vote = new VoteManager();
		List QuestionItems=vote.getActiveQuestion();
		//int iRecCount=QuestionItems.size();
		for(int j=0;j<QuestionItems.size();j++)
		{
		int idd =((Question)QuestionItems.get(j)).getId();
	    voteTotal=((Question)QuestionItems.get(j)).getVotecount();//主题总票数
		List items = vote.getDiscriptionItems(idd);
				if(items.size()>0)
		  {
		  %>
					
				<tr>
					   <td class=c1 color=#000000 align="left" valign="top"><%=j+1%>.<%=((Item)items.get(0)).getTitle()%></td>				
				</tr>
		   <%}%>
		   <% for (int i = 0; i < items.size(); i++) {
		   int iVoteCount=((Item)items.get(i)).getVote();//选项票数
		
		%>
		       <tr>
		           <td>
		             <table border="0" width="99%" cellspacing="3" bordercolor="#FFFFFF" cellpadding="2">
						<tr>
		 					<td class=c1 style=FONT-FAMILY: MS Sans Serif; FONT-WEIGHT: bold vAlign=top width=31 color=#000000>
									<table border="1" cellspacing="0" cellpadding="0" bordercolorlight="#000000" bordercolordark="#000000" bgcolor="<%=getColor(j)%>" height="12" width="12">
										<tr>
										 <td>
											<!--选项显示色彩-->
										 </td>
										</tr>
									</table>
							</td>
							<td><%=((Item)items.get(i)).getOptions()%>
							<%=((Item)items.get(i)).getVote()%>票
							</td>
				    </tr>
	               </table>
                 </td>
				<td class=c1 width=40% bgcolor=#FFFFD9 valign=middle align="center">
				<div align=center>
					<table width="150" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFD9">
						<tr>
							<td width=80% style="BORDER-LEFT: black 1px solid">
								<table height=2 border=1 bordercolorlight="#000000" bordercolordark="#000000" bgcolor="<%=getColor(j)%>" width="<%=getPercent(iVoteCount,voteTotal)%>">
											<tr>
												<td>

												</td>
											</tr>
								</table>
							</td>
							<td width=20%>
							<%=getPercent(iVoteCount,voteTotal)%>
							</td>									

						</tr>
						</table>
						</div>
					</td>
			</tr>
	<%
   }
    }
  %>
      </center>
    </div>
    </table>
		<table bgcolor="#336699" border="0" cellPadding="0" cellSpacing="0" width="100%">
			<tbody>
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>


					<td class="c2" color="#000000"></td>
					<td align="right" class="c1" width="50%" color="#000000">
						<font color="#FFFFFF">│<a href="javascript:window.returnListVote();"><font color="#FFFFFF"> 关闭窗口</font></a><font color="#FFFFFF">│</font></font>
					</td>
				</tr>
			</tbody>
		</table>


	</body>

</html>


