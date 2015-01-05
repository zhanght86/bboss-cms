<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>节假日管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<link href="${pageContext.request.contextPath}/sysmanager/holiday/stylesheet/date.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
    var areaId = '${param.areaId}';
    var setType = "";
    var setYear = "";
    var id_daySet = "";//当前操作的值
     $(document).ready(function() {
    	 initArrangeTable("");
    	 initYear();
    	 initColor();
     }); 
     function queryList(){	
    	 $("#custombackContainer").load("queryAreaList.page #customContent", {},function(){loadjs();});
	 }
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	 
	 
	 function initArrangeTable(year){
		 if(null == year || "" == year){
			 var myDate = new Date();
				 year = myDate.getYear();  
		 }
		 setYear = year;
		 $.ajax({
		 	 	type: "POST",
				url : "initArrangeTable.page",
				data :{year:year},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data) {
						clearTable();
						var array = data;
						for(var i = 0;i<array.length;i++){
							var id = "#"+array[i].id;
							var value = array[i].dayOfMonth;
							$(id).html(value);
							 $(id).click(function(){
								 getUpdateInfo(this);
								}); 

						}
					} 
					intArrange(year,areaId);
				}	
			 }); 
	 }
	 
	 function clearTable(){
		 for(var i = 1 ; i<= 12;i++){
			 for(var j = 1 ; j<=6;j++){
				 for(var k = 1 ; k <= 7 ; k ++){
					 var id = "#"+i.toString()+j.toString()+k.toString();
					 $(id).html("&nbsp;");
					 $(id).attr("bgcolor","");
				 }
			 }
		 }
	 }
	 
	 function initYear(){
		 var myDate = new Date();
		var year = myDate.getYear();  
		var yearArray = [year-5,year-4,year-3,year-2,year-1,year,year+1,year+2,year+3,year+4,year+5];
		 for(var i=0;i<yearArray.length;i++){
			document.getElementById("year").options.add(new Option(yearArray[i], yearArray[i]));
			if(yearArray[i] == year){
				document.getElementById("year").options[i].selected = true;
			}
		} 
	 }
	 
	 function intArrange(year,areaId){		 
		 $.ajax({
		 	 	type: "POST",
				url : "initArrangeDays.page",
				data :{year:year,areaId:areaId},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data) {
						var array = data;
						for(var i = 0;i<array.length;i++){
							var week = array[i].week;
							var type = array[i].type;
							changeBgColor(week,type);
							
						}
					} 
				}	
			 });
	 }
	 function updateSingleArrange(id,year,areaId,type,date){
		 $.ajax({
		 	 	type: "POST",
				url : "updateSingleArrange.page",
				data :{week:id,year:year,areaId:areaId,type:type,date:date},
				dataType : '',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){//如果成功改变颜色
					 if("success" == data){
						 changeBgColor(id,type);
					 }
				}	
			 });
	 }
	 
	 function getUpdateInfo(obj){
		 if(null == obj.innerHTML || "" == obj.innerHTML ||"&nbsp;" == obj.innerHTML){
			 return;
		 }
		 if(null != setType && "" != setType){
			 updateSingleArrange(obj.id,setYear,areaId,setType,obj.innerHTML);
		 }else{
			 $.dialog.alert("操作错误！请先选择设置类型");
		 }
		
		 
	 }
	 
	 function setDayType(type,id){
		  /*  取消设置,或权限设置       */
		 if(null == id_daySet || "" == id_daySet || id == id_daySet){
			 var cur_type=$("#"+id).attr("class") == "point"?"point_toggle":"point";
			 var cur_mouseType = cur_type == "point"?"default":"pointer";
			 $("#"+id).attr("class",cur_type);
			 if(cur_type == "point"){
				 id_daySet = "";
				 document.getElementById("holidayArrange").style.cursor="default";
				 setType = "";
				//界面可操作元素设置恢复
				  $('#setDefault').removeAttr("disabled");
				  $('#setDefault').removeClass("bt_2");
				  $("body").css("background","#fff");
				  $('#year').removeAttr("disabled"); 
			 }else{
				 setType = type;
				 id_daySet = id;
				 //document.getElementById("holidayArrange").style.cursor="pointer";
				 
				  if("1" == type){
					  document.getElementById("holidayArrange").style.cursor="url('<%=request.getContextPath()%>/sysmanager/holiday/images/workset.cur'),auto"; 
				  }else{
					  document.getElementById("holidayArrange").style.cursor="url('<%=request.getContextPath()%>/sysmanager/holiday/images/holidayset.cur'),auto"; 
				  }
				  //界面可操作元素设置失效
				  $('#setDefault').attr("disabled","disabled");
				  $('#setDefault').addClass("bt_2");
				  $("body").css("background","#ddd");
				  $("#holidayArrange").css("background","#fff");
				  $("#date_top").css("background","#fff");  
				  $('#year').attr("disabled","disabled");
			 }
		 }else{ /*  一种设置覆盖另一种   */
			 $("#"+id_daySet).attr("class","point");
			 $("#"+id).attr("class","point_toggle");
			 id_daySet = id;
			 setType = type;
			 if("1" == type){
				  document.getElementById("holidayArrange").style.cursor="url('<%=request.getContextPath()%>/sysmanager/holiday/images/workset.cur'),auto"; 
			  }else{
				  document.getElementById("holidayArrange").style.cursor="url('<%=request.getContextPath()%>/sysmanager/holiday/images/holidayset.cur'),auto"; 
			  }
		 }
	 }
	 function changeBgColor(id,type){
		 if("2" == type){
			 $("#"+id).attr("bgcolor","#129f00");
		 }else if("1" == type){
			 $("#"+id).attr("bgcolor","#00a2ff");
		 }
	 }
	 
	 function saveDefaultSetting(){
		 for(var m =  1;m<=12;m++){
			 for(var w =1;w<=6;w++){
				 for(var d = 1;d<=7;d++){
					 id = m.toString() +w.toString() +d.toString();
					 var date = $("#"+id).html();
					 if(null != date&&""!=date&&"&nbsp;"!=date){
						 var type = (1 == d||7 == d)? 2:1;
						 updateSingleArrange(id,setYear,areaId,type,date);
					 }
				 }
			 }
		 }
	 }
	 
	 
	 
	 function initColor(){
		 for(var m =  1;m<=12;m++){
			 for(var w =1;w<=6;w++){
				 for(var d = 1;d<=7;d++){
					 id = m.toString() +w.toString() +d.toString();
					 var date = $("#"+id).html();
					 var cellColor = $("#"+id).attr("bgcolor");
					 if(null != date&&""!=date&&"&nbsp;"!=date){//判断这一天是否有日期数字存在
						 //判断这一天是否有颜色设置
						 if("#129f00"!=cellColor && "#00a2ff"!=cellColor){
							 changeBgColor(id,(1 == d||7 == d)? 2:1);
						 }
					 }
				 }
			 }
		 }
	 }
