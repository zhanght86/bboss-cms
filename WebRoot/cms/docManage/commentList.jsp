<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<div class="comment_title"><div class="comment_num"><a href="javascript:void">已有<span class="red_num" >${total }</span>条评论</a></div>
          评论</div>
	<pg:equal actual="${docCommentList.totalSize}" value="0" >
		<div style="text-align: center;padding-top:10px;">
			<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
		</pg:equal> 
	<pg:notequal actual="${docCommentList.totalSize}"  value="0">
   <pg:pager scope="request"  data="docCommentList" desc="true" isList="false" containerid="allcomments" selector="customContent">
	
	<pg:param name="docId"/>
	
	<ul class="comment_content">
	 <pg:list autosort="false">
	 	<li onclick="this.className==''?this.className='select_comment':this.className=''">
        	<div class="comment_user">
        	<pg:equal colName="userName" value="__quest">
        		匿名用户
        	</pg:equal>
        	<pg:notequal colName="userName" value="__quest">
        		<pg:cell colName="userName"/>
        	</pg:notequal>
        	<br/>
        	<pg:cell colName="subTime" dateformat="yyyy-MM-dd HH:mm"/>
        	</div>
            <div class="comment"><pg:cell colName="docComment" htmlDecode="true"/>
             
             <div class="operation"><a href="javascript:void(0)" onclick="document.getElementById('docComment').focus()">评论</a></div>
             </div>
          </li>
		</pg:list>
	</ul>
	 <div class="page" style="margin-left:0px;">
         <div align="center"><pg:index tagnumber="10" export="000000110"/> </div>
      </div>
	 	
    </pg:pager>
    </pg:notequal>
</div>		
