<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.awt.*" %>
<%@ page import="org.jfree.chart.*" %>
<%@ page import="org.jfree.chart.plot.*" %>
<%@ page import="org.jfree.chart.servlet.ServletUtilities" %>
<%@ page import="org.jfree.chart.entity.StandardEntityCollection" %>
<%@ page import="org.jfree.chart.renderer.category.BarRenderer3D" %>
<%@ page import="org.jfree.data.category.DefaultCategoryDataset" %>
<%@ page import="org.jfree.data.general.DefaultPieDataset" %>
<%@ page import="org.jfree.chart.renderer.category.BarRenderer3D" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil" %>
<%@ page import="com.frameworkset.common.poolman.PreparedDBUtil" %>

<html>
<head><title>Jfree图表</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<body>
<form name="form1" method="post" >
<div align="cente"><P><font size="4" face="楷体_UTF-8"><strong><font color="#0099ff">投票统计信息</font></strong></font></p>
</div>


<%

 //设置生成饼状图
    int i=0;
    String[] area={"南京市","镇江市","常州市","无锡市","苏州市","南通市"};
    try{
		String sql = "select dq from test1";
        DBUtil db = new DBUtil();
		db.executeSelect(sql);
       
        //按地区进行统计
        //PrintWriter pw=null;
        DefaultPieDataset dataset=new DefaultPieDataset();
        //设置数据源
        for(int j=0;j<6;j++){
           int dq=db.getInt(j,"dq");
           dataset.setValue(area[j],dq);
         
          //添加数据，参数包括名称和数值
        }
       
        JFreeChart chart=ChartFactory.createPieChart("饼型图",dataset,true,true,true);
        chart.setTitle("投票统计图");//设置图片标题
        ChartRenderingInfo info=new ChartRenderingInfo(new StandardEntityCollection());
        String filename=ServletUtilities.saveChartAsJPEG(chart,300,200,info,session);
        //设置生成图片，包括图片的大小，长度是300，宽是200
        //out.println(filename);
        session.setAttribute("file_name1",filename);
        String URL= request.getContextPath() + "/servlet/DisplayChart?filename=" + filename; 
        session.setAttribute("file_url1",URL);

        }
        catch(SQLException e){
          out.println(e);
       }
        

      %>
<%


 //生成柱状图
        try{

         String sql1 = "select dq from test1";
		 DBUtil db = new DBUtil();
		 db.executeSelect(sql1);

         DefaultCategoryDataset dataset=new DefaultCategoryDataset();//设置数据源

         for(int k=0;k<6;k++){
         dataset.addValue(db.getInt(k,"dq"),"学校",area[k]);
         //添加数据，参数包括名称和数值
         }
 
         JFreeChart chart1=ChartFactory.createBarChart3D("统计图","招生地区","招生人数",dataset,PlotOrientation.VERTICAL,false,false,false);

         CategoryPlot plot=chart1.getCategoryPlot();
       
         BarRenderer3D renderer1=new BarRenderer3D();
       
         renderer1.setItemLabelsVisible(true);//显示每个柱子上的数据
        
         renderer1.setItemMargin(0.1);//设置每个柱子之间的距离
         plot.setRenderer(renderer1);//让上面对柱子的设置生效
         String filename=ServletUtilities.saveChartAsJPEG(chart1,300,200,session);
         //生成图片，包括图片的大小

         session.setAttribute("file_name2",filename);
         String URL=request.getContextPath()+"/servlet/DisplayChart?filename="+filename;

         session.setAttribute("file_url2",URL);




         }catch(SQLException e){}


%>
<center><img src="<%=session.getAttribute("file_url1")%>" width=300 height=200 border=0 usemap="#<%=session.getAttribute("file_name1") %>">

<img src="<%=session.getAttribute("file_url2") %>" width=300 height=200 border=0 usemap="#<%=session.getAttribute("file_name2")%>"></center>
<input type="Button" value="确定" onclick="window.open('seeresult_bz.jsp?questionid=193,194,196','mywindow','toolbar=no,left=150,top=200,width=400,scrollbars=yes,height=400,menubar=no,systemMenu=no')">
</body>
</form> 
</html>


