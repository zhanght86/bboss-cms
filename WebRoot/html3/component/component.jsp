<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/html3/component/stylesheet/demopage.css"/>


<!-- <script type="text/javascript" src="../datepicker/My97DatePicker/WdatePicker.js"></script> 
<script type="text/javascript" src="../js/lhgdialog.min.js"></script>
-->
<script>
function pop()
{
  
	$.dialog({  
    id:'iframeId',  
	width: '700px',
	height: '400px',
    min: false,
    max:false,
	title:'历史访问信息',
    lock: true, 
    content: 'url:${pageContext.request.contextPath}/html3/rms/dialog/add_cost.html',
	okVal: '确定为来访人员',
	/*parent:this,*/
    ok: function () { 
        
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '添加成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    }, 
	cancelVal: '关闭', 
    cancel: true  
	});
	
}





</script>


<div class="main_content">
<div class="title_h1">按钮</div>

<div class="title_h2"></div>
<a class="bt_1"><span>按钮</span></a>
<a class="bt_4"><span>按钮</span></a>
<a class="bt_5"><span>按钮</span></a>
<a class="bt_approval"><span>按钮</span></a>

<div class="title_h2"></div>
<a class="bt_next"><span>按钮</span></a>
<a class="bt_add"><span>按钮</span></a>
<a class="bt_sany2"><span>按钮</span></a>
<a class="bt_submit"><span>按钮</span></a>
<a class="bt_approval3"><span>按钮</span></a>
<a class="bt_save"><span>按钮</span></a>
<a class="bt_7"><span>按钮</span></a>
<a class="bt_8"><span>按钮</span></a>
<a class="bt_route">按钮</a>
<a class="bt_cancel"><span>按钮</span></a>

<div class="title_h2"></div>
<a class="bt_sany">按钮</a>
<a class="bt_3"><span>按钮</span></a>
<a class="bt_2"><span>按钮</span></a>





<div class="title_h1">选项卡</div>
<!--tab1-->
<div class="tabs mt_30" id="menu1">
     <ul>
       <li><a href="#" class="current" onclick="setTabC(1,0)">tab1</a></li>                                                         
       <li><a href="#" onclick="setTabC(1,1)">tab2</a></li>
     </ul>
</div>
<div id="main1" class="clearfix">
  <ul></ul>
  <ul style="display:none"></ul>
</div>

<!--tab2-->
  <div class="u_tab_div mt_40"  id="tab01"> 
      <span class="u_tab_hover"><a href="#">tab1</a></span> 
      <span class="u_tab"><a href="#">tab2</a></span>
      <span class="u_tab"><a href="#">tab3<b>(2)</b></a></span> 
  </div>
   <div class="explain_1"><a href="#">填写说明</a></div>
  <div id="tabCon01">
    <ul class="ul_news">
      
    </ul>
    <ul class="ul_news" style="display:none">
     
    </ul>
    <ul class="ul_news" style="display:none">
         
    </ul>
  </div>
  <script type="text/javascript">tabAction("tab01","tabCon01");</script>
  






<!--文本框-->
<div class="title_h1">文本框</div>

<div class="title_h2"></div>
  
    <div class="c_a_form clearfix">
       <div class="form_li3">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /><a class="bt_3 ml_5"><span>选择</span></a></span>
       </div>
        <div class="form_li3">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" class="w261"/></span>
       </div>
        <div class="form_li3">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245"  class="w386"/></span>
       </div>
        <div class="form_li3">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245"  class="w70p"/></span>
       </div>
    </div>

<div class="title_h2"></div>
  
    <div class="c_a_form clearfix w1000">
       <div class="form_li1">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
        <div class="form_li2 clearfix">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
       <div class="form_li1">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
        <div class="form_li2 clearfix">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
      
    </div>
<div class="title_h2"></div>
    
    <div class="c_a_form clearfix w1000">
       <div class="form_li">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
        <div class="form_li">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
       <div class="form_li">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
       <div class="form_li">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
        <div class="form_li">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
       <div class="form_li">
          <span class="form_title"><span class="c3 md_6">*</span>标题：</span>
          <span class="form_content"><input type="text" value="10005245" /></span>
       </div>
    </div>

<div class="title_h1">时间控件</div>
    <div class="c_a_form clearfix w1000">
       <div class="form_li3">
          <span class="form_title"><span class="c3 md_6">*</span>日期选取：</span>
          <span class="form_content"><input type="text"  class="Wdate"  onClick="WdatePicker()" /></span>
       </div>
        <div class="form_li3">
          <span class="form_title"><span class="c3 md_6">*</span>日期选取：</span>
          <span class="form_content"><input type="text"  id="d4311" class="Wdate"   onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})" /> 到 <input type="text"  id="d4312" class="Wdate"   onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/></span>
       </div>   
    </div> 
    
    
<div class="title_h1">弹出窗口</div> 
<a class="bt_3 mt_10" id="pop" onclick="pop()"><span>弹出窗口</span></a>





