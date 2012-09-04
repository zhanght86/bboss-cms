
　　
/*------------------------------------------------------------------------*
 *页面初始化工作：　　　　　　　　　　　                                      　*
 * js_io_smartInput_buildDiv();    //在[显示层的页面]初始化时构建层           *
 * document.onkeydown = js_io_smartInput_responseUpDownEnterKey;          *
 *    　　　　　　　　　　　　　　　　　//在[层内嵌页面]响应"上,下,确定键"事件  　  *
 *------------------------------------------------------------------------*/

/**
* @param obj : 光标进入的 "文本框对象"
* @param urlName: 查询页面 "smartInput.jsp"相对于 "你应用此控件的页面"的路径
* @param smartInputSQL:查询的 SQL语句
* @param backCellsName：返回字段列表,要求:1.第一个为"新增"标识名(其值为 0:新增,1:从数据库中取出)
*                       每字段在页面上需要唯一，其顺序要与 SQL 查出的完全一致)",各字段间由 [:] 隔开,不需要返回的用“NULL”代替。
* @param viewCellsName : 需要显示的字段的表头名,各字段间由 [:] 隔开
* @param viewCellsIndex：需要显示的字段在"查询字段列表"中的位置,从 0开始，各字段间由 [:] 隔开。
* @param isShowWidget [该值从<input isShowWidget='' >获取,default=1]:   控件是否要显示, 0: 隐藏  1和其他为:显示
* @param isNewAllow   [该值从<input isNewAllow='' >获取,default=1]:     是否容许用户新增参数参数, 0:不容许   1:容许
                         如果[0:不容许]则:用户按[Esc]键,或[Tab]键,或点击控件右上方[X]时,将用户所输入的内容清空掉
* */
	function js_io_smartInput_onfocus(obj,urlName,smartInputSQL,backCellsName,viewCellsName,viewCellsIndex){
    	var isShowWidget = obj.getAttribute("isShowWidget");
    	if(isShowWidget == null || isShowWidget == ""){
        	isShowWidget = "1";
        }
        var isNewAllow = obj.getAttribute("isNewAllow");
        if(isNewAllow == null || isNewAllow == ""){
         	isNewAllow = "1";
        }
    	if(isShowWidget == "1"){
            //设置参数（放入cookie）
            	var smartInputName = obj.name;   //包含smartInput控件的‘输入框名字’
        	js_io_smartInput_setParam(smartInputSQL,backCellsName,viewCellsName,viewCellsIndex,isNewAllow,isShowWidget,smartInputName);
        	js_io_smartInput_fillFrame('smartInputFrame',urlName); //填充层
    		js_io_smartInput_textTail();                           //文本框获得焦点时，光标在文字最后
    	}else{
        	js_io_smartInput_setCookie("isShowWidget","0");
      	}
    }

	/*  @更改层内嵌框架中嵌入的页面
   		@param myFrame : 框架名
  		@param url     : 框架内显示的页面 url
	*/
	function js_io_smartInput_fillFrame(myFrame,url){
    	var  cal=document.getElementById(myFrame);          //获取层内嵌的框架
    	cal.src = url;
	}

	//设置参数（放入cookie）
	function js_io_smartInput_setParam(smartInputSQL,backCellsName,viewCellsName,viewCellsIndex,isNewAllow,isShowWidget,smartInputName){
        //清除原来Cookie的值
    	/*js_io_smartInput_clearCookie("smartInputSQL");
    	js_io_smartInput_clearCookie("backCellsName");
    	js_io_smartInput_clearCookie("viewCellsName");
    	js_io_smartInput_clearCookie("viewCellsIndex");
    	js_io_smartInput_clearCookie("isNewAllow");
    	js_io_smartInput_clearCookie("smartInputName");
    	js_io_smartInput_clearCookie("isShowWidget");*/
        //设置新的Cookie
    	js_io_smartInput_setCookie("smartInputSQL",smartInputSQL);
    	js_io_smartInput_setCookie("backCellsName",backCellsName);
    	js_io_smartInput_setCookie("viewCellsName",viewCellsName);
    	js_io_smartInput_setCookie("viewCellsIndex",viewCellsIndex);
    	js_io_smartInput_setCookie("isNewAllow",isNewAllow);
    	js_io_smartInput_setCookie("smartInputName",smartInputName);
    	js_io_smartInput_setCookie("isShowWidget",isShowWidget);
    }

	/**
	* 文本框响应用户键盘动作
	* @param obj : 文本框对象
	* @param myColor:高亮显示的颜色
	* */
    function js_io_smartInput_responseKey(obj,myColor){
    	var cal=document.getElementById("martInputDiv");
    	if(js_io_smartInput_getCookie("isShowWidget") == "1"){
   	 if(event.keyCode==27 || event.keyCode==9){	 //键[Esc][Tab]
    		if(cal.style.display != "none"){
    			js_io_smartInput_closeDiv();
                }
   	 }else if(event.keyCode==40){//向下箭头：将焦点移到SmartInput框
              //如果控件容许显示,而且控件是隐藏的，则让其显示
            if(cal.style.display == "none"){
            	js_io_smartInput_showDiv(obj); //显示层
            }else{
            	js_io_smartInput_goDown('smartInputBody','smartInputTable',myColor);
            }
        }else if(event.keyCode == 13 || event.keyCode == 20 || event.keyCode == 16 || event.keyCode == 17){
        }else{
            if(js_io_smartInput_getCookie("isShowWidget") == "1" && cal.style.display == "none"){
            	js_io_smartInput_showDiv(obj); //显示层
            }
            js_io_smartInput_ref(obj);	                         //其他键：执行SmartInput功能
        }
      }
    }

	/*清空输入的内容
    *@param backCellsName ：需要清空的<input>名字串，各<input>名字以“：”分开
    */
	function js_io_smartInput_clearInput(cellsArray){
        for(var i=1;i<cellsArray.length;i++){
            if(cellsArray[i] != "NULL" && document.all(cellsArray[i]) != null){
            	document.all(cellsArray[i]).value = "";
            }
        }
	}

