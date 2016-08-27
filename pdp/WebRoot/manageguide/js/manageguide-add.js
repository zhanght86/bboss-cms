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
function manageGuideAdd() {
	
	var add_manageguide_form = $("#add_manageguide_form");
	if ($("#tdPwControlIndicators_controlName").val() == "") {
		$("#tdPwControlIndicators_controlName").focus();
		alert("指标名称不能为空，请重新输入！");
		return;
	}
	if ($("#tdPwControlIndicators_controlCode").val() == "") {
		$("#tdPwControlIndicators_controlCode").focus();
		alert("指标编号不能为空，请重新输入！");
		return;
	}
	if ($("#getControType").val() == "-1") {
		$("#getControType").focus();
		alert("指标分类不能为空，请重新输入！");
		return;
	}
	if ($("#getControResType").val() == "-1") {
		$("#getControResType").focus();
		alert("输出类型不能为空，请重新输入！");
		return;
	}
	if ($("#getRuleContent").val() == "-1") {
		$("#getRuleContent").focus();
		alert("规则体不能为空，请重新输入！");
		return;
	}
	add_manageguide_form.attr("target", "queryList");
	add_manageguide_form.attr("action", basePath + "/manage_insertManageGuide.action?cache="+ new Date().getTime());
	add_manageguide_form.submit();
	window.close();
	
}
function manageGuideReset() {
	$("#tdPwControlIndicators_controlName").attr("value","");
	$("#tdPwControlIndicators_controlCode").attr("value","");
	$("#getControType").attr("value","-1");
	$("#getControResType").attr("value","-1");
	$("#tdPwControlIndicators_isUsed").attr("value","");
	$("#getRuleContent").attr("value","-1");
	$("#tdPwControlIndicators_remark").attr("value","");
	
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