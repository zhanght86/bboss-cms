// JavaScript Document
function getCheckBoxValue(checkboxName){
  try{
   //根据元素名获取对象集合,checkBoxArray为一个数组
   var checkBoxArray =document.getElementsByName(checkboxName);
   //循环判断获取被选中的checkbox的值
   var arrLength = checkBoxArray.length;
   var obj = null;
   var checkboxValue = "";
   for(var i = 0 ; i < arrLength;i++){
    obj =  checkBoxArray[i];
    //首先需要判断该对象是否为checkbox,因为名字尾checkboxtest的元素也有可能是其他类型的页面对象
    if(obj.type != null && obj.type != "" && obj.type.toLowerCase() == "checkbox"){ //转换为小写然后比较，这样就不区分大小写了
     if(obj.checked){
	  checkBoxArray[i].checked=false;
	  obj.checked=true;
      checkboxValue +="<span class='md_6 fw'>"+obj.value+"</span>";
     }
    }
   }
   document.getElementById("checkname").innerHTML = checkboxValue;
  }catch(e){
   //js也是有异常的并且是可以捕获的，既然你说你写js经常会出错，何不catch一下错误并弹出相关信息呢。
   alert("JS error,type:" + e.type + ",message:" + e.message);
  }
 }
 
function singleSelect(obj){
	var objname=obj.name;
	var objs = document.getElementsByName(objname);
	var checkboxValue="";
	if(obj.checked==true){
		for(var i=0; i<objs.length; i++){
			objs[i].checked=false;
			checkboxValue+="<span class='md_6 fw'>"+obj.value+"</span>";
		}
		obj.checked=true;
		document.getElementById("checkname").innerHTML = checkboxValue;
	}
}