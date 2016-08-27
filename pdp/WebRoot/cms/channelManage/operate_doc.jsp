
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.Map"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		String userId = accesscontroler.getUserID();

		String[] docid = request.getParameterValues("ID");		
		String  operType = request.getParameter("operType"); 
		//获取操作类型,操作类型如下：
		//1、新增 2、呈送 3、发布 4、归档 5、回收
		
		DocumentManager docManager = new DocumentManagerImpl();
		Map tranIdMap;
		//将文档id的字符串数组转换为整形数组
		int[] intDocid = new int[docid.length];
		for(int i=0;i<intDocid.length;i++){
			intDocid[i] = Integer.parseInt(docid[i]);
		}
		if(docid != null){
			//处理呈送操作
			if(operType.equals("呈送")){
				//先判断是否所有选中的文档都能进行呈送，能执行呈送操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,2);
				if(tranIdMap.size()!=0){
					if(docManager.deliverDoc(intDocid)){
						docManager.recordDocOperate(intDocid,Integer.parseInt(userId),"呈送",tranIdMap);
						for(int i=0;i<intDocid.length;i++){
							%>
							<script language="javascript">
							parent.document.getElementById(<%=intDocid[i]%>).cells[4].innerText="待审";
							</script>
							<%
						}
					}
					else{
						%>
						<script language="javascript">alert("呈送文档时数据库更新失败!");</script>
						<%
					}
				}
				else {
					%>
					<script language="javascript">alert("呈送文档失败,请检查所选文档能否呈送!");</script>
					<%
				}
			}
			//处理发布操作
			else if(operType.equals("发布")){
				//先判断是否所有选中的文档都能进行发布，能则发布操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,5);
				if(tranIdMap.size()!=0){
					if(docManager.pulishDoc(intDocid)){
						docManager.recordDocOperate(intDocid,Integer.parseInt(userId),"发布",tranIdMap);
						for(int i=0;i<intDocid.length;i++){
							%>
							<script language="javascript">
							parent.document.getElementById(<%=intDocid[i]%>).cells[4].innerText="已发";
							</script>
							<%
						}
					}
					else{
						%>
						<script language="javascript">alert("发布文档时数据库更新失败!");</script>
						<%
					}
				}
				else{
					%>
					<script language="javascript">alert("发布文档失败,请检查所选文档能否发布!");</script>
					<%
				}	
			}
			//处理归档操作
			else if(operType.equals("归档")){
				//先判断是否所有选中的文档都能进行归档，能则归档操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,7);
				if(tranIdMap.size()!=0){
					if(docManager.pigeonholeDoc(intDocid)){
						docManager.recordDocOperate(intDocid,Integer.parseInt(userId),"归档",tranIdMap);
						for(int i=0;i<intDocid.length;i++){
							%>
							<script language="javascript">
							parent.document.getElementById(<%=intDocid[i]%>).cells[4].innerText="已归档";
							</script>
							<%
						}
					}
					else{
						%>
						<script language="javascript">alert("归档时数据库更新失败!");</script>
						<%
					}
				}
				else{
					%>
					<script language="javascript">alert("文档归档失败,请检查所选文档能否归档!");</script>
					<%
				}	
			}
			//处理回收操作
			else if(operType.equals("回收")){
				//先判断是否所有选中的文档都能进行回收，能则回收操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,8);
				if(tranIdMap.size()!=0){
					if(docManager.GarbageDoc(intDocid)){
						docManager.recordDocOperate(intDocid,Integer.parseInt(userId),"回收",tranIdMap);
						%>
						<script language="javascript">
						parent.document.location.reload();
						</script>
						<%
					}
					else{
						%>
						<script language="javascript">alert("回收文档时数据库更新失败!");</script>
						<%
					}
				}
				else{
					%>
					<script language="javascript">alert("文档回收失败,请检查所选文档能否回收!");</script>
					<%
				}	
			}
		}
%>


