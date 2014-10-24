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
<%
List rooms = new ArrayList();
for (int i =0 ; i < 10; i ++) {
Map map=new HashMap();
map.put("rommType", "麻将室"+i);
map.put("rommTypeID", "mj"+i);
map.put("overNum", (100+i)+"");
for (int j = 0; j < 10 ; j ++) {//设置动态属性

	map.put("day "+j+"剩余房间数:", (10+j)+"");//map增加时间段内的数据
}
rooms.add(map);
}
List<String> days=new ArrayList<String>();
for (int j = 0; j < 10 ; j ++) {
	days.add("day "+j+"剩余房间数:");
}
request.setAttribute("rooms", rooms);
request.setAttribute("days", days);

List<RoomDay> roomDays=new ArrayList<RoomDay>();
for (int j = 0; j < 10 ; j ++) {
	RoomDay roomDay = new RoomDay();
	roomDay.setDay("day "+j+"剩余房间数:");
	roomDays.add(roomDay);
}
request.setAttribute("rooms", rooms);
request.setAttribute("days", days);
request.setAttribute("roomDays", roomDays);
%>
<div>
<p>currentcelltoColName属性演示</p>
<div>
<pg:list requestKey="rooms">
  <pg:cell colName="rommType"/>
  <pg:cell colName="rommTypeID"/>
  <pg:cell colName="overNum"/>
  <pg:list requestKey="days">
  		<pg:cell/><pg:cell index="0" currentcelltoColName="true"/>
  </pg:list>
</pg:list>
</div>
</div>

<div>
<p>usecurrentCellValuetoCellName属性演示</p>
<div>
<pg:list requestKey="rooms">
  <pg:cell colName="rommType"/>
  <pg:cell colName="rommTypeID"/>
  <pg:cell colName="overNum"/>
  <pg:list requestKey="roomDays">
  		<pg:cell colName="day"/><pg:cell index="0" usecurrentCellValuetoCellName="day"/>
  </pg:list>
</pg:list>
</div>
</div>