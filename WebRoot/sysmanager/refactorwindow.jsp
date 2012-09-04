<%
	/******************************************
	*	系统管理中间跳转页面，实现系统主界面的最大化， *      
	*******************************************/
	String subsystem =  request.getParameter("subsystem_id");
    if(subsystem == null) subsystem = "module";
	String index = request.getContextPath() + "/index.jsp?subsystem_id=" + subsystem;
%>
<script language='javascript'>

 function logon(){
  window.open("<%=index%>",'_blank',"channelmode=yes,width=" + (screen.availwidth-10) + ",height=" + (screen.availheight-50) + ",left=0,top=0,scrollbars=yes,resizable=yes,status=yes");
  var ua = window.navigator.userAgent.toLowerCase();
  var i,ver=0;
  if ((i = ua.indexOf('msie')) != -1){
    ver  = parseFloat('0' + ua.substr(i+5), 10);
    if(ver >= 5.5){
      wb.ExecWB(45,1);
    }
  }
 }
</script>

<body onload="logon();">
<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=0></object>
</body>