</script>

</head>
	<body style="autoflow-y:scroll;">
		<div class="date_content mt_10"  id="date_top">			
		 <div class="check_year"><strong>请选择年份:&nbsp;&nbsp;</strong><select id="year" name="year"  maxlength="50" onchange="initArrangeTable(value)"></select>
		 <a href="javascript:void" id="setDefault" onclick="saveDefaultSetting()" class="bt_1" title="设置全年周一至周五为工作日，周六，周日双休"><span>保存默认配置</span></a>
		 </div>
         
          <div class="explain">
			<p>假期设置：</p>
			<div class="point" id="holiSet" onclick="setDayType('2','holiSet')"><img  src="${pageContext.request.contextPath}/sysmanager/holiday/images/holidayhand.gif" /></div>
			<p>工作日设置：</p>
			<div class="point" id="workSet"><img src="${pageContext.request.contextPath}/sysmanager/holiday/images/workhand.gif" onclick="setDayType('1','workSet');"/></div>
			<p>提示：点击图片后,再点击日期进行设置！</p>
	    </div>
	    </div>		
    <div class="date_content" id="holidayArrange">
    <ul>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">一月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "111">&nbsp;</td>
<td id = "112">&nbsp;</td>
<td id = "113">&nbsp;</td>
<td id = "114">&nbsp;</td>
<td id = "115">&nbsp;</td>
<td id = "116">&nbsp;</td>
<td id = "117" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "121">&nbsp;</td>
<td id = "122">&nbsp;</td>
<td id = "123">&nbsp;</td>
<td id = "124">&nbsp;</td>
<td id = "125">&nbsp;</td>
<td id = "126">&nbsp;</td>
<td id = "127" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "131">&nbsp;</td>
<td id = "132">&nbsp;</td>
<td id = "133">&nbsp;</td>
<td id = "134">&nbsp;</td>
<td id = "135">&nbsp;</td>
<td id = "136">&nbsp;</td>
<td id = "137" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "141">&nbsp;</td>
<td id = "142">&nbsp;</td>
<td id = "143">&nbsp;</td>
<td id = "144">&nbsp;</td>
<td id = "145">&nbsp;</td>
<td id = "146">&nbsp;</td>
<td id = "147" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "151">&nbsp;</td>
<td id = "152">&nbsp;</td>
<td id = "153">&nbsp;</td>
<td id = "154">&nbsp;</td>
<td id = "155">&nbsp;</td>
<td id = "156">&nbsp;</td>
<td id = "157" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "161">&nbsp;</td>
<td id = "162">&nbsp;</td>
<td id = "163">&nbsp;</td>
<td id = "164">&nbsp;</td>
<td id = "165">&nbsp;</td>
<td id = "166">&nbsp;</td>
<td id = "167" class="bordr">&nbsp;</td>
</tr>
                 </table>
                 
            
            
            </div>
        </li>
         <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">二月</caption>
                      <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "211">&nbsp;</td>
