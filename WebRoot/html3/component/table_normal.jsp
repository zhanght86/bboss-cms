<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>   
    





<script type="text/javascript">
$(document).ready(function(){
	tableColor(); 
})

</script>
   

  <div class="position"> 
      <a href="main_index.html"><img src="../images/home.gif" width="11" height="11" /></a> &gt;待办中心&gt; 已办
      </div>
 <div class="search">
        <div id="search_content">
   <div class="sany_li">
     <span class="sany_li_title">部门：</span>
     <span>
        <input name="" type="text"  class="input1" value="部门" onfocus="this.value=''" onblur="if(this.value==''){this.value='单号'}" />
     </span>
    </div>
        <div class="sany_li">
     <span class="sany_li_title">当前状态：</span>
     <span>
       <select name="select" class="select1" id="select">
          <option>审批中</option>
          <option>已完成</option>
       </select>
    </span>
    </div>
    <div class="sany_li2">
     <span class="sany_li_title">部门：</span>
     <span class="fLeft" ><input type="text" class="Wdate" id="d53" onFocus="var d54=$dp.$('d54');WdatePicker({onpicked:function(){d54.focus();},maxDate:'#F{$dp.$D(\'d54\')}'})" value="提交日期"/> 至 
      <input type="text" class="Wdate" id="d54" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d53\')}'})" value="提交日期"//>
     </span>
     <a  href="#" class="bt_search"  id="search_button" style="margin-left:10px;margin-top:0px;" ><span>查询</span></a>
    </div>
    </div> 
      
     
     
     
      
      
     <div style="clear:both;"></div>
    </div> 
 <div class="list_box">
   <div class="tableOperation">
        <div class="sort">
          <ul>
              <li><a>按审批日期排序<span><em></em></span></a></li>
              <li><a>按提交日期排序<span><em></em></span></a></li>
          
          </ul>
        
        </div>
        <div class="operation">
            <ul>
               <li>汇总价格：<input type="text" class="input1 w50" /> 元/立方</li>
               <li><a  href="#" class="bt_senior_search"><span>删除</span></a></li>
               <li><a  href="#" class="bt_senior_search"><span>批量导出</span></a></li>
            </ul>
        
        </div>
    </div>
    
    <div class="tableTotal" id="tableTotal">
   
    
      <table   border="0" cellspacing="0" cellpadding="0" class="table_basic table_bline">
              
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
              <tr>
                
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
              <tr>
               
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
              <tr>
               
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
              <tr>
              
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
              <tr>
               
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
              <tr>
                
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
              <tr>
            
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
              <tr>
          
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
              <tr>
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
  
  <div class="page">
     <ul>
        <li><a class="lastPage">上一页</a></li>
        <li><a>1</a></li>
        <li><a class="select">2</a></li>
        <li><a>3</a></li>
        <li><a>4</a></li>
        <li><a>5</a></li>
        <li><a>6</a></li>
        <li><a>7</a></li>
        <li><a>8</a></li>
        <li><a>9</a></li>
        <li><a>...</a></li>
        <li><a>45</a></li>
        <li><a>46</a></li>
        <li><a class="nextPage">下一页</a></li>
     
     </ul> 
  
  </div>
  
