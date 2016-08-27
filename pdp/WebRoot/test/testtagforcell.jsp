<%@page import="com.sany.activiti.demo.pojo.MaterielTest"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.sany.activiti.demo.pojo.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%

%>

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
 <p> <pg:cell colName="rommType"/></p>
  <p><pg:cell colName="rommTypeID"/></p>
  <p><pg:cell colName="overNum"/></p>
  <p><pg:list requestKey="days">
  		<p><pg:cell/><pg:cell index="0" currentcelltoColName="true"/></p>
  </pg:list></p>
</pg:list>
</div>
</div>

<div>
<p>usecurrentCellValuetoCellName属性演示</p>
<div>
<pg:list requestKey="rooms">
  <p><pg:cell colName="rommType"/></p>
  <p><pg:cell colName="rommTypeID"/></p>
  <p><pg:cell colName="overNum"/></p>
  <p><pg:list requestKey="roomDays">
  		<p><pg:cell colName="day"/><pg:cell index="0" usecurrentCellValuetoCellName="day"/></p>
  </pg:list></p>
</pg:list>
</div>
</div>