/*点击“向下键”，函数将焦点移至“层”内，并高亮显示“查询结果”的第一行
  @param smartInputBody  : 框架内嵌页面body的id 如：<body id="smartInputBody">
  @param smartInputTable : 显示＂查询结果＂的table的id　如：<table id="smartInputTable">
  @param myColor : 高亮显示的颜色（须为小写：如#ff0000）
*/
function js_io_smartInput_goDown(smartInputBody,smartInputTable,myColor){
 	var objBody = document.smartInputFrame.document.getElementById(smartInputBody);   //<body> Object
    objBody.focus();                              //让层内嵌页面获得焦点
    var smartInputTable = document.smartInputFrame.document.getElementById(smartInputTable);  //<table> Object
    var colCount = smartInputTable.rows.length;                                  //表格的行数
    if(colCount > 0){
       var myCol =  smartInputTable.rows(1);
    	myCol.style.backgroundColor=myColor;
    }
}

/* @响应每一次keyUp事件，将输入内容提交到＂层＂内处理
   @param  obj  : 产生该事件的对象
*/
function js_io_smartInput_ref(obj) {
     if(document.smartInputFrame!=null&&document.smartInputFrame.smartInputResponseForm!=null){
		document.smartInputFrame.smartInputResponseForm.smartInputCode.value = obj.value;
		document.smartInputFrame.smartInputResponseForm.submit()
		return true;
     }
}

      /**由键[Esc][Tab]关闭按钮[X]关闭层时触发次函数
      *
      * */
      function js_io_smartInput_closeDiv(){
      	    var cal=document.getElementById("martInputDiv");
            js_io_smartInput_hideDiv(cal);
            //还原“新增标识”
            var backCellsName = js_io_smartInput_getCookie("backCellsName");
       		 var cellsArray = backCellsName.split(":");//解析 backCellsName
            if(cellsArray[0] != "NULL" && document.all(cellsArray[0]) != null){
            		document.all(cellsArray[0]).value = "0";
        	}
            //如果参数不容许新增,则将所有的值清空
            var isNewAllow = js_io_smartInput_getCookie("isNewAllow");
            if(isNewAllow == "0"){
            	js_io_smartInput_clearInput(cellsArray);
            }
            //将光标移入输入框
            var smartInputName = js_io_smartInput_getCookie("smartInputName");
            document.all(smartInputName).focus();
  		}

		/*@desc  响应[键盘上\下\确定键],高亮显示用户选择表格行
        @param event           (系统事件)
        @param smartInputTable :        需要响应键盘事件的表格"id
        @param hightLightColor: 被选中行的颜色
        @param lowLightColor:   未被选中行的颜色
        @memo  颜色需要小写
        应用示例：document.onkeydown = js_io_smartInput_responseUpDownEnterKey;    //页面响应"左,上,下,确定键"事件
        */
    	function js_io_smartInput_responseUpDownEnterKey(){
            var smartInputTable = document.getElementById("smartInputTable");  //表格对象
            var hightLightColor = '#ff0000';                   //"高亮显示"的颜色
            var lowLigthColor = '';                            //"未选中"行的颜色
            var colCount = smartInputTable.rows.length;                  //表格的行数
            if(event.keyCode==38){ //向上键
                for(i=1;i<colCount;i++){
                	if(smartInputTable.rows(i).style.backgroundColor == hightLightColor){
                        smartInputTable.rows(i).style.backgroundColor = lowLigthColor;
                    	if(i==1){
                        	smartInputTable.rows(colCount - 1).style.backgroundColor = hightLightColor;
                        }else{
                         	smartInputTable.rows(i-1).style.backgroundColor = hightLightColor;
                        }
                        break;
                    }
                }
            }else if(event.keyCode==40){　//向下键
                for(i=1;i<colCount;i++){
                	if(smartInputTable.rows(i).style.backgroundColor == hightLightColor){
                        smartInputTable.rows(i).style.backgroundColor = lowLigthColor;
                    	if(i==(colCount - 1)){
                        	smartInputTable.rows(1).style.backgroundColor = hightLightColor;
                        }else{
                         	smartInputTable.rows(i+1).style.backgroundColor = hightLightColor;
                        }
                        break;
                    }
                }
            }
            else if(event.keyCode == 13){  //确定键
                var hightLightCol ;   //用户选中的"表格中一列"[对象]
                for(i=0;i<colCount;i++){
                 	if(smartInputTable.rows(i).style.backgroundColor == hightLightColor){
                    	hightLightCol = smartInputTable.rows(i);
                        js_io_smartInput_returnValue(hightLightCol);
                    }
                }
            }
            else if(event.keyCode == 27 || event.keyCode==9){  //[Esc][Tab] 键 = 隐藏层,清空数据
            	parent.window.js_io_smartInput_closeDiv();
            }else if(event.keyCode == 37){ //向左箭头
            	//将光标移入输入框
            	var smartInputName = js_io_smartInput_getCookie("smartInputName");
           		parent.document.all(smartInputName).focus();
            }
   		 }

        /**
    	* 返回查询结果
        * @param hightLightCol 被选中的列对象
        * */
        function js_io_smartInput_returnValue(hightLightCol){
        	var backFieldStr = js_io_smartInput_getCookie("backCellsName");
        	var backFieldArray = backFieldStr.split(":");

        	for(var i=1;i<backFieldArray.length;i++){
                if(backFieldArray[i] != "NULL" && parent.document.all(backFieldArray[i]) != null){
                	if(parent.document.all(backFieldArray[i]).type=="select-one") {
                        	if(hightLightCol.getAttribute(backFieldArray[i])==""){
                           		parent.document.all(backFieldArray[i]).selectedIndex = 0;
                        	}else{
                            		parent.document.all(backFieldArray[i]).value = hightLightCol.getAttribute(backFieldArray[i]);
                        	}
                    	}else{
                 	 	parent.document.all(backFieldArray[i]).value = hightLightCol.getAttribute(backFieldArray[i]);
                    	}
                }
            }
            if(backFieldArray[0] != "NULL" && parent.document.all(backFieldArray[0]) != null){
                parent.document.all(backFieldArray[0]).value = "1"; //"新增标识"标志为：从数据库取出
            }
			//返回了查询结果后，将查询控件隐藏
            var cal = parent.document.getElementById("martInputDiv");
            js_io_smartInput_hideDiv(cal);
            //将光标移入输入框
            var smartInputName = js_io_smartInput_getCookie("smartInputName");
            parent.document.all(smartInputName).focus();
        }




