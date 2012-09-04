<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
	<pg:equal actual="${docCommentList.totalSize}" value="0" >
		<div style="text-align: center;padding-top:10px;">
			<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
		</pg:equal> 
	<pg:notequal actual="${docCommentList.totalSize}"  value="0">
   <pg:pager scope="request"  data="docCommentList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<pg:param name="docId"/>
	
	 <pg:list autosort="false">
	 	<li onclick="this.className==''?this.className='select_comment':this.className=''">
        	<div class="comment_user"><pg:cell colName="userName"/></div>
            <div class="comment"><pg:cell colName="docComment" htmlDecode="true"/>
             <div class="comment_date"><pg:cell colName="userIP"/>&nbsp;&nbsp;&nbsp;&nbsp;<pg:cell colName="subTime" dateformat="yyyy-MM-dd HH:mm:ss"/></div>
             <div class="operation"><a href="javascript:void(0)" onclick="document.getElementById('docComment').focus()">评论</a></div>
             </div>
          </li>
		</pg:list>
	 	<div>
			&nbsp;&nbsp;<input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="10" export="000000110"/>
		</div>
    </pg:pager>
    </pg:notequal>
</div>		
