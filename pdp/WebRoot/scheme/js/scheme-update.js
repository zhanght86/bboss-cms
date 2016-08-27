$(window.parent.document).find("#userListFrame").load(
		function() {
			var main = $(window.parent.document).find("#userListFrame");
			var thisheight = document.documentElement.scrollHeight
					|| document.body.scrollHeight + 30;
			main.height(thisheight);
		});
function userAddBack() {
	window.history.go(-1);
}
function updateScheme() {
	
	var update_scheme_form = $("#update_scheme_form");
	if ($("#tdPwSchemeTemp_schemeId").val() == "") {
		$("#tdPwSchemeTemp_schemeId").focus();
		alert("方案编号不能为空，请重新输入！");
		return;
	}
	if ($("#tdPwSchemeTemp_schemeName").val() == "") {
		$("#tdPwSchemeTemp_schemeName").focus();
		alert("方案名称不能为空，请重新输入！");
		return;
	}
	if ($("#tdPwSchemeTemp_startTime").val() == "-1") {
		$("#tdPwSchemeTemp_startTime").focus();
		alert("开始时间不能为空，请重新输入！");
		return;
	}
	if ($("#tdPwSchemeTemp_endTime").val() == "-1") {
		$("#tdPwSchemeTemp_endTime").focus();
		alert("结束时间不能为空，请重新输入！");
		return;
	}
	update_scheme_form.attr("target", "queryList");
	update_scheme_form.attr("action", basePath + "/scheme_updateScheme.action?cache="+ new Date().getTime());
	update_scheme_form.submit();
	parent.closeWindow();
	
}
function schemeReset() {
	$("#tdPwSchemeTemp_schemeId").attr("value","");
	$("#tdPwSchemeTemp_schemeName").attr("value","");
	$("#tdPwSchemeTemp_startTime").attr("value","");
	$("#tdPwSchemeTemp_endTime").attr("value","");
	$("#tdPwSchemeTemp_schemeExplain").attr("value","");
}
//验证E-mail地址
function checkEmail(str) {
	//在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
	var Expression = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/; ///^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
	var objExp = new RegExp(Expression);
	/*var pattern =/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
	if(str!="") {
	    if(!pattern.exec(str)) {
	        alert('请输入正确的邮箱地址');
	        object.value="";
	        object.focus();
	    }
	}*/
	if (objExp.test(str)) {
		return true;
	} else {
		return false;
	}
}

function isMobile(value){  
	  if(/^13\d{9}$/g.test(value)||(/^15[0-35-9]\d{8}$/g.test(value))
	     ||(/^18[05-9]\d{8}$/g.test(value))) {    
	      return true;  
	  } else{  
	      return false;  
	  }  
  }  
  //校验(国内)邮政编码
function isPostalCode(value) {
      var pattern =/^[0-9]{6}$/;
      if(value!="") {
          if(!pattern.exec(value)) {
              return false;
           } else {
              return true;
           }
      } 
  }
  //校验密码
function isPassword(value) {
      var pattern =/^[a-z0-9A-Z_]{6,15}$/;
      if(value!="") {
          if(!pattern.exec(value)) {
              return false;
           } else {
              return true;
           }
      } 
  }  
  //校验身份证
function isIdCard(value){
	  var pattern =/(^\d{15}$)|(^\d{17}([0-9]|X)$)/;
	  if(value!=""){
          if(!pattern.exec(value)){
              return false;
          } else {
              return true;
          }
      } 
  }  