<td id = "212">&nbsp;</td>
<td id = "213">&nbsp;</td>
<td id = "214">&nbsp;</td>
<td id = "215">&nbsp;</td>
<td id = "216">&nbsp;</td>
<td id = "217" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "221">&nbsp;</td>
<td id = "222">&nbsp;</td>
<td id = "223">&nbsp;</td>
<td id = "224">&nbsp;</td>
<td id = "225">&nbsp;</td>
<td id = "226">&nbsp;</td>
<td id = "227" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "231">&nbsp;</td>
<td id = "232">&nbsp;</td>
<td id = "233">&nbsp;</td>
<td id = "234">&nbsp;</td>
<td id = "235">&nbsp;</td>
<td id = "236">&nbsp;</td>
<td id = "237" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "241">&nbsp;</td>
<td id = "242">&nbsp;</td>
<td id = "243">&nbsp;</td>
<td id = "244">&nbsp;</td>
<td id = "245">&nbsp;</td>
<td id = "246">&nbsp;</td>
<td id = "247" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "251">&nbsp;</td>
<td id = "252">&nbsp;</td>
<td id = "253">&nbsp;</td>
<td id = "254">&nbsp;</td>
<td id = "255">&nbsp;</td>
<td id = "256">&nbsp;</td>
<td id = "257" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "261">&nbsp;</td>
<td id = "262">&nbsp;</td>
<td id = "263">&nbsp;</td>
<td id = "264">&nbsp;</td>
<td id = "265">&nbsp;</td>
<td id = "266">&nbsp;</td>
<td id = "267" class="bordr">&nbsp;</td>
</tr>
                 </table>
                 
            
            
            </div>
        </li>
         <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">三月</caption>
                      <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "311">&nbsp;</td>
