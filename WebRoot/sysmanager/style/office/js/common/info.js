
/** ------------------------配置信息---------------------------------*
* //@param 1 [这里可以控制'提示框'自动关闭的时间]
* //@param 2 [在这里可以控制控件向上滚动的速度]
* //@param 3 [这里可以控制'提示框'内显示的内容]
* -----------------------------------------------------------------*/

//window.onload = js_msg_getMsg;
window.onload = js_msg_showMsg;
window.onresize = js_msg_resizeDiv;
window.onerror = function(){}


//如果“消息提示框”中有内容，则显示"消息提示框"
function js_msg_showMsg(){
    if(getCookie("js_showFlag")=="1") {
        js_msg_getMsg();
        //clearParament();
        setCookie("js_showFlag","0");
    }
}

// >>>>>>>>>>>>>>>>>> 以下为通用函数 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//全局变量
var js_msg_divHeight,js_msg_docHeight,js_msg_objTimer,js_msg_i = 0;
//显示 "类MSN消息提示框"
function js_msg_getMsg()
{
   var divTop,divLeft,divWidth,docWidth;
   try{
       divTop = parseInt(document.getElementById("eMeng").style.top,10)
       divLeft = parseInt(document.getElementById("eMeng").style.left,10)
       js_msg_divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10)
       divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10)
       docWidth = document.body.clientWidth;
       js_msg_docHeight = document.body.clientHeight;
       document.getElementById("eMeng").style.top = parseInt(document.body.scrollTop,10) + js_msg_docHeight + 10;// js_msg_divHeight
       document.getElementById("eMeng").style.left = parseInt(document.body.scrollLeft,10) + docWidth - divWidth
       document.getElementById("eMeng").style.visibility="visible"
       js_msg_objTimer = window.setInterval("js_msg_moveDiv()",10)
   }catch(e){}
}

//窗口改变大小时, "类MSN消息提示框"位置做调整
function js_msg_resizeDiv()
{
    var divWidth,docWidth;
    js_msg_i+=1
    if(js_msg_i>1000) js_msg_closeDiv()  //@param 1 [这里可以控制'提示框'自动关闭的时间]
    try{
        js_msg_divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10)
        divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10)
        docWidth = document.body.clientWidth;
        js_msg_docHeight = document.body.clientHeight;
        document.getElementById("eMeng").style.top = js_msg_docHeight - js_msg_divHeight + parseInt(document.body.scrollTop,10)
        document.getElementById("eMeng").style.left = docWidth - divWidth + parseInt(document.body.scrollLeft,10)
    }catch(e){}
}

//"类MSN消息提示框" 的移动
function js_msg_moveDiv()
{
   var divTop;
   try
   {
       if(parseInt(document.getElementById("eMeng").style.top,10) <= (js_msg_docHeight - js_msg_divHeight + parseInt(document.body.scrollTop,10)))
       {
          window.clearInterval(js_msg_objTimer)
          js_msg_objTimer = window.setInterval("js_msg_resizeDiv()",1)
       }
       divTop = parseInt(document.getElementById("eMeng").style.top,10)
       document.getElementById("eMeng").style.top = divTop - 1   //@param 2 [在这里可以控制控件向上滚动的速度]
   }catch(e){}
}

//"类MSN消息提示框" 的关闭
function js_msg_closeDiv()
{
   document.getElementById('eMeng').style.visibility='hidden';
   if(js_msg_objTimer) window.clearInterval(js_msg_objTimer)
}

//生成 "类MSN消息提示框" 的基本框架
function js_msg_buildDiv(){
    var titleValue = "";
    var message = getCookie("js_message");
    if(getCookie("js_titleFlag")=="0")
        titleValue = "系统提示：";
    else
        titleValue = "系统警告：";
    document.write("<DIV id='eMeng' style='BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX:99999; LEFT: 0px; VISIBILITY: hidden; BORDER-LEFT: #a6b4cf 1px solid; WIDTH: 180px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: 116px; BACKGROUND-COLOR: #c9d3f3'>");
    document.write("<TABLE style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid' cellSpacing=0 cellPadding=0 width='100%' bgColor=#cfdef4 border=0>");
    document.write("<TBODY>");
    document.write("<TR>");
    document.write("<TD style='FONT-SIZE: 12px;COLOR: #0f2c8c' width=30></TD>");
    document.write("<TD style='FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px;PADDING-left: 4px' vAlign=center width='100%'><font color='#ff0000'>" + titleValue + "</font></TD> ");
    document.write("<TD style='PADDING-TOP: 2px;PADDING-right:2px' vAlign=center align=right width=19><span title=关闭 style='CURSOR: hand;color:red;font-size:12px;font-weight:bold;margin-right:4px;' onclick=js_msg_closeDiv()>×</span></TD>");
    document.write("</TR>");
    document.write("<TR>");
    document.write("<TD style='PADDING-RIGHT: 1px; PADDING-BOTTOM: 1px' colSpan=3 height=90>");
    document.write("<DIV style='BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 13px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 13px;FONT-SIZE: 12px; PADDING-BOTTOM: 13px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 18px;BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%'>" + message);
    document.write("<DIV></TD>");
    document.write("</TR>");
    document.write("</TBODY>");
    document.write("</TABLE>");
    document.write("</DIV>");
}

/*从cookie中读取数据*/
function getCookie(key) {
    var search = key + "=";
    var returnvalue = "";
    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf(search);
        if (offset != -1) {
            offset += search.length
            end = document.cookie.indexOf(";", offset);
            if (end == -1)
                end = document.cookie.length;
            returnvalue = unescape(document.cookie.substring(offset,end))
        }
    }
    return returnvalue;
}

/*设置cookie*/
function setCookie(key,value) {
    document.cookie = key + "=" + value;
}

/*清除cookie*/
function clearCookie(key) {
    if(getCookie(key)!="")
        document.cookie = key + "=";
}
/**
 * 保存参数
 * showFlag "0" 显示系统消息 ; "1" 不显示系统消息
 * titleFlag "0" 系统消息标题为:"系统提示：" ; "1"系统消息标题为:"系统警告："
 * message 系统消息
 */
function saveParament(showFlag,titleFlag,message) {
    setCookie("js_showFlag",showFlag);
    setCookie("js_titleFlag",titleFlag);
    setCookie("js_message",message);
}

/**
 * 清除参数
 */
function clearParament() {
    clearCookie("js_showFlag");
    clearCookie("js_titleFlag");
    clearCookie("js_message");
}
