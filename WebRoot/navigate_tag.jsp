<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/navigator.tld" prefix="navigator"%>
<!--
     功能说明：导航标签
     @param String level:导航层次的根 第几层开始 缺省第一层 必须配置参数
     @param String css_path:外部引用CSS样式路径 缺省根据Layout自动加载相应样式
     @param String levelDegree:导航深度 缺省根层
     @param String column_num:普通导航布局的一行显示列数 缺省十列
     @param String style:导航的形式(text/image) 缺省text导航
     @param String layout:导航布局(normal/left/top-down/top-left) 缺省普通导航布局
 --> 

<html>
    <body>
        <navigator:navigator   style="text"  layout="top-left"  level="1" levelDegree="2" />
    
</html>

