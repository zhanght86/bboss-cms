/*控制浅黄的颜色--------------------------------------------------------------------*/
/*下面两个函数用于:鼠标置于表格的一行,触发动态视觉效果*/
function sbar4 (st) {
  if(st.style.backgroundColor != '#ffe1d2'){
 	 st.style.backgroundColor = '#eeeeee';
  }
}
function cbar4 (st) {
  if(st.style.backgroundColor != '#ffe1d2'){
 	 st.style.backgroundColor = '';
  }
}
/*当radio按钮被选中后:视觉效果改变*/
function check4(o) {
if(o.parentElement.parentElement.style.backgroundColor=='#ffe1d2'){
    o.parentElement.parentElement.style.backgroundColor='';
}
else o.parentElement.parentElement.style.backgroundColor='#ffe1d2';
}

function setYellowBkColor(o) {
	//设置黄色的背景颜色
	o.parentElement.parentElement.style.backgroundColor = '#ffe1d2';
}

function clearBkColor(o) {
	//清楚背景颜色
	o.parentElement.parentElement.style.backgroundColor = '';
}
/*控制浅黄的颜色（结束）--------------------------------------------------------------------*/

/*控制浅黄的颜色【点击 tr 后颜色改变】--------------------------------------------------------------------*/
function clickChange(o) {
	//点击 tr 后颜色改变
	var table = o.parentElement;
	//if (table.rows)
	for (i=0;i<table.rows.length;i++) {
		//如果是红色，则不改变颜色，因为红色代表需要删除的数据行
		if (table.rows(i).style.backgroundColor=='#ff0000') continue;
		if (o.rowIndex==i) {
			//改变指定行的颜色
			table.rows(i).style.backgroundColor='#ffe1d2';
		}else{
			//其余的颜色去掉
			table.rows(i).style.backgroundColor='';
		}
	}
}

function clickChangeTD(o) {
	//点击 td 后颜色改变
	clickChange(o.parentElement);
}
/*控制浅黄的颜色【点击后颜色改变】（结束）--------------------------------------------------------------------*/

/*控制深红的颜色--------------------------------------------------------------------*/

/*下面两个函数用于:鼠标置于表格的一行,触发动态视觉效果*/
function sbar (st,col) {
  if((st.style.backgroundColor != '#ff0000') && (st.style.backgroundColor != '#ffe1d2')){
 	 st.style.backgroundColor = '#eeeeee';
  }
}
function cbar (st) {
  if((st.style.backgroundColor != '#ff0000') && (st.style.backgroundColor != '#ffe1d2')){
 	 st.style.backgroundColor = '';
  }
}
/*当radio按钮被选中后:视觉效果改变*/
function check2(o) {
	//alert("radio");
if(o.parentElement.parentElement.style.backgroundColor=='#ff0000'){
    o.parentElement.parentElement.style.backgroundColor='';
}
else o.parentElement.parentElement.style.backgroundColor='#ff0000';
}
/*控制深红的颜色（结束）--------------------------------------------------------------------*/


/*控制任意【由输入而定】的颜色--------------------------------------------------------------------*/
/*下面两个函数用于:鼠标置于表格的一行,触发动态视觉效果*/
function sbar2 (st,col) {
  if(st.style.backgroundColor != col){
 	 st.style.backgroundColor = '#eeeeee';
  }
}
function cbar2 (st,col) {
  if(st.style.backgroundColor != col){
 	 st.style.backgroundColor = '';
  }
}

/*下面两个函数用于:鼠标置于表格的一行,触发动态视觉效果*/
/*
function sbar (st,col)
{ st.style.backgroundColor = '#eeeeee'; }

function cbar (st)
{ st.style.backgroundColor = ''; }
*/

/*当radio按钮被选中后:视觉效果改变*/
function check(o,c) {
if(o.parentElement.parentElement.style.backgroundColor==c){
    o.parentElement.parentElement.style.backgroundColor='';
}
else o.parentElement.parentElement.style.backgroundColor=c;
}

function check3(o,c){
		o.parentElement.parentElement.style.backgroundColor=c;
}
/*控制任意【由输入而定】的颜色（结束）--------------------------------------------------------------------*/