<td id = "312">&nbsp;</td>
<td id = "313">&nbsp;</td>
<td id = "314">&nbsp;</td>
<td id = "315">&nbsp;</td>
<td id = "316">&nbsp;</td>
<td id = "317" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "321">&nbsp;</td>
<td id = "322">&nbsp;</td>
<td id = "323">&nbsp;</td>
<td id = "324">&nbsp;</td>
<td id = "325">&nbsp;</td>
<td id = "326">&nbsp;</td>
<td id = "327" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "331">&nbsp;</td>
<td id = "332">&nbsp;</td>
<td id = "333">&nbsp;</td>
<td id = "334">&nbsp;</td>
<td id = "335">&nbsp;</td>
<td id = "336">&nbsp;</td>
<td id = "337" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "341">&nbsp;</td>
<td id = "342">&nbsp;</td>
<td id = "343">&nbsp;</td>
<td id = "344">&nbsp;</td>
<td id = "345">&nbsp;</td>
<td id = "346">&nbsp;</td>
<td id = "347" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "351">&nbsp;</td>
<td id = "352">&nbsp;</td>
<td id = "353">&nbsp;</td>
<td id = "354">&nbsp;</td>
<td id = "355">&nbsp;</td>
<td id = "356">&nbsp;</td>
<td id = "357" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "361">&nbsp;</td>
<td id = "362">&nbsp;</td>
<td id = "363">&nbsp;</td>
<td id = "364">&nbsp;</td>
<td id = "365">&nbsp;</td>
<td id = "366">&nbsp;</td>
<td id = "367" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
         <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">四月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "411">&nbsp;</td>
<td id = "412">&nbsp;</td>
<td id = "413">&nbsp;</td>
<td id = "414">&nbsp;</td>
<td id = "415">&nbsp;</td>
<td id = "416">&nbsp;</td>
<td id = "417" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "421">&nbsp;</td>
<td id = "422">&nbsp;</td>
<td id = "423">&nbsp;</td>
<td id = "424">&nbsp;</td>
<td id = "425">&nbsp;</td>
<td id = "426">&nbsp;</td>
<td id = "427" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "431">&nbsp;</td>
<td id = "432">&nbsp;</td>
<td id = "433">&nbsp;</td>
<td id = "434">&nbsp;</td>
<td id = "435">&nbsp;</td>
<td id = "436">&nbsp;</td>
<td id = "437" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "441">&nbsp;</td>
<td id = "442">&nbsp;</td>
<td id = "443">&nbsp;</td>
<td id = "444">&nbsp;</td>
<td id = "445">&nbsp;</td>
<td id = "446">&nbsp;</td>
<td id = "447" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "451">&nbsp;</td>
<td id = "452">&nbsp;</td>
<td id = "453">&nbsp;</td>
<td id = "454">&nbsp;</td>
<td id = "455">&nbsp;</td>
<td id = "456">&nbsp;</td>
<td id = "457" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "461">&nbsp;</td>
<td id = "462">&nbsp;</td>
<td id = "463">&nbsp;</td>
<td id = "464">&nbsp;</td>
<td id = "465">&nbsp;</td>
<td id = "466">&nbsp;</td>
<td id = "467" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
         <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">五月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "511">&nbsp;</td>
<td id = "512">&nbsp;</td>
<td id = "513">&nbsp;</td>
<td id = "514">&nbsp;</td>
<td id = "515">&nbsp;</td>
<td id = "516">&nbsp;</td>
<td id = "517" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "521">&nbsp;</td>
<td id = "522">&nbsp;</td>
<td id = "523">&nbsp;</td>
<td id = "524">&nbsp;</td>
<td id = "525">&nbsp;</td>
<td id = "526">&nbsp;</td>
<td id = "527" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "531">&nbsp;</td>
<td id = "532">&nbsp;</td>
<td id = "533">&nbsp;</td>
<td id = "534">&nbsp;</td>
<td id = "535">&nbsp;</td>
<td id = "536">&nbsp;</td>
<td id = "537" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "541">&nbsp;</td>
<td id = "542">&nbsp;</td>
<td id = "543">&nbsp;</td>
<td id = "544">&nbsp;</td>
<td id = "545">&nbsp;</td>
<td id = "546">&nbsp;</td>
<td id = "547" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "551">&nbsp;</td>
<td id = "552">&nbsp;</td>
<td id = "553">&nbsp;</td>
<td id = "554">&nbsp;</td>
<td id = "555">&nbsp;</td>
<td id = "556">&nbsp;</td>
<td id = "557" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "561">&nbsp;</td>
<td id = "562">&nbsp;</td>
<td id = "563">&nbsp;</td>
<td id = "564">&nbsp;</td>
<td id = "565">&nbsp;</td>
<td id = "566">&nbsp;</td>
<td id = "567" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
         <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">六月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "611">&nbsp;</td>
