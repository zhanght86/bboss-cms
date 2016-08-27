<%@ page contentType="text/html; charset=UTF-8" %>
<HTML>
<HEAD>
<META http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<TITLE>正在处理中...</TITLE>

<STYLE type=text/css>TD {
 FONT-FAMILY: 宋体; FONT-SIZE: 12px; LETTER-SPACING: 2px; LINE-HEIGHT: 150%; font-color: #000000
}
FORM {
 FONT-FAMILY: 宋体; FONT-SIZE: 12px; LETTER-SPACING: 2px; LINE-HEIGHT: 150%; font-color: #000000
}
SELECT {
 FONT-FAMILY: 宋体; FONT-SIZE: 12px; LETTER-SPACING: 2px; LINE-HEIGHT: 150%; font-color: #000000
}
INPUT {
 FONT-FAMILY: 宋体; FONT-SIZE: 12px; LETTER-SPACING: 2px; LINE-HEIGHT: 150%; font-color: #000000
}
TEXTAREA {
 FONT-FAMILY: 宋体; FONT-SIZE: 12px; LETTER-SPACING: 2px; LINE-HEIGHT: 150%; font-color: #000000
}
BODY {
 FONT-FAMILY: 宋体; FONT-SIZE: 12px; LETTER-SPACING: 2px; LINE-HEIGHT: 150%; font-color: #000000
}
A:link {
 COLOR: #666666; FONT-SIZE: 10.5pt; TEXT-DECORATION: none
}
A:visited {
 COLOR: #666666; FONT-SIZE: 10.5pt; TEXT-DECORATION: none
}
A:hover {
 COLOR: #666666; FONT-SIZE: 10.5pt; TEXT-DECORATION: none
}
A:active {
 COLOR: #666666; FONT-SIZE: 10.5pt; TEXT-DECORATION: none
}
</STYLE>

<SCRIPT language=JavaScript> 
var url = ''; 
</SCRIPT>


<STYLE>.proccess {
 BACKGROUND: #ffffff; BORDER-BOTTOM: 1px solid; BORDER-LEFT: 1px solid; BORDER-RIGHT: 1px solid; BORDER-TOP: 1px solid; HEIGHT: 8px; MARGIN: 3px; WIDTH: 8px
}
</STYLE>

</HEAD>
<BODY >

<DIV align=center>
<TABLE align=center height="70%" valign="middle">
 <TBODY>
 <TR>
 <TD align=middle><p></p>
 
 <!-- Displaytext-->
 
 <P></P>
 <FONT class=fontbig> 正在处理中.....

 <!--End Displaytext-->
 <P></P>
 <P></P>
 <P></P>
 <P></P>
 <DIV align=center> 
 <FORM method=post name=proccess>
 <SCRIPT language=javascript>
for(i=0;i<30;i++)document.write("<input class=proccess>")
</SCRIPT>
 </FORM>
 </DIV>
 </font></TD>
 </TR></TBODY></TABLE>
<DIV align=center>
<SCRIPT language=JavaScript><!--
var p=0,j=0;
var c=new Array("lightskyblue","white")
setInterval('proccess();',100)
function proccess(){
 document.forms.proccess.elements[p].style.background=c[j];
 p+=1;
 if(p==30){p=0;j=1-j;}}
--></SCRIPT>
</DIV></DIV>
<DIV align=center>
<SCRIPT>
<!--
if (document.layers) 
 document.write('<Layer src="' + url + ' " VISIBILITY="hide"> </Layer>'); 
 else if (document.all || document.getElementById) 
 document.write('<iframe src="' + url + '" style="visibility: hidden;"></iframe>'); 
else location.href = url;
//-->
</SCRIPT>
</DIV>
<iframe name="doinghtml" height="0" width="0"></iframe>
</BODY>
</HTML>
