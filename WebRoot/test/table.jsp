<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../include/css/jscrollpane2.css" />
<script type="text/javascript" src="../include/jquery-1.4.2.min.js" ></script>
<script type="text/javascript" src="../include/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="../include/js/jquery.jscrollpane.min.js"></script>

<title>无标题文档</title>
<style>
body{margin:0px auto;position:static;*position:absolute}
html{overflow:hidden}

.tableTotal{overflow:auto;overflow-x:hidden;height:400px;position:relative;width:100%;}

.tableLeft{width:260px;float:left;z-index:1000;height:400px;position:relative;}
.tableRight{margin-left:260px;position:relative;}

.table1{font-size:12px;font-family:'microsoft yahei';}
.table1 th{font-size:12px;font-family:'microsoft yahei';table-layout: fixed; word-break: break-all;border-collapse: collapse;height:30px;line-height:100%;padding:3px 5px;text-align:left;word-break: keep-all;}
.table1 td{table-layout: fixed; word-break: break-all;border-collapse: collapse;height:30px;line-height:100%;padding:3px 5px;text-align:left;border-left:1px solid #ccc;word-break: keep-all;}
.fixTop{
	height:30px;
	position:fixed;
	top:0px;
	background-color:#fff;
	/*_position:absolute;
    _bottom:auto;
   _top:expression(eval(document.documentElement.scrollTop));  
   /* _top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)));	*/
  }


</style>


</script>
<script>
$(document).ready(function(e) {
	
	function ThClone(table)
	{
		var table=$("#"+table);
		var tableTh=table.find("table").find("tr").eq(0);
		var tableTd=table.find("table");
		
		
		var tableThClone=tableTh.clone();
		
		var tableClone=$('<table id="'+table+'Clone" border="0" cellspacing="0" cellpadding="0"></table>');
		tableThClone.appendTo(tableClone);
		tableClone.appendTo(table);
		
		tableClone.addClass("table1");
		
		tableClone.css({
			         'position':'fixed',
					 'top': table.offset().top+"px",
					 'background-color':'#c3c3c3',
					 'word-break':'keep-all'			 
				    });
		tableThClone.css({'word-break':'keep-all'	})
		
	    for(var i=0;i<tableTh.find("th").length;i++)
	     {
		    tableClone.find("th").eq(i).width(tableTd.find("td").eq(i).width());	
	     }
		
		
		
		
	}
	ThClone("tableLeft");
	ThClone("tableRight");
	
	$('#tableRight').jScrollPane({
                       //showArrows: true,  //显示自动箭头
                       mouseWheelSpeed:40, //鼠标速度(高度)
                       scrollPagePercent:0.8 //page按钮翻页速度
					  
                   });
	
	
/* 
    var tableLeft=$("#tableLeft");
	alert(tableLeft.offset().left)
	var tableLeftTh=tableLeft.find(".table1").find("tr").eq(0);
	var tableLeftThClone=tableLeftTh.clone();
    tableLeftThClone.appendTo(tableLeft);
	tableLeftThClone.css({'position':'fixed',
	                 'left':tableLeft.offset.left+"px",
					 'top':'0px',
					 'padding-top':'9px',
					 'background-color':'#fff'			 
				    });
	tableLeftThClone.addClass("table1");
    for(var i=0;i<tableLeftTh.find("th").length;i++)
	{
		tableLeftThClone.find("th").eq(i).width(tableLeftTh.find("th").eq(i).width())	
	}
	
*/  
});



</script>

</head>

<body>

<div class="tableTotal" id="tableTotal">
<div class="tableLeft" id="tableLeft">
   <table border="0" cellspacing="0" cellpadding="0" class="table1" width="100%">
 <tr>
  <th>&nbsp;</th>
  <th>序号</th>
  <th>票据号码</th>
  <th>操作</th>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 1 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 2 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 3 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 4 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 5 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 6 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 7 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 8 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 9 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 10 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 11 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 12</td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 13 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 14 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>
 <tr>
   <td><input id="id616189" value="c7ee2713-cc2b-497a-af02-72078a8334c1" type="checkbox" name="mm"  onclick="checkItem(this, 'mmAll')" state="2" /></td>
   <td> 15 </td>
   <td>31400051/20885756</td>
   <td><a href="javascript:void">查看</a>  <a href="javascript:void">修改</a></td>
 </tr>

</table>


</div>
<div class="tableRight" id="tableRight" >

  <table id="demoTable"  border="0" cellspacing="0" cellpadding="0" class="table1" >
          <tr>
         
            <th nowrap="nowrap">公司代码 </th>
            <th nowrap="nowrap">利润中心</th>
            <th nowrap="nowrap">收票日期</th>
            <th nowrap="nowrap">寄票人</th>
            <th nowrap="nowrap">寄票单位</th>
            <th nowrap="nowrap">出票日</th>
            <th nowrap="nowrap">到期日</th>
            <th nowrap="nowrap">出票人</th>
            <th nowrap="nowrap">付款行</th>
            <th nowrap="nowrap">票面金额</th>
            <th nowrap="nowrap">记账金额</th>
            <th nowrap="nowrap">收票单位</th>
            <th nowrap="nowrap">外部客户</th>
            <th nowrap="nowrap">录入人</th>
            <th nowrap="nowrap">记录状态</th>
            <th nowrap="nowrap">录入日期</th>
          </tr>
          <tr>
        
            <td nowrap="nowrap">1001</td>
            <td nowrap="nowrap">LR1802</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">江苏</td>
            <td nowrap="nowrap">周戊棋</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">江苏通行工程建设有限公司</td>
            <td nowrap="nowrap">江苏丹阳农村商业银行清算中心</td>
            <td nowrap="nowrap">100000</td>
            <td nowrap="nowrap">100000</td>
            <td nowrap="nowrap">泵送</td>
            <td nowrap="nowrap">浙江旭业工程设备有限公司</td>
            <td nowrap="nowrap"></td>
            <td nowrap="nowrap">未记账 </td>
            <td nowrap="nowrap">2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
            
            <td>1002</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>江苏</td>
            <td>周戊棋</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td>江苏通行工程建设有限公司</td>
            <td>江苏丹阳农村商业银行清算中心</td>
            <td>50000</td>
            <td>50000</td>
            <td>泵送</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr>
            
            <td>1003</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td>河南圣光集团医药物流有限公司</td>
            <td>洛阳银行郑州分行</td>
            <td>100000</td>
            <td>100000</td>
            <td>泵送</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
           
            <td>1004</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td nowrap="nowrap">河南省亚都国际文化传媒有限公司</td>
            <td>浦发开封分行营业部</td>
            <td>200000</td>
            <td>200000</td>
            <td>湖南中发</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
           
            <td>1005</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td nowrap="nowrap">河南省亚都国际文化传媒有限公司</td>
            <td>浦发开封分行营业部</td>
            <td>200000</td>
            <td>200000</td>
            <td>湖南中发</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
           <tr>
        
            <td nowrap="nowrap">1006</td>
            <td nowrap="nowrap">LR1802</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">江苏</td>
            <td nowrap="nowrap">周戊棋</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">江苏通行工程建设有限公司</td>
            <td nowrap="nowrap">江苏丹阳农村商业银行清算中心</td>
            <td nowrap="nowrap">100000</td>
            <td nowrap="nowrap">100000</td>
            <td nowrap="nowrap">泵送</td>
            <td nowrap="nowrap">浙江旭业工程设备有限公司</td>
            <td nowrap="nowrap"></td>
            <td nowrap="nowrap">未记账 </td>
            <td nowrap="nowrap">2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
          
            <td>1007</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>江苏</td>
            <td>周戊棋</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td>江苏通行工程建设有限公司</td>
            <td>江苏丹阳农村商业银行清算中心</td>
            <td>50000</td>
            <td>50000</td>
            <td>泵送</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr>
           
            <td>1008</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td>河南圣光集团医药物流有限公司</td>
            <td>洛阳银行郑州分行</td>
            <td>100000</td>
            <td>100000</td>
            <td>泵送</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
           
            <td>1009</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td nowrap="nowrap">河南省亚都国际文化传媒有限公司</td>
            <td>浦发开封分行营业部</td>
            <td>200000</td>
            <td>200000</td>
            <td>湖南中发</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
            
            <td>1010</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>

            <td>2012-07-02</td>
            <td nowrap="nowrap">河南省亚都国际文化传媒有限公司</td>
            <td>浦发开封分行营业部</td>
            <td>200000</td>
            <td>200000</td>
            <td>湖南中发</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr> <tr>
         
            <td nowrap="nowrap">1011</td>
            <td nowrap="nowrap">LR1802</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">江苏</td>
            <td nowrap="nowrap">周戊棋</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">2012-07-02</td>
            <td nowrap="nowrap">江苏通行工程建设有限公司</td>
            <td nowrap="nowrap">江苏丹阳农村商业银行清算中心</td>
            <td nowrap="nowrap">100000</td>
            <td nowrap="nowrap">100000</td>
            <td nowrap="nowrap">泵送</td>
            <td nowrap="nowrap">浙江旭业工程设备有限公司</td>
            <td nowrap="nowrap"></td>
            <td nowrap="nowrap">未记账 </td>
            <td nowrap="nowrap">2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
        
            <td>1012</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>江苏</td>
            <td>周戊棋</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td>江苏通行工程建设有限公司</td>
            <td>江苏丹阳农村商业银行清算中心</td>
            <td>50000</td>
            <td>50000</td>
            <td>泵送</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr>
         
            <td>1013</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td>河南圣光集团医药物流有限公司</td>
            <td>洛阳银行郑州分行</td>
            <td>100000</td>
            <td>100000</td>
            <td>泵送</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
      
            <td>1014</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>
            <td>2012-07-02</td>
            <td nowrap="nowrap">河南省亚都国际文化传媒有限公司</td>
            <td>浦发开封分行营业部</td>
            <td>200000</td>
            <td>200000</td>
            <td>湖南中发</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          <tr class="tr2">
            
            <td>1015</td>
            <td>LR1802</td>
            <td>2012-07-02</td>
            <td>河南</td>
            <td>高蒙蒙</td>
            <td>2012-07-02</td>

            <td>2012-07-02</td>
            <td nowrap="nowrap">河南省亚都国际文化传媒有限公司</td>
            <td>浦发开封分行营业部</td>
            <td>200000</td>
            <td>200000</td>
            <td>湖南中发</td>
            <td>浙江旭业工程设备有限公司</td>
            <td></td>
            <td>未记账 </td>
            <td>2012-12-05 15:10:16.0</td>
          </tr>
          
         
        </table>


</div>



</div>

</body>
</html>
