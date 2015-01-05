<%@page import="com.sany.activiti.demo.pojo.MaterielTest"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.sany.activiti.demo.pojo.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%
Map vars = new HashMap();
TaskInfo s;
List datas = new ArrayList();
for(int i = 0; i < 2; i ++)
{
  s = new TaskInfo();
  s.setId("qqqq"+i);
   List mar = new ArrayList();
  s.setMaterielList(mar);
  for(int j = 0; j < 2; j ++)
  {
  	MaterielTest mt = new MaterielTest();
  	mt.setMateriel_name(i + "first martirel"+j);
  	mar.add(mt);
  }
  datas.add(s);
}
vars.put("first", datas);
datas = new ArrayList();
for(int i = 0; i < 2; i ++)
{
  s = new TaskInfo();
  s.setId("sec"+i);
  List mar = new ArrayList();
  s.setMaterielList(mar);
  for(int j = 0; j < 2; j ++)
  {
  	MaterielTest mt = new MaterielTest();
  	mt.setMateriel_name(i+"second martirel"+j);
  	mar.add(mt);
  }
  datas.add(s);
}
vars.put("second", datas);
request.setAttribute("vars", vars);
%>
<div>
<pg:map actual="${vars }">0
<p>
<p>key:<pg:mapkey/></p>
<p>
value:
<pg:list position="0">1
	id:<pg:cell colName="id"/>
	materielList:	<pg:list colName="materielList">0
			<p><pg:cell colName="materiel_name"/></p>
		</pg:list>
</pg:list><br>
<pg:list start="1">id:<pg:cell colName="id"/>
	materielList:	<pg:list colName="materielList">
			<p><pg:cell colName="materiel_name"/></p>
		</pg:list></pg:list>
</p>
</p>
</pg:map>
</div>