/**----------------------------------------------------------------------------------------
* 控件框架基本控制函数
* ----------------------------------------------------------------------------------------*/

   var browserName=navigator.appName;
   var isIE=browserName.indexOf("Microsoft")!=-1?true:false;

  /*　动态构建＂层＂　*/
  function js_io_smartInput_buildDiv()
  {
      document.write("<div id='martInputDiv' style=\"display:none;position:absolute;width:220;background:#DEE5FF;z-index:100\">");
      document.write("<table width='220' align='center' border='1' bordercolorlight='#000000' bordercolordark='#FFFFFF' >");
      document.write("<tr><td align='left' width='195'><font color='#FF0000'>参数内容智能输入</font></td>");
      document.write("<td  align=\"center\" width='25'><span title=关闭 style=\"CURSOR: hand;color:#FF0000;font-size:12px;font-weight:bold;margin-right:4px;\" onclick=js_io_smartInput_closeDiv() >×</span>");
      document.write("</td></tr>");
      document.write("<tr><td colspan='2'>");
      document.write("<iframe id=\"smartInputFrame\"  border=\"0\" frameborder=\"0\" framespacing=\"0\" marginheight=\"10\" name=\"smartInputFrame\" noResize scrolling=\"auto\" src=\"\"  vspale=\"0\" width=\"100%\" height=\"240\"></iframe>");
      document.write("</td></tr>");
      document.write("</table>");
      document.write("</div>");
	  var cal=document.getElementById("martInputDiv");
	  js_io_smartInput_hideDiv(cal);
  }　