<td id = "612">&nbsp;</td>
<td id = "613">&nbsp;</td>
<td id = "614">&nbsp;</td>
<td id = "615">&nbsp;</td>
<td id = "616">&nbsp;</td>
<td id = "617" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "621">&nbsp;</td>
<td id = "622">&nbsp;</td>
<td id = "623">&nbsp;</td>
<td id = "624">&nbsp;</td>
<td id = "625">&nbsp;</td>
<td id = "626">&nbsp;</td>
<td id = "627" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "631">&nbsp;</td>
<td id = "632">&nbsp;</td>
<td id = "633">&nbsp;</td>
<td id = "634">&nbsp;</td>
<td id = "635">&nbsp;</td>
<td id = "636">&nbsp;</td>
<td id = "637" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "641">&nbsp;</td>
<td id = "642">&nbsp;</td>
<td id = "643">&nbsp;</td>
<td id = "644">&nbsp;</td>
<td id = "645">&nbsp;</td>
<td id = "646">&nbsp;</td>
<td id = "647" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "651">&nbsp;</td>
<td id = "652">&nbsp;</td>
<td id = "653">&nbsp;</td>
<td id = "654">&nbsp;</td>
<td id = "655">&nbsp;</td>
<td id = "656">&nbsp;</td>
<td id = "657" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "661">&nbsp;</td>
<td id = "662">&nbsp;</td>
<td id = "663">&nbsp;</td>
<td id = "664">&nbsp;</td>
<td id = "665">&nbsp;</td>
<td id = "666">&nbsp;</td>
<td id = "667" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">七月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "711">&nbsp;</td>
<td id = "712">&nbsp;</td>
<td id = "713">&nbsp;</td>
<td id = "714">&nbsp;</td>
<td id = "715">&nbsp;</td>
<td id = "716">&nbsp;</td>
<td id = "717" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "721">&nbsp;</td>
<td id = "722">&nbsp;</td>
<td id = "723">&nbsp;</td>
<td id = "724">&nbsp;</td>
<td id = "725">&nbsp;</td>
<td id = "726">&nbsp;</td>
<td id = "727" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "731">&nbsp;</td>
<td id = "732">&nbsp;</td>
<td id = "733">&nbsp;</td>
<td id = "734">&nbsp;</td>
<td id = "735">&nbsp;</td>
<td id = "736">&nbsp;</td>
<td id = "737" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "741">&nbsp;</td>
<td id = "742">&nbsp;</td>
<td id = "743">&nbsp;</td>
<td id = "744">&nbsp;</td>
<td id = "745">&nbsp;</td>
<td id = "746">&nbsp;</td>
<td id = "747" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "751">&nbsp;</td>
<td id = "752">&nbsp;</td>
<td id = "753">&nbsp;</td>
<td id = "754">&nbsp;</td>
<td id = "755">&nbsp;</td>
<td id = "756">&nbsp;</td>
<td id = "757" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "761">&nbsp;</td>
<td id = "762">&nbsp;</td>
<td id = "763">&nbsp;</td>
<td id = "764">&nbsp;</td>
<td id = "765">&nbsp;</td>
<td id = "766">&nbsp;</td>
<td id = "767" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">八月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "811">&nbsp;</td>
<td id = "812">&nbsp;</td>
<td id = "813">&nbsp;</td>
<td id = "814">&nbsp;</td>
<td id = "815">&nbsp;</td>
<td id = "816">&nbsp;</td>
<td id = "817" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "821">&nbsp;</td>
<td id = "822">&nbsp;</td>
<td id = "823">&nbsp;</td>
<td id = "824">&nbsp;</td>
<td id = "825">&nbsp;</td>
<td id = "826">&nbsp;</td>
<td id = "827" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "831">&nbsp;</td>
<td id = "832">&nbsp;</td>
<td id = "833">&nbsp;</td>
<td id = "834">&nbsp;</td>
<td id = "835">&nbsp;</td>
<td id = "836">&nbsp;</td>
<td id = "837" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "841">&nbsp;</td>
<td id = "842">&nbsp;</td>
<td id = "843">&nbsp;</td>
<td id = "844">&nbsp;</td>
<td id = "845">&nbsp;</td>
<td id = "846">&nbsp;</td>
<td id = "847" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "851">&nbsp;</td>
<td id = "852">&nbsp;</td>
<td id = "853">&nbsp;</td>
<td id = "854">&nbsp;</td>
<td id = "855">&nbsp;</td>
<td id = "856">&nbsp;</td>
<td id = "857" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "861">&nbsp;</td>
<td id = "862">&nbsp;</td>
<td id = "863">&nbsp;</td>
<td id = "864">&nbsp;</td>
<td id = "865">&nbsp;</td>
<td id = "866">&nbsp;</td>
<td id = "867" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">九月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "911">&nbsp;</td>
<td id = "912">&nbsp;</td>
<td id = "913">&nbsp;</td>
<td id = "914">&nbsp;</td>
<td id = "915">&nbsp;</td>
<td id = "916">&nbsp;</td>
<td id = "917" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "921">&nbsp;</td>
<td id = "922">&nbsp;</td>
<td id = "923">&nbsp;</td>
<td id = "924">&nbsp;</td>
<td id = "925">&nbsp;</td>
<td id = "926">&nbsp;</td>
<td id = "927" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "931">&nbsp;</td>
<td id = "932">&nbsp;</td>
<td id = "933">&nbsp;</td>
<td id = "934">&nbsp;</td>
<td id = "935">&nbsp;</td>
<td id = "936">&nbsp;</td>
<td id = "937" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "941">&nbsp;</td>
<td id = "942">&nbsp;</td>
<td id = "943">&nbsp;</td>
<td id = "944">&nbsp;</td>
<td id = "945">&nbsp;</td>
<td id = "946">&nbsp;</td>
<td id = "947" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "951">&nbsp;</td>
<td id = "952">&nbsp;</td>
<td id = "953">&nbsp;</td>
<td id = "954">&nbsp;</td>
<td id = "955">&nbsp;</td>
<td id = "956">&nbsp;</td>
<td id = "957" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "961">&nbsp;</td>
<td id = "962">&nbsp;</td>
<td id = "963">&nbsp;</td>
<td id = "964">&nbsp;</td>
<td id = "965">&nbsp;</td>
<td id = "966">&nbsp;</td>
<td id = "967" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">十月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "1011">&nbsp;</td>
<td id = "1012">&nbsp;</td>
<td id = "1013">&nbsp;</td>
<td id = "1014">&nbsp;</td>
<td id = "1015">&nbsp;</td>
<td id = "1016">&nbsp;</td>
<td id = "1017" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1021">&nbsp;</td>
<td id = "1022">&nbsp;</td>
<td id = "1023">&nbsp;</td>
<td id = "1024">&nbsp;</td>
<td id = "1025">&nbsp;</td>
<td id = "1026">&nbsp;</td>
<td id = "1027" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1031">&nbsp;</td>
<td id = "1032">&nbsp;</td>
<td id = "1033">&nbsp;</td>
<td id = "1034">&nbsp;</td>
<td id = "1035">&nbsp;</td>
<td id = "1036">&nbsp;</td>
<td id = "1037" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1041">&nbsp;</td>
<td id = "1042">&nbsp;</td>
<td id = "1043">&nbsp;</td>
<td id = "1044">&nbsp;</td>
<td id = "1045">&nbsp;</td>
<td id = "1046">&nbsp;</td>
<td id = "1047" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1051">&nbsp;</td>
<td id = "1052">&nbsp;</td>
<td id = "1053">&nbsp;</td>
<td id = "1054">&nbsp;</td>
<td id = "1055">&nbsp;</td>
<td id = "1056">&nbsp;</td>
<td id = "1057" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1061">&nbsp;</td>
<td id = "1062">&nbsp;</td>
<td id = "1063">&nbsp;</td>
<td id = "1064">&nbsp;</td>
<td id = "1065">&nbsp;</td>
<td id = "1066">&nbsp;</td>
<td id = "1067" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">十一月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "1111">&nbsp;</td>
<td id = "1112">&nbsp;</td>
<td id = "1113">&nbsp;</td>
<td id = "1114">&nbsp;</td>
<td id = "1115">&nbsp;</td>
<td id = "1116">&nbsp;</td>
<td id = "1117" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1121">&nbsp;</td>
<td id = "1122">&nbsp;</td>
<td id = "1123">&nbsp;</td>
<td id = "1124">&nbsp;</td>
<td id = "1125">&nbsp;</td>
<td id = "1126">&nbsp;</td>
<td id = "1127" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1131">&nbsp;</td>
<td id = "1132">&nbsp;</td>
<td id = "1133">&nbsp;</td>
<td id = "1134">&nbsp;</td>
<td id = "1135">&nbsp;</td>
<td id = "1136">&nbsp;</td>
<td id = "1137" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1141">&nbsp;</td>
<td id = "1142">&nbsp;</td>
<td id = "1143">&nbsp;</td>
<td id = "1144">&nbsp;</td>
<td id = "1145">&nbsp;</td>
<td id = "1146">&nbsp;</td>
<td id = "1147" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1151">&nbsp;</td>
<td id = "1152">&nbsp;</td>
<td id = "1153">&nbsp;</td>
<td id = "1154">&nbsp;</td>
<td id = "1155">&nbsp;</td>
<td id = "1156">&nbsp;</td>
<td id = "1157" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1161">&nbsp;</td>
<td id = "1162">&nbsp;</td>
<td id = "1163">&nbsp;</td>
<td id = "1164">&nbsp;</td>
<td id = "1165">&nbsp;</td>
<td id = "1166">&nbsp;</td>
<td id = "1167" class="bordr">&nbsp;</td>
</tr>

                 </table>
                 
            
            
            </div>
        </li>
        <li>
            <div class="d_c_content">
                 <table cellpadding="0" cellspacing="0" border="0" width="100%" class="data_table">
                    <caption class="d_c_title">十二月</caption>
                     <tr>
                         <th class="bdrdhl">日</th>
                         <th>一</th>
                         <th>二</th>
                         <th>三</th>
                         <th>四</th>
                         <th>五</th>
                         <th class="bdrdhr">六</th>  
                     </tr>
                     <tr>
