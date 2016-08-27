<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><pg:siteinfo property="name"/></title>
<script type='text/javascript' src='<pg:sitedomain/>/js/jquery-1.4.2.min.js' language='JavaScript'></script>
<script type='text/javascript' src='<pg:sitedomain/>/js/pager.js' language='JavaScript'></script>
<link href="<pg:sitedomain/>/css/main.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
       $(document).ready(function() {
      	
         $("#sb").click(function() {
        	 queryData();
           });
           
       });
     //查找
   	function queryData()
   	{	var queryString=$("#condition").val();
   		var siteid=$("#siteId").val();
	
		var url = "<%=request.getContextPath()%>/searchmanage/processSearch.freepage  #customContent";
   		$("#allcomments").load(url,
   		{queryString:queryString,siteId: siteid},function(){});
   	

   	}
     </script>

<script type="text/javascript">
			
			
			function search()
			{
				
			}
			
</script>
<link href="<pg:sitedomain/>/css/foot2.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="wrapper">
  <div  class="top">
    <div class="logo_top"><img src="<pg:sitedomain/>/images/top_logo.jpg" width="303" height="31" /></div>    
  </div>
  <div class="positon2"><pg:position/>
  </div>
  <div class="container">
    
  
       
			<div class="comment_1">
	       
	          <table width="100%" border="0" cellspacing="3" cellpadding="0">
	            <tr>
	              <td width="12%"><div align="right">搜索条件：</div></td>
	              <td width="30%"><input id="condition" class="input1" type="text" name="condition" /><input type="hidden" name="siteId" id="siteId" value="<common:request parameter="siteId"/>"/></td>
	               <td align="left"><a class="bt_0" id="sb" href="javascript:void(0)"><span>搜索</span></a></td>
	            </tr>
	            
	           
	          </table>
	        </div>
	        <div id="allcomments">
		        
		    </div>  
    
    </div>
</div>
<div class="footer">
  <div class="isany"></div>
  <div class="quality"></div>
  <div class="copy_right"> <a href="http://www.sanygroup.com/group/zh-cn/" >三一集团门户</a>|<a href="http://www.sanygroup.com/group/zh-cn/media/product_download.htm">资料下载</a>|<a href="http://www.sanygroup.com/group/zh-cn/foot/item.htm">使用条款</a>|<a href="http://www.sanygroup.com/group/zh-cn/foot/contact_us.htm">联系我们</a>|<a href="http://www.sanygroup.com/group/zh-cn/foot/sitemap.htm">网站地图</a><br/>
    &nbsp;
    <script type="text/javascript">
		copyright=new Date();
		update=copyright.getFullYear();
		document.write("1989-"+ update + " 三一集团有限公司 版权所有 ");
	  </script>
</div></div>
</body>
</html>
