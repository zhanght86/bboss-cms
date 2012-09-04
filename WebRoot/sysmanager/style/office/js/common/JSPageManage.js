
/*控制函数:
     名字:JSPageManage.js
     作用: 1. 用于"翻页工具栏"各按钮的控制.
          2. 用于控制"记录列表"显示效果
*/
	function gotoHead(){
          /*首页*/
          var vCurrentPage = document.listForm.currentPage.value;
          if((parseInt(vCurrentPage)) <= 1){}else{
              document.listForm.NextPage.value="1";
              //document.listForm.action="equipmentToBase_list.jsp?flag=1";
         	  document.listForm.submit();
         	 }
          }
        function gotoPre(){
          /*前一页*/
          var vCurrentPage = document.listForm.currentPage.value;
          var vTotalPage = document.listForm.totalPage.value;
          if((parseInt(vCurrentPage))<=1){}else{
            document.listForm.NextPage.value=parseInt(vCurrentPage)-1;
            //document.listForm.action="equipmentToBase_list.jsp?flag=1";
            document.listForm.submit();
            }
          }

        function gotoNext(){
          /*下一页*/
          var vCurrentPage = document.listForm.currentPage.value;
          var vTotalPage = document.listForm.totalPage.value;
          if((parseInt(vCurrentPage))>=(parseInt(vTotalPage))){}else{
            document.listForm.NextPage.value=parseInt(vCurrentPage)+1;
            //document.listForm.action="equipmentToBase_list.jsp?flag=1";
            document.listForm.submit();
            }
          }

        function gotoTail(){
          /*尾页*/
          var vCurrentPage = document.listForm.currentPage.value;
          var vTotalPage = document.listForm.totalPage.value;
          if((parseInt(vCurrentPage))>=(parseInt(vTotalPage))){}else{
            document.listForm.NextPage.value=vTotalPage;
            //document.listForm.action="equipmentToBase_list.jsp?flag=1";
            document.listForm.submit();
            }
          }

/*下面两个函数用于:鼠标置于表格的一行,触发动态视觉效果*/
function sbar (st,col) {
  if(st.style.backgroundColor != '#ff0000'){
 	 st.style.backgroundColor = '#eeeeee';
  }
}
function cbar (st) {
  if(st.style.backgroundColor != '#ff0000'){
 	 st.style.backgroundColor = '';
  }
}

/*当radio按钮被选中后:视觉效果改变*/
function check(o,c) {
if(o.parentElement.parentElement.style.backgroundColor==c){
    o.parentElement.parentElement.style.backgroundColor='';
}
else o.parentElement.parentElement.style.backgroundColor=c;
}