<td id = "1211">&nbsp;</td>
<td id = "1212">&nbsp;</td>
<td id = "1213">&nbsp;</td>
<td id = "1214">&nbsp;</td>
<td id = "1215">&nbsp;</td>
<td id = "1216">&nbsp;</td>
<td id = "1217" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1221">&nbsp;</td>
<td id = "1222">&nbsp;</td>
<td id = "1223">&nbsp;</td>
<td id = "1224">&nbsp;</td>
<td id = "1225">&nbsp;</td>
<td id = "1226">&nbsp;</td>
<td id = "1227" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1231">&nbsp;</td>
<td id = "1232">&nbsp;</td>
<td id = "1233">&nbsp;</td>
<td id = "1234">&nbsp;</td>
<td id = "1235">&nbsp;</td>
<td id = "1236">&nbsp;</td>
<td id = "1237" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1241">&nbsp;</td>
<td id = "1242">&nbsp;</td>
<td id = "1243">&nbsp;</td>
<td id = "1244">&nbsp;</td>
<td id = "1245">&nbsp;</td>
<td id = "1246">&nbsp;</td>
<td id = "1247" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1251">&nbsp;</td>
<td id = "1252">&nbsp;</td>
<td id = "1253">&nbsp;</td>
<td id = "1254">&nbsp;</td>
<td id = "1255">&nbsp;</td>
<td id = "1256">&nbsp;</td>
<td id = "1257" class="bordr">&nbsp;</td>
</tr>
<tr>
<td id = "1261">&nbsp;</td>
<td id = "1262">&nbsp;</td>
<td id = "1263">&nbsp;</td>
<td id = "1264">&nbsp;</td>
<td id = "1265">&nbsp;</td>
<td id = "1266">&nbsp;</td>
<td id = "1267" class="bordr">&nbsp;</td>
</tr>
                 </table>
            </div>
        </li>    
    </ul>
</div>
	</body>
</html>
