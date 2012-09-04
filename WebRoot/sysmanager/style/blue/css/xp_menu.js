 <!--
 var parentObj;
// menu source code

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    以下为建立菜单的部分
//_______________________________________

 //菜单条的结构
 function menu_bar(name,ico,text,key,tip,skey,type,cmd) {
     this.name = name;     //名称
     this.ico = ico;      //图标
     this.text = text;     //文字
     this.key = key;      //热键
     this.tip = tip;      //提示
     this.skey = skey;     //快捷键
     this.type = type;     //命令类型，0为弹出下级菜单，1为执行一个命令(跳转到另一个文件)
     this.cmd = cmd;      //下级菜单的名称或命令字符串，根据type的值而定
 }
 
 //菜单的结构
 function menu(name) {
     this.name = name;      //名称
     this.bar = new Array();   //所包含的菜单条数组
 }
 
 //输出一已定义的主菜单，横排的
 function popMainMenu(menu,left,top,width,height) {
     var strHTML, posLeft, posTop, i;
   
     strHTML = "<div id='" + menu.name + "' style='left:" + left + ";top:" + top + ";width:" + width + ";height:" + height + "' class='MainMenuBG'>";
     document.write (strHTML);

     posLeft = 2;
     posTop = 0;
     for (i=0;i<menu.bar.length;i++) {
         strHTML = "<div id='" + menu.bar[i].name + "' nowrap style='left:" + posLeft + ";top:" + posTop + ";height:26;' class='MainMenu' " +
                   "onmouseover='return menu_onmouseover(this,\"" + menu.bar[i].cmd + "\",\"" + menu.bar[i].text + "\")' onmouseout='return menu_onmouseout(this)' " +
                   "onclick='return menu_onclick(this,\"" + menu.bar[i].cmd + "\",\"" + menu.bar[i].text + "\")' title='" + menu.bar[i].tip + "'>" + menu.bar[i].text;
         if (menu.bar[i].key!="") {
             strHTML += "(<u>" + menu.bar[i].key + "</u>)";
         }
         strHTML += "</div>";
   
	     document.write (strHTML);
         posLeft += document.all.item(menu.bar[i].name).offsetWidth + 1;
     } 
     strHTML = "</div>";
  
     document.write (strHTML);
 }
 
 //输出一个已定义的子菜单，竖排的
 function popSubMenu(menu) {  
     var strHTML, posLeft, posTop, i;

     strHTML = "<div id='" + menu.name + "' style='left:20;top:30;width:104;height:10;' class='SubMenuBG'>";
  
     /*解决层遇到select会被覆盖的问题______程学鹏.20050113*/
     strHTML += "<iframe name='menuIframe' scrolling='no' src='test.jsp' frameborder='0' width='100%' height='" + (18 * menu.bar.length+2) + "'></iframe>";
     /*解决层遇到select会被覆盖的问题______程学鹏.20050113*/

     document.write (strHTML);
  
     posLeft = 1;
     posTop = 1;

     for (i=0;i<menu.bar.length;i++) {
         if (menu.bar[i].name!="\-") {    //如果名称不是“\-”，表示是一个普通菜单条
             strHTML = "<div id='" + menu.bar[i].name + "' style='left:" + posLeft + ";top:" + posTop + ";' class='SubMenu' " +
                       "title='" + menu.bar[i].tip + "' " +
                       "onmouseover='return bar_onmouseover(this," + menu.bar[i].type + ",\"" + menu.bar[i].cmd + "\")' " +
                       "onmouseout='return bar_onmouseout(this," + menu.bar[i].type + ",\"" + menu.bar[i].cmd + "\")' " +
                       "onclick='return bar_onclick(this," + menu.bar[i].type + ",\"" + menu.bar[i].cmd + "\",\"" + menu.bar[i].text + "\")'>" +
                       "  <table border=0 cellspacing=0 cellpadding=0 width=102>" +
                       "    <tr>";
			          /* 在菜单中显示图片的位置
					  +
                       "      <td nowrap align=center width=20 id='" + menu.bar[i].name + "_ico' class='ico'>";
             if (menu.bar[i].ico!="") {
                 strHTML += "<img border=0 width=16 height=16 src='" + menu.bar[i].ico + "'>";
             }else {
                 strHTML += "&nbsp;";
             }
             strHTML += "</td>" +
			          在菜单中显示图片的位置*/
             strHTML += "      <td nowrap id='" + menu.bar[i].name + "_text' class='text'>" + menu.bar[i].text;
             /*在菜单中显示热键的位置
			 if (menu.bar[i].key!="") { strHTML = strHTML + "(<u>" + menu.bar[i].key + "</u>)"; }*/
             strHTML += "</td>" /*+
             strHTML += "<td nowrap align=right id='" + menu.bar[i].name + "_skey' class='skey'>";
             if (menu.bar[i].skey!="") {
                 strHTML += menu.bar[i].skey;
             }else {
                 strHTML += "&nbsp;";
             }
             strHTML += "     </td>" +
			 在菜单中显示热键的位置*/
             strHTML += "      <td nowrap align=center width=20 id='" + menu.bar[i].name + "_sub' class='sub'>";
             if (menu.bar[i].type==0) {
                 //strHTML += "→";如果还有子菜单，显示的图标
            	 strHTML += "<img border='0' width='6' height='10' src='common/images/jiantou-r.gif'>";
             }
			 else {
                 strHTML += "&nbsp;&nbsp;";
             }
             strHTML += "</td>" +
                        "    </tr>" +
                        "  </table>" +
                        "</div>";
             document.write (strHTML);
             posTop += document.all.item(menu.bar[i].name).offsetHeight;
         }
		 else {       //如果名称是“\-”，表示是一条分隔线
             strHTML = "<div style='left:" + posLeft + ";top:" + posTop + ";' class='SubMenu'>" +
                       "  <table border='0' cellpadding='0' cellspacing='0' width='100'>" +
                       "    <tr>" +
                       "      <td nowrap style='background-color: #89A5CF;' width=100></td>" +
                       "      <td nowrap style='padding-left:0px;padding-top:0px;padding-bottom:0px;'>" +
                       "        <table border=0 cellpadding=0 cellspacing=0 width=100%>" +
                       "          <tr><td height=1 bgcolor='#89A5CF'></td></tr>" +
                       "        </table>" +
                       "      </td>" +
                       "    </tr>" +
                       "  </table>" +
                       "</div>";
             document.write (strHTML);
             posTop += 3;
         }
     }
     strHTML = "</div>";

     document.write (strHTML);
     document.all.item(menu.name).style.height = posTop;    //重设子菜单背景框
 }


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    以下为操纵菜单的部分
//_________________________________________________

 function showMenu(menu,submenu) {  //显示一级子菜单
     document.all.item(submenu).style.left = menu.parentElement.offsetLeft + menu.offsetLeft+1;   //设置子菜单位置
     document.all.item(submenu).style.top = menu.parentElement.offsetTop + menu.offsetTop + menu.offsetHeight -1;   //设置子菜单的位置
     document.all.item(submenu).style.visibility = "visible";   //显示子菜单
     mnuPrev[mnuPrev.length] = new menuinfo(submenu,1);   //将新打开的子菜单的ID加入当前显示菜单ID数组
 }
 
 function showSubMenu(menu,submenu) {  //显示二级及以下子菜单
     document.all.item(submenu).style.left = menu.parentElement.offsetLeft + menu.offsetLeft + menu.offsetWidth + 2;   //设置子菜单位置
     document.all.item(submenu).style.top = menu.parentElement.offsetTop + menu.offsetTop + 1;   //设置子菜单的位置
     if (document.all.item(submenu).offsetLeft + document.all.item(submenu).offsetWidth > doc_width) {
         document.all.item(submenu).style.left = menu.parentElement.offsetLeft - document.all.item(submenu).offsetWidth + 1;
     }
     if (document.all.item(submenu).offsetTop + document.all.item(submenu).offsetHeight > doc_height) {
         document.all.item(submenu).style.top = document.all.item(submenu).offsetTop + menu.offsetHeight - document.all.item(submenu).offsetHeight + 2;
     }
     document.all.item(submenu).style.visibility = "visible";   //显示子菜单
     mnuPrev[mnuPrev.length] = new menuinfo(submenu,1);   //将新打开的子菜单的ID加入当前显示菜单ID数组
 }
 
 function showstatus() {   //测试阶段用的函数，显示当前菜单ID数组内的内容
     lblMsg.innerText = "";
     for(var i=0;i<mnuPrev.length;i++) {
         lblMsg.innerText += mnuPrev[i].id + " > ";
     }
 }
 
 function menuinfo(id,type) {
     this.id = id;    //0表示菜单项，1表示菜单框
     this.type = type;
 }

 function menu_onmouseover(obj,cmd,pObj) {
     if(!mClick) {
         obj.className = "MainMenuOver";
         mnuPrev[mnuPrev.length] = new menuinfo(obj.id,0);    //将当前的菜单ID压入当前菜单ID数组
         //showstatus();   //调试阶段使用
     }
	 else {
         if (mnuPrev[0].id == obj.id) { return false; }   //如果是在本身上移动，不作为
         document.all.item(mnuPrev[0].id).className = "MainMenu";
         for (var i=1;i<mnuPrev.length;i++) {
             if (mnuPrev[i].type==0) {   //如果是菜单项，设置成默认样式
                 /*document.all.item(mnuPrev[i].id + "_ico").className = "ico";*/
                 document.all.item(mnuPrev[i].id + "_text").className = "text";
                 /*document.all.item(mnuPrev[i].id + "_skey").className = "skey";*/
                 document.all.item(mnuPrev[i].id + "_sub").className = "sub";
             }
			 else {   //如果是菜单框，隐藏它
                 document.all.item(mnuPrev[i].id).style.visibility = "hidden";
             }
         }
         mnuPrev.length = 0;
         obj.className = "MainMenuClick";
         mnuPrev[mnuPrev.length] = new menuinfo(obj.id,0);
         showMenu(obj,cmd);
         //showstatus();   //调试阶段使用

         parentObj = pObj;
     }
 }

 function menu_onmouseout(obj) {	
     if(!mClick) {
         obj.className = "MainMenu";
         mnuPrev.length --;   //清除最后一个压入当前菜单ID数组的菜单ID
         //showstatus();   //调试阶段使用
     }
 }
 
 function menu_onclick(obj,cmd,pObj) {
	 
	 parentObj = pObj;

     if(!mClick) {    //如果菜单当前状态是非点击状态，证明当前是从非点击状态到点击状态
         obj.className = "MainMenuClick";    //设置当前菜单为鼠标单击之后的状态
         showMenu(obj,cmd);    //显示子菜单
         //showstatus();   //调试阶段使用
         mClick = true;   //设置菜单当前是点击状态
     }
	 else {
         obj.className = "MainMenuOver";    //设置当前菜单为鼠标移动上面的状态，非点击状态
         document.all.item(mnuPrev[mnuPrev.length-1].id).style.visibility = "hidden";
         mnuPrev.length --;
         //showstatus();   //调试阶段使用
         mClick = false;    //主菜单当前是非点击状态
     }
     bMenuClick = true;   //在一个菜单上单击后设置其状态为true,用来屏蔽document_click事件
 }

 function bar_onmouseover(obj,type,cmd) {
     for (var i=1;i<mnuPrev.length;i++) {
         if(mnuPrev[i].id==obj.parentElement.id) {
             for (var j=i+1;j<mnuPrev.length;j++) {    //处理由前一个菜单项打开的子菜单，可能有多层
                 if (mnuPrev[j].type==0) {   //如果是菜单项，设置成默认样式
                     /*document.all.item(mnuPrev[j].id + "_ico").className = "ico";*/
                     document.all.item(mnuPrev[j].id + "_text").className = "text";
                     /*document.all.item(mnuPrev[j].id + "_skey").className = "skey";*/
                     document.all.item(mnuPrev[j].id + "_sub").className = "sub";
                 }
				 else {   //如果是菜单框，隐藏它
                     document.all.item(mnuPrev[j].id).style.visibility = "hidden";
                 }
             }
             mnuPrev.length = i + 1;
             break;
         }
     }
     mnuPrev[mnuPrev.length] = new menuinfo(obj.id,0);
     //showstatus();   //调试阶段使用
     /*document.all.item(obj.id + "_ico").className = "icoOver";*/
     document.all.item(obj.id + "_text").className = "textOver";
     /*document.all.item(obj.id + "_skey").className = "skeyOver";*/
     document.all.item(obj.id + "_sub").className = "subOver";
     if (type==0) { showSubMenu(obj,cmd); }   //如果有下一级菜单就展开
         //showstatus();   //调试阶段使用
 }
  
 function bar_onmouseout(obj,type,cmd) {
     if (type!=0) {
         /*document.all.item(obj.id + "_ico").className = "ico";*/
         document.all.item(obj.id + "_text").className = "text";
         /*document.all.item(obj.id + "_skey").className = "skey";*/
         document.all.item(obj.id + "_sub").className = "sub";
         if (mnuPrev.length>0) {
             mnuPrev.length --;
         }
         //showstatus();   //调试阶段使用
     }
 }

  function bar_onclick(obj,type,cmd,text) {
     	 
     if (type==1) {
		 showStatusBar(text); //在下方的状态栏中显示的信息

         //window.navigate(cmd);   //跳转到相应的文件
         document.main.document.location = cmd; //演示期间，将文件名显示出来
     }
	 else {
         bMenuClick = true;   //在一个菜单上单击后设置其状态为true,用来屏蔽document_click事件
     }
 }


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    以下为与主文件有关的控制部分，具体使用时需要关注的也仅仅这部分而已
//_____________________________________________________________

 var doc_width;    //当前窗口的宽度，随窗口大小改变而改变
 var doc_height;    //当前窗口的高度，随窗口大小改变而改变
 
 var mClick = false;   //一级菜单是否被点击，初始一级菜单是未被点击状态
 var bMenuClick = false;    //单击是否是在一个菜单上，初始单击不是在一个菜单上
 var mnuPrev = new Array();   //当前显示的菜单ID数组，初始为空

 function document_onselectstart() {
     return false;    //屏蔽内容选择功能
 }
 
 //在下方的状态栏中显示的信息
 function showStatusBar(message) {
	 if(parent.bottom!=null) {
		 var text = parentObj + " ---> " + message
         window.open('bottom.jsp?moduleName='+text,'bottom'); 
	 }
 } 

 function document_onclick() {
     if(!bMenuClick) {
         if (mnuPrev.length>0) {
             for (var i=1;i<mnuPrev.length;i++) {
                 if (mnuPrev[i].type==0) {   //如果是菜单项，设置成默认样式
                     /*document.all.item(mnuPrev[i].id + "_ico").className = "ico";*/
                     document.all.item(mnuPrev[i].id + "_text").className = "text";
                     /*document.all.item(mnuPrev[i].id + "_skey").className = "skey";*/
                     document.all.item(mnuPrev[i].id + "_sub").className = "sub";
                 }
				 else {   //如果是菜单框，隐藏它
                     document.all.item(mnuPrev[i].id).style.visibility = "hidden";
                 }
             }
             document.all.item(mnuPrev[0].id).className = "MainMenu";    //将主菜单的状态恢复普通状态
         }
         mnuPrev.length = 0;    //清空当前显示菜单
         mClick = false;    //清除菜单点击状态
     }
	 else {
         bMenuClick = false;
     }
 }

 function window_onresize() {
      //当窗口尺寸变化时改变相应变量的值
      doc_height = window.document.body.offsetHeight;
      doc_width = window.document.body.offsetWidth;
 }

 function window_onload() {
      //初始化窗口尺寸
      doc_height = window.document.body.offsetHeight;
      doc_width = window.document.body.offsetWidth;
 }

 function document.onclick() {
	 //隐藏菜单
     document_onclick();
 }


//-->
