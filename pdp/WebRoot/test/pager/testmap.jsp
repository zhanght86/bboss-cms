<%@ page contentType="text/html; charset=UTF-8" language="java" import="test.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	TestBean bean = null;
	Map mapbeans = new HashMap();//定义一个map，值可能是TestBean类型也可能是另外一个map
	bean = new TestBean();
	bean.setId("uuid");
	bean.setName("多多");
	mapbeans.put(bean.getId(),bean);//添加一个类型为TestBean的元素
	
	bean = new TestBean();
	bean.setId("uuid1");
	bean.setName("多多1");
	mapbeans.put(bean.getId(),bean);//添加一个类型为TestBean的元素
	bean = new TestBean();
	bean.setId("uuid2");
	bean.setName("多多2");
	mapbeans.put(bean.getId(),bean);//添加一个类型为TestBean的元素
	request.setAttribute("mapbeans",mapbeans);
	
	Map<String,String> mapstrings = new HashMap<String,String>();
	mapstrings.put("id1","多多1");
	mapstrings.put("id2","多多2");
	mapstrings.put("id3","多多3");
	mapstrings.put("id4","多多4");
	mapbeans.put("inner", mapstrings);//添加一个类型为Map的元素
	request.setAttribute("mapstrings",mapstrings);
%>
<!-- 
	测试在list标签上直接执行数据库，获取列表信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
-->
<html>
<head>
<title>测试获取map信息实例</title>
</head>
<body>
	<table>
	    <h3>map<String,po>对象信息迭代功能,采用map标签输出map中的元素信息</h3>
		<pg:map requestKey="mapbeans">
			<pg:true typeof="<%=test.TestBean.class %>">
			<tr >
				<td>
					mapkey:<pg:mapkey/>
				</td> 
				<td>
					id:<pg:cell colName="id" />
				</td> 
				<td>
					name:<pg:cell colName="name" />
				</td> 
			</tr>
			</pg:true>
			<pg:true typeof="java.util.Map">
			<tr >
			    <td><table>
				<pg:map>
				    <tr>
					    <td>outer mapkey use expression：<pg:cell expression="{0.mapkey}" /></td> 
						<td>outer mapkey ：<pg:mapkey index="0"/> , inner mapkey:<pg:mapkey/></td> 
						<td>
							inner value:<pg:cell/>
						</td> 
					</tr>	
					
				</pg:map>
				</table></td>
			 </tr>	
			</pg:true>
		</pg:map>		
	</table>
	
	<table>
	    <h3>map<String,String>字符串信息迭代功能</h3>
		<pg:map requestKey="mapstrings">
		
			<tr >
				<td>
					mapkey:<pg:mapkey/>
				</td> 
				<td>
					value:<pg:cell/>
				</td> 
				
			</tr>
		</pg:map>
		
		
	</table>
	
	
</body>
</html>