　/*隐藏＂层＂ */
  function js_io_smartInput_hideDiv(cal){ cal.style.display="none"; }

  /* 显示＂层＂　*/
  function js_io_smartInput_showDiv(obj){
        var cal=document.getElementById("martInputDiv");
     	 var desTxtId=obj;
	  	cal.style.display="";
	  	vtop=js_io_smartInput_offsetTop(obj);
	  	vheight=obj.offsetHeight;
    	 vwidth=obj.offsetWidth;
    	 vleft=js_io_smartInput_offsetLeft(obj)+vwidth;
	  	if(isIE)
	 	 {
	    	 if(vtop+cal.offsetHeight>document.body.scrollTop+document.body.clientHeight)
	     	   vtop=vtop+vheight-cal.offsetHeight;
	    	 if(vleft+cal.offsetWidth>document.body.scrollLeft+document.body.clientWidth)
	      	 vleft=js_io_smartInput_offsetLeft(obj)-cal.offsetWidth;
	    	 cal.style.top=vtop;
	    	 cal.style.left=vleft;
	 	 }
	  	else
	  	{
        	if(vtop+cal.offsetHeight>window.innerHeight)
	      	    vtop=vtop+vheight-cal.offsetHeight;
        	if(vleft+cal.offsetWidth>window.innerWidth)
	       	   vleft=js_io_smartInput_offsetLeft(obj)-cal.offsetWidth;
        	cal.style.top=vtop;
        	cal.style.left=vleft;
	 	 }
  }

　/*设置层离窗口左边的长度*/
  function js_io_smartInput_offsetLeft(obj)
  {
    x = obj.offsetLeft;
    for (objParent=obj.offsetParent;objParent;objParent=objParent.offsetParent)x+=objParent.offsetLeft;
    return x;
  }

　/*设置＂层＂离窗口顶部的长度*/
  function js_io_smartInput_offsetTop(obj)
  {
     y = obj.offsetTop;
     for(objParent=obj.offsetParent;objParent;objParent=objParent.offsetParent)y+=objParent.offsetTop;
     return y;
  }





/**---------------------------------------------------------------------------------------------
* 视觉效果控制函数
* ---------------------------------------------------------------------------------------------*/

        /*下面两个函数用于:鼠标置于表格的一行,触发动态视觉效果*/
          //函数 一
          function js_io_smartInput_sbar(st){
               var hightLightColor = '#ff0000';                  //"高亮显示"的颜色
           	 	st.style.backgroundColor = hightLightColor ;
          }
          //函数 二
          function js_io_smartInput_cbar(st){
          		var lowLigthColor = '';                            //"未选中"行的颜色
           	 	st.style.backgroundColor = lowLigthColor;
          }

          /*作用: 当文本框获得输入焦点时,光标总是在文本框(text)文字的最后(Tail)*/
   		  /*用法: <input type=text name=text1 value="123" onfocus="js_io_smartInput_textTail()"> */
          function js_io_smartInput_textTail() {
        	 var e = event.srcElement;
         	 var r =e.createTextRange();
        	 r.moveStart('character',e.value.length);
        	 r.collapse(true);
        	 r.select();
          }



/**-----------------------------------------------------------------------------------------
* 操作 Cookie
* -----------------------------------------------------------------------------------------*/

		/** 设置Cookie
		* @param key Cookie的标识
		* @param value Cookie的值
		* */
		function js_io_smartInput_setCookie(key,value) {
    		document.cookie = key + "=" + value + ";path=/";
		}

        /*从cookie中读取数据*/
        function js_io_smartInput_getCookie(key) {
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

		/** 清除cookie
		* @param key Cookie的唯一标识
		**/
		function js_io_smartInput_clearCookie(key) {
            if(js_io_smartInput_getCookie(key) != ""){
    			document.cookie = key + "=";
            }
		}


