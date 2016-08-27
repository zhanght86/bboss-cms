/**
   *选择所有的复选框
   *参数1：主复选框名称
   *参数2：明细复选框名称
   */
function checkAll(totalCheckName,oneCheckName){
    if ($("[name='"+totalCheckName+"']").attr("checked")) {
         $("[name="+oneCheckName+"]").each(function () {
			  if (!$(this).attr("checked")) {
				  $(this).attr("checked", "true");
			  }
		 });
     } else {
         $("[name="+oneCheckName+"]").each(function () {
              $(this).removeAttr("checked");
		 });
     }
}
 /**
   *取消某个被选择的复选框
   *参数1：主复选框名称
   *参数2：明细复选框名称
   */
function checkOne(totalCheckName,oneCheckName){
     var totalChecked = true;
     $("[name="+oneCheckName+"]").each(function () {
		 if (!$(this).attr("checked")) {
			      totalChecked = false;
		 } 
	 });
     if (totalChecked) {
		 $("[name='"+totalCheckName+"']").attr("checked", "true");
     } else {
         $("[name='"+totalCheckName+"']").removeAttr("checked");
     }
}
 /**
   *用于取得所选择的复选框的值：格式：'10001','10002'
   *参数1：使用了iframe时，iframe的名称,当没使用iframe 可以使用 ："" 或 null 代替
   *参数2：复选框名称
   */
 function getCheckBoxValue(frames_name,checkBoxName){
      var checkBoxsObj;
      if(frames_name != null && frames_name != ""){
          checkBoxsObj = $(window.frames[frames_name].document).find("[@name='"+checkBoxName+"'][checked]");
      }else{
          checkBoxsObj = $("[@name='"+checkBoxName+"'][checked]");
      }
	  var checkBoxValues = "";
	  $(checkBoxsObj).each(function () {
		  if ($(this).val()!="") {
			  checkBoxValues += $(this).val() + ",";
		  }
	  });
	  return checkBoxValues;
}
		//1.在父窗口中操作 选中IFRAME中的所有单选钮
		//$(window.frames["iframe1"].document).find("input[@type='radio']").attr("checked","true");
		//2. 在IFRAME中操作 选中父窗口中的所有单选钮
		//$(window.parent.document).find("input[@type='radio']").attr("checked","true");
/**
   *选择所有的复选框
   *参数1：主复选框名称
   *参数2：明细复选框名称
   */
function checkAll2(totalCheck,checkName){
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
   if(selectAll[0].checked==true){
	   for (var i=0; i<o.length; i++){
      	  if(!o[i].disabled){
      	  	o[i].checked=true;
      	  }
	   }
   }else{
	   for (var i=0; i<o.length; i++){
   	  	  o[i].checked=false;
   	   }
   }
}

 /**
   *取消某个被选择的复选框
   *参数1：主复选框名称
   *参数2：明细复选框名称
   */
function checkOne2(totalCheck,checkName){
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
	var cbs = true;
	for (var i=0;i<o.length;i++){
		if(!o[i].disabled){
			if (o[i].checked==false){
				cbs=false;
			}
		}
	}
	if(cbs){
		selectAll[0].checked=true;
	}else{
		selectAll[0].checked=false;
	}
}
 /**
   *用于取得所选择的复选框的值：格式：'10001','10002'
   *参数1：使用了iframe时，iframe的名称,当没使用iframe 可以使用 ："" 或 null 代替
   *参数2：复选框名称
   */
 function delCheckBox2(frames_name,checkBoxName){
      var checkBoxs;
      if(frames_name != null && frames_name != ""){
          checkBoxs=document.frames[frames_name].document.getElementsByName(checkBoxName);
      }else{
          checkBoxs=document.getElementsByName(checkBoxName);
      }
	  var optionId = "";
	  for(var i=0;i<checkBoxs.length;i++){
	     if(checkBoxs[i].checked){
	       if(optionId == ""){
	       	  optionId = checkBoxs[i].value;
	       }else{
	          optionId = optionId+ ","+checkBoxs[i].value;
	       }
	     }
	  }
	  return optionId;
}
